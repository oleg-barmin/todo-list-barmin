import {Event, EventTypes} from "./event";

/**
 * Occurs when `TaskRemovalRequest` cannot be processed properly.
 *
 * @extends Event
 */
export class TaskRemovalFailed extends Event {

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