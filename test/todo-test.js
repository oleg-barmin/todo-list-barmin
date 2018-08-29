if (typeof(require) !== 'undefined') {
    todoListModule = require('../src/todo-list.js');
    TodoList = todoListModule.TodoList;
}

QUnit.module("TodoList should ");
QUnit.test("add ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);
    let firstTask = todoList.tasksMap.get(firstTaskID);

    assert.equal(firstTask.content, firstTaskContent, "one task by content.");

    todoList = new TodoList();
    firstTaskContent = "first task";
    firstTaskID = todoList.add(firstTaskContent);
    firstTask = todoList.tasksMap.get(firstTaskID);

    let secondTaskContent = "second task";
    let secondTaskID = todoList.add(secondTaskContent);
    let secondTask = todoList.tasksMap.get(secondTaskID);

    assert.ok(
        firstTask.content === firstTaskContent
        && secondTask.content === secondTaskContent
        && todoList.tasksMap.size === 2,
        " multiple tasks by ID."
    );
});

QUnit.test("remove ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);

    let secondTaskContent = "second task";
    let secondTaskID = todoList.add(secondTaskContent);

    todoList.remove(firstTaskID);

    assert.ok(
        todoList.tasksMap.size === 1
        && todoList.tasksMap.get(secondTaskID),
        "task from list by ID."
    );
});

QUnit.test("update ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let firstTaskID = todoList.add(firstTaskContent);
    let firstTask = todoList.tasksMap.get(firstTaskID);

    let secondTaskContent = "second task";
    let secondTaskID = todoList.add(secondTaskContent);
    let secondTask = todoList.tasksMap.get(secondTaskID);

    let newSecondTaskContent = "new second task content";
    todoList.update(secondTaskID, newSecondTaskContent);

    assert.equal(
        secondTask.content,
        newSecondTaskContent,
        "task list by ID."
    );
});


QUnit.test("complete ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let taskID = todoList.add(firstTaskContent);
    let firstTask = todoList.tasksMap.get(taskID);

    todoList.complete(taskID);

    assert.ok(firstTask.completed, "task.");
});

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

QUnit.test("sort ", async function (assert) {
    let todoList = new TodoList();

    let firstTaskID = todoList.add("first task");

    await sleep(1);
    todoList.add("second task");

    await sleep(1);
    let thirdTaskID = todoList.add("third task");

    await sleep(1);
    todoList.complete(firstTaskID);

    let allTodo = todoList.all();

    assert.equal(allTodo[2].ID, firstTaskID, " when task is done.");

    await sleep(1);
    todoList.update(thirdTaskID, "updated third task");

    allTodo = todoList.all();

    for(let currentTask of allTodo){
        console.log(currentTask);
    }

    assert.equal(allTodo[0].ID, thirdTaskID, "when task updated.");
});