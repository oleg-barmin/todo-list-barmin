package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;

@DisplayName("CreateTaskHandlerTest should")
class CreateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create tasks.")
    void testTaskCreation() {
        Token token = signIn(username, password);
        specification.header(X_TODO_TOKEN, token.getValue());

        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId, getUserId());
        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation"));


        Response response = specification.body(payload)
                .post(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));


        Task task = readTask(todoListId, taskId);
        Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                "response with status code 200, but it don't.");
        Assertions.assertEquals(taskId, task.getId(),
                "return task with requested ID, but it don't.");
    }


    @Override
    Response sendRequest(Token token, UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId, userId);

        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation tests"));

        return specification.body(payload).post(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }
}
