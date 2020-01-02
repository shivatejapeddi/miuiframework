package miui.push;

import android.text.TextUtils;
import com.google.android.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PushAttributes {
    private final Map<String, String> mAttrs = Maps.newHashMap();

    public PushAttributes(Map<String, String> maps) {
        this.mAttrs.putAll(maps);
    }

    public String get(String key) {
        return (String) this.mAttrs.get(key);
    }

    public String toPlain() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : this.mAttrs.entrySet()) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append((String) entry.getKey());
            sb.append(':');
            sb.append((String) entry.getValue());
        }
        return sb.toString();
    }

    public String toString() {
        return toPlain();
    }

    public static PushAttributes parse(String plain) {
        HashMap<String, String> maps = Maps.newHashMap();
        if (!TextUtils.isEmpty(plain)) {
            for (String attr : plain.split(",")) {
                String[] pair = attr.split(":");
                if (pair.length == 2) {
                    maps.put(pair[0], pair[1]);
                }
            }
        }
        return new PushAttributes(maps);
    }
}
