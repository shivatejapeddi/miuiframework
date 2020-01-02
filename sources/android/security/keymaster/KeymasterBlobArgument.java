package android.security.keymaster;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;

class KeymasterBlobArgument extends KeymasterArgument {
    @UnsupportedAppUsage
    public final byte[] blob;

    @UnsupportedAppUsage
    public KeymasterBlobArgument(int tag, byte[] blob) {
        super(tag);
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType == Integer.MIN_VALUE || tagType == -1879048192) {
            this.blob = blob;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad blob tag ");
        stringBuilder.append(tag);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public KeymasterBlobArgument(int tag, Parcel in) {
        super(tag);
        this.blob = in.createByteArray();
    }

    public void writeValue(Parcel out) {
        out.writeByteArray(this.blob);
    }
}
