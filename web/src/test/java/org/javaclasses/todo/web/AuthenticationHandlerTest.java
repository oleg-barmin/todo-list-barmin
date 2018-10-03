package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.Username;
import org.javaclasses.todo.web.given.Actor;
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

/**
 * Testing {@code AuthenticationHandler} which should allow users to sign-in into the system.
 *
 * @author Oleg Barmin
 */
@DisplayName("AuthenticationHandler should")
class AuthenticationHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    /**
     * Encodes given string to base64.
     *
     * @param stringToEncode string to encode
     * @return encoded string
     * @implNote uses {@link Base64} to encode string
     */
    private static String encode(String stringToEncode) {
        return new String(Base64.getEncoder()
                                .encode(stringToEncode.getBytes(US_ASCII)), US_ASCII);
    }

    /**
     * Encodes username and password of given {@link Actor} to base64.
     *
     * <p>Credentials are encoded in format: username:password
     *
     * @param actor actor whose credentials to encode
     * @return encoded credentials of given actor
     */
    private static String getEncodedCredentials(Actor actor) {
        String credentials = String.format("%s:%s", actor.getUsername()
                                                         .getValue(), actor.getPassword()
                                                                           .getValue());

        return encode(credentials);
    }

    /**
     * Creates String with valid value of {@code X_Todo_Token}.
     *
     * @param actor actor with whose credentials value of header will be created.
     * @return string with header value
     */
    private static String getAuthenticationHeaderValue(Actor actor) {
        return authenticationMethodName() + ' ' + getEncodedCredentials(actor);
    }

    /**
     * Get encoded credentials in invalid format.
     *
     * <p>Format: username:password:invalid.
     *
     * @param username username
     * @param password password
     * @return invalid encoded credentials
     */
    private static String getEncodedInvalidFormatCredentials(Username username, Password password) {
        String credentials = String.format("%s:%s:%s",
                                           username.getValue(),
                                           password.getValue(),
                                           "invalid");

        return encode(credentials);
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        Actor actor = getTestEnvironment().registerUser();

        Response response = specification
                .header(headerName(), getAuthenticationHeaderValue(actor))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(
                        describedAs("responds with status code 200, but it don't.", is(HTTP_OK)))
                .body(describedAs("provide token, but it don't.", notNullValue(Token.class)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        Actor actor = getTestEnvironment().registerUser();
        String actorPasswordValue = actor.getPassword()
                                         .getValue();

        Password invalidPassword = new Password(actorPasswordValue + "_invalidPassWord13_");

        Actor invalidCredentialsActor = new Actor(actor.getUserId(), actor.getUsername(), invalidPassword);

        Response response = specification
                .header(headerName(), getAuthenticationHeaderValue(invalidCredentialsActor))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("forbid requests without Authentication header.")
    void testInvalidHeader() {
        Actor actor = getTestEnvironment().registerUser();

        Response response = specification
                .header("WRONG_HEADER",
                        getAuthenticationHeaderValue(actor))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 401, " +
                                                "but it don't.", is(HTTP_UNAUTHORIZED)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        Actor actor = getTestEnvironment().registerUser();
        //invalid authentication scheme
        Response response = specification
                .header(headerName(),
                        "INVALID " + getEncodedCredentials(actor))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        Actor actor = getTestEnvironment().registerUser();
        //missing space after authentication scheme
        Response response = specification
                .header(headerName(),
                        "INVALID" + getEncodedCredentials(actor))
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
        Actor actor = getTestEnvironment().registerUser();

        //invalid credentials format
        Response response = specification
                .header(headerName(),
                        "Basic " + getEncodedInvalidFormatCredentials(actor.getUsername(),
                                                                      actor.getPassword()))
                .when()
                .post(getAuthenticationRoute());

        response.then()
                .statusCode(describedAs("responds with status code 400" +
                                                "when encoded credentials " +
                                                "in invalid format.", is(HTTP_BAD_REQUEST)));
    }
}