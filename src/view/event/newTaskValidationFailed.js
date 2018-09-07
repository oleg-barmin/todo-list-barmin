import {Event, EventTypes} from "./event";


/**
 * Event which occurred when new task description validation failed.
 *
 * @extends Event
 */
export class NewTaskValidationFailed extends Event{

    /**
     * Creates `NewTaskValidationFailed` instance.
     *
     * @param {string} errorMsg description of error
     */
    constructor(errorMsg){
        super(EventTypes.NewTaskValidationFailed);
        this.errorMsg = errorMsg;
    }
}