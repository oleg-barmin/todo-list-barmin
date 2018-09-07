(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (factory());
}(this, (function () { 'use strict';

    /**
     * Is an event which happened in {@link TodoListApp} and marks what happened in `TodoListApp`.
     */
    class Event {

        /**
         * Creates `Event` instance.
         *
         * @param {EventType} eventType type of this event
         */
        constructor(eventType) {
            this.eventType = eventType;
        }
    }

    /**
     * Allows to posts {@link Event} and subscribe on `EventType` to process custom callbacks.
     *
     * When `Event` was posted all callbacks for `EventType` of occurred `Event` will be performed.
     * For `EventBus` work properly transport jQuery object should be provided.
     *
     * Example:
     * ```
     * const firstCustomEventType = new EventType("firstCustomEventType");
     * const secondCustomEventType = new EventType("secondCustomEventType");
     * const eventBus = new EventBus(transport);
     * ```
     *
     * When eventBus and custom EventTypes was declared you can bind callbacks for this event types.
     *
     * ```
     * eventBus.subscribe(firstCustomEventType, function(occurredEvent){
     *     console.log("First callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     *
     * eventBus.subscribe(firstCustomEventType, function(occurredEvent){
     *     console.log("Second callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     *
     * eventBus.subscribe(secondCustomEventType, function(occurredEvent){
     *     console.log("Third callback, occurredEvent: " + occurredEvent.eventType.typeName);
     * });
     * ```
     *
     * Now given callbacks will be performed, when `Event` with corresponded `EventType` will be occurred.
     *
     * ```
     * //first and second callback will be performed
     * eventBus.post(new Event(firstCustomEventType));
     * ```
     *
     * Console output after post:
     * <p>
     * `
     * First callback, occurredEvent: firstCustomEventType
     * Second callback, occurredEvent: secondCustomEventType
     * `
     *
     * if event of `secondCustomEventType` will be posted, only third callback will be processed.
     * ```
     * eventBus.post(new Event(secondCustomEventType));
     * ```
     *  Console output after post:
     *  <p>
     * `
     * Third callback, occurredEvent: secondCustomEventType
     * `
     *
     * Implementation of "event bus" design pattern, based on jQuery.
     */
    class EventBus {

        /**
         * Creates `EventBus` instance.
         *
         * @param transport transport jQuery object to bind `EventBus` on.
         */
        constructor(transport) {
            this._transport = transport;
        }

        /**
         * Performs all callback bounded on `EventType` of given `Event`.
         *
         * @param {Event} event event which will be passed as argument to callbacks
         *                which subscribed to the `EventType` of given event.
         */
        post(event) {
            let typeName = event.eventType.typeName;
            this._transport.trigger(typeName, [event]);
        }

        /**
         * Subscribes given callback to desired `EventType`.
         *
         * @param {EventType} eventType `EventType` to which callback should be bound
         * @param {Function} callback to bind onto `EventType`
         *
         * @return {Function} handler handler of `evenType` with given `callback`.
         *          Should be used to unsubscribe if needed.
         */
        subscribe(eventType, callback) {
            const handler = (event, occurredEvent) => callback(occurredEvent);
            this._transport.on(eventType.typeName, handler);
            return handler;
        }

        /**
         * Unsubscribes given `handler` from `eventType`.
         *
         * @param {EventType} eventType type of event to which handler was subscribed
         * @param {Function} handler handler to unsubscribe
         */
        unsubscribe(eventType, handler){
            this._transport.off(eventType.typeName, handler);
        }
    }

    /**
     * Marks type of {@link Event} to {@link EventBus} bind and call callback of specified `EventType`.
     */
    class EventType {

        /**
         * Creates `EventType` instance.
         *
         * @param {string} typeName string name of the `EventType` instance.
         */
        constructor(typeName) {
            this.typeName = typeName;
        }
    }

    QUnit.module("EventBus should");
    QUnit.test("call ", assert => {
        let transportElement = $("#eventBus");
        const eventBus = new EventBus(transportElement);

        const firstEventType = new EventType("firstEventType");
        const secondEventType = new EventType("secondEventType");

        const firstEvent = new Event(firstEventType);
        const secondEvent = new Event(secondEventType);

        firstEvent.interaction = 0;
        secondEvent.interaction = 0;

        const callback = occurredEvent => occurredEvent.interaction += 1;

        eventBus.subscribe(firstEventType, callback);
        eventBus.subscribe(secondEventType, callback);

        eventBus.post(firstEvent);

        assert.strictEqual(firstEvent.interaction, 1, "callback once for single post.");

        eventBus.post(firstEvent);
        eventBus.post(firstEvent);

        assert.strictEqual(firstEvent.interaction, 3, "callback twice for two posts.");
        assert.strictEqual(secondEvent.interaction, 0, "call only subscribed for this event callbacks.");
    });

})));
