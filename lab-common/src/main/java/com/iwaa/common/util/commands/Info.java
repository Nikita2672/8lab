package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

public class Info extends AbstractCommand {

    public Info(CommandAdmin commandAdmin) {
        super(commandAdmin, "info", "вывести в стандартный поток вывода информацию о коллекции "
                + "(тип, дата инициализации, количество элементов и т.д.)", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult("<html>Collection info:" + getCommandManager().getCollectionManager().getInfo() + "<html>");
    }
}
