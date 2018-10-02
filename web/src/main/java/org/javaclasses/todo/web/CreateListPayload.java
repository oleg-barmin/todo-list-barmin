package org.javaclasses.todo.web;

import org.javaclasses.todo.model.entity.TodoListId;

/**
 * Payload of create list request.
 *
 * @author Oleg Barmin
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
