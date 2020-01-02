package android.webkit;

import android.annotation.UnsupportedAppUsage;
import java.io.InputStream;
import java.util.Map;

@Deprecated
public final class PluginData {
    private long mContentLength;
    private Map<String, String[]> mHeaders;
    private int mStatusCode;
    private InputStream mStream;

    @Deprecated
    @UnsupportedAppUsage
    public PluginData(InputStream stream, long length, Map<String, String[]> headers, int code) {
        this.mStream = stream;
        this.mContentLength = length;
        this.mHeaders = headers;
        this.mStatusCode = code;
    }

    @Deprecated
    @UnsupportedAppUsage
    public InputStream getInputStream() {
        return this.mStream;
    }

    @Deprecated
    @UnsupportedAppUsage
    public long getContentLength() {
        return this.mContentLength;
    }

    @Deprecated
    @UnsupportedAppUsage
    public Map<String, String[]> getHeaders() {
        return this.mHeaders;
    }

    @Deprecated
    @UnsupportedAppUsage
    public int getStatusCode() {
        return this.mStatusCode;
    }
}
