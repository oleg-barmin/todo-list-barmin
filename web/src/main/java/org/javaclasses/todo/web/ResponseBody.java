package org.javaclasses.todo.web;

import com.google.gson.Gson;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Response body of {@link HttpResponse HTTP response}.
 *
 * <p>{@code ResponseBody} contains an instance of {@code <V>} class.
 * Stored value can be serialized to JSON with {@link ResponseBody#asJson()}.
 *
 * <p>Empty {@code ResponseBody} can be created with {@link ResponseBody#empty()}.
 * If try to serialize to JSON an empty {@code ResponseBody} empty string will be returned.
 *
 * @param <V> value of response body
 * @author Oleg Barmin
 */
class ResponseBody<V> {

    private static final ResponseBody<?> EMPTY = new ResponseBody<>();
    private static final Gson gson = GsonFactory.getGson();
    private final V value;

    /**
     * Creates {@code ResponseBody} with empty value.
     *
     * @implNote is used only to create {@link ResponseBody#EMPTY}.
     */
    private ResponseBody() {
        value = null;
    }

    /**
     * Creates {@code ResponseBody} with given value.
     *
     * @param value value of response body
     */
    private ResponseBody(V value) {
        this.value = checkNotNull(value);
    }

    /**
     * Returns empty {@code ResponseBody} instance, which has no value.
     *
     * @return an empty {@code ResponseBody}
     */
    static ResponseBody empty() {
        return EMPTY;
    }

    /**
     * Creates {@code ResponseBody} instance with given value.
     *
     * @param value value to store
     * @param <V>   class of the {@code value}
     * @return {@code ResponseBody} with given value
     */
    static <V> ResponseBody<V> of(V value) {
        checkNotNull(value);
        return new ResponseBody<>(value);
    }

    /**
     * Serializes value of response body into its equivalent JSON representation.
     *
     * @return JSON representation of {@code ResponseBody} value or empty string
     * if {@code ResponseBody} is empty.
     * @implNote to serialize value to JSON {@link Gson} is used
     */
    String asJson() {
        return value == null ? "" : gson.toJson(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseBody)) {
            return false;
        }
        ResponseBody<?> that = (ResponseBody<?>) o;
        return Objects.equals(value, that.value);
    }
}
