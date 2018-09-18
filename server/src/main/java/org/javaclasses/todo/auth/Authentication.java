package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.*;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.UserStorage;

import java.util.Optional;
import java.util.UUID;

/**
 * Authenticate users and provides {@link Token} for each session, which will expire in with time.
 * <p>
 * Each user must sign in in application to get `Token` which should be presented on each operation.
 * <p>
 * User can sign out from application, so `Token` of the session will expire.
 * <p>
 * Unsigned user can create an account in application.
 */
public class Authentication {
    private UserStorage userStorage = new UserStorage();
    private AuthSessionStorage authSessionStorage = new AuthSessionStorage();

    /**
     * Sign in user into application by given {@link Username} and {@link Password} and
     * provides `Token` of the session.
     *
     * @param username username of the user to sign in
     * @param password password of the user to sign in
     * @return `Token` of the session
     * @throws InvalidCredentialsException if user with given username doesn't exist or given password is invalid.
     */
    public Token signIn(Username username, Password password) throws InvalidCredentialsException {
        Optional<User> userByUsername = userStorage.findUserByUsername(username);

        if (userByUsername.isPresent()) {
            User user = userByUsername.get();

            if (user.getPassword().equals(password)) {
                Token token = new Token(UUID.randomUUID().toString());

                AuthSession authSession = new AuthSession();
                authSession.setUserId(user.getId());
                authSession.setId(token);

                authSessionStorage.write(authSession);

                return token;
            }
        }

        throw new InvalidCredentialsException();
    }

    /**
     * Creates user in the application, for him to be able to login.
     *
     * @param username username of new user
     * @param password password of new user
     * @return {@link UserId} ID of newly created user
     * @throws UsernameAlreadyExists if user with given username already exists.
     */
    public UserId createUser(Username username, Password password) throws UsernameAlreadyExists{
        Optional<User> userByUsername = userStorage.findUserByUsername(username);

        if(userByUsername.isPresent()){
            throw new UsernameAlreadyExists(username);
        }

        User user = new User();
        UserId userId = new UserId(UUID.randomUUID().toString());

        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);

        userStorage.write(user);

        return userId;
    }

    /**
     * Closes users session in application and makes given `Token` expired.
     *
     * @param token token of user session to close
     */
    public void signOut(Token token){
        Optional<AuthSession> remove = authSessionStorage.remove(token);

        if(remove.isPresent()){
            return;
        }
    }

    /**
     * Validates if session with given token exists.
     *
     * @param token token of the session to validate
     * @return `UserId` of user who created session
     * @throws SessionDoesNotExistsException if session with given token doesn't exist
     */
    public UserId validate(Token token) throws SessionDoesNotExistsException{
        Optional<AuthSession> authSessionOptional = authSessionStorage.read(token);

        if(authSessionOptional.isPresent()){
            return authSessionOptional.get().getUserId();
        }

        throw new SessionDoesNotExistsException();
    }
}
