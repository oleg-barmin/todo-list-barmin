import {Event, EventTypeEnumeration} from "./event";


/**
 * Event which occurred when new task description validation failed.
 */
export class NewTaskValidationFailed extends Event{

    /**
     * Creates `NewTaskValidationFailed` instance.
     *
     * @param {string} errorMsg description of error
     */
    constructor(errorMsg){
        super(EventTypeEnumeration.NewTaskValidationFailed);
        this.errorMsg = errorMsg;
    }
}