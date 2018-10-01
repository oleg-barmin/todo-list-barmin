package org.javaclasses.todo.web;

/**
 * Data of received request.
 *
 * @author Oleg Barmin
 */
class RequestData {

    private final RequestBody requestBody;
    private final RequestParams requestParams;
    private final RequestHeaders requestHeaders;

    /**
     * Creates {@code RequestData} instance.
     *
     * @param requestBody    body of request
     * @param requestParams  parameters of request
     * @param requestHeaders headers of request
     */
    RequestData(RequestBody requestBody, RequestParams requestParams, RequestHeaders requestHeaders) {
        this.requestBody = requestBody;
        this.requestParams = requestParams;
        this.requestHeaders = requestHeaders;
    }

    RequestBody getRequestBody() {
        return requestBody;
    }

    RequestParams getRequestParams() {
        return requestParams;
    }

    RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }
}
