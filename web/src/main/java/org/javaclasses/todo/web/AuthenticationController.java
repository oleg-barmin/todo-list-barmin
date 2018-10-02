package org.javaclasses.todo.web;

import com.google.common.base.Splitter;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;

import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

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
     * Handlers user authentication and provides user token if given credentials are valid.
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
         * @return Answers:
         * - 401 if request has no {@code Authentication} header;
         * - 403 if {@code Authentication} header in invalid format
         * - 403 if given credentials is invalid
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
                return HttpResponse.unauthorized();
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
}
