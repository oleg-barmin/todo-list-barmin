package org.javaclasses.todo.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("RequestBody should")
class RequestBodyTest {

    // in good case exception will be thrown, so return value cannot be received
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("throw IllegalStateException when try to get from empty response body.")
    void testEmptyCreation() {
        RequestBody requestBody = RequestBody.of("");

        assertThrows(IllegalStateException.class, () -> requestBody.as(String.class));
    }
}