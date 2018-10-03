package org.javaclasses.todo.web;

/**
 * Default configuration of TodoList application.
 *
 * @author Oleg Barmin
 */
public class Configurations {

    private static final int DEFAULT_PORT = 4567;
    private static final String CONTENT_TYPE = "application/json";

    private Configurations() {
    }

    /**
     * Returns default port of application: 4567.
     *
     * @return default port.
     */
    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    /**
     * Returns default content type of application: 'application/json'.
     *
     * @return default content type.
     */
    static String getContentType() {
        return CONTENT_TYPE;
    }
}
