package miui.push;

import android.os.Bundle;
import android.text.TextUtils;

public class Message extends Packet {
    public static final String MSG_TYPE_CHAT = "chat";
    public static final String MSG_TYPE_ERROR = "error";
    public static final String MSG_TYPE_GROUPCHAT = "groupchat";
    public static final String MSG_TYPE_HEADLINE = "hearline";
    public static final String MSG_TYPE_NORMAL = "normal";
    public static final String MSG_TYPE_PPL = "ppl";
    private String fseq;
    private String language;
    private String mAppId;
    private String mBody;
    private String mBodyEncoding;
    private boolean mEncrypted;
    private String mSubject;
    private boolean mTransient = false;
    private String mseq;
    private String seq;
    private String status;
    private String thread = null;
    private String type = null;

    public Message() {
        String str = "";
        this.seq = str;
        this.mseq = str;
        this.fseq = str;
        this.status = str;
        this.mEncrypted = false;
    }

    public Message(String to) {
        String str = "";
        this.seq = str;
        this.mseq = str;
        this.fseq = str;
        this.status = str;
        this.mEncrypted = false;
        setTo(to);
    }

    public Message(String to, String type) {
        String str = "";
        this.seq = str;
        this.mseq = str;
        this.fseq = str;
        this.status = str;
        this.mEncrypted = false;
        setTo(to);
        this.type = type;
    }

    public Message(Bundle bundle) {
        super(bundle);
        String str = "";
        this.seq = str;
        this.mseq = str;
        this.fseq = str;
        this.status = str;
        this.mEncrypted = false;
        this.type = bundle.getString(PushConstants.EXTRA_MESSAGE_TYPE);
        this.language = bundle.getString(PushConstants.EXTRA_MESSAGE_LANGUAGE);
        this.thread = bundle.getString(PushConstants.EXTRA_MESSAGE_THREAD);
        this.mSubject = bundle.getString(PushConstants.EXTRA_MESSAGE_SUBJECT);
        this.mBody = bundle.getString(PushConstants.EXTRA_MESSAGE_BODY);
        this.mBodyEncoding = bundle.getString(PushConstants.EXTRA_BODY_ENCODE);
        this.mAppId = bundle.getString(PushConstants.EXTRA_MESSAGE_APPID);
        this.mTransient = bundle.getBoolean(PushConstants.EXTRA_MESSAGE_TRANSIENT, false);
        this.seq = bundle.getString(PushConstants.EXTRA_MESSAGE_SEQ);
        this.mseq = bundle.getString(PushConstants.EXTRA_MESSAGE_MSEQ);
        this.fseq = bundle.getString(PushConstants.EXTRA_MESSAGE_FSEQ);
        this.status = bundle.getString(PushConstants.EXTRA_MESSAGE_STATUS);
    }

    public String getType() {
        return this.type;
    }

    public void setIsTransient(boolean isTransient) {
        this.mTransient = isTransient;
    }

