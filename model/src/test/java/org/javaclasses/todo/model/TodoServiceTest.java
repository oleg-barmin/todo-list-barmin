package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.model.entity.Username;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.javaclasses.todo.storage.impl.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Testing {@link TodoService} which should provide access to all
 * functions of TodoList application.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings({"OverlyCoupledClass", // TodoService is public API so it is needed many dependencies.
        "ClassIndependentOfModule"})
// integration test of TodoService should have many dependencies.
@DisplayName("TodoService should")
class TodoServiceTest {

    private final Username username = new Username("examaple@mail.org");
    private final Password password = new Password("qwerty12345");
    private Authentication authentication;
    private TodoListStorage todoListStorage;
    private TaskStorage taskStorage;
    private TodoService todoService;

    @BeforeEach
    void init() {
        authentication = new Authentication(new UserStorage(), new AuthSessionStorage());
        todoListStorage = new TodoListStorage();
        taskStorage = new TaskStorage();

        todoService = new TodoService(authentication, todoListStorage, taskStorage);
    }

    private void createUser() {
        authentication.createUser(username, password);
    }

    private TodoList createAndSaveTodoList(UserId owner) {
        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID()
                                                  .toString()))
                .setOwner(owner)
                .build();
        todoListStorage.write(todoList);
        return todoList;
    }

    private Task createAndSaveTask(TodoListId todoListId) {
        Task task = new Task.TaskBuilder()
                .setTaskId(new TaskId(UUID.randomUUID()
                                          .toString()))
                .setTodoListId(todoListId)
                .setDescription("implement task creation in todo service")
                .setCreationDate(new Date())
                .build();

        taskStorage.write(task);
        return task;
    }

    /*
     * To test to-do list creation user ID is not needed, because exception should occur.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("create new TodoLists.")
    void testCreateList() {
        createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        todoService.createList(todoListId)
                   .authorizedWith(token)
                   .execute();
    }

    /*
     * To test to-do list creation user ID is not needed, because exception should occur.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw TodoListAlreadyExistsException if try to create list with ID which already exists in storage.")
    void testCreateListWithRegisteredId() {
        createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        todoService.createList(todoListId)
                   .authorizedWith(token)
                   .execute();

        assertThrows(TodoListAlreadyExistsException.class, () ->
                             todoService.createList(todoListId)
                                        .authorizedWith(token)
                                        .execute()
                , "Should throw TodoListAlreadyExistsException, but it didn't.");
    }

    /*
     * To test read task by to-do list ID creation user ID is not needed, because exception should occur.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("retrieve all Tasks of specified TodoList.")
    void testGetTasksOfTodoList() {
        createUser();
        Token token = authentication.signIn(username, password);
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        todoService.createList(todoListId)
                   .authorizedWith(token)
                   .execute();

        TaskId firstTaskId = new TaskId(UUID.randomUUID()
                                            .toString());
        TaskId secondTaskId = new TaskId(UUID.randomUUID()
                                             .toString());
        TaskId thirdTaskId = new TaskId(UUID.randomUUID()
                                            .toString());

        todoService.addTask(firstTaskId)
                   .authorizedWith(token)
                   .withDescription("task1")
                   .withTodoListId(todoListId)
                   .execute();
        todoService.addTask(secondTaskId)
                   .authorizedWith(token)
                   .withDescription("task2")
                   .withTodoListId(todoListId)
                   .execute();
        todoService.addTask(thirdTaskId)
                   .authorizedWith(token)
                   .withDescription("task3")
                   .withTodoListId(todoListId)
                   .execute();

        List<Task> tasks = todoService.readTasksFrom(todoListId)
                                      .authorizedWith(token)
                                      .execute();

        assertEquals(3, tasks.size(), "TodoList had to contain 3 Tasks, but it didn't.");
    }

    @Test
    @DisplayName("retrieve all to-do list of user.")
    void testReadTodoListsOfUser() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId owner = authentication.validate(token);

        TodoList firstTodoList = new TodoList.TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID()
                                                  .toString()))
                .setOwner(owner)
                .build();
        TodoList secondTodoList = new TodoList.TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID()
                                                  .toString()))
                .setOwner(owner)
                .build();

        todoListStorage.write(firstTodoList);
        todoListStorage.write(secondTodoList);

        Collection<TodoList> uploadedTodoLists = new ArrayList<>();
        uploadedTodoLists.add(firstTodoList);
        uploadedTodoLists.add(secondTodoList);

        List<TodoList> todoLists = todoService.readUserTodoLists()
                                              .authorizedWith(token)
                                              .execute();

        assertEquals(2, todoLists.size(), "user has to have 2 to-do lists, but it didn't.");
        assertTrue(uploadedTodoLists.containsAll(todoLists),
                   "read all uploaded to-do lists, but it don't.");

    }

    @Test
    @DisplayName("find tasks by ID.")
    void testFindTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);
        TodoList todoList = createAndSaveTodoList(userId);
        Task task = createAndSaveTask(todoList.getId());

        Task storedTask = todoService.findTask(task.getId())
                                     .authorizedWith(token)
                                     .execute();

        assertEquals(task, storedTask);
    }

    /* If test perform properly exception will occur, so return value will be received. */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw TaskNotFoundException when try to get task with non-existing ID.")
    void testFindNonExistingTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

        assertThrows(TaskNotFoundException.class,
                     () -> todoService.findTask(taskId)
                                      .authorizedWith(token)
                                      .execute());
    }

    @Test
    @DisplayName("add tasks.")
    void testAddTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);

        TodoList todoList = createAndSaveTodoList(userId);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

        todoService.addTask(taskId)
                   .authorizedWith(token)
                   .withTodoListId(todoList.getId())
                   .withDescription("implement task adding in todo service")
                   .execute();

        Optional<Task> taskById = taskStorage.read(taskId);

        assertTrue(taskById.isPresent(), "add task, but it didn't");
    }

    @Test
    @DisplayName("throw TaskAlreadyExistsException if task with given ID already exists in storage..")
    void testAddTaskWithSameID() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);
        TodoList todoList = createAndSaveTodoList(userId);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

        todoService.addTask(taskId)
                   .authorizedWith(token)
                   .withTodoListId(todoList.getId())
                   .withDescription("write tests on negative cases on task adding")
                   .execute();

        assertThrows(TaskAlreadyExistsException.class, () ->
                             todoService.addTask(taskId)
                                        .authorizedWith(token)
                                        .withTodoListId(todoList.getId())
                                        .withDescription("write tests on TaskAlreadyExistsException")
                                        .execute(),
                     "throw TaskAlreadyExistsException, but it didn't.");
    }

    @Test
    @DisplayName("update tasks.")
    void testUpdateTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);
        TodoList todoList = createAndSaveTodoList(userId);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

        todoService.addTask(taskId)
                   .authorizedWith(token)
                   .withTodoListId(todoList.getId())
                   .withDescription("change dsl api")
                   .execute();

        Optional<Task> notUpdatedTaskOptional = taskStorage.read(taskId);
        if (!notUpdatedTaskOptional.isPresent()) {
            fail("Task to update should be added but it didn't.");
            return;
        }
        Task notUpdatedTask = notUpdatedTaskOptional.get();

        String updatedDescription = "updated";
        todoService.updateTask(notUpdatedTask.getId())
                   .authorizedWith(token)
                   .withDescription(updatedDescription)
                   .setStatus(true)
                   .execute();

        Optional<Task> updatedTaskOptional = taskStorage.read(taskId);
        if (!updatedTaskOptional.isPresent()) {
            fail("store updated task in storage, but it doesn't.");
        }
        Task updatedTask = updatedTaskOptional.get();
        assertEquals(updatedDescription, updatedTask.getDescription(),
                     "update task description, but it didn't.");
    }

    /*
     * To test non-existing task updating user ID is not needed, because exception should occur.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw TaskNotFoundException if try to updated absent task.")
    void testUpdateThrowTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

        assertThrows(TaskNotFoundException.class, () ->
                todoService.updateTask(taskId)
                           .authorizedWith(token)
                           .withDescription("win olympic games")
                           .setStatus(true)
                           .execute());
    }

    @Test
    @DisplayName("throw UpdateCompletedTaskException if try to updated absent task.")
    void testUpdateCompletedTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoList todoList = createAndSaveTodoList(userId);

        todoService.addTask(taskId)
                   .authorizedWith(token)
                   .withTodoListId(todoList.getId())
                   .withDescription("wash my car")
                   .execute();

        todoService.updateTask(taskId)
                   .authorizedWith(token)
                   .withDescription("new task description.")
                   .setStatus(true)
                   .execute();

        assertThrows(UpdateCompletedTaskException.class, () ->
                todoService.updateTask(taskId)
                           .authorizedWith(token)
                           .withDescription("win next olympic games")
                           .setStatus(true)
                           .execute());
    }

    @Test
    @DisplayName("remove tasks by ID.")
    void testTaskDelete() {
        createUser();
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);
        TodoList todoList = createAndSaveTodoList(userId);
        Task task = createAndSaveTask(todoList.getId());

        todoService.removeTask(task.getId())
                   .authorizedWith(token)
                   .execute();

        Optional<Task> taskByID = taskStorage.read(task.getId());
        assertFalse(taskByID.isPresent(), "delete task, but it didn't.");
    }

    @Test
    @DisplayName("throw TaskNotFoundException if try to remove task which doesn't exist.")
    /*
     * To test deleting of non-existing task user ID is not needed, because exception should occur.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testDeleteNotExistingTask() {
        createUser();
        Token token = authentication.signIn(username, password);
        TaskId idOfTaskToDelete = new TaskId("213");

        assertThrows(TaskNotFoundException.class, () ->
                todoService.removeTask(idOfTaskToDelete)
                           .authorizedWith(token)
                           .execute());
    }

}