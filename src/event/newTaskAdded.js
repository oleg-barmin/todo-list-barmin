import {Event, EventTypes} from "./event";

/**
 * Occurs when controller adds new task to the model.
 *
 * @extends Event
 */
export class NewTaskAdded extends Event {

    /**
     * Creates `NewTaskAdded` instance.
     */
    constructor() {
        super(EventTypes.NewTaskAdded);
    }
}