package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

import java.util.stream.Collectors;

public class Help extends AbstractCommand {
    public Help(CommandAdmin commandAdmin) {
        super(commandAdmin, "help", "вывести справку по доступным командам", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult("<html>" + getCommandManager().getCommands()
                .values()
                .stream()
                .filter(value -> !(value.getName().equals("sign_in") || value.getName().equals("sign_up")
                        || value.getName().equals("exit") || value.getName().equals("update")))
                .map(value -> value.getName() + " - " + value.getDescription())
                .collect(Collectors.joining("<br>")) + "<html>", true);
    }
}
