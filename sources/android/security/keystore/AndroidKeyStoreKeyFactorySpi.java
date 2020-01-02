package android.security.keystore;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.storage.VolumeInfo;
import android.security.Credentials;
import android.security.KeyStore;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AndroidKeyStoreKeyFactorySpi extends KeyFactorySpi {
    private final KeyStore mKeyStore = KeyStore.getInstance();

    /* Access modifiers changed, original: protected */
    public <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpecClass) throws InvalidKeySpecException {
        if (key != null) {
            String str = "Unsupported key type: ";
            StringBuilder stringBuilder;
            String str2;
            if (!(key instanceof AndroidKeyStorePrivateKey) && !(key instanceof AndroidKeyStorePublicKey)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(key.getClass().getName());
                stringBuilder.append(". This KeyFactory supports only Android Keystore asymmetric keys");
                throw new InvalidKeySpecException(stringBuilder.toString());
            } else if (keySpecClass == null) {
                throw new InvalidKeySpecException("keySpecClass == null");
            } else if (KeyInfo.class.equals(keySpecClass)) {
                if (key instanceof AndroidKeyStorePrivateKey) {
                    AndroidKeyStorePrivateKey keystorePrivateKey = (AndroidKeyStorePrivateKey) key;
                    str = keystorePrivateKey.getAlias();
                    str2 = Credentials.USER_PRIVATE_KEY;
                    if (str.startsWith(str2)) {
                        return AndroidKeyStoreSecretKeyFactorySpi.getKeyInfo(this.mKeyStore, str.substring(str2.length()), str, keystorePrivateKey.getUid());
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Invalid key alias: ");
                    stringBuilder2.append(str);
                    throw new InvalidKeySpecException(stringBuilder2.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(key.getClass().getName());
                stringBuilder.append(". KeyInfo can be obtained only for Android Keystore private keys");
                throw new InvalidKeySpecException(stringBuilder.toString());
            } else if (X509EncodedKeySpec.class.equals(keySpecClass)) {
                if (key instanceof AndroidKeyStorePublicKey) {
                    return new X509EncodedKeySpec(((AndroidKeyStorePublicKey) key).getEncoded());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(key.getClass().getName());
                stringBuilder.append(". X509EncodedKeySpec can be obtained only for Android Keystore public keys");
                throw new InvalidKeySpecException(stringBuilder.toString());
            } else if (!PKCS8EncodedKeySpec.class.equals(keySpecClass)) {
                boolean equals = RSAPublicKeySpec.class.equals(keySpecClass);
                str = " key";
                str2 = VolumeInfo.ID_PRIVATE_INTERNAL;
                String str3 = "public";
                String str4 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                StringBuilder stringBuilder3;
                if (equals) {
                    if (key instanceof AndroidKeyStoreRSAPublicKey) {
                        AndroidKeyStoreRSAPublicKey rsaKey = (AndroidKeyStoreRSAPublicKey) key;
                        return new RSAPublicKeySpec(rsaKey.getModulus(), rsaKey.getPublicExponent());
                    }
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Obtaining RSAPublicKeySpec not supported for ");
                    stringBuilder3.append(key.getAlgorithm());
                    stringBuilder3.append(str4);
                    if (!(key instanceof AndroidKeyStorePrivateKey)) {
                        str2 = str3;
                    }
                    stringBuilder3.append(str2);
                    stringBuilder3.append(str);
                    throw new InvalidKeySpecException(stringBuilder3.toString());
                } else if (!ECPublicKeySpec.class.equals(keySpecClass)) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("Unsupported key spec: ");
                    stringBuilder4.append(keySpecClass.getName());
                    throw new InvalidKeySpecException(stringBuilder4.toString());
                } else if (key instanceof AndroidKeyStoreECPublicKey) {
                    AndroidKeyStoreECPublicKey ecKey = (AndroidKeyStoreECPublicKey) key;
                    return new ECPublicKeySpec(ecKey.getW(), ecKey.getParams());
                } else {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Obtaining ECPublicKeySpec not supported for ");
                    stringBuilder3.append(key.getAlgorithm());
                    stringBuilder3.append(str4);
                    if (!(key instanceof AndroidKeyStorePrivateKey)) {
                        str2 = str3;
                    }
                    stringBuilder3.append(str2);
                    stringBuilder3.append(str);
                    throw new InvalidKeySpecException(stringBuilder3.toString());
                }
            } else if (key instanceof AndroidKeyStorePrivateKey) {
                throw new InvalidKeySpecException("Key material export of Android Keystore private keys is not supported");
            } else {
                throw new InvalidKeySpecException("Cannot export key material of public key in PKCS#8 format. Only X.509 format (X509EncodedKeySpec) supported for public keys.");
            }
        }
        throw new InvalidKeySpecException("key == null");
    }

    /* Access modifiers changed, original: protected */
    public PrivateKey engineGeneratePrivate(KeySpec spec) throws InvalidKeySpecException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with ");
        stringBuilder.append(KeyGenParameterSpec.class.getName());
        throw new InvalidKeySpecException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public PublicKey engineGeneratePublic(KeySpec spec) throws InvalidKeySpecException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with ");
        stringBuilder.append(KeyGenParameterSpec.class.getName());
        throw new InvalidKeySpecException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("key == null");
        } else if ((key instanceof AndroidKeyStorePrivateKey) || (key instanceof AndroidKeyStorePublicKey)) {
            return key;
        } else {
            throw new InvalidKeyException("To import a key into Android Keystore, use KeyStore.setEntry");
        }
    }
}
