package org.javaclasses.todo.model;

import javax.annotation.Nullable;

/**
 * Occurs when a task description is empty.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of Public API should be public
public class EmptyTaskDescriptionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code EmptyTaskDescriptionException} instance.
     *
     * @param description task description which is null or empty.
     */
    public EmptyTaskDescriptionException(@Nullable String description) {
        super(String.format("Task description cannot be null or empty. Actual Value: '%s'.",
                            description));
    }
}
