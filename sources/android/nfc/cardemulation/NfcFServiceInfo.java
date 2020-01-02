package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;

public final class NfcFServiceInfo implements Parcelable {
    public static final Creator<NfcFServiceInfo> CREATOR = new Creator<NfcFServiceInfo>() {
        public NfcFServiceInfo createFromParcel(Parcel source) {
            String dynamicSystemCode;
            String dynamicNfcid2;
            ResolveInfo info = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(source);
            String description = source.readString();
            String systemCode = source.readString();
            if (source.readInt() != 0) {
                dynamicSystemCode = source.readString();
            } else {
                dynamicSystemCode = null;
            }
            String nfcid2 = source.readString();
            if (source.readInt() != 0) {
                dynamicNfcid2 = source.readString();
            } else {
                dynamicNfcid2 = null;
            }
            return new NfcFServiceInfo(info, description, systemCode, dynamicSystemCode, nfcid2, dynamicNfcid2, source.readInt(), source.readString());
        }

        public NfcFServiceInfo[] newArray(int size) {
            return new NfcFServiceInfo[size];
        }
    };
    private static final String DEFAULT_T3T_PMM = "FFFFFFFFFFFFFFFF";
    static final String TAG = "NfcFServiceInfo";
    final String mDescription;
    String mDynamicNfcid2;
    String mDynamicSystemCode;
    final String mNfcid2;
    final ResolveInfo mService;
    final String mSystemCode;
    final String mT3tPmm;
    final int mUid;

    public NfcFServiceInfo(ResolveInfo info, String description, String systemCode, String dynamicSystemCode, String nfcid2, String dynamicNfcid2, int uid, String t3tPmm) {
        this.mService = info;
        this.mDescription = description;
        this.mSystemCode = systemCode;
        this.mDynamicSystemCode = dynamicSystemCode;
        this.mNfcid2 = nfcid2;
        this.mDynamicNfcid2 = dynamicNfcid2;
        this.mUid = uid;
        this.mT3tPmm = t3tPmm;
    }

