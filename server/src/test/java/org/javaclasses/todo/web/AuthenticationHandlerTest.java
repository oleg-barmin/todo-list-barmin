package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.AuthenticationController.authenticationMethodName;
import static org.javaclasses.todo.web.AuthenticationController.headerName;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.TestUsers.USER_1;

/**
 * @author Oleg Barmin
 */
@DisplayName("AuthenticationController should")
class AuthenticationHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    private static String getBase64EncodedCredentials(Password password) {
        String credentials = String.format("%s:%s", USER_1.getUsername()
                                                          .getValue(), password.getValue());
        String base64EncodedCredentials = new String(Base64.getEncoder()
                                                           .encode(credentials.getBytes(US_ASCII)), US_ASCII);

        return base64EncodedCredentials;
    }

    private static String getAuthenticationHeaderValue(Password password) {
        return authenticationMethodName() + ' ' + getBase64EncodedCredentials(password);
    }

    private static String getBase64EncodedInvalidFormatCredentials() {
        String credentials = String.format("%s:%s:%s",
                                           USER_1.getUsername()
                                                 .getValue(),
                                           USER_1.getPassword()
                                                 .getValue(),
                                           "invalid");

        String base64EncodedCredentials = new String(Base64.getEncoder()
                                                           .encode(credentials.getBytes(US_ASCII)), US_ASCII);
        return base64EncodedCredentials;
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        Response response = specification
                .header(headerName(), getAuthenticationHeaderValue(USER_1.getPassword()))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 200, but it don't.", is(HTTP_OK)))
                .body(describedAs("provide token, but it don't.", notNullValue(Token.class)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        Password invalidPassword = new Password("_invalidPassWord13_");

        Response response = specification
                .header(headerName(), getAuthenticationHeaderValue(invalidPassword))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 403 " +
                                                "when got invalid credentials, but it don't.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Test
    @DisplayName("unauthorize requests without Authentication header.")
    void testInvalidHeader() {
        Response response = specification
                .header("WRONG_HEADER", getAuthenticationHeaderValue(USER_1.getPassword()))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 401, " +
                                                "but it don't.", is(HTTP_UNAUTHORIZED)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        //invalid authentication scheme
        Response response = specification
                .header(headerName(), "INVALID " + getBase64EncodedCredentials(USER_1.getPassword()))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 401 when " +
                                                "invalid occurs scheme in Authentication header, but it don't.",
                                        is(HTTP_UNAUTHORIZED)));
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        //missing space after authentication scheme
        Response response = specification
                .header(headerName(),
                        "INVALID" + getBase64EncodedCredentials(USER_1.getPassword()))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 400 " +
                                                "when space is absent after scheme name.",
                                        is(HTTP_BAD_REQUEST)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials format.")
    void testInvalidDecodedCredentialsFormat() {
        //invalid credentials format
        Response response = specification
                .header(headerName(), "Basic " + getBase64EncodedInvalidFormatCredentials())
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 400" +
                                                "when encoded credentials " +
                                                "in invalid format.", is(HTTP_BAD_REQUEST)));
    }
}