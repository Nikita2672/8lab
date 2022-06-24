package com.iwaa.client.gui.authorize;

import com.iwaa.client.ConnectionHandler;
import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.interimFrames.FabricOfComponents;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;

import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;


public class ConnectionFrame extends AbstractFrame {

    private final JTextField hostField = new JTextField(20);
    private final JTextField portField = new JTextField(20);
    private ConnectionHandler connectionHandler;
    private CommandListener commandListener;
    private JFrame connectionFrame;
    private JPanel connectionMainPanel = new JPanel();

    {
        hostField.setFont(FIELD_FONT);
        portField.setFont(FIELD_FONT);
    }

    public ConnectionFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startConnection(ConnectionHandler handler, CommandListener listener) {
        this.connectionHandler = handler;
        this.commandListener = listener;
        paintFrame();
    }

    private void paintFrame() {
        this.connectionFrame = new JFrame(localisation(Constants.CONNECT_WINDOW));
        connectionFrame.setSize(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE);
        connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionFrame.setLocationRelativeTo(null);
        connectionFrame.setLayout(new BorderLayout());
        createPanel(connectionMainPanel, connectionFrame);
        connectionFrame.add(connectionMainPanel, BorderLayout.CENTER);
        connectionFrame.pack();
        connectionFrame.setResizable(false);
        connectionFrame.setVisible(true);
    }

    private void addListener(JButton connectButton) {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = hostField.getText();
                Integer port = 0;
                try {
                    port = Integer.parseInt(portField.getText());
                    if (connectionHandler.startConnect(host, port) > 0) {
                        connectionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        connectionFrame.dispose();
                        UserFrame userFrame = new UserFrame(getResourceBundle());
                        userFrame.startLogin(commandListener);
                    } else {
                        showMistake(connectionFrame, connectionMainPanel);
                    }
                } catch (NumberFormatException exception) {
                    showMistake(connectionFrame, connectionMainPanel);
                }
            }
        });
    }

    private void showMistake(Frame frame1, JPanel mistakeLabel) {
        JLabel mistakeConnectionLabel = new JLabel(localisation(Constants.MISTAKE_CONNECTION));
        mistakeConnectionLabel.setFont(MISTAKE_FONT);
        mistakeConnectionLabel.setForeground(Color.RED);
        mistakeLabel.add(mistakeConnectionLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        frame1.add(mistakeLabel, BorderLayout.CENTER);
        frame1.setVisible(true);
    }

    private void createPanel(JPanel reloadedPanel, JFrame reloadedFrame) {
        reloadedPanel.removeAll();
        reloadedPanel.revalidate();
        reloadedFrame.setTitle(localisation(Constants.CONNECT_WINDOW));
        JButton connectButton = new JButton(localisation(Constants.CONNECT_BUTTON));
        addListener(connectButton);
        JLabel hostLabel = new JLabel(localisation(Constants.HOST));
        JLabel portLabel = new JLabel(localisation(Constants.PORT));
        connectButton.setFont(BUTTON_FONT);
        hostLabel.setFont(BUTTON_FONT);
        portLabel.setFont(BUTTON_FONT);
        reloadedPanel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(reloadedPanel, hostLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, portLabel, 0, 1);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, hostField, 1, 0);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, portField, 1, 1);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, connectButton, 1, 2);
        JMenuBar lang = createLanguage(Color.BLACK);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, lang, 2, 0);
        reloadedFrame.repaint();
    }

    @Override
    public void repaintForLanguage() {
        createPanel(connectionMainPanel, connectionFrame);
    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
