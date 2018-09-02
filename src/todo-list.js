"use strict";

/**
 * Tasks to do.
 *
 * Allows to add new tasks, marks them as completed,
 * remove from list and update their contents.
 *
 * Task is being sorted by:
 * - Status (uncompleted tasks first, completed last).
 * - Date of last update.
 * - Lexicographically by content.
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
     */
    constructor() {
        this._tasksArray = [];
    }

    /**
     * Adds new task to `TodoList`.
     *
     * @param {string} taskContent content of the task to add
     *
     * @throws EmptyStringException if given task description is not defined or empty
     *
     * @returns {TaskId} id copy of id of task that was added
     */
    add(taskContent) {
        Preconditions.checkStringNotEmpty(taskContent, "task content");

        const taskId = new TaskId(IdGenerator.generateID());
        const currentDate = new Date();
        const taskToAdd = new Task(taskId, taskContent, currentDate);
        this._tasksArray.push(taskToAdd);

        TaskSorter.sortTasksArray(this._tasksArray);

        return CloneUtils.cloneObject(taskId);
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
        TaskSorter.sortTasksArray(this._tasksArray);
    }

    /**
     * Finds task by id and updates its content.
     *
     * @param {TaskId} taskId ID of the specified task
     * @param {string} updatedContent new content of the task
     *
     * @throws TaskNotFoundException if task with specified ID was not found
     */
    update(taskId, updatedContent) {
        Preconditions.isDefined(taskId, "task ID");
        Preconditions.checkStringNotEmpty(updatedContent, "updated content");

        const taskToUpdate = this._getTaskById(taskId);

        if (!taskToUpdate) {
            throw new TaskNotFoundException(taskId);
        }

        if(taskToUpdate.completed){
            throw new CannotUpdateCompletedTaskException(taskId);
        }

        taskToUpdate.content = updatedContent;
        taskToUpdate.lastUpdateDate = new Date();

        TaskSorter.sortTasksArray(this._tasksArray);
    }

    /**
     * Removes task with given ID from to do list.
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
     * Returns all tasks stored in to do list.
     *
     * @returns {Array} tasksArray copy of array with task
     */
    all() {
        return CloneUtils.cloneArray(this._tasksArray);
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
     * @throws ParameterIsNotDefinedException if one of given parameters is not defined
     * @throws DatePointsToFutureException if given date point to future
     * @throws
     */
    constructor(id, description, creationDate) {
        this.id = id;
        this.description = description;
        this.completed = false;
        this.creationDate = Preconditions.checkDateInPast(creationDate, "date of creation");
        this.lastUpdateDate = creationDate;
    }

    toString() {
        return `Task: [ID = ${this.id}, description = ${this.description}, completed = ${this.completed}, `
            + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
    }
}

/**
 * Used to identify tasks.
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
     * Compares `TaskId` objects by stored id.
     *
     * @param taskId
     * @returns {number}
     */
    compareTo(taskId) {
        if (!(taskId instanceof TaskId)) {
            throw new TypeError("Object of TaskId was expected");
        }

        return this.id.localeCompare(taskId.id);
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
     * - Lexicographically by content.
     *
     * @param {Array} array with tasks to sort
     */
    static sortTasksArray(array) {
        array.sort((firstTask, secondTask) => {
            if (firstTask.completed === secondTask.completed) {
                let compareByDateResult = secondTask.lastUpdateDate - firstTask.lastUpdateDate;
                if (compareByDateResult === 0) {
                    let compareByDescriptionResult = firstTask.description.localeCompare(secondTask.description);
                    if (compareByDescriptionResult === 0) {
                        return secondTask.id.compareTo(firstTask.id);
                    }
                    return compareByDescriptionResult;
                }
                return compareByDateResult;
            }

            return firstTask.completed ? 1 : -1;
        });
    }
}

/**
 * Provides static methods to deep clone Arrays and Objects.
 */
class CloneUtils {

    /**
     * Deep copies given array and all it's elements.
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
                arrayCopy[i] = CloneUtils.cloneObject(currentElement);
                continue;
            }
            arrayCopy[i] = currentElement;
        }
        arrayCopy.__proto__ = Array.prototype;
        return arrayCopy;
    }

    /**
     * Deep copies given object.
     *
     * @param {*} objectToClone object to clone
     * @returns {*} copy of Array
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
                    objCopy[key] = CloneUtils.cloneArray(currentProperty);
                    continue;
                }
                objCopy[key] = CloneUtils.cloneObject(currentProperty);
                continue
            }
            objCopy[key] = currentProperty;
        }
        objCopy.__proto__ = objectToClone.constructor.prototype;
        return objCopy;
    }
}

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
     * Validates if given sting is not undefined, null or empty.
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
 * Generates uuid v4 ID strings.
 */
class IdGenerator {

    /**
     * Generates uuid v4 IDs.
     *
     * @returns {string} ID generated uuid v4 ID.
     */
    static generateID() {
        if (typeof(require) !== 'undefined') {
            return require('uuid/v4')();
        }
        return uuidv4();
    }
}

/**
 * Custom error type which indicates that a null or undefined argument was found while it wasn't expected.
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
}

/**
 * Custom error type which indicates that given task description is undefined, null or empty.
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
        super(`Completed tasks cannot be updated. Task with id: ${taskId} is completed.`);
        this.name = this.constructor.name;
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        TodoList: TodoList
    };
}
