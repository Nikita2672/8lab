package com.iwaa.server.db;

public enum DBQuery {

    CREATE_ROUTE_TABLE("CREATE TABLE IF NOT EXISTS routes\n"
            + "(\n"
            + "    route_id BIGSERIAL PRIMARY KEY,\n"
            + "    author_id bigint references users(user_id) on delete cascade,\n"
            + "    route_name character varying(1009) NOT NULL,\n"
            + "    coordinates_x float NOT NULL,\n"
            + "    coordinates_y float NOT NULL,\n"
            + "    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,\n"
            + "    location_from_x bigint NOT NULL,\n"
            + "    location_from_y integer NOT NULL,\n"
            + "    location_from_z integer NOT NULL,\n"
            + "    location_to_x bigint NOT NULL,\n"
            + "    location_to_y integer NOT NULL,\n"
            + "    location_to_z integer NOT NULL,\n"
            + "    distance bigint NOT NULL\n"
            + ")"),
    CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS users\n"
            + "(\n"
            + "    login character varying(200) UNIQUE NOT NULL check(length(login) >= 1),\n"
            + "    password character varying(40) NOT NULL check(length(password) >= 4),\n"
            + "    user_id BIGSERIAL PRIMARY KEY\n"
            + ")"),

    INSERT_USER("INSERT INTO users (login, password) values(?, ?) RETURNING user_id"),

    SELECT_USER_BY_LOGIN_AND_PASSWORD("SELECT user_id from users where login = ? and password = ?"),
    //checked
    INSERT_ROUTE("INSERT INTO routes VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING route_id"),
    //checked
    UPDATE_ROUTE("UPDATE routes SET route_name = ?,"
            + " coordinates_x = ?,"
            + " coordinates_y = ?,"
            + " location_from_x = ?,"
            + " location_from_y = ?,"
            + " location_from_z = ?,"
            + " location_to_x = ?,"
            + " location_to_y = ?,"
            + " location_to_z = ?,"
            + " distance = ?"
            + " WHERE route_id = ?"),

    SELECT_ALL_ROUTES("SELECT route_id, login, route_name, coordinates_x, coordinates_y, creation_date,\n"
            + " location_from_x, location_from_y, location_from_z, location_to_x, location_to_y, location_to_z, distance\n"
            + " FROM routes\n"
            + " JOIN users on author_id = users.user_id"),


    DELETE_ROUTES_BY_AUTHOR("DELETE from routes where author_id = (select user_id from users where login=?)"),

    DELETE_ROUTE_BY_ID("DELETE from routes where route_id = ?");

    private final String query;

    DBQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}
