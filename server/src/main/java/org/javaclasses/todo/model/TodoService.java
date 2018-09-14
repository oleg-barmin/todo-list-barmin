package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.SessionDoesNotExistsException;
import org.javaclasses.todo.auth.impl.AuthenticationImpl;
import org.javaclasses.todo.model.impl.*;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Date;

public class TodoService {
    private Authentication authentication = new AuthenticationImpl();
    private TodoListStorage todoListStorage = new TodoListStorage();
    private TaskStorage taskStorage = new TaskStorage();

    public void createList(Token token, TodoListId todoListId) throws SessionDoesNotExistsException {
        UserId userId = authentication.validate(token);

        TodoList todoList = new TodoList();
        todoList.setId(todoListId);
        todoList.setOwner(userId);

        todoListStorage.write(todoList);
    }

    public void addTask(Token token,
                        TaskId taskId,
                        TodoListId todoListId,
                        String taskDescription,
                        boolean completed,
                        Date creationDate,
                        Date lastUpdateDate) throws SessionDoesNotExistsException {

        authentication.validate(token);

        Task task = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription(taskDescription)
                .setCompleted(completed)
                .setCreationDate(creationDate)
                .setLastUpdateDate(lastUpdateDate)
                .build();

        taskStorage.write(task);
    }

    public void updateTasks(Token token,
                            TaskId taskId,
                            TodoListId todoListId,
                            String taskDescription,
                            boolean completed,
                            Date creationDate,
                            Date lastUpdateDate) throws SessionDoesNotExistsException {

        authentication.validate(token);

        Task task = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription(taskDescription)
                .setCompleted(completed)
                .setCreationDate(creationDate)
                .setLastUpdateDate(lastUpdateDate)
                .build();

        taskStorage.write(task);
    }

    public void deleteTask(Token token, TaskId taskId) throws SessionDoesNotExistsException {
        authentication.validate(token);

        taskStorage.remove(taskId);
    }


}
