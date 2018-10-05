import {Event, EventTypes} from "./event";

/**
 * Occurs when user was successfully signed in into to-do list application.
 *
 * @author Oleg Barmin
 */
export class SignInCompleted extends Event {

    /**
     * Creates `SignInCompleted` instance.
     */
    constructor() {
        super(EventTypes.SignInCompleted)
    }
}