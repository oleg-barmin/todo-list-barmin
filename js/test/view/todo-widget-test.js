import {TodoList} from "../../src/model/todo-list";
import {TodoWidget} from "../../src/view/todoWidget";
import {EventBus} from "../../src/event/event";
import {TaskEditingStarted} from "../../src/event/taskEditingStarted";
import {TaskEdit} from "../../src/view/taskEdit";

QUnit.module("TasksWidget should");
QUnit.test("merge TaskView array and given Tasks array and preserve", assert => {
    const transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);
    const todoWidgetContainer = $("#todoWidgetContainer");

    const todoList = new TodoList();
    const todoWidget = new TodoWidget(todoWidgetContainer, eventBus);

    todoList.add("first task");
    todoList.add("second task");
    todoList.add("third task");
    todoList.add("fourth task");

    let expectedTasksOrder = todoList.all();
    let mergedTaskViewArray = todoWidget._merge([], todoList.all());
    const mergedTaskArray = mergedTaskViewArray.map(taskView => taskView.task);

    assert.deepEqual(mergedTaskArray, expectedTasksOrder, "order of tasks as in Task array.");


    eventBus.post(new TaskEditingStarted(expectedTasksOrder[0].id));
    todoList.add("fifth task");
    mergedTaskViewArray = todoWidget._merge(mergedTaskViewArray, todoList.all());

    assert.ok(mergedTaskViewArray[1].currentState instanceof TaskEdit, "state of one TaskView when task was added.");


    expectedTasksOrder = todoList.all();
    eventBus.post(new TaskEditingStarted(expectedTasksOrder[4].id));
    eventBus.post(new TaskEditingStarted(expectedTasksOrder[3].id));
    todoList.add("sixth task");
    mergedTaskViewArray = todoWidget._merge(mergedTaskViewArray, todoList.all());

    assert.ok(mergedTaskViewArray[2].currentState instanceof TaskEdit &&
        mergedTaskViewArray[4].currentState instanceof TaskEdit &&
        mergedTaskViewArray[5].currentState instanceof TaskEdit,
        "state of multiple TaskViews when task was added."
    );

    const newInputOfTask = "new input";
    mergedTaskViewArray[2].currentInput = newInputOfTask;
    mergedTaskViewArray = todoWidget._merge(mergedTaskViewArray, todoList.all());

    assert.equal(mergedTaskViewArray[2].currentInput, newInputOfTask, "current input in TaskEdit state of TaskView.");
});