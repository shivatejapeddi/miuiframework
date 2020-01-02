package android.widget;

import android.util.Log;
import java.util.Locale;

class OverScrollLogger {
    private static final boolean DEBUG;
    private static final String TAG = "OverScroll";
    private static final boolean VERBOSE;

    OverScrollLogger() {
    }

    static {
        String str = TAG;
        DEBUG = Log.isLoggable(str, 3);
        VERBOSE = Log.isLoggable(str, 2);
    }

    public static void debug(String log) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }

    public static void debug(String format, Object... args) {
        if (DEBUG) {
            Log.d(TAG, String.format(Locale.US, format, args));
        }
    }

    public static void verbose(String log) {
        if (VERBOSE) {
            Log.v(TAG, log);
        }
    }

    public static void verbose(String format, Object... args) {
        if (VERBOSE) {
            Log.v(TAG, String.format(Locale.US, format, args));
        }
    }
}
