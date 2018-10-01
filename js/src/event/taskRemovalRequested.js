import {Event, EventTypes} from "./event";

/**
 * Occurs when task with specified ID need to be removed.
 *
 * @extends Event
 */
export class TaskRemovalRequested extends Event {

    /**
     * Creates `TaskRemovalRequested` instance.
     *
     * @param {TaskId} taskId ID of task to remove.
     */
    constructor(taskId) {
        super(EventTypes.TaskRemovalRequested);
        this.taskId = taskId;
    }
}