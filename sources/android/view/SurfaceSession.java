package android.view;

import android.annotation.UnsupportedAppUsage;

public final class SurfaceSession {
    @UnsupportedAppUsage
    private long mNativeClient = nativeCreate();

    private static native long nativeCreate();

    private static native void nativeDestroy(long j);

    private static native void nativeKill(long j);

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mNativeClient != 0) {
                nativeDestroy(this.mNativeClient);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    @UnsupportedAppUsage
    public void kill() {
        nativeKill(this.mNativeClient);
    }
}
