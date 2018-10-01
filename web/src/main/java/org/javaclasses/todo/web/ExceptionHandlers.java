package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.EmptyTaskDescriptionException;
import org.javaclasses.todo.model.TaskAlreadyExistsException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListAlreadyExistsException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Exception handlers of exceptions which may occur in TodoList application.
 *
 * @author Oleg Barmin
 */
//Contains inner classes to handle exceptions
@SuppressWarnings("unused")
class ExceptionHandlers {

    private ExceptionHandlers() {
    }

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
     * Handles thrown {@link EmptyTaskDescriptionException}.
     */
    public static class EmptyTaskDescriptionHandler implements ExceptionHandler<EmptyTaskDescriptionException> {

        /**
         * Responses with status code 500 status code and exception message in response body.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(EmptyTaskDescriptionException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.internalError(exception.getMessage());
            httpResponse.writeTo(response);
        }

    }

    /**
     * Handles thrown {@link TaskAlreadyExistsException}.
     */
    public static class TaskAlreadyExistsHandler implements ExceptionHandler<TaskAlreadyExistsException> {

        /**
         * Responses with status code 403 status code.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(TaskAlreadyExistsException exception, Request request, Response response) {
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
     * Handles {@link TodoListAlreadyExistsException}.
     */
    public static class TodoListAlreadyExistsHandler implements ExceptionHandler<TodoListAlreadyExistsException> {

        /**
         * Responses with status code 403.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(TodoListAlreadyExistsException exception, Request request, Response response) {
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
            HttpResponse httpResponse = HttpResponse.internalError(exception.getMessage());
            httpResponse.writeTo(response);
        }
    }
}
