package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import android.database.sqlite.SQLiteDatabase.CustomFunction;

public final class SQLiteCustomFunction {
    public final CustomFunction callback;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final String name;
    @UnsupportedAppUsage
    public final int numArgs;

    public SQLiteCustomFunction(String name, int numArgs, CustomFunction callback) {
        if (name != null) {
            this.name = name;
            this.numArgs = numArgs;
            this.callback = callback;
            return;
        }
        throw new IllegalArgumentException("name must not be null.");
    }

    @UnsupportedAppUsage
    private void dispatchCallback(String[] args) {
        this.callback.callback(args);
    }
}
