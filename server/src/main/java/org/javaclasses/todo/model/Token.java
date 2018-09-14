package org.javaclasses.todo.model;

import java.util.Objects;

public class Token {
    private final String value;

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
