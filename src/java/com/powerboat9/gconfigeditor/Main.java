package com.powerboat9.gconfigeditor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static JsonObject json = null;
    public static MotorConfig motorConfig = null;

    public static void main(String[] args) throws UnsupportedLookAndFeelException, FileNotFoundException {
        MainGUI gui = new MainGUI();
    }

    private static JsonObject getConfig(File f) {
        JsonParser p = new JsonParser();
        try {
            return p.parse(new FileReader(f)).getAsJsonObject();
        } catch (IllegalStateException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateConfig(File f) {
        json = getConfig(f);
        try {
            motorConfig = new MotorConfig(json);
        } catch (Exception e) {
            e.printStackTrace();
            motorConfig = null;
        }
    }
}
