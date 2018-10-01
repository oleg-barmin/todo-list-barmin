package org.javaclasses.todo.web;

/**
 * Default configuration of TodoList application.
 */
class Configurations {

    private static final int DEFAULT_PORT = 4567;
    private static final String CONTENT_TYPE = "application/json";

    private Configurations() {
    }

    static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    static String getContentType() {
        return CONTENT_TYPE;
    }
}
