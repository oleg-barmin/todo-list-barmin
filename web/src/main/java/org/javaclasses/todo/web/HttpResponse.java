package org.javaclasses.todo.web;

import spark.Response;

import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Response to request.
 *
 * @author Oleg Barmin
 */
class HttpResponse<B> {

    private final int code;
    private final ResponseBody<?> body;

    /**
     * Creates {@code HttpResponse} instance with given status code and empty body.
     *
     * @param code status code of response
     */
    private HttpResponse(int code) {
        this.code = code;
        this.body = ResponseBody.empty();
    }

    /**
     * Creates {@code HttpResponse} instance with given status code and empty body.
     *
     * @param code status code of HttpResponse
     * @param body body of response
     */
    private HttpResponse(int code, B body) {
        this.code = code;
        this.body = ResponseBody.of(body);
    }

    /**
     * Creates instance of {@code HttpResponse} with given body and 200 status code.
     *
     * @param body body of response
     * @param <B>  body type
     * @return response with 200 status code and given body
     */
    static <B> HttpResponse ok(B body) {
        return new HttpResponse<>(HTTP_OK, body);
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
     * Creates instance of {@code HttpResponse} with empty body and 401 status code.
     *
     * @return response with 401 status code and empty body
     */
    static HttpResponse unauthorized() {
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
     * Creates instance of {@code HttpResponse} with empty body and 403 status code.
     *
     * @return response with 403 status code and empty status code
     */
    static HttpResponse forbidden() {
        return new HttpResponse(HTTP_FORBIDDEN);
    }

    /**
     * Creates instance of {@code HttpResponse} with 500 status code.
     *
     * @return response with 500 status code and given body
     */
    static HttpResponse internalError() {
        return new HttpResponse<>(HTTP_INTERNAL_ERROR);
    }

    /**
     * Writes status code and body of this {@code HttpResponse} into the given {@link Response}.
     *
     * @param response response to write data into
     */
    void writeTo(Response response) {
        response.status(this.code);
        response.body(body.asJson());
    }

    int getCode() {
        return code;
    }

    ResponseBody getBody() {
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
