import {EventBus, EventTypes} from "../src/event/event";
import {DashboardController} from "../src/dashboardController";
import {AddTaskRequest} from "../src/event/addTaskRequest";
import {TodoList} from "../src/model/todo-list";
import {TaskCompletionRequested} from "../src/event/taskCompletionRequested";
import {TaskRemovalRequested} from "../src/event/taskRemovalRequested";
import {TaskUpdateRequested} from "../src/event/taskUpdateRequested";

QUnit.module("DashboardController should");
QUnit.test("", assert => {
    let transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);

    const todoList = new TodoList();
    const controller = new DashboardController(eventBus);
    controller.todoList = todoList;

    let newTaskAddedWasPosted = false;
    let newTaskValidationFailedWasPosted = false;
    let taskListUpdatedWasPosted = false;

    eventBus.subscribe(EventTypes.NewTaskAdded, () => newTaskAddedWasPosted = true);
    eventBus.subscribe(EventTypes.NewTaskValidationFailed, () => newTaskValidationFailedWasPosted = true);
    eventBus.subscribe(EventTypes.TaskListUpdated, () => taskListUpdatedWasPosted = true);


    //adding new task test.
    const taskDescription = "new task";

    eventBus.post(new AddTaskRequest(taskDescription));

    let addedTask = todoList.all()[0];

    assert.strictEqual(addedTask.description, taskDescription, "add new task to TodoList after AddTaskRequest was posted.");
    assert.ok(newTaskAddedWasPosted, "post a newTaskAdded after success AddTaskRequest was posted.");
    assert.ok(taskListUpdatedWasPosted, "post a TaskListUpdated event after success AddTaskRequest was posted");

    eventBus.post(new AddTaskRequest(""));
    assert.ok(newTaskValidationFailedWasPosted, "post a NewTaskValidationFailed if AddTaskRequest with empty task description was posted.");


    //updating task test
    let taskUpdateFailedWasPosted = false;
    taskListUpdatedWasPosted = false;
    const newTaskDescription = "new description of task.";

    eventBus.subscribe(EventTypes.TaskUpdateFailed, () => taskUpdateFailedWasPosted = true);
    eventBus.post(new TaskUpdateRequested(addedTask.id, newTaskDescription));
    eventBus.post(new TaskUpdateRequested(addedTask.id, ""));

    addedTask = todoList.all()[0];

    assert.strictEqual(addedTask.description, newTaskDescription, "update task description when TaskUpdateRequested was posted.");
    assert.ok(taskListUpdatedWasPosted, "post a TaskListUpdated after success process of TaskUpdateRequested.");
    assert.ok(taskUpdateFailedWasPosted, "post TaskUpdateFailed if try to update task with empty description.");


    //completion process test
    let taskCompletionFailedWasPosted = false;
    taskListUpdatedWasPosted = false;

    eventBus.subscribe(EventTypes.TaskCompletionFailed, () => taskCompletionFailedWasPosted = true);
    eventBus.post(new TaskCompletionRequested(addedTask.id));
    eventBus.post(new TaskCompletionRequested(addedTask.id));

    addedTask = todoList.all()[0];

    assert.ok(addedTask.completed, "complete task after TaskCompletionRequested was posted.");
    assert.ok(taskListUpdatedWasPosted, "post a TaskListUpdated after TaskCompletionRequested was posted.");
    assert.ok(taskCompletionFailedWasPosted, "post a TaskCompletionFailed if try to complete task more than one time");


    //removing task test
    let taskRemovalFailedWasPosted = false;
    taskListUpdatedWasPosted = false;

    eventBus.subscribe(EventTypes.TaskRemovalFailed, () => taskRemovalFailedWasPosted = true);
    eventBus.post(new TaskRemovalRequested(addedTask.id));
    eventBus.post(new TaskRemovalRequested(addedTask.id));

    assert.ok(todoList.all().length === 0, "remove task after TaskRemovalRequested was posted.");
    assert.ok(taskListUpdatedWasPosted, "post a TaskListUpdated after TaskRemovalRequested was posted.");
    assert.ok(taskRemovalFailedWasPosted, "post a TaskRemovalFailed if try to remove task which doesn't exists in TodoList.")
});