package android.media;

import android.annotation.UnsupportedAppUsage;
import android.media.audiofx.AudioEffect.Descriptor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class AudioRecordingConfiguration implements Parcelable {
    public static final Creator<AudioRecordingConfiguration> CREATOR = new Creator<AudioRecordingConfiguration>() {
        public AudioRecordingConfiguration createFromParcel(Parcel p) {
            return new AudioRecordingConfiguration(p, null);
        }

        public AudioRecordingConfiguration[] newArray(int size) {
            return new AudioRecordingConfiguration[size];
        }
    };
    private static final String TAG = new String("AudioRecordingConfiguration");
    private final Descriptor[] mClientEffects;
    private final AudioFormat mClientFormat;
    private final String mClientPackageName;
    private final int mClientPortId;
    private final int mClientSessionId;
    private boolean mClientSilenced;
    private final int mClientSource;
    private final int mClientUid;
    private final Descriptor[] mDeviceEffects;
    private final AudioFormat mDeviceFormat;
    private final int mDeviceSource;
    private final int mPatchHandle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AudioSource {
    }

    /* synthetic */ AudioRecordingConfiguration(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public AudioRecordingConfiguration(int uid, int session, int source, AudioFormat clientFormat, AudioFormat devFormat, int patchHandle, String packageName, int clientPortId, boolean clientSilenced, int deviceSource, Descriptor[] clientEffects, Descriptor[] deviceEffects) {
        this.mClientUid = uid;
        this.mClientSessionId = session;
        this.mClientSource = source;
        this.mClientFormat = clientFormat;
        this.mDeviceFormat = devFormat;
        this.mPatchHandle = patchHandle;
        this.mClientPackageName = packageName;
        this.mClientPortId = clientPortId;
        this.mClientSilenced = clientSilenced;
        this.mDeviceSource = deviceSource;
        this.mClientEffects = clientEffects;
        this.mDeviceEffects = deviceEffects;
    }

    public AudioRecordingConfiguration(int uid, int session, int source, AudioFormat clientFormat, AudioFormat devFormat, int patchHandle, String packageName) {
        this(uid, session, source, clientFormat, devFormat, patchHandle, packageName, 0, false, 0, new Descriptor[0], new Descriptor[0]);
    }

    public void dump(PrintWriter pw) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  ");
        stringBuilder.append(toLogFriendlyString(this));
        pw.println(stringBuilder.toString());
    }

    public static String toLogFriendlyString(AudioRecordingConfiguration arc) {
        String str;
        String str2;
        Descriptor desc;
        StringBuilder stringBuilder;
        String clientEffects = new String();
        Descriptor[] descriptorArr = arc.mClientEffects;
        int length = descriptorArr.length;
        int i = 0;
        String clientEffects2 = clientEffects;
        int clientEffects3 = 0;
        while (true) {
            str = "' ";
            str2 = "'";
            if (clientEffects3 >= length) {
                break;
            }
            desc = descriptorArr[clientEffects3];
            stringBuilder = new StringBuilder();
            stringBuilder.append(clientEffects2);
            stringBuilder.append(str2);
            stringBuilder.append(desc.name);
            stringBuilder.append(str);
            clientEffects2 = stringBuilder.toString();
            clientEffects3++;
        }
        clientEffects = new String();
        descriptorArr = arc.mDeviceEffects;
        length = descriptorArr.length;
        while (i < length) {
            desc = descriptorArr[i];
            stringBuilder = new StringBuilder();
            stringBuilder.append(clientEffects);
            stringBuilder.append(str2);
            stringBuilder.append(desc.name);
            stringBuilder.append(str);
            clientEffects = stringBuilder.toString();
            i++;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("session:");
        stringBuilder2.append(arc.mClientSessionId);
        stringBuilder2.append(" -- source client=");
        stringBuilder2.append(MediaRecorder.toLogFriendlyAudioSource(arc.mClientSource));
        String str3 = ", dev=";
        stringBuilder2.append(str3);
        stringBuilder2.append(arc.mDeviceFormat.toLogFriendlyString());
        stringBuilder2.append(" -- uid:");
        stringBuilder2.append(arc.mClientUid);
        stringBuilder2.append(" -- patch:");
        stringBuilder2.append(arc.mPatchHandle);
        stringBuilder2.append(" -- pack:");
        stringBuilder2.append(arc.mClientPackageName);
        stringBuilder2.append(" -- format client=");
        stringBuilder2.append(arc.mClientFormat.toLogFriendlyString());
        stringBuilder2.append(str3);
        stringBuilder2.append(arc.mDeviceFormat.toLogFriendlyString());
        stringBuilder2.append(" -- silenced:");
        stringBuilder2.append(arc.mClientSilenced);
        stringBuilder2.append(" -- effects client=");
        stringBuilder2.append(clientEffects2);
        stringBuilder2.append(str3);
        stringBuilder2.append(clientEffects);
        return new String(stringBuilder2.toString());
    }

    public static AudioRecordingConfiguration anonymizedCopy(AudioRecordingConfiguration in) {
        return new AudioRecordingConfiguration(-1, in.mClientSessionId, in.mClientSource, in.mClientFormat, in.mDeviceFormat, in.mPatchHandle, "", in.mClientPortId, in.mClientSilenced, in.mDeviceSource, in.mClientEffects, in.mDeviceEffects);
    }

    public int getClientAudioSource() {
        return this.mClientSource;
    }

    public int getClientAudioSessionId() {
        return this.mClientSessionId;
    }

    public AudioFormat getFormat() {
        return this.mDeviceFormat;
    }

    public AudioFormat getClientFormat() {
        return this.mClientFormat;
    }

    @UnsupportedAppUsage
    public String getClientPackageName() {
        return this.mClientPackageName;
    }

    @UnsupportedAppUsage
    public int getClientUid() {
        return this.mClientUid;
    }

    public AudioDeviceInfo getAudioDevice() {
        ArrayList<AudioPatch> patches = new ArrayList();
        if (AudioManager.listAudioPatches(patches) != 0) {
            Log.e(TAG, "Error retrieving list of audio patches");
            return null;
        }
        for (int i = 0; i < patches.size(); i++) {
            AudioPatch patch = (AudioPatch) patches.get(i);
            if (patch.id() == this.mPatchHandle) {
                AudioPortConfig[] sources = patch.sources();
                if (sources != null && sources.length > 0) {
                    int devId = sources[0].port().id();
                    AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(1);
                    for (int j = 0; j < devices.length; j++) {
                        if (devices[j].getId() == devId) {
                            return devices[j];
                        }
                    }
                }
                Log.e(TAG, "Couldn't find device for recording, did recording end already?");
                return null;
            }
        }
        Log.e(TAG, "Couldn't find device for recording, did recording end already?");
        return null;
    }

    public int getClientPortId() {
        return this.mClientPortId;
    }

    public boolean isClientSilenced() {
        return this.mClientSilenced;
    }

    public int getAudioSource() {
        return this.mDeviceSource;
    }

    public List<Descriptor> getClientEffects() {
        return new ArrayList(Arrays.asList(this.mClientEffects));
    }

    public List<Descriptor> getEffects() {
        return new ArrayList(Arrays.asList(this.mDeviceEffects));
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mClientSessionId), Integer.valueOf(this.mClientSource)});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Descriptor[] descriptorArr;
        dest.writeInt(this.mClientSessionId);
        dest.writeInt(this.mClientSource);
        this.mClientFormat.writeToParcel(dest, 0);
        this.mDeviceFormat.writeToParcel(dest, 0);
        dest.writeInt(this.mPatchHandle);
        dest.writeString(this.mClientPackageName);
        dest.writeInt(this.mClientUid);
        dest.writeInt(this.mClientPortId);
        dest.writeBoolean(this.mClientSilenced);
        dest.writeInt(this.mDeviceSource);
        dest.writeInt(this.mClientEffects.length);
        int i = 0;
        while (true) {
            descriptorArr = this.mClientEffects;
            if (i >= descriptorArr.length) {
                break;
            }
            descriptorArr[i].writeToParcel(dest);
            i++;
        }
        dest.writeInt(this.mDeviceEffects.length);
        i = 0;
        while (true) {
            descriptorArr = this.mDeviceEffects;
            if (i < descriptorArr.length) {
                descriptorArr[i].writeToParcel(dest);
                i++;
            } else {
                return;
            }
        }
    }

    private AudioRecordingConfiguration(Parcel in) {
        Descriptor[] descriptorArr;
        this.mClientSessionId = in.readInt();
        this.mClientSource = in.readInt();
        this.mClientFormat = (AudioFormat) AudioFormat.CREATOR.createFromParcel(in);
        this.mDeviceFormat = (AudioFormat) AudioFormat.CREATOR.createFromParcel(in);
        this.mPatchHandle = in.readInt();
        this.mClientPackageName = in.readString();
        this.mClientUid = in.readInt();
        this.mClientPortId = in.readInt();
        this.mClientSilenced = in.readBoolean();
        this.mDeviceSource = in.readInt();
        this.mClientEffects = new Descriptor[in.readInt()];
        int i = 0;
        while (true) {
            descriptorArr = this.mClientEffects;
            if (i >= descriptorArr.length) {
                break;
            }
            descriptorArr[i] = new Descriptor(in);
            i++;
        }
        this.mDeviceEffects = new Descriptor[in.readInt()];
        i = 0;
        while (true) {
            descriptorArr = this.mDeviceEffects;
            if (i < descriptorArr.length) {
                descriptorArr[i] = new Descriptor(in);
                i++;
            } else {
                return;
            }
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AudioRecordingConfiguration)) {
            return false;
        }
        AudioRecordingConfiguration that = (AudioRecordingConfiguration) o;
        if (!(this.mClientUid == that.mClientUid && this.mClientSessionId == that.mClientSessionId && this.mClientSource == that.mClientSource && this.mPatchHandle == that.mPatchHandle && this.mClientFormat.equals(that.mClientFormat) && this.mDeviceFormat.equals(that.mDeviceFormat) && this.mClientPackageName.equals(that.mClientPackageName) && this.mClientPortId == that.mClientPortId && this.mClientSilenced == that.mClientSilenced && this.mDeviceSource == that.mDeviceSource && Arrays.equals(this.mClientEffects, that.mClientEffects) && Arrays.equals(this.mDeviceEffects, that.mDeviceEffects))) {
            z = false;
        }
        return z;
    }
}
