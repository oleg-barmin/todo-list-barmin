package org.javaclasses.todo.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Produces configured {@link Gson} instances which allows to properly serialise/deserialize
 * object from/into JSON string.
 *
 * @author Oleg Barmin
 */
class GsonFactory {

    private GsonFactory() {
    }

    /**
     * Creates new configured instance of {@link Gson}.
     *
     * @return configured {@code Gson} instance.
     */
    static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new DateToLongAdapter())
                                .create();
    }

}
