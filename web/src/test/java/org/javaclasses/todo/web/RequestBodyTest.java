package org.javaclasses.todo.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.javaclasses.todo.model.entity.User;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("RequestBody should")
class RequestBodyTest {

    @Test
    @DisplayName("deserialize given JSON to object of given class.")
    void testDeserializeStoredJson() {
        User user = new User(new UserId(UUID.randomUUID()
                                            .toString()));

        String userJson = new Gson().toJson(user);

        RequestBody requestBody = RequestBody.of(userJson);

        User deserializedUser = requestBody.as(User.class);

        assertEquals(user, deserializedUser,
                     "same equals object must be returned, but it don't.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw IllegalStateException when try to get from empty response body.")
    void testEmptyCreation() {
        RequestBody requestBody = RequestBody.of("");

        if (!requestBody.isEmpty()) {
            fail("return true on isEmpty method, when given JSON is empty string, but it don't.");
        }

        assertThrows(IllegalStateException.class, () -> requestBody.as(String.class),
                     "thrown IllegalStateException, but it don't.");
    }

    // in good case exception will be thrown, so return value cannot be received
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw JsonSyntaxException if if stored request body is not valid JSON.")
    void testThrowJsonSyntaxException() {
        RequestBody requestBody = RequestBody.of("not valid JSON");

        assertThrows(JsonSyntaxException.class, () -> requestBody.as(Object.class));
    }

}