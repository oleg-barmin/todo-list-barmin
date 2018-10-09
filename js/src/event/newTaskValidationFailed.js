import {Event, EventTypes} from "./event";


/**
 * Occurs when validation of description of new task in `NewTaskValidationFailed` was failed.
 *
 * @extends Event
 * @author Oleg Barmin
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