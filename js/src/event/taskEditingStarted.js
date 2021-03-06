import {Event, EventTypes} from "./event";

/**
 * Occurs when end-user tries to edit a task.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskEditingStarted extends Event {

    /**
     * Creates `TaskEditingStarted` instance.
     *
     * @param {TaskId} taskId ID of a task which editing was requested.
     */
    constructor(taskId) {
        super(EventTypes.TaskEditingStarted);
        this.taskId = taskId;
    }
}