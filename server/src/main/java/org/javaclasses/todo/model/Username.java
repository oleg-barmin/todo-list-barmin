package org.javaclasses.todo.model;

/**
 * Wraps string with username for user to sing-in into TodoList application.
 */
public class Username {
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
}
