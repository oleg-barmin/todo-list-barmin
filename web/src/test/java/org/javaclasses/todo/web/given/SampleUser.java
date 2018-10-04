package org.javaclasses.todo.web.given;

import com.google.common.collect.Lists;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Username;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents data about user which can be received from client-side of to-do list application.
 *
 * @author Oleg Barmin
 */
public class SampleUser {

    private final Username username;
    private final Password password;

    private final List<String> taskDescriptions;

    SampleUser(Username username, Password password, String... taskDescriptions) {
        this.username = username;
        this.password = password;
        this.taskDescriptions = Lists.newArrayList(taskDescriptions);
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public List<String> getTaskDescriptions() {
        return Collections.unmodifiableList(taskDescriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, taskDescriptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SampleUser)) {
            return false;
        }
        SampleUser that = (SampleUser) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(taskDescriptions, that.taskDescriptions);
    }
}
