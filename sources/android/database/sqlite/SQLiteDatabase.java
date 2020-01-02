package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.DefaultDatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class SQLiteDatabase extends SQLiteClosable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_NONE = 0;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_ROLLBACK = 1;
    @UnsupportedAppUsage
    public static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    public static final int CREATE_IF_NECESSARY = 268435456;
    private static final boolean DEBUG_CLOSE_IDLE_CONNECTIONS = SystemProperties.getBoolean("persist.debug.sqlite.close_idle_connections", false);
    public static final int ENABLE_LEGACY_COMPATIBILITY_WAL = Integer.MIN_VALUE;
    public static final int ENABLE_WRITE_AHEAD_LOGGING = 536870912;
    private static final int EVENT_DB_CORRUPT = 75004;
    public static final int MAX_SQL_CACHE_SIZE = 100;
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 0;
    private static final int OPEN_READ_MASK = 1;
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    private static final String TAG = "SQLiteDatabase";
    private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases = new WeakHashMap();
    private final CloseGuard mCloseGuardLocked = CloseGuard.get();
    @UnsupportedAppUsage
    private final SQLiteDatabaseConfiguration mConfigurationLocked;
    @UnsupportedAppUsage
    private SQLiteConnectionPool mConnectionPoolLocked;
    private final CursorFactory mCursorFactory;
    private final DatabaseErrorHandler mErrorHandler;
    private boolean mHasAttachedDbsLocked;
    private final Object mLock = new Object();
    @UnsupportedAppUsage
    private final ThreadLocal<SQLiteSession> mThreadSession = ThreadLocal.withInitial(new -$$Lambda$RBWjWVyGrOTsQrLCYzJ_G8Uk25Q(this));

    public interface CursorFactory {
        Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery);
    }

    public interface CustomFunction {
        void callback(String[] strArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DatabaseOpenFlags {
    }

    public static final class OpenParams {
        private final CursorFactory mCursorFactory;
        private final DatabaseErrorHandler mErrorHandler;
        private final long mIdleConnectionTimeout;
        private final String mJournalMode;
        private final int mLookasideSlotCount;
        private final int mLookasideSlotSize;
        private final int mOpenFlags;
        private final String mSyncMode;

        public static final class Builder {
            private CursorFactory mCursorFactory;
            private DatabaseErrorHandler mErrorHandler;
            private long mIdleConnectionTimeout = -1;
            private String mJournalMode;
            private int mLookasideSlotCount = -1;
            private int mLookasideSlotSize = -1;
            private int mOpenFlags;
            private String mSyncMode;

            public Builder(OpenParams params) {
                this.mLookasideSlotSize = params.mLookasideSlotSize;
                this.mLookasideSlotCount = params.mLookasideSlotCount;
                this.mOpenFlags = params.mOpenFlags;
                this.mCursorFactory = params.mCursorFactory;
                this.mErrorHandler = params.mErrorHandler;
                this.mJournalMode = params.mJournalMode;
                this.mSyncMode = params.mSyncMode;
            }

            public Builder setLookasideConfig(int slotSize, int slotCount) {
                boolean z = true;
                Preconditions.checkArgument(slotSize >= 0, "lookasideSlotCount cannot be negative");
                Preconditions.checkArgument(slotCount >= 0, "lookasideSlotSize cannot be negative");
                if ((slotSize <= 0 || slotCount <= 0) && !(slotCount == 0 && slotSize == 0)) {
                    z = false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid configuration: ");
                stringBuilder.append(slotSize);
                stringBuilder.append(", ");
                stringBuilder.append(slotCount);
                Preconditions.checkArgument(z, stringBuilder.toString());
                this.mLookasideSlotSize = slotSize;
                this.mLookasideSlotCount = slotCount;
                return this;
            }

            public boolean isWriteAheadLoggingEnabled() {
                return (this.mOpenFlags & 536870912) != 0;
            }

            public Builder setOpenFlags(int openFlags) {
                this.mOpenFlags = openFlags;
                return this;
            }

            public Builder addOpenFlags(int openFlags) {
                this.mOpenFlags |= openFlags;
                return this;
            }

            public Builder removeOpenFlags(int openFlags) {
                this.mOpenFlags &= ~openFlags;
                return this;
            }

            public void setWriteAheadLoggingEnabled(boolean enabled) {
                if (enabled) {
                    addOpenFlags(536870912);
                } else {
                    removeOpenFlags(536870912);
                }
            }

            public Builder setCursorFactory(CursorFactory cursorFactory) {
                this.mCursorFactory = cursorFactory;
                return this;
            }

            public Builder setErrorHandler(DatabaseErrorHandler errorHandler) {
                this.mErrorHandler = errorHandler;
                return this;
            }

            @Deprecated
            public Builder setIdleConnectionTimeout(long idleConnectionTimeoutMs) {
                Preconditions.checkArgument(idleConnectionTimeoutMs >= 0, "idle connection timeout cannot be negative");
                this.mIdleConnectionTimeout = idleConnectionTimeoutMs;
                return this;
            }

            public Builder setJournalMode(String journalMode) {
                Preconditions.checkNotNull(journalMode);
                this.mJournalMode = journalMode;
                return this;
            }

            public Builder setSynchronousMode(String syncMode) {
                Preconditions.checkNotNull(syncMode);
                this.mSyncMode = syncMode;
                return this;
            }

            public OpenParams build() {
                return new OpenParams(this.mOpenFlags, this.mCursorFactory, this.mErrorHandler, this.mLookasideSlotSize, this.mLookasideSlotCount, this.mIdleConnectionTimeout, this.mJournalMode, this.mSyncMode, null);
            }
        }

        /* synthetic */ OpenParams(int x0, CursorFactory x1, DatabaseErrorHandler x2, int x3, int x4, long x5, String x6, String x7, AnonymousClass1 x8) {
            this(x0, x1, x2, x3, x4, x5, x6, x7);
        }

        private OpenParams(int openFlags, CursorFactory cursorFactory, DatabaseErrorHandler errorHandler, int lookasideSlotSize, int lookasideSlotCount, long idleConnectionTimeout, String journalMode, String syncMode) {
            this.mOpenFlags = openFlags;
            this.mCursorFactory = cursorFactory;
            this.mErrorHandler = errorHandler;
            this.mLookasideSlotSize = lookasideSlotSize;
            this.mLookasideSlotCount = lookasideSlotCount;
            this.mIdleConnectionTimeout = idleConnectionTimeout;
            this.mJournalMode = journalMode;
            this.mSyncMode = syncMode;
        }

        public int getLookasideSlotSize() {
            return this.mLookasideSlotSize;
        }

        public int getLookasideSlotCount() {
            return this.mLookasideSlotCount;
        }

        public int getOpenFlags() {
            return this.mOpenFlags;
        }

        public CursorFactory getCursorFactory() {
            return this.mCursorFactory;
        }

        public DatabaseErrorHandler getErrorHandler() {
            return this.mErrorHandler;
        }

        public long getIdleConnectionTimeout() {
            return this.mIdleConnectionTimeout;
        }

        public String getJournalMode() {
            return this.mJournalMode;
        }

        public String getSynchronousMode() {
            return this.mSyncMode;
        }

        public Builder toBuilder() {
            return new Builder(this);
        }
    }

    private SQLiteDatabase(String path, int openFlags, CursorFactory cursorFactory, DatabaseErrorHandler errorHandler, int lookasideSlotSize, int lookasideSlotCount, long idleConnectionTimeoutMs, String journalMode, String syncMode) {
        this.mCursorFactory = cursorFactory;
        this.mErrorHandler = errorHandler != null ? errorHandler : new DefaultDatabaseErrorHandler();
        this.mConfigurationLocked = new SQLiteDatabaseConfiguration(path, openFlags);
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
        sQLiteDatabaseConfiguration.lookasideSlotSize = lookasideSlotSize;
        sQLiteDatabaseConfiguration.lookasideSlotCount = lookasideSlotCount;
        if (ActivityManager.isLowRamDeviceStatic()) {
            sQLiteDatabaseConfiguration = this.mConfigurationLocked;
            sQLiteDatabaseConfiguration.lookasideSlotCount = 0;
            sQLiteDatabaseConfiguration.lookasideSlotSize = 0;
        }
        long effectiveTimeoutMs = Long.MAX_VALUE;
        if (!this.mConfigurationLocked.isInMemoryDb()) {
            if (idleConnectionTimeoutMs >= 0) {
                effectiveTimeoutMs = idleConnectionTimeoutMs;
            } else if (DEBUG_CLOSE_IDLE_CONNECTIONS) {
                effectiveTimeoutMs = (long) SQLiteGlobal.getIdleConnectionTimeout();
            }
        }
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration2 = this.mConfigurationLocked;
        sQLiteDatabaseConfiguration2.idleConnectionTimeoutMs = effectiveTimeoutMs;
        sQLiteDatabaseConfiguration2.journalMode = journalMode;
        sQLiteDatabaseConfiguration2.syncMode = syncMode;
        if (SQLiteCompatibilityWalFlags.isLegacyCompatibilityWalEnabled()) {
            sQLiteDatabaseConfiguration2 = this.mConfigurationLocked;
            sQLiteDatabaseConfiguration2.openFlags |= Integer.MIN_VALUE;
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

    /* Access modifiers changed, original: protected */
    public void onAllReferencesReleased() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        SQLiteConnectionPool pool;
        synchronized (this.mLock) {
            if (this.mCloseGuardLocked != null) {
                if (finalized) {
                    this.mCloseGuardLocked.warnIfOpen();
                }
                this.mCloseGuardLocked.close();
            }
            pool = this.mConnectionPoolLocked;
            this.mConnectionPoolLocked = null;
        }
        if (!finalized) {
            synchronized (sActiveDatabases) {
                sActiveDatabases.remove(this);
            }
            if (pool != null) {
                pool.close();
            }
        }
    }

    public static int releaseMemory() {
        return SQLiteGlobal.releaseMemory();
    }

    @Deprecated
    public void setLockingEnabled(boolean lockingEnabled) {
    }

    /* Access modifiers changed, original: 0000 */
    public String getLabel() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.label;
        }
        return str;
    }

    /* Access modifiers changed, original: 0000 */
    public void onCorruption() {
        EventLog.writeEvent((int) EVENT_DB_CORRUPT, getLabel());
        this.mErrorHandler.onCorruption(this);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public SQLiteSession getThreadSession() {
        return (SQLiteSession) this.mThreadSession.get();
    }

    /* Access modifiers changed, original: 0000 */
    public SQLiteSession createSession() {
        SQLiteConnectionPool pool;
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            pool = this.mConnectionPoolLocked;
        }
        return new SQLiteSession(pool);
    }

    /* Access modifiers changed, original: 0000 */
    public int getThreadDefaultConnectionFlags(boolean readOnly) {
        int flags;
        if (readOnly) {
            flags = 1;
        } else {
            flags = 2;
        }
        if (isMainThread()) {
            return flags | 4;
        }
        return flags;
    }

    private static boolean isMainThread() {
        Looper looper = Looper.myLooper();
        return looper != null && looper == Looper.getMainLooper();
    }

    public void beginTransaction() {
        beginTransaction(null, true);
    }

    public void beginTransactionNonExclusive() {
        beginTransaction(null, false);
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, true);
    }

    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, false);
    }

    @UnsupportedAppUsage
    private void beginTransaction(SQLiteTransactionListener transactionListener, boolean exclusive) {
        acquireReference();
        try {
            int i;
            SQLiteSession threadSession = getThreadSession();
            if (exclusive) {
                i = 2;
            } else {
                i = 1;
            }
            threadSession.beginTransaction(i, transactionListener, getThreadDefaultConnectionFlags(false), null);
        } finally {
            releaseReference();
        }
    }

    public void endTransaction() {
        acquireReference();
        try {
            getThreadSession().endTransaction(null);
        } finally {
            releaseReference();
        }
    }

    public void setTransactionSuccessful() {
        acquireReference();
        try {
            getThreadSession().setTransactionSuccessful();
        } finally {
            releaseReference();
        }
    }

    public boolean inTransaction() {
        acquireReference();
        try {
            boolean hasTransaction = getThreadSession().hasTransaction();
            return hasTransaction;
        } finally {
            releaseReference();
        }
    }

    public boolean isDbLockedByCurrentThread() {
        acquireReference();
        try {
            boolean hasConnection = getThreadSession().hasConnection();
            return hasConnection;
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public boolean isDbLockedByOtherThreads() {
        return false;
    }

    @Deprecated
    public boolean yieldIfContended() {
        return yieldIfContendedHelper(false, -1);
    }

    public boolean yieldIfContendedSafely() {
        return yieldIfContendedHelper(true, -1);
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return yieldIfContendedHelper(true, sleepAfterYieldDelay);
    }

    private boolean yieldIfContendedHelper(boolean throwIfUnsafe, long sleepAfterYieldDelay) {
        acquireReference();
        try {
            boolean yieldTransaction = getThreadSession().yieldTransaction(sleepAfterYieldDelay, throwIfUnsafe, null);
            return yieldTransaction;
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public Map<String, String> getSyncedTables() {
        return new HashMap(0);
    }

    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags) {
        return openDatabase(path, factory, flags, null);
    }

    public static SQLiteDatabase openDatabase(File path, OpenParams openParams) {
        return openDatabase(path.getPath(), openParams);
    }

    @UnsupportedAppUsage
    private static SQLiteDatabase openDatabase(String path, OpenParams openParams) {
        Preconditions.checkArgument(openParams != null, "OpenParams cannot be null");
        SQLiteDatabase sQLiteDatabase = new SQLiteDatabase(path, openParams.mOpenFlags, openParams.mCursorFactory, openParams.mErrorHandler, openParams.mLookasideSlotSize, openParams.mLookasideSlotCount, openParams.mIdleConnectionTimeout, openParams.mJournalMode, openParams.mSyncMode);
        sQLiteDatabase.open();
        return sQLiteDatabase;
    }

    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase db = new SQLiteDatabase(path, flags, factory, errorHandler, -1, -1, -1, null, null);
        db.open();
        return db;
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, CursorFactory factory) {
        return openOrCreateDatabase(file.getPath(), factory);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory) {
        return openDatabase(path, factory, 268435456, null);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openDatabase(path, factory, 268435456, errorHandler);
    }

    public static boolean deleteDatabase(File file) {
        return deleteDatabase(file, true);
    }

    public static boolean deleteDatabase(File file, boolean removeCheckFile) {
        if (file != null) {
            boolean deleted = false | file.delete();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getPath());
            stringBuilder.append("-journal");
            deleted |= new File(stringBuilder.toString()).delete();
            stringBuilder = new StringBuilder();
            stringBuilder.append(file.getPath());
            stringBuilder.append("-shm");
            deleted |= new File(stringBuilder.toString()).delete();
            stringBuilder = new StringBuilder();
            stringBuilder.append(file.getPath());
            stringBuilder.append("-wal");
            deleted |= new File(stringBuilder.toString()).delete();
            stringBuilder = new StringBuilder();
            stringBuilder.append(file.getPath());
            stringBuilder.append("-wipecheck");
            new File(stringBuilder.toString()).delete();
            File dir = file.getParentFile();
            if (dir != null) {
                String prefix = new StringBuilder();
                prefix.append(file.getName());
                prefix.append("-mj");
                prefix = prefix.toString();
                File[] files = dir.listFiles(new FileFilter() {
                    public boolean accept(File candidate) {
                        return candidate.getName().startsWith(prefix);
                    }
                });
                if (files != null) {
                    for (File masterJournal : files) {
                        deleted |= masterJournal.delete();
                    }
                }
            }
            return deleted;
        }
        throw new IllegalArgumentException("file must not be null");
    }

    @UnsupportedAppUsage
    public void reopenReadWrite() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if (isReadOnlyLocked()) {
                int oldOpenFlags = this.mConfigurationLocked.openFlags;
                this.mConfigurationLocked.openFlags = (this.mConfigurationLocked.openFlags & -2) | 0;
                try {
                    this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
                    return;
                } catch (RuntimeException ex) {
                    this.mConfigurationLocked.openFlags = oldOpenFlags;
                    throw ex;
                }
            }
        }
    }

    private void open() {
        String str = TAG;
        try {
            openInner();
        } catch (RuntimeException ex) {
            if (SQLiteDatabaseCorruptException.isCorruptException(ex)) {
                Log.e(str, "Database corruption detected in open()", ex);
                onCorruption();
                openInner();
            } else {
                throw ex;
            }
        } catch (SQLiteException ex2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to open database '");
            stringBuilder.append(getLabel());
            stringBuilder.append("'.");
            Log.e(str, stringBuilder.toString(), ex2);
            close();
            throw ex2;
        }
    }

    private void openInner() {
        synchronized (this.mLock) {
            this.mConnectionPoolLocked = SQLiteConnectionPool.open(this.mConfigurationLocked);
            this.mCloseGuardLocked.open("close");
        }
        synchronized (sActiveDatabases) {
            sActiveDatabases.put(this, null);
        }
    }

    public static SQLiteDatabase create(CursorFactory factory) {
        return openDatabase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH, factory, 268435456);
    }

    public static SQLiteDatabase createInMemory(OpenParams openParams) {
        return openDatabase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH, openParams.toBuilder().addOpenFlags(268435456).build());
    }

    public void addCustomFunction(String name, int numArgs, CustomFunction function) {
        SQLiteCustomFunction wrapper = new SQLiteCustomFunction(name, numArgs, function);
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            this.mConfigurationLocked.customFunctions.add(wrapper);
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.customFunctions.remove(wrapper);
                throw ex;
            }
        }
    }

    public int getVersion() {
        return Long.valueOf(DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
    }

    public void setVersion(int version) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA user_version = ");
        stringBuilder.append(version);
        execSQL(stringBuilder.toString());
    }

    public long getMaximumSize() {
        return getPageSize() * DatabaseUtils.longForQuery(this, "PRAGMA max_page_count;", null);
    }

    public long setMaximumSize(long numBytes) {
        long pageSize = getPageSize();
        long numPages = numBytes / pageSize;
        if (numBytes % pageSize != 0) {
            numPages++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA max_page_count = ");
        stringBuilder.append(numPages);
        return DatabaseUtils.longForQuery(this, stringBuilder.toString(), null) * pageSize;
    }

    public long getPageSize() {
        return DatabaseUtils.longForQuery(this, "PRAGMA page_size;", null);
    }

    public void setPageSize(long numBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA page_size = ");
        stringBuilder.append(numBytes);
        execSQL(stringBuilder.toString());
    }

    @Deprecated
    public void markTableSyncable(String table, String deletedTable) {
    }

    @Deprecated
    public void markTableSyncable(String table, String foreignKey, String updateTable) {
    }

    public static String findEditTable(String tables) {
        if (TextUtils.isEmpty(tables)) {
            throw new IllegalStateException("Invalid tables");
        }
        int spacepos = tables.indexOf(32);
        int commapos = tables.indexOf(44);
        if (spacepos > 0 && (spacepos < commapos || commapos < 0)) {
            return tables.substring(0, spacepos);
        }
        if (commapos <= 0 || (commapos >= spacepos && spacepos >= 0)) {
            return tables;
        }
        return tables.substring(0, commapos);
    }

    public SQLiteStatement compileStatement(String sql) throws SQLException {
        acquireReference();
        try {
            SQLiteStatement sQLiteStatement = new SQLiteStatement(this, sql, null);
            return sQLiteStatement;
        } finally {
            releaseReference();
        }
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, null);
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
    }

    public Cursor queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, null);
    }

    public Cursor queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            Cursor rawQueryWithFactory = rawQueryWithFactory(cursorFactory, SQLiteQueryBuilder.buildQueryString(distinct, table, columns, selection, groupBy, having, orderBy, limit), selectionArgs, findEditTable(table), cancellationSignal);
            return rawQueryWithFactory;
        } finally {
            releaseReference();
        }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, null);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs, CancellationSignal cancellationSignal) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, cancellationSignal);
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        return rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable, null);
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            Cursor query = new SQLiteDirectCursorDriver(this, sql, editTable, cancellationSignal).query(cursorFactory != null ? cursorFactory : this.mCursorFactory, selectionArgs);
            return query;
        } finally {
            releaseReference();
        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, 0);
        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error inserting ");
            stringBuilder.append(values);
            Log.e(TAG, stringBuilder.toString(), e);
            return -1;
        }
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, values, 0);
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        try {
            return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error inserting ");
            stringBuilder.append(initialValues);
            Log.e(TAG, stringBuilder.toString(), e);
            return -1;
        }
    }

    public long replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        acquireReference();
        SQLiteStatement statement;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');
            Object[] bindArgs = null;
            int size = (initialValues == null || initialValues.isEmpty()) ? 0 : initialValues.size();
            if (size > 0) {
                bindArgs = new Object[size];
                int i = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append(i > 0 ? "," : "");
                    sql.append(colName);
                    int i2 = i + 1;
                    bindArgs[i] = initialValues.get(colName);
                    i = i2;
                }
                sql.append(')');
                sql.append(" VALUES (");
                i = 0;
                while (i < size) {
                    sql.append(i > 0 ? ",?" : "?");
                    i++;
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(nullColumnHack);
                stringBuilder.append(") VALUES (NULL");
                sql.append(stringBuilder.toString());
            }
            sql.append(')');
            statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            long executeInsert = statement.executeInsert();
            statement.close();
            releaseReference();
            return executeInsert;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        acquireReference();
        SQLiteStatement statement;
        try {
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM ");
            stringBuilder.append(table);
            if (TextUtils.isEmpty(whereClause)) {
                str = "";
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(" WHERE ");
                stringBuilder2.append(whereClause);
                str = stringBuilder2.toString();
            }
            stringBuilder.append(str);
            statement = new SQLiteStatement(this, stringBuilder.toString(), whereArgs);
            int executeUpdateDelete = statement.executeUpdateDelete();
            statement.close();
            releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, 0);
    }

    public int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Empty values");
        }
        acquireReference();
        SQLiteStatement statement;
        try {
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(table);
            sql.append(" SET ");
            int setValuesSize = values.size();
            int bindArgsSize = whereArgs == null ? setValuesSize : whereArgs.length + setValuesSize;
            Object[] bindArgs = new Object[bindArgsSize];
            int i = 0;
            for (String colName : values.keySet()) {
                sql.append(i > 0 ? "," : "");
                sql.append(colName);
                int i2 = i + 1;
                bindArgs[i] = values.get(colName);
                sql.append("=?");
                i = i2;
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (!TextUtils.isEmpty(whereClause)) {
                sql.append(" WHERE ");
                sql.append(whereClause);
            }
            statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            int executeUpdateDelete = statement.executeUpdateDelete();
            statement.close();
            releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public void execSQL(String sql) throws SQLException {
        executeSql(sql, null);
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        if (bindArgs != null) {
            executeSql(sql, bindArgs);
            return;
        }
        throw new IllegalArgumentException("Empty bindArgs");
    }

    /* JADX WARNING: Missing block: B:36:?, code skipped:
            r2.close();
     */
    public int executeSql(java.lang.String r7, java.lang.Object[] r8) throws android.database.SQLException {
        /*
        r6 = this;
        r6.acquireReference();
        r0 = android.database.DatabaseUtils.getSqlStatementType(r7);	 Catch:{ all -> 0x0054 }
        r1 = 3;
        if (r0 != r1) goto L_0x0025;
    L_0x000a:
        r1 = 0;
        r2 = r6.mLock;	 Catch:{ all -> 0x0054 }
        monitor-enter(r2);	 Catch:{ all -> 0x0054 }
        r3 = r6.mHasAttachedDbsLocked;	 Catch:{ all -> 0x0022 }
        if (r3 != 0) goto L_0x001b;
    L_0x0012:
        r3 = 1;
        r6.mHasAttachedDbsLocked = r3;	 Catch:{ all -> 0x0022 }
        r1 = 1;
        r3 = r6.mConnectionPoolLocked;	 Catch:{ all -> 0x0022 }
        r3.disableIdleConnectionHandler();	 Catch:{ all -> 0x0022 }
    L_0x001b:
        monitor-exit(r2);	 Catch:{ all -> 0x0022 }
        if (r1 == 0) goto L_0x0025;
    L_0x001e:
        r6.disableWriteAheadLogging();	 Catch:{ all -> 0x0054 }
        goto L_0x0025;
    L_0x0022:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0022 }
        throw r3;	 Catch:{ all -> 0x0054 }
    L_0x0025:
        r1 = 8;
        r2 = new android.database.sqlite.SQLiteStatement;	 Catch:{ all -> 0x004a }
        r2.<init>(r6, r7, r8);	 Catch:{ all -> 0x004a }
        r3 = r2.executeUpdateDelete();	 Catch:{ all -> 0x003e }
        r2.close();	 Catch:{ all -> 0x004a }
        if (r0 != r1) goto L_0x003a;
    L_0x0035:
        r1 = r6.mConnectionPoolLocked;	 Catch:{ all -> 0x0054 }
        r1.closeAvailableNonPrimaryConnectionsAndLogExceptions();	 Catch:{ all -> 0x0054 }
    L_0x003a:
        r6.releaseReference();
        return r3;
    L_0x003e:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0040 }
    L_0x0040:
        r4 = move-exception;
        r2.close();	 Catch:{ all -> 0x0045 }
        goto L_0x0049;
    L_0x0045:
        r5 = move-exception;
        r3.addSuppressed(r5);	 Catch:{ all -> 0x004a }
    L_0x0049:
        throw r4;	 Catch:{ all -> 0x004a }
    L_0x004a:
        r2 = move-exception;
        if (r0 != r1) goto L_0x0052;
    L_0x004d:
        r1 = r6.mConnectionPoolLocked;	 Catch:{ all -> 0x0054 }
        r1.closeAvailableNonPrimaryConnectionsAndLogExceptions();	 Catch:{ all -> 0x0054 }
        throw r2;	 Catch:{ all -> 0x0054 }
    L_0x0054:
        r0 = move-exception;
        r6.releaseReference();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteDatabase.executeSql(java.lang.String, java.lang.Object[]):int");
    }

    public void validateSql(String sql, CancellationSignal cancellationSignal) {
        getThreadSession().prepare(sql, getThreadDefaultConnectionFlags(true), cancellationSignal, null);
    }

    public boolean isReadOnly() {
        boolean isReadOnlyLocked;
        synchronized (this.mLock) {
            isReadOnlyLocked = isReadOnlyLocked();
        }
        return isReadOnlyLocked;
    }

    private boolean isReadOnlyLocked() {
        return (this.mConfigurationLocked.openFlags & 1) == 1;
    }

    public boolean isInMemoryDatabase() {
        boolean isInMemoryDb;
        synchronized (this.mLock) {
            isInMemoryDb = this.mConfigurationLocked.isInMemoryDb();
        }
        return isInMemoryDb;
    }

    public boolean isOpen() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionPoolLocked != null;
        }
        return z;
    }

    public boolean needUpgrade(int newVersion) {
        return newVersion > getVersion();
    }

    public final String getPath() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.path;
        }
        return str;
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            synchronized (this.mLock) {
                throwIfNotOpenLocked();
                Locale oldLocale = this.mConfigurationLocked.locale;
                this.mConfigurationLocked.locale = locale;
                try {
                    this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
                } catch (RuntimeException ex) {
                    this.mConfigurationLocked.locale = oldLocale;
                    throw ex;
                }
            }
            return;
        }
        throw new IllegalArgumentException("locale must not be null.");
    }

    public void setMaxSqlCacheSize(int cacheSize) {
        if (cacheSize > 100 || cacheSize < 0) {
            throw new IllegalStateException("expected value between 0 and 100");
        }
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            int oldMaxSqlCacheSize = this.mConfigurationLocked.maxSqlCacheSize;
            this.mConfigurationLocked.maxSqlCacheSize = cacheSize;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.maxSqlCacheSize = oldMaxSqlCacheSize;
                throw ex;
            }
        }
    }

    public void setForeignKeyConstraintsEnabled(boolean enable) {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if (this.mConfigurationLocked.foreignKeyConstraintsEnabled == enable) {
                return;
            }
            this.mConfigurationLocked.foreignKeyConstraintsEnabled = enable;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.foreignKeyConstraintsEnabled = !enable;
                throw ex;
            }
        }
    }

    /* JADX WARNING: Missing block: B:22:0x005a, code skipped:
            return false;
     */
    public boolean enableWriteAheadLogging() {
        /*
        r5 = this;
        r0 = r5.mLock;
        monitor-enter(r0);
        r5.throwIfNotOpenLocked();	 Catch:{ all -> 0x0079 }
        r1 = r5.mConfigurationLocked;	 Catch:{ all -> 0x0079 }
        r1 = r1.openFlags;	 Catch:{ all -> 0x0079 }
        r2 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r1 = r1 & r2;
        r3 = 1;
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        return r3;
    L_0x0012:
        r1 = r5.isReadOnlyLocked();	 Catch:{ all -> 0x0079 }
        r4 = 0;
        if (r1 == 0) goto L_0x001b;
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        return r4;
    L_0x001b:
        r1 = r5.mConfigurationLocked;	 Catch:{ all -> 0x0079 }
        r1 = r1.isInMemoryDb();	 Catch:{ all -> 0x0079 }
        if (r1 == 0) goto L_0x002c;
    L_0x0023:
        r1 = "SQLiteDatabase";
        r2 = "can't enable WAL for memory databases.";
        android.util.Log.i(r1, r2);	 Catch:{ all -> 0x0079 }
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        return r4;
    L_0x002c:
        r1 = r5.mHasAttachedDbsLocked;	 Catch:{ all -> 0x0079 }
        if (r1 == 0) goto L_0x005b;
    L_0x0030:
        r1 = "SQLiteDatabase";
        r2 = 3;
        r1 = android.util.Log.isLoggable(r1, r2);	 Catch:{ all -> 0x0079 }
        if (r1 == 0) goto L_0x0059;
    L_0x0039:
        r1 = "SQLiteDatabase";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0079 }
        r2.<init>();	 Catch:{ all -> 0x0079 }
        r3 = "this database: ";
        r2.append(r3);	 Catch:{ all -> 0x0079 }
        r3 = r5.mConfigurationLocked;	 Catch:{ all -> 0x0079 }
        r3 = r3.label;	 Catch:{ all -> 0x0079 }
        r2.append(r3);	 Catch:{ all -> 0x0079 }
        r3 = " has attached databases. can't  enable WAL.";
        r2.append(r3);	 Catch:{ all -> 0x0079 }
        r2 = r2.toString();	 Catch:{ all -> 0x0079 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0079 }
    L_0x0059:
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        return r4;
    L_0x005b:
        r1 = r5.mConfigurationLocked;	 Catch:{ all -> 0x0079 }
        r4 = r1.openFlags;	 Catch:{ all -> 0x0079 }
        r2 = r2 | r4;
        r1.openFlags = r2;	 Catch:{ all -> 0x0079 }
        r1 = r5.mConnectionPoolLocked;	 Catch:{ RuntimeException -> 0x006c }
        r2 = r5.mConfigurationLocked;	 Catch:{ RuntimeException -> 0x006c }
        r1.reconfigure(r2);	 Catch:{ RuntimeException -> 0x006c }
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        return r3;
    L_0x006c:
        r1 = move-exception;
        r2 = r5.mConfigurationLocked;	 Catch:{ all -> 0x0079 }
        r3 = r2.openFlags;	 Catch:{ all -> 0x0079 }
        r4 = -536870913; // 0xffffffffdfffffff float:-3.6893486E19 double:NaN;
        r3 = r3 & r4;
        r2.openFlags = r3;	 Catch:{ all -> 0x0079 }
        throw r1;	 Catch:{ all -> 0x0079 }
    L_0x0079:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0079 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteDatabase.enableWriteAheadLogging():boolean");
    }

    public void disableWriteAheadLogging() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            int oldFlags = this.mConfigurationLocked.openFlags;
            boolean compatibilityWalEnabled = true;
            boolean walEnabled = (536870912 & oldFlags) != 0;
            if ((Integer.MIN_VALUE & oldFlags) == 0) {
                compatibilityWalEnabled = false;
            }
            if (walEnabled || compatibilityWalEnabled) {
                SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
                sQLiteDatabaseConfiguration.openFlags &= -536870913;
                sQLiteDatabaseConfiguration = this.mConfigurationLocked;
                sQLiteDatabaseConfiguration.openFlags &= Integer.MAX_VALUE;
                try {
                    this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
                    return;
                } catch (RuntimeException ex) {
                    this.mConfigurationLocked.openFlags = oldFlags;
                    throw ex;
                }
            }
        }
    }

    public boolean isWriteAheadLoggingEnabled() {
        boolean z;
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            z = (this.mConfigurationLocked.openFlags & 536870912) != 0;
        }
        return z;
    }

    static ArrayList<DbStats> getDbStats() {
        ArrayList<DbStats> dbStatsList = new ArrayList();
        Iterator it = getActiveDatabases().iterator();
        while (it.hasNext()) {
            ((SQLiteDatabase) it.next()).collectDbStats(dbStatsList);
        }
        return dbStatsList;
    }

    @UnsupportedAppUsage
    private void collectDbStats(ArrayList<DbStats> dbStatsList) {
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked != null) {
                this.mConnectionPoolLocked.collectDbStats(dbStatsList);
            }
        }
    }

    @UnsupportedAppUsage
    private static ArrayList<SQLiteDatabase> getActiveDatabases() {
        ArrayList<SQLiteDatabase> databases = new ArrayList();
        synchronized (sActiveDatabases) {
            databases.addAll(sActiveDatabases.keySet());
        }
        return databases;
    }

    static void dumpAll(Printer printer, boolean verbose, boolean isSystem) {
        ArraySet<String> directories = new ArraySet();
        Iterator it = getActiveDatabases().iterator();
        while (it.hasNext()) {
            ((SQLiteDatabase) it.next()).dump(printer, verbose, isSystem, directories);
        }
        if (directories.size() > 0) {
            String[] dirs = (String[]) directories.toArray(new String[directories.size()]);
            Arrays.sort(dirs);
            for (String dir : dirs) {
                dumpDatabaseDirectory(printer, new File(dir), isSystem);
            }
        }
    }

    private void dump(Printer printer, boolean verbose, boolean isSystem, ArraySet directories) {
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked != null) {
                printer.println("");
                this.mConnectionPoolLocked.dump(printer, verbose, directories);
            }
        }
    }

    private static void dumpDatabaseDirectory(Printer pw, File dir, boolean isSystem) {
        pw.println("");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Database files in ");
        stringBuilder.append(dir.getAbsolutePath());
        stringBuilder.append(":");
        pw.println(stringBuilder.toString());
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            pw.println("  [none]");
            return;
        }
        Arrays.sort(files, -$$Lambda$SQLiteDatabase$1FsSJH2q7x3eeDFXCAu9l4piDsE.INSTANCE);
        for (File f : files) {
            if (isSystem) {
                String name = f.getName();
                if (!(name.endsWith(".db") || name.endsWith(".db-wal") || name.endsWith(".db-journal") || name.endsWith("-wipecheck"))) {
                }
            }
            pw.println(String.format("  %-40s %7db %s", new Object[]{f.getName(), Long.valueOf(f.length()), getFileTimestamps(f.getAbsolutePath())}));
        }
    }

    /* JADX WARNING: Missing block: B:14:0x0027, code skipped:
            r1 = null;
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            r1 = rawQuery("pragma database_list;", null);
     */
    /* JADX WARNING: Missing block: B:18:0x0034, code skipped:
            if (r1.moveToNext() == false) goto L_0x004a;
     */
    /* JADX WARNING: Missing block: B:19:0x0036, code skipped:
            r0.add(new android.util.Pair(r1.getString(1), r1.getString(2)));
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:23:0x004d, code skipped:
            releaseReference();
     */
    /* JADX WARNING: Missing block: B:24:0x0051, code skipped:
            return r0;
     */
    /* JADX WARNING: Missing block: B:26:0x0053, code skipped:
            if (r1 != null) goto L_0x0055;
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:32:0x005b, code skipped:
            releaseReference();
     */
    public java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getAttachedDbs() {
        /*
        r5 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = r5.mLock;
        monitor-enter(r1);
        r2 = r5.mConnectionPoolLocked;	 Catch:{ all -> 0x005f }
        r3 = 0;
        if (r2 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r1);	 Catch:{ all -> 0x005f }
        return r3;
    L_0x000f:
        r2 = r5.mHasAttachedDbsLocked;	 Catch:{ all -> 0x005f }
        if (r2 != 0) goto L_0x0023;
    L_0x0013:
        r2 = new android.util.Pair;	 Catch:{ all -> 0x005f }
        r3 = "main";
        r4 = r5.mConfigurationLocked;	 Catch:{ all -> 0x005f }
        r4 = r4.path;	 Catch:{ all -> 0x005f }
        r2.<init>(r3, r4);	 Catch:{ all -> 0x005f }
        r0.add(r2);	 Catch:{ all -> 0x005f }
        monitor-exit(r1);	 Catch:{ all -> 0x005f }
        return r0;
    L_0x0023:
        r5.acquireReference();	 Catch:{ all -> 0x005f }
        monitor-exit(r1);	 Catch:{ all -> 0x005f }
        r1 = 0;
        r2 = "pragma database_list;";
        r2 = r5.rawQuery(r2, r3);	 Catch:{ all -> 0x0052 }
        r1 = r2;
    L_0x0030:
        r2 = r1.moveToNext();	 Catch:{ all -> 0x0052 }
        if (r2 == 0) goto L_0x0049;
    L_0x0036:
        r2 = new android.util.Pair;	 Catch:{ all -> 0x0052 }
        r3 = 1;
        r3 = r1.getString(r3);	 Catch:{ all -> 0x0052 }
        r4 = 2;
        r4 = r1.getString(r4);	 Catch:{ all -> 0x0052 }
        r2.<init>(r3, r4);	 Catch:{ all -> 0x0052 }
        r0.add(r2);	 Catch:{ all -> 0x0052 }
        goto L_0x0030;
        r1.close();	 Catch:{ all -> 0x005a }
        r5.releaseReference();
        return r0;
    L_0x0052:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0058;
    L_0x0055:
        r1.close();	 Catch:{ all -> 0x005a }
        throw r2;	 Catch:{ all -> 0x005a }
    L_0x005a:
        r1 = move-exception;
        r5.releaseReference();
        throw r1;
    L_0x005f:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005f }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteDatabase.getAttachedDbs():java.util.List");
    }

    public boolean isDatabaseIntegrityOk() {
        acquireReference();
        List<Pair<String, String>> attachedDbs;
        SQLiteStatement prog;
        try {
            attachedDbs = getAttachedDbs();
            if (attachedDbs != null) {
                int i = 0;
                while (i < attachedDbs.size()) {
                    Pair<String, String> p = (Pair) attachedDbs.get(i);
                    prog = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("PRAGMA ");
                    stringBuilder.append((String) p.first);
                    stringBuilder.append(".integrity_check(1);");
                    prog = compileStatement(stringBuilder.toString());
                    String rslt = prog.simpleQueryForString();
                    if (rslt.equalsIgnoreCase("ok")) {
                        prog.close();
                        i++;
                    } else {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("PRAGMA integrity_check on ");
                        stringBuilder2.append((String) p.second);
                        stringBuilder2.append(" returned: ");
                        stringBuilder2.append(rslt);
                        Log.e(str, stringBuilder2.toString());
                        prog.close();
                        releaseReference();
                        return false;
                    }
                }
                releaseReference();
                return true;
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("databaselist for: ");
            stringBuilder3.append(getPath());
            stringBuilder3.append(" couldn't be retrieved. probably because the database is closed");
            throw new IllegalStateException(stringBuilder3.toString());
        } catch (SQLiteException e) {
            attachedDbs = new ArrayList();
            attachedDbs.add(new Pair("main", getPath()));
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQLiteDatabase: ");
        stringBuilder.append(getPath());
        return stringBuilder.toString();
    }

    private void throwIfNotOpenLocked() {
        if (this.mConnectionPoolLocked == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The database '");
            stringBuilder.append(this.mConfigurationLocked.label);
            stringBuilder.append("' is not open.");
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public static void wipeDetected(String filename, String reason) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DB wipe detected: package=");
        stringBuilder.append(ActivityThread.currentPackageName());
        stringBuilder.append(" reason=");
        stringBuilder.append(reason);
        stringBuilder.append(" file=");
        stringBuilder.append(filename);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(getFileTimestamps(filename));
        stringBuilder.append(" checkfile ");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(filename);
        stringBuilder2.append("-wipecheck");
        stringBuilder.append(getFileTimestamps(stringBuilder2.toString()));
        wtfAsSystemServer(TAG, stringBuilder.toString(), new Throwable("STACKTRACE"));
    }

    public static String getFileTimestamps(String path) {
        try {
            BasicFileAttributes attr = Files.readAttributes(FileSystems.getDefault().getPath(path, new String[0]), BasicFileAttributes.class, new LinkOption[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ctime=");
            stringBuilder.append(attr.creationTime());
            stringBuilder.append(" mtime=");
            stringBuilder.append(attr.lastModifiedTime());
            stringBuilder.append(" atime=");
            stringBuilder.append(attr.lastAccessTime());
            return stringBuilder.toString();
        } catch (IOException e) {
            return "[unable to obtain timestamp]";
        }
    }

    static void wtfAsSystemServer(String tag, String message, Throwable stacktrace) {
        Log.e(tag, message, stacktrace);
        ContentResolver.onDbCorruption(tag, message, stacktrace);
    }
}
