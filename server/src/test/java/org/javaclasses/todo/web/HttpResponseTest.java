package org.javaclasses.todo.web;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse should")
class HttpResponseTest {

    @DisplayName("perform equals and hashcode methods correctly.")
    @Test
    void testEqualsAndHashcode() {
        new EqualsTester().addEqualityGroup(HttpResponse.ok(), HttpResponse.ok())
                          .testEquals();
    }
}