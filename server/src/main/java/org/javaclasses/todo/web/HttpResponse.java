package org.javaclasses.todo.web;

import spark.Response;

import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Response to request.
 */
class HttpResponse {

    private final int code;
    private final String body;

    /**
     * Creates {@code HttpResponse} instance with given status code and empty body.
     *
     * @param code status code of response
     */
    private HttpResponse(int code) {
        this.code = code;
        this.body = "";
    }

    /**
     * Creates {@code HttpResponse} instance with given status code and empty body.
     *
     * @param code status code of HttpResponse
     * @param body body of response
     */
    private HttpResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    /**
     * Creates instance of {@code HttpResponse} with given body and 200 status code.
     *
     * @param body body of response
     * @return response with 200 status code and given body
     */
    static HttpResponse ok(String body) {
        return new HttpResponse(HTTP_OK, body);
    }

    /**
     * Creates instance of {@code HttpResponse} with empty body and 200 status code.
     *
     * @return response with 200 status code and empty body
     */
    static HttpResponse ok() {
        return new HttpResponse(HTTP_OK);
    }

    /**
     * Creates instance of {@code HttpResponse} with empty body and 403 status code.
     *
     * @return response with 403 status code and empty body
     */
    static HttpResponse unauthorize() {
        return new HttpResponse(HTTP_UNAUTHORIZED);
    }

    /**
     * Creates instance of {@code HttpResponse} with empty body and 400 status code.
     *
     * @return response with 400 status code and empty body
     */
    static HttpResponse badRequest() {
        return new HttpResponse(HTTP_BAD_REQUEST);
    }

    /**
     * Creates instance of {@code HttpResponse} with empty and 403 status code.
     *
     * @return response with 403 status code and empty status code
     */
    static HttpResponse forbidden() {
        return new HttpResponse(HTTP_FORBIDDEN);
    }

    /**
     * Writes status code and body of this {@code HttpResponse} into the given {@link Response}.
     *
     * @param response response to write data into
     */
    void writeTo(Response response) {
        response.status(this.code);
        response.body(body);
    }

    int getCode() {
        return code;
    }

    String getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getBody());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpResponse)) {
            return false;
        }
        HttpResponse httpResponse = (HttpResponse) o;
        return getCode() == httpResponse.getCode() &&
                Objects.equals(getBody(), httpResponse.getBody());
    }
}
