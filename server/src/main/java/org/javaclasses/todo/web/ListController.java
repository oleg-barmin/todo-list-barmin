package org.javaclasses.todo.web;

import org.javaclasses.todo.model.*;

import java.util.List;

import static org.javaclasses.todo.web.TodoListApplication.TODO_LIST_ID_PARAM;

/**
 * Manages requests which modifies to-do lists.
 */
class ListController {

    static class ListCreationHandler extends SecuredAbstractHandler<CreateListPayload> {

        private final TodoService todoService;


        ListCreationHandler(TodoService todoService) {
            super(CreateListPayload.class);
            this.todoService = todoService;
        }


        @Override
        Answer securedProcess(RequestData<CreateListPayload> requestData, Token token) {
            CreateListPayload payload = requestData.getPayload();

            UserId userId = payload.getUserId();
            TodoListId todoListId = payload.getTodoListId();

            todoService.authorizeBy(token)
                    .createList(todoListId)
                    .withOwner(userId)
                    .execute();

            return Answer.ok();
        }
    }

    /**
     * Reads all tasks from to-do list with given ID.
     */
    static class ReadTasksHandler extends SecuredAbstractHandler<Void> {
        private final TodoService todoService;

        ReadTasksHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = todoService;
        }


        @Override
        Answer securedProcess(RequestData<Void> requestData, Token token) {
            String todoListIdParam = requestData.getRequestParams().getParamValue(TODO_LIST_ID_PARAM);

            TodoListId todoListId = new TodoListId(todoListIdParam);

            List<Task> tasks = todoService.authorizeBy(token)
                    .readTasksFrom(todoListId)
                    .execute();

            String answerBody = objectToJson(tasks);

            return Answer.ok(answerBody);
        }
    }
}
