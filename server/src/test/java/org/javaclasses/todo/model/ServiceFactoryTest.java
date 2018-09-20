package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServiceFactory should")
class ServiceFactoryTest {

    @Test
    @DisplayName("provide instance of Authentication.")
    void testGetAuthentication() {
        Authentication authentication = ServiceFactory.getAuthentication();

        Assertions.assertNotNull(authentication,
                "ServiceFactory should provide not null Authentication, but it don't.");
    }

    @Test
    @DisplayName("provide instance of TodoService.")
    void testGetTodoService() {
        TodoService todoService = ServiceFactory.getTodoService();

        Assertions.assertNotNull(todoService,
                "ServiceFactory should provide not null TodoService, but it don't.");
    }

}