package org.javaclasses.todo.auth;

/**
 * Occurs when trying to validate {@code Token} which does not exists in the system.
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public
public class AuthorizationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code AuthorizationFailedException} instance.
     */
    AuthorizationFailedException() {
        super("Session for given token does not exists.");
    }
}
