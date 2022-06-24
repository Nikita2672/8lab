package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.gui.constants.FontConstants;
import com.iwaa.client.gui.Table.RouteTableModel;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;

public class AddFrame extends AbstractFrame {

    private final JLabel descriptionLabel = new JLabel(localisation(Constants.DESCRIPTION_ADD));
    private final JFrame addFrame = new JFrame(localisation(Constants.ADD_WINDOW));
    private final JButton addButton = new JButton(localisation(Constants.ADD_BUTTON));
    private final JButton addIfMaxButton = new JButton(localisation(Constants.ADD_IF_MAX_BUTTON));

    {
        descriptionLabel.setFont(FontConstants.BUTTON_FONT);
        addFrame.setLayout(new GridBagLayout());
        addFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        addFrame.setLocationRelativeTo(null);
        addButton.setFont(FontConstants.BUTTON_FONT);
        addButton.setBackground(Color.orange);
        addIfMaxButton.setFont(FontConstants.BUTTON_FONT);
        addIfMaxButton.setBackground(Color.orange);
        addFrame.add(descriptionLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addFrame.add(addButton, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addFrame.add(addIfMaxButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addFrame.setResizable(false);
        addFrame.pack();
    }

    public AddFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startAdd(CommandListener commandListener, RouteTableModel routeTableModel, JPanel panel, JFrame frame,
                         JScrollPane jScrollPane, RouteTable routeTable) {
        addFrame.setVisible(true);
        addButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "add", routeTableModel, panel, frame, jScrollPane, routeTable);
            addFrame.dispose();
        });
        addIfMaxButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "add_if_max", routeTableModel, panel, frame, jScrollPane, routeTable);
            addFrame.dispose();
        });
    }

    @Override
    public void repaintForLanguage() {

    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
