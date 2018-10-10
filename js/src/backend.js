import {Task} from "./model/task";
import {TaskId} from "./lib/identifiers";
import {TaskSorter} from "./model/todo-list";

/**
 * Service which sends requests to the server with given host.
 *
 * @author Oleg Barmin
 */
export class Backend {

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
        this.url = `${url}/`;
    }

    /**
     * Sends add task request.
     *
     * @param {TodoListId} todoListId ID of to-do list of task
     * @param {TaskId} taskId ID of task to add
     * @param payload payload of request
     * @param token token of user session.
     * @return {Promise} promise to work process request result.
     */
    addTask(todoListId, taskId, payload, token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve()
                } else {
                    reject();
                }
            };
            xmlHttpRequest.open("POST", `${this.url}/lists/${todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", token);
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
     * @return {Promise} promise to work process request result.
     */
    updateTask(todoListId, taskId, payload, token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve()
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("PUT", `/lists/${todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", token);
            xmlHttpRequest.send(JSON.stringify(payload));
        });
    }

    /**
     * Sends remove task request.
     *
     * @param {TodoListId} todoListId ID of to-do list of task
     * @param {TaskId} taskId ID of task to remove
     * @param token token of user session
     * @return {Promise} promise to work process request result.
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

            xmlHttpRequest.open("DELETE", `/lists/${todoListId.id}/${taskId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", token);
            xmlHttpRequest.send();
        });
    }

    /**
     * Sends read all tasks of to-do list request.
     *
     * @param {TodoListId} todoListId ID of to-do list to read tasks from
     * @param token token of user session
     * @return {Promise} promise to work process request result,
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
                    resolve(TaskSorter.sortTasksArray(tasks));
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("GET", `/lists/${todoListId.id}`);
            xmlHttpRequest.setRequestHeader("X-Todo-Token", token);
            xmlHttpRequest.send();
        });
    }
}