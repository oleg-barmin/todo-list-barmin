import {TodoList} from "../model/todo-list";
import {EventTypeEnumeration} from "./event/event";
import {NewTaskAddedEvent} from "./event/newTaskAddedEvent";
import {NewTaskValidationFailedEvent} from "./event/newTaskValidationFailedEvent";

/**
 * Connects model of {@link TodoList} and {@link TodoComponent}.
 * Reacts on {@link Event} which occurred on view layer.
 */
export class Controller {

    /**
     * Creates `Controller` instance.
     *
     * During constuction of instance it creates new {@link TodoList} instance,
     * where it will contains all tasks and EventBus to process occurred events.
     *
     * @param {EventBus} eventBus evenBus to work with
     */
    constructor(eventBus) {
        this.todoList = new TodoList();
        this.eventBus = eventBus;

        const self = this;
        eventBus.subscribe(EventTypeEnumeration.AddTaskRequest, function (event) {
            try {
                self.todoList.add(event.taskDescription);
                self.eventBus.post(new NewTaskAddedEvent(self.todoList.all()));
            } catch (e) {
                self.eventBus.post(new NewTaskValidationFailedEvent(e.message));
            }
        });
    }

}