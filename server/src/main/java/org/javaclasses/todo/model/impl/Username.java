package org.javaclasses.todo.model.impl;

import java.util.Objects;

/**
 * Wraps string with username for user to sing-in into TodoList application.
 */
public final class Username {
    private String username;

    /**
     * Creates `Username` instance.
     *
     * @param username string with username to wrap
     */
    public Username(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        Username username1 = (Username) o;
        return Objects.equals(getUsername(), username1.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
