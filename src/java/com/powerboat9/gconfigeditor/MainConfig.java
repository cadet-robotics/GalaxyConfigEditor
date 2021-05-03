package com.powerboat9.gconfigeditor;

import com.google.gson.JsonObject;

public class MainConfig {
    MotorConfig motorConfig;

    public MainConfig(JsonObject o) {
        if (o.has("motors") && o.isJsonObject("motors")) {
            motorConfig = new MotorConfig(o.get("motors").getAsJsonObject());
        } else {
            motorConfig = new MotorConfig(null);
        }
    }
}
