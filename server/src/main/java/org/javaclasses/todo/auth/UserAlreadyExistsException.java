package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.Username;

/**
 * Occurs when attempt to create user with username, which already exists in storage.
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public.
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates `UserAlreadyExistsException` instance.
     *
     * @param username username of user which was attempted to create
     */
    public UserAlreadyExistsException(Username username) {
        super("User with username '" + username.getUsername() + "' was already exists.");
    }
}
