import {Application} from "./todo-list-app";

$(function () {
    let todoLists = $(".todoList");
    new Application($(todoLists[0])).start();
    new Application($(todoLists[1])).start();
});