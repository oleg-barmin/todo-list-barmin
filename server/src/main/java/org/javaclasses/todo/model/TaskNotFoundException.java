package org.javaclasses.todo.model;

/**
 * Occurs when attempted when `Task` with desired `TaskId` was not found in the storage.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Creates `TaskNotFoundException` instance.
     *
     * @param taskId ID of the task which was not found
     */
    public TaskNotFoundException(TaskId taskId) {
        super("Task wih ID: '" + taskId + "'wasn't Found");
    }
}
