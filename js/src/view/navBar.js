import {UiComponent} from "./uiComponent";

export class NavBar extends UiComponent {

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

    }
}