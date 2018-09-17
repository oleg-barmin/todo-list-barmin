package org.javaclasses.todo.model;

/**
 * Occurs when occurred task description is null or empty.
 */
class EmptyTaskDescriptionException extends RuntimeException {

    /**
     * Creates `EmptyTaskDescriptionException` instance.
     *
     * @param description task description which is null or empty.
     */
    EmptyTaskDescriptionException(String description) {
        super("Task description cannot be null or empty. Actual Value: '" + description + "'.");
    }
}
