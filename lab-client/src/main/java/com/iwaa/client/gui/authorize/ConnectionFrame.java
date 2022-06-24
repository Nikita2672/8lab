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
    private JFrame frame;
    private JPanel panel = new JPanel();

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
        this.frame = new JFrame(localisation(Constants.CONNECT_WINDOW));
        frame.setSize(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        createPanel(panel, frame);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
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
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        frame.dispose();
                        UserFrame userFrame = new UserFrame(getResourceBundle());
                        userFrame.startLogin(commandListener);
                    } else {
                        showMistake(frame, panel);
                    }
                } catch (NumberFormatException exception) {
                    showMistake(frame, panel);
                }
            }
        });
    }

    private void showMistake(Frame frame1, JPanel jPanel) {
        JLabel mistakeConnectionLabel = new JLabel(localisation(Constants.MISTAKE_CONNECTION));
        mistakeConnectionLabel.setFont(MISTAKE_FONT);
        mistakeConnectionLabel.setForeground(Color.RED);
        jPanel.add(mistakeConnectionLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        frame1.add(jPanel, BorderLayout.CENTER);
        frame1.setVisible(true);
    }

    private void createPanel(JPanel jPanel, JFrame jFrame) {
        jPanel.removeAll();
        jPanel.revalidate();
        jFrame.setTitle(localisation(Constants.CONNECT_WINDOW));
        JButton connectButton = new JButton(localisation(Constants.CONNECT_BUTTON));
        addListener(connectButton);
        JLabel hostLabel = new JLabel(localisation(Constants.HOST));
        JLabel portLabel = new JLabel(localisation(Constants.PORT));
        connectButton.setFont(BUTTON_FONT);
        hostLabel.setFont(BUTTON_FONT);
        portLabel.setFont(BUTTON_FONT);
        jPanel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(jPanel, hostLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(jPanel, portLabel, 0, 1);
        FabricOfComponents.setLocationOfComponent(jPanel, hostField, 1, 0);
        FabricOfComponents.setLocationOfComponent(jPanel, portField, 1, 1);
        FabricOfComponents.setLocationOfComponent(jPanel, connectButton, 1, 2);
        JMenuBar lang = createLanguage(Color.BLACK);
        FabricOfComponents.setLocationOfComponent(jPanel, lang, 2, 0);
        jFrame.repaint();
    }

    @Override
    public void repaintForLanguage() {
        createPanel(panel, frame);
    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
