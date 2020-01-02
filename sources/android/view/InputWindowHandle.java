package android.view;

import android.graphics.Region;
import android.os.IBinder;
import java.lang.ref.WeakReference;

public final class InputWindowHandle {
    public boolean canReceiveKeys;
    public final IWindow clientWindow;
    public long dispatchingTimeoutNanos;
    public int displayId;
    public int frameBottom;
    public int frameLeft;
    public int frameRight;
    public int frameTop;
    public boolean hasFocus;
    public boolean hasWallpaper;
    public final InputApplicationHandle inputApplicationHandle;
    public int inputFeatures;
    public int layer;
    public int layoutParamsFlags;
    public int layoutParamsType;
    public String name;
    public int ownerPid;
    public int ownerUid;
    public boolean paused;
    public int portalToDisplayId = -1;
    private long ptr;
    public boolean replaceTouchableRegionWithCrop;
    public float scaleFactor;
    public int surfaceInset;
    public IBinder token;
    public final Region touchableRegion = new Region();
    public WeakReference<IBinder> touchableRegionCropHandle = new WeakReference(null);
    public boolean visible;

    private native void nativeDispose();

    public InputWindowHandle(InputApplicationHandle inputApplicationHandle, IWindow clientWindow, int displayId) {
        this.inputApplicationHandle = inputApplicationHandle;
        this.clientWindow = clientWindow;
        this.displayId = displayId;
    }

    public String toString() {
        String str = this.name;
        if (str == null) {
            str = "";
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.append(", layer=");
        stringBuilder.append(this.layer);
        stringBuilder.append(", frame=[");
        stringBuilder.append(this.frameLeft);
        str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.frameTop);
        stringBuilder.append(str);
        stringBuilder.append(this.frameRight);
        stringBuilder.append(str);
        stringBuilder.append(this.frameBottom);
        stringBuilder.append("]");
        stringBuilder.append(", touchableRegion=");
        stringBuilder.append(this.touchableRegion);
        stringBuilder.append(", visible=");
        stringBuilder.append(this.visible);
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            nativeDispose();
        } finally {
            super.finalize();
        }
    }

    public void replaceTouchableRegionWithCrop(SurfaceControl bounds) {
        setTouchableRegionCrop(bounds);
        this.replaceTouchableRegionWithCrop = true;
    }

    public void setTouchableRegionCrop(SurfaceControl bounds) {
        if (bounds != null) {
            this.touchableRegionCropHandle = new WeakReference(bounds.getHandle());
        }
    }
}
