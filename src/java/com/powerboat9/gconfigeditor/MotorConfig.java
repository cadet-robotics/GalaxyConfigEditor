package com.powerboat9.gconfigeditor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class MotorConfig {
    public ArrayList<SingleMotorConfig> motors = new ArrayList<>();;

    public MotorConfig(JsonElement e) throws Exception {
        try {
            JsonObject o = e.getAsJsonObject();
            if (!o.has("pwm") || !o.isJsonObject()) {
                return;
            } else {
                o = o.get("pwm").getAsJsonObject();
                for (String s : o.keySet()) {
                    try {
                        motors.add(new SingleMotorConfig(o.get(s).getAsJsonObject(), s));
                    } catch (IllegalStateException|NullPointerException ex) {
                        throw new Exception("[ERROR] " + s, ex);
                    }
                }
            }
        } catch (IllegalStateException ex) {
            Exception ex2 = new Exception(ex);
            ex2.printStackTrace();
            throw ex2;
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

class SingleMotorConfig {
    public int port;
    public String type;
    public String name;

    public SingleMotorConfig(int portIn, String typeIn, String nameIn) {
        port = portIn;
        type = typeIn;
        name = nameIn;
    }

    public SingleMotorConfig(JsonObject o, String name) {
        this(o.get("port").getAsInt(), o.get("type").getAsString(), name);
    }

    public JsonObject build() {
        JsonObject t = new JsonObject();
        t.add("port", new JsonPrimitive(port));
        t.add("type", new JsonPrimitive(type));
        return t;
    }
}