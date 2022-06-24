package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

public class Exit extends AbstractCommand {

    public Exit(CommandAdmin commandAdmin) {
        super(commandAdmin, "exit", "завершить программу", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        getCommandManager().getPerformanceState().switchPerformanceStatus();
        return new CommandResult("Shutting down.", true);
    }

}
