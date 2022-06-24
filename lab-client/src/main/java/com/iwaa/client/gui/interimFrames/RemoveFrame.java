package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.constants.FontConstants;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.gui.Table.RouteTableModel;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    private final JFrame removeFrame = new JFrame(localisation(Constants.REMOVE));
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
        removeFrame.setLocationRelativeTo(null);

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
        removeFrame.setLayout(new GridBagLayout());
        removeFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        removeFrame.setLocationRelativeTo(null);
        clearButton.setFont(FontConstants.BUTTON_FONT);
        clearButton.setBackground(Color.orange);
        removeButton.setFont(FontConstants.BUTTON_FONT);
        removeButton.setBackground(Color.orange);
        removeLowerButton.setFont(FontConstants.BUTTON_FONT);
        removeLowerButton.setBackground(Color.orange);
        removeFrame.add(descriptionLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeFrame.add(removeButton, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeFrame.add(removeLowerButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeFrame.add(clearButton, new GridBagConstraints(0, THREE, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        removeFrame.setResizable(false);
        removeFrame.pack();
    }

    public RemoveFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startRemove(CommandListener commandListener, RouteTableModel routeTableModel, JPanel panel, JFrame jFrame,
                            JScrollPane jScrollPane, RouteTable routeTable) {
        removeFrame.setVisible(true);
        addRemoveListener(commandListener, routeTableModel, panel, jFrame, jScrollPane, routeTable);
        addClearListener(commandListener, routeTableModel, panel, jFrame, jScrollPane, routeTable);
        addRemoveLowerListener(commandListener, routeTableModel, panel, jFrame, jScrollPane, routeTable);
    }

    private void addRemoveListener(CommandListener commandListener, RouteTableModel routeTableModel, JPanel jPanel,
                                   JFrame jFrame, JScrollPane jScrollPane, RouteTable routeTable) {
        removeButton.addActionListener(e -> {
            removeCommandFrame.setVisible(true);
            addSendRemoveListener(commandListener, routeTableModel, jPanel, jFrame, jScrollPane, routeTable);
            routeTable.reload();
            removeFrame.dispose();
        });
    }

    private void addRemoveLowerListener(CommandListener commandListener, RouteTableModel routeTableModel, JPanel jPanel,
                                        JFrame jFrame, JScrollPane jScrollPane, RouteTable routeTable) {
        removeLowerButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "remove_lower", routeTableModel, jPanel,
                    jFrame, jScrollPane, routeTable);
            routeTable.reload();
            removeFrame.dispose();
        });
    }

    private void addClearListener(CommandListener commandListener, RouteTableModel routeTableModel, JPanel jPanel,
                                  JFrame jFrame, JScrollPane jScrollPane, RouteTable routeTable) {
        clearButton.addActionListener(e -> {
            commandListener.runClear();
            routeTable.reload();
            removeFrame.dispose();
        });
    }

    private void addSendRemoveListener(CommandListener commandListener, RouteTableModel routeTableModel, JPanel jPanel,
                                       JFrame jFrame, JScrollPane jScrollPane, RouteTable routeTable) {
        sendRemoveButton.addActionListener(e -> {
            try {
                Long id = Long.parseLong(removeField.getText());
                CommandResult commandResult = commandListener.runRemove(id);
                if (!commandResult.getMessage().equals("Removed successfully!")) {
                    showMistake(mistakeRemoveLabel1);
                } else {
                    removeCommandFrame.dispose();
                    RouteTable.initTable(commandListener, routeTableModel);
                    routeTable.reload();
                }
            } catch (NumberFormatException ex) {
                showMistake(mistakeRemoveLabel);
            }
        });
    }

    private void showMistake(JLabel label) {
        removeCommandFrame.add(label, new GridBagConstraints(0, 1, 1, 1, 1, 1,
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
