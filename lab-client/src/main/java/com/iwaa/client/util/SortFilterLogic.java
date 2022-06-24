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

    private static final HashMap<String, HashSet<String>> COLUMNS = new HashMap<>();
    private ResourceBundle resourceBundle;
    private JScrollPane jScrollPane;
    private RouteTableModel routeTableModel;
    private CommandListener commandListener;
    private String order;
    private String nameOfColumn;

    static {
        COLUMNS.put("Author", new HashSet<>(Arrays.asList("Author", "автор", "Auteur", "Forfatter")));
        COLUMNS.put("Id", new HashSet<>(Arrays.asList("Id", "айди", "ID", "ID")));
        COLUMNS.put("Name", new HashSet<>(Arrays.asList("Name", "имя", "Nom", "Navn")));
        COLUMNS.put("Coordinates", new HashSet<>(Arrays.asList("Coordinates", "координаты", "Coordonner", "Koordinere")));
        COLUMNS.put("Creation Date", new HashSet<>(Arrays.asList("Creation Date", "дата создания", "Date de Création", "opprettelsesdato")));
        COLUMNS.put("Location to", new HashSet<>(Arrays.asList("Location to", "локация куда", "Emplacement à", "Beliggenhet til")));
        COLUMNS.put("Location from", new HashSet<>(Arrays.asList("Location from", "локация откуда", "Emplacement à partir de", "Beliggenhet fra")));
        COLUMNS.put("Distance", new HashSet<>(Arrays.asList("Distance", "дистанция", "Distance", "Avstand")));
        COLUMNS.put("Ascending", new HashSet<>(Arrays.asList("Ascending", "по возрастанию", "Ascendant", "Stigende")));
    }

    public SortFilterLogic(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public static void doSort(String nameOfColumn, String order, CommandListener commandListener, RouteTableModel routeTableModel,
                              JPanel panel, JFrame frame, JScrollPane jScrollPane) {
        Collection<Route> routes = commandListener.runShow().getRoutes();
        boolean ascending;
        ascending = COLUMNS.get("Ascending").contains(order);

        List<Route> resultOfSort = new ArrayList<>();
        if (COLUMNS.get("Creation Date").contains(nameOfColumn)) {
            resultOfSort = creationDateSort(routes, ascending);
        }
        if (COLUMNS.get("Coordinates").contains(nameOfColumn)) {
            resultOfSort = coordinateSort(routes, ascending);
        }
        if (COLUMNS.get("Id").contains(nameOfColumn)) {
            resultOfSort = idSort(routes, ascending);
        }
        if (COLUMNS.get("Author").contains(nameOfColumn)) {
            resultOfSort = authorSort(routes, ascending);
        }
        if (COLUMNS.get("Name").contains(nameOfColumn)) {
            resultOfSort = nameSort(routes, ascending);
        }
        if (COLUMNS.get("Location to").contains(nameOfColumn)) {
            resultOfSort = toSort(routes, ascending);
        }
        if (COLUMNS.get("Location from").contains(nameOfColumn)) {
            resultOfSort = fromSort(routes, ascending);
        }
        if (COLUMNS.get("Distance").contains(nameOfColumn)) {
            resultOfSort = distanceSort(routes, ascending);
        }
        showResult(routeTableModel, resultOfSort, panel, frame, jScrollPane);
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

    private void checkNameAndAuthor(JPanel panel1, JButton jButton, JFrame jFrame, JLabel mistakeLabel,
                                    JPanel panel, JFrame frame) {
        if (COLUMNS.get("Name").contains(nameOfColumn) || COLUMNS.get("Author").contains(nameOfColumn)) {
            JLabel jLabel = new JLabel(localisation(Constants.STRING));
            jLabel.setFont(BUTTON_FONT);
            JTextField jTextField = new JTextField(NUMBER_OF_COLUMNS);
            jTextField.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(panel1, jLabel, 0, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jTextField, 1, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jButton, 1, 1);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jTextField.getText() == null || jTextField.getText().length() == 0) {
                        FabricOfComponents.setLocationOfComponent(panel1, mistakeLabel, 0, 1);
                        jFrame.add(panel1, BorderLayout.CENTER);
                        jFrame.setVisible(true);
                    } else {
                        String str = jTextField.getText();
                        Collection<Route> routes = commandListener.runShow().getRoutes();
                        List<Route> list;
                        if (">".equals(order)) {
                            if (COLUMNS.get("Name").contains(nameOfColumn)) {
                                list = routes.stream().filter(route -> (str.compareTo(route.getName()) > 0)).collect(Collectors.toList());
                            } else {
                                list = routes.stream().filter(route -> (str.compareTo(route.getAuthor()) > 0)).collect(Collectors.toList());
                            }
                        } else {
                            if ("<".equals(order)) {
                                if (COLUMNS.get("Name").contains(nameOfColumn)) {
                                    list = routes.stream().filter(route -> (str.compareTo(route.getName()) < 0)).collect(Collectors.toList());
                                } else {
                                    list = routes.stream().filter(route -> (str.compareTo(route.getAuthor()) < 0)).collect(Collectors.toList());
                                }
                            } else {
                                if (COLUMNS.get("Name").contains(nameOfColumn)) {
                                    list = routes.stream().filter(route -> (str.equals(route.getName()))).collect(Collectors.toList());
                                } else {
                                    list = routes.stream().filter(route -> (str.equals(route.getAuthor()))).collect(Collectors.toList());
                                }
                            }
                        }
                        showResult(routeTableModel, list, panel, frame, jScrollPane);
                        jFrame.dispose();
                    }
                }
            });
            jFrame.add(panel1, BorderLayout.CENTER);
        }
    }

    private void checkDistanceAndId(JPanel panel1, JButton jButton, JFrame jFrame, JLabel mistakeLabel,
                                    JPanel panel, JFrame frame) {
        if (COLUMNS.get("Distance").contains(nameOfColumn) || COLUMNS.get("Id").contains(nameOfColumn)) {
            JLabel jLabel = new JLabel(localisation(Constants.NUMBER));
            jLabel.setFont(BUTTON_FONT);
            JTextField jTextField = new JTextField(NUMBER_OF_COLUMNS);
            jTextField.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(panel1, jLabel, 0, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jTextField, 1, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jButton, 1, 1);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Long distanceOrId = Long.parseLong(jTextField.getText());
                        Collection<Route> routes = commandListener.runShow().getRoutes();
                        List<Route> list;
                        if (">".equals(order)) {
                            if (COLUMNS.get("Distance").contains(nameOfColumn)) {
                                list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getDistance()) < 0)).collect(Collectors.toList());
                            } else {
                                list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getId()) < 0)).collect(Collectors.toList());
                            }
                        } else {
                            if ("<".equals(order)) {
                                if (COLUMNS.get("Distance").contains(nameOfColumn)) {
                                    list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getDistance()) > 0)).collect(Collectors.toList());
                                } else {
                                    list = routes.stream().filter(route -> (distanceOrId.compareTo(route.getId()) > 0)).collect(Collectors.toList());
                                }
                            } else {
                                if (COLUMNS.get("Distance").contains(nameOfColumn)) {
                                    list = routes.stream().filter(route -> (distanceOrId.equals(route.getDistance()))).collect(Collectors.toList());
                                } else {
                                    list = routes.stream().filter(route -> (distanceOrId.equals(route.getId()))).collect(Collectors.toList());
                                }
                            }
                        }
                        showResult(routeTableModel, list, panel, frame, jScrollPane);
                        jFrame.dispose();
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(panel1, mistakeLabel, 0, 1);
                        jFrame.add(panel1, BorderLayout.CENTER);
                        jFrame.setVisible(true);
                    }
                }
            });
            jFrame.add(panel1, BorderLayout.CENTER);
        }
    }

    private void checkLocation(JPanel panel1, JButton jButton, JFrame jFrame, JLabel mistakeLabel,
                               JPanel panel, JFrame frame) {
        if (COLUMNS.get("Location from").contains(nameOfColumn) || COLUMNS.get("Location to").contains(nameOfColumn)) {
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
            FabricOfComponents.setLocationOfComponent(panel1, x, 0, 0);
            FabricOfComponents.setLocationOfComponent(panel1, y, 0, 1);
            FabricOfComponents.setLocationOfComponent(panel1, z, 0, 2);
            FabricOfComponents.setLocationOfComponent(panel1, jTextFieldX, 1, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jTextFieldY, 1, 1);
            FabricOfComponents.setLocationOfComponent(panel1, jTextFieldZ, 1, 2);
            FabricOfComponents.setLocationOfComponent(panel1, jButton, 1, THREE);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Location location = new Location(Long.parseLong(jTextFieldX.getText()),
                                Integer.parseInt(jTextFieldY.getText()),
                                Integer.parseInt(jTextFieldZ.getText()));
                        Collection<Route> routes = commandListener.runShow().getRoutes();
                        List<Route> list;
                        if (">".equals(order)) {
                            if (COLUMNS.get("Location from").contains(nameOfColumn)) {
                                list = routes.stream().filter(route -> (location.compareTo(route.getFrom()) < 0)).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            } else {
                                list = routes.stream().filter(route -> (location.compareTo(route.getTo()) < 0)).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            }
                        }
                        if ("<".equals(order)) {
                            if (COLUMNS.get("Location from").contains(nameOfColumn)) {
                                list = routes.stream().filter(route -> (location.compareTo(route.getFrom()) > 0)).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            } else {
                                list = routes.stream().filter(route -> (location.compareTo(route.getTo()) > 0)).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            }
                        } else {
                            if (COLUMNS.get("Location from").contains(nameOfColumn)) {
                                list = routes.stream().filter(route -> (location.equals(route.getFrom()))).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            } else {
                                list = routes.stream().filter(route -> (location.equals(route.getTo()))).collect(Collectors.toList());
                                showResult(routeTableModel, list, panel, frame, jScrollPane);
                            }
                        }
                        jFrame.dispose();
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(panel1, mistakeLabel, 0, THREE);
                        jFrame.add(panel1, BorderLayout.CENTER);
                        jFrame.setVisible(true);
                    }
                }
            });
            jFrame.add(panel1, BorderLayout.CENTER);
        }
    }

    private void checkCoordinates(JPanel panel1, JButton jButton, JFrame jFrame, JLabel mistakeLabel,
                                  JPanel panel, JFrame frame) {
        if (COLUMNS.get("Coordinates").contains(nameOfColumn)) {
            JLabel x = new JLabel(localisation(Constants.NUMBER_FLOAT));
            JLabel y = new JLabel(localisation(Constants.NUMBER_FLOAT));
            x.setFont(BUTTON_FONT);
            y.setFont(BUTTON_FONT);
            JTextField jTextFieldX = new JTextField(NUMBER_OF_COLUMNS);
            JTextField jTextFieldY = new JTextField(NUMBER_OF_COLUMNS);
            jTextFieldX.setFont(FIELD_FONT);
            jTextFieldY.setFont(FIELD_FONT);
            FabricOfComponents.setLocationOfComponent(panel1, x, 0, 0);
            FabricOfComponents.setLocationOfComponent(panel1, y, 0, 1);
            FabricOfComponents.setLocationOfComponent(panel1, jTextFieldX, 1, 0);
            FabricOfComponents.setLocationOfComponent(panel1, jTextFieldY, 1, 1);
            FabricOfComponents.setLocationOfComponent(panel1, jButton, 1, 2);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
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
                        showResult(routeTableModel, list, panel, frame, jScrollPane);
                        jFrame.dispose();
                    } catch (NumberFormatException ex) {
                        FabricOfComponents.setLocationOfComponent(panel1, mistakeLabel, 0, THREE);
                        jFrame.add(panel1, BorderLayout.CENTER);
                        jFrame.setVisible(true);
                    }
                }
            });
            jFrame.add(panel1, BorderLayout.CENTER);
        }
    }

    private void setComponents(JPanel panel1, JLabel dateLabel, JLabel dateLabelFormat, JTextField dateField, JButton jButton) {
        FabricOfComponents.setLocationOfComponent(panel1, dateLabel, 0, 0);
        FabricOfComponents.setLocationOfComponent(panel1, dateLabelFormat, 0, 1);
        FabricOfComponents.setLocationOfComponent(panel1, dateField, 1, 0);
        FabricOfComponents.setLocationOfComponent(panel1, jButton, 1, 2);
    }

    private void checkCreationDate(JPanel panel1, JButton jButton, JFrame jFrame, JLabel mistakeLabel,
                                   JPanel panel, JFrame frame) {
        if (COLUMNS.get("Creation Date").contains(nameOfColumn)) {
            JLabel dateLabel = new JLabel(localisation(Constants.DATE));
            JLabel dateLabelFormat = new JLabel(localisation(Constants.DATE_FORMAT));
            dateLabel.setFont(BUTTON_FONT);
            dateLabelFormat.setFont(MISTAKE_FONT);
            JTextField dateField = new JTextField(NUMBER_OF_COLUMNS);
            dateField.setFont(FIELD_FONT);
            setComponents(panel1, dateLabel, dateLabelFormat, dateField, jButton);
            jButton.addActionListener(new ActionListener() {
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
                        showResult(routeTableModel, list, panel, frame, jScrollPane);
                        jFrame.dispose();
                    } catch (ParseException ex) {
                        FabricOfComponents.setLocationOfComponent(panel1, mistakeLabel, 0, 2);
                        jFrame.add(panel1, BorderLayout.CENTER);
                        jFrame.setVisible(true);
                    }
                }
            });
            jFrame.add(panel1, BorderLayout.CENTER);
        }
    }

    public void doFilter(String columnName, String order1, CommandListener listener, RouteTableModel model,
                                JPanel panel, JFrame frame, JScrollPane pane) {
        this.order = order1;
        this.jScrollPane = pane;
        this.nameOfColumn = columnName;
        this.routeTableModel = model;
        this.commandListener = listener;
        JFrame jFrame = new JFrame(columnName + " " + order1);
        jFrame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE, STANDART_SQUARE_FRAME_SIZE));
        jFrame.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        JButton jButton = new JButton(localisation(Constants.FILTER_BUTTON));
        JLabel mistakeLabel = new JLabel(localisation(Constants.MISTAKE));
        mistakeLabel.setFont(MISTAKE_FONT);
        mistakeLabel.setForeground(Color.RED);
        jButton.setFont(BUTTON_FONT);
        jButton.setBackground(Color.orange);
        checkNameAndAuthor(panel1, jButton, jFrame, mistakeLabel, panel, frame);
        checkDistanceAndId(panel1, jButton, jFrame, mistakeLabel, panel, frame);
        checkLocation(panel1, jButton, jFrame, mistakeLabel, panel, frame);
        checkCoordinates(panel1, jButton, jFrame, mistakeLabel, panel, frame);
        checkCreationDate(panel1, jButton, jFrame, mistakeLabel, panel, frame);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    private static void showResult(RouteTableModel routeTableModel, List<Route> resultOfSort, JPanel
            panel, JFrame frame, JScrollPane jScrollPane) {
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
        panel.add(jScrollPane, new GridBagConstraints(0, 0, THREE, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();
    }


    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
