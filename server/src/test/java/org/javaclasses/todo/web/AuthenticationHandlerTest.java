package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.javaclasses.todo.web.AuthenticationHandler.AUTHENTICATION_HEADER;
import static org.javaclasses.todo.web.AuthenticationHandler.AUTHENTICATION_SCHEME;
import static org.javaclasses.todo.web.TodoListApplication.AUTHENTICATION_PATH;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@DisplayName("AuthenticationHandler should")
class AuthenticationHandlerTest {
    private static final String emptyResponseBodyMsg = "responds with status code empty, but it don't.";
    private static final String forbiddenAssertMsg = "responds with status code 403, but it don't.";
    private final Authentication authenticationMock = MockServiceFactory.getAuthenticationMock();
    private final Username username = new Username("exmaple@temp.se");
    private final Password password = new Password("PasWord123");

    private static String getHeaderValue(String encodedCredentials) {
        return AUTHENTICATION_SCHEME + ' ' + encodedCredentials;
    }

    private static Token tokenFromJson(String json) {
        return new Gson().fromJson(json, Token.class);
    }

    @AfterEach
    void resetMocks() {
        reset(authenticationMock);
    }

    private String getBase64EncodedCredentials() {
        String credentials = String.format("%s:%s", username.getValue(), password.getValue());
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);

        return base64EncodedCredentials;
    }

    private String getAuthenticationHeaderValue() {
        return getHeaderValue(getBase64EncodedCredentials());
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        Token expectedToken = new Token(UUID.randomUUID().toString());
        when(authenticationMock.signIn(username, password)).thenReturn(expectedToken);

        Response response = given()
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(200, response.getStatusCode(),
                "responds with status code 200, but it don't.");
        Assertions.assertEquals(expectedToken, tokenFromJson(response.asString()),
                "provide token, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        when(authenticationMock.signIn(username, password))
                .thenThrow(new InvalidCredentialsException());

        Response response = given()
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertTrue(response.asString().isEmpty(), emptyResponseBodyMsg);
        Assertions.assertEquals(403, response.getStatusCode(), forbiddenAssertMsg);
    }

    @Test
    @DisplayName("unauthorize requests without Authentication header.")
    void testInvalidHeader() {
        Response response = given()
                .header("WRONG_HEADER", getAuthenticationHeaderValue())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertTrue(response.asString().isEmpty(), emptyResponseBodyMsg);
        Assertions.assertEquals(401, response.getStatusCode(),
                "responds with status code 401, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        //invalid authentication scheme
        Response response = given()
                .header(AUTHENTICATION_HEADER, "INVALID " + getBase64EncodedCredentials())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertTrue(response.asString().isEmpty(), emptyResponseBodyMsg);
        Assertions.assertEquals(403, response.getStatusCode(), forbiddenAssertMsg);
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        //missing space after authentication scheme
        Response response = given()
                .header(AUTHENTICATION_HEADER, "INVALID" + getBase64EncodedCredentials())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertTrue(response.asString().isEmpty(), emptyResponseBodyMsg);
        Assertions.assertEquals(403, response.getStatusCode(), forbiddenAssertMsg);
    }

    private String getBase64EncodedInvalidFormatCredentials() {
        String credentials = String.format("%s:%s:%s", username.getValue(), password.getValue(), "invalid");
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);

        return base64EncodedCredentials;
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials format.")
    void testInvalidDecodedCredentialsFormat() {
        //invalid credentials format
        Response response = given()
                .header(AUTHENTICATION_HEADER, getBase64EncodedInvalidFormatCredentials())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertTrue(response.asString().isEmpty(), emptyResponseBodyMsg);
        Assertions.assertEquals(403, response.getStatusCode(), forbiddenAssertMsg);
    }


}