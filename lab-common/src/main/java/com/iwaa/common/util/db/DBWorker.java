package com.iwaa.common.util.db;

import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;

import java.sql.SQLException;
import java.util.Set;

public interface DBWorker {

    long addRoute(Route route, User user) throws SQLException;
    long addUser(User user) throws SQLException;
    long updateRoute(Route route) throws SQLException;
    long checkUser(User user) throws SQLException;
    long deleteRoutsByUser(User user) throws SQLException;
    long  deleteRouteById(long routeId) throws SQLException;
    Set<Route> selectAllRoutes();

}
