package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * ID of {@link User} which unifies `User`.
 * <p>
 * Wraps a string which contains uuid ID of `User`.
 */
public final class UserId {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return Objects.equals(getId(), userId.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
