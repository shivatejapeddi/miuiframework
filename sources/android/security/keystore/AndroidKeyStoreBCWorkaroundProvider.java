package android.security.keystore;

import android.os.SystemProperties;
import java.security.Provider;

class AndroidKeyStoreBCWorkaroundProvider extends Provider {
    private static final String DESEDE_SYSTEM_PROPERTY = "ro.hardware.keystore_desede";
    private static final String KEYSTORE_PRIVATE_KEY_CLASS_NAME = "android.security.keystore.AndroidKeyStorePrivateKey";
    private static final String KEYSTORE_PUBLIC_KEY_CLASS_NAME = "android.security.keystore.AndroidKeyStorePublicKey";
    private static final String KEYSTORE_SECRET_KEY_CLASS_NAME = "android.security.keystore.AndroidKeyStoreSecretKey";
    private static final String PACKAGE_NAME = "android.security.keystore";

    AndroidKeyStoreBCWorkaroundProvider() {
        super("AndroidKeyStoreBCWorkaround", 1.0d, "Android KeyStore security provider to work around Bouncy Castle");
        String str = KeyProperties.KEY_ALGORITHM_HMAC_SHA1;
        putMacImpl(str, "android.security.keystore.AndroidKeyStoreHmacSpi$HmacSHA1");
        put("Alg.Alias.Mac.1.2.840.113549.2.7", str);
        put("Alg.Alias.Mac.HMAC-SHA1", str);
        put("Alg.Alias.Mac.HMAC/SHA1", str);
        str = KeyProperties.KEY_ALGORITHM_HMAC_SHA224;
        putMacImpl(str, "android.security.keystore.AndroidKeyStoreHmacSpi$HmacSHA224");
        put("Alg.Alias.Mac.1.2.840.113549.2.9", str);
        put("Alg.Alias.Mac.HMAC-SHA224", str);
        put("Alg.Alias.Mac.HMAC/SHA224", str);
        str = KeyProperties.KEY_ALGORITHM_HMAC_SHA256;
        putMacImpl(str, "android.security.keystore.AndroidKeyStoreHmacSpi$HmacSHA256");
        put("Alg.Alias.Mac.1.2.840.113549.2.9", str);
        put("Alg.Alias.Mac.HMAC-SHA256", str);
        put("Alg.Alias.Mac.HMAC/SHA256", str);
        str = KeyProperties.KEY_ALGORITHM_HMAC_SHA384;
        putMacImpl(str, "android.security.keystore.AndroidKeyStoreHmacSpi$HmacSHA384");
        put("Alg.Alias.Mac.1.2.840.113549.2.10", str);
        put("Alg.Alias.Mac.HMAC-SHA384", str);
        put("Alg.Alias.Mac.HMAC/SHA384", str);
        str = KeyProperties.KEY_ALGORITHM_HMAC_SHA512;
        putMacImpl(str, "android.security.keystore.AndroidKeyStoreHmacSpi$HmacSHA512");
        put("Alg.Alias.Mac.1.2.840.113549.2.11", str);
        put("Alg.Alias.Mac.HMAC-SHA512", str);
        put("Alg.Alias.Mac.HMAC/SHA512", str);
        putSymmetricCipherImpl("AES/ECB/NoPadding", "android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi$ECB$NoPadding");
        putSymmetricCipherImpl("AES/ECB/PKCS7Padding", "android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi$ECB$PKCS7Padding");
        putSymmetricCipherImpl("AES/CBC/NoPadding", "android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi$CBC$NoPadding");
        putSymmetricCipherImpl("AES/CBC/PKCS7Padding", "android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi$CBC$PKCS7Padding");
        putSymmetricCipherImpl("AES/CTR/NoPadding", "android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi$CTR$NoPadding");
        if ("true".equals(SystemProperties.get(DESEDE_SYSTEM_PROPERTY))) {
            putSymmetricCipherImpl("DESede/CBC/NoPadding", "android.security.keystore.AndroidKeyStore3DESCipherSpi$CBC$NoPadding");
            putSymmetricCipherImpl("DESede/CBC/PKCS7Padding", "android.security.keystore.AndroidKeyStore3DESCipherSpi$CBC$PKCS7Padding");
            putSymmetricCipherImpl("DESede/ECB/NoPadding", "android.security.keystore.AndroidKeyStore3DESCipherSpi$ECB$NoPadding");
            putSymmetricCipherImpl("DESede/ECB/PKCS7Padding", "android.security.keystore.AndroidKeyStore3DESCipherSpi$ECB$PKCS7Padding");
        }
        putSymmetricCipherImpl("AES/GCM/NoPadding", "android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi$GCM$NoPadding");
        putAsymmetricCipherImpl("RSA/ECB/NoPadding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$NoPadding");
        put("Alg.Alias.Cipher.RSA/None/NoPadding", "RSA/ECB/NoPadding");
        putAsymmetricCipherImpl("RSA/ECB/PKCS1Padding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$PKCS1Padding");
        put("Alg.Alias.Cipher.RSA/None/PKCS1Padding", "RSA/ECB/PKCS1Padding");
        putAsymmetricCipherImpl("RSA/ECB/OAEPPadding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA1AndMGF1Padding");
        put("Alg.Alias.Cipher.RSA/None/OAEPPadding", "RSA/ECB/OAEPPadding");
        putAsymmetricCipherImpl("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA1AndMGF1Padding");
        put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-1AndMGF1Padding", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        putAsymmetricCipherImpl("RSA/ECB/OAEPWithSHA-224AndMGF1Padding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA224AndMGF1Padding");
        str = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
        put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-224AndMGF1Padding", str);
        putAsymmetricCipherImpl(str, "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA256AndMGF1Padding");
        put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-256AndMGF1Padding", str);
        putAsymmetricCipherImpl("RSA/ECB/OAEPWithSHA-384AndMGF1Padding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA384AndMGF1Padding");
        put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-384AndMGF1Padding", "RSA/ECB/OAEPWithSHA-384AndMGF1Padding");
        putAsymmetricCipherImpl("RSA/ECB/OAEPWithSHA-512AndMGF1Padding", "android.security.keystore.AndroidKeyStoreRSACipherSpi$OAEPWithSHA512AndMGF1Padding");
        put("Alg.Alias.Cipher.RSA/None/OAEPWithSHA-512AndMGF1Padding", "RSA/ECB/OAEPWithSHA-512AndMGF1Padding");
        putSignatureImpl("NONEwithRSA", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$NONEWithPKCS1Padding");
        str = "MD5withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$MD5WithPKCS1Padding");
        put("Alg.Alias.Signature.MD5WithRSAEncryption", str);
        put("Alg.Alias.Signature.MD5/RSA", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.4", str);
        put("Alg.Alias.Signature.1.2.840.113549.2.5with1.2.840.113549.1.1.1", str);
        str = "SHA1withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA1WithPKCS1Padding");
        put("Alg.Alias.Signature.SHA1WithRSAEncryption", str);
        put("Alg.Alias.Signature.SHA1/RSA", str);
        put("Alg.Alias.Signature.SHA-1/RSA", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.5", str);
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.1", str);
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.5", str);
        put("Alg.Alias.Signature.1.3.14.3.2.29", str);
        str = "SHA224withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA224WithPKCS1Padding");
        put("Alg.Alias.Signature.SHA224WithRSAEncryption", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.11", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.113549.1.1.1", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.113549.1.1.11", str);
        str = "SHA256withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA256WithPKCS1Padding");
        put("Alg.Alias.Signature.SHA256WithRSAEncryption", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.11", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.113549.1.1.1", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.113549.1.1.11", str);
        str = "SHA384withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA384WithPKCS1Padding");
        put("Alg.Alias.Signature.SHA384WithRSAEncryption", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.12", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.2with1.2.840.113549.1.1.1", str);
        str = "SHA512withRSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA512WithPKCS1Padding");
        put("Alg.Alias.Signature.SHA512WithRSAEncryption", str);
        put("Alg.Alias.Signature.1.2.840.113549.1.1.13", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.3with1.2.840.113549.1.1.1", str);
        putSignatureImpl("SHA1withRSA/PSS", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA1WithPSSPadding");
        putSignatureImpl("SHA224withRSA/PSS", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA224WithPSSPadding");
        putSignatureImpl("SHA256withRSA/PSS", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA256WithPSSPadding");
        putSignatureImpl("SHA384withRSA/PSS", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA384WithPSSPadding");
        putSignatureImpl("SHA512withRSA/PSS", "android.security.keystore.AndroidKeyStoreRSASignatureSpi$SHA512WithPSSPadding");
        putSignatureImpl("NONEwithECDSA", "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$NONE");
        str = "SHA1withECDSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$SHA1");
        put("Alg.Alias.Signature.ECDSA", str);
        put("Alg.Alias.Signature.ECDSAwithSHA1", str);
        put("Alg.Alias.Signature.1.2.840.10045.4.1", str);
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10045.2.1", str);
        str = "SHA224withECDSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$SHA224");
        put("Alg.Alias.Signature.1.2.840.10045.4.3.1", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.4with1.2.840.10045.2.1", str);
        str = "SHA256withECDSA";
        putSignatureImpl(str, "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$SHA256");
        put("Alg.Alias.Signature.1.2.840.10045.4.3.2", str);
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.1with1.2.840.10045.2.1", str);
        putSignatureImpl("SHA384withECDSA", "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$SHA384");
        put("Alg.Alias.Signature.1.2.840.10045.4.3.3", "SHA384withECDSA");
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.2with1.2.840.10045.2.1", "SHA384withECDSA");
        putSignatureImpl("SHA512withECDSA", "android.security.keystore.AndroidKeyStoreECDSASignatureSpi$SHA512");
        put("Alg.Alias.Signature.1.2.840.10045.4.3.4", "SHA512withECDSA");
        put("Alg.Alias.Signature.2.16.840.1.101.3.4.2.3with1.2.840.10045.2.1", "SHA512withECDSA");
    }

    private void putMacImpl(String algorithm, String implClass) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "Mac.";
        stringBuilder.append(str);
        stringBuilder.append(algorithm);
        put(stringBuilder.toString(), implClass);
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(algorithm);
        stringBuilder.append(" SupportedKeyClasses");
        put(stringBuilder.toString(), KEYSTORE_SECRET_KEY_CLASS_NAME);
    }

    private void putSymmetricCipherImpl(String transformation, String implClass) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "Cipher.";
        stringBuilder.append(str);
        stringBuilder.append(transformation);
        put(stringBuilder.toString(), implClass);
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(transformation);
        stringBuilder.append(" SupportedKeyClasses");
        put(stringBuilder.toString(), KEYSTORE_SECRET_KEY_CLASS_NAME);
    }

    private void putAsymmetricCipherImpl(String transformation, String implClass) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "Cipher.";
        stringBuilder.append(str);
        stringBuilder.append(transformation);
        put(stringBuilder.toString(), implClass);
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(transformation);
        stringBuilder.append(" SupportedKeyClasses");
        put(stringBuilder.toString(), "android.security.keystore.AndroidKeyStorePrivateKey|android.security.keystore.AndroidKeyStorePublicKey");
    }

    private void putSignatureImpl(String algorithm, String implClass) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "Signature.";
        stringBuilder.append(str);
        stringBuilder.append(algorithm);
        put(stringBuilder.toString(), implClass);
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(algorithm);
        stringBuilder.append(" SupportedKeyClasses");
        put(stringBuilder.toString(), "android.security.keystore.AndroidKeyStorePrivateKey|android.security.keystore.AndroidKeyStorePublicKey");
    }

    public static String[] getSupportedEcdsaSignatureDigests() {
        return new String[]{KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512};
    }

    public static String[] getSupportedRsaSignatureWithPkcs1PaddingDigests() {
        return new String[]{KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512};
    }
}
