package org.javaclasses.todo.web;

import javax.annotation.Nullable;

/**
 * Data of received request.
 *
 * @param <P> payload of received request
 */
class RequestData<P> {
    private final P payload;
    private final RequestParams requestParams;
    private final RequestHeaders requestHeaders;

    /**
     * Creates {@code RequestData} instance.
     *
     * @param payload        payload of request
     * @param requestParams  parameters of request
     * @param requestHeaders headers of request
     */
    RequestData(@Nullable P payload, RequestParams requestParams, RequestHeaders requestHeaders) {
        this.payload = payload;
        this.requestParams = requestParams;
        this.requestHeaders = requestHeaders;
    }

    P getPayload() {
        return payload;
    }

    RequestParams getRequestParams() {
        return requestParams;
    }

    RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }
}
