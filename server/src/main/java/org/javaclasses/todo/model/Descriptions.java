package org.javaclasses.todo.model;

/**
 * Utility class which performs operation with tasks descriptions.
 */
class Descriptions {

    /**
     * Validates given task description.
     * <p>
     * Task description is invalid if it is null or empty.
     *
     * @param description task description to validate
     * @throws EmptyTaskDescriptionException if given description is null or empty.
     */
    static void validate(String description) throws EmptyTaskDescriptionException {
        if (description == null) {
            throw new EmptyTaskDescriptionException(description);
        }
        description = description.trim();
        if (description.isEmpty()) {
            throw new EmptyTaskDescriptionException(description);
        }
    }
}
