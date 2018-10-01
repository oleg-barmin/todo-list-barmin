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
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.given.TasksIdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UpdateTaskHandler should")
class UpdateTaskHandlerTest extends AbstractPayloadHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("update tasks in the system.")
    void testUpdateTask() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write tests on update task.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "complete this test");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("response with 500 status code when update tasks with empty description.")
    void testUpdateTaskWithEmptyDescription() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "task to test update with");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 500 status code when update tasks with only spaces description.")
    void testUpdateTaskWithOnlySpacesDescription() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

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
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

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

    @Test
    @DisplayName("response with 403 status code when update non-existing task.")
    void testUpdateNonExistingTask() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "non-existing task");

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("update only description.")
    void testUpdateOnlyDescription() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
        String taskDescription = "task with false status";

        addTodoList(todoListId);
        addTask(taskId, todoListId, taskDescription);
        Task notUpdatedTask = readTask(todoListId, taskId);

        String newTaskDescription = "new description of task";
        TaskUpdatePayload payload = new TaskUpdatePayload(newTaskDescription);

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_OK);

        Task updatedTask = readTask(todoListId, taskId);

        assertEquals(newTaskDescription, updatedTask.getDescription(),
                     "task description should be updated, but it don't.");
        assertEquals(notUpdatedTask.isCompleted(), updatedTask.isCompleted(),
                     "task status should not be updated, but it was.");
    }

    @Test
    @DisplayName("update only status.")
    void testUpdateOnlyStatus() {
        setTokenToRequestSpecification();

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
        String taskDescription = "old task";

        addTodoList(todoListId);
        addTask(taskId, todoListId, taskDescription);
        Task notUpdatedTask = readTask(todoListId, taskId);

        TaskUpdatePayload payload = new TaskUpdatePayload(true);

        specification.body(payload)
                     .put(getTaskUrl(todoListId, taskId))
                     .then()
                     .statusCode(HTTP_OK);

        Task updatedTask = readTask(todoListId, taskId);

        assertEquals(notUpdatedTask.getDescription(), updatedTask.getDescription(),
                     "task description should not be updated, but it is.");
        assertTrue(updatedTask.isCompleted(), "task status should be updated, but it don't.");
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "implement sendRequest method in task update handler test.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

        return specification.body(payload)
                            .put(getTaskUrl(todoListId, taskId));
    }

    @Override
    Response sendEmptyPayloadRequest() {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);
        addTask(taskId, todoListId, "any description");

        return specification.body("")
                            .put(getTaskUrl(todoListId, taskId));
    }
}
