package org.javaclasses.todo.web;

import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.Username;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.AuthenticationController.authenticationMethodName;
import static org.javaclasses.todo.web.AuthenticationController.headerName;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

/**
 * Testing {@code AuthenticationHandler} which should allow users to sign-in into the system.
 *
 * @author Oleg Barmin
 */
@DisplayName("AuthenticationHandler should")
class AuthenticationHandlerTest extends AbstractHandlerTest {

    private final SampleUser bob = getBob();
    private final RequestSpecification specification = getNewSpecification();

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
     * Encodes given username and password to base64.
     *
     * <p>Credentials are encoded in format: username:password
     *
     * @param username username to encode
     * @param password password to encode
     * @return encoded credentials
     */
    private static String getEncodedCredentials(Username username, Password password) {
        String credentials = format("%s:%s", username.getValue(), password.getValue());
        return encode(credentials);
    }

    /**
     * Creates String with valid value of {@code X_Todo_Token}.
     *
     * @param actor actor with whose credentials value of header will be created.
     * @return string with header value
     */
    private static String getAuthenticationHeaderValue(Username username, Password password) {
        return authenticationMethodName() + ' ' + getEncodedCredentials(username, password);
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        getTestApplicationEnv().registerUser(bob);

        specification.header(headerName(), getAuthenticationHeaderValue(bob.getUsername(),
                                                                        bob.getPassword()))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(HTTP_OK)
                     .body(describedAs("provide token, but it don't.", notNullValue(Token.class)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        String userPasswordValue = bob.getPassword()
                                      .getValue();

        Password invalidPassword = new Password(userPasswordValue + "_invalidPassWord13_");

        specification.header(headerName(), getAuthenticationHeaderValue(bob.getUsername(),
                                                                        invalidPassword))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("forbid requests without Authentication header.")
    void testInvalidHeader() {
        specification.header("WRONG_HEADER",
                             getAuthenticationHeaderValue(bob.getUsername(),
                                                          bob.getPassword()))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        //invalid authentication scheme
        specification.header(headerName(),
                             "INVALID " + getEncodedCredentials(bob.getUsername(),
                                                                bob.getPassword()))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        //missing space after authentication scheme
        specification.header(headerName(),
                             "INVALID" + getEncodedCredentials(bob.getUsername(), bob.getPassword()))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(describedAs("responds with status code 400 " +
                                                     "when space is absent after scheme name.",
                                             is(HTTP_BAD_REQUEST)));
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials format.")
    void testInvalidDecodedCredentialsFormat() {
        String invalidFormatCredentials = format("%s:%s:%s",
                                                 bob.getUsername()
                                                    .getValue(),
                                                 bob.getPassword()
                                                    .getValue(),
                                                 "invalid");

        //invalid credentials format
        specification.header(headerName(),
                             "Basic " + encode(invalidFormatCredentials))
                     .when()
                     .post(getAuthenticationRoute())
                     .then()
                     .statusCode(describedAs("responds with status code 400" +
                                                     "when encoded credentials " +
                                                     "in invalid format.", is(HTTP_BAD_REQUEST)));
    }
}