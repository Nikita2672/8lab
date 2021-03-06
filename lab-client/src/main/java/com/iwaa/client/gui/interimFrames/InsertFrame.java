package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;

import static com.iwaa.client.gui.constants.GridCoordinates.EIGHT;
import static com.iwaa.client.gui.constants.GridCoordinates.ELEVEN;
import static com.iwaa.client.gui.constants.GridCoordinates.FIVE;
import static com.iwaa.client.gui.constants.GridCoordinates.FOUR;
import static com.iwaa.client.gui.constants.GridCoordinates.NINE;
import static com.iwaa.client.gui.constants.GridCoordinates.SEVEN;
import static com.iwaa.client.gui.constants.GridCoordinates.SIX;
import static com.iwaa.client.gui.constants.GridCoordinates.TEN;
import static com.iwaa.client.gui.constants.GridCoordinates.THIRTEEN;
import static com.iwaa.client.gui.constants.GridCoordinates.THREE;
import static com.iwaa.client.gui.constants.GridCoordinates.TWELVE;

import static com.iwaa.client.gui.constants.SizeConstants.INSERT_SIZE;

import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;
import static com.iwaa.client.gui.interimFrames.FabricOfComponents.setLocationOfComponent;

public class InsertFrame extends AbstractFrame {

    private static final Long MIN_DISTANCE = 1L;
    private static final int MAX_COORDINATE_X = 245;
    private static final int MAX_COORDINATE_Y = 362;

    private final JFrame insertMainFrame = new JFrame(localisation(Constants.INSERT));
    private final JPanel insertPanel = new JPanel();

    private final JTextField nameField = new JTextField(20);
    private final JTextField distanceField = new JTextField(20);

    private final JTextField coordinateXField = new JTextField(20);
    private final JTextField coordinateYField = new JTextField(20);

    private final JTextField fromXField = new JTextField(20);
    private final JTextField fromYField = new JTextField(20);
    private final JTextField fromZField = new JTextField(20);

    private final JTextField toXField = new JTextField(20);
    private final JTextField toYField = new JTextField(20);
    private final JTextField toZField = new JTextField(20);

    private final JLabel nameLabel = new JLabel(localisation(Constants.ENTER_NAME));
    private final JLabel distanceLabel = new JLabel(localisation(Constants.ENTER_DISTANCE));

    private final JLabel coordinateLabel = new JLabel(localisation(Constants.ENTER_COORDINATES));
    private final JLabel coordinateXLabel = new JLabel(localisation(Constants.COORDINATE_X));
    private final JLabel coordinateYLabel = new JLabel(localisation(Constants.COORDINATE_Y));

    private final JLabel locationFromLabel = new JLabel(localisation(Constants.LOCATION_FROM));
    private final JLabel fromXLabel = new JLabel(localisation(Constants.X));
    private final JLabel fromYLabel = new JLabel(localisation(Constants.Y));
    private final JLabel fromZLabel = new JLabel(localisation(Constants.Z));

    private final JLabel locationToLabel = new JLabel(localisation(Constants.ENTER_LOCATION_TO));
    private final JLabel toXLabel = new JLabel(localisation(Constants.X));
    private final JLabel toYLabel = new JLabel(localisation(Constants.Y));
    private final JLabel toZLabel = new JLabel(localisation(Constants.Z));

    private final JLabel mistake = new JLabel(localisation(Constants.MISTAKE_DATA));

    private final JLabel notMax = new JLabel(localisation(Constants.NIT_MAX));
    private final JLabel success = new JLabel(localisation(Constants.SUCCESS));

    private final JButton send = new JButton(localisation(Constants.SEND));

