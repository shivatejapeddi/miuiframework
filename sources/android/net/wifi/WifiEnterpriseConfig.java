package android.net.wifi;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.hotspot2.pps.Credential.UserCredential;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.security.Credentials;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class WifiEnterpriseConfig implements Parcelable {
    public static final String ALTSUBJECT_MATCH_KEY = "altsubject_match";
    public static final String ANON_IDENTITY_KEY = "anonymous_identity";
    public static final String CA_CERT_ALIAS_DELIMITER = " ";
    public static final String CA_CERT_KEY = "ca_cert";
    public static final String CA_CERT_PREFIX = "keystore://CACERT_";
    public static final String CA_PATH_KEY = "ca_path";
    public static final String CLIENT_CERT_KEY = "client_cert";
    public static final String CLIENT_CERT_PREFIX = "keystore://USRCERT_";
    public static final Creator<WifiEnterpriseConfig> CREATOR = new Creator<WifiEnterpriseConfig>() {
        public WifiEnterpriseConfig createFromParcel(Parcel in) {
            WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                enterpriseConfig.mFields.put(in.readString(), in.readString());
            }
            enterpriseConfig.mEapMethod = in.readInt();
            enterpriseConfig.mPhase2Method = in.readInt();
            enterpriseConfig.mCaCerts = ParcelUtil.readCertificates(in);
            enterpriseConfig.mClientPrivateKey = ParcelUtil.readPrivateKey(in);
            enterpriseConfig.mClientCertificateChain = ParcelUtil.readCertificates(in);
            enterpriseConfig.mIsAppInstalledDeviceKeyAndCert = in.readBoolean();
            enterpriseConfig.mIsAppInstalledCaCert = in.readBoolean();
            return enterpriseConfig;
        }

        public WifiEnterpriseConfig[] newArray(int size) {
            return new WifiEnterpriseConfig[size];
        }
    };
    public static final String DOM_SUFFIX_MATCH_KEY = "domain_suffix_match";
    public static final String EAP_ERP = "eap_erp";
    public static final String EAP_KEY = "eap";
    public static final String EMPTY_VALUE = "NULL";
    public static final String ENGINE_DISABLE = "0";
    public static final String ENGINE_ENABLE = "1";
    public static final String ENGINE_ID_KEY = "engine_id";
    public static final String ENGINE_ID_KEYSTORE = "keystore";
    public static final String ENGINE_KEY = "engine";
    public static final String IDENTITY_KEY = "identity";
    public static final String KEYSTORES_URI = "keystores://";
    public static final String KEYSTORE_URI = "keystore://";
    public static final String KEY_SIMNUM = "sim_num";
    public static final String OPP_KEY_CACHING = "proactive_key_caching";
    public static final String PASSWORD_KEY = "password";
    public static final String PHASE2_KEY = "phase2";
    public static final String PLMN_KEY = "plmn";
    public static final String PRIVATE_KEY_ID_KEY = "key_id";
    public static final String REALM_KEY = "realm";
    public static final String SUBJECT_MATCH_KEY = "subject_match";
    private static final String[] SUPPLICANT_CONFIG_KEYS = new String[]{"identity", ANON_IDENTITY_KEY, "password", CLIENT_CERT_KEY, CA_CERT_KEY, SUBJECT_MATCH_KEY, "engine", ENGINE_ID_KEY, PRIVATE_KEY_ID_KEY, ALTSUBJECT_MATCH_KEY, DOM_SUFFIX_MATCH_KEY, CA_PATH_KEY};
    private static final String TAG = "WifiEnterpriseConfig";
    private static final List<String> UNQUOTED_KEYS = Arrays.asList(new String[]{"engine", OPP_KEY_CACHING, EAP_ERP});
    private X509Certificate[] mCaCerts;
    private X509Certificate[] mClientCertificateChain;
    private PrivateKey mClientPrivateKey;
    private int mEapMethod = -1;
    @UnsupportedAppUsage
    private HashMap<String, String> mFields = new HashMap();
    private boolean mIsAppInstalledCaCert = false;
    private boolean mIsAppInstalledDeviceKeyAndCert = false;
    private int mPhase2Method = 0;

    public static final class Eap {
        public static final int AKA = 5;
        public static final int AKA_PRIME = 6;
        public static final int NONE = -1;
        public static final int PEAP = 0;
        public static final int PWD = 3;
        public static final int SIM = 4;
        public static final int TLS = 1;
        public static final int TTLS = 2;
        public static final int UNAUTH_TLS = 7;
        public static final String[] strings = new String[]{"PEAP", SSLSocketFactory.TLS, "TTLS", "PWD", "SIM", "AKA", "AKA'", "WFA-UNAUTH-TLS"};

        private Eap() {
        }
    }

    public static final class Phase2 {
        public static final int AKA = 6;
        public static final int AKA_PRIME = 7;
        private static final String AUTHEAP_PREFIX = "autheap=";
        private static final String AUTH_PREFIX = "auth=";
        public static final int GTC = 4;
        public static final int MSCHAP = 2;
        public static final int MSCHAPV2 = 3;
        public static final int NONE = 0;
        public static final int PAP = 1;
        public static final int SIM = 5;
        public static final String[] strings = new String[]{WifiEnterpriseConfig.EMPTY_VALUE, UserCredential.AUTH_METHOD_PAP, "MSCHAP", "MSCHAPV2", "GTC", "SIM", "AKA", "AKA'"};

        private Phase2() {
        }
    }

    public interface SupplicantLoader {
        String loadValue(String str);
    }

    public interface SupplicantSaver {
        boolean saveValue(String str, String str2);
    }

    private void copyFrom(WifiEnterpriseConfig source, boolean ignoreMaskedPassword, String mask) {
        for (String key : source.mFields.keySet()) {
            if (!ignoreMaskedPassword || !key.equals("password") || !TextUtils.equals((CharSequence) source.mFields.get(key), mask)) {
                this.mFields.put(key, (String) source.mFields.get(key));
            }
        }
        X509Certificate[] x509CertificateArr = source.mCaCerts;
        if (x509CertificateArr != null) {
            this.mCaCerts = (X509Certificate[]) Arrays.copyOf(x509CertificateArr, x509CertificateArr.length);
        } else {
            this.mCaCerts = null;
        }
        this.mClientPrivateKey = source.mClientPrivateKey;
        x509CertificateArr = source.mClientCertificateChain;
        if (x509CertificateArr != null) {
            this.mClientCertificateChain = (X509Certificate[]) Arrays.copyOf(x509CertificateArr, x509CertificateArr.length);
        } else {
            this.mClientCertificateChain = null;
        }
        this.mEapMethod = source.mEapMethod;
        this.mPhase2Method = source.mPhase2Method;
        this.mIsAppInstalledDeviceKeyAndCert = source.mIsAppInstalledDeviceKeyAndCert;
        this.mIsAppInstalledCaCert = source.mIsAppInstalledCaCert;
    }

    public WifiEnterpriseConfig(WifiEnterpriseConfig source) {
        copyFrom(source, false, "");
    }

    public void copyFromExternal(WifiEnterpriseConfig externalConfig, String mask) {
        copyFrom(externalConfig, true, convertToQuotedString(mask));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mFields.size());
        for (Entry<String, String> entry : this.mFields.entrySet()) {
            dest.writeString((String) entry.getKey());
            dest.writeString((String) entry.getValue());
        }
        dest.writeInt(this.mEapMethod);
        dest.writeInt(this.mPhase2Method);
        ParcelUtil.writeCertificates(dest, this.mCaCerts);
        ParcelUtil.writePrivateKey(dest, this.mClientPrivateKey);
        ParcelUtil.writeCertificates(dest, this.mClientCertificateChain);
        dest.writeBoolean(this.mIsAppInstalledDeviceKeyAndCert);
        dest.writeBoolean(this.mIsAppInstalledCaCert);
    }

    public boolean saveToSupplicant(SupplicantSaver saver) {
        boolean is_autheap = false;
        if (!isEapMethodValid()) {
            return false;
        }
        String key;
        int i = this.mEapMethod;
        boolean shouldNotWriteAnonIdentity = i == 4 || i == 5 || i == 6;
        for (String key2 : this.mFields.keySet()) {
            if (!shouldNotWriteAnonIdentity || !ANON_IDENTITY_KEY.equals(key2)) {
                if (!saver.saveValue(key2, (String) this.mFields.get(key2))) {
                    return false;
                }
            }
        }
        if (!saver.saveValue("eap", Eap.strings[this.mEapMethod])) {
            return false;
        }
        int i2 = this.mEapMethod;
        key2 = "phase2";
        if (i2 != 1) {
            int i3 = this.mPhase2Method;
            if (i3 != 0) {
                if (i2 == 2 && i3 == 4) {
                    is_autheap = true;
                }
                String prefix = is_autheap ? "autheap=" : "auth=";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append(Phase2.strings[this.mPhase2Method]);
                return saver.saveValue(key2, convertToQuotedString(stringBuilder.toString()));
            }
        }
        if (this.mPhase2Method == 0) {
            return saver.saveValue(key2, null);
        }
        Log.e(TAG, "WiFi enterprise configuration is invalid as it supplies a phase 2 method but the phase1 method does not support it.");
        return false;
    }

    public void loadFromSupplicant(SupplicantLoader loader) {
        for (String key : SUPPLICANT_CONFIG_KEYS) {
            String value = loader.loadValue(key);
            if (value == null) {
                this.mFields.put(key, EMPTY_VALUE);
            } else {
                this.mFields.put(key, value);
            }
        }
        this.mEapMethod = getStringIndex(Eap.strings, loader.loadValue("eap"), -1);
        String phase2Method = removeDoubleQuotes(loader.loadValue("phase2"));
        String str = "auth=";
        if (phase2Method.startsWith(str)) {
            phase2Method = phase2Method.substring(str.length());
        } else {
            str = "autheap=";
            if (phase2Method.startsWith(str)) {
                phase2Method = phase2Method.substring(str.length());
            }
        }
        this.mPhase2Method = getStringIndex(Phase2.strings, phase2Method, 0);
    }

    public void setEapMethod(int eapMethod) {
        switch (eapMethod) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            case 1:
            case 7:
                setPhase2Method(0);
                break;
            default:
                throw new IllegalArgumentException("Unknown EAP method");
        }
        this.mEapMethod = eapMethod;
        setFieldValue(OPP_KEY_CACHING, "1");
    }

    public void setSimNum(int SIMNum) {
        setFieldValue("sim_num", Integer.toString(SIMNum));
    }

    public String getSimNum() {
        return getFieldValue("sim_num");
    }

    public int getEapMethod() {
        return this.mEapMethod;
    }

    public void setPhase2Method(int phase2Method) {
        switch (phase2Method) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                this.mPhase2Method = phase2Method;
                return;
            default:
                throw new IllegalArgumentException("Unknown Phase 2 method");
        }
    }

    public int getPhase2Method() {
        return this.mPhase2Method;
    }

    public void setIdentity(String identity) {
        setFieldValue("identity", identity, "");
    }

    public String getIdentity() {
        return getFieldValue("identity");
    }

    public void setAnonymousIdentity(String anonymousIdentity) {
        setFieldValue(ANON_IDENTITY_KEY, anonymousIdentity);
    }

    public String getAnonymousIdentity() {
        return getFieldValue(ANON_IDENTITY_KEY);
    }

    public void setPassword(String password) {
        setFieldValue("password", password);
    }

    public String getPassword() {
        return getFieldValue("password");
    }

    public static String encodeCaCertificateAlias(String alias) {
        byte[] bytes = alias.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", new Object[]{Integer.valueOf(bytes[i] & 255)}));
        }
        return sb.toString();
    }

    public static String decodeCaCertificateAlias(String alias) {
        byte[] data = new byte[(alias.length() >> 1)];
        int n = 0;
        int position = 0;
        while (n < alias.length()) {
            data[position] = (byte) Integer.parseInt(alias.substring(n, n + 2), 16);
            n += 2;
            position++;
        }
        try {
            return new String(data, StandardCharsets.UTF_8);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return alias;
        }
    }

    @UnsupportedAppUsage
    public void setCaCertificateAlias(String alias) {
        setFieldValue(CA_CERT_KEY, alias, CA_CERT_PREFIX);
    }

    public void setCaCertificateAliases(String[] aliases) {
        String str = CA_CERT_KEY;
        if (aliases == null) {
            setFieldValue(str, null, CA_CERT_PREFIX);
        } else if (aliases.length == 1) {
            setCaCertificateAlias(aliases[0]);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < aliases.length; i++) {
                if (i > 0) {
                    sb.append(CA_CERT_ALIAS_DELIMITER);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Credentials.CA_CERTIFICATE);
                stringBuilder.append(aliases[i]);
                sb.append(encodeCaCertificateAlias(stringBuilder.toString()));
            }
            setFieldValue(str, sb.toString(), KEYSTORES_URI);
        }
    }

    @UnsupportedAppUsage
    public String getCaCertificateAlias() {
        return getFieldValue(CA_CERT_KEY, CA_CERT_PREFIX);
    }

    public String[] getCaCertificateAliases() {
        String value = getFieldValue(CA_CERT_KEY);
        String str;
        if (value.startsWith(CA_CERT_PREFIX)) {
            return new String[]{getFieldValue(str, CA_CERT_PREFIX)};
        }
        str = KEYSTORES_URI;
        String[] strArr = null;
        if (value.startsWith(str)) {
            String[] aliases = TextUtils.split(value.substring(str.length()), CA_CERT_ALIAS_DELIMITER);
            for (int i = 0; i < aliases.length; i++) {
                aliases[i] = decodeCaCertificateAlias(aliases[i]);
                String str2 = aliases[i];
                String str3 = Credentials.CA_CERTIFICATE;
                if (str2.startsWith(str3)) {
                    aliases[i] = aliases[i].substring(str3.length());
                }
            }
            if (aliases.length != 0) {
                strArr = aliases;
            }
            return strArr;
        }
        if (!TextUtils.isEmpty(value)) {
            strArr = new String[]{value};
        }
        return strArr;
    }

    public void setCaCertificate(X509Certificate cert) {
        if (cert == null) {
            this.mCaCerts = null;
        } else if (cert.getBasicConstraints() >= 0) {
            this.mIsAppInstalledCaCert = true;
            this.mCaCerts = new X509Certificate[]{cert};
        } else {
            this.mCaCerts = null;
            throw new IllegalArgumentException("Not a CA certificate");
        }
    }

    public X509Certificate getCaCertificate() {
        X509Certificate[] x509CertificateArr = this.mCaCerts;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr[0];
    }

    public void setCaCertificates(X509Certificate[] certs) {
        if (certs != null) {
            X509Certificate[] newCerts = new X509Certificate[certs.length];
            int i = 0;
            while (i < certs.length) {
                if (certs[i].getBasicConstraints() >= 0) {
                    newCerts[i] = certs[i];
                    i++;
                } else {
                    this.mCaCerts = null;
                    throw new IllegalArgumentException("Not a CA certificate");
                }
            }
            this.mCaCerts = newCerts;
            this.mIsAppInstalledCaCert = true;
            return;
        }
        this.mCaCerts = null;
    }

    public X509Certificate[] getCaCertificates() {
        X509Certificate[] x509CertificateArr = this.mCaCerts;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr;
    }

    public void resetCaCertificate() {
        this.mCaCerts = null;
    }

    public void setCaPath(String path) {
        setFieldValue(CA_PATH_KEY, path);
    }

    public String getCaPath() {
        return getFieldValue(CA_PATH_KEY);
    }

    @UnsupportedAppUsage
    public void setClientCertificateAlias(String alias) {
        setFieldValue(CLIENT_CERT_KEY, alias, CLIENT_CERT_PREFIX);
        setFieldValue(PRIVATE_KEY_ID_KEY, alias, Credentials.USER_PRIVATE_KEY);
        boolean isEmpty = TextUtils.isEmpty(alias);
        String str = ENGINE_ID_KEY;
        String str2 = "engine";
        if (isEmpty) {
            setFieldValue(str2, "0");
            setFieldValue(str, "");
            return;
        }
        setFieldValue(str2, "1");
        setFieldValue(str, ENGINE_ID_KEYSTORE);
    }

    @UnsupportedAppUsage
    public String getClientCertificateAlias() {
        return getFieldValue(CLIENT_CERT_KEY, CLIENT_CERT_PREFIX);
    }

    public void setClientKeyEntry(PrivateKey privateKey, X509Certificate clientCertificate) {
        X509Certificate[] clientCertificates = null;
        if (clientCertificate != null) {
            clientCertificates = new X509Certificate[]{clientCertificate};
        }
        setClientKeyEntryWithCertificateChain(privateKey, clientCertificates);
    }

    public void setClientKeyEntryWithCertificateChain(PrivateKey privateKey, X509Certificate[] clientCertificateChain) {
        X509Certificate[] newCerts = null;
        if (clientCertificateChain != null && clientCertificateChain.length > 0) {
            if (clientCertificateChain[0].getBasicConstraints() == -1) {
                int i = 1;
                while (i < clientCertificateChain.length) {
                    if (clientCertificateChain[i].getBasicConstraints() != -1) {
                        i++;
                    } else {
                        throw new IllegalArgumentException("All certificates following the first must be CA certificates");
                    }
                }
                newCerts = (X509Certificate[]) Arrays.copyOf(clientCertificateChain, clientCertificateChain.length);
                if (privateKey == null) {
                    throw new IllegalArgumentException("Client cert without a private key");
                } else if (privateKey.getEncoded() == null) {
                    throw new IllegalArgumentException("Private key cannot be encoded");
                }
            }
            throw new IllegalArgumentException("First certificate in the chain must be a client end certificate");
        }
        this.mClientPrivateKey = privateKey;
        this.mClientCertificateChain = newCerts;
        this.mIsAppInstalledDeviceKeyAndCert = true;
    }

    public X509Certificate getClientCertificate() {
        X509Certificate[] x509CertificateArr = this.mClientCertificateChain;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr[0];
    }

    public X509Certificate[] getClientCertificateChain() {
        X509Certificate[] x509CertificateArr = this.mClientCertificateChain;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr;
    }

    public void resetClientKeyEntry() {
        this.mClientPrivateKey = null;
        this.mClientCertificateChain = null;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public void setSubjectMatch(String subjectMatch) {
        setFieldValue(SUBJECT_MATCH_KEY, subjectMatch);
    }

    public String getSubjectMatch() {
        return getFieldValue(SUBJECT_MATCH_KEY);
    }

    public void setAltSubjectMatch(String altSubjectMatch) {
        setFieldValue(ALTSUBJECT_MATCH_KEY, altSubjectMatch);
    }

    public String getAltSubjectMatch() {
        return getFieldValue(ALTSUBJECT_MATCH_KEY);
    }

    public void setDomainSuffixMatch(String domain) {
        setFieldValue(DOM_SUFFIX_MATCH_KEY, domain);
    }

    public String getDomainSuffixMatch() {
        return getFieldValue(DOM_SUFFIX_MATCH_KEY);
    }

    public void setRealm(String realm) {
        setFieldValue(REALM_KEY, realm);
    }

    public String getRealm() {
        return getFieldValue(REALM_KEY);
    }

    public void setPlmn(String plmn) {
        setFieldValue("plmn", plmn);
    }

    public String getPlmn() {
        return getFieldValue("plmn");
    }

    public String getKeyId(WifiEnterpriseConfig current) {
        int i = this.mEapMethod;
        String str = EMPTY_VALUE;
        if (i == -1) {
            if (current != null) {
                str = current.getKeyId(null);
            }
            return str;
        } else if (!isEapMethodValid()) {
            return str;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Eap.strings[this.mEapMethod]);
            stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            stringBuilder.append(Phase2.strings[this.mPhase2Method]);
            return stringBuilder.toString();
        }
    }

    private String removeDoubleQuotes(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    private String convertToQuotedString(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "\"";
        stringBuilder.append(str);
        stringBuilder.append(string);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private int getStringIndex(String[] arr, String toBeFound, int defaultIndex) {
        if (TextUtils.isEmpty(toBeFound)) {
            return defaultIndex;
        }
        for (int i = 0; i < arr.length; i++) {
            if (toBeFound.equals(arr[i])) {
                return i;
            }
        }
        return defaultIndex;
    }

    private String getFieldValue(String key, String prefix) {
        String value = (String) this.mFields.get(key);
        if (TextUtils.isEmpty(value) || EMPTY_VALUE.equals(value)) {
            return "";
        }
        value = removeDoubleQuotes(value);
        if (value.startsWith(prefix)) {
            return value.substring(prefix.length());
        }
        return value;
    }

    public String getFieldValue(String key) {
        return getFieldValue(key, "");
    }

    private void setFieldValue(String key, String value, String prefix) {
        if (TextUtils.isEmpty(value)) {
            this.mFields.put(key, EMPTY_VALUE);
            return;
        }
        String valueToSet;
        StringBuilder stringBuilder;
        if (UNQUOTED_KEYS.contains(key)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(value);
            valueToSet = stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(value);
            valueToSet = convertToQuotedString(stringBuilder.toString());
        }
        this.mFields.put(key, valueToSet);
    }

    public void setFieldValue(String key, String value) {
        setFieldValue(key, value, "");
    }

    public String toString() {
        String str;
        StringBuffer sb = new StringBuffer();
        Iterator it = this.mFields.keySet().iterator();
        while (true) {
            str = "\n";
            if (!it.hasNext()) {
                break;
            }
            String key = (String) it.next();
            String value = "password".equals(key) ? "<removed>" : (String) this.mFields.get(key);
            sb.append(key);
            sb.append(CA_CERT_ALIAS_DELIMITER);
            sb.append(value);
            sb.append(str);
        }
        int i = this.mEapMethod;
        if (i >= 0 && i < Eap.strings.length) {
            sb.append("eap_method: ");
            sb.append(Eap.strings[this.mEapMethod]);
            sb.append(str);
        }
        i = this.mPhase2Method;
        if (i > 0 && i < Phase2.strings.length) {
            sb.append("phase2_method: ");
            sb.append(Phase2.strings[this.mPhase2Method]);
            sb.append(str);
        }
        return sb.toString();
    }

    private boolean isEapMethodValid() {
        int i = this.mEapMethod;
        String str = TAG;
        StringBuilder stringBuilder;
        if (i == -1) {
            Log.e(str, "WiFi enterprise configuration is invalid as it supplies no EAP method.");
            return false;
        } else if (i < 0 || i >= Eap.strings.length) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mEapMethod is invald for WiFi enterprise configuration: ");
            stringBuilder.append(this.mEapMethod);
            Log.e(str, stringBuilder.toString());
            return false;
        } else {
            i = this.mPhase2Method;
            if (i >= 0 && i < Phase2.strings.length) {
                return true;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("mPhase2Method is invald for WiFi enterprise configuration: ");
            stringBuilder.append(this.mPhase2Method);
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    public boolean isAppInstalledDeviceKeyAndCert() {
        return this.mIsAppInstalledDeviceKeyAndCert;
    }

    public boolean isAppInstalledCaCert() {
        return this.mIsAppInstalledCaCert;
    }
}
