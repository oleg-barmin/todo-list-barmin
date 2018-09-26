package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Token;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Secured request handler, which verifies {@link Token} in header of Request.
 *
 * @param <P> payload of request
 */
abstract class SecuredAbstractRequestHandler<P> extends AbstractRequestHandler<P> {
    static final String X_TODO_TOKEN = "X-Todo-Token";

    /**
     * Creates {@code SecuredAbstractRequestHandler} instance.
     *
     * @param payloadClass class of payload.
     */
    SecuredAbstractRequestHandler(Class<P> payloadClass) {
        super(payloadClass);
    }

    @Override
    Answer process(RequestData<P> requestData) {
        String headerValue = requestData.getRequestHeaders().getHeaderValue(X_TODO_TOKEN);

        if (headerValue == null) {
            return new Answer(HTTP_UNAUTHORIZED);
        }

        Token token = new Token(headerValue);

        return processVerifiedRequest(requestData, token);
    }

    /**
     * Handles verified request.
     *
     * @param requestData data of received request
     * @param token       token of user who
     * @return answer to received request
     */
    abstract Answer processVerifiedRequest(RequestData<P> requestData, Token token);
}
