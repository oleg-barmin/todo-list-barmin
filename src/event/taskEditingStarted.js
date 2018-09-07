import {Event, EventTypes} from "./event";

/**
 * Occurs when end user tries to edit a task.
 *
 * @extends Event
 */
export class TaskEditingStarted extends Event {

    /**
     * Creates `TaskEditingStarted` instance.
     *
     * @param {TaskId} taskId ID of a task which editing was requested.
     */
    constructor(taskId) {
        super(EventTypes.StartTaskEditing);
        this.taskId = taskId;
    }
}