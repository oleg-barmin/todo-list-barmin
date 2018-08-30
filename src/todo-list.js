"use strict";

/**
 * Stores and manages all {@link Task}.
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
     * Creates TodoList instance.
     */
    constructor() {
        this._tasksArray = [];
        this._idGenerator = new IdGenerator();
    }

    /**
     * Adds new task to TodoList.
     *
     * @param {string} taskContent content of the task to add.
     * @returns {string} id of task that was added.
     */
    add(taskContent) {
        Preconditions.checkTaskContent(taskContent);

        const taskId = this._idGenerator.generateID();
        const currentDate = new Date();
        const taskToAdd = new Task(taskId, taskContent, currentDate);

        this._tasksArray.unshift(taskToAdd);

        return taskId;
    }

    /**
     * Finds task by its ID.
     *
     * @param {string} taskId id of desired task.
     * @returns {Task} task stored in database.
     * @private
     */
    _getTaskById(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        const desiredTask = this._tasksArray.find(element => element.ID === taskId);

        if (desiredTask) {
            return desiredTask;
        }

        throw new TaskNotFoundError(taskId);
    }

    /**
     * Marks desired task as completed.
     *
     * @param {string} taskId id of desired task.
     */
    complete(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        const storedTask = this._getTaskById(taskId);

        if (storedTask.completed) {
            throw new TaskAlreadyCompletedException(taskId);
        }
        storedTask.completed = true;

        this.remove(taskId);
        const array = this._tasksArray;

        let find = array.find(function (element, index, array) {
            if (element.completed) {
                array.splice(index, 0, storedTask);
                return true;
            }
        });
        if (!find) {
            array.push(storedTask);
        }
    }

    /**
     * Updates content of desired task.
     *
     * @param {string} taskId id of the desired task
     * @param {string} updatedContent new content of desired task.
     */
    update(taskId, updatedContent) {
        Preconditions.isDefined(taskId, "task ID");
        Preconditions.checkTaskContent(updatedContent);

        const taskToUpdate = this._getTaskById(taskId);

        taskToUpdate.content = updatedContent;
        taskToUpdate.lastUpdateDate = new Date();

        this.remove(taskId);
        this._tasksArray.unshift(taskToUpdate);
    }

    /**
     * Removes task with given ID from to do list.
     *
     * @param {string} taskId id of task to delete.
     */
    remove(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        const desiredTask = this._tasksArray.find((element, index, array) => {
            if (element.ID === taskId) {
                array.splice(index, 1);
                return true;
            }
        });

        if (!desiredTask) {
            throw new TaskNotFoundError(taskId);
        }
    }

    /**
     * Returns all tasks stored in to do list.
     *
     * @returns {Array}
     */
    all() {
        return this._tasksArray;
    }
}

/**
 * Stores information about task.
 *
 * It stores task ID, its content, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 */
class Task {

    /**
     * Creates task instance.
     *
     * @param {string} id task id.
     * @param {string} content content of the task.
     * @param {Date} creationDate date when task was created.
     */
    constructor(id, content, creationDate) {
        this.ID = Preconditions.isDefined(id, "ID");
        this.content = Preconditions.isDefined(content, "task content");
        this.completed = false;
        this.creationDate = Preconditions.isDefined(creationDate, "date of creation");
        this.lastUpdateDate = creationDate;
    }

    toString() {
        return `Task: [ID = ${this.ID}, content = ${this.content}, completed = ${this.completed}, `
            + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
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
     * @throws ParameterIsNotDefinedError if given parameter is null or undefined.
     * @returns {*} value of the parameter if it is not null or undefined otherwise ParameterIsNotDefinedError is thrown.
     */
    static isDefined(value, parameterName) {
        if (!value) {
            throw new ParameterIsNotDefinedError(value, parameterName);
        }
        return value;
    }

    /**
     * Validates if given sting is not undefined, null or empty.
     *
     * @param {string} stringToCheck string that should be checked.
     * @returns {string} string value if given string is not
     */
    static checkTaskContent(stringToCheck) {
        if (!(stringToCheck && typeof stringToCheck === "string")) {
            throw new TaskContentError(stringToCheck)
        }
        stringToCheck = stringToCheck.trim();
        if (stringToCheck === "") {
            throw new TaskContentError(stringToCheck)
        }
        return stringToCheck;
    }
}

/**
 * Generates uuid v4 ID strings.
 */
class IdGenerator {

    /**
     * Creates IdGenerator instance.
     */
    constructor() {
        if (typeof(require) !== 'undefined') {
            this.uuidv4 = require('uuid/v4');
            return
        }

        this.uuidv4 = uuidv4;
    }

    /**
     * Generates uuid v4 IDs.
     *
     * @returns {string} ID generated uuid v4 ID.
     */
    generateID() {
        return this.uuidv4()
    }
}

/**
 * Custom error type which indicates that a null or undefined variable was found, when it shouldn't.
 *
 * @extends Error
 */
class ParameterIsNotDefinedError extends Error {

    /**
     * Creates ParameterIsNotDefinedError instance.
     *
     * @param {*} value parameters value.
     * @param {string} parameterName name of the parameter that was checked.
     */
    constructor(value, parameterName) {
        super(`Parameter '${parameterName}' should be not null and not undefined, Actual value: '${value}'`);
        this.name = this.constructor.name;
    }
}

/**
 * Custom error type which indicates that given task content is undefined, null or empty.
 */
class TaskContentError extends Error {

    /**
     * Creates TaskContentError instance.
     *
     * @param {string} value actual value of task content.
     */
    constructor(value) {
        super(`Task content should be a string and cannot be undefined, null or empty. Actual value: '${value}'`);
        this.name = this.constructor.name;
    }
}

/**
 * Custom error type which indicates that desired task was not found.
 *
 * @extends Error
 */
class TaskNotFoundError extends Error {

    /**
     * Crates TaskNotFoundError instance.
     *
     * @param {string} taskId id if the task that was not found.
     */
    constructor(taskId) {
        super(`Task with id ${taskId} was not found.`);
        this.name = this.constructor.name;
    }
}


/**
 * Custom error type which indicates that task was already completed.
 *
 * @extends Error
 */
class TaskAlreadyCompletedException extends Error {

    /**
     * Crates TaskAlreadyCompletedException instance.
     *
     * @param taskId {string} id of task which was already completed.
     */
    constructor(taskId) {
        super(`Task with id ${taskId} is alredy completed.`);
        this.name = this.constructor.name;
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        TodoList: TodoList
    };
}
