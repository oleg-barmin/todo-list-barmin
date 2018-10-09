package org.javaclasses.todo.web;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.web.given.SampleTask;
import org.javaclasses.todo.web.given.SampleUser;
import org.javaclasses.todo.web.given.TestApplicationEnv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.javaclasses.todo.web.Configurations.getContentType;
import static org.javaclasses.todo.web.given.IdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTodoListUrl;

/**
 * An abstract integration test of all handlers, which starts and stops server on each test method,
 * provides methods to:
 * - read {@link Task Tasks} {@link AbstractHandlerTest#readTask(TodoListId, TaskId, RequestSpecification)};
 * - add {@link Task Tasks} {@link AbstractHandlerTest#addTask(TaskId, TodoListId, String, RequestSpecification)};
 * - add {@code TodoList}s {@link AbstractHandlerTest#addTodoList(TodoListId, RequestSpecification)}.
 *
 * @author Oleg Barmin
 */
//abstract test has nothing to test.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractHandlerTest {

    private final TestApplicationEnv testApplicationEnv = new TestApplicationEnv();
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateToLongAdapter())
                                                      .create();

    /**
     * Creates new {@link RequestSpecification} to test cases with multiple users.
     *
     * @return new {@code RequestSpecification} instance
     */
    RequestSpecification getNewSpecification() {
        return given().port(testApplicationEnv.getApplicationPort())
                      .contentType(getContentType());
    }

    TestApplicationEnv getTestApplicationEnv() {
        return testApplicationEnv;
    }

    @BeforeEach
    void startServer() {
        testApplicationEnv.startServer();
    }

    @AfterEach
    void stopServer() {
        testApplicationEnv.stopServer();
    }

    /**
     * Adds new {@code TodoList} with usage of given {@code RequestSpecification}.
     *
     * @param todoListId           ID of {@code TodoList} to add
     * @param requestSpecification request specification to use
     */
    void addTodoList(TodoListId todoListId, RequestSpecification requestSpecification) {
        requestSpecification.post(getTodoListUrl(todoListId));
    }

    /**
     * Adds all {@code Task}s of given {@link SampleUser} to {@code TodoList} with given ID,
     * using given {@code RequestSpecification}.
     *
     * @param user          user whose {@code Task}s should be added
     * @param todoListId    ID of {@code TodoList} to add tasks into
     * @param specification specification to use
     * @return {@code Collection} of added tasks.
     */
    Collection<SampleTask> addAllTasksOf(SampleUser user, TodoListId todoListId, RequestSpecification specification) {
        Collection<SampleTask> uploadedSampleTasks = new ArrayList<>(user.getTaskDescriptions()
                                                                         .size());
        // add all tasks of bob
        for (String taskDescription : user.getTaskDescriptions()) {
            TaskId taskId = generateTaskId();

            specification.body(new CreateTaskPayload(taskDescription))
                         .post(getTaskUrl(todoListId, taskId));

            uploadedSampleTasks.add(new SampleTask(taskId, todoListId, taskDescription));
        }

        return uploadedSampleTasks;
    }

    /**
     * Adds new {@code Task} with usage of given {@code RequestSpecification}.
     *
     * @param taskId        ID of {@code Task} to add
     * @param todoListId    ID of {@code TodoList} to which tasks belongs
     * @param description   description of {@code Task} to add
     * @param specification request specification to use
     */
    void addTask(TaskId taskId, TodoListId todoListId, String description,
                 RequestSpecification specification) {
        CreateTaskPayload payload = new CreateTaskPayload(description);
        specification.body(payload)
                     .post(getTaskUrl(todoListId, taskId));
    }

    /**
     * Reads {@link Task} from given {@code TodoList} with usage of given {@code RequestSpecification}.
     *
     * @param todoListId    ID of {@code TodoList} to read task from
     * @param taskId        ID desired {@code Task}
     * @param specification specification to use with request
     * @return {@code Task} with given ID
     */
    Task readTask(TodoListId todoListId, TaskId taskId, RequestSpecification specification) {
        String json = specification.get(getTaskUrl(todoListId, taskId))
                                   .asString();
        return gson.fromJson(json, Task.class);
    }

    /**
     * Converts array of {@linkplain Task tasks} to {@code Collection} of {@linkplain SampleTask SampleTasks}.
     *
     * @param tasks array of {@code Task}s to convert.
     * @return {@code Collection} of {@code SampleTask}s
     */
    Collection<SampleTask> toSampleTasksCollection(Task[] tasks) {
        Collection<SampleTask> sampleTasks = new ArrayList<>();
        Lists.newArrayList(tasks)
             .forEach(el -> sampleTasks.add(new SampleTask(el.getId(),
                                                           el.getTodoListId(),
                                                           el.getDescription())));
        return sampleTasks;
    }

    /**
     * Reads all {@code Tasks} from {@code TodoList} with given ID with usage of given {@code RequestSpecification}.
     *
     * @param todoListId    ID of {@code TodoList} to read tasks from
     * @param specification specification to use
     * @return {@code Collection} of all {@code SampleTask}s from {@code TodoList} with given ID.
     */
    Collection<SampleTask> readTasks(TodoListId todoListId, RequestSpecification specification) {
        String json = specification.get(getTodoListUrl(todoListId))
                                   .asString();
        return toSampleTasksCollection(gson.fromJson(json, Task[].class));
    }
}
