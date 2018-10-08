import {Event, EventTypes} from "./event";

/**
 * Occurs when user was successfully signed out from to-do list application.
 *
 * @author Oleg Barmin
 */
export class SignOutCompleted extends Event {

    /**
     * Creates `SignOutCompleted` instance.
     */
    constructor() {
        super(EventTypes.SignOutCompleted)
    }
}