package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

public class Show extends AbstractCommand {

    public Show(CommandAdmin commandAdmin) {
        super(commandAdmin, "show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult(getCommandManager().getCollectionManager().show(), true);
    }
}
