(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.bundle = {})));
}(this, (function (exports) { 'use strict';

    /**
     * Is an event which happened in {@link TodoListApp} and marks what happened in `TodoListApp`.
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
        AddTaskRequest: new EventType("AddTaskRequest"),
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
        TaskUpdated: new EventType("TaskUpdated")
    };

    /**
     * Declares basic class for all sub-classes.
     * Each `TodoComponent` sub-class should be connect with {@link EventBus},
     * and contain a element to render into.
     * Render method should be implemented to render the component into the `element`.
     *
     * @abstract
     */
    class TodoComponent {

        /**
         * Saves given element to render into and `EventBus` to connect with controller.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         */
        constructor(element, eventBus){
            this.element = element;
            this.eventBus = eventBus;
        }

        /**
         * Renders component into given element.
         */
        render(){}
    }

    /**
     * Occurred when new task was added on view.
     *
     * @extends Event
     */
    class AddTaskRequest extends Event{

        /**
         * Creates `AddTaskRequest` instance.
         *
         * @param {string} taskDescription description of new task
         */
        constructor(taskDescription){
            super(EventTypes.AddTaskRequest);
            this.taskDescription = taskDescription;
        }
    }

    /**
     * Component which responsible for rendering and processing of add task form.
     */
    class AddTaskForm extends TodoComponent {

        /**
         * Creates `AddTaskForm` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         */
        constructor(element, eventBus) {
            super(element, eventBus);
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
             */
            const newTaskAddedCallback = () => {
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
                errorDiv.removeClass("invisible");
                showErrorCallback(newTaskValidationFailedEvent.errorMsg);
            };

            eventBus.subscribe(EventTypes.NewTaskAdded, newTaskAddedCallback);
            eventBus.subscribe(EventTypes.NewTaskValidationFailed, newTaskValidationFailedCallback);

            addTaskBtn.click(() => eventBus.post(new AddTaskRequest(descriptionTextArea.val())));
            descriptionTextArea.keydown(keyboardEvent => {
                if ((keyboardEvent.ctrlKey || keyboardEvent.metaKey) && keyboardEvent.key === "Enter") {
                    eventBus.post(new AddTaskRequest(descriptionTextArea.val()));
                }
            });
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

    /**
     * Occurs when controller adds new task to the model.
     *
     * @extends Event
     */
    class NewTaskAdded extends Event {

        /**
         * Creates `NewTaskAdded` instance.
         */
        constructor() {
            super(EventTypes.NewTaskAdded);
        }
    }

    /**
     * Occurs when validation of description of new task in `AddTaskRequest` was failed.
     *
     * @extends Event
     */
    class NewTaskValidationFailed extends Event{

        /**
         * Creates `NewTaskValidationFailed` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg){
            super(EventTypes.NewTaskValidationFailed);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Occurs when `TaskCompletionRequest` cannot be processed was failed.
     *
     * @extends Event
     */
    class TaskCompletionFailed extends Event {

        /**
         * Creates `TaskRemovalFailed` instance.
         *
         * @param {string} errorMsg description of error
         */
        constructor(errorMsg) {
            super(EventTypes.TaskCompletionFailed);
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Occurs when `TaskRemovalRequested` cannot be processed was failed.
     *
     * @extends Event
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
     */
    class TaskListUpdated extends Event {

        /**
         * Creates `TaskListUpdated` instance.
         *
         * @param {Array} taskArray sorted array of task from model.
         */
        constructor(taskArray) {
            super(EventTypes.TaskListUpdated);
            this.taskArray = taskArray;
        }
    }

    /**
     * Occurs when `TaskUpdateRequested` cannot be processed.
     *
     * @extends Event
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
     * Event-based facade for {@link TodoList}.
     */
    class Controller {

        /**
         * Creates `Controller` instance.
         *
         * During construction of instance it creates new `TodoList` instance,
         * where it will contains all tasks, and EventBus to process occurred events.
         *
         * @param {EventBus} eventBus evenBus to work with
         */
        constructor(eventBus) {
            this.todoList = new TodoList();
            this.eventBus = eventBus;

            /**
             * Adds new task with description stored in occurred `AddTaskRequest` to `TodoList`.
             *
             * if given description is valid:
             *      - posts {@link NewTaskAdded}
             *      - posts {@link TaskListUpdated} with new task list.
             * Otherwise {@link NewTaskValidationFailed} will be posted
             *
             * @param {AddTaskRequest} addTaskEvent `AddTaskRequest` with description of the task to add.
             */
            const addTaskRequestCallback = addTaskEvent => {
                try {
                    this.todoList.add(addTaskEvent.taskDescription);
                    this.eventBus.post(new NewTaskAdded());
                    this.eventBus.post(new TaskListUpdated(this.todoList.all()));
                } catch (e) {
                    this.eventBus.post(new NewTaskValidationFailed(e.message));
                }
            };

            /**
             * Removes task with ID stored in occurred `TaskRemovalRequested` from `TodoList`.
             *
             * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
             * Otherwise: posts {@link TaskRemovalFailed}.
             *
             * @param {TaskRemovalRequested} taskRemovalEvent `TaskRemovalRequested` event with ID of the task to remove.
             */
            const taskRemovalRequestCallback = taskRemovalEvent => {
                try {
                    this.todoList.remove(taskRemovalEvent.taskId);
                    this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                    this.eventBus.post(new TaskListUpdated(this.todoList.all()));
                } catch (e) {
                    this.eventBus.post(new TaskRemovalFailed("Task removal fail."));
                }
            };

            /**
             * Completes task in `TodoList` by ID which stored in occurred `TaskCompletionRequested`.
             *
             * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
             * Otherwise: posts {@link TaskCompletionFailed}.
             *
             * @param {TaskCompletionRequested} taskCompletionEvent `TaskCompletionRequested` event
             *        which contains ID of task to complete.
             */
            const taskCompletionRequestedCallback = taskCompletionEvent => {
                try {
                    this.todoList.complete(taskCompletionEvent.taskId);
                    this.eventBus.post(new TaskListUpdated(this.todoList.all()));
                } catch (e) {
                    this.eventBus.post(new TaskCompletionFailed("Task completion fail."));
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
             *        which contains ID of task to update and its new description.
             */
            const taskUpdateRequestCallback = taskUpdateEvent => {
                try {
                    this.todoList.update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription);
                    this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                    this.eventBus.post(new TaskListUpdated(this.todoList.all()));
                } catch (e) {
                    this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId, "New task description cannot be empty."));
                }
            };

            eventBus.subscribe(EventTypes.AddTaskRequest, addTaskRequestCallback);
            eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
            eventBus.subscribe(EventTypes.TaskCompletionRequested, taskCompletionRequestedCallback);
            eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);
        }

    }

    /**
     * Occurs when task with specified ID need to be removed.
     *
     * @extends Event
     */
    class TaskRemovalRequested extends Event {

        /**
         * Creates `TaskRemovalRequested` instance.
         *
         * @param {TaskId} taskId ID of task to remove.
         */
        constructor(taskId) {
            super(EventTypes.TaskRemovalRequested);
            this.taskId = taskId;
        }
    }

    /**
     * Occurs when task with specified ID need to be completed.
     *
     * @extends Event
     */
    class TaskCompletionRequested extends Event {

        /**
         * Creates `TaskCompletionRequested` instance.
         *
         * @param {TaskId} taskId ID of task to remove.
         */
        constructor(taskId) {
            super(EventTypes.TaskCompletionRequested);
            this.taskId = taskId;
        }
    }

    /**
     * Occurs when end-user tries to edit a task.
     *
     * @extends Event
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
     * Component which responsible for rendering and processing of task in display state.
     *
     * @extends TodoComponent
     */
    class TaskDisplay extends TodoComponent {

        /**
         * Creates `TaskView` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         */
        constructor(element, eventBus, number, task) {
            super(element, eventBus);
            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
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

            completeBtn.click(() => this.eventBus.post(new TaskCompletionRequested(task.id)));
            editBtn.click(() => this.eventBus.post(new TaskEditingStarted(task.id)));
            removeBtn.click(() => {
                if (confirm("Delete the task?")) {
                    this.eventBus.post(new TaskRemovalRequested(task.id));
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
     * Occurs when end-user submitted changes of a task description.
     *
     * @extends Event
     */
    class TaskUpdateRequested extends Event{

        /**
         * Creates `TaskUpdateRequested` instance.
         *
         * @param {TaskId} taskId ID of task which description needs to be updated
         * @param {string} newTaskDescription new description of task.
         */
        constructor(taskId, newTaskDescription) {
            super(EventTypes.TaskUpdateRequested);
            this.taskId = taskId;
            this.newTaskDescription = newTaskDescription;
        }

    }

    /**
     * Component which responsible for rendering and processing of task in edit state.
     *
     * @extends TodoComponent
     */
    class TaskEdit extends TodoComponent {

        /**
         * Creates `TaskEdit` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         */
        constructor(element, eventBus, number, task) {
            super(element, eventBus);
            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
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
                <label class="${errorLabelClass} alert alert-danger invisible w-100 alert-danger"></label>
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

            if(this.errorMsg){
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
                this.eventBus.post(new TaskUpdateRequested(this.task.id, newTaskDescription));
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
     * @extends TodoComponent
     */
    class TaskView extends TodoComponent {

        /**
         * Creates `TaskView` instance.
         *
         * @param {jQuery} element jQuery element to render into
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         * @param {Number} number number of the task in the list of tasks
         * @param {Task} task task to render
         */
        constructor(element, eventBus, number, task) {
            super(element, eventBus);

            this.eventBus = eventBus;
            this.task = task;
            this.number = number;
            this.currentState = new TaskDisplay(null, this.eventBus, null, null);

            const startTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingStarted,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskEdit(this.element, this.eventBus, this.number, this.task);
                        this.currentState.render();
                    }
                });

            const cancelTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingCanceled,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                        this.currentState.render();
                    }
                });

            const taskUpdatePerformedHandler = this.eventBus.subscribe(EventTypes.TaskUpdated,
                occurredEvent => {
                    if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                        this.element.empty();
                        this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                        this.currentState.render();
                    }
                });

            const taskRemovalPerformedHandler = this.eventBus.subscribe(EventTypes.TaskRemoved,
                (occurredEvent) => {
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
         * todo jsDoc
         */
        isEditing(){
            return this.currentState instanceof TaskEdit;
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
     * @extends TodoComponent
     */
    class TodoWidget extends TodoComponent {

        /**
         * Creates `TodoWidget` instance.
         *
         * @param {jQuery} element JQuery element where all task should be appended
         * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
         */
        constructor(element, eventBus) {
            super(element, eventBus);
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
        _merge(taskViewArray, taskArray){
            return taskArray.map((task, index) => {

                const taskContainer = this.element.append(`<div class="row no-gutters mt-2"></div>`).children().last();
                const taskViewWithCurrentTask = taskViewArray.find(element => element.task.id.compareTo(task.id) === 0);

                if(taskViewWithCurrentTask){
                    taskViewWithCurrentTask.update(taskContainer, ++index, task);
                    return taskViewWithCurrentTask;
                }

                return new TaskView(taskContainer, this.eventBus, ++index, task);
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
                this.element.empty();
                this.taskViewArray = this._merge(this.taskViewArray, taskListUpdatedEvent.taskArray);
                this.taskViewArray.forEach(taskView => taskView.render());
            };

            this.eventBus.subscribe(EventTypes.TaskListUpdated, taskListUpdatedCallback);
            this.eventBus.subscribe(EventTypes.TaskCompletionFailed, event => alert(event.errorMsg));
            this.eventBus.subscribe(EventTypes.TaskRemovalFailed, event => alert(event.errorMsg));
        }

    }

    /**
     * Starts a to-do list app.
     */
    class TodoListApp {

        /**
         * Creates `TodoListApp` instance.
         *
         * @param rootElement root jQuery element where to-do app will be deployed
         */
        constructor(rootElement) {
            this.root = rootElement;
        }

        /**
         * Starts a `TodoListApp` for `rootElement` that was provided in constructor.
         *
         * Creates an environment for necessary components and renders them.
         */
        start() {
            this.root.append("<div class='container'></div>");
            let container = $(this.root.find(".container")[0]);

            container.append(`<div hidden class="eventBus"></div>
        <div class="row justify-content-md-center">
            <div class="col-md-auto">
                <h1>To-Do</h1>
            </div>
        </div>
        <div class="addTaskForm row justify-content-md-center"></div>
        <div class="todoWidget"></div>`);

            this.eventBus = new EventBus(container.find(".eventBus"));
            this.controller = new Controller(this.eventBus);

            let addTaskForm = new AddTaskForm(container.find(".addTaskForm"), this.eventBus);
            let taskView = new TodoWidget(container.find(".todoWidget"), this.eventBus);

            addTaskForm.render();
            taskView.render();
        }
    }


    $(function () {
        let todoLists = $(".todoList");
        new TodoListApp($(todoLists[0])).start();
    });

    exports.TodoListApp = TodoListApp;

    Object.defineProperty(exports, '__esModule', { value: true });

})));
