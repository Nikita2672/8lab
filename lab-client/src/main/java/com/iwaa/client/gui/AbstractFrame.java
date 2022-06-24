package com.iwaa.client.gui;

import com.iwaa.client.local.Local;
import com.iwaa.client.local.Localized;
import com.iwaa.client.local.Constants;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import static com.iwaa.client.gui.constants.SizeConstants.ICON_SIZE;
import static com.iwaa.client.gui.constants.SizeConstants.MENU_WIDTH;
import static com.iwaa.client.gui.constants.SizeConstants.TABLE_TEXT_SIZE;

public abstract class AbstractFrame implements Localized {

    private ResourceBundle resourceBundle;

    public AbstractFrame(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }


    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    protected JMenuBar createLanguage(Color textColor) {
        JMenu lang = new JMenu(localisation(Constants.LANGUAGE));
        lang.setFocusPainted(false);
        lang.setFocusable(false);
        lang.setContentAreaFilled(false);
        lang.setBorderPainted(false);
        lang.setPreferredSize(new Dimension(MENU_WIDTH, ICON_SIZE));
        lang.setFont(new Font("Safari", Font.PLAIN, TABLE_TEXT_SIZE));
        lang.setForeground(textColor);

        JMenuItem rus = new JMenuItem(localisation(Constants.RUSSIAN));
        rus.setName(Local.RUSSIAN);
        JMenuItem nor = new JMenuItem(localisation(Constants.NORWEGIAN));
        nor.setName(Local.NORWEGIAN);
        JMenuItem fr = new JMenuItem(localisation(Constants.FRENCH));
        fr.setName(Local.FRANCHE);
        JMenuItem en = new JMenuItem(localisation(Constants.ENGLISH));
        en.setName(Local.ENGLISH);

        changeMenuAndLAg(rus);
        changeMenuAndLAg(nor);
        changeMenuAndLAg(fr);
        changeMenuAndLAg(en);

        lang.add(rus);
        lang.add(nor);
        lang.add(fr);
        lang.add(en);

        JMenuBar language = new JMenuBar();
        language.setFocusable(false);
        language.add(lang);
        language.setOpaque(false);
        language.setForeground(textColor);
        language.setBorderPainted(false);
        return language;
    }

    private void changeMenuAndLAg(JMenuItem jMenuItem) {
        jMenuItem.addActionListener((ActionEvent e) -> {
            setResourceBundle(Local.LOCALS.get(jMenuItem.getName()));
            repaintFrame();
        });
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private void repaintFrame() {
        repaintForLanguage();
    }

    public abstract void repaintForLanguage();
}
