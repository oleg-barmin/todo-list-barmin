(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (factory());
}(this, (function () { 'use strict';

    /**
     * Static methods that help a method or constructor check whether it was invoked
     * with correct parameters.
     */
    class Preconditions {

        /**
         * Validates whether given argument is not null or undefined.
         *
         * @param {*} value the value of parameter being checked.
         * @param {string} parameterName name of the parameter.
         *
         * @throws ParameterIsNotDefinedException if given parameter is null or undefined.
         *
         * @returns {*} value of the parameter if it is not null or undefined otherwise ParameterIsNotDefinedException is thrown.
         */
        static isDefined(value, parameterName) {
            if (!value) {
                throw new ParameterIsNotDefinedException(value, parameterName);
            }
            return value;
        }

        /**
         * Validates if given string is not undefined, null or empty.
         *
         * @param {string} stringToCheck string that should be checked.
         * @param {string} stringName name of string being checked
         *
         * @throws EmptyStringException if given string is empty or undefined.
         *
         * @returns {string} string value if given string is not
         */
        static checkStringNotEmpty(stringToCheck, stringName) {
            if (!(stringToCheck && typeof stringToCheck === "string")) {
                throw new EmptyStringException(stringToCheck, stringName)
            }
            stringToCheck = stringToCheck.trim();
            if (stringToCheck === "") {
                throw new EmptyStringException(stringToCheck, stringName)
            }
            return stringToCheck;
        }


        /**
         * Validates that given date point to future.
         *
         * @param {Date} dateToCheck date to validate
         * @param {string} parameterName name of parameter being checked
         *
         * @throws DatePointsToFutureException if given date points to future
         * @throws ParameterIsNotDefinedException if given date is not defined
         *
         * @returns {Date} date given date if it is valid
         */
        static checkDateInPast(dateToCheck, parameterName) {
            Preconditions.isDefined(dateToCheck, parameterName);
            if ((new Date() - dateToCheck) < 0) {
                throw new DatePointsToFutureException(dateToCheck)
            }
            return dateToCheck;
        }
    }

    /**
     * Indicates that a null or undefined argument was found while it wasn't expected.
     *
     * @extends Error
     */
    class ParameterIsNotDefinedException extends Error {

        /**
         * Creates `ParameterIsNotDefinedException` instance.
         *
         * @param {*} value parameters value
         * @param {string} parameterName name of the parameter that was checked
         */
        constructor(value, parameterName) {
            super(`Parameter '${parameterName}' should be not null and not undefined, Actual value: '${value}'`);
            this.name = this.constructor.name;
        }

        code() {
        }
    }

    /**
     * Indicates that date that was given to date point to future.
     *
     * @extends Error
     */
    class DatePointsToFutureException extends Error {

        /**
         * Crates `DatePointsToFutureException` instance.
         *
         * @param {Date} date date which point to future.
         */
        constructor(date) {
            super(`Given date '${date}' points to future.`);
            this.name = this.constructor.name;
        }
    }

    /**
     * Indicates that given task description is undefined, null or empty.
     */
    class EmptyStringException extends Error {

        /**
         * Creates `EmptyStringException` instance.
         *
         * @param {string} value actual value of task description
         * @param {string} stringName name of the string that was checked
         */
        constructor(value, stringName) {
            super(`String '${stringName}' should be a string and cannot be undefined, null or empty. Actual value: '${value}'`);
            this.name = this.constructor.name;
            if(value === ""){
                this.message = "Task description cannot be empty.";
            }
        }
    }

    /**
     * Task to do.
     *
     * It stores task ID, its description, status (is it completed or not),
     * date of creation and date of last update (if task wasn't updated
     * the date of creation equals to last update date).
     */
    class Task {

        /**
         * Creates task instance.
         *
         * @param {TaskId} id task ID
         * @param {string} description description of the task
         * @param {Date} creationDate date when task was created
         *
         */
        constructor(id, description, creationDate) {
            this.id = id;
            this.description = description;
            this.completed = false;
            this.creationDate = creationDate;
            this.lastUpdateDate = creationDate;
        }

        toString() {
            return `Task: [ID = ${this.id}, description = ${this.description}, completed = ${this.completed}, `
                + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
        }
    }

    /**
     * Used to identify tasks.
     *
     * @author Oleg Barmin
     */
    class TaskId {

        /**
         * Creates `TaskId` instance.
         *
         * @param id ID to store
         */
        constructor(id) {
            Preconditions.checkStringNotEmpty(id, "ID");
            this.id = id;
        }

        /**
         * Compares `TaskId` objects by stored ID.
         *
         * @param {TaskId} taskId `TaskId` to compare with
         *
         * @throws TypeError if given `taskId` is not TaskId class instance
         *
         * @returns {number} result positive, negative or 0 if given task is less, greater or equal to current.
         */
        compareTo(taskId) {
            if (!(taskId instanceof TaskId)) {
                throw new TypeError("Object of TaskId was expected");
            }

            return this.id.localeCompare(taskId.id);
        }
    }

    /**
     * Generates unique `TaskId` for `Task`.
     *
     * Current implementation based on uuid v4.
     */
    class TaskIdGenerator {

        /**
         * Generates unique `TaskID`.
         *
         * @returns {TaskId} ID generated.
         */
        static generateID() {
            if (typeof(require) !== 'undefined') {
                return require('uuid/v4')();
            }
            const rawId = uuidv4();
            return new TaskId(rawId);
        }
    }

    /**
     * Provides static methods to clone arrays of tasks and single tasks.
     */
    class TasksClone {

        /**
         * Deep copies given `Array` of `Task`.
         *
         * @param {Array} array array to copy
         * @returns {Array} arrayCopy copy of given array
         */
        static cloneArray(array) {
            if (!(array instanceof Array)) {
                throw new TypeError("Array expected. Actual value: " + array)
            }
            let arrayCopy = [];

            for (let i = 0; i < array.length; i++) {
                let currentElement = array[i];
                if (typeof currentElement === "object") {
                    arrayCopy[i] = TasksClone.cloneObject(currentElement);
                    continue;
                }
                arrayCopy[i] = currentElement;
            }
            arrayCopy.__proto__ = Array.prototype;
            return arrayCopy;
        }

        /**
         * Deep copies given `Task`.
         *
         * @param {*} objectToClone object to clone
         * @returns {*} copy of given `Task`
         */
        static cloneObject(objectToClone) {
            if (typeof objectToClone !== "object") {
                return objectToClone;
            }
            let objCopy = {};

            for (let key in objectToClone) {
                let currentProperty = objectToClone[key];
                if (typeof currentProperty === "object") {
                    if (currentProperty instanceof Array) {
                        objCopy[key] = TasksClone.cloneArray(currentProperty);
                        continue;
                    }
                    objCopy[key] = TasksClone.cloneObject(currentProperty);
                    continue
                }
                objCopy[key] = currentProperty;
            }
            objCopy.__proto__ = objectToClone.constructor.prototype;
            return objCopy;
        }
    }

    /**
     * Tasks to-do.
     *
     * Allows:
     *  - add new tasks
     *  - marks them as completed
     *  - update their descriptions
     *  - remove from list
     *
     * Task is being sorted by:
     * - Status (uncompleted tasks first, completed last).
     * - Date of last update.
     * - Lexicographically by description.
     *
     * Example:
     * ```
     *  const todoList = new TodoList();
     *  const firstTaskId = todoList.add("first task");
     *  const secondTaskId = todoList.add("second task");
     *  const thirdTaskId = todoList.add("third task");
     * ```
     * Task list will be sorted by creation date:
     * - third task
     * - second task
     * - first task
     *
     * Then lets complete third task.
     * ```
     * todoList.complete(thirdTaskId);
     * ```
     *
     * Now list will look this way:
     * - second task
     * - first task
     * - ~~third task~~
     *
     * if you update the first task, it will move to the top of the list.
     * ```
     * todoList.update(firstTaskId, "updated first task");
     * ```
     *
     * - <b>updated first task</b>
     * - second task
     * - ~~third task~~
     */
    class TodoList {

        /**
         * Creates `TodoList` instance.
         *
         * @param {TodoListId} todoListId ID of to-do list
         */
        constructor(todoListId) {
            this._tasksArray = [];
            this.todoListId = todoListId;
        }

        /**
         * Adds new task to `TodoList`.
         *
         * @param {string} taskDescription description of the task to add
         *
         * @throws EmptyStringException if given task description is not defined or empty
         *
         * @returns {TaskId} id copy of ID of task that was added
         */
        add(taskDescription) {
            taskDescription = Preconditions.checkStringNotEmpty(taskDescription, "task description");

            const taskId = TaskIdGenerator.generateID();
            const currentDate = new Date();
            const taskToAdd = new Task(taskId, taskDescription, currentDate);
            this._tasksArray.push(taskToAdd);

            TaskSorter.sortTasksArray(this._tasksArray);

            return TasksClone.cloneObject(taskId);
        }

        /**
         * Finds task by its ID.
         *
         * @param {TaskId} taskId ID of specified task
         * @returns {Task} task task with specified ID
         * @private
         */
        _getTaskById(taskId) {
            for (let cur of this._tasksArray) {
                if (cur.id.compareTo(taskId) === 0) {
                    return cur;
                }
            }
        }

        /**
         * Finds task by ID and marks it as completed.
         *
         * @param {TaskId} taskId ID of specified task
         *
         * @throws TaskNotFoundException if task with specified ID was not found
         * @throws TaskAlreadyCompletedException if task with specified ID is already completed
         */
        complete(taskId) {
            Preconditions.isDefined(taskId, "task ID");

            const storedTask = this._getTaskById(taskId);

            if (!storedTask) {
                throw new TaskNotFoundException(taskId);
            }

            if (storedTask.completed) {
                throw new TaskAlreadyCompletedException(taskId);
            }
            storedTask.completed = true;
            storedTask.lastUpdateDate = new Date();
            TaskSorter.sortTasksArray(this._tasksArray);
        }

        /**
         * Finds task by ID and updates its description.
         *
         * @param {TaskId} taskId ID of the specified task
         * @param {string} updatedDescription new description of the task
         *
         * @throws TaskNotFoundException if task with specified ID was not found
         * @throws CannotUpdateCompletedTaskException if try to updated completed task
         */
        update(taskId, updatedDescription) {
            Preconditions.isDefined(taskId, "task ID");
            updatedDescription = Preconditions.checkStringNotEmpty(updatedDescription, "updated description");

            const taskToUpdate = this._getTaskById(taskId);

            if (!taskToUpdate) {
                throw new TaskNotFoundException(taskId);
            }

            if (taskToUpdate.completed) {
                throw new CannotUpdateCompletedTaskException(taskId);
            }

            taskToUpdate.description = updatedDescription;
            taskToUpdate.lastUpdateDate = new Date();

            TaskSorter.sortTasksArray(this._tasksArray);
        }

        /**
         * Removes task with given ID from to-do list.
         *
         * @param {TaskId} taskId ID of task to delete
         *
         * @throws TaskNotFoundException if task with specified ID was not found
         */
        remove(taskId) {
            Preconditions.isDefined(taskId, "task ID");

            const desiredTask = this._tasksArray.find((element, index, array) => {
                if (element.id.compareTo(taskId) === 0) {
                    array.splice(index, 1);
                    return true;
                }
            });

            if (!desiredTask) {
                throw new TaskNotFoundException(taskId);
            }
        }

        /**
         * Returns copy of all tasks stored in to-do list.
         *
         * @returns {Array} tasksArray copy of array with task
         */
        all() {
            return TasksClone.cloneArray(this._tasksArray);
        }
    }

    /**
     * Stores algorithm to sort an array of  `Task`.
     */
    class TaskSorter {

        /**
         * Sorts array of tasks by:
         * - Status (uncompleted then completed).
         * - Date of last update.
         * - Lexicographically by description.
         * - Lexicographically by ID.
         *
         * @param {Array} array with tasks to sort
         */
        static sortTasksArray(array) {
            array.sort((firstTask, secondTask) => {
                if (firstTask.completed === secondTask.completed) {
                    const compareByDateResult = secondTask.lastUpdateDate - firstTask.lastUpdateDate;
                    if (compareByDateResult !== 0) {
                        return compareByDateResult;
                    }
                    const compareByDescriptionResult = firstTask.description.localeCompare(secondTask.description);
                    if (compareByDescriptionResult !== 0) {
                        return compareByDescriptionResult;
                    }
                    return secondTask.id.compareTo(firstTask.id);
                }

                return firstTask.completed ? 1 : -1;
            });
        }
    }

    /**
     * Indicates that task was not found.
     *
     * @extends Error
     */
    class TaskNotFoundException extends Error {

        /**
         * Crates `TaskNotFoundException` instance.
         *
         * @param {TaskId} taskId ID if the task that was not found
         */
        constructor(taskId) {
            super(`Task with ID ${taskId.id} was not found.`);
            this.name = this.constructor.name;
        }
    }


    /**
     * Indicates that task was already completed.
     *
     * @extends Error
     */
    class TaskAlreadyCompletedException extends Error {

        /**
         * Crates `TaskAlreadyCompletedException` instance.
         *
         * @param {TaskId} taskId  ID of task which was already completed.
         */
        constructor(taskId) {
            super(`Task with ID ${taskId.id} is alredy completed.`);
            this.name = this.constructor.name;
        }
    }

    /**
     * Occurs when trying to update completed task.
     *
     * @extends Error
     */
    class CannotUpdateCompletedTaskException extends Error {

        /**
         * Crates `CannotUpdateCompletedTaskException` instance.
         *
         * @param {TaskId} taskId ID of task which was already completed before update attempt.
         */
        constructor(taskId) {
            super(`Completed tasks cannot be updated. Task with ID: ${taskId} is completed.`);
            this.name = this.constructor.name;
        }
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
        assert.ok(!hasDuplicate, " unique id.");
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
        assert.notStrictEqual(objectToCopy, clonedObject, "object with same properties but with a different reference.");

        let arrayToCopy = [1, 2, 3, 4];
        let clonedArray = TasksClone.cloneArray(arrayToCopy);

        assert.deepEqual(arrayToCopy, clonedArray, "array with same elements.");
        arrayToCopy[0] = 10;
        assert.notStrictEqual(arrayToCopy, clonedArray, "array with same elements but with a different references.");

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
        await sleep(10);
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

})));
