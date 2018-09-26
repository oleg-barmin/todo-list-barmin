package org.javaclasses.todo.web;

class CreateTaskPayload {
    private final String taskDescription;

    CreateTaskPayload(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    String getTaskDescription() {
        return taskDescription;
    }
}
