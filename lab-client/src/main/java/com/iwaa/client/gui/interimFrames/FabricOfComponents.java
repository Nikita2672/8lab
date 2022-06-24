package com.iwaa.client.gui.interimFrames;


import javax.swing.JPanel;
import javax.swing.JComponent;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public final class FabricOfComponents {

    private FabricOfComponents() {
    }

    public static void setLocationOfComponent(JPanel panel, JComponent component, int x, int y) {
        panel.add(component, new GridBagConstraints(x, y, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
    }
}
