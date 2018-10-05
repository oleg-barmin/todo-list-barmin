import {Event, EventTypes} from "./event";

/**
 * Occurs when end-user tries to sing-in into to-do list application.
 *
 * @author Oleg Barmin
 */
export class CredentialsSubmitted extends Event {

    /**
     * Creates `CredentialsSubmitted` instance.
     *
     * @param {String} username username of user
     * @param {String} password password of user
     */
    constructor(username, password) {
        super(EventTypes.CredentialsAccepted);
        this.username = username;
        this.password = password;
    }
}