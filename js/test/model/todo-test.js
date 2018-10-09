import {
    CannotUpdateCompletedTaskException,
    TaskAlreadyCompletedException,
    TaskNotFoundException,
    TaskSorter,
    TodoList
} from "../../src/model/todo-list.js";
import {Task} from "../../src/model/task";
import {TaskId} from "../../src/lib/identifiers";
import {TaskIdGenerator} from "../../src/lib/taskIdGenerator";
import {TasksClone} from "../../src/lib/todolists";
import {
    DatePointsToFutureException,
    EmptyStringException,
    ParameterIsNotDefinedException,
    Preconditions
} from "../../src/lib/preconditions";


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
