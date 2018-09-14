package org.javaclasses.todo.auth;

/**
 * Occurs when trying to validate `Token` which does not exists in storage.
 */
public class SessionDoesNotExistsException extends Exception {

    /**
     * Creates `SessionDoesNotExistsException` instance.
     */
    public SessionDoesNotExistsException() {
        super("Session for given token does not exists.");
    }
}
