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
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;

@Nested
class ReadTasksHandlerTest extends AbstractSecuredHandlerTest {
    private final RequestSpecification specification = getRequestSpecification();

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Test
    @DisplayName("read tasks from to-do list.")
    void testReadTasks() {
        Token token = signIn(username, password);
        specification.header(X_TODO_TOKEN, token.getValue());

        UserId userId = getUserId();

        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

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

        addTodoList(todoList);
        addTask(firstTask);
        addTask(secondTask);

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
    Response performOperation(Token invalidToken, UserId userId) {
        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

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

        addTodoList(todoList);
        addTask(firstTask);
        addTask(secondTask);


        Response response = specification.get("/lists/" + todoListUuid);
        return response;
    }
}
