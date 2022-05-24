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
        super(commandAdmin, "sign_in", "sign in user", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            UserLoader userLoader = new UserLoader();
            User user = userLoader.loadUser();
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
            user.setId(result);
            return new CommandResult("Signed in.", user);
        } catch (SQLException e) {
            return new CommandResult("Wrong credentials.");
        }
    }
}
