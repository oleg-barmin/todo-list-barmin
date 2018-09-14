package org.javaclasses.todo.auth.impl;

import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.auth.SessionDoesNotExistsException;
import org.javaclasses.todo.auth.SessionExpiredException;
import org.javaclasses.todo.auth.UsernameAlreadyExists;
import org.javaclasses.todo.model.*;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.UserStorage;

import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public class AuthenticationImpl {
    private UserStorage userStorage = new UserStorage();
    private AuthSessionStorage authSessionStorage = new AuthSessionStorage();

    Token signIn(Username username, Password password) throws InvalidCredentialsException {
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

    UserId createUser(Username username, Password password) throws UsernameAlreadyExists {
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

    void signOut(Token token) throws SessionExpiredException {
        Optional<AuthSession> remove = authSessionStorage.remove(token);

        if(remove.isPresent()){
            return;
        }

        throw new SessionExpiredException();
    }

    UserId validate(Token token) throws SessionDoesNotExistsException {
        Optional<AuthSession> authSessionOptional = authSessionStorage.findById(token);

        if(authSessionOptional.isPresent()){
            return authSessionOptional.get().getUserId();
        }

        throw new SessionDoesNotExistsException();
    }
}
