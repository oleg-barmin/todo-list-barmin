package org.javaclasses.todo.web;

import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;

/**
 * Payload of create list request.
 */
public class CreateListPayload {

    private final UserId userId;
    private final TodoListId todoListId;

    /**
     * Creates {@code CreateListPayload} instance.
     *
     * @param userId     owner of to-do list to create
     * @param todoListId ID of to-do list to create
     */
    CreateListPayload(UserId userId, TodoListId todoListId) {
        this.userId = userId;
        this.todoListId = todoListId;
    }

    public UserId getUserId() {
        return userId;
    }

    public TodoListId getTodoListId() {
        return todoListId;
    }
}
