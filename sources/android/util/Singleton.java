package android.util;

import android.annotation.UnsupportedAppUsage;

public abstract class Singleton<T> {
    @UnsupportedAppUsage
    private T mInstance;

    public abstract T create();

    @UnsupportedAppUsage
    public final T get() {
        Object obj;
        synchronized (this) {
            if (this.mInstance == null) {
                this.mInstance = create();
            }
            obj = this.mInstance;
        }
        return obj;
    }
}
