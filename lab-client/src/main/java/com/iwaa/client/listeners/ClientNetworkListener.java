package com.iwaa.client.listeners;

import com.iwaa.client.ClientAdmin;
import com.iwaa.client.ConnectionHandler;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.NetworkListener;
import com.iwaa.common.util.network.Request;
import com.iwaa.common.util.network.Serializer;


import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

import java.awt.event.WindowEvent;
import java.io.IOException;

import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;

public final class ClientNetworkListener implements NetworkListener {

    private static final int TIMEOUT = 10000;
    private static final int TIMEOUT_FOR_EXIT = 2000;
    private final ConnectionHandler connectionHandler;

    public ClientNetworkListener(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public CommandResult listen(Request request) {
        CommandResult commandResult = null;
        if (connectionHandler.isOpen()) {
            try {
                Serializer serializer = new Serializer();
                ClientAdmin clientAdmin = new ClientAdmin(serializer);
                clientAdmin.send(request, connectionHandler.getOutputStream());
                connectionHandler.getSocket().setSoTimeout(TIMEOUT);
                commandResult = clientAdmin.receive(connectionHandler.getInputStream(), connectionHandler.getSocket().getReceiveBufferSize());
            } catch (IOException e) {
                JFrame connectionReset = new JFrame();
                connectionReset.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
                JLabel connectionResetLabel = new JLabel("Connection reset by peer");
                connectionResetLabel.setFont(MISTAKE_FONT);
                connectionResetLabel.setForeground(Color.RED);
                connectionReset.setLayout(new BorderLayout());
                connectionReset.add(connectionResetLabel, BorderLayout.CENTER);
                connectionReset.pack();
                connectionReset.setResizable(false);
                connectionReset.setLocationRelativeTo(null);
                connectionReset.setVisible(true);
                try {
                    Thread.sleep(TIMEOUT_FOR_EXIT);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                connectionReset.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                connectionHandler.close();
                connectionReset.dispatchEvent(new WindowEvent(connectionReset, WindowEvent.WINDOW_CLOSING));
            }
        }
        return commandResult;
    }

    private void close() {

    }
}
