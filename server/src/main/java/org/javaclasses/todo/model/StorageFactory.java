package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.javaclasses.todo.storage.impl.UserStorage;

/**
 * Provides storage for services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 *
 * @author Oleg Barmin
 * @implNote all services are lazy initialized
 */
/* Services must not be serialized */
@SuppressWarnings("NonSerializableFieldInSerializableClass")
class StorageFactory {

    private StorageFactory() {
    }

    /**
     * Provides instance of {@link AuthSessionStorage}.
     *
     * @return instance of {@code AuthSessionStorage}
     */
    static AuthSessionStorage getAuthSessionStorage() {
        return AuthSessionStorageEnum.INSTANCE.authSessionStorage;
    }

    /**
     * Provides instance of {@link TaskStorage}.
     *
     * @return instance of {@code TaskStorage}
     */
    static TaskStorage getTaskStorage() {
        return TaskStorageEnum.INSTANCE.taskStorage;
    }

    /**
     * Provides instance of {@link TodoListStorage}.
     *
     * @return instance of {@code TodoListStorage}
     */
    static TodoListStorage getTodoListStorage() {
        return TodoListStorageEnum.INSTANCE.todoListStorage;
    }

    /**
     * Provides instance of {@link UserStorage}.
     *
     * @return instance of {@code UserStorage}
     */
    static UserStorage getUserStorage() {
        return UserStorageEnum.INSTANCE.userStorage;
    }

    private enum AuthSessionStorageEnum {
        INSTANCE;

        private final AuthSessionStorage authSessionStorage;

        AuthSessionStorageEnum() {
            this.authSessionStorage = new AuthSessionStorage();
        }
    }

    private enum TaskStorageEnum {
        INSTANCE;

        private final TaskStorage taskStorage;

        TaskStorageEnum() {
            this.taskStorage = new TaskStorage();
        }
    }

    private enum TodoListStorageEnum {
        INSTANCE;

        private final TodoListStorage todoListStorage;

        TodoListStorageEnum() {
            this.todoListStorage = new TodoListStorage();
        }
    }

    private enum UserStorageEnum {
        INSTANCE;

        private final UserStorage userStorage;

        UserStorageEnum() {
            this.userStorage = new UserStorage();
        }
    }


}
