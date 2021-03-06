package org.javaclasses.todo.model;

import org.javaclasses.todo.model.entity.TaskId;

/**
 * Occurs when try to add task with ID which already exists in the system.
 *
 * @author Oleg Barmin
 */
public class TaskAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code TaskAlreadyExistsException} instance.
     *
     * @param taskId ID of the task which is already exists
     */
    TaskAlreadyExistsException(TaskId taskId) {
        super(String.format("Task with ID: '%s' already exists.", taskId));
    }
}
