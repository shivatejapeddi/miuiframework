package android.os;

import android.os.IMessenger.Stub;
import android.os.Parcelable.Creator;

public final class Messenger implements Parcelable {
    public static final Creator<Messenger> CREATOR = new Creator<Messenger>() {
        public Messenger createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            return target != null ? new Messenger(target) : null;
        }

        public Messenger[] newArray(int size) {
            return new Messenger[size];
        }
    };
    private final IMessenger mTarget;

    public Messenger(Handler target) {
        this.mTarget = target.getIMessenger();
    }

    public void send(Message message) throws RemoteException {
        this.mTarget.send(message);
    }

    public IBinder getBinder() {
        return this.mTarget.asBinder();
    }

    public boolean equals(Object otherObj) {
        boolean z = false;
        if (otherObj == null) {
            return false;
        }
        try {
            z = this.mTarget.asBinder().equals(((Messenger) otherObj).mTarget.asBinder());
            return z;
        } catch (ClassCastException e) {
            return z;
        }
    }

    public int hashCode() {
        return this.mTarget.asBinder().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(this.mTarget.asBinder());
    }

    public static void writeMessengerOrNullToParcel(Messenger messenger, Parcel out) {
        IBinder asBinder;
        if (messenger != null) {
            asBinder = messenger.mTarget.asBinder();
        } else {
            asBinder = null;
        }
        out.writeStrongBinder(asBinder);
    }

    public static Messenger readMessengerOrNullFromParcel(Parcel in) {
        IBinder b = in.readStrongBinder();
        return b != null ? new Messenger(b) : null;
    }

    public Messenger(IBinder target) {
        this.mTarget = Stub.asInterface(target);
    }
}
