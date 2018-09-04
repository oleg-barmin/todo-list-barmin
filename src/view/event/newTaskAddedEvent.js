import {Event, EventTypeEnum} from "./event";

export class NewTaskAddedEvent extends Event{
    constructor(taskArray){
        super(EventTypeEnum.NewTaskAddedEvent);
        this.data = taskArray;
    }
}