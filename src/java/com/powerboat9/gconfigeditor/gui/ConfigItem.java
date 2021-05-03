package com.powerboat9.gconfigeditor.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.powerboat9.gconfigeditor.EnumMotorType;
import com.powerboat9.gconfigeditor.SingleMotorConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigItem extends JPanel {
    public JTextField label;
    public JSpinner port;
    public JSpinner motorType;
    public JButton removeButton;

    public ConfigList parent;

    public SingleMotorConfig config;

    public ConfigItem(SingleMotorConfig con, ConfigList parentIn) {
        super();
        config = con;
        parent = parentIn;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBackground(new Color(200, 200, 200));
        label = new JTextField();
        label.setColumns(10);
        label.setText(con.name);
        label.addActionListener(e -> parent.markDirty());
        label.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                parent.markDirty();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                parent.markDirty();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                parent.markDirty();
            }
        });
        this.add(label);
        port = new JSpinner();
        SpinnerNumberModel model1 = new SpinnerNumberModel();
        model1.setMinimum(0);
        port.setModel(model1);
        port.addChangeListener(e -> parent.markDirty());
        port.setValue(con.port);
        this.add(port);
        motorType = new JSpinner();
        SpinnerListModel model2 = new SpinnerListModel(EnumMotorType.typeNames);
        motorType.setModel(model2);
        motorType.addChangeListener(e -> parent.markDirty());
        try {
            //System.out.println(EnumMotorType.valueOf(con.type));
            motorType.setValue(con.type);
        } catch (IllegalArgumentException | NullPointerException ex) {
            motorType.setValue("UNKNOWN");
        }
        ((JSpinner.ListEditor) motorType.getEditor()).getTextField().setColumns(7);
        this.add(motorType);
        removeButton = new JButton();
        removeButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentIn.removeItem(ConfigItem.this);
            }
        });
        removeButton.setText("-");
        this.add(removeButton);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d1 = super.getMaximumSize();
        Dimension d2 = getPreferredSize();
        return new Dimension(d1.width, d2.height);
    }

    @Override
    public void setEnabled(boolean v) {
        super.setEnabled(v);
        label.setEnabled(v);
        port.setEnabled(v);
        motorType.setEnabled(v);
    }

    public void build(JsonObject o) {
        if (isEnabled()) {
            throw new RuntimeException("Cannot build while enabled");
        }
        JsonObject ret = new JsonObject();
        ret.add("port", new JsonPrimitive((Integer) port.getValue()));
        ret.add("type", new JsonPrimitive((String) motorType.getValue()));
        o.add(label.getText(), ret);
    }
}
