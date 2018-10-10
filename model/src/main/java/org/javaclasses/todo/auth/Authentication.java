package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.AuthSession;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.User;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.model.entity.Username;
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
public class Authentication {

    private final UserStorage userStorage;
    private final Storage<Token, AuthSession> authSessionStorage;

    public Authentication(UserStorage userStorage, Storage<Token, AuthSession> authSessionStorage) {
        this.userStorage = userStorage;
        this.authSessionStorage = authSessionStorage;
    }

    /**
     * Validate is username or password values is empty.
     *
     * @param username username to validate
     * @param password password to validate
     */
    private static void validateCredentials(Username username, Password password) {
        if (username.getValue()
                    .trim()
                    .isEmpty() || password.getValue()
                                          .trim()
                                          .isEmpty()) {
            throw new EmptyCredentialsException();
        }

    }

    /**
     * Sign in user into the system by given {@link Username} and {@link Password} and
     * provides {@code Token} of the session.
     *
     * @param username username of the user to sign in
     * @param password password of the user to sign in
     * @return {@code Token} of the session
     * @throws EmptyCredentialsException if username or password is empty
     * @throws InvalidCredentialsException if user with given username doesn't exist or given password is invalid.
     */
    public Token signIn(Username username, Password password) throws InvalidCredentialsException {
        validateCredentials(username, password);

        Optional<User> userByUsername = userStorage.findBy(username);

        if (userByUsername.isPresent()) {
            User user = userByUsername.get();

            if (user.getPassword()
                    .equals(password)) {
                Token token = new Token(UUID.randomUUID()
                                            .toString());

                AuthSession authSession = new AuthSession(token);
                authSession.setUserId(user.getId());

                authSessionStorage.write(authSession);

                return token;
            }
        }

        throw new InvalidCredentialsException();
    }

    /**
     * Closes users session in the system.
     *
     * @param token token of user session to close
     */
    //return values is not needed to sign out user
    @SuppressWarnings("ResultOfMethodCallIgnored")
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
            return authSessionOptional.get()
                                      .getUserId();
        }

        throw new AuthorizationFailedException(token);
    }

    /**
     * Creates user in the system if user with given username hasn't exists yet.
     *
     * @param username username of new user
     * @param password password of new user
     * @throws EmptyCredentialsException  if username or password is empty
     * @throws UserAlreadyExistsException if user with given username already exists
     */
    public void createUser(Username username, Password password) throws UserAlreadyExistsException {
        validateCredentials(username, password);

        Optional<User> userByUsername = userStorage.findBy(username);

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException(username);
        }

        UserId userId = new UserId(UUID.randomUUID()
                                       .toString());
        User user = new User(userId);

        user.setUsername(username);
        user.setPassword(password);

        userStorage.write(user);
    }
}
