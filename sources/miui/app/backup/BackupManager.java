package miui.app.backup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.BrowserContract;
import android.provider.ContactsContract;
import android.provider.MiuiSettings.XSpace;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.app.backup.IPackageBackupRestoreObserver.Stub;

public class BackupManager {
    public static final String BACKUP_FILE_HEADER_MAGIC = "MIUI BACKUP\n";
    public static final int BACKUP_FILE_VERSION = 2;
    public static String DOMAIN_ATTACH = "miui_att";
    public static String DOMAIN_BAK = "miui_bak";
    public static String DOMAIN_END = "miui_end";
    public static String DOMAIN_META = "miui_meta";
    public static final int ERR_AUTHENTICATION_FAILED = 3;
    public static final int ERR_BAKFILE_BROKEN = 6;
    public static final int ERR_BINDER_DIED = 8;
    public static final int ERR_IO_PERMISSION = 7;
    public static final int ERR_NONE = 0;
    public static final int ERR_NOT_ALLOW = 9;
    public static final int ERR_NO_BACKUPAGENT = 2;
    public static final int ERR_UNKNOWN = 1;
    public static final int ERR_VERSION_TOO_OLD = 5;
    public static final int ERR_VERSION_UNSUPPORTED = 4;
    public static final int FEATURE_FULL_BACKUP = -1;
    public static final int PROG_TYPE_NORMAL = 0;
    public static final int PROG_TYPE_RECORD = 1;
    public static final String SERVICE_NAME = "MiuiBackup";
    public static final int STATE_BACKUP = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_RESTORE = 2;
    private static final String TAG = "Backup:BackupManager";
    public static final int TYPE_NORMAL_BACKUP_TIMEOUT_SCALE = 1;
    public static final int TYPE_RECORD_BACKUP_TIMEOUT_SCALE = 6;
    private static HashSet mSystemAppWhiteSet = new HashSet();
    private static HashSet sProgRecordAppSet = new HashSet();
    private static WeakReference<BackupManager> sWRInstance;
    private Object mBackupRestoreLatch = new Object();
    private BackupRestoreListener mBackupRestoreListener;
    private Context mContext;
    private IBinder mICaller = new Binder();
    private boolean mIsAcquired;
    private ParcelFileDescriptor[] mPips;
    private IBackupManager mService;
    private AtomicBoolean mTransactionLatch = null;

    public interface BackupRestoreListener {
        void onBackupDataTransaction(String str, int i, ParcelFileDescriptor parcelFileDescriptor);

        void onBackupEnd(String str, int i);

        void onBackupStart(String str, int i);

        void onCustomProgressChange(String str, int i, int i2, long j, long j2);

        void onError(String str, int i, int i2);

        void onRestoreEnd(String str, int i);

        void onRestoreProgress(String str, int i, long j);

        void onRestoreStart(String str, int i);
    }

    private class FullBackupRestoreObserver extends Stub {
        private FullBackupRestoreObserver() {
        }

        /* synthetic */ FullBackupRestoreObserver(BackupManager x0, AnonymousClass1 x1) {
            this();
        }

