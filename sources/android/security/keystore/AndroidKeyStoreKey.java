package android.security.keystore;

import java.security.Key;

public class AndroidKeyStoreKey implements Key {
    private final String mAlgorithm;
    private final String mAlias;
    private final int mUid;

    public AndroidKeyStoreKey(String alias, int uid, String algorithm) {
        this.mAlias = alias;
        this.mUid = uid;
        this.mAlgorithm = algorithm;
    }

    /* Access modifiers changed, original: 0000 */
    public String getAlias() {
        return this.mAlias;
    }

    /* Access modifiers changed, original: 0000 */
    public int getUid() {
        return this.mUid;
    }

    public String getAlgorithm() {
        return this.mAlgorithm;
    }

    public String getFormat() {
        return null;
    }

    public byte[] getEncoded() {
        return null;
    }

    public int hashCode() {
        int result = 1 * 31;
        String str = this.mAlgorithm;
        int i = 0;
        int hashCode = (result + (str == null ? 0 : str.hashCode())) * 31;
        str = this.mAlias;
        if (str != null) {
            i = str.hashCode();
        }
        return ((hashCode + i) * 31) + this.mUid;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AndroidKeyStoreKey other = (AndroidKeyStoreKey) obj;
        String str = this.mAlgorithm;
        if (str == null) {
            if (other.mAlgorithm != null) {
                return false;
            }
        } else if (!str.equals(other.mAlgorithm)) {
            return false;
        }
        str = this.mAlias;
        if (str == null) {
            if (other.mAlias != null) {
                return false;
            }
        } else if (!str.equals(other.mAlias)) {
            return false;
        }
        if (this.mUid != other.mUid) {
            return false;
        }
        return true;
    }
}
