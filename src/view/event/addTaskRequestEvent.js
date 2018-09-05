import {Event, EventTypeEnumeration} from "./event";

/**
 * Event which occurred when new task was added on view.
 * Transfers description of new task.
 */
export class AddTaskRequestEvent extends Event{

    /**
     * Creates `AddTaskRequestEvent` instance.
     *
     * @param {string} taskDescription description of new task
     */
    constructor(taskDescription){
        super(EventTypeEnumeration.AddTaskRequest);
        this.taskDescription = taskDescription;
    }
}