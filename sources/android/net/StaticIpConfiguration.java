package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.shared.InetAddressUtils;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class StaticIpConfiguration implements Parcelable {
    public static final Creator<StaticIpConfiguration> CREATOR = new Creator<StaticIpConfiguration>() {
        public StaticIpConfiguration createFromParcel(Parcel in) {
            return StaticIpConfiguration.readFromParcel(in);
        }

        public StaticIpConfiguration[] newArray(int size) {
            return new StaticIpConfiguration[size];
        }
    };
    @UnsupportedAppUsage
    public final ArrayList<InetAddress> dnsServers;
    @UnsupportedAppUsage
    public String domains;
    @UnsupportedAppUsage
    public InetAddress gateway;
    @UnsupportedAppUsage
    public LinkAddress ipAddress;

    public static final class Builder {
        private Iterable<InetAddress> mDnsServers;
        private String mDomains;
        private InetAddress mGateway;
        private LinkAddress mIpAddress;

        public Builder setIpAddress(LinkAddress ipAddress) {
            this.mIpAddress = ipAddress;
            return this;
        }

        public Builder setGateway(InetAddress gateway) {
            this.mGateway = gateway;
            return this;
        }

        public Builder setDnsServers(Iterable<InetAddress> dnsServers) {
            this.mDnsServers = dnsServers;
            return this;
        }

        public Builder setDomains(String newDomains) {
            this.mDomains = newDomains;
            return this;
        }

        public StaticIpConfiguration build() {
            StaticIpConfiguration config = new StaticIpConfiguration();
            config.ipAddress = this.mIpAddress;
            config.gateway = this.mGateway;
            for (InetAddress server : this.mDnsServers) {
                config.dnsServers.add(server);
            }
            config.domains = this.mDomains;
            return config;
        }
    }

    public StaticIpConfiguration() {
        this.dnsServers = new ArrayList();
    }

    public StaticIpConfiguration(StaticIpConfiguration source) {
        this();
        if (source != null) {
            this.ipAddress = source.ipAddress;
            this.gateway = source.gateway;
            this.dnsServers.addAll(source.dnsServers);
            this.domains = source.domains;
        }
    }

    public void clear() {
        this.ipAddress = null;
        this.gateway = null;
        this.dnsServers.clear();
        this.domains = null;
    }

    public LinkAddress getIpAddress() {
        return this.ipAddress;
    }

    public InetAddress getGateway() {
        return this.gateway;
    }

    public List<InetAddress> getDnsServers() {
        return this.dnsServers;
    }

    public String getDomains() {
        return this.domains;
    }

    public void addDnsServer(InetAddress server) {
        this.dnsServers.add(server);
    }

    public List<RouteInfo> getRoutes(String iface) {
        List<RouteInfo> routes = new ArrayList(3);
        LinkAddress linkAddress = this.ipAddress;
        if (linkAddress != null) {
            RouteInfo connectedRoute = new RouteInfo(linkAddress, null, iface);
            routes.add(connectedRoute);
            InetAddress inetAddress = this.gateway;
            if (!(inetAddress == null || connectedRoute.matches(inetAddress))) {
                routes.add(RouteInfo.makeHostRoute(this.gateway, iface));
            }
        }
        InetAddress inetAddress2 = this.gateway;
        if (inetAddress2 != null) {
            routes.add(new RouteInfo((IpPrefix) null, inetAddress2, iface));
        }
        return routes;
    }

    public LinkProperties toLinkProperties(String iface) {
        LinkProperties lp = new LinkProperties();
        lp.setInterfaceName(iface);
        LinkAddress linkAddress = this.ipAddress;
        if (linkAddress != null) {
            lp.addLinkAddress(linkAddress);
        }
        for (RouteInfo route : getRoutes(iface)) {
            lp.addRoute(route);
        }
        Iterator it = this.dnsServers.iterator();
        while (it.hasNext()) {
            lp.addDnsServer((InetAddress) it.next());
        }
        lp.setDomains(this.domains);
        return lp;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("IP address ");
        LinkAddress linkAddress = this.ipAddress;
        String str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (linkAddress != null) {
            str.append(linkAddress);
            str.append(str2);
        }
        str.append("Gateway ");
        InetAddress inetAddress = this.gateway;
        if (inetAddress != null) {
            str.append(inetAddress.getHostAddress());
            str.append(str2);
        }
        str.append(" DNS servers: [");
        Iterator it = this.dnsServers.iterator();
        while (it.hasNext()) {
            InetAddress dnsServer = (InetAddress) it.next();
            str.append(str2);
            str.append(dnsServer.getHostAddress());
        }
        str.append(" ] Domains ");
        String str3 = this.domains;
        if (str3 != null) {
            str.append(str3);
        }
        return str.toString();
    }

    public int hashCode() {
        int result = 13 * 47;
        LinkAddress linkAddress = this.ipAddress;
        int i = 0;
        int hashCode = (result + (linkAddress == null ? 0 : linkAddress.hashCode())) * 47;
        InetAddress inetAddress = this.gateway;
        result = (hashCode + (inetAddress == null ? 0 : inetAddress.hashCode())) * 47;
        String str = this.domains;
        if (str != null) {
            i = str.hashCode();
        }
        return ((result + i) * 47) + this.dnsServers.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticIpConfiguration)) {
            return false;
        }
        StaticIpConfiguration other = (StaticIpConfiguration) obj;
        if (!(other != null && Objects.equals(this.ipAddress, other.ipAddress) && Objects.equals(this.gateway, other.gateway) && this.dnsServers.equals(other.dnsServers) && Objects.equals(this.domains, other.domains))) {
            z = false;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ipAddress, flags);
        InetAddressUtils.parcelInetAddress(dest, this.gateway, flags);
        dest.writeInt(this.dnsServers.size());
        Iterator it = this.dnsServers.iterator();
        while (it.hasNext()) {
            InetAddressUtils.parcelInetAddress(dest, (InetAddress) it.next(), flags);
        }
        dest.writeString(this.domains);
    }

    public static StaticIpConfiguration readFromParcel(Parcel in) {
        StaticIpConfiguration s = new StaticIpConfiguration();
        s.ipAddress = (LinkAddress) in.readParcelable(null);
        s.gateway = InetAddressUtils.unparcelInetAddress(in);
        s.dnsServers.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            s.dnsServers.add(InetAddressUtils.unparcelInetAddress(in));
        }
        s.domains = in.readString();
        return s;
    }
}
