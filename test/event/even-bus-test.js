import {Event, EventBus, EventType} from "../../src/event/event";

QUnit.module("EventBus should");
QUnit.test("", assert => {
    const transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);

    const firstEventType = new EventType("firstEventType");
    const secondEventType = new EventType("secondEventType");

    const firstEvent = new Event(firstEventType);
    const secondEvent = new Event(secondEventType);

    firstEvent.interaction = 0;
    secondEvent.interaction = 0;

    const callback = occurredEvent => occurredEvent.interaction++;

    eventBus.post(firstEvent);

    assert.ok(firstEvent.interaction === 0
        && secondEvent.interaction === 0,
        "post events even if no one is subscribed on their even types."
    );

    const firstEventTypeHandler = eventBus.subscribe(firstEventType, callback);
    eventBus.subscribe(secondEventType, callback);

    eventBus.post(firstEvent);

    assert.strictEqual(firstEvent.interaction, 1, "call callback once for single post.");

    eventBus.post(firstEvent);
    eventBus.post(firstEvent);

    assert.strictEqual(firstEvent.interaction, 3, "call callback twice for two posts.");
    assert.strictEqual(secondEvent.interaction, 0, "call only subscribed for this event callbacks.");

    eventBus.unsubscribe(firstEventType,firstEventTypeHandler);
    eventBus.post(firstEvent);

    assert.strictEqual(firstEvent.interaction, 3, "don't call callback for event which was unsubscribed.");

});

QUnit.test("throw ", assert => {
    assert.throws(
        () => {
            new EventBus(undefined);
        },
        Error,
        "Error if given transport is not defined."
    );

    assert.throws(
        () => {
            new EventBus(new Event());
        },
        TypeError,
        "TypeError if given transport is not a jQuery object."
    );

    const transportElement = $("#eventBus");
    const eventBus = new EventBus(transportElement);

    assert.throws(
        () => {
            eventBus.post("");
        },
        TypeError,
        "TypeError if given post argument is not instance of Event."
    );

    assert.throws(
        () => {
            eventBus.subscribe("", "");
        },
        TypeError,
        "TypeError if given subscribe eventType argument is no instance of EventType."
    );

    const eventType = new EventType("eventType");

    assert.throws(
        () => {
            eventBus.subscribe(eventType, "");
        },
        TypeError,
        "TypeError if given subscribe callback argument is no a Function."
    );
});