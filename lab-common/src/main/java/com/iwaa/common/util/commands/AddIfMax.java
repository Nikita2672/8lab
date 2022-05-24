package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.data.RouteCreator;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

public class AddIfMax extends AbstractCommand {

    public AddIfMax(CommandAdmin commandAdmin) {
        super(commandAdmin, "add_if_max", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", 0);
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
            return new Object[0];
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        Route newRoute = (Route) args[0];
        User user = (User) args[1];
        if (newRoute.compareTo(Collections.max(getCommandManager().getCollectionManager().getCollection())) > 0) {
            try {
                getCommandManager().getDBWorker().addRoute(newRoute, user);
            } catch (SQLException e) {
                return new CommandResult("Couldn't add route");
            }
            getCommandManager().getCollectionManager().add(newRoute);
            return new CommandResult("New Route was successfully added!");
        } else {
            return new CommandResult("Given route is not maximal");
        }
    }
}
