import {UiComponent} from "../view/uiComponent";

/**
 * Renders necessary information to user according to current state of to-do list application.
 *
 * Only one page per application should be rendered.
 *
 * @author Oleg Barmin
 */
export class Page extends UiComponent {

    /**
     * Creates `Page` instance.
     *
     * @param {jQuery} element element to render page into
     * @param {EventBus} eventBus to subscribe and publish page specific events
     * @param {Authentication} authentication to authenticate users.
     */
    constructor(element, eventBus, authentication) {
        super(element, eventBus);
        this.authentication = authentication;
    }
}