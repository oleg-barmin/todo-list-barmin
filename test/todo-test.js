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