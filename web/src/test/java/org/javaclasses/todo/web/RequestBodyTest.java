package org.javaclasses.todo.web;

import com.google.common.testing.EqualsTester;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.javaclasses.todo.model.entity.User;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("RequestBody should")
class RequestBodyTest {

    @DisplayName("process equals and hashcode methods correctly.")
    @Test
    void testEqualsAndHashcode() {
        new EqualsTester().addEqualityGroup(RequestBody.of(""), RequestBody.of(""))
                          .testEquals();

        String body = "request body.";

        new EqualsTester().addEqualityGroup(RequestBody.of(body), RequestBody.of(body))
                          .testEquals();
    }

    // If test performs properly NullPointerException will be throw, so return value won't be received.
    // null is given in test needs.
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @Test
    @DisplayName("throw NullPointerException when given value is null.")
    void testNullBody() {
        Assertions.assertThrows(NullPointerException.class, () -> RequestBody.of(null));
    }


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