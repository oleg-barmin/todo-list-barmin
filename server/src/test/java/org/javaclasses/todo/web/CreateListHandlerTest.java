package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.Routes.getCreateTodoListRoute;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestUsers.USER_1;

@DisplayName("CreateListHandler should")
class CreateListHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create new lists by signed in user.")
    void testCreateList() {
        CreateListPayload payload = new CreateListPayload(
                new TodoListId(UUID.randomUUID().toString())
        );

        Response response = specification
                .header(X_TODO_TOKEN, USER_1.getToken().getValue())
                .body(new Gson().toJson(payload))
                .post(getCreateTodoListRoute());

        Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                "response status must be 200, when signed in user creates list, but it don't.");
    }

    @Override
    Response sendRequest(Token token, UserId userId) {
        CreateListPayload payload = new CreateListPayload(
                new TodoListId(UUID.randomUUID().toString())
        );

        return specification.body(new Gson().toJson(payload))
                            .post(getCreateTodoListRoute());
    }
}
