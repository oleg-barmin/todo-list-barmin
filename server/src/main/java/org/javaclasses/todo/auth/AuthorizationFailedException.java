package org.javaclasses.todo.auth;

/**
 * Occurs when trying to validate `Token` which does not exists in storage.
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public
public class AuthorizationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates `AuthorizationFailedException` instance.
     */
    public AuthorizationFailedException() {
        super("Session for given token does not exists.");
    }
}
