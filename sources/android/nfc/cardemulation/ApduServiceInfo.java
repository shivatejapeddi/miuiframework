package android.nfc.cardemulation;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class ApduServiceInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<ApduServiceInfo> CREATOR = new Creator<ApduServiceInfo>() {
        public ApduServiceInfo createFromParcel(Parcel source) {
            Parcel parcel = source;
            ResolveInfo info = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(parcel);
            String description = source.readString();
            boolean onHost = source.readInt() != 0;
            String offHostName = source.readString();
            String staticOffHostName = source.readString();
            ArrayList<AidGroup> staticAidGroups = new ArrayList();
            if (source.readInt() > 0) {
                parcel.readTypedList(staticAidGroups, AidGroup.CREATOR);
            }
            ArrayList<AidGroup> dynamicAidGroups = new ArrayList();
            if (source.readInt() > 0) {
                parcel.readTypedList(dynamicAidGroups, AidGroup.CREATOR);
            }
            return new ApduServiceInfo(info, description, staticAidGroups, dynamicAidGroups, source.readInt() != 0, source.readInt(), source.readInt(), source.readString(), offHostName, staticOffHostName);
        }

        public ApduServiceInfo[] newArray(int size) {
            return new ApduServiceInfo[size];
        }
    };
    static final String TAG = "ApduServiceInfo";
    protected int mBannerResourceId;
    protected String mDescription;
    @UnsupportedAppUsage
    protected HashMap<String, AidGroup> mDynamicAidGroups;
    String mOffHostName;
    protected boolean mOnHost;
    protected boolean mRequiresDeviceUnlock;
    @UnsupportedAppUsage
    protected ResolveInfo mService;
    protected String mSettingsActivityName;
    @UnsupportedAppUsage
    protected HashMap<String, AidGroup> mStaticAidGroups;
    final String mStaticOffHostName;
    protected int mUid;

    @UnsupportedAppUsage
    public ApduServiceInfo(ResolveInfo info, String description, ArrayList<AidGroup> staticAidGroups, ArrayList<AidGroup> dynamicAidGroups, boolean requiresUnlock, int bannerResource, int uid, String settingsActivityName, String offHost, String staticOffHost) {
        AidGroup aidGroup;
        this.mService = info;
        this.mDescription = description;
        this.mStaticAidGroups = new HashMap();
        this.mDynamicAidGroups = new HashMap();
        this.mOffHostName = offHost;
        this.mStaticOffHostName = staticOffHost;
        this.mOnHost = offHost == null;
        this.mRequiresDeviceUnlock = requiresUnlock;
        Iterator it = staticAidGroups.iterator();
        while (it.hasNext()) {
            aidGroup = (AidGroup) it.next();
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        it = dynamicAidGroups.iterator();
        while (it.hasNext()) {
            aidGroup = (AidGroup) it.next();
            this.mDynamicAidGroups.put(aidGroup.category, aidGroup);
        }
        this.mBannerResourceId = bannerResource;
        this.mUid = uid;
        this.mSettingsActivityName = settingsActivityName;
    }

    @UnsupportedAppUsage
    public ApduServiceInfo(PackageManager pm, ResolveInfo info, boolean onHost) throws XmlPullParserException, IOException {
        int i;
        String tagName;
        PackageManager packageManager = pm;
        ResolveInfo resolveInfo = info;
        boolean z = onHost;
        ServiceInfo si = resolveInfo.serviceInfo;
        XmlResourceParser parser = null;
        if (z) {
            try {
                parser = si.loadXmlMetaData(packageManager, HostApduService.SERVICE_META_DATA);
                if (parser == null) {
                    throw new XmlPullParserException("No android.nfc.cardemulation.host_apdu_service meta-data");
                }
            } catch (NameNotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to create context for: ");
                stringBuilder.append(si.packageName);
                throw new XmlPullParserException(stringBuilder.toString());
            } catch (Throwable th) {
                if (parser != null) {
                    parser.close();
                }
            }
        } else {
            parser = si.loadXmlMetaData(packageManager, OffHostApduService.SERVICE_META_DATA);
            if (parser == null) {
                throw new XmlPullParserException("No android.nfc.cardemulation.off_host_apdu_service meta-data");
            }
        }
        int eventType = parser.getEventType();
        while (true) {
            i = 2;
            if (eventType == 2 || eventType == 1) {
                tagName = parser.getName();
            } else {
                eventType = parser.next();
            }
        }
        tagName = parser.getName();
        if (z) {
            if (!"host-apdu-service".equals(tagName)) {
                throw new XmlPullParserException("Meta-data does not start with <host-apdu-service> tag");
            }
        }
        if (!z) {
            if (!"offhost-apdu-service".equals(tagName)) {
                throw new XmlPullParserException("Meta-data does not start with <offhost-apdu-service> tag");
            }
        }
        Resources res = packageManager.getResourcesForApplication(si.applicationInfo);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int i2 = 3;
        int i3 = 0;
        if (z) {
            TypedArray sa = res.obtainAttributes(attrs, R.styleable.HostApduService);
            this.mService = resolveInfo;
            this.mDescription = sa.getString(0);
            this.mRequiresDeviceUnlock = sa.getBoolean(2, false);
            this.mBannerResourceId = sa.getResourceId(3, -1);
            this.mSettingsActivityName = sa.getString(1);
            this.mOffHostName = null;
            this.mStaticOffHostName = this.mOffHostName;
            sa.recycle();
        } else {
            TypedArray sa2 = res.obtainAttributes(attrs, R.styleable.OffHostApduService);
            this.mService = resolveInfo;
            this.mDescription = sa2.getString(0);
            this.mRequiresDeviceUnlock = false;
            this.mBannerResourceId = sa2.getResourceId(2, -1);
            this.mSettingsActivityName = sa2.getString(1);
            this.mOffHostName = sa2.getString(3);
            if (this.mOffHostName != null) {
                if (this.mOffHostName.equals("eSE")) {
                    this.mOffHostName = "eSE1";
                } else if (this.mOffHostName.equals("SIM")) {
                    this.mOffHostName = "SIM1";
                }
            }
            this.mStaticOffHostName = this.mOffHostName;
            sa2.recycle();
        }
        this.mStaticAidGroups = new HashMap();
        this.mDynamicAidGroups = new HashMap();
        this.mOnHost = z;
        int depth = parser.getDepth();
        AidGroup currentGroup = null;
        while (true) {
            int next = parser.next();
            eventType = next;
            if ((next != i2 || parser.getDepth() > depth) && eventType != 1) {
                TypedArray groupAttrs;
                String groupCategory;
                String str;
                StringBuilder stringBuilder2;
                tagName = parser.getName();
                String str2 = "aid-group";
                String str3 = TAG;
                if (eventType == i) {
                    if (str2.equals(tagName) && currentGroup == null) {
                        AidGroup currentGroup2;
                        groupAttrs = res.obtainAttributes(attrs, R.styleable.AidGroup);
                        String groupCategory2 = groupAttrs.getString(1);
                        String groupDescription = groupAttrs.getString(i3);
                        groupCategory = groupCategory2;
                        str = "other";
                        if (!CardEmulation.CATEGORY_PAYMENT.equals(groupCategory)) {
                            groupCategory = str;
                        }
                        AidGroup currentGroup3 = (AidGroup) this.mStaticAidGroups.get(groupCategory);
                        String str4;
                        if (currentGroup3 == null) {
                            currentGroup2 = new AidGroup(groupCategory, groupDescription);
                        } else if (str.equals(groupCategory)) {
                            currentGroup2 = currentGroup3;
                            str4 = groupDescription;
                        } else {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Not allowing multiple aid-groups in the ");
                            stringBuilder2.append(groupCategory);
                            stringBuilder2.append(" category");
                            Log.e(str3, stringBuilder2.toString());
                            currentGroup2 = null;
                            str4 = groupDescription;
                        }
                        groupAttrs.recycle();
                        currentGroup = currentGroup2;
                        i = 2;
                        i2 = 3;
                        i3 = 0;
                        packageManager = pm;
                    }
                }
                if (eventType == 3 && str2.equals(tagName) && currentGroup != null) {
                    if (currentGroup.aids.size() <= 0) {
                        Log.e(str3, "Not adding <aid-group> with empty or invalid AIDs");
                    } else if (!this.mStaticAidGroups.containsKey(currentGroup.category)) {
                        this.mStaticAidGroups.put(currentGroup.category, currentGroup);
                    }
                    currentGroup = null;
                    i2 = 3;
                    i = 2;
                    i3 = 0;
                    packageManager = pm;
                } else {
                    String str5 = "Ignoring invalid or duplicate aid: ";
                    if (eventType == 2) {
                        if ("aid-filter".equals(tagName) && currentGroup != null) {
                            groupAttrs = res.obtainAttributes(attrs, R.styleable.AidFilter);
                            groupCategory = groupAttrs.getString(0).toUpperCase();
                            if (!CardEmulation.isValidAid(groupCategory) || currentGroup.aids.contains(groupCategory)) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str5);
                                stringBuilder2.append(groupCategory);
                                Log.e(str3, stringBuilder2.toString());
                            } else {
                                currentGroup.aids.add(groupCategory);
                            }
                            groupAttrs.recycle();
                            packageManager = pm;
                            i = 2;
                            i2 = 3;
                            i3 = 0;
                        }
                    }
                    StringBuilder stringBuilder3;
                    if (eventType == 2 && "aid-prefix-filter".equals(tagName) && currentGroup != null) {
                        TypedArray a = res.obtainAttributes(attrs, R.styleable.AidFilter);
                        str2 = a.getString(0).toUpperCase().concat("*");
                        if (!CardEmulation.isValidAid(str2) || currentGroup.aids.contains(str2)) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str5);
                            stringBuilder3.append(str2);
                            Log.e(str3, stringBuilder3.toString());
                        } else {
                            currentGroup.aids.add(str2);
                        }
                        a.recycle();
                        packageManager = pm;
                        i = 2;
                        i2 = 3;
                        i3 = 0;
                    } else {
                        if (eventType == 2 && tagName.equals("aid-suffix-filter") && currentGroup != null) {
                            groupAttrs = res.obtainAttributes(attrs, R.styleable.AidFilter);
                            str = groupAttrs.getString(0).toUpperCase().concat("#");
                            if (!CardEmulation.isValidAid(str) || currentGroup.aids.contains(str)) {
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(str5);
                                stringBuilder3.append(str);
                                Log.e(str3, stringBuilder3.toString());
                            } else {
                                currentGroup.aids.add(str);
                            }
                            groupAttrs.recycle();
                        }
                        packageManager = pm;
                        i = 2;
                        i2 = 3;
                        i3 = 0;
                    }
                }
            }
        }
        parser.close();
        this.mUid = si.applicationInfo.uid;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public String getOffHostSecureElement() {
        return this.mOffHostName;
    }

    public List<String> getAids() {
        ArrayList<String> aids = new ArrayList();
        Iterator it = getAidGroups().iterator();
        while (it.hasNext()) {
            aids.addAll(((AidGroup) it.next()).aids);
        }
        return aids;
    }

    public List<String> getPrefixAids() {
        ArrayList<String> prefixAids = new ArrayList();
        Iterator it = getAidGroups().iterator();
        while (it.hasNext()) {
            for (String aid : ((AidGroup) it.next()).aids) {
                if (aid.endsWith("*")) {
                    prefixAids.add(aid);
                }
            }
        }
        return prefixAids;
    }

    public List<String> getSubsetAids() {
        ArrayList<String> subsetAids = new ArrayList();
        Iterator it = getAidGroups().iterator();
        while (it.hasNext()) {
            for (String aid : ((AidGroup) it.next()).aids) {
                if (aid.endsWith("#")) {
                    subsetAids.add(aid);
                }
            }
        }
        return subsetAids;
    }

    public AidGroup getDynamicAidGroupForCategory(String category) {
        return (AidGroup) this.mDynamicAidGroups.get(category);
    }

    public boolean removeDynamicAidGroupForCategory(String category) {
        return this.mDynamicAidGroups.remove(category) != null;
    }

    public ArrayList<AidGroup> getAidGroups() {
        ArrayList<AidGroup> groups = new ArrayList();
        for (Entry<String, AidGroup> entry : this.mDynamicAidGroups.entrySet()) {
            groups.add((AidGroup) entry.getValue());
        }
        for (Entry<String, AidGroup> entry2 : this.mStaticAidGroups.entrySet()) {
            if (!this.mDynamicAidGroups.containsKey(entry2.getKey())) {
                groups.add((AidGroup) entry2.getValue());
            }
        }
        return groups;
    }

    public String getCategoryForAid(String aid) {
        Iterator it = getAidGroups().iterator();
        while (it.hasNext()) {
            AidGroup group = (AidGroup) it.next();
            if (group.aids.contains(aid.toUpperCase())) {
                return group.category;
            }
        }
        return null;
    }

    public boolean hasCategory(String category) {
        return this.mStaticAidGroups.containsKey(category) || this.mDynamicAidGroups.containsKey(category);
    }

    @UnsupportedAppUsage
    public boolean isOnHost() {
        return this.mOnHost;
    }

    @UnsupportedAppUsage
    public boolean requiresUnlock() {
        return this.mRequiresDeviceUnlock;
    }

    @UnsupportedAppUsage
    public String getDescription() {
        return this.mDescription;
    }

    @UnsupportedAppUsage
    public int getUid() {
        return this.mUid;
    }

    public void setOrReplaceDynamicAidGroup(AidGroup aidGroup) {
        this.mDynamicAidGroups.put(aidGroup.getCategory(), aidGroup);
    }

    public void setOffHostSecureElement(String offHost) {
        this.mOffHostName = offHost;
    }

    public void unsetOffHostSecureElement() {
        this.mOffHostName = this.mStaticOffHostName;
    }

    public CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public CharSequence loadAppLabel(PackageManager pm) {
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(this.mService.resolvePackageName, 128));
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    @UnsupportedAppUsage
    public Drawable loadBanner(PackageManager pm) {
        String str = "Could not load banner.";
        String str2 = TAG;
        try {
            str = pm.getResourcesForApplication(this.mService.serviceInfo.packageName).getDrawable(this.mBannerResourceId);
            return str;
        } catch (NotFoundException e) {
            Log.e(str2, str);
            return null;
        } catch (NameNotFoundException e2) {
            Log.e(str2, str);
            return null;
        }
    }

    @UnsupportedAppUsage
    public String getSettingsActivityName() {
        return this.mSettingsActivityName;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("ApduService: ");
        out.append(getComponent());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", description: ");
        stringBuilder.append(this.mDescription);
        out.append(stringBuilder.toString());
        out.append(", Static AID Groups: ");
        for (AidGroup aidGroup : this.mStaticAidGroups.values()) {
            out.append(aidGroup.toString());
        }
        out.append(", Dynamic AID Groups: ");
        for (AidGroup aidGroup2 : this.mDynamicAidGroups.values()) {
            out.append(aidGroup2.toString());
        }
        return out.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ApduServiceInfo) {
            return ((ApduServiceInfo) o).getComponent().equals(getComponent());
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
        dest.writeInt(this.mOnHost);
        dest.writeString(this.mOffHostName);
        dest.writeString(this.mStaticOffHostName);
        dest.writeInt(this.mStaticAidGroups.size());
        if (this.mStaticAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mStaticAidGroups.values()));
        }
        dest.writeInt(this.mDynamicAidGroups.size());
        if (this.mDynamicAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mDynamicAidGroups.values()));
        }
        dest.writeInt(this.mRequiresDeviceUnlock);
        dest.writeInt(this.mBannerResourceId);
        dest.writeInt(this.mUid);
        dest.writeString(this.mSettingsActivityName);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        String str;
        String str2;
        AidGroup group;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("    ");
        stringBuilder2.append(getComponent());
        stringBuilder2.append(" (Description: ");
        stringBuilder2.append(getDescription());
        stringBuilder2.append(")");
        pw.println(stringBuilder2.toString());
        if (this.mOnHost) {
            pw.println("    On Host Service");
        } else {
            pw.println("    Off-host Service");
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("        Current off-host SE:");
            stringBuilder2.append(this.mOffHostName);
            stringBuilder2.append(" static off-host SE:");
            stringBuilder2.append(this.mStaticOffHostName);
            pw.println(stringBuilder2.toString());
        }
        pw.println("    Static AID groups:");
        Iterator it = this.mStaticAidGroups.values().iterator();
        while (true) {
            str = "            AID: ";
            str2 = "        Category: ";
            if (!it.hasNext()) {
                break;
            }
            group = (AidGroup) it.next();
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(group.category);
            pw.println(stringBuilder.toString());
            for (String aid : group.aids) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(aid);
                pw.println(stringBuilder3.toString());
            }
        }
        pw.println("    Dynamic AID groups:");
        for (AidGroup group2 : this.mDynamicAidGroups.values()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(group2.category);
            pw.println(stringBuilder.toString());
            for (String aid2 : group2.aids) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str);
                stringBuilder4.append(aid2);
                pw.println(stringBuilder4.toString());
            }
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("    Settings Activity: ");
        stringBuilder2.append(this.mSettingsActivityName);
        pw.println(stringBuilder2.toString());
    }
}
