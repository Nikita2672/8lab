package com.iwaa.common.util.network;

import com.iwaa.common.util.commands.AbstractCommand;
import com.iwaa.common.util.entities.User;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

    private AbstractCommand command;
    private Object[] args;

    public Request(AbstractCommand command, Object[] args) {
        this.command = command;
        this.args = args;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public User getUser() {
        return (User) args[args.length - 1];
    }

    @Override
    public String toString() {
        return "Request{"
                + "command=" + command
                + ", args=" + Arrays.toString(args)
                + '}';
    }
}
