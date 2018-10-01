package org.javaclasses.todo.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters of request.
 *
 * @author Oleg Barmin
 */
class RequestParams {

    private final Map<String, String> paramsMap;

    /**
     * Creates {@code RequestParams} instance.
     *
     * @param paramsMap map of parameters name and their values
     */
    RequestParams(Map<String, String> paramsMap) {
        this.paramsMap = new HashMap<>(paramsMap);
    }

    /**
     * Provides value of parameter.
     *
     * @param paramName name of param to find value of
     * @return value of requested param
     */
    String getParamValue(String paramName) {
        return paramsMap.get(paramName);
    }
}
