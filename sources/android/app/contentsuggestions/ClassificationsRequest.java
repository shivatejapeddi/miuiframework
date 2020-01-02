package android.app.contentsuggestions;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

@SystemApi
public final class ClassificationsRequest implements Parcelable {
    public static final Creator<ClassificationsRequest> CREATOR = new Creator<ClassificationsRequest>() {
        public ClassificationsRequest createFromParcel(Parcel source) {
            return new ClassificationsRequest(source.createTypedArrayList(ContentSelection.CREATOR), source.readBundle(), null);
        }

        public ClassificationsRequest[] newArray(int size) {
            return new ClassificationsRequest[size];
        }
    };
    private final Bundle mExtras;
    private final List<ContentSelection> mSelections;

    @SystemApi
    public static final class Builder {
        private Bundle mExtras;
        private final List<ContentSelection> mSelections;

        public Builder(List<ContentSelection> selections) {
            this.mSelections = selections;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public ClassificationsRequest build() {
            return new ClassificationsRequest(this.mSelections, this.mExtras, null);
        }
    }

    /* synthetic */ ClassificationsRequest(List x0, Bundle x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    private ClassificationsRequest(List<ContentSelection> selections, Bundle extras) {
        this.mSelections = selections;
        this.mExtras = extras;
    }

    public List<ContentSelection> getSelections() {
        return this.mSelections;
    }

    public Bundle getExtras() {
        Bundle bundle = this.mExtras;
        return bundle == null ? new Bundle() : bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mSelections);
        dest.writeBundle(this.mExtras);
    }
}