    public NfcFServiceInfo(PackageManager pm, ResolveInfo info) throws XmlPullParserException, IOException {
        PackageManager packageManager = pm;
        ResolveInfo resolveInfo = info;
        ServiceInfo si = resolveInfo.serviceInfo;
        XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(packageManager, HostNfcFService.SERVICE_META_DATA);
            if (parser != null) {
                int eventType = parser.getEventType();
                while (eventType != 2 && eventType != 1) {
                    eventType = parser.next();
                }
                if ("host-nfcf-service".equals(parser.getName())) {
                    String str;
                    Resources res = packageManager.getResourcesForApplication(si.applicationInfo);
                    AttributeSet attrs = Xml.asAttributeSet(parser);
                    TypedArray sa = res.obtainAttributes(attrs, R.styleable.HostNfcFService);
                    this.mService = resolveInfo;
                    this.mDescription = sa.getString(0);
                    this.mDynamicSystemCode = null;
                    this.mDynamicNfcid2 = null;
                    sa.recycle();
                    String systemCode = null;
                    String nfcid2 = null;
                    String t3tPmm = null;
                    int depth = parser.getDepth();
                    while (true) {
                        int next = parser.next();
                        eventType = next;
                        str = WifiEnterpriseConfig.EMPTY_VALUE;
                        if (next == 3) {
                            next = depth;
                            if (parser.getDepth() <= next) {
                                break;
                            }
                        } else {
                            next = depth;
                        }
                        if (eventType == 1) {
                            break;
                        }
                        TypedArray a;
                        String systemCode2;
                        StringBuilder stringBuilder;
                        String tagName = parser.getName();
                        String str2 = TAG;
                        if (eventType == 2) {
                            if ("system-code-filter".equals(tagName) && systemCode == null) {
                                a = res.obtainAttributes(attrs, R.styleable.SystemCodeFilter);
                                systemCode2 = a.getString(0).toUpperCase();
                                if (NfcFCardEmulation.isValidSystemCode(systemCode2) || systemCode2.equalsIgnoreCase(str)) {
                                    systemCode = systemCode2;
                                } else {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Invalid System Code: ");
                                    stringBuilder.append(systemCode2);
                                    Log.e(str2, stringBuilder.toString());
                                    systemCode = null;
                                }
                                a.recycle();
                                packageManager = pm;
                                resolveInfo = info;
                                depth = next;
                            }
                        }
                        int i;
                        if (eventType == 2 && "nfcid2-filter".equals(tagName) && nfcid2 == null) {
                            a = res.obtainAttributes(attrs, R.styleable.Nfcid2Filter);
                            systemCode2 = a.getString(0).toUpperCase();
                            if (systemCode2.equalsIgnoreCase("RANDOM") || systemCode2.equalsIgnoreCase(str) || NfcFCardEmulation.isValidNfcid2(systemCode2)) {
                                nfcid2 = systemCode2;
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Invalid NFCID2: ");
                                stringBuilder.append(systemCode2);
                                Log.e(str2, stringBuilder.toString());
                                nfcid2 = null;
                            }
                            a.recycle();
                            packageManager = pm;
                            resolveInfo = info;
                            depth = next;
                        } else if (eventType == 2 && tagName.equals("t3tPmm-filter") && t3tPmm == null) {
                            TypedArray a2 = res.obtainAttributes(attrs, R.styleable.T3tPmmFilter);
                            t3tPmm = a2.getString(0).toUpperCase();
                            a2.recycle();
                            resolveInfo = info;
                            i = 2;
                            depth = next;
                            packageManager = pm;
                            next = 0;
                        } else {
                            resolveInfo = info;
                            i = 2;
                            depth = next;
                            packageManager = pm;
                            next = 0;
                        }
                    }
                    this.mSystemCode = systemCode == null ? str : systemCode;
                    if (nfcid2 != null) {
                        str = nfcid2;
                    }
                    this.mNfcid2 = str;
                    this.mT3tPmm = t3tPmm == null ? DEFAULT_T3T_PMM : t3tPmm;
                    parser.close();
                    this.mUid = si.applicationInfo.uid;
                    return;
                }
                throw new XmlPullParserException("Meta-data does not start with <host-nfcf-service> tag");
            }
            throw new XmlPullParserException("No android.nfc.cardemulation.host_nfcf_service meta-data");
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to create context for: ");
            stringBuilder2.append(si.packageName);
            throw new XmlPullParserException(stringBuilder2.toString());
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public String getSystemCode() {
        String str = this.mDynamicSystemCode;
        return str == null ? this.mSystemCode : str;
    }

    public void setOrReplaceDynamicSystemCode(String systemCode) {
        this.mDynamicSystemCode = systemCode;
    }

    public String getNfcid2() {
        String str = this.mDynamicNfcid2;
        return str == null ? this.mNfcid2 : str;
    }

    public void setOrReplaceDynamicNfcid2(String nfcid2) {
        this.mDynamicNfcid2 = nfcid2;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public int getUid() {
        return this.mUid;
    }

    public String getT3tPmm() {
        return this.mT3tPmm;
    }

    public CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    public String toString() {
        StringBuilder out = new StringBuilder("NfcFService: ");
        out.append(getComponent());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", description: ");
        stringBuilder.append(this.mDescription);
        out.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", System Code: ");
        stringBuilder.append(this.mSystemCode);
        out.append(stringBuilder.toString());
        if (this.mDynamicSystemCode != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(", dynamic System Code: ");
            stringBuilder.append(this.mDynamicSystemCode);
            out.append(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(", NFCID2: ");
        stringBuilder.append(this.mNfcid2);
        out.append(stringBuilder.toString());
        if (this.mDynamicNfcid2 != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(", dynamic NFCID2: ");
            stringBuilder.append(this.mDynamicNfcid2);
            out.append(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(", T3T PMM:");
        stringBuilder.append(this.mT3tPmm);
        out.append(stringBuilder.toString());
        return out.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NfcFServiceInfo)) {
            return false;
        }
        NfcFServiceInfo thatService = (NfcFServiceInfo) o;
        if (thatService.getComponent().equals(getComponent()) && thatService.mSystemCode.equalsIgnoreCase(this.mSystemCode) && thatService.mNfcid2.equalsIgnoreCase(this.mNfcid2) && thatService.mT3tPmm.equalsIgnoreCase(this.mT3tPmm)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getComponent().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mDescription);
        dest.writeString(this.mSystemCode);
        int i = 1;
        dest.writeInt(this.mDynamicSystemCode != null ? 1 : 0);
        String str = this.mDynamicSystemCode;
        if (str != null) {
            dest.writeString(str);
        }
        dest.writeString(this.mNfcid2);
        if (this.mDynamicNfcid2 == null) {
            i = 0;
        }
        dest.writeInt(i);
        str = this.mDynamicNfcid2;
        if (str != null) {
            dest.writeString(str);
        }
        dest.writeInt(this.mUid);
        dest.writeString(this.mT3tPmm);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    ");
        stringBuilder.append(getComponent());
        stringBuilder.append(" (Description: ");
        stringBuilder.append(getDescription());
        stringBuilder.append(")");
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("    System Code: ");
        stringBuilder.append(getSystemCode());
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("    NFCID2: ");
        stringBuilder.append(getNfcid2());
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("    T3tPmm: ");
        stringBuilder.append(getT3tPmm());
        pw.println(stringBuilder.toString());
    }
}
