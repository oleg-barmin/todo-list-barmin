package org.javaclasses.todo.web;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides ports for TodoList application testing.
 *
 * @author Oleg Barmin
 */
class PortProvider {

    private static final AtomicInteger atomicInteger = new AtomicInteger(4567);

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
