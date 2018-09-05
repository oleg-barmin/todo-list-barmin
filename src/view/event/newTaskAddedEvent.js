import {Event, EventTypeEnumeration} from "./event";

/**
 * Event which occurred when new task was added to model.
 * Transfers array of Tasks;
 *
 * @extends Event
 */
export class NewTaskAddedEvent extends Event{

    /**
     * Creates `NewTaskAddedEvent` instance.
     *
     * @param {Array} taskArray sorted array of task from model.
     */
    constructor(taskArray){
        super(EventTypeEnumeration.NewTaskAddedEvent);
        this.taskArray = taskArray;
    }
}