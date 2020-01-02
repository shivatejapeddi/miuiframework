package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.statistics.MicroscopicEvent.MicroEventFields;

public final class LooperCheckPoint extends MicroscopicEvent<MicroEventFields> {
    public static final Creator<LooperOnce> CREATOR = new Creator<LooperOnce>() {
        public LooperOnce createFromParcel(Parcel source) {
            LooperOnce object = new LooperOnce();
            object.readFromParcel(source);
            return object;
        }

        public LooperOnce[] newArray(int size) {
            return new LooperOnce[size];
        }
    };

    public LooperCheckPoint() {
        super(12, new MicroEventFields(false));
    }
}
