package android.database.sqlite;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.database.sqlite.SQLiteDebug.NoPreloadHolder;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.os.Trace;
import android.security.keymaster.KeymasterDefs;
import android.util.Log;
import android.util.LruCache;
import android.util.Printer;
import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public final class SQLiteConnection implements OnCancelListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean DEBUG = false;
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String TAG = "SQLiteConnection";
    private int mCancellationSignalAttachCount;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final SQLiteDatabaseConfiguration mConfiguration;
    private final int mConnectionId;
    private long mConnectionPtr;
    private final boolean mIsPrimaryConnection;
    private final boolean mIsReadOnlyConnection;
    private boolean mOnlyAllowReadOnlyOperations;
    private final SQLiteConnectionPool mPool;
    private final PreparedStatementCache mPreparedStatementCache;
    private PreparedStatement mPreparedStatementPool;
    private final OperationLog mRecentOperations;

    private static final class Operation {
        private static final int MAX_TRACE_METHOD_NAME_LEN = 256;
        public ArrayList<Object> mBindArgs;
        public int mCookie;
        public long mEndTime;
        public Exception mException;
        public boolean mFinished;
        public String mKind;
        public String mPath;
        public long mResultLong;
        public String mResultString;
        public String mSql;
        public long mStartTime;
        public long mStartWallTime;

        private Operation() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0068  */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x00bf  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00d8  */
        /* JADX WARNING: Removed duplicated region for block: B:48:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x00e6  */
        public void describe(java.lang.StringBuilder r7, boolean r8) {
            /*
            r6 = this;
            r0 = r6.mKind;
            r7.append(r0);
            r0 = r6.mFinished;
            if (r0 == 0) goto L_0x001c;
        L_0x0009:
            r0 = " took ";
            r7.append(r0);
            r0 = r6.mEndTime;
            r2 = r6.mStartTime;
            r0 = r0 - r2;
            r7.append(r0);
            r0 = "ms";
            r7.append(r0);
            goto L_0x0030;
        L_0x001c:
            r0 = " started ";
            r7.append(r0);
            r0 = java.lang.System.currentTimeMillis();
            r2 = r6.mStartWallTime;
            r0 = r0 - r2;
            r7.append(r0);
            r0 = "ms ago";
            r7.append(r0);
        L_0x0030:
            r0 = " - ";
            r7.append(r0);
            r0 = r6.getStatus();
            r7.append(r0);
            r0 = r6.mSql;
            r1 = "\"";
            if (r0 == 0) goto L_0x0053;
        L_0x0042:
            r0 = ", sql=\"";
            r7.append(r0);
            r0 = r6.mSql;
            r0 = android.database.sqlite.SQLiteConnection.trimSqlForDisplay(r0);
            r7.append(r0);
            r7.append(r1);
        L_0x0053:
            if (r8 == 0) goto L_0x0065;
        L_0x0055:
            r0 = android.database.sqlite.SQLiteDebug.NoPreloadHolder.DEBUG_LOG_DETAILED;
            if (r0 == 0) goto L_0x0065;
        L_0x0059:
            r0 = r6.mBindArgs;
            if (r0 == 0) goto L_0x0065;
        L_0x005d:
            r0 = r0.size();
            if (r0 == 0) goto L_0x0065;
        L_0x0063:
            r0 = 1;
            goto L_0x0066;
        L_0x0065:
            r0 = 0;
        L_0x0066:
            if (r0 == 0) goto L_0x00b1;
        L_0x0068:
            r2 = ", bindArgs=[";
            r7.append(r2);
            r2 = r6.mBindArgs;
            r2 = r2.size();
            r3 = 0;
        L_0x0074:
            if (r3 >= r2) goto L_0x00ac;
        L_0x0076:
            r4 = r6.mBindArgs;
            r4 = r4.get(r3);
            if (r3 == 0) goto L_0x0083;
        L_0x007e:
            r5 = ", ";
            r7.append(r5);
        L_0x0083:
            if (r4 != 0) goto L_0x008b;
        L_0x0085:
            r5 = "null";
            r7.append(r5);
            goto L_0x00a9;
        L_0x008b:
            r5 = r4 instanceof byte[];
            if (r5 == 0) goto L_0x0095;
        L_0x008f:
            r5 = "<byte[]>";
            r7.append(r5);
            goto L_0x00a9;
        L_0x0095:
            r5 = r4 instanceof java.lang.String;
            if (r5 == 0) goto L_0x00a6;
        L_0x0099:
            r7.append(r1);
            r5 = r4;
            r5 = (java.lang.String) r5;
            r7.append(r5);
            r7.append(r1);
            goto L_0x00a9;
        L_0x00a6:
            r7.append(r4);
        L_0x00a9:
            r3 = r3 + 1;
            goto L_0x0074;
        L_0x00ac:
            r3 = "]";
            r7.append(r3);
        L_0x00b1:
            r2 = ", path=";
            r7.append(r2);
            r2 = r6.mPath;
            r7.append(r2);
            r2 = r6.mException;
            if (r2 == 0) goto L_0x00d0;
        L_0x00bf:
            r2 = ", exception=\"";
            r7.append(r2);
            r2 = r6.mException;
            r2 = r2.getMessage();
            r7.append(r2);
            r7.append(r1);
        L_0x00d0:
            r2 = r6.mResultLong;
            r4 = -9223372036854775808;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 == 0) goto L_0x00e2;
        L_0x00d8:
            r2 = ", result=";
            r7.append(r2);
            r2 = r6.mResultLong;
            r7.append(r2);
        L_0x00e2:
            r2 = r6.mResultString;
            if (r2 == 0) goto L_0x00f3;
        L_0x00e6:
            r2 = ", result=\"";
            r7.append(r2);
            r2 = r6.mResultString;
            r7.append(r2);
            r7.append(r1);
        L_0x00f3:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnection$Operation.describe(java.lang.StringBuilder, boolean):void");
        }

        private String getStatus() {
            if (!this.mFinished) {
                return "running";
            }
            return this.mException != null ? "failed" : "succeeded";
        }

        private String getTraceMethodName() {
            String methodName = new StringBuilder();
            methodName.append(this.mKind);
            methodName.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            methodName.append(this.mSql);
            methodName = methodName.toString();
            if (methodName.length() > 256) {
                return methodName.substring(0, 256);
            }
            return methodName;
        }
    }

    private static final class OperationLog {
        private static final int COOKIE_GENERATION_SHIFT = 8;
        private static final int COOKIE_INDEX_MASK = 255;
        private static final int MAX_RECENT_OPERATIONS = 20;
        private int mGeneration;
        private int mIndex;
        private final Operation[] mOperations = new Operation[20];
        private final SQLiteConnectionPool mPool;
        private long mResultLong = Long.MIN_VALUE;
        private String mResultString;

        OperationLog(SQLiteConnectionPool pool) {
            this.mPool = pool;
        }

        public int beginOperation(String kind, String sql, Object[] bindArgs) {
            Operation operation;
            this.mResultLong = Long.MIN_VALUE;
            this.mResultString = null;
            synchronized (this.mOperations) {
                int index = (this.mIndex + 1) % 20;
                operation = this.mOperations[index];
                if (operation == null) {
                    operation = new Operation();
                    this.mOperations[index] = operation;
                } else {
                    operation.mFinished = false;
                    operation.mException = null;
                    if (operation.mBindArgs != null) {
                        operation.mBindArgs.clear();
                    }
                }
                operation.mStartWallTime = System.currentTimeMillis();
                operation.mStartTime = SystemClock.uptimeMillis();
                operation.mKind = kind;
                operation.mSql = sql;
                operation.mPath = this.mPool.getPath();
                operation.mResultLong = Long.MIN_VALUE;
                operation.mResultString = null;
                if (bindArgs != null) {
                    if (operation.mBindArgs == null) {
                        operation.mBindArgs = new ArrayList();
                    } else {
                        operation.mBindArgs.clear();
                    }
                    for (Object arg : bindArgs) {
                        if (arg == null || !(arg instanceof byte[])) {
                            operation.mBindArgs.add(arg);
                        } else {
                            operation.mBindArgs.add(SQLiteConnection.EMPTY_BYTE_ARRAY);
                        }
                    }
                }
                operation.mCookie = newOperationCookieLocked(index);
                if (Trace.isTagEnabled(1048576)) {
                    Trace.asyncTraceBegin(1048576, operation.getTraceMethodName(), operation.mCookie);
                }
                this.mIndex = index;
            }
            return operation.mCookie;
        }

        public void failOperation(int cookie, Exception ex) {
            synchronized (this.mOperations) {
                Operation operation = getOperationLocked(cookie);
                if (operation != null) {
                    operation.mException = ex;
                }
            }
        }

        public void endOperation(int cookie) {
            synchronized (this.mOperations) {
                if (endOperationDeferLogLocked(cookie)) {
                    logOperationLocked(cookie, null);
                }
            }
        }

        public boolean endOperationDeferLog(int cookie) {
            boolean endOperationDeferLogLocked;
            synchronized (this.mOperations) {
                endOperationDeferLogLocked = endOperationDeferLogLocked(cookie);
            }
            return endOperationDeferLogLocked;
        }

        public void logOperation(int cookie, String detail) {
            synchronized (this.mOperations) {
                logOperationLocked(cookie, detail);
            }
        }

        public void setResult(long longResult) {
            this.mResultLong = longResult;
        }

        public void setResult(String stringResult) {
            this.mResultString = stringResult;
        }

        private boolean endOperationDeferLogLocked(int cookie) {
            Operation operation = getOperationLocked(cookie);
            boolean z = false;
            if (operation == null) {
                return false;
            }
            if (Trace.isTagEnabled(1048576)) {
                Trace.asyncTraceEnd(1048576, operation.getTraceMethodName(), operation.mCookie);
            }
            operation.mEndTime = SystemClock.uptimeMillis();
            operation.mFinished = true;
            long execTime = operation.mEndTime - operation.mStartTime;
            this.mPool.onStatementExecuted(execTime);
            if (NoPreloadHolder.DEBUG_LOG_SLOW_QUERIES && SQLiteDebug.shouldLogSlowQuery(execTime)) {
                z = true;
            }
            return z;
        }

        private void logOperationLocked(int cookie, String detail) {
            Operation operation = getOperationLocked(cookie);
            operation.mResultLong = this.mResultLong;
            operation.mResultString = this.mResultString;
            StringBuilder msg = new StringBuilder();
            operation.describe(msg, true);
            if (detail != null) {
                msg.append(", ");
                msg.append(detail);
            }
            Log.d(SQLiteConnection.TAG, msg.toString());
        }

        private int newOperationCookieLocked(int index) {
            int generation = this.mGeneration;
            this.mGeneration = generation + 1;
            return (generation << 8) | index;
        }

        private Operation getOperationLocked(int cookie) {
            Operation operation = this.mOperations[cookie & 255];
            return operation.mCookie == cookie ? operation : null;
        }

        public String describeCurrentOperation() {
            synchronized (this.mOperations) {
                Operation operation = this.mOperations[this.mIndex];
                if (operation == null || operation.mFinished) {
                    return null;
                }
                StringBuilder msg = new StringBuilder();
                operation.describe(msg, false);
                String stringBuilder = msg.toString();
                return stringBuilder;
            }
        }

        public void dump(Printer printer) {
            synchronized (this.mOperations) {
                printer.println("  Most recently executed operations:");
                int index = this.mIndex;
                Operation operation = this.mOperations[index];
                if (operation != null) {
                    SimpleDateFormat opDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    int n = 0;
                    do {
                        StringBuilder msg = new StringBuilder();
                        msg.append("    ");
                        msg.append(n);
                        msg.append(": [");
                        msg.append(opDF.format(new Date(operation.mStartWallTime)));
                        msg.append("] ");
                        operation.describe(msg, false);
                        printer.println(msg.toString());
                        if (index > 0) {
                            index--;
                        } else {
                            index = 19;
                        }
                        n++;
                        operation = this.mOperations[index];
                        if (operation == null) {
                            break;
                        }
                    } while (n < 20);
                } else {
                    printer.println("    <none>");
                }
            }
        }
    }

    private static final class PreparedStatement {
        public boolean mInCache;
        public boolean mInUse;
        public int mNumParameters;
        public PreparedStatement mPoolNext;
        public boolean mReadOnly;
        public String mSql;
        public long mStatementPtr;
        public int mType;

        private PreparedStatement() {
        }
    }

    private final class PreparedStatementCache extends LruCache<String, PreparedStatement> {
        public PreparedStatementCache(int size) {
            super(size);
        }

        /* Access modifiers changed, original: protected */
        public void entryRemoved(boolean evicted, String key, PreparedStatement oldValue, PreparedStatement newValue) {
            oldValue.mInCache = false;
            if (!oldValue.mInUse) {
                SQLiteConnection.this.finalizePreparedStatement(oldValue);
            }
        }

        public void dump(Printer printer) {
            printer.println("  Prepared statement cache:");
            Map<String, PreparedStatement> cache = snapshot();
            if (cache.isEmpty()) {
                printer.println("    <none>");
                return;
            }
            int i = 0;
            for (Entry<String, PreparedStatement> entry : cache.entrySet()) {
                PreparedStatement statement = (PreparedStatement) entry.getValue();
                if (statement.mInCache) {
                    String sql = (String) entry.getKey();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("    ");
                    stringBuilder.append(i);
                    stringBuilder.append(": statementPtr=0x");
                    stringBuilder.append(Long.toHexString(statement.mStatementPtr));
                    stringBuilder.append(", numParameters=");
                    stringBuilder.append(statement.mNumParameters);
                    stringBuilder.append(", type=");
                    stringBuilder.append(statement.mType);
                    stringBuilder.append(", readOnly=");
                    stringBuilder.append(statement.mReadOnly);
                    stringBuilder.append(", sql=\"");
                    stringBuilder.append(SQLiteConnection.trimSqlForDisplay(sql));
                    stringBuilder.append("\"");
                    printer.println(stringBuilder.toString());
                }
                i++;
            }
        }
    }

    private static native void nativeBindBlob(long j, long j2, int i, byte[] bArr);

    private static native void nativeBindDouble(long j, long j2, int i, double d);

    private static native void nativeBindLong(long j, long j2, int i, long j3);

    private static native void nativeBindNull(long j, long j2, int i);

    private static native void nativeBindString(long j, long j2, int i, String str);

    private static native void nativeCancel(long j);

    private static native void nativeClose(long j);

    private static native void nativeExecute(long j, long j2);

    private static native int nativeExecuteForBlobFileDescriptor(long j, long j2);

    private static native int nativeExecuteForChangedRowCount(long j, long j2);

    private static native long nativeExecuteForCursorWindow(long j, long j2, long j3, int i, int i2, boolean z);

    private static native long nativeExecuteForLastInsertedRowId(long j, long j2);

    private static native long nativeExecuteForLong(long j, long j2);

    private static native String nativeExecuteForString(long j, long j2);

    private static native void nativeFinalizeStatement(long j, long j2);

    private static native int nativeGetColumnCount(long j, long j2);

    private static native String nativeGetColumnName(long j, long j2, int i);

    private static native int nativeGetDbLookaside(long j);

    private static native int nativeGetParameterCount(long j, long j2);

    private static native boolean nativeIsReadOnly(long j, long j2);

    private static native long nativeOpen(String str, int i, String str2, boolean z, boolean z2, int i2, int i3);

    private static native long nativePrepareStatement(long j, String str);

    private static native void nativeRegisterCustomFunction(long j, SQLiteCustomFunction sQLiteCustomFunction);

    private static native void nativeRegisterLocalizedCollators(long j, String str);

    private static native void nativeResetCancel(long j, boolean z);

    private static native void nativeResetStatementAndClearBindings(long j, long j2);

    private SQLiteConnection(SQLiteConnectionPool pool, SQLiteDatabaseConfiguration configuration, int connectionId, boolean primaryConnection) {
        this.mPool = pool;
        this.mRecentOperations = new OperationLog(this.mPool);
        this.mConfiguration = new SQLiteDatabaseConfiguration(configuration);
        this.mConnectionId = connectionId;
        this.mIsPrimaryConnection = primaryConnection;
        boolean z = true;
        if ((configuration.openFlags & 1) == 0) {
            z = false;
        }
        this.mIsReadOnlyConnection = z;
        this.mPreparedStatementCache = new PreparedStatementCache(this.mConfiguration.maxSqlCacheSize);
        this.mCloseGuard.open("close");
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (!(this.mPool == null || this.mConnectionPtr == 0)) {
                this.mPool.onConnectionLeaked();
            }
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    static SQLiteConnection open(SQLiteConnectionPool pool, SQLiteDatabaseConfiguration configuration, int connectionId, boolean primaryConnection) {
        SQLiteConnection connection = new SQLiteConnection(pool, configuration, connectionId, primaryConnection);
        try {
            connection.open();
            return connection;
        } catch (SQLiteException ex) {
            connection.dispose(false);
            throw ex;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        dispose(false);
    }

    private void open() {
        int cookie = this.mRecentOperations.beginOperation("open", null, null);
        try {
            this.mConnectionPtr = nativeOpen(this.mConfiguration.path, this.mConfiguration.openFlags, this.mConfiguration.label, NoPreloadHolder.DEBUG_SQL_STATEMENTS, NoPreloadHolder.DEBUG_SQL_TIME, this.mConfiguration.lookasideSlotSize, this.mConfiguration.lookasideSlotCount);
            setPageSize();
            setForeignKeyModeFromConfiguration();
            setWalModeFromConfiguration();
            setJournalSizeLimit();
            setAutoCheckpointInterval();
            setLocaleFromConfiguration();
            int functionCount = this.mConfiguration.customFunctions.size();
            for (int i = 0; i < functionCount; i++) {
                nativeRegisterCustomFunction(this.mConnectionPtr, (SQLiteCustomFunction) this.mConfiguration.customFunctions.get(i));
            }
        } finally {
            this.mRecentOperations.endOperation(cookie);
        }
    }

    private void dispose(boolean finalized) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (finalized) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        if (this.mConnectionPtr != 0) {
            int cookie = this.mRecentOperations.beginOperation("close", null, null);
            try {
                this.mPreparedStatementCache.evictAll();
                nativeClose(this.mConnectionPtr);
                this.mConnectionPtr = 0;
            } finally {
                this.mRecentOperations.endOperation(cookie);
            }
        }
    }

    private void setPageSize() {
        if (!this.mConfiguration.isInMemoryDb() && !this.mIsReadOnlyConnection) {
            long newValue = (long) SQLiteGlobal.getDefaultPageSize();
            if (executeForLong("PRAGMA page_size", null, null) != newValue) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PRAGMA page_size=");
                stringBuilder.append(newValue);
                execute(stringBuilder.toString(), null, null);
            }
        }
    }

    private void setAutoCheckpointInterval() {
        if (!this.mConfiguration.isInMemoryDb() && !this.mIsReadOnlyConnection) {
            long newValue = (long) SQLiteGlobal.getWALAutoCheckpoint();
            if (executeForLong("PRAGMA wal_autocheckpoint", null, null) != newValue) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PRAGMA wal_autocheckpoint=");
                stringBuilder.append(newValue);
                executeForLong(stringBuilder.toString(), null, null);
            }
        }
    }

    private void setJournalSizeLimit() {
        if (!this.mConfiguration.isInMemoryDb() && !this.mIsReadOnlyConnection) {
            long newValue = (long) SQLiteGlobal.getJournalSizeLimit();
            if (executeForLong("PRAGMA journal_size_limit", null, null) != newValue) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PRAGMA journal_size_limit=");
                stringBuilder.append(newValue);
                executeForLong(stringBuilder.toString(), null, null);
            }
        }
    }

    private void setForeignKeyModeFromConfiguration() {
        if (!this.mIsReadOnlyConnection) {
            long newValue = this.mConfiguration.foreignKeyConstraintsEnabled ? 1 : 0;
            if (executeForLong("PRAGMA foreign_keys", null, null) != newValue) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PRAGMA foreign_keys=");
                stringBuilder.append(newValue);
                execute(stringBuilder.toString(), null, null);
            }
        }
    }

    private void setWalModeFromConfiguration() {
        if (!this.mConfiguration.isInMemoryDb() && !this.mIsReadOnlyConnection) {
            boolean walEnabled = (this.mConfiguration.openFlags & 536870912) != 0;
            boolean isCompatibilityWalEnabled = this.mConfiguration.isLegacyCompatibilityWalEnabled();
            if (walEnabled || isCompatibilityWalEnabled) {
                setJournalMode("WAL");
                if (this.mConfiguration.syncMode != null) {
                    setSyncMode(this.mConfiguration.syncMode);
                } else if (isCompatibilityWalEnabled) {
                    setSyncMode(SQLiteCompatibilityWalFlags.getWALSyncMode());
                } else {
                    setSyncMode(SQLiteGlobal.getWALSyncMode());
                }
                maybeTruncateWalFile();
                return;
            }
            setJournalMode(this.mConfiguration.journalMode == null ? SQLiteGlobal.getDefaultJournalMode() : this.mConfiguration.journalMode);
            setSyncMode(this.mConfiguration.syncMode == null ? SQLiteGlobal.getDefaultSyncMode() : this.mConfiguration.syncMode);
        }
    }

    private void maybeTruncateWalFile() {
        long threshold = SQLiteGlobal.getWALTruncateSize();
        if (threshold != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mConfiguration.path);
            stringBuilder.append("-wal");
            File walFile = new File(stringBuilder.toString());
            if (walFile.isFile()) {
                long size = walFile.length();
                if (size >= threshold) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(walFile.getAbsolutePath());
                    stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder2.append(size);
                    stringBuilder2.append(" bytes: Bigger than ");
                    stringBuilder2.append(threshold);
                    stringBuilder2.append("; truncating");
                    String stringBuilder3 = stringBuilder2.toString();
                    String str = TAG;
                    Log.i(str, stringBuilder3);
                    try {
                        executeForString("PRAGMA wal_checkpoint(TRUNCATE)", null, null);
                    } catch (SQLiteException e) {
                        Log.w(str, "Failed to truncate the -wal file", e);
                    }
                }
            }
        }
    }

    private void setSyncMode(String newValue) {
        if (!canonicalizeSyncMode(executeForString("PRAGMA synchronous", null, null)).equalsIgnoreCase(canonicalizeSyncMode(newValue))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PRAGMA synchronous=");
            stringBuilder.append(newValue);
            execute(stringBuilder.toString(), null, null);
        }
    }

    private static java.lang.String canonicalizeSyncMode(java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        r1 = 2;
        r2 = 1;
        switch(r0) {
            case 48: goto L_0x001e;
            case 49: goto L_0x0014;
            case 50: goto L_0x000a;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x0028;
    L_0x000a:
        r0 = "2";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0012:
        r0 = r1;
        goto L_0x0029;
    L_0x0014:
        r0 = "1";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x001c:
        r0 = r2;
        goto L_0x0029;
    L_0x001e:
        r0 = "0";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0026:
        r0 = 0;
        goto L_0x0029;
    L_0x0028:
        r0 = -1;
    L_0x0029:
        if (r0 == 0) goto L_0x0036;
    L_0x002b:
        if (r0 == r2) goto L_0x0033;
    L_0x002d:
        if (r0 == r1) goto L_0x0030;
    L_0x002f:
        return r3;
    L_0x0030:
        r0 = "FULL";
        return r0;
    L_0x0033:
        r0 = "NORMAL";
        return r0;
    L_0x0036:
        r0 = "OFF";
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnection.canonicalizeSyncMode(java.lang.String):java.lang.String");
    }

    private void setJournalMode(String newValue) {
        String value = executeForString("PRAGMA journal_mode", null, null);
        if (!value.equalsIgnoreCase(newValue)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PRAGMA journal_mode=");
                stringBuilder.append(newValue);
                if (executeForString(stringBuilder.toString(), null, null).equalsIgnoreCase(newValue)) {
                    return;
                }
            } catch (SQLiteDatabaseLockedException e) {
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Could not change the database journal mode of '");
            stringBuilder2.append(this.mConfiguration.label);
            stringBuilder2.append("' from '");
            stringBuilder2.append(value);
            stringBuilder2.append("' to '");
            stringBuilder2.append(newValue);
            stringBuilder2.append("' because the database is locked.  This usually means that there are other open connections to the database which prevents the database from enabling or disabling write-ahead logging mode.  Proceeding without changing the journal mode.");
            Log.w(TAG, stringBuilder2.toString());
        }
    }

    private void setLocaleFromConfiguration() {
        String str = "COMMIT";
        String str2 = "ROLLBACK";
        if ((this.mConfiguration.openFlags & 16) == 0) {
            String newLocale = this.mConfiguration.locale.toString();
            nativeRegisterLocalizedCollators(this.mConnectionPtr, newLocale);
            if (!this.mConfiguration.isInMemoryDb()) {
                checkDatabaseWiped();
            }
            if (!this.mIsReadOnlyConnection) {
                try {
                    execute("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)", null, null);
                    String oldLocale = executeForString("SELECT locale FROM android_metadata UNION SELECT NULL ORDER BY locale DESC LIMIT 1", null, null);
                    if (oldLocale == null || !oldLocale.equals(newLocale)) {
                        execute("BEGIN", null, null);
                        execute("DELETE FROM android_metadata", null, null);
                        execute("INSERT INTO android_metadata (locale) VALUES(?)", new Object[]{newLocale}, null);
                        execute("REINDEX LOCALIZED", null, null);
                        if (!true) {
                            str = str2;
                        }
                        execute(str, null, null);
                    }
                } catch (SQLiteException ex) {
                    throw ex;
                } catch (RuntimeException ex2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to change locale for db '");
                    stringBuilder.append(this.mConfiguration.label);
                    stringBuilder.append("' to '");
                    stringBuilder.append(newLocale);
                    stringBuilder.append("'.");
                    throw new SQLiteException(stringBuilder.toString(), ex2);
                } catch (Throwable th) {
                    if (!false) {
                        str = str2;
                    }
                    execute(str, null, null);
                }
            }
        }
    }

    private void checkDatabaseWiped() {
        if (SQLiteGlobal.checkDbWipe()) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mConfiguration.path);
                stringBuilder.append("-wipecheck");
                File checkFile = new File(stringBuilder.toString());
                boolean hasMetadataTable = executeForLong("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='android_metadata'", null, null) > 0;
                boolean hasCheckFile = checkFile.exists();
                if (!(this.mIsReadOnlyConnection || hasCheckFile)) {
                    checkFile.createNewFile();
                }
                if (!hasMetadataTable && hasCheckFile) {
                    SQLiteDatabase.wipeDetected(this.mConfiguration.path, "unknown");
                }
            } catch (IOException | RuntimeException ex) {
                SQLiteDatabase.wtfAsSystemServer(TAG, "Unexpected exception while checking for wipe", ex);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void reconfigure(SQLiteDatabaseConfiguration configuration) {
        boolean walModeChanged = false;
        this.mOnlyAllowReadOnlyOperations = false;
        int functionCount = configuration.customFunctions.size();
        for (int i = 0; i < functionCount; i++) {
            SQLiteCustomFunction function = (SQLiteCustomFunction) configuration.customFunctions.get(i);
            if (!this.mConfiguration.customFunctions.contains(function)) {
                nativeRegisterCustomFunction(this.mConnectionPtr, function);
            }
        }
        boolean foreignKeyModeChanged = configuration.foreignKeyConstraintsEnabled != this.mConfiguration.foreignKeyConstraintsEnabled;
        if (((configuration.openFlags ^ this.mConfiguration.openFlags) & KeymasterDefs.KM_ULONG_REP) != 0) {
            walModeChanged = true;
        }
        boolean localeChanged = configuration.locale.equals(this.mConfiguration.locale) ^ true;
        this.mConfiguration.updateParametersFrom(configuration);
        this.mPreparedStatementCache.resize(configuration.maxSqlCacheSize);
        if (foreignKeyModeChanged) {
            setForeignKeyModeFromConfiguration();
        }
        if (walModeChanged) {
            setWalModeFromConfiguration();
        }
        if (localeChanged) {
            setLocaleFromConfiguration();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setOnlyAllowReadOnlyOperations(boolean readOnly) {
        this.mOnlyAllowReadOnlyOperations = readOnly;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPreparedStatementInCache(String sql) {
        return this.mPreparedStatementCache.get(sql) != null;
    }

    public int getConnectionId() {
        return this.mConnectionId;
    }

    public boolean isPrimaryConnection() {
        return this.mIsPrimaryConnection;
    }

    public void prepare(String sql, SQLiteStatementInfo outStatementInfo) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("prepare", sql, null);
            PreparedStatement statement;
            try {
                statement = acquirePreparedStatement(sql);
                if (outStatementInfo != null) {
                    outStatementInfo.numParameters = statement.mNumParameters;
                    outStatementInfo.readOnly = statement.mReadOnly;
                    int columnCount = nativeGetColumnCount(this.mConnectionPtr, statement.mStatementPtr);
                    if (columnCount == 0) {
                        outStatementInfo.columnNames = EMPTY_STRING_ARRAY;
                    } else {
                        outStatementInfo.columnNames = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            outStatementInfo.columnNames[i] = nativeGetColumnName(this.mConnectionPtr, statement.mStatementPtr, i);
                        }
                    }
                }
                releasePreparedStatement(statement);
                this.mRecentOperations.endOperation(cookie);
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th) {
                    this.mRecentOperations.endOperation(cookie);
                }
            } catch (Throwable th2) {
                releasePreparedStatement(statement);
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public void execute(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("execute", sql, bindArgs);
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    nativeExecute(this.mConnectionPtr, statement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    this.mRecentOperations.endOperation(cookie);
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    this.mRecentOperations.endOperation(cookie);
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public long executeForLong(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("executeForLong", sql, bindArgs);
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    long ret = nativeExecuteForLong(this.mConnectionPtr, statement.mStatementPtr);
                    this.mRecentOperations.setResult(ret);
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    this.mRecentOperations.endOperation(cookie);
                    return ret;
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    this.mRecentOperations.endOperation(cookie);
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public String executeForString(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("executeForString", sql, bindArgs);
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    String ret = nativeExecuteForString(this.mConnectionPtr, statement.mStatementPtr);
                    this.mRecentOperations.setResult(ret);
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    this.mRecentOperations.endOperation(cookie);
                    return ret;
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    this.mRecentOperations.endOperation(cookie);
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public ParcelFileDescriptor executeForBlobFileDescriptor(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("executeForBlobFileDescriptor", sql, bindArgs);
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    int fd = nativeExecuteForBlobFileDescriptor(this.mConnectionPtr, statement.mStatementPtr);
                    ParcelFileDescriptor adoptFd = fd >= 0 ? ParcelFileDescriptor.adoptFd(fd) : null;
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    this.mRecentOperations.endOperation(cookie);
                    return adoptFd;
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    this.mRecentOperations.endOperation(cookie);
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public int executeForChangedRowCount(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        String str = "changedRows=";
        if (sql != null) {
            int changedRows = 0;
            int cookie = this.mRecentOperations.beginOperation("executeForChangedRowCount", sql, bindArgs);
            OperationLog operationLog;
            StringBuilder stringBuilder;
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    changedRows = nativeExecuteForChangedRowCount(this.mConnectionPtr, statement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    if (this.mRecentOperations.endOperationDeferLog(cookie)) {
                        operationLog = this.mRecentOperations;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(changedRows);
                        operationLog.logOperation(cookie, stringBuilder.toString());
                    }
                    return changedRows;
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    if (this.mRecentOperations.endOperationDeferLog(cookie)) {
                        operationLog = this.mRecentOperations;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(changedRows);
                        operationLog.logOperation(cookie, stringBuilder.toString());
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    public long executeForLastInsertedRowId(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        if (sql != null) {
            int cookie = this.mRecentOperations.beginOperation("executeForLastInsertedRowId", sql, bindArgs);
            try {
                PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    long nativeExecuteForLastInsertedRowId = nativeExecuteForLastInsertedRowId(this.mConnectionPtr, statement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                    releasePreparedStatement(statement);
                    this.mRecentOperations.endOperation(cookie);
                    return nativeExecuteForLastInsertedRowId;
                } catch (Throwable th) {
                    releasePreparedStatement(statement);
                }
            } catch (RuntimeException ex) {
                try {
                    this.mRecentOperations.failOperation(cookie, ex);
                    throw ex;
                } catch (Throwable th2) {
                    this.mRecentOperations.endOperation(cookie);
                }
            }
        } else {
            throw new IllegalArgumentException("sql must not be null.");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:81:0x0176 A:{Catch:{ all -> 0x01a4 }} */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0176 A:{Catch:{ all -> 0x01a4 }} */
    public int executeForCursorWindow(java.lang.String r26, java.lang.Object[] r27, android.database.CursorWindow r28, int r29, int r30, boolean r31, android.os.CancellationSignal r32) {
        /*
        r25 = this;
        r1 = r25;
        r2 = r26;
        r3 = r27;
        r4 = r28;
        r14 = r29;
        r15 = r32;
        r13 = ", countedRows=";
        r12 = ", filledRows=";
        r11 = ", actualPos=";
        r9 = "', startPos=";
        r10 = "window='";
        if (r2 == 0) goto L_0x01b6;
    L_0x0019:
        if (r4 == 0) goto L_0x01ac;
    L_0x001b:
        r28.acquireReference();
        r16 = -1;
        r17 = -1;
        r18 = -1;
        r0 = r1.mRecentOperations;	 Catch:{ all -> 0x01a6 }
        r5 = "executeForCursorWindow";
        r0 = r0.beginOperation(r5, r2, r3);	 Catch:{ all -> 0x01a6 }
        r7 = r0;
        r0 = r25.acquirePreparedStatement(r26);	 Catch:{ RuntimeException -> 0x0158, all -> 0x0149 }
        r8 = r0;
        r1.throwIfStatementForbidden(r8);	 Catch:{ all -> 0x013a }
        r1.bindArguments(r8, r3);	 Catch:{ all -> 0x013a }
        r1.applyBlockGuardPolicy(r8);	 Catch:{ all -> 0x013a }
        r1.attachCancellationSignal(r15);	 Catch:{ all -> 0x013a }
        r5 = r1.mConnectionPtr;	 Catch:{ all -> 0x012b }
        r2 = r8.mStatementPtr;	 Catch:{ all -> 0x012b }
        r19 = r9;
        r20 = r10;
        r9 = r4.mWindowPtr;	 Catch:{ all -> 0x0122 }
        r21 = r7;
        r14 = r8;
        r7 = r2;
        r3 = r19;
        r2 = r20;
        r22 = r11;
        r11 = r29;
        r23 = r12;
        r12 = r30;
        r24 = r13;
        r13 = r31;
        r5 = nativeExecuteForCursorWindow(r5, r7, r9, r11, r12, r13);	 Catch:{ all -> 0x0116 }
        r0 = 32;
        r7 = r5 >> r0;
        r7 = (int) r7;
        r8 = (int) r5;
        r0 = r28.getNumRows();	 Catch:{ all -> 0x0106 }
        r9 = r0;
        r4.setStartPosition(r7);	 Catch:{ all -> 0x00f4 }
        r1.detachCancellationSignal(r15);	 Catch:{ all -> 0x00e1 }
        r1.releasePreparedStatement(r14);	 Catch:{ RuntimeException -> 0x00ce, all -> 0x00c1 }
        r0 = r1.mRecentOperations;	 Catch:{ all -> 0x00bc }
        r10 = r21;
        r0 = r0.endOperationDeferLog(r10);	 Catch:{ all -> 0x00bc }
        if (r0 == 0) goto L_0x00b5;
    L_0x007f:
        r0 = r1.mRecentOperations;	 Catch:{ all -> 0x00bc }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00bc }
        r11.<init>();	 Catch:{ all -> 0x00bc }
        r11.append(r2);	 Catch:{ all -> 0x00bc }
        r11.append(r4);	 Catch:{ all -> 0x00bc }
        r11.append(r3);	 Catch:{ all -> 0x00bc }
        r12 = r10;
        r10 = r29;
        r11.append(r10);	 Catch:{ all -> 0x01a4 }
        r13 = r22;
        r11.append(r13);	 Catch:{ all -> 0x01a4 }
        r11.append(r7);	 Catch:{ all -> 0x01a4 }
        r2 = r23;
        r11.append(r2);	 Catch:{ all -> 0x01a4 }
        r11.append(r9);	 Catch:{ all -> 0x01a4 }
        r2 = r24;
        r11.append(r2);	 Catch:{ all -> 0x01a4 }
        r11.append(r8);	 Catch:{ all -> 0x01a4 }
        r2 = r11.toString();	 Catch:{ all -> 0x01a4 }
        r0.logOperation(r12, r2);	 Catch:{ all -> 0x01a4 }
        goto L_0x00b8;
    L_0x00b5:
        r12 = r10;
        r10 = r29;
    L_0x00b8:
        r28.releaseReference();
        return r8;
    L_0x00bc:
        r0 = move-exception;
        r10 = r29;
        goto L_0x01a8;
    L_0x00c1:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        goto L_0x016e;
    L_0x00ce:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        r16 = r7;
        r17 = r8;
        r18 = r9;
        goto L_0x0160;
    L_0x00e1:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        r16 = r7;
        r17 = r8;
        r18 = r9;
        goto L_0x0143;
    L_0x00f4:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        r16 = r7;
        r17 = r8;
        r18 = r9;
        goto L_0x0134;
    L_0x0106:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        r16 = r7;
        r17 = r8;
        goto L_0x0134;
    L_0x0116:
        r0 = move-exception;
        r10 = r29;
        r12 = r21;
        r13 = r22;
        r5 = r23;
        r6 = r24;
        goto L_0x0134;
    L_0x0122:
        r0 = move-exception;
        r5 = r12;
        r6 = r13;
        r10 = r14;
        r3 = r19;
        r2 = r20;
        goto L_0x0131;
    L_0x012b:
        r0 = move-exception;
        r3 = r9;
        r2 = r10;
        r5 = r12;
        r6 = r13;
        r10 = r14;
    L_0x0131:
        r12 = r7;
        r14 = r8;
        r13 = r11;
    L_0x0134:
        r1.detachCancellationSignal(r15);	 Catch:{ all -> 0x0138 }
        throw r0;	 Catch:{ all -> 0x0138 }
    L_0x0138:
        r0 = move-exception;
        goto L_0x0143;
    L_0x013a:
        r0 = move-exception;
        r3 = r9;
        r2 = r10;
        r5 = r12;
        r6 = r13;
        r10 = r14;
        r12 = r7;
        r14 = r8;
        r13 = r11;
    L_0x0143:
        r1.releasePreparedStatement(r14);	 Catch:{ RuntimeException -> 0x0147 }
        throw r0;	 Catch:{ RuntimeException -> 0x0147 }
    L_0x0147:
        r0 = move-exception;
        goto L_0x0160;
    L_0x0149:
        r0 = move-exception;
        r3 = r9;
        r2 = r10;
        r5 = r12;
        r6 = r13;
        r10 = r14;
        r12 = r7;
        r13 = r11;
        r7 = r16;
        r8 = r17;
        r9 = r18;
        goto L_0x016e;
    L_0x0158:
        r0 = move-exception;
        r3 = r9;
        r2 = r10;
        r5 = r12;
        r6 = r13;
        r10 = r14;
        r12 = r7;
        r13 = r11;
    L_0x0160:
        r7 = r1.mRecentOperations;	 Catch:{ all -> 0x0167 }
        r7.failOperation(r12, r0);	 Catch:{ all -> 0x0167 }
        throw r0;	 Catch:{ all -> 0x0167 }
    L_0x0167:
        r0 = move-exception;
        r7 = r16;
        r8 = r17;
        r9 = r18;
    L_0x016e:
        r11 = r1.mRecentOperations;	 Catch:{ all -> 0x01a4 }
        r11 = r11.endOperationDeferLog(r12);	 Catch:{ all -> 0x01a4 }
        if (r11 == 0) goto L_0x01a2;
    L_0x0176:
        r11 = r1.mRecentOperations;	 Catch:{ all -> 0x01a4 }
        r14 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01a4 }
        r14.<init>();	 Catch:{ all -> 0x01a4 }
        r14.append(r2);	 Catch:{ all -> 0x01a4 }
        r14.append(r4);	 Catch:{ all -> 0x01a4 }
        r14.append(r3);	 Catch:{ all -> 0x01a4 }
        r14.append(r10);	 Catch:{ all -> 0x01a4 }
        r14.append(r13);	 Catch:{ all -> 0x01a4 }
        r14.append(r7);	 Catch:{ all -> 0x01a4 }
        r14.append(r5);	 Catch:{ all -> 0x01a4 }
        r14.append(r9);	 Catch:{ all -> 0x01a4 }
        r14.append(r6);	 Catch:{ all -> 0x01a4 }
        r14.append(r8);	 Catch:{ all -> 0x01a4 }
        r2 = r14.toString();	 Catch:{ all -> 0x01a4 }
        r11.logOperation(r12, r2);	 Catch:{ all -> 0x01a4 }
        throw r0;	 Catch:{ all -> 0x01a4 }
    L_0x01a4:
        r0 = move-exception;
        goto L_0x01a8;
    L_0x01a6:
        r0 = move-exception;
        r10 = r14;
    L_0x01a8:
        r28.releaseReference();
        throw r0;
    L_0x01ac:
        r10 = r14;
        r0 = new java.lang.IllegalArgumentException;
        r2 = "window must not be null.";
        r0.<init>(r2);
        throw r0;
    L_0x01b6:
        r10 = r14;
        r0 = new java.lang.IllegalArgumentException;
        r2 = "sql must not be null.";
        r0.<init>(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnection.executeForCursorWindow(java.lang.String, java.lang.Object[], android.database.CursorWindow, int, int, boolean, android.os.CancellationSignal):int");
    }

    private PreparedStatement acquirePreparedStatement(String sql) {
        PreparedStatement statement = (PreparedStatement) this.mPreparedStatementCache.get(sql);
        boolean skipCache = false;
        if (statement != null) {
            if (!statement.mInUse) {
                return statement;
            }
            skipCache = true;
        }
        long statementPtr = nativePrepareStatement(this.mConnectionPtr, sql);
        try {
            int numParameters = nativeGetParameterCount(this.mConnectionPtr, statementPtr);
            int type = DatabaseUtils.getSqlStatementType(sql);
            statement = obtainPreparedStatement(sql, statementPtr, numParameters, type, nativeIsReadOnly(this.mConnectionPtr, statementPtr));
            if (!skipCache && isCacheable(type)) {
                this.mPreparedStatementCache.put(sql, statement);
                statement.mInCache = true;
            }
            statement.mInUse = true;
            return statement;
        } catch (RuntimeException ex) {
            if (statement == null || !statement.mInCache) {
                nativeFinalizeStatement(this.mConnectionPtr, statementPtr);
            }
            throw ex;
        }
    }

    private void releasePreparedStatement(PreparedStatement statement) {
        statement.mInUse = false;
        if (statement.mInCache) {
            try {
                nativeResetStatementAndClearBindings(this.mConnectionPtr, statement.mStatementPtr);
                return;
            } catch (SQLiteException e) {
                this.mPreparedStatementCache.remove(statement.mSql);
                return;
            }
        }
        finalizePreparedStatement(statement);
    }

    private void finalizePreparedStatement(PreparedStatement statement) {
        nativeFinalizeStatement(this.mConnectionPtr, statement.mStatementPtr);
        recyclePreparedStatement(statement);
    }

    private void attachCancellationSignal(CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
            this.mCancellationSignalAttachCount++;
            if (this.mCancellationSignalAttachCount == 1) {
                nativeResetCancel(this.mConnectionPtr, true);
                cancellationSignal.setOnCancelListener(this);
            }
        }
    }

    private void detachCancellationSignal(CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            this.mCancellationSignalAttachCount--;
            if (this.mCancellationSignalAttachCount == 0) {
                cancellationSignal.setOnCancelListener(null);
                nativeResetCancel(this.mConnectionPtr, false);
            }
        }
    }

    public void onCancel() {
        nativeCancel(this.mConnectionPtr);
    }

    private void bindArguments(PreparedStatement statement, Object[] bindArgs) {
        int count = bindArgs != null ? bindArgs.length : 0;
        if (count != statement.mNumParameters) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected ");
            stringBuilder.append(statement.mNumParameters);
            stringBuilder.append(" bind arguments but ");
            stringBuilder.append(count);
            stringBuilder.append(" were provided.");
            throw new SQLiteBindOrColumnIndexOutOfRangeException(stringBuilder.toString());
        } else if (count != 0) {
            long statementPtr = statement.mStatementPtr;
            for (int i = 0; i < count; i++) {
                Object arg = bindArgs[i];
                int typeOfObject = DatabaseUtils.getTypeOfObject(arg);
                if (typeOfObject == 0) {
                    nativeBindNull(this.mConnectionPtr, statementPtr, i + 1);
                } else if (typeOfObject == 1) {
                    nativeBindLong(this.mConnectionPtr, statementPtr, i + 1, ((Number) arg).longValue());
                } else if (typeOfObject == 2) {
                    nativeBindDouble(this.mConnectionPtr, statementPtr, i + 1, ((Number) arg).doubleValue());
                } else if (typeOfObject == 4) {
                    nativeBindBlob(this.mConnectionPtr, statementPtr, i + 1, (byte[]) arg);
                } else if (arg instanceof Boolean) {
                    nativeBindLong(this.mConnectionPtr, statementPtr, i + 1, ((Boolean) arg).booleanValue() ? 1 : 0);
                } else {
                    nativeBindString(this.mConnectionPtr, statementPtr, i + 1, arg.toString());
                }
            }
        }
    }

    private void throwIfStatementForbidden(PreparedStatement statement) {
        if (this.mOnlyAllowReadOnlyOperations && !statement.mReadOnly) {
            throw new SQLiteException("Cannot execute this statement because it might modify the database but the connection is read-only.");
        }
    }

    private static boolean isCacheable(int statementType) {
        if (statementType == 2 || statementType == 1) {
            return true;
        }
        return false;
    }

    private void applyBlockGuardPolicy(PreparedStatement statement) {
        if (!this.mConfiguration.isInMemoryDb()) {
            if (statement.mReadOnly) {
                BlockGuard.getThreadPolicy().onReadFromDisk();
            } else {
                BlockGuard.getThreadPolicy().onWriteToDisk();
            }
        }
    }

    public void dump(Printer printer, boolean verbose) {
        dumpUnsafe(printer, verbose);
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpUnsafe(Printer printer, boolean verbose) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Connection #");
        stringBuilder.append(this.mConnectionId);
        stringBuilder.append(":");
        printer.println(stringBuilder.toString());
        if (verbose) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("  connectionPtr: 0x");
            stringBuilder.append(Long.toHexString(this.mConnectionPtr));
            printer.println(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("  isPrimaryConnection: ");
        stringBuilder.append(this.mIsPrimaryConnection);
        printer.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("  onlyAllowReadOnlyOperations: ");
        stringBuilder.append(this.mOnlyAllowReadOnlyOperations);
        printer.println(stringBuilder.toString());
        this.mRecentOperations.dump(printer);
        if (verbose) {
            this.mPreparedStatementCache.dump(printer);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public String describeCurrentOperationUnsafe() {
        return this.mRecentOperations.describeCurrentOperation();
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00e6 A:{Splitter:B:15:0x0057, ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:28:0x00e6, code skipped:
            r0 = th;
     */
    public void collectDbStats(java.util.ArrayList<android.database.sqlite.SQLiteDebug.DbStats> r29) {
        /*
        r28 = this;
        r9 = r28;
        r10 = r29;
        r11 = "PRAGMA ";
        r0 = r9.mConnectionPtr;
        r12 = nativeGetDbLookaside(r0);
        r1 = 0;
        r3 = 0;
        r13 = 0;
        r0 = "PRAGMA page_count;";
        r5 = r9.executeForLong(r0, r13, r13);	 Catch:{ SQLiteException -> 0x0023 }
        r1 = r5;
        r0 = "PRAGMA page_size;";
        r5 = r9.executeForLong(r0, r13, r13);	 Catch:{ SQLiteException -> 0x0023 }
        r3 = r5;
        r14 = r1;
        r16 = r3;
        goto L_0x0027;
    L_0x0023:
        r0 = move-exception;
        r14 = r1;
        r16 = r3;
    L_0x0027:
        r1 = r28;
        r2 = r12;
        r3 = r14;
        r5 = r16;
        r0 = r1.getMainDbStatsUnsafe(r2, r3, r5);
        r10.add(r0);
        r0 = new android.database.CursorWindow;
        r1 = "collectDbStats";
        r0.<init>(r1);
        r8 = r0;
        r2 = "PRAGMA database_list;";
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r0 = 0;
        r1 = r28;
        r4 = r8;
        r18 = r8;
        r8 = r0;
        r1.executeForCursorWindow(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x00f1, all -> 0x00ed }
        r1 = 1;
        r0 = r1;
        r2 = r0;
    L_0x004f:
        r0 = r18.getNumRows();	 Catch:{ SQLiteException -> 0x00f1, all -> 0x00ed }
        if (r2 >= r0) goto L_0x00ea;
    L_0x0055:
        r3 = r18;
        r0 = r3.getString(r2, r1);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r4 = r0;
        r0 = 2;
        r0 = r3.getString(r2, r0);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r5 = r0;
        r14 = 0;
        r16 = 0;
        r0 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.<init>();	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.append(r11);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.append(r4);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r6 = ".page_count;";
        r0.append(r6);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0 = r0.toString();	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r6 = r9.executeForLong(r0, r13, r13);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r14 = r6;
        r0 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.<init>();	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.append(r11);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0.append(r4);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r6 = ".page_size;";
        r0.append(r6);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r0 = r0.toString();	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r6 = r9.executeForLong(r0, r13, r13);	 Catch:{ SQLiteException -> 0x009a, all -> 0x00e6 }
        r16 = r6;
        goto L_0x009b;
    L_0x009a:
        r0 = move-exception;
    L_0x009b:
        r0 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r0.<init>();	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6 = "  (attached) ";
        r0.append(r6);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r0.append(r4);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r0 = r0.toString();	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6 = r5.isEmpty();	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        if (r6 != 0) goto L_0x00c7;
    L_0x00b2:
        r6 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6.<init>();	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6.append(r0);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r7 = ": ";
        r6.append(r7);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6.append(r5);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r6 = r6.toString();	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r0 = r6;
    L_0x00c7:
        r6 = new android.database.sqlite.SQLiteDebug$DbStats;	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r18 = r6;
        r19 = r0;
        r20 = r14;
        r22 = r16;
        r18.<init>(r19, r20, r22, r24, r25, r26, r27);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r10.add(r6);	 Catch:{ SQLiteException -> 0x00e8, all -> 0x00e6 }
        r2 = r2 + 1;
        r18 = r3;
        goto L_0x004f;
    L_0x00e6:
        r0 = move-exception;
        goto L_0x00f7;
    L_0x00e8:
        r0 = move-exception;
        goto L_0x00fd;
    L_0x00ea:
        r3 = r18;
        goto L_0x00fd;
    L_0x00ed:
        r0 = move-exception;
        r3 = r18;
        goto L_0x00f7;
    L_0x00f1:
        r0 = move-exception;
        r3 = r18;
        goto L_0x00fd;
    L_0x00f5:
        r0 = move-exception;
        r3 = r8;
    L_0x00f7:
        r3.close();
        throw r0;
    L_0x00fb:
        r0 = move-exception;
        r3 = r8;
    L_0x00fd:
        r3.close();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnection.collectDbStats(java.util.ArrayList):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void collectDbStatsUnsafe(ArrayList<DbStats> dbStatsList) {
        dbStatsList.add(getMainDbStatsUnsafe(0, 0, 0));
    }

    private DbStats getMainDbStatsUnsafe(int lookaside, long pageCount, long pageSize) {
        String label = this.mConfiguration.path;
        if (!this.mIsPrimaryConnection) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(label);
            stringBuilder.append(" (");
            stringBuilder.append(this.mConnectionId);
            stringBuilder.append(")");
            label = stringBuilder.toString();
        }
        return new DbStats(label, pageCount, pageSize, lookaside, this.mPreparedStatementCache.hitCount(), this.mPreparedStatementCache.missCount(), this.mPreparedStatementCache.size());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQLiteConnection: ");
        stringBuilder.append(this.mConfiguration.path);
        stringBuilder.append(" (");
        stringBuilder.append(this.mConnectionId);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private PreparedStatement obtainPreparedStatement(String sql, long statementPtr, int numParameters, int type, boolean readOnly) {
        PreparedStatement statement = this.mPreparedStatementPool;
        if (statement != null) {
            this.mPreparedStatementPool = statement.mPoolNext;
            statement.mPoolNext = null;
            statement.mInCache = false;
        } else {
            statement = new PreparedStatement();
        }
        statement.mSql = sql;
        statement.mStatementPtr = statementPtr;
        statement.mNumParameters = numParameters;
        statement.mType = type;
        statement.mReadOnly = readOnly;
        return statement;
    }

    private void recyclePreparedStatement(PreparedStatement statement) {
        statement.mSql = null;
        statement.mPoolNext = this.mPreparedStatementPool;
        this.mPreparedStatementPool = statement;
    }

    private static String trimSqlForDisplay(String sql) {
        return sql.replaceAll("[\\s]*\\n+[\\s]*", WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
    }
}
