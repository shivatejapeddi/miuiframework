package android.security.keystore;

import android.security.Credentials;
import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.BlockMode;
import android.security.keystore.KeyProperties.Digest;
import android.security.keystore.KeyProperties.EncryptionPadding;
import android.security.keystore.KeyProperties.KeyAlgorithm;
import android.security.keystore.KeyProperties.Purpose;
import android.security.keystore.KeyProtection.Builder;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore.Entry;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import libcore.util.EmptyArray;

public class AndroidKeyStoreSpi extends KeyStoreSpi {
    public static final String NAME = "AndroidKeyStore";
    private KeyStore mKeyStore;
    private int mUid = -1;

    static class KeyStoreX509Certificate extends DelegatingX509Certificate {
        private final String mPrivateKeyAlias;
        private final int mPrivateKeyUid;

        KeyStoreX509Certificate(String privateKeyAlias, int privateKeyUid, X509Certificate delegate) {
            super(delegate);
            this.mPrivateKeyAlias = privateKeyAlias;
            this.mPrivateKeyUid = privateKeyUid;
        }

        public PublicKey getPublicKey() {
            PublicKey original = super.getPublicKey();
            return AndroidKeyStoreProvider.getAndroidKeyStorePublicKey(this.mPrivateKeyAlias, this.mPrivateKeyUid, original.getAlgorithm(), original.getEncoded());
        }
    }

