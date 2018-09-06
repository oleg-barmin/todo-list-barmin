import {Event, EventTypeEnumeration} from "./event";

/**
 * Event which occurred when new task was added on view.
 * Transfers description of new task.
 */
export class AddTaskRequest extends Event{

    /**
     * Creates `AddTaskRequest` instance.
     *
     * @param {string} taskDescription description of new task
     */
    constructor(taskDescription){
        super(EventTypeEnumeration.AddTaskRequest);
        this.taskDescription = taskDescription;
    }
}