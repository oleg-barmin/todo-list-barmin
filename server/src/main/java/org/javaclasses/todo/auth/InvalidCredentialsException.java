package org.javaclasses.todo.auth;

/**
 * Occurs when user tries to sign in with invalid username or password.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part public API should be public
public class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code InvalidCredentialsException} instance.
     */
    InvalidCredentialsException() {
        super("Invalid username/password was given.");
    }
}
