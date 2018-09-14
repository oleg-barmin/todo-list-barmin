package org.javaclasses.todo.model;

/**
 * An entity which represents user who uses TodoList application.
 * <p>
 * Contains:
 * - ID of user.
 * - username of user
 * - password of user
 */
public class User extends Entity<UserId> {
    private UserId id;
    private Username username;
    private Password password;

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }
}
