package org.javaclasses.todo.web;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonSyntaxException;
import org.javaclasses.todo.ServiceFactory;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.EmptyTaskDescriptionException;
import org.javaclasses.todo.model.TaskAlreadyExistsException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListAlreadyExistsException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import spark.Service;

import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.Configurations.getDefaultPort;
import static org.javaclasses.todo.web.ExceptionHandlers.AuthorizationFailedHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.EmptyTaskDescriptionHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.InvalidCredentialsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.JsonSyntaxExceptionHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TaskAlreadyExistsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TaskNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TodoListAlreadyExistsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TodoListNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.UpdateCompletedTaskHandler;
import static org.javaclasses.todo.web.TaskController.CreateTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.GetTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.RemoveTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.UpdateTaskRequestHandler;
import static org.javaclasses.todo.web.TodoListController.CreateTodoListRequestHandler;
import static org.javaclasses.todo.web.TodoListController.ReadTasksRequestHandler;

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

    public static void main(String[] args) {
        int port = getDefaultPort();
        if (args.length == 1) {
            String arg = args[0];
            port = Integer.parseInt(arg);
        }
        new TodoListApplication(port).start();
    }

    /**
     * Starts {@code TodoListApplication} server on given port.
     */
    @SuppressWarnings("OverlyCoupledMethod") // start server method needs many dependencies to init all handlers.
    public void start() {
        service.staticFileLocation("resources/public/");

        // general exception handlers
        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(JsonSyntaxException.class, new JsonSyntaxExceptionHandler());

        // authentication routes
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());

        service.post(Routes.getAuthenticationRoute(), new AuthenticationHandler(authentication));

        // todo list routes
        service.exception(TodoListAlreadyExistsException.class, new TodoListAlreadyExistsHandler());
        service.exception(TodoListNotFoundException.class, new TodoListNotFoundHandler());

        service.post(Routes.getTodoListRoute(), new CreateTodoListRequestHandler(todoService));
        service.get(Routes.getTodoListRoute(), new ReadTasksRequestHandler(todoService));

        // tasks routes
        service.exception(TaskAlreadyExistsException.class, new TaskAlreadyExistsHandler());
        service.exception(TaskNotFoundException.class, new TaskNotFoundHandler());

        service.exception(EmptyTaskDescriptionException.class, new EmptyTaskDescriptionHandler());
        service.exception(UpdateCompletedTaskException.class, new UpdateCompletedTaskHandler());

        service.get(Routes.getTaskRoute(), new GetTaskRequestHandler(todoService));
        service.post(Routes.getTaskRoute(), new CreateTaskRequestHandler(todoService));
        service.put(Routes.getTaskRoute(), new UpdateTaskRequestHandler(todoService));
        service.delete(Routes.getTaskRoute(), new RemoveTaskRequestHandler(todoService));

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
