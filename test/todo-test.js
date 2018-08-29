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

    assert.equal(firstTask.content, firstTaskContent, "one task by content.");

    todoList = new TodoList();
    firstTaskContent = "first task";
    firstTaskID = todoList.add(firstTaskContent);
    firstTask = todoList._getTaskById(firstTaskID);

    let secondTaskContent = "second task";
    let secondTaskID = todoList.add(secondTaskContent);
    let secondTask = todoList._getTaskById(secondTaskID);

    assert.ok(
        firstTask.content === firstTaskContent
        && secondTask.content === secondTaskContent
        && todoList.tasksArray.length === 2,
        " multiple tasks by ID."
    );
});

QUnit.test("remove ", assert => {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);

    let secondTaskContent = "second task";
    todoList.add(secondTaskContent);

    todoList.remove(firstTaskID);

    assert.ok(
        todoList.tasksArray.length === 1
        && todoList._getTaskById,
        "task from list by ID."
    );
});

QUnit.test("update ", assert => {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);
    todoList._getTaskById(firstTaskID);

    let secondTaskContent = "second task";
    let secondTaskID = todoList.add(secondTaskContent);
    let secondTask = todoList._getTaskById(secondTaskID);

    let newSecondTaskContent = "new second task content";
    todoList.update(secondTaskID, newSecondTaskContent);

    assert.equal(
        secondTask.content,
        newSecondTaskContent,
        "task list by ID."
    );
});


QUnit.test("complete ", assert => {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let taskID = todoList.add(firstTaskContent);
    let firstTask = todoList._getTaskById(taskID);

    todoList.complete(taskID);

    assert.ok(firstTask.completed, "task.");
});

QUnit.test("sort ", assert => {
    let todoList = new TodoList();

    let firstTaskID = todoList.add("first task");
    todoList.add("second task");
    let thirdTaskID = todoList.add("third task");

    todoList.complete(firstTaskID);

    let allTodo = todoList.all();

    assert.equal(allTodo[2].ID, firstTaskID, " when task is done.");

    todoList.update(thirdTaskID, "updated third task");
    allTodo = todoList.all();

    assert.equal(allTodo[0].ID, thirdTaskID, "when task updated.");
});

QUnit.test("throw ", assert => {
    let todoList = new TodoList();

    let taskId = todoList.add("first task");
    todoList.add("second task");
    todoList.add("third task");

    assert.throws(
        () => todoList.add(undefined),
        new TaskContentError(undefined),
        "TaskContentError if add task with undefined content."
    );

    assert.throws(
        () => todoList.add(null),
        new TaskContentError(null),
        "TaskContentError if add task with null content."
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
        () => todoList.complete(null),
        new ParameterIsNotDefinedError(null, "task ID"),
        "ParameterIsNotDefinedError if complete argument is null."
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
        () => todoList.update(undefined, "content"),
        new ParameterIsNotDefinedError(undefined, "task ID"),
        "ParameterIsNotDefinedError if update first argument is undefined."
    );

    assert.throws(
        () => todoList.update(null, "content"),
        new ParameterIsNotDefinedError(null, "task ID"),
        "ParameterIsNotDefinedError if update first argument is null."
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
        () => todoList.update(taskId, null),
        new TaskContentError(null),
        "TaskContentError if update second argument is null."
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
        () => todoList.remove(null),
        new ParameterIsNotDefinedError(null, "task ID"),
        "ParameterIsNotDefinedError if remove argument is null."
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
