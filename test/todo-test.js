if (typeof(require) !== 'undefined') {
    todoListModule = require('../src/todo-list.js');
    TodoList = todoListModule.TodoList;
}

QUnit.module("Preconditions should");
QUnit.test("throw ", assert => {
    assert.throws(
        () => Preconditions.isDefined(undefined, "any parameter"),
        new ParameterIsNotDefinedException(undefined, "any parameter"),
        "ParameterIsNotDefinedException when isDefined() argument is undefined."
    );

    assert.throws(
        () => Preconditions.isDefined(null, "any parameter"),
        new ParameterIsNotDefinedException(null, "any parameter"),
        "ParameterIsNotDefinedException when isDefined() argument is null."
    );

    assert.throws(
        () => Preconditions.checkStringIsDefinedAndNotEmpty(undefined, "any string"),
        new EmptyStringException(undefined, "any string"),
        "EmptyStringException when checkStringIsDefinedAndNotEmpty() argument is undefined."
    );

    assert.throws(
        () => Preconditions.checkStringIsDefinedAndNotEmpty(null, "any string"),
        new EmptyStringException(null, "any string"),
        "EmptyStringException when checkStringIsDefinedAndNotEmpty() argument is null."
    );

    assert.throws(
        () => Preconditions.checkStringIsDefinedAndNotEmpty("", "any string"),
        new EmptyStringException("", "any string"),
        "EmptyStringException when checkStringIsDefinedAndNotEmpty() argument is empty."
    );

    assert.throws(
        () => Preconditions.checkStringIsDefinedAndNotEmpty("    ", "any string"),
        new EmptyStringException("", "any string"),
        "EmptyStringException when checkStringIsDefinedAndNotEmpty() argument contains only of multiple spaces."
    );

    const futureDate = new Date();
    futureDate.setFullYear(2020);
    assert.throws(
        () => Preconditions.validateDate(futureDate, "any date"),
        new TaskDateException(futureDate),
        "TaskDateException when validateDate() argument is date that point to future."
    );
});

QUnit.module("TodoList should ");
QUnit.test("add ", assert => {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);
    let firstTask = todoList._getTaskById(firstTaskID);

    assert.equal(firstTask.description, firstTaskContent, "task by content.");

    todoList = new TodoList();
    firstTaskContent = "first task";
    firstTaskID = todoList.add(firstTaskContent);
    firstTask = todoList._getTaskById(firstTaskID);

    const secondTaskContent = "second task";
    const secondTaskID = todoList.add(secondTaskContent);
    const secondTask = todoList._getTaskById(secondTaskID);

    assert.ok(
        firstTask.description === firstTaskContent
        && secondTask.description === secondTaskContent
        && todoList._tasksArray.length === 2,
        " multiple tasks by ID."
    );
});

QUnit.test("remove ", assert => {
    const todoList = new TodoList();

    const firstTaskContent = "first task";
    const firstTaskID = todoList.add(firstTaskContent);

    const secondTaskContent = "second task";
    const secondTaskID = todoList.add(secondTaskContent);

    todoList.remove(firstTaskID);

    assert.ok(
        todoList._tasksArray.length === 1
        && todoList._getTaskById(secondTaskID),
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
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
    const todoList = new TodoList();

    const firstTaskId = todoList.add("first task");
    await sleep(1);

    const secondTaskId = todoList.add("second task");
    await sleep(1);

    const thirdTaskId = todoList.add("third task");
    await sleep(1);

    const fourthTaskId = todoList.add("fourth task");
    await sleep(1);

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
        new EmptyStringException(undefined, "task content"),
        "EmptyStringException if add task with undefined content."
    );

    assert.throws(
        () => todoList.add(""),
        new EmptyStringException("", "task content"),
        "EmptyStringException if add task with empty content."
    );

    assert.throws(
        () => todoList.complete(undefined),
        new ParameterIsNotDefinedException(undefined, "task ID"),
        "ParameterIsNotDefinedException if complete argument is undefined."
    );

    assert.throws(
        () => todoList.complete(new TaskId("")),
        new EmptyStringException("", "ID"),
        "EmptyStringException if complete argument is empty string."
    );

    assert.throws(
        () => todoList.complete(new TaskId("123")),
        new TaskNotFoundException(new TaskId("123")),
        "TaskNotFoundException if complete argument is non-existing ID."
    );

    assert.throws(
        () => todoList.complete(thirdTaskId),
        new TaskAlreadyCompletedException(thirdTaskId),
        "TaskAlreadyCompletedException when trying to complete completed task."
    );

    assert.throws(
        () => todoList.update(undefined, "content"),
        new ParameterIsNotDefinedException(undefined, "task ID"),
        "ParameterIsNotDefinedException if update first argument is undefined."
    );

    assert.throws(
        () => todoList.update("", "content"),
        new ParameterIsNotDefinedException("", "task ID"),
        "ParameterIsNotDefinedException if update first argument is empty string."
    );

    assert.throws(
        () => todoList.update(new TaskId("123"), "content"),
        new TaskNotFoundException(new TaskId("123")),
        "TaskNotFoundException if update first argument is non-existing ID."
    );

    assert.throws(
        () => todoList.update(taskId, undefined),
        new EmptyStringException(undefined, "updated content"),
        "EmptyStringException if update second argument is undefined."
    );

    assert.throws(
        () => todoList.update(taskId, ""),
        new EmptyStringException("", "updated content"),
        "EmptyStringException if update second argument is empty string."
    );

    assert.throws(
        () => todoList.remove(undefined),
        new ParameterIsNotDefinedException(undefined, "task ID"),
        "ParameterIsNotDefinedException if remove argument is undefined."
    );

    assert.throws(
        () => todoList.remove(new TaskId("")),
        new EmptyStringException("", "ID"),
        "ParameterIsNotDefinedException if remove argument is empty string."
    );

    assert.throws(
        () => todoList.remove(new TaskId("123")),
        new TaskNotFoundException(new TaskId("123")),
        "TaskNotFoundException if remove non-existing task."
    );

});
