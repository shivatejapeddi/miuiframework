package android.net;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.HexDump;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class IpSecAlgorithm implements Parcelable {
    public static final String AUTH_CRYPT_AES_GCM = "rfc4106(gcm(aes))";
    public static final String AUTH_HMAC_MD5 = "hmac(md5)";
    public static final String AUTH_HMAC_SHA1 = "hmac(sha1)";
    public static final String AUTH_HMAC_SHA256 = "hmac(sha256)";
    public static final String AUTH_HMAC_SHA384 = "hmac(sha384)";
    public static final String AUTH_HMAC_SHA512 = "hmac(sha512)";
    public static final Creator<IpSecAlgorithm> CREATOR = new Creator<IpSecAlgorithm>() {
        public IpSecAlgorithm createFromParcel(Parcel in) {
            return new IpSecAlgorithm(in.readString(), in.createByteArray(), in.readInt());
        }

        public IpSecAlgorithm[] newArray(int size) {
            return new IpSecAlgorithm[size];
        }
    };
    public static final String CRYPT_AES_CBC = "cbc(aes)";
    public static final String CRYPT_NULL = "ecb(cipher_null)";
    private static final String TAG = "IpSecAlgorithm";
    private final byte[] mKey;
    private final String mName;
    private final int mTruncLenBits;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AlgorithmName {
    }

    public IpSecAlgorithm(String algorithm, byte[] key) {
        this(algorithm, key, 0);
    }

    public IpSecAlgorithm(String algorithm, byte[] key, int truncLenBits) {
        this.mName = algorithm;
        this.mKey = (byte[]) key.clone();
        this.mTruncLenBits = truncLenBits;
        checkValidOrThrow(this.mName, this.mKey.length * 8, this.mTruncLenBits);
    }

    public String getName() {
        return this.mName;
    }

    public byte[] getKey() {
        return (byte[]) this.mKey.clone();
    }

    public int getTruncationLengthBits() {
        return this.mTruncLenBits;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mName);
        out.writeByteArray(this.mKey);
        out.writeInt(this.mTruncLenBits);
    }

    private static void checkValidOrThrow(java.lang.String r10, int r11, int r12) {
        /*
        r0 = 1;
        r1 = 1;
        r2 = r10.hashCode();
        r3 = 1;
        r4 = 0;
        switch(r2) {
            case -1137603038: goto L_0x004d;
            case 394796030: goto L_0x0043;
            case 559425185: goto L_0x0038;
            case 559457797: goto L_0x002d;
            case 559510590: goto L_0x0022;
            case 759177996: goto L_0x0017;
            case 2065384259: goto L_0x000c;
            default: goto L_0x000b;
        };
    L_0x000b:
        goto L_0x0058;
    L_0x000c:
        r2 = "hmac(sha1)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x0015:
        r2 = 2;
        goto L_0x0059;
    L_0x0017:
        r2 = "hmac(md5)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x0020:
        r2 = r3;
        goto L_0x0059;
    L_0x0022:
        r2 = "hmac(sha512)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x002b:
        r2 = 5;
        goto L_0x0059;
    L_0x002d:
        r2 = "hmac(sha384)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x0036:
        r2 = 4;
        goto L_0x0059;
    L_0x0038:
        r2 = "hmac(sha256)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x0041:
        r2 = 3;
        goto L_0x0059;
    L_0x0043:
        r2 = "cbc(aes)";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x004b:
        r2 = r4;
        goto L_0x0059;
    L_0x004d:
        r2 = "rfc4106(gcm(aes))";
        r2 = r10.equals(r2);
        if (r2 == 0) goto L_0x000b;
    L_0x0056:
        r2 = 6;
        goto L_0x0059;
    L_0x0058:
        r2 = -1;
    L_0x0059:
        r5 = 192; // 0xc0 float:2.69E-43 double:9.5E-322;
        r6 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        r7 = 96;
        r8 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r9 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        switch(r2) {
            case 0: goto L_0x00e4;
            case 1: goto L_0x00d6;
            case 2: goto L_0x00c8;
            case 3: goto L_0x00ba;
            case 4: goto L_0x00aa;
            case 5: goto L_0x0099;
            case 6: goto L_0x007d;
            default: goto L_0x0066;
        };
    L_0x0066:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Couldn't find an algorithm: ";
        r3.append(r4);
        r3.append(r10);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x007d:
        if (r11 == r6) goto L_0x008a;
    L_0x007f:
        r2 = 224; // 0xe0 float:3.14E-43 double:1.107E-321;
        if (r11 == r2) goto L_0x008a;
    L_0x0083:
        r2 = 288; // 0x120 float:4.04E-43 double:1.423E-321;
        if (r11 != r2) goto L_0x0088;
    L_0x0087:
        goto L_0x008a;
    L_0x0088:
        r2 = r4;
        goto L_0x008b;
    L_0x008a:
        r2 = r3;
    L_0x008b:
        r0 = r2;
        r2 = 64;
        if (r12 == r2) goto L_0x0096;
    L_0x0090:
        if (r12 == r7) goto L_0x0096;
    L_0x0092:
        if (r12 != r9) goto L_0x0095;
    L_0x0094:
        goto L_0x0096;
    L_0x0095:
        r3 = r4;
    L_0x0096:
        r1 = r3;
        goto L_0x00ee;
    L_0x0099:
        r2 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        if (r11 != r2) goto L_0x009f;
    L_0x009d:
        r5 = r3;
        goto L_0x00a0;
    L_0x009f:
        r5 = r4;
    L_0x00a0:
        r0 = r5;
        if (r12 < r8) goto L_0x00a6;
    L_0x00a3:
        if (r12 > r2) goto L_0x00a6;
    L_0x00a5:
        goto L_0x00a7;
    L_0x00a6:
        r3 = r4;
    L_0x00a7:
        r1 = r3;
        goto L_0x00ee;
    L_0x00aa:
        r2 = 384; // 0x180 float:5.38E-43 double:1.897E-321;
        if (r11 != r2) goto L_0x00b0;
    L_0x00ae:
        r6 = r3;
        goto L_0x00b1;
    L_0x00b0:
        r6 = r4;
    L_0x00b1:
        r0 = r6;
        if (r12 < r5) goto L_0x00b7;
    L_0x00b4:
        if (r12 > r2) goto L_0x00b7;
    L_0x00b6:
        goto L_0x00b8;
    L_0x00b7:
        r3 = r4;
    L_0x00b8:
        r1 = r3;
        goto L_0x00ee;
    L_0x00ba:
        if (r11 != r8) goto L_0x00be;
    L_0x00bc:
        r2 = r3;
        goto L_0x00bf;
    L_0x00be:
        r2 = r4;
    L_0x00bf:
        r0 = r2;
        if (r12 < r7) goto L_0x00c5;
    L_0x00c2:
        if (r12 > r8) goto L_0x00c5;
    L_0x00c4:
        goto L_0x00c6;
    L_0x00c5:
        r3 = r4;
    L_0x00c6:
        r1 = r3;
        goto L_0x00ee;
    L_0x00c8:
        if (r11 != r6) goto L_0x00cc;
    L_0x00ca:
        r2 = r3;
        goto L_0x00cd;
    L_0x00cc:
        r2 = r4;
    L_0x00cd:
        r0 = r2;
        if (r12 < r7) goto L_0x00d3;
    L_0x00d0:
        if (r12 > r6) goto L_0x00d3;
    L_0x00d2:
        goto L_0x00d4;
    L_0x00d3:
        r3 = r4;
    L_0x00d4:
        r1 = r3;
        goto L_0x00ee;
    L_0x00d6:
        if (r11 != r9) goto L_0x00da;
    L_0x00d8:
        r2 = r3;
        goto L_0x00db;
    L_0x00da:
        r2 = r4;
    L_0x00db:
        r0 = r2;
        if (r12 < r7) goto L_0x00e1;
    L_0x00de:
        if (r12 > r9) goto L_0x00e1;
    L_0x00e0:
        goto L_0x00e2;
    L_0x00e1:
        r3 = r4;
    L_0x00e2:
        r1 = r3;
        goto L_0x00ee;
    L_0x00e4:
        if (r11 == r9) goto L_0x00ec;
    L_0x00e6:
        if (r11 == r5) goto L_0x00ec;
    L_0x00e8:
        if (r11 != r8) goto L_0x00eb;
    L_0x00ea:
        goto L_0x00ec;
    L_0x00eb:
        r3 = r4;
    L_0x00ec:
        r0 = r3;
    L_0x00ee:
        if (r0 == 0) goto L_0x010a;
    L_0x00f0:
        if (r1 == 0) goto L_0x00f3;
    L_0x00f2:
        return;
    L_0x00f3:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid truncation keyLength: ";
        r3.append(r4);
        r3.append(r12);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x010a:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid key material keyLength: ";
        r3.append(r4);
        r3.append(r11);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecAlgorithm.checkValidOrThrow(java.lang.String, int, int):void");
    }

    public boolean isAuthentication() {
        /*
        r7 = this;
        r0 = r7.getName();
        r1 = r0.hashCode();
        r2 = 0;
        r3 = 4;
        r4 = 3;
        r5 = 2;
        r6 = 1;
        switch(r1) {
            case 559425185: goto L_0x003d;
            case 559457797: goto L_0x0032;
            case 559510590: goto L_0x0027;
            case 759177996: goto L_0x001c;
            case 2065384259: goto L_0x0011;
            default: goto L_0x0010;
        };
    L_0x0010:
        goto L_0x0048;
    L_0x0011:
        r1 = "hmac(sha1)";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0010;
    L_0x001a:
        r0 = r6;
        goto L_0x0049;
    L_0x001c:
        r1 = "hmac(md5)";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0010;
    L_0x0025:
        r0 = r2;
        goto L_0x0049;
    L_0x0027:
        r1 = "hmac(sha512)";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0010;
    L_0x0030:
        r0 = r3;
        goto L_0x0049;
    L_0x0032:
        r1 = "hmac(sha384)";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0010;
    L_0x003b:
        r0 = r4;
        goto L_0x0049;
    L_0x003d:
        r1 = "hmac(sha256)";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0010;
    L_0x0046:
        r0 = r5;
        goto L_0x0049;
    L_0x0048:
        r0 = -1;
    L_0x0049:
        if (r0 == 0) goto L_0x0054;
    L_0x004b:
        if (r0 == r6) goto L_0x0054;
    L_0x004d:
        if (r0 == r5) goto L_0x0054;
    L_0x004f:
        if (r0 == r4) goto L_0x0054;
    L_0x0051:
        if (r0 == r3) goto L_0x0054;
    L_0x0053:
        return r2;
    L_0x0054:
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecAlgorithm.isAuthentication():boolean");
    }

    public boolean isEncryption() {
        return getName().equals(CRYPT_AES_CBC);
    }

    public boolean isAead() {
        return getName().equals(AUTH_CRYPT_AES_GCM);
    }

    private static boolean isUnsafeBuild() {
        return Build.IS_DEBUGGABLE && Build.IS_ENG;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{mName=");
        stringBuilder.append(this.mName);
        stringBuilder.append(", mKey=");
        stringBuilder.append(isUnsafeBuild() ? HexDump.toHexString(this.mKey) : "<hidden>");
        stringBuilder.append(", mTruncLenBits=");
        stringBuilder.append(this.mTruncLenBits);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @VisibleForTesting
    public static boolean equals(IpSecAlgorithm lhs, IpSecAlgorithm rhs) {
        boolean z = true;
        if (lhs == null || rhs == null) {
            if (lhs != rhs) {
                z = false;
            }
            return z;
        }
        if (!(lhs.mName.equals(rhs.mName) && Arrays.equals(lhs.mKey, rhs.mKey) && lhs.mTruncLenBits == rhs.mTruncLenBits)) {
            z = false;
        }
        return z;
    }
}
