package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

/**
 * Exception handlers of exceptions which may occur in TodoList application.
 */
//Contains inner classes to handle exceptions
@SuppressWarnings("unused")
class ExceptionHandlers {

    /**
     * Handles thrown AuthorizationFailedException.
     */
    public static class AuthorizationFailedHandler implements ExceptionHandler<AuthorizationFailedException> {

        /**
         * Response with status code 403 and exception message in body.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
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

        /**
         * Response with status code 403 and exception message in body.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
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

        /**
         * Response with status code 403 and exception message in body.
         *
         * @param exception occurred exception instance
         * @param request request which caused exception
         * @param response response to configure
         */
        @Override
        public void handle(TodoListNotFoundException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
        }
    }

    /**
     * Handles TaskNotFoundException.
     */
    public static class TaskNotFoundHandler implements ExceptionHandler<TaskNotFoundException> {

        /**
         * Response with status code 403 and exception message in body.
         *
         * @param exception occurred exception instance
         * @param request request which caused exception
         * @param response response to configure
         */
        @Override
        public void handle(TaskNotFoundException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
        }
    }

    /**
     * Handles UpdateCompletedTaskException.
     */
    public static class UpdateCompletedTaskHandler implements ExceptionHandler<UpdateCompletedTaskException> {

        /**
         * Response with status code 403 and exception message in body.
         *
         * @param exception occurred exception instance
         * @param request request which caused exception
         * @param response response to configure
         */
        @Override
        public void handle(UpdateCompletedTaskException exception, Request request, Response response) {
            response.status(HTTP_FORBIDDEN);
            response.body(exception.getMessage());
        }
    }
}
