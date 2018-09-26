package org.javaclasses.todo.web;

class TaskUpdatePayload {
    private final boolean completed;
    private final String taskDescription;

    TaskUpdatePayload(boolean completed, String taskDescription) {
        this.completed = completed;
        this.taskDescription = taskDescription;
    }

    boolean isCompleted() {
        return completed;
    }

    String getTaskDescription() {
        return taskDescription;
    }
}
