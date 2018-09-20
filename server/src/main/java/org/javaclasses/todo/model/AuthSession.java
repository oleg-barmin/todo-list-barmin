package org.javaclasses.todo.model;

/**
 * An entity which represents a user session.
 *
 * <p>When user sign in into the system he receives a {@code Token},
 * this token will be stored with ID of the user who signed in {@code AuthSession} instance.
 * Newly created {@code AuthSession} will be stored in the system to validate user actions.
 */
public final class AuthSession extends Entity<Token> {
    private UserId userId;

    public AuthSession(Token token) {
        super(token);
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
