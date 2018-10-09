import {Event, EventTypes} from "./event";

/**
 * Occurs when `TaskRemovalRequested` cannot be processed was failed.
 *
 * @extends Event
 * @author Oleg Barmin
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