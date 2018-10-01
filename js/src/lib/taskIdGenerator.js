import {TaskId} from "./identifiers";

/**
 * Generates unique TaskId for `Task`.
 *
 * Current implementation based on uuid v4.
 */
export class TaskIdGenerator {

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