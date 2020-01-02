package android.security.keystore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Locale;
import libcore.util.EmptyArray;

public abstract class KeyProperties {
    public static final String BLOCK_MODE_CBC = "CBC";
    public static final String BLOCK_MODE_CTR = "CTR";
    public static final String BLOCK_MODE_ECB = "ECB";
    public static final String BLOCK_MODE_GCM = "GCM";
    public static final String DIGEST_MD5 = "MD5";
    public static final String DIGEST_NONE = "NONE";
    public static final String DIGEST_SHA1 = "SHA-1";
    public static final String DIGEST_SHA224 = "SHA-224";
    public static final String DIGEST_SHA256 = "SHA-256";
    public static final String DIGEST_SHA384 = "SHA-384";
    public static final String DIGEST_SHA512 = "SHA-512";
    public static final String ENCRYPTION_PADDING_NONE = "NoPadding";
    public static final String ENCRYPTION_PADDING_PKCS7 = "PKCS7Padding";
    public static final String ENCRYPTION_PADDING_RSA_OAEP = "OAEPPadding";
    public static final String ENCRYPTION_PADDING_RSA_PKCS1 = "PKCS1Padding";
    @Deprecated
    public static final String KEY_ALGORITHM_3DES = "DESede";
    public static final String KEY_ALGORITHM_AES = "AES";
    public static final String KEY_ALGORITHM_EC = "EC";
    public static final String KEY_ALGORITHM_HMAC_SHA1 = "HmacSHA1";
    public static final String KEY_ALGORITHM_HMAC_SHA224 = "HmacSHA224";
    public static final String KEY_ALGORITHM_HMAC_SHA256 = "HmacSHA256";
    public static final String KEY_ALGORITHM_HMAC_SHA384 = "HmacSHA384";
    public static final String KEY_ALGORITHM_HMAC_SHA512 = "HmacSHA512";
    public static final String KEY_ALGORITHM_RSA = "RSA";
    public static final int ORIGIN_GENERATED = 1;
    public static final int ORIGIN_IMPORTED = 2;
    public static final int ORIGIN_SECURELY_IMPORTED = 8;
    public static final int ORIGIN_UNKNOWN = 4;
    public static final int PURPOSE_DECRYPT = 2;
    public static final int PURPOSE_ENCRYPT = 1;
    public static final int PURPOSE_SIGN = 4;
    public static final int PURPOSE_VERIFY = 8;
    public static final int PURPOSE_WRAP_KEY = 32;
    public static final String SIGNATURE_PADDING_RSA_PKCS1 = "PKCS1";
    public static final String SIGNATURE_PADDING_RSA_PSS = "PSS";

    public static abstract class BlockMode {
        private BlockMode() {
        }

