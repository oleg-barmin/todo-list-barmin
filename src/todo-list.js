/**
 * Stores and manages all {@link Task}.
 *
 * Allows to add new tasks, marks them as completed,
 * remove from list and update their contents.
 */
class TodoList {

    /**
     * Creates TodoList instance.
     */
    constructor() {
        this.tasksList = new Map();
        this.idGenerator = new IdGenerator();
    }

    /**
     * Adds new task to TodoList.
     *
     * @param {string} taskContent content of the task to add.
     * @returns {string} id of task that was added.
     */
    add(taskContent) {
        Preconditions.checkTaskContent(taskContent);

        let taskID = this.idGenerator.generateID();
        let currentDate = new Date();
        let taskToAdd = new Task(taskID, taskContent, currentDate);

        this.tasksList.set(taskID, taskToAdd);

        return taskID;
    }

    /**
     * Finds task by its ID.
     *
     * @param {string} taskID id of desired task.
     * @returns {Task} task stored in database.
     * @private
     */
    _getTaskById(taskID) {
        Preconditions.isDefined(taskID, "task ID");

        let storedTask = this.tasksList.get(taskID);

        if (!storedTask) {
            throw new TaskNotFoundError(taskID);
        }

        return storedTask;

    }

    /**
     * Marks desired task as completed.
     *
     * @param {string} taskID id of desired task.
     */
    complete(taskID) {
        Preconditions.isDefined(taskID, "task ID");

        let storedTask = this._getTaskById(taskID);

        if (storedTask.completed) {
            throw new TaskAlreadyCompletedException(taskID);
        }

        storedTask.completed = true;
    }

    /**
     * Updates content of desired task.
     *
     * @param {string} taskID id of the desired task
     * @param {string} updatedContent new content of desired task.
     */
    update(taskID, updatedContent) {
        Preconditions.isDefined(taskID, "task ID");
        Preconditions.checkTaskContent(updatedContent);

        let storedTask = this._getTaskById(taskID);

        storedTask.content = updatedContent;
        storedTask.lastUpdateDate = new Date();
    }

    /**
     * Removes task with given ID from todoList.
     *
     * @param {string} taskID id of
     */
    remove(taskID) {
        Preconditions.isDefined(taskID, "task ID");

        if (!this.tasksList.delete(taskID)) {
            throw new TaskNotFoundError(taskID);
        }
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
        if (!(stringToCheck || typeof stringToCheck !== "string")) {
            throw new TaskContentError(stringToCheck)
        }
        stringToCheck = stringToCheck.trim();
        if (stringToCheck === "") {
            throw TaskContentError(stringToCheck)
        }
        return stringToCheck;
    }
}

/**
 * Generates uuid v4 id strings.
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
        super(`Task content cannot be undefined, null or empty. Actual value: '${value}'`);
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
     * @param {string} taskID id if the task that was not found.
     */
    constructor(taskID) {
        super(`Task with id ${taskID} was not found.`);
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
     * @param taskID {string} id of task which was already completed.
     */
    constructor(taskID) {
        super(`Task with id ${taskID} is alredy completed.`);
        this.name = this.constructor.name;
    }
}
