package com.powerboat9.gconfigeditor.gui;

import com.google.gson.JsonObject;
import com.powerboat9.gconfigeditor.MotorConfig;
import com.powerboat9.gconfigeditor.SingleMotorConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ConfigList extends JPanel {
    private ArrayList<ConfigItem> items = new ArrayList<>();

    private JLabel notFound = new JLabel("File could not be loaded");
    private MainPanel mainPanel;

    private JButton addButton;

    private GridBagLayout lay;

    public static GridBagConstraints con = new GridBagConstraints();

    static {
        con.fill = GridBagConstraints.HORIZONTAL;
        con.weightx = 1;
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.anchor = GridBagConstraints.PAGE_START;
    }

    public static GridBagConstraints conEnd = new GridBagConstraints();

    static {
        conEnd.fill = GridBagConstraints.HORIZONTAL;
        conEnd.weightx = 1;
        conEnd.weighty = 1;
        conEnd.gridwidth = GridBagConstraints.REMAINDER;
        conEnd.anchor = GridBagConstraints.PAGE_START;
    }

    private JPanel endPanel;

    public ConfigList(MainPanel mainPanelIn) {
        super();
        lay = new GridBagLayout();
        this.setLayout(lay);
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        mainPanel = mainPanelIn;
        endPanel = new JPanel();
        addButton = new JButton();
        //addButton.setPreferredSize(new Dimension(addButton.getMaximumSize().width, addButton.getPreferredSize().height));
        addButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = null;
                int n = 0;
                boolean notOk = true;
                while (notOk) {
                    notOk = false;
                    s = "motor" + (++n);
                    for (ConfigItem t : items) {
                        if (t.config.name.equals(s)) {
                            notOk = true;
                            break;
                        }
                    }
                }
                addItem(new ConfigItem(new SingleMotorConfig(0, "victor", s), ConfigList.this));
            }
        });
        addButton.setText("+");
        lay.addLayoutComponent(addButton, con);
        this.add(addButton);
        lay.addLayoutComponent(endPanel, conEnd);
        this.add(endPanel);
    }

    public void loadFrom(MotorConfig mcon) {
        this.remove(endPanel);
        lay.removeLayoutComponent(endPanel);
        for (ConfigItem c : items) this.remove(c);
        items.clear();
        if (mcon == null) {
            this.add(notFound);
        } else {
            this.remove(notFound);
            ConfigItem t;
            for (SingleMotorConfig c : mcon.motors) {
                t = new ConfigItem(c, this);
                lay.addLayoutComponent(t, con);
                items.add(t);
                this.add(t);
            }
        }
        lay.addLayoutComponent(endPanel, conEnd);
        this.add(endPanel);
    }

    public void removeItem(ConfigItem i) {
        if (items.remove(i)) {
            this.remove(i);
            lay.removeLayoutComponent(i);
            this.repaint();
            this.revalidate();
            markDirty();
        }
    }

    public void addItem(ConfigItem i) {
        items.add(i);
        lay.addLayoutComponent(i, con);
        this.remove(endPanel);
        lay.removeLayoutComponent(endPanel);
        this.add(i);
        lay.addLayoutComponent(endPanel, conEnd);
        this.add(endPanel);
        this.repaint();
        this.revalidate();
        markDirty();
    }

    public void markDirty() {
        mainPanel.markDirty();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (ConfigItem i : items) i.setEnabled(enabled);
    }

    public JsonObject build() {
        JsonObject o = new JsonObject();
        for (ConfigItem i : items) i.build(o);
        return o;
    }
}