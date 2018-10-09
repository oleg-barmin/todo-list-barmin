package org.javaclasses.todo.web;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Adapter for {@link com.google.gson.GsonBuilder} for {@code Date} class,
 * it serializes dates into {@code long} and deserialize them from {@code long}.
 *
 * @author Oleg Barmin
 */
public class DateToLongAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(value.getTime());
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        return new Date(in.nextLong());
    }
}
