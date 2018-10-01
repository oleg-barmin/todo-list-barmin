package org.javaclasses.todo.model;

/**
 * Unique identifier of {@code User}.
 *
 * @author Oleg Barmin
 * @implNote value of identifier should be a string with UUID.
 */
public final class UserId extends EntityId<String> {

    /**
     * Creates {@code UserId} instance.
     *
     * @param value value of ID
     */
    public UserId(String value) {
        super(value);
    }
}
