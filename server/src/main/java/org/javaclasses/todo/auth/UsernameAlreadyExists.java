package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.Username;

/**
 * Occurs when attempt to create user with username, which already exists in storage.
 */
public class UsernameAlreadyExists extends RuntimeException {

    /**
     * Creates `UsernameAlreadyExists` instance.
     *
     * @param username username of user which was attempted to create
     */
    UsernameAlreadyExists(Username username) {
        super("User with username '" + username.getUsername() + "' was already exists.");
    }
}
