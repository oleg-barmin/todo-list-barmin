package org.javaclasses.todo.web;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonSyntaxException;
import org.javaclasses.todo.ServiceFactory;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.EmptyCredentialsException;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.EmptyTaskDescriptionException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.TaskAlreadyExistsException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListAlreadyExistsException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import org.javaclasses.todo.model.entity.Username;
import spark.Service;

import static java.lang.System.getProperty;
import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.AuthenticationController.SingOutHandler;
import static org.javaclasses.todo.web.AuthenticationController.TokenValidationHandler;
import static org.javaclasses.todo.web.Configurations.getDefaultPort;
import static org.javaclasses.todo.web.ExceptionHandlers.AuthorizationFailedHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.EmptyCredentialsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.EmptyTaskDescriptionHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.InvalidCredentialsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.JsonSyntaxExceptionHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TaskAlreadyExistsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TaskNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TodoListAlreadyExistsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TodoListNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.UpdateCompletedTaskHandler;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.Routes.getTaskRoute;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;
import static org.javaclasses.todo.web.Routes.getUserListsRoute;
import static org.javaclasses.todo.web.TaskController.CreateTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.GetTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.RemoveTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.UpdateTaskRequestHandler;
import static org.javaclasses.todo.web.TodoListController.CreateTodoListRequestHandler;
import static org.javaclasses.todo.web.TodoListController.ReadTasksRequestHandler;
import static org.javaclasses.todo.web.TodoListController.ReadUserListsHandler;

/**
 * Runs server with TodoList application which provides access to {@link TodoService} functionality.
 *
 * <p>Application is based on REST a architectural style.
 *
 * <p>Services allows to:
 * - Authenticate users;
 * - Create new to-do lists;
 * - Read tasks from to-do lists;
 * - Find tasks by ID;
 * - Add new tasks to to-do lists;
 * - Update existing tasks;
 * - Remove existing tasks.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings({"OverlyCoupledClass", // TodoListApplication is REST API, it needs to use many dependencies to work.
        "WeakerAccess"}) // TodoListApplication is public API, so its methods and static field must be public.
public class TodoListApplication {

    private final Service service = Service.ignite();

    private final Authentication authentication;
    private final TodoService todoService;

    /**
     * Creates {@code TodoListApplication} instance.
     *
     * @param port port to start application on
     */
    public TodoListApplication(int port) {
        ServiceFactory serviceFactory = new ServiceFactory();
        this.authentication = serviceFactory.getAuthentication();
        this.todoService = serviceFactory.getTodoService();
        service.port(port);
    }

    // reading from system props default user credentials and port.
    @SuppressWarnings("AccessOfSystemProperties")
    public static void main(String[] args) {
        int port = getDefaultPort();

        String usernameStr = getProperty("todo.username");
        String passwordStr = getProperty("todo.password");
        String portStr = getProperty("todo.port");

        if (portStr != null) {
            port = Integer.parseInt(portStr);
        }

        TodoListApplication todoListApplication = new TodoListApplication(port);

        if (!(usernameStr == null || passwordStr == null)) {
            todoListApplication.authentication.createUser(new Username(usernameStr), new Password(passwordStr));
        }

        todoListApplication.start();
    }

    /**
     * Starts {@code TodoListApplication} server on given port.
     */
    @SuppressWarnings("OverlyCoupledMethod") // start server method needs many dependencies to init all handlers.
    public void start() {
        service.staticFileLocation("public/");

        // general exception handlers
        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(JsonSyntaxException.class, new JsonSyntaxExceptionHandler());
        service.exception(EmptyCredentialsException.class, new EmptyCredentialsHandler());

        // authentication routes
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());

        service.get(getAuthenticationRoute(), new TokenValidationHandler(authentication));
        service.post(getAuthenticationRoute(), new AuthenticationHandler(authentication));
        service.delete(getAuthenticationRoute(), new SingOutHandler(authentication));

        // to-do list routes
        service.exception(TodoListAlreadyExistsException.class, new TodoListAlreadyExistsHandler());
        service.exception(TodoListNotFoundException.class, new TodoListNotFoundHandler());

        service.post(getTodoListRoute(), new CreateTodoListRequestHandler(todoService));
        service.get(getTodoListRoute(), new ReadTasksRequestHandler(todoService));

        // user lists route
        service.get(getUserListsRoute(), new ReadUserListsHandler(todoService));

        // tasks routes
        service.exception(TaskAlreadyExistsException.class, new TaskAlreadyExistsHandler());
        service.exception(TaskNotFoundException.class, new TaskNotFoundHandler());

        service.exception(EmptyTaskDescriptionException.class, new EmptyTaskDescriptionHandler());
        service.exception(UpdateCompletedTaskException.class, new UpdateCompletedTaskHandler());

        service.get(getTaskRoute(), new GetTaskRequestHandler(todoService));
        service.post(getTaskRoute(), new CreateTaskRequestHandler(todoService));
        service.put(getTaskRoute(), new UpdateTaskRequestHandler(todoService));
        service.delete(getTaskRoute(), new RemoveTaskRequestHandler(todoService));

    }

    /**
     * Stops to-do list application.
     */
    public void stop() {
        service.stop();
    }

    @VisibleForTesting
    protected Authentication getAuthentication() {
        return authentication;
    }

}
