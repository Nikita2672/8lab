package com.iwaa.client.gui.authorize;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.gui.interimFrames.FabricOfComponents;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPasswordField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.GridCoordinates.THREE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;


public class UserFrame extends AbstractFrame {

    private final JTextField loginField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private CommandListener commandListener;
    private JFrame userMainFrame;
    private final JPanel userMainPanel = new JPanel();

    {
        loginField.setFont(FIELD_FONT);
        passwordField.setFont(FIELD_FONT);
    }

    public UserFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startLogin(CommandListener listener) {
        this.commandListener = listener;
        this.userMainFrame = new JFrame(localisation(Constants.LOGIN_WINDOW));
        userMainFrame.setSize(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE);
        userMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userMainFrame.setLocationRelativeTo(null);
        userMainFrame.setLayout(new BorderLayout());
        createPanel(userMainPanel, userMainFrame);
        userMainFrame.add(userMainPanel, BorderLayout.CENTER);
        userMainFrame.pack();
        userMainFrame.setResizable(false);
        userMainFrame.setVisible(true);
    }

    private void addSignUpListener(JButton signUpButton) {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = passwordField.getText();
                CommandResult result = commandListener.runUser(login, password, "sign_up");
                if (result.isSuccess()) {
                    userMainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    userMainFrame.dispose();
                    RouteTable routeTable = new RouteTable(getResourceBundle());
                    routeTable.startTable(commandListener, login);
                } else {
                    JLabel mistakeLoginLabel = new JLabel(localisation(Constants.MISTAKE_LOGIN));
                    mistakeLoginLabel.setFont(MISTAKE_FONT);
                    mistakeLoginLabel.setForeground(Color.RED);
                    showMistake(userMainFrame, userMainPanel, mistakeLoginLabel);
                }
            }
        });
    }

    private void addSignInListener(JButton signInButton) {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String login = loginField.getText();
                    String password = passwordField.getText();
                    CommandResult result = commandListener.runUser(login, password, "sign_in");
                    if (result.isSuccess()) {
                        userMainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        userMainFrame.dispose();
                        RouteTable routeTable = new RouteTable(getResourceBundle());
                        routeTable.startTable(commandListener, login);
                    } else {
                        JLabel mistakeUserLabel = new JLabel(localisation(Constants.MISTAKE_USER));
                        mistakeUserLabel.setFont(MISTAKE_FONT);
                        mistakeUserLabel.setForeground(Color.RED);
                        showMistake(userMainFrame, userMainPanel, mistakeUserLabel);
                    }
                } catch (NullPointerException exception) {
                    JLabel mistakeServerLabel = new JLabel("server don't answer");
                    mistakeServerLabel.setFont(MISTAKE_FONT);
                    mistakeServerLabel.setBackground(Color.RED);
                    showMistake(userMainFrame, userMainPanel, mistakeServerLabel);
                }
            }
        });
    }

    private void createPanel(JPanel reloadedPanel, JFrame reloadedFrame) {
        reloadedPanel.removeAll();
        reloadedPanel.revalidate();
        JButton signInButton = new JButton(localisation(Constants.SIGN_IN_BUTTON));
        JButton signUpButton = new JButton(localisation(Constants.SIGN_UP_BUTTON));
        JLabel loginLabel = new JLabel(localisation(Constants.LOGIN));
        JLabel passwordLabel = new JLabel(localisation(Constants.PASSWORD));
        JLabel infoLabel = new JLabel(localisation(Constants.INFO));
        loginLabel.setFont(BUTTON_FONT);
        passwordLabel.setFont(BUTTON_FONT);
        signInButton.setFont(BUTTON_FONT);
        signUpButton.setFont(BUTTON_FONT);
        infoLabel.setFont(MISTAKE_FONT);
        infoLabel.setForeground(Color.BLUE);
        addSignInListener(signInButton);
        addSignUpListener(signUpButton);
        reloadedPanel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(reloadedPanel, loginLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, passwordLabel, 0, 1);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, loginField, 1, 0);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, passwordField, 1, 1);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, signInButton, 1, 2);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, signUpButton, 1, THREE);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, infoLabel, 0, THREE);
        JMenuBar lang = createLanguage(Color.BLACK);
        FabricOfComponents.setLocationOfComponent(reloadedPanel, lang, 2, 0);
        reloadedFrame.repaint();
    }

    private void showMistake(Frame frameWithMistake, JPanel panelWithMistake, JLabel mistakeLabel) {
        FabricOfComponents.setLocationOfComponent(panelWithMistake, mistakeLabel, 0, 2);
        frameWithMistake.add(panelWithMistake, BorderLayout.CENTER);
        frameWithMistake.setVisible(true);
    }

    @Override
    public void repaintForLanguage() {
        createPanel(userMainPanel, userMainFrame);
    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
