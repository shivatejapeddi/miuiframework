package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.ArrayUtils;
import java.io.ByteArrayInputStream;
import java.lang.ref.SoftReference;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Signature implements Parcelable {
    public static final Creator<Signature> CREATOR = new Creator<Signature>() {
        public Signature createFromParcel(Parcel source) {
            return new Signature(source, null);
        }

        public Signature[] newArray(int size) {
            return new Signature[size];
        }
    };
    private Certificate[] mCertificateChain;
    private int mFlags;
    private int mHashCode;
    private boolean mHaveHashCode;
    private final byte[] mSignature;
    private SoftReference<String> mStringRef;

    /* synthetic */ Signature(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public Signature(byte[] signature) {
        this.mSignature = (byte[]) signature.clone();
        this.mCertificateChain = null;
    }

    public Signature(Certificate[] certificateChain) throws CertificateEncodingException {
        this.mSignature = certificateChain[0].getEncoded();
        if (certificateChain.length > 1) {
            this.mCertificateChain = (Certificate[]) Arrays.copyOfRange(certificateChain, 1, certificateChain.length);
        }
    }

    private static final int parseHexDigit(int nibble) {
        if (48 <= nibble && nibble <= 57) {
            return nibble - 48;
        }
        if (97 <= nibble && nibble <= 102) {
            return (nibble - 97) + 10;
        }
        if (65 <= nibble && nibble <= 70) {
            return (nibble - 65) + 10;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid character ");
        stringBuilder.append(nibble);
        stringBuilder.append(" in hex string");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Signature(String text) {
        byte[] input = text.getBytes();
        int N = input.length;
        if (N % 2 == 0) {
            byte[] sig = new byte[(N / 2)];
            int sigIndex = 0;
            int i = 0;
            while (i < N) {
                int i2 = i + 1;
                int i3 = i2 + 1;
                int sigIndex2 = sigIndex + 1;
                sig[sigIndex] = (byte) ((parseHexDigit(input[i]) << 4) | parseHexDigit(input[i2]));
                i = i3;
                sigIndex = sigIndex2;
            }
            this.mSignature = sig;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("text size ");
        stringBuilder.append(N);
        stringBuilder.append(" is not even");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setFlags(int flags) {
        this.mFlags = flags;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public char[] toChars() {
        return toChars(null, null);
    }

    public char[] toChars(char[] existingArray, int[] outLen) {
        byte[] sig = this.mSignature;
        int N = sig.length;
        int N2 = N * 2;
        char[] text = (existingArray == null || N2 > existingArray.length) ? new char[N2] : existingArray;
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 15;
            text[j * 2] = (char) (d >= 10 ? (d + 97) - 10 : d + 48);
            d = v & 15;
            text[(j * 2) + 1] = (char) (d >= 10 ? (d + 97) - 10 : d + 48);
        }
        if (outLen != null) {
            outLen[0] = N;
        }
        return text;
    }

    public String toCharsString() {
        SoftReference softReference = this.mStringRef;
        String str = softReference == null ? null : (String) softReference.get();
        if (str != null) {
            return str;
        }
        str = new String(toChars());
        this.mStringRef = new SoftReference(str);
        return str;
    }

    public byte[] toByteArray() {
        byte[] bArr = this.mSignature;
        byte[] bytes = new byte[bArr.length];
        System.arraycopy(bArr, 0, bytes, 0, bArr.length);
        return bytes;
    }

    @UnsupportedAppUsage
    public PublicKey getPublicKey() throws CertificateException {
        return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(this.mSignature)).getPublicKey();
    }

    public Signature[] getChainSignatures() throws CertificateEncodingException {
        Certificate[] certificateArr = this.mCertificateChain;
        int i = 0;
        if (certificateArr == null) {
            return new Signature[]{this};
        }
        Signature[] chain = new Signature[(certificateArr.length + 1)];
        chain[0] = this;
        int i2 = 1;
        int length = certificateArr.length;
        while (i < length) {
            int i3 = i2 + 1;
            chain[i2] = new Signature(certificateArr[i].getEncoded());
            i++;
            i2 = i3;
        }
        return chain;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                Signature other = (Signature) obj;
                if (this == other || Arrays.equals(this.mSignature, other.mSignature)) {
                    z = true;
                }
                return z;
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.mHaveHashCode) {
            return this.mHashCode;
        }
        this.mHashCode = Arrays.hashCode(this.mSignature);
        this.mHaveHashCode = true;
        return this.mHashCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeByteArray(this.mSignature);
    }

    private Signature(Parcel source) {
        this.mSignature = source.createByteArray();
    }

    public static boolean areExactMatch(Signature[] a, Signature[] b) {
        return a.length == b.length && ArrayUtils.containsAll((Object[]) a, (Object[]) b) && ArrayUtils.containsAll((Object[]) b, (Object[]) a);
    }

    public static boolean areEffectiveMatch(Signature[] a, Signature[] b) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Signature[] aPrime = new Signature[a.length];
        for (int i = 0; i < a.length; i++) {
            aPrime[i] = bounce(cf, a[i]);
        }
        Signature[] bPrime = new Signature[b.length];
        for (int i2 = 0; i2 < b.length; i2++) {
            bPrime[i2] = bounce(cf, b[i2]);
        }
        return areExactMatch(aPrime, bPrime);
    }

    public static boolean areEffectiveMatch(Signature a, Signature b) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return bounce(cf, a).equals(bounce(cf, b));
    }

    public static Signature bounce(CertificateFactory cf, Signature s) throws CertificateException {
        Signature sPrime = new Signature(((X509Certificate) cf.generateCertificate(new ByteArrayInputStream(s.mSignature))).getEncoded());
        if (Math.abs(sPrime.mSignature.length - s.mSignature.length) <= 2) {
            return sPrime;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bounced cert length looks fishy; before ");
        stringBuilder.append(s.mSignature.length);
        stringBuilder.append(", after ");
        stringBuilder.append(sPrime.mSignature.length);
        throw new CertificateException(stringBuilder.toString());
    }
}
