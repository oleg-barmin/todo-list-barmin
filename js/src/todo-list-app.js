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
        this.eventBus = new EventBus(this.root);

        this.signInPage = new SignInPage(this.root, this.eventBus, this.authentication);

        const signInCompletedCallback = () => {
            this.userLists = new UserLists(this.authentication.token);
            this.dashboardPage = new DashboardPage(this.root, this.eventBus, this.authentication, this.userLists);
            this.dashboardPage.render();
        };

        this.eventBus.subscribe(EventTypes.SignInCompleted, signInCompletedCallback);
        this.eventBus.subscribe(EventTypes.SignOutCompleted, () => this.signInPage.render());

        this.authentication.checkSignInUser()
            .then(() => {
                this.userLists = new UserLists(this.authentication.token);
                this.dashboardPage = new DashboardPage(this.root, this.eventBus, this.authentication, this.userLists);
                this.dashboardPage.render();
            })
            .catch(() => {
                this.signInPage.render();
            });
    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});