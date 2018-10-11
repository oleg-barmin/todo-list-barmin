import {TodoListId} from "./lib/identifiers";

/**
 * Allow to manage user to-do lists by sending requests to server.
 *
 * @author Oleg Barmin
 */
export class UserLists {

    /**
     * Creates `UserLists` instance.
     *
     * @param {Backend} backend to send request
     * @param token token of user
     */
    constructor(backend, token) {
        this.backend = backend;
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
            this.backend.readUserLists(this.token)
                .then((body) => {
                    const todoLists = JSON.parse(body);
                    const todoListIds = todoLists.map((el) => new TodoListId(el.id.value));
                    resolve(todoListIds)
                })
                .catch(reject)
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
            this.backend.createList(todoListId, this.token)
                .then(resolve)
                .catch(reject)
        });
    }
}