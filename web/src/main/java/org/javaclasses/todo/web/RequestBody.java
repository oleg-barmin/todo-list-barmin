package org.javaclasses.todo.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Body of HTTP request.
 *
 * <p>{@code RequestBody} contain a JSON string as value.
 * Stored JSON cam be deserialized into object of given class {@link RequestBody#as(Class)}.
 *
 * <p>{@code RequestBody} can be empty, if given value is empty string.
 * If try to deserialize value of empty {@code RequestBody} {@link IllegalStateException} will occur,
 * so before deserialize value of empty {@code RequestBody} {@link RequestBody#isEmpty()} method should be called,
 * to validate is occurred {@code RequestBody} is empty.
 *
 * @author Oleg Barmin
 */
class RequestBody {

    private static final RequestBody EMPTY = new RequestBody();
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateToLongAdapter())
                                                      .create();

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
     * @throws IllegalStateException               if try to deserialize an empty {@code RequestBody}
     * @throws com.google.gson.JsonSyntaxException if stored request body is not
     *                                             valid representation of object of {@code <T>} class.
     * @implNote for deserialization {@link Gson} is used.
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
