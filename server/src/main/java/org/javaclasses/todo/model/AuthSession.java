package org.javaclasses.todo.model;

/**
 * An entity which represents a user session.
 *
 * <p>When user signs in into the system he receives a {@code Token}, which marks his session.
 *
 * <p>When user sign out or {@code Token} expires users session ends.
 *
 * <p>Newly created {@code AuthSession} will be stored in the system to validate user actions.
 *
 * @author Oleg Barmin
 */
public final class AuthSession extends Entity<Token> {

    private UserId userId;

    public AuthSession(Token token) {
        super(token);
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }
}
