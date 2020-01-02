package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase.OpenParams;
import android.database.sqlite.SQLiteDatabase.OpenParams.Builder;
import android.os.FileUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.File;

public abstract class SQLiteOpenHelper implements AutoCloseable {
    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private boolean mIsInitializing;
    private final int mMinimumSupportedVersion;
    @UnsupportedAppUsage
    private final String mName;
    private final int mNewVersion;
    private Builder mOpenParamsBuilder;

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        this(context, name, factory, version, 0, errorHandler);
    }

    public SQLiteOpenHelper(Context context, String name, int version, OpenParams openParams) {
        this(context, name, version, 0, openParams.toBuilder());
    }

    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, int minimumSupportedVersion, DatabaseErrorHandler errorHandler) {
        this(context, name, version, minimumSupportedVersion, new Builder());
        this.mOpenParamsBuilder.setCursorFactory(factory);
        this.mOpenParamsBuilder.setErrorHandler(errorHandler);
    }

    private SQLiteOpenHelper(Context context, String name, int version, int minimumSupportedVersion, Builder openParamsBuilder) {
        Preconditions.checkNotNull(openParamsBuilder);
        if (version >= 1) {
            this.mContext = context;
            this.mName = name;
            this.mNewVersion = version;
            this.mMinimumSupportedVersion = Math.max(0, minimumSupportedVersion);
            setOpenParamsBuilder(openParamsBuilder);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Version must be >= 1, was ");
        stringBuilder.append(version);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public String getDatabaseName() {
        return this.mName;
    }

    public void setWriteAheadLoggingEnabled(boolean enabled) {
        synchronized (this) {
            if (this.mOpenParamsBuilder.isWriteAheadLoggingEnabled() != enabled) {
                if (!(this.mDatabase == null || !this.mDatabase.isOpen() || this.mDatabase.isReadOnly())) {
                    if (enabled) {
                        this.mDatabase.enableWriteAheadLogging();
                    } else {
                        this.mDatabase.disableWriteAheadLogging();
                    }
                }
                this.mOpenParamsBuilder.setWriteAheadLoggingEnabled(enabled);
            }
            this.mOpenParamsBuilder.removeOpenFlags(Integer.MIN_VALUE);
        }
    }

    public void setLookasideConfig(int slotSize, int slotCount) {
        synchronized (this) {
            if (this.mDatabase != null) {
                if (this.mDatabase.isOpen()) {
                    throw new IllegalStateException("Lookaside memory config cannot be changed after opening the database");
                }
            }
            this.mOpenParamsBuilder.setLookasideConfig(slotSize, slotCount);
        }
    }

    public void setOpenParams(OpenParams openParams) {
        Preconditions.checkNotNull(openParams);
        synchronized (this) {
            if (this.mDatabase != null) {
                if (this.mDatabase.isOpen()) {
                    throw new IllegalStateException("OpenParams cannot be set after opening the database");
                }
            }
            setOpenParamsBuilder(new Builder(openParams));
        }
    }

    private void setOpenParamsBuilder(Builder openParamsBuilder) {
        this.mOpenParamsBuilder = openParamsBuilder;
        this.mOpenParamsBuilder.addOpenFlags(268435456);
    }

    @Deprecated
    public void setIdleConnectionTimeout(long idleConnectionTimeoutMs) {
        synchronized (this) {
            if (this.mDatabase != null) {
                if (this.mDatabase.isOpen()) {
                    throw new IllegalStateException("Connection timeout setting cannot be changed after opening the database");
                }
            }
            this.mOpenParamsBuilder.setIdleConnectionTimeout(idleConnectionTimeoutMs);
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true);
        }
        return databaseLocked;
    }

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(false);
        }
        return databaseLocked;
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null) {
            if (!sQLiteDatabase.isOpen()) {
                this.mDatabase = null;
            } else if (!(writable && this.mDatabase.isReadOnly())) {
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        File filePath;
        StringBuilder stringBuilder;
        sQLiteDatabase = this.mDatabase;
        OpenParams params;
        try {
            this.mIsInitializing = true;
            if (sQLiteDatabase != null) {
                if (writable && sQLiteDatabase.isReadOnly()) {
                    sQLiteDatabase.reopenReadWrite();
                }
            } else if (this.mName == null) {
                sQLiteDatabase = SQLiteDatabase.createInMemory(this.mOpenParamsBuilder.build());
            } else {
                filePath = this.mContext.getDatabasePath(this.mName);
                params = this.mOpenParamsBuilder.build();
                sQLiteDatabase = SQLiteDatabase.openDatabase(filePath, params);
                setFilePermissionsForDb(filePath.getPath());
            }
        } catch (SQLException ex) {
            if (writable) {
                throw ex;
            } else {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Couldn't open ");
                stringBuilder2.append(this.mName);
                stringBuilder2.append(" for writing (will try read-only):");
                Log.e(str, stringBuilder2.toString(), ex);
                sQLiteDatabase = SQLiteDatabase.openDatabase(filePath, params.toBuilder().addOpenFlags(1).build());
            }
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (!(sQLiteDatabase == null || sQLiteDatabase == this.mDatabase)) {
                sQLiteDatabase.close();
            }
        }
        onConfigure(sQLiteDatabase);
        int version = sQLiteDatabase.getVersion();
        if (version != this.mNewVersion) {
            if (sQLiteDatabase.isReadOnly()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Can't upgrade read-only database from version ");
                stringBuilder.append(sQLiteDatabase.getVersion());
                stringBuilder.append(" to ");
                stringBuilder.append(this.mNewVersion);
                stringBuilder.append(": ");
                stringBuilder.append(this.mName);
                throw new SQLiteException(stringBuilder.toString());
            } else if (version <= 0 || version >= this.mMinimumSupportedVersion) {
                sQLiteDatabase.beginTransaction();
                if (version == 0) {
                    onCreate(sQLiteDatabase);
                } else if (version > this.mNewVersion) {
                    onDowngrade(sQLiteDatabase, version, this.mNewVersion);
                } else {
                    onUpgrade(sQLiteDatabase, version, this.mNewVersion);
                }
                sQLiteDatabase.setVersion(this.mNewVersion);
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
            } else {
                filePath = new File(sQLiteDatabase.getPath());
                onBeforeDelete(sQLiteDatabase);
                sQLiteDatabase.close();
                if (SQLiteDatabase.deleteDatabase(filePath)) {
                    this.mIsInitializing = false;
                    SQLiteDatabase databaseLocked = getDatabaseLocked(writable);
                    this.mIsInitializing = false;
                    if (sQLiteDatabase != this.mDatabase) {
                        sQLiteDatabase.close();
                    }
                    return databaseLocked;
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Unable to delete obsolete database ");
                stringBuilder3.append(this.mName);
                stringBuilder3.append(" with version ");
                stringBuilder3.append(version);
                throw new IllegalStateException(stringBuilder3.toString());
            }
        }
        onOpen(sQLiteDatabase);
        if (sQLiteDatabase.isReadOnly()) {
            String filePath2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Opened ");
            stringBuilder.append(this.mName);
            stringBuilder.append(" in read-only mode");
            Log.w(filePath2, stringBuilder.toString());
        }
        this.mDatabase = sQLiteDatabase;
        this.mIsInitializing = false;
        if (sQLiteDatabase != this.mDatabase) {
            sQLiteDatabase.close();
        }
        return sQLiteDatabase;
    }

    private static void setFilePermissionsForDb(String dbPath) {
        FileUtils.setPermissions(dbPath, (int) DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER, -1, -1);
    }

    public synchronized void close() {
        if (this.mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        } else if (this.mDatabase != null && this.mDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }

    public void onConfigure(SQLiteDatabase db) {
    }

    public void onBeforeDelete(SQLiteDatabase db) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't downgrade database from version ");
        stringBuilder.append(oldVersion);
        stringBuilder.append(" to ");
        stringBuilder.append(newVersion);
        throw new SQLiteException(stringBuilder.toString());
    }

    public void onOpen(SQLiteDatabase db) {
    }
}
