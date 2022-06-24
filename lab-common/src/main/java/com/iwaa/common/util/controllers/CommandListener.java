package com.iwaa.common.util.controllers;

import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.ResponseExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import static com.iwaa.common.util.commands.ParametersConstants.COORDINATE_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.ID_ARGS;


public class CommandListener implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final boolean onClient;
    private final CommandAdmin commandAdmin;
    private User user;
    private Reader reader;
    private final FileManager fileManager = new FileManager();

    public CommandListener(Reader reader, CommandAdmin commandAdmin, boolean onClient) {
        this.reader = reader;
        this.commandAdmin = commandAdmin;
        this.onClient = onClient;
    }

    public CommandListener(CommandAdmin commandAdmin, boolean onClient) {
        this(new InputStreamReader(System.in), commandAdmin, onClient);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Reader getReader() {
        return reader;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOnClient() {
        return onClient;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(reader)) {
            while (commandAdmin.getPerformanceState().getPerformanceStatus()) {
                if (reader.getClass() != FileReader.class) {
                    System.out.println("===========================");
                }
                outputUserName();
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    CommandResult commandResult = commandAdmin.onCommandReceived(input, isOnClient(), user);
                    if (commandResult != null) {
                        ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
                        responseExecutor.execute();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid i/o.");
        }
    }

    public CommandResult runUser(String login, String password, String nameOfCommand) {
        setUser(new User());
        CommandResult commandResult = commandAdmin.onCommandReceived(nameOfCommand + " " + login + " " + password, isOnClient(), user);
        if (commandResult != null) {
            System.out.println(commandResult.getMessage());
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runInfo() {
        CommandResult commandResult = commandAdmin.onCommandReceived("info", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runHelp() {
        CommandResult commandResult = commandAdmin.onCommandReceived("help", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runPrint() {
        CommandResult commandResult = commandAdmin.onCommandReceived("print_field_descending_distance", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runHistory() {
        CommandResult commandResult = commandAdmin.onCommandReceived("history", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runClear() {
        CommandResult commandResult = commandAdmin.onCommandReceived("clear", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runGroup() {
        CommandResult commandResult = commandAdmin.onCommandReceived("group_counting_by_distance", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runShow() {
        CommandResult commandResult = commandAdmin.onCommandReceived("show", isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
            return commandResult;
        }
        Set<Route> routes = new HashSet<>();
        return new CommandResult(routes, true);
    }

    public CommandResult runRemove(Long id) {
        CommandResult commandResult = commandAdmin.onCommandReceived("remove_by_id" + " " + id, isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
            return commandResult;
        } else {
            return new CommandResult("<html>There is no such Route<br>or you haven't permissions<html>", false);
        }
    }

    public CommandResult runAdd(ArrayList<String> args) {
        CommandResult commandResult = commandAdmin.onCommandReceived("add" + " " + args.get(0) + " " + args.get(1) + " "
                + args.get(2) + " " + args.get(COORDINATE_Y_ARGS) + " " + args.get(LOCATION_FROM_X_ARGS) + " "
                + args.get(LOCATION_FROM_Y_ARGS) + " " + args.get(LOCATION_FROM_Z_ARGS) + " "
                + args.get(LOCATION_TO_X_ARGS) + " " + args.get(LOCATION_TO_Y_ARGS)
                + " " + args.get(LOCATION_TO_Z_ARGS), isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runUpdate(ArrayList<String> args) {
        CommandResult commandResult = commandAdmin.onCommandReceived("update" + " " + args.get(0) + " " + args.get(1) + " "
                + args.get(2) + " " + args.get(COORDINATE_Y_ARGS) + " " + args.get(LOCATION_FROM_X_ARGS) + " "
                + args.get(LOCATION_FROM_Y_ARGS) + " " + args.get(LOCATION_FROM_Z_ARGS) + " "
                + args.get(LOCATION_TO_X_ARGS) + " " + args.get(LOCATION_TO_Y_ARGS)
                + " " + args.get(LOCATION_TO_Z_ARGS) + " " + args.get(ID_ARGS), isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
            return commandResult;
        } else {
            return new CommandResult("It's not your Route", false);
        }
    }

    public CommandResult runAddIfMax(ArrayList<String> args) {
        CommandResult commandResult = commandAdmin.onCommandReceived("add_if_max" + " " + args.get(0) + " " + args.get(1) + " "
                + args.get(2) + " " + args.get(COORDINATE_Y_ARGS) + " " + args.get(LOCATION_FROM_X_ARGS) + " "
                + args.get(LOCATION_FROM_Y_ARGS) + " " + args.get(LOCATION_FROM_Z_ARGS) + " "
                + args.get(LOCATION_TO_X_ARGS) + " " + args.get(LOCATION_TO_Y_ARGS)
                + " " + args.get(LOCATION_TO_Z_ARGS), isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runRemoveLower(ArrayList<String> args) {
        CommandResult commandResult = commandAdmin.onCommandReceived("remove_lower" + " " + args.get(0) + " " + args.get(1) + " "
                + args.get(2) + " " + args.get(COORDINATE_Y_ARGS) + " " + args.get(LOCATION_FROM_X_ARGS) + " "
                + args.get(LOCATION_FROM_Y_ARGS) + " " + args.get(LOCATION_FROM_Z_ARGS) + " "
                + args.get(LOCATION_TO_X_ARGS) + " " + args.get(LOCATION_TO_Y_ARGS)
                + " " + args.get(LOCATION_TO_Z_ARGS), isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runFilter(Long distance) {
        CommandResult commandResult = commandAdmin.onCommandReceived("filter_less_than_distance" + " " + distance, isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public CommandResult runFile(File file) {
        StringJoiner results = new StringJoiner("\n");
        try {
            fileManager.connectToFile(file);
            reader = new FileReader(file);
            while (commandAdmin.getPerformanceState().getPerformanceStatus()) {
                String input = fileManager.nextLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    CommandResult commandResult = commandAdmin.onCommandReceived(input, isOnClient(), user);
                    if (commandResult != null) {
                        ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
                        responseExecutor.execute();
                        results.add(commandResult.getMessage());
                    }
                }
            }
            return new CommandResult(results.toString(), true);
        } catch (IOException e) {
            return new CommandResult("There is no such file or you haven't permissions", false);
        }
    }

    public CommandResult runScript(String path) {
        CommandResult commandResult = commandAdmin.onCommandReceived("execute_script" + " " + path, isOnClient(), user);
        if (commandResult != null) {
            ResponseExecutor responseExecutor = new ResponseExecutor(commandResult, this);
            responseExecutor.execute();
        }
        return commandResult;
    }

    public void outputUserName() {

    }
}
