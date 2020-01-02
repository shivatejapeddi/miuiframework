package android.content;

import android.accounts.Account;
import android.content.ISyncAdapter.Stub;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadedSyncAdapter {
    private static final boolean ENABLE_LOG;
    @Deprecated
    public static final int LOG_SYNC_DETAILS = 2743;
    private static final String TAG = "SyncAdapter";
    private boolean mAllowParallelSyncs;
    private final boolean mAutoInitialize;
    private final Context mContext;
    private final ISyncAdapterImpl mISyncAdapterImpl;
    private final AtomicInteger mNumSyncStarts;
    private final Object mSyncThreadLock;
    private final HashMap<Account, SyncThread> mSyncThreads;

    private class ISyncAdapterImpl extends Stub {
        private ISyncAdapterImpl() {
        }

        public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback cb) {
            Handler.getMain().sendMessage(PooledLambda.obtainMessage(-$$Lambda$AbstractThreadedSyncAdapter$ISyncAdapterImpl$L6ZtOCe8gjKwJj0908ytPlrD8Rc.INSTANCE, AbstractThreadedSyncAdapter.this, cb));
        }

        /* JADX WARNING: Removed duplicated region for block: B:55:0x010e A:{Catch:{ all -> 0x00fd }} */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x011e  */
        /* JADX WARNING: Missing block: B:26:0x0087, code skipped:
            if (android.content.AbstractThreadedSyncAdapter.access$100() == false) goto L_0x0091;
     */
        /* JADX WARNING: Missing block: B:27:0x0089, code skipped:
            android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "startSync() finishing");
     */
        /* JADX WARNING: Missing block: B:28:0x0091, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:39:0x00e4, code skipped:
            if (r0 == false) goto L_0x00eb;
     */
        /* JADX WARNING: Missing block: B:41:?, code skipped:
            r14.onFinished(android.content.SyncResult.ALREADY_IN_PROGRESS);
     */
        /* JADX WARNING: Missing block: B:43:0x00ef, code skipped:
            if (android.content.AbstractThreadedSyncAdapter.access$100() == false) goto L_0x00f9;
     */
        /* JADX WARNING: Missing block: B:44:0x00f1, code skipped:
            android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "startSync() finishing");
     */
        /* JADX WARNING: Missing block: B:45:0x00f9, code skipped:
            return;
     */
        public void startSync(android.content.ISyncContext r18, java.lang.String r19, android.accounts.Account r20, android.os.Bundle r21) {
            /*
            r17 = this;
            r1 = r17;
            r10 = r19;
            r11 = r20;
            r12 = r21;
            r0 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r0 == 0) goto L_0x003a;
        L_0x000e:
            if (r12 == 0) goto L_0x0013;
        L_0x0010:
            r21.size();
        L_0x0013:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r2 = "startSync() start ";
            r0.append(r2);
            r0.append(r10);
            r2 = " ";
            r0.append(r2);
            r0.append(r11);
            r2 = " ";
            r0.append(r2);
            r0.append(r12);
            r0 = r0.toString();
            r2 = "SyncAdapter";
            android.util.Log.d(r2, r0);
        L_0x003a:
            r0 = new android.content.SyncContext;	 Catch:{ Error | RuntimeException -> 0x0105, Error | RuntimeException -> 0x0105, all -> 0x0101 }
            r13 = r18;
            r0.<init>(r13);	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r14 = r0;
            r0 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r0 = r0.toSyncKey(r11);	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r15 = r0;
            r0 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r16 = r0.mSyncThreadLock;	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            monitor-enter(r16);	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r0 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00fa }
            r0 = r0.mSyncThreads;	 Catch:{ all -> 0x00fa }
            r0 = r0.containsKey(r15);	 Catch:{ all -> 0x00fa }
            r2 = 1;
            if (r0 != 0) goto L_0x00d5;
        L_0x005d:
            r0 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00fa }
            r0 = r0.mAutoInitialize;	 Catch:{ all -> 0x00fa }
            if (r0 == 0) goto L_0x009c;
        L_0x0065:
            if (r12 == 0) goto L_0x009c;
        L_0x0067:
            r0 = "initialize";
            r3 = 0;
            r0 = r12.getBoolean(r0, r3);	 Catch:{ all -> 0x00fa }
            if (r0 == 0) goto L_0x009c;
        L_0x0070:
            r0 = android.content.ContentResolver.getIsSyncable(r11, r10);	 Catch:{ all -> 0x0092 }
            if (r0 >= 0) goto L_0x0079;
        L_0x0076:
            android.content.ContentResolver.setIsSyncable(r11, r10, r2);	 Catch:{ all -> 0x0092 }
        L_0x0079:
            r0 = new android.content.SyncResult;	 Catch:{ all -> 0x00fa }
            r0.<init>();	 Catch:{ all -> 0x00fa }
            r14.onFinished(r0);	 Catch:{ all -> 0x00fa }
            monitor-exit(r16);	 Catch:{ all -> 0x00fa }
            r0 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r0 == 0) goto L_0x0091;
        L_0x0089:
            r0 = "SyncAdapter";
            r2 = "startSync() finishing";
            android.util.Log.d(r0, r2);
        L_0x0091:
            return;
        L_0x0092:
            r0 = move-exception;
            r2 = new android.content.SyncResult;	 Catch:{ all -> 0x00fa }
            r2.<init>();	 Catch:{ all -> 0x00fa }
            r14.onFinished(r2);	 Catch:{ all -> 0x00fa }
            throw r0;	 Catch:{ all -> 0x00fa }
        L_0x009c:
            r0 = new android.content.AbstractThreadedSyncAdapter$SyncThread;	 Catch:{ all -> 0x00fa }
            r3 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00fa }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fa }
            r2.<init>();	 Catch:{ all -> 0x00fa }
            r4 = "SyncAdapterThread-";
            r2.append(r4);	 Catch:{ all -> 0x00fa }
            r4 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00fa }
            r4 = r4.mNumSyncStarts;	 Catch:{ all -> 0x00fa }
            r4 = r4.incrementAndGet();	 Catch:{ all -> 0x00fa }
            r2.append(r4);	 Catch:{ all -> 0x00fa }
            r4 = r2.toString();	 Catch:{ all -> 0x00fa }
            r9 = 0;
            r2 = r0;
            r5 = r14;
            r6 = r19;
            r7 = r20;
            r8 = r21;
            r2.<init>(r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00fa }
            r2 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00fa }
            r2 = r2.mSyncThreads;	 Catch:{ all -> 0x00fa }
            r2.put(r15, r0);	 Catch:{ all -> 0x00fa }
            r0.start();	 Catch:{ all -> 0x00fa }
            r0 = 0;
            goto L_0x00e3;
        L_0x00d5:
            r0 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ all -> 0x00fa }
            if (r0 == 0) goto L_0x00e2;
        L_0x00db:
            r0 = "SyncAdapter";
            r3 = "  alreadyInProgress";
            android.util.Log.d(r0, r3);	 Catch:{ all -> 0x00fa }
        L_0x00e2:
            r0 = r2;
        L_0x00e3:
            monitor-exit(r16);	 Catch:{ all -> 0x00fa }
            if (r0 == 0) goto L_0x00eb;
        L_0x00e6:
            r2 = android.content.SyncResult.ALREADY_IN_PROGRESS;	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
            r14.onFinished(r2);	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
        L_0x00eb:
            r0 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r0 == 0) goto L_0x00f9;
        L_0x00f1:
            r0 = "SyncAdapter";
            r2 = "startSync() finishing";
            android.util.Log.d(r0, r2);
        L_0x00f9:
            return;
        L_0x00fa:
            r0 = move-exception;
            monitor-exit(r16);	 Catch:{ all -> 0x00fa }
            throw r0;	 Catch:{ Error | RuntimeException -> 0x00ff, Error | RuntimeException -> 0x00ff }
        L_0x00fd:
            r0 = move-exception;
            goto L_0x0118;
        L_0x00ff:
            r0 = move-exception;
            goto L_0x0108;
        L_0x0101:
            r0 = move-exception;
            r13 = r18;
            goto L_0x0118;
        L_0x0105:
            r0 = move-exception;
            r13 = r18;
        L_0x0108:
            r2 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ all -> 0x00fd }
            if (r2 == 0) goto L_0x0116;
        L_0x010e:
            r2 = "SyncAdapter";
            r3 = "startSync() caught exception";
            android.util.Log.d(r2, r3, r0);	 Catch:{ all -> 0x00fd }
            throw r0;	 Catch:{ all -> 0x00fd }
        L_0x0118:
            r2 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r2 == 0) goto L_0x0126;
        L_0x011e:
            r2 = "SyncAdapter";
            r3 = "startSync() finishing";
            android.util.Log.d(r2, r3);
        L_0x0126:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.content.AbstractThreadedSyncAdapter$ISyncAdapterImpl.startSync(android.content.ISyncContext, java.lang.String, android.accounts.Account, android.os.Bundle):void");
        }

        public void cancelSync(ISyncContext syncContext) {
            SyncThread info = null;
            try {
                synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                    for (SyncThread current : AbstractThreadedSyncAdapter.this.mSyncThreads.values()) {
                        if (current.mSyncContext.getSyncContextBinder() == syncContext.asBinder()) {
                            info = current;
                            break;
                        }
                    }
                }
                if (info != null) {
                    if (AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        String str = AbstractThreadedSyncAdapter.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("cancelSync() ");
                        stringBuilder.append(info.mAuthority);
                        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder.append(info.mAccount);
                        Log.d(str, stringBuilder.toString());
                    }
                    if (AbstractThreadedSyncAdapter.this.mAllowParallelSyncs) {
                        AbstractThreadedSyncAdapter.this.onSyncCanceled(info);
                    } else {
                        AbstractThreadedSyncAdapter.this.onSyncCanceled();
                    }
                } else if (AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    Log.w(AbstractThreadedSyncAdapter.TAG, "cancelSync() unknown context");
                }
                if (AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    Log.d(AbstractThreadedSyncAdapter.TAG, "cancelSync() finishing");
                }
            } catch (Error | RuntimeException th) {
                try {
                    if (AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        Log.d(AbstractThreadedSyncAdapter.TAG, "cancelSync() caught exception", th);
                    }
                    throw th;
                } catch (Throwable th2) {
                    if (AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        Log.d(AbstractThreadedSyncAdapter.TAG, "cancelSync() finishing");
                    }
                }
            }
        }
    }

    private class SyncThread extends Thread {
        private final Account mAccount;
        private final String mAuthority;
        private final Bundle mExtras;
        private final SyncContext mSyncContext;
        private final Account mThreadsKey;

        private SyncThread(String name, SyncContext syncContext, String authority, Account account, Bundle extras) {
            super(name);
            this.mSyncContext = syncContext;
            this.mAuthority = authority;
            this.mAccount = account;
            this.mExtras = extras;
            this.mThreadsKey = AbstractThreadedSyncAdapter.this.toSyncKey(account);
        }

        /* JADX WARNING: Removed duplicated region for block: B:78:0x0103 A:{Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, all -> 0x00ea }} */
        /* JADX WARNING: Removed duplicated region for block: B:82:0x011c  */
        /* JADX WARNING: Removed duplicated region for block: B:85:0x0125  */
        /* JADX WARNING: Removed duplicated region for block: B:88:0x0131 A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:93:0x0143  */
        /* JADX WARNING: Removed duplicated region for block: B:72:0x00f3 A:{Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, all -> 0x00ea }} */
        /* JADX WARNING: Removed duplicated region for block: B:101:0x0153  */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x015c  */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0168 A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:112:0x017a  */
        public void run() {
            /*
            r11 = this;
            r0 = 10;
            android.os.Process.setThreadPriority(r0);
            r0 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r0 == 0) goto L_0x0012;
        L_0x000b:
            r0 = "SyncAdapter";
            r1 = "Thread started";
            android.util.Log.d(r0, r1);
        L_0x0012:
            r0 = r11.mAuthority;
            r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
            android.os.Trace.traceBegin(r1, r0);
            r0 = new android.content.SyncResult;
            r0.<init>();
            r3 = 0;
            r9 = 1;
            r4 = r11.isCanceled();	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            if (r4 == 0) goto L_0x006a;
        L_0x0026:
            r4 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            if (r4 == 0) goto L_0x0033;
        L_0x002c:
            r4 = "SyncAdapter";
            r5 = "Already canceled";
            android.util.Log.d(r4, r5);	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
        L_0x0033:
            android.os.Trace.traceEnd(r1);
            if (r3 == 0) goto L_0x003b;
        L_0x0038:
            r3.release();
        L_0x003b:
            r1 = r11.isCanceled();
            if (r1 != 0) goto L_0x0046;
        L_0x0041:
            r1 = r11.mSyncContext;
            r1.onFinished(r0);
        L_0x0046:
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r4 = r1.mSyncThreadLock;
            monitor-enter(r4);
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0067 }
            r1 = r1.mSyncThreads;	 Catch:{ all -> 0x0067 }
            r2 = r11.mThreadsKey;	 Catch:{ all -> 0x0067 }
            r1.remove(r2);	 Catch:{ all -> 0x0067 }
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            r1 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r1 == 0) goto L_0x0066;
        L_0x005f:
            r1 = "SyncAdapter";
            r2 = "Thread finished";
            android.util.Log.d(r1, r2);
        L_0x0066:
            return;
        L_0x0067:
            r1 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0067 }
            throw r1;
        L_0x006a:
            r4 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            if (r4 == 0) goto L_0x0077;
        L_0x0070:
            r4 = "SyncAdapter";
            r5 = "Calling onPerformSync...";
            android.util.Log.d(r4, r5);	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
        L_0x0077:
            r4 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            r4 = r4.mContext;	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            r4 = r4.getContentResolver();	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            r5 = r11.mAuthority;	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            r4 = r4.acquireContentProviderClient(r5);	 Catch:{ SecurityException -> 0x00fc, Error | RuntimeException -> 0x00ec, Error | RuntimeException -> 0x00ec }
            r10 = r4;
            if (r10 == 0) goto L_0x0098;
        L_0x008a:
            r3 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            r4 = r11.mAccount;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            r5 = r11.mExtras;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            r6 = r11.mAuthority;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            r7 = r10;
            r8 = r0;
            r3.onPerformSync(r4, r5, r6, r7, r8);	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            goto L_0x009a;
        L_0x0098:
            r0.databaseError = r9;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
        L_0x009a:
            r3 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
            if (r3 == 0) goto L_0x00a8;
        L_0x00a0:
            r3 = "SyncAdapter";
            r4 = "onPerformSync done";
            android.util.Log.d(r3, r4);	 Catch:{ SecurityException -> 0x00e7, Error | RuntimeException -> 0x00e4, Error | RuntimeException -> 0x00e4, all -> 0x00e1 }
        L_0x00a8:
            android.os.Trace.traceEnd(r1);
            if (r10 == 0) goto L_0x00b0;
        L_0x00ad:
            r10.release();
        L_0x00b0:
            r1 = r11.isCanceled();
            if (r1 != 0) goto L_0x00bb;
        L_0x00b6:
            r1 = r11.mSyncContext;
            r1.onFinished(r0);
        L_0x00bb:
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r3 = r1.mSyncThreadLock;
            monitor-enter(r3);
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00de }
            r1 = r1.mSyncThreads;	 Catch:{ all -> 0x00de }
            r2 = r11.mThreadsKey;	 Catch:{ all -> 0x00de }
            r1.remove(r2);	 Catch:{ all -> 0x00de }
            monitor-exit(r3);	 Catch:{ all -> 0x00de }
            r1 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r1 == 0) goto L_0x00db;
        L_0x00d4:
            r1 = "SyncAdapter";
            r2 = "Thread finished";
            android.util.Log.d(r1, r2);
        L_0x00db:
            r3 = r10;
            goto L_0x014a;
        L_0x00de:
            r1 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x00de }
            throw r1;
        L_0x00e1:
            r4 = move-exception;
            r3 = r10;
            goto L_0x014e;
        L_0x00e4:
            r4 = move-exception;
            r3 = r10;
            goto L_0x00ed;
        L_0x00e7:
            r4 = move-exception;
            r3 = r10;
            goto L_0x00fd;
        L_0x00ea:
            r4 = move-exception;
            goto L_0x014e;
        L_0x00ec:
            r4 = move-exception;
        L_0x00ed:
            r5 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ all -> 0x00ea }
            if (r5 == 0) goto L_0x00fa;
        L_0x00f3:
            r5 = "SyncAdapter";
            r6 = "caught exception";
            android.util.Log.d(r5, r6, r4);	 Catch:{ all -> 0x00ea }
            throw r4;	 Catch:{ all -> 0x00ea }
        L_0x00fc:
            r4 = move-exception;
        L_0x00fd:
            r5 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;	 Catch:{ all -> 0x00ea }
            if (r5 == 0) goto L_0x010a;
        L_0x0103:
            r5 = "SyncAdapter";
            r6 = "SecurityException";
            android.util.Log.d(r5, r6, r4);	 Catch:{ all -> 0x00ea }
        L_0x010a:
            r5 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x00ea }
            r6 = r11.mAccount;	 Catch:{ all -> 0x00ea }
            r7 = r11.mExtras;	 Catch:{ all -> 0x00ea }
            r8 = r11.mAuthority;	 Catch:{ all -> 0x00ea }
            r5.onSecurityException(r6, r7, r8, r0);	 Catch:{ all -> 0x00ea }
            r0.databaseError = r9;	 Catch:{ all -> 0x00ea }
            android.os.Trace.traceEnd(r1);
            if (r3 == 0) goto L_0x011f;
        L_0x011c:
            r3.release();
        L_0x011f:
            r1 = r11.isCanceled();
            if (r1 != 0) goto L_0x012a;
        L_0x0125:
            r1 = r11.mSyncContext;
            r1.onFinished(r0);
        L_0x012a:
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r4 = r1.mSyncThreadLock;
            monitor-enter(r4);
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x014b }
            r1 = r1.mSyncThreads;	 Catch:{ all -> 0x014b }
            r2 = r11.mThreadsKey;	 Catch:{ all -> 0x014b }
            r1.remove(r2);	 Catch:{ all -> 0x014b }
            monitor-exit(r4);	 Catch:{ all -> 0x014b }
            r1 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r1 == 0) goto L_0x014a;
        L_0x0143:
            r1 = "SyncAdapter";
            r2 = "Thread finished";
            android.util.Log.d(r1, r2);
        L_0x014a:
            return;
        L_0x014b:
            r1 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x014b }
            throw r1;
        L_0x014e:
            android.os.Trace.traceEnd(r1);
            if (r3 == 0) goto L_0x0156;
        L_0x0153:
            r3.release();
        L_0x0156:
            r1 = r11.isCanceled();
            if (r1 != 0) goto L_0x0161;
        L_0x015c:
            r1 = r11.mSyncContext;
            r1.onFinished(r0);
        L_0x0161:
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r1 = r1.mSyncThreadLock;
            monitor-enter(r1);
            r2 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0182 }
            r2 = r2.mSyncThreads;	 Catch:{ all -> 0x0182 }
            r5 = r11.mThreadsKey;	 Catch:{ all -> 0x0182 }
            r2.remove(r5);	 Catch:{ all -> 0x0182 }
            monitor-exit(r1);	 Catch:{ all -> 0x0182 }
            r1 = android.content.AbstractThreadedSyncAdapter.ENABLE_LOG;
            if (r1 == 0) goto L_0x0181;
        L_0x017a:
            r1 = "SyncAdapter";
            r2 = "Thread finished";
            android.util.Log.d(r1, r2);
        L_0x0181:
            throw r4;
        L_0x0182:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0182 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.content.AbstractThreadedSyncAdapter$SyncThread.run():void");
        }

        private boolean isCanceled() {
            return Thread.currentThread().isInterrupted();
        }
    }

    public abstract void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult);

    static {
        boolean z = Build.IS_DEBUGGABLE && Log.isLoggable(TAG, 3);
        ENABLE_LOG = z;
    }

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize) {
        this(context, autoInitialize, false);
    }

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        this.mSyncThreads = new HashMap();
        this.mSyncThreadLock = new Object();
        this.mContext = context;
        this.mISyncAdapterImpl = new ISyncAdapterImpl();
        this.mNumSyncStarts = new AtomicInteger(0);
        this.mAutoInitialize = autoInitialize;
        this.mAllowParallelSyncs = allowParallelSyncs;
    }

    public Context getContext() {
        return this.mContext;
    }

    private Account toSyncKey(Account account) {
        if (this.mAllowParallelSyncs) {
            return account;
        }
        return null;
    }

    public final IBinder getSyncAdapterBinder() {
        return this.mISyncAdapterImpl.asBinder();
    }

    private void handleOnUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback cb) {
        boolean doSync;
        String str = TAG;
        try {
            doSync = onUnsyncableAccount();
        } catch (RuntimeException e) {
            Log.e(str, "Exception while calling onUnsyncableAccount, assuming 'true'", e);
            doSync = true;
        }
        try {
            cb.onUnsyncableAccountDone(doSync);
        } catch (RemoteException e2) {
            Log.e(str, "Could not report result of onUnsyncableAccount", e2);
        }
    }

    public boolean onUnsyncableAccount() {
        return true;
    }

    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
    }

    public void onSyncCanceled() {
        SyncThread syncThread;
        synchronized (this.mSyncThreadLock) {
            syncThread = (SyncThread) this.mSyncThreads.get(null);
        }
        if (syncThread != null) {
            syncThread.interrupt();
        }
    }

    public void onSyncCanceled(Thread thread) {
        thread.interrupt();
    }
}
