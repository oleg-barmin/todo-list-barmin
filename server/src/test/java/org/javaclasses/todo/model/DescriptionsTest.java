package org.javaclasses.todo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Descriptions should")
class DescriptionsTest {

    @Test
    @DisplayName("throw EmptyTaskDescriptionException if given description to validate is empty.")
    void testValidateEmptyDescription() {
        Assertions.assertThrows(EmptyTaskDescriptionException.class, () -> Descriptions.validate("  "));
        Assertions.assertThrows(EmptyTaskDescriptionException.class, () -> Descriptions.validate(""));
    }

    @Test
    @DisplayName("throw NullPointerException if given description to validate is empty.")
    void testValidateNullDescription() {
        Assertions.assertThrows(NullPointerException.class, () -> Descriptions.validate(null));
    }

    @Test
    @DisplayName("throw nothing if given description to validate is valid.")
    void testValidateValidDescription() {
        Descriptions.validate("abc");
        Descriptions.validate("  adc  ");
    }
}