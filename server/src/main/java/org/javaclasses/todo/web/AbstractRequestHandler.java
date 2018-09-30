package org.javaclasses.todo.web;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static org.javaclasses.todo.web.Configurations.getContentType;

/**
 * Abstract handler of requests by endpoint.
 *
 * @author Oleg Barmin
 */
abstract class AbstractRequestHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {
        Map<String, String> headersMap = new HashMap<>();
        request.headers()
               .forEach(header -> headersMap.put(header, request.headers(header)));

        RequestBody body = RequestBody.of(request.body());
        RequestParams params = new RequestParams(request.params());
        RequestHeaders headers = new RequestHeaders(headersMap);

        RequestData requestData = new RequestData(body, params, headers);

        HttpResponse httpResponse = process(requestData);

        response.status(httpResponse.getCode());
        response.type(getContentType());

        return httpResponse.getBody()
                           .asJson();
    }

    /**
     * Processes current {@code requestData}.
     *
     * @param requestData data of request
     * @return answer to requestData
     */
    abstract HttpResponse process(RequestData requestData);
}
