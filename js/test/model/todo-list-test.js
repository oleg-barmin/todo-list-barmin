import {TodoList} from "../../src/todo-list";
import {TodoListIdGenerator} from "../../src/lib/todoListIdGenerator";
import {TaskIdGenerator} from "../../src/lib/taskIdGenerator";
import {EmptyStringException, ParameterIsNotDefinedException} from "../../src/lib/preconditions";
import {TaskId} from "../../src/lib/identifiers";
import {MockBackend} from "./mockBackend";


QUnit.module("TodoList should");
QUnit.test("send requests to add tasks", assert => {
    const todoListId = TodoListIdGenerator.generateID();
    const token = "token";
    const mockBackend = new MockBackend();

    const todoList = new TodoList(todoListId, token, mockBackend);

    const taskDescription = "wash my car";
    const returnValue = todoList.add(taskDescription);

    assert.strictEqual(returnValue, mockBackend.mockReturnValue,
        "and return value which returned backend instance.");
    assert.strictEqual(mockBackend.lastParams.todoListId, todoListId,
        "to to-do list which ID was given during construction.");
    assert.strictEqual(mockBackend.lastParams.payload.taskDescription, taskDescription,
        "add task with description given in parameter.");
    assert.strictEqual(mockBackend.lastParams.token, token,
        "with token which was given during construction.");
});

QUnit.test("send requests to update tasks", assert => {
    const todoListId = TodoListIdGenerator.generateID();
    const token = "token";
    const mockBackend = new MockBackend();

    const todoList = new TodoList(todoListId, token, mockBackend);

    const taskId = TaskIdGenerator.generateID();
    const taskDescription = "visit my grandmother.";

    const returnValue = todoList.update(taskId, taskDescription, true);

    assert.strictEqual(returnValue, mockBackend.mockReturnValue,
        "and return value which returned backend instance.");
    assert.strictEqual(mockBackend.lastParams.todoListId, todoListId,
        "in the to-do list which ID was given during construction.");
    assert.strictEqual(mockBackend.lastParams.taskId, taskId,
        "update task with given ID.");
    assert.strictEqual(mockBackend.lastParams.payload.taskDescription, taskDescription,
        "update task with given description.");
    assert.ok(mockBackend.lastParams.payload.taskStatus, "update task with given status.");
    assert.strictEqual(mockBackend.lastParams.token, token,
        "with token which was given during construction.");
});

QUnit.test("send requests to remove tasks", assert => {
    const todoListId = TodoListIdGenerator.generateID();
    const token = "token";
    const mockBackend = new MockBackend();

    const todoList = new TodoList(todoListId, token, mockBackend);

    const taskId = TaskIdGenerator.generateID();

    const returnValue = todoList.remove(taskId);

    assert.strictEqual(returnValue, mockBackend.mockReturnValue,
        "and return value which returned backend instance.");
    assert.strictEqual(mockBackend.lastParams.todoListId, todoListId,
        "from to-do list which ID was given during construction.");
    assert.strictEqual(mockBackend.lastParams.taskId, taskId,
        "remove task with given ID.");
    assert.strictEqual(mockBackend.lastParams.token, token,
        "with token which was given during construction.");
});

QUnit.test("send requests to read tasks from to-do list", assert => {
    const todoListId = TodoListIdGenerator.generateID();
    const token = "token";
    const mockBackend = new MockBackend();

    const todoList = new TodoList(todoListId, token, mockBackend);

    const returnValue = todoList.all();

    assert.strictEqual(returnValue, mockBackend.mockReturnValue,
        "and return value which returned backend instance.");
    assert.strictEqual(mockBackend.lastParams.todoListId, todoListId,
        "which ID was given during construction.");
    assert.strictEqual(mockBackend.lastParams.token, token,
        "with token which was given during construction.");
});

QUnit.test("throw", assert => {
    const todoListId = TodoListIdGenerator.generateID();
    const token = "token";
    const mockBackend = new MockBackend();


    const todoList = new TodoList(todoListId, token, mockBackend);

    const taskId = TaskIdGenerator.generateID();

    assert.throws(
        () => todoList.add(undefined),
        new EmptyStringException(undefined, "task description"),
        "EmptyStringException if add task with undefined description."
    );
    assert.throws(
        () => todoList.add(""),
        new EmptyStringException("", "task description"),
        "EmptyStringException if add task with empty description."
    );
    assert.throws(
        () => todoList.update(undefined, "description"),
        new ParameterIsNotDefinedException(undefined, "task ID"),
        "ParameterIsNotDefinedException if update first argument is undefined."
    );
    assert.throws(
        () => todoList.update("", "description"),
        new ParameterIsNotDefinedException("", "task ID"),
        "ParameterIsNotDefinedException if update first argument is empty string."
    );
    assert.throws(
        () => todoList.update(taskId, undefined),
        new EmptyStringException(undefined, "updated description"),
        "EmptyStringException if update second argument is undefined."
    );
    assert.throws(
        () => todoList.update(taskId, ""),
        new EmptyStringException("", "updated description"),
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
});

