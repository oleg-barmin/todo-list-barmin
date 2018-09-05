(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (factory());
}(this, (function () { 'use strict';

    /**
     * Transport object transfers data between {@link Controller} and TodoComponents.
     *
     * Is used in {@link EventBus}.
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
     * For `EventBus` work properly transport JQuery object should be provided.
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
     * Implementation of "event bus" design pattern, based on jquery.
     */
    class EventBus {

        /**
         * Creates `EventBus` instance.
         *
         * @param transport transport JQuery object to bind `EventBus` on.
         */
        constructor(transport) {
            this._transport = transport;
        }

        /**
         * Triggers all callback bound on `EventType` of given `Event`.
         *
         * @param {Event} event event which will be passed as argument to callbacks
         *                which subscribed to the `EventType` of given event.
         */
        post(event) {
            let typeName = event.eventType.typeName;
            this._transport.trigger(typeName, [event]);
        }

        /**
         * Binds given callback to desired `EventType`.
         *
         * @param {EventType} eventType `EventType` to which callback should be bound
         * @param {Function} callback to bind onto `EventType`
         */
        subscribe(eventType, callback) {
            this._transport.on(eventType.typeName, function (event, occurredEvent) {
                callback(occurredEvent);
            });
        }
    }

    /**
     * Marks type of {@link Event} to {@link EventBus} bind and call callback of specified `EventType`.
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


    const EventTypeEnumeration = {
        AddTaskRequest: new EventType("AddTaskRequest"),
        NewTaskAddedEvent: new EventType("NewTaskAddedEvent"),
        NewTaskValidationFailed: new EventType("NewTaskValidationFailed"),
        TaskCompletionRequested: new EventType("TaskCompletionRequested"),
        TaskRemovalRequest: new EventType("TaskRemovalRequest"),
        TaskCompletionFailed: new EventType("TaskCompletionFailed"),
        TaskRemovalFail: new EventType("TaskRemovalFailed")
    };

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
         * @param taskId `TaskId` to compare with
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
     * Generates unique TaskId for `Task`.
     *
     * Current implementation based on uuid v4.
     */
    class TaskIdGenerator {

        /**
         * Generates unique TaskID.
         *
         * @returns {TaskId} ID generated TaskID.
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
         */
        constructor() {
            this._tasksArray = [];
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
            Preconditions.checkStringNotEmpty(taskDescription, "task description");

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
            Preconditions.checkStringNotEmpty(updatedDescription, "updated description");

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

    /**
     * Event which occurred when new task was added to model.
     * Transfers array of Tasks;
     *
     * @extends Event
     */
    class NewTaskAddedEvent extends Event{

        /**
         * Creates `NewTaskAddedEvent` instance.
         *
         * @param {Array} taskArray sorted array of task from model.
         */
        constructor(taskArray){
            super(EventTypeEnumeration.NewTaskAddedEvent);
            this.taskArray = taskArray;
        }
    }

    /**
     * Event which occurred when new task description validation failed.
     */
    class NewTaskValidationFailedEvent extends Event{

        /**
         * Creates `NewTaskValidationFailedEvent` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg){
            super(EventTypeEnumeration.NewTaskValidationFailed);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Occurs when `TaskCompletionRequest` cannot be processed properly.
     */
    class TaskCompletionFailed extends Event {

        /**
         * Creates `TaskRemovalFailed` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg) {
            super(EventTypeEnumeration.TaskCompletionFailed);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Occurs when `TaskRemovalRequest` cannot be processed properly.
     */
    class TaskRemovalFailed extends Event {

        /**
         * Creates `TaskRemovalFailed` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg) {
            super(EventTypeEnumeration.TaskRemovalFail);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Connects model of {@link TodoList} and {@link TodoComponent}.
     * Reacts on {@link Event} which occurred on view layer.
     */
    class Controller {

        /**
         * Creates `Controller` instance.
         *
         * During constuction of instance it creates new {@link TodoList} instance,
         * where it will contains all tasks and EventBus to process occurred events.
         *
         * @param {EventBus} eventBus evenBus to work with
         */
        constructor(eventBus) {
            this.todoList = new TodoList();
            this.eventBus = eventBus;

            const self = this;
            eventBus.subscribe(EventTypeEnumeration.AddTaskRequest, function (occurredEvent) {
                try {
                    self.todoList.add(occurredEvent.taskDescription);
                    self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()));
                } catch (e) {
                    self.eventBus.post(new NewTaskValidationFailedEvent(e.message));
                }
            });

            eventBus.subscribe(EventTypeEnumeration.TaskRemovalRequest, function (occurredEvent) {
                try {
                    self.todoList.remove(occurredEvent.taskId);
                    self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()));
                } catch (e) {
                    self.eventBus.post(new TaskRemovalFailed("Task removal fail."));
                }
            });

            eventBus.subscribe(EventTypeEnumeration.TaskCompletionRequested, function (occurredEvent) {
                try {
                    self.todoList.complete(occurredEvent.taskId);
                    self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()));
                } catch (e) {
                    self.eventBus.post(new TaskCompletionFailed("Task completion fail."));
                }
            });
        }

    }

    /**
     * Event which occurred when new task was added on view.
     * Transfers description of new task.
     */
    class AddTaskRequestEvent extends Event{

        /**
         * Creates `AddTaskRequestEvent` instance.
         *
         * @param {string} taskDescription description of new task
         */
        constructor(taskDescription){
            super(EventTypeEnumeration.AddTaskRequest);
            this.taskDescription = taskDescription;
        }
    }

    QUnit.module("EventBus should");
    QUnit.test("call ", assert => {
        let transportElement = $("#eventBus");
        const eventBus = new EventBus(transportElement);

        const firstEventType = new EventType("firstEventType");
        const secondEventType = new EventType("secondEventType");

        const firstEvent = new Event(firstEventType);
        const secondEvent = new Event(secondEventType);

        firstEvent.interaction = 0;
        secondEvent.interaction = 0;

        const callback = occurredEvent => occurredEvent.interaction += 1;

        eventBus.subscribe(firstEventType, callback);
        eventBus.subscribe(secondEventType, callback);

        eventBus.post(firstEvent);

        assert.strictEqual(firstEvent.interaction, 1, "callback once for single post.");

        eventBus.post(firstEvent);
        eventBus.post(firstEvent);

        assert.strictEqual(firstEvent.interaction, 3, "callback twice for two posts.");
        assert.strictEqual(secondEvent.interaction, 0, "call only subscribed for this event callbacks.");
    });

    QUnit.module("Controller should");
    QUnit.test("", assert => {
        let transportElement = $("#eventBus");
        const eventBus = new EventBus(transportElement);

        const todoList = new TodoList();
        const controller = new Controller(eventBus);

        let newTaskAddedEventWasProduced = false;
        let newTaskValidationFailed = false;

        eventBus.subscribe(EventTypeEnumeration.NewTaskAddedEvent, () => newTaskAddedEventWasProduced = true);
        eventBus.subscribe(EventTypeEnumeration.NewTaskValidationFailed, () => newTaskValidationFailed = true);

        controller.todoList = todoList;
        const taskDescription = "new task";
        eventBus.post(new AddTaskRequestEvent(taskDescription));
        eventBus.post(new AddTaskRequestEvent(""));


        assert.strictEqual(todoList.all()[0].description, taskDescription, "add new task to TodoList.");
        assert.ok(newTaskAddedEventWasProduced, "post a newTaskAddedEvent after success AddTaskRequestEvent process.");
        assert.ok(newTaskAddedEventWasProduced, "post a newTaskAddedEventWasProduced if given task description is empty.");

    });

})));
