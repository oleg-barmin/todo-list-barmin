import {Event, EventTypes} from "./event";


/**
 * Occurs when validation of description of new task in `AddTaskRequest` was failed.
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