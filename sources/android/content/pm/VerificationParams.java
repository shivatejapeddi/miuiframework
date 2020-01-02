package android.content.pm;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public class VerificationParams implements Parcelable {
    public static final Creator<VerificationParams> CREATOR = new Creator<VerificationParams>() {
        public VerificationParams createFromParcel(Parcel source) {
            return new VerificationParams(source, null);
        }

        public VerificationParams[] newArray(int size) {
            return new VerificationParams[size];
        }
    };
    public static final int NO_UID = -1;
    private static final String TO_STRING_PREFIX = "VerificationParams{";
    private int mInstallerUid;
    private final Uri mOriginatingURI;
    private final int mOriginatingUid;
    private final Uri mReferrer;
    private final Uri mVerificationURI;

    /* synthetic */ VerificationParams(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public VerificationParams(Uri verificationURI, Uri originatingURI, Uri referrer, int originatingUid) {
        this.mVerificationURI = verificationURI;
        this.mOriginatingURI = originatingURI;
        this.mReferrer = referrer;
        this.mOriginatingUid = originatingUid;
        this.mInstallerUid = -1;
    }

    public Uri getVerificationURI() {
        return this.mVerificationURI;
    }

    public Uri getOriginatingURI() {
        return this.mOriginatingURI;
    }

    public Uri getReferrer() {
        return this.mReferrer;
    }

    public int getOriginatingUid() {
        return this.mOriginatingUid;
    }

    public int getInstallerUid() {
        return this.mInstallerUid;
    }

    public void setInstallerUid(int uid) {
        this.mInstallerUid = uid;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VerificationParams)) {
            return false;
        }
        VerificationParams other = (VerificationParams) o;
        Uri uri = this.mVerificationURI;
        if (uri == null) {
            if (other.mVerificationURI != null) {
                return false;
            }
        } else if (!uri.equals(other.mVerificationURI)) {
            return false;
        }
        uri = this.mOriginatingURI;
        if (uri == null) {
            if (other.mOriginatingURI != null) {
                return false;
            }
        } else if (!uri.equals(other.mOriginatingURI)) {
            return false;
        }
        uri = this.mReferrer;
        if (uri == null) {
            if (other.mReferrer != null) {
                return false;
            }
        } else if (!uri.equals(other.mReferrer)) {
            return false;
        }
        if (this.mOriginatingUid == other.mOriginatingUid && this.mInstallerUid == other.mInstallerUid) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        Uri uri = this.mVerificationURI;
        int i = 1;
        int hash = 3 + ((uri == null ? 1 : uri.hashCode()) * 5);
        uri = this.mOriginatingURI;
        hash += (uri == null ? 1 : uri.hashCode()) * 7;
        uri = this.mReferrer;
        if (uri != null) {
            i = uri.hashCode();
        }
        return ((hash + (i * 11)) + (this.mOriginatingUid * 13)) + (this.mInstallerUid * 17);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(TO_STRING_PREFIX);
        sb.append("mVerificationURI=");
        sb.append(this.mVerificationURI.toString());
        sb.append(",mOriginatingURI=");
        sb.append(this.mOriginatingURI.toString());
        sb.append(",mReferrer=");
        sb.append(this.mReferrer.toString());
        sb.append(",mOriginatingUid=");
        sb.append(this.mOriginatingUid);
        sb.append(",mInstallerUid=");
        sb.append(this.mInstallerUid);
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mVerificationURI, 0);
        dest.writeParcelable(this.mOriginatingURI, 0);
        dest.writeParcelable(this.mReferrer, 0);
        dest.writeInt(this.mOriginatingUid);
        dest.writeInt(this.mInstallerUid);
    }

    private VerificationParams(Parcel source) {
        this.mVerificationURI = (Uri) source.readParcelable(Uri.class.getClassLoader());
        this.mOriginatingURI = (Uri) source.readParcelable(Uri.class.getClassLoader());
        this.mReferrer = (Uri) source.readParcelable(Uri.class.getClassLoader());
        this.mOriginatingUid = source.readInt();
        this.mInstallerUid = source.readInt();
    }
}
