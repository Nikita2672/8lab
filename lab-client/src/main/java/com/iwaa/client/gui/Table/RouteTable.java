package com.iwaa.client.gui.Table;

import com.iwaa.client.gui.AbstractFrame;
import com.iwaa.client.gui.VisualFrame.GraphicsPanel;
import com.iwaa.client.gui.VisualFrame.Listener;
import com.iwaa.client.gui.interimFrames.FabricOfComponents;
import com.iwaa.client.gui.interimFrames.FilterFrame;
import com.iwaa.client.gui.interimFrames.AddFrame;
import com.iwaa.client.gui.interimFrames.InsertFrame;
import com.iwaa.client.gui.interimFrames.RemoveFrame;
import com.iwaa.client.local.Constants;
import com.iwaa.client.util.SortFilterLogic;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.network.CommandResult;

import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.GridCoordinates.EIGHT;
import static com.iwaa.client.gui.constants.GridCoordinates.FIVE;
import static com.iwaa.client.gui.constants.GridCoordinates.FOUR;
import static com.iwaa.client.gui.constants.GridCoordinates.SEVEN;
import static com.iwaa.client.gui.constants.GridCoordinates.SIX;
import static com.iwaa.client.gui.constants.GridCoordinates.THREE;
import static com.iwaa.client.gui.constants.SizeConstants.AREA_COLUMNS;
import static com.iwaa.client.gui.constants.SizeConstants.HELP_TEXT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_SQUARE_FRAME_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.HELP_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.HELP_WIDTH_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.ICON_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.INFO_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.INFO_WIDTH_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.MAIN_FRAME_HEIGHT;
import static com.iwaa.client.gui.constants.SizeConstants.MAIN_FRAME_WIDTH;
import static com.iwaa.client.gui.constants.SizeConstants.PREFERRED_WIDTH_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.PRINTER_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.PRINTER_WIDTH_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.ROW_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.SORT_FILTER_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.TABLE_HEADER_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.TABLE_TEXT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.VISUAL_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.VISUAL_WIDTH_SIZE;
import static com.iwaa.client.util.SortFilterLogic.doSort;

public class RouteTable extends AbstractFrame {

    private final Box verticalBox = Box.createVerticalBox();
    private final Box horizontalDownBox = Box.createHorizontalBox();
    private final Box horizontalTopBox = Box.createHorizontalBox();
    private final JButton infoButton = new JButton();
    private final JButton scriptButton = new JButton();
    private final JButton historyButton = new JButton();
    private final JButton addButton = new JButton();
    private final JButton removeButton = new JButton();
    private final JButton helpButton = new JButton();
    private final JButton filterButton = new JButton();
    private final JButton groupButton = new JButton();
    private final JButton printButton = new JButton();
    private final JButton reloadButton = new JButton();
    private final JButton viewButton = new JButton();
    private final JLabel sortMistake = new JLabel(localisation(Constants.SORT_MISTAKE));
    private ArrayList<String[]> tableElements;
    private CommandListener commandListenerForTable;
    private final JPanel mainPanel = new JPanel();
    private JFrame mainJFrame = new JFrame(localisation(Constants.MAIN_WINDOW));
    private JScrollPane jScrollPane;
    private String login;
    private RouteTableModel routeTableModel;

    public RouteTable(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }


