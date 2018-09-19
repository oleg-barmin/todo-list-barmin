package org.javaclasses.todo.auth;

/**
 * Occurs when user tries to sign in with invalid username or password.
 */
@SuppressWarnings("WeakerAccess") // part public API should be public
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Creates `InvalidCredentialsException` instance.
     */
    public InvalidCredentialsException() {
        super("Invalid username/password was given.");
    }
}
