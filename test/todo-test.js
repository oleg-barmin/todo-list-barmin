import {TodoList, TaskSorter, TaskAlreadyCompletedException, CannotUpdateCompletedTaskException, TaskNotFoundException} from "../src/model/todo-list.js";
import {Task} from "../src/model/task";
import {TaskId} from "../src/lib/identifiers";
import {TaskIdGenerator} from "../src/lib/taskIdGenerator";
import {TasksClone} from "../src/lib/todolists";
import {Preconditions, ParameterIsNotDefinedException, DatePointsToFutureException, EmptyStringException} from "../src/lib/preconditions";


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
        () => Preconditions.checkStringNotEmpty(undefined, "any string"),
        new EmptyStringException(undefined, "any string"),
        "EmptyStringException when checkStringNotEmpty() argument is undefined."
    );

    assert.throws(
        () => Preconditions.checkStringNotEmpty(null, "any string"),
        new EmptyStringException(null, "any string"),
        "EmptyStringException when checkStringNotEmpty() argument is null."
    );

    assert.throws(
        () => Preconditions.checkStringNotEmpty("", "any string"),
        new EmptyStringException("", "any string"),
        "EmptyStringException when checkStringNotEmpty() argument is empty."
    );

    assert.throws(
        () => Preconditions.checkStringNotEmpty("    ", "any string"),
        new EmptyStringException("", "any string"),
        "EmptyStringException when checkStringNotEmpty() argument contains only of multiple spaces."
    );

    const futureDate = new Date();
    futureDate.setFullYear(2020);
    assert.throws(
        () => Preconditions.checkDateInPast(futureDate, "any date"),
        new DatePointsToFutureException(futureDate),
        "DatePointsToFutureException when checkDateInPast() argument is date that point to future."
    );
});

QUnit.module("TasksSorter should ");
QUnit.test("sort ", assert => {
    const firstDate = new Date("01 Jan 1970 00:00:00 GMT");
    const secondDate = new Date("02 Jan 1970 00:00:00 GMT");
    const thirdDate = new Date("03 Jan 1970 00:00:00 GMT");
    const fourthDate = new Date("04 Jan 1970 00:00:00 GMT");

    let firstTask = new Task(new TaskId("1"), "1", firstDate);
    let secondTask = new Task(new TaskId("2"), "2", secondDate);
    let thirdTask = new Task(new TaskId("3"), "3", thirdDate);
    let fourthTask = new Task(new TaskId("4"), "4", fourthDate);

    let array = [
        firstTask,
        fourthTask,
        secondTask,
        thirdTask
    ];

    TaskSorter.sortTasksArray(array);

    assert.propEqual(
        array,
        [
            fourthTask,
            thirdTask,
            secondTask,
            firstTask
        ],
        " by date."
    );

    const sameDate = new Date();

    firstTask.lastUpdateDate = sameDate;
    secondTask.lastUpdateDate = sameDate;
    thirdTask.lastUpdateDate = sameDate;
    fourthTask.lastUpdateDate = sameDate;

    array = [
        firstTask,
        fourthTask,
        thirdTask,
        secondTask
    ];

    TaskSorter.sortTasksArray(array);

    assert.propEqual(
        array,
        [
            firstTask,
            secondTask,
            thirdTask,
            fourthTask
        ],
        " by description if dates are same."
    );

    const sameDescription = "same description";
    firstTask.description = sameDescription;
    secondTask.description = sameDescription;
    thirdTask.description = sameDescription;
    fourthTask.description = sameDescription;


    assert.propEqual(
        array,
        [
            firstTask,
            secondTask,
            thirdTask,
            fourthTask
        ],
        " by ID if dates and descriptions are same."
    );
});

QUnit.module("TaskIdGenerator should");
QUnit.test("generate ", assert => {
    let set = new Set();

    let hasDuplicate = false;
    for (let i = 0; i < 1000; i++) {
        let id = TaskIdGenerator.generateID();
        if (set.has(id)) {
            hasDuplicate = true;
            break;
        }
        set.add(id);
    }
    assert.ok(!hasDuplicate, " unique id.")
});

