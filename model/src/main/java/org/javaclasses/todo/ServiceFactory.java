package org.javaclasses.todo;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.StorageFactory;
import org.javaclasses.todo.model.TodoService;

/**
 * Provides services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 *
 * @author Oleg Barmin
 * @implNote all services are lazy initialized
 */
public class ServiceFactory {

    private final StorageFactory storageFactory;
    private Authentication authentication;
    private TodoService todoService;

    public ServiceFactory() {
        storageFactory = new StorageFactory();
    }

    /**
     * Provides instance of {@link TodoService}.
     *
     * @return instance of {@code TodoService}
     */
    public TodoService getTodoService() {
        if (todoService == null) {
            todoService = new TodoService(
                    getAuthentication(),
                    storageFactory.getTodoListStorage(),
                    storageFactory.getTaskStorage());
        }
        return todoService;
    }

    /**
     * Provides instance of {@link Authentication}.
     *
     * @return instance of {@code Authentication}
     */
    public synchronized Authentication getAuthentication() {
        if (authentication == null) {
            authentication =
                    new Authentication(storageFactory.getUserStorage(),
                                       storageFactory.getAuthSessionStorage());
        }
        return authentication;
    }
}