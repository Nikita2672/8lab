package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.util.Objects;

public class FindById extends AbstractCommand {

    public FindById(CommandAdmin commandAdmin) {
        super(commandAdmin, "", "", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        User user = (User) args[1];
        Route foundRoute = getCommandManager().getCollectionManager().findById((Long) args[0]);
        if (foundRoute.getId().equals(-1L)) {
            return new CommandResult((String) null);
        }
        if (!Objects.equals(foundRoute.getAuthor(), user.getLogin())) {
            return new CommandResult("", null);
        }
        return new CommandResult(foundRoute.currentValues(), user);
    }
}
