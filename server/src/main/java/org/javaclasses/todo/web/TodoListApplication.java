package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.*;
import spark.Service;

import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.*;
import static org.javaclasses.todo.web.ListController.ReadTasksRequestHandler;
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
    public static final String AUTHENTICATION_ROUTE = "/auth";
    public static final String CREATE_LIST_ROUTE = "/lists";
    public static final String READ_TASKS_ROUTE = "/lists/:todolistid";
    public static final String TASKS_ROUTE = "/lists/:todolistid/:taskid";

    //TodoListApplication REST API url parameters names.
    static final String TODO_LIST_ID_PARAM = ":todolistid";
    static final String TASK_ID_PARAM = ":taskid";

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
        service.exception(UpdateCompletedTaskException.class, new UpdateCompletedTaskHandler());

        service.post(AUTHENTICATION_ROUTE, new AuthenticationHandler(authentication));

        service.post(CREATE_LIST_ROUTE, new ListController.ListCreationRequestHandler(todoService));

        service.get(READ_TASKS_ROUTE, new ReadTasksRequestHandler(todoService));

        service.get(TASKS_ROUTE, new GetTaskRequestHandler(todoService));
        service.post(TASKS_ROUTE, new CreateTaskRequestHandler(todoService));
        service.put(TASKS_ROUTE, new TaskUpdateRequestHandler(todoService));
        service.delete(TASKS_ROUTE, new TaskRemoveRequestHandler(todoService));
    }

    /**
     * Stops to-do list application.
     */
    public void stop() {
        service.stop();
    }
}
