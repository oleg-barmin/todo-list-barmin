package org.javaclasses.todo.model;

/**
 * An entity which represents user who uses TodoList application.
 */
public class User extends Entity<UserId> {
    private Username username;
    private Password password;

    public User(UserId userId) {
        super(userId);
    }

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
