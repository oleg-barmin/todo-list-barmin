import {TodoListId} from "./lib/identifiers";

/**
 * Allow to manage user to-do lists by sending requests to server.
 */
export class UserLists {

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