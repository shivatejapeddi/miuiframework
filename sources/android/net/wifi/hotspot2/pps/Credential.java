package android.net.wifi.hotspot2.pps;

import android.net.wifi.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Credential implements Parcelable {
    public static final Creator<Credential> CREATOR = new Creator<Credential>() {
        public Credential createFromParcel(Parcel in) {
            Credential credential = new Credential();
            credential.setCreationTimeInMillis(in.readLong());
            credential.setExpirationTimeInMillis(in.readLong());
            credential.setRealm(in.readString());
            credential.setCheckAaaServerCertStatus(in.readInt() != 0);
            credential.setUserCredential((UserCredential) in.readParcelable(null));
            credential.setCertCredential((CertificateCredential) in.readParcelable(null));
            credential.setSimCredential((SimCredential) in.readParcelable(null));
            credential.setCaCertificates(ParcelUtil.readCertificates(in));
            credential.setClientCertificateChain(ParcelUtil.readCertificates(in));
            credential.setClientPrivateKey(ParcelUtil.readPrivateKey(in));
            return credential;
        }

        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };
    private static final int MAX_REALM_BYTES = 253;
    private static final String TAG = "Credential";
    private X509Certificate[] mCaCertificates = null;
    private CertificateCredential mCertCredential = null;
    private boolean mCheckAaaServerCertStatus = false;
    private X509Certificate[] mClientCertificateChain = null;
    private PrivateKey mClientPrivateKey = null;
    private long mCreationTimeInMillis = Long.MIN_VALUE;
    private long mExpirationTimeInMillis = Long.MIN_VALUE;
    private String mRealm = null;
    private SimCredential mSimCredential = null;
    private UserCredential mUserCredential = null;

    public static final class CertificateCredential implements Parcelable {
        private static final int CERT_SHA256_FINGER_PRINT_LENGTH = 32;
        public static final String CERT_TYPE_X509V3 = "x509v3";
        public static final Creator<CertificateCredential> CREATOR = new Creator<CertificateCredential>() {
            public CertificateCredential createFromParcel(Parcel in) {
                CertificateCredential certCredential = new CertificateCredential();
                certCredential.setCertType(in.readString());
                certCredential.setCertSha256Fingerprint(in.createByteArray());
                return certCredential;
            }

            public CertificateCredential[] newArray(int size) {
                return new CertificateCredential[size];
            }
        };
        private byte[] mCertSha256Fingerprint = null;
        private String mCertType = null;

        public void setCertType(String certType) {
            this.mCertType = certType;
        }

        public String getCertType() {
            return this.mCertType;
        }

        public void setCertSha256Fingerprint(byte[] certSha256Fingerprint) {
            this.mCertSha256Fingerprint = certSha256Fingerprint;
        }

        public byte[] getCertSha256Fingerprint() {
            return this.mCertSha256Fingerprint;
        }

        public CertificateCredential(CertificateCredential source) {
            if (source != null) {
                this.mCertType = source.mCertType;
                byte[] bArr = source.mCertSha256Fingerprint;
                if (bArr != null) {
                    this.mCertSha256Fingerprint = Arrays.copyOf(bArr, bArr.length);
                }
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mCertType);
            dest.writeByteArray(this.mCertSha256Fingerprint);
        }

        public boolean equals(Object thatObject) {
            boolean z = true;
            if (this == thatObject) {
                return true;
            }
            if (!(thatObject instanceof CertificateCredential)) {
                return false;
            }
            CertificateCredential that = (CertificateCredential) thatObject;
            if (!(TextUtils.equals(this.mCertType, that.mCertType) && Arrays.equals(this.mCertSha256Fingerprint, that.mCertSha256Fingerprint))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mCertType, Integer.valueOf(Arrays.hashCode(this.mCertSha256Fingerprint))});
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CertificateType: ");
            stringBuilder.append(this.mCertType);
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }

        public boolean validate() {
            boolean equals = TextUtils.equals(CERT_TYPE_X509V3, this.mCertType);
            String str = Credential.TAG;
            if (equals) {
                byte[] bArr = this.mCertSha256Fingerprint;
                if (bArr != null && bArr.length == 32) {
                    return true;
                }
                Log.d(str, "Invalid SHA-256 fingerprint");
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported certificate type: ");
            stringBuilder.append(this.mCertType);
            Log.d(str, stringBuilder.toString());
            return false;
        }
    }

    public static final class SimCredential implements Parcelable {
        public static final Creator<SimCredential> CREATOR = new Creator<SimCredential>() {
            public SimCredential createFromParcel(Parcel in) {
                SimCredential simCredential = new SimCredential();
                simCredential.setImsi(in.readString());
                simCredential.setEapType(in.readInt());
                return simCredential;
            }

            public SimCredential[] newArray(int size) {
                return new SimCredential[size];
            }
        };
        private static final int MAX_IMSI_LENGTH = 15;
        private int mEapType = Integer.MIN_VALUE;
        private String mImsi = null;

        public void setImsi(String imsi) {
            this.mImsi = imsi;
        }

        public String getImsi() {
            return this.mImsi;
        }

        public void setEapType(int eapType) {
            this.mEapType = eapType;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public SimCredential(SimCredential source) {
            if (source != null) {
                this.mImsi = source.mImsi;
                this.mEapType = source.mEapType;
            }
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object thatObject) {
            boolean z = true;
            if (this == thatObject) {
                return true;
            }
            if (!(thatObject instanceof SimCredential)) {
                return false;
            }
            SimCredential that = (SimCredential) thatObject;
            if (!(TextUtils.equals(this.mImsi, that.mImsi) && this.mEapType == that.mEapType)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mImsi, Integer.valueOf(this.mEapType)});
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("IMSI: ");
            builder.append(this.mImsi);
            String str = "\n";
            builder.append(str);
            builder.append("EAPType: ");
            builder.append(this.mEapType);
            builder.append(str);
            return builder.toString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mImsi);
            dest.writeInt(this.mEapType);
        }

        public boolean validate() {
            if (!verifyImsi()) {
                return false;
            }
            int i = this.mEapType;
            if (i == 18 || i == 23 || i == 50) {
                return true;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid EAP Type for SIM credential: ");
            stringBuilder.append(this.mEapType);
            Log.d(Credential.TAG, stringBuilder.toString());
            return false;
        }

        private boolean verifyImsi() {
            boolean isEmpty = TextUtils.isEmpty(this.mImsi);
            String str = Credential.TAG;
            if (isEmpty) {
                Log.d(str, "Missing IMSI");
                return false;
            } else if (this.mImsi.length() > 15) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("IMSI exceeding maximum length: ");
                stringBuilder.append(this.mImsi.length());
                Log.d(str, stringBuilder.toString());
                return false;
            } else {
                char stopChar = 0;
                int nonDigit = 0;
                while (nonDigit < this.mImsi.length()) {
                    stopChar = this.mImsi.charAt(nonDigit);
                    if (stopChar < '0' || stopChar > '9') {
                        break;
                    }
                    nonDigit++;
                }
                if (nonDigit == this.mImsi.length()) {
                    return true;
                }
                if (nonDigit == this.mImsi.length() - 1 && stopChar == '*') {
                    return true;
                }
                return false;
            }
        }
    }

    public static final class UserCredential implements Parcelable {
        public static final String AUTH_METHOD_MSCHAP = "MS-CHAP";
        public static final String AUTH_METHOD_MSCHAPV2 = "MS-CHAP-V2";
        public static final String AUTH_METHOD_PAP = "PAP";
        public static final Creator<UserCredential> CREATOR = new Creator<UserCredential>() {
            public UserCredential createFromParcel(Parcel in) {
                UserCredential userCredential = new UserCredential();
                userCredential.setUsername(in.readString());
                userCredential.setPassword(in.readString());
                boolean z = true;
                userCredential.setMachineManaged(in.readInt() != 0);
                userCredential.setSoftTokenApp(in.readString());
                if (in.readInt() == 0) {
                    z = false;
                }
                userCredential.setAbleToShare(z);
                userCredential.setEapType(in.readInt());
                userCredential.setNonEapInnerMethod(in.readString());
                return userCredential;
            }

            public UserCredential[] newArray(int size) {
                return new UserCredential[size];
            }
        };
        private static final int MAX_PASSWORD_BYTES = 255;
        private static final int MAX_USERNAME_BYTES = 63;
        private static final Set<String> SUPPORTED_AUTH = new HashSet(Arrays.asList(new String[]{AUTH_METHOD_PAP, AUTH_METHOD_MSCHAP, AUTH_METHOD_MSCHAPV2}));
        private boolean mAbleToShare = false;
        private int mEapType = Integer.MIN_VALUE;
        private boolean mMachineManaged = false;
        private String mNonEapInnerMethod = null;
        private String mPassword = null;
        private String mSoftTokenApp = null;
        private String mUsername = null;

        public void setUsername(String username) {
            this.mUsername = username;
        }

        public String getUsername() {
            return this.mUsername;
        }

        public void setPassword(String password) {
            this.mPassword = password;
        }

        public String getPassword() {
            return this.mPassword;
        }

        public void setMachineManaged(boolean machineManaged) {
            this.mMachineManaged = machineManaged;
        }

        public boolean getMachineManaged() {
            return this.mMachineManaged;
        }

        public void setSoftTokenApp(String softTokenApp) {
            this.mSoftTokenApp = softTokenApp;
        }

        public String getSoftTokenApp() {
            return this.mSoftTokenApp;
        }

        public void setAbleToShare(boolean ableToShare) {
            this.mAbleToShare = ableToShare;
        }

        public boolean getAbleToShare() {
            return this.mAbleToShare;
        }

        public void setEapType(int eapType) {
            this.mEapType = eapType;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public void setNonEapInnerMethod(String nonEapInnerMethod) {
            this.mNonEapInnerMethod = nonEapInnerMethod;
        }

        public String getNonEapInnerMethod() {
            return this.mNonEapInnerMethod;
        }

        public UserCredential(UserCredential source) {
            if (source != null) {
                this.mUsername = source.mUsername;
                this.mPassword = source.mPassword;
                this.mMachineManaged = source.mMachineManaged;
                this.mSoftTokenApp = source.mSoftTokenApp;
                this.mAbleToShare = source.mAbleToShare;
                this.mEapType = source.mEapType;
                this.mNonEapInnerMethod = source.mNonEapInnerMethod;
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mUsername);
            dest.writeString(this.mPassword);
            dest.writeInt(this.mMachineManaged);
            dest.writeString(this.mSoftTokenApp);
            dest.writeInt(this.mAbleToShare);
            dest.writeInt(this.mEapType);
            dest.writeString(this.mNonEapInnerMethod);
        }

        public boolean equals(Object thatObject) {
            boolean z = true;
            if (this == thatObject) {
                return true;
            }
            if (!(thatObject instanceof UserCredential)) {
                return false;
            }
            UserCredential that = (UserCredential) thatObject;
            if (!(TextUtils.equals(this.mUsername, that.mUsername) && TextUtils.equals(this.mPassword, that.mPassword) && this.mMachineManaged == that.mMachineManaged && TextUtils.equals(this.mSoftTokenApp, that.mSoftTokenApp) && this.mAbleToShare == that.mAbleToShare && this.mEapType == that.mEapType && TextUtils.equals(this.mNonEapInnerMethod, that.mNonEapInnerMethod))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mUsername, this.mPassword, Boolean.valueOf(this.mMachineManaged), this.mSoftTokenApp, Boolean.valueOf(this.mAbleToShare), Integer.valueOf(this.mEapType), this.mNonEapInnerMethod});
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Username: ");
            builder.append(this.mUsername);
            String str = "\n";
            builder.append(str);
            builder.append("MachineManaged: ");
            builder.append(this.mMachineManaged);
            builder.append(str);
            builder.append("SoftTokenApp: ");
            builder.append(this.mSoftTokenApp);
            builder.append(str);
            builder.append("AbleToShare: ");
            builder.append(this.mAbleToShare);
            builder.append(str);
            builder.append("EAPType: ");
            builder.append(this.mEapType);
            builder.append(str);
            builder.append("AuthMethod: ");
            builder.append(this.mNonEapInnerMethod);
            builder.append(str);
            return builder.toString();
        }

        public boolean validate() {
            boolean isEmpty = TextUtils.isEmpty(this.mUsername);
            String str = Credential.TAG;
            StringBuilder stringBuilder;
            if (isEmpty) {
                Log.d(str, "Missing username");
                return false;
            } else if (this.mUsername.getBytes(StandardCharsets.UTF_8).length > 63) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("username exceeding maximum length: ");
                stringBuilder.append(this.mUsername.getBytes(StandardCharsets.UTF_8).length);
                Log.d(str, stringBuilder.toString());
                return false;
            } else if (TextUtils.isEmpty(this.mPassword)) {
                Log.d(str, "Missing password");
                return false;
            } else if (this.mPassword.getBytes(StandardCharsets.UTF_8).length > 255) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("password exceeding maximum length: ");
                stringBuilder.append(this.mPassword.getBytes(StandardCharsets.UTF_8).length);
                Log.d(str, stringBuilder.toString());
                return false;
            } else if (this.mEapType != 21) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid EAP Type for user credential: ");
                stringBuilder.append(this.mEapType);
                Log.d(str, stringBuilder.toString());
                return false;
            } else if (SUPPORTED_AUTH.contains(this.mNonEapInnerMethod)) {
                return true;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid non-EAP inner method for EAP-TTLS: ");
                stringBuilder.append(this.mNonEapInnerMethod);
                Log.d(str, stringBuilder.toString());
                return false;
            }
        }
    }

    public void setCreationTimeInMillis(long creationTimeInMillis) {
        this.mCreationTimeInMillis = creationTimeInMillis;
    }

    public long getCreationTimeInMillis() {
        return this.mCreationTimeInMillis;
    }

    public void setExpirationTimeInMillis(long expirationTimeInMillis) {
        this.mExpirationTimeInMillis = expirationTimeInMillis;
    }

    public long getExpirationTimeInMillis() {
        return this.mExpirationTimeInMillis;
    }

    public void setRealm(String realm) {
        this.mRealm = realm;
    }

    public String getRealm() {
        return this.mRealm;
    }

    public void setCheckAaaServerCertStatus(boolean checkAaaServerCertStatus) {
        this.mCheckAaaServerCertStatus = checkAaaServerCertStatus;
    }

    public boolean getCheckAaaServerCertStatus() {
        return this.mCheckAaaServerCertStatus;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.mUserCredential = userCredential;
    }

    public UserCredential getUserCredential() {
        return this.mUserCredential;
    }

    public void setCertCredential(CertificateCredential certCredential) {
        this.mCertCredential = certCredential;
    }

    public CertificateCredential getCertCredential() {
        return this.mCertCredential;
    }

    public void setSimCredential(SimCredential simCredential) {
        this.mSimCredential = simCredential;
    }

    public SimCredential getSimCredential() {
        return this.mSimCredential;
    }

    public void setCaCertificate(X509Certificate caCertificate) {
        this.mCaCertificates = null;
        if (caCertificate != null) {
            this.mCaCertificates = new X509Certificate[]{caCertificate};
        }
    }

    public void setCaCertificates(X509Certificate[] caCertificates) {
        this.mCaCertificates = caCertificates;
    }

    public X509Certificate getCaCertificate() {
        X509Certificate[] x509CertificateArr = this.mCaCertificates;
        return (x509CertificateArr == null || x509CertificateArr.length > 1) ? null : x509CertificateArr[0];
    }

    public X509Certificate[] getCaCertificates() {
        return this.mCaCertificates;
    }

    public void setClientCertificateChain(X509Certificate[] certificateChain) {
        this.mClientCertificateChain = certificateChain;
    }

    public X509Certificate[] getClientCertificateChain() {
        return this.mClientCertificateChain;
    }

    public void setClientPrivateKey(PrivateKey clientPrivateKey) {
        this.mClientPrivateKey = clientPrivateKey;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public Credential(Credential source) {
        if (source != null) {
            this.mCreationTimeInMillis = source.mCreationTimeInMillis;
            this.mExpirationTimeInMillis = source.mExpirationTimeInMillis;
            this.mRealm = source.mRealm;
            this.mCheckAaaServerCertStatus = source.mCheckAaaServerCertStatus;
            UserCredential userCredential = source.mUserCredential;
            if (userCredential != null) {
                this.mUserCredential = new UserCredential(userCredential);
            }
            CertificateCredential certificateCredential = source.mCertCredential;
            if (certificateCredential != null) {
                this.mCertCredential = new CertificateCredential(certificateCredential);
            }
            SimCredential simCredential = source.mSimCredential;
            if (simCredential != null) {
                this.mSimCredential = new SimCredential(simCredential);
            }
            X509Certificate[] x509CertificateArr = source.mClientCertificateChain;
            if (x509CertificateArr != null) {
                this.mClientCertificateChain = (X509Certificate[]) Arrays.copyOf(x509CertificateArr, x509CertificateArr.length);
            }
            x509CertificateArr = source.mCaCertificates;
            if (x509CertificateArr != null) {
                this.mCaCertificates = (X509Certificate[]) Arrays.copyOf(x509CertificateArr, x509CertificateArr.length);
            }
            this.mClientPrivateKey = source.mClientPrivateKey;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mCreationTimeInMillis);
        dest.writeLong(this.mExpirationTimeInMillis);
        dest.writeString(this.mRealm);
        dest.writeInt(this.mCheckAaaServerCertStatus);
        dest.writeParcelable(this.mUserCredential, flags);
        dest.writeParcelable(this.mCertCredential, flags);
        dest.writeParcelable(this.mSimCredential, flags);
        ParcelUtil.writeCertificates(dest, this.mCaCertificates);
        ParcelUtil.writeCertificates(dest, this.mClientCertificateChain);
        ParcelUtil.writePrivateKey(dest, this.mClientPrivateKey);
    }

    /* JADX WARNING: Missing block: B:37:0x007c, code skipped:
            if (isPrivateKeyEquals(r7.mClientPrivateKey, r1.mClientPrivateKey) != false) goto L_0x0080;
     */
    public boolean equals(java.lang.Object r8) {
        /*
        r7 = this;
        r0 = 1;
        if (r7 != r8) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r8 instanceof android.net.wifi.hotspot2.pps.Credential;
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r1 = r8;
        r1 = (android.net.wifi.hotspot2.pps.Credential) r1;
        r3 = r7.mRealm;
        r4 = r1.mRealm;
        r3 = android.text.TextUtils.equals(r3, r4);
        if (r3 == 0) goto L_0x007f;
    L_0x0017:
        r3 = r7.mCreationTimeInMillis;
        r5 = r1.mCreationTimeInMillis;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x007f;
    L_0x001f:
        r3 = r7.mExpirationTimeInMillis;
        r5 = r1.mExpirationTimeInMillis;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x007f;
    L_0x0027:
        r3 = r7.mCheckAaaServerCertStatus;
        r4 = r1.mCheckAaaServerCertStatus;
        if (r3 != r4) goto L_0x007f;
    L_0x002d:
        r3 = r7.mUserCredential;
        if (r3 != 0) goto L_0x0036;
    L_0x0031:
        r3 = r1.mUserCredential;
        if (r3 != 0) goto L_0x007f;
    L_0x0035:
        goto L_0x003e;
    L_0x0036:
        r4 = r1.mUserCredential;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x007f;
    L_0x003e:
        r3 = r7.mCertCredential;
        if (r3 != 0) goto L_0x0047;
    L_0x0042:
        r3 = r1.mCertCredential;
        if (r3 != 0) goto L_0x007f;
    L_0x0046:
        goto L_0x004f;
    L_0x0047:
        r4 = r1.mCertCredential;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x007f;
    L_0x004f:
        r3 = r7.mSimCredential;
        if (r3 != 0) goto L_0x0058;
    L_0x0053:
        r3 = r1.mSimCredential;
        if (r3 != 0) goto L_0x007f;
    L_0x0057:
        goto L_0x0060;
    L_0x0058:
        r4 = r1.mSimCredential;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x007f;
    L_0x0060:
        r3 = r7.mCaCertificates;
        r4 = r1.mCaCertificates;
        r3 = isX509CertificatesEquals(r3, r4);
        if (r3 == 0) goto L_0x007f;
    L_0x006a:
        r3 = r7.mClientCertificateChain;
        r4 = r1.mClientCertificateChain;
        r3 = isX509CertificatesEquals(r3, r4);
        if (r3 == 0) goto L_0x007f;
    L_0x0074:
        r3 = r7.mClientPrivateKey;
        r4 = r1.mClientPrivateKey;
        r3 = isPrivateKeyEquals(r3, r4);
        if (r3 == 0) goto L_0x007f;
    L_0x007e:
        goto L_0x0080;
    L_0x007f:
        r0 = r2;
    L_0x0080:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.pps.Credential.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Long.valueOf(this.mCreationTimeInMillis), Long.valueOf(this.mExpirationTimeInMillis), this.mRealm, Boolean.valueOf(this.mCheckAaaServerCertStatus), this.mUserCredential, this.mCertCredential, this.mSimCredential, this.mClientPrivateKey, Integer.valueOf(Arrays.hashCode(this.mCaCertificates)), Integer.valueOf(Arrays.hashCode(this.mClientCertificateChain))});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Realm: ");
        builder.append(this.mRealm);
        String str = "\n";
        builder.append(str);
        builder.append("CreationTime: ");
        long j = this.mCreationTimeInMillis;
        Object obj = "Not specified";
        builder.append(j != Long.MIN_VALUE ? new Date(j) : obj);
        builder.append(str);
        builder.append("ExpirationTime: ");
        j = this.mExpirationTimeInMillis;
        if (j != Long.MIN_VALUE) {
            obj = new Date(j);
        }
        builder.append(obj);
        builder.append(str);
        builder.append("CheckAAAServerStatus: ");
        builder.append(this.mCheckAaaServerCertStatus);
        builder.append(str);
        if (this.mUserCredential != null) {
            builder.append("UserCredential Begin ---\n");
            builder.append(this.mUserCredential);
            builder.append("UserCredential End ---\n");
        }
        if (this.mCertCredential != null) {
            builder.append("CertificateCredential Begin ---\n");
            builder.append(this.mCertCredential);
            builder.append("CertificateCredential End ---\n");
        }
        if (this.mSimCredential != null) {
            builder.append("SIMCredential Begin ---\n");
            builder.append(this.mSimCredential);
            builder.append("SIMCredential End ---\n");
        }
        return builder.toString();
    }

    public boolean validate(boolean isR1) {
        boolean isEmpty = TextUtils.isEmpty(this.mRealm);
        String str = TAG;
        if (isEmpty) {
            Log.d(str, "Missing realm");
            return false;
        } else if (this.mRealm.getBytes(StandardCharsets.UTF_8).length > 253) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("realm exceeding maximum length: ");
            stringBuilder.append(this.mRealm.getBytes(StandardCharsets.UTF_8).length);
            Log.d(str, stringBuilder.toString());
            return false;
        } else {
            if (this.mUserCredential != null) {
                if (!verifyUserCredential(isR1)) {
                    return false;
                }
            } else if (this.mCertCredential != null) {
                if (!verifyCertCredential(isR1)) {
                    return false;
                }
            } else if (this.mSimCredential == null) {
                Log.d(str, "Missing required credential");
                return false;
            } else if (!verifySimCredential()) {
                return false;
            }
            return true;
        }
    }

    private boolean verifyUserCredential(boolean isR1) {
        UserCredential userCredential = this.mUserCredential;
        String str = TAG;
        if (userCredential == null) {
            Log.d(str, "Missing user credential");
            return false;
        } else if (this.mCertCredential != null || this.mSimCredential != null) {
            Log.d(str, "Contained more than one type of credential");
            return false;
        } else if (!userCredential.validate()) {
            return false;
        } else {
            if (!isR1 || this.mCaCertificates != null) {
                return true;
            }
            Log.d(str, "Missing CA Certificate for user credential");
            return false;
        }
    }

    private boolean verifyCertCredential(boolean isR1) {
        CertificateCredential certificateCredential = this.mCertCredential;
        String str = TAG;
        if (certificateCredential == null) {
            Log.d(str, "Missing certificate credential");
            return false;
        } else if (this.mUserCredential != null || this.mSimCredential != null) {
            Log.d(str, "Contained more than one type of credential");
            return false;
        } else if (!certificateCredential.validate()) {
            return false;
        } else {
            if (isR1 && this.mCaCertificates == null) {
                Log.d(str, "Missing CA Certificate for certificate credential");
                return false;
            } else if (this.mClientPrivateKey == null) {
                Log.d(str, "Missing client private key for certificate credential");
                return false;
            } else {
                try {
                    if (verifySha256Fingerprint(this.mClientCertificateChain, this.mCertCredential.getCertSha256Fingerprint())) {
                        return true;
                    }
                    Log.d(str, "SHA-256 fingerprint mismatch");
                    return false;
                } catch (NoSuchAlgorithmException | CertificateEncodingException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to verify SHA-256 fingerprint: ");
                    stringBuilder.append(e.getMessage());
                    Log.d(str, stringBuilder.toString());
                    return false;
                }
            }
        }
    }

    private boolean verifySimCredential() {
        SimCredential simCredential = this.mSimCredential;
        String str = TAG;
        if (simCredential == null) {
            Log.d(str, "Missing SIM credential");
            return false;
        } else if (this.mUserCredential == null && this.mCertCredential == null) {
            return simCredential.validate();
        } else {
            Log.d(str, "Contained more than one type of credential");
            return false;
        }
    }

    private static boolean isPrivateKeyEquals(PrivateKey key1, PrivateKey key2) {
        boolean z = true;
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null || key2 == null) {
            return false;
        }
        if (!(TextUtils.equals(key1.getAlgorithm(), key2.getAlgorithm()) && Arrays.equals(key1.getEncoded(), key2.getEncoded()))) {
            z = false;
        }
        return z;
    }

    public static boolean isX509CertificateEquals(X509Certificate cert1, X509Certificate cert2) {
        if (cert1 == null && cert2 == null) {
            return true;
        }
        if (cert1 == null || cert2 == null) {
            return false;
        }
        boolean result = false;
        try {
            result = Arrays.equals(cert1.getEncoded(), cert2.getEncoded());
        } catch (CertificateEncodingException e) {
        }
        return result;
    }

    /* JADX WARNING: Missing block: B:18:0x0024, code skipped:
            return false;
     */
    private static boolean isX509CertificatesEquals(java.security.cert.X509Certificate[] r5, java.security.cert.X509Certificate[] r6) {
        /*
        r0 = 1;
        if (r5 != 0) goto L_0x0006;
    L_0x0003:
        if (r6 != 0) goto L_0x0006;
    L_0x0005:
        return r0;
    L_0x0006:
        r1 = 0;
        if (r5 == 0) goto L_0x0024;
    L_0x0009:
        if (r6 != 0) goto L_0x000c;
    L_0x000b:
        goto L_0x0024;
    L_0x000c:
        r2 = r5.length;
        r3 = r6.length;
        if (r2 == r3) goto L_0x0011;
    L_0x0010:
        return r1;
    L_0x0011:
        r2 = 0;
    L_0x0012:
        r3 = r5.length;
        if (r2 >= r3) goto L_0x0023;
    L_0x0015:
        r3 = r5[r2];
        r4 = r6[r2];
        r3 = isX509CertificateEquals(r3, r4);
        if (r3 != 0) goto L_0x0020;
    L_0x001f:
        return r1;
    L_0x0020:
        r2 = r2 + 1;
        goto L_0x0012;
    L_0x0023:
        return r0;
    L_0x0024:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.pps.Credential.isX509CertificatesEquals(java.security.cert.X509Certificate[], java.security.cert.X509Certificate[]):boolean");
    }

    private static boolean verifySha256Fingerprint(X509Certificate[] certChain, byte[] expectedFingerprint) throws NoSuchAlgorithmException, CertificateEncodingException {
        if (certChain == null) {
            return false;
        }
        MessageDigest digester = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
        for (X509Certificate certificate : certChain) {
            digester.reset();
            if (Arrays.equals(expectedFingerprint, digester.digest(certificate.getEncoded()))) {
                return true;
            }
        }
        return false;
    }
}
