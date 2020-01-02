package android.hardware.biometrics;

import android.security.keystore.AndroidKeyStoreProvider;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class CryptoObject {
    private final Object mCrypto;

    public CryptoObject(Signature signature) {
        this.mCrypto = signature;
    }

    public CryptoObject(Cipher cipher) {
        this.mCrypto = cipher;
    }

    public CryptoObject(Mac mac) {
        this.mCrypto = mac;
    }

    public Signature getSignature() {
        Object obj = this.mCrypto;
        return obj instanceof Signature ? (Signature) obj : null;
    }

    public Cipher getCipher() {
        Object obj = this.mCrypto;
        return obj instanceof Cipher ? (Cipher) obj : null;
    }

    public Mac getMac() {
        Object obj = this.mCrypto;
        return obj instanceof Mac ? (Mac) obj : null;
    }

    public final long getOpId() {
        Object obj = this.mCrypto;
        return obj != null ? AndroidKeyStoreProvider.getKeyStoreOperationHandle(obj) : 0;
    }
}
