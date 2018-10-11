import {DashboardController} from "../dashboardController";
import {Page} from "./page";
import {NavBar} from "../view/navBar";
import {TodoListView} from "../view/todoListView";
import {EventTypes} from "../event/event";
import {TodoListsUpdated} from "../event/TodoListsUpdated";

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
     * @param {Backend} backend to send requests to the server
     */
    constructor(element, eventBus, authentication, backend) {
        super(element, eventBus, authentication);
        this.backend = backend;
    }

    /**
     * Renders `DashboardPage` into given element.
     */
    render() {
        this.element.empty();

        // Renders navigation bar.
        const navBarContainerClass = "navBarContainer";
        this.element.append(`<div class='${navBarContainerClass}'></div>`);
        const navBarContainer = this.element.find(`.${navBarContainerClass}`);
        let navBar = new NavBar(navBarContainer, this.eventBus, this.authentication);
        navBar.render();

        this.dashboardController = new DashboardController(this.eventBus,
            this.authentication, this.backend);

        /**
         * Renders all to-do lists of user, if user has no to-do list yet one to-do list will be created.
         *
         * @param {TodoListsUpdated} event `TodoListsUpdated` event to process
         */
        const prepareDashboard = event => {
            const todoListsIds = event.todoListIds;

            const userTodoListsId = todoListsIds.map(el => el);

            userTodoListsId.forEach(el => {
                this.element.append(`<div class='container'></div>`);
                const container = this.element.find(".container").last();
                new TodoListView(container, this.eventBus, el).render();
            })
        };

        const todoListUpdatedHandler = this.eventBus.subscribe(EventTypes.TodoListsUpdated, prepareDashboard);
        this.eventBus.subscribe(EventTypes.SignOutCompleted, () =>
            this.eventBus.unsubscribe(EventTypes.TodoListsUpdated, todoListUpdatedHandler));
    }
}