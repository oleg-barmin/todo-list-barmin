package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.Routes.getUserListsRoute;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

@DisplayName("ReadUserListsHandler should")
class ReadUserListsHandlerTest extends AbstractSecuredHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final RequestSpecification bobSpecification = getRequestSpecificationFor(bob);

    @Test
    @DisplayName("read all to-do lists of user.")
    void testReadTasksNonExistingTodoList() {
        TodoListId firstTodoList = generateTodoListId();
        TodoListId secondTodoList = generateTodoListId();

        addTodoList(firstTodoList, bobSpecification);
        addTodoList(secondTodoList, bobSpecification);

        bobSpecification.get(getUserListsRoute())
                        .then()
                        .statusCode(HTTP_OK);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        return specification.get(getUserListsRoute());
    }
}
