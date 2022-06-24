package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.entities.UserLoader;
import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.network.CommandResult;

import java.io.IOException;
import java.sql.SQLException;

public class SignInCommand extends AbstractCommand {

    public SignInCommand(CommandAdmin commandAdmin) {
        super(commandAdmin, "sign_in", "", 2);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            String login = (String) args[0];
            String password = (String) args[1];
            UserLoader userLoader = new UserLoader();
            User user = userLoader.loadUser(login, password);
            return new Object[]{user};
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        try {
            User user = (User) args[0];
            long result = getCommandManager().getDBWorker().checkUser(user);
            if (result > 0) {
                user.setId(result);
                return new CommandResult("Signed in", user);
            } else {
                return new CommandResult("There is no such user");
            }
        } catch (SQLException e) {
            return new CommandResult("Wrong credentials.");
        }
    }
}
