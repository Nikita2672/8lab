package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.constants.FontConstants;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.GridCoordinates.THREE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;

public class RemoveFrame extends AbstractFrame {

    private final JLabel descriptionLabel = new JLabel(localisation(Constants.REMOVE_DESCRIPTION));
    private final JFrame removeMainFrame = new JFrame(localisation(Constants.REMOVE));
    private final JFrame removeCommandFrame = new JFrame(localisation(Constants.REMOVE_BY_ID));

    private final JButton sendRemoveButton = new JButton(localisation(Constants.SEND));
    private final JTextField removeField = new JTextField(20);
    private final JLabel removeLabel = new JLabel(localisation(Constants.REMOVE_INFO));
    private final JLabel mistakeRemoveLabel = new JLabel(localisation(Constants.MISTAKE_REMOVE_INFO));
    private final JLabel mistakeRemoveLabel1 = new JLabel(localisation(Constants.REMOVE_MISTAKE));

    private final JButton removeButton = new JButton(localisation(Constants.REMOVE));
    private final JButton removeLowerButton = new JButton(localisation(Constants.REMOVE_LOWER));
    private final JButton clearButton = new JButton(localisation(Constants.CLEAR));


    {
        mistakeRemoveLabel.setFont(FontConstants.MISTAKE_FONT);
        mistakeRemoveLabel.setForeground(Color.RED);

        mistakeRemoveLabel1.setFont(FontConstants.MISTAKE_FONT);
        mistakeRemoveLabel1.setForeground(Color.RED);

        removeLabel.setFont(FontConstants.BUTTON_FONT);
        removeField.setFont(FontConstants.FIELD_FONT);
        sendRemoveButton.setFont(FontConstants.BUTTON_FONT);
        sendRemoveButton.setBackground(Color.orange);
        removeCommandFrame.setLayout(new GridBagLayout());
        removeMainFrame.setLocationRelativeTo(null);

        removeCommandFrame.add(removeLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeCommandFrame.add(removeField, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeCommandFrame.add(sendRemoveButton, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeCommandFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        removeCommandFrame.pack();

        descriptionLabel.setFont(FontConstants.BUTTON_FONT);
        removeMainFrame.setLayout(new GridBagLayout());
        removeMainFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        removeMainFrame.setLocationRelativeTo(null);
        clearButton.setFont(FontConstants.BUTTON_FONT);
        clearButton.setBackground(Color.orange);
        removeButton.setFont(FontConstants.BUTTON_FONT);
        removeButton.setBackground(Color.orange);
        removeLowerButton.setFont(FontConstants.BUTTON_FONT);
        removeLowerButton.setBackground(Color.orange);

        removeMainFrame.add(descriptionLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeMainFrame.add(removeButton, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeMainFrame.add(removeLowerButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeMainFrame.add(clearButton, new GridBagConstraints(0, THREE, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeMainFrame.setResizable(false);
        removeMainFrame.pack();
    }

    public RemoveFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startRemove(CommandListener commandListener, RouteTable routeTable) {
        removeMainFrame.setVisible(true);
        addRemoveListener(commandListener, routeTable);
        addClearListener(commandListener, routeTable);
        addRemoveLowerListener(commandListener, routeTable);
    }

    private void addRemoveListener(CommandListener commandListener, RouteTable routeTable) {
        removeButton.addActionListener(e -> {
            removeCommandFrame.setVisible(true);
            addSendRemoveListener(commandListener, routeTable);
            routeTable.reload();
            removeMainFrame.dispose();
        });
    }

    private void addRemoveLowerListener(CommandListener commandListener, RouteTable routeTable) {
        removeLowerButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "remove_lower", routeTable);
            routeTable.reload();
            removeMainFrame.dispose();
        });
    }

    private void addClearListener(CommandListener commandListener, RouteTable routeTable) {
        clearButton.addActionListener(e -> {
            commandListener.runClear();
            routeTable.reload();
            removeMainFrame.dispose();
        });
    }

    private void addSendRemoveListener(CommandListener commandListener, RouteTable routeTable) {
        sendRemoveButton.addActionListener(e -> {
            try {
                Long id = Long.parseLong(removeField.getText());
                CommandResult commandResult = commandListener.runRemove(id);
                if (!commandResult.isSuccess()) {
                    showMistake(mistakeRemoveLabel1);
                } else {
                    removeCommandFrame.dispose();
                    RouteTable.initTable(commandListener, routeTable.getRouteTableModel());
                    routeTable.reload();
                }
            } catch (NumberFormatException ex) {
                showMistake(mistakeRemoveLabel);
            }
        });
    }

    private void showMistake(JLabel mistakeLabel) {
        removeCommandFrame.add(mistakeLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeCommandFrame.setVisible(true);
    }

    @Override
    public void repaintForLanguage() {

    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
