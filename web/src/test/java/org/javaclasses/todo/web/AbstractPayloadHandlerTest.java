package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;

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
     * @param requestBody   body of request to send
     * @param specification specification to send request with
     * @return response to requests with empty body
     */
    abstract Response sendRequestWithBody(String requestBody,
                                          RequestSpecification specification);

    @Test
    @DisplayName("response with 500 status code if request was with empty body.")
    void testWithEmptyPayload() {
        RequestSpecification specification = getRequestSpecificationFor(getAlice());

        sendRequestWithBody("", specification).then()
                                              .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 500 status code if request body is invalid JSON.")
    void testWithInvalidJson() {
        RequestSpecification specification = getRequestSpecificationFor(getAlice());

        sendRequestWithBody("invalid JSON", specification).then()
                                                          .statusCode(HTTP_INTERNAL_ERROR);
    }
}
