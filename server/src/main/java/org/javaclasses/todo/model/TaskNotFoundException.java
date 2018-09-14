package org.javaclasses.todo.model;

/**
 * Occurs when attempted when `Task` with desired `TaskId` was not found in the storage.
 */
public class TaskNotFoundException extends Exception {

    /**
     * Creates `TaskNotFoundException` instance.
     */
    public TaskNotFoundException() {
        super("Task wasn't Found");
    }
}
