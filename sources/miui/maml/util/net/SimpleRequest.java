package miui.maml.util.net;

import android.content.ContentValues;
import android.text.format.DateFormat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

public final class SimpleRequest {
    private static final String CER_12306 = "-----BEGIN CERTIFICATE-----\nMIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4XDTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb29bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHletne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDovLzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Ptx1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQog555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n-----END CERTIFICATE-----";
    private static final boolean DEBUG = false;
    private static final String HOST_12306 = "kyfw.12306.cn";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String LOCATION = "Location";
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String PARAM_IGNORE_12306_CA = "ignore12306ca";
    private static final int TIMEOUT = 30000;
    public static final String UTF8 = "utf-8";
    private static final Logger log = Logger.getLogger(SimpleRequest.class.getSimpleName());
    private static String sUserAgent;

    public static class HeaderContent {
        private final Map<String, String> headers = new HashMap();

        public void putHeader(String key, String value) {
            this.headers.put(key, value);
        }

        public String getHeader(String key) {
            return (String) this.headers.get(key);
        }

        public Map<String, String> getHeaders() {
            return this.headers;
        }

        public void putHeaders(Map<String, String> headers) {
            this.headers.putAll(headers);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HeaderContent{headers=");
            stringBuilder.append(this.headers);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    public static class MapContent extends HeaderContent {
        private Map<String, Object> bodies;

        public MapContent(Map<String, Object> bodies) {
            this.bodies = bodies;
        }

        public Object getFromBody(String key) {
            return this.bodies.get(key);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MapContent{bodies=");
            stringBuilder.append(this.bodies);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    public static class StreamContent extends HeaderContent {
        private InputStream stream;

        public StreamContent(InputStream stream) {
            this.stream = stream;
        }

        public InputStream getStream() {
            return this.stream;
        }

        public void closeStream() {
            IOUtils.closeQuietly(this.stream);
        }
    }

    public static class StringContent extends HeaderContent {
        private String body;

        public StringContent(String body) {
            this.body = body;
        }

        public String getBody() {
            return this.body;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("StringContent{body='");
            stringBuilder.append(this.body);
            stringBuilder.append(DateFormat.QUOTE);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    protected static String appendUrl(String origin, ContentValues contentValues) {
        if (origin != null) {
            StringBuilder urlBuilder = new StringBuilder(origin);
            if (contentValues != null) {
                String paramPart = format(contentValues, UTF8);
                if (paramPart != null && paramPart.length() > 0) {
                    String str = "?";
                    if (origin.contains(str)) {
                        urlBuilder.append(PARAMETER_SEPARATOR);
                    } else {
                        urlBuilder.append(str);
                    }
                    urlBuilder.append(paramPart);
                }
            }
            return urlBuilder.toString();
        }
        throw new NullPointerException("origin is not allowed null");
    }

    public static String format(ContentValues contentValues, String encoding) {
        StringBuilder result = new StringBuilder();
        for (String key : contentValues.keySet()) {
            String encodedName = encode(key, encoding);
            String value = contentValues.getAsString(key);
            String encodedValue = value != null ? encode(value, encoding) : "";
            if (result.length() > 0) {
                result.append(PARAMETER_SEPARATOR);
            }
            result.append(encodedName);
            result.append(NAME_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }

    private static String encode(String content, String encoding) {
        String str;
        if (encoding != null) {
            str = encoding;
        } else {
            try {
                str = ISO_8859_1;
            } catch (UnsupportedEncodingException problem) {
                throw new IllegalArgumentException(problem);
            }
        }
        return URLEncoder.encode(content, str);
    }

    private static boolean needIgnore12306CA(Map<String, String> params) {
        if (params != null) {
            String bool = Boolean.TRUE.toString();
            String str = PARAM_IGNORE_12306_CA;
            if (bool.equalsIgnoreCase((String) params.get(str))) {
                params.remove(str);
                return true;
            }
        }
        return false;
    }

    public static StringContent getAsString(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody) throws IOException, AccessDeniedException, AuthenticationFailureException {
        boolean ignore12306CA = needIgnore12306CA(params);
        String fullUrl = appendUrl(url, ObjectUtils.mapToPairs(params));
        HttpURLConnection conn = makeConn(fullUrl, cookies, ignore12306CA);
        if (conn != null) {
            Reader br;
            try {
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.connect();
                int code = conn.getResponseCode();
                if (code != 200) {
                    if (code != 302) {
                        StringBuilder stringBuilder;
                        if (code == 403) {
                            throw new AccessDeniedException("access denied, encrypt error or user is forbidden to access the resource");
                        } else if (code == 401 || code == 400) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("authentication failure for get, code: ");
                            stringBuilder.append(code);
                            throw new AuthenticationFailureException(stringBuilder.toString());
                        } else {
                            Logger logger = log;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("http status error when GET: ");
                            stringBuilder.append(code);
                            logger.info(stringBuilder.toString());
                            if (code == 301) {
                                logger = log;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("unexpected redirect from ");
                                stringBuilder.append(conn.getURL().getHost());
                                stringBuilder.append(" to ");
                                stringBuilder.append(conn.getHeaderField(LOCATION));
                                logger.info(stringBuilder.toString());
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("unexpected http res code: ");
                            stringBuilder.append(code);
                            throw new IOException(stringBuilder.toString());
                        }
                    }
                }
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                CookieManager cm = new CookieManager();
                URI reqUri = URI.create(fullUrl);
                cm.put(reqUri, headerFields);
                Map<String, String> cookieMap = parseCookies(cm.getCookieStore().get(reqUri));
                cookieMap.putAll(ObjectUtils.listToMap(headerFields));
                StringBuilder sb = new StringBuilder();
                if (readBody) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024);
                    while (true) {
                        String readLine = br.readLine();
                        String line = readLine;
                        if (readLine == null) {
                            break;
                        }
                        sb.append(line);
                    }
                    IOUtils.closeQuietly(br);
                }
                StringContent stringContent = new StringContent(sb.toString());
                stringContent.putHeaders(cookieMap);
                conn.disconnect();
                return stringContent;
            } catch (ProtocolException e) {
                try {
                    throw new IOException("protocol error");
                } catch (Throwable th) {
                    conn.disconnect();
                }
            } catch (Throwable th2) {
                IOUtils.closeQuietly(br);
            }
        } else {
            log.severe("failed to create URLConnection");
            throw new IOException("failed to create connection");
        }
    }

    public static StreamContent getAsStream(String url, Map<String, String> params, Map<String, String> cookies) throws IOException, AccessDeniedException, AuthenticationFailureException {
        boolean ignore12306CA = needIgnore12306CA(params);
        String fullUrl = appendUrl(url, ObjectUtils.mapToPairs(params));
        HttpURLConnection conn = makeConn(fullUrl, cookies, ignore12306CA);
        if (conn != null) {
            try {
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setInstanceFollowRedirects(true);
                conn.connect();
                int code = conn.getResponseCode();
                StringBuilder stringBuilder;
                if (code == 200) {
                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    CookieManager cm = new CookieManager();
                    URI reqUri = URI.create(fullUrl);
                    cm.put(reqUri, headerFields);
                    Map<String, String> cookieMap = parseCookies(cm.getCookieStore().get(reqUri));
                    cookieMap.putAll(ObjectUtils.listToMap(headerFields));
                    StreamContent streamContent = new StreamContent(conn.getInputStream());
                    streamContent.putHeaders(cookieMap);
                    return streamContent;
                } else if (code == 403) {
                    throw new AccessDeniedException("access denied, encrypt error or user is forbidden to access the resource");
                } else if (code == 401 || code == 400) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("authentication failure for get, code: ");
                    stringBuilder.append(code);
                    throw new AuthenticationFailureException(stringBuilder.toString());
                } else {
                    Logger logger = log;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("http status error when GET: ");
                    stringBuilder.append(code);
                    logger.info(stringBuilder.toString());
                    if (code == 301) {
                        logger = log;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("unexpected redirect from ");
                        stringBuilder.append(conn.getURL().getHost());
                        stringBuilder.append(" to ");
                        stringBuilder.append(conn.getHeaderField(LOCATION));
                        logger.info(stringBuilder.toString());
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("unexpected http res code: ");
                    stringBuilder.append(code);
                    throw new IOException(stringBuilder.toString());
                }
            } catch (ProtocolException e) {
                throw new IOException("protocol error");
            }
        }
        log.severe("failed to create URLConnection");
        throw new IOException("failed to create connection");
    }

    public static MapContent getAsMap(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody) throws IOException, AccessDeniedException, AuthenticationFailureException {
        return convertStringToMap(getAsString(url, params, cookies, readBody));
    }

    public static StringContent postAsString(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody) throws IOException, AccessDeniedException, AuthenticationFailureException {
        String str = UTF8;
        HttpURLConnection conn = makeConn(url, cookies, needIgnore12306CA(params));
        if (conn != null) {
            OutputStream bos;
            Reader br;
            try {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.connect();
                ContentValues contentValues = ObjectUtils.mapToPairs(params);
                if (contentValues != null) {
                    String content = format(contentValues, str);
                    bos = new BufferedOutputStream(conn.getOutputStream());
                    bos.write(content.getBytes(str));
                    IOUtils.closeQuietly(bos);
                }
                int code = conn.getResponseCode();
                if (code != 200) {
                    if (code != 302) {
                        StringBuilder stringBuilder;
                        if (code == 403) {
                            throw new AccessDeniedException("access denied, encrypt error or user is forbidden to access the resource");
                        } else if (code == 401 || code == 400) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("authentication failure for post, code: ");
                            stringBuilder.append(code);
                            throw new AuthenticationFailureException(stringBuilder.toString());
                        } else {
                            Logger logger = log;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("http status error when POST: ");
                            stringBuilder.append(code);
                            logger.info(stringBuilder.toString());
                            if (code == 301) {
                                logger = log;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("unexpected redirect from ");
                                stringBuilder.append(conn.getURL().getHost());
                                stringBuilder.append(" to ");
                                stringBuilder.append(conn.getHeaderField(LOCATION));
                                logger.info(stringBuilder.toString());
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("unexpected http res code: ");
                            stringBuilder.append(code);
                            throw new IOException(stringBuilder.toString());
                        }
                    }
                }
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                CookieManager cm = new CookieManager();
                URI reqUri = URI.create(url);
                cm.put(reqUri, headerFields);
                Map<String, String> cookieMap = parseCookies(cm.getCookieStore().get(reqUri));
                cookieMap.putAll(ObjectUtils.listToMap(headerFields));
                StringBuilder sb = new StringBuilder();
                if (readBody) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024);
                    while (true) {
                        String readLine = br.readLine();
                        String line = readLine;
                        if (readLine == null) {
                            break;
                        }
                        sb.append(line);
                    }
                    IOUtils.closeQuietly(br);
                }
                StringContent stringContent = new StringContent(sb.toString());
                stringContent.putHeaders(cookieMap);
                conn.disconnect();
                return stringContent;
            } catch (ProtocolException e) {
                try {
                    throw new IOException("protocol error");
                } catch (Throwable th) {
                    conn.disconnect();
                }
            } catch (Throwable th2) {
                IOUtils.closeQuietly(bos);
            }
        } else {
            log.severe("failed to create URLConnection");
            throw new IOException("failed to create connection");
        }
    }

    public static MapContent postAsMap(String url, Map<String, String> params, Map<String, String> cookies, boolean readBody) throws IOException, AccessDeniedException, AuthenticationFailureException {
        return convertStringToMap(postAsString(url, params, cookies, readBody));
    }

    protected static MapContent convertStringToMap(StringContent stringContent) {
        if (stringContent == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringContent.getBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return null;
        }
        MapContent mapContent = new MapContent(ObjectUtils.jsonToMap(jsonObject));
        mapContent.putHeaders(stringContent.getHeaders());
        return mapContent;
    }

    protected static HttpURLConnection makeConn(String url, Map<String, String> cookies) {
        return makeConn(url, cookies, false);
    }

    protected static HttpURLConnection makeConn(String url, Map<String, String> cookies, boolean ignore12306CA) {
        URL req = null;
        try {
            req = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (req == null) {
            log.severe("failed to init url");
            return null;
        }
        InputStream caInput;
        try {
            HttpURLConnection conn = (HttpURLConnection) req.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (cookies != null) {
                conn.setRequestProperty("Cookie", joinMap(cookies, "; "));
            }
            if (HOST_12306.equals(req.getHost()) && (conn instanceof HttpsURLConnection)) {
                HttpsURLConnection httpsCon = (HttpsURLConnection) conn;
                SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);
                if (ignore12306CA) {
                    sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }}, null);
                } else {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    caInput = null;
                    caInput = new BufferedInputStream(new ByteArrayInputStream(CER_12306.getBytes()));
                    Certificate ca = cf.generateCertificate(caInput);
                    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                    ks.load(null, null);
                    ks.setCertificateEntry("ca", ca);
                    TrustManagerFactory tf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tf.init(ks);
                    sslContext.init(null, tf.getTrustManagers(), null);
                    IOUtils.closeQuietly(caInput);
                }
                httpsCon.setSSLSocketFactory(sslContext.getSocketFactory());
            }
            return conn;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        } catch (Throwable th) {
            IOUtils.closeQuietly(caInput);
        }
    }

    protected static String joinMap(Map<String, String> map, String sp) {
        if (map == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Entry<String, String> entry : map.entrySet()) {
            if (i > 0) {
                sb.append(sp);
            }
            String value = (String) entry.getValue();
            sb.append((String) entry.getKey());
            sb.append(NAME_VALUE_SEPARATOR);
            sb.append(value);
            i++;
        }
        return sb.toString();
    }

    protected static Map<String, String> parseCookies(List<HttpCookie> cookies) {
        Map<String, String> cookieMap = new HashMap();
        for (HttpCookie cookie : cookies) {
            if (!cookie.hasExpired()) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (name != null) {
                    cookieMap.put(name, value);
                }
            }
        }
        return cookieMap;
    }
}
