package org.javaclasses.todo.web;

import com.google.gson.Gson;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Body of HTTP request.
 *
 * @author Oleg Barmin
 */
class RequestBody {

    private static final RequestBody EMPTY = new RequestBody();
    private static final Gson gson = new Gson();

    private final String value;

    /**
     * Creates empty {@code RequestBody} with no value.
     *
     * @implNote is used only to create {@link RequestBody#EMPTY}.
     */
    private RequestBody() {
        value = null;
    }

    /**
     * Creates {@code RequestBody} instance with given value.
     *
     * @param value value of request body
     */
    private RequestBody(String value) {
        this.value = value;
    }

    /**
     * Creates {@code RequestBody} instance with given JSON.
     *
     * @param json sting with JSON
     * @return instance with given {@code json} or empty {@code RequestBody} if empty string was given.
     */
    public static RequestBody of(String json) {
        checkNotNull(json);

        if (json.trim()
                .isEmpty()) {

            return EMPTY;
        }
        return new RequestBody(json);
    }

    /**
     * Deserializers value of this {@code RequestBody} into an object of the given tClass.
     *
     * @param tClass the class of T
     * @param <T>    the type of the desired object
     * @return an object of type T from stored {@code value}
     * @throws IllegalStateException if try to deserialize an empty {@code RequestBody}
     */
    <T> T as(Class<T> tClass) {
        checkNotNull(tClass);

        if (value == null) {
            throw new IllegalStateException("RequestBody.get() cannot be called on empty value.");
        }
        return gson.fromJson(value, tClass);
    }

    /**
     * Return {@code true} if this {@code RequestBody} is empty, otherwise {@code false}.
     *
     * @return {@code true} if this {@code RequestBody} is empty, otherwise {@code false}
     */
    boolean isEmpty() {
        return value == null;
    }
}
