package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.ServiceFactory;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;
import org.junit.jupiter.api.*;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.javaclasses.todo.web.AuthenticationHandler.AUTHENTICATION_HEADER;
import static org.javaclasses.todo.web.AuthenticationHandler.AUTHENTICATION_SCHEME;
import static org.javaclasses.todo.web.TodoListApplication.AUTHENTICATION_PATH;
import static org.javaclasses.todo.web.TodoListApplication.PreRegisteredUsers.USER_1;

@DisplayName("AuthenticationHandler should")
class AuthenticationHandlerTest {

    private static final TodoListApplication todoListApplication = new TodoListApplication();

    private static final Username username = USER_1.getUsername();
    private static final Password password = USER_1.getPassword();

    private final RequestSpecification requestSpecification = given().port(4567);


    @BeforeAll
    static void startServer() {
        todoListApplication.start();
    }

    private static String getBase64EncodedCredentials(Password password) {
        String credentials = String.format("%s:%s", username.getValue(), password.getValue());
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);

        return base64EncodedCredentials;
    }

    private static String getHeaderValue(String encodedCredentials) {
        return AUTHENTICATION_SCHEME + ' ' + encodedCredentials;
    }

    private static String getAuthenticationHeaderValue(Password password) {
        return getHeaderValue(getBase64EncodedCredentials(password));
    }

    private static Token tokenFromJson(String json) {
        return new Gson().fromJson(json, Token.class);
    }

    private static String getBase64EncodedInvalidFormatCredentials() {
        String credentials = String.format("%s:%s:%s", username.getValue(), password.getValue(), "invalid");
        String base64EncodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes(UTF_8)), UTF_8);
        return base64EncodedCredentials;
    }

    /*
     * User ID is not needed in REST test.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    void registerUser() {
        Authentication authentication = ServiceFactory.getAuthentication();
        authentication.createUser(username, password);
    }

    @AfterEach
    void clearStorages() {
        ServiceFactory.clearStorages();
    }

    @Test
    @DisplayName("authenticate registered users and provide them `Token`s.")
    void testValidCredentials() {
        Response response = requestSpecification
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue(password))
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(200, response.getStatusCode(),
                "responds with status code 200, but it don't.");
        Assertions.assertNotNull(tokenFromJson(response.asString()), "provide token, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials.")
    void testInvalidCredentials() {
        Password invalidPassword = new Password("_invalidPassWord13_");

        Response response = requestSpecification
                .header(AUTHENTICATION_HEADER, getAuthenticationHeaderValue(invalidPassword))
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when got invalid credentials, but it don't.");
    }

    @Test
    @DisplayName("unauthorize requests without Authentication header.")
    void testInvalidHeader() {
        Response response = requestSpecification
                .header("WRONG_HEADER", getAuthenticationHeaderValue(password))
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(401, response.getStatusCode(),
                "responds with status code 401, but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid Authentication scheme.")
    void testInvalidAuthScheme() {
        //invalid authentication scheme
        Response response = requestSpecification
                .header(AUTHENTICATION_HEADER, "INVALID " + getBase64EncodedCredentials(password))
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when invalid occurs scheme in Authentication header, " +
                        "but it don't.");
    }

    @Test
    @DisplayName("forbid access for requests with absent space after authentication scheme.")
    void testInvalidHeaderValue() {
        //missing space after authentication scheme
        Response response = requestSpecification
                .header(AUTHENTICATION_HEADER, "INVALID" + getBase64EncodedCredentials(password))
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when space is absent after scheme name.");
    }

    @Test
    @DisplayName("forbid access for requests with invalid credentials format.")
    void testInvalidDecodedCredentialsFormat() {
        //invalid credentials format
        Response response = requestSpecification
                .header(AUTHENTICATION_HEADER, "Basic " + getBase64EncodedInvalidFormatCredentials())
                .when()
                .post(AUTHENTICATION_PATH);

        Assertions.assertEquals(403, response.getStatusCode(),
                "responds with status code 403 when encoded credentials in invalid format.");
    }


}