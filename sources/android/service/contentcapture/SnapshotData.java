package android.service.contentcapture;

import android.annotation.SystemApi;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class SnapshotData implements Parcelable {
    public static final Creator<SnapshotData> CREATOR = new Creator<SnapshotData>() {
        public SnapshotData createFromParcel(Parcel parcel) {
            return new SnapshotData(parcel);
        }

        public SnapshotData[] newArray(int size) {
            return new SnapshotData[size];
        }
    };
    private final AssistContent mAssistContent;
    private final Bundle mAssistData;
    private final AssistStructure mAssistStructure;

    public SnapshotData(Bundle assistData, AssistStructure assistStructure, AssistContent assistContent) {
        this.mAssistData = assistData;
        this.mAssistStructure = assistStructure;
        this.mAssistContent = assistContent;
    }

    SnapshotData(Parcel parcel) {
        this.mAssistData = parcel.readBundle();
        this.mAssistStructure = (AssistStructure) parcel.readParcelable(null);
        this.mAssistContent = (AssistContent) parcel.readParcelable(null);
    }

    public Bundle getAssistData() {
        return this.mAssistData;
    }

    public AssistStructure getAssistStructure() {
        return this.mAssistStructure;
    }

    public AssistContent getAssistContent() {
        return this.mAssistContent;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeBundle(this.mAssistData);
        parcel.writeParcelable(this.mAssistStructure, flags);
        parcel.writeParcelable(this.mAssistContent, flags);
    }
}
