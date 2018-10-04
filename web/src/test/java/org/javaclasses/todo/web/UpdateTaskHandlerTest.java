package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleTask;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.given.IdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("UpdateTaskHandler should")
class UpdateTaskHandlerTest extends AbstractPayloadHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final TodoListId bobTodoListId = generateTodoListId();
    private final Iterator<String> bobDescriptionIterator = bob.getTaskDescriptions()
                                                               .iterator();
    private final RequestSpecification bobSpecification = getRequestSpecificationFor(bob);

    @BeforeEach
    void createTodoList() {
        addTodoList(bobTodoListId, bobSpecification);
    }

    @Test
    @DisplayName("update tasks in the system.")
    void testUpdateTask() {
        Iterator<SampleTask> bobTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification).iterator();

        SampleTask taskToUpdate = bobTasks.next();

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "updated task.");

        bobSpecification.body(payload)
                        .put(getTaskUrl(bobTodoListId, taskToUpdate.getTaskId()))
                        .then()
                        .statusCode(HTTP_OK);

        Task updatedTask = readTask(bobTodoListId, taskToUpdate.getTaskId(), bobSpecification);

        assertEquals(payload.getTaskDescription(), updatedTask.getDescription(),
                     "task description should be updated, but it don't.");
        assertFalse(updatedTask.isCompleted(), "task status should remain uncompleted, but it don't.");
    }

    @Test
    @DisplayName("response with 500 status code when update tasks with empty description.")
    void testUpdateTaskWithEmptyDescription() {
        Iterator<SampleTask> bobTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification).iterator();

        SampleTask taskToUpdate = bobTasks.next();
        String newTaskDescription = "";

        TaskUpdatePayload payload = new TaskUpdatePayload(false, newTaskDescription);

        bobSpecification.body(payload)
                        .put(getTaskUrl(bobTodoListId, taskToUpdate.getTaskId()))
                        .then()
                        .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 500 status code when update completed task.")
    void testUpdateCompletedTask() {
        Iterator<SampleTask> bobTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification).iterator();

        SampleTask taskToUpdate = bobTasks.next();

        TaskUpdatePayload payload = new TaskUpdatePayload(true, taskToUpdate.getDescription());

        bobSpecification.body(payload)
                        .put(getTaskUrl(bobTodoListId, taskToUpdate.getTaskId()));

        payload = new TaskUpdatePayload(true, taskToUpdate.getDescription());

        bobSpecification.body(payload)
                        .put(getTaskUrl(bobTodoListId, taskToUpdate.getTaskId()))
                        .then()
                        .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 403 status code when update non-existing task.")
    void testUpdateNonExistingTask() {
        Iterator<SampleTask> bobTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification).iterator();

        SampleTask taskToUpdate = bobTasks.next();

        TaskId nonExistingTask = generateTaskId();

        TaskUpdatePayload payload = new TaskUpdatePayload(false, taskToUpdate.getDescription());

        bobSpecification.body(payload)
                        .put(getTaskUrl(bobTodoListId, nonExistingTask))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("response with status code 403 when try to update task from other user to-do list.")
    void testUpdateTaskFromOtherUserTodoList() {
        SampleUser alice = getAlice();
        RequestSpecification aliceSpecification = getRequestSpecificationFor(alice);

        TodoListId aliceTodoListId = generateTodoListId();
        addTodoList(aliceTodoListId, aliceSpecification);

        Iterator<SampleTask> aliceTasksIterator = addAllTasksOf(alice, aliceTodoListId, aliceSpecification).iterator();

        SampleTask aliceTask = aliceTasksIterator.next();

        TaskUpdatePayload payload = new TaskUpdatePayload(false, bobDescriptionIterator.next());

        bobSpecification.body(payload)
                        .put(getTaskUrl(aliceTodoListId, aliceTask.getTaskId()))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);

    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        TaskId taskId = generateTaskId();
        addTask(taskId, bobTodoListId, bobDescriptionIterator.next(), specification);
        TaskUpdatePayload payload = new TaskUpdatePayload(false, bobDescriptionIterator.next());
        return specification.body(payload)
                            .put(getTaskUrl(bobTodoListId, taskId));
    }

    @Override
    Response sendRequestWithBody(String requestBody, RequestSpecification specification) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId, specification);
        addTask(taskId, todoListId, bobDescriptionIterator.next(), specification);

        return specification.body(requestBody)
                            .put(getTaskUrl(todoListId, taskId));
    }
}
