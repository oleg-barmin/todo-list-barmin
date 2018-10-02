package org.javaclasses.todo.model;

import org.javaclasses.todo.ServiceFactory;
import org.javaclasses.todo.auth.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ServiceFactory should")
class ServiceFactoryTest {

    @Test
    @DisplayName("provide instance of Authentication.")
    void testGetAuthentication() {
        ServiceFactory serviceFactory = new ServiceFactory();
        Authentication authentication = serviceFactory.getAuthentication();

        Assertions.assertNotNull(authentication,
                                 "provide not null Authentication, but it don't.");
    }

    @Test
    @DisplayName("provide instance of TodoService.")
    void testGetTodoService() {
        ServiceFactory serviceFactory = new ServiceFactory();
        TodoService todoService = serviceFactory.getTodoService();

        Assertions.assertNotNull(todoService,
                                 "provide not null TodoService, but it don't.");
    }
}