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
 *  In `TaskDisplay` state end user is able to:
 *  - mark task as completed
 *  - remove task
 *  - switch to `TaskEdit` state
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
     * @param element Jquery element to render into
     * @param {EventBus} eventBus eventBust to subscribe and post events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     */
    constructor(element, eventBus, number, task) {
        super(element, eventBus);
        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
        this.currentState = null;
    }

    /**
     * Renders given task in `TaskDisplay` state.
     */
    render() {
        this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task).render();

        const startTaskEditingHandler = this.eventBus.subscribe(EventTypes.StartTaskEditing, occurredEvent => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.empty();
                this.currentState = new TaskEdit(this.element, this.eventBus, this.number, this.task).render();
            }
        });

        const cancelTaskEditingHandler = this.eventBus.subscribe(EventTypes.CancelTaskEditing, occurredEvent => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.empty();
                this.currentState = new TaskDisplay(this.element, this.eventBus, this.number, this.task).render();
            }
        });

        const taskRemovalPerformedHandler = this.eventBus.subscribe(EventTypes.TaskRemovalPerformed, (occurredEvent) => {
            if (occurredEvent.taskId === this.task.id) {
                this.element.remove();
                this.eventBus.unsubscribe(EventTypes.StartTaskEditing, startTaskEditingHandler);
                this.eventBus.unsubscribe(EventTypes.CancelTaskEditing, cancelTaskEditingHandler);
                this.eventBus.unsubscribe(EventTypes.TaskRemovalPerformed, taskRemovalPerformedHandler);
            }
        });
    }
}