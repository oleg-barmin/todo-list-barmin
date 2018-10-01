import {Event, EventTypes} from "./event";

/**
 * Occurs when `TaskCompletionRequest` cannot be processed was failed.
 *
 * @extends Event
 */
export class TaskCompletionFailed extends Event {

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