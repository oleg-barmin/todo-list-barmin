package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TodoListApplication.CREATE_LIST_PATH;

@Nested
class CreateListHandlerTest extends AbstractSecuredHandlerTest {
    private final RequestSpecification specification = getRequestSpecification();

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Test
    @DisplayName("create new lists by signed in user.")
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
    Response performOperation(Token token, UserId userId) {
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
