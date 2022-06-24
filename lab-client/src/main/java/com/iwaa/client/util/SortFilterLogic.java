package com.iwaa.client.util;

import com.iwaa.client.gui.Table.RouteTableModel;
import com.iwaa.client.gui.interimFrames.FabricOfComponents;
import com.iwaa.client.local.Localized;
import com.iwaa.client.local.Constants;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;

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
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.FIELD_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.GridCoordinates.EIGHT;
import static com.iwaa.client.gui.constants.GridCoordinates.FIVE;
import static com.iwaa.client.gui.constants.GridCoordinates.FOUR;
import static com.iwaa.client.gui.constants.GridCoordinates.SEVEN;
import static com.iwaa.client.gui.constants.GridCoordinates.SIX;
import static com.iwaa.client.gui.constants.GridCoordinates.THREE;
import static com.iwaa.client.gui.constants.SizeConstants.NUMBER_OF_COLUMNS;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_SQUARE_FRAME_SIZE;


public final class SortFilterLogic implements Localized {

    private static final String AUTHOR = "Author";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String COORDINATES = "Coordinates";
    private static final String CREATION_DATE = "Creation Date";
    private static final String LOCATION_TO = "Location to";
    private static final String LOCATION_FROM = "Location from";
    private static final String DISTANCE = "Distance";
    private static final String ASCENDING = "Ascending";

    private static final HashMap<String, HashSet<String>> COLUMNS = new HashMap<>();
    private ResourceBundle resourceBundle;
    private JScrollPane jScrollPane;
    private RouteTableModel routeTableModel;
    private CommandListener commandListener;
    private String order;
    private String nameOfColumn;

    static {
        COLUMNS.put(AUTHOR, new HashSet<>(Arrays.asList("Author", "автор", "Auteur", "Forfatter")));
        COLUMNS.put(ID, new HashSet<>(Arrays.asList("Id", "айди", "ID", "ID")));
        COLUMNS.put(NAME, new HashSet<>(Arrays.asList("Name", "имя", "Nom", "Navn")));
        COLUMNS.put(COORDINATES, new HashSet<>(Arrays.asList("Coordinates", "координаты", "Coordonner", "Koordinere")));
        COLUMNS.put(CREATION_DATE, new HashSet<>(Arrays.asList("Creation Date", "дата создания", "Date de Création", "opprettelsesdato")));
        COLUMNS.put(LOCATION_TO, new HashSet<>(Arrays.asList("Location to", "локация куда", "Emplacement à", "Beliggenhet til")));
        COLUMNS.put(LOCATION_FROM, new HashSet<>(Arrays.asList("Location from", "локация откуда", "Emplacement à partir de", "Beliggenhet fra")));
        COLUMNS.put(DISTANCE, new HashSet<>(Arrays.asList("Distance", "дистанция", "Distance", "Avstand")));
        COLUMNS.put(ASCENDING, new HashSet<>(Arrays.asList("Ascending", "по возрастанию", "Ascendant", "Stigende")));
    }

