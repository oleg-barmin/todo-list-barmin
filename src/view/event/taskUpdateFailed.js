import {Event, EventTypeEnumeration} from "./event";

class TaskUpdateFailed extends Event{
    constructor(errorMsg){
        super(EventTypeEnumeration.TaskUpdateFailed)
    }
}