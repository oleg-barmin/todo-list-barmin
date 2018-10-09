import {Event, EventTypes} from "./event";

/**
 * Occurs when processing of `TaskUpdateRequested` event was performed successfully.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskUpdated extends Event{

    /**
     * Creates `TaskUpdated` instance.
     *
     * @param {TaskId} taskId description of error
     */
    constructor(taskId) {
        super(EventTypes.TaskUpdated);
        this.taskId = taskId;
    }
}