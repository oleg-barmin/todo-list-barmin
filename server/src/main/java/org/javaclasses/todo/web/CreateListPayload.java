package org.javaclasses.todo.web;

import org.javaclasses.todo.model.TodoListId;

/**
 * Payload of create list request.
 */
public class CreateListPayload {

    private final TodoListId todoListId;

    /**
     * Creates {@code CreateListPayload} instance.
     *
     * @param todoListId ID of to-do list to create
     */
    CreateListPayload(TodoListId todoListId) {
        this.todoListId = todoListId;
    }

    public TodoListId getTodoListId() {
        return todoListId;
    }
}
