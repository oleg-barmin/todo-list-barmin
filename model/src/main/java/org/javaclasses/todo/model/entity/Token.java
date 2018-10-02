package org.javaclasses.todo.model.entity;

/**
 * Unique identifier of {@code AuthSession}.
 *
 * @author Oleg Barmin
 * @implNote value of identifier should be a string with UUID.
 */
public final class Token extends EntityId<String> {

    /**
     * Creates {@code Token} instance.
     *
     * @param value string with value of ID.
     */
    public Token(String value) {
        super(value);
    }
}
