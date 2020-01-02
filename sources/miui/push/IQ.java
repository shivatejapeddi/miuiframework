package miui.push;

import android.os.Bundle;

public class IQ extends Packet {
    private Type type = Type.GET;

    public static class Type {
        public static final Type ERROR = new Type("error");
        public static final Type GET = new Type("get");
        public static final Type RESULT = new Type("result");
        public static final Type SET = new Type("set");
        private String value;

        public static Type fromString(String type) {
            if (type == null) {
                return null;
            }
            type = type.toLowerCase();
            if (GET.toString().equals(type)) {
                return GET;
            }
            if (SET.toString().equals(type)) {
                return SET;
            }
            if (ERROR.toString().equals(type)) {
                return ERROR;
            }
            if (RESULT.toString().equals(type)) {
                return RESULT;
            }
            return null;
        }

        private Type(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    public IQ(Bundle bundle) {
        super(bundle);
        String str = PushConstants.EXTRA_IQ_TYPE;
        if (bundle.containsKey(str)) {
            this.type = Type.fromString(bundle.getString(str));
        }
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type == null) {
            this.type = Type.GET;
        } else {
            this.type = type;
        }
    }

    public Bundle toBundle() {
        Bundle bundle = super.toBundle();
        Type type = this.type;
        if (type != null) {
            bundle.putString(PushConstants.EXTRA_IQ_TYPE, type.toString());
        }
        return bundle;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<iq ");
        String str = "\" ";
        if (getPacketID() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("id=\"");
            stringBuilder.append(getPacketID());
            stringBuilder.append(str);
            buf.append(stringBuilder.toString());
        }
        if (getTo() != null) {
            buf.append("to=\"");
            buf.append(StringUtils.escapeForXML(getTo()));
            buf.append(str);
        }
        if (getFrom() != null) {
            buf.append("from=\"");
            buf.append(StringUtils.escapeForXML(getFrom()));
            buf.append(str);
        }
        if (getChannelId() != null) {
            buf.append("chid=\"");
            buf.append(StringUtils.escapeForXML(getChannelId()));
            buf.append(str);
        }
        if (this.type == null) {
            buf.append("type=\"get\">");
        } else {
            buf.append("type=\"");
            buf.append(getType());
            buf.append("\">");
        }
        String queryXML = getChildElementXML();
        if (queryXML != null) {
            buf.append(queryXML);
        }
        buf.append(getExtensionsXML());
        XMPPError error = getError();
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.append("</iq>");
        return buf.toString();
    }

    public String getChildElementXML() {
        return null;
    }

    public static IQ createResultIQ(IQ request) {
        if (request.getType() == Type.GET || request.getType() == Type.SET) {
            IQ result = new IQ() {
                public String getChildElementXML() {
                    return null;
                }
            };
            result.setType(Type.RESULT);
            result.setPacketID(request.getPacketID());
            result.setFrom(request.getTo());
            result.setTo(request.getFrom());
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IQ must be of type 'set' or 'get'. Original IQ: ");
        stringBuilder.append(request.toXML());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static IQ createErrorResponse(IQ request, XMPPError error) {
        if (request.getType() == Type.GET || request.getType() == Type.SET) {
            IQ result = new IQ() {
                public String getChildElementXML() {
                    return IQ.this.getChildElementXML();
                }
            };
            result.setType(Type.ERROR);
            result.setPacketID(request.getPacketID());
            result.setFrom(request.getTo());
            result.setTo(request.getFrom());
            result.setError(error);
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IQ must be of type 'set' or 'get'. Original IQ: ");
        stringBuilder.append(request.toXML());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
