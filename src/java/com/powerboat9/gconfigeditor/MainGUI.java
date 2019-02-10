package com.powerboat9.gconfigeditor;

import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.text.StyledEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MainGUI extends JFrame {
    private MainPanel menuPanel = null;

    public MainGUI() throws UnsupportedLookAndFeelException {
        super();
        {
            LookAndFeel old = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
                e.printStackTrace();
                UIManager.setLookAndFeel(old);
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        }
        this.setTitle("Galaxy Config Editor");
        this.setSize(800, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);menuPanel = new MainPanel();
        add(menuPanel);
        setJMenuBar(new MainMenu(this, menuPanel));
        this.setVisible(true);
    }
}

class MainMenu extends JMenuBar {
    JMenu file;
    JMenuItem loadConfig;

    JFileChooser configChooser;
    JFrame mainFrame;

    public MainMenu(JFrame mainFrameIn, MainPanel mainPanel) {
        super();
        mainFrame = mainFrameIn;
        configChooser = new JFileChooser();
        configChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        loadConfig = new JMenuItem();
        loadConfig.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int r = configChooser.showDialog(mainFrame, "Select");
                if (r == JFileChooser.APPROVE_OPTION) {
                    Main.updateConfig(configChooser.getSelectedFile());
                    mainPanel.loadFrom(Main.json);
                }
            }
        });
        loadConfig.setText("Load Config");
        file.add(loadConfig);
        this.add(file);
    }
}

class MainPanel extends JTabbedPane {
    public ConfigList motorMenu;

    public MainPanel() {
        super();
        motorMenu = new ConfigList();
        addTab("Motors", motorMenu);
    }

    public void loadFrom(JsonObject obj) {
        motorMenu.loadFrom(obj);
    }
}

class ConfigList extends JList<ConfigItem> {
    private ArrayList<ConfigItem> items = new ArrayList<>();

    private JLabel notFound = new JLabel("File could not be loaded");

    public ConfigList() {
        super();
    }

    public void loadFrom(JsonObject obj) {
        items.clear();
        if (obj == null) {
            this.add(notFound);
        }
    }
}

class ConfigItem extends JPanel {
}