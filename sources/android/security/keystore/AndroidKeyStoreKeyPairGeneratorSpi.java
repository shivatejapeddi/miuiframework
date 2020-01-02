package android.security.keystore;

import android.security.Credentials;
import android.security.KeyPairGeneratorSpec;
import android.security.KeyStore;
import android.security.KeyStore.State;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.Digest;
import com.android.internal.util.ArrayUtils;
import com.android.org.bouncycastle.x509.X509V3CertificateGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class AndroidKeyStoreKeyPairGeneratorSpi extends KeyPairGeneratorSpi {
    private static final int EC_DEFAULT_KEY_SIZE = 256;
    private static final int RSA_DEFAULT_KEY_SIZE = 2048;
    private static final int RSA_MAX_KEY_SIZE = 8192;
    private static final int RSA_MIN_KEY_SIZE = 512;
    private static final List<String> SUPPORTED_EC_NIST_CURVE_NAMES = new ArrayList();
    private static final Map<String, Integer> SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE = new HashMap();
    private static final List<Integer> SUPPORTED_EC_NIST_CURVE_SIZES = new ArrayList();
    private boolean mEncryptionAtRestRequired;
    private String mEntryAlias;
    private int mEntryUid;
    private String mJcaKeyAlgorithm;
    private int mKeySizeBits;
    private KeyStore mKeyStore;
    private int mKeymasterAlgorithm = -1;
    private int[] mKeymasterBlockModes;
    private int[] mKeymasterDigests;
    private int[] mKeymasterEncryptionPaddings;
    private int[] mKeymasterPurposes;
    private int[] mKeymasterSignaturePaddings;
    private final int mOriginalKeymasterAlgorithm;
    private BigInteger mRSAPublicExponent;
    private SecureRandom mRng;
    private KeyGenParameterSpec mSpec;

    public static class EC extends AndroidKeyStoreKeyPairGeneratorSpi {
        public EC() {
            super(3);
        }
    }

    public static class RSA extends AndroidKeyStoreKeyPairGeneratorSpi {
        public RSA() {
            super(1);
        }
    }

    static {
        Map map = SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE;
        Integer valueOf = Integer.valueOf(224);
        map.put("p-224", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp224r1", valueOf);
        map = SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE;
        valueOf = Integer.valueOf(256);
        map.put("p-256", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp256r1", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("prime256v1", valueOf);
        map = SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE;
        valueOf = Integer.valueOf(384);
        map.put("p-384", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp384r1", valueOf);
        map = SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE;
        valueOf = Integer.valueOf(521);
        map.put("p-521", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp521r1", valueOf);
        SUPPORTED_EC_NIST_CURVE_NAMES.addAll(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.keySet());
        Collections.sort(SUPPORTED_EC_NIST_CURVE_NAMES);
        SUPPORTED_EC_NIST_CURVE_SIZES.addAll(new HashSet(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.values()));
        Collections.sort(SUPPORTED_EC_NIST_CURVE_SIZES);
    }

    protected AndroidKeyStoreKeyPairGeneratorSpi(int keymasterAlgorithm) {
        this.mOriginalKeymasterAlgorithm = keymasterAlgorithm;
    }

    public void initialize(int keysize, SecureRandom random) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KeyGenParameterSpec.class.getName());
        stringBuilder.append(" or ");
        stringBuilder.append(KeyPairGeneratorSpec.class.getName());
        stringBuilder.append(" required to initialize this KeyPairGenerator");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:87:0x026d  */
    public void initialize(java.security.spec.AlgorithmParameterSpec r19, java.security.SecureRandom r20) throws java.security.InvalidAlgorithmParameterException {
        /*
        r18 = this;
        r1 = r18;
        r2 = r19;
        r18.resetAll();
        r3 = 0;
        if (r2 == 0) goto L_0x023d;
    L_0x000a:
        r4 = 0;
        r0 = r1.mOriginalKeymasterAlgorithm;	 Catch:{ all -> 0x0239 }
        r5 = r0;
        r0 = r2 instanceof android.security.keystore.KeyGenParameterSpec;	 Catch:{ all -> 0x0239 }
        r6 = -1;
        r7 = 0;
        r8 = 1;
        if (r0 == 0) goto L_0x001c;
    L_0x0015:
        r0 = r2;
        r0 = (android.security.keystore.KeyGenParameterSpec) r0;	 Catch:{ all -> 0x0239 }
        r9 = r4;
        r4 = r0;
        goto L_0x00fd;
    L_0x001c:
        r0 = r2 instanceof android.security.KeyPairGeneratorSpec;	 Catch:{ all -> 0x0239 }
        if (r0 == 0) goto L_0x01fc;
    L_0x0020:
        r0 = r2;
        r0 = (android.security.KeyPairGeneratorSpec) r0;	 Catch:{ all -> 0x0239 }
        r9 = r0;
        r0 = r9.getKeyType();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r10 = r0;
        if (r10 == 0) goto L_0x003d;
        r0 = android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(r10);	 Catch:{ IllegalArgumentException -> 0x0032 }
        r5 = r0;
        goto L_0x003d;
    L_0x0032:
        r0 = move-exception;
        r6 = r0;
        r0 = r6;
        r6 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r7 = "Invalid key type in parameters";
        r6.<init>(r7, r0);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        throw r6;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
    L_0x003d:
        if (r5 == r8) goto L_0x007a;
    L_0x003f:
        r0 = 3;
        if (r5 != r0) goto L_0x0061;
    L_0x0042:
        r0 = new android.security.keystore.KeyGenParameterSpec$Builder;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.getKeystoreAlias();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r12 = 12;
        r0.<init>(r11, r12);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = "NONE";
        r12 = "SHA-1";
        r13 = "SHA-224";
        r14 = "SHA-256";
        r15 = "SHA-384";
        r16 = "SHA-512";
        r11 = new java.lang.String[]{r11, r12, r13, r14, r15, r16};	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setDigests(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        goto L_0x00b6;
    L_0x0061:
        r0 = new java.security.ProviderException;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r6 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r6.<init>();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r7 = "Unsupported algorithm: ";
        r6.append(r7);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r7 = r1.mKeymasterAlgorithm;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r6.append(r7);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r6 = r6.toString();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.<init>(r6);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        throw r0;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
    L_0x007a:
        r0 = new android.security.keystore.KeyGenParameterSpec$Builder;	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.getKeystoreAlias();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r12 = 15;
        r0.<init>(r11, r12);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = "NONE";
        r12 = "MD5";
        r13 = "SHA-1";
        r14 = "SHA-224";
        r15 = "SHA-256";
        r16 = "SHA-384";
        r17 = "SHA-512";
        r11 = new java.lang.String[]{r11, r12, r13, r14, r15, r16, r17};	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setDigests(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = "NoPadding";
        r12 = "PKCS1Padding";
        r13 = "OAEPPadding";
        r11 = new java.lang.String[]{r11, r12, r13};	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setEncryptionPaddings(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = "PKCS1";
        r12 = "PSS";
        r11 = new java.lang.String[]{r11, r12};	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setSignaturePaddings(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setRandomizedEncryptionRequired(r7);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
    L_0x00b6:
        r11 = r9.getKeySize();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        if (r11 == r6) goto L_0x00c3;
    L_0x00bc:
        r11 = r9.getKeySize();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setKeySize(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
    L_0x00c3:
        r11 = r9.getAlgorithmParameterSpec();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        if (r11 == 0) goto L_0x00d1;
        r11 = r9.getAlgorithmParameterSpec();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setAlgorithmParameterSpec(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
    L_0x00d1:
        r11 = r9.getSubjectDN();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setCertificateSubject(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.getSerialNumber();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setCertificateSerialNumber(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.getStartDate();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setCertificateNotBefore(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.getEndDate();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0.setCertificateNotAfter(r11);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r9.isEncryptionRequired();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r4 = r11;
        r0.setUserAuthenticationRequired(r7);	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r11 = r0.build();	 Catch:{ IllegalArgumentException | NullPointerException -> 0x01f3, IllegalArgumentException | NullPointerException -> 0x01f3 }
        r0 = r11;
        r9 = r4;
        r4 = r0;
    L_0x00fd:
        r0 = r4.getKeystoreAlias();	 Catch:{ all -> 0x0239 }
        r1.mEntryAlias = r0;	 Catch:{ all -> 0x0239 }
        r0 = r4.getUid();	 Catch:{ all -> 0x0239 }
        r1.mEntryUid = r0;	 Catch:{ all -> 0x0239 }
        r1.mSpec = r4;	 Catch:{ all -> 0x0239 }
        r1.mKeymasterAlgorithm = r5;	 Catch:{ all -> 0x0239 }
        r1.mEncryptionAtRestRequired = r9;	 Catch:{ all -> 0x0239 }
        r0 = r4.getKeySize();	 Catch:{ all -> 0x0239 }
        r1.mKeySizeBits = r0;	 Catch:{ all -> 0x0239 }
        r18.initAlgorithmSpecificParameters();	 Catch:{ all -> 0x0239 }
        r0 = r1.mKeySizeBits;	 Catch:{ all -> 0x0239 }
        if (r0 != r6) goto L_0x0122;
    L_0x011c:
        r0 = getDefaultKeySize(r5);	 Catch:{ all -> 0x0239 }
        r1.mKeySizeBits = r0;	 Catch:{ all -> 0x0239 }
    L_0x0122:
        r0 = r1.mKeySizeBits;	 Catch:{ all -> 0x0239 }
        r6 = r1.mSpec;	 Catch:{ all -> 0x0239 }
        r6 = r6.isStrongBoxBacked();	 Catch:{ all -> 0x0239 }
        checkValidKeySize(r5, r0, r6);	 Catch:{ all -> 0x0239 }
        r0 = r4.getKeystoreAlias();	 Catch:{ all -> 0x0239 }
        if (r0 == 0) goto L_0x01e9;
    L_0x0133:
        r0 = android.security.keystore.KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.getPurposes();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = android.security.keystore.KeyProperties.Purpose.allToKeymaster(r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterPurposes = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.getBlockModes();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = android.security.keystore.KeyProperties.BlockMode.allToKeymaster(r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterBlockModes = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.getEncryptionPaddings();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterEncryptionPaddings = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.getPurposes();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r6 & r8;
        if (r6 == 0) goto L_0x01a2;
    L_0x015d:
        r6 = r4.isRandomizedEncryptionRequired();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        if (r6 == 0) goto L_0x01a2;
    L_0x0163:
        r6 = r1.mKeymasterEncryptionPaddings;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = r6.length;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
    L_0x0166:
        if (r7 >= r8) goto L_0x01a2;
    L_0x0168:
        r10 = r6[r7];	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r11 = android.security.keystore.KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(r10);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        if (r11 == 0) goto L_0x0174;
    L_0x0171:
        r7 = r7 + 1;
        goto L_0x0166;
    L_0x0174:
        r6 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7.<init>();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = "Randomized encryption (IND-CPA) required but may be violated by padding scheme: ";
        r7.append(r8);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(r10);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7.append(r8);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = ". See ";
        r7.append(r8);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = android.security.keystore.KeyGenParameterSpec.class;
        r8 = r8.getName();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7.append(r8);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r8 = " documentation.";
        r7.append(r8);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7 = r7.toString();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6.<init>(r7);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        throw r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.getSignaturePaddings();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = android.security.keystore.KeyProperties.SignaturePadding.allToKeymaster(r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterSignaturePaddings = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = r4.isDigestsSpecified();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        if (r6 == 0) goto L_0x01be;
    L_0x01b3:
        r6 = r4.getDigests();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6 = android.security.keystore.KeyProperties.Digest.allToKeymaster(r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterDigests = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        goto L_0x01c2;
    L_0x01be:
        r6 = libcore.util.EmptyArray.INT;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mKeymasterDigests = r6;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
    L_0x01c2:
        r6 = new android.security.keymaster.KeymasterArguments;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r6.<init>();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r7 = r1.mSpec;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        android.security.keystore.KeymasterUtils.addUserAuthArgs(r6, r7);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x01e0, IllegalArgumentException | IllegalStateException -> 0x01e0 }
        r1.mJcaKeyAlgorithm = r0;	 Catch:{ all -> 0x0239 }
        r6 = r20;
        r1.mRng = r6;	 Catch:{ all -> 0x026a }
        r7 = android.security.KeyStore.getInstance();	 Catch:{ all -> 0x026a }
        r1.mKeyStore = r7;	 Catch:{ all -> 0x026a }
        r0 = 1;
        if (r0 != 0) goto L_0x01df;
    L_0x01dc:
        r18.resetAll();
    L_0x01df:
        return;
    L_0x01e0:
        r0 = move-exception;
        r6 = r20;
        r7 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ all -> 0x026a }
        r7.<init>(r0);	 Catch:{ all -> 0x026a }
        throw r7;	 Catch:{ all -> 0x026a }
    L_0x01e9:
        r6 = r20;
        r0 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ all -> 0x026a }
        r7 = "KeyStore entry alias not provided";
        r0.<init>(r7);	 Catch:{ all -> 0x026a }
        throw r0;	 Catch:{ all -> 0x026a }
    L_0x01f3:
        r0 = move-exception;
        r6 = r20;
        r7 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ all -> 0x026a }
        r7.<init>(r0);	 Catch:{ all -> 0x026a }
        throw r7;	 Catch:{ all -> 0x026a }
    L_0x01fc:
        r6 = r20;
        r0 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ all -> 0x026a }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x026a }
        r7.<init>();	 Catch:{ all -> 0x026a }
        r8 = "Unsupported params class: ";
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r8 = r19.getClass();	 Catch:{ all -> 0x026a }
        r8 = r8.getName();	 Catch:{ all -> 0x026a }
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r8 = ". Supported: ";
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r8 = android.security.keystore.KeyGenParameterSpec.class;
        r8 = r8.getName();	 Catch:{ all -> 0x026a }
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r8 = ", ";
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r8 = android.security.KeyPairGeneratorSpec.class;
        r8 = r8.getName();	 Catch:{ all -> 0x026a }
        r7.append(r8);	 Catch:{ all -> 0x026a }
        r7 = r7.toString();	 Catch:{ all -> 0x026a }
        r0.<init>(r7);	 Catch:{ all -> 0x026a }
        throw r0;	 Catch:{ all -> 0x026a }
    L_0x0239:
        r0 = move-exception;
        r6 = r20;
        goto L_0x026b;
    L_0x023d:
        r6 = r20;
        r0 = new java.security.InvalidAlgorithmParameterException;	 Catch:{ all -> 0x026a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x026a }
        r4.<init>();	 Catch:{ all -> 0x026a }
        r5 = "Must supply params of type ";
        r4.append(r5);	 Catch:{ all -> 0x026a }
        r5 = android.security.keystore.KeyGenParameterSpec.class;
        r5 = r5.getName();	 Catch:{ all -> 0x026a }
        r4.append(r5);	 Catch:{ all -> 0x026a }
        r5 = " or ";
        r4.append(r5);	 Catch:{ all -> 0x026a }
        r5 = android.security.KeyPairGeneratorSpec.class;
        r5 = r5.getName();	 Catch:{ all -> 0x026a }
        r4.append(r5);	 Catch:{ all -> 0x026a }
        r4 = r4.toString();	 Catch:{ all -> 0x026a }
        r0.<init>(r4);	 Catch:{ all -> 0x026a }
        throw r0;	 Catch:{ all -> 0x026a }
    L_0x026a:
        r0 = move-exception;
    L_0x026b:
        if (r3 != 0) goto L_0x0270;
    L_0x026d:
        r18.resetAll();
    L_0x0270:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void");
    }

    private void resetAll() {
        this.mEntryAlias = null;
        this.mEntryUid = -1;
        this.mJcaKeyAlgorithm = null;
        this.mKeymasterAlgorithm = -1;
        this.mKeymasterPurposes = null;
        this.mKeymasterBlockModes = null;
        this.mKeymasterEncryptionPaddings = null;
        this.mKeymasterSignaturePaddings = null;
        this.mKeymasterDigests = null;
        this.mKeySizeBits = 0;
        this.mSpec = null;
        this.mRSAPublicExponent = null;
        this.mEncryptionAtRestRequired = false;
        this.mRng = null;
        this.mKeyStore = null;
    }

    private void initAlgorithmSpecificParameters() throws InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algSpecificSpec = this.mSpec.getAlgorithmParameterSpec();
        int i = this.mKeymasterAlgorithm;
        String str = " vs ";
        String str2 = ": ";
        String str3 = " and ";
        int i2;
        StringBuilder stringBuilder;
        if (i == 1) {
            BigInteger publicExponent = null;
            if (algSpecificSpec instanceof RSAKeyGenParameterSpec) {
                RSAKeyGenParameterSpec rsaSpec = (RSAKeyGenParameterSpec) algSpecificSpec;
                i2 = this.mKeySizeBits;
                if (i2 == -1) {
                    this.mKeySizeBits = rsaSpec.getKeysize();
                } else if (i2 != rsaSpec.getKeysize()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("RSA key size must match  between ");
                    stringBuilder2.append(this.mSpec);
                    stringBuilder2.append(str3);
                    stringBuilder2.append(algSpecificSpec);
                    stringBuilder2.append(str2);
                    stringBuilder2.append(this.mKeySizeBits);
                    stringBuilder2.append(str);
                    stringBuilder2.append(rsaSpec.getKeysize());
                    throw new InvalidAlgorithmParameterException(stringBuilder2.toString());
                }
                publicExponent = rsaSpec.getPublicExponent();
            } else if (algSpecificSpec != null) {
                throw new InvalidAlgorithmParameterException("RSA may only use RSAKeyGenParameterSpec");
            }
            if (publicExponent == null) {
                publicExponent = RSAKeyGenParameterSpec.F4;
            }
            if (publicExponent.compareTo(BigInteger.ZERO) < 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("RSA public exponent must be positive: ");
                stringBuilder.append(publicExponent);
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            } else if (publicExponent.compareTo(KeymasterArguments.UINT64_MAX_VALUE) <= 0) {
                this.mRSAPublicExponent = publicExponent;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported RSA public exponent: ");
                stringBuilder.append(publicExponent);
                stringBuilder.append(". Maximum supported value: ");
                stringBuilder.append(KeymasterArguments.UINT64_MAX_VALUE);
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        } else if (i != 3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unsupported algorithm: ");
            stringBuilder3.append(this.mKeymasterAlgorithm);
            throw new ProviderException(stringBuilder3.toString());
        } else if (algSpecificSpec instanceof ECGenParameterSpec) {
            String curveName = ((ECGenParameterSpec) algSpecificSpec).getName();
            Integer ecSpecKeySizeBits = (Integer) SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.get(curveName.toLowerCase(Locale.US));
            if (ecSpecKeySizeBits != null) {
                i2 = this.mKeySizeBits;
                if (i2 == -1) {
                    this.mKeySizeBits = ecSpecKeySizeBits.intValue();
                    return;
                } else if (i2 != ecSpecKeySizeBits.intValue()) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("EC key size must match  between ");
                    stringBuilder4.append(this.mSpec);
                    stringBuilder4.append(str3);
                    stringBuilder4.append(algSpecificSpec);
                    stringBuilder4.append(str2);
                    stringBuilder4.append(this.mKeySizeBits);
                    stringBuilder4.append(str);
                    stringBuilder4.append(ecSpecKeySizeBits);
                    throw new InvalidAlgorithmParameterException(stringBuilder4.toString());
                } else {
                    return;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported EC curve name: ");
            stringBuilder.append(curveName);
            stringBuilder.append(". Supported: ");
            stringBuilder.append(SUPPORTED_EC_NIST_CURVE_NAMES);
            throw new InvalidAlgorithmParameterException(stringBuilder.toString());
        } else if (algSpecificSpec != null) {
            throw new InvalidAlgorithmParameterException("EC may only use ECGenParameterSpec");
        }
    }

    public KeyPair generateKeyPair() {
        KeyStore keyStore = this.mKeyStore;
        if (keyStore == null || this.mSpec == null) {
            throw new IllegalStateException("Not initialized");
        }
        int flags = this.mEncryptionAtRestRequired;
        if ((flags & 1) == 0 || keyStore.state() == State.UNLOCKED) {
            if (this.mSpec.isStrongBoxBacked()) {
                flags |= 16;
            }
            byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, (this.mKeySizeBits + 7) / 8);
            Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias, this.mEntryUid);
            String privateKeyAlias = new StringBuilder();
            privateKeyAlias.append(Credentials.USER_PRIVATE_KEY);
            privateKeyAlias.append(this.mEntryAlias);
            privateKeyAlias = privateKeyAlias.toString();
            try {
                generateKeystoreKeyPair(privateKeyAlias, constructKeyGenerationArguments(), additionalEntropy, flags);
                KeyPair keyPair = loadKeystoreKeyPair(privateKeyAlias);
                storeCertificateChain(flags, createCertificateChain(privateKeyAlias, keyPair));
                if (!true) {
                    Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias, this.mEntryUid);
                }
                return keyPair;
            } catch (ProviderException e) {
                if ((this.mSpec.getPurposes() & 32) != 0) {
                    throw new SecureKeyImportUnavailableException(e);
                }
                throw e;
            } catch (Throwable th) {
                if (!false) {
                    Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias, this.mEntryUid);
                }
            }
        } else {
            throw new IllegalStateException("Encryption at rest using secure lock screen credential requested for key pair, but the user has not yet entered the credential");
        }
    }

    private Iterable<byte[]> createCertificateChain(String privateKeyAlias, KeyPair keyPair) throws ProviderException {
        byte[] challenge = this.mSpec.getAttestationChallenge();
        if (challenge == null) {
            return Collections.singleton(generateSelfSignedCertificateBytes(keyPair));
        }
        KeymasterArguments args = new KeymasterArguments();
        args.addBytes(KeymasterDefs.KM_TAG_ATTESTATION_CHALLENGE, challenge);
        return getAttestationChain(privateKeyAlias, keyPair, args);
    }

    private void generateKeystoreKeyPair(String privateKeyAlias, KeymasterArguments args, byte[] additionalEntropy, int flags) throws ProviderException {
        String str = privateKeyAlias;
        KeymasterArguments keymasterArguments = args;
        byte[] bArr = additionalEntropy;
        int errorCode = this.mKeyStore.generateKey(str, keymasterArguments, bArr, this.mEntryUid, flags, new KeyCharacteristics());
        if (errorCode != 1) {
            String str2 = "Failed to generate key pair";
            if (errorCode == -68) {
                throw new StrongBoxUnavailableException(str2);
            }
            throw new ProviderException(str2, KeyStore.getKeyStoreException(errorCode));
        }
    }

    private KeyPair loadKeystoreKeyPair(String privateKeyAlias) throws ProviderException {
        try {
            KeyPair result = AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(this.mKeyStore, privateKeyAlias, this.mEntryUid);
            if (this.mJcaKeyAlgorithm.equalsIgnoreCase(result.getPrivate().getAlgorithm())) {
                return result;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Generated key pair algorithm does not match requested algorithm: ");
            stringBuilder.append(result.getPrivate().getAlgorithm());
            stringBuilder.append(" vs ");
            stringBuilder.append(this.mJcaKeyAlgorithm);
            throw new ProviderException(stringBuilder.toString());
        } catch (KeyPermanentlyInvalidatedException | UnrecoverableKeyException e) {
            throw new ProviderException("Failed to load generated key pair from keystore", e);
        }
    }

    private KeymasterArguments constructKeyGenerationArguments() {
        KeymasterArguments args = new KeymasterArguments();
        args.addUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, (long) this.mKeySizeBits);
        args.addEnum(268435458, this.mKeymasterAlgorithm);
        args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, this.mKeymasterPurposes);
        args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockModes);
        args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterEncryptionPaddings);
        args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterSignaturePaddings);
        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigests);
        KeymasterUtils.addUserAuthArgs(args, this.mSpec);
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, this.mSpec.getKeyValidityStart());
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, this.mSpec.getKeyValidityForOriginationEnd());
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, this.mSpec.getKeyValidityForConsumptionEnd());
        addAlgorithmSpecificParameters(args);
        if (this.mSpec.isUniqueIdIncluded()) {
            args.addBoolean(KeymasterDefs.KM_TAG_INCLUDE_UNIQUE_ID);
        }
        return args;
    }

    private void storeCertificateChain(int flags, Iterable<byte[]> iterable) throws ProviderException {
        Iterator<byte[]> iter = iterable.iterator();
        storeCertificate(Credentials.USER_CERTIFICATE, (byte[]) iter.next(), flags, "Failed to store certificate");
        if (iter.hasNext()) {
            ByteArrayOutputStream certificateConcatenationStream = new ByteArrayOutputStream();
            while (iter.hasNext()) {
                byte[] data = (byte[]) iter.next();
                certificateConcatenationStream.write(data, 0, data.length);
            }
            storeCertificate(Credentials.CA_CERTIFICATE, certificateConcatenationStream.toByteArray(), flags, "Failed to store attestation CA certificate");
        }
    }

    private void storeCertificate(String prefix, byte[] certificateBytes, int flags, String failureMessage) throws ProviderException {
        int insertErrorCode = this.mKeyStore;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append(this.mEntryAlias);
        insertErrorCode = insertErrorCode.insert(stringBuilder.toString(), certificateBytes, this.mEntryUid, flags);
        if (insertErrorCode != 1) {
            throw new ProviderException(failureMessage, KeyStore.getKeyStoreException(insertErrorCode));
        }
    }

    private byte[] generateSelfSignedCertificateBytes(KeyPair keyPair) throws ProviderException {
        try {
            return generateSelfSignedCertificate(keyPair.getPrivate(), keyPair.getPublic()).getEncoded();
        } catch (IOException | CertificateParsingException e) {
            throw new ProviderException("Failed to generate self-signed certificate", e);
        } catch (CertificateEncodingException e2) {
            throw new ProviderException("Failed to obtain encoded form of self-signed certificate", e2);
        }
    }

    private Iterable<byte[]> getAttestationChain(String privateKeyAlias, KeyPair keyPair, KeymasterArguments args) throws ProviderException {
        KeymasterCertificateChain outChain = new KeymasterCertificateChain();
        int errorCode = this.mKeyStore.attestKey(privateKeyAlias, args, outChain);
        if (errorCode == 1) {
            Collection<byte[]> chain = outChain.getCertificates();
            if (chain.size() >= 2) {
                return chain;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attestation certificate chain contained ");
            stringBuilder.append(chain.size());
            stringBuilder.append(" entries. At least two are required.");
            throw new ProviderException(stringBuilder.toString());
        }
        throw new ProviderException("Failed to generate attestation certificate chain", KeyStore.getKeyStoreException(errorCode));
    }

    private void addAlgorithmSpecificParameters(KeymasterArguments keymasterArgs) {
        int i = this.mKeymasterAlgorithm;
        if (i == 1) {
            keymasterArgs.addUnsignedLong(KeymasterDefs.KM_TAG_RSA_PUBLIC_EXPONENT, this.mRSAPublicExponent);
        } else if (i != 3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported algorithm: ");
            stringBuilder.append(this.mKeymasterAlgorithm);
            throw new ProviderException(stringBuilder.toString());
        }
    }

    private X509Certificate generateSelfSignedCertificate(PrivateKey privateKey, PublicKey publicKey) throws CertificateParsingException, IOException {
        String signatureAlgorithm = getCertificateSignatureAlgorithm(this.mKeymasterAlgorithm, this.mKeySizeBits, this.mSpec);
        if (signatureAlgorithm == null) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
        try {
            return generateSelfSignedCertificateWithValidSignature(privateKey, publicKey, signatureAlgorithm);
        } catch (Exception e) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
    }

    private X509Certificate generateSelfSignedCertificateWithValidSignature(PrivateKey privateKey, PublicKey publicKey, String signatureAlgorithm) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setPublicKey(publicKey);
        certGen.setSerialNumber(this.mSpec.getCertificateSerialNumber());
        certGen.setSubjectDN(this.mSpec.getCertificateSubject());
        certGen.setIssuerDN(this.mSpec.getCertificateSubject());
        certGen.setNotBefore(this.mSpec.getCertificateNotBefore());
        certGen.setNotAfter(this.mSpec.getCertificateNotAfter());
        certGen.setSignatureAlgorithm(signatureAlgorithm);
        return certGen.generate(privateKey);
    }

    /* JADX WARNING: Missing block: B:18:?, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:19:0x00e1, code skipped:
            r7 = move-exception;
     */
    /* JADX WARNING: Missing block: B:20:0x00e2, code skipped:
            r5.addSuppressed(r7);
     */
    private java.security.cert.X509Certificate generateSelfSignedCertificateWithFakeSignature(java.security.PublicKey r10) throws java.io.IOException, java.security.cert.CertificateParsingException {
        /*
        r9 = this;
        r0 = new com.android.org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
        r0.<init>();
        r1 = r9.mKeymasterAlgorithm;
        r2 = 1;
        if (r1 == r2) goto L_0x004e;
    L_0x000a:
        r2 = 3;
        if (r1 != r2) goto L_0x0035;
    L_0x000d:
        r1 = com.android.org.bouncycastle.asn1.x9.X9ObjectIdentifiers.ecdsa_with_SHA256;
        r2 = new com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
        r2.<init>(r1);
        r3 = new com.android.org.bouncycastle.asn1.ASN1EncodableVector;
        r3.<init>();
        r4 = new com.android.org.bouncycastle.asn1.DERInteger;
        r5 = 0;
        r4.<init>(r5);
        r3.add(r4);
        r4 = new com.android.org.bouncycastle.asn1.DERInteger;
        r4.<init>(r5);
        r3.add(r4);
        r4 = new com.android.org.bouncycastle.asn1.DERSequence;
        r4.<init>();
        r4 = r4.getEncoded();
        goto L_0x005a;
    L_0x0035:
        r1 = new java.security.ProviderException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unsupported key algorithm: ";
        r2.append(r3);
        r3 = r9.mKeymasterAlgorithm;
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x004e:
        r1 = com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.sha256WithRSAEncryption;
        r3 = new com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
        r4 = com.android.org.bouncycastle.asn1.DERNull.INSTANCE;
        r3.<init>(r1, r4);
        r4 = new byte[r2];
        r2 = r3;
    L_0x005a:
        r3 = new com.android.org.bouncycastle.asn1.ASN1InputStream;
        r5 = r10.getEncoded();
        r3.<init>(r5);
        r5 = r3.readObject();	 Catch:{ all -> 0x00da }
        r5 = com.android.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo.getInstance(r5);	 Catch:{ all -> 0x00da }
        r0.setSubjectPublicKeyInfo(r5);	 Catch:{ all -> 0x00da }
        r3.close();
        r3 = new com.android.org.bouncycastle.asn1.ASN1Integer;
        r5 = r9.mSpec;
        r5 = r5.getCertificateSerialNumber();
        r3.<init>(r5);
        r0.setSerialNumber(r3);
        r3 = new com.android.org.bouncycastle.jce.X509Principal;
        r5 = r9.mSpec;
        r5 = r5.getCertificateSubject();
        r5 = r5.getEncoded();
        r3.<init>(r5);
        r0.setSubject(r3);
        r0.setIssuer(r3);
        r5 = new com.android.org.bouncycastle.asn1.x509.Time;
        r6 = r9.mSpec;
        r6 = r6.getCertificateNotBefore();
        r5.<init>(r6);
        r0.setStartDate(r5);
        r5 = new com.android.org.bouncycastle.asn1.x509.Time;
        r6 = r9.mSpec;
        r6 = r6.getCertificateNotAfter();
        r5.<init>(r6);
        r0.setEndDate(r5);
        r0.setSignature(r2);
        r5 = r0.generateTBSCertificate();
        r6 = new com.android.org.bouncycastle.asn1.ASN1EncodableVector;
        r6.<init>();
        r6.add(r5);
        r6.add(r2);
        r7 = new com.android.org.bouncycastle.asn1.DERBitString;
        r7.<init>(r4);
        r6.add(r7);
        r7 = new com.android.org.bouncycastle.jce.provider.X509CertificateObject;
        r8 = new com.android.org.bouncycastle.asn1.DERSequence;
        r8.<init>(r6);
        r8 = com.android.org.bouncycastle.asn1.x509.Certificate.getInstance(r8);
        r7.<init>(r8);
        return r7;
    L_0x00da:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x00dc }
    L_0x00dc:
        r6 = move-exception;
        r3.close();	 Catch:{ all -> 0x00e1 }
        goto L_0x00e5;
    L_0x00e1:
        r7 = move-exception;
        r5.addSuppressed(r7);
    L_0x00e5:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.generateSelfSignedCertificateWithFakeSignature(java.security.PublicKey):java.security.cert.X509Certificate");
    }

    private static int getDefaultKeySize(int keymasterAlgorithm) {
        if (keymasterAlgorithm == 1) {
            return 2048;
        }
        if (keymasterAlgorithm == 3) {
            return 256;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported algorithm: ");
        stringBuilder.append(keymasterAlgorithm);
        throw new ProviderException(stringBuilder.toString());
    }

    private static void checkValidKeySize(int keymasterAlgorithm, int keySize, boolean isStrongBoxBacked) throws InvalidAlgorithmParameterException {
        if (keymasterAlgorithm != 1) {
            StringBuilder stringBuilder;
            if (keymasterAlgorithm != 3) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported algorithm: ");
                stringBuilder.append(keymasterAlgorithm);
                throw new ProviderException(stringBuilder.toString());
            } else if (isStrongBoxBacked && keySize != 256) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported StrongBox EC key size: ");
                stringBuilder.append(keySize);
                stringBuilder.append(" bits. Supported: 256");
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            } else if (!SUPPORTED_EC_NIST_CURVE_SIZES.contains(Integer.valueOf(keySize))) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported EC key size: ");
                stringBuilder.append(keySize);
                stringBuilder.append(" bits. Supported: ");
                stringBuilder.append(SUPPORTED_EC_NIST_CURVE_SIZES);
                throw new InvalidAlgorithmParameterException(stringBuilder.toString());
            }
        } else if (keySize < 512 || keySize > 8192) {
            throw new InvalidAlgorithmParameterException("RSA key size must be >= 512 and <= 8192");
        }
    }

    private static String getCertificateSignatureAlgorithm(int keymasterAlgorithm, int keySizeBits, KeyGenParameterSpec spec) {
        if ((spec.getPurposes() & 4) == 0 || spec.isUserAuthenticationRequired() || !spec.isDigestsSpecified()) {
            return null;
        }
        int bestDigestOutputSizeBits;
        int keymasterDigest;
        StringBuilder stringBuilder;
        if (keymasterAlgorithm != 1) {
            if (keymasterAlgorithm == 3) {
                int bestKeymasterDigest = -1;
                bestDigestOutputSizeBits = -1;
                for (Integer keymasterDigest2 : getAvailableKeymasterSignatureDigests(spec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests())) {
                    keymasterDigest = keymasterDigest2.intValue();
                    int outputSizeBits = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                    if (outputSizeBits == keySizeBits) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                        break;
                    } else if (bestKeymasterDigest == -1) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                    } else if (bestDigestOutputSizeBits < keySizeBits) {
                        if (outputSizeBits > bestDigestOutputSizeBits) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        }
                    } else if (outputSizeBits < bestDigestOutputSizeBits && outputSizeBits >= keySizeBits) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                    }
                }
                if (bestKeymasterDigest == -1) {
                    return null;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest));
                stringBuilder.append("WithECDSA");
                return stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported algorithm: ");
            stringBuilder.append(keymasterAlgorithm);
            throw new ProviderException(stringBuilder.toString());
        } else if (!ArrayUtils.contains(SignaturePadding.allToKeymaster(spec.getSignaturePaddings()), 5)) {
            return null;
        } else {
            bestDigestOutputSizeBits = keySizeBits - 240;
            int bestKeymasterDigest2 = -1;
            keymasterDigest = -1;
            for (Integer keymasterDigest3 : getAvailableKeymasterSignatureDigests(spec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests())) {
                int keymasterDigest4 = keymasterDigest3.intValue();
                int outputSizeBits2 = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest4);
                if (outputSizeBits2 <= bestDigestOutputSizeBits) {
                    if (bestKeymasterDigest2 == -1) {
                        bestKeymasterDigest2 = keymasterDigest4;
                        keymasterDigest = outputSizeBits2;
                    } else if (outputSizeBits2 > keymasterDigest) {
                        bestKeymasterDigest2 = keymasterDigest4;
                        keymasterDigest = outputSizeBits2;
                    }
                }
            }
            if (bestKeymasterDigest2 == -1) {
                return null;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest2));
            stringBuilder.append("WithRSA");
            return stringBuilder.toString();
        }
    }

    private static Set<Integer> getAvailableKeymasterSignatureDigests(String[] authorizedKeyDigests, String[] supportedSignatureDigests) {
        Set<Integer> authorizedKeymasterKeyDigests = new HashSet();
        int i = 0;
        for (int keymasterDigest : Digest.allToKeymaster(authorizedKeyDigests)) {
            authorizedKeymasterKeyDigests.add(Integer.valueOf(keymasterDigest));
        }
        Set<Integer> supportedKeymasterSignatureDigests = new HashSet();
        int[] allToKeymaster = Digest.allToKeymaster(supportedSignatureDigests);
        int length = allToKeymaster.length;
        while (i < length) {
            supportedKeymasterSignatureDigests.add(Integer.valueOf(allToKeymaster[i]));
            i++;
        }
        Set<Integer> result = new HashSet(supportedKeymasterSignatureDigests);
        result.retainAll(authorizedKeymasterKeyDigests);
        return result;
    }
}
