package com.powerboat9.gconfigeditor.gui;

import com.powerboat9.gconfigeditor.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {
    JMenu file;
    JMenuItem loadConfig;
    JMenuItem export;

    JFileChooser configChooser;
    JFileChooser exportChooser;
    MainGUI mainFrame;

    public MainMenu(MainGUI mainFrameIn, MainPanel mainPanel) {
        super();
        mainFrame = mainFrameIn;
        configChooser = new JFileChooser();
        configChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        exportChooser = new JFileChooser();
        exportChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        loadConfig = new JMenuItem();
        loadConfig.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int r = configChooser.showDialog(mainFrame, "Save");
                if (r == JFileChooser.APPROVE_OPTION) {
                    Main.updateConfig(configChooser.getSelectedFile());
                    mainFrame.reloadConfig();
                }
            }
        });
        loadConfig.setText("Load Config");
        file.add(loadConfig);
        export = new JMenuItem();
        export.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = exportChooser.showDialog(mainFrame, "Save");
                if (r == JFileChooser.APPROVE_OPTION) {
                    Main.writeConfig(exportChooser.getSelectedFile());
                }
            }
        });
        export.setText("Export");
        file.add(export);
        this.add(file);
    }
}