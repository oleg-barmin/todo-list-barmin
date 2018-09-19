package org.javaclasses.todo.model;

/**
 * ID of {@link User} which ensures uniqueness of {@code User}.
 */
public final class UserId extends EntityId<String>{

    /**
     * Creates {@code UserId} instance.
     *
     * @param value string which contains uuid to ensure uniqueness of {@code User}s
     */
    public UserId(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "UserId{} " + super.toString();
    }
}
