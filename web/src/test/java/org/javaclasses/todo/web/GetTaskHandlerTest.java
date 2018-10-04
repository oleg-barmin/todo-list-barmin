package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.javaclasses.todo.web.given.IdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;

/**
 * Integration test retrieving of {@link Task} with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("GetTaskHandler should")
class GetTaskHandlerTest extends AbstractSecuredHandlerTest {

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
    @DisplayName("read tasks by ID.")
    void testGetTaskById() {
        TaskId taskId = generateTaskId();
        addTodoList(bobTodoListId, bobSpecification);

        addTask(taskId, bobTodoListId, bobDescriptionIterator.next(), bobSpecification);

        bobSpecification.get(getTaskUrl(bobTodoListId, taskId))
                        .then()
                        .statusCode(HTTP_OK)
                        .body(notNullValue(Task.class));
    }

    @Test
    @DisplayName("return 403 status code when read tasks from other user to-do list.")
    void testGetTaskFromOtherUserTodoList() {
        SampleUser alice = getAlice();
        RequestSpecification aliceSpecification = getRequestSpecificationFor(alice);
        TodoListId aliceTodoListId = generateTodoListId();
        addTodoList(aliceTodoListId, aliceSpecification);

        TaskId aliceTaskId = generateTaskId();
        addTask(aliceTaskId, aliceTodoListId, alice.getTaskDescriptions()
                                                   .iterator()
                                                   .next(), bobSpecification);

        bobSpecification.get(getTaskUrl(aliceTodoListId, aliceTaskId))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        TaskId taskId = generateTaskId();

        addTodoList(bobTodoListId, bobSpecification);
        addTask(taskId, bobTodoListId, bobDescriptionIterator.next(),
                specification);

        return specification.get(getTaskUrl(bobTodoListId, taskId));
    }
}
