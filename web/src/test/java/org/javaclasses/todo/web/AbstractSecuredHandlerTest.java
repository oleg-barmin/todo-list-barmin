package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.web.given.SignedInActor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.getXTodoToken;

/**
 * Abstract class which allows to sub-classes to test if their operation is
 * secured.
 *
 * @author Oleg Barmin
 */
abstract class AbstractSecuredHandlerTest extends AbstractHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    abstract Response sendRequest(UserId userId);

    @Override
    RequestSpecification getNewSpecification() {
        RequestSpecification newSpecification = super.getNewSpecification();
        setTokenToRequestSpecification(newSpecification);
        return newSpecification;
    }

    /**
     * Sets a valid token of random registered user
     * to {@link AbstractHandlerTest#specification request specification}.
     *
     * @param requestSpecification request specification to set token into
     */
    void setTokenToRequestSpecification(RequestSpecification requestSpecification) {
        SignedInActor actor = getTestEnvironment().createAndSignInActor();
        requestSpecification.header(getXTodoToken(), actor.getToken()
                                                          .getValue());
    }

    @Test
    @DisplayName("forbid operation to unauthorized users.")
    void testForbidOperation() {
        SignedInActor actor = getTestEnvironment().createAndSignInActor();

        Token token = new Token(actor.getToken()
                                     .getValue() + "invalid token");

        specification.header(getXTodoToken(), token.getValue());

        sendRequest(actor.getUserId())
                .then()
                .statusCode(describedAs("response status must be 403, " +
                                                "when not signed in user creates list, but it don't.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Test
    @DisplayName("unauthorized if header with token is invalid.")
    void testUnauthorizedOperation() {
        SignedInActor actor = getTestEnvironment().createAndSignInActor();

        specification.header("INVALID_HEADER", actor.getToken()
                                                    .getValue());

        sendRequest(actor.getUserId())
                .then()
                .statusCode(describedAs("response status must be 401, when attempt to " +
                                                "create list with invalid token header, but it don't.",
                                        is(HTTP_UNAUTHORIZED)));
    }
}
