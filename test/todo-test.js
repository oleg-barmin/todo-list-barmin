if (typeof(require) !== 'undefined') {
    todoListModule = require('../src/todo-list.js');
    TodoList = todoListModule.TodoList;
}

QUnit.module("TodoList should ");
QUnit.test("add ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let taskID = todoList.add(firstTaskContent);
    let firstTask = todoList.tasksList.get(taskID);

    assert.equal(firstTask.content, firstTaskContent, "task.");
});

QUnit.test("complete ", function (assert) {
    let todoList = new TodoList();

    let firstTaskContent = "first task";
    let taskID = todoList.add(firstTaskContent);
    let firstTask = todoList.tasksList.get(taskID);

    todoList.complete(taskID);

    assert.ok(firstTask.completed, "task.");
});

QUnit.test("sort ", function (assert) {
    let todoList = new TodoList();

    let firstTaskID = todoList.add("first task");
    let secondTaskID = todoList.add("second task");
    let thirdTaskID = todoList.add("third task");

    todoList.complete(firstTaskID);

    let allTodo = todoList.all();
    assert.equal(allTodo[2].ID, firstTaskID, " when task is done.");

    todoList.update(thirdTaskID, "updated third task");

    allTodo = todoList.all();
    assert.equal(allTodo[0].ID, thirdTaskID, "when task updated.");
});