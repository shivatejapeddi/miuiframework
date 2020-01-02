package android.app.backup;

import android.annotation.SystemApi;
import android.app.backup.IBackupManagerMonitor.Stub;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SystemApi
public class RestoreSession {
    static final String TAG = "RestoreSession";
    IRestoreSession mBinder;
    final Context mContext;
    RestoreObserverWrapper mObserver = null;

    private class BackupManagerMonitorWrapper extends Stub {
        final BackupManagerMonitor mMonitor;

        BackupManagerMonitorWrapper(BackupManagerMonitor monitor) {
            this.mMonitor = monitor;
        }

        public void onEvent(Bundle event) throws RemoteException {
            this.mMonitor.onEvent(event);
        }
    }

    private class RestoreObserverWrapper extends IRestoreObserver.Stub {
        static final int MSG_RESTORE_FINISHED = 3;
        static final int MSG_RESTORE_SETS_AVAILABLE = 4;
        static final int MSG_RESTORE_STARTING = 1;
        static final int MSG_UPDATE = 2;
        final RestoreObserver mAppObserver;
        final Handler mHandler;

        RestoreObserverWrapper(Context context, RestoreObserver appObserver) {
            this.mHandler = new Handler(context.getMainLooper(), RestoreSession.this) {
                public void handleMessage(Message msg) {
                    int i = msg.what;
                    if (i == 1) {
                        RestoreObserverWrapper.this.mAppObserver.restoreStarting(msg.arg1);
                    } else if (i == 2) {
                        RestoreObserverWrapper.this.mAppObserver.onUpdate(msg.arg1, (String) msg.obj);
                    } else if (i == 3) {
                        RestoreObserverWrapper.this.mAppObserver.restoreFinished(msg.arg1);
                    } else if (i == 4) {
                        RestoreObserverWrapper.this.mAppObserver.restoreSetsAvailable((RestoreSet[]) msg.obj);
                    }
                }
            };
            this.mAppObserver = appObserver;
        }

        public void restoreSetsAvailable(RestoreSet[] result) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(4, result));
        }

        public void restoreStarting(int numPackages) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(1, numPackages, 0));
        }

        public void onUpdate(int nowBeingRestored, String currentPackage) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(2, nowBeingRestored, 0, currentPackage));
        }

        public void restoreFinished(int error) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(3, error, 0));
        }
    }

    public int getAvailableRestoreSets(RestoreObserver observer, BackupManagerMonitor monitor) {
        BackupManagerMonitorWrapper monitorWrapper;
        RestoreObserverWrapper obsWrapper = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            return this.mBinder.getAvailableRestoreSets(obsWrapper, monitorWrapper);
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to get available sets");
            return -1;
        }
    }

    public int getAvailableRestoreSets(RestoreObserver observer) {
        return getAvailableRestoreSets(observer, null);
    }

    public int restoreAll(long token, RestoreObserver observer, BackupManagerMonitor monitor) {
        int err = -1;
        RestoreObserverWrapper restoreObserverWrapper = this.mObserver;
        String str = TAG;
        if (restoreObserverWrapper != null) {
            Log.d(str, "restoreAll() called during active restore");
            return -1;
        }
        BackupManagerMonitorWrapper monitorWrapper;
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            str = this.mBinder.restoreAll(token, this.mObserver, monitorWrapper);
            err = str;
        } catch (RemoteException e) {
            Log.d(str, "Can't contact server to restore");
        }
        return err;
    }

    public int restoreAll(long token, RestoreObserver observer) {
        return restoreAll(token, observer, null);
    }

    public int restorePackages(long token, RestoreObserver observer, Set<String> packages, BackupManagerMonitor monitor) {
        int err = -1;
        RestoreObserverWrapper restoreObserverWrapper = this.mObserver;
        String str = TAG;
        if (restoreObserverWrapper != null) {
            Log.d(str, "restoreAll() called during active restore");
            return -1;
        }
        BackupManagerMonitorWrapper monitorWrapper;
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            err = this.mBinder.restorePackages(token, this.mObserver, (String[]) packages.toArray(new String[0]), monitorWrapper);
        } catch (RemoteException e) {
            Log.d(str, "Can't contact server to restore packages");
        }
        return err;
    }

    public int restorePackages(long token, RestoreObserver observer, Set<String> packages) {
        return restorePackages(token, observer, packages, null);
    }

    @Deprecated
    public int restoreSome(long token, RestoreObserver observer, BackupManagerMonitor monitor, String[] packages) {
        return restorePackages(token, observer, new HashSet(Arrays.asList(packages)), monitor);
    }

    @Deprecated
    public int restoreSome(long token, RestoreObserver observer, String[] packages) {
        return restoreSome(token, observer, null, packages);
    }

    public int restorePackage(String packageName, RestoreObserver observer, BackupManagerMonitor monitor) {
        int err = -1;
        RestoreObserverWrapper restoreObserverWrapper = this.mObserver;
        String str = TAG;
        if (restoreObserverWrapper != null) {
            Log.d(str, "restorePackage() called during active restore");
            return -1;
        }
        BackupManagerMonitorWrapper monitorWrapper;
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            str = this.mBinder.restorePackage(packageName, this.mObserver, monitorWrapper);
            err = str;
        } catch (RemoteException e) {
            Log.d(str, "Can't contact server to restore package");
        }
        return err;
    }

    public int restorePackage(String packageName, RestoreObserver observer) {
        return restorePackage(packageName, observer, null);
    }

    public void endRestoreSession() {
        try {
            this.mBinder.endRestoreSession();
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to get available sets");
        } catch (Throwable th) {
            this.mBinder = null;
        }
        this.mBinder = null;
    }

    RestoreSession(Context context, IRestoreSession binder) {
        this.mContext = context;
        this.mBinder = binder;
    }
}
