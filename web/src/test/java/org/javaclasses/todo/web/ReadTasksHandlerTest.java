package org.javaclasses.todo.web;

import com.google.common.collect.Lists;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;
import static org.javaclasses.todo.web.given.TasksIdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTodoListUrl;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test of retrieving all {@link Task TasksIdGenerator} of to-do list with specified ID creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("ReadTaskHandler should")
class ReadTasksHandlerTest extends AbstractSecuredHandlerTest {
    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("read tasks from to-do list.")
    void testReadTasks() {
        setTokenToRequestSpecification();

        TodoListId todoListId = generateTodoListId();
        TaskId firstTaskId = generateTaskId();
        TaskId secondTaskId = generateTaskId();

        addTodoList(todoListId);

        addTask(firstTaskId, todoListId, "write tests on read tasks.");
        addTask(secondTaskId, todoListId, "second task to do");

        Task firstTask = readTask(todoListId, firstTaskId);
        Task secondTask = readTask(todoListId, secondTaskId);

        Collection<Task> addedTasks = new LinkedList<>();
        addedTasks.add(firstTask);
        addedTasks.add(secondTask);

        Response response = specification.get(getTodoListUrl(todoListId));

        response.then()
                .statusCode(describedAs("return status code 200, when" +
                                                " signed in user read tasks from his to-do list.",
                                        is(HTTP_OK)));

        Task[] receivedTasks = response.body()
                                       .as(Task[].class);
        assertTrue(addedTasks.containsAll(Lists.newArrayList(receivedTasks)),
                   "provide all tasks of to-do list, but it don't.");
    }

    @Test
    @DisplayName("forbid to read tasks from non-existing to-do lists.")
    void testReadTasksNonExistingTodoList() {
        setTokenToRequestSpecification();

        Response response = specification.get("/lists/123414/" + UUID.randomUUID()
                                                                     .toString());

        assertEquals(HTTP_FORBIDDEN, response.getStatusCode(),
                     "return status code 403, when signed in user read tasks from non-existing to-do list.");
    }

    @Override
    Response sendRequest(UserId userId) {
        TodoListId todoListId = generateTodoListId();
        TaskId firstTaskId = generateTaskId();
        TaskId secondTaskId = generateTaskId();

        addTodoList(todoListId);
        addTask(firstTaskId, todoListId, "write negative cases tests on read tasks.");
        addTask(secondTaskId, todoListId, "write more negative tests");

        return specification.get(getTodoListUrl(todoListId));
    }
}
