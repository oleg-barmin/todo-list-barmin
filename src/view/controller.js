import {TodoList} from "../model/todo-list";
import {EventTypeEnumeration} from "./event/event";
import {NewTaskAddedEvent} from "./event/newTaskAddedEvent";

/**
 * Connects model of To-do list - {@link TodoList} and To-do Components.
 * Reacts on {@link Event} which occurred on view layer.
 */
export class Controller {

    /**
     * Creates `Controller` instance and subscribes on necessary event types.
     *
     * @param {EventBus} eventBus evenBus to work with
     */
    constructor(eventBus) {
        this.todoList = new TodoList();
        this.eventBus = eventBus;

        const self = this;
        eventBus.subscribe(EventTypeEnumeration.AddTaskRequest, function (event) {
            self.todoList.add(event.taskDescription);
            self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()))
        });
    }

}