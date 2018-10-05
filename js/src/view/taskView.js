import {UiComponent} from "./uiComponent";
import {TaskDisplay} from "./taskDisplay";
import {EventTypes} from "../event/event";
import {TaskEdit} from "./taskEdit";

/**
 * Component which responsible for displaying of task.
 *
 * Has two states:
 *  - {@link TaskDisplay}
 *  - {@link TaskEdit}
 *
 *
 *  In `TaskDisplay` state end user is able to:
 *  - mark task as completed
 *  - remove task
 *  - switch to `TaskEdit` state
 *
 *
 *  In `TaskEdit` state end user is able to:
 *  - edit task description
 *  - save new task description
 *  - cancel editing (switch to `TaskDisplay` state)
 *
 * @extends UiComponent
 */
export class TaskView extends UiComponent {

    /**
     * Creates `TaskView` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     */
    constructor(element, eventBus, number, task) {
        super(element, eventBus);

        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
        this.currentState = new TaskDisplay(null, this.eventBus, null, null);

        const startTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingStarted,
            occurredEvent => {
                if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                    this.element.empty();
                    this.currentState = new TaskEdit(this.element, this.eventBus, this.number, this.task);
                    this.currentState.render();
                }
            });

        const cancelTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingCanceled,
            occurredEvent => {
                if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                    this.element.empty();
                    this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                    this.currentState.render();
                }
            });

        const taskUpdatePerformedHandler = this.eventBus.subscribe(EventTypes.TaskUpdated,
            occurredEvent => {
                if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                    this.element.empty();
                    this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                    this.currentState.render();
                }
            });

        const taskRemovalPerformedHandler = this.eventBus.subscribe(EventTypes.TaskRemoved,
            (occurredEvent) => {
                if (occurredEvent.taskId.compareTo(this.task.id) === 0) {
                    this.element.remove();
                    this.eventBus.unsubscribe(EventTypes.TaskEditingStarted, startTaskEditingHandler);
                    this.eventBus.unsubscribe(EventTypes.TaskEditingCanceled, cancelTaskEditingHandler);
                    this.eventBus.unsubscribe(EventTypes.TaskRemoved, taskRemovalPerformedHandler);
                    this.eventBus.unsubscribe(EventTypes.TaskUpdated, taskUpdatePerformedHandler);
                }
            });
    }

    /**
     * todo jsDoc
     */
    isEditing() {
        return this.currentState instanceof TaskEdit;
    }

    /**
     * Removes previous element, stores given element to render into, task and number of the task in the TodoList.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {number} number number of the task in list
     * @param {Task} task task to render.
     */
    update(element, number, task) {
        this.element.remove();
        this.element = element;
        this.number = number;
        this.task = task;
    }

    /**
     * Renders task in current state of TaskView onto given element.
     */
    render() {
        this.currentState.element = this.element;
        this.currentState.number = this.number;
        this.currentState.task = this.task;

        this.currentState.render();
    }
}