package com.powerboat9.gconfigeditor.gui;

import javax.swing.*;

public class ViewPort extends JScrollPane {
    public MainPanel innerPanel;

    public ViewPort(MainPanel in) {
        super(in);
        innerPanel = in;
    }
}
