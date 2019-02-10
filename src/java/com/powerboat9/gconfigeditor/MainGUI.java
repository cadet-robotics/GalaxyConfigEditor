package com.powerboat9.gconfigeditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MainGUI extends JFrame {
    private MainPanel menuPanel = null;

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
        add(menuPanel);
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
}

class MainMenu extends JMenuBar {
    JMenu file;
    JMenuItem loadConfig;

    JFileChooser configChooser;
    MainGUI mainFrame;

    public MainMenu(MainGUI mainFrameIn, MainPanel mainPanel) {
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
                    mainFrame.reloadConfig();
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
}

class ViewPort extends JScrollPane {
    public MainPanel parent;
    public ConfigList data;

    public ViewPort(MainPanel in) {
        super();
        this.setLayout(new ScrollPaneLayout());
        parent = in;
        data = new ConfigList(parent);
    }
}

class ConfigList extends JPanel {
    private ArrayList<ConfigItem> items = new ArrayList<>();

    private JLabel notFound = new JLabel("File could not be loaded");
    private MainPanel mainPanel;

    private JButton addButton;

    private boolean dirty;

    public ConfigList(MainPanel mainPanelIn) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        mainPanel = mainPanelIn;
        addButton = new JButton();
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
        this.add(addButton);
    }

    public void loadFrom(MotorConfig con) {
        for (ConfigItem c : items) this.remove(c);
        items.clear();
        if (con == null) {
            this.add(notFound);
        } else {
            this.remove(notFound);
            ConfigItem t;
            for (SingleMotorConfig c : con.motors) {
                t = new ConfigItem(c, this);
                items.add(t);
                this.add(t);
            }
        }
    }

    public void removeItem(ConfigItem i) {
        if (items.remove(i)) {
            this.remove(i);
            this.repaint();
            this.revalidate();
            markDirty();
        }
    }

    public void addItem(ConfigItem i) {
        items.add(i);
        this.add(i);
        this.repaint();
        this.revalidate();
        markDirty();
    }

    public void markDirty() {
        mainPanel.markDirty();
    }
}

class ConfigItem extends JPanel {
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
}