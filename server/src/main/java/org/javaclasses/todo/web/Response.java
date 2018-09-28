package org.javaclasses.todo.web;

import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Response to request.
 */
class Response {

    private final int code;
    private final String body;

    /**
     * Creates {@code Response} instance with given status code and empty body.
     *
     * @param code status code of answer
     */
    private Response(int code) {
        this.code = code;
        this.body = "";
    }

    /**
     * Creates {@code Response} instance with given status code and empty body.
     *
     * @param code status code of Response
     * @param body body of answer
     */
    private Response(int code, String body) {
        this.code = code;
        this.body = body;
    }

    /**
     * Creates instance of {@code Response} with empty body and 200 status code.
     *
     * @return answer with 200 status code and empty body
     */
    static Response ok() {
        return new Response(HTTP_OK);
    }

    static Response unauthorize() {
        return new Response(HTTP_UNAUTHORIZED);
    }

    static Response badRequest() {
        return new Response(HTTP_BAD_REQUEST);
    }

    String getBody() {
        return body;
    }

    /**
     * Creates instance of {@code Response} with given body and 200 status code.
     *
     * @param body body of answer
     * @return answer with 200 status code and given body
     */
    static Response ok(String body) {
        return new Response(HTTP_OK, body);
    }

    int getCode() {
        return code;
    }

    /**
     * Creates instance of {@code Response} with empty and 403 status code.
     *
     * @return answer with 403 status code and empty status code
     */
    static Response forbidden() {
        return new Response(HTTP_FORBIDDEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Response)) {
            return false;
        }
        Response response = (Response) o;
        return getCode() == response.getCode() &&
                Objects.equals(getBody(), response.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getBody());
    }
}
