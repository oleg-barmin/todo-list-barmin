package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;

/**
 * Integration test of to-do list creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("CreateListHandler should")
class CreateListHandlerTest extends AbstractPayloadHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create new lists by signed in user.")
    void testCreateList() {
        setTokenToRequestSpecification(specification);

        CreateListPayload payload = new CreateListPayload(generateTodoListId());

        Response response = specification
                .body(payload)
                .post(getTodoListRoute());

        response.then()
                .statusCode(describedAs("response status must be 200, " +
                                                "when signed in user creates list, but it don't.",
                                        is(HTTP_OK)));
    }

    @Test
    @DisplayName("response with 403 status code when try to add to-do list with existing ID.")
    void testCreateListWithExistingId() {
        setTokenToRequestSpecification(specification);

        TodoListId todoListId = generateTodoListId();
        CreateListPayload payload = new CreateListPayload(todoListId);

        addTodoList(todoListId);

        Response response = specification.body(payload)
                                         .post(getTodoListRoute());

        response.then()
                .statusCode(describedAs("response status must be 403 when " +
                                                "try to create list with existing Id.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Override
    Response sendRequest(UserId userId) {
        CreateListPayload payload = new CreateListPayload(generateTodoListId());

        return specification.body(payload)
                            .post(getTodoListRoute());
    }

    @Override
    Response sendEmptyPayloadRequest() {
        return specification.post(getTodoListRoute());
    }
}
