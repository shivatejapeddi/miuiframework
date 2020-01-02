package android.view;

import android.graphics.Rect;
import android.os.IBinder;
import com.android.internal.util.Preconditions;
import java.util.concurrent.Executor;

public abstract class CompositionSamplingListener {
    private final Executor mExecutor;
    private final long mNativeListener = nativeCreate(this);

    private static native long nativeCreate(CompositionSamplingListener compositionSamplingListener);

    private static native void nativeDestroy(long j);

    private static native void nativeRegister(long j, IBinder iBinder, int i, int i2, int i3, int i4);

    private static native void nativeUnregister(long j);

    public abstract void onSampleCollected(float f);

    public CompositionSamplingListener(Executor executor) {
        this.mExecutor = executor;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mNativeListener != 0) {
                unregister(this);
                nativeDestroy(this.mNativeListener);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public static void register(CompositionSamplingListener listener, int displayId, IBinder stopLayer, Rect samplingArea) {
        Preconditions.checkArgument(displayId == 0, "default display only for now");
        nativeRegister(listener.mNativeListener, stopLayer, samplingArea.left, samplingArea.top, samplingArea.right, samplingArea.bottom);
    }

    public static void unregister(CompositionSamplingListener listener) {
        nativeUnregister(listener.mNativeListener);
    }

    private static void dispatchOnSampleCollected(CompositionSamplingListener listener, float medianLuma) {
        listener.mExecutor.execute(new -$$Lambda$CompositionSamplingListener$hrbPutjnKRv7VkkiY9eg32N6QA8(listener, medianLuma));
    }
}
