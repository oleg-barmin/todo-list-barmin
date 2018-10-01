package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.UserId;
import org.javaclasses.todo.model.Username;

/**
 * Represents a user which was registered in the system.
 *
 * @author Oleg Barmin
 */
public class Actor {

    private final UserId userId;
    private final Username username;
    private final Password password;

    /**
     * Creates an {@code Actor} instance.
     *
     * @param userId   ID of registered user
     * @param username username of registered user
     * @param password password of registered user
     */
    Actor(UserId userId, Username username, Password password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }
}
