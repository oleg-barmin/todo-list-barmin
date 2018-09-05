import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when `TaskCompletionRequest` cannot be processed properly.
 */
export class TaskCompletionFailed extends Event {

    /**
     * Creates `TaskRemovalFailed` instance.
     *
     * @param {string} errorMsg description of error
     */
    constructor(errorMsg) {
        super(EventTypeEnumeration.TaskCompletionFailed);
        this.errorMsg = errorMsg;
    }
}