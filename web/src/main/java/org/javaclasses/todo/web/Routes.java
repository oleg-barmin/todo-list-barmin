package org.javaclasses.todo.web;

import static java.lang.String.format;

/**
 * Provides routes to endpoints of {@link TodoListApplication}.
 */
public class Routes {

    private static final String TODO_LIST_ID_PARAM = ":todolistid";
    private static final String TASK_ID_PARAM = ":taskid";

    private static final String AUTHENTICATION_ROUTE = "/auth";
    private static final String TODO_LIST_ROUTE = format("/lists/%s", TODO_LIST_ID_PARAM);
    private static final String TASK_ROUTE = format("/lists/%s/%s", TODO_LIST_ID_PARAM, TASK_ID_PARAM);

    private Routes() {
    }

    /**
     * Provides authentication route.
     *
     * @return authentication route.
     */
    static String getAuthenticationRoute() {
        return AUTHENTICATION_ROUTE;
    }

    /**
     * Provides read tasks route.
     *
     * @return read tasks route
     */
    public static String getTodoListRoute() {
        return TODO_LIST_ROUTE;
    }

    /**
     * Provides tasks route.
     *
     * @return tasks route.
     */
    public static String getTaskRoute() {
        return TASK_ROUTE;
    }

    /**
     * URL parameter value for ID of to-do list.
     *
     * @return value of to-do list ID parameter
     */
    public static String getTodoListIdParam() {
        return TODO_LIST_ID_PARAM;
    }

    /**
     * URL parameter value for ID of task.
     *
     * @return value of task ID parameter
     */
    public static String getTaskIdParam() {
        return TASK_ID_PARAM;
    }
}
