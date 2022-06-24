package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

import java.util.Queue;
import java.util.stream.Collectors;

public class History extends AbstractCommand {

    public History(CommandAdmin commandAdmin) {
        super(commandAdmin, "history", "вывести последние 11 команд (без их аргументов)", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        Queue<String> history = getCommandManager().getCommandHistory();
        if (history.isEmpty()) {
            return new CommandResult("History is empty.");
        } else {
            return new CommandResult("<html>The last 11 commands were:<br>" + history.stream().
                    limit(history.size() - 1).
                    collect(Collectors.joining("<br>")) + "<html>");
        }
    }
}
