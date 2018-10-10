(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
        typeof define === 'function' && define.amd ? define(factory) :
            (factory());
}(this, (function () {
    'use strict';

    /**
     * Static methods that help a method or constructor check whether it was invoked
     * with correct parameters.
     *
     * @author Oleg Barmin
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
    }

    /**
     * Indicates that a null or undefined argument was found while it wasn't expected.
     *
     * @extends Error
     * @author Oleg Barmin
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
     * Indicates that given task description is undefined, null or empty.
     *
     * @extends Error
     * @author Oleg Barmin
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
            if (value === "") {
                this.message = "Task description cannot be empty.";
            }
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
     * Used to identify `TodoList`s.
     *
     * @author Oleg Barmin
     */
    class TodoListId {

        /**
         * Creates `TodoListId` instance.
         *
         * @param id ID to store
         */
        constructor(id) {
            Preconditions.checkStringNotEmpty(id, "ID");
            this.id = id;
        }

        /**
         * Compares `TodoListId` objects by stored ID.
         *
         * @param {TodoListId} todoListId `TodoListId` to compare with
         *
         * @throws TypeError if given `todoListId` is not TodoListId class instance
         *
         * @returns {number} result positive, negative or 0 if given task is less, greater or equal to current.
         */
        compareTo(todoListId) {
            if (!(todoListId instanceof TodoListId)) {
                throw new TypeError("Object of TodoListId was expected");
            }

            return this.id.localeCompare(todoListId.id);
        }
    }

    /**
     * Generates unique `TaskId` for `Task`.
     *
     * Current implementation based on uuid v4.
     *
     * @author Oleg Barmin
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
     * Sends requests to the server to manage tasks of one
     * to-do list which ID was given during construction.
     *
     * Allows to:
     *  - add new tasks;
     *  - mark tasks as completed;
     *  - update tasks with new descriptions and status;
     *  - remove tasks from list;
     *  - retrieve sorted list of tasks (tasks are sorted by {@link TaskSorter}).
     *
     * Tasks is being sorted by:
     * - Status (uncompleted tasks first, completed last).
     * - Date of last update.
     * - Lexicographically by description.
     *
     * @author Oleg Barmin
     */
    class TodoList {

        /**
         * Creates `TodoList` instance.
         *
         * @param {TodoListId} todoListId ID of this to-do lists
         * @param token token of user who works with this to-do list
         * @param {Backend} backend to send requests
         */
        constructor(todoListId, token, backend) {
            this.todoListId = todoListId;
            this.token = token;
            this.backend = backend;
        }

        /**
         * Sends request to add `Task` into this to-do list.
         *
         * @param {string} taskDescription description of the task to add
         * @returns {Promise} promise to work with. If request was processed successfully
         *                    promise will be resolved, otherwise it will be rejected.
         *
         * @throws EmptyStringException if given task description is empty.
         */
        add(taskDescription) {
            Preconditions.checkStringNotEmpty(taskDescription, "task description");

            const taskId = TaskIdGenerator.generateID();

            const payload = {
                taskDescription: taskDescription.trim()
            };

            return this.backend.addTask(this.todoListId, taskId, payload, this.token)
        }

        /**
         * Sends request to updateTask task with given ID with new status and description.
         *
         * @param {TaskId} taskId ID of task to updateTask
         * @param {string} updatedDescription new description of the task
         * @param {boolean} status new status of task
         * @return {Promise} promise to work with. If request was processed successfully
         *                   promise will be resolved, otherwise it will be rejected.
         *
         * @throws ParameterIsNotDefinedException if given `taskId` is undefined or null
         * @throws EmptyStringException if given description is undefined, null  or empty
         */
        update(taskId, updatedDescription, status = false) {
            Preconditions.isDefined(taskId, "task ID");
            Preconditions.checkStringNotEmpty(updatedDescription, "updated description");

            const payload = {
                taskStatus: status,
                taskDescription: updatedDescription.trim()
            };

            return this.backend.updateTask(this.todoListId, taskId, payload, this.token);
        }

        /**
         * Sends request to removeTask task with given ID.
         *
         * @param {TaskId} taskId ID of task to delete
         * @return {Promise} promise to work with. If request was processed successfully
         *                  promise will be resolved, otherwise it will be rejected.
         *
         * @throws ParameterIsNotDefinedException if given `taskId` is undefined or null
         */
        remove(taskId) {
            Preconditions.isDefined(taskId, "task ID");
            return this.backend.removeTask(this.todoListId, taskId, this.token);
        }

        /**
         * Sends request to retrieve all tasks of this `TodoList`.
         *
         * @return {Promise} promise to work with. If request was processed successfully
         *                   promise will be resolved and sorted array of `Task`s will be returned,
         *                   otherwise it will be rejected.
         */
        all() {
            return new Promise((resolve) => {
                this.backend.readTasksFrom(this.todoListId, this.token)
                    .then((tasks) => resolve(TaskSorter.sortTasksArray(tasks)));
            })
        }
    }

    /**
     * Stores algorithm to sort an array of  `Task`.
     *
     * @author Oleg Barmin
     */
    class TaskSorter {

        /**
         * Sorts array of tasks by:
         * - Status (uncompleted then completed).
         * - Date of last updateTask.
         * - Lexicographically by description.
         * - Lexicographically by ID.
         *
         * @param {Array} array with tasks to sort
         */
        static sortTasksArray(array) {
            return array.sort((firstTask, secondTask) => {
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
     * Generates unique `TodoListId` for `TodoList`.
     *
     * Current implementation based on uuid v4.
     *
     * @author Oleg Barmin
     */
    class TodoListIdGenerator {

        /**
         * Generates unique `TodoListId`.
         *
         * @returns {TodoListId} ID generated TaskID.
         */
        static generateID() {
            if (typeof(require) !== 'undefined') {
                return require('uuid/v4')();
            }
            const rawId = uuidv4();
            return new TodoListId(rawId);
        }
    }

    /**
     * Task to do.
     *
     * It stores task ID, its description, status (is it completed or not),
     * date of creation and date of last update (if task wasn't updated
     * the date of creation equals to last update date).
     *
     * @author Oleg Barmin
     */
    class Task {

        /**
         * Creates task instance.
         *
         * @param {TaskId} id task ID
         * @param {string} description description of the task
         * @param {Date} creationDate date when task was created
         *
         * @param {boolean} [completed=false] status of task (completed or not)
         * @param {Date} [lastUpdate=creationDate] date of last task update
         */
        constructor(id, description, creationDate, completed = false, lastUpdate = creationDate) {
            this.id = id;
            this.description = description;
            this.completed = completed;
            this.creationDate = creationDate;
            this.lastUpdateDate = lastUpdate;
        }

        toString() {
            return `Task: [ID = ${this.id}, description = ${this.description}, completed = ${this.completed}, `
                + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
        }
    }

    /**
     * Service which sends requests to the server with given URL.
     *
     * @author Oleg Barmin
     */
    class Backend {

        /**
         * Creates `Backend` instance with given URL.
         *
         * Given URL should be in following format: `<protocol name>://<hostname>`.
         *
         * Examples of URL:
         * - `https://www.google.com`;
         * - `https://www.amazon.com`;
         * - `http://localhost:8080`.
         *
         * @param {string} url base URL of server.
         */
        constructor(url) {
            this.urlBuilder = new UrlBuilder(url);
            this.tokenHeader = "X-Todo-Token";
        }

        /**
         * Sends add task request.
         *
         * @param {TodoListId} todoListId ID of to-do list of task
         * @param {TaskId} taskId ID of task to add
         * @param payload payload of request
         * @param token token of user session.
         * @return {Promise} promise to process request result.
         */
        addTask(todoListId, taskId, payload, token) {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        resolve();
                    } else {
                        reject();
                    }
                };
                xmlHttpRequest.open(HttpMethods.POST, this.urlBuilder.buildTaskUrl(todoListId, taskId));
                xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                xmlHttpRequest.send(JSON.stringify(payload));
            });
        }

        /**
         * Sends update task request.
         *
         * @param {TodoListId} todoListId ID of to-do list of task
         * @param {TaskId} taskId ID of task to update
         * @param payload payload of request
         * @param token token of user session
         * @return {Promise} promise to process request result.
         */
        updateTask(todoListId, taskId, payload, token) {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        resolve();
                    } else {
                        reject();
                    }
                };

                xmlHttpRequest.open(HttpMethods.PUT, this.urlBuilder.buildTaskUrl(todoListId, taskId));
                xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                xmlHttpRequest.send(JSON.stringify(payload));
            });
        }

        /**
         * Sends remove task request.
         *
         * @param {TodoListId} todoListId ID of to-do list of task
         * @param {TaskId} taskId ID of task to remove
         * @param token token of user session
         * @return {Promise} promise to process request result.
         */
        removeTask(todoListId, taskId, token) {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        resolve();
                    } else {
                        reject();
                    }
                };

                xmlHttpRequest.open(HttpMethods.DELETE, this.urlBuilder.buildTaskUrl(todoListId, taskId));
                xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                xmlHttpRequest.send();
            });
        }

        /**
         * Sends read all tasks of to-do list request.
         *
         * @param {TodoListId} todoListId ID of to-do list to read tasks from
         * @param token token of user session
         * @return {Promise} promise to process request result,
         * which contains array of {@link Task} if request was successful.
         */
        readTasksFrom(todoListId, token) {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        const rawTasks = JSON.parse(xmlHttpRequest.response);
                        const tasks = rawTasks.map((el) => {
                            return new Task(new TaskId(el.id.value),
                                el.description,
                                new Date(el.creationDate),
                                el.completed,
                                new Date(el.lastUpdateDate))
                        });
                        resolve(tasks);
                    } else {
                        reject();
                    }
                };

                xmlHttpRequest.open(HttpMethods.GET, this.urlBuilder.buildTodoListUrl(todoListId));
                xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                xmlHttpRequest.send();
            });
        }
    }

    /**
     * Builds URLs to endpoints of to-do list application.
     *
     * @author Oleg Barmin
     */
    class UrlBuilder {

        constructor(url) {
            this.url = url;
        }

        buildTaskUrl(todoListId, taskId) {
            return `${this.url}/lists/${todoListId.id}/${taskId.id}`
        }

        buildTodoListUrl(todoListId) {
            return `${this.url}/lists/${todoListId.id}`
        }
    }

    /**
     * Provides HTTP methods names.
     *
     * @author Oleg Barmin
     */
    class HttpMethods {

        static get GET() {
            return "GET";
        }

        static get POST() {
            return "POST";
        }

        static get DELETE() {
            return "DELETE";
        }

        static get PUT() {
            return "PUT";
        }
    }

    /**
     * Allows to validate which methods of `Backend` is called inside `TodoList` and what parameters was given.
     *
     * @author Oleg Barmin
     */
    class MockBackend extends Backend {

        constructor() {
            super("MOCK");
            this._lastParams = {};
            this._mockReturnValue = "mock return value";
        }

        get lastParams() {
            return this._lastParams;
        }

        get mockReturnValue() {
            return this._mockReturnValue;
        }

        addTask(todoListId, taskId, payload, token) {
            this._lastParams = {
                todoListId: todoListId,
                taskId: taskId,
                payload: payload,
                token: token
            };
            return this._mockReturnValue;
        }

        updateTask(todoListId, taskId, payload, token) {
            this._lastParams = {
                todoListId: todoListId,
                taskId: taskId,
                payload: payload,
                token: token
            };
            return this._mockReturnValue;
        }


        removeTask(todoListId, taskId, token) {
            this._lastParams = {
                todoListId: todoListId,
                taskId: taskId,
                token: token
            };
            return this._mockReturnValue;
        }


        readTasksFrom(todoListId, token) {
            this._lastParams = {
                todoListId: todoListId,
                token: token
            };
            return this._mockReturnValue;
        }
    }

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

})));
