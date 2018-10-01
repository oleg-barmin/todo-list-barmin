package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.given.TasksIdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;

/**
 * Integration test of {@code Task} removing with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("RemoveTaskHandler should")
class RemoveTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("remove tasks from system by ID.")
    void testRemoveTaskById() {
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write tests on remove task by ID.");

        Response response = specification.delete(getTaskUrl(todoListId, taskId));

        response.then()
                .statusCode(describedAs("return status code 200, when " +
                                                "signed in user removes task by ID from his to-do list.",
                                        is(HTTP_OK)));
    }

    @Test
    @DisplayName("response with 403 status code when removing non-existing task.")
    void testRemoveTaskNonExisting() {
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);

        specification.delete(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("response with 403 status code when removing to-do list of other user.")
    void testRemoveTaskFromOtherUserTodoList() {
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
        addTodoList(todoListId);
        addTask(taskId, todoListId, "buy bread");

        RequestSpecification newSpecification = getNewSpecification();

        TaskId taskIdActor = generateTaskId();
        TodoListId todoListIdActor = generateTodoListId();
        addTodoList(todoListIdActor, newSpecification);
        addTask(taskIdActor, todoListIdActor, "buy bread for actor 1", newSpecification);

        newSpecification.delete(getTaskUrl(todoListId, taskId))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on remove task by ID.");

        return specification.delete(getTaskUrl(todoListId, taskId));
    }
}
