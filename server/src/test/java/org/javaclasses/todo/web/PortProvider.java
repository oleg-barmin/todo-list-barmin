package org.javaclasses.todo.web;

import java.util.concurrent.atomic.AtomicInteger;

import static org.javaclasses.todo.web.Configurations.getDefaultPort;

/**
 * Provides ports for TodoList application testing.
 *
 * @author Oleg Barmin
 */
class PortProvider {

    private static final AtomicInteger atomicInteger = new AtomicInteger(getDefaultPort());

    private PortProvider() {
    }

    /**
     * Provides available port.
     *
     * @return available port
     */
    static int getAvailablePort() {
        return atomicInteger.incrementAndGet();
    }
}
