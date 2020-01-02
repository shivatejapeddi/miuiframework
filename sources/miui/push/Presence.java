package miui.push;

import android.os.Bundle;

public class Presence extends Packet {
    private Mode mode = null;
    private int priority = Integer.MIN_VALUE;
    private String status = null;
    private Type type = Type.available;

    public enum Mode {
        chat,
        available,
        away,
        xa,
        dnd
    }

    public enum Type {
        available,
        unavailable,
        subscribe,
        subscribed,
        unsubscribe,
        unsubscribed,
        error,
        probe
    }

    public Presence(Type type) {
        setType(type);
    }

    public Presence(Type type, String status, int priority, Mode mode) {
        setType(type);
        setStatus(status);
        setPriority(priority);
        setMode(mode);
    }

    public Presence(Bundle b) {
        super(b);
        String str = PushConstants.EXTRA_PRES_TYPE;
        if (b.containsKey(str)) {
            this.type = Type.valueOf(b.getString(str));
        }
        str = PushConstants.EXTRA_PRES_STATUS;
        if (b.containsKey(str)) {
            this.status = b.getString(str);
        }
        str = PushConstants.EXTRA_PRES_PRIORITY;
        if (b.containsKey(str)) {
            this.priority = b.getInt(str);
        }
        str = PushConstants.EXTRA_PRES_MODE;
        if (b.containsKey(str)) {
            this.mode = Mode.valueOf(b.getString(str));
        }
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();
        Type type = this.type;
        if (type != null) {
            bundle.putString(PushConstants.EXTRA_PRES_TYPE, type.toString());
        }
        String str = this.status;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_PRES_STATUS, str);
        }
        int i = this.priority;
        if (i != Integer.MIN_VALUE) {
            bundle.putInt(PushConstants.EXTRA_PRES_PRIORITY, i);
        }
        Mode mode = this.mode;
        if (!(mode == null || mode == Mode.available)) {
            bundle.putString(PushConstants.EXTRA_PRES_MODE, this.mode.toString());
        }
        return bundle;
    }

    public boolean isAvailable() {
        return this.type == Type.available;
    }

    public boolean isAway() {
        return this.type == Type.available && (this.mode == Mode.away || this.mode == Mode.xa || this.mode == Mode.dnd);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type != null) {
            this.type = type;
            return;
        }
        throw new NullPointerException("Type cannot be null");
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        if (priority < -128 || priority > 128) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Priority value ");
            stringBuilder.append(priority);
            stringBuilder.append(" is not valid. Valid range is -128 through 128.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.priority = priority;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<presence");
        String str = "\"";
        if (getXmlns() != null) {
            buf.append(" xmlns=\"");
            buf.append(getXmlns());
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
        if (this.type != null) {
            buf.append(" type=\"");
            buf.append(this.type);
            buf.append(str);
        }
        buf.append(">");
        if (this.status != null) {
            buf.append("<status>");
            buf.append(StringUtils.escapeForXML(this.status));
            buf.append("</status>");
        }
        if (this.priority != Integer.MIN_VALUE) {
            buf.append("<priority>");
            buf.append(this.priority);
            buf.append("</priority>");
        }
        Mode mode = this.mode;
        if (!(mode == null || mode == Mode.available)) {
            buf.append("<show>");
            buf.append(this.mode);
            buf.append("</show>");
        }
        buf.append(getExtensionsXML());
        XMPPError error = getError();
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.append("</presence>");
        return buf.toString();
    }
}
