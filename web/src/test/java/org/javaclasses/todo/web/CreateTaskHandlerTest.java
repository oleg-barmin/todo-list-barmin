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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.given.IdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test of {@link Task} creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("CreateTaskHandlerTest should")
class CreateTaskHandlerTest extends AbstractPayloadHandlerTest {

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
    @DisplayName("create tasks.")
    void testTaskCreation() {
        Collection<SampleTask> uploadedSampleTasks = new ArrayList<>(bob.getTaskDescriptions()
                                                                        .size());
        // add all tasks of bob
        while (bobDescriptionIterator.hasNext()) {
            TaskId taskId = generateTaskId();
            String taskDescription = bobDescriptionIterator.next();

            bobSpecification.body(new CreateTaskPayload(taskDescription))
                            .post(getTaskUrl(bobTodoListId, taskId))
                            .then()
                            .statusCode(HTTP_OK);

            uploadedSampleTasks.add(new SampleTask(taskId, bobTodoListId, taskDescription));
        }

        Collection<SampleTask> tasks = readTasks(bobTodoListId, bobSpecification);
        assertTrue(tasks.containsAll(uploadedSampleTasks),
                   "all added task should be uploaded.");
    }

    @Test
    @DisplayName("response with 500 status code if given task description is empty.")
    void testCreationTaskWithEmptyDescription() {
        TaskId taskId = generateTaskId();
        String taskDescription = "";

        bobSpecification.body(new CreateTaskPayload(taskDescription))
                        .post(getTaskUrl(bobTodoListId, taskId))
                        .then()
                        .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 500 status code if given task description is filled only with spaces.")
    void testCreationTaskWithOnlySpacesDescription() {
        TaskId taskId = generateTaskId();
        String taskDescription = "    ";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        bobSpecification.body(payload)
                        .post(getTaskUrl(bobTodoListId, taskId))
                        .then()
                        .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("response with 403 status code when create task with existing ID.")
    void testCreationTaskWithExistingId() {
        TaskId taskId = generateTaskId();
        Iterator<String> descriptionIterator = bob.getTaskDescriptions()
                                                  .iterator();
        CreateTaskPayload payload = new CreateTaskPayload(descriptionIterator.next());

        addTask(taskId, bobTodoListId, descriptionIterator.next(), bobSpecification);

        bobSpecification.body(payload)
                        .post(getTaskUrl(bobTodoListId, taskId))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("response with 403 status code when create task with non-existing to-do-list ID.")
    void testCreationTaskWithNonExistingId() {
        TodoListId nonExistingTodoListId = generateTodoListId();

        CreateTaskPayload payload = new CreateTaskPayload(bobDescriptionIterator.next());

        bobSpecification.body(payload)
                        .post(getTaskUrl(nonExistingTodoListId, generateTaskId()))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("response with 403 status code when create task in other bob to-do list.")
    void testCreationTaskInOtherUserTodoList() {
        RequestSpecification aliceSpecification = getRequestSpecificationFor(getAlice());
        TodoListId aliceTodoListId = generateTodoListId();
        addTodoList(aliceTodoListId, aliceSpecification);

        addTodoList(aliceTodoListId, aliceSpecification);

        CreateTaskPayload payload = new CreateTaskPayload(bobDescriptionIterator.next());

        bobSpecification.body(payload)
                        .post(getTaskUrl(aliceTodoListId, generateTaskId()))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        TaskId taskId = generateTaskId();

        addTodoList(bobTodoListId, bobSpecification);

        CreateTaskPayload payload = new CreateTaskPayload(bobDescriptionIterator.next());

        return specification.body(payload)
                            .post(getTaskUrl(bobTodoListId, taskId));
    }

    @Override
    Response sendRequestWithBody(String requestBody, RequestSpecification specification) {
        return specification.body(requestBody)
                            .post(getTaskUrl(bobTodoListId, generateTaskId()));
    }
}