QUnit.module("TasksClone should");
QUnit.test("return cloned ", assert => {
    let innerObject = {
        one: "one",
        two: "two"
    };
    let objectToCopy = {
        stringProperty: "old",
        innerObject: innerObject
    };

    let clonedObject = TasksClone.cloneObject(objectToCopy);
    assert.deepEqual(objectToCopy, clonedObject, "object with same properties.");
    objectToCopy.prop1 = "new";
    objectToCopy.innerObject.one = "modified";
    assert.notDeepEqual(objectToCopy, clonedObject, "object with same properties but with a different reference.");

    let arrayToCopy = [1, 2, 3, 4];
    let clonedArray = TasksClone.cloneArray(arrayToCopy);

    assert.deepEqual(arrayToCopy, clonedArray, "array with same elements.");
    arrayToCopy[0] = 10;
    assert.notDeepEqual(arrayToCopy, clonedArray, "array with same elements but with a different references.");

});

QUnit.module("TodoList should ");
QUnit.test("add ", assert => {
    let todoList = new TodoList();

    let firstTaskDescription = "first task";
    let firstTaskID = todoList.add(firstTaskDescription);
    let firstTask = todoList._getTaskById(firstTaskID);

    assert.strictEqual(firstTask.description, firstTaskDescription, "task by description.");

    todoList = new TodoList();
    firstTaskDescription = "first task";
    firstTaskID = todoList.add(firstTaskDescription);
    firstTask = todoList._getTaskById(firstTaskID);

    const secondTaskDescription = "second task";
    const secondTaskID = todoList.add(secondTaskDescription);
    const secondTask = todoList._getTaskById(secondTaskID);

    assert.ok(
        firstTask.description === firstTaskDescription
        && secondTask.description === secondTaskDescription
        && todoList._tasksArray.length === 2,
        " multiple tasks by ID."
    );
});

QUnit.test("remove ", assert => {
    const todoList = new TodoList();

    const firstTaskDescription = "first task";
    const firstTaskID = todoList.add(firstTaskDescription);

    const secondTaskDescription = "second task";
    const secondTaskID = todoList.add(secondTaskDescription);

    todoList.remove(firstTaskID);

    assert.ok(
        todoList._tasksArray.length === 1
        && todoList._getTaskById(secondTaskID),
        "task from list by ID."
    );
});

QUnit.test("update ", assert => {
    const todoList = new TodoList();

    const firstTaskDescription = "first task";
    const firstTaskID = todoList.add(firstTaskDescription);
    todoList._getTaskById(firstTaskID);

    const secondTaskDescription = "second task";
    const secondTaskID = todoList.add(secondTaskDescription);
    const secondTask = todoList._getTaskById(secondTaskID);

    const newSecondTaskDescription = "new second task description";
    todoList.update(secondTaskID, newSecondTaskDescription);

    assert.strictEqual(
        secondTask.description,
        newSecondTaskDescription,
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

QUnit.test("all ", assert => {
    const todoList = new TodoList();

    const firstTaskId = todoList.add("first task");
    todoList.add("second task");
    const arrayCopy = todoList.all();

    todoList.remove(firstTaskId);

    const newArray = todoList.all();
    assert.notPropEqual(newArray, arrayCopy, "return copy of stored task list.");

    arrayCopy[0].description = "modified";
    assert.notPropEqual(arrayCopy[0], newArray[0], " copy of all tasks in the array");

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
        new EmptyStringException(undefined, "task description"),
        "EmptyStringException if add task with undefined description."
    );

    assert.throws(
        () => todoList.add(""),
        new EmptyStringException("", "task description"),
        "EmptyStringException if add task with empty description."
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
        () => todoList.update(thirdTaskId, "new description"),
        new CannotUpdateCompletedTaskException(thirdTaskId),
        "CannotUpdateCompletedTaskException if try to update completed task."
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
        () => todoList.update(new TaskId("123"), "description"),
        new TaskNotFoundException(new TaskId("123")),
        "TaskNotFoundException if update first argument is non-existing ID."
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

    assert.throws(
        () => todoList.remove(new TaskId("123")),
        new TaskNotFoundException(new TaskId("123")),
        "TaskNotFoundException if remove non-existing task."
    );

});
