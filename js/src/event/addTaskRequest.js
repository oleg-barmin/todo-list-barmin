import {Event, EventTypes} from "./event";

/**
 * Occurred when new task was added on view.
 *
 * @extends Event
 */
export class AddTaskRequest extends Event{

    /**
     * Creates `AddTaskRequest` instance.
     *
     * @param {string} taskDescription description of new task
     */
    constructor(taskDescription){
        super(EventTypes.AddTaskRequest);
        this.taskDescription = taskDescription;
    }
}