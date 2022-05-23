package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.entities.UserLoader;
import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.network.CommandResult;

import java.io.IOException;

public class SignUpCommand extends AbstractCommand {

    public SignUpCommand(CommandAdmin commandAdmin) {
        super(commandAdmin, "sign_up", "sign up user", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            UserLoader userLoader = new UserLoader();
            User newUser = userLoader.loadUser();
            return new Object[]{newUser};
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        User user = (User) args[0];
        long result = getCommandManager().getDBWorker().addUser(user);
        if (result <= 0) {
            return new CommandResult("Failed to create a user, this login is already occupied.");
        }
        user.setId(result);
        return new CommandResult("Signed up.", user);
    }

}
