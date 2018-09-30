package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.TestUsers.USER_1;

@DisplayName("UpdateTaskHandler should")
class UpdateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("update tasks in the system.")
    void testUpdateTask() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write tests on update task.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "complete this test");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(describedAs("return status code 200, if" +
                                                     " update completed successfully",
                                             is(HTTP_OK)));
    }

    @Test
    @DisplayName("response with 500 status code when update tasks with empty description.")
    void testUpdateTaskWithEmptyDescription() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "task to test update with");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(describedAs("return status code 500",
                                             is(HTTP_INTERNAL_ERROR)));
    }

    @Test
    @DisplayName("response with 500 status code when update tasks with only spaces description.")
    void testUpdateTaskWithOnlySpacesDescription() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "task to test update with only spaces description");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "    ");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(describedAs("return status code 500 when description is only spaces",
                                             is(HTTP_INTERNAL_ERROR)));
    }

    @Test
    @DisplayName("response with 500 status code when update completed task.")
    void testUpdateCompletedTask() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "task to complete");

        TaskUpdatePayload payload = new TaskUpdatePayload(true, "completed task");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId));

        payload = new TaskUpdatePayload(true, "new description of completed task");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(describedAs("return status code 500 when update completed task",
                                             is(HTTP_INTERNAL_ERROR)));
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on task update.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

        return specification.body(payload)
                            .put(getTaskUrl(todoListId, taskId));
    }
}
