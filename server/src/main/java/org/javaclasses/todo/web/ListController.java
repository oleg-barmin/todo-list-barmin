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
    static class ListCreationRequestHandler extends SecuredAbstractRequestHandler<CreateListPayload> {

        private final TodoService todoService;


        /**
         * Creates {@code ListCreationRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        ListCreationRequestHandler(TodoService todoService) {
            super(CreateListPayload.class);
            this.todoService = checkNotNull(todoService);
        }


        @Override
        Answer processVerifiedRequest(RequestData<CreateListPayload> requestData, Token token) {
            CreateListPayload payload = requestData.getPayload();

            TodoListId todoListId = payload.getTodoListId();

            todoService.createList(todoListId)
                    .authorizedWith(token)
                    .execute();

            return Answer.ok();
        }
    }

    /**
     * Handles read all {@code Task}s from {@code TodoList} request.
     */
    static class ReadTasksRequestHandler extends SecuredAbstractRequestHandler<Void> {
        private final TodoService todoService;

        /**
         * Creates {@code ReadTasksRequestHandler} instance.
         *
         * @param todoService service to work with.
         */
        ReadTasksRequestHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
        }


        @Override
        Answer processVerifiedRequest(RequestData<Void> requestData, Token token) {
            String todoListIdParam = requestData.getRequestParams().getParamValue(TODO_LIST_ID_PARAM);

            TodoListId todoListId = new TodoListId(todoListIdParam);

            List<Task> tasks = todoService.readTasksFrom(todoListId)
                    .authorizedWith(token)
                    .execute();

            String answerBody = objectToJson(tasks);

            return Answer.ok(answerBody);
        }
    }
}
