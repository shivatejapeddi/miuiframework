package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.media.IMediaHTTPConnection.Stub;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.os.IBinder;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import miui.maml.elements.MusicLyricParser;
import miui.maml.util.net.SimpleRequest;

public class MediaHTTPConnection extends Stub {
    private static final int CONNECT_TIMEOUT_MS = 30000;
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "MediaHTTPConnection";
    private static final boolean VERBOSE = false;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private boolean mAllowCrossDomainRedirect = true;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private boolean mAllowCrossProtocolRedirect = true;
    @UnsupportedAppUsage
    private volatile HttpURLConnection mConnection = null;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private long mCurrentOffset = -1;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private Map<String, String> mHeaders = null;
    @GuardedBy({"this"})
    private InputStream mInputStream = null;
    private long mNativeContext;
    private final AtomicInteger mNumDisconnectingThreads = new AtomicInteger(0);
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private long mTotalSize = -1;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private URL mURL = null;

    private final native void native_finalize();

    private final native IBinder native_getIMemory();

    private static final native void native_init();

    private final native int native_readAt(long j, int i);

    private final native void native_setup();

    @UnsupportedAppUsage
    public MediaHTTPConnection() {
        if (CookieHandler.getDefault() == null) {
            Log.w(TAG, "MediaHTTPConnection: Unexpected. No CookieHandler found.");
        }
        native_setup();
    }

