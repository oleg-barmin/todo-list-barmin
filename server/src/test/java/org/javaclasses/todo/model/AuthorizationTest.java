package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @author Oleg Barmin
 */
@DisplayName("Authorization should")
class AuthorizationTest {
    private final TaskStorage taskStorage = new TaskStorage();
    private final TodoListStorage todoListStorage = new TodoListStorage();
    private final Authorization authorization = new Authorization(taskStorage, todoListStorage);

    private static TodoList getBuild(UserId owner, TodoListId todoListId) {
        return new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(owner)
                .build();
    }

    private static Task createTask(TodoListId todoListId, TaskId taskId) {
        return new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription("conquer the world on Sunday")
                .setCreationDate(new Date())
                .build();
    }

    @Test
    @DisplayName("validate access to to-do lists.")
    void testValidateAccessToTodoList() {
        UserId userId = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        TodoList todoList = getBuild(userId, todoListId);
        todoListStorage.write(todoList);


        authorization.validateAssess(userId, todoListId);
    }

    @Test
    @DisplayName("throw TodoListNotFoundException if try to access to to-do list which doesn't exist.")
    void testValidateAccessToNonExistingTodoList() {
        UserId userId = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        Assertions.assertThrows(TodoListNotFoundException.class, () -> authorization.validateAssess(userId, todoListId));
    }

    @Test
    @DisplayName("throw AccessDeniedException if try to access to to-do list which has other owner.")
    void testValidateAccessToForeignTodoList() {
        UserId owner = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());
        TodoList todoList = getBuild(owner, todoListId);
        todoListStorage.write(todoList);
        UserId userId = new UserId(UUID.randomUUID().toString());

        Assertions.assertThrows(AccessDeniedException.class, () -> authorization.validateAssess(userId, todoListId));
    }

    @Test
    @DisplayName("validate access to tasks.")
    void testValidateAccessToTask() {
        UserId userId = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        TodoList todoList = getBuild(userId, todoListId);
        todoListStorage.write(todoList);

        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        Task task = createTask(todoListId, taskId);

        taskStorage.write(task);

        authorization.validateAssess(userId, taskId);
    }

    @Test
    @DisplayName("throw TaskNotFoundException if try to access task which doesn't exist")
    void testValidateAccessNonExistingToTask() {
        UserId userId = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        TodoList todoList = getBuild(userId, todoListId);
        todoListStorage.write(todoList);

        TaskId taskId = new TaskId(UUID.randomUUID().toString());

        Assertions.assertThrows(TaskNotFoundException.class, () -> authorization.validateAssess(userId, taskId));
    }

    @Test
    @DisplayName("throw AccessDeniedException if try to access to task which belongs to to-do list with other owner.")
    void testValidateAccessToForeignTask() {
        UserId owner = new UserId(UUID.randomUUID().toString());
        UserId userId = new UserId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        TodoList todoList = getBuild(owner, todoListId);
        todoListStorage.write(todoList);

        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        Task task = createTask(todoListId, taskId);

        taskStorage.write(task);

        Assertions.assertThrows(AccessDeniedException.class, () -> authorization.validateAssess(userId, todoListId));
    }
}
