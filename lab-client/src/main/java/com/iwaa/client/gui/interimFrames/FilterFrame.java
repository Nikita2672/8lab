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
import java.awt.Frame;
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
    private final JFrame frame;
    private final JPanel panel;

    {
        distanceField.setFont(FIELD_FONT);
        sendButton.setFont(BUTTON_FONT);
        sendButton.setBackground(Color.ORANGE);
        distanceLabel.setFont(BUTTON_FONT);
        mistakeLabel.setFont(MISTAKE_FONT);
        mistakeLabel.setForeground(Color.RED);
        frame = new JFrame(localisation(Constants.FILTER_WINDOW));
        frame.setSize(STANDART_WIDTH_SIZE, STANDART_HEIGHT_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        panel = createPanel();
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
    }

    public FilterFrame(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    public void startFilter(CommandListener commandListener) {
        frame.setVisible(true);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long distance;
                try {
                    distance = Long.parseLong(distanceField.getText());
                    CommandResult commandResult = commandListener.runFilter(distance);
                    frame.dispose();
                    JFrame jFrame = new JFrame(localisation(Constants.RESULT_FILTER));
                    jFrame.setSize(new Dimension(BIG_WIDTH_SIZE, BIG_HEIGHT_SIZE));
                    jFrame.setLayout(new BorderLayout());
                    String title = localisation(Constants.TITLE) + " " + distance + "<br> <html>";
                    JLabel result = new JLabel(title + commandResult.getMessage());
                    result.setFont(LABEL_RESULT_FONT);
                    jFrame.add(result, BorderLayout.CENTER);
                    jFrame.setResizable(false);
                    jFrame.setVisible(true);
                } catch (NumberFormatException exception) {
                    showMistake(frame, panel);
                }
            }
        });
    }

    private void showMistake(Frame jFrame, JPanel jPanel) {
        FabricOfComponents.setLocationOfComponent(jPanel, mistakeLabel, 0, 1);
        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.setVisible(true);
    }

    private JPanel createPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(jPanel, distanceLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(jPanel, distanceField, 1, 0);
        FabricOfComponents.setLocationOfComponent(jPanel, sendButton, 1, 1);
        return jPanel;
    }

    @Override
    public void repaintForLanguage() {

    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }
}
