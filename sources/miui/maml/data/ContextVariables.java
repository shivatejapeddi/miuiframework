package miui.maml.data;

import android.graphics.Bitmap;
import java.util.HashMap;

public class ContextVariables {
    private HashMap<String, Object> mMap = new HashMap();

    public void setVar(String name, Object value) {
        this.mMap.put(name, value);
    }

    public Object getVar(String name) {
        return this.mMap.get(name);
    }

    public String getString(String name) {
        Object value = this.mMap.get(name);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    public Double getDouble(String name) {
        Object value = this.mMap.get(name);
        if (value != null && (value instanceof Double)) {
            return (Double) value;
        }
        return null;
    }

    public Integer getInt(String name) {
        Object value = this.mMap.get(name);
        if (value != null && (value instanceof Integer)) {
            return (Integer) value;
        }
        return null;
    }

    public Long getLong(String name) {
        Object value = this.mMap.get(name);
        if (value != null && (value instanceof Long)) {
            return (Long) value;
        }
        return null;
    }

    public Bitmap getBmp(String name) {
        Object value = this.mMap.get(name);
        if (value != null && (value instanceof Bitmap)) {
            return (Bitmap) value;
        }
        return null;
    }

    public void clear() {
        this.mMap.clear();
    }
}
