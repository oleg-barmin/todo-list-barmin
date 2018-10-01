package org.javaclasses.todo.model;

/**
 * Occurs when try to update completed task.
 *
 * @author Oleg Barmin
 */
public class UpdateCompletedTaskException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code UpdateCompletedTaskException} instance.
     *
     * @param taskId ID of the completed task
     */
    UpdateCompletedTaskException(TaskId taskId) {
        super("Task with ID: '" + taskId + "' is completed. Completed tasks cannot be updated.");
    }
}
