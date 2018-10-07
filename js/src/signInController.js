import {SignInCompleted} from "./event/signInCompleted";
import {SignInFailed} from "./event/signInFailed";
import {EventTypes} from "./event/event";

/**
 * Event based facade for `SignInPage`.
 *
 * @author Oleg Barmin
 */
export class SignInController {

    /**
     * Creates `SignInController` instance.
     *
     * @param {EventBus} eventBus to subscribe on page specific events
     * @param {Authentication} authentication to authenticate users
     */
    constructor(eventBus, authentication) {
        this.eventBus = eventBus;
        this.authentication = authentication;

        /**
         * Posts `SignInCompleted` event if user was successfully authenticated,
         * otherwise posts `SignInFailed` event.
         *
         * @param {CredentialsSubmitted} credentialsSubmittedEvent event
         *         which contains username and password typed in by user
         */
        const credentialsSubmittedRequestCallback = credentialsSubmittedEvent => {
            const username = credentialsSubmittedEvent.username;
            const password = credentialsSubmittedEvent.password;

            this.authentication
                .signIn(username, password)
                .then(() => {
                    this.eventBus.post(new SignInCompleted())
                })
                .catch(() => {
                    this.eventBus.post(new SignInFailed())
                });
        };

        eventBus.subscribe(EventTypes.CredentialsSubmitted, credentialsSubmittedRequestCallback)
    }
}