        public static int toKeymaster(String blockMode) {
            if (KeyProperties.BLOCK_MODE_ECB.equalsIgnoreCase(blockMode)) {
                return 1;
            }
            if (KeyProperties.BLOCK_MODE_CBC.equalsIgnoreCase(blockMode)) {
                return 2;
            }
            if (KeyProperties.BLOCK_MODE_CTR.equalsIgnoreCase(blockMode)) {
                return 3;
            }
            if (KeyProperties.BLOCK_MODE_GCM.equalsIgnoreCase(blockMode)) {
                return 32;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported block mode: ");
            stringBuilder.append(blockMode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static String fromKeymaster(int blockMode) {
            if (blockMode == 1) {
                return KeyProperties.BLOCK_MODE_ECB;
            }
            if (blockMode == 2) {
                return KeyProperties.BLOCK_MODE_CBC;
            }
            if (blockMode == 3) {
                return KeyProperties.BLOCK_MODE_CTR;
            }
            if (blockMode == 32) {
                return KeyProperties.BLOCK_MODE_GCM;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported block mode: ");
            stringBuilder.append(blockMode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static String[] allFromKeymaster(Collection<Integer> blockModes) {
            if (blockModes == null || blockModes.isEmpty()) {
                return EmptyArray.STRING;
            }
            String[] result = new String[blockModes.size()];
            int offset = 0;
            for (Integer blockMode : blockModes) {
                result[offset] = fromKeymaster(blockMode.intValue());
                offset++;
            }
            return result;
        }

        public static int[] allToKeymaster(String[] blockModes) {
            if (blockModes == null || blockModes.length == 0) {
                return EmptyArray.INT;
            }
            int[] result = new int[blockModes.length];
            for (int i = 0; i < blockModes.length; i++) {
                result[i] = toKeymaster(blockModes[i]);
            }
            return result;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BlockModeEnum {
    }

    public static abstract class Digest {
        private Digest() {
        }

        public static int toKeymaster(java.lang.String r9) {
            /*
            r0 = java.util.Locale.US;
            r0 = r9.toUpperCase(r0);
            r1 = r0.hashCode();
            r2 = 1;
            r3 = 0;
            r4 = 6;
            r5 = 5;
            r6 = 4;
            r7 = 3;
            r8 = 2;
            switch(r1) {
                case -1523887821: goto L_0x0051;
                case -1523887726: goto L_0x0047;
                case -1523886674: goto L_0x003d;
                case -1523884971: goto L_0x0033;
                case 76158: goto L_0x0029;
                case 2402104: goto L_0x001f;
                case 78861104: goto L_0x0015;
                default: goto L_0x0014;
            };
        L_0x0014:
            goto L_0x005b;
        L_0x0015:
            r1 = "SHA-1";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x001d:
            r0 = r3;
            goto L_0x005c;
        L_0x001f:
            r1 = "NONE";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0027:
            r0 = r5;
            goto L_0x005c;
        L_0x0029:
            r1 = "MD5";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0031:
            r0 = r4;
            goto L_0x005c;
        L_0x0033:
            r1 = "SHA-512";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x003b:
            r0 = r6;
            goto L_0x005c;
        L_0x003d:
            r1 = "SHA-384";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0045:
            r0 = r7;
            goto L_0x005c;
        L_0x0047:
            r1 = "SHA-256";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x004f:
            r0 = r8;
            goto L_0x005c;
        L_0x0051:
            r1 = "SHA-224";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0059:
            r0 = r2;
            goto L_0x005c;
        L_0x005b:
            r0 = -1;
        L_0x005c:
            switch(r0) {
                case 0: goto L_0x007c;
                case 1: goto L_0x007b;
                case 2: goto L_0x007a;
                case 3: goto L_0x0079;
                case 4: goto L_0x0078;
                case 5: goto L_0x0077;
                case 6: goto L_0x0076;
                default: goto L_0x005f;
            };
        L_0x005f:
            r0 = new java.lang.IllegalArgumentException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Unsupported digest algorithm: ";
            r1.append(r2);
            r1.append(r9);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0076:
            return r2;
        L_0x0077:
            return r3;
        L_0x0078:
            return r4;
        L_0x0079:
            return r5;
        L_0x007a:
            return r6;
        L_0x007b:
            return r7;
        L_0x007c:
            return r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.KeyProperties$Digest.toKeymaster(java.lang.String):int");
        }

        public static String fromKeymaster(int digest) {
            switch (digest) {
                case 0:
                    return KeyProperties.DIGEST_NONE;
                case 1:
                    return KeyProperties.DIGEST_MD5;
                case 2:
                    return KeyProperties.DIGEST_SHA1;
                case 3:
                    return KeyProperties.DIGEST_SHA224;
                case 4:
                    return KeyProperties.DIGEST_SHA256;
                case 5:
                    return KeyProperties.DIGEST_SHA384;
                case 6:
                    return KeyProperties.DIGEST_SHA512;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported digest algorithm: ");
                    stringBuilder.append(digest);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public static String fromKeymasterToSignatureAlgorithmDigest(int digest) {
            switch (digest) {
                case 0:
                    return KeyProperties.DIGEST_NONE;
                case 1:
                    return KeyProperties.DIGEST_MD5;
                case 2:
                    return "SHA1";
                case 3:
                    return "SHA224";
                case 4:
                    return "SHA256";
                case 5:
                    return "SHA384";
                case 6:
                    return "SHA512";
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported digest algorithm: ");
                    stringBuilder.append(digest);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public static String[] allFromKeymaster(Collection<Integer> digests) {
            if (digests.isEmpty()) {
                return EmptyArray.STRING;
            }
            String[] result = new String[digests.size()];
            int offset = 0;
            for (Integer digest : digests) {
                result[offset] = fromKeymaster(digest.intValue());
                offset++;
            }
            return result;
        }

        public static int[] allToKeymaster(String[] digests) {
            if (digests == null || digests.length == 0) {
                return EmptyArray.INT;
            }
            int[] result = new int[digests.length];
            int offset = 0;
            for (String digest : digests) {
                result[offset] = toKeymaster(digest);
                offset++;
            }
            return result;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DigestEnum {
    }

    public static abstract class EncryptionPadding {
        private EncryptionPadding() {
        }

        public static int toKeymaster(String padding) {
            if (KeyProperties.ENCRYPTION_PADDING_NONE.equalsIgnoreCase(padding)) {
                return 1;
            }
            if (KeyProperties.ENCRYPTION_PADDING_PKCS7.equalsIgnoreCase(padding)) {
                return 64;
            }
            if (KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1.equalsIgnoreCase(padding)) {
                return 4;
            }
            if (KeyProperties.ENCRYPTION_PADDING_RSA_OAEP.equalsIgnoreCase(padding)) {
                return 2;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported encryption padding scheme: ");
            stringBuilder.append(padding);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static String fromKeymaster(int padding) {
            if (padding == 1) {
                return KeyProperties.ENCRYPTION_PADDING_NONE;
            }
            if (padding == 2) {
                return KeyProperties.ENCRYPTION_PADDING_RSA_OAEP;
            }
            if (padding == 4) {
                return KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1;
            }
            if (padding == 64) {
                return KeyProperties.ENCRYPTION_PADDING_PKCS7;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported encryption padding: ");
            stringBuilder.append(padding);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static int[] allToKeymaster(String[] paddings) {
            if (paddings == null || paddings.length == 0) {
                return EmptyArray.INT;
            }
            int[] result = new int[paddings.length];
            for (int i = 0; i < paddings.length; i++) {
                result[i] = toKeymaster(paddings[i]);
            }
            return result;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EncryptionPaddingEnum {
    }

    public static abstract class KeyAlgorithm {
        private KeyAlgorithm() {
        }

        public static int toKeymasterAsymmetricKeyAlgorithm(String algorithm) {
            if (KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(algorithm)) {
                return 3;
            }
            if (KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(algorithm)) {
                return 1;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported key algorithm: ");
            stringBuilder.append(algorithm);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static String fromKeymasterAsymmetricKeyAlgorithm(int keymasterAlgorithm) {
            if (keymasterAlgorithm == 1) {
                return KeyProperties.KEY_ALGORITHM_RSA;
            }
            if (keymasterAlgorithm == 3) {
                return KeyProperties.KEY_ALGORITHM_EC;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported key algorithm: ");
            stringBuilder.append(keymasterAlgorithm);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static int toKeymasterSecretKeyAlgorithm(String algorithm) {
            if (KeyProperties.KEY_ALGORITHM_AES.equalsIgnoreCase(algorithm)) {
                return 32;
            }
            if (KeyProperties.KEY_ALGORITHM_3DES.equalsIgnoreCase(algorithm)) {
                return 33;
            }
            if (algorithm.toUpperCase(Locale.US).startsWith("HMAC")) {
                return 128;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported secret key algorithm: ");
            stringBuilder.append(algorithm);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static String fromKeymasterSecretKeyAlgorithm(int keymasterAlgorithm, int keymasterDigest) {
            if (keymasterAlgorithm == 32) {
                return KeyProperties.KEY_ALGORITHM_AES;
            }
            if (keymasterAlgorithm == 33) {
                return KeyProperties.KEY_ALGORITHM_3DES;
            }
            StringBuilder stringBuilder;
            if (keymasterAlgorithm != 128) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported key algorithm: ");
                stringBuilder.append(keymasterAlgorithm);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (keymasterDigest == 2) {
                return KeyProperties.KEY_ALGORITHM_HMAC_SHA1;
            } else {
                if (keymasterDigest == 3) {
                    return KeyProperties.KEY_ALGORITHM_HMAC_SHA224;
                }
                if (keymasterDigest == 4) {
                    return KeyProperties.KEY_ALGORITHM_HMAC_SHA256;
                }
                if (keymasterDigest == 5) {
                    return KeyProperties.KEY_ALGORITHM_HMAC_SHA384;
                }
                if (keymasterDigest == 6) {
                    return KeyProperties.KEY_ALGORITHM_HMAC_SHA512;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported HMAC digest: ");
                stringBuilder.append(Digest.fromKeymaster(keymasterDigest));
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public static int toKeymasterDigest(String algorithm) {
            String algorithmUpper = algorithm.toUpperCase(Locale.US);
            String str = "HMAC";
            int i = -1;
            if (!algorithmUpper.startsWith(str)) {
                return -1;
            }
            str = algorithmUpper.substring(str.length());
            switch (str.hashCode()) {
                case -1850268184:
                    if (str.equals("SHA224")) {
                        i = 1;
                        break;
                    }
                    break;
                case -1850268089:
                    if (str.equals("SHA256")) {
                        i = 2;
                        break;
                    }
                    break;
                case -1850267037:
                    if (str.equals("SHA384")) {
                        i = 3;
                        break;
                    }
                    break;
                case -1850265334:
                    if (str.equals("SHA512")) {
                        i = 4;
                        break;
                    }
                    break;
                case 2543909:
                    if (str.equals("SHA1")) {
                        i = 0;
                        break;
                    }
                    break;
            }
            if (i == 0) {
                return 2;
            }
            if (i == 1) {
                return 3;
            }
            if (i == 2) {
                return 4;
            }
            if (i == 3) {
                return 5;
            }
            if (i == 4) {
                return 6;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported HMAC digest: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyAlgorithmEnum {
    }

    public static abstract class Origin {
        private Origin() {
        }

        public static int fromKeymaster(int origin) {
            if (origin == 0) {
                return 1;
            }
            if (origin == 2) {
                return 2;
            }
            if (origin == 3) {
                return 4;
            }
            if (origin == 4) {
                return 8;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown origin: ");
            stringBuilder.append(origin);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OriginEnum {
    }

    public static abstract class Purpose {
        private Purpose() {
        }

        public static int toKeymaster(int purpose) {
            if (purpose == 1) {
                return 0;
            }
            if (purpose == 2) {
                return 1;
            }
            if (purpose == 4) {
                return 2;
            }
            if (purpose == 8) {
                return 3;
            }
            if (purpose == 32) {
                return 5;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown purpose: ");
            stringBuilder.append(purpose);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static int fromKeymaster(int purpose) {
            if (purpose == 0) {
                return 1;
            }
            if (purpose == 1) {
                return 2;
            }
            if (purpose == 2) {
                return 4;
            }
            if (purpose == 3) {
                return 8;
            }
            if (purpose == 5) {
                return 32;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown purpose: ");
            stringBuilder.append(purpose);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public static int[] allToKeymaster(int purposes) {
            int[] result = KeyProperties.getSetFlags(purposes);
            for (int i = 0; i < result.length; i++) {
                result[i] = toKeymaster(result[i]);
            }
            return result;
        }

        public static int allFromKeymaster(Collection<Integer> purposes) {
            int result = 0;
            for (Integer keymasterPurpose : purposes) {
                result |= fromKeymaster(keymasterPurpose.intValue());
            }
            return result;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PurposeEnum {
    }

    static abstract class SignaturePadding {
        private SignaturePadding() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0048  */
        static int toKeymaster(java.lang.String r4) {
            /*
            r0 = java.util.Locale.US;
            r0 = r4.toUpperCase(r0);
            r1 = r0.hashCode();
            r2 = 79536; // 0x136b0 float:1.11454E-40 double:3.9296E-319;
            r3 = 1;
            if (r1 == r2) goto L_0x0020;
        L_0x0010:
            r2 = 76183014; // 0x48a75e6 float:3.2551917E-36 double:3.763941E-316;
            if (r1 == r2) goto L_0x0016;
        L_0x0015:
            goto L_0x002a;
        L_0x0016:
            r1 = "PKCS1";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0015;
        L_0x001e:
            r0 = 0;
            goto L_0x002b;
        L_0x0020:
            r1 = "PSS";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0015;
        L_0x0028:
            r0 = r3;
            goto L_0x002b;
        L_0x002a:
            r0 = -1;
        L_0x002b:
            if (r0 == 0) goto L_0x0048;
        L_0x002d:
            if (r0 != r3) goto L_0x0031;
        L_0x002f:
            r0 = 3;
            return r0;
        L_0x0031:
            r0 = new java.lang.IllegalArgumentException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Unsupported signature padding scheme: ";
            r1.append(r2);
            r1.append(r4);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0048:
            r0 = 5;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.KeyProperties$SignaturePadding.toKeymaster(java.lang.String):int");
        }

        static String fromKeymaster(int padding) {
            if (padding == 3) {
                return KeyProperties.SIGNATURE_PADDING_RSA_PSS;
            }
            if (padding == 5) {
                return KeyProperties.SIGNATURE_PADDING_RSA_PKCS1;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported signature padding: ");
            stringBuilder.append(padding);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        static int[] allToKeymaster(String[] paddings) {
            if (paddings == null || paddings.length == 0) {
                return EmptyArray.INT;
            }
            int[] result = new int[paddings.length];
            for (int i = 0; i < paddings.length; i++) {
                result[i] = toKeymaster(paddings[i]);
            }
            return result;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SignaturePaddingEnum {
    }

    private KeyProperties() {
    }

    private static int[] getSetFlags(int flags) {
        if (flags == 0) {
            return EmptyArray.INT;
        }
        int[] result = new int[getSetBitCount(flags)];
        int resultOffset = 0;
        int flag = 1;
        while (flags != 0) {
            if ((flags & 1) != 0) {
                result[resultOffset] = flag;
                resultOffset++;
            }
            flags >>>= 1;
            flag <<= 1;
        }
        return result;
    }

    private static int getSetBitCount(int value) {
        if (value == 0) {
            return 0;
        }
        int result = 0;
        while (value != 0) {
            if ((value & 1) != 0) {
                result++;
            }
            value >>>= 1;
        }
        return result;
    }
}
