package com.iwaa.client;

import com.iwaa.client.gui.authorize.ConnectionFrame;
import com.iwaa.client.local.Local;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.state.State;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class ConnectionHandler {

    private final State state;
    private Socket socket;
    private boolean isOpen = false;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectionHandler(State state) {
        this.state = state;
    }

    public void startConnection(CommandListener commandListener) {
        ConnectionFrame connectionFrame = new ConnectionFrame(Local.getResourceBundleDefault());
        connectionFrame.startConnection(this, commandListener);
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public int startConnect(String address, int port) {
        try {
            socket = new Socket(address, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            isOpen = true;
            System.out.println("Connected.");
            return 1;
        } catch (IOException | IllegalArgumentException e) {
            isOpen = false;
            System.out.println("Invalid host or port");
            return -1;
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            isOpen = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
