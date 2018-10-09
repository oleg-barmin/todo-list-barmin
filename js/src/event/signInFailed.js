import {Event, EventTypes} from "./event";

/**
 * Occurs when user tried to sign-in with invalid username or password.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class SignInFailed extends Event {

    /**
     * Creates `SignInFailed` instance.
     */
    constructor() {
        super(EventTypes.SignInFailed);
    }
}