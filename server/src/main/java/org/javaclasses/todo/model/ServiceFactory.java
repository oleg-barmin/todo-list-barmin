package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;

/**
 * Provides services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 *
 * @author Oleg Barmin
 * @implNote all services are lazy initialized
 */
@SuppressWarnings({
        "NonSerializableFieldInSerializableClass", // service classes cannot be serialized
        "WeakerAccess" // part of public API should remain public.
})
public class ServiceFactory {

    private final StorageFactory storageFactory;
    private Authentication authentication;
    private TodoService todoService;

    public ServiceFactory(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

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
                    new Authentication(storageFactory.getUserStorage(), storageFactory.getAuthSessionStorage());
        }
        return authentication;
    }
}