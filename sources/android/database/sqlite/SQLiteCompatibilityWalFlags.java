package android.database.sqlite;

import android.app.ActivityThread;
import android.app.Application;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

public class SQLiteCompatibilityWalFlags {
    private static final String TAG = "SQLiteCompatibilityWalFlags";
    private static volatile boolean sCallingGlobalSettings;
    private static volatile boolean sInitialized;
    private static volatile boolean sLegacyCompatibilityWalEnabled;
    private static volatile long sTruncateSize = -1;
    private static volatile String sWALSyncMode;

    private SQLiteCompatibilityWalFlags() {
    }

    @VisibleForTesting
    public static boolean isLegacyCompatibilityWalEnabled() {
        initIfNeeded();
        return sLegacyCompatibilityWalEnabled;
    }

    @VisibleForTesting
    public static String getWALSyncMode() {
        initIfNeeded();
        if (sLegacyCompatibilityWalEnabled) {
            return sWALSyncMode;
        }
        throw new IllegalStateException("isLegacyCompatibilityWalEnabled() == false");
    }

    @VisibleForTesting
    public static long getTruncateSize() {
        initIfNeeded();
        return sTruncateSize;
    }

    private static void initIfNeeded() {
        if (!sInitialized && !sCallingGlobalSettings) {
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Application app = activityThread == null ? null : activityThread.getApplication();
            String flags = null;
            if (app == null) {
                Log.w(TAG, "Cannot read global setting sqlite_compatibility_wal_flags - Application state not available");
            } else {
                try {
                    sCallingGlobalSettings = true;
                    flags = Global.getString(app.getContentResolver(), Global.SQLITE_COMPATIBILITY_WAL_FLAGS);
                } finally {
                    sCallingGlobalSettings = false;
                }
            }
            init(flags);
        }
    }

    @VisibleForTesting
    public static void init(String flags) {
        String str = TAG;
        if (TextUtils.isEmpty(flags)) {
            sInitialized = true;
            return;
        }
        KeyValueListParser parser = new KeyValueListParser(',');
        try {
            parser.setString(flags);
            sLegacyCompatibilityWalEnabled = parser.getBoolean("legacy_compatibility_wal_enabled", false);
            sWALSyncMode = parser.getString("wal_syncmode", SQLiteGlobal.getWALSyncMode());
            sTruncateSize = (long) parser.getInt("truncate_size", -1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Read compatibility WAL flags: legacy_compatibility_wal_enabled=");
            stringBuilder.append(sLegacyCompatibilityWalEnabled);
            stringBuilder.append(", wal_syncmode=");
            stringBuilder.append(sWALSyncMode);
            Log.i(str, stringBuilder.toString());
            sInitialized = true;
        } catch (IllegalArgumentException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Setting has invalid format: ");
            stringBuilder2.append(flags);
            Log.e(str, stringBuilder2.toString(), e);
            sInitialized = true;
        }
    }

    @VisibleForTesting
    public static void reset() {
        sInitialized = false;
        sLegacyCompatibilityWalEnabled = false;
        sWALSyncMode = null;
    }
}
