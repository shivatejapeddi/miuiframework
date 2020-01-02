package android.database.sqlite;

import android.database.sqlite.SQLiteDebug.DbStats;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.OperationCanceledException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.PrefixPrinter;
import android.util.Printer;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public final class SQLiteConnectionPool implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int CONNECTION_FLAG_INTERACTIVE = 4;
    public static final int CONNECTION_FLAG_PRIMARY_CONNECTION_AFFINITY = 2;
    public static final int CONNECTION_FLAG_READ_ONLY = 1;
    private static final long CONNECTION_POOL_BUSY_MILLIS = 30000;
    private static final String TAG = "SQLiteConnectionPool";
    private final WeakHashMap<SQLiteConnection, AcquiredConnectionStatus> mAcquiredConnections = new WeakHashMap();
    private final ArrayList<SQLiteConnection> mAvailableNonPrimaryConnections = new ArrayList();
    private SQLiteConnection mAvailablePrimaryConnection;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final SQLiteDatabaseConfiguration mConfiguration;
    private final AtomicBoolean mConnectionLeaked = new AtomicBoolean();
    private ConnectionWaiter mConnectionWaiterPool;
    private ConnectionWaiter mConnectionWaiterQueue;
    @GuardedBy({"mLock"})
    private IdleConnectionHandler mIdleConnectionHandler;
    private boolean mIsOpen;
    private final Object mLock = new Object();
    private int mMaxConnectionPoolSize;
    private int mNextConnectionId;
    private final AtomicLong mTotalExecutionTimeCounter = new AtomicLong(0);

    enum AcquiredConnectionStatus {
        NORMAL,
        RECONFIGURE,
        DISCARD
    }

    private static final class ConnectionWaiter {
        public SQLiteConnection mAssignedConnection;
        public int mConnectionFlags;
        public RuntimeException mException;
        public ConnectionWaiter mNext;
        public int mNonce;
        public int mPriority;
        public String mSql;
        public long mStartTime;
        public Thread mThread;
        public boolean mWantPrimaryConnection;

        private ConnectionWaiter() {
        }

        /* synthetic */ ConnectionWaiter(AnonymousClass1 x0) {
            this();
        }
    }

    private class IdleConnectionHandler extends Handler {
        private final long mTimeout;

        IdleConnectionHandler(Looper looper, long timeout) {
            super(looper);
            this.mTimeout = timeout;
        }

        /* JADX WARNING: Missing block: B:13:0x0057, code skipped:
            return;
     */
        public void handleMessage(android.os.Message r6) {
            /*
            r5 = this;
            r0 = android.database.sqlite.SQLiteConnectionPool.this;
            r0 = r0.mLock;
            monitor-enter(r0);
            r1 = android.database.sqlite.SQLiteConnectionPool.this;	 Catch:{ all -> 0x0058 }
            r1 = r1.mIdleConnectionHandler;	 Catch:{ all -> 0x0058 }
            if (r5 == r1) goto L_0x0011;
        L_0x000f:
            monitor-exit(r0);	 Catch:{ all -> 0x0058 }
            return;
        L_0x0011:
            r1 = android.database.sqlite.SQLiteConnectionPool.this;	 Catch:{ all -> 0x0058 }
            r2 = r6.what;	 Catch:{ all -> 0x0058 }
            r1 = r1.closeAvailableConnectionLocked(r2);	 Catch:{ all -> 0x0058 }
            if (r1 == 0) goto L_0x0056;
        L_0x001b:
            r1 = "SQLiteConnectionPool";
            r2 = 3;
            r1 = android.util.Log.isLoggable(r1, r2);	 Catch:{ all -> 0x0058 }
            if (r1 == 0) goto L_0x0056;
        L_0x0024:
            r1 = "SQLiteConnectionPool";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0058 }
            r2.<init>();	 Catch:{ all -> 0x0058 }
            r3 = "Closed idle connection ";
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r3 = android.database.sqlite.SQLiteConnectionPool.this;	 Catch:{ all -> 0x0058 }
            r3 = r3.mConfiguration;	 Catch:{ all -> 0x0058 }
            r3 = r3.label;	 Catch:{ all -> 0x0058 }
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r3 = " ";
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r3 = r6.what;	 Catch:{ all -> 0x0058 }
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r3 = " after ";
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r3 = r5.mTimeout;	 Catch:{ all -> 0x0058 }
            r2.append(r3);	 Catch:{ all -> 0x0058 }
            r2 = r2.toString();	 Catch:{ all -> 0x0058 }
            android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0058 }
        L_0x0056:
            monitor-exit(r0);	 Catch:{ all -> 0x0058 }
            return;
        L_0x0058:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0058 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnectionPool$IdleConnectionHandler.handleMessage(android.os.Message):void");
        }

        /* Access modifiers changed, original: 0000 */
        public void connectionReleased(SQLiteConnection con) {
            sendEmptyMessageDelayed(con.getConnectionId(), this.mTimeout);
        }

        /* Access modifiers changed, original: 0000 */
        public void connectionAcquired(SQLiteConnection con) {
            removeMessages(con.getConnectionId());
        }

        /* Access modifiers changed, original: 0000 */
        public void connectionClosed(SQLiteConnection con) {
            removeMessages(con.getConnectionId());
        }
    }

    private SQLiteConnectionPool(SQLiteDatabaseConfiguration configuration) {
        this.mConfiguration = new SQLiteDatabaseConfiguration(configuration);
        setMaxConnectionPoolSizeLocked();
        if (this.mConfiguration.idleConnectionTimeoutMs != Long.MAX_VALUE) {
            setupIdleConnectionHandler(Looper.getMainLooper(), this.mConfiguration.idleConnectionTimeoutMs);
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public static SQLiteConnectionPool open(SQLiteDatabaseConfiguration configuration) {
        if (configuration != null) {
            SQLiteConnectionPool pool = new SQLiteConnectionPool(configuration);
            pool.open();
            return pool;
        }
        throw new IllegalArgumentException("configuration must not be null.");
    }

    private void open() {
        this.mAvailablePrimaryConnection = openConnectionLocked(this.mConfiguration, true);
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionReleased(this.mAvailablePrimaryConnection);
            }
        }
        this.mIsOpen = true;
        this.mCloseGuard.open("close");
    }

    public void close() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (finalized) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        if (!finalized) {
            synchronized (this.mLock) {
                throwIfClosedLocked();
                this.mIsOpen = false;
                closeAvailableConnectionsAndLogExceptionsLocked();
                int pendingCount = this.mAcquiredConnections.size();
                if (pendingCount != 0) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The connection pool for ");
                    stringBuilder.append(this.mConfiguration.label);
                    stringBuilder.append(" has been closed but there are still ");
                    stringBuilder.append(pendingCount);
                    stringBuilder.append(" connections in use.  They will be closed as they are released back to the pool.");
                    Log.i(str, stringBuilder.toString());
                }
                wakeConnectionWaitersLocked();
            }
        }
    }

    public void reconfigure(SQLiteDatabaseConfiguration configuration) {
        if (configuration != null) {
            synchronized (this.mLock) {
                throwIfClosedLocked();
                boolean onlyCompatWalChanged = false;
                boolean walModeChanged = ((configuration.openFlags ^ this.mConfiguration.openFlags) & 536870912) != 0;
                if (walModeChanged) {
                    if (this.mAcquiredConnections.isEmpty()) {
                        closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
                    } else {
                        throw new IllegalStateException("Write Ahead Logging (WAL) mode cannot be enabled or disabled while there are transactions in progress.  Finish all transactions and release all active database connections first.");
                    }
                }
                if (configuration.foreignKeyConstraintsEnabled != this.mConfiguration.foreignKeyConstraintsEnabled) {
                    if (!this.mAcquiredConnections.isEmpty()) {
                        throw new IllegalStateException("Foreign Key Constraints cannot be enabled or disabled while there are transactions in progress.  Finish all transactions and release all active database connections first.");
                    }
                }
                if ((this.mConfiguration.openFlags ^ configuration.openFlags) == Integer.MIN_VALUE) {
                    onlyCompatWalChanged = true;
                }
                if (onlyCompatWalChanged || this.mConfiguration.openFlags == configuration.openFlags) {
                    this.mConfiguration.updateParametersFrom(configuration);
                    setMaxConnectionPoolSizeLocked();
                    closeExcessConnectionsAndLogExceptionsLocked();
                    reconfigureAllConnectionsLocked();
                } else {
                    if (walModeChanged) {
                        closeAvailableConnectionsAndLogExceptionsLocked();
                    }
                    SQLiteConnection newPrimaryConnection = openConnectionLocked(configuration, true);
                    closeAvailableConnectionsAndLogExceptionsLocked();
                    discardAcquiredConnectionsLocked();
                    this.mAvailablePrimaryConnection = newPrimaryConnection;
                    this.mConfiguration.updateParametersFrom(configuration);
                    setMaxConnectionPoolSizeLocked();
                }
                wakeConnectionWaitersLocked();
            }
            return;
        }
        throw new IllegalArgumentException("configuration must not be null.");
    }

    public SQLiteConnection acquireConnection(String sql, int connectionFlags, CancellationSignal cancellationSignal) {
        SQLiteConnection con = waitForConnection(sql, connectionFlags, cancellationSignal);
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionAcquired(con);
            }
        }
        return con;
    }

    public void releaseConnection(SQLiteConnection connection) {
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionReleased(connection);
            }
            AcquiredConnectionStatus status = (AcquiredConnectionStatus) this.mAcquiredConnections.remove(connection);
            if (status != null) {
                if (!this.mIsOpen) {
                    closeConnectionAndLogExceptionsLocked(connection);
                } else if (connection.isPrimaryConnection()) {
                    if (recycleConnectionLocked(connection, status)) {
                        this.mAvailablePrimaryConnection = connection;
                    }
                    wakeConnectionWaitersLocked();
                } else if (this.mAvailableNonPrimaryConnections.size() >= this.mMaxConnectionPoolSize - 1) {
                    closeConnectionAndLogExceptionsLocked(connection);
                } else {
                    if (recycleConnectionLocked(connection, status)) {
                        this.mAvailableNonPrimaryConnections.add(connection);
                    }
                    wakeConnectionWaitersLocked();
                }
            } else {
                throw new IllegalStateException("Cannot perform this operation because the specified connection was not acquired from this pool or has already been released.");
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean recycleConnectionLocked(SQLiteConnection connection, AcquiredConnectionStatus status) {
        if (status == AcquiredConnectionStatus.RECONFIGURE) {
            try {
                connection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to reconfigure released connection, closing it: ");
                stringBuilder.append(connection);
                Log.e(TAG, stringBuilder.toString(), ex);
                status = AcquiredConnectionStatus.DISCARD;
            }
        }
        if (status != AcquiredConnectionStatus.DISCARD) {
            return true;
        }
        closeConnectionAndLogExceptionsLocked(connection);
        return false;
    }

    public boolean shouldYieldConnection(SQLiteConnection connection, int connectionFlags) {
        synchronized (this.mLock) {
            if (!this.mAcquiredConnections.containsKey(connection)) {
                throw new IllegalStateException("Cannot perform this operation because the specified connection was not acquired from this pool or has already been released.");
            } else if (this.mIsOpen) {
                boolean isSessionBlockingImportantConnectionWaitersLocked = isSessionBlockingImportantConnectionWaitersLocked(connection.isPrimaryConnection(), connectionFlags);
                return isSessionBlockingImportantConnectionWaitersLocked;
            } else {
                return false;
            }
        }
    }

    public void collectDbStats(ArrayList<DbStats> dbStatsList) {
        synchronized (this.mLock) {
            if (this.mAvailablePrimaryConnection != null) {
                this.mAvailablePrimaryConnection.collectDbStats(dbStatsList);
            }
            Iterator it = this.mAvailableNonPrimaryConnections.iterator();
            while (it.hasNext()) {
                ((SQLiteConnection) it.next()).collectDbStats(dbStatsList);
            }
            for (SQLiteConnection connection : this.mAcquiredConnections.keySet()) {
                connection.collectDbStatsUnsafe(dbStatsList);
            }
        }
    }

    private SQLiteConnection openConnectionLocked(SQLiteDatabaseConfiguration configuration, boolean primaryConnection) {
        int connectionId = this.mNextConnectionId;
        this.mNextConnectionId = connectionId + 1;
        return SQLiteConnection.open(this, configuration, connectionId, primaryConnection);
    }

    /* Access modifiers changed, original: 0000 */
    public void onConnectionLeaked() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("A SQLiteConnection object for database '");
        stringBuilder.append(this.mConfiguration.label);
        stringBuilder.append("' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.");
        Log.w(TAG, stringBuilder.toString());
        this.mConnectionLeaked.set(true);
    }

    /* Access modifiers changed, original: 0000 */
    public void onStatementExecuted(long executionTimeMs) {
        this.mTotalExecutionTimeCounter.addAndGet(executionTimeMs);
    }

    @GuardedBy({"mLock"})
    private void closeAvailableConnectionsAndLogExceptionsLocked() {
        closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
        SQLiteConnection sQLiteConnection = this.mAvailablePrimaryConnection;
        if (sQLiteConnection != null) {
            closeConnectionAndLogExceptionsLocked(sQLiteConnection);
            this.mAvailablePrimaryConnection = null;
        }
    }

    @GuardedBy({"mLock"})
    private boolean closeAvailableConnectionLocked(int connectionId) {
        for (int i = this.mAvailableNonPrimaryConnections.size() - 1; i >= 0; i--) {
            SQLiteConnection c = (SQLiteConnection) this.mAvailableNonPrimaryConnections.get(i);
            if (c.getConnectionId() == connectionId) {
                closeConnectionAndLogExceptionsLocked(c);
                this.mAvailableNonPrimaryConnections.remove(i);
                return true;
            }
        }
        SQLiteConnection sQLiteConnection = this.mAvailablePrimaryConnection;
        if (sQLiteConnection == null || sQLiteConnection.getConnectionId() != connectionId) {
            return false;
        }
        closeConnectionAndLogExceptionsLocked(this.mAvailablePrimaryConnection);
        this.mAvailablePrimaryConnection = null;
        return true;
    }

    @GuardedBy({"mLock"})
    private void closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked() {
        int count = this.mAvailableNonPrimaryConnections.size();
        for (int i = 0; i < count; i++) {
            closeConnectionAndLogExceptionsLocked((SQLiteConnection) this.mAvailableNonPrimaryConnections.get(i));
        }
        this.mAvailableNonPrimaryConnections.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public void closeAvailableNonPrimaryConnectionsAndLogExceptions() {
        synchronized (this.mLock) {
            closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
        }
    }

    @GuardedBy({"mLock"})
    private void closeExcessConnectionsAndLogExceptionsLocked() {
        int connection = this.mAvailableNonPrimaryConnections.size();
        while (true) {
            int availableCount = connection - 1;
            if (connection > this.mMaxConnectionPoolSize - 1) {
                closeConnectionAndLogExceptionsLocked((SQLiteConnection) this.mAvailableNonPrimaryConnections.remove(availableCount));
                connection = availableCount;
            } else {
                return;
            }
        }
    }

    @GuardedBy({"mLock"})
    private void closeConnectionAndLogExceptionsLocked(SQLiteConnection connection) {
        try {
            connection.close();
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionClosed(connection);
            }
        } catch (RuntimeException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to close connection, its fate is now in the hands of the merciful GC: ");
            stringBuilder.append(connection);
            Log.e(TAG, stringBuilder.toString(), ex);
        }
    }

    private void discardAcquiredConnectionsLocked() {
        markAcquiredConnectionsLocked(AcquiredConnectionStatus.DISCARD);
    }

    @GuardedBy({"mLock"})
    private void reconfigureAllConnectionsLocked() {
        SQLiteConnection sQLiteConnection = this.mAvailablePrimaryConnection;
        String str = TAG;
        if (sQLiteConnection != null) {
            try {
                sQLiteConnection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to reconfigure available primary connection, closing it: ");
                stringBuilder.append(this.mAvailablePrimaryConnection);
                Log.e(str, stringBuilder.toString(), ex);
                closeConnectionAndLogExceptionsLocked(this.mAvailablePrimaryConnection);
                this.mAvailablePrimaryConnection = null;
            }
        }
        int count = this.mAvailableNonPrimaryConnections.size();
        int i = 0;
        while (i < count) {
            SQLiteConnection connection = (SQLiteConnection) this.mAvailableNonPrimaryConnections.get(i);
            try {
                connection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to reconfigure available non-primary connection, closing it: ");
                stringBuilder2.append(connection);
                Log.e(str, stringBuilder2.toString(), ex2);
                closeConnectionAndLogExceptionsLocked(connection);
                int i2 = i - 1;
                this.mAvailableNonPrimaryConnections.remove(i);
                count--;
                i = i2;
            }
            i++;
        }
        markAcquiredConnectionsLocked(AcquiredConnectionStatus.RECONFIGURE);
    }

    private void markAcquiredConnectionsLocked(AcquiredConnectionStatus status) {
        if (!this.mAcquiredConnections.isEmpty()) {
            ArrayList<SQLiteConnection> keysToUpdate = new ArrayList(this.mAcquiredConnections.size());
            for (Entry<SQLiteConnection, AcquiredConnectionStatus> entry : this.mAcquiredConnections.entrySet()) {
                AcquiredConnectionStatus oldStatus = (AcquiredConnectionStatus) entry.getValue();
                if (!(status == oldStatus || oldStatus == AcquiredConnectionStatus.DISCARD)) {
                    keysToUpdate.add((SQLiteConnection) entry.getKey());
                }
            }
            int updateCount = keysToUpdate.size();
            for (int i = 0; i < updateCount; i++) {
                this.mAcquiredConnections.put((SQLiteConnection) keysToUpdate.get(i), status);
            }
        }
    }

    /* JADX WARNING: Missing block: B:35:0x0068, code skipped:
            if (r11 == null) goto L_0x0072;
     */
    /* JADX WARNING: Missing block: B:36:0x006a, code skipped:
            r11.setOnCancelListener(new android.database.sqlite.SQLiteConnectionPool.AnonymousClass1(r9));
     */
    /* JADX WARNING: Missing block: B:37:0x0072, code skipped:
            r3 = 30000;
     */
    /* JADX WARNING: Missing block: B:39:?, code skipped:
            r6 = r1.mStartTime + 30000;
     */
    /* JADX WARNING: Missing block: B:41:0x007e, code skipped:
            if (r9.mConnectionLeaked.compareAndSet(true, r13) == false) goto L_0x008e;
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            r12 = r9.mLock;
     */
    /* JADX WARNING: Missing block: B:44:0x0082, code skipped:
            monitor-enter(r12);
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            wakeConnectionWaitersLocked();
     */
    /* JADX WARNING: Missing block: B:47:0x0086, code skipped:
            monitor-exit(r12);
     */
    /* JADX WARNING: Missing block: B:53:0x008b, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:54:0x008c, code skipped:
            r12 = r14;
     */
    /* JADX WARNING: Missing block: B:55:0x008e, code skipped:
            r12 = r14;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            java.util.concurrent.locks.LockSupport.parkNanos(r9, r3 * android.util.TimeUtils.NANOS_PER_MS);
            java.lang.Thread.interrupted();
            r13 = r9.mLock;
     */
    /* JADX WARNING: Missing block: B:58:0x009c, code skipped:
            monitor-enter(r13);
     */
    /* JADX WARNING: Missing block: B:60:?, code skipped:
            throwIfClosedLocked();
            r14 = r1.mAssignedConnection;
            r15 = r1.mException;
     */
    /* JADX WARNING: Missing block: B:61:0x00a4, code skipped:
            if (r14 != false) goto L_0x00c8;
     */
    /* JADX WARNING: Missing block: B:62:0x00a6, code skipped:
            if (r15 == null) goto L_0x00ab;
     */
    /* JADX WARNING: Missing block: B:63:0x00a8, code skipped:
            r19 = r6;
     */
    /* JADX WARNING: Missing block: B:64:0x00ab, code skipped:
            r16 = android.os.SystemClock.uptimeMillis();
     */
    /* JADX WARNING: Missing block: B:66:0x00b1, code skipped:
            if (r16 >= r6) goto L_0x00b6;
     */
    /* JADX WARNING: Missing block: B:67:0x00b3, code skipped:
            r3 = r16 - r6;
     */
    /* JADX WARNING: Missing block: B:68:0x00b6, code skipped:
            r19 = r6;
     */
    /* JADX WARNING: Missing block: B:70:?, code skipped:
            logConnectionPoolBusyLocked(r16 - r1.mStartTime, r10);
     */
    /* JADX WARNING: Missing block: B:71:0x00bf, code skipped:
            r3 = 30000;
            r6 = r16 + 30000;
     */
    /* JADX WARNING: Missing block: B:73:?, code skipped:
            monitor-exit(r13);
     */
    /* JADX WARNING: Missing block: B:74:0x00c5, code skipped:
            r14 = r12;
            r13 = false;
     */
    /* JADX WARNING: Missing block: B:75:0x00c8, code skipped:
            r19 = r6;
     */
    /* JADX WARNING: Missing block: B:77:?, code skipped:
            recycleConnectionWaiterLocked(r1);
     */
    /* JADX WARNING: Missing block: B:78:0x00cd, code skipped:
            if (r14 == false) goto L_0x00d8;
     */
    /* JADX WARNING: Missing block: B:79:0x00cf, code skipped:
            monitor-exit(r13);
     */
    /* JADX WARNING: Missing block: B:80:0x00d0, code skipped:
            if (r11 == null) goto L_0x00d6;
     */
    /* JADX WARNING: Missing block: B:81:0x00d2, code skipped:
            r11.setOnCancelListener(null);
     */
    /* JADX WARNING: Missing block: B:82:0x00d6, code skipped:
            return r14;
     */
    /* JADX WARNING: Missing block: B:84:?, code skipped:
            throw r15;
     */
    /* JADX WARNING: Missing block: B:85:0x00d9, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:86:0x00da, code skipped:
            r6 = r19;
     */
    /* JADX WARNING: Missing block: B:87:0x00dd, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:88:0x00de, code skipped:
            r19 = r6;
     */
    /* JADX WARNING: Missing block: B:90:?, code skipped:
            monitor-exit(r13);
     */
    /* JADX WARNING: Missing block: B:92:?, code skipped:
            throw r0;
     */
    /* JADX WARNING: Missing block: B:93:0x00e2, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:94:0x00e4, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:95:0x00e6, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:96:0x00e7, code skipped:
            r12 = r14;
     */
    /* JADX WARNING: Missing block: B:97:0x00e8, code skipped:
            if (r11 != null) goto L_0x00ea;
     */
    /* JADX WARNING: Missing block: B:98:0x00ea, code skipped:
            r11.setOnCancelListener(null);
     */
    /* JADX WARNING: Missing block: B:99:0x00ee, code skipped:
            throw r0;
     */
    private android.database.sqlite.SQLiteConnection waitForConnection(java.lang.String r22, int r23, android.os.CancellationSignal r24) {
        /*
        r21 = this;
        r9 = r21;
        r10 = r23;
        r11 = r24;
        r0 = r10 & 2;
        r13 = 0;
        if (r0 == 0) goto L_0x000d;
    L_0x000b:
        r0 = 1;
        goto L_0x000e;
    L_0x000d:
        r0 = r13;
    L_0x000e:
        r14 = r0;
        r15 = r9.mLock;
        monitor-enter(r15);
        r21.throwIfClosedLocked();	 Catch:{ all -> 0x00ef }
        if (r11 == 0) goto L_0x001f;
    L_0x0017:
        r24.throwIfCanceled();	 Catch:{ all -> 0x001b }
        goto L_0x001f;
    L_0x001b:
        r0 = move-exception;
        r12 = r14;
        goto L_0x00f1;
    L_0x001f:
        r0 = 0;
        if (r14 != 0) goto L_0x0027;
    L_0x0022:
        r1 = r21.tryAcquireNonPrimaryConnectionLocked(r22, r23);	 Catch:{ all -> 0x001b }
        r0 = r1;
    L_0x0027:
        if (r0 != 0) goto L_0x002e;
    L_0x0029:
        r1 = r9.tryAcquirePrimaryConnectionLocked(r10);	 Catch:{ all -> 0x001b }
        r0 = r1;
    L_0x002e:
        if (r0 == 0) goto L_0x0032;
    L_0x0030:
        monitor-exit(r15);	 Catch:{ all -> 0x001b }
        return r0;
    L_0x0032:
        r1 = getPriority(r23);	 Catch:{ all -> 0x00ef }
        r8 = r1;
        r3 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x00ef }
        r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x00ef }
        r1 = r21;
        r5 = r8;
        r6 = r14;
        r7 = r22;
        r12 = r8;
        r8 = r23;
        r1 = r1.obtainConnectionWaiterLocked(r2, r3, r5, r6, r7, r8);	 Catch:{ all -> 0x00ef }
        r2 = 0;
        r5 = r9.mConnectionWaiterQueue;	 Catch:{ all -> 0x00ef }
    L_0x004f:
        if (r5 == 0) goto L_0x005d;
    L_0x0051:
        r6 = r5.mPriority;	 Catch:{ all -> 0x001b }
        if (r12 <= r6) goto L_0x0058;
    L_0x0055:
        r1.mNext = r5;	 Catch:{ all -> 0x001b }
        goto L_0x005d;
    L_0x0058:
        r2 = r5;
        r6 = r5.mNext;	 Catch:{ all -> 0x001b }
        r5 = r6;
        goto L_0x004f;
    L_0x005d:
        if (r2 == 0) goto L_0x0062;
    L_0x005f:
        r2.mNext = r1;	 Catch:{ all -> 0x001b }
        goto L_0x0064;
    L_0x0062:
        r9.mConnectionWaiterQueue = r1;	 Catch:{ all -> 0x00ef }
    L_0x0064:
        r6 = r1.mNonce;	 Catch:{ all -> 0x00ef }
        r2 = r6;
        monitor-exit(r15);	 Catch:{ all -> 0x00ef }
        if (r11 == 0) goto L_0x0072;
    L_0x006a:
        r0 = new android.database.sqlite.SQLiteConnectionPool$1;
        r0.<init>(r1, r2);
        r11.setOnCancelListener(r0);
    L_0x0072:
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r6 = r1.mStartTime;	 Catch:{ all -> 0x00e6 }
        r6 = r6 + r3;
    L_0x0077:
        r0 = r9.mConnectionLeaked;	 Catch:{ all -> 0x00e6 }
        r8 = 1;
        r0 = r0.compareAndSet(r8, r13);	 Catch:{ all -> 0x00e6 }
        if (r0 == 0) goto L_0x008e;
    L_0x0080:
        r12 = r9.mLock;	 Catch:{ all -> 0x008b }
        monitor-enter(r12);	 Catch:{ all -> 0x008b }
        r21.wakeConnectionWaitersLocked();	 Catch:{ all -> 0x0088 }
        monitor-exit(r12);	 Catch:{ all -> 0x0088 }
        goto L_0x008e;
    L_0x0088:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0088 }
        throw r0;	 Catch:{ all -> 0x008b }
    L_0x008b:
        r0 = move-exception;
        r12 = r14;
        goto L_0x00e8;
    L_0x008e:
        r15 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r12 = r14;
        r13 = r3 * r15;
        java.util.concurrent.locks.LockSupport.parkNanos(r9, r13);	 Catch:{ all -> 0x00e4 }
        java.lang.Thread.interrupted();	 Catch:{ all -> 0x00e4 }
        r13 = r9.mLock;	 Catch:{ all -> 0x00e4 }
        monitor-enter(r13);	 Catch:{ all -> 0x00e4 }
        r21.throwIfClosedLocked();	 Catch:{ all -> 0x00dd }
        r14 = r1.mAssignedConnection;	 Catch:{ all -> 0x00dd }
        r15 = r1.mException;	 Catch:{ all -> 0x00dd }
        if (r14 != 0) goto L_0x00c8;
    L_0x00a6:
        if (r15 == 0) goto L_0x00ab;
    L_0x00a8:
        r19 = r6;
        goto L_0x00ca;
    L_0x00ab:
        r16 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x00dd }
        r18 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r18 >= 0) goto L_0x00b6;
    L_0x00b3:
        r3 = r16 - r6;
        goto L_0x00c4;
    L_0x00b6:
        r19 = r6;
        r5 = r1.mStartTime;	 Catch:{ all -> 0x00d9 }
        r5 = r16 - r5;
        r9.logConnectionPoolBusyLocked(r5, r10);	 Catch:{ all -> 0x00d9 }
        r3 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r5 = r16 + r3;
        r6 = r5;
    L_0x00c4:
        monitor-exit(r13);	 Catch:{ all -> 0x00e2 }
        r14 = r12;
        r13 = 0;
        goto L_0x0077;
    L_0x00c8:
        r19 = r6;
    L_0x00ca:
        r9.recycleConnectionWaiterLocked(r1);	 Catch:{ all -> 0x00d9 }
        if (r14 == 0) goto L_0x00d7;
    L_0x00cf:
        monitor-exit(r13);	 Catch:{ all -> 0x00d9 }
        if (r11 == 0) goto L_0x00d6;
    L_0x00d2:
        r5 = 0;
        r11.setOnCancelListener(r5);
    L_0x00d6:
        return r14;
        throw r15;	 Catch:{ all -> 0x00d9 }
    L_0x00d9:
        r0 = move-exception;
        r6 = r19;
        goto L_0x00e0;
    L_0x00dd:
        r0 = move-exception;
        r19 = r6;
    L_0x00e0:
        monitor-exit(r13);	 Catch:{ all -> 0x00e2 }
        throw r0;	 Catch:{ all -> 0x00e4 }
    L_0x00e2:
        r0 = move-exception;
        goto L_0x00e0;
    L_0x00e4:
        r0 = move-exception;
        goto L_0x00e8;
    L_0x00e6:
        r0 = move-exception;
        r12 = r14;
    L_0x00e8:
        if (r11 == 0) goto L_0x00ee;
    L_0x00ea:
        r3 = 0;
        r11.setOnCancelListener(r3);
    L_0x00ee:
        throw r0;
    L_0x00ef:
        r0 = move-exception;
        r12 = r14;
    L_0x00f1:
        monitor-exit(r15);	 Catch:{ all -> 0x00f3 }
        throw r0;
    L_0x00f3:
        r0 = move-exception;
        goto L_0x00f1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnectionPool.waitForConnection(java.lang.String, int, android.os.CancellationSignal):android.database.sqlite.SQLiteConnection");
    }

    @GuardedBy({"mLock"})
    private void cancelConnectionWaiterLocked(ConnectionWaiter waiter) {
        if (waiter.mAssignedConnection == null && waiter.mException == null) {
            ConnectionWaiter predecessor = null;
            for (ConnectionWaiter current = this.mConnectionWaiterQueue; current != waiter; current = current.mNext) {
                predecessor = current;
            }
            if (predecessor != null) {
                predecessor.mNext = waiter.mNext;
            } else {
                this.mConnectionWaiterQueue = waiter.mNext;
            }
            waiter.mException = new OperationCanceledException();
            LockSupport.unpark(waiter.mThread);
            wakeConnectionWaitersLocked();
        }
    }

    private void logConnectionPoolBusyLocked(long waitMillis, int connectionFlags) {
        String description;
        Thread thread = Thread.currentThread();
        StringBuilder msg = new StringBuilder();
        msg.append("The connection pool for database '");
        msg.append(this.mConfiguration.label);
        msg.append("' has been unable to grant a connection to thread ");
        msg.append(thread.getId());
        msg.append(" (");
        msg.append(thread.getName());
        msg.append(") ");
        msg.append("with flags 0x");
        msg.append(Integer.toHexString(connectionFlags));
        msg.append(" for ");
        msg.append(((float) waitMillis) * 0.001f);
        msg.append(" seconds.\n");
        ArrayList<String> requests = new ArrayList();
        int activeConnections = 0;
        int idleConnections = 0;
        if (!this.mAcquiredConnections.isEmpty()) {
            for (SQLiteConnection connection : this.mAcquiredConnections.keySet()) {
                description = connection.describeCurrentOperationUnsafe();
                if (description != null) {
                    requests.add(description);
                    activeConnections++;
                } else {
                    idleConnections++;
                }
            }
        }
        int availableConnections = this.mAvailableNonPrimaryConnections.size();
        if (this.mAvailablePrimaryConnection != null) {
            availableConnections++;
        }
        msg.append("Connections: ");
        msg.append(activeConnections);
        msg.append(" active, ");
        msg.append(idleConnections);
        msg.append(" idle, ");
        msg.append(availableConnections);
        msg.append(" available.\n");
        if (!requests.isEmpty()) {
            msg.append("\nRequests in progress:\n");
            Iterator it = requests.iterator();
            while (it.hasNext()) {
                description = (String) it.next();
                msg.append("  ");
                msg.append(description);
                msg.append("\n");
            }
        }
        Log.w(TAG, msg.toString());
    }

    @GuardedBy({"mLock"})
    private void wakeConnectionWaitersLocked() {
        ConnectionWaiter predecessor = null;
        ConnectionWaiter waiter = this.mConnectionWaiterQueue;
        boolean primaryConnectionNotAvailable = false;
        boolean nonPrimaryConnectionNotAvailable = false;
        while (waiter != null) {
            boolean unpark = false;
            if (this.mIsOpen) {
                SQLiteConnection connection = null;
                try {
                    if (!(waiter.mWantPrimaryConnection || nonPrimaryConnectionNotAvailable)) {
                        connection = tryAcquireNonPrimaryConnectionLocked(waiter.mSql, waiter.mConnectionFlags);
                        if (connection == null) {
                            nonPrimaryConnectionNotAvailable = true;
                        }
                    }
                    if (connection == null && !primaryConnectionNotAvailable) {
                        connection = tryAcquirePrimaryConnectionLocked(waiter.mConnectionFlags);
                        if (connection == null) {
                            primaryConnectionNotAvailable = true;
                        }
                    }
                    if (connection != null) {
                        waiter.mAssignedConnection = connection;
                        unpark = true;
                    } else if (nonPrimaryConnectionNotAvailable && primaryConnectionNotAvailable) {
                        return;
                    }
                } catch (RuntimeException ex) {
                    waiter.mException = ex;
                    unpark = true;
                }
            } else {
                unpark = true;
            }
            ConnectionWaiter successor = waiter.mNext;
            if (unpark) {
                if (predecessor != null) {
                    predecessor.mNext = successor;
                } else {
                    this.mConnectionWaiterQueue = successor;
                }
                waiter.mNext = null;
                LockSupport.unpark(waiter.mThread);
            } else {
                predecessor = waiter;
            }
            waiter = successor;
        }
    }

    @GuardedBy({"mLock"})
    private SQLiteConnection tryAcquirePrimaryConnectionLocked(int connectionFlags) {
        SQLiteConnection connection = this.mAvailablePrimaryConnection;
        if (connection != null) {
            this.mAvailablePrimaryConnection = null;
            finishAcquireConnectionLocked(connection, connectionFlags);
            return connection;
        }
        for (SQLiteConnection acquiredConnection : this.mAcquiredConnections.keySet()) {
            if (acquiredConnection.isPrimaryConnection()) {
                return null;
            }
        }
        connection = openConnectionLocked(this.mConfiguration, true);
        finishAcquireConnectionLocked(connection, connectionFlags);
        return connection;
    }

    @GuardedBy({"mLock"})
    private SQLiteConnection tryAcquireNonPrimaryConnectionLocked(String sql, int connectionFlags) {
        int i;
        SQLiteConnection connection;
        int availableCount = this.mAvailableNonPrimaryConnections.size();
        if (availableCount > 1 && sql != null) {
            for (i = 0; i < availableCount; i++) {
                connection = (SQLiteConnection) this.mAvailableNonPrimaryConnections.get(i);
                if (connection.isPreparedStatementInCache(sql)) {
                    this.mAvailableNonPrimaryConnections.remove(i);
                    finishAcquireConnectionLocked(connection, connectionFlags);
                    return connection;
                }
            }
        }
        if (availableCount > 0) {
            SQLiteConnection connection2 = (SQLiteConnection) this.mAvailableNonPrimaryConnections.remove(availableCount - 1);
            finishAcquireConnectionLocked(connection2, connectionFlags);
            return connection2;
        }
        i = this.mAcquiredConnections.size();
        if (this.mAvailablePrimaryConnection != null) {
            i++;
        }
        if (i >= this.mMaxConnectionPoolSize) {
            return null;
        }
        connection = openConnectionLocked(this.mConfiguration, false);
        finishAcquireConnectionLocked(connection, connectionFlags);
        return connection;
    }

    @GuardedBy({"mLock"})
    private void finishAcquireConnectionLocked(SQLiteConnection connection, int connectionFlags) {
        try {
            connection.setOnlyAllowReadOnlyOperations((connectionFlags & 1) != 0);
            this.mAcquiredConnections.put(connection, AcquiredConnectionStatus.NORMAL);
        } catch (RuntimeException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to prepare acquired connection for session, closing it: ");
            stringBuilder.append(connection);
            stringBuilder.append(", connectionFlags=");
            stringBuilder.append(connectionFlags);
            Log.e(TAG, stringBuilder.toString());
            closeConnectionAndLogExceptionsLocked(connection);
            throw ex;
        }
    }

    private boolean isSessionBlockingImportantConnectionWaitersLocked(boolean holdingPrimaryConnection, int connectionFlags) {
        ConnectionWaiter waiter = this.mConnectionWaiterQueue;
        if (waiter != null) {
            int priority = getPriority(connectionFlags);
            while (priority <= waiter.mPriority) {
                if (!holdingPrimaryConnection && waiter.mWantPrimaryConnection) {
                    waiter = waiter.mNext;
                    if (waiter == null) {
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static int getPriority(int connectionFlags) {
        return (connectionFlags & 4) != 0 ? 1 : 0;
    }

    private void setMaxConnectionPoolSizeLocked() {
        if (this.mConfiguration.isInMemoryDb() || (this.mConfiguration.openFlags & 536870912) == 0) {
            this.mMaxConnectionPoolSize = 1;
        } else {
            this.mMaxConnectionPoolSize = SQLiteGlobal.getWALConnectionPoolSize();
        }
    }

    @VisibleForTesting
    public void setupIdleConnectionHandler(Looper looper, long timeoutMs) {
        synchronized (this.mLock) {
            this.mIdleConnectionHandler = new IdleConnectionHandler(looper, timeoutMs);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void disableIdleConnectionHandler() {
        synchronized (this.mLock) {
            this.mIdleConnectionHandler = null;
        }
    }

    private void throwIfClosedLocked() {
        if (!this.mIsOpen) {
            throw new IllegalStateException("Cannot perform this operation because the connection pool has been closed.");
        }
    }

    private ConnectionWaiter obtainConnectionWaiterLocked(Thread thread, long startTime, int priority, boolean wantPrimaryConnection, String sql, int connectionFlags) {
        ConnectionWaiter waiter = this.mConnectionWaiterPool;
        if (waiter != null) {
            this.mConnectionWaiterPool = waiter.mNext;
            waiter.mNext = null;
        } else {
            waiter = new ConnectionWaiter();
        }
        waiter.mThread = thread;
        waiter.mStartTime = startTime;
        waiter.mPriority = priority;
        waiter.mWantPrimaryConnection = wantPrimaryConnection;
        waiter.mSql = sql;
        waiter.mConnectionFlags = connectionFlags;
        return waiter;
    }

    private void recycleConnectionWaiterLocked(ConnectionWaiter waiter) {
        waiter.mNext = this.mConnectionWaiterPool;
        waiter.mThread = null;
        waiter.mSql = null;
        waiter.mAssignedConnection = null;
        waiter.mException = null;
        waiter.mNonce++;
        this.mConnectionWaiterPool = waiter;
    }

    public void dump(Printer printer, boolean verbose, ArraySet<String> directories) {
        Printer indentedPrinter = PrefixPrinter.create(printer, "    ");
        synchronized (this.mLock) {
            int count;
            if (directories != null) {
                directories.add(new File(this.mConfiguration.path).getParent());
            }
            boolean isCompatibilityWalEnabled = this.mConfiguration.isLegacyCompatibilityWalEnabled();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Connection pool for ");
            stringBuilder.append(this.mConfiguration.path);
            stringBuilder.append(":");
            printer.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("  Open: ");
            stringBuilder.append(this.mIsOpen);
            printer.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("  Max connections: ");
            stringBuilder.append(this.mMaxConnectionPoolSize);
            printer.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("  Total execution time: ");
            stringBuilder.append(this.mTotalExecutionTimeCounter);
            printer.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("  Configuration: openFlags=");
            stringBuilder.append(this.mConfiguration.openFlags);
            stringBuilder.append(", isLegacyCompatibilityWalEnabled=");
            stringBuilder.append(isCompatibilityWalEnabled);
            stringBuilder.append(", journalMode=");
            stringBuilder.append(TextUtils.emptyIfNull(this.mConfiguration.journalMode));
            stringBuilder.append(", syncMode=");
            stringBuilder.append(TextUtils.emptyIfNull(this.mConfiguration.syncMode));
            printer.println(stringBuilder.toString());
            if (isCompatibilityWalEnabled) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Compatibility WAL enabled: wal_syncmode=");
                stringBuilder.append(SQLiteCompatibilityWalFlags.getWALSyncMode());
                printer.println(stringBuilder.toString());
            }
            if (this.mConfiguration.isLookasideConfigSet()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Lookaside config: sz=");
                stringBuilder.append(this.mConfiguration.lookasideSlotSize);
                stringBuilder.append(" cnt=");
                stringBuilder.append(this.mConfiguration.lookasideSlotCount);
                printer.println(stringBuilder.toString());
            }
            if (this.mConfiguration.idleConnectionTimeoutMs != Long.MAX_VALUE) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Idle connection timeout: ");
                stringBuilder.append(this.mConfiguration.idleConnectionTimeoutMs);
                printer.println(stringBuilder.toString());
            }
            printer.println("  Available primary connection:");
            if (this.mAvailablePrimaryConnection != null) {
                this.mAvailablePrimaryConnection.dump(indentedPrinter, verbose);
            } else {
                indentedPrinter.println("<none>");
            }
            printer.println("  Available non-primary connections:");
            if (this.mAvailableNonPrimaryConnections.isEmpty()) {
                indentedPrinter.println("<none>");
            } else {
                count = this.mAvailableNonPrimaryConnections.size();
                for (int i = 0; i < count; i++) {
                    ((SQLiteConnection) this.mAvailableNonPrimaryConnections.get(i)).dump(indentedPrinter, verbose);
                }
            }
            printer.println("  Acquired connections:");
            if (this.mAcquiredConnections.isEmpty()) {
                indentedPrinter.println("<none>");
            } else {
                for (Entry<SQLiteConnection, AcquiredConnectionStatus> entry : this.mAcquiredConnections.entrySet()) {
                    ((SQLiteConnection) entry.getKey()).dumpUnsafe(indentedPrinter, verbose);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("  Status: ");
                    stringBuilder2.append(entry.getValue());
                    indentedPrinter.println(stringBuilder2.toString());
                }
            }
            printer.println("  Connection waiters:");
            if (this.mConnectionWaiterQueue != null) {
                count = 0;
                long now = SystemClock.uptimeMillis();
                ConnectionWaiter waiter = this.mConnectionWaiterQueue;
                while (waiter != null) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(count);
                    stringBuilder3.append(": waited for ");
                    stringBuilder3.append(((float) (now - waiter.mStartTime)) * 0.001f);
                    stringBuilder3.append(" ms - thread=");
                    stringBuilder3.append(waiter.mThread);
                    stringBuilder3.append(", priority=");
                    stringBuilder3.append(waiter.mPriority);
                    stringBuilder3.append(", sql='");
                    stringBuilder3.append(waiter.mSql);
                    stringBuilder3.append("'");
                    indentedPrinter.println(stringBuilder3.toString());
                    waiter = waiter.mNext;
                    count++;
                }
            } else {
                indentedPrinter.println("<none>");
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQLiteConnectionPool: ");
        stringBuilder.append(this.mConfiguration.path);
        return stringBuilder.toString();
    }

    public String getPath() {
        return this.mConfiguration.path;
    }
}
