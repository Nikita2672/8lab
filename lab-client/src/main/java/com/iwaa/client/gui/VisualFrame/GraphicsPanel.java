package com.iwaa.client.gui.VisualFrame;

import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.data.Route;


import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.iwaa.client.gui.constants.SizeConstants.HEAD_BOX_RANGE;

public class GraphicsPanel extends JPanel implements Runnable {


    protected static final int END_OF_ARROW = 5;
    protected static final int BEGINNING_OF_ARROW = 10;
    protected static final int TAIL = 15;

    private static final int TIME = 10;
    private static final int DEFAULT_X = 600;
    private static final int DEFAULT_Y = 600;

    private int xParam = DEFAULT_X;
    private int yParam = DEFAULT_Y;

    private Line horizontalLine;
    private Line verticalLine;
    private Dimension dimension;
    private CommandListener commandListener;

    private Map<Route, ArrayList<Point>> mapOfHeadBoxes = new HashMap<>();

    public GraphicsPanel(JFrame frame, CommandListener commandListener) {
        this.commandListener = commandListener;
        horizontalLine = new Line(0, frame.getSize().height / 2, frame.getSize().width, frame.getSize().height / 2);
        verticalLine = new Line(frame.getSize().width / 2, 0, frame.getSize().width / 2, frame.getSize().height);
        this.dimension = frame.getSize();
        new Thread(this).start();
    }

    public Map<Route, ArrayList<Point>> getMapOfHeadBoxes() {
        return mapOfHeadBoxes;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        xParam -= 1;
        yParam -= 1;
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(horizontalLine);
        g2.draw(verticalLine);
        Collection<Route> routes = commandListener.runShow().getRoutes();
        Set<Route> routes1 = new HashSet<>();
        for (Route route : routes) {
            if (xParam > route.getCoordinates().getX() || yParam > route.getCoordinates().getY()) {
                if (xParam > route.getCoordinates().getX() & yParam > route.getCoordinates().getY()) {
                    drawRoute(g2, xParam, yParam, route.getDistance(), Color.BLACK,
                            0);
                }
                if (xParam > route.getCoordinates().getX() & yParam <= route.getCoordinates().getY()) {
                    drawRoute(g2, xParam, route.getCoordinates().getY(), route.getDistance(), Color.BLACK,
                            0);
                }
                if (xParam <= route.getCoordinates().getX() & yParam > route.getCoordinates().getY()) {
                    drawRoute(g2, route.getCoordinates().getX(), yParam, route.getDistance(), Color.BLACK,
                            0);
                }
            } else {
                drawRoute(g2, route.getCoordinates().getX(), route.getCoordinates().getY(), route.getDistance(), Color.BLACK, 0);
                routes1.add(route);
            }
        }
        for (Route route: routes1) {
            ArrayList<Point> pointArrayList = new ArrayList<>();
            float x = route.getCoordinates().getX();
            float y = route.getCoordinates().getY();
            pointArrayList.add(new Point(x - route.getDistance(), y + HEAD_BOX_RANGE));
            pointArrayList.add(new Point(x, y - HEAD_BOX_RANGE));
            mapOfHeadBoxes.put(route, pointArrayList);
        }
    }

    public void drawRoute(Graphics2D g2, float a, float b, long distance, Color ownerColor, int alph) {
        float x = a + dimension.width / 2;
        float y = -(b - dimension.height / 2);
        Color color = new Color(ownerColor.getRed(), ownerColor.getGreen(), ownerColor.getBlue(), alph);
        g2.setPaint(Color.BLACK); //color
        g2.draw(new Line(x - distance, y, x, y));
        g2.draw(new Line(x - distance - END_OF_ARROW, y - END_OF_ARROW, x - distance, y));
        g2.draw(new Line(x - distance - END_OF_ARROW, y + END_OF_ARROW, x - distance, y));
        g2.draw(new Line(x - distance - END_OF_ARROW, y - END_OF_ARROW, x - distance - END_OF_ARROW, y + END_OF_ARROW));
        g2.draw(new Line(x - BEGINNING_OF_ARROW, y - BEGINNING_OF_ARROW, x, y));
        g2.draw(new Line(x - BEGINNING_OF_ARROW, y + BEGINNING_OF_ARROW, x, y));
        g2.draw(new Line(x - BEGINNING_OF_ARROW, y - TAIL, x + END_OF_ARROW, y));
        g2.draw(new Line(x - BEGINNING_OF_ARROW, y + TAIL, x + END_OF_ARROW, y));
    }

    @Override
    public void run() {
        while (true) {
            super.repaint();
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
