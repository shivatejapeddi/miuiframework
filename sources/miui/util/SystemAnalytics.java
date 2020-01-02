package miui.util;

import android.content.Context;
import android.content.Intent;
import android.provider.UserDictionary.Words;
import android.text.TextUtils;
import android.util.Slog;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;

public class SystemAnalytics {
    public static final String CONFIGKEY_BOOT_SHUT = "systemserver_bootshuttime";
    private static final int LOGTYPE_EVENT = 0;
    private static final String SYSTEM_APP_ID = "systemserver";
    private static final String TAG = "SystemAnalytics";

    public static class Action {
        protected static final String ACTION = "_action_";
        protected static final String CATEGORY = "_category_";
        protected static final String EVENT_ID = "_event_id_";
        protected static final String LABEL = "_label_";
        protected static final String VALUE = "_value_";
        private JSONObject mContent = new JSONObject();
        private JSONObject mExtra = new JSONObject();
        private Set<String> sKeywords = new HashSet();

        public Action() {
            this.sKeywords.add(EVENT_ID);
            this.sKeywords.add(CATEGORY);
            this.sKeywords.add(ACTION);
            this.sKeywords.add(LABEL);
            this.sKeywords.add(VALUE);
        }

        /* Access modifiers changed, original: protected */
        public Action addEventId(String eventId) {
            addContent(EVENT_ID, (Object) eventId);
            return this;
        }

        public Action addParam(String key, JSONObject value) {
            ensureKey(key);
            addContent(key, (Object) value);
            return this;
        }

        public Action addParam(String key, int value) {
            ensureKey(key);
            addContent(key, value);
            return this;
        }

        public Action addParam(String key, long value) {
            ensureKey(key);
            addContent(key, value);
            return this;
        }

        public Action addParam(String key, String value) {
            ensureKey(key);
            addContent(key, (Object) value);
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public void addContent(String key, int value) {
            if (!TextUtils.isEmpty(key)) {
                try {
                    this.mContent.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addContent(String key, long value) {
            if (!TextUtils.isEmpty(key)) {
                try {
                    this.mContent.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addContent(String key, Object value) {
            if (!TextUtils.isEmpty(key)) {
                try {
                    this.mContent.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addContent(JSONObject json) {
            if (json != null) {
                Iterator it = json.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    ensureKey(key);
                    try {
                        this.mContent.put(key, json.get(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void addExtra(String key, String value) {
            try {
                this.mExtra.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void ensureKey(String key) {
            if (!TextUtils.isEmpty(key) && this.sKeywords.contains(key)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("this key ");
                stringBuilder.append(key);
                stringBuilder.append(" is built-in, please pick another key.");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: final */
        public final JSONObject getContent() {
            return this.mContent;
        }

        /* Access modifiers changed, original: final */
        public final JSONObject getExtra() {
            return this.mExtra;
        }
    }

    public static void trackSystem(Context context, String key, Action action) {
        String str = TAG;
        try {
            Intent intent = new Intent();
            intent.setClassName("com.miui.analytics", "com.miui.analytics.EventService");
            intent.putExtra("key", key != null ? key : "");
            intent.putExtra("content", action.getContent().toString());
            Slog.i(str, action.getContent().toString());
            intent.putExtra("extra", action.getExtra().toString());
            intent.putExtra(Words.APP_ID, SYSTEM_APP_ID);
            intent.putExtra("type", 0);
            context.startService(intent);
        } catch (Exception e) {
            Slog.e(str, "track system error!", e);
        }
    }
}
