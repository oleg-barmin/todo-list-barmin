import {Event, EventBus, EventType, EventTypes} from "../../src/view/event/event";
import {Controller} from "../../src/view/controller";
import {AddTaskRequest} from "../../src/view/event/addTaskRequest";
import {TodoList} from "../../src/model/todo-list";
import {TaskCompletionRequested} from "../../src/view/event/taskCompletionRequested";
import {TaskRemovalRequested} from "../../src/view/event/taskRemovalRequested";
import {TaskUpdateRequested} from "../../src/view/event/taskUpdateRequested";

QUnit.module("EventBus should");
QUnit.test("call ", assert => {
    let transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);

    const firstEventType = new EventType("firstEventType");
    const secondEventType = new EventType("secondEventType");

    const firstEvent = new Event(firstEventType);
    const secondEvent = new Event(secondEventType);

    firstEvent.interaction = 0;
    secondEvent.interaction = 0;

    const callback = occurredEvent => occurredEvent.interaction += 1;

    eventBus.subscribe(firstEventType, callback);
    eventBus.subscribe(secondEventType, callback);

    eventBus.post(firstEvent);

    assert.strictEqual(firstEvent.interaction, 1, "callback once for single post.");

    eventBus.post(firstEvent);
    eventBus.post(firstEvent);

    assert.strictEqual(firstEvent.interaction, 3, "callback twice for two posts.");
    assert.strictEqual(secondEvent.interaction, 0, "call only subscribed for this event callbacks.");
});

QUnit.module("Controller should");
QUnit.test("", assert => {
    let transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);

    const todoList = new TodoList();
    const controller = new Controller(eventBus);
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