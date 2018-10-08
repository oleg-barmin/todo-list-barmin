import {EventBus, EventTypes} from "./event/event";
import {Authentication} from "./authentication";
import {DashboardPage} from "./page/dashboardPage";
import {SignInPage} from "./page/signInPage";

/**
 * Starts a to-do list app.
 */
export class TodoListApp {

    /**
     * Creates `TodoListApp` instance.
     *
     * @param rootElement root jQuery element where to-do app will be deployed
     */
    constructor(rootElement) {
        this.root = rootElement;
        this.authentication = new Authentication();
    }

    /**
     * Starts a `TodoListApp` for `rootElement` that was provided in constructor.
     *
     * Creates an environment for necessary components and renders them.
     */
    start() {
        const pageContainerClass = "pageContainer";

        this.root.append(`<div class="${pageContainerClass}"></div>`);
        this.root.append(`<div hidden class="eventBus"></div>`);

        this.eventBus = new EventBus(this.root.find(".eventBus"));

        const container = $(this.root.find(`.${pageContainerClass}`)[0]);

        this.eventBus.subscribe(EventTypes.SignInCompleted, () => {
            new DashboardPage(container, this.eventBus, this.authentication).render();
        });

        new SignInPage(container, this.eventBus, this.authentication).render();

    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});