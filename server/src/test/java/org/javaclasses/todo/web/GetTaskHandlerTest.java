package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.TestUsers.USER_1;

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
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());

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

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on find task by ID.");

        return specification.get(getTaskUrl(todoListId, taskId));
    }
}
