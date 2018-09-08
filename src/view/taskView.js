import {TodoComponent} from "./todoComponent";
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
 * @extends TodoComponent
 */
export class TaskView extends TodoComponent {

    /**
     * Creates `TaskView` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus eventBust to subscribe and post events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     */
    constructor(element, eventBus, number, task) {
        super(element, eventBus);

        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
        this.currentState = new TaskDisplay(null, this.eventBus, null, null);

        const startTaskEditingHandler = this.eventBus.subscribe(EventTypes.TaskEditingStarted, occurredEvent => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.empty();
                this.currentState = new TaskEdit(this.element, this.eventBus, this.number, this.task);
                this.currentState.render();
            }
        });

        const cancelTaskEditingHandler = this.eventBus.subscribe(EventTypes.CancelTaskEditing, occurredEvent => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.empty();
                this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                this.currentState.render();
            }
        });

        const taskUpdatePerformedHandler = this.eventBus.subscribe(EventTypes.TaskUpdatePerformed, occurredEvent => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.empty();
                this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task);
                this.currentState.render();
            }
        });

        const taskRemovalPerformedHandler = this.eventBus.subscribe(EventTypes.TaskRemovalPerformed, (occurredEvent) => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.remove();
                this.eventBus.unsubscribe(EventTypes.TaskEditingStarted, startTaskEditingHandler);
                this.eventBus.unsubscribe(EventTypes.CancelTaskEditing, cancelTaskEditingHandler);
                this.eventBus.unsubscribe(EventTypes.TaskRemovalPerformed, taskRemovalPerformedHandler);
                this.eventBus.unsubscribe(EventTypes.TaskUpdatePerformed, taskUpdatePerformedHandler);
            }
        });
    }

    /**
     * Renders given task in `TaskDisplay` state onto given element.
     * if method was called
     *
     * @param {jQuery} [element] jQuery element to render into
     * @param {number} [number] number of the task in list
     * @param {Task} [task] task to render.
     */
    render(element = undefined, number = undefined, task = undefined) {
        this.element = element || this.element;
        this.number = number || this.number;
        this.task = task || this.task;

        this.currentState.element = this.element;
        this.currentState.number = this.number;
        this.currentState.task = this.task;

        this.currentState.render();
    }
}