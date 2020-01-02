package android.telephony.mbms;

import android.annotation.SystemApi;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class UriPathPair implements Parcelable {
    public static final Creator<UriPathPair> CREATOR = new Creator<UriPathPair>() {
        public UriPathPair createFromParcel(Parcel in) {
            return new UriPathPair(in, null);
        }

        public UriPathPair[] newArray(int size) {
            return new UriPathPair[size];
        }
    };
    private final Uri mContentUri;
    private final Uri mFilePathUri;

    public UriPathPair(Uri fileUri, Uri contentUri) {
        if (fileUri != null) {
            if (ContentResolver.SCHEME_FILE.equals(fileUri.getScheme())) {
                if (contentUri != null) {
                    if ("content".equals(contentUri.getScheme())) {
                        this.mFilePathUri = fileUri;
                        this.mContentUri = contentUri;
                        return;
                    }
                }
                throw new IllegalArgumentException("Content URI must have content scheme");
            }
        }
        throw new IllegalArgumentException("File URI must have file scheme");
    }

    private UriPathPair(Parcel in) {
        this.mFilePathUri = (Uri) in.readParcelable(Uri.class.getClassLoader());
        this.mContentUri = (Uri) in.readParcelable(Uri.class.getClassLoader());
    }

    public Uri getFilePathUri() {
        return this.mFilePathUri;
    }

    public Uri getContentUri() {
        return this.mContentUri;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mFilePathUri, flags);
        dest.writeParcelable(this.mContentUri, flags);
    }
}
