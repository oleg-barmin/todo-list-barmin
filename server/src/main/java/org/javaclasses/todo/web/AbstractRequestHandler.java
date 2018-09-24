package org.javaclasses.todo.web;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract handler of requests by endpoint.
 *
 * @param <P> payload of request to process.
 */
abstract class AbstractRequestHandler<P> implements Route {
    private final Class<P> valueClass;

    /**
     * Creates {@code AbstractRequestHandler} instance.
     *
     * @param payloadClass class of payload.
     */
    AbstractRequestHandler(Class<P> payloadClass) {
        this.valueClass = payloadClass;
    }

    /**
     * Creates JSON from given object.
     *
     * @param object object to creates JSON from
     * @return JSON of given object.
     */
    String objectToJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Creates object of {@code payloadClass} from given JSON.
     *
     * @param json JSON to create object from
     * @return object created from given JSON
     */
    private P objectFromJson(String json) {
        return new Gson().fromJson(json, valueClass);
    }

    @Override
    public Object handle(Request request, Response response) {
        P value = null;
        if (valueClass != EmptyPayload.class) {
            value = objectFromJson(request.body());
        }

        Map<String, String> headersMap = new HashMap<>();
        request.headers().forEach(header -> headersMap.put(header, request.headers(header)));

        RequestHeaders requestHeaders = new RequestHeaders(headersMap);
        RequestParams requestParams = new RequestParams(request.params());

        Answer answer = process(value, requestParams, requestHeaders);

        response.status(answer.getCode());
        response.type("application/json");
        response.body(answer.getBody());

        return answer.getBody();
    }

    /**
     * Processes current request.
     *
     * @param payload payload of request
     * @param params  parameters of request
     * @param headers headers of request
     * @return answer to request
     */
    abstract Answer process(@Nullable P payload, RequestParams params, RequestHeaders headers);
}
