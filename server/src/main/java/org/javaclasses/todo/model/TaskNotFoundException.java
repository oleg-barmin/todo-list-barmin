package org.javaclasses.todo.model;

/**
 * Occurs when try when {@code Task} with desired {@code TaskId} was not found in the system.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of Public API should be public
public class TaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code TaskNotFoundException} instance.
     *
     * @param taskId ID of the task which was not found
     */
    TaskNotFoundException(TaskId taskId) {
        super(String.format("Task wih ID: '%s' wasn't Found", taskId));
    }
}
