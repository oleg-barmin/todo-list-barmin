package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.getXTodoToken;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;

/**
 * Abstract class which allows to sub-classes to test if their operation is
 * secured.
 *
 * @author Oleg Barmin
 */
abstract class AbstractSecuredHandlerTest extends AbstractHandlerTest {

    abstract Response sendRequest(RequestSpecification specification);

    /**
     * Creates {@code RequestSpecification} for given {@code SampleUser}.
     *
     * <p>To send any requests to REST API of application request specification should be created
     * for user to be able to access secured pages.
     *
     * @param user user to get specification of
     * @return specification of given user
     */
    RequestSpecification getRequestSpecificationFor(SampleUser user) {
        RequestSpecification specification = getNewSpecification();

        getTestApplicationEnv().registerUser(user);
        Token token = getTestApplicationEnv().signInUser(user);
        specification.header(getXTodoToken(), token.getValue());

        return specification;
    }

    @Test
    @DisplayName("forbid operation to unauthenticated users.")
    void testForbidOperation() {
        RequestSpecification specification = getNewSpecification();
        specification.header(getXTodoToken(), new Token("unregistered token"));

        sendRequest(specification).then()
                                  .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedOperation() {
        SampleUser alice = getAlice();
        RequestSpecification specification = getNewSpecification();
        getTestApplicationEnv().registerUser(alice);

        Token token = getTestApplicationEnv().signInUser(alice);

        specification.header("INVALID_HEADER", token);
        sendRequest(specification).then()
                                  .statusCode(HTTP_UNAUTHORIZED);
    }

}
