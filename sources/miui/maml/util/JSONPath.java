package miui.maml.util;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONPath {
    private static final String LOG_TAG = "JSONPath";
    private JSONObject mRoot;
    private JSONArray mRootArray;

    public JSONPath(JSONObject o) {
        this.mRoot = o;
    }

    public JSONPath(JSONArray o) {
        this.mRootArray = o;
    }

    public Object get(String path) {
        String str = LOG_TAG;
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        String[] segs = path.split("/");
        Object obj = this.mRoot;
        if (obj == null) {
            obj = this.mRootArray;
        }
        if (obj == null) {
            return null;
        }
        int i = 0;
        while (i < segs.length) {
            try {
                String seg = segs[i];
                if (!TextUtils.isEmpty(seg)) {
                    int index = -1;
                    int ind = seg.indexOf("[");
                    if (ind != -1) {
                        index = Integer.parseInt(seg.substring(ind + 1, seg.length() - 1));
                        seg = seg.substring(0, ind);
                    }
                    if ((obj instanceof JSONObject) && !TextUtils.isEmpty(seg)) {
                        obj = ((JSONObject) obj).get(seg);
                    }
                    if (obj instanceof JSONArray) {
                        JSONArray arr = (JSONArray) obj;
                        if (index == -1) {
                            break;
                        }
                        obj = arr.get(index);
                    }
                    if (obj == null || obj == JSONObject.NULL) {
                        return null;
                    }
                }
                i++;
            } catch (JSONException e) {
                Log.d(str, e.toString());
                return null;
            } catch (Exception e2) {
                Log.d(str, e2.toString());
                return null;
            }
        }
        return obj;
    }
}
