package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;

import java.io.File;

public class ExecuteScript extends AbstractCommand {

    public ExecuteScript(CommandAdmin commandAdmin) {
        super(commandAdmin, "execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме", 1);
    }

    @Override
    public CommandResult execute(Object[] args) {
        String fileName = (String) args[0];
        if (getCommandManager().getCommandListener().getFileManager().getCurrentFiles().contains(new File(fileName))) {
            return new CommandResult("The command was ignored because it will call recursion.");
        } else {
            File file = new File(fileName);
            getCommandManager().getCommandListener().runFile(file);
        }
        return new CommandResult("Exiting from " + fileName);
    }
}
