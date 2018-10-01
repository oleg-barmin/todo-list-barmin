package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Token;

/**
 * Secured request handler, which verifies {@link Token} in header of Request.
 *
 * @author Oleg Barmin
 */
abstract class SecuredAbstractRequestHandler extends AbstractRequestHandler {

    static final String X_TODO_TOKEN = "X-Todo-Token";

    @Override
    HttpResponse process(RequestData requestData) {
        String headerValue = requestData.getRequestHeaders()
                                        .getHeaderValue(X_TODO_TOKEN);

        if (headerValue == null) {
            return HttpResponse.unauthorized();
        }

        Token token = new Token(headerValue);

        return process(requestData, token);
    }

    /**
     * Handles verified request.
     *
     * @param requestData data of received request
     * @param token       token of user who sent request
     * @return answer to received request
     */
    abstract HttpResponse process(RequestData requestData, Token token);
}