    {
        insertMainFrame.setLayout(new BorderLayout());
        insertMainFrame.setSize(new Dimension(INSERT_SIZE, INSERT_SIZE));
        insertMainFrame.setLocationRelativeTo(null);

        notMax.setFont(BUTTON_FONT);
        success.setFont(BUTTON_FONT);

        send.setFont(BUTTON_FONT);
        send.setBackground(Color.orange);

        mistake.setFont(MISTAKE_FONT);
        mistake.setForeground(Color.RED);

        nameField.setFont(FIELD_FONT);
        distanceField.setFont(FIELD_FONT);

        coordinateXField.setFont(FIELD_FONT);
        coordinateYField.setFont(FIELD_FONT);

        fromXField.setFont(FIELD_FONT);
        fromYField.setFont(FIELD_FONT);
        fromZField.setFont(FIELD_FONT);

        toXField.setFont(FIELD_FONT);
        toYField.setFont(FIELD_FONT);
        toZField.setFont(FIELD_FONT);

        nameLabel.setFont(BUTTON_FONT);
        distanceLabel.setFont(BUTTON_FONT);

        coordinateLabel.setFont(BUTTON_FONT);
        coordinateXLabel.setFont(BUTTON_FONT);
        coordinateYLabel.setFont(BUTTON_FONT);

        locationFromLabel.setFont(BUTTON_FONT);
        fromXLabel.setFont(BUTTON_FONT);
        fromYLabel.setFont(BUTTON_FONT);
        fromZLabel.setFont(BUTTON_FONT);

        locationToLabel.setFont(BUTTON_FONT);
        toXLabel.setFont(BUTTON_FONT);
        toYLabel.setFont(BUTTON_FONT);
        toZLabel.setFont(BUTTON_FONT);

        insertPanel.setLayout(new GridBagLayout());
        setLocationOfComponent(insertPanel, nameLabel, 0, 0);
        setLocationOfComponent(insertPanel, nameField, 1, 0);
        setLocationOfComponent(insertPanel, distanceLabel, 0, 1);
        setLocationOfComponent(insertPanel, distanceField, 1, 1);
        setLocationOfComponent(insertPanel, coordinateLabel, 1, 2);
        setLocationOfComponent(insertPanel, coordinateXLabel, 0, THREE);
        setLocationOfComponent(insertPanel, coordinateXField, 1, THREE);
        setLocationOfComponent(insertPanel, coordinateYLabel, 0, FOUR);
        setLocationOfComponent(insertPanel, coordinateYField, 1, FOUR);
        setLocationOfComponent(insertPanel, locationFromLabel, 1, FIVE);
        setLocationOfComponent(insertPanel, fromXLabel, 0, SIX);
        setLocationOfComponent(insertPanel, fromXField, 1, SIX);
        setLocationOfComponent(insertPanel, fromYLabel, 0, SEVEN);
        setLocationOfComponent(insertPanel, fromYField, 1, SEVEN);
        setLocationOfComponent(insertPanel, fromZLabel, 0, EIGHT);
        setLocationOfComponent(insertPanel, fromZField, 1, EIGHT);
        setLocationOfComponent(insertPanel, locationToLabel, 1, NINE);
        setLocationOfComponent(insertPanel, toXLabel, 0, TEN);
        setLocationOfComponent(insertPanel, toXField, 1, TEN);
        setLocationOfComponent(insertPanel, toYLabel, 0, ELEVEN);
        setLocationOfComponent(insertPanel, toYField, 1, ELEVEN);
        setLocationOfComponent(insertPanel, toZLabel, 0, TWELVE);
        setLocationOfComponent(insertPanel, toZField, 1, TWELVE);
        setLocationOfComponent(insertPanel, send, 1, THIRTEEN);

        insertMainFrame.add(insertPanel);
        insertMainFrame.pack();
        insertMainFrame.setResizable(false);
    }

    public InsertFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    private boolean checkName(String name) {
        if (name == null) {
            return false;
        }
        return name.length() > 0;
    }

