package org.javaclasses.todo.model;

/**
 * Occurs when attempt to add task with ID which already exists in storage.
 */
public class TaskAlreadyExistsException extends RuntimeException {

    /**
     * Creates `TaskAlreadyExistsException` instance.
     *
     * @param taskId ID of the task which is already exists
     */
    public TaskAlreadyExistsException(TaskId taskId) {
        super("Task with ID: '" + taskId + "'already exists.");
    }
}
