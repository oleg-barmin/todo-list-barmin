package org.javaclasses.todo.web;

import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.Token;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.javaclasses.todo.web.Routes.getTodoListIdParam;

/**
 * Processes requests which modifies {@link TodoList}.
 *
 * @author Oleg Barmin
 */
class TodoListController {

    /**
     * Handles create {@code TodoList} request.
     */
    static class CreateTodoListRequestHandler extends SecuredAbstractRequestHandler<CreateListPayload> {

        private final TodoService todoService;

        /**
         * Creates {@code CreateTodoListRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        CreateTodoListRequestHandler(TodoService todoService) {
            super(CreateListPayload.class);
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Creates to-do list with ID specified in URL parameters.
         *
         * @param requestData data of create list request
         * @param token       token of user who sent request
         * @return answer with 200 status code if list was created successfully
         * @throws AuthorizationFailedException if user token expired.
         */
        @Override
        HttpResponse processVerifiedRequest(RequestData<CreateListPayload> requestData, Token token) {
            CreateListPayload payload = requestData.getPayload();

            TodoListId todoListId = payload.getTodoListId();

            todoService.createList(todoListId)
                       .authorizedWith(token)
                       .execute();

            return HttpResponse.ok();
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

        /**
         * Reads all tasks from to-do list with ID specified in URL params.
         *
         * @param requestData data of read all tasks
         * @param token       token of user who sent request
         * @return answer with status code 200 and requested to-do list tasks
         * if reading of tasks was performed successfully
         * @throws TodoListNotFoundException    if to-do list with given ID was not found
         * @throws AuthorizationFailedException if user toke expired or
         *                                      user has no permission to read task from this list.
         */
        @Override
        HttpResponse processVerifiedRequest(RequestData<Void> requestData, Token token) {
            String todoListIdParam = requestData.getRequestParams()
                                                .getParamValue(getTodoListIdParam());

            TodoListId todoListId = new TodoListId(todoListIdParam);

            List<Task> tasks = todoService.readTasksFrom(todoListId)
                                          .authorizedWith(token)
                                          .execute();

            String answerBody = objectToJson(tasks);

            return HttpResponse.ok(answerBody);
        }
    }
}
