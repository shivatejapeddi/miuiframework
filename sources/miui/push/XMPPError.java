package miui.push;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMPPError {
    private List<CommonPacketExtension> applicationExtensions = null;
    private int code;
    private String condition;
    private String message;
    private String reason;
    private String type;

    public static class Condition {
        public static final Condition bad_request = new Condition("bad-request");
        public static final Condition conflict = new Condition("conflict");
        public static final Condition feature_not_implemented = new Condition("feature-not-implemented");
        public static final Condition forbidden = new Condition("forbidden");
        public static final Condition gone = new Condition("gone");
        public static final Condition interna_server_error = new Condition("internal-server-error");
        public static final Condition item_not_found = new Condition("item-not-found");
        public static final Condition jid_malformed = new Condition("jid-malformed");
        public static final Condition no_acceptable = new Condition("not-acceptable");
        public static final Condition not_allowed = new Condition("not-allowed");
        public static final Condition not_authorized = new Condition("not-authorized");
        public static final Condition payment_required = new Condition("payment-required");
        public static final Condition recipient_unavailable = new Condition("recipient-unavailable");
        public static final Condition redirect = new Condition("redirect");
        public static final Condition registration_required = new Condition("registration-required");
        public static final Condition remote_server_error = new Condition("remote-server-error");
        public static final Condition remote_server_not_found = new Condition("remote-server-not-found");
        public static final Condition remote_server_timeout = new Condition("remote-server-timeout");
        public static final Condition request_timeout = new Condition("request-timeout");
        public static final Condition resource_constraint = new Condition("resource-constraint");
        public static final Condition service_unavailable = new Condition("service-unavailable");
        public static final Condition subscription_required = new Condition("subscription-required");
        public static final Condition undefined_condition = new Condition("undefined-condition");
        public static final Condition unexpected_request = new Condition("unexpected-request");
        private String value;

        public Condition(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }

    public XMPPError(Condition condition) {
        init(condition);
        this.message = null;
    }

    public XMPPError(Condition condition, String messageText) {
        init(condition);
        this.message = messageText;
    }

    public XMPPError(int code) {
        this.code = code;
        this.message = null;
    }

    public XMPPError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public XMPPError(int code, String type, String reason, String condition, String message, List<CommonPacketExtension> extension) {
        this.code = code;
        this.type = type;
        this.reason = reason;
        this.condition = condition;
        this.message = message;
        this.applicationExtensions = extension;
    }

    public XMPPError(Bundle bundle) {
        this.code = bundle.getInt(PushConstants.EXTRA_ERROR_CODE);
        String str = PushConstants.EXTRA_ERROR_TYPE;
        if (bundle.containsKey(str)) {
            this.type = bundle.getString(str);
        }
        this.condition = bundle.getString(PushConstants.EXTRA_ERROR_CONDITION);
        this.reason = bundle.getString(PushConstants.EXTRA_ERROR_REASON);
        this.message = bundle.getString(PushConstants.EXTRA_ERROR_MESSAGE);
        Parcelable[] extBundles = bundle.getParcelableArray(PushConstants.EXTRA_EXTENSIONS);
        if (extBundles != null) {
            this.applicationExtensions = new ArrayList(extBundles.length);
            for (Parcelable p : extBundles) {
                CommonPacketExtension ext = CommonPacketExtension.parseFromBundle((Bundle) p);
                if (ext != null) {
                    this.applicationExtensions.add(ext);
                }
            }
        }
    }

    private void init(Condition condition) {
        this.condition = condition.value;
    }

    public String getCondition() {
        return this.condition;
    }

    public String getReason() {
        return this.reason;
    }

    public String getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        String str = this.type;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_ERROR_TYPE, str);
        }
        bundle.putInt(PushConstants.EXTRA_ERROR_CODE, this.code);
        str = this.reason;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_ERROR_REASON, str);
        }
        str = this.condition;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_ERROR_CONDITION, str);
        }
        str = this.message;
        if (str != null) {
            bundle.putString(PushConstants.EXTRA_ERROR_MESSAGE, str);
        }
        List list = this.applicationExtensions;
        if (list != null) {
            Bundle[] extBundle = new Bundle[list.size()];
            int i = 0;
            for (CommonPacketExtension ext : this.applicationExtensions) {
                Bundle subBundle = ext.toBundle();
                if (subBundle != null) {
                    int i2 = i + 1;
                    extBundle[i] = subBundle;
                    i = i2;
                }
            }
            bundle.putParcelableArray(PushConstants.EXTRA_EXTENSIONS, extBundle);
        }
        return bundle;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<error code=\"");
        buf.append(this.code);
        String str = "\"";
        buf.append(str);
        if (this.type != null) {
            buf.append(" type=\"");
            buf.append(this.type);
            buf.append(str);
        }
        if (this.reason != null) {
            buf.append(" reason=\"");
            buf.append(this.reason);
            buf.append(str);
        }
        buf.append(">");
        if (this.condition != null) {
            buf.append("<");
            buf.append(this.condition);
            buf.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
        }
        if (this.message != null) {
            buf.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
            buf.append(this.message);
            buf.append("</text>");
        }
        for (PacketExtension element : getExtensions()) {
            buf.append(element.toXML());
        }
        buf.append("</error>");
        return buf.toString();
    }

    public String toString() {
        StringBuilder txt = new StringBuilder();
        String str = this.condition;
        if (str != null) {
            txt.append(str);
        }
        txt.append("(");
        txt.append(this.code);
        txt.append(")");
        if (this.message != null) {
            txt.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            txt.append(this.message);
        }
        return txt.toString();
    }

    public synchronized List<CommonPacketExtension> getExtensions() {
        if (this.applicationExtensions == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.applicationExtensions);
    }

    /* JADX WARNING: Missing block: B:20:0x0037, code skipped:
            return null;
     */
    public synchronized miui.push.PacketExtension getExtension(java.lang.String r5, java.lang.String r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.applicationExtensions;	 Catch:{ all -> 0x0038 }
        r1 = 0;
        if (r0 == 0) goto L_0x0036;
    L_0x0006:
        if (r5 == 0) goto L_0x0036;
    L_0x0008:
        if (r6 != 0) goto L_0x000b;
    L_0x000a:
        goto L_0x0036;
    L_0x000b:
        r0 = r4.applicationExtensions;	 Catch:{ all -> 0x0038 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0038 }
    L_0x0011:
        r2 = r0.hasNext();	 Catch:{ all -> 0x0038 }
        if (r2 == 0) goto L_0x0034;
    L_0x0017:
        r2 = r0.next();	 Catch:{ all -> 0x0038 }
        r2 = (miui.push.PacketExtension) r2;	 Catch:{ all -> 0x0038 }
        r3 = r2.getElementName();	 Catch:{ all -> 0x0038 }
        r3 = r5.equals(r3);	 Catch:{ all -> 0x0038 }
        if (r3 == 0) goto L_0x0033;
    L_0x0027:
        r3 = r2.getNamespace();	 Catch:{ all -> 0x0038 }
        r3 = r6.equals(r3);	 Catch:{ all -> 0x0038 }
        if (r3 == 0) goto L_0x0033;
    L_0x0031:
        monitor-exit(r4);
        return r2;
    L_0x0033:
        goto L_0x0011;
    L_0x0034:
        monitor-exit(r4);
        return r1;
    L_0x0036:
        monitor-exit(r4);
        return r1;
    L_0x0038:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.push.XMPPError.getExtension(java.lang.String, java.lang.String):miui.push.PacketExtension");
    }

    public synchronized void addExtension(CommonPacketExtension extension) {
        if (this.applicationExtensions == null) {
            this.applicationExtensions = new ArrayList();
        }
        this.applicationExtensions.add(extension);
    }

    public synchronized void setExtension(List<CommonPacketExtension> extension) {
        this.applicationExtensions = extension;
    }
}
