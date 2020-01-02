package android.os;

import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.IBinder.DeathRecipient;
import android.os.IExternalVibrationController.Stub;
import android.os.Parcelable.Creator;
import android.util.Slog;
import com.android.internal.util.Preconditions;

public class ExternalVibration implements Parcelable {
    public static final Creator<ExternalVibration> CREATOR = new Creator<ExternalVibration>() {
        public ExternalVibration createFromParcel(Parcel in) {
            return new ExternalVibration(in, null);
        }

        public ExternalVibration[] newArray(int size) {
            return new ExternalVibration[size];
        }
    };
    private static final String TAG = "ExternalVibration";
    private AudioAttributes mAttrs;
    private IExternalVibrationController mController;
    private String mPkg;
    private IBinder mToken;
    private int mUid;

    /* synthetic */ ExternalVibration(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ExternalVibration(int uid, String pkg, AudioAttributes attrs, IExternalVibrationController controller) {
        this.mUid = uid;
        this.mPkg = (String) Preconditions.checkNotNull(pkg);
        this.mAttrs = (AudioAttributes) Preconditions.checkNotNull(attrs);
        this.mController = (IExternalVibrationController) Preconditions.checkNotNull(controller);
        this.mToken = new Binder();
    }

    private ExternalVibration(Parcel in) {
        this.mUid = in.readInt();
        this.mPkg = in.readString();
        this.mAttrs = readAudioAttributes(in);
        this.mController = Stub.asInterface(in.readStrongBinder());
        this.mToken = in.readStrongBinder();
    }

    private AudioAttributes readAudioAttributes(Parcel in) {
        int usage = in.readInt();
        int contentType = in.readInt();
        int capturePreset = in.readInt();
        return new Builder().setUsage(usage).setContentType(contentType).setCapturePreset(capturePreset).setFlags(in.readInt()).build();
    }

    public int getUid() {
        return this.mUid;
    }

    public String getPackage() {
        return this.mPkg;
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAttrs;
    }

    public boolean mute() {
        try {
            this.mController.mute();
            return true;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to mute vibration stream: ");
            stringBuilder.append(this);
            Slog.wtf(TAG, stringBuilder.toString(), e);
            return false;
        }
    }

    public boolean unmute() {
        try {
            this.mController.unmute();
            return true;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to unmute vibration stream: ");
            stringBuilder.append(this);
            Slog.wtf(TAG, stringBuilder.toString(), e);
            return false;
        }
    }

    public void linkToDeath(DeathRecipient recipient) {
        try {
            this.mToken.linkToDeath(recipient, 0);
        } catch (RemoteException e) {
        }
    }

    public void unlinkToDeath(DeathRecipient recipient) {
        this.mToken.unlinkToDeath(recipient, 0);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ExternalVibration)) {
            return false;
        }
        return this.mToken.equals(((ExternalVibration) o).mToken);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ExternalVibration{uid=");
        stringBuilder.append(this.mUid);
        stringBuilder.append(", pkg=");
        stringBuilder.append(this.mPkg);
        stringBuilder.append(", attrs=");
        stringBuilder.append(this.mAttrs);
        stringBuilder.append(", controller=");
        stringBuilder.append(this.mController);
        stringBuilder.append("token=");
        stringBuilder.append(this.mController);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mUid);
        out.writeString(this.mPkg);
        writeAudioAttributes(this.mAttrs, out, flags);
        out.writeParcelable(this.mAttrs, flags);
        out.writeStrongBinder(this.mController.asBinder());
        out.writeStrongBinder(this.mToken);
    }

    private static void writeAudioAttributes(AudioAttributes attrs, Parcel out, int flags) {
        out.writeInt(attrs.getUsage());
        out.writeInt(attrs.getContentType());
        out.writeInt(attrs.getCapturePreset());
        out.writeInt(attrs.getAllFlags());
    }

    public int describeContents() {
        return 0;
    }
}
