package com.miui.whetstone.utils;

import com.miui.whetstone.WhetstoneAction;
import org.json.JSONException;
import org.json.JSONObject;

public class WhetstoneActionBuilder {
    private JSONObject content = new JSONObject();
    private String department;

    public WhetstoneActionBuilder(String key) {
        this.department = key;
    }

    public WhetstoneActionBuilder add(String key, int value) {
        try {
            this.content.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WhetstoneActionBuilder add(String key, long value) {
        try {
            this.content.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WhetstoneActionBuilder add(String key, String value) {
        try {
            this.content.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WhetstoneActionBuilder add(String key, JSONObject value) {
        try {
            this.content.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WhetstoneAction getAction() {
        return new WhetstoneAction(this.department, this.content);
    }
}
