package com.powerboat9.gconfigeditor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class MotorConfig {
    public ArrayList<SingleMotorConfig> motors = new ArrayList<>();;

    public MotorConfig(JsonObject e) throws Exception {
        for (String s : e.keySet()) {
            if (e.get())motors.add(new SingleMotorConfig(e.get(s).getAsJsonObject(), s));
        }
    }

    public void build(JsonObject o) {
        JsonObject pwm;
        if (o.has("pwm") && o.get("pwm").isJsonObject()) pwm = o.get("pwm").getAsJsonObject();
        else {
            pwm = new JsonObject();
            o.add("pwm", pwm);
        }
        for (SingleMotorConfig c : motors) pwm.add(c.name, c.build());
    }
}

