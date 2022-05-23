package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CollectionAdmin;
import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.data.RouteCreator;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;

public class Add extends AbstractCommand {

    public Add(CommandAdmin commandAdmin) {
        super(commandAdmin, "add", "добавить новый элемент в коллекцию", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            RouteCreator routeCreator = new RouteCreator(getCommandManager().getCommandListener().getReader());
            if (getCommandManager().getCommandListener().getReader().getClass() == FileReader.class) {
                routeCreator.setFileManager(getCommandManager().getCommandListener().getFileManager());
            }
            Route routeToAdd = routeCreator.createRoute();
            return new Object[]{routeToAdd, args[0]};
        } catch (IOException e) {
            System.out.println("Input error.");
            return null;
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        Route routeToAdd = (Route) args[0];
        User user = (User) args[1];
        DBWorker dbWorker = getCommandManager().getDBWorker();
        CollectionAdmin collectionAdmin = getCommandManager().getCollectionManager();
        long a = dbWorker.addRoute(routeToAdd, user);
        if (a <= 0) {
            return new CommandResult("Couldn't add route");
        }
        collectionAdmin.add(routeToAdd);
        return new CommandResult("your Route was successfully added");
    }
}
