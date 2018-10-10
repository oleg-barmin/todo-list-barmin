(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.bundle = {})));
}(this, (function (exports) { 'use strict';

    /**
     * Is an event which happened in {@link TodoListApp} and marks what happened in `TodoListApp`.
     *
     * @author Oleg Barmin
     */
    class Event {

        /**
         * Creates `Event` instance.
         *
         * @param {EventType} eventType type of this event
         */
        constructor(eventType) {
            this.eventType = eventType;
        }
    }

    /**
     * Allows to posts {@link Event} and subscribe on `EventType` to process custom callbacks.
     *
     * When `Event` was posted all callbacks for `EventType` of occurred `Event` will be performed.
     * For `EventBus` work properly transport jQuery object should be provided.
     *
     * Example:
     * ```
     * const firstCustomEventType = new EventType("firstCustomEventType");
     * const secondCustomEventType = new EventType("secondCustomEventType");
     * const eventBus = new EventBus(transport);
     * ```
     *
     * When eventBus and custom EventTypes was declared you can bind callbacks for this event types.
     *
     * ```
     * eventBus.subscribe(firstCustomEventType, function(occurredEvent){
     *     console.log("First callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     *
     * eventBus.subscribe(firstCustomEventType, function(occurredEvent){
     *     console.log("Second callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     *
     * eventBus.subscribe(secondCustomEventType, function(occurredEvent){
     *     console.log("Third callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     * ```
     *
     * Now given callbacks will be performed, when `Event` with corresponded `EventType` will be occurred.
     *
     * ```
     * //first and second callback will be performed
     * eventBus.post(new Event(firstCustomEventType));
     * ```
     *
     * Console output after post:
     * <p>
     * `
     * First callback, occurredEvent: firstCustomEventType
     * Second callback, occurredEvent: secondCustomEventType
     * `
     *
     * if event of `secondCustomEventType` will be posted, only third callback will be processed.
     * ```
     * eventBus.post(new Event(secondCustomEventType));
     * ```
     *  Console output after post:
     *  <p>
     * `
     * Third callback, occurredEvent: secondCustomEventType
     * `
     *
     * Implementation of "event bus" design pattern, based on jQuery.
     *
     * @author Oleg Barmin
     */
    class EventBus {

        /**
         * Creates `EventBus` instance.
         *
         * @param transport transport jQuery object to bind `EventBus` on.
         *
         * @throws Error if given `transport` is not defined (null or undefined)
         * @throws TypeError if given object is no a jQuery object
         */
        constructor(transport) {
            if (!transport) {
                throw new Error("Transport for `EventBus` should be defined.")
            }
            if (!(transport instanceof $)) {
                throw new TypeError("jQuery object was expected.")
            }
            this._transport = transport;
        }

        /**
         * Performs all callbacks that were subscribed on `EventType` of given `Event`.
         *
         * @param {Event} event event which will be passed as argument to callbacks
         *                which subscribed to the `EventType` of given event.
         *
         * @throws TypeError if given `event` is no an instance of `Event`
         */
        post(event) {
            if (!(event instanceof Event)) {
                throw new TypeError("event argument should be instance of Event.");
            }

            let typeName = event.eventType.typeName;
            this._transport.trigger(typeName, [event]);
        }

        /**
         * Subscribes given callback to desired `EventType`.
         *
         * @param {EventType} eventType `EventType` to which callback should be bound
         * @param {Function} callback to bind onto `EventType`
         *
         * @return {Function} handler handler of `evenType` with given `callback`.
         *          Should be used to unsubscribe if needed.
         *
         * @throws TypeError if given `eventType` is not instance of `EventType`
         * @throws TypeError if given `callback` is not instance of `Function`
         */
        subscribe(eventType, callback) {
            if (!(eventType instanceof EventType)) {
                throw new TypeError("eventType argument should be instance of eventType.");
            }
            if (!(callback instanceof Function)) {
                throw new TypeError("callback argument should be instance of Function.");
            }

            const handler = (event, occurredEvent) => callback(occurredEvent);
            this._transport.on(eventType.typeName, handler);
            return handler;
        }

        /**
         * Unsubscribes given `handler` from `eventType`.
         *
         * @param {EventType} eventType type of event to which handler was subscribed
         * @param {Function} handler handler to unsubscribe
         *
         * @throws TypeError if given `eventType` is not instance of `EventType`
         * @throws TypeError if given `handler` is not instance of `Function`
         */
        unsubscribe(eventType, handler) {
            if (!(eventType instanceof EventType)) {
                throw new TypeError("eventType argument should be instance of eventType.");
            }
            if (!(handler instanceof Function)) {
                throw new TypeError("handler argument should be instance of Function.");
            }
            this._transport.off(eventType.typeName, handler);
        }
    }

    /**
     * Marks type of {@link Event} to {@link EventBus} bind and call callback of specified `EventType`.
     *
     * @author Oleg Barmin
     */
    class EventType {

        /**
         * Creates `EventType` instance.
         *
         * @param {string} typeName string name of the `EventType` instance.
         */
        constructor(typeName) {
            this.typeName = typeName;
        }
    }


    const EventTypes = {
        TaskAddRequest: new EventType("TaskAddRequested"),
        NewTaskAdded: new EventType("NewTaskAdded"),
        TaskListUpdated: new EventType("TaskListUpdated"),
        TaskCompletionRequested: new EventType("TaskCompletionRequested"),
        TaskRemovalRequested: new EventType("TaskRemovalRequested"),
        TaskEditingStarted: new EventType("TaskEditingStarted"),
        TaskEditingCanceled: new EventType("TaskEditingCanceled"),
        TaskUpdateRequested: new EventType("TaskUpdateRequested"),
        TaskRemovalFailed: new EventType("TaskRemovalFailed"),
        TaskCompletionFailed: new EventType("TaskCompletionFailed"),
        NewTaskValidationFailed: new EventType("NewTaskValidationFailed"),
        TaskUpdateFailed: new EventType("TaskUpdateFailed"),
        TaskRemoved: new EventType("TaskRemoved"),
        TaskUpdated: new EventType("TaskUpdated"),
        CredentialsSubmitted: new EventType("CredentialsSubmitted"),
        SignInFailed: new EventType("SignInFailed"),
        SignInCompleted: new EventType("SignInCompleted"),
        SignOutCompleted: new EventType("SignOutCompleted")
    };

    /**
     * Authenticate user by his username and password and signs out users from the system.
     *
     * @author Oleg Barmin
     */
    class Authentication {

        /**
         * Creates `Authentication` instance.
         */
        constructor() {
            this._token = null;
            this.tokenHeader = "X-Todo-Token";
            this.tokenKey = "org.javaclasses.todo.token";
        }

        /**
         * Returns stored token.
         *
         * @return {string} token value
         */
        get token() {
            return this._token;
        }

        /**
         * @param value value attempted to set to token.
         * @throws Error if try to call this method.
         */
        static set token(value) {
            throw new Error("Authentication token cannot be set from outside of class.")
        }

        /**
         * Validates if session token exists in local storage,
         * if it is then sends request to validate is this token expired.
         *
         * @return {Promise} to work with, which is rejected
         * if token wasn't stored in local storage or stored token expired,
         * otherwise promise is resolved.
         */
        checkSignInUser() {
            return new Promise((resolve, reject) => {
                const token = localStorage.getItem(this.tokenKey);
                if (token) {
                    const xmlHttpRequest = new XMLHttpRequest();
                    xmlHttpRequest.onload = () => {
                        if (xmlHttpRequest.status === 200) {
                            this._token = token;
                            resolve();
                        } else {
                            localStorage.removeItem(this.tokenKey);
                            this._token = null;
                            reject();
                        }
                    };
                    xmlHttpRequest.open("GET", "/auth");
                    xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                    xmlHttpRequest.send();
                } else {
                    reject();
                }
            });

        }

        /**
         * Returns `Promise` which signs in user with given username and password.
         *
         * If given credentials is valid promise will be resolved and
         * token of user session will be stored, otherwise
         * AuthenticationFailedException will be thrown inside promise.
         *
         * @param {String} username username of user who tries to sign-in
         * @param {String} password password of user who tries to sign-in
         * @returns {Promise} to work with
         */
        signIn(username, password) {
            return new Promise((resolve, reject) => {
                const usernameAndPassword = username + ":" + password;
                const encodedCredentials = btoa(usernameAndPassword);

                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        this._token = JSON.parse(xmlHttpRequest.response).value;
                        localStorage.setItem(this.tokenKey, this._token);
                        resolve(this._token);
                    } else {
                        reject(new AuthenticationFailedException());
                    }
                };

                xmlHttpRequest.open("POST", "/auth");
                xmlHttpRequest.setRequestHeader("Authentication", "Basic " + encodedCredentials);
                xmlHttpRequest.send();
            });
        }

        /**
         * Returns `Promise` which signs out user with stored token.
         *
         * If sign-out performed successfully this promise will be resolved and
         * token of user session will be erased.
         *
         * @returns {Promise} to work with
         */
        signOut() {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        resolve();
                        this._token = null;
                    } else {
                        reject();
                    }
                    localStorage.removeItem(this.tokenKey);
                };

                xmlHttpRequest.open("DELETE", "/auth");
                xmlHttpRequest.setRequestHeader(this.tokenHeader, this._token);
                xmlHttpRequest.send();
            });
        }
    }

    /**
     * Occurs when user typed in invalid username or password during authentication.
     *
     * @author Oleg Barmin
     */
    class AuthenticationFailedException extends Error {

        /**
         * Creates `AuthenticationFailedException` instance.
         */
        constructor() {
            super("Authentication failed.");
        }

    }

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
     * Occurs when controller adds new task to the model.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class NewTaskAdded extends Event {

        /**
         * Creates `NewTaskAdded` instance.
         *
         * @param {TodoListId} todoListId ID of to-do list to which task was added.
         */
        constructor(todoListId) {
            super(EventTypes.NewTaskAdded);
            this.todoListId = todoListId;
        }
    }

    /**
     * Occurs when validation of description of new task in `NewTaskValidationFailed` was failed.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class NewTaskValidationFailed extends Event{

        /**
         * Creates `NewTaskValidationFailed` instance.
         *
         * @param {string} errorMsg description of error
         * @param {TodoListId} todoListId ID of to-do list
         */
        constructor(errorMsg, todoListId) {
            super(EventTypes.NewTaskValidationFailed);
            this.errorMsg = errorMsg;
            this.todoListId = todoListId;
        }
    }

    /**
     * Occurs when `TaskRemovalRequested` cannot be processed was failed.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskRemovalFailed extends Event {

        /**
         * Creates `TaskRemovalFailed` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg) {
            super(EventTypes.TaskRemovalFailed);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Occurs when controller updated list of tasks.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskListUpdated extends Event {

        /**
         * Creates `TaskListUpdated` instance.
         *
         * @param {Array} taskArray sorted array of task from model.
         * @param {TodoListId } todoListId ID of `TodoList` which task was updated
         */
        constructor(taskArray, todoListId) {
            super(EventTypes.TaskListUpdated);
            this.taskArray = taskArray;
            this.todoListId = todoListId;
        }
    }

    /**
     * Occurs when `TaskUpdateRequested` cannot be processed.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskUpdateFailed extends Event {

        /**
         * Creates `TaskUpdateFailed` instance.
         *
         * @param {TaskId} taskId ID of task which updating was failed
         * @param {string} errorMsg error message to display on view
         */
        constructor(taskId, errorMsg) {
            super(EventTypes.TaskUpdateFailed);
            this.errorMsg = errorMsg;
            this.taskId = taskId;
        }
    }

    /**
     * Occurs when processing of `TaskRemovalRequested` event was performed successfully.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskRemoved extends Event{

        /**
         * Creates `TaskRemoved` instance.
         *
         * @param taskId ID of the task, which removal was performed
         */
        constructor(taskId){
            super(EventTypes.TaskRemoved);
            this.taskId = taskId;
        }
    }

    /**
     * Occurs when processing of `TaskUpdateRequested` event was performed successfully.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskUpdated extends Event{

        /**
         * Creates `TaskUpdated` instance.
         *
         * @param {TaskId} taskId description of error
         */
        constructor(taskId) {
            super(EventTypes.TaskUpdated);
            this.taskId = taskId;
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
     * Event-based facade for {@link TodoList}.
     *
     * @author Oleg Barmin
     */
    class DashboardController {

        /**
         * Creates `DashboardController` instance.
         *
         * During construction of instance it creates new `TodoList` instance,
         * where it will contains all tasks, and EventBus to process occurred events.
         *
         * @param {EventBus} eventBus evenBus to work with
         * @param {Authentication} authentication to authorized operations
         * @param {Array} todoListIds ID of `TodoList`s to work with
         */
        constructor(eventBus, authentication, todoListIds) {
            this.eventBus = eventBus;
            this.authentication = authentication;
            this.backend = new Backend(window.location.origin);
            this.todoLists = new Map();

            todoListIds.forEach(el => {
                this.todoLists.set(el.id, new TodoList(el, this.authentication.token, this.backend));

                this.todoLists.get(el.id).all().then(tasks => {
                    this.eventBus.post(new TaskListUpdated(tasks, el));
                }).catch(() => {
                    alert("task list updateTask failed.");
                });
            });

            /**
             * Sends requests to receive all tasks of this `TodoList`.
             *
             * If response code was 200: posts `TaskListUpdated` event with received tasks
             * of to-do list which tasks was updated.
             */
            const updateTaskList = (todoListId) => {
                this.todoLists.get(todoListId.id).all()
                    .then(tasks => {
                        this.eventBus.post(new TaskListUpdated(tasks, todoListId));
                    }).catch(() => {
                    alert("Failed to updateTask tasks list, try to reload page.");
                });
            };

            /**
             * Adds new task with description stored in occurred `TaskAddRequested` to `TodoList`.
             *
             * if given description is valid:
             *      - posts {@link NewTaskAdded}
             *      - posts {@link TaskListUpdated} with new task list.
             * Otherwise {@link NewTaskValidationFailed} will be posted
             *
             * @param {TaskAddRequested} taskAddRequested `TaskAddRequested` with description of the task to add.
             */
            const addTaskRequestCallback = taskAddRequested => {
                try {
                    this.todoLists.get(taskAddRequested.todoListId.id).add(taskAddRequested.taskDescription)
                        .then(() => {
                            this.eventBus.post(new NewTaskAdded(taskAddRequested.todoListId));
                            updateTaskList(taskAddRequested.todoListId);
                        }).catch(() => {
                        alert("Failed to add task, try to reload page.");
                    });
                } catch (e) {
                    this.eventBus.post(new NewTaskValidationFailed(e.message, taskAddRequested.todoListId));
                }
            };

            /**
             * Removes task with ID stored in occurred `TaskRemovalRequested` from `TodoList`.
             *
             * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
             * Otherwise: posts {@link TaskRemovalFailed}.
             *
             * @param {TaskRemovalRequested} taskRemovalEvent `TaskRemovalRequested` event with ID of the task to removeTask.
             */
            const taskRemovalRequestCallback = taskRemovalEvent => {
                try {
                    this.todoLists.get(taskRemovalEvent.todoListId.id).remove(taskRemovalEvent.taskId)
                        .then(() => {
                            this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                            updateTaskList(taskRemovalEvent.todoListId);
                        }).catch(() => {
                        alert("Failed to removeTask task, try to reload page.");
                    });
                } catch (e) {
                    this.eventBus.post(new TaskRemovalFailed("Task removal fail."));
                }
            };

            /**
             * Updates task in `TodoList` by ID with new description.
             * Both ID and new description are stored in occurred `TaskCompletionRequested` event.
             *
             * If task with given ID was found in `TodoList`:
             *      - posts {@link TaskUpdated} with ID of updated task.
             *      - posts {@link TaskListUpdated} with new task list.
             * Otherwise: posts {@link TaskUpdateFailed}.
             *
             * @param {TaskUpdateRequested} taskUpdateEvent `TaskUpdateRequested` event
             *        which contains ID of task to updateTask and its new description.
             */
            const taskUpdateRequestCallback = taskUpdateEvent => {
                try {
                    this.todoLists.get(taskUpdateEvent.todoListId.id).update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription, taskUpdateEvent.status)
                        .then(() => {
                            if (!taskUpdateEvent.status) {
                                this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                            }
                            updateTaskList(taskUpdateEvent.todoListId);
                        }).catch(() => {
                        alert("Failed to updateTask task, try to reload page.");
                    });
                } catch (e) {
                    this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId,
                        "New task description cannot be empty."));
                }
            };

            const addTaskHandler =
                eventBus.subscribe(EventTypes.TaskAddRequest, addTaskRequestCallback);
            const removeTaskHandler =
                eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
            const updateTaskHandler =
                eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);

            eventBus.subscribe(EventTypes.SignOutCompleted, () => {
                eventBus.unsubscribe(EventTypes.TaskAddRequest, addTaskHandler);
                eventBus.unsubscribe(EventTypes.TaskRemovalRequested, removeTaskHandler);
                eventBus.unsubscribe(EventTypes.TaskUpdateRequested, updateTaskHandler);
            });
        }

    }

    /**
     * Declares basic class for all sub-classes.
     * Each `UiComponent` sub-class should be connect with {@link EventBus},
     * and contain a element to render into.
     * Render method should be implemented to render the component into the `element`.
     *
     * @abstract
     */
    class UiComponent {

        /**
         * Saves given element to render into and `EventBus` to connect with controller.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         */
        constructor(element, eventBus) {
            this.element = element;
            this.eventBus = eventBus;
        }

        /**
         * Renders component into given element.
         */
        render() {
        }
    }

    /**
     * Renders necessary information to user according to current state of to-do list application.
     *
     * Only one page per application should be rendered.
     *
     * @author Oleg Barmin
     */
    class Page extends UiComponent {

        /**
         * Creates `Page` instance.
         *
         * @param {jQuery} element element to render page into
         * @param {EventBus} eventBus to subscribe and publish page specific events
         * @param {Authentication} authentication to authenticate users.
         */
        constructor(element, eventBus, authentication) {
            super(element, eventBus);
            this.authentication = authentication;
        }
    }

    /**
     * Occurs when user was successfully signed out from to-do list application.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class SignOutCompleted extends Event {

        /**
         * Creates `SignOutCompleted` instance.
         */
        constructor() {
            super(EventTypes.SignOutCompleted);
        }
    }

    /**
     * Navigation bar which allows users to perform operation, which available across all pages.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class NavBar extends UiComponent {

        /**
         * Creates `NavBar` instance.
         *
         * @param {jQuery} element element to render into
         * @param {EventBus} evenBus to subscribe and post component specific events
         * @param {Authentication} authentication to authenticate user
         */
        constructor(element, evenBus, authentication) {
            super(element, evenBus);
            this.authentication = authentication;
        }

        render() {
            this.element.empty();
            const signOutBtnClass = "signOutBtn";
            this.element.append(`<nav class="navbar bg-primary justify-content-between">
                              <a class="navbar-brand"></a>
                              <a class="${signOutBtnClass} text-white" href="#">Sign out</a>
                            </nav>`);

            let signOutBtn = this.element.find(`.${signOutBtnClass}`);

            signOutBtn.click(() => {
                this.authentication.signOut()
                    .then(() => {
                        this.eventBus.post(new SignOutCompleted());
                    });
            });

        }
    }

    /**
     * Occurred when new task was added on view.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskAddRequested extends Event {

        /**
         * Creates `TaskAddRequested` instance.
         *
         * @param {string} taskDescription description of new task
         * @param {TodoListId} todoListId ID of `TodoList` to which task was added
         */
        constructor(taskDescription, todoListId) {
            super(EventTypes.TaskAddRequest);
            this.todoListId = todoListId;
            this.taskDescription = taskDescription;
        }
    }

    /**
     * Component which responsible for rendering and processing of add task form.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class AddTaskForm extends UiComponent {

        /**
         * Creates `AddTaskForm` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {TodoListId} todoListId ID of to-do list to which `AddTaskForm` is related to
         */
        constructor(element, eventBus, todoListId) {
            super(element, eventBus);
            this.todoListId = todoListId;
        }

        /**
         * Renders tasks container and subscribes to necessary Events.
         */
        render() {
            const container = this.element;
            const descriptionTextAreaClass = "descriptionTextArea";
            const addTaskBtnClass = "addTaskBtn";
            const errorContainerClass = "errorMsgContainer";

            container.empty();

            container.append(`<div class="col">
                <textarea class="${descriptionTextAreaClass} form-control w-100"></textarea>
            </div>
            <div class="col col-1 align-self-end">
                <button class="${addTaskBtnClass} btn btn-default btn-primary w-100">Add</button>
            </div>
            <div class="w-100"></div>
            <div class="col alert alert-danger invisible errorMsgContainer w-100 pl-3" role="alert">
            </div>`);

            const addTaskBtn = container.find(`.${addTaskBtnClass}`);
            const descriptionTextArea = container.find(`.${descriptionTextAreaClass}`);
            const errorDiv = container.find(`.${errorContainerClass}`);
            const eventBus = this.eventBus;

            /**
             * Renders given error message under `descriptionTextAreaClass`.
             *
             * @param {string} errorMsg error message to render
             */
            const showErrorCallback = errorMsg => {
                errorDiv.empty();
                let iconSpan = $("<div>");
                iconSpan.addClass("octicon");
                iconSpan.addClass("octicon-stop");
                errorDiv.append(iconSpan);
                errorDiv.append(" " + errorMsg);
            };

            /**
             * Processes `NewTaskAdded` event.
             * Makes `descriptionTextArea` and `errorDiv` empty and invisible.
             *
             * @param {NewTaskAdded} event `NewTaskAdded` event which happened.
             */
            const newTaskAddedCallback = (event) => {
                if (this.todoListId.id === event.todoListId.id)
                    descriptionTextArea.val('');
                errorDiv.empty();
                errorDiv.addClass("invisible");
            };

            /**
             * Processes `NewTaskValidationFailed`.
             * Makes `errorDiv` visible and appends into it error message from occurred `NewTaskValidationFailed` event.
             *
             * @param {NewTaskValidationFailed} newTaskValidationFailedEvent `NewTaskValidationFailed` with
             *        error message to display.
             */
            const newTaskValidationFailedCallback = newTaskValidationFailedEvent => {
                if (newTaskValidationFailedEvent.todoListId.id === this.todoListId.id) {
                    errorDiv.removeClass("invisible");
                    showErrorCallback(newTaskValidationFailedEvent.errorMsg);
                }
            };

            eventBus.subscribe(EventTypes.NewTaskAdded, newTaskAddedCallback);
            eventBus.subscribe(EventTypes.NewTaskValidationFailed, newTaskValidationFailedCallback);

            addTaskBtn.click(() => eventBus.post(new TaskAddRequested(descriptionTextArea.val(), this.todoListId)));
            descriptionTextArea.keydown(keyboardEvent => {
                if ((keyboardEvent.ctrlKey || keyboardEvent.metaKey) && keyboardEvent.key === "Enter") {
                    eventBus.post(new TaskAddRequested(descriptionTextArea.val(), this.todoListId));
                }
            });
        }

    }

    /**
     * Occurs when task with specified ID need to be removed.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskRemovalRequested extends Event {

        /**
         * Creates `TaskRemovalRequested` instance.
         *
         * @param {TaskId} taskId ID of task to remove.
         * @param {TodoListId} todoListId ID of `TodoList` which task was removed
         */
        constructor(taskId, todoListId) {
            super(EventTypes.TaskRemovalRequested);
            this.taskId = taskId;
            this.todoListId = todoListId;
        }
    }

    /**
     * Occurs when end-user tries to edit a task.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskEditingStarted extends Event {

        /**
         * Creates `TaskEditingStarted` instance.
         *
         * @param {TaskId} taskId ID of a task which editing was requested.
         */
        constructor(taskId) {
            super(EventTypes.TaskEditingStarted);
            this.taskId = taskId;
        }
    }

    /**
     * Occurs when end-user submitted changes of a task description.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskUpdateRequested extends Event {

        /**
         * Creates `TaskUpdateRequested` instance.
         *
         * @param {TaskId} taskId ID of task which description needs to be updated
         * @param {string} newTaskDescription new description of task
         * @param {boolean} [status=false] status of tasks
         * @param {TodoListId} todoListId ID of `TodoList` which task update was requested
         */
        constructor(taskId, newTaskDescription, status = false, todoListId) {
            super(EventTypes.TaskUpdateRequested);
            this.taskId = taskId;
            this.status = status;
            this.newTaskDescription = newTaskDescription;
            this.todoListId = todoListId;
        }

    }

    /**
     * Component which responsible for rendering and processing of task in display state.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class TaskDisplay extends UiComponent {

        /**
         * Creates `TaskView` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         * @param {TodoListId} todoListId ID of to-do list to which `TaskDisplay` is related to
         */
        constructor(element, eventBus, number, task, todoListId) {
            super(element, eventBus);
            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
            this.todoListId = todoListId;
        }

        /**
         * Renders task into given element.
         */
        render() {
            const task = this.task;

            const removeBtnClass = "removeBtn";
            const completeBtnClass = "completeBtn";
            const editBtnClass = "editBtn";

            const escapedTaskDescription = $('<div/>').text(this.task.description).html();
            const taskDescriptionDivClass = "taskDescription";

            this.element.append(
                `<div class="col-md-auto pr-2">${this.number}.</div>
           <div class="col-9 ${taskDescriptionDivClass}" style="white-space: pre-wrap;">${escapedTaskDescription}</div>
                <div class="col text-right">
                    <button class="${editBtnClass} btn btn-light octicon octicon-pencil"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${completeBtnClass} btn btn-light octicon octicon-check"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${removeBtnClass} btn btn-light octicon octicon-trashcan"></button>
                </div>`
            );


            const removeBtn = this.element.find(`.${removeBtnClass}`);
            const completeBtn = this.element.find(`.${completeBtnClass}`);
            const editBtn = this.element.find(`.${editBtnClass}`);
            const taskDescriptionDiv = this.element.find(`.${taskDescriptionDivClass}`);

            completeBtn.click(() => this.eventBus.post(new TaskUpdateRequested(task.id, escapedTaskDescription, true, this.todoListId)));
            editBtn.click(() => this.eventBus.post(new TaskEditingStarted(task.id)));
            removeBtn.click(() => {
                if (confirm("Delete the task?")) {
                    this.eventBus.post(new TaskRemovalRequested(task.id, this.todoListId));
                }
            });

            if (task.completed) {
                completeBtn.remove();
                editBtn.remove();
                taskDescriptionDiv.replaceWith(() => $(`<del style="white-space: pre-wrap;"/>`)
                    .append(taskDescriptionDiv.contents()));
            }
        }
    }

    /**
     * Occurs when end-user tries to cancel a task editing.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class TaskEditingCanceled extends Event {

        /**
         * Creates `TaskEditingCanceled` instance.
         *
         * @param {TaskId} taskId ID of a task which editing was canceled
         */
        constructor(taskId) {
            super(EventTypes.TaskEditingCanceled);
            this.taskId = taskId;
        }
    }

    /**
     * Component which responsible for rendering and processing of task in edit state.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class TaskEdit extends UiComponent {

        /**
         * Creates `TaskEdit` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         * @param {TodoListId} todoListId ID of to-do list to which `TaskEdit` is related to
         */
        constructor(element, eventBus, number, task, todoListId) {
            super(element, eventBus);
            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
            this.todoListId = todoListId;
            this.currentInput = task.description;
            this.errorMsg = null;
        }

        render() {
            const saveBtnClass = "saveBtn";
            const cancelBtnClass = "cancelBtn";
            const editDescriptionTextAreaClass = "editDescriptionTextArea";
            const errorLabelClass = "errorMsgLabel";

            this.element.append(
                `<div class="col-md-auto pr-2">${this.number}.</div>
                <div class="col-9">
                    <textarea style="white-space: pre-wrap;" class="${editDescriptionTextAreaClass} form-control"></textarea>
                </div>
                <div class="col text-right">
                    <button class="${saveBtnClass} btn btn-sm btn-primary">Save</button>
                </div>
                <div class="col-sm-auto">
                    <button class="${cancelBtnClass} btn btn-sm btn-light">Cancel</button>
                </div>
                <div class="w-100"></div>
                <div class="col">
                <label class="col-sm-auto pr-0"></label>
                <label class="${errorLabelClass} col-9 invisible alert alert-danger p-1 mt-1"></label>
            </div>`
            );

            const saveBtn = this.element.find(`.${saveBtnClass}`);
            const cancelBtn = this.element.find(`.${cancelBtnClass}`);
            const editTextArea = this.element.find(`.${editDescriptionTextAreaClass}`);
            const errorLabel = this.element.find(`.${errorLabelClass}`);

            const descriptionRowsNumber = this.currentInput.split(/\r\n|\r|\n/).length;
            editTextArea.attr("rows", descriptionRowsNumber > 10 ? 10 : descriptionRowsNumber);
            editTextArea.focus().val(this.currentInput);
            editTextArea.scrollTop(editTextArea[0].scrollHeight - editTextArea.height());


            const renderErrorMsgCallback = errorMsg => {
                this.errorMsg = errorMsg;
                errorLabel.removeClass("invisible");
                errorLabel.empty();
                errorLabel.append(this.errorMsg);
            };

            if (this.errorMsg) {
                renderErrorMsgCallback(this.errorMsg);
            }


            /**
             * Processes `TaskUpdateFailed` event.
             * Makes `errorLabel` visible and appends into it error message from occurred `TaskUpdateFailed` event.
             *
             * @param {TaskUpdateFailed} taskUpdateFailedEvent occurred `TaskUpdateFailed` event
             *         with error message to display.
             */
            const taskUpdateFailedCallback = taskUpdateFailedEvent => {
                if (taskUpdateFailedEvent.taskId.compareTo(this.task.id) === 0) {
                    renderErrorMsgCallback(taskUpdateFailedEvent.errorMsg);
                }
            };
            this.eventBus.subscribe(EventTypes.TaskUpdateFailed, taskUpdateFailedCallback);

            cancelBtn.click(() => this.eventBus.post(new TaskEditingCanceled(this.task.id)));

            /**
             * Posts `TaskEditingCanceled` if tasks description equals content of textarea,
             * otherwise posts `TaskUpdateRequested`.
             */
            const saveCallback = () => {
                const newTaskDescription = editTextArea.val();
                if (newTaskDescription === this.task.description) {
                    this.eventBus.post(new TaskEditingCanceled(this.task.id));
                    return;
                }
                this.eventBus.post(new TaskUpdateRequested(this.task.id, newTaskDescription, false, this.todoListId));
            };

            saveBtn.click(saveCallback);

            editTextArea.change(() => this.currentInput = editTextArea.val());
            editTextArea.keydown(keyboardEvent => {
                if ((keyboardEvent.ctrlKey || keyboardEvent.metaKey) && keyboardEvent.key === "Enter") {
                    saveCallback();
                }
            });
        }
    }

    /**
     * Component which responsible for displaying of task.
     *
     * Has two states:
     *  - {@link TaskDisplay}
     *  - {@link TaskEdit}
     *
     *
     *  In `TaskDisplay` state end user is able to:
     *  - mark task as completed
     *  - remove task
     *  - switch to `TaskEdit` state
     *
     *
     *  In `TaskEdit` state end user is able to:
     *  - edit task description
     *  - save new task description
     *  - cancel editing (switch to `TaskDisplay` state)
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class TaskView extends UiComponent {

        /**
         * Creates `TaskView` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         * @param {TodoListId} todoListId ID of to-do list to which `TaskView` is related to
         */
        constructor(element, eventBus, number, task, todoListId) {
            super(element, eventBus);

            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
            this.todoListId = todoListId;
            this.currentState = new TaskDisplay(null, this.eventBus, null, null, this.todoListId);

            const startTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingStarted,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskEdit(this.element,
                            this.eventBus,
                            this.number,
                            this.task,
                            this.todoListId);
                        this.currentState.render();
                    }
                });

            const cancelTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingCanceled,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskDisplay(this.element,
                            this.eventBus,
                            this.number,
                            this.task,
                            this.todoListId);
                        this.currentState.render();
                    }
                });

            const taskUpdatePerformedHandler = this.eventBus.subscribe(EventTypes.TaskUpdated,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskDisplay(this.element,
                            this.eventBus,
                            this.number, this.task,
                            this.todoListId);
                        this.currentState.render();
                    }
                });

            const taskRemovalPerformedHandler = this.eventBus.subscribe(EventTypes.TaskRemoved,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.remove();
                        this.eventBus.unsubscribe(EventTypes.TaskEditingStarted, startTaskEditingHandler);
                        this.eventBus.unsubscribe(EventTypes.TaskEditingCanceled, cancelTaskEditingHandler);
                        this.eventBus.unsubscribe(EventTypes.TaskRemoved, taskRemovalPerformedHandler);
                        this.eventBus.unsubscribe(EventTypes.TaskUpdated, taskUpdatePerformedHandler);
                    }
                });
        }

        /**
         * Removes previous element, stores given element to render into, task and number of the task in the TodoList.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {number} number number of the task in list
         * @param {Task} task task to render.
         */
        update(element, number, task) {
            this.element.remove();
            this.element = element;
            this.number = number;
            this.task = task;
        }

        /**
         * Renders task in current state of TaskView onto given element.
         */
        render() {
            this.currentState.element = this.element;
            this.currentState.number = this.number;
            this.currentState.task = this.task;

            this.currentState.render();
        }
    }

    /**
     * Renders list of tasks.
     *
     * When {@link NewTaskAdded} happens gets new tasks,
     * removes previous task list and renders new tasks from `NewTaskAdded`.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class TodoWidget extends UiComponent {

        /**
         * Creates `TodoWidget` instance.
         *
         * @param {jQuery} element JQuery element where all task should be appended
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {TodoListId} todoListId ID of to-do list to which `TodoWidget` is related to
         */
        constructor(element, eventBus, todoListId) {
            super(element, eventBus);
            this.todoListId = todoListId;
            this.taskViewArray = [];
        }

        /**
         * Merges given `taskViewArray` and `taskArray`.
         *
         * If `taskViewArray` doesn't contain a `TaskView` with task from `taskArray` method creates new `TaskView`.
         *
         * @param {Array} taskViewArray array of `TaskView` array to merge with tasks
         * @param {Array} taskArray array of task to merge with
         * @return {Array} taskViewArray array of `TaskView`s in order of `taskArray`
         *
         * @private
         */
        _merge(taskViewArray, taskArray) {
            return taskArray.map((task, index) => {

                const taskContainer = this.element.append(`<div class="row no-gutters mt-2"></div>`).children().last();
                const taskViewWithCurrentTask = taskViewArray.find(element => element.task.id.compareTo(task.id) === 0);

                if (taskViewWithCurrentTask) {
                    taskViewWithCurrentTask.update(taskContainer, ++index, task);
                    return taskViewWithCurrentTask;
                }

                return new TaskView(taskContainer, this.eventBus, ++index, task, this.todoListId);
            });
        }

        /**
         * Empties given container for tasks to populate it with new tasks.
         */
        render() {
            this.element.empty();

            /**
             * Processes `TaskListUpdated` event.
             * Populates container of tasks with `TaskView` for each task stored in occurred `TaskListUpdated` event.
             *
             * @param {TaskListUpdated} taskListUpdatedEvent occurred `TaskListUpdated` event with array of new tasks.
             */
            const taskListUpdatedCallback = taskListUpdatedEvent => {
                if (taskListUpdatedEvent.todoListId === this.todoListId) {
                    this.element.empty();
                    this.taskViewArray = this._merge(this.taskViewArray, taskListUpdatedEvent.taskArray);
                    this.taskViewArray.forEach(taskView => taskView.render());
                }
            };

            this.eventBus.subscribe(EventTypes.TaskListUpdated, taskListUpdatedCallback);
            this.eventBus.subscribe(EventTypes.TaskCompletionFailed, event => alert(event.errorMsg));
            this.eventBus.subscribe(EventTypes.TaskRemovalFailed, event => alert(event.errorMsg));
        }

    }

    /**
     * Component which responsible for rendering of `TodoList`.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class TodoListView extends UiComponent {

        /**
         * Creates `TodoListView` instance.
         *
         * @param {jQuery} element element to render into
         * @param {EventBus} eventBus to subscribe and post component specific events.
         * @param {TodoListId} todoListId ID of to-do list to render
         */
        constructor(element, eventBus, todoListId) {
            super(element, eventBus);
            this.todoListId = todoListId;
        }

        /**
         * Renders `TodoListView` component into given element.
         */
        render() {
            this.element.empty();

            this.element.append(`<div class="row justify-content-md-center">
                                <div class="col-md-auto">
                                    <h1>To-Do</h1>
                                </div>
                                </div>
                            <div class="addTaskForm row justify-content-md-center"></div>
                            <div class="todoWidget"></div>`);


            let addTaskForm = new AddTaskForm(this.element.find(".addTaskForm"), this.eventBus, this.todoListId);
            let taskView = new TodoWidget(this.element.find(".todoWidget"), this.eventBus, this.todoListId);

            addTaskForm.render();
            taskView.render();
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
     * Provides user access to core functionality of to-do list application.
     *
     * @author Oleg Barmin
     */
    class DashboardPage extends Page {


        /**
         * Creates `DashboardPage` instance.
         *
         * @param {jQuery} element element to render page into
         * @param {EventBus} eventBus to subscribe and post page specific events
         * @param {Authentication} authentication to control user session
         * @param {UserLists} userLists to work with user lists
         */
        constructor(element, eventBus, authentication, userLists) {
            super(element, eventBus, authentication);
            this.userLists = userLists;
        }

        /**
         * Renders `DashboardPage` into given element.
         */
        render() {
            this.element.empty();

            // Renders navigation bar.
            const navBarContainerClass = "navBarContainer";
            this.element.append(`<div class='${navBarContainerClass}'></div>`);
            const navBarContainer = this.element.find(`.${navBarContainerClass}`);
            let navBar = new NavBar(navBarContainer, this.eventBus, this.authentication);
            navBar.render();

            /**
             * Renders `TodoListView`s for all given to-do list IDs.
             *
             * @param {Array} todoListsIds array of ID of to-do lists
             */
            const renderTodoListsArray = todoListsIds => {
                const userTodoListsId = todoListsIds.map(el => el);

                this.dashboardController = new DashboardController(this.eventBus,
                    this.authentication, userTodoListsId);

                userTodoListsId.forEach(el => {
                    this.element.append(`<div class='container'></div>`);
                    const container = this.element.find(".container").last();
                    new TodoListView(container, this.eventBus, el).render();
                });
            };

            /**
             * Renders all to-do lists of user, if user has no to-do list yet one to-do list will be created.
             *
             * @param {Array} todoListsIds array of IDs of to-do lists to render.
             */
            const prepareDashboard = todoListsIds => {
                if (todoListsIds.length === 0) {
                    // if user has no lists - create one
                    const initialTodoListId = TodoListIdGenerator.generateID();

                    // TODO:10/10/2018:oleg.barmin: leave creation of only one to-do list.
                    const secondTodoListId = TodoListIdGenerator.generateID();
                    this.userLists.create(initialTodoListId)
                        .then(() => {
                            this.userLists.create(secondTodoListId).then(() => {
                                renderTodoListsArray([initialTodoListId, secondTodoListId]);
                            });
                        });
                }
                else {
                    renderTodoListsArray(todoListsIds);
                }
            };

            this.userLists.readLists().then(prepareDashboard);
        }
    }

    /**
     * Occurs when user was successfully signed in into to-do list application.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class SignInCompleted extends Event {

        /**
         * Creates `SignInCompleted` instance.
         *
         * @param token of user who signed in
         */
        constructor(token) {
            super(EventTypes.SignInCompleted);
            this.token = token;
        }
    }

    /**
     * Occurs when user tried to sign-in with invalid username or password.
     *
     * @extends Event
     * @author Oleg Barmin
     */
    class SignInFailed extends Event {

        /**
         * Creates `SignInFailed` instance.
         */
        constructor() {
            super(EventTypes.SignInFailed);
        }
    }

    /**
     * Event based facade for `SignInPage`.
     *
     * @author Oleg Barmin
     */
    class SignInController {

        /**
         * Creates `SignInController` instance.
         *
         * @param {EventBus} eventBus to subscribe on page specific events
         * @param {Authentication} authentication to authenticate users
         */
        constructor(eventBus, authentication) {
            this.eventBus = eventBus;
            this.authentication = authentication;

            /**
             * Posts `SignInCompleted` event if user was successfully authenticated,
             * otherwise posts `SignInFailed` event.
             *
             * @param {CredentialsSubmitted} credentialsSubmittedEvent event
             *         which contains username and password typed in by user
             */
            const credentialsSubmittedRequestCallback = credentialsSubmittedEvent => {
                const username = credentialsSubmittedEvent.username;
                const password = credentialsSubmittedEvent.password;

                this.authentication
                    .signIn(username, password)
                    .then(token => this.eventBus.post(new SignInCompleted(token)))
                    .catch(() => this.eventBus.post(new SignInFailed()));
            };

            const credentialsSubmittedHandler = eventBus.subscribe(EventTypes.CredentialsSubmitted,
                credentialsSubmittedRequestCallback);

            eventBus.subscribe(EventTypes.SignInCompleted,
                () => eventBus.unsubscribe(EventTypes.CredentialsSubmitted, credentialsSubmittedHandler));
        }
    }

    /**
     * Occurs when end-user tries to sing-in into to-do list application.
     *
     * @author Oleg Barmin
     * @extends Event
     */
    class CredentialsSubmitted extends Event {

        /**
         * Creates `CredentialsSubmitted` instance.
         *
         * @param {String} username username of user
         * @param {String} password password of user
         */
        constructor(username, password) {
            super(EventTypes.CredentialsSubmitted);
            this.username = username;
            this.password = password;
        }
    }

    /**
     * Sign-in form which meets unauthenticated user on homepage of to-do list application.
     *
     * <p>End-user has to sign-in through this form to get access to
     * all to-do list application functionality.
     *
     * @extends UiComponent
     * @author Oleg Barmin
     */
    class SignInForm extends UiComponent {

        /**
         * Creates `SignInForm` instance.
         *
         * @param {jQuery} element element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         */
        constructor(element, eventBus) {
            super(element, eventBus);
        }

        /**
         * Renders `SignInForm` into the given element.
         */
        render() {
            this.element.append(`<div class="row justify-content-md-center">
        <div class="col-4 login-form">
            <h2 class="text-center">Sign In</h2>
            <input class="usernameInput form-control" placeholder="Username">
            <input class="passwordInput mt-2 form-control" placeholder="Password" type="password">
            <label class="d-none errorLabel mt-2 form-control alert alert-danger"></label>
            <button class="loginBtn mt-2 form-control btn btn-primary">Sign In</button>
        </div>
    </div>`);

            const loginDiv = this.element.find(".login-form");

            const usernameInput = loginDiv.find(".usernameInput");
            const passwordInput = loginDiv.find(".passwordInput");
            const errorLabel = loginDiv.find(".errorLabel");
            const loginBtn = loginDiv.find(".loginBtn");

            /**
             * Posts `CredentialsSubmitted` with typed in username and password.
             */
            const sendSubmittedCredentials = () => {
                const username = usernameInput.val();
                const password = passwordInput.val();

                if (username.trim().length === 0 || password.trim().length === 0) {
                    errorLabel.text("Username/password field cannot be empty.");
                    errorLabel.removeClass("d-none");
                    return;
                }

                this.eventBus.post(new CredentialsSubmitted(username, password));
            };

            this.eventBus.subscribe(EventTypes.SignInFailed, () => {
                errorLabel.text("Invalid username or password.");
                errorLabel.removeClass("d-none");
            });
            loginBtn.click(sendSubmittedCredentials);
            loginDiv.keydown(keyboardEvent => {
                if (keyboardEvent.key === "Enter") {
                    sendSubmittedCredentials();
                }
            });
        }
    }

    /**
     * Page which allows to end-user to authenticate into to-do list application
     * and get access to applications functionality.
     *
     * @author Oleg Barmin
     */
    class SignInPage extends Page {

        /**
         * Renders `SingInPage` into the given element.
         */
        render() {
            this.element.empty();
            this.element.append(`<div class='container'></div>`);

            const container = this.element.find(".container");

            this.signInController = new SignInController(this.eventBus, this.authentication);
            this.signInForm = new SignInForm(container, this.eventBus);

            this.signInForm.render();
        }
    }

    /**
     * Allow to manage user to-do lists by sending requests to server.
     *
     * @author Oleg Barmin
     */
    class UserLists {

        /**
         * Creates `UserLists` instance.
         *
         * @param token token of user
         */
        constructor(token) {
            this.token = token;
        }

        /**
         * Sends read all to-do list request of user by his token.
         *
         * Method provides `Promise` instance which is resolved in case
         * if to-do list ID was received successfully, otherwise promise will be rejected.
         *
         * @returns {Promise} promise to work with.
         */
        readLists() {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        const todoLists = JSON.parse(xmlHttpRequest.response);
                        const todoListIds = todoLists.map((el) => new TodoListId(el.id.value));
                        resolve(todoListIds);
                    } else {
                        reject();
                    }
                };

                xmlHttpRequest.open("GET", "/lists");
                xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
                xmlHttpRequest.send();
            });
        }

        /**
         * Sends creates new to-do lists with given ID request.
         *
         * Method provides `Promise` instance which is resolved in case
         * if to-do list was created successfully, otherwise promise will be rejected.
         *
         * @param {TodoListId} todoListId ID of to-do list to create with
         * @returns {Promise} promise to work with
         */
        create(todoListId) {
            return new Promise((resolve, reject) => {
                const xmlHttpRequest = new XMLHttpRequest();

                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        resolve();
                    } else {
                        reject();
                    }
                };

                xmlHttpRequest.open("POST", `/lists/${todoListId.id}`);
                xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
                xmlHttpRequest.send();
            });
        }
    }

    /**
     * Starts a to-do list app.
     *
     * @author Oleg Barmin
     */
    class TodoListApp {

        /**
         * Creates `TodoListApp` instance.
         *
         * @param rootElement root jQuery element where to-do app will be deployed
         */
        constructor(rootElement) {
            this.root = rootElement;
            this.authentication = new Authentication();
        }

        /**
         * Starts a `TodoListApp` for `rootElement` that was provided in constructor.
         *
         * Creates an environment for necessary components and renders them.
         */
        start() {
            this.eventBus = new EventBus(this.root);

            this.signInPage = new SignInPage(this.root, this.eventBus, this.authentication);

            const signInCompletedCallback = () => {
                this.userLists = new UserLists(this.authentication.token);
                this.dashboardPage = new DashboardPage(this.root, this.eventBus, this.authentication, this.userLists);
                this.dashboardPage.render();
            };

            this.eventBus.subscribe(EventTypes.SignInCompleted, signInCompletedCallback);
            this.eventBus.subscribe(EventTypes.SignOutCompleted, () => this.signInPage.render());

            this.authentication.checkSignInUser()
                .then(() => {
                    this.userLists = new UserLists(this.authentication.token);
                    this.dashboardPage = new DashboardPage(this.root, this.eventBus, this.authentication, this.userLists);
                    this.dashboardPage.render();
                })
                .catch(() => {
                    this.signInPage.render();
                });
        }
    }


    $(function () {
        let todoLists = $(".todoList");
        new TodoListApp($(todoLists[0])).start();
    });

    exports.TodoListApp = TodoListApp;

    Object.defineProperty(exports, '__esModule', { value: true });

})));
