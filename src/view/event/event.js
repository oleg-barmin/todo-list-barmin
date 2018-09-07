/**
 * Transport object transfers data between {@link Controller} and TodoComponents.
 *
 * Is used in {@link EventBus}.
 */
export class Event {

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
export class EventBus {

    /**
     * Creates `EventBus` instance.
     *
     * @param transport transport jQuery object to bind `EventBus` on.
     */
    constructor(transport) {
        this._transport = transport;
    }

    /**
     * Triggers all callback bound on `EventType` of given `Event`.
     *
     * @param {Event} event event which will be passed as argument to callbacks
     *                which subscribed to the `EventType` of given event.
     */
    post(event) {
        let typeName = event.eventType.typeName;
        this._transport.trigger(typeName, [event]);
    }

    /**
     * Binds given callback to desired `EventType`.
     *
     * @param {EventType} eventType `EventType` to which callback should be bound
     * @param {Function} callback to bind onto `EventType`
     */
    subscribe(eventType, callback) {
        this._transport.on(eventType.typeName, function (event, occurredEvent) {
            callback(occurredEvent)
        });
    }
}

/**
 * Marks type of {@link Event} to {@link EventBus} bind and call callback of specified `EventType`.
 */
export class EventType {

    /**
     * Creates `EventType` instance.
     *
     * @param {string} typeName string name of the `EventType` instance.
     */
    constructor(typeName) {
        this.typeName = typeName;
    }
}


export const EventTypes = {
    AddTaskRequest: new EventType("AddTaskRequest"),
    NewTaskAdded: new EventType("NewTaskAdded"),
    TaskListUpdated: new EventType("TaskListUpdated"),
    TaskCompletionRequested: new EventType("TaskCompletionRequested"),
    TaskRemovalRequest: new EventType("TaskRemovalRequested"),
    StartTaskEditing: new EventType("TaskEditingStarted"),
    CancelTaskEditing: new EventType("CancelTaskEditing"),
    TaskUpdateRequest: new EventType("TaskUpdateRequested"),
    TaskRemovalFailed: new EventType("TaskRemovalFailed"),
    TaskCompletionFailed: new EventType("TaskCompletionFailed"),
    NewTaskValidationFailed: new EventType("NewTaskValidationFailed"),
    TaskUpdateFailed: new EventType("TaskUpdateFailed"),
    TaskRemovalPerformed: new EventType("TaskRemovalPerformed")
};


