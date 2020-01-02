package android.security.keystore;

import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.ProtectionParameter;

class AndroidKeyStoreLoadStoreParameter implements LoadStoreParameter {
    private final int mUid;

    AndroidKeyStoreLoadStoreParameter(int uid) {
        this.mUid = uid;
    }

    public ProtectionParameter getProtectionParameter() {
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public int getUid() {
        return this.mUid;
    }
}
