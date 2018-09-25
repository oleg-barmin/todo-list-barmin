package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.ServiceFactory;
import org.javaclasses.todo.model.Username;

import static spark.Spark.post;

/**
 * TodoList application.
 */
public class TodoListApplication {
    static final String AUTHENTICATION_PATH = "/auth";
    private final Authentication authentication;

    /**
     * Creates {@code TodoListApplication} instance.
     */
    TodoListApplication() {
        this.authentication = ServiceFactory.getAuthentication();
    }

    public static void main(String[] args) {
        new TodoListApplication().start();
    }

    /**
     * Starts server with to-do list application.
     */
    void start() {
        authentication.createUser(new Username("user"), new Password("password"));

        post(AUTHENTICATION_PATH, new AuthenticationHandler(authentication));
    }


    @SuppressWarnings("NonSerializableFieldInSerializableClass")
    public enum PreRegisteredUsers {
        USER_1;

        private final Username username;
        private final Password password;

        PreRegisteredUsers() {
            username = new Username("first_user");
            password = new Password("first_User_password_123");
        }

        public Username getUsername() {
            return username;
        }

        public Password getPassword() {
            return password;
        }
    }
}
