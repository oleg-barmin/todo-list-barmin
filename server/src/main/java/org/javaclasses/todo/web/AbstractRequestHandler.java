package org.javaclasses.todo.web;

import com.google.gson.Gson;
import spark.Request;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract handler of requests by endpoint.
 *
 * @param <P> payload of request to process.
 * @author Oleg Barmin
 */
abstract class AbstractRequestHandler<P> implements Route {

    private final Class<P> payloadClass;

    /**
     * Creates {@code AbstractRequestHandler} instance.
     *
     * @param payloadClass class of payload.
     */
    AbstractRequestHandler(Class<P> payloadClass) {
        this.payloadClass = payloadClass;
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
        return new Gson().fromJson(json, payloadClass);
    }

    @Override
    public Object handle(Request request, spark.Response response) {
        P value = null;

        if (payloadClass != Void.class) {
            value = objectFromJson(request.body());
        }

        Map<String, String> headersMap = new HashMap<>();
        request.headers()
               .forEach(header -> headersMap.put(header, request.headers(header)));

        RequestHeaders requestHeaders = new RequestHeaders(headersMap);
        RequestParams requestParams = new RequestParams(request.params());

        RequestData<P> requestData = new RequestData<>(value, requestParams, requestHeaders);

        HttpResponse answer = process(requestData);

        response.status(answer.getCode());
        response.body(answer.getBody());

        return answer.getBody();
    }

    /**
     * Processes current {@code requestData}.
     *
     * @param requestData data of request
     * @return answer to requestData
     */
    abstract HttpResponse process(RequestData<P> requestData);
}
