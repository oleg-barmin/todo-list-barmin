import {Event, EventTypes} from "./event";

/**
 * Occurs when processing of `TaskUpdateRequested` event was performed successfully.
 */
export class TaskUpdatePerformed extends Event{
    /**
     * Creates `TaskUpdatePerformed` instance.
     *
     * @param {TaskId} taskId description of error
     */
    constructor(taskId) {
        super(EventTypes.TaskUpdatePerformed);
        this.taskId = taskId;
    }
}