    public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        String userKeyAlias = new StringBuilder();
        userKeyAlias.append(Credentials.USER_PRIVATE_KEY);
        userKeyAlias.append(alias);
        userKeyAlias = userKeyAlias.toString();
        if (!this.mKeyStore.contains(userKeyAlias, this.mUid)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.USER_SECRET_KEY);
            stringBuilder.append(alias);
            userKeyAlias = stringBuilder.toString();
            if (!this.mKeyStore.contains(userKeyAlias, this.mUid)) {
                return null;
            }
        }
        try {
            return AndroidKeyStoreProvider.loadAndroidKeyStoreKeyFromKeystore(this.mKeyStore, userKeyAlias, this.mUid);
        } catch (KeyPermanentlyInvalidatedException e) {
            throw new UnrecoverableKeyException(e.getMessage());
        }
    }

    public Certificate[] engineGetCertificateChain(String alias) {
        if (alias != null) {
            X509Certificate leaf = (X509Certificate) engineGetCertificate(alias);
            if (leaf == null) {
                return null;
            }
            Certificate[] caList;
            byte[] caBytes = this.mKeyStore;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.CA_CERTIFICATE);
            stringBuilder.append(alias);
            caBytes = caBytes.get(stringBuilder.toString(), this.mUid, true);
            if (caBytes != null) {
                Collection<X509Certificate> caChain = toCertificates(caBytes);
                caList = new Certificate[(caChain.size() + 1)];
                int i = 1;
                for (Certificate certificate : caChain) {
                    int i2 = i + 1;
                    caList[i] = certificate;
                    i = i2;
                }
            } else {
                caList = new Certificate[1];
            }
            caList[0] = leaf;
            return caList;
        }
        throw new NullPointerException("alias == null");
    }

    public Certificate engineGetCertificate(String alias) {
        if (alias != null) {
            byte[] encodedCert = this.mKeyStore;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.USER_CERTIFICATE);
            stringBuilder.append(alias);
            encodedCert = encodedCert.get(stringBuilder.toString(), this.mUid);
            if (encodedCert != null) {
                return getCertificateForPrivateKeyEntry(alias, encodedCert);
            }
            KeyStore keyStore = this.mKeyStore;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Credentials.CA_CERTIFICATE);
            stringBuilder2.append(alias);
            encodedCert = keyStore.get(stringBuilder2.toString(), this.mUid);
            if (encodedCert != null) {
                return getCertificateForTrustedCertificateEntry(encodedCert);
            }
            return null;
        }
        throw new NullPointerException("alias == null");
    }

    private Certificate getCertificateForTrustedCertificateEntry(byte[] encodedCert) {
        return toCertificate(encodedCert);
    }

    private Certificate getCertificateForPrivateKeyEntry(String alias, byte[] encodedCert) {
        X509Certificate cert = toCertificate(encodedCert);
        if (cert == null) {
            return null;
        }
        String privateKeyAlias = new StringBuilder();
        privateKeyAlias.append(Credentials.USER_PRIVATE_KEY);
        privateKeyAlias.append(alias);
        privateKeyAlias = privateKeyAlias.toString();
        if (this.mKeyStore.contains(privateKeyAlias, this.mUid)) {
            return wrapIntoKeyStoreCertificate(privateKeyAlias, this.mUid, cert);
        }
        return cert;
    }

    private static KeyStoreX509Certificate wrapIntoKeyStoreCertificate(String privateKeyAlias, int uid, X509Certificate certificate) {
        return certificate != null ? new KeyStoreX509Certificate(privateKeyAlias, uid, certificate) : null;
    }

    private static X509Certificate toCertificate(byte[] bytes) {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            Log.w("AndroidKeyStore", "Couldn't parse certificate in keystore", e);
            return null;
        }
    }

    private static Collection<X509Certificate> toCertificates(byte[] bytes) {
        try {
            return CertificateFactory.getInstance("X.509").generateCertificates(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            Log.w("AndroidKeyStore", "Couldn't parse certificates in keystore", e);
            return new ArrayList();
        }
    }

    private Date getModificationDate(String alias) {
        long epochMillis = this.mKeyStore.getmtime(alias, this.mUid);
        if (epochMillis == -1) {
            return null;
        }
        return new Date(epochMillis);
    }

    public Date engineGetCreationDate(String alias) {
        if (alias != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.USER_PRIVATE_KEY);
            stringBuilder.append(alias);
            Date d = getModificationDate(stringBuilder.toString());
            if (d != null) {
                return d;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Credentials.USER_SECRET_KEY);
            stringBuilder2.append(alias);
            d = getModificationDate(stringBuilder2.toString());
            if (d != null) {
                return d;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Credentials.USER_CERTIFICATE);
            stringBuilder2.append(alias);
            d = getModificationDate(stringBuilder2.toString());
            if (d != null) {
                return d;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Credentials.CA_CERTIFICATE);
            stringBuilder2.append(alias);
            return getModificationDate(stringBuilder2.toString());
        }
        throw new NullPointerException("alias == null");
    }

    public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) throws KeyStoreException {
        if (password != null && password.length > 0) {
            throw new KeyStoreException("entries cannot be protected with passwords");
        } else if (key instanceof PrivateKey) {
            setPrivateKeyEntry(alias, (PrivateKey) key, chain, null);
        } else if (key instanceof SecretKey) {
            setSecretKeyEntry(alias, (SecretKey) key, null);
        } else {
            throw new KeyStoreException("Only PrivateKey and SecretKey are supported");
        }
    }

    private static KeyProtection getLegacyKeyProtectionParameter(PrivateKey key) throws KeyStoreException {
        Builder specBuilder;
        String keyAlgorithm = key.getAlgorithm();
        if (KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyAlgorithm)) {
            specBuilder = new Builder(12);
            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
        } else if (KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyAlgorithm)) {
            specBuilder = new Builder(15);
            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE, KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
            specBuilder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, KeyProperties.SIGNATURE_PADDING_RSA_PSS);
            specBuilder.setRandomizedEncryptionRequired(false);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported key algorithm: ");
            stringBuilder.append(keyAlgorithm);
            throw new KeyStoreException(stringBuilder.toString());
        }
        specBuilder.setUserAuthenticationRequired(false);
        return specBuilder.build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:143:0x0301  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0301  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0301  */
    private void setPrivateKeyEntry(java.lang.String r22, java.security.PrivateKey r23, java.security.cert.Certificate[] r24, java.security.KeyStore.ProtectionParameter r25) throws java.security.KeyStoreException {
        /*
        r21 = this;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        r4 = r24;
        r5 = r25;
        r0 = 0;
        if (r5 != 0) goto L_0x0013;
    L_0x000d:
        r6 = getLegacyKeyProtectionParameter(r23);
        r15 = r0;
        goto L_0x0041;
    L_0x0013:
        r6 = r5 instanceof android.security.KeyStoreParameter;
        if (r6 == 0) goto L_0x0027;
    L_0x0017:
        r6 = getLegacyKeyProtectionParameter(r23);
        r7 = r5;
        r7 = (android.security.KeyStoreParameter) r7;
        r8 = r7.isEncryptionRequired();
        if (r8 == 0) goto L_0x0025;
    L_0x0024:
        r0 = 1;
    L_0x0025:
        r15 = r0;
        goto L_0x0041;
    L_0x0027:
        r6 = r5 instanceof android.security.keystore.KeyProtection;
        if (r6 == 0) goto L_0x0362;
    L_0x002b:
        r6 = r5;
        r6 = (android.security.keystore.KeyProtection) r6;
        r7 = r6.isCriticalToDeviceEncryption();
        if (r7 == 0) goto L_0x0036;
    L_0x0034:
        r0 = r0 | 8;
    L_0x0036:
        r7 = r6.isStrongBoxBacked();
        if (r7 == 0) goto L_0x0040;
    L_0x003c:
        r0 = r0 | 16;
        r15 = r0;
        goto L_0x0041;
    L_0x0040:
        r15 = r0;
    L_0x0041:
        if (r4 == 0) goto L_0x035a;
    L_0x0043:
        r0 = r4.length;
        if (r0 == 0) goto L_0x035a;
    L_0x0046:
        r0 = r4.length;
        r13 = new java.security.cert.X509Certificate[r0];
        r0 = 0;
    L_0x004a:
        r7 = r4.length;
        if (r0 >= r7) goto L_0x0096;
    L_0x004d:
        r7 = r4[r0];
        r7 = r7.getType();
        r8 = "X.509";
        r7 = r8.equals(r7);
        r8 = "Certificates must be in X.509 format: invalid cert #";
        if (r7 == 0) goto L_0x0081;
    L_0x005d:
        r7 = r4[r0];
        r7 = r7 instanceof java.security.cert.X509Certificate;
        if (r7 == 0) goto L_0x006c;
    L_0x0063:
        r7 = r4[r0];
        r7 = (java.security.cert.X509Certificate) r7;
        r13[r0] = r7;
        r0 = r0 + 1;
        goto L_0x004a;
    L_0x006c:
        r7 = new java.security.KeyStoreException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r8);
        r9.append(r0);
        r8 = r9.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0081:
        r7 = new java.security.KeyStoreException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r8);
        r9.append(r0);
        r8 = r9.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0096:
        r0 = 0;
        r7 = r13[r0];	 Catch:{ CertificateEncodingException -> 0x034f }
        r7 = r7.getEncoded();	 Catch:{ CertificateEncodingException -> 0x034f }
        r12 = r7;
        r7 = r4.length;
        r11 = 1;
        if (r7 <= r11) goto L_0x00f1;
    L_0x00a3:
        r7 = r13.length;
        r7 = r7 - r11;
        r7 = new byte[r7][];
        r8 = 0;
        r9 = 0;
    L_0x00a9:
        r10 = r7.length;
        if (r9 >= r10) goto L_0x00d6;
    L_0x00ac:
        r10 = r9 + 1;
        r10 = r13[r10];	 Catch:{ CertificateEncodingException -> 0x00be }
        r10 = r10.getEncoded();	 Catch:{ CertificateEncodingException -> 0x00be }
        r7[r9] = r10;	 Catch:{ CertificateEncodingException -> 0x00be }
        r10 = r7[r9];	 Catch:{ CertificateEncodingException -> 0x00be }
        r10 = r10.length;	 Catch:{ CertificateEncodingException -> 0x00be }
        r8 = r8 + r10;
        r9 = r9 + 1;
        goto L_0x00a9;
    L_0x00be:
        r0 = move-exception;
        r10 = new java.security.KeyStoreException;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r14 = "Failed to encode certificate #";
        r11.append(r14);
        r11.append(r9);
        r11 = r11.toString();
        r10.<init>(r11, r0);
        throw r10;
    L_0x00d6:
        r9 = new byte[r8];
        r10 = 0;
        r14 = 0;
    L_0x00da:
        r11 = r7.length;
        if (r14 >= r11) goto L_0x00ef;
    L_0x00dd:
        r11 = r7[r14];
        r11 = r11.length;
        r4 = r7[r14];
        java.lang.System.arraycopy(r4, r0, r9, r10, r11);
        r10 = r10 + r11;
        r4 = 0;
        r7[r14] = r4;
        r14 = r14 + 1;
        r4 = r24;
        r11 = 1;
        goto L_0x00da;
    L_0x00ef:
        r4 = r9;
        goto L_0x00f3;
    L_0x00f1:
        r9 = 0;
        r4 = r9;
    L_0x00f3:
        r7 = r3 instanceof android.security.keystore.AndroidKeyStorePrivateKey;
        if (r7 == 0) goto L_0x0100;
    L_0x00f7:
        r7 = r3;
        r7 = (android.security.keystore.AndroidKeyStoreKey) r7;
        r7 = r7.getAlias();
        r11 = r7;
        goto L_0x0102;
    L_0x0100:
        r7 = 0;
        r11 = r7;
    L_0x0102:
        r7 = "USRPKEY_";
        if (r11 == 0) goto L_0x0142;
    L_0x0106:
        r8 = r11.startsWith(r7);
        if (r8 == 0) goto L_0x0142;
    L_0x010c:
        r0 = r7.length();
        r0 = r11.substring(r0);
        r8 = r2.equals(r0);
        if (r8 == 0) goto L_0x0123;
    L_0x011a:
        r8 = 0;
        r9 = 0;
        r0 = 0;
        r17 = r0;
        r3 = r8;
        r5 = r9;
        goto L_0x0226;
    L_0x0123:
        r7 = new java.security.KeyStoreException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Can only replace keys with same alias: ";
        r8.append(r9);
        r8.append(r2);
        r9 = " != ";
        r8.append(r9);
        r8.append(r0);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0142:
        r8 = 1;
        r9 = r23.getFormat();
        if (r9 == 0) goto L_0x032e;
    L_0x0149:
        r10 = "PKCS#8";
        r10 = r10.equals(r9);
        if (r10 == 0) goto L_0x032e;
    L_0x0151:
        r10 = r23.getEncoded();
        if (r10 == 0) goto L_0x0326;
    L_0x0157:
        r14 = new android.security.keymaster.KeymasterArguments;
        r14.<init>();
        r18 = r23.getAlgorithm();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r0 = android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(r18);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = 268435458; // 0x10000002 float:2.5243555E-29 double:1.32624738E-315;
        r14.addEnum(r3, r0);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r0 = r6.getPurposes();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = android.security.keystore.KeyProperties.Purpose.allToKeymaster(r0);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r5 = 536870913; // 0x20000001 float:1.0842023E-19 double:2.652494744E-315;
        r14.addEnums(r5, r3);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = r6.isDigestsSpecified();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        if (r3 == 0) goto L_0x0197;
    L_0x0180:
        r3 = 536870917; // 0x20000005 float:1.0842028E-19 double:2.652494763E-315;
        r5 = r6.getDigests();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r5 = android.security.keystore.KeyProperties.Digest.allToKeymaster(r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r14.addEnums(r3, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        goto L_0x0197;
    L_0x018f:
        r0 = move-exception;
        r16 = r11;
        r11 = r12;
        r19 = r13;
        goto L_0x0320;
    L_0x0197:
        r3 = 536870916; // 0x20000004 float:1.0842027E-19 double:2.65249476E-315;
        r5 = r6.getBlockModes();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r5 = android.security.keystore.KeyProperties.BlockMode.allToKeymaster(r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r14.addEnums(r3, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = r6.getEncryptionPaddings();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(r3);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r5 = r0 & 1;
        if (r5 == 0) goto L_0x01ed;
    L_0x01b2:
        r5 = r6.isRandomizedEncryptionRequired();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        if (r5 == 0) goto L_0x01ea;
    L_0x01b8:
        r5 = r3.length;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r18 = r0;
        r0 = 0;
    L_0x01bc:
        if (r0 >= r5) goto L_0x01ef;
    L_0x01be:
        r17 = r3[r0];	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r19 = android.security.keystore.KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(r17);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        if (r19 == 0) goto L_0x01ca;
    L_0x01c7:
        r0 = r0 + 1;
        goto L_0x01bc;
    L_0x01ca:
        r0 = new java.security.KeyStoreException;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r5 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r5.<init>();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r7 = "Randomized encryption (IND-CPA) required but is violated by encryption padding mode: ";
        r5.append(r7);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r7 = android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(r17);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r5.append(r7);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r7 = ". See KeyProtection documentation.";
        r5.append(r7);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r5 = r5.toString();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        r0.<init>(r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
        throw r0;	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x018f, IllegalArgumentException | IllegalStateException -> 0x018f }
    L_0x01ea:
        r18 = r0;
        goto L_0x01ef;
    L_0x01ed:
        r18 = r0;
    L_0x01ef:
        r0 = 536870918; // 0x20000006 float:1.084203E-19 double:2.65249477E-315;
        r14.addEnums(r0, r3);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r5 = r6.getSignaturePaddings();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r5 = android.security.keystore.KeyProperties.SignaturePadding.allToKeymaster(r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r14.addEnums(r0, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        android.security.keystore.KeymasterUtils.addUserAuthArgs(r14, r6);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r0 = 1610613136; // 0x60000190 float:3.6895247E19 double:7.95748619E-315;
        r5 = r6.getKeyValidityStart();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r14.addDateIfNotNull(r0, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r0 = 1610613137; // 0x60000191 float:3.689525E19 double:7.957486197E-315;
        r5 = r6.getKeyValidityForOriginationEnd();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r14.addDateIfNotNull(r0, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r0 = 1610613138; // 0x60000192 float:3.6895256E19 double:7.9574862E-315;
        r5 = r6.getKeyValidityForConsumptionEnd();	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r14.addDateIfNotNull(r0, r5);	 Catch:{ IllegalArgumentException | IllegalStateException -> 0x031a, IllegalArgumentException | IllegalStateException -> 0x031a }
        r3 = r8;
        r17 = r10;
        r5 = r14;
    L_0x0226:
        r18 = 0;
        if (r3 == 0) goto L_0x0278;
    L_0x022a:
        r0 = r1.mKeyStore;	 Catch:{ all -> 0x0270 }
        r8 = r1.mUid;	 Catch:{ all -> 0x0270 }
        android.security.Credentials.deleteAllTypesForAlias(r0, r2, r8);	 Catch:{ all -> 0x0270 }
        r14 = new android.security.keymaster.KeyCharacteristics;	 Catch:{ all -> 0x0270 }
        r14.<init>();	 Catch:{ all -> 0x0270 }
        r0 = r1.mKeyStore;	 Catch:{ all -> 0x0270 }
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0270 }
        r8.<init>();	 Catch:{ all -> 0x0270 }
        r8.append(r7);	 Catch:{ all -> 0x0270 }
        r8.append(r2);	 Catch:{ all -> 0x0270 }
        r8 = r8.toString();	 Catch:{ all -> 0x0270 }
        r10 = 1;
        r9 = r1.mUid;	 Catch:{ all -> 0x0270 }
        r7 = r0;
        r0 = r9;
        r9 = r5;
        r16 = r11;
        r11 = r17;
        r20 = r12;
        r12 = r0;
        r19 = r13;
        r13 = r15;
        r0 = r7.importKey(r8, r9, r10, r11, r12, r13, r14);	 Catch:{ all -> 0x026b }
        r7 = 1;
        if (r0 != r7) goto L_0x025f;
    L_0x025e:
        goto L_0x028d;
    L_0x025f:
        r7 = new java.security.KeyStoreException;	 Catch:{ all -> 0x026b }
        r8 = "Failed to store private key";
        r9 = android.security.KeyStore.getKeyStoreException(r0);	 Catch:{ all -> 0x026b }
        r7.<init>(r8, r9);	 Catch:{ all -> 0x026b }
        throw r7;	 Catch:{ all -> 0x026b }
    L_0x026b:
        r0 = move-exception;
        r11 = r20;
        goto L_0x02ff;
    L_0x0270:
        r0 = move-exception;
        r16 = r11;
        r19 = r13;
        r11 = r12;
        goto L_0x02ff;
    L_0x0278:
        r16 = r11;
        r20 = r12;
        r19 = r13;
        r7 = 1;
        r0 = r1.mKeyStore;	 Catch:{ all -> 0x02fc }
        r8 = r1.mUid;	 Catch:{ all -> 0x02fc }
        android.security.Credentials.deleteCertificateTypesForAlias(r0, r2, r8);	 Catch:{ all -> 0x02fc }
        r0 = r1.mKeyStore;	 Catch:{ all -> 0x02fc }
        r8 = r1.mUid;	 Catch:{ all -> 0x02fc }
        android.security.Credentials.deleteLegacyKeyForAlias(r0, r2, r8);	 Catch:{ all -> 0x02fc }
    L_0x028d:
        r0 = r1.mKeyStore;	 Catch:{ all -> 0x02fc }
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fc }
        r8.<init>();	 Catch:{ all -> 0x02fc }
        r9 = "USRCERT_";
        r8.append(r9);	 Catch:{ all -> 0x02fc }
        r8.append(r2);	 Catch:{ all -> 0x02fc }
        r8 = r8.toString();	 Catch:{ all -> 0x02fc }
        r9 = r1.mUid;	 Catch:{ all -> 0x02fc }
        r11 = r20;
        r0 = r0.insert(r8, r11, r9, r15);	 Catch:{ all -> 0x02fa }
        if (r0 != r7) goto L_0x02ee;
    L_0x02aa:
        r8 = r1.mKeyStore;	 Catch:{ all -> 0x02fa }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r9.<init>();	 Catch:{ all -> 0x02fa }
        r10 = "CACERT_";
        r9.append(r10);	 Catch:{ all -> 0x02fa }
        r9.append(r2);	 Catch:{ all -> 0x02fa }
        r9 = r9.toString();	 Catch:{ all -> 0x02fa }
        r10 = r1.mUid;	 Catch:{ all -> 0x02fa }
        r8 = r8.insert(r9, r4, r10, r15);	 Catch:{ all -> 0x02fa }
        r0 = r8;
        if (r0 != r7) goto L_0x02e2;
    L_0x02c6:
        r0 = 1;
        if (r0 != 0) goto L_0x02e1;
    L_0x02c9:
        if (r3 == 0) goto L_0x02d3;
    L_0x02cb:
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteAllTypesForAlias(r7, r2, r8);
        goto L_0x02e1;
    L_0x02d3:
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteCertificateTypesForAlias(r7, r2, r8);
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteLegacyKeyForAlias(r7, r2, r8);
    L_0x02e1:
        return;
    L_0x02e2:
        r7 = new java.security.KeyStoreException;	 Catch:{ all -> 0x02fa }
        r8 = "Failed to store certificate chain";
        r9 = android.security.KeyStore.getKeyStoreException(r0);	 Catch:{ all -> 0x02fa }
        r7.<init>(r8, r9);	 Catch:{ all -> 0x02fa }
        throw r7;	 Catch:{ all -> 0x02fa }
    L_0x02ee:
        r7 = new java.security.KeyStoreException;	 Catch:{ all -> 0x02fa }
        r8 = "Failed to store certificate #0";
        r9 = android.security.KeyStore.getKeyStoreException(r0);	 Catch:{ all -> 0x02fa }
        r7.<init>(r8, r9);	 Catch:{ all -> 0x02fa }
        throw r7;	 Catch:{ all -> 0x02fa }
    L_0x02fa:
        r0 = move-exception;
        goto L_0x02ff;
    L_0x02fc:
        r0 = move-exception;
        r11 = r20;
    L_0x02ff:
        if (r18 != 0) goto L_0x0319;
    L_0x0301:
        if (r3 == 0) goto L_0x030b;
    L_0x0303:
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteAllTypesForAlias(r7, r2, r8);
        goto L_0x0319;
    L_0x030b:
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteCertificateTypesForAlias(r7, r2, r8);
        r7 = r1.mKeyStore;
        r8 = r1.mUid;
        android.security.Credentials.deleteLegacyKeyForAlias(r7, r2, r8);
    L_0x0319:
        throw r0;
    L_0x031a:
        r0 = move-exception;
        r16 = r11;
        r11 = r12;
        r19 = r13;
    L_0x0320:
        r3 = new java.security.KeyStoreException;
        r3.<init>(r0);
        throw r3;
    L_0x0326:
        r0 = new java.security.KeyStoreException;
        r3 = "Private key did not export any key material";
        r0.<init>(r3);
        throw r0;
    L_0x032e:
        r16 = r11;
        r11 = r12;
        r19 = r13;
        r0 = new java.security.KeyStoreException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Unsupported private key export format: ";
        r3.append(r5);
        r3.append(r9);
        r5 = ". Only private keys which export their key material in PKCS#8 format are supported.";
        r3.append(r5);
        r3 = r3.toString();
        r0.<init>(r3);
        throw r0;
    L_0x034f:
        r0 = move-exception;
        r19 = r13;
        r3 = new java.security.KeyStoreException;
        r4 = "Failed to encode certificate #0";
        r3.<init>(r4, r0);
        throw r3;
    L_0x035a:
        r0 = new java.security.KeyStoreException;
        r3 = "Must supply at least one Certificate with PrivateKey";
        r0.<init>(r3);
        throw r0;
    L_0x0362:
        r3 = new java.security.KeyStoreException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Unsupported protection parameter class:";
        r4.append(r5);
        r5 = r25.getClass();
        r5 = r5.getName();
        r4.append(r5);
        r5 = ". Supported: ";
        r4.append(r5);
        r5 = android.security.keystore.KeyProtection.class;
        r5 = r5.getName();
        r4.append(r5);
        r5 = ", ";
        r4.append(r5);
        r5 = android.security.KeyStoreParameter.class;
        r5 = r5.getName();
        r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.AndroidKeyStoreSpi.setPrivateKeyEntry(java.lang.String, java.security.PrivateKey, java.security.cert.Certificate[], java.security.KeyStore$ProtectionParameter):void");
    }

    private void setSecretKeyEntry(String entryAlias, SecretKey key, ProtectionParameter param) throws KeyStoreException {
        RuntimeException e;
        KeymasterArguments keymasterArguments;
        String str = entryAlias;
        SecretKey secretKey = key;
        ProtectionParameter protectionParameter = param;
        if (protectionParameter == null || (protectionParameter instanceof KeyProtection)) {
            KeyProtection params = (KeyProtection) protectionParameter;
            String keyAliasPrefix;
            StringBuilder stringBuilder;
            if (secretKey instanceof AndroidKeyStoreSecretKey) {
                String keyAliasInKeystore = ((AndroidKeyStoreSecretKey) secretKey).getAlias();
                if (keyAliasInKeystore != null) {
                    keyAliasPrefix = Credentials.USER_PRIVATE_KEY;
                    if (!keyAliasInKeystore.startsWith(keyAliasPrefix)) {
                        keyAliasPrefix = Credentials.USER_SECRET_KEY;
                        if (!keyAliasInKeystore.startsWith(keyAliasPrefix)) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("KeyStore-backed secret key has invalid alias: ");
                            stringBuilder2.append(keyAliasInKeystore);
                            throw new KeyStoreException(stringBuilder2.toString());
                        }
                    }
                    String keyEntryAlias = keyAliasInKeystore.substring(keyAliasPrefix.length());
                    if (!str.equals(keyEntryAlias)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Can only replace KeyStore-backed keys with same alias: ");
                        stringBuilder.append(str);
                        stringBuilder.append(" != ");
                        stringBuilder.append(keyEntryAlias);
                        throw new KeyStoreException(stringBuilder.toString());
                    } else if (params != null) {
                        throw new KeyStoreException("Modifying KeyStore-backed key using protection parameters not supported");
                    } else {
                        return;
                    }
                }
                throw new KeyStoreException("KeyStore-backed secret key does not have an alias");
            } else if (params != null) {
                keyAliasPrefix = key.getFormat();
                StringBuilder stringBuilder3;
                if (keyAliasPrefix == null) {
                    throw new KeyStoreException("Only secret keys that export their key material are supported");
                } else if ("RAW".equals(keyAliasPrefix)) {
                    byte[] keyMaterial = key.getEncoded();
                    if (keyMaterial != null) {
                        KeymasterArguments args = new KeymasterArguments();
                        try {
                            int keymasterImpliedDigest;
                            int[] keymasterDigests;
                            int[] keymasterDigestsFromParams;
                            StringBuilder stringBuilder4;
                            int keymasterAlgorithm = KeyAlgorithm.toKeymasterSecretKeyAlgorithm(key.getAlgorithm());
                            args.addEnum(268435458, keymasterAlgorithm);
                            int i = 0;
                            if (keymasterAlgorithm == 128) {
                                try {
                                    keymasterImpliedDigest = KeyAlgorithm.toKeymasterDigest(key.getAlgorithm());
                                    if (keymasterImpliedDigest != -1) {
                                        keymasterDigests = new int[]{keymasterImpliedDigest};
                                        if (params.isDigestsSpecified()) {
                                            keymasterDigestsFromParams = Digest.allToKeymaster(params.getDigests());
                                            if (keymasterDigestsFromParams.length != 1 || keymasterDigestsFromParams[0] != keymasterImpliedDigest) {
                                                stringBuilder4 = new StringBuilder();
                                                stringBuilder4.append("Unsupported digests specification: ");
                                                stringBuilder4.append(Arrays.asList(params.getDigests()));
                                                stringBuilder4.append(". Only ");
                                                stringBuilder4.append(Digest.fromKeymaster(keymasterImpliedDigest));
                                                stringBuilder4.append(" supported for HMAC key algorithm ");
                                                stringBuilder4.append(key.getAlgorithm());
                                                throw new KeyStoreException(stringBuilder4.toString());
                                            }
                                        }
                                    } else {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("HMAC key algorithm digest unknown for key algorithm ");
                                        stringBuilder.append(key.getAlgorithm());
                                        throw new ProviderException(stringBuilder.toString());
                                    }
                                } catch (IllegalArgumentException | IllegalStateException e2) {
                                    e = e2;
                                    keymasterArguments = args;
                                    throw new KeyStoreException(e);
                                }
                            } else if (params.isDigestsSpecified()) {
                                keymasterDigests = Digest.allToKeymaster(params.getDigests());
                            } else {
                                keymasterDigests = EmptyArray.INT;
                            }
                            args.addEnums(KeymasterDefs.KM_TAG_DIGEST, keymasterDigests);
                            keymasterImpliedDigest = params.getPurposes();
                            keymasterDigestsFromParams = BlockMode.allToKeymaster(params.getBlockModes());
                            if ((keymasterImpliedDigest & 1) != 0) {
                                if (params.isRandomizedEncryptionRequired()) {
                                    int length = keymasterDigestsFromParams.length;
                                    while (i < length) {
                                        int keymasterBlockMode = keymasterDigestsFromParams[i];
                                        if (KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(keymasterBlockMode)) {
                                            i++;
                                        } else {
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("Randomized encryption (IND-CPA) required but may be violated by block mode: ");
                                            stringBuilder4.append(BlockMode.fromKeymaster(keymasterBlockMode));
                                            stringBuilder4.append(". See KeyProtection documentation.");
                                            throw new KeyStoreException(stringBuilder4.toString());
                                        }
                                    }
                                }
                            }
                            args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, Purpose.allToKeymaster(keymasterImpliedDigest));
                            args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, keymasterDigestsFromParams);
                            if (params.getSignaturePaddings().length <= 0) {
                                args.addEnums(KeymasterDefs.KM_TAG_PADDING, EncryptionPadding.allToKeymaster(params.getEncryptionPaddings()));
                                KeymasterUtils.addUserAuthArgs(args, params);
                                KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(args, keymasterAlgorithm, keymasterDigestsFromParams, keymasterDigests);
                                args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, params.getKeyValidityStart());
                                args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, params.getKeyValidityForOriginationEnd());
                                args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, params.getKeyValidityForConsumptionEnd());
                                if ((keymasterImpliedDigest & 1) != 0) {
                                    if (!params.isRandomizedEncryptionRequired()) {
                                        args.addBoolean(KeymasterDefs.KM_TAG_CALLER_NONCE);
                                    }
                                }
                                keymasterAlgorithm = 0;
                                if (params.isCriticalToDeviceEncryption()) {
                                    keymasterAlgorithm = 0 | 8;
                                }
                                if (params.isStrongBoxBacked()) {
                                    keymasterAlgorithm |= 16;
                                }
                                Credentials.deleteAllTypesForAlias(this.mKeyStore, str, this.mUid);
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(Credentials.USER_PRIVATE_KEY);
                                stringBuilder3.append(str);
                                keymasterImpliedDigest = this.mKeyStore.importKey(stringBuilder3.toString(), args, 3, keyMaterial, this.mUid, keymasterAlgorithm, new KeyCharacteristics());
                                if (keymasterImpliedDigest != 1) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Failed to import secret key. Keystore error code: ");
                                    stringBuilder.append(keymasterImpliedDigest);
                                    throw new KeyStoreException(stringBuilder.toString());
                                }
                                return;
                            }
                            try {
                                throw new KeyStoreException("Signature paddings not supported for symmetric keys");
                            } catch (IllegalArgumentException | IllegalStateException e3) {
                                e = e3;
                                throw new KeyStoreException(e);
                            }
                        } catch (IllegalArgumentException | IllegalStateException e4) {
                            e = e4;
                            keymasterArguments = args;
                            throw new KeyStoreException(e);
                        }
                    }
                    throw new KeyStoreException("Key did not export its key material despite supporting RAW format export");
                } else {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Unsupported secret key material export format: ");
                    stringBuilder3.append(keyAliasPrefix);
                    throw new KeyStoreException(stringBuilder3.toString());
                }
            } else {
                throw new KeyStoreException("Protection parameters must be specified when importing a symmetric key");
            }
        }
        StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append("Unsupported protection parameter class: ");
        stringBuilder5.append(param.getClass().getName());
        stringBuilder5.append(". Supported: ");
        stringBuilder5.append(KeyProtection.class.getName());
        throw new KeyStoreException(stringBuilder5.toString());
    }

    private void setWrappedKeyEntry(String alias, WrappedKeyEntry entry, ProtectionParameter param) throws KeyStoreException {
        if (param == null) {
            String mode;
            byte[] maskingKey = new byte[32];
            KeymasterArguments args = new KeymasterArguments();
            String[] parts = entry.getTransformation().split("/");
            String algorithm = parts[0];
            if (KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(algorithm)) {
                args.addEnum(268435458, 1);
            } else if (KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(algorithm)) {
                args.addEnum(268435458, 1);
            }
            if (parts.length > 1) {
                mode = parts[1];
                if (KeyProperties.BLOCK_MODE_ECB.equalsIgnoreCase(mode)) {
                    args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, 1);
                } else if (KeyProperties.BLOCK_MODE_CBC.equalsIgnoreCase(mode)) {
                    args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, 2);
                } else if (KeyProperties.BLOCK_MODE_CTR.equalsIgnoreCase(mode)) {
                    args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, 3);
                } else if (KeyProperties.BLOCK_MODE_GCM.equalsIgnoreCase(mode)) {
                    args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, 32);
                }
            }
            if (parts.length > 2) {
                String padding = parts[2];
                if (!KeyProperties.ENCRYPTION_PADDING_NONE.equalsIgnoreCase(padding)) {
                    if (KeyProperties.ENCRYPTION_PADDING_PKCS7.equalsIgnoreCase(padding)) {
                        args.addEnums(KeymasterDefs.KM_TAG_PADDING, 64);
                    } else if (KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1.equalsIgnoreCase(padding)) {
                        args.addEnums(KeymasterDefs.KM_TAG_PADDING, 4);
                    } else if (KeyProperties.ENCRYPTION_PADDING_RSA_OAEP.equalsIgnoreCase(padding)) {
                        args.addEnums(KeymasterDefs.KM_TAG_PADDING, 2);
                    }
                }
            }
            KeyGenParameterSpec spec = (KeyGenParameterSpec) entry.getAlgorithmParameterSpec();
            if (spec.isDigestsSpecified()) {
                String digest = spec.getDigests()[0];
                if (!KeyProperties.DIGEST_NONE.equalsIgnoreCase(digest)) {
                    if (KeyProperties.DIGEST_MD5.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 1);
                    } else if (KeyProperties.DIGEST_SHA1.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 2);
                    } else if (KeyProperties.DIGEST_SHA224.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 3);
                    } else if (KeyProperties.DIGEST_SHA256.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 4);
                    } else if (KeyProperties.DIGEST_SHA384.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 5);
                    } else if (KeyProperties.DIGEST_SHA512.equalsIgnoreCase(digest)) {
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, 6);
                    }
                }
            }
            int errorCode = this.mKeyStore;
            StringBuilder stringBuilder = new StringBuilder();
            String str = Credentials.USER_PRIVATE_KEY;
            stringBuilder.append(str);
            stringBuilder.append(alias);
            mode = stringBuilder.toString();
            byte[] wrappedKeyBytes = entry.getWrappedKeyBytes();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(entry.getWrappingKeyAlias());
            int i = 1;
            errorCode = errorCode.importWrappedKey(mode, wrappedKeyBytes, stringBuilder2.toString(), maskingKey, args, GateKeeper.getSecureUserId(), 0, this.mUid, new KeyCharacteristics());
            if (errorCode == -100) {
                throw new SecureKeyImportUnavailableException("Could not import wrapped key");
            } else if (errorCode != i) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to import wrapped key. Keystore error code: ");
                stringBuilder.append(errorCode);
                throw new KeyStoreException(stringBuilder.toString());
            } else {
                return;
            }
        }
        throw new KeyStoreException("Protection parameters are specified inside wrapped keys");
    }

    public void engineSetKeyEntry(String alias, byte[] userKey, Certificate[] chain) throws KeyStoreException {
        throw new KeyStoreException("Operation not supported because key encoding is unknown");
    }

    public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
        if (isKeyEntry(alias)) {
            throw new KeyStoreException("Entry exists and is not a trusted certificate");
        } else if (cert != null) {
            try {
                byte[] encoded = cert.getEncoded();
                KeyStore keyStore = this.mKeyStore;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Credentials.CA_CERTIFICATE);
                stringBuilder.append(alias);
                if (!keyStore.put(stringBuilder.toString(), encoded, this.mUid, 0)) {
                    throw new KeyStoreException("Couldn't insert certificate; is KeyStore initialized?");
                }
            } catch (CertificateEncodingException e) {
                throw new KeyStoreException(e);
            }
        } else {
            throw new NullPointerException("cert == null");
        }
    }

    public void engineDeleteEntry(String alias) throws KeyStoreException {
        if (!Credentials.deleteAllTypesForAlias(this.mKeyStore, alias, this.mUid)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to delete entry: ");
            stringBuilder.append(alias);
            throw new KeyStoreException(stringBuilder.toString());
        }
    }

    private Set<String> getUniqueAliases() {
        String[] rawAliases = this.mKeyStore.list("", this.mUid);
        if (rawAliases == null) {
            return new HashSet();
        }
        Set<String> aliases = new HashSet(rawAliases.length);
        for (String alias : rawAliases) {
            int idx = alias.indexOf(95);
            if (idx == -1 || alias.length() <= idx) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid alias: ");
                stringBuilder.append(alias);
                Log.e("AndroidKeyStore", stringBuilder.toString());
            } else {
                aliases.add(new String(alias.substring(idx + 1)));
            }
        }
        return aliases;
    }

    public Enumeration<String> engineAliases() {
        return Collections.enumeration(getUniqueAliases());
    }

    public boolean engineContainsAlias(String alias) {
        if (alias != null) {
            KeyStore keyStore = this.mKeyStore;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.USER_PRIVATE_KEY);
            stringBuilder.append(alias);
            if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
                keyStore = this.mKeyStore;
                stringBuilder = new StringBuilder();
                stringBuilder.append(Credentials.USER_SECRET_KEY);
                stringBuilder.append(alias);
                if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
                    keyStore = this.mKeyStore;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(Credentials.USER_CERTIFICATE);
                    stringBuilder.append(alias);
                    if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
                        keyStore = this.mKeyStore;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(Credentials.CA_CERTIFICATE);
                        stringBuilder.append(alias);
                        if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        throw new NullPointerException("alias == null");
    }

    public int engineSize() {
        return getUniqueAliases().size();
    }

    public boolean engineIsKeyEntry(String alias) {
        return isKeyEntry(alias);
    }

    private boolean isKeyEntry(String alias) {
        KeyStore keyStore = this.mKeyStore;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Credentials.USER_PRIVATE_KEY);
        stringBuilder.append(alias);
        if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
            keyStore = this.mKeyStore;
            stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.USER_SECRET_KEY);
            stringBuilder.append(alias);
            if (!keyStore.contains(stringBuilder.toString(), this.mUid)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCertificateEntry(String alias) {
        if (alias != null) {
            KeyStore keyStore = this.mKeyStore;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Credentials.CA_CERTIFICATE);
            stringBuilder.append(alias);
            return keyStore.contains(stringBuilder.toString(), this.mUid);
        }
        throw new NullPointerException("alias == null");
    }

    public boolean engineIsCertificateEntry(String alias) {
        return !isKeyEntry(alias) && isCertificateEntry(alias);
    }

    public String engineGetCertificateAlias(Certificate cert) {
        if (cert == null) {
            return null;
        }
        if (!"X.509".equalsIgnoreCase(cert.getType())) {
            return null;
        }
        try {
            byte[] targetCertBytes = cert.getEncoded();
            if (targetCertBytes == null) {
                return null;
            }
            byte[] certBytes;
            StringBuilder stringBuilder;
            String alias;
            Set<String> nonCaEntries = new HashSet();
            String[] certAliases = this.mKeyStore;
            int i = this.mUid;
            String str = Credentials.USER_CERTIFICATE;
            certAliases = certAliases.list(str, i);
            i = 0;
            if (certAliases != null) {
                for (String alias2 : certAliases) {
                    certBytes = this.mKeyStore;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(alias2);
                    certBytes = certBytes.get(stringBuilder.toString(), this.mUid);
                    if (certBytes != null) {
                        nonCaEntries.add(alias2);
                        if (Arrays.equals(certBytes, targetCertBytes)) {
                            return alias2;
                        }
                    }
                }
            }
            String[] caAliases = this.mKeyStore;
            int i2 = this.mUid;
            String str2 = Credentials.CA_CERTIFICATE;
            caAliases = caAliases.list(str2, i2);
            if (certAliases != null) {
                i2 = caAliases.length;
                while (i < i2) {
                    alias2 = caAliases[i];
                    if (!nonCaEntries.contains(alias2)) {
                        KeyStore keyStore = this.mKeyStore;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(alias2);
                        certBytes = keyStore.get(stringBuilder.toString(), this.mUid);
                        if (certBytes != null && Arrays.equals(certBytes, targetCertBytes)) {
                            return alias2;
                        }
                    }
                    i++;
                }
            }
            return null;
        } catch (CertificateEncodingException e) {
            return null;
        }
    }

    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException("Can not serialize AndroidKeyStore to OutputStream");
    }

    public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        if (stream != null) {
            throw new IllegalArgumentException("InputStream not supported");
        } else if (password == null) {
            this.mKeyStore = KeyStore.getInstance();
            this.mUid = -1;
        } else {
            throw new IllegalArgumentException("password not supported");
        }
    }

    public void engineLoad(LoadStoreParameter param) throws IOException, NoSuchAlgorithmException, CertificateException {
        int uid = -1;
        if (param != null) {
            if (param instanceof AndroidKeyStoreLoadStoreParameter) {
                uid = ((AndroidKeyStoreLoadStoreParameter) param).getUid();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported param type: ");
                stringBuilder.append(param.getClass());
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        this.mKeyStore = KeyStore.getInstance();
        this.mUid = uid;
    }

    public void engineSetEntry(String alias, Entry entry, ProtectionParameter param) throws KeyStoreException {
        if (entry != null) {
            Credentials.deleteAllTypesForAlias(this.mKeyStore, alias, this.mUid);
            if (entry instanceof TrustedCertificateEntry) {
                engineSetCertificateEntry(alias, ((TrustedCertificateEntry) entry).getTrustedCertificate());
                return;
            }
            if (entry instanceof PrivateKeyEntry) {
                PrivateKeyEntry prE = (PrivateKeyEntry) entry;
                setPrivateKeyEntry(alias, prE.getPrivateKey(), prE.getCertificateChain(), param);
            } else if (entry instanceof SecretKeyEntry) {
                setSecretKeyEntry(alias, ((SecretKeyEntry) entry).getSecretKey(), param);
            } else if (entry instanceof WrappedKeyEntry) {
                setWrappedKeyEntry(alias, (WrappedKeyEntry) entry, param);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Entry must be a PrivateKeyEntry, SecretKeyEntry or TrustedCertificateEntry; was ");
                stringBuilder.append(entry);
                throw new KeyStoreException(stringBuilder.toString());
            }
            return;
        }
        throw new KeyStoreException("entry == null");
    }
}
