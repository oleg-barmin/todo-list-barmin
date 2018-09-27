package org.javaclasses.todo.web;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides ports for TodoList application testing.
 */
class PortProvider {

    private static final AtomicInteger atomicInteger = new AtomicInteger(4567);

    private PortProvider() {
    }

    static int getPort() {
        return atomicInteger.incrementAndGet();
    }
}
