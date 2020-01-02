package android.app.job;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class JobSnapshot implements Parcelable {
    public static final Creator<JobSnapshot> CREATOR = new Creator<JobSnapshot>() {
        public JobSnapshot createFromParcel(Parcel in) {
            return new JobSnapshot(in);
        }

        public JobSnapshot[] newArray(int size) {
            return new JobSnapshot[size];
        }
    };
    private final boolean mIsRunnable;
    private final JobInfo mJob;
    private final int mSatisfiedConstraints;

    public JobSnapshot(JobInfo info, int satisfiedMask, boolean runnable) {
        this.mJob = info;
        this.mSatisfiedConstraints = satisfiedMask;
        this.mIsRunnable = runnable;
    }

    public JobSnapshot(Parcel in) {
        this.mJob = (JobInfo) JobInfo.CREATOR.createFromParcel(in);
        this.mSatisfiedConstraints = in.readInt();
        this.mIsRunnable = in.readBoolean();
    }

    private boolean satisfied(int flag) {
        return (this.mSatisfiedConstraints & flag) != 0;
    }

    public JobInfo getJobInfo() {
        return this.mJob;
    }

    public boolean isRunnable() {
        return this.mIsRunnable;
    }

    public boolean isChargingSatisfied() {
        if (!this.mJob.isRequireCharging() || satisfied(1)) {
            return true;
        }
        return false;
    }

    public boolean isBatteryNotLowSatisfied() {
        return !this.mJob.isRequireBatteryNotLow() || satisfied(2);
    }

    public boolean isRequireDeviceIdleSatisfied() {
        return !this.mJob.isRequireDeviceIdle() || satisfied(4);
    }

    public boolean isRequireStorageNotLowSatisfied() {
        return !this.mJob.isRequireStorageNotLow() || satisfied(8);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.mJob.writeToParcel(out, flags);
        out.writeInt(this.mSatisfiedConstraints);
        out.writeBoolean(this.mIsRunnable);
    }
}
