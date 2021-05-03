package com.powerboat9.gconfigeditor.gui;

import com.google.gson.JsonObject;
import com.powerboat9.gconfigeditor.Main;

import javax.swing.*;

public class MainPanel extends JTabbedPane {
    public ConfigList motorMenu;

    MainGUI mainWindow;

    public MainPanel(MainGUI mainWindowIn) {
        super();
        mainWindow = mainWindowIn;
        motorMenu = new ConfigList(this);
        if (Main.json != null) motorMenu.loadFrom(Main.motorConfig);
        addTab("Motors", motorMenu);
    }

    public void reloadConfig() {
        motorMenu.loadFrom(Main.motorConfig);
        this.repaint();
        this.revalidate();
    }

    public void markDirty() {
        mainWindow.markDirty();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        motorMenu.setEnabled(enabled);
    }

    public JsonObject build() {
        JsonObject o = new JsonObject();
        o.add("motors", motorMenu.build());
        return o;
    }
}