import {Event, EventTypes} from "./event";

/**
 * Occurred when new task was added on view.
 *
 * @extends Event
 */
export class TaskAddRequest extends Event {

    /**
     * Creates `TaskAddRequest` instance.
     *
     * @param {string} taskDescription description of new task
     */
    constructor(taskDescription){
        super(EventTypes.TaskAddRequest);
        this.taskDescription = taskDescription;
    }
}