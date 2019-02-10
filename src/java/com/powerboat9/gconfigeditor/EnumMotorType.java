package com.powerboat9.gconfigeditor;

public enum EnumMotorType {
    victor,
    spark,
    talon,
    UNKNOWN;

    public static String[] typeNames;

    static {
        EnumMotorType[] t = EnumMotorType.values();
        typeNames = new String[t.length];
        for (int i = 0; i < t.length; i++) {
            typeNames[i] = t[i].name();
        }
    }
}