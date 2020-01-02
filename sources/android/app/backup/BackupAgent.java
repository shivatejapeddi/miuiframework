package android.app.backup;

import android.app.IBackupAgent.Stub;
import android.app.QueuedWork;
import android.app.backup.FullBackup.BackupScheme;
import android.app.backup.FullBackup.BackupScheme.PathWithRequiredFlags;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

public abstract class BackupAgent extends ContextWrapper {
    private static final boolean DEBUG = false;
    public static final int FLAG_CLIENT_SIDE_ENCRYPTION_ENABLED = 1;
    public static final int FLAG_DEVICE_TO_DEVICE_TRANSFER = 2;
    public static final int FLAG_FAKE_CLIENT_SIDE_ENCRYPTION_ENABLED = Integer.MIN_VALUE;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_SUCCESS = 0;
    private static final String TAG = "BackupAgent";
    public static final int TYPE_DIRECTORY = 2;
    public static final int TYPE_EOF = 0;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_SYMLINK = 3;
    private final IBinder mBinder = new BackupServiceBinder().asBinder();
    Handler mHandler = null;
    private UserHandle mUser;

    private class BackupServiceBinder extends Stub {
        private static final String TAG = "BackupServiceBinder";

        private BackupServiceBinder() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:38:0x00c9  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00c9  */
        public void doBackup(android.os.ParcelFileDescriptor r18, android.os.ParcelFileDescriptor r19, android.os.ParcelFileDescriptor r20, long r21, android.app.backup.IBackupCallback r23, int r24) throws android.os.RemoteException {
            /*
            r17 = this;
            r1 = r17;
            r2 = r23;
            r3 = ") threw";
            r4 = "onBackup (";
            r5 = "BackupServiceBinder";
            r6 = android.os.Binder.clearCallingIdentity();
            r0 = new android.app.backup.BackupDataOutput;
            r8 = r19.getFileDescriptor();
            r9 = r21;
            r11 = r24;
            r0.<init>(r8, r9, r11);
            r8 = r0;
            r12 = -1;
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ IOException -> 0x0084, RuntimeException -> 0x005c, all -> 0x0053 }
            r14 = r18;
            r15 = r20;
            r0.onBackup(r14, r8, r15);	 Catch:{ IOException -> 0x004f, RuntimeException -> 0x004d, all -> 0x004b }
            r3 = 0;
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            android.os.Binder.restoreCallingIdentity(r6);
            r2.operationComplete(r3);	 Catch:{ RemoteException -> 0x0036 }
            goto L_0x0037;
        L_0x0036:
            r0 = move-exception;
        L_0x0037:
            r0 = android.os.Binder.getCallingPid();
            r5 = android.os.Process.myPid();
            if (r0 == r5) goto L_0x004a;
        L_0x0041:
            libcore.io.IoUtils.closeQuietly(r18);
            libcore.io.IoUtils.closeQuietly(r19);
            libcore.io.IoUtils.closeQuietly(r20);
        L_0x004a:
            return;
        L_0x004b:
            r0 = move-exception;
            goto L_0x0058;
        L_0x004d:
            r0 = move-exception;
            goto L_0x0061;
        L_0x004f:
            r0 = move-exception;
            r16 = r8;
            goto L_0x008b;
        L_0x0053:
            r0 = move-exception;
            r14 = r18;
            r15 = r20;
        L_0x0058:
            r3 = r0;
            r16 = r8;
            goto L_0x00b2;
        L_0x005c:
            r0 = move-exception;
            r14 = r18;
            r15 = r20;
        L_0x0061:
            r16 = r8;
            r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b0 }
            r8.<init>();	 Catch:{ all -> 0x00b0 }
            r8.append(r4);	 Catch:{ all -> 0x00b0 }
            r4 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x00b0 }
            r4 = r4.getClass();	 Catch:{ all -> 0x00b0 }
            r4 = r4.getName();	 Catch:{ all -> 0x00b0 }
            r8.append(r4);	 Catch:{ all -> 0x00b0 }
            r8.append(r3);	 Catch:{ all -> 0x00b0 }
            r3 = r8.toString();	 Catch:{ all -> 0x00b0 }
            android.util.Log.d(r5, r3, r0);	 Catch:{ all -> 0x00b0 }
            throw r0;	 Catch:{ all -> 0x00b0 }
        L_0x0084:
            r0 = move-exception;
            r14 = r18;
            r15 = r20;
            r16 = r8;
        L_0x008b:
            r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b0 }
            r8.<init>();	 Catch:{ all -> 0x00b0 }
            r8.append(r4);	 Catch:{ all -> 0x00b0 }
            r4 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x00b0 }
            r4 = r4.getClass();	 Catch:{ all -> 0x00b0 }
            r4 = r4.getName();	 Catch:{ all -> 0x00b0 }
            r8.append(r4);	 Catch:{ all -> 0x00b0 }
            r8.append(r3);	 Catch:{ all -> 0x00b0 }
            r3 = r8.toString();	 Catch:{ all -> 0x00b0 }
            android.util.Log.d(r5, r3, r0);	 Catch:{ all -> 0x00b0 }
            r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x00b0 }
            r3.<init>(r0);	 Catch:{ all -> 0x00b0 }
            throw r3;	 Catch:{ all -> 0x00b0 }
        L_0x00b0:
            r0 = move-exception;
            r3 = r0;
        L_0x00b2:
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            android.os.Binder.restoreCallingIdentity(r6);
            r2.operationComplete(r12);	 Catch:{ RemoteException -> 0x00be }
            goto L_0x00bf;
        L_0x00be:
            r0 = move-exception;
        L_0x00bf:
            r0 = android.os.Binder.getCallingPid();
            r4 = android.os.Process.myPid();
            if (r0 == r4) goto L_0x00d2;
        L_0x00c9:
            libcore.io.IoUtils.closeQuietly(r18);
            libcore.io.IoUtils.closeQuietly(r19);
            libcore.io.IoUtils.closeQuietly(r20);
        L_0x00d2:
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent$BackupServiceBinder.doBackup(android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor, long, android.app.backup.IBackupCallback, int):void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:34:0x00c9  */
        public void doRestore(android.os.ParcelFileDescriptor r17, long r18, android.os.ParcelFileDescriptor r20, int r21, android.app.backup.IBackupManager r22) throws android.os.RemoteException {
            /*
            r16 = this;
            r1 = r16;
            r2 = r21;
            r3 = r22;
            r4 = ") threw";
            r5 = "onRestore (";
            r6 = "BackupServiceBinder";
            r7 = android.os.Binder.clearCallingIdentity();
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            r0 = new android.app.backup.BackupDataInput;
            r9 = r17.getFileDescriptor();
            r0.<init>(r9);
            r9 = r0;
            r10 = 0;
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ IOException -> 0x0082, RuntimeException -> 0x005c, all -> 0x0055 }
            r12 = r18;
            r14 = r20;
            r0.onRestore(r9, r12, r14);	 Catch:{ IOException -> 0x0053, RuntimeException -> 0x0051 }
            r0 = android.app.backup.BackupAgent.this;
            r0.reloadSharedPreferences();
            android.os.Binder.restoreCallingIdentity(r7);
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ RemoteException -> 0x003d }
            r0 = r0.getBackupUserId();	 Catch:{ RemoteException -> 0x003d }
            r3.opCompleteForUser(r0, r2, r10);	 Catch:{ RemoteException -> 0x003d }
            goto L_0x003e;
        L_0x003d:
            r0 = move-exception;
        L_0x003e:
            r0 = android.os.Binder.getCallingPid();
            r4 = android.os.Process.myPid();
            if (r0 == r4) goto L_0x004e;
        L_0x0048:
            libcore.io.IoUtils.closeQuietly(r17);
            libcore.io.IoUtils.closeQuietly(r20);
        L_0x004e:
            return;
        L_0x004f:
            r0 = move-exception;
            goto L_0x005a;
        L_0x0051:
            r0 = move-exception;
            goto L_0x0061;
        L_0x0053:
            r0 = move-exception;
            goto L_0x0087;
        L_0x0055:
            r0 = move-exception;
            r12 = r18;
            r14 = r20;
        L_0x005a:
            r4 = r0;
            goto L_0x00ac;
        L_0x005c:
            r0 = move-exception;
            r12 = r18;
            r14 = r20;
        L_0x0061:
            r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004f }
            r15.<init>();	 Catch:{ all -> 0x004f }
            r15.append(r5);	 Catch:{ all -> 0x004f }
            r5 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x004f }
            r5 = r5.getClass();	 Catch:{ all -> 0x004f }
            r5 = r5.getName();	 Catch:{ all -> 0x004f }
            r15.append(r5);	 Catch:{ all -> 0x004f }
            r15.append(r4);	 Catch:{ all -> 0x004f }
            r4 = r15.toString();	 Catch:{ all -> 0x004f }
            android.util.Log.d(r6, r4, r0);	 Catch:{ all -> 0x004f }
            throw r0;	 Catch:{ all -> 0x004f }
        L_0x0082:
            r0 = move-exception;
            r12 = r18;
            r14 = r20;
        L_0x0087:
            r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004f }
            r15.<init>();	 Catch:{ all -> 0x004f }
            r15.append(r5);	 Catch:{ all -> 0x004f }
            r5 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x004f }
            r5 = r5.getClass();	 Catch:{ all -> 0x004f }
            r5 = r5.getName();	 Catch:{ all -> 0x004f }
            r15.append(r5);	 Catch:{ all -> 0x004f }
            r15.append(r4);	 Catch:{ all -> 0x004f }
            r4 = r15.toString();	 Catch:{ all -> 0x004f }
            android.util.Log.d(r6, r4, r0);	 Catch:{ all -> 0x004f }
            r4 = new java.lang.RuntimeException;	 Catch:{ all -> 0x004f }
            r4.<init>(r0);	 Catch:{ all -> 0x004f }
            throw r4;	 Catch:{ all -> 0x004f }
        L_0x00ac:
            r0 = android.app.backup.BackupAgent.this;
            r0.reloadSharedPreferences();
            android.os.Binder.restoreCallingIdentity(r7);
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ RemoteException -> 0x00be }
            r0 = r0.getBackupUserId();	 Catch:{ RemoteException -> 0x00be }
            r3.opCompleteForUser(r0, r2, r10);	 Catch:{ RemoteException -> 0x00be }
            goto L_0x00bf;
        L_0x00be:
            r0 = move-exception;
        L_0x00bf:
            r0 = android.os.Binder.getCallingPid();
            r5 = android.os.Process.myPid();
            if (r0 == r5) goto L_0x00cf;
        L_0x00c9:
            libcore.io.IoUtils.closeQuietly(r17);
            libcore.io.IoUtils.closeQuietly(r20);
        L_0x00cf:
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent$BackupServiceBinder.doRestore(android.os.ParcelFileDescriptor, long, android.os.ParcelFileDescriptor, int, android.app.backup.IBackupManager):void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:44:0x00f4  */
        public void doFullBackup(android.os.ParcelFileDescriptor r17, long r18, int r20, android.app.backup.IBackupManager r21, int r22) {
            /*
            r16 = this;
            r1 = r16;
            r2 = r20;
            r3 = r21;
            r4 = ") threw";
            r5 = "Unable to finalize backup stream!";
            r6 = "onFullBackup (";
            r7 = "BackupServiceBinder";
            r8 = android.os.Binder.clearCallingIdentity();
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            r12 = 4;
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ IOException -> 0x0096, RuntimeException -> 0x006e, all -> 0x0065 }
            r13 = new android.app.backup.FullBackupDataOutput;	 Catch:{ IOException -> 0x0096, RuntimeException -> 0x006e, all -> 0x0065 }
            r14 = r17;
            r10 = r18;
            r15 = r22;
            r13.<init>(r14, r10, r15);	 Catch:{ IOException -> 0x0063, RuntimeException -> 0x0061 }
            r0.onFullBackup(r13);	 Catch:{ IOException -> 0x0063, RuntimeException -> 0x0061 }
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            r0 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x003d }
            r4 = r17.getFileDescriptor();	 Catch:{ IOException -> 0x003d }
            r0.<init>(r4);	 Catch:{ IOException -> 0x003d }
            r4 = new byte[r12];	 Catch:{ IOException -> 0x003d }
            r0.write(r4);	 Catch:{ IOException -> 0x003d }
            goto L_0x0041;
        L_0x003d:
            r0 = move-exception;
            android.util.Log.e(r7, r5);
        L_0x0041:
            android.os.Binder.restoreCallingIdentity(r8);
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ RemoteException -> 0x0050 }
            r0 = r0.getBackupUserId();	 Catch:{ RemoteException -> 0x0050 }
            r4 = 0;
            r3.opCompleteForUser(r0, r2, r4);	 Catch:{ RemoteException -> 0x0050 }
            goto L_0x0051;
        L_0x0050:
            r0 = move-exception;
        L_0x0051:
            r0 = android.os.Binder.getCallingPid();
            r4 = android.os.Process.myPid();
            if (r0 == r4) goto L_0x005e;
        L_0x005b:
            libcore.io.IoUtils.closeQuietly(r17);
        L_0x005e:
            return;
        L_0x005f:
            r0 = move-exception;
            goto L_0x006c;
        L_0x0061:
            r0 = move-exception;
            goto L_0x0075;
        L_0x0063:
            r0 = move-exception;
            goto L_0x009d;
        L_0x0065:
            r0 = move-exception;
            r14 = r17;
            r10 = r18;
            r15 = r22;
        L_0x006c:
            r4 = r0;
            goto L_0x00c2;
        L_0x006e:
            r0 = move-exception;
            r14 = r17;
            r10 = r18;
            r15 = r22;
        L_0x0075:
            r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005f }
            r13.<init>();	 Catch:{ all -> 0x005f }
            r13.append(r6);	 Catch:{ all -> 0x005f }
            r6 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x005f }
            r6 = r6.getClass();	 Catch:{ all -> 0x005f }
            r6 = r6.getName();	 Catch:{ all -> 0x005f }
            r13.append(r6);	 Catch:{ all -> 0x005f }
            r13.append(r4);	 Catch:{ all -> 0x005f }
            r4 = r13.toString();	 Catch:{ all -> 0x005f }
            android.util.Log.d(r7, r4, r0);	 Catch:{ all -> 0x005f }
            throw r0;	 Catch:{ all -> 0x005f }
        L_0x0096:
            r0 = move-exception;
            r14 = r17;
            r10 = r18;
            r15 = r22;
        L_0x009d:
            r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005f }
            r13.<init>();	 Catch:{ all -> 0x005f }
            r13.append(r6);	 Catch:{ all -> 0x005f }
            r6 = android.app.backup.BackupAgent.this;	 Catch:{ all -> 0x005f }
            r6 = r6.getClass();	 Catch:{ all -> 0x005f }
            r6 = r6.getName();	 Catch:{ all -> 0x005f }
            r13.append(r6);	 Catch:{ all -> 0x005f }
            r13.append(r4);	 Catch:{ all -> 0x005f }
            r4 = r13.toString();	 Catch:{ all -> 0x005f }
            android.util.Log.d(r7, r4, r0);	 Catch:{ all -> 0x005f }
            r4 = new java.lang.RuntimeException;	 Catch:{ all -> 0x005f }
            r4.<init>(r0);	 Catch:{ all -> 0x005f }
            throw r4;	 Catch:{ all -> 0x005f }
        L_0x00c2:
            r0 = android.app.backup.BackupAgent.this;
            r0.waitForSharedPrefs();
            r0 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00d6 }
            r6 = r17.getFileDescriptor();	 Catch:{ IOException -> 0x00d6 }
            r0.<init>(r6);	 Catch:{ IOException -> 0x00d6 }
            r6 = new byte[r12];	 Catch:{ IOException -> 0x00d6 }
            r0.write(r6);	 Catch:{ IOException -> 0x00d6 }
            goto L_0x00da;
        L_0x00d6:
            r0 = move-exception;
            android.util.Log.e(r7, r5);
        L_0x00da:
            android.os.Binder.restoreCallingIdentity(r8);
            r0 = android.app.backup.BackupAgent.this;	 Catch:{ RemoteException -> 0x00e9 }
            r0 = r0.getBackupUserId();	 Catch:{ RemoteException -> 0x00e9 }
            r5 = 0;
            r3.opCompleteForUser(r0, r2, r5);	 Catch:{ RemoteException -> 0x00e9 }
            goto L_0x00ea;
        L_0x00e9:
            r0 = move-exception;
        L_0x00ea:
            r0 = android.os.Binder.getCallingPid();
            r5 = android.os.Process.myPid();
            if (r0 == r5) goto L_0x00f7;
        L_0x00f4:
            libcore.io.IoUtils.closeQuietly(r17);
        L_0x00f7:
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent$BackupServiceBinder.doFullBackup(android.os.ParcelFileDescriptor, long, int, android.app.backup.IBackupManager, int):void");
        }

        public void doMeasureFullBackup(long quotaBytes, int token, IBackupManager callbackBinder, int transportFlags) {
            StringBuilder stringBuilder;
            String str = ") threw";
            String str2 = "onFullBackup[M] (";
            String str3 = TAG;
            long ident = Binder.clearCallingIdentity();
            FullBackupDataOutput measureOutput = new FullBackupDataOutput(quotaBytes, transportFlags);
            BackupAgent.this.waitForSharedPrefs();
            try {
                BackupAgent.this.onFullBackup(measureOutput);
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, measureOutput.getSize());
                } catch (RemoteException e) {
                }
            } catch (IOException ex) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(BackupAgent.this.getClass().getName());
                stringBuilder.append(str);
                Log.d(str3, stringBuilder.toString(), ex);
                throw new RuntimeException(ex);
            } catch (RuntimeException ex2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(BackupAgent.this.getClass().getName());
                stringBuilder.append(str);
                Log.d(str3, stringBuilder.toString(), ex2);
                throw ex2;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, measureOutput.getSize());
                } catch (RemoteException e2) {
                }
            }
        }

        public void doRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime, int token, IBackupManager callbackBinder) throws RemoteException {
            int i = token;
            IBackupManager iBackupManager = callbackBinder;
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onRestoreFile(data, size, type, domain, path, mode, mtime);
                BackupAgent.this.waitForSharedPrefs();
                BackupAgent.this.reloadSharedPreferences();
                Binder.restoreCallingIdentity(ident);
                try {
                    iBackupManager.opCompleteForUser(BackupAgent.this.getBackupUserId(), i, 0);
                } catch (RemoteException e) {
                }
                if (Binder.getCallingPid() != Process.myPid()) {
                    IoUtils.closeQuietly(data);
                }
            } catch (IOException e2) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onRestoreFile (");
                stringBuilder.append(BackupAgent.this.getClass().getName());
                stringBuilder.append(") threw");
                Log.d(str, stringBuilder.toString(), e2);
                throw new RuntimeException(e2);
            } catch (Throwable th) {
                Throwable th2 = th;
                BackupAgent.this.waitForSharedPrefs();
                BackupAgent.this.reloadSharedPreferences();
                Binder.restoreCallingIdentity(ident);
                try {
                    iBackupManager.opCompleteForUser(BackupAgent.this.getBackupUserId(), i, 0);
                } catch (RemoteException e3) {
                }
                if (Binder.getCallingPid() != Process.myPid()) {
                    IoUtils.closeQuietly(data);
                }
            }
        }

        public void doRestoreFinished(int token, IBackupManager callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onRestoreFinished();
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0);
                } catch (RemoteException e) {
                }
            } catch (Exception e2) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onRestoreFinished (");
                stringBuilder.append(BackupAgent.this.getClass().getName());
                stringBuilder.append(") threw");
                Log.d(str, stringBuilder.toString(), e2);
                throw e2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0);
                } catch (RemoteException e3) {
                }
            }
        }

        public void fail(String message) {
            BackupAgent.this.getHandler().post(new FailRunnable(message));
        }

        public void doQuotaExceeded(long backupDataBytes, long quotaBytes, IBackupCallback callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            try {
                BackupAgent.this.onQuotaExceeded(backupDataBytes, quotaBytes);
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.operationComplete(0);
                } catch (RemoteException e) {
                }
            } catch (Exception e2) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onQuotaExceeded(");
                stringBuilder.append(BackupAgent.this.getClass().getName());
                stringBuilder.append(") threw");
                Log.d(str, stringBuilder.toString(), e2);
                throw e2;
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.operationComplete(-1);
                } catch (RemoteException e3) {
                }
            }
        }
    }

    static class FailRunnable implements Runnable {
        private String mMessage;

        FailRunnable(String message) {
            this.mMessage = message;
        }

        public void run() {
            throw new IllegalStateException(this.mMessage);
        }
    }

    class SharedPrefsSynchronizer implements Runnable {
        public final CountDownLatch mLatch = new CountDownLatch(1);

        SharedPrefsSynchronizer() {
        }

        public void run() {
            QueuedWork.waitToFinish();
            this.mLatch.countDown();
        }
    }

    public abstract void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) throws IOException;

    public abstract void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException;

    /* Access modifiers changed, original: 0000 */
    public Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return this.mHandler;
    }

    private void waitForSharedPrefs() {
        Handler h = getHandler();
        SharedPrefsSynchronizer s = new SharedPrefsSynchronizer();
        h.postAtFrontOfQueue(s);
        try {
            s.mLatch.await();
        } catch (InterruptedException e) {
        }
    }

    public BackupAgent() {
        super(null);
    }

    public void onCreate() {
    }

    public void onCreate(UserHandle user) {
        onCreate();
        this.mUser = user;
    }

    public void onDestroy() {
    }

    public void onRestore(BackupDataInput data, long appVersionCode, ParcelFileDescriptor newState) throws IOException {
        onRestore(data, (int) appVersionCode, newState);
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
        BackupScheme backupScheme = FullBackup.getBackupScheme(this);
        if (backupScheme.isFullBackupContentEnabled()) {
            String str;
            try {
                String libDir;
                Map<String, Set<PathWithRequiredFlags>> manifestIncludeMap = backupScheme.maybeParseAndGetCanonicalIncludePaths();
                ArraySet<PathWithRequiredFlags> manifestExcludeSet = backupScheme.maybeParseAndGetCanonicalExcludePaths();
                String packageName = getPackageName();
                ApplicationInfo appInfo = getApplicationInfo();
                Context ceContext = createCredentialProtectedStorageContext();
                String rootDir = ceContext.getDataDir().getCanonicalPath();
                String filesDir = ceContext.getFilesDir().getCanonicalPath();
                String noBackupDir = ceContext.getNoBackupFilesDir().getCanonicalPath();
                str = "foo";
                String databaseDir = ceContext.getDatabasePath(str).getParentFile().getCanonicalPath();
                String sharedPrefsDir = ceContext.getSharedPreferencesPath(str).getParentFile().getCanonicalPath();
                String cacheDir = ceContext.getCacheDir().getCanonicalPath();
                String codeCacheDir = ceContext.getCodeCacheDir().getCanonicalPath();
                Context deContext = createDeviceProtectedStorageContext();
                String deviceRootDir = deContext.getDataDir().getCanonicalPath();
                String deviceFilesDir = deContext.getFilesDir().getCanonicalPath();
                String deviceNoBackupDir = deContext.getNoBackupFilesDir().getCanonicalPath();
                String rootDir2 = rootDir;
                rootDir = deContext.getDatabasePath(str).getParentFile().getCanonicalPath();
                str = deContext.getSharedPreferencesPath(str).getParentFile().getCanonicalPath();
                Map<String, Set<PathWithRequiredFlags>> manifestIncludeMap2 = manifestIncludeMap;
                String deviceCacheDir = deContext.getCacheDir().getCanonicalPath();
                ArraySet<PathWithRequiredFlags> manifestExcludeSet2 = manifestExcludeSet;
                String deviceCodeCacheDir = deContext.getCodeCacheDir().getCanonicalPath();
                String deviceRootDir2 = deviceRootDir;
                if (appInfo.nativeLibraryDir != null) {
                    libDir = new File(appInfo.nativeLibraryDir).getCanonicalPath();
                } else {
                    libDir = null;
                }
                ArraySet<String> traversalExcludeSet = new ArraySet();
                traversalExcludeSet.add(filesDir);
                traversalExcludeSet.add(noBackupDir);
                traversalExcludeSet.add(databaseDir);
                traversalExcludeSet.add(sharedPrefsDir);
                traversalExcludeSet.add(cacheDir);
                traversalExcludeSet.add(codeCacheDir);
                traversalExcludeSet.add(deviceFilesDir);
                traversalExcludeSet.add(deviceNoBackupDir);
                traversalExcludeSet.add(rootDir);
                traversalExcludeSet.add(str);
                traversalExcludeSet.add(deviceCacheDir);
                traversalExcludeSet.add(deviceCodeCacheDir);
                if (libDir != null) {
                    traversalExcludeSet.add(libDir);
                }
                String deviceSharedPrefsDir = str;
                deviceNoBackupDir = deviceRootDir2;
                ArraySet<String> traversalExcludeSet2 = traversalExcludeSet;
                Map<String, Set<PathWithRequiredFlags>> map = manifestIncludeMap2;
                manifestIncludeMap = map;
                ArraySet<PathWithRequiredFlags> arraySet = manifestExcludeSet2;
                manifestExcludeSet = arraySet;
                applyXmlFiltersAndDoFullBackupForDomain(packageName, "r", manifestIncludeMap, manifestExcludeSet, traversalExcludeSet2, data);
                traversalExcludeSet = traversalExcludeSet2;
                traversalExcludeSet.add(rootDir2);
                cacheDir = packageName;
                str = sharedPrefsDir;
                libDir = databaseDir;
                Map<String, Set<PathWithRequiredFlags>> map2 = manifestIncludeMap;
                ArraySet<PathWithRequiredFlags> arraySet2 = manifestExcludeSet;
                String deviceSharedPrefsDir2 = deviceSharedPrefsDir;
                deviceSharedPrefsDir = filesDir;
                ArraySet<String> arraySet3 = traversalExcludeSet;
                String sharedPrefsDir2 = str;
                str = rootDir;
                FullBackupDataOutput fullBackupDataOutput = data;
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.DEVICE_ROOT_TREE_TOKEN, map2, arraySet2, arraySet3, fullBackupDataOutput);
                traversalExcludeSet.add(deviceNoBackupDir);
                traversalExcludeSet.remove(deviceSharedPrefsDir);
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.FILES_TREE_TOKEN, map2, arraySet2, arraySet3, fullBackupDataOutput);
                traversalExcludeSet.add(deviceSharedPrefsDir);
                traversalExcludeSet.remove(deviceFilesDir);
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.DEVICE_FILES_TREE_TOKEN, map2, arraySet2, arraySet3, fullBackupDataOutput);
                traversalExcludeSet.add(deviceFilesDir);
                traversalExcludeSet.remove(libDir);
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.DATABASE_TREE_TOKEN, map2, arraySet2, arraySet3, fullBackupDataOutput);
                traversalExcludeSet.add(libDir);
                traversalExcludeSet.remove(str);
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.DEVICE_DATABASE_TREE_TOKEN, map2, arraySet2, arraySet3, fullBackupDataOutput);
                traversalExcludeSet.add(str);
                rootDir = sharedPrefsDir2;
                traversalExcludeSet.remove(rootDir);
                deviceFilesDir = rootDir;
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.SHAREDPREFS_TREE_TOKEN, map2, arraySet2, arraySet3, data);
                traversalExcludeSet.add(deviceFilesDir);
                rootDir = deviceSharedPrefsDir2;
                traversalExcludeSet.remove(rootDir);
                deviceFilesDir = rootDir;
                applyXmlFiltersAndDoFullBackupForDomain(cacheDir, FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN, map2, arraySet2, arraySet3, data);
                traversalExcludeSet.add(deviceFilesDir);
                if (!(Process.myUid() == 1000 || getExternalFilesDir(null) == null)) {
                    applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.MANAGED_EXTERNAL_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                }
            } catch (IOException | XmlPullParserException e) {
                BackupScheme backupScheme2 = backupScheme;
                str = "BackupXmlParserLogging";
                if (Log.isLoggable(str, 2)) {
                    Log.v(str, "Exception trying to parse fullBackupContent xml file! Aborting full backup.", e);
                }
            }
        }
    }

    public void onQuotaExceeded(long backupDataBytes, long quotaBytes) {
    }

    private int getBackupUserId() {
        UserHandle userHandle = this.mUser;
        return userHandle == null ? super.getUserId() : userHandle.getIdentifier();
    }

    private void applyXmlFiltersAndDoFullBackupForDomain(String packageName, String domainToken, Map<String, Set<PathWithRequiredFlags>> includeMap, ArraySet<PathWithRequiredFlags> filterSet, ArraySet<String> traversalExcludeSet, FullBackupDataOutput data) throws IOException {
        String str = domainToken;
        if (includeMap == null) {
        } else if (includeMap.size() == 0) {
        } else if (includeMap.get(domainToken) != null) {
            for (PathWithRequiredFlags includeFile : (Set) includeMap.get(domainToken)) {
                if (areIncludeRequiredTransportFlagsSatisfied(includeFile.getRequiredFlags(), data.getTransportFlags())) {
                    fullBackupFileTree(packageName, domainToken, includeFile.getPath(), filterSet, traversalExcludeSet, data);
                }
            }
            return;
        } else {
            return;
        }
        fullBackupFileTree(packageName, domainToken, FullBackup.getBackupScheme(this).tokenToDirectoryPath(domainToken), filterSet, traversalExcludeSet, data);
    }

    private boolean areIncludeRequiredTransportFlagsSatisfied(int includeFlags, int transportFlags) {
        return (transportFlags & includeFlags) == includeFlags;
    }

    /* JADX WARNING: Removed duplicated region for block: B:75:0x01f4  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00dd  */
    public final void fullBackupFile(java.io.File r30, android.app.backup.FullBackupDataOutput r31) {
        /*
        r29 = this;
        r1 = "BackupAgent";
        r0 = "foo";
        r2 = 0;
        r3 = r29.getApplicationInfo();
        r4 = r29.createCredentialProtectedStorageContext();	 Catch:{ IOException -> 0x020c }
        r5 = r4.getDataDir();	 Catch:{ IOException -> 0x020c }
        r5 = r5.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r6 = r4.getFilesDir();	 Catch:{ IOException -> 0x020c }
        r6 = r6.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r7 = r4.getNoBackupFilesDir();	 Catch:{ IOException -> 0x020c }
        r7 = r7.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r8 = r4.getDatabasePath(r0);	 Catch:{ IOException -> 0x020c }
        r8 = r8.getParentFile();	 Catch:{ IOException -> 0x020c }
        r8 = r8.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r9 = r4.getSharedPreferencesPath(r0);	 Catch:{ IOException -> 0x020c }
        r9 = r9.getParentFile();	 Catch:{ IOException -> 0x020c }
        r9 = r9.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r10 = r4.getCacheDir();	 Catch:{ IOException -> 0x020c }
        r10 = r10.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r11 = r4.getCodeCacheDir();	 Catch:{ IOException -> 0x020c }
        r11 = r11.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r12 = r29.createDeviceProtectedStorageContext();	 Catch:{ IOException -> 0x020c }
        r13 = r12.getDataDir();	 Catch:{ IOException -> 0x020c }
        r13 = r13.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r14 = r12.getFilesDir();	 Catch:{ IOException -> 0x020c }
        r14 = r14.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r15 = r12.getNoBackupFilesDir();	 Catch:{ IOException -> 0x020c }
        r15 = r15.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r16 = r12.getDatabasePath(r0);	 Catch:{ IOException -> 0x020c }
        r16 = r16.getParentFile();	 Catch:{ IOException -> 0x020c }
        r16 = r16.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r17 = r16;
        r0 = r12.getSharedPreferencesPath(r0);	 Catch:{ IOException -> 0x020c }
        r0 = r0.getParentFile();	 Catch:{ IOException -> 0x020c }
        r0 = r0.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r16 = r12.getCacheDir();	 Catch:{ IOException -> 0x020c }
        r16 = r16.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r18 = r16;
        r16 = r12.getCodeCacheDir();	 Catch:{ IOException -> 0x020c }
        r16 = r16.getCanonicalPath();	 Catch:{ IOException -> 0x020c }
        r19 = r16;
        r16 = r2;
        r2 = r3.nativeLibraryDir;	 Catch:{ IOException -> 0x0208 }
        r20 = r4;
        if (r2 != 0) goto L_0x00a1;
    L_0x009f:
        r4 = 0;
        goto L_0x00ac;
    L_0x00a1:
        r2 = new java.io.File;	 Catch:{ IOException -> 0x0208 }
        r4 = r3.nativeLibraryDir;	 Catch:{ IOException -> 0x0208 }
        r2.<init>(r4);	 Catch:{ IOException -> 0x0208 }
        r4 = r2.getCanonicalPath();	 Catch:{ IOException -> 0x0208 }
    L_0x00ac:
        r2 = r4;
        r4 = android.os.Process.myUid();	 Catch:{ IOException -> 0x0208 }
        r22 = r3;
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r4 == r3) goto L_0x00cc;
    L_0x00b7:
        r4 = 0;
        r3 = r29;
        r4 = r3.getExternalFilesDir(r4);	 Catch:{ IOException -> 0x00c9 }
        if (r4 == 0) goto L_0x00ce;
    L_0x00c0:
        r21 = r4.getCanonicalPath();	 Catch:{ IOException -> 0x00c9 }
        r16 = r21;
        r4 = r16;
        goto L_0x00d0;
    L_0x00c9:
        r0 = move-exception;
        goto L_0x0211;
    L_0x00cc:
        r3 = r29;
    L_0x00ce:
        r4 = r16;
    L_0x00d0:
        r16 = r30.getCanonicalPath();	 Catch:{ IOException -> 0x0204 }
        r12 = r16;
        r16 = r12.startsWith(r10);
        if (r16 != 0) goto L_0x01f4;
    L_0x00dd:
        r16 = r12.startsWith(r11);
        if (r16 != 0) goto L_0x01e9;
    L_0x00e3:
        r16 = r12.startsWith(r7);
        if (r16 != 0) goto L_0x01de;
    L_0x00e9:
        r3 = r18;
        r16 = r12.startsWith(r3);
        if (r16 != 0) goto L_0x01d3;
    L_0x00f1:
        r16 = r3;
        r3 = r19;
        r18 = r12.startsWith(r3);
        if (r18 != 0) goto L_0x01cc;
    L_0x00fb:
        r18 = r12.startsWith(r15);
        if (r18 != 0) goto L_0x01c5;
    L_0x0101:
        r18 = r12.startsWith(r2);
        if (r18 == 0) goto L_0x010f;
    L_0x0107:
        r19 = r2;
        r20 = r17;
        r17 = r0;
        goto L_0x01fe;
    L_0x010f:
        r18 = 0;
        r19 = r12.startsWith(r8);
        if (r19 == 0) goto L_0x0123;
    L_0x0117:
        r1 = "db";
        r18 = r8;
        r19 = r2;
        r2 = r17;
        r17 = r18;
        goto L_0x0195;
    L_0x0123:
        r19 = r12.startsWith(r9);
        if (r19 == 0) goto L_0x0136;
    L_0x0129:
        r1 = "sp";
        r18 = r9;
        r19 = r2;
        r2 = r17;
        r17 = r18;
        goto L_0x0195;
    L_0x0136:
        r19 = r12.startsWith(r6);
        if (r19 == 0) goto L_0x0147;
    L_0x013c:
        r1 = "f";
        r18 = r6;
        r19 = r2;
        r2 = r17;
        r17 = r18;
        goto L_0x0195;
    L_0x0147:
        r19 = r12.startsWith(r5);
        if (r19 == 0) goto L_0x0159;
    L_0x014d:
        r1 = "r";
        r18 = r5;
        r19 = r2;
        r2 = r17;
        r17 = r18;
        goto L_0x0195;
    L_0x0159:
        r19 = r2;
        r2 = r17;
        r17 = r12.startsWith(r2);
        if (r17 == 0) goto L_0x0168;
    L_0x0163:
        r1 = "d_db";
        r17 = r2;
        goto L_0x0195;
    L_0x0168:
        r17 = r12.startsWith(r0);
        if (r17 == 0) goto L_0x0173;
    L_0x016e:
        r1 = "d_sp";
        r17 = r0;
        goto L_0x0195;
    L_0x0173:
        r17 = r12.startsWith(r14);
        if (r17 == 0) goto L_0x017e;
    L_0x0179:
        r1 = "d_f";
        r17 = r14;
        goto L_0x0195;
    L_0x017e:
        r17 = r12.startsWith(r13);
        if (r17 == 0) goto L_0x0189;
    L_0x0184:
        r1 = "d_r";
        r17 = r13;
        goto L_0x0195;
    L_0x0189:
        if (r4 == 0) goto L_0x01a7;
    L_0x018b:
        r17 = r12.startsWith(r4);
        if (r17 == 0) goto L_0x01a7;
    L_0x0191:
        r1 = "ef";
        r17 = r4;
    L_0x0195:
        r23 = r29.getPackageName();
        r25 = 0;
        r24 = r1;
        r26 = r17;
        r27 = r12;
        r28 = r31;
        android.app.backup.FullBackup.backupToTar(r23, r24, r25, r26, r27, r28);
        return;
    L_0x01a7:
        r17 = r0;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r20 = r2;
        r2 = "File ";
        r0.append(r2);
        r0.append(r12);
        r2 = " is in an unsupported location; skipping";
        r0.append(r2);
        r0 = r0.toString();
        android.util.Log.w(r1, r0);
        return;
    L_0x01c5:
        r19 = r2;
        r20 = r17;
        r17 = r0;
        goto L_0x01fe;
    L_0x01cc:
        r19 = r2;
        r20 = r17;
        r17 = r0;
        goto L_0x01fe;
    L_0x01d3:
        r16 = r3;
        r20 = r17;
        r3 = r19;
        r17 = r0;
        r19 = r2;
        goto L_0x01fe;
    L_0x01de:
        r20 = r17;
        r16 = r18;
        r3 = r19;
        r17 = r0;
        r19 = r2;
        goto L_0x01fe;
    L_0x01e9:
        r20 = r17;
        r16 = r18;
        r3 = r19;
        r17 = r0;
        r19 = r2;
        goto L_0x01fe;
    L_0x01f4:
        r20 = r17;
        r16 = r18;
        r3 = r19;
        r17 = r0;
        r19 = r2;
    L_0x01fe:
        r0 = "lib, cache, code_cache, and no_backup files are not backed up";
        android.util.Log.w(r1, r0);
        return;
    L_0x0204:
        r0 = move-exception;
        r16 = r4;
        goto L_0x0211;
    L_0x0208:
        r0 = move-exception;
        r22 = r3;
        goto L_0x0211;
    L_0x020c:
        r0 = move-exception;
        r16 = r2;
        r22 = r3;
    L_0x0211:
        r2 = "Unable to obtain canonical paths";
        android.util.Log.w(r1, r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent.fullBackupFile(java.io.File, android.app.backup.FullBackupDataOutput):void");
    }

    /* Access modifiers changed, original: protected|final */
    public final void fullBackupFileTree(String packageName, String domain, String startingPath, ArraySet<PathWithRequiredFlags> manifestExcludes, ArraySet<String> systemExcludes, FullBackupDataOutput output) {
        StringBuilder stringBuilder;
        ArraySet arraySet = manifestExcludes;
        ArraySet<String> arraySet2 = systemExcludes;
        String str = "BackupXmlParserLogging";
        String domainPath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domainPath != null) {
            File rootFile = new File(startingPath);
            if (rootFile.exists()) {
                LinkedList<File> scanQueue = new LinkedList();
                scanQueue.add(rootFile);
                while (scanQueue.size() > 0) {
                    File file = (File) scanQueue.remove(0);
                    try {
                        StructStat stat = Os.lstat(file.getPath());
                        if (OsConstants.S_ISREG(stat.st_mode) || OsConstants.S_ISDIR(stat.st_mode)) {
                            String filePath = file.getCanonicalPath();
                            if (arraySet == null) {
                            } else if (manifestExcludesContainFilePath(arraySet, filePath)) {
                            }
                            if (arraySet2 == null || !arraySet2.contains(filePath)) {
                                if (OsConstants.S_ISDIR(stat.st_mode)) {
                                    File[] contents = file.listFiles();
                                    if (contents != null) {
                                        int length = contents.length;
                                        int i = 0;
                                        while (i < length) {
                                            scanQueue.add(0, contents[i]);
                                            i++;
                                            ArraySet<PathWithRequiredFlags> arraySet3 = manifestExcludes;
                                        }
                                    }
                                }
                                FullBackup.backupToTar(packageName, domain, null, domainPath, filePath, output);
                                arraySet = manifestExcludes;
                            }
                        }
                    } catch (IOException e) {
                        if (Log.isLoggable(str, 2)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Error canonicalizing path of ");
                            stringBuilder.append(file);
                            Log.v(str, stringBuilder.toString());
                        }
                        arraySet = manifestExcludes;
                    } catch (ErrnoException e2) {
                        if (Log.isLoggable(str, 2)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Error scanning file ");
                            stringBuilder.append(file);
                            stringBuilder.append(" : ");
                            stringBuilder.append(e2);
                            Log.v(str, stringBuilder.toString());
                        }
                        arraySet = manifestExcludes;
                    }
                }
            }
        }
    }

    private boolean manifestExcludesContainFilePath(ArraySet<PathWithRequiredFlags> manifestExcludes, String filePath) {
        Iterator it = manifestExcludes.iterator();
        while (it.hasNext()) {
            String excludePath = ((PathWithRequiredFlags) it.next()).getPath();
            if (excludePath != null && excludePath.equals(filePath)) {
                return true;
            }
        }
        return false;
    }

    public void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        File file = destination;
        FullBackup.restoreFile(data, size, type, mode, mtime, isFileEligibleForRestore(file) ? file : null);
    }

    private boolean isFileEligibleForRestore(File destination) throws IOException {
        BackupScheme bs = FullBackup.getBackupScheme(this);
        String excludes = "onRestoreFile \"";
        String str = "BackupXmlParserLogging";
        if (bs.isFullBackupContentEnabled()) {
            String destinationCanonicalPath = destination.getCanonicalPath();
            StringBuilder stringBuilder;
            try {
                Map<String, Set<PathWithRequiredFlags>> includes = bs.maybeParseAndGetCanonicalIncludePaths();
                excludes = bs.maybeParseAndGetCanonicalExcludePaths();
                if (excludes == null || !BackupUtils.isFileSpecifiedInPathList(destination, excludes)) {
                    if (!(includes == null || includes.isEmpty())) {
                        ArraySet<PathWithRequiredFlags> excludes2 = false;
                        for (Set<PathWithRequiredFlags> domainIncludes : includes.values()) {
                            excludes2 |= BackupUtils.isFileSpecifiedInPathList(destination, domainIncludes);
                            if (excludes2 != null) {
                                break;
                            }
                        }
                        if (excludes2 == null) {
                            if (Log.isLoggable(str, 2)) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("onRestoreFile: Trying to restore \"");
                                stringBuilder.append(destinationCanonicalPath);
                                stringBuilder.append("\" but it isn't specified in the included files; skipping.");
                                Log.v(str, stringBuilder.toString());
                            }
                            return false;
                        }
                    }
                    return true;
                }
                if (Log.isLoggable(str, 2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("onRestoreFile: \"");
                    stringBuilder.append(destinationCanonicalPath);
                    stringBuilder.append("\": listed in excludes; skipping.");
                    Log.v(str, stringBuilder.toString());
                }
                return false;
            } catch (XmlPullParserException e) {
                if (Log.isLoggable(str, 2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(excludes);
                    stringBuilder.append(destinationCanonicalPath);
                    stringBuilder.append("\" : Exception trying to parse fullBackupContent xml file! Aborting onRestoreFile.");
                    Log.v(str, stringBuilder.toString(), e);
                }
                return false;
            }
        }
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(excludes);
            stringBuilder2.append(destination.getCanonicalPath());
            stringBuilder2.append("\" : fullBackupContent not enabled for ");
            stringBuilder2.append(getPackageName());
            Log.v(str, stringBuilder2.toString());
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime) throws IOException {
        long mode2;
        String str = domain;
        String basePath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(str);
        if (str.equals(FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
            mode2 = -1;
        } else {
            mode2 = mode;
        }
        if (basePath != null) {
            File outFile = new File(basePath, path);
            String outPath = outFile.getCanonicalPath();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(basePath);
            stringBuilder.append(File.separatorChar);
            if (outPath.startsWith(stringBuilder.toString())) {
                onRestoreFile(data, size, outFile, type, mode2, mtime);
                return;
            }
        }
        String str2 = path;
        FullBackup.restoreFile(data, size, type, mode2, mtime, null);
    }

    public void onRestoreFinished() {
    }

    public final IBinder onBind() {
        return this.mBinder;
    }

    public void attach(Context context) {
        attachBaseContext(context);
    }
}
