package com.android.internal.net;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.Network;
import android.net.ProxyInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import com.android.internal.R;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VpnConfig implements Parcelable {
    public static final Creator<VpnConfig> CREATOR = new Creator<VpnConfig>() {
        public VpnConfig createFromParcel(Parcel in) {
            VpnConfig config = new VpnConfig();
            config.user = in.readString();
            config.interfaze = in.readString();
            config.session = in.readString();
            config.mtu = in.readInt();
            in.readTypedList(config.addresses, LinkAddress.CREATOR);
            in.readTypedList(config.routes, RouteInfo.CREATOR);
            config.dnsServers = in.createStringArrayList();
            config.searchDomains = in.createStringArrayList();
            config.allowedApplications = in.createStringArrayList();
            config.disallowedApplications = in.createStringArrayList();
            config.configureIntent = (PendingIntent) in.readParcelable(null);
            config.startTime = in.readLong();
            boolean z = true;
            config.legacy = in.readInt() != 0;
            config.blocking = in.readInt() != 0;
            config.allowBypass = in.readInt() != 0;
            config.allowIPv4 = in.readInt() != 0;
            config.allowIPv6 = in.readInt() != 0;
            if (in.readInt() == 0) {
                z = false;
            }
            config.isMetered = z;
            config.underlyingNetworks = (Network[]) in.createTypedArray(Network.CREATOR);
            config.proxyInfo = (ProxyInfo) in.readParcelable(null);
            return config;
        }

        public VpnConfig[] newArray(int size) {
            return new VpnConfig[size];
        }
    };
    public static final String DIALOGS_PACKAGE = "com.android.vpndialogs";
    public static final String LEGACY_VPN = "[Legacy VPN]";
    public static final String SERVICE_INTERFACE = "android.net.VpnService";
    public List<LinkAddress> addresses = new ArrayList();
    public boolean allowBypass;
    public boolean allowIPv4;
    public boolean allowIPv6;
    public List<String> allowedApplications;
    public boolean blocking;
    public PendingIntent configureIntent;
    public List<String> disallowedApplications;
    public List<String> dnsServers;
    public String interfaze;
    public boolean isMetered = true;
    public boolean legacy;
    public int mtu = -1;
    public ProxyInfo proxyInfo;
    public List<RouteInfo> routes = new ArrayList();
    public List<String> searchDomains;
    public String session;
    public long startTime = -1;
    public Network[] underlyingNetworks;
    public String user;

    public static Intent getIntentForConfirmation() {
        Intent intent = new Intent();
        ComponentName componentName = ComponentName.unflattenFromString(Resources.getSystem().getString(R.string.config_customVpnConfirmDialogComponent));
        intent.setClassName(componentName.getPackageName(), componentName.getClassName());
        return intent;
    }

    public static PendingIntent getIntentForStatusPanel(Context context) {
        Intent intent = new Intent();
        intent.setClassName(DIALOGS_PACKAGE, "com.android.vpndialogs.ManageDialog");
        intent.addFlags(1350565888);
        return PendingIntent.getActivityAsUser(context, 0, intent, 0, null, UserHandle.CURRENT);
    }

    public static CharSequence getVpnLabel(Context context, String packageName) throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.net.VpnService");
        intent.setPackage(packageName);
        List<ResolveInfo> services = pm.queryIntentServices(intent, 0);
        if (services == null || services.size() != 1) {
            return pm.getApplicationInfo(packageName, 0).loadLabel(pm);
        }
        return ((ResolveInfo) services.get(0)).loadLabel(pm);
    }

    public void updateAllowedFamilies(InetAddress address) {
        if (address instanceof Inet4Address) {
            this.allowIPv4 = true;
        } else {
            this.allowIPv6 = true;
        }
    }

    public void addLegacyRoutes(String routesStr) {
        if (!routesStr.trim().equals("")) {
            for (String route : routesStr.trim().split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER)) {
                RouteInfo info = new RouteInfo(new IpPrefix(route), null);
                this.routes.add(info);
                updateAllowedFamilies(info.getDestination().getAddress());
            }
        }
    }

    public void addLegacyAddresses(String addressesStr) {
        if (!addressesStr.trim().equals("")) {
            for (String address : addressesStr.trim().split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER)) {
                LinkAddress addr = new LinkAddress(address);
                this.addresses.add(addr);
                updateAllowedFamilies(addr.getAddress());
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.user);
        out.writeString(this.interfaze);
        out.writeString(this.session);
        out.writeInt(this.mtu);
        out.writeTypedList(this.addresses);
        out.writeTypedList(this.routes);
        out.writeStringList(this.dnsServers);
        out.writeStringList(this.searchDomains);
        out.writeStringList(this.allowedApplications);
        out.writeStringList(this.disallowedApplications);
        out.writeParcelable(this.configureIntent, flags);
        out.writeLong(this.startTime);
        out.writeInt(this.legacy);
        out.writeInt(this.blocking);
        out.writeInt(this.allowBypass);
        out.writeInt(this.allowIPv4);
        out.writeInt(this.allowIPv6);
        out.writeInt(this.isMetered);
        out.writeTypedArray(this.underlyingNetworks, flags);
        out.writeParcelable(this.proxyInfo, flags);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VpnConfig");
        stringBuilder.append("{ user=");
        stringBuilder.append(this.user);
        stringBuilder.append(", interface=");
        stringBuilder.append(this.interfaze);
        stringBuilder.append(", session=");
        stringBuilder.append(this.session);
        stringBuilder.append(", mtu=");
        stringBuilder.append(this.mtu);
        stringBuilder.append(", addresses=");
        stringBuilder.append(toString(this.addresses));
        stringBuilder.append(", routes=");
        stringBuilder.append(toString(this.routes));
        stringBuilder.append(", dns=");
        stringBuilder.append(toString(this.dnsServers));
        stringBuilder.append(", searchDomains=");
        stringBuilder.append(toString(this.searchDomains));
        stringBuilder.append(", allowedApps=");
        stringBuilder.append(toString(this.allowedApplications));
        stringBuilder.append(", disallowedApps=");
        stringBuilder.append(toString(this.disallowedApplications));
        stringBuilder.append(", configureIntent=");
        stringBuilder.append(this.configureIntent);
        stringBuilder.append(", startTime=");
        stringBuilder.append(this.startTime);
        stringBuilder.append(", legacy=");
        stringBuilder.append(this.legacy);
        stringBuilder.append(", blocking=");
        stringBuilder.append(this.blocking);
        stringBuilder.append(", allowBypass=");
        stringBuilder.append(this.allowBypass);
        stringBuilder.append(", allowIPv4=");
        stringBuilder.append(this.allowIPv4);
        stringBuilder.append(", allowIPv6=");
        stringBuilder.append(this.allowIPv6);
        stringBuilder.append(", underlyingNetworks=");
        stringBuilder.append(Arrays.toString(this.underlyingNetworks));
        stringBuilder.append(", proxyInfo=");
        stringBuilder.append(this.proxyInfo.toString());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    static <T> String toString(List<T> ls) {
        if (ls == null) {
            return "null";
        }
        return Arrays.toString(ls.toArray());
    }
}
