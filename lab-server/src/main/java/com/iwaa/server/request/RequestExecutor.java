package com.iwaa.server.request;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.Request;

import java.sql.SQLException;

public class RequestExecutor {

    private final CommandAdmin commandAdmin;

    public RequestExecutor(CommandAdmin commandAdmin) {
        this.commandAdmin = commandAdmin;
    }

    public CommandResult execute(Request request) {
        request.getCommand().setCommandAdmin(commandAdmin);
        if (commandAdmin.getCommandsWithoutAuth().containsKey(request.getCommand().getName())) {
            return commandAdmin.executeCommand(request.getCommand(), request.getArgs());
        }
        try {
            long checkUserResult = commandAdmin.getDBWorker().checkUser(request.getUser());
            if (checkUserResult == 0) {
                return new CommandResult("Sign in/up first, call \"help\" to see list of commands.", false);
            }
        } catch (SQLException e) {
            return new CommandResult("DB problems, try again later.", false);
        }
        return commandAdmin.executeCommand(request.getCommand(), request.getArgs());
    }
}
