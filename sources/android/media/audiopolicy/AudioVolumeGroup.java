package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.media.AudioAttributes;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SystemApi
public final class AudioVolumeGroup implements Parcelable {
    public static final Creator<AudioVolumeGroup> CREATOR = new Creator<AudioVolumeGroup>() {
        public AudioVolumeGroup createFromParcel(Parcel in) {
            int index;
            Preconditions.checkNotNull(in, "in Parcel must not be null");
            String name = in.readString();
            int id = in.readInt();
            int nbAttributes = in.readInt();
            AudioAttributes[] audioAttributes = new AudioAttributes[nbAttributes];
            for (index = 0; index < nbAttributes; index++) {
                audioAttributes[index] = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(in);
            }
            index = in.readInt();
            int[] streamTypes = new int[index];
            for (int index2 = 0; index2 < index; index2++) {
                streamTypes[index2] = in.readInt();
            }
            return new AudioVolumeGroup(name, id, audioAttributes, streamTypes);
        }

        public AudioVolumeGroup[] newArray(int size) {
            return new AudioVolumeGroup[size];
        }
    };
    public static final int DEFAULT_VOLUME_GROUP = -1;
    private static final String TAG = "AudioVolumeGroup";
    @GuardedBy({"sLock"})
    private static List<AudioVolumeGroup> sAudioVolumeGroups;
    private static final Object sLock = new Object();
    private final AudioAttributes[] mAudioAttributes;
    private int mId;
    private int[] mLegacyStreamTypes;
    private final String mName;

    private static native int native_list_audio_volume_groups(ArrayList<AudioVolumeGroup> arrayList);

    public static List<AudioVolumeGroup> getAudioVolumeGroups() {
        if (sAudioVolumeGroups == null) {
            synchronized (sLock) {
                if (sAudioVolumeGroups == null) {
                    sAudioVolumeGroups = initializeAudioVolumeGroups();
                }
            }
        }
        return sAudioVolumeGroups;
    }

    private static List<AudioVolumeGroup> initializeAudioVolumeGroups() {
        ArrayList<AudioVolumeGroup> avgList = new ArrayList();
        if (native_list_audio_volume_groups(avgList) != 0) {
            Log.w(TAG, ": listAudioVolumeGroups failed");
        }
        return avgList;
    }

    AudioVolumeGroup(String name, int id, AudioAttributes[] audioAttributes, int[] legacyStreamTypes) {
        Preconditions.checkNotNull(name, "name must not be null");
        Preconditions.checkNotNull(audioAttributes, "audioAttributes must not be null");
        Preconditions.checkNotNull(legacyStreamTypes, "legacyStreamTypes must not be null");
        this.mName = name;
        this.mId = id;
        this.mAudioAttributes = audioAttributes;
        this.mLegacyStreamTypes = legacyStreamTypes;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioVolumeGroup thatAvg = (AudioVolumeGroup) o;
        if (!(this.mName == thatAvg.mName && this.mId == thatAvg.mId && this.mAudioAttributes.equals(thatAvg.mAudioAttributes))) {
            z = false;
        }
        return z;
    }

    public List<AudioAttributes> getAudioAttributes() {
        return Arrays.asList(this.mAudioAttributes);
    }

    public int[] getLegacyStreamTypes() {
        return this.mLegacyStreamTypes;
    }

    public String name() {
        return this.mName;
    }

    public int getId() {
        return this.mId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mId);
        dest.writeInt(this.mAudioAttributes.length);
        int i = 0;
        for (AudioAttributes attributes : this.mAudioAttributes) {
            attributes.writeToParcel(dest, flags | 1);
        }
        dest.writeInt(this.mLegacyStreamTypes.length);
        int[] iArr = this.mLegacyStreamTypes;
        int length = iArr.length;
        while (i < length) {
            dest.writeInt(iArr[i]);
            i++;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n Name: ");
        s.append(this.mName);
        s.append(" Id: ");
        s.append(Integer.toString(this.mId));
        s.append("\n     Supported Audio Attributes:");
        int i = 0;
        for (AudioAttributes attribute : this.mAudioAttributes) {
            s.append("\n       -");
            s.append(attribute.toString());
        }
        s.append("\n     Supported Legacy Stream Types: { ");
        int[] iArr = this.mLegacyStreamTypes;
        int length = iArr.length;
        while (i < length) {
            s.append(Integer.toString(iArr[i]));
            s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            i++;
        }
        s.append("}");
        return s.toString();
    }
}
