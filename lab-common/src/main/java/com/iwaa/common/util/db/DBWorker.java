package com.iwaa.common.util.db;

import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;

import java.util.concurrent.CopyOnWriteArraySet;

public interface DBWorker {

    long addRoute(Route route, User user);
    long addUser(User user);
    long updateRoute(Route route);
    long checkUser(User user);
    long deleteRoutsByUser(User user);
    long  deleteRouteById(long routeId);
    CopyOnWriteArraySet<Route> selectAllRoutes();

}
