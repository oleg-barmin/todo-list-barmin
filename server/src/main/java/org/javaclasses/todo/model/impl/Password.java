package org.javaclasses.todo.model.impl;

import java.util.Objects;

/**
 * Wraps string with user password to sign in into TodoList application.
 */
public final class Password {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password1 = (Password) o;
        return Objects.equals(getPassword(), password1.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword());
    }
}
