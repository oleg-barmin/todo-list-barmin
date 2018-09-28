package org.javaclasses.todo.web;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Response should")
class ResponseTest {

    @DisplayName("perform equals and hashcode methods correctly.")
    @Test
    void testEqualsAndHashcode() {
        new EqualsTester().addEqualityGroup(Response.ok(), Response.ok())
                          .testEquals();
    }
}