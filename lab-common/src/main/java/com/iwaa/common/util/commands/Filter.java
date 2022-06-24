package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

public class Filter extends AbstractCommand {

    public Filter(CommandAdmin commandAdmin) {
        super(commandAdmin, "filter_less_than_distance", "выводит все элементы значения полей distance которых меньше чем заданное", 1);
    }

    public Object[] readArgs(Object[] args) {
        try {
            User user = (User) args[1];
            long distance = Long.parseLong((String) args[0]);
            return new Object[]{distance, user};
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of distance.");
        }
        return null;
    }

    @Override
    public CommandResult execute(Object[] args) {
        long distance = (long) args[0];
        return new CommandResult("<html>" + getCommandManager().getCollectionManager().outFilter(distance) + "<html>");
    }
}
