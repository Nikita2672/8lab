package com.iwaa.client.gui.VisualFrame;

import com.iwaa.client.gui.Table.RouteTable;
import com.iwaa.client.gui.interimFrames.FabricOfComponents;
import com.iwaa.client.gui.interimFrames.InsertFrame;
import com.iwaa.common.util.data.Route;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;


import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.FontConstants.BUTTON_FONT;
import static com.iwaa.client.gui.constants.FontConstants.MISTAKE_FONT;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.STANDART_SQUARE_FRAME_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.VISUAL_HEIGHT_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.VISUAL_WIDTH_SIZE;

public class Listener implements MouseListener {

    private int x;
    private int y;

    private final Map<Route, ArrayList<Point>> mapOfHeadBoxes;
    private final ResourceBundle resourceBundle;
    private final RouteTable routeTable;


    public Listener(Map<Route, ArrayList<Point>> mapOfHeadBoxes, ResourceBundle resourceBundle,
                    RouteTable routeTable) {
        this.resourceBundle = resourceBundle;
        this.routeTable = routeTable;
        this.mapOfHeadBoxes = mapOfHeadBoxes;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX() - VISUAL_WIDTH_SIZE / 2;
        y = -(e.getY() - VISUAL_HEIGHT_SIZE / 2);
        for (Route route : mapOfHeadBoxes.keySet()) {
            if ((mapOfHeadBoxes.get(route).get(0).getX() < x) & (mapOfHeadBoxes.get(route).get(0).getY() > y)) {
                if ((mapOfHeadBoxes.get(route).get(1).getX() > x) & (mapOfHeadBoxes.get(route).get(1).getY() < y)) {
                    showInfo(route);
                    break;
                }
            } else {
                System.out.println("x was clicked: " + x);
                System.out.println("y was clicked: " + y);
            }
        }
    }

    private void showInfo(Route route) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(STANDART_SQUARE_FRAME_SIZE / 2, STANDART_HEIGHT_SIZE / 2));
        frame.setLocationRelativeTo(null);
        JLabel info = new JLabel(route.toString());
        info.setFont(MISTAKE_FONT);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        FabricOfComponents.setLocationOfComponent(panel, info, 0, 0);
        if (routeTable.getLogin().equals(route.getAuthor())) {
            JButton update = new JButton("update");
            update.setFont(BUTTON_FONT);
            update.setBackground(Color.ORANGE);
            addUpdateListener(frame, update, route.getId());
            FabricOfComponents.setLocationOfComponent(panel, update, 0, 1);
        }
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void addUpdateListener(JFrame frame, JButton button, long id) {
        button.addActionListener(e -> {
            frame.dispose();
            InsertFrame insertFrame = new InsertFrame(resourceBundle);
            insertFrame.startInsertFrame(routeTable.getCommandListenerForTable(), "update" + " " + id, routeTable);
    });
}

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
