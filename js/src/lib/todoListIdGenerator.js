import {TodoListId} from "./identifiers";

/**
 * Generates unique `TodoListId` for `TodoList`.
 *
 * Current implementation based on uuid v4.
 *
 * @author Oleg Barmin
 */
export class TodoListIdGenerator {

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