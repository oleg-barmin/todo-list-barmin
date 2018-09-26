package org.javaclasses.todo.web;

/**
 * Payload of task update request.
 */
class TaskUpdatePayload {

    private final boolean taskStatus;
    private final String taskDescription;

    /**
     * Creates {@code TaskUpdatePayload} instance.
     *
     * @param taskStatus      new status of task to update
     * @param taskDescription new description of task to update
     */
    TaskUpdatePayload(boolean taskStatus, String taskDescription) {
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
    }

    boolean isTaskStatus() {
        return taskStatus;
    }

    String getTaskDescription() {
        return taskDescription;
    }
}
