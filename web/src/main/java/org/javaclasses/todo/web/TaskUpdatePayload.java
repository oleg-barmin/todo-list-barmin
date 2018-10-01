package org.javaclasses.todo.web;

/**
 * Payload of task update request.
 *
 * @author Oleg Barmin
 */
class TaskUpdatePayload {

    private boolean taskStatus;
    private String taskDescription;

    TaskUpdatePayload(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    TaskUpdatePayload(String taskDescription) {
        this.taskDescription = taskDescription;
    }

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
