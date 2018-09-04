export class Event {
    constructor(eventType, data) {
        this.eventType = eventType;
        this.data = data;
    }
}

export class EventBus {

    constructor(transport) {
        this._transport = transport;
    }

    post(event) {
        let typeName = event.eventType.typeName;
        console.log(typeName + " was posted");
        this._transport.trigger(typeName, [event]);
    }

    subscribe(eventType, callback) {
        this._transport.on(eventType.typeName, function (event, data) {
            callback(data)
        });
    }
}

class EventType {
    constructor(typeName) {
        this.typeName = typeName;
    }
}


export const EventTypeEnum = {
    AddTaskRequest: new EventType("AddTaskRequest"),
    NewTaskAddedEvent: new EventType("NewTaskAddedEvent")
};