    private void initButton(ImageIcon imageIcon, JButton button, Box box) {
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT));
        button.setIcon(icon);
        button.setBackground(Color.ORANGE);
        box.add(button);
    }

    {
        sortMistake.setFont(MISTAKE_FONT);
        sortMistake.setForeground(Color.RED);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\informationbutton_83733.png"), infoButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\hourglass_icon-icons.com_71171.png"), historyButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\add-sign_icon-icons.com_54067.png"), addButton, horizontalDownBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\minus-sign-inside-a-black-circle_icon-icons.com_73488.png"), removeButton, horizontalDownBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\questionmark_99738.png"), helpButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\filter_descending_sort_icon_149476.png"), filterButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\group_layer_layers_icon_195089.png"), groupButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\print-black-printer-tool-symbol_icon-icons.com_54467.png"), printButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\notepad_78910.png"), scriptButton, verticalBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\eye_icon-icons.com_71204.png"), viewButton, horizontalDownBox);
        initButton(new ImageIcon("lab-client\\src\\main\\resources\\icons\\reload_refresh_update_icon_149403.png"), reloadButton, horizontalDownBox);
        mainJFrame.setSize(new Dimension(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT));
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setLayout(new BorderLayout());
        mainPanel.setLayout(new GridBagLayout());
    }

    public void startTable(CommandListener commandListener, String sendedLogin) {
        this.login = sendedLogin;
        this.commandListenerForTable = commandListener;
        createPanel();
        mainJFrame.add(verticalBox, BorderLayout.LINE_END);
        mainJFrame.add(horizontalDownBox, BorderLayout.SOUTH);
        addListeners();
        mainJFrame.setVisible(true);
    }

    private void createPanel() {
        ArrayList<String> columns = initList();
        this.routeTableModel = new RouteTableModel(columns);
        JMenuBar lang = createLanguage(Color.BLACK);
        lang.setFont(BUTTON_FONT);
        lang.setBackground(Color.ORANGE);
        JLabel loginLabel = new JLabel(localisation(Constants.LOGIN_INFO) + " " + login + " ");
        loginLabel.setFont(BUTTON_FONT);
        JButton sortButton = new JButton(localisation(Constants.SORT_BUTTON));
        JButton sortFilterButton = new JButton(localisation(Constants.FILTER_BUTTON));
        sortButton.setFont(BUTTON_FONT);
        sortButton.setBackground(Color.ORANGE);
        sortFilterButton.setFont(BUTTON_FONT);
        sortFilterButton.setBackground(Color.ORANGE);
        sortButton.setSize(new Dimension(SORT_FILTER_SIZE, SORT_FILTER_SIZE));
        sortFilterButton.setSize(new Dimension(SORT_FILTER_SIZE, SORT_FILTER_SIZE));
        restartBox(loginLabel, sortButton, sortFilterButton, lang);
        JTable routeTable = restartTable(routeTableModel);
        setFontForTable(routeTable);
        this.jScrollPane = new JScrollPane(routeTable);
        jScrollPane.setPreferredSize(new Dimension(STANDART_HEIGHT_SIZE, STANDART_HEIGHT_SIZE));
        tableElements = initTable(commandListenerForTable, routeTableModel);
        addSorListener(sortButton);
        addSorFilterListener(sortFilterButton);
        restartPanel();
        mainJFrame.add(mainPanel, BorderLayout.CENTER);
        mainJFrame.add(horizontalTopBox, BorderLayout.NORTH);
        mainJFrame.repaint();
    }

    private JTable restartTable(RouteTableModel model) {
        return new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                JComponent jc = (JComponent) super.prepareRenderer(renderer, row, column);
                jc.setBorder(BorderFactory.createLineBorder(Color.BLACK, THREE));
                return jc;
            }

            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                if (tableElements.get(rowIndex)[0].equals(login)) {
                    updateTable(rowIndex);
                }
            }
        };
    }

    private void restartPanel() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(jScrollPane, new GridBagConstraints(0, 0, THREE, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
        mainPanel.repaint();
    }

    private void restartBox(JLabel loginLabel, JButton sortButton, JButton sortFilterButton, JMenuBar lang) {
        horizontalTopBox.removeAll();
        horizontalTopBox.revalidate();
        horizontalTopBox.add(loginLabel);
        horizontalTopBox.add(sortButton);
        horizontalTopBox.add(sortFilterButton);
        horizontalTopBox.add(lang);
    }

    private ArrayList<String> initList() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add(localisation(Constants.AUTHOR));
        columns.add(localisation(Constants.IDENTIFICATION));
        columns.add(localisation(Constants.NAME));
        columns.add(localisation(Constants.COORDINATES));
        columns.add(localisation(Constants.CREATION_DATE));
        columns.add(localisation(Constants.LOCATION_TO));
        columns.add(localisation(Constants.LOCATION_FROM));
        columns.add(localisation(Constants.DISTANCE));
        return columns;
    }

    private void setFontForTable(JTable routeTable) {
        routeTable.setFont(new Font("Roboto", Font.ITALIC, TABLE_TEXT_SIZE));
        routeTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, TABLE_HEADER_SIZE));
        routeTable.setBackground(Color.WHITE);
        routeTable.setRowHeight(ROW_HEIGHT_SIZE);
        routeTable.getColumnModel().getColumn(0).setPreferredWidth(PREFERRED_WIDTH_SIZE);
        routeTable.getColumnModel().getColumn(1).setPreferredWidth(PREFERRED_WIDTH_SIZE);
        routeTable.getColumnModel().getColumn(2).setPreferredWidth(PREFERRED_WIDTH_SIZE);
        routeTable.getColumnModel().getColumn(SEVEN).setPreferredWidth(PREFERRED_WIDTH_SIZE);
    }

    private void addListeners() {
        addInfoListener();
        addHistoryListener();
        addFilterListener();
        addGroupListener();
        addPrintListener();
        addPlusListener();
        addMinusListener();
        addReloadListener();
        addViewListener();
        addHelpListener();
        addScriptListener();
        mainJFrame.setResizable(false);
        mainJFrame.setVisible(true);
    }


    private void updateTable(int rowIndex) {
        Long id = Long.parseLong(tableElements.get(rowIndex)[1]);
        InsertFrame insertFrame = new InsertFrame(getResourceBundle());
        insertFrame.startInsertFrame(commandListenerForTable, "update " + id, this);
    }

    private void addScriptListener() {
        scriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame scriptFrame = new JFrame(localisation(Constants.EXECUTE_SCRIPT_WINDOW));
                scriptFrame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
                scriptFrame.setLayout(new BorderLayout());
                scriptFrame.setLocationRelativeTo(null);
                JTextField pathField = new JTextField(TABLE_TEXT_SIZE);
                pathField.setFont(FIELD_FONT);
                JLabel pathLabel = new JLabel(localisation(Constants.PATH));
                pathLabel.setFont(BUTTON_FONT);
                JPanel scriptPanel = new JPanel();
                JButton executeScriptButton = new JButton(localisation(Constants.EXECUTE));
                executeScriptButton.setBackground(Color.ORANGE);
                executeScriptButton.setFont(BUTTON_FONT);
                addExecuteScriptButtonListener(executeScriptButton, pathField, scriptPanel, scriptFrame);
                scriptPanel.setLayout(new GridBagLayout());
                FabricOfComponents.setLocationOfComponent(scriptPanel, pathLabel, 0, 0);
                FabricOfComponents.setLocationOfComponent(scriptPanel, pathField, 1, 0);
                FabricOfComponents.setLocationOfComponent(scriptPanel, executeScriptButton, 1, 1);
                scriptFrame.add(scriptPanel, BorderLayout.CENTER);
                scriptFrame.pack();
                scriptFrame.setResizable(false);
                scriptFrame.setVisible(true);
            }
        });
    }

    private void addExecuteScriptButtonListener(JButton scrButton, JTextField pathField, JPanel scriptPanel, JFrame frameScript) {
        scrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileReader fileReader = new FileReader(new File(pathField.getText()));
                    frameScript.dispose();
                    JFrame jFrame = new JFrame();
                    jFrame.setLayout(new BorderLayout());
                    jFrame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
                    jFrame.setResizable(false);
                    jFrame.setLocationRelativeTo(null);
                    JPanel areaJPanel = new JPanel();
                    areaJPanel.setLayout(new BorderLayout());
                    JTextArea jTextArea = new JTextArea(TABLE_TEXT_SIZE, AREA_COLUMNS);
                    jTextArea.setFont(MISTAKE_FONT);
                    CommandResult results = commandListenerForTable.runScript(pathField.getText());
                    String text = results.getMessage();
                    String answer = text.replaceAll("<br>", "\n");
                    answer = answer.replaceAll("<html>", "");
                    jTextArea.setText(answer);
                    jTextArea.setLineWrap(true);
                    JScrollPane pane = new JScrollPane(jTextArea);
                    areaJPanel.add(pane, BorderLayout.CENTER);
                    areaJPanel.revalidate();
                    jFrame.add(areaJPanel, BorderLayout.CENTER);
                    jFrame.setVisible(true);
                } catch (FileNotFoundException exception) {
                    JLabel label = new JLabel(localisation(Constants.MISTAKE_PATH));
                    label.setFont(MISTAKE_FONT);
                    label.setForeground(Color.RED);
                    FabricOfComponents.setLocationOfComponent(scriptPanel, label, 0, 1);
                    frameScript.add(scriptPanel);
                    frameScript.setVisible(true);
                }
            }
        });
    }

    private void addInfoListener() {
        infoButton.addActionListener(e -> {
            CommandResult result = commandListenerForTable.runInfo();
            JFrame frame = new JFrame(localisation(Constants.INFO_WINDOW));
            frame.setLocationRelativeTo(null);
            frame.setSize(new Dimension(INFO_WIDTH_SIZE, INFO_HEIGHT_SIZE));
            frame.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(result.getMessage());
            jLabel.setFont(FIELD_FONT);
            frame.add(jLabel, BorderLayout.CENTER);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private void addPrintListener() {
        printButton.addActionListener(e -> {
            CommandResult result = commandListenerForTable.runPrint();
            JFrame frame = new JFrame(localisation(Constants.PRINTER_WINDOW));
            frame.setLocationRelativeTo(null);
            frame.setSize(new Dimension(PRINTER_WIDTH_SIZE, PRINTER_HEIGHT_SIZE));
            frame.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(localisation(Constants.PRINTER_INFO) + result.getMessage());
            jLabel.setFont(FIELD_FONT);
            frame.add(jLabel, BorderLayout.CENTER);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private void addGroupListener() {
        groupButton.addActionListener(e -> {
            CommandResult result = commandListenerForTable.runGroup();
            JFrame frame = new JFrame(localisation(Constants.GROUP_WINDOW));
            frame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(result.getMessage());
            jLabel.setFont(FIELD_FONT);
            frame.add(jLabel, BorderLayout.CENTER);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private void addHistoryListener() {
        historyButton.addActionListener(e -> {
            CommandResult result = commandListenerForTable.runHistory();
            JFrame frame = new JFrame(localisation(Constants.HISTORY_WINDOW));
            frame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(result.getMessage());
            jLabel.setFont(FIELD_FONT);
            frame.add(jLabel, BorderLayout.CENTER);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private void addReloadListener() {
        reloadButton.addActionListener(e -> {
            reload();
        });
    }

    public static ArrayList<String[]> initTable(CommandListener commandListener, RouteTableModel routeTableModel) {
        routeTableModel.clear();
        Collection<Route> routes = commandListener.runShow().getRoutes();
        for (Route route : routes) {
            String[] str = new String[EIGHT];
            str[0] = route.getAuthor();
            str[1] = route.getId().toString();
            str[2] = route.getName();
            str[THREE] = route.getCoordinates().toString();
            str[FOUR] = route.getCreationDate().toString();
            str[FIVE] = route.getFrom().toString();
            str[SIX] = route.getTo().toString();
            str[SEVEN] = route.getDistance().toString();
            routeTableModel.addDate(str);
        }
        return routeTableModel.getDataArrayList();
    }

    private void addPlusListener() {
        addButton.addActionListener(e -> {
            AddFrame addFrame = new AddFrame(getResourceBundle());
            addFrame.startAdd(commandListenerForTable, this);
        });
    }

    private void addMinusListener() {
        removeButton.addActionListener(e -> {
            RemoveFrame removeFrame = new RemoveFrame(getResourceBundle());
            removeFrame.startRemove(commandListenerForTable, this);
        });
    }

    public String getLogin() {
        return login;
    }

    private void addFilterListener() {
        filterButton.addActionListener(e -> {
            FilterFrame filterFrame = new FilterFrame(getResourceBundle());
            filterFrame.startFilter(commandListenerForTable);
        });
    }

    private void addHelpListener() {
        helpButton.addActionListener(e -> {
            CommandResult result = commandListenerForTable.runHelp();
            JFrame frame = new JFrame(localisation(Constants.HELP_WINDOW));
            frame.setSize(new Dimension(HELP_WIDTH_SIZE, HELP_HEIGHT_SIZE));
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(result.getMessage());
            jLabel.setFont(new Font("Consolas", Font.ITALIC, HELP_TEXT_SIZE));
            frame.add(jLabel, BorderLayout.CENTER);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private void addViewListener() {
        viewButton.addActionListener(e -> {
            JFrame viewFrame = new JFrame();
            viewFrame.setTitle(localisation(Constants.VIEW_WINDOW));
            viewFrame.setSize(new Dimension(VISUAL_WIDTH_SIZE, VISUAL_HEIGHT_SIZE));
            viewFrame.setResizable(false);
            viewFrame.setLayout(new BorderLayout());
            GraphicsPanel graphicsPanel = new GraphicsPanel(viewFrame, commandListenerForTable);
            graphicsPanel.addMouseListener(new Listener(graphicsPanel.getMapOfHeadBoxes(), getResourceBundle(), this));
            viewFrame.add(graphicsPanel, BorderLayout.CENTER);
            viewFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            JButton exitButton = new JButton("Exit");
            addExitListener(exitButton, graphicsPanel, viewFrame);
            viewFrame.add(exitButton, BorderLayout.SOUTH);
            viewFrame.setVisible(true);
        });
    }

    private void addExitListener(JButton exitButton, GraphicsPanel graphicsPanel, JFrame viewFrame) {
        exitButton.setFont(BUTTON_FONT);
        exitButton.setBackground(Color.ORANGE);
        exitButton.addActionListener(event -> {
            graphicsPanel.swabReloading();
            viewFrame.dispose();
        });
    }


    public void reload() {
        createPanel();
    }

    private void addSorFilterListener(JButton sortFilterButton) {
        sortFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame filterFrame = new JFrame(localisation(Constants.FILTER_BUTTON));
                filterFrame.setLocationRelativeTo(null);
                filterFrame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
                filterFrame.setLayout(new BorderLayout());
                JPanel filterPanel = new JPanel();
                filterPanel.setLayout(new GridBagLayout());
                ArrayList<JRadioButton> columnList = initColumnButtons();
                ArrayList<JRadioButton> inequalityList = initInequalityButtons();
                JButton filterSendButton = new JButton(localisation(Constants.FILTER_BUTTON));
                filterSendButton.setFont(BUTTON_FONT);
                filterSendButton.setBackground(Color.ORANGE);
                addSortCommandListener(filterFrame, filterPanel, filterSendButton, inequalityList, columnList,
                        mainPanel, "Filter");
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(0), 0, 0);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(1), 0, 1);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(2), 0, 2);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(THREE), 0, THREE);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(FOUR), 0, FOUR);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(FIVE), 0, FIVE);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(SIX), 0, SIX);
                FabricOfComponents.setLocationOfComponent(filterPanel, columnList.get(SEVEN), 0, SEVEN);
                FabricOfComponents.setLocationOfComponent(filterPanel, inequalityList.get(0), 1, THREE);
                FabricOfComponents.setLocationOfComponent(filterPanel, inequalityList.get(1), 1, FOUR);
                FabricOfComponents.setLocationOfComponent(filterPanel, inequalityList.get(2), 1, FIVE);
                FabricOfComponents.setLocationOfComponent(filterPanel, filterSendButton, 1, EIGHT);
                filterFrame.add(filterPanel, BorderLayout.CENTER);
                filterFrame.pack();
                filterFrame.setResizable(false);
                filterFrame.setVisible(true);
            }
        });
    }

    private ArrayList<JRadioButton> initColumnButtons() {
        ArrayList<JRadioButton> columnSet = new ArrayList<>();
        ButtonGroup groupByColumn = new ButtonGroup();
        JRadioButton author = new JRadioButton(localisation(Constants.AUTHOR));
        columnSet.add(author);
        JRadioButton id = new JRadioButton(localisation(Constants.IDENTIFICATION));
        columnSet.add(id);
        JRadioButton name = new JRadioButton(localisation(Constants.NAME));
        columnSet.add(name);
        JRadioButton coordinates = new JRadioButton(localisation(Constants.COORDINATES));
        columnSet.add(coordinates);
        JRadioButton creationDate = new JRadioButton(localisation(Constants.CREATION_DATE));
        columnSet.add(creationDate);
        JRadioButton to = new JRadioButton(localisation(Constants.LOCATION_TO));
        columnSet.add(to);
        JRadioButton from = new JRadioButton(localisation(Constants.LOCATION_FROM));
        columnSet.add(from);
        JRadioButton distance = new JRadioButton(localisation(Constants.DISTANCE));
        columnSet.add(distance);
        for (JRadioButton jRadioButton : columnSet) {
            jRadioButton.setFont(BUTTON_FONT);
            groupByColumn.add(jRadioButton);
        }
        return columnSet;
    }

    private ArrayList<JRadioButton> initInequalityButtons() {
        ArrayList<JRadioButton> inequalityList = new ArrayList<>();
        ButtonGroup inequality = new ButtonGroup();
        JRadioButton less = new JRadioButton("<");
        inequalityList.add(less);
        JRadioButton more = new JRadioButton(">");
        inequalityList.add(more);
        JRadioButton equal = new JRadioButton("=");
        inequalityList.add(equal);
        for (JRadioButton jRadioButton : inequalityList) {
            jRadioButton.setFont(BUTTON_FONT);
            inequality.add(jRadioButton);
        }
        return inequalityList;
    }

    private ArrayList<JRadioButton> initOrderButtons() {
        ArrayList<JRadioButton> orderList = new ArrayList<>();
        ButtonGroup inequality = new ButtonGroup();
        JRadioButton ascending = new JRadioButton(localisation(Constants.ASCENDING));
        orderList.add(ascending);
        JRadioButton descending = new JRadioButton(localisation(Constants.DESCENDING));
        orderList.add(descending);
        for (JRadioButton jRadioButton : orderList) {
            jRadioButton.setFont(BUTTON_FONT);
            inequality.add(jRadioButton);
        }
        return orderList;
    }


    private void addSorListener(JButton sortButton) {
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame sortFrame = new JFrame(localisation(Constants.SORT_WINDOW));
                sortFrame.setLocationRelativeTo(null);
                sortFrame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
                sortFrame.setLayout(new BorderLayout());
                JPanel sortPanel = new JPanel();
                sortPanel.setLayout(new GridBagLayout());

                ArrayList<JRadioButton> columnList = initColumnButtons();
                ArrayList<JRadioButton> orderList = initOrderButtons();
                JButton sortButton = new JButton(localisation(Constants.SORT_BUTTON));
                sortButton.setFont(BUTTON_FONT);
                sortButton.setBackground(Color.ORANGE);
                addSortCommandListener(sortFrame, sortPanel, sortButton, orderList, columnList,
                        mainPanel, "Sort");
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(0), 0, 0);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(1), 0, 1);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(2), 0, 2);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(THREE), 0, THREE);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(FOUR), 0, FOUR);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(FIVE), 0, FIVE);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(SIX), 0, SIX);
                FabricOfComponents.setLocationOfComponent(sortPanel, columnList.get(SEVEN), 0, SEVEN);
                FabricOfComponents.setLocationOfComponent(sortPanel, orderList.get(0), 1, THREE);
                FabricOfComponents.setLocationOfComponent(sortPanel, orderList.get(1), 1, FOUR);
                FabricOfComponents.setLocationOfComponent(sortPanel, sortButton, 1, EIGHT);

                sortFrame.add(sortPanel, BorderLayout.CENTER);
                sortFrame.pack();
                sortFrame.setResizable(false);
                sortFrame.setVisible(true);
            }
        });
    }

    private void addSortCommandListener(JFrame sortFilterFrame, JPanel sortFilterPanel, JButton sortFilterButton,
                                        List<JRadioButton> orderList, List<JRadioButton> columnList, JPanel panel,
                                        String command) {
        sortFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isChooseOrder = false;
                boolean isChooseColumn = false;
                String nameOfColumn = null;
                String nameOfOrder = null;
                for (JRadioButton jRadioButton : orderList) {
                    if (jRadioButton.isSelected()) {
                        nameOfOrder = jRadioButton.getText();
                        isChooseOrder = true;
                        break;
                    }
                }
                for (JRadioButton jRadioButton : columnList) {
                    if (jRadioButton.isSelected()) {
                        nameOfColumn = jRadioButton.getText();
                        isChooseColumn = true;
                        break;
                    }
                }
                if (isChooseColumn && isChooseOrder) {
                    sortFilterFrame.dispose();
                    if ("Sort".equals(command)) {
                        doSort(nameOfColumn, nameOfOrder, commandListenerForTable, routeTableModel, panel, mainJFrame, jScrollPane);
                    } else {
                        SortFilterLogic sortFilterLogic = new SortFilterLogic(getResourceBundle());
                        sortFilterLogic.doFilter(nameOfColumn, nameOfOrder, commandListenerForTable, routeTableModel, panel, mainJFrame, jScrollPane);
                    }
                } else {
                    FabricOfComponents.setLocationOfComponent(sortFilterPanel, sortMistake, 0, EIGHT);
                    sortFilterFrame.add(sortFilterPanel, BorderLayout.CENTER);
                    sortFilterFrame.setVisible(true);
                }
            }
        });
    }

    @Override
    public void repaintForLanguage() {
        createPanel();
    }

    @Override
    public String localisation(Constants constants) {
        return super.localisation(constants);
    }

    public CommandListener getCommandListenerForTable() {
        return commandListenerForTable;
    }

    public RouteTableModel getRouteTableModel() {
        return routeTableModel;
    }
}
