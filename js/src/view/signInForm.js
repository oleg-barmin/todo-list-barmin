import {UiComponent} from "./uiComponent";
import {CredentialsSubmitted} from "../event/credentialsSubmitted";
import {EventTypes} from "../event/event";

/**
 * Sign-in form which meets unauthenticated user on homepage of to-do list application.
 *
 * <p>End-user has to sign-in through this form to get access to
 * all to-do list application functionality.
 *
 * @extends UiComponent
 * @author Oleg Barmin
 */
export class SignInForm extends UiComponent {

    /**
     * Creates `SignInForm` instance.
     *
     * @param {jQuery} element element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
     */
    constructor(element, eventBus) {
        super(element, eventBus);
    }

    /**
     * Renders `SignInForm` into the given element.
     */
    render() {
        this.element.append(`<div class="row justify-content-md-center">
        <div class="col-4 login-form">
            <h2 class="text-center">Sign In</h2>
            <input class="usernameInput form-control" placeholder="Username">
            <input class="passwordInput mt-2 form-control" placeholder="Password" type="password">
            <label class="d-none errorLabel mt-2 form-control alert alert-danger"></label>
            <button class="loginBtn mt-2 form-control btn btn-primary">Sign In</button>
        </div>
    </div>`);

        const loginDiv = this.element.find(".login-form");

        const usernameInput = loginDiv.find(".usernameInput");
        const passwordInput = loginDiv.find(".passwordInput");
        const errorLabel = loginDiv.find(".errorLabel");
        const loginBtn = loginDiv.find(".loginBtn");

        /**
         * Posts `CredentialsSubmitted` with typed in username and password.
         */
        const sendSubmittedCredentials = () => {
            const username = usernameInput.val();
            const password = passwordInput.val();

            if (username.trim().length === 0 || password.trim().length === 0) {
                errorLabel.text("Username/password field cannot be empty.");
                errorLabel.removeClass("d-none");
                return;
            }

            this.eventBus.post(new CredentialsSubmitted(username, password))
        };

        this.eventBus.subscribe(EventTypes.SignInFailed, () => {
            errorLabel.text("Invalid username or password.");
            errorLabel.removeClass("d-none")
        });
        loginBtn.click(sendSubmittedCredentials);
        loginDiv.keydown(keyboardEvent => {
            if (keyboardEvent.key === "Enter") {
                sendSubmittedCredentials();
            }
        });
    }
}