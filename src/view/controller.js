import {TodoList} from "../model/todo-list";
import {EventTypeEnum} from "./event/event";
import {NewTaskAddedEvent} from "./event/newTaskAddedEvent";

export class Controller {
    constructor(eventBus) {
        this.todoList = new TodoList();
        this.eventBus = eventBus;

        const self = this;
        eventBus.subscribe(EventTypeEnum.AddTaskRequest, function (event) {
            self.todoList.add(event.data);
            self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()))
        });
    }

}