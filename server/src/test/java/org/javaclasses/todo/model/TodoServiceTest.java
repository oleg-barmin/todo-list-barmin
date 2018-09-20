package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("OverlyCoupledClass")
@DisplayName("TodoService should")
class TodoServiceTest {
    private Authentication authentication;
    private TodoListStorage todoListStorage;
    private TaskStorage taskStorage;
    private TodoService todoService;

    private final Username username = new Username("examaple@mail.org");
    private final Password password = new Password("qwerty12345");

    @BeforeEach
    void init() {
        authentication = new Authentication();
        todoListStorage = new TodoListStorage();
        taskStorage = new TaskStorage();

        todoService = new TodoService(authentication, todoListStorage, taskStorage);
    }


    private UserId createUser() {
        return authentication.createUser(username, password);
    }

    private TodoList createAndSaveTodoList(UserId owner) {
        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID().toString()))
                .setOwner(owner)
                .build();
        todoListStorage.write(todoList);
        return todoList;
    }

    private Task createAndSaveTask(TodoListId todoListId) {
        Task task = new Task.TaskBuilder()
                .setTaskId(new TaskId(UUID.randomUUID().toString()))
                .setTodoListId(todoListId)
                .setDescription("task")
                .setCreationDate(new Date())
                .build();

        taskStorage.write(task);
        return task;
    }

    @Test
    @DisplayName("create new TodoLists.")
    void testCreateList() {
        UserId userId = createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        todoService.authorizeBy(token)
                .createList(todoListId)
                .withOwner(userId)
                .execute();
    }

    @Test
    @DisplayName("throw TodoListAlreadyExistsException if try to create list with ID which already exists in storage.")
    void testCreateListWithRegisteredId() {
        UserId userId = createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        todoService.authorizeBy(token).
                createList(todoListId)
                .withOwner(userId)
                .execute();

        Assertions.assertThrows(TodoListAlreadyExistsException.class, () ->
                        todoService.authorizeBy(token)
                                .createList(todoListId)
                                .withOwner(userId)
                                .execute()
                , "Should throw TodoListAlreadyExistsException, but it didn't.");
    }

    @Test
    @DisplayName("retrieve all Tasks of specified TodoList.")
    void testGetTasksOfTodoList() {
        UserId userId = createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        todoService.authorizeBy(token)
                .createList(todoListId)
                .withOwner(userId)
                .execute();

        TaskId firstTask = new TaskId(UUID.randomUUID().toString());
        TaskId secondTask = new TaskId(UUID.randomUUID().toString());
        TaskId thirdTask = new TaskId(UUID.randomUUID().toString());

        todoService.authorizeBy(token)
                .addTask(firstTask)
                .withDescription("task1")
                .withTodoListId(todoListId)
                .execute();
        todoService.authorizeBy(token)
                .addTask(secondTask)
                .withDescription("task2")
                .withTodoListId(todoListId)
                .execute();
        todoService.authorizeBy(token)
                .addTask(thirdTask)
                .withDescription("task3")
                .withTodoListId(todoListId)
                .execute();


        List<Task> tasks = todoService.authorizeBy(token).readTasksFrom(todoListId).execute();

        Assertions.assertEquals(3, tasks.size(), "TodoList had to contain 3 Tasks, but it didn't.");
    }

    @Test
    @DisplayName("add tasks.")
    void testAddTask() {
        UserId userId = createUser();
        TodoList todoList = createAndSaveTodoList(userId);
        Token token = authentication.signIn(username, password);
        TaskId taskId = new TaskId(UUID.randomUUID().toString());


        todoService.authorizeBy(token)
                .addTask(taskId)
                .withTodoListId(todoList.getId())
                .withDescription("task")
                .execute();

        Optional<Task> taskById = taskStorage.read(taskId);

        Assertions.assertTrue(taskById.isPresent(), "Task should be added, but it didn't");
    }

    @Test
    @DisplayName("throw TaskAlreadyExistsException if task with given ID already exists in storage..")
    void testAddTaskWithSameID() {
        UserId userId = createUser();
        TodoList todoList = createAndSaveTodoList(userId);
        Token token = authentication.signIn(username, password);
        TaskId taskId = new TaskId(UUID.randomUUID().toString());


        todoService.authorizeBy(token)
                .addTask(taskId)
                .withTodoListId(todoList.getId())
                .withDescription("task")
                .execute();

        Assertions.assertThrows(TaskAlreadyExistsException.class, () ->
                        todoService.authorizeBy(token)
                                .addTask(taskId)
                                .withTodoListId(todoList.getId())
                                .withDescription("task")
                                .execute(),
                "should throw TaskAlreadyExistsException, but it didn't.");
    }

    @Test
    @DisplayName("update tasks.")
    void testUpdateTask() {
        UserId userId = createUser();
        Token token = authentication.signIn(username, password);
        TodoList todoList = createAndSaveTodoList(userId);
        TaskId taskId = new TaskId(UUID.randomUUID().toString());

        todoService.authorizeBy(token)
                .addTask(taskId)
                .withTodoListId(todoList.getId())
                .withDescription("task")
                .execute();

        Optional<Task> notUpdatedTaskOptional = taskStorage.read(taskId);
        if (!notUpdatedTaskOptional.isPresent()) {
            Assertions.fail("Task to update should be added but it didn't.");
            return;
        }
        Task notUpdatedTask = notUpdatedTaskOptional.get();


        String updatedDescription = "updated";
        todoService.authorizeBy(token)
                .updateTask(notUpdatedTask.getId())
                .withDescription(updatedDescription)
                .completed()
                .execute();


        Optional<Task> updatedTaskOptional = taskStorage.read(taskId);
        if (!updatedTaskOptional.isPresent()) {
            Assertions.fail("Updated task should be in storage, but it doesn't.");
        }
        Task updatedTask = updatedTaskOptional.get();
        Assertions.assertEquals(updatedDescription, updatedTask.getDescription(),
                "Task description should be updated, but it didn't.");
    }

    @Test
    @DisplayName("remove tasks by ID.")
    void testTaskDelete() {
        UserId userId = createUser();
        TodoList todoList = createAndSaveTodoList(userId);
        Task task = createAndSaveTask(todoList.getId());
        Token token = authentication.signIn(username, password);


        todoService.authorizeBy(token).removeTask(task.getId()).execute();


        Optional<Task> taskByID = taskStorage.read(task.getId());
        Assertions.assertFalse(taskByID.isPresent(), "Task should be deleted, but it didn't.");
    }

    @Test
    @DisplayName("throw TaskNotFoundException if try to remove task which doesn't exist.")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testDeleteNotExistingTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        TaskId idOfTaskToDelete = new TaskId("213");

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                todoService.authorizeBy(token).removeTask(idOfTaskToDelete).execute());
    }

}