package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Token;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

abstract class SecuredAbstractHandler<P> extends AbstractRequestHandler<P> {
    static final String X_TODO_TOKEN = "X-Todo-Token";

    /**
     * Creates {@code AbstractRequestHandler} instance.
     *
     * @param payloadClass class of payload.
     */
    SecuredAbstractHandler(Class<P> payloadClass) {
        super(payloadClass);
    }

    @Override
    Answer process(RequestData<P> requestData) {
        String headerValue = requestData.getRequestHeaders().getHeaderValue(X_TODO_TOKEN);

        if (headerValue == null) {
            return new Answer(HTTP_UNAUTHORIZED);
        }

        Token token = new Token(headerValue);

        return securedProcess(requestData, token);
    }

    /**
     * Handles secured request.
     *
     * @param requestData data of received request
     * @param token       token of user who
     * @return answer to received request
     */
    abstract Answer securedProcess(RequestData<P> requestData, Token token);
}