        public void onBackupStart(final String pkg, final int feature) throws RemoteException {
            BackupManager.this.mTransactionLatch = new AtomicBoolean(false);
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onBackupStart(pkg, feature);
                new Thread() {
                    public void run() {
                        try {
                            synchronized (BackupManager.this.mTransactionLatch) {
                                BackupManager.this.mBackupRestoreListener.onBackupDataTransaction(pkg, feature, BackupManager.this.mPips[0]);
                                BackupManager.this.mTransactionLatch.set(true);
                                BackupManager.this.mTransactionLatch.notifyAll();
                            }
                            try {
                                BackupManager.this.mPips[0].close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Throwable th) {
                            try {
                                BackupManager.this.mPips[0].close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }

        public void onBackupEnd(String pkg, int feature) throws RemoteException {
            try {
                BackupManager.this.mPips[1].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (BackupManager.this.mTransactionLatch != null) {
                synchronized (BackupManager.this.mTransactionLatch) {
                    while (!BackupManager.this.mTransactionLatch.get()) {
                        try {
                            BackupManager.this.mTransactionLatch.wait();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onBackupEnd(pkg, feature);
            }
            try {
                synchronized (BackupManager.this.mBackupRestoreLatch) {
                    BackupManager.this.mBackupRestoreLatch.notifyAll();
                }
            } catch (Exception e3) {
                Log.e(BackupManager.TAG, "", e3);
            }
        }

        public void onError(String pkg, int feature, int err) throws RemoteException {
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onError(pkg, feature, err);
            }
        }

        public void onRestoreStart(String pkg, int feature) throws RemoteException {
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onRestoreStart(pkg, feature);
            }
        }

        public void onRestoreEnd(String pkg, int feature) throws RemoteException {
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onRestoreEnd(pkg, feature);
            }
            try {
                synchronized (BackupManager.this.mBackupRestoreLatch) {
                    BackupManager.this.mBackupRestoreLatch.notifyAll();
                }
            } catch (Exception e) {
                Log.e(BackupManager.TAG, "", e);
            }
        }

        public void onRestoreError(String pkg, int feature, int err) throws RemoteException {
        }

        public void onCustomProgressChange(String pkg, int feature, int progType, long prog, long total) {
            if (BackupManager.this.mBackupRestoreListener != null) {
                BackupManager.this.mBackupRestoreListener.onCustomProgressChange(pkg, feature, progType, prog, total);
            }
        }
    }

    static {
        mSystemAppWhiteSet.add(BrowserContract.AUTHORITY);
        mSystemAppWhiteSet.add("com.miui.weather2");
        mSystemAppWhiteSet.add("com.miui.notes");
        mSystemAppWhiteSet.add("com.android.email");
        sProgRecordAppSet.add(ContactsContract.AUTHORITY);
        sProgRecordAppSet.add("com.android.mms");
        sProgRecordAppSet.add("com.tencent.mm");
        sProgRecordAppSet.add(XSpace.QQ_PACKAGE_NAME);
    }

    public boolean acquire(IBackupServiceStateObserver stateObserver) {
        try {
            if (!this.mIsAcquired) {
                this.mIsAcquired = this.mService.acquire(stateObserver, this.mICaller);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this.mIsAcquired;
    }

    public void release(IBackupServiceStateObserver stateObserver) {
        try {
            this.mService.release(stateObserver);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mIsAcquired = false;
    }

    public void setFutureTask(List<String> packages) {
        try {
            this.mService.setFutureTask(packages);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isServiceIdle() {
        try {
            return this.mService.isServiceIdle();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSysAppForBackup(Context context, String pkg) {
        try {
            return isSysAppForBackup(context.getPackageManager().getPackageInfo(pkg, 64));
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isSysAppForBackup(PackageInfo info) {
        if (info == null || info.applicationInfo == null) {
            return false;
        }
        if ((info.applicationInfo.flags & 1) != 0 || mSystemAppWhiteSet.contains(info.applicationInfo.packageName)) {
            return true;
        }
        return false;
    }

    public static boolean isProgRecordApp(String pkg, int feature) {
        return sProgRecordAppSet.contains(pkg);
    }

    public static final BackupManager getBackupManager(Context context) {
        WeakReference weakReference = sWRInstance;
        BackupManager instance = weakReference == null ? null : (BackupManager) weakReference.get();
        if (instance != null) {
            return instance;
        }
        instance = new BackupManager(context.getApplicationContext());
        sWRInstance = new WeakReference(instance);
        return instance;
    }

    private BackupManager(Context context) {
        this.mContext = context;
        this.mService = IBackupManager.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
    }

    public void backupPackage(String pkg, int feature, String pwd, String encryptedPwd, boolean includeApk, boolean forceBackup, BackupRestoreListener l) {
        backupPackage(pkg, feature, pwd, encryptedPwd, includeApk, forceBackup, false, l);
    }

    public void backupPackage(String pkg, int feature, String pwd, String encryptedPwd, boolean includeApk, boolean forceBackup, boolean shouldSkipData, BackupRestoreListener l) {
        if (this.mIsAcquired) {
            this.mBackupRestoreListener = l;
            if (this.mService != null) {
                ParcelFileDescriptor[] parcelFileDescriptorArr;
                try {
                    this.mPips = ParcelFileDescriptor.createPipe();
                    synchronized (this.mBackupRestoreLatch) {
                        this.mService.backupPackage(this.mPips[1], this.mPips[0], pkg, feature, pwd, encryptedPwd, includeApk, forceBackup, shouldSkipData, new FullBackupRestoreObserver(this, null));
                        this.mBackupRestoreLatch.wait();
                    }
                    parcelFileDescriptorArr = this.mPips;
                    if (parcelFileDescriptorArr != null) {
                        try {
                            if (parcelFileDescriptorArr[0] != null) {
                                parcelFileDescriptorArr[0].close();
                            }
                            if (this.mPips[1] != null) {
                                this.mPips[1].close();
                                return;
                            }
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "IOException", e);
                            return;
                        }
                    }
                    return;
                } catch (IOException e2) {
                    Log.e(TAG, "create pipe failed", e2);
                    parcelFileDescriptorArr = this.mPips;
                    if (parcelFileDescriptorArr != null) {
                        if (parcelFileDescriptorArr[0] != null) {
                            parcelFileDescriptorArr[0].close();
                        }
                        if (this.mPips[1] != null) {
                            this.mPips[1].close();
                            return;
                        }
                        return;
                    }
                    return;
                } catch (RemoteException e3) {
                    Log.e(TAG, "Remove invoking failed", e3);
                    parcelFileDescriptorArr = this.mPips;
                    if (parcelFileDescriptorArr != null) {
                        if (parcelFileDescriptorArr[0] != null) {
                            parcelFileDescriptorArr[0].close();
                        }
                        if (this.mPips[1] != null) {
                            this.mPips[1].close();
                            return;
                        }
                        return;
                    }
                    return;
                } catch (InterruptedException e4) {
                    try {
                        Log.e(TAG, "InterruptedException", e4);
                        parcelFileDescriptorArr = this.mPips;
                        if (parcelFileDescriptorArr != null) {
                            if (parcelFileDescriptorArr[0] != null) {
                                parcelFileDescriptorArr[0].close();
                            }
                            if (this.mPips[1] != null) {
                                this.mPips[1].close();
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        parcelFileDescriptorArr = this.mPips;
                        if (parcelFileDescriptorArr != null) {
                            try {
                                if (parcelFileDescriptorArr[0] != null) {
                                    parcelFileDescriptorArr[0].close();
                                }
                                if (this.mPips[1] != null) {
                                    this.mPips[1].close();
                                }
                            } catch (IOException e22) {
                                Log.e(TAG, "IOException", e22);
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
        BackupRestoreListener backupRestoreListener = l;
        throw new RuntimeException("You must acquire first to use the backup or restore service");
    }

    public void restoreFile(final ParcelFileDescriptor fd, String pwd, boolean forceRestore, BackupRestoreListener l) {
        if (this.mIsAcquired) {
            this.mBackupRestoreListener = l;
            try {
                ParcelFileDescriptor[] pipe;
                synchronized (this.mBackupRestoreLatch) {
                    pipe = ParcelFileDescriptor.createPipe();
                    final ParcelFileDescriptor writeSide = pipe[1];
                    new Thread() {
                        public void run() {
                            String str = "IOException";
                            String str2 = BackupManager.TAG;
                            InputStream is = null;
                            OutputStream os = null;
                            try {
                                is = new FileInputStream(fd.getFileDescriptor());
                                os = new FileOutputStream(writeSide.getFileDescriptor());
                                byte[] buf = new byte[1024];
                                while (true) {
                                    int read = is.read(buf);
                                    int read2 = read;
                                    if (read > 0) {
                                        os.write(buf, 0, read2);
                                    } else {
                                        try {
                                            break;
                                        } catch (IOException e) {
                                            Log.e(str2, str, e);
                                            return;
                                        }
                                    }
                                }
                                is.close();
                                os.close();
                                if (writeSide != null) {
                                    writeSide.close();
                                }
                            } catch (IOException e2) {
                                Log.e(str2, str, e2);
                                if (is != null) {
                                    is.close();
                                }
                                if (os != null) {
                                    os.close();
                                }
                                if (writeSide != null) {
                                    writeSide.close();
                                }
                            } catch (Throwable th) {
                                if (is != null) {
                                    try {
                                        is.close();
                                    } catch (IOException e3) {
                                        Log.e(str2, str, e3);
                                    }
                                }
                                if (os != null) {
                                    os.close();
                                }
                                if (writeSide != null) {
                                    writeSide.close();
                                }
                            }
                        }
                    }.start();
                    this.mService.restoreFile(pipe[0], pwd, forceRestore, new FullBackupRestoreObserver(this, null));
                    this.mBackupRestoreLatch.wait();
                }
                if (pipe[0] != null) {
                    try {
                        pipe[0].close();
                    } catch (IOException e) {
                        Log.e(TAG, "IOException", e);
                    }
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoteException", e2);
                if (null != null && null[0] != null) {
                    null[0].close();
                }
            } catch (InterruptedException e3) {
                Log.e(TAG, "InterruptedException", e3);
                if (null != null && null[0] != null) {
                    null[0].close();
                }
            } catch (IOException e4) {
                try {
                    Log.e(TAG, "IOException", e4);
                    if (null != null && null[0] != null) {
                        null[0].close();
                    }
                } catch (Throwable th) {
                    if (!(null == null || null[0] == null)) {
                        try {
                            null[0].close();
                        } catch (IOException e5) {
                            Log.e(TAG, "IOException", e5);
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("You must acquire first to use the backup or restore service");
        }
    }

    public int getCurrentWorkingFeature() {
        try {
            return this.mService.getCurrentWorkingFeature();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return -1;
        }
    }

    public String getCurrentRunningPackage() {
        try {
            return this.mService.getCurrentRunningPackage();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return null;
        }
    }

    public int getState() {
        try {
            return this.mService.getState();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return -1;
        }
    }

    public void setIsNeedBeKilled(boolean isNeedBeKilled) {
        try {
            this.mService.setIsNeedBeKilled(this.mContext.getPackageName(), isNeedBeKilled);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setWorkingError(int errCode) {
        try {
            this.mService.errorOccur(errCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setCustomProgress(int progType, int prog, int total) {
        try {
            if (this.mContext.getPackageName().equals(this.mService.getCurrentRunningPackage())) {
                this.mService.setCustomProgress(progType, prog, total);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean delCacheBackup() {
        try {
            this.mService.delCacheBackup();
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return false;
        }
    }
}
