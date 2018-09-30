package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestUsers.USER_1;

/**
 * Integration test of to-do list creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("CreateListHandler should")
class CreateListHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create new lists by signed in user.")
    void testCreateList() {
        CreateListPayload payload = new CreateListPayload(
                new TodoListId(UUID.randomUUID()
                                   .toString())
        );

        Response response = specification
                .header(X_TODO_TOKEN, USER_1.getToken()
                                            .getValue())
                .body(payload)
                .post(getTodoListRoute());

        response.then()
                .statusCode(describedAs("response status must be 200, " +
                                                "when signed in user creates list, but it don't.",
                                        is(HTTP_OK)));
    }

    @Override
    Response sendRequest(UserId userId) {
        CreateListPayload payload = new CreateListPayload(
                new TodoListId(UUID.randomUUID()
                                   .toString())
        );

        return specification.body(payload)
                            .post(getTodoListRoute());
    }
}
