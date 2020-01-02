package android.view;

import android.os.IBinder;

public final class InputApplicationHandle {
    public long dispatchingTimeoutNanos;
    public String name;
    private long ptr;
    public IBinder token;

    private native void nativeDispose();

    public InputApplicationHandle(IBinder token) {
        this.token = token;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            nativeDispose();
        } finally {
            super.finalize();
        }
    }
}
