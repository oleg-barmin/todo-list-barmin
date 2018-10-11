import {EventBus, EventTypes} from "../src/event/event";
import {Authentication} from "../src/authentication";
import {MockBackend} from "./model/mockBackend";
import {SignInController} from "../src/signInController";
import {CredentialsSubmitted} from "../src/event/credentialsSubmitted";

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

QUnit.module("SignInController should");
QUnit.test("post SignInCompleted if user was signed in", async assert => {
    const body = $(document.body);
    const eventBus = new EventBus(body);
    const mockBackend = new MockBackend();

    const authentication = new Authentication(mockBackend);
    SignInController.initializeController(eventBus, authentication);

    const username = "bob";
    const password = "bob123";
    const token = JSON.stringify({
        value: "token"
    });

    let calls = 0;
    eventBus.subscribe(EventTypes.SignInCompleted, () => calls++);
    mockBackend.signInUser = () => {
        return new Promise((resolve => {
            console.log("resolve");
            resolve(token)
        }));
    };

    eventBus.post(new CredentialsSubmitted(username, password));

    await sleep(100);

    assert.strictEqual(calls, 1);
});

QUnit.test("post SignInFailed event on user sign in fail.", async assert => {
    const body = $(document.body);
    const eventBus = new EventBus(body);
    const mockBackend = new MockBackend();

    const authentication = new Authentication(mockBackend);
    SignInController.initializeController(eventBus, authentication);

    const username = "bob";
    const password = "bob123";

    let calls = 0;
    mockBackend.signInUser = () => {
        return new Promise(((resolve, reject) => {
            console.log("reject");
            reject()
        }));
    };
    eventBus.subscribe(EventTypes.SignInFailed, () => calls++);
    eventBus.post(new CredentialsSubmitted(username, password));

    await sleep(100);
    assert.strictEqual(calls, 1);
});