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
        this.tasksArray = [];
        this.idGenerator = new IdGenerator();
    }

    _sortTasks() {
        let sortFunction = function (firstTask, secondTask) {
            if (firstTask.completed === secondTask.completed) {
                let compareByDateResult = secondTask.lastUpdateDate - firstTask.lastUpdateDate;
                if (compareByDateResult === 0) {
                    return secondTask.content.localeCompare(firstTask.content);
                }
                return compareByDateResult;
            }

            return firstTask.completed ? 1 : -1;
        };

        this.tasksArray.sort(sortFunction);
    }

    /**
     * Adds new task to TodoList.
     *
     * @param {string} taskContent content of the task to add.
     * @returns {string} id of task that was added.
     */
    add(taskContent) {
        Preconditions.checkTaskContent(taskContent);

        let taskId = this.idGenerator.generateID();
        let currentDate = new Date();
        let taskToAdd = new Task(taskId, taskContent, currentDate);

        this.tasksArray.push(taskToAdd);
        this._sortTasks();

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

        let desiredTask = this.tasksArray.find(element => element.ID === taskId);

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

        let storedTask = this._getTaskById(taskId);

        if (storedTask.completed) {
            throw new TaskAlreadyCompletedException(taskId);
        }

        storedTask.completed = true;
        this._sortTasks();
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

        let storedTask = this._getTaskById(taskId);

        storedTask.content = updatedContent;
        storedTask.lastUpdateDate = new Date();
        this._sortTasks();
    }

    /**
     * Removes task with given ID from to do list.
     *
     * @param {string} taskId id of task to delete.
     */
    remove(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        let desiredTask = this.tasksArray.find((element, index, array)=> {
            if(element.ID === taskId){
                array.splice(index, 1);
                return true;
            }
        });

        if(!desiredTask){
            throw new TaskNotFoundError(taskId);
        }
    }

    /**
     * Returns all tasks stored in to do list.
     *
     * @returns {*}
     */
    all() {
        return this.tasksArray;
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
