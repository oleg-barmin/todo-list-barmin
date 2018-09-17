package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * A user session.
 *
 * Contains:
 *  - `Token` of session.
 *  - `UserId` of user who created session
 */
public class AuthSession extends Entity<Token>{
    private UserId userId;

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthSession)) return false;
        AuthSession that = (AuthSession) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
