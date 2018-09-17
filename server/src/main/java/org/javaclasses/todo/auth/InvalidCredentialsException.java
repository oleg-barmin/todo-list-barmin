package org.javaclasses.todo.auth;

/**
 * Occurs when user tries to sign in with invalid username or password.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Creates `InvalidCredentialsException` instance.
     */
    InvalidCredentialsException() {
        super("Invalid username/password was given.");
    }
}
