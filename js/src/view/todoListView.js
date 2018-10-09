import {UiComponent} from "./uiComponent";
import {AddTaskForm} from "./addTaskForm";
import {TodoWidget} from "./todoWidget";

/**
 * Component which responsible for rendering of `TodoList`.
 *
 * @extends UiComponent
 * @author Oleg Barmin
 */
export class TodoListView extends UiComponent {

    /**
     * Creates `TodoListView` instance.
     *
     * @param {jQuery} element element to render into
     * @param {EventBus} eventBus to subscribe and post component specific events.
     * @param {TodoListId} todoListId ID of to-do list to render
     */
    constructor(element, eventBus, todoListId) {
        super(element, eventBus);
        this.todoListId = todoListId;
    }

    /**
     * Renders `TodoListView` component into given element.
     */
    render() {
        this.element.empty();

        this.element.append(`<div class="row justify-content-md-center">
                                <div class="col-md-auto">
                                    <h1>To-Do</h1>
                                </div>
                                </div>
                            <div class="addTaskForm row justify-content-md-center"></div>
                            <div class="todoWidget"></div>`);


        let addTaskForm = new AddTaskForm(this.element.find(".addTaskForm"), this.eventBus, this.todoListId);
        let taskView = new TodoWidget(this.element.find(".todoWidget"), this.eventBus, this.todoListId);

        addTaskForm.render();
        taskView.render();
    }
}