import {Event, EventTypes} from "./event";

/**
 * Occurs when `TaskUpdateRequest` cannot be processed.
 *
 * @extends Event
 */
export class TaskUpdateFailed extends Event {

    /**
     * Creates `TaskUpdateFailed` instance.
     *
     * @param {TaskId} taskId ID of task which updating was failed
     * @param {string} errorMsg error message to display on view
     */
    constructor(taskId, errorMsg) {
        super(EventTypes.TaskUpdateFailed);
        this.errorMsg = errorMsg;
        this.taskId = taskId;
    }
}