package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
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

    @Test
    void testTaskCreation() {
        Token token = signIn(username, password);
        getRequestSpecification().header(X_TODO_TOKEN, token.getValue());

        UserId userId = getUserId();

        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

        String taskUuid = UUID.randomUUID().toString();

        addTodoList(todoList);


        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation"));
        getRequestSpecification().body(payload);
        Response response = getRequestSpecification().post(String.format("/lists/%s/%s", todoListUuid, taskUuid));

        Assertions.assertEquals(HTTP_OK, response.getStatusCode());
    }


    @Override
    Response performOperation(Token token, UserId userId) {
        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        TodoList todoList = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();

        addTodoList(todoList);

        String taskUuid = UUID.randomUUID().toString();
        String payload = new Gson().toJson(new CreateTaskPayload("implement task creation tests"));
        getRequestSpecification().body(payload);
        Response response = getRequestSpecification().post(String.format("/lists/%s/%s", todoListUuid, taskUuid));

        return response;
    }
}
