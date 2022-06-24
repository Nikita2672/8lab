package com.iwaa.client.gui.interimFrames;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.LABEL_RESULT_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.SizeConstants.BIG_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.BIG_WIDTH_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_WIDTH_SIZE;

public class FilterFrame extends AbstractFrame {

    private final JTextField distanceField = new JTextField(20);
    private final JButton sendButton = new JButton(localisation(Constants.SEND));
    private final JLabel distanceLabel = new JLabel(localisation(Constants.DISTANCE_ENTER));
    private final JLabel mistakeLabel = new JLabel(localisation(Constants.MISTAKE_DISTANCE));
    private final JFrame filterMainFrame;
    private final JPanel filterMainPanel;

    {
        distanceField.setFont(FIELD_FONT);
        sendButton.setFont(BUTTON_FONT);
        sendButton.setBackground(Color.ORANGE);
        distanceLabel.setFont(BUTTON_FONT);
        mistakeLabel.setFont(MISTAKE_FONT);
        mistakeLabel.setForeground(Color.RED);
        filterMainFrame = new JFrame(localisation(Constants.FILTER_WINDOW));
        filterMainFrame.setSize(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE);
        filterMainFrame.setLocationRelativeTo(null);
        filterMainFrame.setLayout(new BorderLayout());
        filterMainPanel = createPanel();
        filterMainFrame.add(filterMainPanel, BorderLayout.CENTER);
        filterMainFrame.pack();
        filterMainFrame.setResizable(false);
    }

    public FilterFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startFilter(CommandListener commandListener) {
        filterMainFrame.setVisible(true);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long distance;
                try {
                    distance = Long.parseLong(distanceField.getText());
                    CommandResult commandResult = commandListener.runFilter(distance);
                    filterMainFrame.dispose();
                    JFrame resultFilterFrame = new JFrame(localisation(Constants.RESULT_FILTER));
                    resultFilterFrame.setSize(new Dimension(BIG_WIDTH_SIZE, BIG_HEIGHT_SIZE));
                    resultFilterFrame.setLayout(new BorderLayout());
                    String title = localisation(Constants.TITLE) + " " + distance + "<br> <html>";
                    JLabel resultLabel = new JLabel(title + commandResult.getMessage());
                    resultLabel.setFont(LABEL_RESULT_FONT);
                    resultFilterFrame.add(resultLabel, BorderLayout.CENTER);
                    resultFilterFrame.setResizable(false);
                    resultFilterFrame.setVisible(true);
                } catch (NumberFormatException exception) {
                    showMistake();
                }
            }
        });
    }

    private void showMistake() {
        FabricOfComponents.setLocationOfComponent(filterMainPanel, mistakeLabel, 0, 1);
        filterMainFrame.add(filterMainPanel, BorderLayout.CENTER);
        filterMainFrame.setVisible(true);
    }

    private JPanel createPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(mainPanel, distanceLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(mainPanel, distanceField, 1, 0);
        FabricOfComponents.setLocationOfComponent(mainPanel, sendButton, 1, 1);
        return mainPanel;
    }

    @Override
    public void repaintForLanguage() {

    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
