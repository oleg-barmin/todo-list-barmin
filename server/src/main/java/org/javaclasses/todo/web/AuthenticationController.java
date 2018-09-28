package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Process user authentication requests.
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
    static class AuthenticationHandler extends AbstractRequestHandler<Void> {

        private final Authentication authentication;

        /**
         * Creates {@code AuthenticationHandler} instance.
         *
         * @param authentication authentication service to work with
         */
        AuthenticationHandler(Authentication authentication) {
            super(Void.class);
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
        @Override
        HttpResponse process(RequestData<Void> requestData) {
            String authorizationHeader = requestData.getRequestHeaders()
                                                    .getHeaderValue(AUTHENTICATION_HEADER);

            if (authorizationHeader == null) {
                return HttpResponse.unauthorize();
            }

            String[] schemeAndCredentials = authorizationHeader.split(" ");

            if (schemeAndCredentials.length != 2) {
                return HttpResponse.badRequest();
            }

            if (!schemeAndCredentials[0].equals(AUTHENTICATION_METHOD)) {
                return HttpResponse.unauthorize();
            }

            String base64EncodedCredentials = schemeAndCredentials[1];

            String decodedCredentials = new String(Base64.getDecoder()
                                                         .decode(base64EncodedCredentials), US_ASCII);

            String[] usernameAndPassword = decodedCredentials.split(":");

            if (usernameAndPassword.length != 2) {
                return HttpResponse.badRequest();
            }

            Username username = new Username(usernameAndPassword[0]);
            Password password = new Password(usernameAndPassword[1]);

            Token token = authentication.signIn(username, password);

            return HttpResponse.ok(objectToJson(token));
        }
    }

}
