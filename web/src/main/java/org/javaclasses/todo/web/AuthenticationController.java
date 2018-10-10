package org.javaclasses.todo.web;

import com.google.common.base.Splitter;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.Username;

import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.getXTodoToken;

/**
 * Process user authentication requests.
 *
 * @author Oleg Barmin
 */
class AuthenticationController {

    private static final String AUTHENTICATION_HEADER = "Authentication";
    private static final String AUTHENTICATION_METHOD = "Basic";

    private AuthenticationController() {
    }

    /**
     * Provides Authentication header value.
     *
     * <p>Authentication header should be provided by user to authenticate into the system.
     *
     * <p>Value of Authentication header should be base64 encoded
     * user credentials (username and password divided by single colon), divided by space.
     *
     * <p>Encoded user credentials should be prepended with authentication method and space.
     *
     * <p>Example of Header and its value:
     * {@code Authentication: Basic bm90X2FfdXNlcjp3cm9uZ19wYXNzd29yZAo }
     *
     * @return name of Authentication header.
     */
    static String headerName() {
        return AUTHENTICATION_HEADER;
    }

    /**
     * Provides name of Authentication method.
     *
     * <p>In to-do list application by default it is Basic.
     *
     * @return name of Authentication method.
     */
    static String authenticationMethodName() {
        return AUTHENTICATION_METHOD;
    }

    /**
     * Handlers user authentication and provides user {@link Token} if given credentials are valid.
     *
     * <p>Authentication is based on <a href="https://bit.ly/2DV5cNC">Basis authentication scheme</a>.
     */
    static class AuthenticationHandler extends AbstractRequestHandler {

        private final Authentication authentication;

        /**
         * Creates {@code AuthenticationHandler} instance.
         *
         * @param authentication authentication service to work with
         */
        AuthenticationHandler(Authentication authentication) {
            this.authentication = authentication;
        }

        /**
         * Processes authentication requests.
         *
         * @param requestData data of request
         * @return Responses:
         * - 401 if request has no {@code Authentication} header or given credentials invalid;
         * - 400 if {@code Authentication} header in invalid format
         * - 200 with token if user was signed.
         */
        @SuppressWarnings("UnstableApiUsage") // splitToList should be used because of errorprompt advice
        @Override
        HttpResponse process(RequestData requestData) {
            String authorizationHeader = requestData.getRequestHeaders()
                                                    .getHeaderValue(AUTHENTICATION_HEADER);

            if (authorizationHeader == null) {
                return HttpResponse.unauthorized();
            }

            List<String> schemeAndCredentials = Splitter.on(' ')
                                                        .splitToList(authorizationHeader);

            if (schemeAndCredentials.size() != 2) {
                return HttpResponse.badRequest();
            }

            if (!schemeAndCredentials.get(0)
                                     .equals(AUTHENTICATION_METHOD)) {
                return HttpResponse.badRequest();
            }

            String base64EncodedCredentials = schemeAndCredentials.get(1);

            CharSequence decodedCredentials = new String(Base64.getDecoder()
                                                               .decode(base64EncodedCredentials), US_ASCII);

            List<String> usernameAndPassword = Splitter.on(':')
                                                       .splitToList(decodedCredentials);

            if (usernameAndPassword.size() != 2) {
                return HttpResponse.badRequest();
            }

            Username username = new Username(usernameAndPassword.get(0));
            Password password = new Password(usernameAndPassword.get(1));

            Token token = authentication.signIn(username, password);

            return HttpResponse.ok(token);
        }
    }

    /**
     * Handles signs out user request.
     *
     * <p>To sign out user should provide {@link SecuredAbstractRequestHandler#X_TODO_TOKEN} with
     * value of token.
     *
     * @author Oleg Barmin
     */
    static class SingOutHandler extends SecuredAbstractRequestHandler {

        private final Authentication authentication;

        /**
         * Creates {@code SingOutHandler} instance.
         *
         * @param authentication authentication service to work with
         */
        SingOutHandler(Authentication authentication) {
            this.authentication = authentication;
        }

        /**
         * Processes user sign out request.
         *
         * @param requestData data of received request
         * @param token       token of user who sent request
         * @return {@code HttpResponse} with 200 status code if request was processed successfully.
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            authentication.signOut(token);
            return HttpResponse.ok();
        }
    }

    /**
     * Validates if token given in request is still active.
     */
    static class TokenValidationHandler extends AbstractRequestHandler {

        private final Authentication authentication;

        /**
         * Creates {@code TokenValidationHandler} instance.
         *
         * @param authentication authentication service to work with
         */
        TokenValidationHandler(Authentication authentication) {
            this.authentication = authentication;
        }

        /**
         * Validates token given in request header.
         *
         * @param requestData data of request
         * @return {@code HttpResponse} with 401 status code if:
         * - no token header was provided;
         * - given token is non-active.
         * If token is valid returns response with 200 status code.
         */
        /*
         * Method validates token, so if during token validation no exception was throw, then token is valid.
         */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        HttpResponse process(RequestData requestData) {
            String headerValue = requestData.getRequestHeaders()
                                            .getHeaderValue(getXTodoToken());

            if (headerValue == null) {
                return HttpResponse.unauthorized();
            }

            try {
                authentication.validate(new Token(headerValue));
            } catch (AuthorizationFailedException exception) {
                return HttpResponse.unauthorized();
            }
            return HttpResponse.ok();
        }
    }
}
