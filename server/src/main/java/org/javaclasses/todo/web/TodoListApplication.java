package org.javaclasses.todo.web;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.ServiceFactory;
import org.javaclasses.todo.model.Username;

import static spark.Service.ignite;

/**
 * TodoList application.
 */
public class TodoListApplication {
    static final String AUTHENTICATION_PATH = "/auth";
    private static final int PORT = 8080;
    private final Authentication authentication;
    private boolean running = false;


    @VisibleForTesting
    TodoListApplication(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Creates {@code TodoListApplication} instance.
     */
    private TodoListApplication() {
        this.authentication = ServiceFactory.getAuthentication();
    }

    public static void main(String[] args) {
        new TodoListApplication().start();
    }

    /**
     * Starts server with to-do list application.
     */
    void start() {
        if (running) {
            return;
        }
        ignite().port(PORT)
                .post(AUTHENTICATION_PATH, new AuthenticationHandler(authentication));

        authentication.createUser(new Username("user"), new Password("password"));
        running = true;
    }
}
