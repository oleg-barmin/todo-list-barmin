package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Exception handlers of exceptions which may occur in TodoList application.
 */
//Contains inner classes to handle exceptions
@SuppressWarnings("unused")
class ExceptionHandlers {

    /**
     * Handles thrown {@link AuthorizationFailedException}.
     */
    public static class AuthorizationFailedHandler implements ExceptionHandler<AuthorizationFailedException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(AuthorizationFailedException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.forbidden();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles thrown {@link InvalidCredentialsException}.
     */
    public static class InvalidCredentialsHandler implements ExceptionHandler<InvalidCredentialsException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(InvalidCredentialsException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.forbidden();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles thrown {@link TodoListNotFoundException}.
     */
    public static class TodoListNotFoundHandler implements ExceptionHandler<TodoListNotFoundException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(TodoListNotFoundException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.forbidden();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles {@link TaskNotFoundException}.
     */
    public static class TaskNotFoundHandler implements ExceptionHandler<TaskNotFoundException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(TaskNotFoundException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.forbidden();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles {@link UpdateCompletedTaskException}.
     */
    public static class UpdateCompletedTaskHandler implements ExceptionHandler<UpdateCompletedTaskException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(UpdateCompletedTaskException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.forbidden();
            httpResponse.writeTo(response);
        }
    }
}
