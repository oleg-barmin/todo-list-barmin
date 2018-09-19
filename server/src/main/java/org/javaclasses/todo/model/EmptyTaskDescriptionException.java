package org.javaclasses.todo.model;

import javax.annotation.Nullable;

/**
 * Occurs when occurred task description is null or empty.
 */
@SuppressWarnings("WeakerAccess") // part of Public API should be public
public class EmptyTaskDescriptionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code EmptyTaskDescriptionException} instance.
     *
     * @param description task description which is null or empty.
     */
    EmptyTaskDescriptionException(@Nullable String description) {
        super("Task description cannot be null or empty. Actual Value: '" + description + "'.");
    }
}