    @UnsupportedAppUsage
    public synchronized IBinder connect(String uri, String headers) {
        try {
            disconnect();
            try {
                this.mAllowCrossDomainRedirect = true;
                this.mURL = new URL(uri);
                this.mHeaders = convertHeaderStringToMap(headers);
                return native_getIMemory();
            } catch (MalformedURLException e) {
            }
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    private static boolean parseBoolean(String val) {
        boolean z = true;
        try {
            if (Long.parseLong(val) == 0) {
                z = false;
            }
            return z;
        } catch (NumberFormatException e) {
            if (!("true".equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val))) {
                z = false;
            }
            return z;
        }
    }

    private synchronized boolean filterOutInternalHeaders(String key, String val) {
        if (!"android-allow-cross-domain-redirect".equalsIgnoreCase(key)) {
            return false;
        }
        this.mAllowCrossDomainRedirect = parseBoolean(val);
        this.mAllowCrossProtocolRedirect = this.mAllowCrossDomainRedirect;
        return true;
    }

    private synchronized Map<String, String> convertHeaderStringToMap(String headers) {
        HashMap<String, String> map;
        map = new HashMap();
        for (String pair : headers.split(MusicLyricParser.CRLF)) {
            int colonPos = pair.indexOf(":");
            if (colonPos >= 0) {
                String key = pair.substring(0, colonPos);
                String val = pair.substring(colonPos + 1);
                if (!filterOutInternalHeaders(key, val)) {
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    @UnsupportedAppUsage
    public void disconnect() {
        this.mNumDisconnectingThreads.incrementAndGet();
        try {
            HttpURLConnection connectionToDisconnect = this.mConnection;
            if (connectionToDisconnect != null) {
                connectionToDisconnect.disconnect();
            }
            synchronized (this) {
                teardownConnection();
                this.mHeaders = null;
                this.mURL = null;
            }
            this.mNumDisconnectingThreads.decrementAndGet();
        } catch (Throwable th) {
            this.mNumDisconnectingThreads.decrementAndGet();
        }
    }

    private synchronized void teardownConnection() {
        if (this.mConnection != null) {
            if (this.mInputStream != null) {
                try {
                    this.mInputStream.close();
                } catch (IOException e) {
                }
                this.mInputStream = null;
            }
            this.mConnection.disconnect();
            this.mConnection = null;
            this.mCurrentOffset = -1;
        }
    }

    private static final boolean isLocalHost(URL url) {
        if (url == null) {
            return false;
        }
        String host = url.getHost();
        if (host == null) {
            return false;
        }
        try {
            if (host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST) || NetworkUtils.numericToInetAddress(host).isLoopbackAddress()) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
        }
    }

    private synchronized void seekTo(long offset) throws IOException {
        long j = offset;
        synchronized (this) {
            teardownConnection();
            long location = -1;
            try {
                URL url = this.mURL;
                boolean noProxy = isLocalHost(url);
                int redirectCount = 0;
                while (this.mNumDisconnectingThreads.get() <= 0) {
                    if (noProxy) {
                        this.mConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
                    } else {
                        this.mConnection = (HttpURLConnection) url.openConnection();
                    }
                    if (this.mNumDisconnectingThreads.get() <= 0) {
                        this.mConnection.setConnectTimeout(30000);
                        this.mConnection.setInstanceFollowRedirects(this.mAllowCrossDomainRedirect);
                        if (this.mHeaders != null) {
                            for (Entry<String, String> entry : this.mHeaders.entrySet()) {
                                this.mConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                            }
                        }
                        if (j > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("bytes=");
                            stringBuilder.append(j);
                            stringBuilder.append("-");
                            this.mConnection.setRequestProperty("Range", stringBuilder.toString());
                        }
                        int response = this.mConnection.getResponseCode();
                        if (response == 300 || response == 301 || response == 302 || response == 303 || response == 307) {
                            redirectCount++;
                            if (redirectCount <= 20) {
                                String method = this.mConnection.getRequestMethod();
                                if (response == 307 && !method.equals("GET")) {
                                    if (!method.equals("HEAD")) {
                                        throw new NoRouteToHostException("Invalid redirect");
                                    }
                                }
                                String location2 = this.mConnection.getHeaderField(SimpleRequest.LOCATION);
                                if (location2 != null) {
                                    url = new URL(this.mURL, location2);
                                    if (!url.getProtocol().equals(IntentFilter.SCHEME_HTTPS)) {
                                        if (!url.getProtocol().equals(IntentFilter.SCHEME_HTTP)) {
                                            throw new NoRouteToHostException("Unsupported protocol redirect");
                                        }
                                    }
                                    boolean sameProtocol = this.mURL.getProtocol().equals(url.getProtocol());
                                    if (!this.mAllowCrossProtocolRedirect) {
                                        if (!sameProtocol) {
                                            throw new NoRouteToHostException("Cross-protocol redirects are disallowed");
                                        }
                                    }
                                    boolean sameHost = this.mURL.getHost().equals(url.getHost());
                                    if (!this.mAllowCrossDomainRedirect) {
                                        if (!sameHost) {
                                            throw new NoRouteToHostException("Cross-domain redirects are disallowed");
                                        }
                                    }
                                    if (response != 307) {
                                        this.mURL = url;
                                    }
                                    location = -1;
                                } else {
                                    throw new NoRouteToHostException("Invalid redirect");
                                }
                            }
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Too many redirects: ");
                            stringBuilder2.append(redirectCount);
                            throw new NoRouteToHostException(stringBuilder2.toString());
                        }
                        if (this.mAllowCrossDomainRedirect) {
                            this.mURL = this.mConnection.getURL();
                        }
                        if (response == 206) {
                            String contentRange = this.mConnection.getHeaderField("Content-Range");
                            this.mTotalSize = location;
                            if (contentRange != null) {
                                int lastSlashPos = contentRange.lastIndexOf(47);
                                if (lastSlashPos >= 0) {
                                    try {
                                        this.mTotalSize = Long.parseLong(contentRange.substring(lastSlashPos + 1));
                                    } catch (NumberFormatException e) {
                                    }
                                }
                            }
                        } else if (response == 200) {
                            this.mTotalSize = (long) this.mConnection.getContentLength();
                        } else {
                            throw new IOException();
                        }
                        if (j > 0) {
                            if (response != 206) {
                                throw new ProtocolException();
                            }
                        }
                        this.mInputStream = new BufferedInputStream(this.mConnection.getInputStream());
                        this.mCurrentOffset = j;
                    } else {
                        throw new IOException("concurrently disconnecting");
                    }
                }
                throw new IOException("concurrently disconnecting");
            } catch (IOException e2) {
                this.mTotalSize = -1;
                teardownConnection();
                this.mCurrentOffset = -1;
                throw e2;
            }
        }
    }

    @UnsupportedAppUsage
    public synchronized int readAt(long offset, int size) {
        return native_readAt(offset, size);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x008d A:{Splitter:B:4:0x0014, ExcHandler: ProtocolException (r2_5 'e' java.net.ProtocolException)} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0063 A:{Splitter:B:4:0x0014, ExcHandler: NoRouteToHostException (r2_4 'e' java.net.NoRouteToHostException)} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A:{Splitter:B:4:0x0014, ExcHandler: Exception (e java.lang.Exception)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:17:0x0031, code skipped:
            r2 = e;
     */
    /* JADX WARNING: Missing block: B:20:0x0035, code skipped:
            return -1;
     */
    /* JADX WARNING: Missing block: B:29:0x0063, code skipped:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:31:?, code skipped:
            r3 = TAG;
            r4 = new java.lang.StringBuilder();
            r4.append("readAt ");
            r4.append(r9);
            r4.append(" / ");
            r4.append(r12);
            r4.append(" => ");
            r4.append(r2);
            android.util.Log.w(r3, r4.toString());
     */
    /* JADX WARNING: Missing block: B:33:0x008c, code skipped:
            return android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
     */
    /* JADX WARNING: Missing block: B:34:0x008d, code skipped:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            r3 = TAG;
            r4 = new java.lang.StringBuilder();
            r4.append("readAt ");
            r4.append(r9);
            r4.append(" / ");
            r4.append(r12);
            r4.append(" => ");
            r4.append(r2);
            android.util.Log.w(r3, r4.toString());
     */
    /* JADX WARNING: Missing block: B:38:0x00b6, code skipped:
            return android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
     */
    private synchronized int readAt(long r9, byte[] r11, int r12) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = new android.os.StrictMode$ThreadPolicy$Builder;	 Catch:{ all -> 0x00b7 }
        r0.<init>();	 Catch:{ all -> 0x00b7 }
        r0 = r0.permitAll();	 Catch:{ all -> 0x00b7 }
        r0 = r0.build();	 Catch:{ all -> 0x00b7 }
        android.os.StrictMode.setThreadPolicy(r0);	 Catch:{ all -> 0x00b7 }
        r1 = -1010; // 0xfffffffffffffc0e float:NaN double:NaN;
        r2 = -1;
        r3 = r8.mCurrentOffset;	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0039, IOException -> 0x0036, Exception -> 0x0033 }
        r3 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r3 == 0) goto L_0x001d;
    L_0x001a:
        r8.seekTo(r9);	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
    L_0x001d:
        r3 = r8.mInputStream;	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
        r4 = 0;
        r3 = r3.read(r11, r4, r12);	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
        if (r3 != r2) goto L_0x0027;
    L_0x0026:
        r3 = 0;
    L_0x0027:
        r4 = r8.mCurrentOffset;	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
        r6 = (long) r3;	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
        r4 = r4 + r6;
        r8.mCurrentOffset = r4;	 Catch:{ ProtocolException -> 0x008d, NoRouteToHostException -> 0x0063, UnknownServiceException -> 0x0031, IOException -> 0x002f, Exception -> 0x0033 }
        monitor-exit(r8);
        return r3;
    L_0x002f:
        r1 = move-exception;
        goto L_0x0037;
    L_0x0031:
        r2 = move-exception;
        goto L_0x003a;
    L_0x0033:
        r1 = move-exception;
        monitor-exit(r8);
        return r2;
    L_0x0036:
        r1 = move-exception;
    L_0x0037:
        monitor-exit(r8);
        return r2;
    L_0x0039:
        r2 = move-exception;
    L_0x003a:
        r3 = "MediaHTTPConnection";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b7 }
        r4.<init>();	 Catch:{ all -> 0x00b7 }
        r5 = "readAt ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r9);	 Catch:{ all -> 0x00b7 }
        r5 = " / ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r12);	 Catch:{ all -> 0x00b7 }
        r5 = " => ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r2);	 Catch:{ all -> 0x00b7 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b7 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x00b7 }
        monitor-exit(r8);
        return r1;
    L_0x0063:
        r2 = move-exception;
        r3 = "MediaHTTPConnection";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b7 }
        r4.<init>();	 Catch:{ all -> 0x00b7 }
        r5 = "readAt ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r9);	 Catch:{ all -> 0x00b7 }
        r5 = " / ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r12);	 Catch:{ all -> 0x00b7 }
        r5 = " => ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r2);	 Catch:{ all -> 0x00b7 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b7 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x00b7 }
        monitor-exit(r8);
        return r1;
    L_0x008d:
        r2 = move-exception;
        r3 = "MediaHTTPConnection";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b7 }
        r4.<init>();	 Catch:{ all -> 0x00b7 }
        r5 = "readAt ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r9);	 Catch:{ all -> 0x00b7 }
        r5 = " / ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r12);	 Catch:{ all -> 0x00b7 }
        r5 = " => ";
        r4.append(r5);	 Catch:{ all -> 0x00b7 }
        r4.append(r2);	 Catch:{ all -> 0x00b7 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b7 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x00b7 }
        monitor-exit(r8);
        return r1;
    L_0x00b7:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaHTTPConnection.readAt(long, byte[], int):int");
    }

    public synchronized long getSize() {
        if (this.mConnection == null) {
            try {
                seekTo(0);
            } catch (IOException e) {
                return -1;
            }
        }
        return this.mTotalSize;
    }

    @UnsupportedAppUsage
    public synchronized String getMIMEType() {
        if (this.mConnection == null) {
            try {
                seekTo(0);
            } catch (IOException e) {
                return ContentResolver.MIME_TYPE_DEFAULT;
            }
        }
        return this.mConnection.getContentType();
    }

    @UnsupportedAppUsage
    public synchronized String getUri() {
        return this.mURL.toString();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }
}
