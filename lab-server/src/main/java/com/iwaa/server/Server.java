package com.iwaa.server;

import com.iwaa.common.util.controllers.CollectionAdmin;
import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.state.State;
import com.iwaa.server.db.DBInitializer;
import com.iwaa.server.request.RequestExecutor;
import com.iwaa.server.collection.CollectionAdminImpl;
import com.iwaa.server.db.DBConnector;
import com.iwaa.server.db.DBWorkerImpl;
import com.iwaa.server.utils.Encryptor;
import com.iwaa.server.utils.MD2Encryptor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            DBConnector dbConnector = new DBConnector();
            DBInitializer dbInitializer = new DBInitializer(dbConnector);
            dbInitializer.init();
            Encryptor encryptor = new MD2Encryptor();
            DBWorker dbWorker = new DBWorkerImpl(dbConnector, encryptor);
            Set<Route> routes = dbWorker.selectAllRoutes();
            if (routes != null) {
                CollectionAdmin collectionAdmin = new CollectionAdminImpl(routes);
                State state = new State();
                CommandAdmin commandAdmin = new CommandAdmin(collectionAdmin, dbWorker, state);
                RequestExecutor requestExecutor = new RequestExecutor(commandAdmin);
                try {
                    ServerConnectAdmin serverConnectAdmin = new ServerConnectAdmin(requestExecutor, state);
                    new Thread(new CommandListener(commandAdmin, false)).start();
                    serverConnectAdmin.run();
                } catch (IOException e) {
                    System.out.println("The reason of exception:\n" + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Please initialize correct local variables\n"
                    + "DB_HOST\n"
                    + "DB_NAME\n"
                    + "DB_USER\n"
                    + "DB_PASSWORD");
        }
    }
}
