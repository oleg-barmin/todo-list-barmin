package org.javaclasses.todo.web;

import java.util.Objects;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Answer to request.
 */
public class Answer {

    private final int code;
    private final String body;

    /**
     * Creates {@code Answer} instance with given status code and empty body.
     *
     * @param code status code of answer
     */
    Answer(int code) {
        this.code = code;
        this.body = "";
    }

    /**
     * Creates {@code Answer} instance with given status code and empty body.
     *
     * @param code status code of Answer
     * @param body body of answer
     */
    Answer(int code, String body) {
        this.code = code;
        this.body = body;
    }

    /**
     * Creates instance of {@code Answer} with given body and 200 status code.
     *
     * @param body body of answer
     * @return answer with 200 status code and given body
     */
    static Answer ok(String body) {
        return new Answer(HTTP_OK, body);
    }

    /**
     * Creates instance of {@code Answer} with empty and 403 status code.
     *
     * @return answer with 403 status code and empty status code
     */
    static Answer forbidden() {
        return new Answer(HTTP_FORBIDDEN);
    }

    String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return getCode() == answer.getCode() &&
                Objects.equals(getBody(), answer.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getBody());
    }
}
