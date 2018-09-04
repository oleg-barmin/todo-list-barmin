import {Event, EventTypeEnum} from "./event";

export class AddTaskRequestEvent extends Event{
    constructor(taskDescription){
        super(EventTypeEnum.AddTaskRequest, taskDescription);
    }
}