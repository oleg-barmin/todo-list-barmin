package org.javaclasses.todo.model;

/**
 * Wraps string with user password to sign in into TodoList application.
 */
public class Password {
    private String password;

    /**
     * Creates `Password` instance.
     *
     * @param password string password to wrap.
     */
    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
