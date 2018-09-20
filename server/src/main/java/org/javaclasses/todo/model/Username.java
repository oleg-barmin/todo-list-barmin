package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * Wraps string with value for user to sing-in into TodoList application.
 */
public final class Username {
    private final String value;

    /**
     * Creates `Username` instance.
     *
     * @param value string with value to wrap
     */
    public Username(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
