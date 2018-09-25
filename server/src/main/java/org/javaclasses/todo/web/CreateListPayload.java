package org.javaclasses.todo.web;

import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;

public class CreateListPayload {
    private final UserId userId;
    private final TodoListId todoListId;

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
