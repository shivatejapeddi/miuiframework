package android.webkit;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.Handler;

@Deprecated
abstract class WebSyncManager implements Runnable {
    protected static final String LOGTAG = "websync";
    protected WebViewDatabase mDataBase;
    @UnsupportedAppUsage
    protected Handler mHandler;

    @UnsupportedAppUsage
    public abstract void syncFromRamToFlash();

    protected WebSyncManager(Context context, String name) {
    }

    /* Access modifiers changed, original: protected */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("doesn't implement Cloneable");
    }

    public void run() {
    }

    public void sync() {
    }

    public void resetSync() {
    }

    public void startSync() {
    }

    public void stopSync() {
    }

    /* Access modifiers changed, original: protected */
    public void onSyncInit() {
    }
}
