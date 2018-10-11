package org.javaclasses.todo.web;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Test of equals and hashcode methods of {@link HttpResponse}.
 *
 * @author Oleg Barmin
 */
@DisplayName("HttpResponse should")
class HttpResponseTest {

    @DisplayName("perform equals and hashcode methods correctly.")
    @Test
    void testEqualsAndHashcode() {
        new EqualsTester().addEqualityGroup(HttpResponse.ok(), HttpResponse.ok())
                          .testEquals();
    }

    // If test performs properly NullPointerException will be throw, so return value won't be received.
    // null is given in test needs.
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @Test
    @DisplayName("throw NullPointerException when given body is null.")
    void testNullBody() {
        Assertions.assertThrows(NullPointerException.class, () -> HttpResponse.ok(null));
    }

    @Test
    @DisplayName("create instance with desired 200 code and desired body.")
    void testCreateInstanceWith200StatusCode() {
        String body = "response body.";

        ResponseBody<String> responseBody = ResponseBody.of(body);

        HttpResponse ok = HttpResponse.ok(body);
        Assertions.assertEquals(HTTP_OK, ok.getCode());
        Assertions.assertEquals(responseBody, ok.getBody());
    }

    @Test
    @DisplayName("create instance with desired status code and without body.")
    void testCreateInstanceWithStatusCodeAndEmptyBody() {
        HttpResponse ok = HttpResponse.ok();
        Assertions.assertEquals(HTTP_OK, ok.getCode());
        Assertions.assertEquals(ResponseBody.empty(), ok.getBody());

        HttpResponse badRequest = HttpResponse.badRequest();
        Assertions.assertEquals(HTTP_BAD_REQUEST, badRequest.getCode());
        Assertions.assertEquals(ResponseBody.empty(), badRequest.getBody());

        HttpResponse unauthorized = HttpResponse.unauthorized();
        Assertions.assertEquals(HTTP_UNAUTHORIZED, unauthorized.getCode());
        Assertions.assertEquals(ResponseBody.empty(), unauthorized.getBody());

        HttpResponse forbidden = HttpResponse.forbidden();
        Assertions.assertEquals(HTTP_FORBIDDEN, forbidden.getCode());
        Assertions.assertEquals(ResponseBody.empty(), forbidden.getBody());

        HttpResponse internalError = HttpResponse.internalError();
        Assertions.assertEquals(HTTP_INTERNAL_ERROR, internalError.getCode());
        Assertions.assertEquals(ResponseBody.empty(), internalError.getBody());
    }
}