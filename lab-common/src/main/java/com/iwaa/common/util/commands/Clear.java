package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.sql.SQLException;

public class Clear extends AbstractCommand {
    public Clear(CommandAdmin commandAdmin) {
        super(commandAdmin, "clear", "очистить коллекцию", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[]{args[0]};
    }

    @Override
    public CommandResult execute(Object[] args) {
        User user = (User) args[0];
        try {
            getCommandManager().getDBWorker().deleteRoutsByUser(user);
            getCommandManager().getCollectionManager().clear(user.getLogin());
            return new CommandResult("Your routes was successfully deleted", true);
        } catch (SQLException e) {
            return new CommandResult("Could not clear routes, db problems.", false);
        }
    }
}
