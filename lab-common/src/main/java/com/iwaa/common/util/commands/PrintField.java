package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

public class PrintField extends AbstractCommand {

    public PrintField(CommandAdmin commandAdmin) {
        super(commandAdmin, "print_field_descending_distance", "вывести значения поля distance всех элементов в "
                + "порядке убывания", 0);
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult(getCommandManager().getCollectionManager().outFields());
    }
}
