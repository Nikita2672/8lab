package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.data.RouteCreator;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RemoveLower extends AbstractCommand {

    public RemoveLower(CommandAdmin commandAdmin) {
        super(commandAdmin, "remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", 0);
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
        }
        return null;
    }

    @Override
    public CommandResult execute(Object[] args) {
        User user = (User) args[1];
        int collectionLen = getCommandManager().getCollectionManager().getCollection().size();
        List<Route> routesToDelete = getCommandManager().getCollectionManager().getCollection().stream().filter(route -> (Objects.equals(route.getAuthor(), user.getLogin()) && route.compareTo((Route) args[0]) < 0)).collect(Collectors.toList());
        for (Route routeToDelete : routesToDelete) {
            try {
                getCommandManager().getDBWorker().deleteRouteById(routeToDelete.getId());
                getCommandManager().getCollectionManager().remove(routeToDelete);
            } catch (SQLException e) {
                return new CommandResult("Couldn't delete because DB problems.");
            }
        }
        return new CommandResult((collectionLen - getCommandManager().getCollectionManager().getCollection().size()) + " object(s) was deleted");
    }
}
