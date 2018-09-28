package org.javaclasses.todo.web;

/**
 * Provides routes to endpoints of {@link TodoListApplication}.
 */
class Routes {

    private static final String AUTHENTICATION_ROUTE = "/auth";
    private static final String TODO_LIST_ROUTE = "/lists/:todolistid";
    private static final String TASK_ROUTE = "/lists/:todolistid/:taskid";

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
    static String getTodoListRoute() {
        return TODO_LIST_ROUTE;
    }

    /**
     * Provides tasks route.
     *
     * @return tasks route.
     */
    static String getTaskRoute() {
        return TASK_ROUTE;
    }
}
