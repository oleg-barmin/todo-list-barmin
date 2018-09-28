package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestUsers.UN_SINGED_IN_USER;
import static org.javaclasses.todo.web.TestUsers.USER_1;

abstract class AbstractSecuredHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    abstract Response sendRequest(Token token, UserId userId);

    @Test
    @DisplayName("forbid operation to unauthorized users.")
    void testForbidOperation() {
        Token token = new Token("invalid token");
        specification.header(X_TODO_TOKEN, token.getValue());

        sendRequest(token, UN_SINGED_IN_USER.getUserId())
                .then()
                .statusCode(describedAs("response status must be 403, " +
                                                "when not signed in user creates list, but it don't.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedOperation() {
        specification.header("INVALID_HEADER", USER_1.getToken()
                                                     .getValue());

        sendRequest(USER_1.getToken(), USER_1.getUserId())
                .then()
                .statusCode(describedAs("response status must be 401, when attempt to " +
                                                "create list with invalid token header, but it don't.",
                                        is(HTTP_UNAUTHORIZED)));
    }
}
