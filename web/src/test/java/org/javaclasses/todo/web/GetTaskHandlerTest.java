package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.given.TasksIdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;

/**
 * Integration test retrieving of {@link Task} with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("GetTaskHandler should")
class GetTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("read tasks by ID.")
    void testGetTaskById() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write tests on find task by ID.");

        specification.get(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(describedAs("return status code 200, " +
                                                     "when signed in user find tasks by ID from his to-do list.",
                                             is(HTTP_OK)))
                     .body(describedAs("provide task by ID, but it don't.",
                                       notNullValue(Task.class)));
    }

    @Test
    @DisplayName("return 403 status code when read tasks from other user to-do list.")
    void testGetTaskFromOtherUserTodoList() {
        setTokenToRequestSpecification(specification);

        TaskId firstUserTaskId = generateTaskId();
        TodoListId firstUserTodoListId = generateTodoListId();

        addTodoList(firstUserTodoListId);
        addTask(firstUserTaskId, firstUserTodoListId, "buy bread");

        RequestSpecification secondUserSpec = getNewSpecification();

        secondUserSpec.get(getTaskUrl(firstUserTodoListId, firstUserTaskId))
                      .then()
                      .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on find task by ID.");

        return specification.get(getTaskUrl(todoListId, taskId));
    }
}
