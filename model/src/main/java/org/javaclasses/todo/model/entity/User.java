package org.javaclasses.todo.model.entity;

import org.javaclasses.todo.model.Password;

/**
 * An entity which represents user of TodoList application.
 *
 * @author Oleg Barmin
 */
public final class User extends Entity<UserId> {

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
