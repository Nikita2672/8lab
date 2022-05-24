package com.iwaa.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInitializer {

    private final DBConnector dbConnector;

    public DBInitializer(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public int init() throws SQLException {

        try (Connection connection = dbConnector.connect();
             PreparedStatement createUsersTable = connection.prepareStatement(DBQuery.CREATE_USERS_TABLE.getQuery());
             PreparedStatement createRouteTable = connection.prepareStatement(DBQuery.CREATE_ROUTE_TABLE.getQuery())
        ) {
            createUsersTable.execute();
            createRouteTable.execute();
            return 1;
        }
    }
}
