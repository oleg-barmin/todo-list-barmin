package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;

@Nested
class UpdateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Test
    void testUpdateTask() {
        Token token = signIn(username, password);
        getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

        UserId userId = getUserId();

        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        String taskUuid = UUID.randomUUID().toString();
        TaskId taskId = new TaskId(taskUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

        Task firstTask = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription("write tests on update task.")
                .setCreationDate(new Date())
                .build();

        addTodoList(todoList);
        addTask(firstTask);

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "complete this test");

        String body = new Gson().toJson(payload);

        getRequestSpecification().body(body);

        Response response = getRequestSpecification().put(String.format("/lists/%s/%s", todoListUuid, taskUuid));


        Assertions.assertEquals(HTTP_OK, response.getStatusCode());
    }


    @Override
    Response performOperation(Token token, UserId userId) {
        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        String taskUuid = UUID.randomUUID().toString();
        TaskId taskId = new TaskId(taskUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

        Task firstTask = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription("write negative cases tests on task update.")
                .setCreationDate(new Date())
                .build();

        addTodoList(todoList);
        addTask(firstTask);


        TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

        String body = new Gson().toJson(payload);

        getRequestSpecification().body(body);

        Response response = getRequestSpecification().put(String.format("/lists/%s/%s", todoListUuid, taskUuid));

        return response;
    }
}
