package com.iwaa.common.util.network;

import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

public class CommandResult implements Serializable {

    private String message;
    private Collection<Route> routes;
    private User user;

    public CommandResult(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public CommandResult(String message) {
        this.message = message;
    }

    public CommandResult(Collection<Route> routes) {
        this.routes = routes;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void showResult() {
        if (routes != null) {
            if (routes.isEmpty()) {
                System.out.println("Collection is empty.");
                return;
            }
            System.out.println(routes.stream()
                    .map(Route::toString)
                    .collect(Collectors.joining("\n")));
            return;
        }
        System.out.println(message);
    }

    @Override
    public String toString() {
        return "Response{"
                + "message='" + message + '\''
                + '}';
    }
}