package org.javaclasses.todo.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Headers of request.
 *
 * @author Oleg Barmin
 */
class RequestHeaders {

    private final Map<String, String> headersMap;

    /**
     * Creates {@code RequestHeaders} instance.
     *
     * @param headersMap map with headers and their values
     */
    RequestHeaders(Map<String, String> headersMap) {
        this.headersMap = new HashMap<>(headersMap);
    }

    /**
     * Provides value of given header.
     *
     * @param headerName name of header to find value of
     * @return value of header
     */
    String getHeaderValue(String headerName) {
        return headersMap.get(headerName);
    }
}
