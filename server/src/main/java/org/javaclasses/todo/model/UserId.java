package org.javaclasses.todo.model;

/**
 * ID of {@link User} which unifies `User`.
 * <p>
 * Wraps a string which contains uuid ID of `User`.
 */
public class UserId {
    private String id;

    /**
     * Creates `UserId` instance.
     *
     * @param id string which contains uuid to unify `User`s
     */
    public UserId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
