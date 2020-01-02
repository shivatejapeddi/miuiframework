package miui.push;

import android.os.Bundle;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class MiPushMessage {
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DESC = "description";
    private static final String KEY_EXTRA = "extra";
    private static final String KEY_MESSAGE_ID = "messageId";
    private static final String KEY_MESSAGE_TYPE = "messageType";
    private static final String KEY_NOTIFIED = "isNotified";
    private static final String KEY_NOTIFY_ID = "notifyId";
    private static final String KEY_NOTIFY_TYPE = "notifyType";
    private static final String KEY_PASS_THROUGH = "passThrough";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TOPIC = "topic";
    public static final int MESSAGE_TYPE_ALIAS = 1;
    public static final int MESSAGE_TYPE_REG = 0;
    public static final int MESSAGE_TYPE_TOPIC = 2;
    private static final long serialVersionUID = 1;
    private String alias;
    private String category;
    private String content;
    private String description;
    private HashMap<String, String> extra = new HashMap();
    private boolean isNotified;
    private String messageId;
    private int messageType;
    private int notifyId;
    private int notifyType;
    private int passThrough;
    private String title;
    private String topic;

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getNotifyType() {
        return this.notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public int getNotifyId() {
        return this.notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

    public boolean isNotified() {
        return this.isNotified;
    }

    public void setNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPassThrough() {
        return this.passThrough;
    }

    public void setPassThrough(int passThrough) {
        this.passThrough = passThrough;
    }

    public Map<String, String> getExtra() {
        return this.extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra.clear();
        if (extra != null) {
            this.extra.putAll(extra);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("messageId={");
        stringBuilder.append(this.messageId);
        stringBuilder.append("},passThrough={");
        stringBuilder.append(this.passThrough);
        stringBuilder.append("},alias={");
        stringBuilder.append(this.alias);
        stringBuilder.append("},topic={");
        stringBuilder.append(this.topic);
        stringBuilder.append("},content={");
        stringBuilder.append(this.content);
        stringBuilder.append("},description={");
        stringBuilder.append(this.description);
        stringBuilder.append("},title={");
        stringBuilder.append(this.title);
        stringBuilder.append("},isNotified={");
        stringBuilder.append(this.isNotified);
        stringBuilder.append("},notifyId={");
        stringBuilder.append(this.notifyId);
        stringBuilder.append("},notifyType={");
        stringBuilder.append(this.notifyType);
        stringBuilder.append("}, category={");
        stringBuilder.append(this.category);
        stringBuilder.append("}, extra={");
        stringBuilder.append(this.extra);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE_ID, this.messageId);
        bundle.putInt(KEY_PASS_THROUGH, this.passThrough);
        bundle.putInt(KEY_MESSAGE_TYPE, this.messageType);
        if (!TextUtils.isEmpty(this.alias)) {
            bundle.putString("alias", this.alias);
        }
        if (!TextUtils.isEmpty(this.topic)) {
            bundle.putString(KEY_TOPIC, this.topic);
        }
        bundle.putString("content", this.content);
        if (!TextUtils.isEmpty(this.description)) {
            bundle.putString("description", this.description);
        }
        if (!TextUtils.isEmpty(this.title)) {
            bundle.putString("title", this.title);
        }
        bundle.putBoolean(KEY_NOTIFIED, this.isNotified);
        bundle.putInt(KEY_NOTIFY_ID, this.notifyId);
        bundle.putInt(KEY_NOTIFY_TYPE, this.notifyType);
        if (!TextUtils.isEmpty(this.category)) {
            bundle.putString("category", this.category);
        }
        HashMap hashMap = this.extra;
        if (hashMap != null) {
            bundle.putSerializable(KEY_EXTRA, hashMap);
        }
        return bundle;
    }

    public static MiPushMessage fromBundle(Bundle bundle) {
        MiPushMessage message = new MiPushMessage();
        message.messageId = bundle.getString(KEY_MESSAGE_ID);
        message.messageType = bundle.getInt(KEY_MESSAGE_TYPE);
        message.passThrough = bundle.getInt(KEY_PASS_THROUGH);
        message.alias = bundle.getString("alias");
        message.topic = bundle.getString(KEY_TOPIC);
        message.content = bundle.getString("content");
        message.description = bundle.getString("description");
        message.title = bundle.getString("title");
        message.isNotified = bundle.getBoolean(KEY_NOTIFIED);
        message.notifyId = bundle.getInt(KEY_NOTIFY_ID);
        message.notifyType = bundle.getInt(KEY_NOTIFY_TYPE);
        message.category = bundle.getString("category");
        message.extra = (HashMap) bundle.getSerializable(KEY_EXTRA);
        return message;
    }
}
