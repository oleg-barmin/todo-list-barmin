package org.javaclasses.todo.storage.impl;

/**
 * Occurs on programming error, when sub-classes of {@code InMemoryStorage}
 * try to find field which does not exists in stored entity or access to this field was denied.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public
public class SearchByFieldException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code SearchByFieldException} instance.
     *
     * @param fieldName name of field which was not found
     */
    public SearchByFieldException(String fieldName) {
        super("Field with name: '" + fieldName + "' was not found.");
    }
}
