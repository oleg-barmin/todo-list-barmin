package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleTask;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.given.IdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

/**
 * Integration test of {@code Task} removing with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("RemoveTaskHandler should")
class RemoveTaskHandlerTest extends AbstractSecuredHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final TodoListId bobTodoListId = generateTodoListId();
    private final RequestSpecification bobSpecification = getRequestSpecificationFor(bob);

    @BeforeEach
    void createTodoList() {
        addTodoList(bobTodoListId, bobSpecification);
    }

    @Test
    @DisplayName("remove tasks from system by ID.")
    void testRemoveTaskById() {
        Collection<SampleTask> bobTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification);

        SampleTask taskToRemove = bobTasks.iterator()
                                          .next();

        bobSpecification.delete(getTaskUrl(bobTodoListId, taskToRemove.getTaskId()))
                        .then()
                        .statusCode(HTTP_OK);

        Collection<SampleTask> taskInSystem = readTasks(bobTodoListId, bobSpecification);

        Assertions.assertFalse(taskInSystem.contains(taskToRemove), "task should be removed, but it don't.");

    }

    @Test
    @DisplayName("response with 403 status code when removing non-existing task.")
    void testRemoveTaskNonExisting() {
        TaskId taskId = generateTaskId();
        TodoListId nonExistingTodoList = generateTodoListId();

        bobSpecification.delete(getTaskUrl(nonExistingTodoList, taskId))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    @DisplayName("response with 403 status code when removing to-do list of other user.")
    void testRemoveTaskFromOtherUserTodoList() {
        SampleUser alice = getAlice();
        RequestSpecification aliceSpecification = getRequestSpecificationFor(alice);

        TodoListId aliceTodoListId = generateTodoListId();
        addTodoList(aliceTodoListId, aliceSpecification);

        Iterator<SampleTask> aliceTasks = addAllTasksOf(alice, aliceTodoListId, aliceSpecification).iterator();

        SampleTask taskToRemove = aliceTasks.next();

        bobSpecification.delete(getTaskUrl(aliceTodoListId, taskToRemove.getTaskId()))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        TaskId taskId = generateTaskId();
        addTask(taskId, bobTodoListId, bob.getTaskDescriptions()
                                          .iterator()
                                          .next(), specification);

        return specification.delete(getTaskUrl(bobTodoListId, taskId));
    }
}
