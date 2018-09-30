package org.javaclasses.todo.web;

import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;

import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.javaclasses.todo.web.Routes.getTaskIdParam;
import static org.javaclasses.todo.web.Routes.getTaskRoute;
import static org.javaclasses.todo.web.Routes.getTodoListIdParam;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;

/**
 * Provides methods for easily creating of links with given URL params.
 *
 * @author Oleg Barmin
 */
class TestRoutesProvider {
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

    static String getTaskUrl(TodoListId todoListId, TaskId taskId) {
        return format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue());
    }

    static String getTodoListUrl(TodoListId todoListId) {
        return format(TODO_LIST_ROUTE_FORMAT, todoListId.getValue());
    }
}
