package server;

import com.iwaa.common.util.controllers.CollectionAdmin;
import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.state.State;
import server.collection.CollectionAdminImpl;
import server.db.DBConnector;
import server.db.DBInitializer;
import server.db.DBWorkerImpl;
import server.request.RequestExecutor;
import server.utils.Encryptor;
import server.utils.MD2Encryptor;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        DBConnector dbConnector = new DBConnector();
        DBInitializer dbInitializer = new DBInitializer(dbConnector);
        if (dbInitializer.init() > 0) {
            Encryptor encryptor = new MD2Encryptor();
            DBWorker dbWorker = new DBWorkerImpl(dbConnector, encryptor);
            CopyOnWriteArraySet<Route> routes = dbWorker.selectAllRoutes();
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
                    System.out.print("");
                }
            }
        } else {
            System.out.println("Please initialize correct local variables\n"
                            + "HOST\n"
                            + "DB_NAME\n"
                            + "USER\n"
                            + "PASSWORD");
        }
    }
}
