package android.hardware.soundtrigger;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.OsConstants;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@SystemApi
public class SoundTrigger {
    public static final int RECOGNITION_MODE_USER_AUTHENTICATION = 4;
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 2;
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = 1;
    public static final int RECOGNITION_STATUS_ABORT = 1;
    public static final int RECOGNITION_STATUS_FAILURE = 2;
    public static final int RECOGNITION_STATUS_GET_STATE_RESPONSE = 3;
    public static final int RECOGNITION_STATUS_SUCCESS = 0;
    public static final int SERVICE_STATE_DISABLED = 1;
    public static final int SERVICE_STATE_ENABLED = 0;
    public static final int SOUNDMODEL_STATUS_UPDATED = 0;
    public static final int STATUS_BAD_VALUE = (-OsConstants.EINVAL);
    public static final int STATUS_DEAD_OBJECT = (-OsConstants.EPIPE);
    public static final int STATUS_ERROR = Integer.MIN_VALUE;
    public static final int STATUS_INVALID_OPERATION = (-OsConstants.ENOSYS);
    public static final int STATUS_NO_INIT = (-OsConstants.ENODEV);
    public static final int STATUS_OK = 0;
    public static final int STATUS_PERMISSION_DENIED = (-OsConstants.EPERM);

    public static class ConfidenceLevel implements Parcelable {
        public static final Creator<ConfidenceLevel> CREATOR = new Creator<ConfidenceLevel>() {
            public ConfidenceLevel createFromParcel(Parcel in) {
                return ConfidenceLevel.fromParcel(in);
            }

            public ConfidenceLevel[] newArray(int size) {
                return new ConfidenceLevel[size];
            }
        };
        @UnsupportedAppUsage
        public final int confidenceLevel;
        @UnsupportedAppUsage
        public final int userId;

        @UnsupportedAppUsage
        public ConfidenceLevel(int userId, int confidenceLevel) {
            this.userId = userId;
            this.confidenceLevel = confidenceLevel;
        }

