package com.powerboat9.gconfigeditor;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SingleMotorConfig {
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
