package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTodoListUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

/**
 * Integration test of to-do list creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("CreateListHandler should")
class CreateListHandlerTest extends AbstractSecuredHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final RequestSpecification specification = getRequestSpecificationFor(bob);
    private final TodoListId todoListId = generateTodoListId();


    @Test
    @DisplayName("create new lists by signed in bob.")
    void testCreateList() {
        specification.post(getTodoListUrl(todoListId))
                     .then()
                     .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("response with 403 status code when try to add to-do list with existing ID.")
    void testCreateListWithExistingId() {
        addTodoList(todoListId, specification);

        specification.post(getTodoListUrl(todoListId))
                     .then()
                     .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        return specification.post(getTodoListUrl(generateTodoListId()));
    }
}
