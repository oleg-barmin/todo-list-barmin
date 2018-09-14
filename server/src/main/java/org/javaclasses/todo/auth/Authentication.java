package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.impl.Password;
import org.javaclasses.todo.model.impl.Token;
import org.javaclasses.todo.model.impl.UserId;
import org.javaclasses.todo.model.impl.Username;

public interface Authentication {
    Token signIn(Username username, Password password) throws InvalidCredentialsException;

    UserId createUser(Username username, Password password) throws UsernameAlreadyExists;

    void signOut(Token token) throws SessionExpiredException;

    UserId validate(Token token) throws SessionDoesNotExistsException;
}
