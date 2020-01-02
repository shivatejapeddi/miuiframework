package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.Uri;
import android.os.IIncidentAuthListener.Stub;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.Parcelable.Creator;
import android.util.Slog;
import android.util.TimeUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
public class IncidentManager {
    public static final int FLAG_CONFIRMATION_DIALOG = 1;
    public static final int PRIVACY_POLICY_AUTO = 200;
    public static final int PRIVACY_POLICY_EXPLICIT = 100;
    public static final int PRIVACY_POLICY_LOCAL = 0;
    private static final String TAG = "IncidentManager";
    public static final String URI_AUTHORITY = "android.os.IncidentManager";
    public static final String URI_PARAM_CALLING_PACKAGE = "pkg";
    public static final String URI_PARAM_FLAGS = "flags";
    public static final String URI_PARAM_ID = "id";
    public static final String URI_PARAM_RECEIVER_CLASS = "receiver";
    public static final String URI_PARAM_REPORT_ID = "r";
    public static final String URI_PARAM_TIMESTAMP = "t";
    public static final String URI_PATH = "/pending";
    public static final String URI_SCHEME = "content";
    private IIncidentCompanion mCompanionService;
    private final Context mContext;
    private IIncidentManager mIncidentService;
    private Object mLock = new Object();

    public static class AuthListener {
        Stub mBinder = new Stub() {
            public void onReportApproved() {
                if (AuthListener.this.mExecutor != null) {
                    AuthListener.this.mExecutor.execute(new -$$Lambda$IncidentManager$AuthListener$1$lPkHJjJYlkckZZgbwSfNFtF2x_U(this));
                } else {
                    AuthListener.this.onReportApproved();
                }
            }

            public /* synthetic */ void lambda$onReportApproved$0$IncidentManager$AuthListener$1() {
                AuthListener.this.onReportApproved();
            }

            public void onReportDenied() {
                if (AuthListener.this.mExecutor != null) {
                    AuthListener.this.mExecutor.execute(new -$$Lambda$IncidentManager$AuthListener$1$VoPbrfU3RKoeruCLRzIQ8yeLsyQ(this));
                } else {
                    AuthListener.this.onReportDenied();
                }
            }

            public /* synthetic */ void lambda$onReportDenied$1$IncidentManager$AuthListener$1() {
                AuthListener.this.onReportDenied();
            }
        };
        Executor mExecutor;

        public void onReportApproved() {
        }

        public void onReportDenied() {
        }
    }

    @SystemApi
    public static class IncidentReport implements Parcelable, Closeable {
        public static final Creator<IncidentReport> CREATOR = new Creator() {
            public IncidentReport[] newArray(int size) {
                return new IncidentReport[size];
            }

            public IncidentReport createFromParcel(Parcel in) {
                return new IncidentReport(in);
            }
        };
        private ParcelFileDescriptor mFileDescriptor;
        private final int mPrivacyPolicy;
        private final long mTimestampNs;

        public IncidentReport(Parcel in) {
            this.mTimestampNs = in.readLong();
            this.mPrivacyPolicy = in.readInt();
            if (in.readInt() != 0) {
                this.mFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(in);
            } else {
                this.mFileDescriptor = null;
            }
        }

        public void close() {
            try {
                if (this.mFileDescriptor != null) {
                    this.mFileDescriptor.close();
                    this.mFileDescriptor = null;
                }
            } catch (IOException e) {
            }
        }

        public long getTimestamp() {
            return this.mTimestampNs / TimeUtils.NANOS_PER_MS;
        }

        public long getPrivacyPolicy() {
            return (long) this.mPrivacyPolicy;
        }

        public InputStream getInputStream() throws IOException {
            ParcelFileDescriptor parcelFileDescriptor = this.mFileDescriptor;
            if (parcelFileDescriptor == null) {
                return null;
            }
            return new AutoCloseInputStream(parcelFileDescriptor);
        }

        public int describeContents() {
            return this.mFileDescriptor != null ? 1 : 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeLong(this.mTimestampNs);
            out.writeInt(this.mPrivacyPolicy);
            if (this.mFileDescriptor != null) {
                out.writeInt(1);
                this.mFileDescriptor.writeToParcel(out, flags);
                return;
            }
            out.writeInt(0);
        }
    }

