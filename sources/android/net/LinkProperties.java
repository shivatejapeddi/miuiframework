package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class LinkProperties implements Parcelable {
    public static final Creator<LinkProperties> CREATOR = new Creator<LinkProperties>() {
        public LinkProperties createFromParcel(Parcel in) {
            int i;
            LinkProperties netProp = new LinkProperties();
            String iface = in.readString();
            if (iface != null) {
                netProp.setInterfaceName(iface);
            }
            int addressCount = in.readInt();
            for (i = 0; i < addressCount; i++) {
                netProp.addLinkAddress((LinkAddress) in.readParcelable(null));
            }
            addressCount = in.readInt();
            for (i = 0; i < addressCount; i++) {
                try {
                    netProp.addDnsServer(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e) {
                }
            }
            addressCount = in.readInt();
            for (i = 0; i < addressCount; i++) {
                try {
                    netProp.addValidatedPrivateDnsServer(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e2) {
                }
            }
            netProp.setUsePrivateDns(in.readBoolean());
            netProp.setPrivateDnsServerName(in.readString());
            addressCount = in.readInt();
            for (i = 0; i < addressCount; i++) {
                try {
                    netProp.addPcscfServer(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e3) {
                }
            }
            netProp.setDomains(in.readString());
            netProp.setMtu(in.readInt());
            netProp.setTcpBufferSizes(in.readString());
            addressCount = in.readInt();
            for (i = 0; i < addressCount; i++) {
                netProp.addRoute((RouteInfo) in.readParcelable(null));
            }
            if (in.readByte() == (byte) 1) {
                netProp.setHttpProxy((ProxyInfo) in.readParcelable(null));
            }
            netProp.setNat64Prefix((IpPrefix) in.readParcelable(null));
            ArrayList<LinkProperties> stackedLinks = new ArrayList();
            in.readList(stackedLinks, LinkProperties.class.getClassLoader());
            Iterator it = stackedLinks.iterator();
            while (it.hasNext()) {
                netProp.addStackedLink((LinkProperties) it.next());
            }
            return netProp;
        }

        public LinkProperties[] newArray(int size) {
            return new LinkProperties[size];
        }
    };
    private static final int MAX_MTU = 10000;
    private static final int MIN_MTU = 68;
    private static final int MIN_MTU_V6 = 1280;
    private final ArrayList<InetAddress> mDnses = new ArrayList();
    private String mDomains;
    private ProxyInfo mHttpProxy;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mIfaceName;
    private final ArrayList<LinkAddress> mLinkAddresses = new ArrayList();
    private int mMtu;
    private IpPrefix mNat64Prefix;
    private final ArrayList<InetAddress> mPcscfs = new ArrayList();
    private String mPrivateDnsServerName;
    private ArrayList<RouteInfo> mRoutes = new ArrayList();
    private Hashtable<String, LinkProperties> mStackedLinks = new Hashtable();
    private String mTcpBufferSizes;
    private boolean mUsePrivateDns;
    private final ArrayList<InetAddress> mValidatedPrivateDnses = new ArrayList();

    public static class CompareResult<T> {
        public final List<T> added = new ArrayList();
        public final List<T> removed = new ArrayList();

        public CompareResult(Collection<T> oldItems, Collection<T> newItems) {
            if (oldItems != null) {
                this.removed.addAll(oldItems);
            }
            if (newItems != null) {
                for (T newItem : newItems) {
                    if (!this.removed.remove(newItem)) {
                        this.added.add(newItem);
                    }
                }
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removed=[");
            CharSequence charSequence = ",";
            stringBuilder.append(TextUtils.join(charSequence, this.removed));
            stringBuilder.append("] added=[");
            stringBuilder.append(TextUtils.join(charSequence, this.added));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public enum ProvisioningChange {
        STILL_NOT_PROVISIONED,
        LOST_PROVISIONING,
        GAINED_PROVISIONING,
        STILL_PROVISIONED
    }

    @UnsupportedAppUsage
    public static ProvisioningChange compareProvisioning(LinkProperties before, LinkProperties after) {
        if (before.isProvisioned() && after.isProvisioned()) {
            if ((!before.isIpv4Provisioned() || after.isIpv4Provisioned()) && (!before.isIpv6Provisioned() || after.isIpv6Provisioned())) {
                return ProvisioningChange.STILL_PROVISIONED;
            }
            return ProvisioningChange.LOST_PROVISIONING;
        } else if (before.isProvisioned() && !after.isProvisioned()) {
            return ProvisioningChange.LOST_PROVISIONING;
        } else {
            if (before.isProvisioned() || !after.isProvisioned()) {
                return ProvisioningChange.STILL_NOT_PROVISIONED;
            }
            return ProvisioningChange.GAINED_PROVISIONING;
        }
    }

    @SystemApi
    public LinkProperties(LinkProperties source) {
        if (source != null) {
            this.mIfaceName = source.mIfaceName;
            this.mLinkAddresses.addAll(source.mLinkAddresses);
            this.mDnses.addAll(source.mDnses);
            this.mValidatedPrivateDnses.addAll(source.mValidatedPrivateDnses);
            this.mUsePrivateDns = source.mUsePrivateDns;
            this.mPrivateDnsServerName = source.mPrivateDnsServerName;
            this.mPcscfs.addAll(source.mPcscfs);
            this.mDomains = source.mDomains;
            this.mRoutes.addAll(source.mRoutes);
            ProxyInfo proxyInfo = source.mHttpProxy;
            this.mHttpProxy = proxyInfo == null ? null : new ProxyInfo(proxyInfo);
            for (LinkProperties l : source.mStackedLinks.values()) {
                addStackedLink(l);
            }
            setMtu(source.mMtu);
            this.mTcpBufferSizes = source.mTcpBufferSizes;
            this.mNat64Prefix = source.mNat64Prefix;
        }
    }

    public void setInterfaceName(String iface) {
        this.mIfaceName = iface;
        ArrayList<RouteInfo> newRoutes = new ArrayList(this.mRoutes.size());
        Iterator it = this.mRoutes.iterator();
        while (it.hasNext()) {
            newRoutes.add(routeWithInterface((RouteInfo) it.next()));
        }
        this.mRoutes = newRoutes;
    }

    public String getInterfaceName() {
        return this.mIfaceName;
    }

    @UnsupportedAppUsage
    public List<String> getAllInterfaceNames() {
        List<String> interfaceNames = new ArrayList(this.mStackedLinks.size() + 1);
        String str = this.mIfaceName;
        if (str != null) {
            interfaceNames.add(str);
        }
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            interfaceNames.addAll(stacked.getAllInterfaceNames());
        }
        return interfaceNames;
    }

    @UnsupportedAppUsage
    public List<InetAddress> getAddresses() {
        List<InetAddress> addresses = new ArrayList();
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            addresses.add(((LinkAddress) it.next()).getAddress());
        }
        return Collections.unmodifiableList(addresses);
    }

    @UnsupportedAppUsage
    public List<InetAddress> getAllAddresses() {
        List<InetAddress> addresses = new ArrayList();
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            addresses.add(((LinkAddress) it.next()).getAddress());
        }
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            addresses.addAll(stacked.getAllAddresses());
        }
        return addresses;
    }

    private int findLinkAddressIndex(LinkAddress address) {
        for (int i = 0; i < this.mLinkAddresses.size(); i++) {
            if (((LinkAddress) this.mLinkAddresses.get(i)).isSameAddressAs(address)) {
                return i;
            }
        }
        return -1;
    }

    @SystemApi
    public boolean addLinkAddress(LinkAddress address) {
        if (address == null) {
            return false;
        }
        int i = findLinkAddressIndex(address);
        if (i < 0) {
            this.mLinkAddresses.add(address);
            return true;
        } else if (((LinkAddress) this.mLinkAddresses.get(i)).equals(address)) {
            return false;
        } else {
            this.mLinkAddresses.set(i, address);
            return true;
        }
    }

    @SystemApi
    public boolean removeLinkAddress(LinkAddress toRemove) {
        int i = findLinkAddressIndex(toRemove);
        if (i < 0) {
            return false;
        }
        this.mLinkAddresses.remove(i);
        return true;
    }

    public List<LinkAddress> getLinkAddresses() {
        return Collections.unmodifiableList(this.mLinkAddresses);
    }

    @UnsupportedAppUsage
    public List<LinkAddress> getAllLinkAddresses() {
        List<LinkAddress> addresses = new ArrayList(this.mLinkAddresses);
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            addresses.addAll(stacked.getAllLinkAddresses());
        }
        return addresses;
    }

    public void setLinkAddresses(Collection<LinkAddress> addresses) {
        this.mLinkAddresses.clear();
        for (LinkAddress address : addresses) {
            addLinkAddress(address);
        }
    }

    @SystemApi
    public boolean addDnsServer(InetAddress dnsServer) {
        if (dnsServer == null || this.mDnses.contains(dnsServer)) {
            return false;
        }
        this.mDnses.add(dnsServer);
        return true;
    }

    @SystemApi
    public boolean removeDnsServer(InetAddress dnsServer) {
        return this.mDnses.remove(dnsServer);
    }

    public void setDnsServers(Collection<InetAddress> dnsServers) {
        this.mDnses.clear();
        for (InetAddress dnsServer : dnsServers) {
            addDnsServer(dnsServer);
        }
    }

    public List<InetAddress> getDnsServers() {
        return Collections.unmodifiableList(this.mDnses);
    }

    @SystemApi
    public void setUsePrivateDns(boolean usePrivateDns) {
        this.mUsePrivateDns = usePrivateDns;
    }

    public boolean isPrivateDnsActive() {
        return this.mUsePrivateDns;
    }

    @SystemApi
    public void setPrivateDnsServerName(String privateDnsServerName) {
        this.mPrivateDnsServerName = privateDnsServerName;
    }

    public String getPrivateDnsServerName() {
        return this.mPrivateDnsServerName;
    }

    public boolean addValidatedPrivateDnsServer(InetAddress dnsServer) {
        if (dnsServer == null || this.mValidatedPrivateDnses.contains(dnsServer)) {
            return false;
        }
        this.mValidatedPrivateDnses.add(dnsServer);
        return true;
    }

    public boolean removeValidatedPrivateDnsServer(InetAddress dnsServer) {
        return this.mValidatedPrivateDnses.remove(dnsServer);
    }

    @SystemApi
    public void setValidatedPrivateDnsServers(Collection<InetAddress> dnsServers) {
        this.mValidatedPrivateDnses.clear();
        for (InetAddress dnsServer : dnsServers) {
            addValidatedPrivateDnsServer(dnsServer);
        }
    }

    @SystemApi
    public List<InetAddress> getValidatedPrivateDnsServers() {
        return Collections.unmodifiableList(this.mValidatedPrivateDnses);
    }

    public boolean addPcscfServer(InetAddress pcscfServer) {
        if (pcscfServer == null || this.mPcscfs.contains(pcscfServer)) {
            return false;
        }
        this.mPcscfs.add(pcscfServer);
        return true;
    }

    public boolean removePcscfServer(InetAddress pcscfServer) {
        return this.mPcscfs.remove(pcscfServer);
    }

    @SystemApi
    public void setPcscfServers(Collection<InetAddress> pcscfServers) {
        this.mPcscfs.clear();
        for (InetAddress pcscfServer : pcscfServers) {
            addPcscfServer(pcscfServer);
        }
    }

    @SystemApi
    public List<InetAddress> getPcscfServers() {
        return Collections.unmodifiableList(this.mPcscfs);
    }

    public void setDomains(String domains) {
        this.mDomains = domains;
    }

    public String getDomains() {
        return this.mDomains;
    }

    public void setMtu(int mtu) {
        this.mMtu = mtu;
    }

    public int getMtu() {
        return this.mMtu;
    }

    @SystemApi
    public void setTcpBufferSizes(String tcpBufferSizes) {
        this.mTcpBufferSizes = tcpBufferSizes;
    }

    @SystemApi
    public String getTcpBufferSizes() {
        return this.mTcpBufferSizes;
    }

    private RouteInfo routeWithInterface(RouteInfo route) {
        return new RouteInfo(route.getDestination(), route.getGateway(), this.mIfaceName, route.getType());
    }

    public boolean addRoute(RouteInfo route) {
        String routeIface = route.getInterface();
        if (routeIface == null || routeIface.equals(this.mIfaceName)) {
            route = routeWithInterface(route);
            if (this.mRoutes.contains(route)) {
                return false;
            }
            this.mRoutes.add(route);
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Route added with non-matching interface: ");
        stringBuilder.append(routeIface);
        stringBuilder.append(" vs. ");
        stringBuilder.append(this.mIfaceName);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @SystemApi
    public boolean removeRoute(RouteInfo route) {
        return Objects.equals(this.mIfaceName, route.getInterface()) && this.mRoutes.remove(route);
    }

    public List<RouteInfo> getRoutes() {
        return Collections.unmodifiableList(this.mRoutes);
    }

    public void ensureDirectlyConnectedRoutes() {
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            addRoute(new RouteInfo((LinkAddress) it.next(), null, this.mIfaceName));
        }
    }

    @UnsupportedAppUsage
    public List<RouteInfo> getAllRoutes() {
        List<RouteInfo> routes = new ArrayList(this.mRoutes);
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            routes.addAll(stacked.getAllRoutes());
        }
        return routes;
    }

    public void setHttpProxy(ProxyInfo proxy) {
        this.mHttpProxy = proxy;
    }

    public ProxyInfo getHttpProxy() {
        return this.mHttpProxy;
    }

    @SystemApi
    public IpPrefix getNat64Prefix() {
        return this.mNat64Prefix;
    }

    @SystemApi
    public void setNat64Prefix(IpPrefix prefix) {
        if (prefix == null || prefix.getPrefixLength() == 96) {
            this.mNat64Prefix = prefix;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Only 96-bit prefixes are supported: ");
        stringBuilder.append(prefix);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public boolean addStackedLink(LinkProperties link) {
        if (link.getInterfaceName() == null) {
            return false;
        }
        this.mStackedLinks.put(link.getInterfaceName(), link);
        return true;
    }

    public boolean removeStackedLink(String iface) {
        return ((LinkProperties) this.mStackedLinks.remove(iface)) != null;
    }

    @UnsupportedAppUsage
    public List<LinkProperties> getStackedLinks() {
        if (this.mStackedLinks.isEmpty()) {
            return Collections.emptyList();
        }
        List<LinkProperties> stacked = new ArrayList();
        for (LinkProperties link : this.mStackedLinks.values()) {
            stacked.add(new LinkProperties(link));
        }
        return Collections.unmodifiableList(stacked);
    }

    public void clear() {
        this.mIfaceName = null;
        this.mLinkAddresses.clear();
        this.mDnses.clear();
        this.mUsePrivateDns = false;
        this.mPrivateDnsServerName = null;
        this.mPcscfs.clear();
        this.mDomains = null;
        this.mRoutes.clear();
        this.mHttpProxy = null;
        this.mStackedLinks.clear();
        this.mMtu = 0;
        this.mTcpBufferSizes = null;
        this.mNat64Prefix = null;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringJoiner resultJoiner = new StringJoiner(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, "{", "}");
        if (this.mIfaceName != null) {
            resultJoiner.add("InterfaceName:");
            resultJoiner.add(this.mIfaceName);
        }
        resultJoiner.add("LinkAddresses: [");
        CharSequence charSequence = ",";
        if (!this.mLinkAddresses.isEmpty()) {
            resultJoiner.add(TextUtils.join(charSequence, this.mLinkAddresses));
        }
        String str = "]";
        resultJoiner.add(str);
        resultJoiner.add("DnsAddresses: [");
        if (!this.mDnses.isEmpty()) {
            resultJoiner.add(TextUtils.join(charSequence, this.mDnses));
        }
        resultJoiner.add(str);
        if (this.mUsePrivateDns) {
            resultJoiner.add("UsePrivateDns: true");
        }
        if (this.mPrivateDnsServerName != null) {
            resultJoiner.add("PrivateDnsServerName:");
            resultJoiner.add(this.mPrivateDnsServerName);
        }
        if (!this.mPcscfs.isEmpty()) {
            resultJoiner.add("PcscfAddresses: [");
            resultJoiner.add(TextUtils.join(charSequence, this.mPcscfs));
            resultJoiner.add(str);
        }
        if (!this.mValidatedPrivateDnses.isEmpty()) {
            StringJoiner validatedPrivateDnsesJoiner = new StringJoiner(charSequence, "ValidatedPrivateDnsAddresses: [", str);
            Iterator it = this.mValidatedPrivateDnses.iterator();
            while (it.hasNext()) {
                validatedPrivateDnsesJoiner.add(((InetAddress) it.next()).getHostAddress());
            }
            resultJoiner.add(validatedPrivateDnsesJoiner.toString());
        }
        resultJoiner.add("Domains:");
        resultJoiner.add(this.mDomains);
        resultJoiner.add("MTU:");
        resultJoiner.add(Integer.toString(this.mMtu));
        if (this.mTcpBufferSizes != null) {
            resultJoiner.add("TcpBufferSizes:");
            resultJoiner.add(this.mTcpBufferSizes);
        }
        resultJoiner.add("Routes: [");
        if (!this.mRoutes.isEmpty()) {
            resultJoiner.add(TextUtils.join(charSequence, this.mRoutes));
        }
        resultJoiner.add(str);
        if (this.mHttpProxy != null) {
            resultJoiner.add("HttpProxy:");
            resultJoiner.add(this.mHttpProxy.toString());
        }
        if (this.mNat64Prefix != null) {
            resultJoiner.add("Nat64Prefix:");
            resultJoiner.add(this.mNat64Prefix.toString());
        }
        Collection<LinkProperties> stackedLinksValues = this.mStackedLinks.values();
        if (!stackedLinksValues.isEmpty()) {
            StringJoiner stackedLinksJoiner = new StringJoiner(charSequence, "Stacked: [", str);
            for (LinkProperties lp : stackedLinksValues) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                stringBuilder.append(lp);
                stringBuilder.append(" ]");
                stackedLinksJoiner.add(stringBuilder.toString());
            }
            resultJoiner.add(stackedLinksJoiner.toString());
        }
        return resultJoiner.toString();
    }

    @SystemApi
    public boolean hasIpv4Address() {
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            if (((LinkAddress) it.next()).getAddress() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasIPv4Address() {
        return hasIpv4Address();
    }

    private boolean hasIpv4AddressOnInterface(String iface) {
        return (Objects.equals(iface, this.mIfaceName) && hasIpv4Address()) || (iface != null && this.mStackedLinks.containsKey(iface) && ((LinkProperties) this.mStackedLinks.get(iface)).hasIpv4Address());
    }

    @SystemApi
    public boolean hasGlobalIpv6Address() {
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress address = (LinkAddress) it.next();
            if ((address.getAddress() instanceof Inet6Address) && address.isGlobalPreferred()) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasGlobalIPv6Address() {
        return hasGlobalIpv6Address();
    }

    @UnsupportedAppUsage
    public boolean hasIpv4DefaultRoute() {
        Iterator it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (((RouteInfo) it.next()).isIPv4Default()) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasIPv4DefaultRoute() {
        return hasIpv4DefaultRoute();
    }

    @SystemApi
    public boolean hasIpv6DefaultRoute() {
        Iterator it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (((RouteInfo) it.next()).isIPv6Default()) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasIPv6DefaultRoute() {
        return hasIpv6DefaultRoute();
    }

    @UnsupportedAppUsage
    public boolean hasIpv4DnsServer() {
        Iterator it = this.mDnses.iterator();
        while (it.hasNext()) {
            if (((InetAddress) it.next()) instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasIPv4DnsServer() {
        return hasIpv4DnsServer();
    }

    @UnsupportedAppUsage
    public boolean hasIpv6DnsServer() {
        Iterator it = this.mDnses.iterator();
        while (it.hasNext()) {
            if (((InetAddress) it.next()) instanceof Inet6Address) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean hasIPv6DnsServer() {
        return hasIpv6DnsServer();
    }

    public boolean hasIpv4PcscfServer() {
        Iterator it = this.mPcscfs.iterator();
        while (it.hasNext()) {
            if (((InetAddress) it.next()) instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIpv6PcscfServer() {
        Iterator it = this.mPcscfs.iterator();
        while (it.hasNext()) {
            if (((InetAddress) it.next()) instanceof Inet6Address) {
                return true;
            }
        }
        return false;
    }

    @SystemApi
    public boolean isIpv4Provisioned() {
        return hasIpv4Address() && hasIpv4DefaultRoute() && hasIpv4DnsServer();
    }

    @SystemApi
    public boolean isIpv6Provisioned() {
        return hasGlobalIpv6Address() && hasIpv6DefaultRoute() && hasIpv6DnsServer();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean isIPv6Provisioned() {
        return isIpv6Provisioned();
    }

    @SystemApi
    public boolean isProvisioned() {
        return isIpv4Provisioned() || isIpv6Provisioned();
    }

    @SystemApi
    public boolean isReachable(InetAddress ip) {
        RouteInfo bestRoute = RouteInfo.selectBestRoute(getAllRoutes(), ip);
        boolean z = false;
        if (bestRoute == null) {
            return false;
        }
        if (ip instanceof Inet4Address) {
            return hasIpv4AddressOnInterface(bestRoute.getInterface());
        }
        if (!(ip instanceof Inet6Address)) {
            return false;
        }
        if (ip.isLinkLocalAddress()) {
            if (((Inet6Address) ip).getScopeId() != 0) {
                z = true;
            }
            return z;
        }
        if (!bestRoute.hasGateway() || hasGlobalIpv6Address()) {
            z = true;
        }
        return z;
    }

    @UnsupportedAppUsage
    public boolean isIdenticalInterfaceName(LinkProperties target) {
        return TextUtils.equals(getInterfaceName(), target.getInterfaceName());
    }

    @UnsupportedAppUsage
    public boolean isIdenticalAddresses(LinkProperties target) {
        Collection<InetAddress> targetAddresses = target.getAddresses();
        Collection<InetAddress> sourceAddresses = getAddresses();
        return sourceAddresses.size() == targetAddresses.size() ? sourceAddresses.containsAll(targetAddresses) : false;
    }

    @UnsupportedAppUsage
    public boolean isIdenticalDnses(LinkProperties target) {
        Collection<InetAddress> targetDnses = target.getDnsServers();
        String targetDomains = target.getDomains();
        String str = this.mDomains;
        boolean z = false;
        if (str == null) {
            if (targetDomains != null) {
                return false;
            }
        } else if (!str.equals(targetDomains)) {
            return false;
        }
        if (this.mDnses.size() == targetDnses.size()) {
            z = this.mDnses.containsAll(targetDnses);
        }
        return z;
    }

    public boolean isIdenticalPrivateDns(LinkProperties target) {
        return isPrivateDnsActive() == target.isPrivateDnsActive() && TextUtils.equals(getPrivateDnsServerName(), target.getPrivateDnsServerName());
    }

    public boolean isIdenticalValidatedPrivateDnses(LinkProperties target) {
        Collection<InetAddress> targetDnses = target.getValidatedPrivateDnsServers();
        return this.mValidatedPrivateDnses.size() == targetDnses.size() ? this.mValidatedPrivateDnses.containsAll(targetDnses) : false;
    }

    public boolean isIdenticalPcscfs(LinkProperties target) {
        Collection<InetAddress> targetPcscfs = target.getPcscfServers();
        return this.mPcscfs.size() == targetPcscfs.size() ? this.mPcscfs.containsAll(targetPcscfs) : false;
    }

    @UnsupportedAppUsage
    public boolean isIdenticalRoutes(LinkProperties target) {
        Collection<RouteInfo> targetRoutes = target.getRoutes();
        return this.mRoutes.size() == targetRoutes.size() ? this.mRoutes.containsAll(targetRoutes) : false;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public boolean isIdenticalHttpProxy(LinkProperties target) {
        if (getHttpProxy() == null) {
            return target.getHttpProxy() == null;
        } else {
            return getHttpProxy().equals(target.getHttpProxy());
        }
    }

    @UnsupportedAppUsage
    public boolean isIdenticalStackedLinks(LinkProperties target) {
        if (!this.mStackedLinks.keySet().equals(target.mStackedLinks.keySet())) {
            return false;
        }
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            if (!stacked.equals(target.mStackedLinks.get(stacked.getInterfaceName()))) {
                return false;
            }
        }
        return true;
    }

    public boolean isIdenticalMtu(LinkProperties target) {
        return getMtu() == target.getMtu();
    }

    public boolean isIdenticalTcpBufferSizes(LinkProperties target) {
        return Objects.equals(this.mTcpBufferSizes, target.mTcpBufferSizes);
    }

    public boolean isIdenticalNat64Prefix(LinkProperties target) {
        return Objects.equals(this.mNat64Prefix, target.mNat64Prefix);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LinkProperties)) {
            return false;
        }
        LinkProperties target = (LinkProperties) obj;
        if (!(isIdenticalInterfaceName(target) && isIdenticalAddresses(target) && isIdenticalDnses(target) && isIdenticalPrivateDns(target) && isIdenticalValidatedPrivateDnses(target) && isIdenticalPcscfs(target) && isIdenticalRoutes(target) && isIdenticalHttpProxy(target) && isIdenticalStackedLinks(target) && isIdenticalMtu(target) && isIdenticalTcpBufferSizes(target) && isIdenticalNat64Prefix(target))) {
            z = false;
        }
        return z;
    }

    public CompareResult<LinkAddress> compareAddresses(LinkProperties target) {
        return new CompareResult(this.mLinkAddresses, target != null ? target.getLinkAddresses() : null);
    }

    public CompareResult<InetAddress> compareDnses(LinkProperties target) {
        return new CompareResult(this.mDnses, target != null ? target.getDnsServers() : null);
    }

    public CompareResult<InetAddress> compareValidatedPrivateDnses(LinkProperties target) {
        return new CompareResult(this.mValidatedPrivateDnses, target != null ? target.getValidatedPrivateDnsServers() : null);
    }

    public CompareResult<RouteInfo> compareAllRoutes(LinkProperties target) {
        return new CompareResult(getAllRoutes(), target != null ? target.getAllRoutes() : null);
    }

    public CompareResult<String> compareAllInterfaceNames(LinkProperties target) {
        return new CompareResult(getAllInterfaceNames(), target != null ? target.getAllInterfaceNames() : null);
    }

    public int hashCode() {
        int i;
        String str;
        String str2 = this.mIfaceName;
        if (str2 == null) {
            i = 0;
        } else {
            i = ((str2.hashCode() + (this.mLinkAddresses.size() * 31)) + (this.mDnses.size() * 37)) + (this.mValidatedPrivateDnses.size() * 61);
            str = this.mDomains;
            i = (i + (str == null ? 0 : str.hashCode())) + (this.mRoutes.size() * 41);
            ProxyInfo proxyInfo = this.mHttpProxy;
            i = (i + (proxyInfo == null ? 0 : proxyInfo.hashCode())) + (this.mStackedLinks.hashCode() * 47);
        }
        i += this.mMtu * 51;
        str = this.mTcpBufferSizes;
        i = ((i + (str == null ? 0 : str.hashCode())) + (this.mUsePrivateDns ? 57 : 0)) + (this.mPcscfs.size() * 67);
        str = this.mPrivateDnsServerName;
        return (i + (str == null ? 0 : str.hashCode())) + Objects.hash(new Object[]{this.mNat64Prefix});
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getInterfaceName());
        dest.writeInt(this.mLinkAddresses.size());
        Iterator it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            dest.writeParcelable((LinkAddress) it.next(), flags);
        }
        dest.writeInt(this.mDnses.size());
        it = this.mDnses.iterator();
        while (it.hasNext()) {
            dest.writeByteArray(((InetAddress) it.next()).getAddress());
        }
        dest.writeInt(this.mValidatedPrivateDnses.size());
        it = this.mValidatedPrivateDnses.iterator();
        while (it.hasNext()) {
            dest.writeByteArray(((InetAddress) it.next()).getAddress());
        }
        dest.writeBoolean(this.mUsePrivateDns);
        dest.writeString(this.mPrivateDnsServerName);
        dest.writeInt(this.mPcscfs.size());
        it = this.mPcscfs.iterator();
        while (it.hasNext()) {
            dest.writeByteArray(((InetAddress) it.next()).getAddress());
        }
        dest.writeString(this.mDomains);
        dest.writeInt(this.mMtu);
        dest.writeString(this.mTcpBufferSizes);
        dest.writeInt(this.mRoutes.size());
        it = this.mRoutes.iterator();
        while (it.hasNext()) {
            dest.writeParcelable((RouteInfo) it.next(), flags);
        }
        if (this.mHttpProxy != null) {
            dest.writeByte((byte) 1);
            dest.writeParcelable(this.mHttpProxy, flags);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeParcelable(this.mNat64Prefix, 0);
        dest.writeList(new ArrayList(this.mStackedLinks.values()));
    }

    public static boolean isValidMtu(int mtu, boolean ipv6) {
        boolean z = true;
        if (ipv6) {
            if (mtu < 1280 || mtu > 10000) {
                z = false;
            }
            return z;
        }
        if (mtu < 68 || mtu > 10000) {
            z = false;
        }
        return z;
    }
}
