package com.powerboat9.gconfigeditor.gui;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainGUI extends JFrame {
    private MainPanel menuPanel;
    private ViewPort viewPort;

    private boolean dirty = false;

    public MainGUI() {
        super();
        this.setTitle("Galaxy Config Editor");
        this.setSize(800, 640);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (MainGUI.this.isDirty()) {
                    if (JOptionPane.showConfirmDialog(MainGUI.this, "Unsaved Changes. Close Anyway?",
                            "Unsaved Changes",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
        });
        menuPanel = new MainPanel(this);
        viewPort = new ViewPort(menuPanel);
        add(viewPort);
        setJMenuBar(new MainMenu(this, menuPanel));
        this.setVisible(true);
    }

    public void reloadConfig() {
        menuPanel.reloadConfig();
    }

    public void markDirty() {
        dirty = true;
    }

    public void setUndirty() {
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setEditEnabled(boolean v) {
        viewPort.innerPanel.setEnabled(v);
    }

    public JsonObject build() {
        return menuPanel.build();
    }
}