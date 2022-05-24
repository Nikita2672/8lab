package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.data.RouteCreator;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.Request;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Update extends AbstractCommand {

    public Update(CommandAdmin commandAdmin) {
        super(commandAdmin, "update", "обновить значение элемента коллекции, id которого равен заданному", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            long id = Long.parseLong((String) args[0]);
            User user = (User) args[1];
            CommandResult commandResult = getCommandManager().getNetworkListener()
                    .listen(new Request(new FindById(getCommandManager()), new Object[]{id, user}));
            if (commandResult.getMessage() == null) {
                System.out.println("No route with such id");
                return null;
            }
            if (commandResult.getUser() == null) {
                System.out.println("you don't have rights to update this route.");
                return null;
            }
            RouteCreator routeCreator = new RouteCreator(getCommandManager().getCommandListener().getReader());
            if (getCommandManager().getCommandListener().getReader().getClass() == FileReader.class) {
                routeCreator.setFileManager(getCommandManager().getCommandListener().getFileManager());
            }
            Route route = routeCreator.createRoute();
            route.setId(id);
            return new Object[]{route, user};
        } catch (IOException e) {
            System.out.println("Input error.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of id.");
        }
        return null;
    }

    @Override
    public CommandResult execute(Object[] args) {
        try {
        Route updatedRoute = (Route) args[0];
        User user = (User) args[1];
        updatedRoute.setAuthor((user.getLogin()));
        getCommandManager().getDBWorker().updateRoute(updatedRoute);
        getCommandManager().getCollectionManager().update(updatedRoute);
        return new CommandResult("Your Route was successfully Updated");
        } catch (SQLException e) {
            return new CommandResult("Couldn't update route because of DB problems.");
        }
    }
}
