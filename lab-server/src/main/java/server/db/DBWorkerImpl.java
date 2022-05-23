package server.db;


import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.entities.User;
import server.utils.Encryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArraySet;

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

    public DBWorkerImpl(DBConnector dbConnector, Encryptor encryptor) {
        this.dbConnector = dbConnector;
        this.encryptor = encryptor;
    }

    @Override
    public long addRoute(Route route, User user) {
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
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long addUser(User user) {
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
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long checkUser(User user) {
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
        } catch (SQLException e) {
            System.out.println("mistake");
            return -1;
        }
    }

    @Override
    public long updateRoute(Route route) {
        try (Connection connection = dbConnector.connect();
             PreparedStatement updateRoute = connection.prepareStatement(DBQuery.UPDATE_ROUTE.getQuery())
        ) {
            fillUpdateStatementWithRouteData(updateRoute, route);
            updateRoute.executeUpdate();
            return route.getId();
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long deleteRoutsByUser(User user) {
        try (Connection connection = dbConnector.connect();
             PreparedStatement deleterouteByUser = connection.prepareStatement(DBQuery.DELETE_ROUTES_BY_AUTHOR.getQuery())
        ) {
            deleterouteByUser.setString(1, user.getLogin());
            return deleterouteByUser.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long deleteRouteById(long routeId) {
        try (Connection connection = dbConnector.connect();
             PreparedStatement deleterouteById = connection.prepareStatement(DBQuery.DELETE_ROUTE_BY_ID.getQuery())
        ) {
            deleterouteById.setLong(1, routeId);
            return deleterouteById.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }


    @Override
    public CopyOnWriteArraySet<Route> selectAllRoutes() {
        try (Connection connection = dbConnector.connect();
             PreparedStatement selectAllroutes = connection.prepareStatement(DBQuery.SELECT_ALL_ROUTES.getQuery())
        ) {
            ResultSet resultSet = selectAllroutes.executeQuery();
            return parseRoutesFromResulSet(resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    private CopyOnWriteArraySet<Route> parseRoutesFromResulSet(ResultSet resultSet) throws SQLException {
        CopyOnWriteArraySet<Route> routes = new CopyOnWriteArraySet<>();
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

    private void fillAddStatementWithRouteData(PreparedStatement statement, Route route) throws SQLException {
        statement.setString(ADD_STATEMENT_NAME, route.getName());
        statement.setFloat(ADD_STATEMENT_COORDINATE_X, route.getCoordinates().getX());
        statement.setFloat(ADD_STATEMENT_COORDINATE_Y, route.getCoordinates().getY());
        statement.setLong(ADD_STATEMENT_LOCATION_FROM_X, route.getFrom().getX());
        statement.setInt(ADD_STATEMENT_LOCATION_FROM_Y, route.getFrom().getY());
        statement.setInt(ADD_STATEMENT_LOCATION_FROM_Z, route.getFrom().getZ());
        statement.setLong(ADD_STATEMENT_LOCATION_TO_X, route.getTo().getX());
        statement.setInt(ADD_STATEMENT_LOCATION_TO_Y, route.getTo().getY());
        statement.setInt(ADD_STATEMENT_LOCATION_TO_Z, route.getTo().getZ());
        statement.setLong(ADD_STATEMENT_DISTANCE, route.getDistance());
    }

    private void fillUpdateStatementWithRouteData(PreparedStatement statement, Route route) throws SQLException {
        statement.setString(UPDATE_STATEMENT_NAME, route.getName());
        statement.setFloat(UPDATE_STATEMENT_COORDINATE_X, route.getCoordinates().getX());
        statement.setFloat(UPDATE_STATEMENT_COORDINATE_Y, route.getCoordinates().getY());
        statement.setLong(UPDATE_STATEMENT_LOCATION_FROM_X, route.getFrom().getX());
        statement.setInt(UPDATE_STATEMENT_LOCATION_FROM_Y, route.getFrom().getY());
        statement.setInt(UPDATE_STATEMENT_LOCATION_FROM_Z, route.getFrom().getZ());
        statement.setLong(UPDATE_STATEMENT_LOCATION_TO_X, route.getTo().getX());
        statement.setInt(UPDATE_STATEMENT_LOCATION_TO_Y, route.getTo().getY());
        statement.setInt(UPDATE_STATEMENT_LOCATION_TO_Z, route.getTo().getZ());
        statement.setLong(UPDATE_STATEMENT_DISTANCE, route.getDistance());
        statement.setLong(UPDATE_STATEMENT_ID, route.getId());
    }
}
