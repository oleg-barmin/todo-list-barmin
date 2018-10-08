import {DashboardController} from "../dashboardController";
import {AddTaskForm} from "../view/addTaskForm";
import {TodoWidget} from "../view/todoWidget";
import {Page} from "./page";

/**
 * Provides user access to core functionality of to-do list application.
 *
 * @author Oleg Barmin
 */
export class DashboardPage extends Page {

    /**
     * Renders `DashboardPage` into given element.
     */
    render() {
        this.element.empty();

        this.element.append(`<div class='container'></div>`);
        const container = $(this.element.find(".container")[0]);

        this.dashboardController = new DashboardController(this.eventBus, this.authentication);

        container.append(`<div class="row justify-content-md-center">
                                <div class="col-md-auto">
                                    <h1>To-Do</h1>
                                </div>
                                </div>
                            <div class="addTaskForm row justify-content-md-center"></div>
                            <div class="todoWidget"></div>`);

        let addTaskForm = new AddTaskForm(container.find(".addTaskForm"), this.eventBus);
        let taskView = new TodoWidget(container.find(".todoWidget"), this.eventBus);

        addTaskForm.render();
        taskView.render();
    }
}