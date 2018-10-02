package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;

import java.util.regex.Pattern;

import static org.javaclasses.todo.web.Routes.getTaskIdParam;
import static org.javaclasses.todo.web.Routes.getTaskRoute;
import static org.javaclasses.todo.web.Routes.getTodoListIdParam;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;

/**
 * Provides methods for easily creating of links with given URL params.
 *
 * @author Oleg Barmin
 */
public class TestRoutesProvider {

    private static final String TASK_ROUTE_FORMAT;
    private static final String TODO_LIST_ROUTE_FORMAT;

    static {
        Pattern pattern = Pattern.compile(getTodoListIdParam() + '|' + getTaskIdParam());
        TASK_ROUTE_FORMAT = pattern.matcher(getTaskRoute())
                                   .replaceAll("%s");
        TODO_LIST_ROUTE_FORMAT = pattern.matcher(getTodoListRoute())
                                        .replaceAll("%s");
    }

    private TestRoutesProvider() {
    }

    /**
     * Creates a URL with IDs of to-do list and task to access task in REST API of to-do list application.
     *
     * @param todoListId ID of to-do list to set into the URL
     * @param taskId     ID of task to set into the URL
     * @return URL with given params
     */
    public static String getTaskUrl(TodoListId todoListId, TaskId taskId) {
        return String.format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue());
    }

    /**
     * Creates a URL with ID of to-do list to access to-do list in REST API of to-do list application.
     *
     * @param todoListId ID of to-do list to set into the URL
     * @return URL with given param
     */
    public static String getTodoListUrl(TodoListId todoListId) {
        return String.format(TODO_LIST_ROUTE_FORMAT, todoListId.getValue());
    }
}
