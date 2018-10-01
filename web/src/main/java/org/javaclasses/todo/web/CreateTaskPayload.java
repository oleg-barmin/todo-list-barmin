package org.javaclasses.todo.web;

/**
 * Payload of create task request.
 *
 * @author Oleg Barmin
 */
class CreateTaskPayload {

    private final String taskDescription;

    /**
     * Creates {@code CreateTaskPayload} instance.
     *
     * @param taskDescription description of task to create.
     */
    CreateTaskPayload(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    String getTaskDescription() {
        return taskDescription;
    }
}
