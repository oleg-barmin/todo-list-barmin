import {Event, EventTypes} from "./event";

/**
 * Occurs when end user tries to edit a task.
 *
 * @extends Event
 */
export class StartTaskEditing extends Event {

    /**
     * Creates `StartTaskEditing` instance.
     *
     * @param {TaskId} taskId ID of a task which editing was requested.
     */
    constructor(taskId) {
        super(EventTypes.StartTaskEditing);
        this.taskId = taskId;
    }
}