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

@DisplayName("TodoService should")
class TodoServiceTest {
    private Authentication authentication;
    private TodoListStorage todoListStorage;
    private TaskStorage taskStorage;

    private TodoService todoService;

    @BeforeEach
    void init() {
        authentication = new Authentication();
        todoListStorage = new TodoListStorage();
        taskStorage = new TaskStorage();

        todoService = new TodoService(authentication, todoListStorage, taskStorage);
    }



    private UserId createUser() {
        Username username = new Username("user");
        Password password = new Password("password");

        return authentication.createUser(username, password);
    }

    private TodoList createTodoList(UserId owner) {
        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID().toString()))
                .setOwner(owner)
                .build();
        return todoListStorage.write(todoList);
    }

    private Task createTask(TodoListId todoListId) {
        Task task = new Task.TaskBuilder()
                .setTaskId(new TaskId(UUID.randomUUID().toString()))
                .setTodoListId(todoListId)
                .setDescription("task")
                .setCreationDate(new Date())
                .build();

        return taskStorage.write(task);
    }

    @Test
    @DisplayName("create new TodoLists.")
    void testCreateList(){
        UserId userId = createUser();
        Token token = authentication.signIn(new Username("user"), new Password("password"));
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        todoService.createList(todoListId)
                .withOwner(userId)
                .execute(token);
    }

    @Test
    @DisplayName("retrieve all Tasks of specified TodoList.")
    void testGetTasksOfTodoList(){
        UserId userId = createUser();
        Token token = authentication.signIn(new Username("user"), new Password("password"));
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        todoService.createList(todoListId)
                .withOwner(userId)
                .execute(token);

        TaskId firstTask = new TaskId(UUID.randomUUID().toString());
        TaskId secondTask = new TaskId(UUID.randomUUID().toString());
        TaskId thirdTask = new TaskId(UUID.randomUUID().toString());

        todoService.addTask(firstTask)
                .withDescription("task1")
                .withTodoListId(todoListId)
                .execute(token);
        todoService.addTask(secondTask)
                .withDescription("task2")
                .withTodoListId(todoListId)
                .execute(token);
        todoService.addTask(thirdTask)
                .withDescription("task3")
                .withTodoListId(todoListId)
                .execute(token);


        List<Task> tasks = todoService.getTasksOfTodoList(todoListId).execute(token);

        Assertions.assertEquals(3, tasks.size(), "TodoList had to contain 3 Tasks, but it didn't.");
    }

    @Test
    @DisplayName("add tasks.")
    void testAddTask() {
        UserId userId = createUser();
        TodoList todoList = createTodoList(userId);
        Token token = authentication.signIn(new Username("user"), new Password("password"));
        TaskId taskId = new TaskId(UUID.randomUUID().toString());


        todoService.addTask(taskId)
                .withTodoListId(todoList.getId())
                .withDescription("task")
                .execute(token);

        Optional<Task> taskById = taskStorage.findById(taskId);

        Assertions.assertTrue(taskById.isPresent(), "Task should be added, but it didn't");
    }

    @Test
    @DisplayName("update tasks.")
    void testUpdateTask() {
        UserId userId = createUser();
        Token token = authentication.signIn(new Username("user"), new Password("password"));
        TodoList todoList = createTodoList(userId);
        TaskId taskId = new TaskId(UUID.randomUUID().toString());

        todoService.addTask(taskId)
                .withTodoListId(todoList.getId())
                .withDescription("task")
                .execute(token);

        Optional<Task> notUpdatedTaskOptional = taskStorage.findById(taskId);
        if(!notUpdatedTaskOptional.isPresent()) {
            Assertions.fail("Task to update should be added but it didn't.");
            return;
        }
        Task notUpdatedTask = notUpdatedTaskOptional.get();


        String updatedDescription = "updated";
        todoService.updateTask(notUpdatedTask.getId())
                .withTodoListId(notUpdatedTask.getTodoListId())
                .withDescription(updatedDescription)
                .withStatus(notUpdatedTask.isCompleted())
                .withCreationDate(notUpdatedTask.getCreationDate())
                .withLastUpdateDate(notUpdatedTask.getLastUpdateDate())
                .execute(token);


        Optional<Task> updatedTaskOptional = taskStorage.findById(taskId);
        if(!updatedTaskOptional.isPresent()){
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
        TodoList todoList = createTodoList(userId);
        Task task = createTask(todoList.getId());
        Token token = authentication.signIn(new Username("user"), new Password("password"));


        todoService.removeTask(task.getId()).execute(token);


        Optional<Task> taskByID = taskStorage.findById(task.getId());
        Assertions.assertFalse(taskByID.isPresent(), "Task should be deleted, but it didn't.");
    }

}