    public String getAppId() {
        return this.mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getSeq() {
        return this.seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMSeq() {
        return this.mseq;
    }

    public void setMSeq(String mseq) {
        this.mseq = mseq;
    }

    public String getFSeq() {
        return this.fseq;
    }

    public void setFSeq(String fseq) {
        this.fseq = fseq;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEncrypted(boolean encrypted) {
        this.mEncrypted = encrypted;
    }

    public boolean getEncrypted() {
        return this.mEncrypted;
    }

    public String getSubject() {
        return this.mSubject;
    }

    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    public String getBody() {
        return this.mBody;
    }

    public String getBodyEncoding() {
        return this.mBodyEncoding;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public void setBody(String body, String encoding) {
        this.mBody = body;
        this.mBodyEncoding = encoding;
    }

    public String getThread() {
        return this.thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();
        if (!TextUtils.isEmpty(this.type)) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_TYPE, this.type);
        }
        String str = this.language;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_LANGUAGE, str);
        }
        str = this.mSubject;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_SUBJECT, str);
        }
        str = this.mBody;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_BODY, str);
        }
        if (!TextUtils.isEmpty(this.mBodyEncoding)) {
            bundle.putString(PushConstants.EXTRA_BODY_ENCODE, this.mBodyEncoding);
        }
        str = this.thread;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_THREAD, str);
        }
        str = this.mAppId;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_APPID, str);
        }
        if (this.mTransient) {
            bundle.putBoolean(PushConstants.EXTRA_MESSAGE_TRANSIENT, true);
        }
        if (!TextUtils.isEmpty(this.seq)) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_SEQ, this.seq);
        }
        if (!TextUtils.isEmpty(this.mseq)) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_MSEQ, this.mseq);
        }
        if (!TextUtils.isEmpty(this.fseq)) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_FSEQ, this.fseq);
        }
        if (!TextUtils.isEmpty(this.status)) {
            bundle.putString(PushConstants.EXTRA_MESSAGE_STATUS, this.status);
        }
        return bundle;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        String str = "\"";
        if (getXmlns() != null) {
            buf.append(" xmlns=\"");
            buf.append(getXmlns());
            buf.append(str);
        }
        if (this.language != null) {
            buf.append(" xml:lang=\"");
            buf.append(getLanguage());
            buf.append(str);
        }
        if (getPacketID() != null) {
            buf.append(" id=\"");
            buf.append(getPacketID());
            buf.append(str);
        }
        if (getTo() != null) {
            buf.append(" to=\"");
            buf.append(StringUtils.escapeForXML(getTo()));
            buf.append(str);
        }
        if (!TextUtils.isEmpty(getSeq())) {
            buf.append(" seq=\"");
            buf.append(getSeq());
            buf.append(str);
        }
        if (!TextUtils.isEmpty(getMSeq())) {
            buf.append(" mseq=\"");
            buf.append(getMSeq());
            buf.append(str);
        }
        if (!TextUtils.isEmpty(getFSeq())) {
            buf.append(" fseq=\"");
            buf.append(getFSeq());
            buf.append(str);
        }
        if (!TextUtils.isEmpty(getStatus())) {
            buf.append(" status=\"");
            buf.append(getStatus());
            buf.append(str);
        }
        if (getFrom() != null) {
            buf.append(" from=\"");
            buf.append(StringUtils.escapeForXML(getFrom()));
            buf.append(str);
        }
        if (getChannelId() != null) {
            buf.append(" chid=\"");
            buf.append(StringUtils.escapeForXML(getChannelId()));
            buf.append(str);
        }
        if (this.mTransient) {
            buf.append(" transient=\"true\"");
        }
        if (!TextUtils.isEmpty(this.mAppId)) {
            buf.append(" appid=\"");
            buf.append(getAppId());
            buf.append(str);
        }
        if (!TextUtils.isEmpty(this.type)) {
            buf.append(" type=\"");
            buf.append(this.type);
            buf.append(str);
        }
        if (this.mEncrypted) {
            buf.append(" s=\"1\"");
        }
        String str2 = ">";
        buf.append(str2);
        if (this.mSubject != null) {
            buf.append("<subject>");
            buf.append(StringUtils.escapeForXML(this.mSubject));
            buf.append("</subject>");
        }
        if (this.mBody != null) {
            buf.append("<body");
            if (!TextUtils.isEmpty(this.mBodyEncoding)) {
                buf.append(" encode=\"");
                buf.append(this.mBodyEncoding);
                buf.append(str);
            }
            buf.append(str2);
            buf.append(StringUtils.escapeForXML(this.mBody));
            buf.append("</body>");
        }
        if (this.thread != null) {
            buf.append("<thread>");
            buf.append(this.thread);
            buf.append("</thread>");
        }
        if ("error".equalsIgnoreCase(this.type)) {
            XMPPError error = getError();
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (!super.equals(message)) {
            return false;
        }
        String str = this.mBody;
        if (!str == null ? str.equals(message.mBody) : message.mBody == null) {
            return false;
        }
        str = this.language;
        if (!str == null ? str.equals(message.language) : message.language == null) {
            return false;
        }
        str = this.mSubject;
        if (!str == null ? str.equals(message.mSubject) : message.mSubject == null) {
            return false;
        }
        str = this.thread;
        if (!str == null ? str.equals(message.thread) : message.thread == null) {
            return false;
        }
        if (this.type != message.type) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.type;
        int i = 0;
        int result = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.mBody;
        int hashCode = (result + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.thread;
        result = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.language;
        hashCode = (result + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.mSubject;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }
}
