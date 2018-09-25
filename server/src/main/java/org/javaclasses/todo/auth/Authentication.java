package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.*;
import org.javaclasses.todo.storage.Storage;
import org.javaclasses.todo.storage.impl.UserStorage;

import java.util.Optional;
import java.util.UUID;

/**
 * Authenticates users and provides {@link Token} for each user session, which will expire with time.
 *
 * <p>Each user must sign in in application to get {@code Token} which should be presented on each operation.
 *
 * <p>User can sign out from application, so {@code Token} of the session will expire.
 *
 * <p>Unsigned user can create an account in application.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // All methods of public API should be public
public class Authentication {

    private final UserStorage userStorage;
    private final Storage<Token, AuthSession> authSessionStorage;

    public Authentication(UserStorage userStorage, Storage<Token, AuthSession> authSessionStorage) {
        this.userStorage = userStorage;
        this.authSessionStorage = authSessionStorage;
    }

    /**
     * Sign in user into the system by given {@link Username} and {@link Password} and
     * provides {@code Token} of the session.
     *
     * @param username username of the user to sign in
     * @param password password of the user to sign in
     * @return {@code Token} of the session
     * @throws InvalidCredentialsException if user with given username doesn't exist or given password is invalid.
     */
    public Token signIn(Username username, Password password) throws InvalidCredentialsException {
        Optional<User> userByUsername = userStorage.findUserByUsername(username);

        if (userByUsername.isPresent()) {
            User user = userByUsername.get();

            if (user.getPassword().equals(password)) {
                Token token = new Token(UUID.randomUUID().toString());

                AuthSession authSession = new AuthSession(token);
                authSession.setUserId(user.getId());

                authSessionStorage.write(authSession);

                return token;
            }
        }

        throw new InvalidCredentialsException();
    }

    /**
     * Creates user in the system if user with given username hasn't exists yet.
     *
     * @param username username of new user
     * @param password password of new user
     * @return {@link UserId} ID of newly created user
     * @throws UserAlreadyExistsException if user with given username already exists.
     */
    public UserId createUser(Username username, Password password) throws UserAlreadyExistsException {
        Optional<User> userByUsername = userStorage.findUserByUsername(username);

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException(username);
        }

        UserId userId = new UserId(UUID.randomUUID().toString());
        User user = new User(userId);

        user.setUsername(username);
        user.setPassword(password);

        userStorage.write(user);

        return userId;
    }

    /**
     * Closes users session in the system.
     *
     * @param token token of user session to close
     */
    public void signOut(Token token) {
        authSessionStorage.remove(token);
    }

    /**
     * Validates if session with given token exists.
     *
     * @param token token of the session to validate
     * @return {@code UserId} of user who created session
     * @throws AuthorizationFailedException if session with given token doesn't exist
     */
    public UserId validate(Token token) throws AuthorizationFailedException {
        Optional<AuthSession> authSessionOptional = authSessionStorage.read(token);

        if (authSessionOptional.isPresent()) {
            return authSessionOptional.get().getUserId();
        }

        throw new AuthorizationFailedException();
    }
}
