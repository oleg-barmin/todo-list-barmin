import {DashboardController} from "../dashboardController";
import {Page} from "./page";
import {NavBar} from "../view/navBar";
import {TodoListView} from "../view/todoListView";
import {TodoListIdGenerator} from "../lib/todoListIdGenerator";

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

        // Renders navigation bar.
        const navBarContainerClass = "navBarContainer";
        this.element.append(`<div class='${navBarContainerClass}'></div>`);
        const navBarContainer = this.element.find(`.${navBarContainerClass}`);
        let navBar = new NavBar(navBarContainer, this.eventBus, this.authentication);
        navBar.render();

        /**
         * Renders `TodoListView`s for all given to-do list IDs.
         *
         * @param {Array} todoListsIds array of ID of to-do lists
         */
        const renderTodoListsArray = todoListsIds => {
            const userTodoListsId = todoListsIds.map(el => el);

            this.dashboardController = new DashboardController(this.eventBus,
                this.authentication, userTodoListsId);

            userTodoListsId.forEach(el => {
                this.element.append(`<div class='container'></div>`);
                const container = this.element.find(".container").last();
                new TodoListView(container, this.eventBus, el).render();
            })
        };

        /**
         * Renders all to-do lists of user, if user has no to-do list yet one to-do list will be created.
         *
         * @param {Array} todoListsIds array of IDs of to-do lists to render.
         */
        const prepareDashboard = todoListsIds => {
            if (todoListsIds.length === 0) {
                // if user has no lists - create one
                const initialTodoListId = TodoListIdGenerator.generateID();
                this.userLists.create(initialTodoListId)
                    .then(() => renderTodoListsArray([initialTodoListId]));
            }
            else {
                renderTodoListsArray(todoListsIds)
            }
        };

        this.userLists.readLists().then(prepareDashboard);
    }
}