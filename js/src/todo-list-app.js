import {EventBus, EventTypes} from "./event/event";
import {Authentication} from "./authentication";
import {DashboardPage} from "./page/dashboardPage";
import {SignInPage} from "./page/signInPage";
import {UserLists} from "./userLists";

/**
 * Starts a to-do list app.
 *
 * @author Oleg Barmin
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
        this.eventBus = new EventBus(this.root);

        const pageContainer = $(this.root.find(`.${pageContainerClass}`)[0]);

        const signInCompletedCallback = event => {
            this.userLists = new UserLists(event.token);
            this.dashboardPage = new DashboardPage(pageContainer, this.eventBus,
                this.authentication, this.userLists);
            this.dashboardPage.render();
        };

        this.eventBus.subscribe(EventTypes.SignInCompleted, signInCompletedCallback);
        this.eventBus.subscribe(EventTypes.SignOutCompleted, () => {
            this.signInPage.render();
        });

        this.signInPage = new SignInPage(pageContainer, this.eventBus, this.authentication);
        this.signInPage.render();
    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});