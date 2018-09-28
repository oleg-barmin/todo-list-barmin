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
    private static final String AUTHENTICATION_SCHEME = "Basic";

    private AuthenticationController() {
    }

    static String headerName() {
        return AUTHENTICATION_HEADER;
    }

    static String schemeName() {
        return AUTHENTICATION_SCHEME;
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
        Response process(RequestData<Void> requestData) {
            String authorizationHeader = requestData.getRequestHeaders()
                                                    .getHeaderValue(AUTHENTICATION_HEADER);

            if (authorizationHeader == null) {
                return Response.unauthorize();
            }

            String[] schemeAndCredentials = authorizationHeader.split(" ");

            if (schemeAndCredentials.length != 2) {
                return Response.badRequest();
            }

            if (!schemeAndCredentials[0].equals(AUTHENTICATION_SCHEME)) {
                return Response.unauthorize();
            }

            String base64EncodedCredentials = schemeAndCredentials[1];

            String decodedCredentials = new String(Base64.getDecoder()
                                                         .decode(base64EncodedCredentials), US_ASCII);

            String[] usernameAndPassword = decodedCredentials.split(":");

            if (usernameAndPassword.length != 2) {
                return Response.badRequest();
            }

            Username username = new Username(usernameAndPassword[0]);
            Password password = new Password(usernameAndPassword[1]);

            Token token = authentication.signIn(username, password);

            return Response.ok(objectToJson(token));
        }
    }

}
