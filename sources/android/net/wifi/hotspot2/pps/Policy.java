package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public final class Policy implements Parcelable {
    public static final Creator<Policy> CREATOR = new Creator<Policy>() {
        public Policy createFromParcel(Parcel in) {
            Policy policy = new Policy();
            policy.setMinHomeDownlinkBandwidth(in.readLong());
            policy.setMinHomeUplinkBandwidth(in.readLong());
            policy.setMinRoamingDownlinkBandwidth(in.readLong());
            policy.setMinRoamingUplinkBandwidth(in.readLong());
            policy.setExcludedSsidList(in.createStringArray());
            policy.setRequiredProtoPortMap(readProtoPortMap(in));
            policy.setMaximumBssLoadValue(in.readInt());
            policy.setPreferredRoamingPartnerList(readRoamingPartnerList(in));
            policy.setPolicyUpdate((UpdateParameter) in.readParcelable(null));
            return policy;
        }

        public Policy[] newArray(int size) {
            return new Policy[size];
        }

        private Map<Integer, String> readProtoPortMap(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<Integer, String> protoPortMap = new HashMap(size);
            for (int i = 0; i < size; i++) {
                int key = in.readInt();
                protoPortMap.put(Integer.valueOf(key), in.readString());
            }
            return protoPortMap;
        }

        private List<RoamingPartner> readRoamingPartnerList(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            List<RoamingPartner> partnerList = new ArrayList();
            for (int i = 0; i < size; i++) {
                partnerList.add((RoamingPartner) in.readParcelable(null));
            }
            return partnerList;
        }
    };
    private static final int MAX_EXCLUSION_SSIDS = 128;
    private static final int MAX_PORT_STRING_BYTES = 64;
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "Policy";
    private String[] mExcludedSsidList = null;
    private int mMaximumBssLoadValue = Integer.MIN_VALUE;
    private long mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinHomeUplinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
    private UpdateParameter mPolicyUpdate = null;
    private List<RoamingPartner> mPreferredRoamingPartnerList = null;
    private Map<Integer, String> mRequiredProtoPortMap = null;

    public static final class RoamingPartner implements Parcelable {
        public static final Creator<RoamingPartner> CREATOR = new Creator<RoamingPartner>() {
            public RoamingPartner createFromParcel(Parcel in) {
                RoamingPartner roamingPartner = new RoamingPartner();
                roamingPartner.setFqdn(in.readString());
                roamingPartner.setFqdnExactMatch(in.readInt() != 0);
                roamingPartner.setPriority(in.readInt());
                roamingPartner.setCountries(in.readString());
                return roamingPartner;
            }

            public RoamingPartner[] newArray(int size) {
                return new RoamingPartner[size];
            }
        };
        private String mCountries = null;
        private String mFqdn = null;
        private boolean mFqdnExactMatch = false;
        private int mPriority = Integer.MIN_VALUE;

        public void setFqdn(String fqdn) {
            this.mFqdn = fqdn;
        }

        public String getFqdn() {
            return this.mFqdn;
        }

        public void setFqdnExactMatch(boolean fqdnExactMatch) {
            this.mFqdnExactMatch = fqdnExactMatch;
        }

        public boolean getFqdnExactMatch() {
            return this.mFqdnExactMatch;
        }

        public void setPriority(int priority) {
            this.mPriority = priority;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public void setCountries(String countries) {
            this.mCountries = countries;
        }

        public String getCountries() {
            return this.mCountries;
        }

        public RoamingPartner(RoamingPartner source) {
            if (source != null) {
                this.mFqdn = source.mFqdn;
                this.mFqdnExactMatch = source.mFqdnExactMatch;
                this.mPriority = source.mPriority;
                this.mCountries = source.mCountries;
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mFqdn);
            dest.writeInt(this.mFqdnExactMatch);
            dest.writeInt(this.mPriority);
            dest.writeString(this.mCountries);
        }

        public boolean equals(Object thatObject) {
            boolean z = true;
            if (this == thatObject) {
                return true;
            }
            if (!(thatObject instanceof RoamingPartner)) {
                return false;
            }
            RoamingPartner that = (RoamingPartner) thatObject;
            if (!(TextUtils.equals(this.mFqdn, that.mFqdn) && this.mFqdnExactMatch == that.mFqdnExactMatch && this.mPriority == that.mPriority && TextUtils.equals(this.mCountries, that.mCountries))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mFqdn, Boolean.valueOf(this.mFqdnExactMatch), Integer.valueOf(this.mPriority), this.mCountries});
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("FQDN: ");
            builder.append(this.mFqdn);
            String str = "\n";
            builder.append(str);
            builder.append("ExactMatch: ");
            builder.append("mFqdnExactMatch");
            builder.append(str);
            builder.append("Priority: ");
            builder.append(this.mPriority);
            builder.append(str);
            builder.append("Countries: ");
            builder.append(this.mCountries);
            builder.append(str);
            return builder.toString();
        }

        public boolean validate() {
            boolean isEmpty = TextUtils.isEmpty(this.mFqdn);
            String str = Policy.TAG;
            if (isEmpty) {
                Log.d(str, "Missing FQDN");
                return false;
            } else if (!TextUtils.isEmpty(this.mCountries)) {
                return true;
            } else {
                Log.d(str, "Missing countries");
                return false;
            }
        }
    }

    public void setMinHomeDownlinkBandwidth(long minHomeDownlinkBandwidth) {
        this.mMinHomeDownlinkBandwidth = minHomeDownlinkBandwidth;
    }

    public long getMinHomeDownlinkBandwidth() {
        return this.mMinHomeDownlinkBandwidth;
    }

    public void setMinHomeUplinkBandwidth(long minHomeUplinkBandwidth) {
        this.mMinHomeUplinkBandwidth = minHomeUplinkBandwidth;
    }

    public long getMinHomeUplinkBandwidth() {
        return this.mMinHomeUplinkBandwidth;
    }

    public void setMinRoamingDownlinkBandwidth(long minRoamingDownlinkBandwidth) {
        this.mMinRoamingDownlinkBandwidth = minRoamingDownlinkBandwidth;
    }

    public long getMinRoamingDownlinkBandwidth() {
        return this.mMinRoamingDownlinkBandwidth;
    }

    public void setMinRoamingUplinkBandwidth(long minRoamingUplinkBandwidth) {
        this.mMinRoamingUplinkBandwidth = minRoamingUplinkBandwidth;
    }

    public long getMinRoamingUplinkBandwidth() {
        return this.mMinRoamingUplinkBandwidth;
    }

    public void setExcludedSsidList(String[] excludedSsidList) {
        this.mExcludedSsidList = excludedSsidList;
    }

    public String[] getExcludedSsidList() {
        return this.mExcludedSsidList;
    }

    public void setRequiredProtoPortMap(Map<Integer, String> requiredProtoPortMap) {
        this.mRequiredProtoPortMap = requiredProtoPortMap;
    }

    public Map<Integer, String> getRequiredProtoPortMap() {
        return this.mRequiredProtoPortMap;
    }

    public void setMaximumBssLoadValue(int maximumBssLoadValue) {
        this.mMaximumBssLoadValue = maximumBssLoadValue;
    }

    public int getMaximumBssLoadValue() {
        return this.mMaximumBssLoadValue;
    }

    public void setPreferredRoamingPartnerList(List<RoamingPartner> partnerList) {
        this.mPreferredRoamingPartnerList = partnerList;
    }

    public List<RoamingPartner> getPreferredRoamingPartnerList() {
        return this.mPreferredRoamingPartnerList;
    }

    public void setPolicyUpdate(UpdateParameter policyUpdate) {
        this.mPolicyUpdate = policyUpdate;
    }

    public UpdateParameter getPolicyUpdate() {
        return this.mPolicyUpdate;
    }

    public Policy(Policy source) {
        if (source != null) {
            this.mMinHomeDownlinkBandwidth = source.mMinHomeDownlinkBandwidth;
            this.mMinHomeUplinkBandwidth = source.mMinHomeUplinkBandwidth;
            this.mMinRoamingDownlinkBandwidth = source.mMinRoamingDownlinkBandwidth;
            this.mMinRoamingUplinkBandwidth = source.mMinRoamingUplinkBandwidth;
            this.mMaximumBssLoadValue = source.mMaximumBssLoadValue;
            String[] strArr = source.mExcludedSsidList;
            if (strArr != null) {
                this.mExcludedSsidList = (String[]) Arrays.copyOf(strArr, strArr.length);
            }
            Map map = source.mRequiredProtoPortMap;
            if (map != null) {
                this.mRequiredProtoPortMap = Collections.unmodifiableMap(map);
            }
            List list = source.mPreferredRoamingPartnerList;
            if (list != null) {
                this.mPreferredRoamingPartnerList = Collections.unmodifiableList(list);
            }
            UpdateParameter updateParameter = source.mPolicyUpdate;
            if (updateParameter != null) {
                this.mPolicyUpdate = new UpdateParameter(updateParameter);
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mMinHomeDownlinkBandwidth);
        dest.writeLong(this.mMinHomeUplinkBandwidth);
        dest.writeLong(this.mMinRoamingDownlinkBandwidth);
        dest.writeLong(this.mMinRoamingUplinkBandwidth);
        dest.writeStringArray(this.mExcludedSsidList);
        writeProtoPortMap(dest, this.mRequiredProtoPortMap);
        dest.writeInt(this.mMaximumBssLoadValue);
        writeRoamingPartnerList(dest, flags, this.mPreferredRoamingPartnerList);
        dest.writeParcelable(this.mPolicyUpdate, flags);
    }

    public boolean equals(Object thatObject) {
        boolean z = true;
        if (this == thatObject) {
            return true;
        }
        if (!(thatObject instanceof Policy)) {
            return false;
        }
        Policy that = (Policy) thatObject;
        if (this.mMinHomeDownlinkBandwidth == that.mMinHomeDownlinkBandwidth && this.mMinHomeUplinkBandwidth == that.mMinHomeUplinkBandwidth && this.mMinRoamingDownlinkBandwidth == that.mMinRoamingDownlinkBandwidth && this.mMinRoamingUplinkBandwidth == that.mMinRoamingUplinkBandwidth && Arrays.equals(this.mExcludedSsidList, that.mExcludedSsidList)) {
            Map map = this.mRequiredProtoPortMap;
            if (map != null ? !map.equals(that.mRequiredProtoPortMap) : that.mRequiredProtoPortMap != null) {
                if (this.mMaximumBssLoadValue == that.mMaximumBssLoadValue) {
                    List list = this.mPreferredRoamingPartnerList;
                    if (list != null ? !list.equals(that.mPreferredRoamingPartnerList) : that.mPreferredRoamingPartnerList != null) {
                        UpdateParameter updateParameter = this.mPolicyUpdate;
                        if (updateParameter != null) {
                        }
                    }
                }
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Long.valueOf(this.mMinHomeDownlinkBandwidth), Long.valueOf(this.mMinHomeUplinkBandwidth), Long.valueOf(this.mMinRoamingDownlinkBandwidth), Long.valueOf(this.mMinRoamingUplinkBandwidth), this.mExcludedSsidList, this.mRequiredProtoPortMap, Integer.valueOf(this.mMaximumBssLoadValue), this.mPreferredRoamingPartnerList, this.mPolicyUpdate});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MinHomeDownlinkBandwidth: ");
        builder.append(this.mMinHomeDownlinkBandwidth);
        String str = "\n";
        builder.append(str);
        builder.append("MinHomeUplinkBandwidth: ");
        builder.append(this.mMinHomeUplinkBandwidth);
        builder.append(str);
        builder.append("MinRoamingDownlinkBandwidth: ");
        builder.append(this.mMinRoamingDownlinkBandwidth);
        builder.append(str);
        builder.append("MinRoamingUplinkBandwidth: ");
        builder.append(this.mMinRoamingUplinkBandwidth);
        builder.append(str);
        builder.append("ExcludedSSIDList: ");
        builder.append(this.mExcludedSsidList);
        builder.append(str);
        builder.append("RequiredProtoPortMap: ");
        builder.append(this.mRequiredProtoPortMap);
        builder.append(str);
        builder.append("MaximumBSSLoadValue: ");
        builder.append(this.mMaximumBssLoadValue);
        builder.append(str);
        builder.append("PreferredRoamingPartnerList: ");
        builder.append(this.mPreferredRoamingPartnerList);
        builder.append(str);
        if (this.mPolicyUpdate != null) {
            builder.append("PolicyUpdate Begin ---\n");
            builder.append(this.mPolicyUpdate);
            builder.append("PolicyUpdate End ---\n");
        }
        return builder.toString();
    }

    public boolean validate() {
        UpdateParameter updateParameter = this.mPolicyUpdate;
        String str = TAG;
        if (updateParameter == null) {
            Log.d(str, "PolicyUpdate not specified");
            return false;
        } else if (!updateParameter.validate()) {
            return false;
        } else {
            StringBuilder stringBuilder;
            String[] strArr = this.mExcludedSsidList;
            if (strArr != null) {
                if (strArr.length > 128) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SSID exclusion list size exceeded the max: ");
                    stringBuilder.append(this.mExcludedSsidList.length);
                    Log.d(str, stringBuilder.toString());
                    return false;
                }
                for (String ssid : strArr) {
                    if (ssid.getBytes(StandardCharsets.UTF_8).length > 32) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid SSID: ");
                        stringBuilder.append(ssid);
                        Log.d(str, stringBuilder.toString());
                        return false;
                    }
                }
            }
            Map map = this.mRequiredProtoPortMap;
            if (map != null) {
                for (Entry<Integer, String> entry : map.entrySet()) {
                    String portNumber = (String) entry.getValue();
                    if (portNumber.getBytes(StandardCharsets.UTF_8).length > 64) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("PortNumber string bytes exceeded the max: ");
                        stringBuilder.append(portNumber);
                        Log.d(str, stringBuilder.toString());
                        return false;
                    }
                }
            }
            List<RoamingPartner> list = this.mPreferredRoamingPartnerList;
            if (list != null) {
                for (RoamingPartner partner : list) {
                    if (!partner.validate()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static void writeProtoPortMap(Parcel dest, Map<Integer, String> protoPortMap) {
        if (protoPortMap == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(protoPortMap.size());
        for (Entry<Integer, String> entry : protoPortMap.entrySet()) {
            dest.writeInt(((Integer) entry.getKey()).intValue());
            dest.writeString((String) entry.getValue());
        }
    }

    private static void writeRoamingPartnerList(Parcel dest, int flags, List<RoamingPartner> partnerList) {
        if (partnerList == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(partnerList.size());
        for (RoamingPartner partner : partnerList) {
            dest.writeParcelable(partner, flags);
        }
    }
}
