package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.Username;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Handlers user authentication and provides user token if given credentials are valid.
 *
 * <p>Authentication is based on 'Basis authentication scheme'.
 */
class AuthenticationHandler extends AbstractRequestHandler<EmptyPayload> {
    static final String AUTHENTICATION_HEADER = "Authentication";
    static final String AUTHENTICATION_SCHEME = "Basic";
    private final Authentication authentication;


    /**
     * Creates {@code AuthenticationHandler} instance.
     *
     * @param authentication authentication service to work with
     */
    AuthenticationHandler(Authentication authentication) {
        super(EmptyPayload.class);
        this.authentication = authentication;
    }

    /**
     * Processes authentication requests.
     *
     * @param payload payload of request
     * @param params  parameters of request
     * @param headers headers of request
     * @return Answers:
     * - 401 if request has no 'Authentication' header;
     * - 403 if 'Authentication' header in invalid format
     * - 403 if given credentials is invalid
     * - 200 with token if user was signed.
     */
    @Override
    Answer process(@Nullable EmptyPayload payload, RequestParams params, RequestHeaders headers) {
        String authorizationHeader = headers.getHeaderValue(AUTHENTICATION_HEADER);

        if (authorizationHeader == null) {
            return new Answer(HTTP_UNAUTHORIZED);
        }

        String[] authenticationValue = authorizationHeader.split(" ");

        if (authenticationValue.length != 2) {
            return Answer.forbidden();
        }

        if (!authenticationValue[0].equals(AUTHENTICATION_SCHEME)) {
            return Answer.forbidden();
        }

        String base64EncodedCredentials = authenticationValue[1];
        String decodedCredentials = new String(Base64.getDecoder().decode(base64EncodedCredentials), StandardCharsets.UTF_8);
        String[] credentials = decodedCredentials.split(":");

        if (credentials.length != 2) {
            return Answer.forbidden();
        }

        Username username = new Username(credentials[0]);
        Password password = new Password(credentials[1]);

        Token token;

        try {
            token = authentication.signIn(username, password);
        } catch (InvalidCredentialsException e) {
            return Answer.forbidden();
        }

        return Answer.ok(objectToJson(token));
    }
}