    @SystemApi
    public static class PendingReport {
        private final int mFlags;
        private final String mRequestingPackage;
        private final long mTimestamp;
        private final Uri mUri;

        public PendingReport(Uri uri) {
            StringBuilder stringBuilder;
            try {
                this.mFlags = Integer.parseInt(uri.getQueryParameter("flags"));
                String requestingPackage = uri.getQueryParameter("pkg");
                if (requestingPackage != null) {
                    this.mRequestingPackage = requestingPackage;
                    try {
                        this.mTimestamp = Long.parseLong(uri.getQueryParameter(IncidentManager.URI_PARAM_TIMESTAMP));
                        this.mUri = uri;
                        return;
                    } catch (NumberFormatException e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Invalid URI: No t parameter. ");
                        stringBuilder2.append(uri);
                        throw new RuntimeException(stringBuilder2.toString());
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid URI: No pkg parameter. ");
                stringBuilder.append(uri);
                throw new RuntimeException(stringBuilder.toString());
            } catch (NumberFormatException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid URI: No flags parameter. ");
                stringBuilder.append(uri);
                throw new RuntimeException(stringBuilder.toString());
            }
        }

        public String getRequestingPackage() {
            return this.mRequestingPackage;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public long getTimestamp() {
            return this.mTimestamp;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PendingReport(");
            stringBuilder.append(getUri().toString());
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PendingReport)) {
                return false;
            }
            PendingReport that = (PendingReport) obj;
            if (!(this.mUri.equals(that.mUri) && this.mFlags == that.mFlags && this.mRequestingPackage.equals(that.mRequestingPackage) && this.mTimestamp == that.mTimestamp)) {
                z = false;
            }
            return z;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrivacyPolicy {
    }

    public IncidentManager(Context context) {
        this.mContext = context;
    }

    public void reportIncident(IncidentReportArgs args) {
        reportIncidentInternal(args);
    }

    public void requestAuthorization(int callingUid, String callingPackage, int flags, AuthListener listener) {
        requestAuthorization(callingUid, callingPackage, flags, this.mContext.getMainExecutor(), listener);
    }

    public void requestAuthorization(int callingUid, String callingPackage, int flags, Executor executor, AuthListener listener) {
        try {
            if (listener.mExecutor == null) {
                listener.mExecutor = executor;
                getCompanionServiceLocked().authorizeReport(callingUid, callingPackage, null, null, flags, listener.mBinder);
                return;
            }
            throw new RuntimeException("Do not reuse AuthListener objects when calling requestAuthorization");
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void cancelAuthorization(AuthListener listener) {
        try {
            getCompanionServiceLocked().cancelAuthorization(listener.mBinder);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<PendingReport> getPendingReports() {
        try {
            List<String> strings = getCompanionServiceLocked().getPendingReports();
            int size = strings.size();
            ArrayList<PendingReport> result = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                result.add(new PendingReport(Uri.parse((String) strings.get(i))));
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void approveReport(Uri uri) {
        try {
            getCompanionServiceLocked().approveReport(uri.toString());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void denyReport(Uri uri) {
        try {
            getCompanionServiceLocked().denyReport(uri.toString());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Uri> getIncidentReportList(String receiverClass) {
        try {
            List<String> strings = getCompanionServiceLocked().getIncidentReportList(this.mContext.getPackageName(), receiverClass);
            int size = strings.size();
            ArrayList<Uri> result = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                result.add(Uri.parse((String) strings.get(i)));
            }
            return result;
        } catch (RemoteException ex) {
            throw new RuntimeException("System server or incidentd going down", ex);
        }
    }

    public IncidentReport getIncidentReport(Uri uri) {
        String id = uri.getQueryParameter("r");
        if (id == null) {
            return null;
        }
        String pkg = uri.getQueryParameter("pkg");
        if (pkg != null) {
            String cls = uri.getQueryParameter(URI_PARAM_RECEIVER_CLASS);
            if (cls != null) {
                try {
                    return getCompanionServiceLocked().getIncidentReport(pkg, cls, id);
                } catch (RemoteException ex) {
                    throw new RuntimeException("System server or incidentd going down", ex);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid URI: No receiver parameter. ");
            stringBuilder.append(uri);
            throw new RuntimeException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid URI: No pkg parameter. ");
        stringBuilder2.append(uri);
        throw new RuntimeException(stringBuilder2.toString());
    }

    public void deleteIncidentReports(Uri uri) {
        String str = "System server or incidentd going down";
        if (uri == null) {
            try {
                getCompanionServiceLocked().deleteAllIncidentReports(this.mContext.getPackageName());
                return;
            } catch (RemoteException ex) {
                throw new RuntimeException(str, ex);
            }
        }
        String pkg = uri.getQueryParameter("pkg");
        if (pkg != null) {
            String cls = uri.getQueryParameter(URI_PARAM_RECEIVER_CLASS);
            if (cls != null) {
                String id = uri.getQueryParameter("r");
                if (id != null) {
                    try {
                        getCompanionServiceLocked().deleteIncidentReports(pkg, cls, id);
                        return;
                    } catch (RemoteException ex2) {
                        throw new RuntimeException(str, ex2);
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid URI: No r parameter. ");
                stringBuilder.append(uri);
                throw new RuntimeException(stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Invalid URI: No receiver parameter. ");
            stringBuilder2.append(uri);
            throw new RuntimeException(stringBuilder2.toString());
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Invalid URI: No pkg parameter. ");
        stringBuilder3.append(uri);
        throw new RuntimeException(stringBuilder3.toString());
    }

    private void reportIncidentInternal(IncidentReportArgs args) {
        String str = TAG;
        try {
            IIncidentManager service = getIIncidentManagerLocked();
            if (service == null) {
                Slog.e(str, "reportIncident can't find incident binder service");
            } else {
                service.reportIncident(args);
            }
        } catch (RemoteException ex) {
            Slog.e(str, "reportIncident failed", ex);
        }
    }

    private IIncidentManager getIIncidentManagerLocked() throws RemoteException {
        IIncidentManager iIncidentManager = this.mIncidentService;
        if (iIncidentManager != null) {
            return iIncidentManager;
        }
        synchronized (this.mLock) {
            IIncidentManager iIncidentManager2;
            if (this.mIncidentService != null) {
                iIncidentManager2 = this.mIncidentService;
                return iIncidentManager2;
            }
            this.mIncidentService = IIncidentManager.Stub.asInterface(ServiceManager.getService(Context.INCIDENT_SERVICE));
            if (this.mIncidentService != null) {
                this.mIncidentService.asBinder().linkToDeath(new -$$Lambda$IncidentManager$yGukxCMuLDmoRlrh5jGUmq5BOTk(this), 0);
            }
            iIncidentManager2 = this.mIncidentService;
            return iIncidentManager2;
        }
    }

    public /* synthetic */ void lambda$getIIncidentManagerLocked$0$IncidentManager() {
        synchronized (this.mLock) {
            this.mIncidentService = null;
        }
    }

    private IIncidentCompanion getCompanionServiceLocked() throws RemoteException {
        IIncidentCompanion iIncidentCompanion = this.mCompanionService;
        if (iIncidentCompanion != null) {
            return iIncidentCompanion;
        }
        synchronized (this) {
            if (this.mCompanionService != null) {
                iIncidentCompanion = this.mCompanionService;
                return iIncidentCompanion;
            }
            this.mCompanionService = IIncidentCompanion.Stub.asInterface(ServiceManager.getService(Context.INCIDENT_COMPANION_SERVICE));
            if (this.mCompanionService != null) {
                this.mCompanionService.asBinder().linkToDeath(new -$$Lambda$IncidentManager$mfBTEJgu7VPkoPMTQdf1KC7oi5g(this), 0);
            }
            iIncidentCompanion = this.mCompanionService;
            return iIncidentCompanion;
        }
    }

    public /* synthetic */ void lambda$getCompanionServiceLocked$1$IncidentManager() {
        synchronized (this.mLock) {
            this.mCompanionService = null;
        }
    }
}
