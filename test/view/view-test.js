import {EventBus, EventTypeEnumeration} from "../../src/view/event/event";
import {EventType} from "../../src/view/event/event";
import {Event} from "../../src/view/event/event";
import {Controller} from "../../src/view/controller";
import {NewTaskAdded} from "../../src/view/event/newTaskAdded";
import {AddTaskRequest} from "../../src/view/event/addTaskRequest";
import {TodoList} from "../../src/model/todo-list";

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

    let newTaskAddedEventWasProduced = false;
    let newTaskValidationFailed = false;

    eventBus.subscribe(EventTypeEnumeration.NewTaskAdded, () => newTaskAddedEventWasProduced = true);
    eventBus.subscribe(EventTypeEnumeration.NewTaskValidationFailed, () => newTaskValidationFailed = true);

    controller.todoList = todoList;
    const taskDescription = "new task";
    eventBus.post(new AddTaskRequest(taskDescription));
    eventBus.post(new AddTaskRequest(""));


    assert.strictEqual(todoList.all()[0].description, taskDescription, "add new task to TodoList.");
    assert.ok(newTaskAddedEventWasProduced, "post a newTaskAddedEvent after success AddTaskRequest process.");
    assert.ok(newTaskAddedEventWasProduced, "post a newTaskAddedEventWasProduced if given task description is empty.");

});