package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * Wraps string with user password to sign in into TodoList application.
 */
public final class Password {
    private final String password;

    /**
     * Creates `Password` instance.
     *
     * @param password string password to wrap.
     */
    public Password(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
