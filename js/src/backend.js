import {Task} from "./model/task";
import {TaskId, TodoListId} from "./lib/identifiers";

/**
 * Service which sends requests to the server with given URL.
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
                    resolve()
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
                    resolve()
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

    /**
     * Sends requests to validate user token.
     *
     * @param token token of user session
     * @return {Promise} promise to process request result
     */
    sendValidateTokenUser(token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();
            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve()
                } else {
                    reject();
                }
            };
            xmlHttpRequest.open(HttpMethods.GET, this.urlBuilder.getAuthUrl());
            xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
            xmlHttpRequest.send();
        });
    }

    /**
     * Sends sign in user request.
     *
     * @param {string} header header name to send request with
     * @param {string} headerValue value of header to send request with
     * @return {Promise} promise to process request result,
     * if it was resolved response body will be returned
     */
    signInUser(header, headerValue) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve(xmlHttpRequest.response)
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open(HttpMethods.POST, this.urlBuilder.getAuthUrl());
            xmlHttpRequest.setRequestHeader(header, headerValue);
            xmlHttpRequest.send();
        });
    }

    /**
     * Sends requests to sign out user
     *
     * @param token token of user session
     * @return {Promise} promise to process request result
     */
    signOutUser(token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve();
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open(HttpMethods.DELETE, this.urlBuilder.getAuthUrl());
            xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
            xmlHttpRequest.send();
        });
    }

    /**
     * Send read user to-do lists request.
     *
     * @param token token of user session
     * @return {Promise} promise to process request result, if it was resolved response body will be returned.
     */
    readUserLists(token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve(xmlHttpRequest.response);
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open(HttpMethods.GET, this.urlBuilder.getListsUrl());
            xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
            xmlHttpRequest.send();
        });
    }

    /**
     * Sends create to-do list request.
     *
     * @param {TodoListId} todoListId ID of to-do list to create
     * @param token token of user session
     * @return {Promise} promise to process request result
     */
    createList(todoListId, token) {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve();
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open(HttpMethods.POST, this.urlBuilder.buildTodoListUrl(todoListId));
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

    /**
     * Creates `UrlBuilder` instance.
     *
     * @param {string} url base URL of server
     */
    constructor(url) {
        this.url = url;
    }

    /**
     * Builds URL to access tasks.
     *
     * @param {TodoListId} todoListId ID of to-do list to which belongs desired task
     * @param {TaskId} taskId ID of desired task
     * @return {string} URL to desired task
     */
    buildTaskUrl(todoListId, taskId) {
        return `${this.getListsUrl()}/${todoListId.id}/${taskId.id}`
    }

    /**
     * Builds URL to access to-do lists with given ID.
     *
     * @param {TodoListId} todoListId ID of desired to-do list
     * @return {string} URL to desired to-do list
     */
    buildTodoListUrl(todoListId) {
        return `${this.getListsUrl()}/${todoListId.id}`
    }

    /**
     * Provides URL to authentication service.
     *
     * @return {string} URL to authentication service
     */
    getAuthUrl() {
        return `${this.url}/auth`;
    }

    /**
     * Provides URL to lists.
     *
     * @return {string} URL to lists
     */
    getListsUrl() {
        return `${this.url}/lists`;
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