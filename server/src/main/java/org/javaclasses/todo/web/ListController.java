package org.javaclasses.todo.web;

import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;

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
}
