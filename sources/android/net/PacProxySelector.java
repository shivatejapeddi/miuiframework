package android.net;

import android.content.IntentFilter;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ServiceManager;
import android.util.Log;
import com.android.net.IProxyService;
import com.android.net.IProxyService.Stub;
import com.google.android.collect.Lists;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PacProxySelector extends ProxySelector {
    private static final String PROXY = "PROXY ";
    public static final String PROXY_SERVICE = "com.android.net.IProxyService";
    private static final String SOCKS = "SOCKS ";
    private static final String TAG = "PacProxySelector";
    private final List<Proxy> mDefaultList;
    private IProxyService mProxyService = Stub.asInterface(ServiceManager.getService(PROXY_SERVICE));

    public PacProxySelector() {
        if (this.mProxyService == null) {
            Log.e(TAG, "PacManager: no proxy service");
        }
        this.mDefaultList = Lists.newArrayList(Proxy.NO_PROXY);
    }

    public List<Proxy> select(URI uri) {
        if (this.mProxyService == null) {
            this.mProxyService = Stub.asInterface(ServiceManager.getService(PROXY_SERVICE));
        }
        IProxyService iProxyService = this.mProxyService;
        String str = TAG;
        if (iProxyService == null) {
            Log.e(str, "select: no proxy service return NO_PROXY");
            return Lists.newArrayList(Proxy.NO_PROXY);
        }
        String urlString;
        String response = null;
        try {
            if (!IntentFilter.SCHEME_HTTP.equalsIgnoreCase(uri.getScheme())) {
                uri = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), "/", null, null);
            }
            urlString = uri.toURL().toString();
        } catch (URISyntaxException e) {
            urlString = uri.getHost();
        } catch (MalformedURLException e2) {
            urlString = uri.getHost();
        }
        try {
            str = this.mProxyService.resolvePacFile(uri.getHost(), urlString);
            response = str;
        } catch (Exception e3) {
            Log.e(str, "Error resolving PAC File", e3);
        }
        if (response == null) {
            return this.mDefaultList;
        }
        return parseResponse(response);
    }

    private static List<Proxy> parseResponse(String response) {
        String[] split = response.split(";");
        List<Proxy> ret = Lists.newArrayList();
        for (String s : split) {
            String trimmed = s.trim();
            if (trimmed.equals("DIRECT")) {
                ret.add(Proxy.NO_PROXY);
            } else {
                String str = PROXY;
                Proxy proxy;
                if (trimmed.startsWith(str)) {
                    proxy = proxyFromHostPort(Type.HTTP, trimmed.substring(str.length()));
                    if (proxy != null) {
                        ret.add(proxy);
                    }
                } else {
                    str = SOCKS;
                    if (trimmed.startsWith(str)) {
                        proxy = proxyFromHostPort(Type.SOCKS, trimmed.substring(str.length()));
                        if (proxy != null) {
                            ret.add(proxy);
                        }
                    }
                }
            }
        }
        if (ret.size() == 0) {
            ret.add(Proxy.NO_PROXY);
        }
        return ret;
    }

    private static Proxy proxyFromHostPort(Type type, String hostPortString) {
        try {
            String[] hostPort = hostPortString.split(":");
            return new Proxy(type, InetSocketAddress.createUnresolved(hostPort[null], Integer.parseInt(hostPort[1])));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to parse proxy ");
            stringBuilder.append(hostPortString);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(e);
            Log.d(TAG, stringBuilder.toString());
            return null;
        }
    }

    public void connectFailed(URI uri, SocketAddress address, IOException failure) {
    }
}
