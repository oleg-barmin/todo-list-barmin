import {EventBus} from "../src/view/event/event";
import {EventType} from "../src/view/event/event";
import {Event} from "../src/view/event/event";

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