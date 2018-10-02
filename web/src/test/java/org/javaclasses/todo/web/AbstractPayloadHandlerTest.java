package org.javaclasses.todo.web;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * Provides contract which allows to easily test if handler sub-class
 * process properly requests with invalid body.
 *
 * @author Oleg Barmin
 */
abstract class AbstractPayloadHandlerTest extends AbstractSecuredHandlerTest {

    /**
     * Sends request with given body.
     *
     * @param requestBody body of request to send
     * @return response to requests with empty body
     */
    abstract Response sendWithBodyRequest(String requestBody);

    @Test
    @DisplayName("response with 400 status code if request was with empty body.")
    void testWithEmptyPayload() {
        setTokenToRequestSpecification(getRequestSpecification());

        Response response = sendWithBodyRequest("");

        response.then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("response with 400 status code if request body is invalid JSON.")
    void testWithInvalidJson() {
        setTokenToRequestSpecification(getRequestSpecification());

        Response response = sendWithBodyRequest("invalid JSON");

        response.then()
                .statusCode(HTTP_BAD_REQUEST);
    }
}
