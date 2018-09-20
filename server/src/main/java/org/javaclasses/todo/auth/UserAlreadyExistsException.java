package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.Username;

/**
 * Occurs when attempt to create user with username, which already exists in the system.
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public.
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code UserAlreadyExistsException} instance.
     *
     * @param username username of user which was attempted to create
     */
    UserAlreadyExistsException(Username username) {
        super("User with username '" + username.getValue() + "' was exists.");
    }
}
