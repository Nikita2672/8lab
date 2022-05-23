package com.iwaa.common.util.controllers;

import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.ResponseExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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

    public void runFile(File file) {
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
                    }
                }
            }
            reader = new InputStreamReader(System.in);
        } catch (IOException e) {
            System.out.println("There is no such file or you haven't permissions");
            reader = new InputStreamReader(System.in);
        }
    }

    public void outputUserName() {

    }

}
