package com.iwaa.common.util.commands;


import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.network.CommandResult;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {

    private final String name;
    private final String description;
    private final int inlineArgsCount;
    private transient CommandAdmin commandAdmin;

    public AbstractCommand(CommandAdmin commandAdmin, String name, String description, int inlineArgsCount) {
        this.name = name;
        this.description = description;
        this.inlineArgsCount = inlineArgsCount;
        this.commandAdmin = commandAdmin;
    }

    public void setCommandAdmin(CommandAdmin commandAdmin) {
        this.commandAdmin = commandAdmin;
    }

    public abstract CommandResult execute(Object[] args);

    public CommandAdmin getCommandManager() {
        return commandAdmin;
    }

    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        return new Object[]{args[0]};
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getInlineArgsCount() {
        return inlineArgsCount;
    }
}
