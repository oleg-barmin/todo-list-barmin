package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.*;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TodoListApplication.LISTS_PATH;

@DisplayName("ListController should")
class ListControllerTest extends AbstractControllerTest {

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create new lists when user signed in.")
    void testCreateList() {
        Token token = signIn(username, password);

        CreateListPayload payload = new CreateListPayload(
                new UserId(UUID.randomUUID().toString()),
                new TodoListId(UUID.randomUUID().toString())
        );

        String requestBody = new Gson().toJson(payload);

        specification.header(X_TODO_TOKEN, token.getValue());
        specification.body(requestBody);
        Response response = specification.post(LISTS_PATH);

        Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                "response status must be 200, when signed in user creates list, but it don't.");
    }

    @Test
    @DisplayName("forbid creation of lists to unauthorized users.")
    void testForbidCreateList() {
        CreateListPayload payload = new CreateListPayload(
                new UserId(UUID.randomUUID().toString()),
                new TodoListId(UUID.randomUUID().toString())
        );

        String requestBody = new Gson().toJson(payload);

        specification.header(X_TODO_TOKEN, "invalid token");
        specification.body(requestBody);
        Response response = specification.post(LISTS_PATH);

        Assertions.assertEquals(HTTP_FORBIDDEN, response.getStatusCode(),
                "response status must be 403, when not signed in user creates list, but it don't.");
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedCreateList() {
        Token token = signIn(username, password);

        specification.header("INVALID_HEADER", token.getValue());
        Response response = specification.post(LISTS_PATH);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, response.getStatusCode(),
                "response status must be 401, " +
                        "when attempt to create list with invalid token header, but it don't.");
    }
}