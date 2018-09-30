package org.javaclasses.todo.web;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestUsers.USER_1;

/**
 * todo documentation.
 *
 * @author Oleg Barmin
 */
abstract class AbstractPayloadHandlerTest extends AbstractSecuredHandlerTest {

    abstract Response sendEmptyPayloadRequest();

    @Test
    @DisplayName("response with 400 status code if request was with empty body")
    void testWithEmptyPayload() {
        getRequestSpecification().header(X_TODO_TOKEN, USER_1.getToken()
                                                             .getValue());
        Response response = sendEmptyPayloadRequest();

        response.then()
                .statusCode(describedAs("response code should be 400", is(HTTP_BAD_REQUEST)));
    }
}
