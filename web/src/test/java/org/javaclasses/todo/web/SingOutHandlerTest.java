package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTodoListUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

@DisplayName("SignOutHandle should")
class SingOutHandlerTest extends AbstractSecuredHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final RequestSpecification bobSpecification = getRequestSpecificationFor(bob);

    @Test
    @DisplayName("sign out user from the system.")
    void testSingOutAuthorizedUser() {

        //signed in user creates to-do list.
        addTodoList(generateTodoListId(), bobSpecification);

        //sign out.
        bobSpecification.delete(getAuthenticationRoute())
                        .then()
                        .statusCode(HTTP_OK);

        // if user sign out it should be forbidden for him to do anything.
        bobSpecification.post(getTodoListUrl(generateTodoListId()))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);

    }

    @Test
    @DisplayName("response with 401 status code, when user without X_Todo_Token header tries to sign out.")
    void testSignOutWithoutToken() {
        getNewSpecification().delete(getAuthenticationRoute())
                             .then()
                             .statusCode(HTTP_UNAUTHORIZED);

    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        return specification.delete(getAuthenticationRoute());
    }

    @Override
    @Disabled
        // sign out operation can be performed by any user who has X_Todo_Token
    void testForbidOperation() {
        super.testForbidOperation();
    }
}
