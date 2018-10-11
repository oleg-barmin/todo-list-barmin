import {EventBus, EventTypes} from "./event/event";
import {Authentication} from "./authentication";
import {DashboardPage} from "./page/dashboardPage";
import {SignInPage} from "./page/signInPage";
import {UserLists} from "./userLists";
import {Backend} from "./backend";
import {TodoListsUpdated} from "./event/TodoListsUpdated";
import {TodoListIdGenerator} from "./lib/todoListIdGenerator";

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
        this.backend = new Backend(window.location.origin);
        this.authentication = new Authentication(this.backend);
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
            this.userLists = new UserLists(this.backend, this.authentication.token);
            this.dashboardPage = new DashboardPage(this.root, this.eventBus, this.authentication, this.backend);
            this.dashboardPage.render();

            this.userLists.readLists().then((todoListsIds) => {
                if (todoListsIds.length === 0) {
                    // if user has no lists - create one
                    const initialTodoListId = TodoListIdGenerator.generateID();
                    this.userLists.create(initialTodoListId)
                        .then(() => this.eventBus.post(new TodoListsUpdated([initialTodoListId])))
                        .catch(() => alert("Initializing of to-do list failed."))
                }
                else {
                    this.eventBus.post(new TodoListsUpdated(todoListsIds))
                }
            });
        };

        this.eventBus.subscribe(EventTypes.SignInCompleted, () => signInCompletedCallback());
        this.eventBus.subscribe(EventTypes.SignOutCompleted, () => this.signInPage.render());

        this.authentication.checkSignInUser()
            .then(() => signInCompletedCallback())
            .catch(() => this.signInPage.render());
    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});