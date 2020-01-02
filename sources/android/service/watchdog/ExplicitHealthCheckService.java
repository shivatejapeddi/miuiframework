package android.service.watchdog;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.watchdog.IExplicitHealthCheckService.Stub;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SystemApi
public abstract class ExplicitHealthCheckService extends Service {
    public static final String BIND_PERMISSION = "android.permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE";
    public static final String EXTRA_HEALTH_CHECK_PASSED_PACKAGE = "android.service.watchdog.extra.health_check_passed_package";
    public static final String EXTRA_REQUESTED_PACKAGES = "android.service.watchdog.extra.requested_packages";
    public static final String EXTRA_SUPPORTED_PACKAGES = "android.service.watchdog.extra.supported_packages";
    public static final String SERVICE_INTERFACE = "android.service.watchdog.ExplicitHealthCheckService";
    private static final String TAG = "ExplicitHealthCheckService";
    private RemoteCallback mCallback;
    private final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
    private final ExplicitHealthCheckServiceWrapper mWrapper = new ExplicitHealthCheckServiceWrapper();

    private class ExplicitHealthCheckServiceWrapper extends Stub {
        private ExplicitHealthCheckServiceWrapper() {
        }

        public void setCallback(RemoteCallback callback) throws RemoteException {
            ExplicitHealthCheckService.this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$EnMyVE8D3ameNypg4gr2IMP7BCo(this, callback));
        }

        public /* synthetic */ void lambda$setCallback$0$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(RemoteCallback callback) {
            ExplicitHealthCheckService.this.mCallback = callback;
        }

        public /* synthetic */ void lambda$request$1$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(String packageName) {
            ExplicitHealthCheckService.this.onRequestHealthCheck(packageName);
        }

        public void request(String packageName) throws RemoteException {
            ExplicitHealthCheckService.this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$Gn8La3kwBvbjLET_Nqtstvz2RZg(this, packageName));
        }

        public void cancel(String packageName) throws RemoteException {
            ExplicitHealthCheckService.this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$pC_jIpmynGf4FhRLSRCGbJwUkGE(this, packageName));
        }

        public /* synthetic */ void lambda$cancel$2$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(String packageName) {
            ExplicitHealthCheckService.this.onCancelHealthCheck(packageName);
        }

        public void getSupportedPackages(RemoteCallback callback) throws RemoteException {
            ExplicitHealthCheckService.this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$5Rv9E4-Jc0y0GMGqI_g-82qtYpg(this, callback));
        }

        public /* synthetic */ void lambda$getSupportedPackages$3$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(RemoteCallback callback) {
            List<PackageConfig> packages = ExplicitHealthCheckService.this.onGetSupportedPackages();
            Objects.requireNonNull(packages, "Supported package list must be non-null");
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ExplicitHealthCheckService.EXTRA_SUPPORTED_PACKAGES, new ArrayList(packages));
            callback.sendResult(bundle);
        }

        public void getRequestedPackages(RemoteCallback callback) throws RemoteException {
            ExplicitHealthCheckService.this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$yycuCTr7mDJWrqK-xpXb1sTmkyA(this, callback));
        }

        public /* synthetic */ void lambda$getRequestedPackages$4$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(RemoteCallback callback) {
            List<String> packages = ExplicitHealthCheckService.this.onGetRequestedPackages();
            Objects.requireNonNull(packages, "Requested  package list must be non-null");
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ExplicitHealthCheckService.EXTRA_REQUESTED_PACKAGES, new ArrayList(packages));
            callback.sendResult(bundle);
        }
    }

    @SystemApi
    public static final class PackageConfig implements Parcelable {
        public static final Creator<PackageConfig> CREATOR = new Creator<PackageConfig>() {
            public PackageConfig createFromParcel(Parcel source) {
                return new PackageConfig(source, null);
            }

            public PackageConfig[] newArray(int size) {
                return new PackageConfig[size];
            }
        };
        private static final long DEFAULT_HEALTH_CHECK_TIMEOUT_MILLIS = TimeUnit.HOURS.toMillis(1);
        private final long mHealthCheckTimeoutMillis;
        private final String mPackageName;

        public PackageConfig(String packageName, long healthCheckTimeoutMillis) {
            this.mPackageName = (String) Preconditions.checkNotNull(packageName);
            if (healthCheckTimeoutMillis == 0) {
                this.mHealthCheckTimeoutMillis = DEFAULT_HEALTH_CHECK_TIMEOUT_MILLIS;
            } else {
                this.mHealthCheckTimeoutMillis = Preconditions.checkArgumentNonnegative(healthCheckTimeoutMillis);
            }
        }

        private PackageConfig(Parcel parcel) {
            this.mPackageName = parcel.readString();
            this.mHealthCheckTimeoutMillis = parcel.readLong();
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public long getHealthCheckTimeoutMillis() {
            return this.mHealthCheckTimeoutMillis;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PackageConfig{");
            stringBuilder.append(this.mPackageName);
            stringBuilder.append(", ");
            stringBuilder.append(this.mHealthCheckTimeoutMillis);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public boolean equals(Object other) {
            boolean z = true;
            if (other == this) {
                return true;
            }
            if (!(other instanceof PackageConfig)) {
                return false;
            }
            PackageConfig otherInfo = (PackageConfig) other;
            if (!(Objects.equals(Long.valueOf(otherInfo.getHealthCheckTimeoutMillis()), Long.valueOf(this.mHealthCheckTimeoutMillis)) && Objects.equals(otherInfo.getPackageName(), this.mPackageName))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mPackageName, Long.valueOf(this.mHealthCheckTimeoutMillis)});
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.mPackageName);
            parcel.writeLong(this.mHealthCheckTimeoutMillis);
        }
    }

    public abstract void onCancelHealthCheck(String str);

    public abstract List<String> onGetRequestedPackages();

    public abstract List<PackageConfig> onGetSupportedPackages();

    public abstract void onRequestHealthCheck(String str);

    public final IBinder onBind(Intent intent) {
        return this.mWrapper;
    }

    public final void notifyHealthCheckPassed(String packageName) {
        this.mHandler.post(new -$$Lambda$ExplicitHealthCheckService$ulagkAZ2bM7-LW9T7PSTxSLQfBE(this, packageName));
    }

    public /* synthetic */ void lambda$notifyHealthCheckPassed$0$ExplicitHealthCheckService(String packageName) {
        if (this.mCallback != null) {
            Objects.requireNonNull(packageName, "Package passing explicit health check must be non-null");
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_HEALTH_CHECK_PASSED_PACKAGE, packageName);
            this.mCallback.sendResult(bundle);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("System missed explicit health check result for ");
        stringBuilder.append(packageName);
        Log.wtf(TAG, stringBuilder.toString());
    }
}
