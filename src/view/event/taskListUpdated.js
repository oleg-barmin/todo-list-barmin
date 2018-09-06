import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when controller updated list of tasks.
 */
export class TaskListUpdated extends Event {

    /**
     * Creates `TaskListUpdated` instance.
     *
     * @param {Array} taskArray sorted array of task from model.
     */
    constructor(taskArray) {
        super(EventTypeEnumeration.TaskListUpdated);
        this.taskArray = taskArray;
    }
}