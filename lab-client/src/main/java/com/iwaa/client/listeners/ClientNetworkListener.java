package com.iwaa.client.listeners;

import com.iwaa.client.ClientAdmin;
import com.iwaa.client.ConnectionHandler;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.NetworkListener;
import com.iwaa.common.util.network.Request;
import com.iwaa.common.util.network.Serializer;

import java.io.IOException;

public final class ClientNetworkListener implements NetworkListener {

    private static final int TIMEOUT = 10000;
    private final ConnectionHandler connectionHandler;

    public ClientNetworkListener(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public CommandResult listen(Request request) {
        CommandResult commandResult = null;
        if (!connectionHandler.isOpen()) {
            connectionHandler.openConnection();
        }
        if (connectionHandler.isOpen()) {
            try {
                Serializer serializer = new Serializer();
                ClientAdmin clientAdmin = new ClientAdmin(serializer);
                clientAdmin.send(request, connectionHandler.getOutputStream());
                connectionHandler.getSocket().setSoTimeout(TIMEOUT);
                commandResult = clientAdmin.receive(connectionHandler.getInputStream(), connectionHandler.getSocket().getReceiveBufferSize());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                connectionHandler.close();
            }
        }
        return commandResult;
    }
}
