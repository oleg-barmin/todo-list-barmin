import {Backend} from "../../src/backend";

/**
 * Allows to validate which methods of `Backend` is called inside `TodoList` and what parameters was given.
 *
 * @author Oleg Barmin
 */
export class MockBackend extends Backend {

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