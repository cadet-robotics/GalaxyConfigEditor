package com.powerboat9.gconfigeditor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static JsonObject json = null;
    public static MotorConfig motorConfig = null;

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        if (args.length > 1) throw new IllegalArgumentException("Usage: (GalaxyConfig.jar) [file]");
        if (args.length == 1) updateConfig(new File(args[0]));
        MainGUI gui = new MainGUI();
    }

    private static JsonObject getConfig(File f) {
        try {
            return new JsonParser().parse(new FileReader(f)).getAsJsonObject();
        } catch (IllegalStateException | FileNotFoundException | JsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateConfig(File f) {
        System.out.println(f);
        if ((json = getConfig(f)) == null) {
            motorConfig = null;
            return;
        }
        try {
            motorConfig = new MotorConfig(json);
        } catch (Exception e) {
            e.printStackTrace();
            motorConfig = null;
        }
    }
}