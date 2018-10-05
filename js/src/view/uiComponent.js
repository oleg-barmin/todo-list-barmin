/**
 * Declares basic class for all sub-classes.
 * Each `UiComponent` sub-class should be connect with {@link EventBus},
 * and contain a element to render into.
 * Render method should be implemented to render the component into the `element`.
 *
 * @abstract
 */
export class UiComponent {

    /**
     * Saves given element to render into and `EventBus` to connect with controller.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
     */
    constructor(element, eventBus){
        this.element = element;
        this.eventBus = eventBus;
    }

    /**
     * Renders component into given element.
     */
    render(){}
}