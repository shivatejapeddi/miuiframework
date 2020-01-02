package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.security.KeyChain;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ConfigParser {
    private static final String BOUNDARY = "boundary=";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ENCODING_BASE64 = "base64";
    private static final String TAG = "ConfigParser";
    private static final String TYPE_CA_CERT = "application/x-x509-ca-cert";
    private static final String TYPE_MULTIPART_MIXED = "multipart/mixed";
    private static final String TYPE_PASSPOINT_PROFILE = "application/x-passpoint-profile";
    private static final String TYPE_PKCS12 = "application/x-pkcs12";
    private static final String TYPE_WIFI_CONFIG = "application/x-wifi-config";

    private static class MimeHeader {
        public String boundary;
        public String contentType;
        public String encodingType;

        private MimeHeader() {
            this.contentType = null;
            this.boundary = null;
            this.encodingType = null;
        }
    }

    private static class MimePart {
        public byte[] data;
        public boolean isLast;
        public String type;

        private MimePart() {
            this.type = null;
            this.data = null;
            this.isLast = false;
        }
    }

    public static PasspointConfiguration parsePasspointConfig(String mimeType, byte[] data) {
        boolean equals = TextUtils.equals(mimeType, TYPE_WIFI_CONFIG);
        PasspointConfiguration passpointConfiguration = null;
        String str = TAG;
        if (equals) {
            try {
                passpointConfiguration = createPasspointConfig(parseMimeMultipartMessage(new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(Base64.decode(new String(data, StandardCharsets.ISO_8859_1), 0)), StandardCharsets.ISO_8859_1))));
                return passpointConfiguration;
            } catch (IOException | IllegalArgumentException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to parse installation file: ");
                stringBuilder.append(e.getMessage());
                Log.e(str, stringBuilder.toString());
                return passpointConfiguration;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unexpected MIME type: ");
        stringBuilder2.append(mimeType);
        Log.e(str, stringBuilder2.toString());
        return null;
    }

    private static PasspointConfiguration createPasspointConfig(Map<String, byte[]> mimeParts) throws IOException {
        byte[] profileData = (byte[]) mimeParts.get(TYPE_PASSPOINT_PROFILE);
        if (profileData != null) {
            PasspointConfiguration config = PpsMoParser.parseMoText(new String(profileData));
            if (config == null) {
                throw new IOException("Failed to parse Passpoint profile");
            } else if (config.getCredential() != null) {
                byte[] caCertData = (byte[]) mimeParts.get(TYPE_CA_CERT);
                if (caCertData != null) {
                    try {
                        config.getCredential().setCaCertificate(parseCACert(caCertData));
                    } catch (CertificateException e) {
                        throw new IOException("Failed to parse CA Certificate");
                    }
                }
                byte[] pkcs12Data = (byte[]) mimeParts.get(TYPE_PKCS12);
                if (pkcs12Data != null) {
                    try {
                        Pair<PrivateKey, List<X509Certificate>> clientKey = parsePkcs12(pkcs12Data);
                        config.getCredential().setClientPrivateKey((PrivateKey) clientKey.first);
                        config.getCredential().setClientCertificateChain((X509Certificate[]) ((List) clientKey.second).toArray(new X509Certificate[((List) clientKey.second).size()]));
                    } catch (IOException | GeneralSecurityException e2) {
                        throw new IOException("Failed to parse PCKS12 string");
                    }
                }
                return config;
            } else {
                throw new IOException("Passpoint profile missing credential");
            }
        }
        throw new IOException("Missing Passpoint Profile");
    }

    private static Map<String, byte[]> parseMimeMultipartMessage(LineNumberReader in) throws IOException {
        MimeHeader header = parseHeaders(in);
        StringBuilder stringBuilder;
        if (!TextUtils.equals(header.contentType, TYPE_MULTIPART_MIXED)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid content type: ");
            stringBuilder.append(header.contentType);
            throw new IOException(stringBuilder.toString());
        } else if (TextUtils.isEmpty(header.boundary)) {
            throw new IOException("Missing boundary string");
        } else if (TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
            while (true) {
                String line = in.readLine();
                if (line != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("--");
                    stringBuilder.append(header.boundary);
                    if (line.equals(stringBuilder.toString())) {
                        HashMap mimeParts = new HashMap();
                        MimePart mimePart;
                        do {
                            mimePart = parseMimePart(in, header.boundary);
                            mimeParts.put(mimePart.type, mimePart.data);
                        } while (mimePart.isLast == null);
                        return mimeParts;
                    }
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unexpected EOF before first boundary @ ");
                    stringBuilder2.append(in.getLineNumber());
                    throw new IOException(stringBuilder2.toString());
                }
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected encoding: ");
            stringBuilder.append(header.encodingType);
            throw new IOException(stringBuilder.toString());
        }
    }

    private static MimePart parseMimePart(LineNumberReader in, String boundary) throws IOException {
        MimeHeader header = parseHeaders(in);
        StringBuilder stringBuilder;
        if (!TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected encoding type: ");
            stringBuilder.append(header.encodingType);
            throw new IOException(stringBuilder.toString());
        } else if (TextUtils.equals(header.contentType, TYPE_PASSPOINT_PROFILE) || TextUtils.equals(header.contentType, TYPE_CA_CERT) || TextUtils.equals(header.contentType, TYPE_PKCS12)) {
            StringBuilder text = new StringBuilder();
            boolean isLast = false;
            String partBoundary = new StringBuilder();
            String str = "--";
            partBoundary.append(str);
            partBoundary.append(boundary);
            partBoundary = partBoundary.toString();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(partBoundary);
            stringBuilder2.append(str);
            str = stringBuilder2.toString();
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unexpected EOF file in body @ ");
                    stringBuilder3.append(in.getLineNumber());
                    throw new IOException(stringBuilder3.toString());
                } else if (line.startsWith(partBoundary)) {
                    if (line.equals(str)) {
                        isLast = true;
                    }
                    line = new MimePart();
                    line.type = header.contentType;
                    line.data = Base64.decode(text.toString(), 0);
                    line.isLast = isLast;
                    return line;
                } else {
                    text.append(line);
                }
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected content type: ");
            stringBuilder.append(header.contentType);
            throw new IOException(stringBuilder.toString());
        }
    }

    private static MimeHeader parseHeaders(LineNumberReader in) throws IOException {
        MimeHeader header = new MimeHeader();
        for (Entry<String, String> entry : readHeaders(in).entrySet()) {
            String str = (String) entry.getKey();
            Object obj = -1;
            int hashCode = str.hashCode();
            if (hashCode != 747297921) {
                if (hashCode == 949037134 && str.equals(CONTENT_TYPE)) {
                    obj = null;
                }
            } else if (str.equals(CONTENT_TRANSFER_ENCODING)) {
                obj = 1;
            }
            if (obj == null) {
                Pair<String, String> value = parseContentType((String) entry.getValue());
                header.contentType = (String) value.first;
                header.boundary = (String) value.second;
            } else if (obj != 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignore header: ");
                stringBuilder.append((String) entry.getKey());
                Log.d(TAG, stringBuilder.toString());
            } else {
                header.encodingType = (String) entry.getValue();
            }
        }
        return header;
    }

    private static Pair<String, String> parseContentType(String contentType) throws IOException {
        String[] attributes = contentType.split(";");
        String boundary = null;
        if (attributes.length >= 1) {
            String type = attributes[0].trim();
            for (int i = 1; i < attributes.length; i++) {
                String attribute = attributes[i].trim();
                String str = BOUNDARY;
                if (attribute.startsWith(str)) {
                    boundary = attribute.substring(str.length());
                    if (boundary.length() > 1) {
                        str = "\"";
                        if (boundary.startsWith(str) && boundary.endsWith(str)) {
                            boundary = boundary.substring(1, boundary.length() - 1);
                        }
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Ignore Content-Type attribute: ");
                    stringBuilder.append(attributes[i]);
                    Log.d(TAG, stringBuilder.toString());
                }
            }
            return new Pair(type, boundary);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid Content-Type: ");
        stringBuilder2.append(contentType);
        throw new IOException(stringBuilder2.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00ad  */
    private static java.util.Map<java.lang.String, java.lang.String> readHeaders(java.io.LineNumberReader r9) throws java.io.IOException {
        /*
        r0 = new java.util.HashMap;
        r0.<init>();
        r1 = 0;
        r2 = 0;
    L_0x0007:
        r3 = r9.readLine();
        if (r3 == 0) goto L_0x00b5;
    L_0x000d:
        r4 = r3.length();
        if (r4 == 0) goto L_0x00ab;
    L_0x0013:
        r4 = r3.trim();
        r4 = r4.length();
        if (r4 != 0) goto L_0x001f;
    L_0x001d:
        goto L_0x00ab;
    L_0x001f:
        r4 = 58;
        r4 = r3.indexOf(r4);
        r5 = "' @ ";
        if (r4 >= 0) goto L_0x0059;
    L_0x0029:
        if (r2 == 0) goto L_0x0038;
    L_0x002b:
        r5 = 32;
        r2.append(r5);
        r5 = r3.trim();
        r2.append(r5);
        goto L_0x0088;
    L_0x0038:
        r6 = new java.io.IOException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Bad header line: '";
        r7.append(r8);
        r7.append(r3);
        r7.append(r5);
        r5 = r9.getLineNumber();
        r7.append(r5);
        r5 = r7.toString();
        r6.<init>(r5);
        throw r6;
    L_0x0059:
        r6 = 0;
        r7 = r3.charAt(r6);
        r7 = java.lang.Character.isWhitespace(r7);
        if (r7 != 0) goto L_0x008a;
    L_0x0064:
        if (r1 == 0) goto L_0x006d;
    L_0x0066:
        r5 = r2.toString();
        r0.put(r1, r5);
    L_0x006d:
        r5 = r3.substring(r6, r4);
        r1 = r5.trim();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r2 = r5;
        r5 = r4 + 1;
        r5 = r3.substring(r5);
        r5 = r5.trim();
        r2.append(r5);
    L_0x0088:
        goto L_0x0007;
    L_0x008a:
        r6 = new java.io.IOException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Illegal blank prefix in header line '";
        r7.append(r8);
        r7.append(r3);
        r7.append(r5);
        r5 = r9.getLineNumber();
        r7.append(r5);
        r5 = r7.toString();
        r6.<init>(r5);
        throw r6;
    L_0x00ab:
        if (r1 == 0) goto L_0x00b4;
    L_0x00ad:
        r4 = r2.toString();
        r0.put(r1, r4);
    L_0x00b4:
        return r0;
    L_0x00b5:
        r4 = new java.io.IOException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Missing line @ ";
        r5.append(r6);
        r6 = r9.getLineNumber();
        r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.ConfigParser.readHeaders(java.io.LineNumberReader):java.util.Map");
    }

    private static X509Certificate parseCACert(byte[] octets) throws CertificateException {
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(octets));
    }

    private static Pair<PrivateKey, List<X509Certificate>> parsePkcs12(byte[] octets) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(KeyChain.EXTRA_PKCS12);
        ByteArrayInputStream in = new ByteArrayInputStream(octets);
        int i = 0;
        ks.load(in, new char[0]);
        in.close();
        if (ks.size() == 1) {
            String alias = (String) ks.aliases().nextElement();
            if (alias != null) {
                PrivateKey clientKey = (PrivateKey) ks.getKey(alias, null);
                List<X509Certificate> clientCertificateChain = null;
                Certificate[] chain = ks.getCertificateChain(alias);
                if (chain != null) {
                    clientCertificateChain = new ArrayList();
                    int length = chain.length;
                    while (i < length) {
                        Certificate certificate = chain[i];
                        if (certificate instanceof X509Certificate) {
                            clientCertificateChain.add((X509Certificate) certificate);
                            i++;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unexpceted certificate type: ");
                            stringBuilder.append(certificate.getClass());
                            throw new IOException(stringBuilder.toString());
                        }
                    }
                }
                return new Pair(clientKey, clientCertificateChain);
            }
            throw new IOException("No alias found");
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unexpected key size: ");
        stringBuilder2.append(ks.size());
        throw new IOException(stringBuilder2.toString());
    }
}
