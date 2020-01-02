package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.IntentFilter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SystemApi
public class WebAddress {
    static final int MATCH_GROUP_AUTHORITY = 2;
    static final int MATCH_GROUP_HOST = 3;
    static final int MATCH_GROUP_PATH = 5;
    static final int MATCH_GROUP_PORT = 4;
    static final int MATCH_GROUP_SCHEME = 1;
    static Pattern sAddressPattern = Pattern.compile("(?:(http|https|file)\\:\\/\\/)?(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯%_-][a-zA-Z0-9 -퟿豈-﷏ﷰ-￯%_\\.-]*|\\[[0-9a-fA-F:\\.]+\\])?(?:\\:([0-9]*))?(\\/?[^#]*)?.*", 2);
    private String mAuthInfo;
    @UnsupportedAppUsage
    private String mHost;
    @UnsupportedAppUsage
    private String mPath;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mPort;
    @UnsupportedAppUsage
    private String mScheme;

    public WebAddress(String address) throws ParseException {
        if (address != null) {
            String str = "";
            this.mScheme = str;
            this.mHost = str;
            this.mPort = -1;
            String str2 = "/";
            this.mPath = str2;
            this.mAuthInfo = str;
            Matcher m = sAddressPattern.matcher(address);
            if (m.matches()) {
                String t = m.group(1);
                if (t != null) {
                    this.mScheme = t.toLowerCase(Locale.ROOT);
                }
                t = m.group(2);
                if (t != null) {
                    this.mAuthInfo = t;
                }
                t = m.group(3);
                if (t != null) {
                    this.mHost = t;
                }
                t = m.group(4);
                if (t != null && t.length() > 0) {
                    try {
                        this.mPort = Integer.parseInt(t);
                    } catch (NumberFormatException e) {
                        throw new ParseException("Bad port");
                    }
                }
                t = m.group(5);
                if (t != null && t.length() > 0) {
                    if (t.charAt(0) == '/') {
                        this.mPath = t;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(t);
                        this.mPath = stringBuilder.toString();
                    }
                }
                int i = this.mPort;
                String str3 = IntentFilter.SCHEME_HTTPS;
                if (i == 443 && this.mScheme.equals(str)) {
                    this.mScheme = str3;
                } else if (this.mPort == -1) {
                    if (this.mScheme.equals(str3)) {
                        this.mPort = 443;
                    } else {
                        this.mPort = 80;
                    }
                }
                if (this.mScheme.equals(str)) {
                    this.mScheme = IntentFilter.SCHEME_HTTP;
                    return;
                }
                return;
            }
            throw new ParseException("Bad address");
        }
        throw new NullPointerException();
    }

    public String toString() {
        StringBuilder stringBuilder;
        String port = "";
        if ((this.mPort != 443 && this.mScheme.equals(IntentFilter.SCHEME_HTTPS)) || (this.mPort != 80 && this.mScheme.equals(IntentFilter.SCHEME_HTTP))) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(":");
            stringBuilder2.append(Integer.toString(this.mPort));
            port = stringBuilder2.toString();
        }
        String authInfo = "";
        if (this.mAuthInfo.length() > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mAuthInfo);
            stringBuilder.append("@");
            authInfo = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.mScheme);
        stringBuilder.append("://");
        stringBuilder.append(authInfo);
        stringBuilder.append(this.mHost);
        stringBuilder.append(port);
        stringBuilder.append(this.mPath);
        return stringBuilder.toString();
    }

    public void setScheme(String scheme) {
        this.mScheme = scheme;
    }

    @UnsupportedAppUsage
    public String getScheme() {
        return this.mScheme;
    }

    @UnsupportedAppUsage
    public void setHost(String host) {
        this.mHost = host;
    }

    @UnsupportedAppUsage
    public String getHost() {
        return this.mHost;
    }

    public void setPort(int port) {
        this.mPort = port;
    }

    @UnsupportedAppUsage
    public int getPort() {
        return this.mPort;
    }

    @UnsupportedAppUsage
    public void setPath(String path) {
        this.mPath = path;
    }

    @UnsupportedAppUsage
    public String getPath() {
        return this.mPath;
    }

    public void setAuthInfo(String authInfo) {
        this.mAuthInfo = authInfo;
    }

    @UnsupportedAppUsage
    public String getAuthInfo() {
        return this.mAuthInfo;
    }
}
