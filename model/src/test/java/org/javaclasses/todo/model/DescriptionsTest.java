package org.javaclasses.todo.model;

import org.javaclasses.todo.model.entity.Descriptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing {@link Descriptions} utility class which allows to validate task description.
 *
 * @author Oleg Barmin
 */
@DisplayName("Descriptions should")
class DescriptionsTest {

    @Test
    @DisplayName("throw EmptyTaskDescriptionException if given description to validate is empty.")
    void testValidateEmptyDescription() {
        Assertions.assertThrows(EmptyTaskDescriptionException.class,
                                () -> Descriptions.validate("  "));
        Assertions.assertThrows(EmptyTaskDescriptionException.class,
                                () -> Descriptions.validate(""));
    }

    @SuppressWarnings("ConstantConditions") // null value was given to validate method behaviour
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