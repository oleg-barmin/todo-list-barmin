package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

@DisplayName("TokenValidationHandler should")
class TokenValidationHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification bobSpecification = getRequestSpecificationFor(getBob());

    @Test
    @DisplayName("validate token of signed in users.")
    void testValidateLogin() {
        bobSpecification.get(getAuthenticationRoute())
                        .then()
                        .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("unauthorize operation to users which signed out.")
    void testValidateLoginOfNonSignedInUser() {
        bobSpecification.delete(getAuthenticationRoute());

        bobSpecification.get(getAuthenticationRoute())
                        .then()
                        .statusCode(HTTP_UNAUTHORIZED);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        return specification.get(getAuthenticationRoute());
    }

    @Override
    @Test
    @DisplayName("unauthorize operation for user which did not provide token header.")
    void testForbidOperation() {
        sendRequest(getNewSpecification())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }
}
