import {UiComponent} from "./uiComponent";
import {AddTaskForm} from "./addTaskForm";
import {TodoWidget} from "./todoWidget";

/**
 * Component which responsible for rendering of `TodoList`.
 *
 * @author Oleg Barmin
 */
export class TodoListView extends UiComponent {

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


        let addTaskForm = new AddTaskForm(this.element.find(".addTaskForm"), this.eventBus);
        let taskView = new TodoWidget(this.element.find(".todoWidget"), this.eventBus);

        addTaskForm.render();
        taskView.render();
    }
}