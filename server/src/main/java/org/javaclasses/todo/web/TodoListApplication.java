package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.ServiceFactory;
import org.javaclasses.todo.model.TodoService;
import spark.Service;

import static org.javaclasses.todo.web.AuthenticationController.*;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static spark.Service.ignite;

/**
 * TodoList application.
 */
public class TodoListApplication {

    static final String LISTS_PATH = "/lists";
    private final Service service = ignite();

    static final String AUTHENTICATION_PATH = "/auth";
    private final ServiceFactory serviceFactory;

    /**
     * Creates {@code TodoListApplication} instance.
     *
     * @param port port to start application on
     * @param serviceFactory factory of services
     */
    TodoListApplication(int port, ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        service.port(port);
    }

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactory();
        Authentication authentication = serviceFactory.getAuthentication();
        authentication.createUser(USER_1.getUsername(), USER_1.getPassword());

        new TodoListApplication(4567, serviceFactory).start();
    }

    /**
     * Starts to-do list application on given port.
     */
    void start() {
        Authentication authentication = serviceFactory.getAuthentication();
        TodoService todoService = serviceFactory.getTodoService();

        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());

        service.post(AUTHENTICATION_PATH, new AuthenticationHandler(authentication));
        service.post(LISTS_PATH, new ListController.ListCreationHandler(todoService));
    }

    /**
     * Stops to-do list application.
     */
    void stop() {
        service.stop();
    }
}
