package org.javaclasses.todo.model.impl;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword());
    }
}
