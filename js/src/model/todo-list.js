import {Preconditions} from "../lib/preconditions.js";
import {Task} from "./task.js";
import {TaskIdGenerator} from "../lib/taskIdGenerator.js";
import {TaskId} from "../lib/identifiers";

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
export class TodoList {

    /**
     * Creates `TodoList` instance.
     *
     * @param {TodoListId} todoListId ID of this to-do lists
     * @param token token of user who works with this to-do list
     */
    constructor(todoListId, token) {
        this._tasksArray = [];
        this.todoListId = todoListId;
        this.token = token;
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

        return new Promise((resolve, reject) => {
            const taskId = TaskIdGenerator.generateID();
            const xmlHttpRequest = new XMLHttpRequest();

            const createTaskPayload = {
                taskDescription: taskDescription.trim()
            };

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve()
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("POST", `/lists/${this.todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
            xmlHttpRequest.send(JSON.stringify(createTaskPayload));
        });
    }

    /**
     * Sends request to update task with given ID with new status and description.
     *
     * @param {TaskId} taskId ID of task to update
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

        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            const updateTaskPayload = {
                taskStatus: status,
                taskDescription: updatedDescription.trim()
            };

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve()
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("PUT", `/lists/${this.todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
            xmlHttpRequest.send(JSON.stringify(updateTaskPayload));
        });
    }

    /**
     * Sends request to remove task with given ID.
     *
     * @param {TaskId} taskId ID of task to delete
     * @return {Promise} promise to work with. If request was processed successfully
     *                  promise will be resolved, otherwise it will be rejected.
     *
     * @throws ParameterIsNotDefinedException if given `taskId` is undefined or null
     */
    remove(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve();
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("DELETE", `/lists/${this.todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
            xmlHttpRequest.send();
        });
    }

    /**
     * Sends request to retrieve all tasks of this `TodoList`.
     *
     * @return {Promise} promise to work with. If request was processed successfully
     *                   promise will be resolved and sorted array of `Task`s will be returned,
     *                   otherwise it will be rejected.
     */
    all() {
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
                    resolve(TaskSorter.sortTasksArray(tasks));
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("GET", `/lists/${this.todoListId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
            xmlHttpRequest.send();
        });
    }
}

/**
 * Stores algorithm to sort an array of  `Task`.
 *
 * @author Oleg Barmin
 */
export class TaskSorter {

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