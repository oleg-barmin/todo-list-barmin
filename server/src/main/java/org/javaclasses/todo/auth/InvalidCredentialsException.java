package org.javaclasses.todo.auth;

/**
 * Occurs when user tries to sign in with invalid username or password.
 */
public class InvalidCredentialsException extends Exception {

    /**
     * Creates `InvalidCredentialsException` instance.
     */
    public InvalidCredentialsException() {
        super("Invalid username/password was given.");
    }
}
