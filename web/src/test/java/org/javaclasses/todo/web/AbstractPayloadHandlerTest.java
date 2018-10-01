package org.javaclasses.todo.web;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;

/**
 * Provides contract which allows to easily test if handler sub-class
 * process properly requests with invalid body.
 *
 * @author Oleg Barmin
 */
abstract class AbstractPayloadHandlerTest extends AbstractSecuredHandlerTest {

    /**
     * Sends request with empty body.
     *
     * @return response to requests with empty body
     */
    abstract Response sendEmptyPayloadRequest();

    @Test
    @DisplayName("response with 400 status code if request was with empty body")
    void testWithEmptyPayload() {
        setTokenToRequestSpecification(getRequestSpecification());

        Response response = sendEmptyPayloadRequest();

        response.then()
                .statusCode(describedAs("response code should be 400", is(HTTP_BAD_REQUEST)));
    }
}
