package com.iwaa.common.util.network;

import com.iwaa.common.util.controllers.CommandListener;

public class ResponseExecutor {

    private final CommandResult commandResult;
    private final CommandListener commandListener;

    public ResponseExecutor(CommandResult commandResult, CommandListener commandListener) {
        this.commandResult = commandResult;
        this.commandListener = commandListener;
    }

    public void execute() {
        commandResult.showResult();
        if (commandResult.getUser() != null) {
            commandListener.setUser(commandResult.getUser());
        }
    }

}
