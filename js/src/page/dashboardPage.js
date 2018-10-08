import {DashboardController} from "../dashboardController";
import {AddTaskForm} from "../view/addTaskForm";
import {TodoWidget} from "../view/todoWidget";
import {Page} from "./page";
import {NavBar} from "../view/navBar";

/**
 * Provides user access to core functionality of to-do list application.
 *
 * @author Oleg Barmin
 */
export class DashboardPage extends Page {


    /**
     * Creates `DashboardPage` instance.
     *
     * @param {jQuery} element element to render page into
     * @param {EventBus} eventBus to subscribe and post page specific events
     * @param {Authentication} authentication to control user session
     * @param {UserLists} userLists to work with user lists
     */
    constructor(element, eventBus, authentication, userLists) {
        super(element, eventBus, authentication);
        this.userLists = userLists;
    }

    /**
     * Renders `DashboardPage` into given element.
     */
    render() {
        this.element.empty();

        const navBarContainerClass = "navBarContainer";

        this.element.append(`<div class='${navBarContainerClass}'></div>`);
        this.element.append(`<div class='container'></div>`);

        const container = $(this.element.find(".container")[0]);
        const navBarContainer = $(this.element.find(`.${navBarContainerClass}`)[0]);

        container.append(`<div class="row justify-content-md-center">
                                <div class="col-md-auto">
                                    <h1>To-Do</h1>
                                </div>
                                </div>
                            <div class="addTaskForm row justify-content-md-center"></div>
                            <div class="todoWidget"></div>`);

        this.dashboardController = new DashboardController(this.eventBus,
            this.authentication, this.userLists);

        let navBar = new NavBar(navBarContainer, this.eventBus, this.authentication);
        let addTaskForm = new AddTaskForm(container.find(".addTaskForm"), this.eventBus);
        let taskView = new TodoWidget(container.find(".todoWidget"), this.eventBus);

        navBar.render();
        addTaskForm.render();
        taskView.render();
    }
}