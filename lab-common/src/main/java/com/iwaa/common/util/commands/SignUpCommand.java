package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.entities.UserLoader;
import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.network.CommandResult;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpCommand extends AbstractCommand {

    public SignUpCommand(CommandAdmin commandAdmin) {
        super(commandAdmin, "sign_up", "", 2);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            String login = (String) args[0];
            String password = (String) args[1];
            UserLoader userLoader = new UserLoader();
            User newUser = userLoader.loadUser(login, password);
            return new Object[]{newUser};
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        try {
            User user = (User) args[0];
            long result = getCommandManager().getDBWorker().addUser(user);
            user.setId(result);
            return new CommandResult("Signed up", user);
        } catch (SQLException e) {
            return new CommandResult("Failed to create a user, this login is already occupied.");
        }
    }
}
