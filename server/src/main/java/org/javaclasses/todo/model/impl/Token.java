package org.javaclasses.todo.model.impl;

import java.util.Objects;

/**
 * Identifies user session.
 */
public class Token {
    private final String value;

    /**
     * Creates `Token` instance.
     *
     * @param value string with uuid to wrap.
     */
    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return Objects.equals(getValue(), token.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
