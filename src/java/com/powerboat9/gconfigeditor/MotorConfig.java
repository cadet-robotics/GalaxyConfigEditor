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
                        int motorNum = o.get("port").getAsInt();
                        String type = o.get("type").getAsString();
                        motors.add(new SingleMotorConfig(motorNum, type, s));
                    } catch (IllegalStateException ex) {
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

    public JsonObject build() {
        JsonObject pwm = new JsonObject();
        for (SingleMotorConfig c : motors) {
            JsonObject desc = new JsonObject();
            desc.add("port", new JsonPrimitive(c.port));
            pwm.add(c.name, new JsonObject());
        }
        return pwm;
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
}