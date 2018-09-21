package org.javaclasses.todo.model;

/**
 * Unique identifier {@code AuthSession}.
 *
 * @author Oleg Barmin
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