    public SortFilterLogic(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public static void doSort(String nameOfColumn, String order, CommandListener commandListener, RouteTableModel routeTableModel,
                              JPanel mainPanel, JFrame mainFrame, JScrollPane jScrollPane) {
        Collection<Route> routes = commandListener.runShow().getRoutes();
        boolean ascending;
        ascending = COLUMNS.get("Ascending").contains(order);

        List<Route> resultOfSort = new ArrayList<>();
        if (COLUMNS.get(CREATION_DATE).contains(nameOfColumn)) {
            resultOfSort = creationDateSort(routes, ascending);
        }
        if (COLUMNS.get(COORDINATES).contains(nameOfColumn)) {
            resultOfSort = coordinateSort(routes, ascending);
        }
        if (COLUMNS.get(ID).contains(nameOfColumn)) {
            resultOfSort = idSort(routes, ascending);
        }
        if (COLUMNS.get(AUTHOR).contains(nameOfColumn)) {
            resultOfSort = authorSort(routes, ascending);
        }
        if (COLUMNS.get(NAME).contains(nameOfColumn)) {
            resultOfSort = nameSort(routes, ascending);
        }
        if (COLUMNS.get(LOCATION_TO).contains(nameOfColumn)) {
            resultOfSort = toSort(routes, ascending);
        }
        if (COLUMNS.get(LOCATION_FROM).contains(nameOfColumn)) {
            resultOfSort = fromSort(routes, ascending);
        }
        if (COLUMNS.get(DISTANCE).contains(nameOfColumn)) {
            resultOfSort = distanceSort(routes, ascending);
        }
        showResult(routeTableModel, resultOfSort, mainPanel, mainFrame, jScrollPane);
    }

    private static List<Route> creationDateSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getCreationDate)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getCreationDate).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> authorSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getAuthor)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getAuthor).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> idSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getId)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getId).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> nameSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getName)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getName).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> coordinateSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getCoordinates)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getCoordinates).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> fromSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getFrom)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getFrom).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> toSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getTo)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getTo).reversed()).collect(Collectors.toList());
        }
    }

    private static List<Route> distanceSort(Collection<Route> routes, Boolean order) {
        if (order) {
            return routes.stream().sorted(Comparator.comparing(Route::getDistance)).collect(Collectors.toList());
        } else {
            return routes.stream().sorted(Comparator.comparing(Route::getDistance).reversed()).collect(Collectors.toList());
        }
    }

    private void checkNameAndAuthor(JPanel filterPanelToInsert, JButton filterButton, JFrame filterFrameToInsert, JLabel mistakeLabel,
                                    JPanel mainPanel, JFrame mainFrame) {
        if (COLUMNS.get(NAME).contains(nameOfColumn) || COLUMNS.get(AUTHOR).contains(nameOfColumn)) {
            JLabel jLabel = new JLabel(localisation(Constants.STRING));
            jLabel.setFont(BUTTON_FONT);
            JTextField jTextField = new JTextField(NUMBER_OF_COLUMNS);
            jTextField.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jLabel, 0, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextField, 1, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, filterButton, 1, 1);
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jTextField.getText() == null || jTextField.getText().length() == 0) {
                        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, mistakeLabel, 0, 1);
                        filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
                        filterFrameToInsert.setVisible(true);
                    } else {
                        filterListByNameOrAuthor(jTextField, mainPanel, mainFrame, filterFrameToInsert);
                    }
                }
            });
            filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
        }
    }

    private void filterListByNameOrAuthor(JTextField jTextField, JPanel mainPanel, JFrame mainFrame, JFrame filterFrameToInsert) {
        String str = jTextField.getText();
        Collection<Route> routes = commandListener.runShow().getRoutes();
        List<Route> list;
        if (">".equals(order)) {
            if (COLUMNS.get(NAME).contains(nameOfColumn)) {
                list = routes.stream().filter(route -> (str.compareTo(route.getName()) > 0)).collect(Collectors.toList());
            } else {
                list = routes.stream().filter(route -> (str.compareTo(route.getAuthor()) > 0)).collect(Collectors.toList());
            }
        } else {
            if ("<".equals(order)) {
                if (COLUMNS.get(NAME).contains(nameOfColumn)) {
                    list = routes.stream().filter(route -> (str.compareTo(route.getName()) < 0)).collect(Collectors.toList());
                } else {
                    list = routes.stream().filter(route -> (str.compareTo(route.getAuthor()) < 0)).collect(Collectors.toList());
                }
            } else {
                if (COLUMNS.get(NAME).contains(nameOfColumn)) {
                    list = routes.stream().filter(route -> (str.equals(route.getName()))).collect(Collectors.toList());
                } else {
                    list = routes.stream().filter(route -> (str.equals(route.getAuthor()))).collect(Collectors.toList());
                }
            }
        }
        showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
        filterFrameToInsert.dispose();
    }

    private void checkDistanceAndId(JPanel filterPanelToInsert, JButton filterButton, JFrame filterFrameToInsert, JLabel mistakeLabel,
                                    JPanel mainPanel, JFrame mainFrame) {
        if (COLUMNS.get(DISTANCE).contains(nameOfColumn) || COLUMNS.get(ID).contains(nameOfColumn)) {
            JLabel jLabel = new JLabel(localisation(Constants.NUMBER));
            jLabel.setFont(BUTTON_FONT);
            JTextField jTextField = new JTextField(NUMBER_OF_COLUMNS);
            jTextField.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jLabel, 0, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextField, 1, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, filterButton, 1, 1);
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        filterByDistanceAndId(jTextField, mainPanel, mainFrame, filterFrameToInsert);
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, mistakeLabel, 0, 1);
                        filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
                        filterFrameToInsert.setVisible(true);
                    }
                }
            });
            filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
        }
    }

    private void filterByDistanceAndId(JTextField jTextField, JPanel mainPanel, JFrame mainFrame, JFrame filterFrameToInsert) throws NumberFormatException {
        Long distanceOrId = Long.parseLong(jTextField.getText());
        Collection<Route> routes = commandListener.runShow().getRoutes();
        List<Route> list;
        if (">".equals(order)) {
            if (COLUMNS.get(DISTANCE).contains(nameOfColumn)) {
                list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getDistance()) < 0)).collect(Collectors.toList());
            } else {
                list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getId()) < 0)).collect(Collectors.toList());
            }
        } else {
            if ("<".equals(order)) {
                if (COLUMNS.get(DISTANCE).contains(nameOfColumn)) {
                    list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getDistance()) > 0)).collect(Collectors.toList());
                } else {
                    list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getId()) > 0)).collect(Collectors.toList());
                }
            } else {
                if (COLUMNS.get(DISTANCE).contains(nameOfColumn)) {
                    list = routes.stream().filter(route -> (distanceOrId.equals(route.getDistance()))).collect(Collectors.toList());
                } else {
                    list = routes.stream().filter(route -> (distanceOrId.equals(route.getId()))).collect(Collectors.toList());
                }
            }
        }
        showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
        filterFrameToInsert.dispose();
    }

    private void checkLocation(JPanel filterPanelToInsert, JButton filterButton, JFrame filterFrameToInsert, JLabel mistakeLabel,
                               JPanel mainPanel, JFrame mainFrame) {
        if (COLUMNS.get(LOCATION_FROM).contains(nameOfColumn) || COLUMNS.get(LOCATION_TO).contains(nameOfColumn)) {
            JLabel x = new JLabel(localisation(Constants.X));
            JLabel y = new JLabel(localisation(Constants.Y));
            JLabel z = new JLabel(localisation(Constants.Z));
            x.setFont(BUTTON_FONT);
            y.setFont(BUTTON_FONT);
            z.setFont(BUTTON_FONT);
            JTextField jTextFieldX = new JTextField(NUMBER_OF_COLUMNS);
            JTextField jTextFieldY = new JTextField(NUMBER_OF_COLUMNS);
            JTextField jTextFieldZ = new JTextField(NUMBER_OF_COLUMNS);
            jTextFieldX.setFont(FIELD_FONT);
            jTextFieldY.setFont(FIELD_FONT);
            jTextFieldZ.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, x, 0, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, y, 0, 1);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, z, 0, 2);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextFieldX, 1, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextFieldY, 1, 1);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextFieldZ, 1, 2);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, filterButton, 1, THREE);
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        filterByLocation(jTextFieldX, jTextFieldY, jTextFieldZ, mainPanel, mainFrame, filterFrameToInsert);
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, mistakeLabel, 0, THREE);
                        filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
                        filterFrameToInsert.setVisible(true);
                    }
                }
            });
            filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
        }
    }

    private void filterByLocation(JTextField jTextFieldX, JTextField jTextFieldY, JTextField jTextFieldZ,
                                  JPanel mainPanel, JFrame mainFrame, JFrame filterFrameToInsert) throws NumberFormatException {
        Location location = new Location(Long.parseLong(jTextFieldX.getText()),
                Integer.parseInt(jTextFieldY.getText()),
                Integer.parseInt(jTextFieldZ.getText()));
        Collection<Route> routes = commandListener.runShow().getRoutes();
        List<Route> list;
        if (">".equals(order)) {
            if (COLUMNS.get(LOCATION_FROM).contains(nameOfColumn)) {
                list = routes.stream().filter(route -> (location.compareTo(route.getFrom()) < 0)).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            } else {
                list = routes.stream().filter(route -> (location.compareTo(route.getTo()) < 0)).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            }
        }
        if ("<".equals(order)) {
            if (COLUMNS.get(LOCATION_FROM).contains(nameOfColumn)) {
                list = routes.stream().filter(route -> (location.compareTo(route.getFrom()) > 0)).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            } else {
                list = routes.stream().filter(route -> (location.compareTo(route.getTo()) > 0)).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            }
        } else {
            if (COLUMNS.get(LOCATION_FROM).contains(nameOfColumn)) {
                list = routes.stream().filter(route -> (location.equals(route.getFrom()))).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            } else {
                list = routes.stream().filter(route -> (location.equals(route.getTo()))).collect(Collectors.toList());
                showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
            }
        }
        filterFrameToInsert.dispose();
    }

    private void checkCoordinates(JPanel filterPanelToInsert, JButton filterButton, JFrame filterFrameToInsert, JLabel mistakeLabel,
                                  JPanel mainPanel, JFrame mainFrame) {
        if (COLUMNS.get(COORDINATES).contains(nameOfColumn)) {
            JLabel x = new JLabel(localisation(Constants.NUMBER_FLOAT));
            JLabel y = new JLabel(localisation(Constants.NUMBER_FLOAT));
            x.setFont(BUTTON_FONT);
            y.setFont(BUTTON_FONT);
            JTextField jTextFieldX = new JTextField(NUMBER_OF_COLUMNS);
            JTextField jTextFieldY = new JTextField(NUMBER_OF_COLUMNS);
            jTextFieldX.setFont(FIELD_FONT);
            jTextFieldY.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, x, 0, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, y, 0, 1);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextFieldX, 1, 0);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, jTextFieldY, 1, 1);
            FabricOfComponents.setLocationOfComponent(filterPanelToInsert, filterButton, 1, 2);
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        filterByCoordinates(jTextFieldX, jTextFieldY, filterFrameToInsert, mainPanel, mainFrame);
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, mistakeLabel, 0, THREE);
                        filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
                        filterFrameToInsert.setVisible(true);
                    }
                }
            });
            filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
        }
    }

    private void filterByCoordinates(JTextField jTextFieldX, JTextField jTextFieldY, JFrame filterFrameToInsert,
                                     JPanel mainPanel, JFrame mainFrame) {
        float xData = Float.parseFloat(jTextFieldX.getText());
        float yData = Float.parseFloat(jTextFieldY.getText());
        Coordinates coordinates = new Coordinates(xData, yData);
        Collection<Route> routes = commandListener.runShow().getRoutes();
        List<Route> list;
        if (">".equals(order)) {
            list = routes.stream().filter(route -> (coordinates.compareTo(route.getCoordinates()) < 0)).collect(Collectors.toList());
        } else {
            if ("<".equals(order)) {
                list = routes.stream().filter(route -> (coordinates.compareTo(route.getCoordinates()) > 0)).collect(Collectors.toList());
            } else {
                list = routes.stream().filter(route -> (coordinates.equals(route.getCoordinates()))).collect(Collectors.toList());
            }
        }
        showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
        filterFrameToInsert.dispose();
    }

    private void setComponents(JPanel filterPanelToInsert, JLabel dateLabel, JLabel dateLabelFormat, JTextField dateField, JButton filterButton) {
        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, dateLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, dateLabelFormat, 0, 1);
        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, dateField, 1, 0);
        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, filterButton, 1, 2);
    }

    private void checkCreationDate(JPanel filterPanelToInsert, JButton filterButton, JFrame filterFrameToInsert, JLabel mistakeLabel,
                                   JPanel mainPanel, JFrame mainFrame) {
        if (COLUMNS.get(CREATION_DATE).contains(nameOfColumn)) {
            JLabel dateLabel = new JLabel(localisation(Constants.DATE));
            JLabel dateLabelFormat = new JLabel(localisation(Constants.DATE_FORMAT));
            dateLabel.setFont(BUTTON_FONT);
            dateLabelFormat.setFont(MISTAKE_FONT);
            JTextField dateField = new JTextField(NUMBER_OF_COLUMNS);
            dateField.setFont(FIELD_FONT);
            setComponents(filterPanelToInsert, dateLabel, dateLabelFormat, dateField, filterButton);
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                        String inputDate = dateField.getText();
                        Date date = formatter.parse(inputDate);
                        Collection<Route> routes = commandListener.runShow().getRoutes();
                        List<Route> list;
                        if (">".equals(order)) {
                            list = routes.stream().filter(route -> (date.compareTo(route.getCreationDate()) < 0)).collect(Collectors.toList());
                        } else {
                            if ("<".equals(order)) {
                                list = routes.stream().filter(route -> (date.compareTo(route.getCreationDate()) > 0)).collect(Collectors.toList());
                            } else {
                                list = routes.stream().filter(route -> (date.equals(route.getCreationDate()))).collect(Collectors.toList());
                            }
                        }
                        showResult(routeTableModel, list, mainPanel, mainFrame, jScrollPane);
                        filterFrameToInsert.dispose();
                    } catch (ParseException ex) {
                        FabricOfComponents.setLocationOfComponent(filterPanelToInsert, mistakeLabel, 0, 2);
                        filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
                        filterFrameToInsert.setVisible(true);
                    }
                }
            });
            filterFrameToInsert.add(filterPanelToInsert, BorderLayout.CENTER);
        }
    }

    public void doFilter(String columnName, String order1, CommandListener listener, RouteTableModel model,
                         JPanel mainPanel, JFrame mainFrame, JScrollPane pane) {
        this.order = order1;
        this.jScrollPane = pane;
        this.nameOfColumn = columnName;
        this.routeTableModel = model;
        this.commandListener = listener;
        JFrame filterFrameToInsert = new JFrame(columnName + " " + order1);
        filterFrameToInsert.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
        filterFrameToInsert.setLayout(new BorderLayout());
        JPanel filterPanelToInsert = new JPanel();
        filterPanelToInsert.setLayout(new GridBagLayout());
        JButton filterButton = new JButton(localisation(Constants.FILTER_BUTTON));
        JLabel mistakeLabel = new JLabel(localisation(Constants.MISTAKE));
        mistakeLabel.setFont(MISTAKE_FONT);
        mistakeLabel.setForeground(Color.RED);
        filterButton.setFont(BUTTON_FONT);
        filterButton.setBackground(Color.orange);
        checkNameAndAuthor(filterPanelToInsert, filterButton, filterFrameToInsert, mistakeLabel, mainPanel, mainFrame);
        checkDistanceAndId(filterPanelToInsert, filterButton, filterFrameToInsert, mistakeLabel, mainPanel, mainFrame);
        checkLocation(filterPanelToInsert, filterButton, filterFrameToInsert, mistakeLabel, mainPanel, mainFrame);
        checkCoordinates(filterPanelToInsert, filterButton, filterFrameToInsert, mistakeLabel, mainPanel, mainFrame);
        checkCreationDate(filterPanelToInsert, filterButton, filterFrameToInsert, mistakeLabel, mainPanel, mainFrame);
        filterFrameToInsert.pack();
        filterFrameToInsert.setLocationRelativeTo(null);
        filterFrameToInsert.setResizable(false);
        filterFrameToInsert.setVisible(true);
    }

    private static void showResult(RouteTableModel routeTableModel, List<Route> resultOfSort, JPanel
            mainPanel, JFrame mainFrame, JScrollPane jScrollPane) {
        routeTableModel.clear();
        for (Route route : resultOfSort) {
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
        mainPanel.add(jScrollPane, new GridBagConstraints(0, 0, THREE, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.repaint();
    }


    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
