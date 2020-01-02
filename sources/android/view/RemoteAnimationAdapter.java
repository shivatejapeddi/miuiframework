package android.view;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.IRemoteAnimationRunner.Stub;

public class RemoteAnimationAdapter implements Parcelable {
    public static final Creator<RemoteAnimationAdapter> CREATOR = new Creator<RemoteAnimationAdapter>() {
        public RemoteAnimationAdapter createFromParcel(Parcel in) {
            return new RemoteAnimationAdapter(in);
        }

        public RemoteAnimationAdapter[] newArray(int size) {
            return new RemoteAnimationAdapter[size];
        }
    };
    private int mCallingPid;
    private final boolean mChangeNeedsSnapshot;
    private final long mDuration;
    private final IRemoteAnimationRunner mRunner;
    private final long mStatusBarTransitionDelay;

    @UnsupportedAppUsage
    public RemoteAnimationAdapter(IRemoteAnimationRunner runner, long duration, long statusBarTransitionDelay, boolean changeNeedsSnapshot) {
        this.mRunner = runner;
        this.mDuration = duration;
        this.mChangeNeedsSnapshot = changeNeedsSnapshot;
        this.mStatusBarTransitionDelay = statusBarTransitionDelay;
    }

    @UnsupportedAppUsage
    public RemoteAnimationAdapter(IRemoteAnimationRunner runner, long duration, long statusBarTransitionDelay) {
        this(runner, duration, statusBarTransitionDelay, false);
    }

    public RemoteAnimationAdapter(Parcel in) {
        this.mRunner = Stub.asInterface(in.readStrongBinder());
        this.mDuration = in.readLong();
        this.mStatusBarTransitionDelay = in.readLong();
        this.mChangeNeedsSnapshot = in.readBoolean();
    }

    public IRemoteAnimationRunner getRunner() {
        return this.mRunner;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public long getStatusBarTransitionDelay() {
        return this.mStatusBarTransitionDelay;
    }

    public boolean getChangeNeedsSnapshot() {
        return this.mChangeNeedsSnapshot;
    }

    public void setCallingPid(int pid) {
        this.mCallingPid = pid;
    }

    public int getCallingPid() {
        return this.mCallingPid;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongInterface(this.mRunner);
        dest.writeLong(this.mDuration);
        dest.writeLong(this.mStatusBarTransitionDelay);
        dest.writeBoolean(this.mChangeNeedsSnapshot);
    }
}
