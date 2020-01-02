package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

@SystemApi
public final class WrappedApplicationKey implements Parcelable {
    public static final Creator<WrappedApplicationKey> CREATOR = new Creator<WrappedApplicationKey>() {
        public WrappedApplicationKey createFromParcel(Parcel in) {
            return new WrappedApplicationKey(in);
        }

        public WrappedApplicationKey[] newArray(int length) {
            return new WrappedApplicationKey[length];
        }
    };
    private String mAlias;
    private byte[] mEncryptedKeyMaterial;
    private byte[] mMetadata;

    public static class Builder {
        private WrappedApplicationKey mInstance = new WrappedApplicationKey();

        public Builder setAlias(String alias) {
            this.mInstance.mAlias = alias;
            return this;
        }

        public Builder setEncryptedKeyMaterial(byte[] encryptedKeyMaterial) {
            this.mInstance.mEncryptedKeyMaterial = encryptedKeyMaterial;
            return this;
        }

        public Builder setMetadata(byte[] metadata) {
            this.mInstance.mMetadata = metadata;
            return this;
        }

        public WrappedApplicationKey build() {
            Preconditions.checkNotNull(this.mInstance.mAlias);
            Preconditions.checkNotNull(this.mInstance.mEncryptedKeyMaterial);
            return this.mInstance;
        }
    }

    private WrappedApplicationKey() {
    }

    @Deprecated
    public WrappedApplicationKey(String alias, byte[] encryptedKeyMaterial) {
        this.mAlias = (String) Preconditions.checkNotNull(alias);
        this.mEncryptedKeyMaterial = (byte[]) Preconditions.checkNotNull(encryptedKeyMaterial);
    }

    public String getAlias() {
        return this.mAlias;
    }

    public byte[] getEncryptedKeyMaterial() {
        return this.mEncryptedKeyMaterial;
    }

    public byte[] getMetadata() {
        return this.mMetadata;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mAlias);
        out.writeByteArray(this.mEncryptedKeyMaterial);
        out.writeByteArray(this.mMetadata);
    }

    protected WrappedApplicationKey(Parcel in) {
        this.mAlias = in.readString();
        this.mEncryptedKeyMaterial = in.createByteArray();
        if (in.dataAvail() > 0) {
            this.mMetadata = in.createByteArray();
        }
    }

    public int describeContents() {
        return 0;
    }
}
