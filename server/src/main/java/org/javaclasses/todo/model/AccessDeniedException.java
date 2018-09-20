package org.javaclasses.todo.model;

/**
 * Occurs when user try to access content, which he has no authority to perform.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public
public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code AccessDeniedException} instance.
     *
     * @param userId   ID of the user who tried to access content
     * @param entityId ID of entity which attempted to access
     */
    AccessDeniedException(UserId userId, EntityId entityId) {
        super(String.format("User with ID '%s' has no authority to modify TodoList with ID: '%s'", userId, entityId));
    }
}
