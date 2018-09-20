package org.javaclasses.todo.model;

/**
 * ID of {@link User} which ensures uniqueness of {@code User}.
 *
 * @author Oleg Barmin
 */
public final class UserId extends EntityId<String> {

    /**
     * Creates {@code UserId} instance.
     *
     * @param value string which contains uuid to ensure uniqueness of {@code User}s
     */
    public UserId(String value) {
        super(value);
    }
}
