package com.iwaa.server.db;

import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.entities.User;
import com.iwaa.server.utils.Encryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DBWorkerImpl implements DBWorker {

    private static final int ADD_STATEMENT_AUTHOR_ID = 1;
    private static final int ADD_STATEMENT_NAME = 2;
    private static final int ADD_STATEMENT_COORDINATE_X = 3;
    private static final int ADD_STATEMENT_COORDINATE_Y = 4;
    private static final int ADD_STATEMENT_CREATION_DATE = 5;
    private static final int ADD_STATEMENT_LOCATION_FROM_X = 6;
    private static final int ADD_STATEMENT_LOCATION_FROM_Y = 7;
    private static final int ADD_STATEMENT_LOCATION_FROM_Z = 8;
    private static final int ADD_STATEMENT_LOCATION_TO_X = 9;
    private static final int ADD_STATEMENT_LOCATION_TO_Y = 10;
    private static final int ADD_STATEMENT_LOCATION_TO_Z = 11;
    private static final int ADD_STATEMENT_DISTANCE = 12;

    private static final int UPDATE_STATEMENT_NAME = 1;
    private static final int UPDATE_STATEMENT_COORDINATE_X = 2;
    private static final int UPDATE_STATEMENT_COORDINATE_Y = 3;
    private static final int UPDATE_STATEMENT_LOCATION_FROM_X = 4;
    private static final int UPDATE_STATEMENT_LOCATION_FROM_Y = 5;
    private static final int UPDATE_STATEMENT_LOCATION_FROM_Z = 6;
    private static final int UPDATE_STATEMENT_LOCATION_TO_X = 7;
    private static final int UPDATE_STATEMENT_LOCATION_TO_Y = 8;
    private static final int UPDATE_STATEMENT_LOCATION_TO_Z = 9;
    private static final int UPDATE_STATEMENT_DISTANCE = 10;
    private static final int UPDATE_STATEMENT_ID = 11;

    private final DBConnector dbConnector;
    private final Encryptor encryptor;
    private final Statement statement = null;

    public DBWorkerImpl(DBConnector dbConnector, Encryptor encryptor) {
        this.dbConnector = dbConnector;
        this.encryptor = encryptor;
    }

    @Override
    public long addRoute(Route route, User user) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement addRouteToTable = connection.prepareStatement(DBQuery.INSERT_ROUTE.getQuery())
        ) {
            fillAddStatementWithRouteData(addRouteToTable, route);
            addRouteToTable.setTimestamp(ADD_STATEMENT_CREATION_DATE, Timestamp.from(route.getCreationDate().toInstant()));
            addRouteToTable.setLong(ADD_STATEMENT_AUTHOR_ID, user.getId());
            ResultSet resultSet = addRouteToTable.executeQuery();
            resultSet.next();
            long routeId = resultSet.getLong("route_id");
            route.setId(routeId);
            route.setAuthor(user.getLogin());
            return routeId;
        }
    }

    @Override
    public long addUser(User user) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement addUserToTable = connection.prepareStatement(DBQuery.INSERT_USER.getQuery())
        ) {
            addUserToTable.setString(1, user.getLogin());
            addUserToTable.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = addUserToTable.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("user_id");
            user.setId(userId);
            return userId;
        }
    }

    @Override
    public long checkUser(User user) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement checkUser = connection.prepareStatement(DBQuery.SELECT_USER_BY_LOGIN_AND_PASSWORD.getQuery())
        ) {
            checkUser.setString(1, user.getLogin());
            checkUser.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = checkUser.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("user_id");
            }
            return 0;
        }
    }

    @Override
    public long updateRoute(Route route) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement updateRoute = connection.prepareStatement(DBQuery.UPDATE_ROUTE.getQuery())
        ) {
            fillUpdateStatementWithRouteData(updateRoute, route);
            updateRoute.executeUpdate();
            return route.getId();
        }
    }

    @Override
    public long deleteRoutsByUser(User user) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement deleterouteByUser = connection.prepareStatement(DBQuery.DELETE_ROUTES_BY_AUTHOR.getQuery())
        ) {
            deleterouteByUser.setString(1, user.getLogin());
            return deleterouteByUser.executeUpdate();
        }
    }

    @Override
    public long deleteRouteById(long routeId) throws SQLException {
        try (Connection connection = dbConnector.connect();
             PreparedStatement deleteRouteById = connection.prepareStatement(DBQuery.DELETE_ROUTE_BY_ID.getQuery())
        ) {
            deleteRouteById.setLong(1, routeId);
            return deleteRouteById.executeUpdate();
        }
    }


    @Override
    public Set<Route> selectAllRoutes() {
        try (Connection connection = dbConnector.connect();
             PreparedStatement selectAllroutes = connection.prepareStatement(DBQuery.SELECT_ALL_ROUTES.getQuery())
        ) {
            ResultSet resultSet = selectAllroutes.executeQuery();
            return parseRoutesFromResulSet(resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    private Set<Route> parseRoutesFromResulSet(ResultSet resultSet) throws SQLException {
        Set<Route> routes = ConcurrentHashMap.newKeySet();
        while (resultSet.next()) {
            routes.add(getRouteFromTable(resultSet));
        }
        return routes;
    }

    private Route getRouteFromTable(ResultSet resultSet) throws SQLException {
        Route route = new Route(resultSet.getString("route_name"),
                new Coordinates(
                        resultSet.getFloat("coordinates_x"),
                        resultSet.getFloat("coordinates_y")
                ),
                new Location(
                        resultSet.getLong("location_from_x"),
                        resultSet.getInt("location_from_y"),
                        resultSet.getInt("location_from_z")),
                new Location(
                        resultSet.getLong("location_to_x"),
                        resultSet.getInt("location_to_y"),
                        resultSet.getInt("location_to_z")),
                resultSet.getLong("distance"));
        route.setId(resultSet.getLong("route_id"));
        route.setAuthor(resultSet.getString("login"));
        route.setCreationDate(resultSet.getTimestamp("creation_date"));
        return route;
    }

    private void fillAddStatementWithRouteData(PreparedStatement preparedStatement, Route route) throws SQLException {
        preparedStatement.setString(ADD_STATEMENT_NAME, route.getName());
        preparedStatement.setFloat(ADD_STATEMENT_COORDINATE_X, route.getCoordinates().getX());
        preparedStatement.setFloat(ADD_STATEMENT_COORDINATE_Y, route.getCoordinates().getY());
        preparedStatement.setLong(ADD_STATEMENT_LOCATION_FROM_X, route.getFrom().getX());
        preparedStatement.setInt(ADD_STATEMENT_LOCATION_FROM_Y, route.getFrom().getY());
        preparedStatement.setInt(ADD_STATEMENT_LOCATION_FROM_Z, route.getFrom().getZ());
        preparedStatement.setLong(ADD_STATEMENT_LOCATION_TO_X, route.getTo().getX());
        preparedStatement.setInt(ADD_STATEMENT_LOCATION_TO_Y, route.getTo().getY());
        preparedStatement.setInt(ADD_STATEMENT_LOCATION_TO_Z, route.getTo().getZ());
        preparedStatement.setLong(ADD_STATEMENT_DISTANCE, route.getDistance());
    }

    private void fillUpdateStatementWithRouteData(PreparedStatement preparedStatement, Route route) throws SQLException {
        preparedStatement.setString(UPDATE_STATEMENT_NAME, route.getName());
        preparedStatement.setFloat(UPDATE_STATEMENT_COORDINATE_X, route.getCoordinates().getX());
        preparedStatement.setFloat(UPDATE_STATEMENT_COORDINATE_Y, route.getCoordinates().getY());
        preparedStatement.setLong(UPDATE_STATEMENT_LOCATION_FROM_X, route.getFrom().getX());
        preparedStatement.setInt(UPDATE_STATEMENT_LOCATION_FROM_Y, route.getFrom().getY());
        preparedStatement.setInt(UPDATE_STATEMENT_LOCATION_FROM_Z, route.getFrom().getZ());
        preparedStatement.setLong(UPDATE_STATEMENT_LOCATION_TO_X, route.getTo().getX());
        preparedStatement.setInt(UPDATE_STATEMENT_LOCATION_TO_Y, route.getTo().getY());
        preparedStatement.setInt(UPDATE_STATEMENT_LOCATION_TO_Z, route.getTo().getZ());
        preparedStatement.setLong(UPDATE_STATEMENT_DISTANCE, route.getDistance());
        preparedStatement.setLong(UPDATE_STATEMENT_ID, route.getId());
    }
}
