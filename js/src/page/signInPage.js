import {SignInController} from "../signInController";
import {SignInForm} from "../view/signInForm";
import {Page} from "./page";

/**
 * Page which allows to end-user to authenticate into to-do list application
 * and get access to applications functionality.
 *
 * @author Oleg Barmin
 */
export class SignInPage extends Page {

    /**
     * Renders `SingInPage` into the given element.
     */
    render() {
        this.element.empty();
        this.element.append(`<div class='container'></div>`);

        const container = this.element.find(".container");

        SignInController.initializeController(this.eventBus, this.authentication);
        this.signInForm = new SignInForm(container, this.eventBus);

        this.signInForm.render()
    }
}