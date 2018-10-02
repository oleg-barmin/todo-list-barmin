package org.javaclasses.todo.model;

import org.javaclasses.todo.ServiceFactory;
import org.javaclasses.todo.auth.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing {@link ServiceFactory} which should provide same instances of services on one instance of itself.
 */
@DisplayName("ServiceFactory should")
class ServiceFactoryTest {

    @Test
    @DisplayName("provide instance of Authentication.")
    void testGetAuthentication() {
        ServiceFactory serviceFactory = new ServiceFactory();
        Authentication authentication = serviceFactory.getAuthentication();

        assertNotNull(authentication,
                                 "provide not null Authentication, but it don't.");
    }

    @Test
    @DisplayName("provide instance of TodoService.")
    void testGetTodoService() {
        ServiceFactory serviceFactory = new ServiceFactory();
        TodoService todoService = serviceFactory.getTodoService();

        assertNotNull(todoService,
                                 "provide not null TodoService, but it don't.");
    }
}