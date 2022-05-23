package com.iwaa.client;

import com.iwaa.client.listeners.ClientCommandListener;
import com.iwaa.client.listeners.ClientNetworkListener;
import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.state.State;

public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            State state = new State();
            ConnectionHandler connectionHandler = new ConnectionHandler(state);
            ClientNetworkListener clientListener = new ClientNetworkListener(connectionHandler);
            CommandAdmin commandAdmin = new CommandAdmin(clientListener, state);
            ClientCommandListener commandListener = new ClientCommandListener(commandAdmin);
            commandAdmin.setCommandListener(commandListener);
            System.out.println("Hello!");
            connectionHandler.openConnection();
            commandListener.launch();
            connectionHandler.close();
        } catch (NullPointerException e) {
            System.out.println("Good bye");
        }
    }
}