        private static ConfidenceLevel fromParcel(Parcel in) {
            return new ConfidenceLevel(in.readInt(), in.readInt());
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.userId);
            dest.writeInt(this.confidenceLevel);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            return (((1 * 31) + this.confidenceLevel) * 31) + this.userId;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ConfidenceLevel other = (ConfidenceLevel) obj;
            if (this.confidenceLevel == other.confidenceLevel && this.userId == other.userId) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ConfidenceLevel [userId=");
            stringBuilder.append(this.userId);
            stringBuilder.append(", confidenceLevel=");
            stringBuilder.append(this.confidenceLevel);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class RecognitionEvent {
        public static final Creator<RecognitionEvent> CREATOR = new Creator<RecognitionEvent>() {
            public RecognitionEvent createFromParcel(Parcel in) {
                return RecognitionEvent.fromParcel(in);
            }

            public RecognitionEvent[] newArray(int size) {
                return new RecognitionEvent[size];
            }
        };
        @UnsupportedAppUsage
        public final boolean captureAvailable;
        public final int captureDelayMs;
        public final AudioFormat captureFormat;
        public final int capturePreambleMs;
        @UnsupportedAppUsage
        public final int captureSession;
        @UnsupportedAppUsage
        public final byte[] data;
        @UnsupportedAppUsage
        public final int soundModelHandle;
        @UnsupportedAppUsage
        public final int status;
        public final boolean triggerInData;

        @UnsupportedAppUsage
        public RecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, AudioFormat captureFormat, byte[] data) {
            this.status = status;
            this.soundModelHandle = soundModelHandle;
            this.captureAvailable = captureAvailable;
            this.captureSession = captureSession;
            this.captureDelayMs = captureDelayMs;
            this.capturePreambleMs = capturePreambleMs;
            this.triggerInData = triggerInData;
            this.captureFormat = captureFormat;
            this.data = data;
        }

        public boolean isCaptureAvailable() {
            return this.captureAvailable;
        }

        public AudioFormat getCaptureFormat() {
            return this.captureFormat;
        }

        public int getCaptureSession() {
            return this.captureSession;
        }

        public byte[] getData() {
            return this.data;
        }

        protected static RecognitionEvent fromParcel(Parcel in) {
            AudioFormat captureFormat;
            int status = in.readInt();
            int soundModelHandle = in.readInt();
            boolean captureAvailable = in.readByte() == (byte) 1;
            int captureSession = in.readInt();
            int captureDelayMs = in.readInt();
            int capturePreambleMs = in.readInt();
            boolean triggerInData = in.readByte() == (byte) 1;
            if (in.readByte() == (byte) 1) {
                captureFormat = new Builder().setChannelMask(in.readInt()).setEncoding(in.readInt()).setSampleRate(in.readInt()).build();
            } else {
                captureFormat = null;
            }
            return new RecognitionEvent(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, in.readBlob());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeInt(this.soundModelHandle);
            dest.writeByte((byte) this.captureAvailable);
            dest.writeInt(this.captureSession);
            dest.writeInt(this.captureDelayMs);
            dest.writeInt(this.capturePreambleMs);
            dest.writeByte((byte) this.triggerInData);
            if (this.captureFormat != null) {
                dest.writeByte((byte) 1);
                dest.writeInt(this.captureFormat.getSampleRate());
                dest.writeInt(this.captureFormat.getEncoding());
                dest.writeInt(this.captureFormat.getChannelMask());
            } else {
                dest.writeByte((byte) 0);
            }
            dest.writeBlob(this.data);
        }

        public int hashCode() {
            int result = 1 * 31;
            boolean z = this.captureAvailable;
            int i = MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP;
            result = (((((((result + (z ? MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT)) * 31) + this.captureDelayMs) * 31) + this.capturePreambleMs) * 31) + this.captureSession) * 31;
            if (!this.triggerInData) {
                i = MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT;
            }
            result += i;
            AudioFormat audioFormat = this.captureFormat;
            if (audioFormat != null) {
                result = (((((result * 31) + audioFormat.getSampleRate()) * 31) + this.captureFormat.getEncoding()) * 31) + this.captureFormat.getChannelMask();
            }
            return (((((result * 31) + Arrays.hashCode(this.data)) * 31) + this.soundModelHandle) * 31) + this.status;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            RecognitionEvent other = (RecognitionEvent) obj;
            if (this.captureAvailable != other.captureAvailable || this.captureDelayMs != other.captureDelayMs || this.capturePreambleMs != other.capturePreambleMs || this.captureSession != other.captureSession || !Arrays.equals(this.data, other.data) || this.soundModelHandle != other.soundModelHandle || this.status != other.status || this.triggerInData != other.triggerInData) {
                return false;
            }
            AudioFormat audioFormat = this.captureFormat;
            if (audioFormat == null) {
                if (other.captureFormat != null) {
                    return false;
                }
            } else if (other.captureFormat != null && audioFormat.getSampleRate() == other.captureFormat.getSampleRate() && this.captureFormat.getEncoding() == other.captureFormat.getEncoding() && this.captureFormat.getChannelMask() == other.captureFormat.getChannelMask()) {
                return true;
            } else {
                return false;
            }
            return true;
        }

        public String toString() {
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("RecognitionEvent [status=");
            stringBuilder2.append(this.status);
            stringBuilder2.append(", soundModelHandle=");
            stringBuilder2.append(this.soundModelHandle);
            stringBuilder2.append(", captureAvailable=");
            stringBuilder2.append(this.captureAvailable);
            stringBuilder2.append(", captureSession=");
            stringBuilder2.append(this.captureSession);
            stringBuilder2.append(", captureDelayMs=");
            stringBuilder2.append(this.captureDelayMs);
            stringBuilder2.append(", capturePreambleMs=");
            stringBuilder2.append(this.capturePreambleMs);
            stringBuilder2.append(", triggerInData=");
            stringBuilder2.append(this.triggerInData);
            String str2 = "";
            if (this.captureFormat == null) {
                str = str2;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", sampleRate=");
                stringBuilder.append(this.captureFormat.getSampleRate());
                str = stringBuilder.toString();
            }
            stringBuilder2.append(str);
            if (this.captureFormat == null) {
                str = str2;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", encoding=");
                stringBuilder.append(this.captureFormat.getEncoding());
                str = stringBuilder.toString();
            }
            stringBuilder2.append(str);
            if (this.captureFormat != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", channelMask=");
                stringBuilder.append(this.captureFormat.getChannelMask());
                str2 = stringBuilder.toString();
            }
            stringBuilder2.append(str2);
            stringBuilder2.append(", data=");
            byte[] bArr = this.data;
            stringBuilder2.append(bArr == null ? 0 : bArr.length);
            stringBuilder2.append("]");
            return stringBuilder2.toString();
        }
    }

    public static class GenericRecognitionEvent extends RecognitionEvent implements Parcelable {
        public static final Creator<GenericRecognitionEvent> CREATOR = new Creator<GenericRecognitionEvent>() {
            public GenericRecognitionEvent createFromParcel(Parcel in) {
                return GenericRecognitionEvent.fromParcelForGeneric(in);
            }

            public GenericRecognitionEvent[] newArray(int size) {
                return new GenericRecognitionEvent[size];
            }
        };

        @UnsupportedAppUsage
        public GenericRecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, AudioFormat captureFormat, byte[] data) {
            super(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
        }

        private static GenericRecognitionEvent fromParcelForGeneric(Parcel in) {
            RecognitionEvent event = RecognitionEvent.fromParcel(in);
            return new GenericRecognitionEvent(event.status, event.soundModelHandle, event.captureAvailable, event.captureSession, event.captureDelayMs, event.capturePreambleMs, event.triggerInData, event.captureFormat, event.data);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            RecognitionEvent other = (RecognitionEvent) obj;
            return super.equals(obj);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GenericRecognitionEvent ::");
            stringBuilder.append(super.toString());
            return stringBuilder.toString();
        }
    }

    public static class SoundModel {
        public static final int TYPE_GENERIC_SOUND = 1;
        public static final int TYPE_KEYPHRASE = 0;
        public static final int TYPE_UNKNOWN = -1;
        @UnsupportedAppUsage
        public final byte[] data;
        public final int type;
        @UnsupportedAppUsage
        public final UUID uuid;
        @UnsupportedAppUsage
        public final UUID vendorUuid;

        public SoundModel(UUID uuid, UUID vendorUuid, int type, byte[] data) {
            this.uuid = uuid;
            this.vendorUuid = vendorUuid;
            this.type = type;
            this.data = data;
        }

        public int hashCode() {
            int hashCode = ((((1 * 31) + Arrays.hashCode(this.data)) * 31) + this.type) * 31;
            UUID uuid = this.uuid;
            int i = 0;
            int hashCode2 = (hashCode + (uuid == null ? 0 : uuid.hashCode())) * 31;
            uuid = this.vendorUuid;
            if (uuid != null) {
                i = uuid.hashCode();
            }
            return hashCode2 + i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof SoundModel)) {
                return false;
            }
            SoundModel other = (SoundModel) obj;
            if (!Arrays.equals(this.data, other.data) || this.type != other.type) {
                return false;
            }
            UUID uuid = this.uuid;
            if (uuid == null) {
                if (other.uuid != null) {
                    return false;
                }
            } else if (!uuid.equals(other.uuid)) {
                return false;
            }
            uuid = this.vendorUuid;
            if (uuid == null) {
                if (other.vendorUuid != null) {
                    return false;
                }
            } else if (!uuid.equals(other.vendorUuid)) {
                return false;
            }
            return true;
        }
    }

    public static class GenericSoundModel extends SoundModel implements Parcelable {
        public static final Creator<GenericSoundModel> CREATOR = new Creator<GenericSoundModel>() {
            public GenericSoundModel createFromParcel(Parcel in) {
                return GenericSoundModel.fromParcel(in);
            }

            public GenericSoundModel[] newArray(int size) {
                return new GenericSoundModel[size];
            }
        };

        @UnsupportedAppUsage
        public GenericSoundModel(UUID uuid, UUID vendorUuid, byte[] data) {
            super(uuid, vendorUuid, 1, data);
        }

        public int describeContents() {
            return 0;
        }

        private static GenericSoundModel fromParcel(Parcel in) {
            UUID uuid = UUID.fromString(in.readString());
            UUID vendorUuid = null;
            if (in.readInt() >= 0) {
                vendorUuid = UUID.fromString(in.readString());
            }
            return new GenericSoundModel(uuid, vendorUuid, in.readBlob());
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uuid.toString());
            if (this.vendorUuid == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(this.vendorUuid.toString().length());
                dest.writeString(this.vendorUuid.toString());
            }
            dest.writeBlob(this.data);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GenericSoundModel [uuid=");
            stringBuilder.append(this.uuid);
            stringBuilder.append(", vendorUuid=");
            stringBuilder.append(this.vendorUuid);
            stringBuilder.append(", type=");
            stringBuilder.append(this.type);
            stringBuilder.append(", data=");
            stringBuilder.append(this.data == null ? 0 : this.data.length);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class Keyphrase implements Parcelable {
        public static final Creator<Keyphrase> CREATOR = new Creator<Keyphrase>() {
            public Keyphrase createFromParcel(Parcel in) {
                return Keyphrase.fromParcel(in);
            }

            public Keyphrase[] newArray(int size) {
                return new Keyphrase[size];
            }
        };
        @UnsupportedAppUsage
        public final int id;
        @UnsupportedAppUsage
        public final String locale;
        @UnsupportedAppUsage
        public final int recognitionModes;
        @UnsupportedAppUsage
        public final String text;
        @UnsupportedAppUsage
        public final int[] users;

        @UnsupportedAppUsage
        public Keyphrase(int id, int recognitionModes, String locale, String text, int[] users) {
            this.id = id;
            this.recognitionModes = recognitionModes;
            this.locale = locale;
            this.text = text;
            this.users = users;
        }

        private static Keyphrase fromParcel(Parcel in) {
            int[] users;
            int id = in.readInt();
            int recognitionModes = in.readInt();
            String locale = in.readString();
            String text = in.readString();
            int numUsers = in.readInt();
            if (numUsers >= 0) {
                int[] users2 = new int[numUsers];
                in.readIntArray(users2);
                users = users2;
            } else {
                users = null;
            }
            return new Keyphrase(id, recognitionModes, locale, text, users);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.recognitionModes);
            dest.writeString(this.locale);
            dest.writeString(this.text);
            int[] iArr = this.users;
            if (iArr != null) {
                dest.writeInt(iArr.length);
                dest.writeIntArray(this.users);
                return;
            }
            dest.writeInt(-1);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            int result = 1 * 31;
            String str = this.text;
            int i = 0;
            result = (((result + (str == null ? 0 : str.hashCode())) * 31) + this.id) * 31;
            str = this.locale;
            if (str != null) {
                i = str.hashCode();
            }
            return ((((result + i) * 31) + this.recognitionModes) * 31) + Arrays.hashCode(this.users);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Keyphrase other = (Keyphrase) obj;
            String str = this.text;
            if (str == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!str.equals(other.text)) {
                return false;
            }
            if (this.id != other.id) {
                return false;
            }
            str = this.locale;
            if (str == null) {
                if (other.locale != null) {
                    return false;
                }
            } else if (!str.equals(other.locale)) {
                return false;
            }
            if (this.recognitionModes == other.recognitionModes && Arrays.equals(this.users, other.users)) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Keyphrase [id=");
            stringBuilder.append(this.id);
            stringBuilder.append(", recognitionModes=");
            stringBuilder.append(this.recognitionModes);
            stringBuilder.append(", locale=");
            stringBuilder.append(this.locale);
            stringBuilder.append(", text=");
            stringBuilder.append(this.text);
            stringBuilder.append(", users=");
            stringBuilder.append(Arrays.toString(this.users));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class KeyphraseRecognitionEvent extends RecognitionEvent implements Parcelable {
        public static final Creator<KeyphraseRecognitionEvent> CREATOR = new Creator<KeyphraseRecognitionEvent>() {
            public KeyphraseRecognitionEvent createFromParcel(Parcel in) {
                return KeyphraseRecognitionEvent.fromParcelForKeyphrase(in);
            }

            public KeyphraseRecognitionEvent[] newArray(int size) {
                return new KeyphraseRecognitionEvent[size];
            }
        };
        @UnsupportedAppUsage
        public final KeyphraseRecognitionExtra[] keyphraseExtras;

        @UnsupportedAppUsage
        public KeyphraseRecognitionEvent(int status, int soundModelHandle, boolean captureAvailable, int captureSession, int captureDelayMs, int capturePreambleMs, boolean triggerInData, AudioFormat captureFormat, byte[] data, KeyphraseRecognitionExtra[] keyphraseExtras) {
            super(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
            this.keyphraseExtras = keyphraseExtras;
        }

        private static KeyphraseRecognitionEvent fromParcelForKeyphrase(Parcel in) {
            AudioFormat captureFormat;
            int status = in.readInt();
            int soundModelHandle = in.readInt();
            boolean captureAvailable = in.readByte() == (byte) 1;
            int captureSession = in.readInt();
            int captureDelayMs = in.readInt();
            int capturePreambleMs = in.readInt();
            boolean triggerInData = in.readByte() == (byte) 1;
            if (in.readByte() == (byte) 1) {
                captureFormat = new Builder().setChannelMask(in.readInt()).setEncoding(in.readInt()).setSampleRate(in.readInt()).build();
            } else {
                captureFormat = null;
            }
            return new KeyphraseRecognitionEvent(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, in.readBlob(), (KeyphraseRecognitionExtra[]) in.createTypedArray(KeyphraseRecognitionExtra.CREATOR));
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeInt(this.soundModelHandle);
            dest.writeByte((byte) this.captureAvailable);
            dest.writeInt(this.captureSession);
            dest.writeInt(this.captureDelayMs);
            dest.writeInt(this.capturePreambleMs);
            dest.writeByte((byte) this.triggerInData);
            if (this.captureFormat != null) {
                dest.writeByte((byte) 1);
                dest.writeInt(this.captureFormat.getSampleRate());
                dest.writeInt(this.captureFormat.getEncoding());
                dest.writeInt(this.captureFormat.getChannelMask());
            } else {
                dest.writeByte((byte) 0);
            }
            dest.writeBlob(this.data);
            dest.writeTypedArray(this.keyphraseExtras, flags);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            return (super.hashCode() * 31) + Arrays.hashCode(this.keyphraseExtras);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            if (Arrays.equals(this.keyphraseExtras, ((KeyphraseRecognitionEvent) obj).keyphraseExtras)) {
                return true;
            }
            return false;
        }

        public String toString() {
            String str;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("KeyphraseRecognitionEvent [keyphraseExtras=");
            stringBuilder2.append(Arrays.toString(this.keyphraseExtras));
            stringBuilder2.append(", status=");
            stringBuilder2.append(this.status);
            stringBuilder2.append(", soundModelHandle=");
            stringBuilder2.append(this.soundModelHandle);
            stringBuilder2.append(", captureAvailable=");
            stringBuilder2.append(this.captureAvailable);
            stringBuilder2.append(", captureSession=");
            stringBuilder2.append(this.captureSession);
            stringBuilder2.append(", captureDelayMs=");
            stringBuilder2.append(this.captureDelayMs);
            stringBuilder2.append(", capturePreambleMs=");
            stringBuilder2.append(this.capturePreambleMs);
            stringBuilder2.append(", triggerInData=");
            stringBuilder2.append(this.triggerInData);
            String str2 = "";
            if (this.captureFormat == null) {
                str = str2;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", sampleRate=");
                stringBuilder.append(this.captureFormat.getSampleRate());
                str = stringBuilder.toString();
            }
            stringBuilder2.append(str);
            if (this.captureFormat == null) {
                str = str2;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", encoding=");
                stringBuilder.append(this.captureFormat.getEncoding());
                str = stringBuilder.toString();
            }
            stringBuilder2.append(str);
            if (this.captureFormat != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(", channelMask=");
                stringBuilder.append(this.captureFormat.getChannelMask());
                str2 = stringBuilder.toString();
            }
            stringBuilder2.append(str2);
            stringBuilder2.append(", data=");
            stringBuilder2.append(this.data == null ? 0 : this.data.length);
            stringBuilder2.append("]");
            return stringBuilder2.toString();
        }
    }

    public static class KeyphraseRecognitionExtra implements Parcelable {
        public static final Creator<KeyphraseRecognitionExtra> CREATOR = new Creator<KeyphraseRecognitionExtra>() {
            public KeyphraseRecognitionExtra createFromParcel(Parcel in) {
                return KeyphraseRecognitionExtra.fromParcel(in);
            }

            public KeyphraseRecognitionExtra[] newArray(int size) {
                return new KeyphraseRecognitionExtra[size];
            }
        };
        @UnsupportedAppUsage
        public final int coarseConfidenceLevel;
        @UnsupportedAppUsage
        public final ConfidenceLevel[] confidenceLevels;
        @UnsupportedAppUsage
        public final int id;
        @UnsupportedAppUsage
        public final int recognitionModes;

        @UnsupportedAppUsage
        public KeyphraseRecognitionExtra(int id, int recognitionModes, int coarseConfidenceLevel, ConfidenceLevel[] confidenceLevels) {
            this.id = id;
            this.recognitionModes = recognitionModes;
            this.coarseConfidenceLevel = coarseConfidenceLevel;
            this.confidenceLevels = confidenceLevels;
        }

        private static KeyphraseRecognitionExtra fromParcel(Parcel in) {
            return new KeyphraseRecognitionExtra(in.readInt(), in.readInt(), in.readInt(), (ConfidenceLevel[]) in.createTypedArray(ConfidenceLevel.CREATOR));
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.recognitionModes);
            dest.writeInt(this.coarseConfidenceLevel);
            dest.writeTypedArray(this.confidenceLevels, flags);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            return (((((((1 * 31) + Arrays.hashCode(this.confidenceLevels)) * 31) + this.id) * 31) + this.recognitionModes) * 31) + this.coarseConfidenceLevel;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            KeyphraseRecognitionExtra other = (KeyphraseRecognitionExtra) obj;
            if (Arrays.equals(this.confidenceLevels, other.confidenceLevels) && this.id == other.id && this.recognitionModes == other.recognitionModes && this.coarseConfidenceLevel == other.coarseConfidenceLevel) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("KeyphraseRecognitionExtra [id=");
            stringBuilder.append(this.id);
            stringBuilder.append(", recognitionModes=");
            stringBuilder.append(this.recognitionModes);
            stringBuilder.append(", coarseConfidenceLevel=");
            stringBuilder.append(this.coarseConfidenceLevel);
            stringBuilder.append(", confidenceLevels=");
            stringBuilder.append(Arrays.toString(this.confidenceLevels));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class KeyphraseSoundModel extends SoundModel implements Parcelable {
        public static final Creator<KeyphraseSoundModel> CREATOR = new Creator<KeyphraseSoundModel>() {
            public KeyphraseSoundModel createFromParcel(Parcel in) {
                return KeyphraseSoundModel.fromParcel(in);
            }

            public KeyphraseSoundModel[] newArray(int size) {
                return new KeyphraseSoundModel[size];
            }
        };
        @UnsupportedAppUsage
        public final Keyphrase[] keyphrases;

        @UnsupportedAppUsage
        public KeyphraseSoundModel(UUID uuid, UUID vendorUuid, byte[] data, Keyphrase[] keyphrases) {
            super(uuid, vendorUuid, 0, data);
            this.keyphrases = keyphrases;
        }

        private static KeyphraseSoundModel fromParcel(Parcel in) {
            UUID uuid = UUID.fromString(in.readString());
            UUID vendorUuid = null;
            if (in.readInt() >= 0) {
                vendorUuid = UUID.fromString(in.readString());
            }
            return new KeyphraseSoundModel(uuid, vendorUuid, in.readBlob(), (Keyphrase[]) in.createTypedArray(Keyphrase.CREATOR));
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uuid.toString());
            if (this.vendorUuid == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(this.vendorUuid.toString().length());
                dest.writeString(this.vendorUuid.toString());
            }
            dest.writeBlob(this.data);
            dest.writeTypedArray(this.keyphrases, flags);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("KeyphraseSoundModel [keyphrases=");
            stringBuilder.append(Arrays.toString(this.keyphrases));
            stringBuilder.append(", uuid=");
            stringBuilder.append(this.uuid);
            stringBuilder.append(", vendorUuid=");
            stringBuilder.append(this.vendorUuid);
            stringBuilder.append(", type=");
            stringBuilder.append(this.type);
            stringBuilder.append(", data=");
            stringBuilder.append(this.data == null ? 0 : this.data.length);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }

        public int hashCode() {
            return (super.hashCode() * 31) + Arrays.hashCode(this.keyphrases);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || !(obj instanceof KeyphraseSoundModel)) {
                return false;
            }
            if (Arrays.equals(this.keyphrases, ((KeyphraseSoundModel) obj).keyphrases)) {
                return true;
            }
            return false;
        }
    }

    public static class ModuleProperties implements Parcelable {
        public static final Creator<ModuleProperties> CREATOR = new Creator<ModuleProperties>() {
            public ModuleProperties createFromParcel(Parcel in) {
                return ModuleProperties.fromParcel(in);
            }

            public ModuleProperties[] newArray(int size) {
                return new ModuleProperties[size];
            }
        };
        public final String description;
        @UnsupportedAppUsage
        public final int id;
        public final String implementor;
        public final int maxBufferMs;
        public final int maxKeyphrases;
        @UnsupportedAppUsage
        public final int maxSoundModels;
        public final int maxUsers;
        public final int powerConsumptionMw;
        public final int recognitionModes;
        public final boolean returnsTriggerInEvent;
        public final boolean supportsCaptureTransition;
        public final boolean supportsConcurrentCapture;
        @UnsupportedAppUsage
        public final UUID uuid;
        public final int version;

        @UnsupportedAppUsage
        ModuleProperties(int id, String implementor, String description, String uuid, int version, int maxSoundModels, int maxKeyphrases, int maxUsers, int recognitionModes, boolean supportsCaptureTransition, int maxBufferMs, boolean supportsConcurrentCapture, int powerConsumptionMw, boolean returnsTriggerInEvent) {
            this.id = id;
            this.implementor = implementor;
            this.description = description;
            this.uuid = UUID.fromString(uuid);
            this.version = version;
            this.maxSoundModels = maxSoundModels;
            this.maxKeyphrases = maxKeyphrases;
            this.maxUsers = maxUsers;
            this.recognitionModes = recognitionModes;
            this.supportsCaptureTransition = supportsCaptureTransition;
            this.maxBufferMs = maxBufferMs;
            this.supportsConcurrentCapture = supportsConcurrentCapture;
            this.powerConsumptionMw = powerConsumptionMw;
            this.returnsTriggerInEvent = returnsTriggerInEvent;
        }

        private static ModuleProperties fromParcel(Parcel in) {
            return new ModuleProperties(in.readInt(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readByte() == (byte) 1, in.readInt(), in.readByte() == (byte) 1, in.readInt(), in.readByte() == (byte) 1);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.implementor);
            dest.writeString(this.description);
            dest.writeString(this.uuid.toString());
            dest.writeInt(this.version);
            dest.writeInt(this.maxSoundModels);
            dest.writeInt(this.maxKeyphrases);
            dest.writeInt(this.maxUsers);
            dest.writeInt(this.recognitionModes);
            dest.writeByte((byte) this.supportsCaptureTransition);
            dest.writeInt(this.maxBufferMs);
            dest.writeByte((byte) this.supportsConcurrentCapture);
            dest.writeInt(this.powerConsumptionMw);
            dest.writeByte((byte) this.returnsTriggerInEvent);
        }

        public int describeContents() {
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ModuleProperties [id=");
            stringBuilder.append(this.id);
            stringBuilder.append(", implementor=");
            stringBuilder.append(this.implementor);
            stringBuilder.append(", description=");
            stringBuilder.append(this.description);
            stringBuilder.append(", uuid=");
            stringBuilder.append(this.uuid);
            stringBuilder.append(", version=");
            stringBuilder.append(this.version);
            stringBuilder.append(", maxSoundModels=");
            stringBuilder.append(this.maxSoundModels);
            stringBuilder.append(", maxKeyphrases=");
            stringBuilder.append(this.maxKeyphrases);
            stringBuilder.append(", maxUsers=");
            stringBuilder.append(this.maxUsers);
            stringBuilder.append(", recognitionModes=");
            stringBuilder.append(this.recognitionModes);
            stringBuilder.append(", supportsCaptureTransition=");
            stringBuilder.append(this.supportsCaptureTransition);
            stringBuilder.append(", maxBufferMs=");
            stringBuilder.append(this.maxBufferMs);
            stringBuilder.append(", supportsConcurrentCapture=");
            stringBuilder.append(this.supportsConcurrentCapture);
            stringBuilder.append(", powerConsumptionMw=");
            stringBuilder.append(this.powerConsumptionMw);
            stringBuilder.append(", returnsTriggerInEvent=");
            stringBuilder.append(this.returnsTriggerInEvent);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class RecognitionConfig implements Parcelable {
        public static final Creator<RecognitionConfig> CREATOR = new Creator<RecognitionConfig>() {
            public RecognitionConfig createFromParcel(Parcel in) {
                return RecognitionConfig.fromParcel(in);
            }

            public RecognitionConfig[] newArray(int size) {
                return new RecognitionConfig[size];
            }
        };
        public final boolean allowMultipleTriggers;
        @UnsupportedAppUsage
        public final boolean captureRequested;
        @UnsupportedAppUsage
        public final byte[] data;
        @UnsupportedAppUsage
        public final KeyphraseRecognitionExtra[] keyphrases;

        @UnsupportedAppUsage
        public RecognitionConfig(boolean captureRequested, boolean allowMultipleTriggers, KeyphraseRecognitionExtra[] keyphrases, byte[] data) {
            this.captureRequested = captureRequested;
            this.allowMultipleTriggers = allowMultipleTriggers;
            this.keyphrases = keyphrases;
            this.data = data;
        }

        private static RecognitionConfig fromParcel(Parcel in) {
            boolean allowMultipleTriggers = false;
            boolean captureRequested = in.readByte() == (byte) 1;
            if (in.readByte() == (byte) 1) {
                allowMultipleTriggers = true;
            }
            return new RecognitionConfig(captureRequested, allowMultipleTriggers, (KeyphraseRecognitionExtra[]) in.createTypedArray(KeyphraseRecognitionExtra.CREATOR), in.readBlob());
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) this.captureRequested);
            dest.writeByte((byte) this.allowMultipleTriggers);
            dest.writeTypedArray(this.keyphrases, flags);
            dest.writeBlob(this.data);
        }

        public int describeContents() {
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RecognitionConfig [captureRequested=");
            stringBuilder.append(this.captureRequested);
            stringBuilder.append(", allowMultipleTriggers=");
            stringBuilder.append(this.allowMultipleTriggers);
            stringBuilder.append(", keyphrases=");
            stringBuilder.append(Arrays.toString(this.keyphrases));
            stringBuilder.append(", data=");
            stringBuilder.append(Arrays.toString(this.data));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static class SoundModelEvent implements Parcelable {
        public static final Creator<SoundModelEvent> CREATOR = new Creator<SoundModelEvent>() {
            public SoundModelEvent createFromParcel(Parcel in) {
                return SoundModelEvent.fromParcel(in);
            }

            public SoundModelEvent[] newArray(int size) {
                return new SoundModelEvent[size];
            }
        };
        public final byte[] data;
        public final int soundModelHandle;
        public final int status;

        @UnsupportedAppUsage
        SoundModelEvent(int status, int soundModelHandle, byte[] data) {
            this.status = status;
            this.soundModelHandle = soundModelHandle;
            this.data = data;
        }

        private static SoundModelEvent fromParcel(Parcel in) {
            return new SoundModelEvent(in.readInt(), in.readInt(), in.readBlob());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeInt(this.soundModelHandle);
            dest.writeBlob(this.data);
        }

        public int hashCode() {
            return (((((1 * 31) + Arrays.hashCode(this.data)) * 31) + this.soundModelHandle) * 31) + this.status;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SoundModelEvent other = (SoundModelEvent) obj;
            if (Arrays.equals(this.data, other.data) && this.soundModelHandle == other.soundModelHandle && this.status == other.status) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SoundModelEvent [status=");
            stringBuilder.append(this.status);
            stringBuilder.append(", soundModelHandle=");
            stringBuilder.append(this.soundModelHandle);
            stringBuilder.append(", data=");
            byte[] bArr = this.data;
            stringBuilder.append(bArr == null ? 0 : bArr.length);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public interface StatusListener {
        void onRecognition(RecognitionEvent recognitionEvent);

        void onServiceDied();

        void onServiceStateChange(int i);

        void onSoundModelUpdate(SoundModelEvent soundModelEvent);
    }

    private static native int listModules(String str, ArrayList<ModuleProperties> arrayList);

    private SoundTrigger() {
    }

    static String getCurrentOpPackageName() {
        String packageName = ActivityThread.currentOpPackageName();
        if (packageName == null) {
            return "";
        }
        return packageName;
    }

    @UnsupportedAppUsage
    public static int listModules(ArrayList<ModuleProperties> modules) {
        return listModules(getCurrentOpPackageName(), modules);
    }

    @UnsupportedAppUsage
    public static SoundTriggerModule attachModule(int moduleId, StatusListener listener, Handler handler) {
        if (listener == null) {
            return null;
        }
        return new SoundTriggerModule(moduleId, listener, handler);
    }
}
