package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.Request;

import java.sql.SQLException;

public class Remove extends AbstractCommand {

    public Remove(CommandAdmin commandAdmin) {
        super(commandAdmin, "remove_by_id", "удалить элемент из коллекции по его ID", 1);
    }

    public Object[] readArgs(Object[] args) {
        try {
            User user = (User) args[1];
            long routeId = Long.parseLong((String) args[0]);
            CommandResult commandResult = getCommandManager().getNetworkListener().listen(new Request(new FindById(getCommandManager()), new Object[]{routeId, user}));
            if (commandResult.getMessage() == null) {
                System.out.println("No route found with such id.");
                return null;
            }
            if (commandResult.getUser() == null) {
                System.out.println("You do not have rights to remove this route.");
                return null;
            }

            return new Object[]{routeId, user};
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of id.");
        }
        return null;
    }

    @Override
    public CommandResult execute(Object[] args) {
        try {
            long routeId = (Long) args[0];
            long delResult = getCommandManager().getDBWorker().deleteRouteById(routeId);
            if (delResult == 0) {
                return new CommandResult("No route found with such id.", false);
            }
            getCommandManager().getCollectionManager().removeById(routeId);
            return new CommandResult("Removed successfully!", true);
        } catch (SQLException e) {
            return new CommandResult("Could not remove route because of DB problems.", false);
        }
    }
}
