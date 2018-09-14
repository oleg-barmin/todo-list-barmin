package org.javaclasses.todo.model;

public class AuthSession extends Entity<Token>{
    private UserId userId;

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
