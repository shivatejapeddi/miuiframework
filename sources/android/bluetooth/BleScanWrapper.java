package android.bluetooth;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.bluetooth.le.ResultStorageDescriptor;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;
import java.util.Objects;

public final class BleScanWrapper implements Parcelable {
    public static final Creator<BleScanWrapper> CREATOR = new Creator<BleScanWrapper>() {
        public BleScanWrapper[] newArray(int size) {
            return new BleScanWrapper[size];
        }

        public BleScanWrapper createFromParcel(Parcel in) {
            return new BleScanWrapper(in, null);
        }
    };
    private final int mClientIf;
    private final List<ScanFilter> mFilters;
    private final boolean mIsFlagIntent;
    private final String mOpPackageName;
    private final PendingIntent mPendingIntent;
    private final List<List<ResultStorageDescriptor>> mResultStorages;
    private final ScanSettings mSettings;
    private final WorkSource mWorkSource;

    /* synthetic */ BleScanWrapper(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public BleScanWrapper(int clientIf, ScanSettings settings, List<ScanFilter> filters, WorkSource workSource, List<List<ResultStorageDescriptor>> resultStorages, String opPackageName) {
        this.mIsFlagIntent = false;
        this.mPendingIntent = null;
        this.mClientIf = clientIf;
        this.mSettings = settings;
        this.mFilters = filters;
        this.mWorkSource = workSource;
        this.mResultStorages = resultStorages;
        this.mOpPackageName = opPackageName;
    }

    public BleScanWrapper(PendingIntent pi, ScanSettings settings, List<ScanFilter> filters, String opPackageName) {
        this.mIsFlagIntent = true;
        this.mPendingIntent = pi;
        this.mClientIf = -1;
        this.mSettings = settings;
        this.mFilters = filters;
        this.mWorkSource = null;
        this.mResultStorages = null;
        this.mOpPackageName = opPackageName;
    }

    public void startScan(IBluetoothGatt gatt) throws RemoteException {
        if (this.mIsFlagIntent) {
            gatt.startScanForIntent(this.mPendingIntent, this.mSettings, this.mFilters, ActivityThread.currentOpPackageName());
            return;
        }
        gatt.startScan(this.mClientIf, this.mSettings, this.mFilters, this.mResultStorages, ActivityThread.currentOpPackageName());
    }

    public void stopScan(IBluetoothGatt gatt) throws RemoteException {
        if (this.mIsFlagIntent) {
            gatt.stopScanForIntent(this.mPendingIntent, ActivityThread.currentOpPackageName());
        } else {
            gatt.stopScan(this.mClientIf);
        }
    }

    private BleScanWrapper(Parcel in) {
        this.mClientIf = in.readInt();
        if (in.readInt() != 0) {
            this.mSettings = (ScanSettings) ScanSettings.CREATOR.createFromParcel(in);
        } else {
            this.mSettings = null;
        }
        this.mFilters = in.createTypedArrayList(ScanFilter.CREATOR);
        if (in.readInt() != 0) {
            this.mWorkSource = (WorkSource) WorkSource.CREATOR.createFromParcel(in);
        } else {
            this.mWorkSource = null;
        }
        this.mResultStorages = in.readArrayList(getClass().getClassLoader());
        this.mOpPackageName = in.readString();
        this.mIsFlagIntent = in.readBoolean();
        if (this.mIsFlagIntent) {
            this.mPendingIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(in);
        } else {
            this.mPendingIntent = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mClientIf);
        if (this.mSettings != null) {
            dest.writeInt(1);
            this.mSettings.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeTypedList(this.mFilters);
        if (this.mWorkSource != null) {
            dest.writeInt(1);
            this.mWorkSource.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeList(this.mResultStorages);
        dest.writeString(this.mOpPackageName);
        dest.writeBoolean(this.mIsFlagIntent);
        if (this.mIsFlagIntent) {
            this.mPendingIntent.writeToParcel(dest, 0);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BleScanWrapper.class.getSimpleName());
        stringBuilder.append("[mClientIf = ");
        stringBuilder.append(this.mClientIf);
        stringBuilder.append(" mPendingIntent=");
        PendingIntent pendingIntent = this.mPendingIntent;
        stringBuilder.append(pendingIntent == null ? null : pendingIntent.getTarget());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BleScanWrapper other = (BleScanWrapper) obj;
        if (this.mIsFlagIntent) {
            return this.mPendingIntent.equals(other.mPendingIntent);
        }
        if (this.mClientIf != other.mClientIf) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (this.mIsFlagIntent) {
            return this.mPendingIntent.hashCode();
        }
        return Objects.hash(new Object[]{Integer.valueOf(this.mClientIf)});
    }
}
