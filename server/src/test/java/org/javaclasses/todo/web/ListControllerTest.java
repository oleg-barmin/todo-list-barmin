package org.javaclasses.todo.web;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TodoListApplication.CREATE_LIST_PATH;

@DisplayName("ListController should")
class ListControllerTest {

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Nested
    class CreateListHandlerTest extends AbstractSecuredHandlerTest {
        private final RequestSpecification specification = getRequestSpecification();

        @Test
        @DisplayName("create new lists when user signed in.")
        void testCreateList() {
            Token token = signIn(username, password);
            specification.header(X_TODO_TOKEN, token.getValue());


            CreateListPayload payload = new CreateListPayload(
                    new UserId(UUID.randomUUID().toString()),
                    new TodoListId(UUID.randomUUID().toString())
            );

            String requestBody = new Gson().toJson(payload);
            specification.body(requestBody);

            Response response = specification.post(CREATE_LIST_PATH);

            Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                    "response status must be 200, when signed in user creates list, but it don't.");
        }

        @Override
        Response performOperation(Token token) {
            CreateListPayload payload = new CreateListPayload(
                    new UserId(UUID.randomUUID().toString()),
                    new TodoListId(UUID.randomUUID().toString())
            );

            String requestBody = new Gson().toJson(payload);
            specification.body(requestBody);
            Response response = specification.post(CREATE_LIST_PATH);
            return response;
        }
    }

    @Nested
    class ReadTasksHandlerTest extends AbstractSecuredHandlerTest {
        private final RequestSpecification specification = getRequestSpecification();

        @Test
        @DisplayName("read tasks from to-do list.")
        void testReadTasks() {
            Token token = signIn(username, password);
            specification.header(X_TODO_TOKEN, token.getValue());

            UserId userId = getStorageFactory().getAuthSessionStorage().read(token).get().getUserId();

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(new TaskId(UUID.randomUUID().toString()))
                    .setTodoListId(todoListId)
                    .setDescription("write tests on read tasks.")
                    .setCreationDate(new Date())
                    .build();
            Task secondTask = new Task.TaskBuilder()
                    .setTaskId(new TaskId(UUID.randomUUID().toString()))
                    .setTodoListId(todoListId)
                    .setDescription("second task to do")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);
            getStorageFactory().getTaskStorage().write(secondTask);

            Collection<Task> addedTasks = new LinkedList<>();
            addedTasks.add(firstTask);
            addedTasks.add(secondTask);


            Response response = specification.get("/lists/" + todoListUuid);

            Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                    "return status code 200, when signed in user read tasks from his to-do list.");

            Task[] receivedTasks = new Gson().fromJson(response.body().asString(), Task[].class);

            Assertions.assertTrue(addedTasks.containsAll(Lists.newArrayList(receivedTasks)),
                    "provide all tasks of to-do list, but it don't.");
        }

        @Test
        @DisplayName("forbid to read tasks from non-existing to-do lists..")
        void testReadTasksNonExistingTodoList() {
            Token token = signIn(username, password);
            specification.header(X_TODO_TOKEN, token.getValue());


            Response response = specification.get("/lists/2notExists213");

            Assertions.assertEquals(HTTP_FORBIDDEN, response.getStatusCode(),
                    "return status code 403, when signed in user read tasks from non-existing to-do list.");
        }

        @Override
        Response performOperation(Token invalidToken) {
            UserId userId = new UserId(invalidToken.getValue());

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(new TaskId(UUID.randomUUID().toString()))
                    .setTodoListId(todoListId)
                    .setDescription("write tests on read tasks.")
                    .setCreationDate(new Date())
                    .build();
            Task secondTask = new Task.TaskBuilder()
                    .setTaskId(new TaskId(UUID.randomUUID().toString()))
                    .setTodoListId(todoListId)
                    .setDescription("second task to do")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);
            getStorageFactory().getTaskStorage().write(secondTask);


            Response response = specification.get("/lists/" + todoListUuid);
            return response;
        }
    }


    @Nested
    class FindTaskHandlerTest extends AbstractSecuredHandlerTest {

        @Test
        @DisplayName("read tasks by ID.")
        void testFindTaskById() {
            Token token = signIn(username, password);
            getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

            UserId userId = getStorageFactory().getAuthSessionStorage().read(token).get().getUserId();

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);

            Response response = getRequestSpecification().get(String.format("/lists/%s/%s", todoListUuid, taskUuid));

            Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                    "return status code 200, when signed in user find tasks by ID from his to-do list.");

            Task receivedTask = new Gson().fromJson(response.body().asString(), Task.class);

            Assertions.assertEquals(firstTask, receivedTask,
                    "provide task by ID, but it don't.");
        }

        @Override
        Response performOperation(Token token) {
            UserId userId = new UserId(token.getValue());

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);

            Response response = getRequestSpecification().get(String.format("/lists/%s/%s", todoListUuid, taskUuid));
            return response;
        }
    }


    @Nested
    class CreateTaskHandlerTest extends AbstractSecuredHandlerTest {
        @Test
        void testTaskCreation() {
            Token token = signIn(username, password);
            getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

            UserId userId = getStorageFactory().getAuthSessionStorage().read(token).get().getUserId();

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            String taskUuid = UUID.randomUUID().toString();

            String payload = new Gson().toJson(new CreateTaskPayload("implement task adding"));

            getRequestSpecification().body(payload);
            Response response = getRequestSpecification().post(String.format("/lists/%s/%s", todoListUuid, taskUuid));

            Assertions.assertEquals(HTTP_OK, response.getStatusCode());
        }


        @Override
        Response performOperation(Token token) {
            UserId userId = new UserId(UUID.randomUUID().toString());

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            String taskUuid = UUID.randomUUID().toString();

            String payload = new Gson().toJson(new CreateTaskPayload("implement task adding"));

            getRequestSpecification().body(payload);
            Response response = getRequestSpecification().post(String.format("/lists/%s/%s", todoListUuid, taskUuid));
            return response;
        }
    }


    @Nested
    class UpdateTaskHandlerTest extends AbstractSecuredHandlerTest {

        @Test
        void testUpdateTask() {
            Token token = signIn(username, password);
            getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

            UserId userId = getStorageFactory().getAuthSessionStorage().read(token).get().getUserId();

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);


            TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

            String body = new Gson().toJson(payload);

            getRequestSpecification().body(body);

            Response response = getRequestSpecification().put(String.format("/lists/%s/%s", todoListUuid, taskUuid));


            Assertions.assertEquals(HTTP_OK, response.getStatusCode());
        }


        @Override
        Response performOperation(Token token) {
            UserId userId = new UserId(UUID.randomUUID().toString());

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);


            TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

            String body = new Gson().toJson(payload);

            getRequestSpecification().body(body);

            Response response = getRequestSpecification().put(String.format("/lists/%s/%s", todoListUuid, taskUuid));

            return response;
        }
    }


    @Nested
    class RemoveTaskHandlerTest extends AbstractSecuredHandlerTest {

        @Test
        @DisplayName("remove tasks by ID.")
        void testFindTaskById() {
            Token token = signIn(username, password);
            getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

            UserId userId = getStorageFactory().getAuthSessionStorage().read(token).get().getUserId();

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);

            Response response = getRequestSpecification().delete(String.format("/lists/%s/%s", todoListUuid, taskUuid));

            Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                    "return status code 200, when signed in user find tasks by ID from his to-do list.");
        }

        @Override
        Response performOperation(Token token) {
            UserId userId = new UserId(token.getValue());

            String todoListUuid = UUID.randomUUID().toString();
            TodoListId todoListId = new TodoListId(todoListUuid);

            String taskUuid = UUID.randomUUID().toString();
            TaskId taskId = new TaskId(taskUuid);

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(userId)
                    .build();
            getStorageFactory().getTodoListStorage().write(build);

            Task firstTask = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription("write tests on find task by ID.")
                    .setCreationDate(new Date())
                    .build();

            getStorageFactory().getTaskStorage().write(firstTask);

            Response response = getRequestSpecification().delete(String.format("/lists/%s/%s", todoListUuid, taskUuid));
            return response;
        }
    }

}