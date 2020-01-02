package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public final class HomeSp implements Parcelable {
    public static final Creator<HomeSp> CREATOR = new Creator<HomeSp>() {
        public HomeSp createFromParcel(Parcel in) {
            HomeSp homeSp = new HomeSp();
            homeSp.setFqdn(in.readString());
            homeSp.setFriendlyName(in.readString());
            homeSp.setIconUrl(in.readString());
            homeSp.setHomeNetworkIds(readHomeNetworkIds(in));
            homeSp.setMatchAllOis(in.createLongArray());
            homeSp.setMatchAnyOis(in.createLongArray());
            homeSp.setOtherHomePartners(in.createStringArray());
            homeSp.setRoamingConsortiumOis(in.createLongArray());
            return homeSp;
        }

        public HomeSp[] newArray(int size) {
            return new HomeSp[size];
        }

        private Map<String, Long> readHomeNetworkIds(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<String, Long> networkIds = new HashMap(size);
            for (int i = 0; i < size; i++) {
                String key = in.readString();
                Long value = null;
                long readValue = in.readLong();
                if (readValue != -1) {
                    value = Long.valueOf(readValue);
                }
                networkIds.put(key, value);
            }
            return networkIds;
        }
    };
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "HomeSp";
    private String mFqdn = null;
    private String mFriendlyName = null;
    private Map<String, Long> mHomeNetworkIds = null;
    private String mIconUrl = null;
    private long[] mMatchAllOis = null;
    private long[] mMatchAnyOis = null;
    private String[] mOtherHomePartners = null;
    private long[] mRoamingConsortiumOis = null;

    public void setFqdn(String fqdn) {
        this.mFqdn = fqdn;
    }

    public String getFqdn() {
        return this.mFqdn;
    }

    public void setFriendlyName(String friendlyName) {
        this.mFriendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.mFriendlyName;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setHomeNetworkIds(Map<String, Long> homeNetworkIds) {
        this.mHomeNetworkIds = homeNetworkIds;
    }

    public Map<String, Long> getHomeNetworkIds() {
        return this.mHomeNetworkIds;
    }

    public void setMatchAllOis(long[] matchAllOis) {
        this.mMatchAllOis = matchAllOis;
    }

    public long[] getMatchAllOis() {
        return this.mMatchAllOis;
    }

    public void setMatchAnyOis(long[] matchAnyOis) {
        this.mMatchAnyOis = matchAnyOis;
    }

    public long[] getMatchAnyOis() {
        return this.mMatchAnyOis;
    }

    public void setOtherHomePartners(String[] otherHomePartners) {
        this.mOtherHomePartners = otherHomePartners;
    }

    public String[] getOtherHomePartners() {
        return this.mOtherHomePartners;
    }

    public void setRoamingConsortiumOis(long[] roamingConsortiumOis) {
        this.mRoamingConsortiumOis = roamingConsortiumOis;
    }

    public long[] getRoamingConsortiumOis() {
        return this.mRoamingConsortiumOis;
    }

    public HomeSp(HomeSp source) {
        if (source != null) {
            this.mFqdn = source.mFqdn;
            this.mFriendlyName = source.mFriendlyName;
            this.mIconUrl = source.mIconUrl;
            Map map = source.mHomeNetworkIds;
            if (map != null) {
                this.mHomeNetworkIds = Collections.unmodifiableMap(map);
            }
            long[] jArr = source.mMatchAllOis;
            if (jArr != null) {
                this.mMatchAllOis = Arrays.copyOf(jArr, jArr.length);
            }
            jArr = source.mMatchAnyOis;
            if (jArr != null) {
                this.mMatchAnyOis = Arrays.copyOf(jArr, jArr.length);
            }
            String[] strArr = source.mOtherHomePartners;
            if (strArr != null) {
                this.mOtherHomePartners = (String[]) Arrays.copyOf(strArr, strArr.length);
            }
            jArr = source.mRoamingConsortiumOis;
            if (jArr != null) {
                this.mRoamingConsortiumOis = Arrays.copyOf(jArr, jArr.length);
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFqdn);
        dest.writeString(this.mFriendlyName);
        dest.writeString(this.mIconUrl);
        writeHomeNetworkIds(dest, this.mHomeNetworkIds);
        dest.writeLongArray(this.mMatchAllOis);
        dest.writeLongArray(this.mMatchAnyOis);
        dest.writeStringArray(this.mOtherHomePartners);
        dest.writeLongArray(this.mRoamingConsortiumOis);
    }

    /* JADX WARNING: Missing block: B:25:0x0062, code skipped:
            if (java.util.Arrays.equals(r5.mRoamingConsortiumOis, r1.mRoamingConsortiumOis) != false) goto L_0x0066;
     */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r0 = 1;
        if (r5 != r6) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r6 instanceof android.net.wifi.hotspot2.pps.HomeSp;
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r1 = r6;
        r1 = (android.net.wifi.hotspot2.pps.HomeSp) r1;
        r3 = r5.mFqdn;
        r4 = r1.mFqdn;
        r3 = android.text.TextUtils.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x0017:
        r3 = r5.mFriendlyName;
        r4 = r1.mFriendlyName;
        r3 = android.text.TextUtils.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x0021:
        r3 = r5.mIconUrl;
        r4 = r1.mIconUrl;
        r3 = android.text.TextUtils.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x002b:
        r3 = r5.mHomeNetworkIds;
        if (r3 != 0) goto L_0x0034;
    L_0x002f:
        r3 = r1.mHomeNetworkIds;
        if (r3 != 0) goto L_0x0065;
    L_0x0033:
        goto L_0x003c;
    L_0x0034:
        r4 = r1.mHomeNetworkIds;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0065;
    L_0x003c:
        r3 = r5.mMatchAllOis;
        r4 = r1.mMatchAllOis;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x0046:
        r3 = r5.mMatchAnyOis;
        r4 = r1.mMatchAnyOis;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x0050:
        r3 = r5.mOtherHomePartners;
        r4 = r1.mOtherHomePartners;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x005a:
        r3 = r5.mRoamingConsortiumOis;
        r4 = r1.mRoamingConsortiumOis;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 == 0) goto L_0x0065;
    L_0x0064:
        goto L_0x0066;
    L_0x0065:
        r0 = r2;
    L_0x0066:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.pps.HomeSp.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mFqdn, this.mFriendlyName, this.mIconUrl, this.mHomeNetworkIds, this.mMatchAllOis, this.mMatchAnyOis, this.mOtherHomePartners, this.mRoamingConsortiumOis});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FQDN: ");
        builder.append(this.mFqdn);
        String str = "\n";
        builder.append(str);
        builder.append("FriendlyName: ");
        builder.append(this.mFriendlyName);
        builder.append(str);
        builder.append("IconURL: ");
        builder.append(this.mIconUrl);
        builder.append(str);
        builder.append("HomeNetworkIDs: ");
        builder.append(this.mHomeNetworkIds);
        builder.append(str);
        builder.append("MatchAllOIs: ");
        builder.append(this.mMatchAllOis);
        builder.append(str);
        builder.append("MatchAnyOIs: ");
        builder.append(this.mMatchAnyOis);
        builder.append(str);
        builder.append("OtherHomePartners: ");
        builder.append(this.mOtherHomePartners);
        builder.append(str);
        builder.append("RoamingConsortiumOIs: ");
        builder.append(this.mRoamingConsortiumOis);
        builder.append(str);
        return builder.toString();
    }

    public boolean validate() {
        boolean isEmpty = TextUtils.isEmpty(this.mFqdn);
        String str = TAG;
        if (isEmpty) {
            Log.d(str, "Missing FQDN");
            return false;
        } else if (TextUtils.isEmpty(this.mFriendlyName)) {
            Log.d(str, "Missing friendly name");
            return false;
        } else {
            Map map = this.mHomeNetworkIds;
            if (map != null) {
                for (Entry<String, Long> entry : map.entrySet()) {
                    if (entry.getKey() == null || ((String) entry.getKey()).getBytes(StandardCharsets.UTF_8).length > 32) {
                        Log.d(str, "Invalid SSID in HomeNetworkIDs");
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static void writeHomeNetworkIds(Parcel dest, Map<String, Long> networkIds) {
        if (networkIds == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(networkIds.size());
        for (Entry<String, Long> entry : networkIds.entrySet()) {
            dest.writeString((String) entry.getKey());
            if (entry.getValue() == null) {
                dest.writeLong(-1);
            } else {
                dest.writeLong(((Long) entry.getValue()).longValue());
            }
        }
    }
}
