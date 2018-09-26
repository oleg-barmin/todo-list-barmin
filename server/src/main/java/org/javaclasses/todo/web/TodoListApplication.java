package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.*;
import spark.Service;

import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.*;
import static org.javaclasses.todo.web.ListController.ListCreationHandler;
import static org.javaclasses.todo.web.ListController.ReadTasksHandler;
import static org.javaclasses.todo.web.TaskController.*;
import static spark.Service.ignite;

/**
 * Runs server with TodoList application which provides access to application functionality.
 *
 * <p>Application is based on REST a architectural style.
 *
 * Services allows to:
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

    // TodoListApplication REST API endpoints
    public static final String AUTHENTICATION_PATH = "/auth";
    public static final String CREATE_LIST_PATH = "/lists";
    public static final String READ_TASKS_PATH = "lists/:todolistid";
    public static final String TASKS_PATH = "/lists/:todolistid/:taskid";

    private static final int DEFAULT_PORT = 4567;

    private final Service service = ignite();
    private final ServiceFactory serviceFactory;


    /**
     * Creates {@code TodoListApplication} instance.
     *
     * @param port           port to start application on
     * @param serviceFactory factory of services
     */
    public TodoListApplication(int port, ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        service.port(port);
    }

    public static void main(String[] args) {
        new TodoListApplication(DEFAULT_PORT, new ServiceFactory()).start();
    }

    /**
     * Starts {@code TodoListApplication} server on given port.
     */
    @SuppressWarnings("OverlyCoupledMethod") // start server method needs many dependencies to init all handlers.
    public void start() {
        Authentication authentication = serviceFactory.getAuthentication();
        TodoService todoService = serviceFactory.getTodoService();

        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());
        service.exception(TodoListNotFoundException.class, new TodoListNotFoundHandler());
        service.exception(TaskNotFoundException.class, new TaskNotFoundHandler());

        service.post(AUTHENTICATION_PATH, new AuthenticationHandler(authentication));

        service.post(CREATE_LIST_PATH, new ListCreationHandler(todoService));

        service.get(READ_TASKS_PATH, new ReadTasksHandler(todoService));

        service.get(TASKS_PATH, new GetTaskHandler(todoService));
        service.post(TASKS_PATH, new CreateTaskHandler(todoService));
        service.put(TASKS_PATH, new TaskUpdateHandler(todoService));
        service.delete(TASKS_PATH, new TaskRemoveHandler(todoService));
    }

    /**
     * Stops to-do list application.
     */
    public void stop() {
        service.stop();
    }
}
