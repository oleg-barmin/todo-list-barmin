import {Event, EventTypes} from "./event";

/**
 * Event which occurred when new task was added on view.
 * Transfers description of new task.
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