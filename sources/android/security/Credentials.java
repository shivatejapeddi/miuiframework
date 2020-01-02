package android.security;

import android.annotation.UnsupportedAppUsage;
import com.android.org.bouncycastle.util.io.pem.PemObject;
import com.android.org.bouncycastle.util.io.pem.PemReader;
import com.android.org.bouncycastle.util.io.pem.PemWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class Credentials {
    public static final String CA_CERTIFICATE = "CACERT_";
    public static final String EXTENSION_CER = ".cer";
    public static final String EXTENSION_CRT = ".crt";
    public static final String EXTENSION_P12 = ".p12";
    public static final String EXTENSION_PFX = ".pfx";
    public static final String EXTRA_CA_CERTIFICATES_DATA = "ca_certificates_data";
    public static final String EXTRA_CA_CERTIFICATES_NAME = "ca_certificates_name";
    public static final String EXTRA_INSTALL_AS_UID = "install_as_uid";
    public static final String EXTRA_PRIVATE_KEY = "PKEY";
    public static final String EXTRA_PUBLIC_KEY = "KEY";
    public static final String EXTRA_USER_CERTIFICATE_DATA = "user_certificate_data";
    public static final String EXTRA_USER_CERTIFICATE_NAME = "user_certificate_name";
    public static final String EXTRA_USER_PRIVATE_KEY_DATA = "user_private_key_data";
    public static final String EXTRA_USER_PRIVATE_KEY_NAME = "user_private_key_name";
    public static final String INSTALL_ACTION = "android.credentials.INSTALL";
    public static final String INSTALL_AS_USER_ACTION = "android.credentials.INSTALL_AS_USER";
    public static final String LOCKDOWN_VPN = "LOCKDOWN_VPN";
    private static final String LOGTAG = "Credentials";
    public static final String USER_CERTIFICATE = "USRCERT_";
    public static final String USER_PRIVATE_KEY = "USRPKEY_";
    public static final String USER_SECRET_KEY = "USRSKEY_";
    public static final String VPN = "VPN_";
    public static final String WIFI = "WIFI_";

    @UnsupportedAppUsage
    public static byte[] convertToPem(Certificate... objects) throws IOException, CertificateEncodingException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        PemWriter pw = new PemWriter(new OutputStreamWriter(bao, StandardCharsets.US_ASCII));
        for (Certificate o : objects) {
            pw.writeObject(new PemObject("CERTIFICATE", o.getEncoded()));
        }
        pw.close();
        return bao.toByteArray();
    }

    public static List<X509Certificate> convertFromPem(byte[] bytes) throws IOException, CertificateException {
        PemReader pr = new PemReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.US_ASCII));
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            List<X509Certificate> result = new ArrayList();
            while (true) {
                PemObject readPemObject = pr.readPemObject();
                PemObject o = readPemObject;
                if (readPemObject == null) {
                    break;
                } else if (o.getType().equals("CERTIFICATE")) {
                    result.add((X509Certificate) cf.generateCertificate(new ByteArrayInputStream(o.getContent())));
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown type ");
                    stringBuilder.append(o.getType());
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            return result;
        } finally {
            pr.close();
        }
    }

    public static boolean deleteAllTypesForAlias(KeyStore keystore, String alias) {
        return deleteAllTypesForAlias(keystore, alias, -1);
    }

    public static boolean deleteAllTypesForAlias(KeyStore keystore, String alias, int uid) {
        return deleteUserKeyTypeForAlias(keystore, alias, uid) & deleteCertificateTypesForAlias(keystore, alias, uid);
    }

    public static boolean deleteCertificateTypesForAlias(KeyStore keystore, String alias) {
        return deleteCertificateTypesForAlias(keystore, alias, -1);
    }

    public static boolean deleteCertificateTypesForAlias(KeyStore keystore, String alias, int uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(USER_CERTIFICATE);
        stringBuilder.append(alias);
        boolean delete = keystore.delete(stringBuilder.toString(), uid);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(CA_CERTIFICATE);
        stringBuilder2.append(alias);
        return delete & keystore.delete(stringBuilder2.toString(), uid);
    }

    public static boolean deleteUserKeyTypeForAlias(KeyStore keystore, String alias) {
        return deleteUserKeyTypeForAlias(keystore, alias, -1);
    }

    public static boolean deleteUserKeyTypeForAlias(KeyStore keystore, String alias, int uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(USER_PRIVATE_KEY);
        stringBuilder.append(alias);
        int ret = keystore.delete2(stringBuilder.toString(), uid);
        if (ret == 7) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(USER_SECRET_KEY);
            stringBuilder2.append(alias);
            return keystore.delete(stringBuilder2.toString(), uid);
        }
        boolean z = true;
        if (ret != 1) {
            z = false;
        }
        return z;
    }

    public static boolean deleteLegacyKeyForAlias(KeyStore keystore, String alias, int uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(USER_SECRET_KEY);
        stringBuilder.append(alias);
        return keystore.delete(stringBuilder.toString(), uid);
    }
}
