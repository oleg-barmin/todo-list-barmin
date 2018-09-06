import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when `TaskRemovalRequest` cannot be processed properly.
 */
export class TaskRemovalFailed extends Event {

    /**
     * Creates `TaskRemovalFailed` instance.
     *
     * @param {string} errorMsg description of error
     */
    constructor(errorMsg) {
        super(EventTypeEnumeration.TaskRemovalFailed);
        this.errorMsg = errorMsg;
    }
}