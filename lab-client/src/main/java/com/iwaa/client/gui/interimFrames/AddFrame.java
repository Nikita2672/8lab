package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.gui.constants.FontConstants;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
    private final JFrame addMainFrame = new JFrame(localisation(Constants.ADD_WINDOW));
    private final JButton addButton = new JButton(localisation(Constants.ADD_BUTTON));
    private final JButton addIfMaxButton = new JButton(localisation(Constants.ADD_IF_MAX_BUTTON));

    {
        descriptionLabel.setFont(FontConstants.BUTTON_FONT);
        addMainFrame.setLayout(new GridBagLayout());
        addMainFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        addMainFrame.setLocationRelativeTo(null);
        addButton.setFont(FontConstants.BUTTON_FONT);
        addButton.setBackground(Color.orange);
        addIfMaxButton.setFont(FontConstants.BUTTON_FONT);
        addIfMaxButton.setBackground(Color.orange);
        addMainFrame.add(descriptionLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addMainFrame.add(addButton, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addMainFrame.add(addIfMaxButton, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        addMainFrame.setResizable(false);
        addMainFrame.pack();
    }

    public AddFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startAdd(CommandListener commandListener, RouteTable routeTable) {
        addMainFrame.setVisible(true);
        addButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "add", routeTable);
            addMainFrame.dispose();
        });
        addIfMaxButton.addActionListener(e -> {
            InsertFrame insertFrame = new InsertFrame(getResourceBundle());
            insertFrame.startInsertFrame(commandListener, "add_if_max", routeTable);
            addMainFrame.dispose();
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
