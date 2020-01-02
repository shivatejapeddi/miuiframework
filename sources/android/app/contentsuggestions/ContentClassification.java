package android.app.contentsuggestions;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ContentClassification implements Parcelable {
    public static final Creator<ContentClassification> CREATOR = new Creator<ContentClassification>() {
        public ContentClassification createFromParcel(Parcel source) {
            return new ContentClassification(source.readString(), source.readBundle());
        }

        public ContentClassification[] newArray(int size) {
            return new ContentClassification[size];
        }
    };
    private final String mClassificationId;
    private final Bundle mExtras;

    public ContentClassification(String classificationId, Bundle extras) {
        this.mClassificationId = classificationId;
        this.mExtras = extras;
    }

    public String getId() {
        return this.mClassificationId;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mClassificationId);
        dest.writeBundle(this.mExtras);
    }
}
