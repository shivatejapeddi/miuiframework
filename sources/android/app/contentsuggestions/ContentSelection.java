package android.app.contentsuggestions;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ContentSelection implements Parcelable {
    public static final Creator<ContentSelection> CREATOR = new Creator<ContentSelection>() {
        public ContentSelection createFromParcel(Parcel source) {
            return new ContentSelection(source.readString(), source.readBundle());
        }

        public ContentSelection[] newArray(int size) {
            return new ContentSelection[size];
        }
    };
    private final Bundle mExtras;
    private final String mSelectionId;

    public ContentSelection(String selectionId, Bundle extras) {
        this.mSelectionId = selectionId;
        this.mExtras = extras;
    }

    public String getId() {
        return this.mSelectionId;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSelectionId);
        dest.writeBundle(this.mExtras);
    }
}
