package android.hardware.display;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.graphics.ColorSpace;
import android.graphics.ColorSpace.Named;
import android.graphics.Point;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.display.IDisplayManagerCallback.Stub;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.DisplayInfo;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DisplayManagerGlobal {
    private static final boolean DEBUG = false;
    public static final int EVENT_DISPLAY_ADDED = 1;
    public static final int EVENT_DISPLAY_CHANGED = 2;
    public static final int EVENT_DISPLAY_REMOVED = 3;
    private static final String TAG = "DisplayManager";
    private static final boolean USE_CACHE = false;
    @UnsupportedAppUsage
    private static DisplayManagerGlobal sInstance;
    private DisplayManagerCallback mCallback;
    private int[] mDisplayIdCache;
    private final SparseArray<DisplayInfo> mDisplayInfoCache = new SparseArray();
    private final ArrayList<DisplayListenerDelegate> mDisplayListeners = new ArrayList();
    @UnsupportedAppUsage
    private final IDisplayManager mDm;
    private final Object mLock = new Object();
    private final ColorSpace mWideColorSpace;
    private int mWifiDisplayScanNestCount;

    private static final class DisplayListenerDelegate extends Handler {
        public final DisplayListener mListener;

        DisplayListenerDelegate(DisplayListener listener, Looper looper) {
            super(looper, null, true);
            this.mListener = listener;
        }

        public void sendDisplayEvent(int displayId, int event) {
            sendMessage(obtainMessage(event, displayId, null));
        }

        public void clearEvents() {
            removeCallbacksAndMessages(null);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                this.mListener.onDisplayAdded(msg.arg1);
            } else if (i == 2) {
                this.mListener.onDisplayChanged(msg.arg1);
            } else if (i == 3) {
                this.mListener.onDisplayRemoved(msg.arg1);
            }
        }
    }

    private final class DisplayManagerCallback extends Stub {
        private DisplayManagerCallback() {
        }

        public void onDisplayEvent(int displayId, int event) {
            DisplayManagerGlobal.this.handleDisplayEvent(displayId, event);
        }
    }

    private static final class VirtualDisplayCallback extends IVirtualDisplayCallback.Stub {
        private VirtualDisplayCallbackDelegate mDelegate;

        public VirtualDisplayCallback(Callback callback, Handler handler) {
            if (callback != null) {
                this.mDelegate = new VirtualDisplayCallbackDelegate(callback, handler);
            }
        }

        public void onPaused() {
            VirtualDisplayCallbackDelegate virtualDisplayCallbackDelegate = this.mDelegate;
            if (virtualDisplayCallbackDelegate != null) {
                virtualDisplayCallbackDelegate.sendEmptyMessage(0);
            }
        }

        public void onResumed() {
            VirtualDisplayCallbackDelegate virtualDisplayCallbackDelegate = this.mDelegate;
            if (virtualDisplayCallbackDelegate != null) {
                virtualDisplayCallbackDelegate.sendEmptyMessage(1);
            }
        }

        public void onStopped() {
            VirtualDisplayCallbackDelegate virtualDisplayCallbackDelegate = this.mDelegate;
            if (virtualDisplayCallbackDelegate != null) {
                virtualDisplayCallbackDelegate.sendEmptyMessage(2);
            }
        }
    }

    private static final class VirtualDisplayCallbackDelegate extends Handler {
        public static final int MSG_DISPLAY_PAUSED = 0;
        public static final int MSG_DISPLAY_RESUMED = 1;
        public static final int MSG_DISPLAY_STOPPED = 2;
        private final Callback mCallback;

        public VirtualDisplayCallbackDelegate(Callback callback, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mCallback = callback;
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 0) {
                this.mCallback.onPaused();
            } else if (i == 1) {
                this.mCallback.onResumed();
            } else if (i == 2) {
                this.mCallback.onStopped();
            }
        }
    }

    private DisplayManagerGlobal(IDisplayManager dm) {
        this.mDm = dm;
        try {
            this.mWideColorSpace = ColorSpace.get(Named.values()[this.mDm.getPreferredWideGamutColorSpaceId()]);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static DisplayManagerGlobal getInstance() {
        DisplayManagerGlobal displayManagerGlobal;
        synchronized (DisplayManagerGlobal.class) {
            if (sInstance == null) {
                IBinder b = ServiceManager.getService(Context.DISPLAY_SERVICE);
                if (b != null) {
                    sInstance = new DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
                }
            }
            displayManagerGlobal = sInstance;
        }
        return displayManagerGlobal;
    }

    @UnsupportedAppUsage
    public DisplayInfo getDisplayInfo(int displayId) {
        try {
            synchronized (this.mLock) {
                DisplayInfo info = this.mDm.getDisplayInfo(displayId);
                if (info == null) {
                    return null;
                }
                registerCallbackIfNeededLocked();
                return info;
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int[] getDisplayIds() {
        try {
            int[] displayIds;
            synchronized (this.mLock) {
                displayIds = this.mDm.getDisplayIds();
                registerCallbackIfNeededLocked();
            }
            return displayIds;
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public boolean isUidPresentOnDisplay(int uid, int displayId) {
        try {
            return this.mDm.isUidPresentOnDisplay(uid, displayId);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public Display getCompatibleDisplay(int displayId, DisplayAdjustments daj) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, daj);
    }

    public Display getCompatibleDisplay(int displayId, Resources resources) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, resources);
    }

    @UnsupportedAppUsage
    public Display getRealDisplay(int displayId) {
        return getCompatibleDisplay(displayId, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    public void registerDisplayListener(DisplayListener listener, Handler handler) {
        if (listener != null) {
            synchronized (this.mLock) {
                if (findDisplayListenerLocked(listener) < 0) {
                    this.mDisplayListeners.add(new DisplayListenerDelegate(listener, getLooperForHandler(handler)));
                    registerCallbackIfNeededLocked();
                }
            }
            return;
        }
        throw new IllegalArgumentException("listener must not be null");
    }

    public void unregisterDisplayListener(DisplayListener listener) {
        if (listener != null) {
            synchronized (this.mLock) {
                int index = findDisplayListenerLocked(listener);
                if (index >= 0) {
                    ((DisplayListenerDelegate) this.mDisplayListeners.get(index)).clearEvents();
                    this.mDisplayListeners.remove(index);
                }
            }
            return;
        }
        throw new IllegalArgumentException("listener must not be null");
    }

    private static Looper getLooperForHandler(Handler handler) {
        Looper looper = handler != null ? handler.getLooper() : Looper.myLooper();
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        if (looper != null) {
            return looper;
        }
        throw new RuntimeException("Could not get Looper for the UI thread.");
    }

    private int findDisplayListenerLocked(DisplayListener listener) {
        int numListeners = this.mDisplayListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (((DisplayListenerDelegate) this.mDisplayListeners.get(i)).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    private void registerCallbackIfNeededLocked() {
        if (this.mCallback == null) {
            this.mCallback = new DisplayManagerCallback();
            try {
                this.mDm.registerCallback(this.mCallback);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private void handleDisplayEvent(int displayId, int event) {
        synchronized (this.mLock) {
            int numListeners = this.mDisplayListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((DisplayListenerDelegate) this.mDisplayListeners.get(i)).sendDisplayEvent(displayId, event);
            }
        }
    }

    public void startWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount;
            this.mWifiDisplayScanNestCount = i + 1;
            if (i == 0) {
                registerCallbackIfNeededLocked();
                try {
                    this.mDm.startWifiDisplayScan();
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
        }
    }

    public void stopWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount - 1;
            this.mWifiDisplayScanNestCount = i;
            if (i == 0) {
                try {
                    this.mDm.stopWifiDisplayScan();
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } else if (this.mWifiDisplayScanNestCount < 0) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Wifi display scan nest count became negative: ");
                stringBuilder.append(this.mWifiDisplayScanNestCount);
                Log.wtf(str, stringBuilder.toString());
                this.mWifiDisplayScanNestCount = 0;
            }
        }
    }

    public void connectWifiDisplay(String deviceAddress) {
        if (deviceAddress != null) {
            try {
                this.mDm.connectWifiDisplay(deviceAddress);
                return;
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("deviceAddress must not be null");
    }

    public void pauseWifiDisplay() {
        try {
            this.mDm.pauseWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void resumeWifiDisplay() {
        try {
            this.mDm.resumeWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void disconnectWifiDisplay() {
        try {
            this.mDm.disconnectWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void renameWifiDisplay(String deviceAddress, String alias) {
        if (deviceAddress != null) {
            try {
                this.mDm.renameWifiDisplay(deviceAddress, alias);
                return;
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("deviceAddress must not be null");
    }

    public void forgetWifiDisplay(String deviceAddress) {
        if (deviceAddress != null) {
            try {
                this.mDm.forgetWifiDisplay(deviceAddress);
                return;
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("deviceAddress must not be null");
    }

    @UnsupportedAppUsage
    public WifiDisplayStatus getWifiDisplayStatus() {
        try {
            return this.mDm.getWifiDisplayStatus();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void requestColorMode(int displayId, int colorMode) {
        try {
            this.mDm.requestColorMode(displayId, colorMode);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public VirtualDisplay createVirtualDisplay(Context context, MediaProjection projection, String name, int width, int height, int densityDpi, Surface surface, int flags, Callback callback, Handler handler, String uniqueId) {
        RemoteException ex;
        String str = name;
        Surface surface2;
        Handler handler2;
        if (TextUtils.isEmpty(name)) {
            surface2 = surface;
            handler2 = handler;
            throw new IllegalArgumentException("name must be non-null and non-empty");
        } else if (width <= 0 || height <= 0 || densityDpi <= 0) {
            surface2 = surface;
            handler2 = handler;
            throw new IllegalArgumentException("width, height, and densityDpi must be greater than 0");
        } else {
            VirtualDisplayCallback callbackWrapper = new VirtualDisplayCallback(callback, handler);
            VirtualDisplayCallback callbackWrapper2;
            try {
                callbackWrapper2 = callbackWrapper;
                try {
                    int displayId = this.mDm.createVirtualDisplay(callbackWrapper, projection != null ? projection.getProjection() : null, context.getPackageName(), name, width, height, densityDpi, surface, flags, uniqueId);
                    String str2 = TAG;
                    if (displayId < 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Could not create virtual display: ");
                        stringBuilder.append(str);
                        Log.e(str2, stringBuilder.toString());
                        return null;
                    }
                    Display display = getRealDisplay(displayId);
                    if (display != null) {
                        return new VirtualDisplay(this, display, callbackWrapper2, surface);
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Could not obtain display info for newly created virtual display: ");
                    stringBuilder2.append(str);
                    Log.wtf(str2, stringBuilder2.toString());
                    try {
                        this.mDm.releaseVirtualDisplay(callbackWrapper2);
                        return null;
                    } catch (RemoteException ex2) {
                        throw ex2.rethrowFromSystemServer();
                    }
                } catch (RemoteException e) {
                    ex2 = e;
                    surface2 = surface;
                    throw ex2.rethrowFromSystemServer();
                }
            } catch (RemoteException e2) {
                ex2 = e2;
                surface2 = surface;
                callbackWrapper2 = callbackWrapper;
                throw ex2.rethrowFromSystemServer();
            }
        }
    }

    public void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) {
        try {
            this.mDm.setVirtualDisplaySurface(token, surface);
            setVirtualDisplayState(token, surface != null);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) {
        try {
            this.mDm.resizeVirtualDisplay(token, width, height, densityDpi);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void releaseVirtualDisplay(IVirtualDisplayCallback token) {
        try {
            this.mDm.releaseVirtualDisplay(token);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setVirtualDisplayState(IVirtualDisplayCallback token, boolean isOn) {
        try {
            this.mDm.setVirtualDisplayState(token, isOn);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public Point getStableDisplaySize() {
        try {
            return this.mDm.getStableDisplaySize();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public List<BrightnessChangeEvent> getBrightnessEvents(String callingPackage) {
        try {
            ParceledListSlice<BrightnessChangeEvent> events = this.mDm.getBrightnessEvents(callingPackage);
            if (events == null) {
                return Collections.emptyList();
            }
            return events.getList();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public ColorSpace getPreferredWideGamutColorSpace() {
        return this.mWideColorSpace;
    }

    public void setBrightnessConfigurationForUser(BrightnessConfiguration c, int userId, String packageName) {
        try {
            this.mDm.setBrightnessConfigurationForUser(c, userId, packageName);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public BrightnessConfiguration getBrightnessConfigurationForUser(int userId) {
        try {
            return this.mDm.getBrightnessConfigurationForUser(userId);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public BrightnessConfiguration getDefaultBrightnessConfiguration() {
        try {
            return this.mDm.getDefaultBrightnessConfiguration();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setTemporaryBrightness(int brightness) {
        try {
            this.mDm.setTemporaryBrightness(brightness);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setTemporaryAutoBrightnessAdjustment(float adjustment) {
        try {
            this.mDm.setTemporaryAutoBrightnessAdjustment(adjustment);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public Pair<float[], float[]> getMinimumBrightnessCurve() {
        try {
            Curve curve = this.mDm.getMinimumBrightnessCurve();
            return Pair.create(curve.getX(), curve.getY());
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public List<AmbientBrightnessDayStats> getAmbientBrightnessStats() {
        try {
            ParceledListSlice<AmbientBrightnessDayStats> stats = this.mDm.getAmbientBrightnessStats();
            if (stats == null) {
                return Collections.emptyList();
            }
            return stats.getList();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }
}
