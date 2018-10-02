package org.javaclasses.todo.model;

import org.javaclasses.todo.model.entity.TaskId;

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
    public TaskNotFoundException(TaskId taskId) {
        super(String.format("Task wih ID: '%s' wasn't Found", taskId));
    }
}
