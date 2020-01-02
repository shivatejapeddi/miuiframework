package miui.push;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.Time;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Packet {
    protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase();
    private static String DEFAULT_XML_NS = null;
    public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
    public static final DateFormat XEP_0082_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static long id = 0;
    private static String prefix;
    private String chId = null;
    private XMPPError error = null;
    private String from = null;
    private List<CommonPacketExtension> packetExtensions = new CopyOnWriteArrayList();
    private String packetID = null;
    private final Map<String, Object> properties = new HashMap();
    private String to = null;
    private String xmlns = DEFAULT_XML_NS;

    public abstract String toXML();

    static {
        XEP_0082_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtils.randomString(5));
        stringBuilder.append("-");
        prefix = stringBuilder.toString();
    }

    public static synchronized String nextID() {
        String stringBuilder;
        synchronized (Packet.class) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(prefix);
            long j = id;
            id = 1 + j;
            stringBuilder2.append(Long.toString(j));
            stringBuilder = stringBuilder2.toString();
        }
        return stringBuilder;
    }

    public static void setDefaultXmlns(String defaultXmlns) {
        DEFAULT_XML_NS = defaultXmlns;
    }

    public Packet(Bundle b) {
        this.to = b.getString(PushConstants.EXTRA_TO);
        this.from = b.getString(PushConstants.EXTRA_FROM);
        this.chId = b.getString(PushConstants.EXTRA_CHID);
        this.packetID = b.getString(PushConstants.EXTRA_PACKET_ID);
        Parcelable[] extBundles = b.getParcelableArray(PushConstants.EXTRA_EXTENSIONS);
        if (extBundles != null) {
            this.packetExtensions = new ArrayList(extBundles.length);
            for (Parcelable p : extBundles) {
                CommonPacketExtension ext = CommonPacketExtension.parseFromBundle((Bundle) p);
                if (ext != null) {
                    this.packetExtensions.add(ext);
                }
            }
        }
        Bundle errBundle = b.getBundle(PushConstants.EXTRA_ERROR);
        if (errBundle != null) {
            this.error = new XMPPError(errBundle);
        }
    }

    public String getPacketID() {
        if (ID_NOT_AVAILABLE.equals(this.packetID)) {
            return null;
        }
        if (this.packetID == null) {
            this.packetID = nextID();
        }
        return this.packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    public String getChannelId() {
        return this.chId;
    }

    public void setChannelId(String appId) {
        this.chId = appId;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public XMPPError getError() {
        return this.error;
    }

    public void setError(XMPPError error) {
        this.error = error;
    }

    public synchronized Collection<CommonPacketExtension> getExtensions() {
        if (this.packetExtensions == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList(this.packetExtensions));
    }

    public CommonPacketExtension getExtension(String elementName) {
        return getExtension(elementName, null);
    }

    public CommonPacketExtension getExtension(String elementName, String namespace) {
        for (CommonPacketExtension ext : this.packetExtensions) {
            if ((namespace == null || namespace.equals(ext.getNamespace())) && elementName.equals(ext.getElementName())) {
                return ext;
            }
        }
        return null;
    }

    public void addExtension(CommonPacketExtension extension) {
        this.packetExtensions.add(extension);
    }

    public void removeExtension(CommonPacketExtension extension) {
        this.packetExtensions.remove(extension);
    }

    public synchronized Object getProperty(String name) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(name);
    }

    public synchronized void setProperty(String name, Object value) {
        if (value instanceof Serializable) {
            this.properties.put(name, value);
        } else {
            throw new IllegalArgumentException("Value must be serialiazble");
        }
    }

    public synchronized void deleteProperty(String name) {
        if (this.properties != null) {
            this.properties.remove(name);
        }
    }

    public synchronized Collection<String> getPropertyNames() {
        if (this.properties == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.xmlns)) {
            bundle.putString(PushConstants.EXTRA_EXTENSION_NAMESPACE, this.xmlns);
        }
        if (!TextUtils.isEmpty(this.from)) {
            bundle.putString(PushConstants.EXTRA_FROM, this.from);
        }
        if (!TextUtils.isEmpty(this.to)) {
            bundle.putString(PushConstants.EXTRA_TO, this.to);
        }
        if (!TextUtils.isEmpty(this.packetID)) {
            bundle.putString(PushConstants.EXTRA_PACKET_ID, this.packetID);
        }
        if (!TextUtils.isEmpty(this.chId)) {
            bundle.putString(PushConstants.EXTRA_CHID, this.chId);
        }
        XMPPError xMPPError = this.error;
        if (xMPPError != null) {
            bundle.putBundle(PushConstants.EXTRA_ERROR, xMPPError.toBundle());
        }
        List list = this.packetExtensions;
        if (list != null) {
            Bundle[] extBundle = new Bundle[list.size()];
            int i = 0;
            for (CommonPacketExtension ext : this.packetExtensions) {
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

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized String getExtensionsXML() {
        StringBuilder buf;
        buf = new StringBuilder();
        for (PacketExtension extension : getExtensions()) {
            buf.append(extension.toXML());
        }
        if (!(this.properties == null || this.properties.isEmpty())) {
            buf.append("<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">");
            for (String name : getPropertyNames()) {
                Object value = getProperty(name);
                buf.append("<property>");
                buf.append("<name>");
                buf.append(StringUtils.escapeForXML(name));
                buf.append("</name>");
                buf.append("<value type=\"");
                if (value instanceof Integer) {
                    buf.append("integer\">");
                    buf.append(value);
                    buf.append("</value>");
                } else if (value instanceof Long) {
                    buf.append("long\">");
                    buf.append(value);
                    buf.append("</value>");
                } else if (value instanceof Float) {
                    buf.append("float\">");
                    buf.append(value);
                    buf.append("</value>");
                } else if (value instanceof Double) {
                    buf.append("double\">");
                    buf.append(value);
                    buf.append("</value>");
                } else if (value instanceof Boolean) {
                    buf.append("boolean\">");
                    buf.append(value);
                    buf.append("</value>");
                } else if (value instanceof String) {
                    buf.append("string\">");
                    buf.append(StringUtils.escapeForXML((String) value));
                    buf.append("</value>");
                } else {
                    ByteArrayOutputStream byteStream = null;
                    ObjectOutputStream out = null;
                    try {
                        byteStream = new ByteArrayOutputStream();
                        out = new ObjectOutputStream(byteStream);
                        out.writeObject(value);
                        buf.append("java-object\">");
                        buf.append(StringUtils.encodeBase64(byteStream.toByteArray()));
                        buf.append("</value>");
                        try {
                            out.close();
                        } catch (Exception e) {
                        }
                        try {
                            byteStream.close();
                        } catch (Exception e2) {
                        }
                    } catch (Exception e3) {
                        try {
                            e3.printStackTrace();
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e4) {
                                }
                            }
                            if (byteStream != null) {
                                byteStream.close();
                            }
                        } catch (Throwable th) {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e5) {
                                }
                            }
                            if (byteStream != null) {
                                try {
                                    byteStream.close();
                                } catch (Exception e6) {
                                }
                            }
                        }
                    }
                }
                buf.append("</property>");
            }
            buf.append("</properties>");
        }
        return buf.toString();
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Packet packet = (Packet) o;
        XMPPError xMPPError = this.error;
        if (!xMPPError == null ? xMPPError.equals(packet.error) : packet.error == null) {
            return false;
        }
        String str = this.from;
        if (!str == null ? str.equals(packet.from) : packet.from == null) {
            return false;
        }
        if (!this.packetExtensions.equals(packet.packetExtensions)) {
            return false;
        }
        str = this.packetID;
        if (!str == null ? str.equals(packet.packetID) : packet.packetID == null) {
            return false;
        }
        str = this.chId;
        if (!str == null ? str.equals(packet.chId) : packet.chId == null) {
            return false;
        }
        Map map = this.properties;
        if (!map == null ? map.equals(packet.properties) : packet.properties == null) {
            return false;
        }
        str = this.to;
        if (!str == null ? str.equals(packet.to) : packet.to == null) {
            return false;
        }
        str = this.xmlns;
        if (str == null ? packet.xmlns != null : !str.equals(packet.xmlns)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.xmlns;
        int i = 0;
        int result = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.packetID;
        int hashCode = (result + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.to;
        result = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.from;
        hashCode = (result + (str2 != null ? str2.hashCode() : 0)) * 31;
        str2 = this.chId;
        result = (((((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + this.packetExtensions.hashCode()) * 31) + this.properties.hashCode()) * 31;
        XMPPError xMPPError = this.error;
        if (xMPPError != null) {
            i = xMPPError.hashCode();
        }
        return result + i;
    }
}
