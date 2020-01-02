package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class NetworkCapabilities implements Parcelable {
    private static final long CONNECTIVITY_MANAGED_CAPABILITIES = 17498112;
    public static final Creator<NetworkCapabilities> CREATOR = new Creator<NetworkCapabilities>() {
        public NetworkCapabilities createFromParcel(Parcel in) {
            NetworkCapabilities netCap = new NetworkCapabilities();
            netCap.mNetworkCapabilities = in.readLong();
            netCap.mUnwantedNetworkCapabilities = in.readLong();
            netCap.mTransportTypes = in.readLong();
            netCap.mLinkUpBandwidthKbps = in.readInt();
            netCap.mLinkDownBandwidthKbps = in.readInt();
            netCap.mNetworkSpecifier = (NetworkSpecifier) in.readParcelable(null);
            netCap.mTransportInfo = (TransportInfo) in.readParcelable(null);
            netCap.mSignalStrength = in.readInt();
            netCap.mUids = in.readArraySet(null);
            netCap.mSSID = in.readString();
            return netCap;
        }

        public NetworkCapabilities[] newArray(int size) {
            return new NetworkCapabilities[size];
        }
    };
    private static final long DEFAULT_CAPABILITIES = 57344;
    private static final long FORCE_RESTRICTED_CAPABILITIES = 4194304;
    private static final int INVALID_UID = -1;
    public static final int LINK_BANDWIDTH_UNSPECIFIED = 0;
    private static final int MAX_NET_CAPABILITY = 24;
    public static final int MAX_TRANSPORT = 7;
    private static final int MIN_NET_CAPABILITY = 0;
    public static final int MIN_TRANSPORT = 0;
    private static final long MUTABLE_CAPABILITIES = 20922368;
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
    public static final int NET_CAPABILITY_CBS = 5;
    public static final int NET_CAPABILITY_DUN = 2;
    public static final int NET_CAPABILITY_EIMS = 10;
    public static final int NET_CAPABILITY_FOREGROUND = 19;
    public static final int NET_CAPABILITY_FOTA = 3;
    public static final int NET_CAPABILITY_IA = 7;
    public static final int NET_CAPABILITY_IMS = 4;
    public static final int NET_CAPABILITY_INTERNET = 12;
    public static final int NET_CAPABILITY_MCX = 23;
    public static final int NET_CAPABILITY_MMS = 0;
    public static final int NET_CAPABILITY_NOT_CONGESTED = 20;
    public static final int NET_CAPABILITY_NOT_METERED = 11;
    public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
    public static final int NET_CAPABILITY_NOT_ROAMING = 18;
    public static final int NET_CAPABILITY_NOT_SUSPENDED = 21;
    public static final int NET_CAPABILITY_NOT_VPN = 15;
    @SystemApi
    public static final int NET_CAPABILITY_OEM_PAID = 22;
    @SystemApi
    public static final int NET_CAPABILITY_PARTIAL_CONNECTIVITY = 24;
    public static final int NET_CAPABILITY_RCS = 8;
    public static final int NET_CAPABILITY_SUPL = 1;
    public static final int NET_CAPABILITY_TRUSTED = 14;
    public static final int NET_CAPABILITY_VALIDATED = 16;
    public static final int NET_CAPABILITY_WIFI_P2P = 6;
    public static final int NET_CAPABILITY_XCAP = 9;
    private static final long NON_REQUESTABLE_CAPABILITIES = 20905984;
    @VisibleForTesting
    static final long RESTRICTED_CAPABILITIES = 8390588;
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;
    private static final String TAG = "NetworkCapabilities";
    public static final int TRANSPORT_BLUETOOTH = 2;
    public static final int TRANSPORT_CELLULAR = 0;
    public static final int TRANSPORT_ETHERNET = 3;
    public static final int TRANSPORT_LOWPAN = 6;
    private static final String[] TRANSPORT_NAMES = new String[]{"CELLULAR", "WIFI", "BLUETOOTH", "ETHERNET", "VPN", "WIFI_AWARE", "LOWPAN", "TEST"};
    public static final int TRANSPORT_TEST = 7;
    public static final int TRANSPORT_VPN = 4;
    public static final int TRANSPORT_WIFI = 1;
    public static final int TRANSPORT_WIFI_AWARE = 5;
    @VisibleForTesting
    static final long UNRESTRICTED_CAPABILITIES = 4163;
    private int mEstablishingVpnAppUid = -1;
    private int mLinkDownBandwidthKbps = 0;
    private int mLinkUpBandwidthKbps = 0;
    @UnsupportedAppUsage
    private long mNetworkCapabilities;
    private NetworkSpecifier mNetworkSpecifier = null;
    private String mSSID;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mSignalStrength = Integer.MIN_VALUE;
    private TransportInfo mTransportInfo = null;
    private long mTransportTypes;
    private ArraySet<UidRange> mUids = null;
    private long mUnwantedNetworkCapabilities;

    private interface NameOf {
        String nameOf(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetCapability {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Transport {
    }

    @UnsupportedAppUsage
    public NetworkCapabilities() {
        clearAll();
        this.mNetworkCapabilities = DEFAULT_CAPABILITIES;
    }

    public NetworkCapabilities(NetworkCapabilities nc) {
        if (nc != null) {
            set(nc);
        }
    }

    public void clearAll() {
        this.mUnwantedNetworkCapabilities = 0;
        this.mTransportTypes = 0;
        this.mNetworkCapabilities = 0;
        this.mLinkDownBandwidthKbps = 0;
        this.mLinkUpBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mTransportInfo = null;
        this.mSignalStrength = Integer.MIN_VALUE;
        this.mUids = null;
        this.mEstablishingVpnAppUid = -1;
        this.mSSID = null;
    }

    public void set(NetworkCapabilities nc) {
        this.mNetworkCapabilities = nc.mNetworkCapabilities;
        this.mTransportTypes = nc.mTransportTypes;
        this.mLinkUpBandwidthKbps = nc.mLinkUpBandwidthKbps;
        this.mLinkDownBandwidthKbps = nc.mLinkDownBandwidthKbps;
        this.mNetworkSpecifier = nc.mNetworkSpecifier;
        this.mTransportInfo = nc.mTransportInfo;
        this.mSignalStrength = nc.mSignalStrength;
        setUids(nc.mUids);
        this.mEstablishingVpnAppUid = nc.mEstablishingVpnAppUid;
        this.mUnwantedNetworkCapabilities = nc.mUnwantedNetworkCapabilities;
        this.mSSID = nc.mSSID;
    }

    @UnsupportedAppUsage
    public NetworkCapabilities addCapability(int capability) {
        checkValidCapability(capability);
        this.mNetworkCapabilities |= (long) (1 << capability);
        this.mUnwantedNetworkCapabilities &= (long) (~(1 << capability));
        return this;
    }

    public void addUnwantedCapability(int capability) {
        checkValidCapability(capability);
        this.mUnwantedNetworkCapabilities |= (long) (1 << capability);
        this.mNetworkCapabilities &= (long) (~(1 << capability));
    }

    @UnsupportedAppUsage
    public NetworkCapabilities removeCapability(int capability) {
        checkValidCapability(capability);
        long mask = (long) (~(1 << capability));
        this.mNetworkCapabilities &= mask;
        this.mUnwantedNetworkCapabilities &= mask;
        return this;
    }

    public NetworkCapabilities setCapability(int capability, boolean value) {
        if (value) {
            addCapability(capability);
        } else {
            removeCapability(capability);
        }
        return this;
    }

    public int[] getCapabilities() {
        return BitUtils.unpackBits(this.mNetworkCapabilities);
    }

    public int[] getUnwantedCapabilities() {
        return BitUtils.unpackBits(this.mUnwantedNetworkCapabilities);
    }

    public void setCapabilities(int[] capabilities, int[] unwantedCapabilities) {
        this.mNetworkCapabilities = BitUtils.packBits(capabilities);
        this.mUnwantedNetworkCapabilities = BitUtils.packBits(unwantedCapabilities);
    }

    @Deprecated
    public void setCapabilities(int[] capabilities) {
        setCapabilities(capabilities, new int[0]);
    }

    public boolean hasCapability(int capability) {
        return isValidCapability(capability) && (this.mNetworkCapabilities & ((long) (1 << capability))) != 0;
    }

    public boolean hasUnwantedCapability(int capability) {
        return isValidCapability(capability) && (this.mUnwantedNetworkCapabilities & ((long) (1 << capability))) != 0;
    }

    public boolean hasConnectivityManagedCapability() {
        return (this.mNetworkCapabilities & CONNECTIVITY_MANAGED_CAPABILITIES) != 0;
    }

    private void combineNetCapabilities(NetworkCapabilities nc) {
        this.mNetworkCapabilities |= nc.mNetworkCapabilities;
        this.mUnwantedNetworkCapabilities |= nc.mUnwantedNetworkCapabilities;
    }

    public String describeFirstNonRequestableCapability() {
        long nonRequestable = (this.mNetworkCapabilities | this.mUnwantedNetworkCapabilities) & NON_REQUESTABLE_CAPABILITIES;
        if (nonRequestable != 0) {
            return capabilityNameOf(BitUtils.unpackBits(nonRequestable)[0]);
        }
        if (this.mLinkUpBandwidthKbps != 0 || this.mLinkDownBandwidthKbps != 0) {
            return "link bandwidth";
        }
        if (hasSignalStrength()) {
            return "signalStrength";
        }
        return null;
    }

    private boolean satisfiedByNetCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        long requestedCapabilities = this.mNetworkCapabilities;
        long requestedUnwantedCapabilities = this.mUnwantedNetworkCapabilities;
        long providedCapabilities = nc.mNetworkCapabilities;
        if (onlyImmutable) {
            requestedCapabilities &= -20922369;
            requestedUnwantedCapabilities &= -20922369;
        }
        return (providedCapabilities & requestedCapabilities) == requestedCapabilities && (requestedUnwantedCapabilities & providedCapabilities) == 0;
    }

    public boolean equalsNetCapabilities(NetworkCapabilities nc) {
        return nc.mNetworkCapabilities == this.mNetworkCapabilities && nc.mUnwantedNetworkCapabilities == this.mUnwantedNetworkCapabilities;
    }

    private boolean equalsNetCapabilitiesRequestable(NetworkCapabilities that) {
        return (this.mNetworkCapabilities & -20905985) == (that.mNetworkCapabilities & -20905985) && (this.mUnwantedNetworkCapabilities & -20905985) == (-20905985 & that.mUnwantedNetworkCapabilities);
    }

    public void maybeMarkCapabilitiesRestricted() {
        boolean hasRestrictedCapabilities = true;
        boolean forceRestrictedCapability = (this.mNetworkCapabilities & 4194304) != 0;
        boolean hasUnrestrictedCapabilities = (this.mNetworkCapabilities & UNRESTRICTED_CAPABILITIES) != 0;
        if ((this.mNetworkCapabilities & RESTRICTED_CAPABILITIES) == 0) {
            hasRestrictedCapabilities = false;
        }
        if (forceRestrictedCapability || (hasRestrictedCapabilities && !hasUnrestrictedCapabilities)) {
            removeCapability(13);
        }
    }

    public static boolean isValidTransport(int transportType) {
        return transportType >= 0 && transportType <= 7;
    }

    @UnsupportedAppUsage
    public NetworkCapabilities addTransportType(int transportType) {
        checkValidTransportType(transportType);
        this.mTransportTypes |= (long) (1 << transportType);
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public NetworkCapabilities removeTransportType(int transportType) {
        checkValidTransportType(transportType);
        this.mTransportTypes &= (long) (~(1 << transportType));
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public NetworkCapabilities setTransportType(int transportType, boolean value) {
        if (value) {
            addTransportType(transportType);
        } else {
            removeTransportType(transportType);
        }
        return this;
    }

    @SystemApi
    public int[] getTransportTypes() {
        return BitUtils.unpackBits(this.mTransportTypes);
    }

    public void setTransportTypes(int[] transportTypes) {
        this.mTransportTypes = BitUtils.packBits(transportTypes);
    }

    public boolean hasTransport(int transportType) {
        return isValidTransport(transportType) && (this.mTransportTypes & ((long) (1 << transportType))) != 0;
    }

    private void combineTransportTypes(NetworkCapabilities nc) {
        this.mTransportTypes |= nc.mTransportTypes;
    }

    private boolean satisfiedByTransportTypes(NetworkCapabilities nc) {
        long j = this.mTransportTypes;
        return j == 0 || (j & nc.mTransportTypes) != 0;
    }

    public boolean equalsTransportTypes(NetworkCapabilities nc) {
        return nc.mTransportTypes == this.mTransportTypes;
    }

    public void setEstablishingVpnAppUid(int uid) {
        this.mEstablishingVpnAppUid = uid;
    }

    public int getEstablishingVpnAppUid() {
        return this.mEstablishingVpnAppUid;
    }

    public NetworkCapabilities setLinkUpstreamBandwidthKbps(int upKbps) {
        this.mLinkUpBandwidthKbps = upKbps;
        return this;
    }

    public int getLinkUpstreamBandwidthKbps() {
        return this.mLinkUpBandwidthKbps;
    }

    public NetworkCapabilities setLinkDownstreamBandwidthKbps(int downKbps) {
        this.mLinkDownBandwidthKbps = downKbps;
        return this;
    }

    public int getLinkDownstreamBandwidthKbps() {
        return this.mLinkDownBandwidthKbps;
    }

    private void combineLinkBandwidths(NetworkCapabilities nc) {
        this.mLinkUpBandwidthKbps = Math.max(this.mLinkUpBandwidthKbps, nc.mLinkUpBandwidthKbps);
        this.mLinkDownBandwidthKbps = Math.max(this.mLinkDownBandwidthKbps, nc.mLinkDownBandwidthKbps);
    }

    private boolean satisfiedByLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps <= nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps <= nc.mLinkDownBandwidthKbps;
    }

    private boolean equalsLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps == nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps == nc.mLinkDownBandwidthKbps;
    }

    public static int minBandwidth(int a, int b) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        return Math.min(a, b);
    }

    public static int maxBandwidth(int a, int b) {
        return Math.max(a, b);
    }

    public NetworkCapabilities setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier == null || Long.bitCount(this.mTransportTypes) == 1) {
            this.mNetworkSpecifier = networkSpecifier;
            return this;
        }
        throw new IllegalStateException("Must have a single transport specified to use setNetworkSpecifier");
    }

    public NetworkCapabilities setTransportInfo(TransportInfo transportInfo) {
        this.mTransportInfo = transportInfo;
        return this;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public NetworkSpecifier getNetworkSpecifier() {
        return this.mNetworkSpecifier;
    }

    public TransportInfo getTransportInfo() {
        return this.mTransportInfo;
    }

    private void combineSpecifiers(NetworkCapabilities nc) {
        NetworkSpecifier networkSpecifier = this.mNetworkSpecifier;
        if (networkSpecifier == null || networkSpecifier.equals(nc.mNetworkSpecifier)) {
            setNetworkSpecifier(nc.mNetworkSpecifier);
            return;
        }
        throw new IllegalStateException("Can't combine two networkSpecifiers");
    }

    private boolean satisfiedBySpecifier(NetworkCapabilities nc) {
        NetworkSpecifier networkSpecifier = this.mNetworkSpecifier;
        return networkSpecifier == null || networkSpecifier.satisfiedBy(nc.mNetworkSpecifier) || (nc.mNetworkSpecifier instanceof MatchAllNetworkSpecifier);
    }

    private boolean equalsSpecifier(NetworkCapabilities nc) {
        return Objects.equals(this.mNetworkSpecifier, nc.mNetworkSpecifier);
    }

    private void combineTransportInfos(NetworkCapabilities nc) {
        TransportInfo transportInfo = this.mTransportInfo;
        if (transportInfo == null || transportInfo.equals(nc.mTransportInfo)) {
            setTransportInfo(nc.mTransportInfo);
            return;
        }
        throw new IllegalStateException("Can't combine two TransportInfos");
    }

    private boolean equalsTransportInfo(NetworkCapabilities nc) {
        return Objects.equals(this.mTransportInfo, nc.mTransportInfo);
    }

    @UnsupportedAppUsage
    public NetworkCapabilities setSignalStrength(int signalStrength) {
        this.mSignalStrength = signalStrength;
        return this;
    }

    @UnsupportedAppUsage
    public boolean hasSignalStrength() {
        return this.mSignalStrength > Integer.MIN_VALUE;
    }

    public int getSignalStrength() {
        return this.mSignalStrength;
    }

    private void combineSignalStrength(NetworkCapabilities nc) {
        this.mSignalStrength = Math.max(this.mSignalStrength, nc.mSignalStrength);
    }

    private boolean satisfiedBySignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength <= nc.mSignalStrength;
    }

    private boolean equalsSignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength == nc.mSignalStrength;
    }

    public NetworkCapabilities setSingleUid(int uid) {
        ArraySet<UidRange> identity = new ArraySet(1);
        identity.add(new UidRange(uid, uid));
        setUids(identity);
        return this;
    }

    public NetworkCapabilities setUids(Set<UidRange> uids) {
        if (uids == null) {
            this.mUids = null;
        } else {
            this.mUids = new ArraySet((Collection) uids);
        }
        return this;
    }

    public Set<UidRange> getUids() {
        ArraySet arraySet = this.mUids;
        return arraySet == null ? null : new ArraySet(arraySet);
    }

    public boolean appliesToUid(int uid) {
        ArraySet arraySet = this.mUids;
        if (arraySet == null) {
            return true;
        }
        Iterator it = arraySet.iterator();
        while (it.hasNext()) {
            if (((UidRange) it.next()).contains(uid)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public boolean equalsUids(NetworkCapabilities nc) {
        Set<UidRange> comparedUids = nc.mUids;
        boolean z = false;
        if (comparedUids == null) {
            if (this.mUids == null) {
                z = true;
            }
            return z;
        }
        ArraySet arraySet = this.mUids;
        if (arraySet == null) {
            return false;
        }
        arraySet = new ArraySet(arraySet);
        for (UidRange range : comparedUids) {
            if (!arraySet.contains(range)) {
                return false;
            }
            arraySet.remove(range);
        }
        return arraySet.isEmpty();
    }

    public boolean satisfiedByUids(NetworkCapabilities nc) {
        if (nc.mUids != null) {
            ArraySet arraySet = this.mUids;
            if (arraySet != null) {
                Iterator it = arraySet.iterator();
                while (it.hasNext()) {
                    UidRange requiredRange = (UidRange) it.next();
                    if (requiredRange.contains(nc.mEstablishingVpnAppUid)) {
                        return true;
                    }
                    if (!nc.appliesToUidRange(requiredRange)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    @VisibleForTesting
    public boolean appliesToUidRange(UidRange requiredRange) {
        ArraySet arraySet = this.mUids;
        if (arraySet == null) {
            return true;
        }
        Iterator it = arraySet.iterator();
        while (it.hasNext()) {
            if (((UidRange) it.next()).containsRange(requiredRange)) {
                return true;
            }
        }
        return false;
    }

    private void combineUids(NetworkCapabilities nc) {
        ArraySet arraySet = nc.mUids;
        if (arraySet != null) {
            ArraySet arraySet2 = this.mUids;
            if (arraySet2 != null) {
                arraySet2.addAll(arraySet);
                return;
            }
        }
        this.mUids = null;
    }

    public NetworkCapabilities setSSID(String ssid) {
        this.mSSID = ssid;
        return this;
    }

    public String getSSID() {
        return this.mSSID;
    }

    public boolean equalsSSID(NetworkCapabilities nc) {
        return Objects.equals(this.mSSID, nc.mSSID);
    }

    public boolean satisfiedBySSID(NetworkCapabilities nc) {
        String str = this.mSSID;
        return str == null || str.equals(nc.mSSID);
    }

    private void combineSSIDs(NetworkCapabilities nc) {
        String str = this.mSSID;
        if (str == null || str.equals(nc.mSSID)) {
            setSSID(nc.mSSID);
            return;
        }
        throw new IllegalStateException("Can't combine two SSIDs");
    }

    public void combineCapabilities(NetworkCapabilities nc) {
        combineNetCapabilities(nc);
        combineTransportTypes(nc);
        combineLinkBandwidths(nc);
        combineSpecifiers(nc);
        combineTransportInfos(nc);
        combineSignalStrength(nc);
        combineUids(nc);
        combineSSIDs(nc);
    }

    private boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        return nc != null && satisfiedByNetCapabilities(nc, onlyImmutable) && satisfiedByTransportTypes(nc) && ((onlyImmutable || satisfiedByLinkBandwidths(nc)) && satisfiedBySpecifier(nc) && ((onlyImmutable || satisfiedBySignalStrength(nc)) && ((onlyImmutable || satisfiedByUids(nc)) && (onlyImmutable || satisfiedBySSID(nc)))));
    }

    @SystemApi
    public boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, false);
    }

    public boolean satisfiedByImmutableNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, true);
    }

    public String describeImmutableDifferences(NetworkCapabilities that) {
        if (that == null) {
            return "other NetworkCapabilities was null";
        }
        String before;
        String after;
        StringJoiner joiner = new StringJoiner(", ");
        long oldImmutableCapabilities = this.mNetworkCapabilities & -20924417;
        long newImmutableCapabilities = -20924417 & that.mNetworkCapabilities;
        if (oldImmutableCapabilities != newImmutableCapabilities) {
            before = capabilityNamesOf(BitUtils.unpackBits(oldImmutableCapabilities));
            after = capabilityNamesOf(BitUtils.unpackBits(newImmutableCapabilities));
            joiner.add(String.format("immutable capabilities changed: %s -> %s", new Object[]{before, after}));
        }
        if (!equalsSpecifier(that)) {
            NetworkSpecifier before2 = getNetworkSpecifier();
            NetworkSpecifier after2 = that.getNetworkSpecifier();
            joiner.add(String.format("specifier changed: %s -> %s", new Object[]{before2, after2}));
        }
        if (!equalsTransportTypes(that)) {
            before = transportNamesOf(getTransportTypes());
            after = transportNamesOf(that.getTransportTypes());
            joiner.add(String.format("transports changed: %s -> %s", new Object[]{before, after}));
        }
        return joiner.toString();
    }

    public boolean equalRequestableCapabilities(NetworkCapabilities nc) {
        boolean z = false;
        if (nc == null) {
            return false;
        }
        if (equalsNetCapabilitiesRequestable(nc) && equalsTransportTypes(nc) && equalsSpecifier(nc)) {
            z = true;
        }
        return z;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof NetworkCapabilities)) {
            return false;
        }
        NetworkCapabilities that = (NetworkCapabilities) obj;
        if (equalsNetCapabilities(that) && equalsTransportTypes(that) && equalsLinkBandwidths(that) && equalsSignalStrength(that) && equalsSpecifier(that) && equalsTransportInfo(that) && equalsUids(that) && equalsSSID(that)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        long j = this.mNetworkCapabilities;
        int i = ((int) (j & -1)) + (((int) (j >> 32)) * 3);
        j = this.mUnwantedNetworkCapabilities;
        i = (i + (((int) (j & -1)) * 5)) + (((int) (j >> 32)) * 7);
        j = this.mTransportTypes;
        return ((((((((i + (((int) (-1 & j)) * 11)) + (((int) (j >> 32)) * 13)) + (this.mLinkUpBandwidthKbps * 17)) + (this.mLinkDownBandwidthKbps * 19)) + (Objects.hashCode(this.mNetworkSpecifier) * 23)) + (this.mSignalStrength * 29)) + (Objects.hashCode(this.mUids) * 31)) + (Objects.hashCode(this.mSSID) * 37)) + (Objects.hashCode(this.mTransportInfo) * 41);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mNetworkCapabilities);
        dest.writeLong(this.mUnwantedNetworkCapabilities);
        dest.writeLong(this.mTransportTypes);
        dest.writeInt(this.mLinkUpBandwidthKbps);
        dest.writeInt(this.mLinkDownBandwidthKbps);
        dest.writeParcelable((Parcelable) this.mNetworkSpecifier, flags);
        dest.writeParcelable((Parcelable) this.mTransportInfo, flags);
        dest.writeInt(this.mSignalStrength);
        dest.writeArraySet(this.mUids);
        dest.writeString(this.mSSID);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (0 != this.mTransportTypes) {
            sb.append(" Transports: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mTransportTypes, -$$Lambda$FpGXkd3pLxeXY58eJ_84mi1PLWQ.INSTANCE, "|");
        }
        String str = "&";
        if (0 != this.mNetworkCapabilities) {
            sb.append(" Capabilities: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mNetworkCapabilities, -$$Lambda$p1_56lwnt1xBuY1muPblbN1Dtkw.INSTANCE, str);
        }
        if (0 != this.mUnwantedNetworkCapabilities) {
            sb.append(" Unwanted: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mUnwantedNetworkCapabilities, -$$Lambda$p1_56lwnt1xBuY1muPblbN1Dtkw.INSTANCE, str);
        }
        str = "Kbps";
        if (this.mLinkUpBandwidthKbps > 0) {
            sb.append(" LinkUpBandwidth>=");
            sb.append(this.mLinkUpBandwidthKbps);
            sb.append(str);
        }
        if (this.mLinkDownBandwidthKbps > 0) {
            sb.append(" LinkDnBandwidth>=");
            sb.append(this.mLinkDownBandwidthKbps);
            sb.append(str);
        }
        str = ">";
        if (this.mNetworkSpecifier != null) {
            sb.append(" Specifier: <");
            sb.append(this.mNetworkSpecifier);
            sb.append(str);
        }
        if (this.mTransportInfo != null) {
            sb.append(" TransportInfo: <");
            sb.append(this.mTransportInfo);
            sb.append(str);
        }
        if (hasSignalStrength()) {
            sb.append(" SignalStrength: ");
            sb.append(this.mSignalStrength);
        }
        ArraySet arraySet = this.mUids;
        if (arraySet != null) {
            if (1 == arraySet.size() && ((UidRange) this.mUids.valueAt(0)).count() == 1) {
                sb.append(" Uid: ");
                sb.append(((UidRange) this.mUids.valueAt(0)).start);
            } else {
                sb.append(" Uids: <");
                sb.append(this.mUids);
                sb.append(str);
            }
        }
        if (this.mEstablishingVpnAppUid != -1) {
            sb.append(" EstablishingAppUid: ");
            sb.append(this.mEstablishingVpnAppUid);
        }
        if (this.mSSID != null) {
            sb.append(" SSID: ");
            sb.append(this.mSSID);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void appendStringRepresentationOfBitMaskToStringBuilder(StringBuilder sb, long bitMask, NameOf nameFetcher, String separator) {
        int bitPos = 0;
        boolean firstElementAdded = false;
        while (bitMask != 0) {
            if ((1 & bitMask) != 0) {
                if (firstElementAdded) {
                    sb.append(separator);
                } else {
                    firstElementAdded = true;
                }
                sb.append(nameFetcher.nameOf(bitPos));
            }
            bitMask >>= 1;
            bitPos++;
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        int i = 0;
        for (int transport : getTransportTypes()) {
            proto.write(2259152797697L, transport);
        }
        int[] capabilities = getCapabilities();
        int length = capabilities.length;
        while (i < length) {
            proto.write(2259152797698L, capabilities[i]);
            i++;
        }
        proto.write(1120986464259L, this.mLinkUpBandwidthKbps);
        proto.write(1120986464260L, this.mLinkDownBandwidthKbps);
        NetworkSpecifier networkSpecifier = this.mNetworkSpecifier;
        if (networkSpecifier != null) {
            proto.write(1138166333445L, networkSpecifier.toString());
        }
        TransportInfo transportInfo = this.mTransportInfo;
        proto.write(1133871366150L, hasSignalStrength());
        proto.write((long) NetworkCapabilitiesProto.SIGNAL_STRENGTH, this.mSignalStrength);
        proto.end(token);
    }

    public static String capabilityNamesOf(int[] capabilities) {
        StringJoiner joiner = new StringJoiner("|");
        if (capabilities != null) {
            for (int c : capabilities) {
                joiner.add(capabilityNameOf(c));
            }
        }
        return joiner.toString();
    }

    public static String capabilityNameOf(int capability) {
        switch (capability) {
            case 0:
                return "MMS";
            case 1:
                return "SUPL";
            case 2:
                return "DUN";
            case 3:
                return "FOTA";
            case 4:
                return "IMS";
            case 5:
                return "CBS";
            case 6:
                return "WIFI_P2P";
            case 7:
                return "IA";
            case 8:
                return "RCS";
            case 9:
                return "XCAP";
            case 10:
                return "EIMS";
            case 11:
                return "NOT_METERED";
            case 12:
                return "INTERNET";
            case 13:
                return "NOT_RESTRICTED";
            case 14:
                return "TRUSTED";
            case 15:
                return "NOT_VPN";
            case 16:
                return "VALIDATED";
            case 17:
                return "CAPTIVE_PORTAL";
            case 18:
                return "NOT_ROAMING";
            case 19:
                return "FOREGROUND";
            case 20:
                return "NOT_CONGESTED";
            case 21:
                return "NOT_SUSPENDED";
            case 22:
                return "OEM_PAID";
            case 23:
                return "MCX";
            case 24:
                return "PARTIAL_CONNECTIVITY";
            default:
                return Integer.toString(capability);
        }
    }

    @UnsupportedAppUsage
    public static String transportNamesOf(int[] types) {
        StringJoiner joiner = new StringJoiner("|");
        if (types != null) {
            for (int t : types) {
                joiner.add(transportNameOf(t));
            }
        }
        return joiner.toString();
    }

    public static String transportNameOf(int transport) {
        if (isValidTransport(transport)) {
            return TRANSPORT_NAMES[transport];
        }
        return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
    }

    private static void checkValidTransportType(int transport) {
        boolean isValidTransport = isValidTransport(transport);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid TransportType ");
        stringBuilder.append(transport);
        Preconditions.checkArgument(isValidTransport, stringBuilder.toString());
    }

    private static boolean isValidCapability(int capability) {
        return capability >= 0 && capability <= 24;
    }

    private static void checkValidCapability(int capability) {
        boolean isValidCapability = isValidCapability(capability);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NetworkCapability ");
        stringBuilder.append(capability);
        stringBuilder.append("out of range");
        Preconditions.checkArgument(isValidCapability, stringBuilder.toString());
    }

    public boolean isMetered() {
        return hasCapability(11) ^ 1;
    }
}
