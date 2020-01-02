package android.telephony;

import android.annotation.SystemApi;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class UiccAccessRule implements Parcelable {
    public static final Creator<UiccAccessRule> CREATOR = new Creator<UiccAccessRule>() {
        public UiccAccessRule createFromParcel(Parcel in) {
            return new UiccAccessRule(in);
        }

        public UiccAccessRule[] newArray(int size) {
            return new UiccAccessRule[size];
        }
    };
    private static final int ENCODING_VERSION = 1;
    private static final String TAG = "UiccAccessRule";
    private final long mAccessType;
    private final byte[] mCertificateHash;
    private final String mPackageName;

    public static byte[] encodeRules(UiccAccessRule[] accessRules) {
        if (accessRules == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(baos);
            output.writeInt(1);
            output.writeInt(accessRules.length);
            for (UiccAccessRule accessRule : accessRules) {
                output.writeInt(accessRule.mCertificateHash.length);
                output.write(accessRule.mCertificateHash);
                if (accessRule.mPackageName != null) {
                    output.writeBoolean(true);
                    output.writeUTF(accessRule.mPackageName);
                } else {
                    output.writeBoolean(false);
                }
                output.writeLong(accessRule.mAccessType);
            }
            output.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("ByteArrayOutputStream should never lead to an IOException", e);
        }
    }

    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r2.close();
     */
    public static android.telephony.UiccAccessRule[] decodeRules(byte[] r12) {
        /*
        r0 = 0;
        if (r12 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = new java.io.ByteArrayInputStream;
        r1.<init>(r12);
        r2 = new java.io.DataInputStream;	 Catch:{ IOException -> 0x0051 }
        r2.<init>(r1);	 Catch:{ IOException -> 0x0051 }
        r2.readInt();	 Catch:{ all -> 0x0045 }
        r3 = r2.readInt();	 Catch:{ all -> 0x0045 }
        r4 = new android.telephony.UiccAccessRule[r3];	 Catch:{ all -> 0x0045 }
        r5 = 0;
    L_0x0018:
        if (r5 >= r3) goto L_0x003d;
    L_0x001a:
        r6 = r2.readInt();	 Catch:{ all -> 0x0045 }
        r7 = new byte[r6];	 Catch:{ all -> 0x0045 }
        r2.readFully(r7);	 Catch:{ all -> 0x0045 }
        r8 = r2.readBoolean();	 Catch:{ all -> 0x0045 }
        if (r8 == 0) goto L_0x002e;
    L_0x0029:
        r8 = r2.readUTF();	 Catch:{ all -> 0x0045 }
        goto L_0x002f;
    L_0x002e:
        r8 = r0;
    L_0x002f:
        r9 = r2.readLong();	 Catch:{ all -> 0x0045 }
        r11 = new android.telephony.UiccAccessRule;	 Catch:{ all -> 0x0045 }
        r11.<init>(r7, r8, r9);	 Catch:{ all -> 0x0045 }
        r4[r5] = r11;	 Catch:{ all -> 0x0045 }
        r5 = r5 + 1;
        goto L_0x0018;
    L_0x003d:
        r2.close();	 Catch:{ all -> 0x0045 }
        r2.close();	 Catch:{ IOException -> 0x0051 }
        return r4;
    L_0x0045:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r3 = move-exception;
        r2.close();	 Catch:{ all -> 0x004c }
        goto L_0x0050;
    L_0x004c:
        r4 = move-exception;
        r0.addSuppressed(r4);	 Catch:{ IOException -> 0x0051 }
    L_0x0050:
        throw r3;	 Catch:{ IOException -> 0x0051 }
    L_0x0051:
        r0 = move-exception;
        r2 = new java.lang.IllegalStateException;
        r3 = "ByteArrayInputStream should never lead to an IOException";
        r2.<init>(r3, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.UiccAccessRule.decodeRules(byte[]):android.telephony.UiccAccessRule[]");
    }

    public UiccAccessRule(byte[] certificateHash, String packageName, long accessType) {
        this.mCertificateHash = certificateHash;
        this.mPackageName = packageName;
        this.mAccessType = accessType;
    }

    UiccAccessRule(Parcel in) {
        this.mCertificateHash = in.createByteArray();
        this.mPackageName = in.readString();
        this.mAccessType = in.readLong();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mCertificateHash);
        dest.writeString(this.mPackageName);
        dest.writeLong(this.mAccessType);
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getCertificateHexString() {
        return IccUtils.bytesToHexString(this.mCertificateHash);
    }

    public int getCarrierPrivilegeStatus(PackageInfo packageInfo) {
        if (packageInfo.signatures == null || packageInfo.signatures.length == 0) {
            throw new IllegalArgumentException("Must use GET_SIGNATURES when looking up package info");
        }
        for (Signature sig : packageInfo.signatures) {
            int accessStatus = getCarrierPrivilegeStatus(sig, packageInfo.packageName);
            if (accessStatus != 0) {
                return accessStatus;
            }
        }
        return 0;
    }

    public int getCarrierPrivilegeStatus(Signature signature, String packageName) {
        byte[] certHash = getCertHash(signature, KeyProperties.DIGEST_SHA1);
        byte[] certHash256 = getCertHash(signature, KeyProperties.DIGEST_SHA256);
        if (matches(certHash, packageName) || matches(certHash256, packageName)) {
            return 1;
        }
        return 0;
    }

    private boolean matches(byte[] certHash, String packageName) {
        return certHash != null && Arrays.equals(this.mCertificateHash, certHash) && (TextUtils.isEmpty(this.mPackageName) || this.mPackageName.equals(packageName));
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UiccAccessRule that = (UiccAccessRule) obj;
        if (!(Arrays.equals(this.mCertificateHash, that.mCertificateHash) && Objects.equals(this.mPackageName, that.mPackageName) && this.mAccessType == that.mAccessType)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((1 * 31) + Arrays.hashCode(this.mCertificateHash)) * 31) + Objects.hashCode(this.mPackageName)) * 31) + Objects.hashCode(Long.valueOf(this.mAccessType));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cert: ");
        stringBuilder.append(IccUtils.bytesToHexString(this.mCertificateHash));
        stringBuilder.append(" pkg: ");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append(" access: ");
        stringBuilder.append(this.mAccessType);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    private static byte[] getCertHash(Signature signature, String algo) {
        try {
            return MessageDigest.getInstance(algo).digest(signature.toByteArray());
        } catch (NoSuchAlgorithmException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NoSuchAlgorithmException: ");
            stringBuilder.append(ex);
            Rlog.e(TAG, stringBuilder.toString());
            return null;
        }
    }
}
