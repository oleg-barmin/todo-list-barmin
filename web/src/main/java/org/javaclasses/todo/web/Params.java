package org.javaclasses.todo.web;

/**
 * URL parameters used in {@link TodoListApplication}.
 *
 * @author Oleg Barmin
 */
public class Params {

    private static final String TODO_LIST_ID_PARAM = ":todolistid";
    private static final String TASK_ID_PARAM = ":taskid";

    private Params() {
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
