package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

public class GroupCounting extends AbstractCommand {

    public GroupCounting(CommandAdmin commandAdmin) {
        super(commandAdmin, "group_counting_by_distance", "сгруппировать элементы коллекции по значению поля distance, "
                + "вывести количество элементов в каждой группе", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult(getCommandManager().getCollectionManager().outputGroupsByDistance());
    }
}
