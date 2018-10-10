package org.javaclasses.todo.web;

import com.google.gson.JsonSyntaxException;
import org.javaclasses.todo.auth.EmptyCredentialsException;
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
     *
     * <p>Occurs when {@code SecuredAbstractRequestHandler} received request with access request to entity
     * which is forbidden for occurred user to access.
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
     * Handles thrown {@link EmptyCredentialsException}.
     *
     * <p>Occurs when tries to authenticate with empty username or password.
     */
    public static class EmptyCredentialsHandler implements ExceptionHandler<EmptyCredentialsException> {

        @Override
        public void handle(EmptyCredentialsException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.internalError();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles thrown {@link EmptyTaskDescriptionException}.
     *
     * <p>Occurs when {@code CreateTaskRequestHandler} or {@code UpdateTaskRequestHandler} received request
     * to create/update {@code Task} with invalid description.
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
            HttpResponse httpResponse = HttpResponse.internalError();
            httpResponse.writeTo(response);
        }

    }

    /**
     * Handles thrown {@link TaskAlreadyExistsException}.
     *
     * <p>Occurs when {@code CreateTaskRequestHandler} received request
     * to create {@code Task} with existing ID.
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
     *
     * <p>Occurs when {@code AbstractRequestHandler} sub-classes received request
     * to find/modify {@code Task} with non-existing ID.
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
     *
     * <p>Occurs when {@code CreateTodoListRequestHandler} received request
     * to create {@code TodoList} with existing ID.
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
     *
     * <p>Occurs when {@code AbstractRequestHandler} sub-classes received request
     * to find/modify {@code TodoList} with non-existing ID.
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
     *
     * <p>Occurs when {@code AuthenticationHandler} received request to sign in with invalid credentials.
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
            HttpResponse httpResponse = HttpResponse.unauthorized();
            httpResponse.writeTo(response);
        }

    }

    /**
     * Handles {@link UpdateCompletedTaskException}.
     *
     * <p>Occurs when {@code UpdateTaskHandler} received request to update completed task.
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
            HttpResponse httpResponse = HttpResponse.internalError();
            httpResponse.writeTo(response);
        }
    }

    /**
     * Handles {@link JsonSyntaxException}.
     *
     * <p>Occurs when {@link AbstractRequestHandler} sub-classes tries to deserialize invalid request body with
     * invalid syntax.
     */
    public static class JsonSyntaxExceptionHandler implements ExceptionHandler<JsonSyntaxException> {

        /**
         * Responses with status code 500.
         *
         * @param exception occurred exception instance
         * @param request   request which caused exception
         * @param response  response to configure
         */
        @Override
        public void handle(JsonSyntaxException exception, Request request, Response response) {
            HttpResponse httpResponse = HttpResponse.internalError();
            httpResponse.writeTo(response);
        }
    }
}
