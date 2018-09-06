import {TodoList} from "../model/todo-list";
import {EventTypeEnumeration} from "./event/event";
import {NewTaskAdded} from "./event/newTaskAdded";
import {NewTaskValidationFailed} from "./event/newTaskValidationFailed";
import {TaskCompletionFailed} from "./event/taskCompletionFailed";
import {TaskRemovalFailed} from "./event/taskRemovalFailed";
import {TaskListUpdated} from "./event/taskListUpdated";

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
        eventBus.subscribe(EventTypeEnumeration.AddTaskRequest, function (occurredEvent) {
            try {
                self.todoList.add(occurredEvent.taskDescription);
                self.eventBus.post(new NewTaskAdded());
                self.eventBus.post(new TaskListUpdated(self.todoList.all()));
            } catch (e) {
                self.eventBus.post(new NewTaskValidationFailed(e.message));
            }
        });

        eventBus.subscribe(EventTypeEnumeration.TaskRemovalRequest, function (occurredEvent) {
            try {
                self.todoList.remove(occurredEvent.taskId);
                self.eventBus.post(new TaskListUpdated(self.todoList.all()));
            } catch (e) {
                self.eventBus.post(new TaskRemovalFailed("Task removal fail."))
            }
        });

        eventBus.subscribe(EventTypeEnumeration.TaskCompletionRequested, function (occurredEvent) {
            try {
                self.todoList.complete(occurredEvent.taskId);
                self.eventBus.post(new TaskListUpdated(self.todoList.all()));
            } catch (e) {
                self.eventBus.post(new TaskCompletionFailed("Task completion fail."))
            }
        });

        eventBus.subscribe(EventTypeEnumeration.TaskUpdateRequest, function (occurredEvent) {
            try {
                self.todoList.update(occurredEvent.taskId, occurredEvent.newTaskDescription);
                self.eventBus.post(new TaskListUpdated(self.todoList.all()));
            } catch (e) {
                self.eventBus.post(new TaskCompletionFailed("Task updated fail."))
            }
        });
    }

}