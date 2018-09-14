package org.javaclasses.todo.auth;

/**
 * Occurs when attempt to sign out with expired Token.
 */
public class SessionExpiredException extends Exception {

    /**
     * Creates `SessionExpiredException` instance.
     */
    public SessionExpiredException() {
        super("Session is already expired.");
    }
}
