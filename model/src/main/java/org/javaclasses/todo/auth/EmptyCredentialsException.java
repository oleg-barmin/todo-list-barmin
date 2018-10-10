package org.javaclasses.todo.auth;

/**
 * Occurs when try to authenticate user with empty username or password.
 *
 * @author Oleg Barmin
 */
class EmptyCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code EmptyCredentialsException} instance.
     */
    EmptyCredentialsException() {
        super("Username or password is empty.");
    }
}
