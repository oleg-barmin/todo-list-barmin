package org.javaclasses.todo.web;

import org.javaclasses.todo.model.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.javaclasses.todo.web.TodoListApplication.TODO_LIST_ID_PARAM;

/**
 * Manages requests which modifies {@link TodoList}.
 */
class ListController {

    /**
     * Handles create {@code TodoList} request.
     */
    static class ListCreationHandler extends SecuredAbstractHandler<CreateListPayload> {

        private final TodoService todoService;


        /**
         * Creates {@code ListCreationHandler} instance.
         *
         * @param todoService service to work with
         */
        ListCreationHandler(TodoService todoService) {
            super(CreateListPayload.class);
            this.todoService = checkNotNull(todoService);
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
     * Handles read all {@code Task}s from {@code TodoList} request.
     */
    static class ReadTasksHandler extends SecuredAbstractHandler<Void> {
        private final TodoService todoService;

        /**
         * Creates {@code ReadTasksHandler} instance.
         *
         * @param todoService service to work with.
         */
        ReadTasksHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
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
