package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;

@Nested
class CreateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    void testTaskCreation() {
        Token token = signIn(username, password);
        specification.header(X_TODO_TOKEN, token.getValue());
        UserId userId = getUserId();

        String taskUuid = UUID.randomUUID().toString();
        String todoListUuid = UUID.randomUUID().toString();

        TodoListId todoListId = new TodoListId(todoListUuid);
        addTodoList(todoListId, userId);

        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation"));
        Response response = specification.body(payload).post(String.format("/lists/%s/%s", todoListUuid, taskUuid));


        Task task = readTask(todoListId, new TaskId(taskUuid));

        Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                "response with status code 200, but it don't.");
        Assertions.assertEquals(new TaskId(taskUuid), task.getId(),
                "return task with requested ID, but it don't.");
    }


    @Override
    Response performOperation(Token token, UserId userId) {
        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);
        addTodoList(todoListId, userId);

        String taskUuid = UUID.randomUUID().toString();

        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation tests"));

        return specification.body(payload).post(String.format("/lists/%s/%s", todoListUuid, taskUuid));
    }
}
