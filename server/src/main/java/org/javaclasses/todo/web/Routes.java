package org.javaclasses.todo.web;

/**
 * Provides routes to endpoints of {@link TodoListApplication}.
 */
class Routes {

    private static final String AUTHENTICATION_ROUTE = "/auth";
    private static final String CREATE_TODO_LIST_ROUTE = "/lists";
    private static final String READ_TASKS_ROUTE = "/lists/:todolistid";
    private static final String TASKS_ROUTE = "/lists/:todolistid/:taskid";

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
     * Provides create to-do list route.
     *
     * @return create to-do list route
     */
    static String getCreateTodoListRoute() {
        return CREATE_TODO_LIST_ROUTE;
    }

    /**
     * Provides read tasks route.
     *
     * @return read tasks route
     */
    static String getReadTasksRoute() {
        return READ_TASKS_ROUTE;
    }

    /**
     * Provides tasks route.
     *
     * @return tasks route.
     */
    static String getTasksRoute() {
        return TASKS_ROUTE;
    }
}
