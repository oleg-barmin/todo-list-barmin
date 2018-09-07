import {Event, EventTypes} from "./event";

/**
 * Occurs when controller updated list of tasks.
 *
 * @extends Event
 */
export class TaskListUpdated extends Event {

    /**
     * Creates `TaskListUpdated` instance.
     *
     * @param {Array} taskArray sorted array of task from model.
     */
    constructor(taskArray) {
        super(EventTypes.TaskListUpdated);
        this.taskArray = taskArray;
    }
}