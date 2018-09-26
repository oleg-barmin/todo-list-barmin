package org.javaclasses.todo.web;

import io.restassured.response.Response;
import org.javaclasses.todo.model.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractHandler.X_TODO_TOKEN;

abstract class AbstractSecuredHandlerTest extends AbstractHandlerTest {
    abstract Response performOperation(Token token);

    @Test
    @DisplayName("forbid operation to unauthorized users.")
    void testForbidOperation() {
        String invalidToken = "invalid token";
        getRequestSpecification().header(X_TODO_TOKEN, invalidToken);

        Response response = performOperation(new Token(invalidToken));

        Assertions.assertEquals(HTTP_FORBIDDEN, response.getStatusCode(),
                "response status must be 403, when not signed in user creates list, but it don't.");
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedOperation() {
        Token token = signIn(USER_1.getUsername(), USER_1.getPassword());

        getRequestSpecification().header("INVALID_HEADER", token.getValue());
        Response response = performOperation(token);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, response.getStatusCode(),
                "response status must be 401, " +
                        "when attempt to create list with invalid token header, but it don't.");
    }
}
