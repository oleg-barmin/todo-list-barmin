package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

/**
 * Exception handlers which may occur in TodoList application.
 */
class ExceptionHandlers {

    /**
     * Handles thrown AuthorizationFailedException.
     */
    public static class AuthorizationFailedHandler implements ExceptionHandler<AuthorizationFailedException> {

        @Override
        public void handle(AuthorizationFailedException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
            response.body(exception.getMessage());
        }
    }

    /**
     * Handles thrown InvalidCredentialsException.
     */
    public static class InvalidCredentialsHandler implements ExceptionHandler<InvalidCredentialsException> {

        @Override
        public void handle(InvalidCredentialsException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
            response.body(exception.getMessage());
        }
    }

    /**
     * Handles thrown TodoListNotFoundException.
     */
    public static class TodoListNotFoundHandler implements ExceptionHandler<TodoListNotFoundException> {

        @Override
        public void handle(TodoListNotFoundException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
            response.body("");
        }
    }

    /**
     * Handles TaskNotFoundException.
     */
    public static class TaskNotFoundHandler implements ExceptionHandler<TaskNotFoundException> {

        @Override
        public void handle(TaskNotFoundException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
            response.body(exception.getMessage());
        }
    }
}
