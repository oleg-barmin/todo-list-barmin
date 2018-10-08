import {UiComponent} from "./uiComponent";
import {SignOutCompleted} from "../event/signOutCompleted";

/**
 * Navigation bar which allows users to perform operation, which available across all pages.
 *
 * @author Oleg Barmin
 */
export class NavBar extends UiComponent {

    /**
     * Creates `NavBar` instance.
     *
     * @param {jQuery} element element to render into
     * @param {EventBus} evenBus to subscribe and post component specific events
     * @param {Authentication} authentication to authenticate user
     */
    constructor(element, evenBus, authentication) {
        super(element, evenBus);
        this.authentication = authentication;
    }

    render() {
        this.element.empty();
        const signOutBtnClass = "signOutBtn";
        this.element.append(`<nav class="navbar navbar-light bg-light justify-content-between">
  <a class="navbar-brand"></a>
  <button class="${signOutBtnClass} btn btn-primary my-2 my-sm-0" type="submit">Sign Out</button>
</nav>`);

        let signOutBtn = this.element.find(`.${signOutBtnClass}`);

        signOutBtn.click(() => {
            this.authentication.signOut()
                .then(() => {
                    this.eventBus.post(new SignOutCompleted())
                })
        })

    }
}