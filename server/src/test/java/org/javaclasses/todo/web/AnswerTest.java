package org.javaclasses.todo.web;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Answer should")
class AnswerTest {

    @DisplayName("perform equals and hashcode methods correctly.")
    @Test
    void testEqualsAndHashcode() {
        int statusCode = 200;
        String body = "body";

        new EqualsTester().addEqualityGroup(
                new Answer(statusCode, body),
                new Answer(statusCode, body)
        )
                .testEquals();
    }
}