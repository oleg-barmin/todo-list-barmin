package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler.AUTHENTICATION_HEADER;
import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler.AUTHENTICATION_SCHEME;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.TodoListApplication.AUTHENTICATION_ROUTE;

@DisplayName("AuthenticationController should")
class AuthenticationHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    private String getBase64EncodedCredentials(Password password) {
        String credentials = String.format("%s:%s", username.getValue(), password.getValue());
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);

        return base64EncodedCredentials;
    }


    private String getAuthenticationHeaderValue(Password password) {
        return AUTHENTICATION_SCHEME + ' ' + getBase64EncodedCredentials(password);
    }

    private String getBase64EncodedInvalidFormatCredentials() {
        String credentials = String.format("%s:%s:%s", username.getValue(), password.getValue(), "invalid");
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);
        return base64EncodedCredentials;
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        Response response = specification
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue(password))
                .when()
                .post(AUTHENTICATION_ROUTE);


        Token token = null;
        if (!response.body().asString().isEmpty()) {
            token = new Gson().fromJson(response.body().asString(), Token.class);
        }

        Assertions.assertEquals(200, response.getStatusCode(),
                "responds with status code 200, but it don't.");
        Assertions.assertNotNull(token, "provide token, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        Password invalidPassword = new Password("_invalidPassWord13_");

        Response response = specification
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue(invalidPassword))
                .when()
                .post(AUTHENTICATION_ROUTE);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when got invalid credentials, but it don't.");
    }

    @Test
    @DisplayName("unauthorize requests without Authentication header.")
    void testInvalidHeader() {
        Response response = specification
                .header("WRONG_HEADER", getAuthenticationHeaderValue(password))
                .when()
                .post(AUTHENTICATION_ROUTE);

        Assertions.assertEquals(401, response.getStatusCode(),
                "responds with status code 401, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        //invalid authentication scheme
        Response response = specification
                .header(AUTHENTICATION_HEADER, "INVALID " + getBase64EncodedCredentials(password))
                .when()
                .post(AUTHENTICATION_ROUTE);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when invalid occurs scheme in Authentication header, " +
                        "but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        //missing space after authentication scheme
        Response response = specification
                .header(AUTHENTICATION_HEADER, "INVALID" + getBase64EncodedCredentials(password))
                .when()
                .post(AUTHENTICATION_ROUTE);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when space is absent after scheme name.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials format.")
    void testInvalidDecodedCredentialsFormat() {
        //invalid credentials format
        Response response = specification
                .header(AUTHENTICATION_HEADER, "Basic " + getBase64EncodedInvalidFormatCredentials())
                .when()
                .post(AUTHENTICATION_ROUTE);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when encoded credentials in invalid format.");
    }
}