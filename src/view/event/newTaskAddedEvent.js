import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when controller adds new task to the model.
 *
 * @extends Event
 */
export class NewTaskAddedEvent extends Event {

    /**
     * Creates `NewTaskAddedEvent` instance.
     */
    constructor() {
        super(EventTypeEnumeration.NewTaskAddedEvent);
    }
}