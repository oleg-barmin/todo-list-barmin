package org.javaclasses.todo.model.entity;

import org.javaclasses.todo.model.EmptyTaskDescriptionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides utility methods to simplify work with task description.
 *
 * @author Oleg Barmin
 */
public class Descriptions {

    private Descriptions() {
    }

    /**
     * Validates given task description.
     *
     * <p>Task description is invalid if it is empty.
     *
     * @param description task description to validate
     * @throws EmptyTaskDescriptionException if given description is empty.
     */
    public static void validate(String description) throws EmptyTaskDescriptionException {
        checkNotNull(description);

        if (description.trim()
                       .isEmpty()) {
            throw new EmptyTaskDescriptionException(description);
        }
    }
}
