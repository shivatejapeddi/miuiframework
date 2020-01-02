package miui.maml.util.net;

import android.content.ContentValues;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ObjectUtils {
    public static ContentValues mapToPairs(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        for (Entry<String, String> entry : map.entrySet()) {
            String value = (String) entry.getValue();
            values.put((String) entry.getKey(), value != null ? value : "");
        }
        return values;
    }

    public static Map<String, Object> jsonToMap(JSONObject jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap();
        Iterator iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            map.put(key, convertObj(jsonObj.opt(key)));
        }
        return map;
    }

    public static Object convertObjectToJson(Object obj) {
        if (obj instanceof List) {
            List<Object> objList = (List) obj;
            JSONArray array = new JSONArray();
            for (Object o : objList) {
                array.put(convertObjectToJson(o));
            }
            return array;
        } else if (!(obj instanceof Map)) {
            return obj;
        } else {
            JSONObject jobj = new JSONObject();
            Map objMap = (Map) obj;
            for (Object key : objMap.keySet()) {
                try {
                    jobj.put((String) key, convertObjectToJson(objMap.get(key)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jobj;
        }
    }

    public static Map<String, String> listToMap(Map<String, List<String>> listMap) {
        Map<String, String> map = new HashMap();
        if (listMap != null) {
            for (Entry<String, List<String>> entry : listMap.entrySet()) {
                String key = (String) entry.getKey();
                List<String> valueList = (List) entry.getValue();
                if (!(key == null || valueList == null || valueList.size() <= 0)) {
                    map.put(key, (String) valueList.get(0));
                }
            }
        }
        return map;
    }

    public static String flattenMap(Map<?, ?> map) {
        if (map == null) {
            return "null";
        }
        Set<? extends Entry<?, ?>> entries = map.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entry<?, ?> entry : entries) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            sb.append("(");
            sb.append(key);
            sb.append(",");
            sb.append(value);
            sb.append("),");
        }
        sb.append("}");
        return sb.toString();
    }

    private static Object convertObj(Object obj) {
        if (obj instanceof JSONObject) {
            return jsonToMap((JSONObject) obj);
        }
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            int size = array.length();
            List<Object> list = new ArrayList();
            for (int i = 0; i < size; i++) {
                list.add(convertObj(array.opt(i)));
            }
            return list;
        } else if (obj == JSONObject.NULL) {
            return null;
        } else {
            return obj;
        }
    }
}
