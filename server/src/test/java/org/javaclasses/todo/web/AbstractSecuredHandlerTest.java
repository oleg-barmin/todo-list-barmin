package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestUsers.UN_SINGED_IN_USER;
import static org.javaclasses.todo.web.TestUsers.USER_1;

abstract class AbstractSecuredHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    abstract Response sendRequest(Token token, UserId userId);

    @Test
    @DisplayName("forbid operation to unauthorized users.")
    void testForbidOperation() {
        String invalidToken = "invalid token";
        specification.header(X_TODO_TOKEN, invalidToken);

        Response response = sendRequest(new Token(invalidToken), UN_SINGED_IN_USER.getUserId());

        Assertions.assertEquals(HTTP_FORBIDDEN, response.getStatusCode(),
                "response status must be 403, when not signed in user creates list, but it don't.");
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedOperation() {
        specification.header("INVALID_HEADER", USER_1.getToken().getValue());
        Response response = sendRequest(USER_1.getToken(), USER_1.getUserId());

        Assertions.assertEquals(HTTP_UNAUTHORIZED, response.getStatusCode(),
                "response status must be 401, " +
                        "when attempt to create list with invalid token header, but it don't.");
    }
}
