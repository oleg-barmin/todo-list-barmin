package org.javaclasses.todo.web;

import static java.lang.String.format;

/**
 * Provides routes to endpoints of {@link TodoListApplication}.
 */
public class Routes {

    private static final String AUTHENTICATION_ROUTE = "/auth";
    private static final String USER_LISTS_ROUTE = "/lists";
    private static final String TODO_LIST_ROUTE = format("/lists/%s", Params.getTodoListIdParam());
    private static final String TASK_ROUTE = format("/lists/%s/%s", Params.getTodoListIdParam(),
                                                    Params.getTaskIdParam());

    private Routes() {
    }

    /**
     * Provides Users lists route.
     *
     * @return user lists route
     */
    public static String getUserListsRoute() {
        return USER_LISTS_ROUTE;
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

}