    private boolean checkDistance(String distance) {
        try {
            Long res = Long.parseLong(distance);
            return res > MIN_DISTANCE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkCoordinate(String x, int max) {
        try {
            int res = Integer.parseInt(x);
            return res <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkLocationInt(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkLocationLong(String x) {
        try {
            Long.parseLong(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void startInsertFrame(CommandListener commandListener, String command, RouteTable routeTable) {
        insertMainFrame.setTitle(command);
        insertMainFrame.setVisible(true);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = checkResult();
                if (!result) {
                    insertPanel.add(mistake, new GridBagConstraints(0, THIRTEEN, 1, 1, 1, 1,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(2, 2, 2, 2), 0, 0));
                    insertMainFrame.add(insertPanel);
                    insertMainFrame.setVisible(true);
                } else {
                    if ("add".equals(command)) {
                        doAdd(commandListener, routeTable);
                    }
                    if ("add_if_max".equals(command)) {
                        doAddIfMax(commandListener, routeTable);
                    }
                    if ("remove_lower".equals(command)) {
                        doRemoveLower(commandListener, routeTable);
                    }
                    if ("update".equals(command.split(" ")[0])) {
                        doUpdate(commandListener, command, routeTable);
                    }
                }
            }
        });
    }

    private void doUpdate(CommandListener commandListener, String command, RouteTable routeTable) {
        ArrayList<String> args = new ArrayList<>();
        initArgs(args);
        args.add(command.split(" ")[1]);
        CommandResult commandResult = commandListener.runUpdate(args);
        insertMainFrame.dispose();
        showResult(commandResult, routeTable);
    }

    private void doRemoveLower(CommandListener commandListener, RouteTable routeTable) {
        ArrayList<String> args = new ArrayList<>();
        initArgs(args);
        CommandResult commandResult = commandListener.runRemoveLower(args);
        insertMainFrame.dispose();
        showResult(commandResult, routeTable);
    }

    private void doAdd(CommandListener commandListener, RouteTable routeTable) {
        ArrayList<String> args = new ArrayList<>();
        initArgs(args);
        commandListener.runAdd(args);
        insertMainFrame.dispose();
        routeTable.reload();
        showResult(success);
    }

    private void doAddIfMax(CommandListener commandListener, RouteTable routeTable) {
        ArrayList<String> args = new ArrayList<>();
        initArgs(args);
        CommandResult commandResult = commandListener.runAddIfMax(args);
        insertMainFrame.dispose();
        routeTable.reload();
        if (!commandResult.isSuccess()) {
            showResult(notMax);
        } else {
            showResult(success);
        }
    }

    private boolean checkResult() {
        return checkName(nameField.getText()) & checkDistance(distanceField.getText())
                & checkCoordinate(coordinateXField.getText(), MAX_COORDINATE_X)
                & checkCoordinate(coordinateYField.getText(), MAX_COORDINATE_Y)
                & checkLocationLong(fromXField.getText()) & checkLocationInt(fromYField.getText())
                & checkLocationInt(fromZField.getText()) & checkLocationLong(toXField.getText())
                & checkLocationInt(toYField.getText()) & checkLocationInt(toZField.getText());
    }

    private void showResult(CommandResult commandResult, RouteTable routeTable) {
        JLabel answer = new JLabel(commandResult.getMessage());
        answer.setFont(BUTTON_FONT);
        routeTable.reload();
        showResult(answer);
    }

    private void showResult(JLabel result) {
        JFrame resultFrame = new JFrame("result");
        resultFrame.setSize(new Dimension(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE));
        resultFrame.setLayout(new BorderLayout());
        resultFrame.setLocationRelativeTo(null);
        resultFrame.add(result, BorderLayout.CENTER);
        resultFrame.pack();
        resultFrame.setResizable(false);
        resultFrame.setVisible(true);
    }

    private void initArgs(ArrayList<String> args) {
        args.add(nameField.getText());
        args.add(distanceField.getText());
        args.add(coordinateXField.getText());
        args.add(coordinateYField.getText());
        args.add(fromXField.getText());
        args.add(fromYField.getText());
        args.add(fromZField.getText());
        args.add(toXField.getText());
        args.add(toYField.getText());
        args.add(toZField.getText());
    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }

    @Override
    public void repaintForLanguage() {

    }
}
