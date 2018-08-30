if (typeof(require) !== 'undefined') {
    todoListModule = require('../src/todo-list.js');
    TodoList = todoListModule.TodoList;
}

QUnit.module("TodoList should ");
QUnit.test("add ", assert => {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);
    let firstTask = todoList._getTaskById(firstTaskID);

    assert.equal(firstTask.content, firstTaskContent, "task by content.");

    todoList = new TodoList();
    firstTaskContent = "first task";
    firstTaskID = todoList.add(firstTaskContent);
    firstTask = todoList._getTaskById(firstTaskID);

    const secondTaskContent = "second task";
    const secondTaskID = todoList.add(secondTaskContent);
    const secondTask = todoList._getTaskById(secondTaskID);

    assert.ok(
        firstTask.content === firstTaskContent
        && secondTask.content === secondTaskContent
        && todoList._tasksArray.length === 2,
        " multiple tasks by ID."
    );
});

QUnit.test("remove ", assert => {
    const todoList = new TodoList();

    const firstTaskContent = "first task";
    const firstTaskID = todoList.add(firstTaskContent);

    const secondTaskContent = "second task";
    todoList.add(secondTaskContent);

    todoList.remove(firstTaskID);

    assert.ok(
        todoList._tasksArray.length === 1
        && todoList._getTaskById,
        "task from list by ID."
    );
});

QUnit.test("update ", assert => {
    const todoList = new TodoList();

    const firstTaskContent = "first task";
    const firstTaskID = todoList.add(firstTaskContent);
    todoList._getTaskById(firstTaskID);

    const secondTaskContent = "second task";
    const secondTaskID = todoList.add(secondTaskContent);
    const secondTask = todoList._getTaskById(secondTaskID);

    const newSecondTaskContent = "new second task content";
    todoList.update(secondTaskID, newSecondTaskContent);

    assert.equal(
        secondTask.content,
        newSecondTaskContent,
        "task list by ID."
    );
});


QUnit.test("complete ", assert => {
    const todoList = new TodoList();

    const taskID = todoList.add("first task");
    const task = todoList._getTaskById(taskID);

    todoList.complete(taskID);

    assert.ok(task.completed, "task by ID.");
});


QUnit.test("sort ", async function (assert) {
    const todoList = new TodoList();

    const firstTaskId = todoList.add("first task");
    const secondTaskId = todoList.add("second task");
    const thirdTaskId = todoList.add("third task");
    const fourthTaskId = todoList.add("fourth task");

    const firstTask = todoList._getTaskById(firstTaskId);
    const secondTask = todoList._getTaskById(secondTaskId);
    const thirdTask = todoList._getTaskById(thirdTaskId);
    const fourthTask = todoList._getTaskById(fourthTaskId);

    let tasksArray = todoList.all();
    let expectedArray = [
        fourthTask,
        thirdTask,
        secondTask,
        firstTask
    ];

    assert.propEqual(
        tasksArray,
        expectedArray,
        "by last update date."
    );


    todoList.complete(thirdTaskId);
    todoList.complete(fourthTaskId);

    tasksArray = todoList.all();

    expectedArray = [
        secondTask,
        firstTask,
        fourthTask,
        thirdTask
    ];

    assert.propEqual(
        tasksArray,
        expectedArray,
        " when task was marked as done."
    );

    todoList.update(firstTaskId, "updated third task");

    tasksArray = todoList.all();
    expectedArray = [
        firstTask,
        secondTask,
        fourthTask,
        thirdTask
    ];

    assert.propEqual(tasksArray, expectedArray, "when task was updated.");
});

QUnit.test("throw ", assert => {
    const todoList = new TodoList();

    const taskId = todoList.add("first task");
    todoList.add("second task");
    const thirdTaskId = todoList.add("third task");
    todoList.complete(thirdTaskId);

    assert.throws(
        () => todoList.add(undefined),
        new TaskContentError(undefined),
        "TaskContentError if add task with undefined content."
    );

    assert.throws(
        () => todoList.add(""),
        new TaskContentError(""),
        "TaskNotFoundError if add task with empty content."
    );

    assert.throws(
        () => todoList.complete(undefined),
        new ParameterIsNotDefinedError(undefined, "task ID"),
        "ParameterIsNotDefinedError if complete argument is undefined."
    );

    assert.throws(
        () => todoList.complete(""),
        new ParameterIsNotDefinedError("", "task ID"),
        "ParameterIsNotDefinedError if complete argument is empty string."
    );

    assert.throws(
        () => todoList.complete("123"),
        new TaskNotFoundError("123"),
        "TaskNotFoundError if complete argument is non-existing ID."
    );

    assert.throws(
        () => todoList.complete(thirdTaskId),
        new TaskAlreadyCompletedException(thirdTaskId),
        "TaskAlreadyCompletedException when trying to complete completed task."
    );

    assert.throws(
        () => todoList.update(undefined, "content"),
        new ParameterIsNotDefinedError(undefined, "task ID"),
        "ParameterIsNotDefinedError if update first argument is undefined."
    );

    assert.throws(
        () => todoList.update("", "content"),
        new ParameterIsNotDefinedError("", "task ID"),
        "ParameterIsNotDefinedError if update first argument is empty string."
    );

    assert.throws(
        () => todoList.update("123", "content"),
        new TaskNotFoundError("123"),
        "TaskNotFoundError if update first argument is non-existing ID."
    );

    assert.throws(
        () => todoList.update(taskId, undefined),
        new TaskContentError(undefined),
        "TaskContentError if update second argument is undefined."
    );

    assert.throws(
        () => todoList.update(taskId, ""),
        new TaskContentError(""),
        "TaskContentError if update second argument is empty string."
    );

    assert.throws(
        () => todoList.remove(undefined),
        new ParameterIsNotDefinedError(undefined, "task ID"),
        "ParameterIsNotDefinedError if remove argument is undefined."
    );

    assert.throws(
        () => todoList.remove(""),
        new ParameterIsNotDefinedError("", "task ID"),
        "ParameterIsNotDefinedError if remove argument is empty string."
    );

    assert.throws(
        () => todoList.remove("123"),
        new TaskNotFoundError("123"),
        "TaskNotFoundError if remove non-existing task."
    );

});
