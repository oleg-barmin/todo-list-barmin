package org.javaclasses.todo.web;

import com.google.common.collect.Lists;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleTask;
import org.javaclasses.todo.web.given.SampleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.given.IdGenerator.generateTodoListId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTodoListUrl;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getAlice;
import static org.javaclasses.todo.web.given.UserSourceTestEnv.getBob;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test of retrieving all {@link Task TasksIdGenerator} of to-do list with specified ID creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("ReadTaskHandler should")
class ReadTasksHandlerTest extends AbstractSecuredHandlerTest {

    // Bob data
    private final SampleUser bob = getBob();
    private final TodoListId bobTodoListId = generateTodoListId();
    private final RequestSpecification bobSpecification = getRequestSpecificationFor(bob);

    @BeforeEach
    void createTodoList() {
        addTodoList(bobTodoListId, bobSpecification);
    }

    @Test
    @DisplayName("read tasks from to-do list.")
    void testReadTasks() {
        Collection<SampleTask> uploadedSampleTasks = addAllTasksOf(bob, bobTodoListId, bobSpecification);

        Response response = bobSpecification.get(getTodoListUrl(bobTodoListId));

        response.then()
                .statusCode(HTTP_OK);

        Collection<SampleTask> receivedTasks = toSampleTasksCollection(response.body()
                                                                               .as(Task[].class));
        assertTrue(uploadedSampleTasks.containsAll(Lists.newArrayList(receivedTasks)),
                   "provide all tasks of to-do list, but it don't.");
    }

    @Test
    @DisplayName("response with 403 status code when reading tasks from other user to-do list.")
    void testReadTasksOtherUserTodoList() {
        SampleUser alice = getAlice();
        RequestSpecification aliceSpecification = getRequestSpecificationFor(alice);

        TodoListId aliceTodoListId = generateTodoListId();
        addTodoList(aliceTodoListId, aliceSpecification);

        Iterator<SampleTask> aliceTasksIterator = addAllTasksOf(alice, aliceTodoListId, aliceSpecification).iterator();

        bobSpecification.get(getTaskUrl(aliceTodoListId, aliceTasksIterator.next()
                                                                           .getTaskId()));
    }

    @Test
    @DisplayName("forbid to read tasks from non-existing to-do lists.")
    void testReadTasksNonExistingTodoList() {
        TodoListId nonExistingTodoList = generateTodoListId();

        bobSpecification.get(getTodoListUrl(nonExistingTodoList))
                        .then()
                        .statusCode(HTTP_FORBIDDEN);

    }

    @Override
    Response sendRequest(RequestSpecification specification) {
        return specification.get(getTodoListUrl(bobTodoListId));
    }
}
