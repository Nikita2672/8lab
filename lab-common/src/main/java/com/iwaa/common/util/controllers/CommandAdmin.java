package com.iwaa.common.util.controllers;

import com.iwaa.common.util.commands.AbstractCommand;
import com.iwaa.common.util.commands.Add;
import com.iwaa.common.util.commands.AddIfMax;
import com.iwaa.common.util.commands.Clear;
import com.iwaa.common.util.commands.ExecuteScript;
import com.iwaa.common.util.commands.Exit;
import com.iwaa.common.util.commands.Filter;
import com.iwaa.common.util.commands.GroupCounting;
import com.iwaa.common.util.commands.Help;
import com.iwaa.common.util.commands.History;
import com.iwaa.common.util.commands.Info;
import com.iwaa.common.util.commands.PrintField;
import com.iwaa.common.util.commands.Remove;
import com.iwaa.common.util.commands.RemoveLower;
import com.iwaa.common.util.commands.Show;
import com.iwaa.common.util.commands.SignInCommand;
import com.iwaa.common.util.commands.SignUpCommand;
import com.iwaa.common.util.commands.Update;
import com.iwaa.common.util.db.DBWorker;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.NetworkListener;
import com.iwaa.common.util.network.Request;
import com.iwaa.common.util.state.State;
import com.iwaa.common.util.utils.DataParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class CommandAdmin {

    private static final int HISTORY_LENGTH = 12;
    private final Queue<String> commandHistory = new ConcurrentLinkedQueue<>();
    private final Map<String, AbstractCommand> clientCommands = new HashMap<>();
    private final Map<String, AbstractCommand> serverCommands = new HashMap<>();
    private final Map<String, AbstractCommand> commandsSendingWithoutSending = new HashMap<>();
    private final Map<String, AbstractCommand> commandsWithoutAuth = new HashMap<>();
    private final State state;
    private NetworkListener networkListener;
    private CollectionAdmin collectionAdmin;
    private DBWorker dbWorker;
    private CommandListener commandListener;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public CommandAdmin(CollectionAdmin collectionAdmin, DBWorker dbWorker, State state) {
        this.state = state;
        this.collectionAdmin = collectionAdmin;
        this.dbWorker = dbWorker;
    }

    public CommandAdmin(NetworkListener networkListener, State state) {
        this.state = state;
        this.networkListener = networkListener;
    }

    {
        serverCommands.put("exit", new Exit(this));

        commandsSendingWithoutSending.putAll(serverCommands);
        commandsSendingWithoutSending.put("execute_script", new ExecuteScript(this));

        commandsWithoutAuth.putAll(serverCommands);
        commandsWithoutAuth.put("sign_in", new SignInCommand(this));
        commandsWithoutAuth.put("sign_up", new SignUpCommand(this));
        commandsWithoutAuth.put("help", new Help(this));

        clientCommands.putAll(serverCommands);
        clientCommands.putAll(commandsSendingWithoutSending);
        clientCommands.putAll(commandsWithoutAuth);
        clientCommands.put("add", new Add(this));
        clientCommands.put("add_if_max", new AddIfMax(this));
        clientCommands.put("clear", new Clear(this));
        clientCommands.put("history", new History(this));
        clientCommands.put("filter_less_than_distance", new Filter(this));
        clientCommands.put("remove_lower", new RemoveLower(this));
        clientCommands.put("info", new Info(this));
        clientCommands.put("print_field_descending_distance", new PrintField(this));
        clientCommands.put("remove_by_id", new Remove(this));
        clientCommands.put("update", new Update(this));
        clientCommands.put("group_counting_by_distance", new GroupCounting(this));
        clientCommands.put("show", new Show(this));
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public State getPerformanceState() {
        return state;
    }

    public synchronized Map<String, AbstractCommand> getCommandsWithoutAuth() {
        return commandsWithoutAuth;
    }

    public DBWorker getDBWorker() {
        return dbWorker;
    }

    public CollectionAdmin getCollectionManager() {
        return collectionAdmin;
    }

    public NetworkListener getNetworkListener() {
        return networkListener;
    }

    public Map<String, AbstractCommand> getCommands() {
        return clientCommands;
    }

    public Queue<String> getCommandHistory() {
        return commandHistory;
    }

    public void addCommandToHistory(String commandName) {
        Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            commandHistory.add(commandName);
            if (commandHistory.size() > HISTORY_LENGTH) {
                commandHistory.poll();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public CommandResult onCommandReceived(String inputData, boolean fromClient, User user) {
        Map<String, AbstractCommand> commands = serverCommands;
        if (fromClient) {
            commands = clientCommands;
        }
        String[] commandWithRawArgs = DataParser.normalize(inputData);
        String commandName = commandWithRawArgs[0].toLowerCase(Locale.ROOT);
        String[] rawArgs = Arrays.copyOfRange(commandWithRawArgs, 1, commandWithRawArgs.length);
        if (commands.containsKey(commandName)) {
            AbstractCommand command = commands.get(commandName);
            try {
                return processCommand(command, rawArgs, user);
            } catch (EndOfStreamException e) {
                state.switchPerformanceStatus();
            }
        }
        return new CommandResult("No such command, call \"help\" to see the list of commands.");
    }

    public CommandResult processCommand(AbstractCommand command, String[] rawArgs, User user) throws EndOfStreamException {
        if (rawArgs.length == command.getInlineArgsCount()) {
            Object[] rawArgsAndUser = new Object[rawArgs.length + 1];
            System.arraycopy(rawArgs, 0, rawArgsAndUser, 0, rawArgs.length);
            rawArgsAndUser[rawArgsAndUser.length - 1] = user;

            Object[] commandArgs = command.readArgs(rawArgsAndUser);
            if (commandArgs != null) {
                if (commandsSendingWithoutSending.containsKey(command.getName())) {
                    return executeCommand(command, commandArgs);
                } else {
                    return networkListener.listen(new Request(command, commandArgs));
                }
            }
        } else {
            return new CommandResult("Wrong number of arguments.");
        }
        return null;
    }

    public CommandResult executeCommand(AbstractCommand command, Object[] args) {
        addCommandToHistory(command.getName());
        return command.execute(args);
    }
}
