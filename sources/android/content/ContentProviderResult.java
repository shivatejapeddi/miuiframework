package android.content;

import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class ContentProviderResult implements Parcelable {
    public static final Creator<ContentProviderResult> CREATOR = new Creator<ContentProviderResult>() {
        public ContentProviderResult createFromParcel(Parcel source) {
            return new ContentProviderResult(source);
        }

        public ContentProviderResult[] newArray(int size) {
            return new ContentProviderResult[size];
        }
    };
    public final Integer count;
    public final String failure;
    public final Uri uri;

    public ContentProviderResult(Uri uri) {
        this((Uri) Preconditions.checkNotNull(uri), null, null);
    }

    public ContentProviderResult(int count) {
        this(null, Integer.valueOf(count), null);
    }

    public ContentProviderResult(String failure) {
        this(null, null, failure);
    }

    public ContentProviderResult(Uri uri, Integer count, String failure) {
        this.uri = uri;
        this.count = count;
        this.failure = failure;
    }

    public ContentProviderResult(Parcel source) {
        if (source.readInt() != 0) {
            this.uri = (Uri) Uri.CREATOR.createFromParcel(source);
        } else {
            this.uri = null;
        }
        if (source.readInt() != 0) {
            this.count = Integer.valueOf(source.readInt());
        } else {
            this.count = null;
        }
        if (source.readInt() != 0) {
            this.failure = source.readString();
        } else {
            this.failure = null;
        }
    }

    public ContentProviderResult(ContentProviderResult cpr, int userId) {
        this.uri = ContentProvider.maybeAddUserId(cpr.uri, userId);
        this.count = cpr.count;
        this.failure = cpr.failure;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.uri != null) {
            dest.writeInt(1);
            this.uri.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (this.count != null) {
            dest.writeInt(1);
            dest.writeInt(this.count.intValue());
        } else {
            dest.writeInt(0);
        }
        if (this.failure != null) {
            dest.writeInt(1);
            dest.writeString(this.failure);
            return;
        }
        dest.writeInt(0);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder;
        StringBuilder sb = new StringBuilder("ContentProviderResult(");
        Uri uri = this.uri;
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (uri != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("uri=");
            stringBuilder.append(this.uri);
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
        }
        if (this.count != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("count=");
            stringBuilder.append(this.count);
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
        }
        if (this.uri != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("failure=");
            stringBuilder.append(this.failure);
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
