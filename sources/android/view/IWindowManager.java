package android.view;

import android.annotation.UnsupportedAppUsage;
import android.app.IAssistDataReceiver;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.internal.os.IResultReceiver;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IShortcutService;
import com.miui.internal.transition.IMiuiGestureControlHelper;

public interface IWindowManager extends IInterface {

    public static class Default implements IWindowManager {
        public boolean startViewServer(int port) throws RemoteException {
            return false;
        }

        public boolean stopViewServer() throws RemoteException {
            return false;
        }

        public boolean isViewServerRunning() throws RemoteException {
            return false;
        }

        public IWindowSession openSession(IWindowSessionCallback callback) throws RemoteException {
            return null;
        }

        public void getInitialDisplaySize(int displayId, Point size) throws RemoteException {
        }

        public void getBaseDisplaySize(int displayId, Point size) throws RemoteException {
        }

        public void setForcedDisplaySize(int displayId, int width, int height) throws RemoteException {
        }

        public void clearForcedDisplaySize(int displayId) throws RemoteException {
        }

        public int getInitialDisplayDensity(int displayId) throws RemoteException {
            return 0;
        }

        public int getBaseDisplayDensity(int displayId) throws RemoteException {
            return 0;
        }

        public void setForcedDisplayDensityForUser(int displayId, int density, int userId) throws RemoteException {
        }

        public void clearForcedDisplayDensityForUser(int displayId, int userId) throws RemoteException {
        }

        public void setForcedDisplayScalingMode(int displayId, int mode) throws RemoteException {
        }

        public void setOverscan(int displayId, int left, int top, int right, int bottom) throws RemoteException {
        }

        public void setEventDispatching(boolean enabled) throws RemoteException {
        }

        public void addWindowToken(IBinder token, int type, int displayId) throws RemoteException {
        }

        public void removeWindowToken(IBinder token, int displayId) throws RemoteException {
        }

        public void prepareAppTransition(int transit, boolean alwaysKeepCurrent) throws RemoteException {
        }

        public void overridePendingAppTransitionLaunchFromHome(int startX, int startY, int startWidth, int startHeight, int displayId) throws RemoteException {
        }

        public void overrideMiuiAnimSupportWinInset(Rect inset) throws RemoteException {
        }

        public void cancelMiuiThumbnailAnimation(int displayId) throws RemoteException {
        }

        public void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture specsFuture, IRemoteCallback startedCallback, boolean scaleUp, int displayId) throws RemoteException {
        }

        public void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter, int displayId) throws RemoteException {
        }

        public void executeAppTransition() throws RemoteException {
        }

        public void endProlongedAnimations() throws RemoteException {
        }

        public void startFreezingScreen(int exitAnim, int enterAnim) throws RemoteException {
        }

        public void stopFreezingScreen() throws RemoteException {
        }

        public void disableKeyguard(IBinder token, String tag, int userId) throws RemoteException {
        }

        public void reenableKeyguard(IBinder token, int userId) throws RemoteException {
        }

        public void exitKeyguardSecurely(IOnKeyguardExitResult callback) throws RemoteException {
        }

        public boolean isKeyguardLocked() throws RemoteException {
            return false;
        }

        public boolean isKeyguardSecure(int userId) throws RemoteException {
            return false;
        }

        public void dismissKeyguard(IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
        }

        public void setSwitchingUser(boolean switching) throws RemoteException {
        }

        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        public float getAnimationScale(int which) throws RemoteException {
            return 0.0f;
        }

        public float[] getAnimationScales() throws RemoteException {
            return null;
        }

        public void setAnimationScale(int which, float scale) throws RemoteException {
        }

        public void setAnimationScales(float[] scales) throws RemoteException {
        }

        public float getCurrentAnimatorScale() throws RemoteException {
            return 0.0f;
        }

        public void setInTouchMode(boolean showFocus) throws RemoteException {
        }

        public void showStrictModeViolation(boolean on) throws RemoteException {
        }

        public void setStrictModeVisualIndicatorPreference(String enabled) throws RemoteException {
        }

        public void refreshScreenCaptureDisabled(int userId) throws RemoteException {
        }

        public void updateRotation(boolean alwaysSendConfiguration, boolean forceRelayout) throws RemoteException {
        }

        public int getDefaultDisplayRotation() throws RemoteException {
            return 0;
        }

        public int watchRotation(IRotationWatcher watcher, int displayId) throws RemoteException {
            return 0;
        }

        public void removeRotationWatcher(IRotationWatcher watcher) throws RemoteException {
        }

        public int getPreferredOptionsPanelGravity(int displayId) throws RemoteException {
            return 0;
        }

        public void freezeRotation(int rotation) throws RemoteException {
        }

        public void thawRotation() throws RemoteException {
        }

        public boolean isRotationFrozen() throws RemoteException {
            return false;
        }

        public void freezeDisplayRotation(int displayId, int rotation) throws RemoteException {
        }

        public void thawDisplayRotation(int displayId) throws RemoteException {
        }

        public boolean isDisplayRotationFrozen(int displayId) throws RemoteException {
            return false;
        }

        public Bitmap screenshotWallpaper() throws RemoteException {
            return null;
        }

        public boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener listener, int displayId) throws RemoteException {
            return false;
        }

        public void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener listener, int displayId) throws RemoteException {
        }

        public void registerSystemGestureExclusionListener(ISystemGestureExclusionListener listener, int displayId) throws RemoteException {
        }

        public void unregisterSystemGestureExclusionListener(ISystemGestureExclusionListener listener, int displayId) throws RemoteException {
        }

        public boolean requestAssistScreenshot(IAssistDataReceiver receiver) throws RemoteException {
            return false;
        }

        public void statusBarVisibilityChanged(int displayId, int visibility) throws RemoteException {
        }

        public void setForceShowSystemBars(boolean show) throws RemoteException {
        }

        public void setRecentsVisibility(boolean visible) throws RemoteException {
        }

        public void setPipVisibility(boolean visible) throws RemoteException {
        }

        public void setShelfHeight(boolean visible, int shelfHeight) throws RemoteException {
        }

        public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean enabled) throws RemoteException {
        }

        public boolean hasNavigationBar(int displayId) throws RemoteException {
            return false;
        }

        public int getNavBarPosition(int displayId) throws RemoteException {
            return 0;
        }

        public void lockNow(Bundle options) throws RemoteException {
        }

        public boolean isSafeModeEnabled() throws RemoteException {
            return false;
        }

        public void enableScreenIfNeeded() throws RemoteException {
        }

        public boolean clearWindowContentFrameStats(IBinder token) throws RemoteException {
            return false;
        }

        public WindowContentFrameStats getWindowContentFrameStats(IBinder token) throws RemoteException {
            return null;
        }

        public int getDockedStackSide() throws RemoteException {
            return 0;
        }

        public void setDockedStackDividerTouchRegion(Rect touchableRegion) throws RemoteException {
        }

        public void registerDockedStackListener(IDockedStackListener listener) throws RemoteException {
        }

        public void registerPinnedStackListener(int displayId, IPinnedStackListener listener) throws RemoteException {
        }

        public void setResizeDimLayer(boolean visible, int targetWindowingMode, float alpha) throws RemoteException {
        }

        public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
        }

        public void getStableInsets(int displayId, Rect outInsets) throws RemoteException {
        }

        public void setForwardedInsets(int displayId, Insets insets) throws RemoteException {
        }

        public void registerShortcutKey(long shortcutCode, IShortcutService keySubscriber) throws RemoteException {
        }

        public void createInputConsumer(IBinder token, String name, int displayId, InputChannel inputChannel) throws RemoteException {
        }

        public boolean destroyInputConsumer(String name, int displayId) throws RemoteException {
            return false;
        }

        public Region getCurrentImeTouchRegion() throws RemoteException {
            return null;
        }

        public void registerDisplayFoldListener(IDisplayFoldListener listener) throws RemoteException {
        }

        public void unregisterDisplayFoldListener(IDisplayFoldListener listener) throws RemoteException {
        }

        public void startWindowTrace() throws RemoteException {
        }

        public void stopWindowTrace() throws RemoteException {
        }

        public boolean isWindowTraceEnabled() throws RemoteException {
            return false;
        }

        public void requestUserActivityNotification() throws RemoteException {
        }

        public void dontOverrideDisplayInfo(int displayId) throws RemoteException {
        }

        public int getWindowingMode(int displayId) throws RemoteException {
            return 0;
        }

        public void setWindowingMode(int displayId, int mode) throws RemoteException {
        }

        public int getRemoveContentMode(int displayId) throws RemoteException {
            return 0;
        }

        public void setRemoveContentMode(int displayId, int mode) throws RemoteException {
        }

        public boolean shouldShowWithInsecureKeyguard(int displayId) throws RemoteException {
            return false;
        }

        public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) throws RemoteException {
        }

        public boolean shouldShowSystemDecors(int displayId) throws RemoteException {
            return false;
        }

        public void setShouldShowSystemDecors(int displayId, boolean shouldShow) throws RemoteException {
        }

        public boolean shouldShowIme(int displayId) throws RemoteException {
            return false;
        }

        public void setShouldShowIme(int displayId, boolean shouldShow) throws RemoteException {
        }

        public boolean injectInputAfterTransactionsApplied(InputEvent ev, int mode) throws RemoteException {
            return false;
        }

        public void syncInputTransactions() throws RemoteException {
        }

        public void registerMiuiGestureControlHelper(IMiuiGestureControlHelper helper) throws RemoteException {
        }

        public void unregisterMiuiGestureControlHelper() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWindowManager {
        private static final String DESCRIPTOR = "android.view.IWindowManager";
        static final int TRANSACTION_addWindowToken = 16;
        static final int TRANSACTION_cancelMiuiThumbnailAnimation = 21;
        static final int TRANSACTION_clearForcedDisplayDensityForUser = 12;
        static final int TRANSACTION_clearForcedDisplaySize = 8;
        static final int TRANSACTION_clearWindowContentFrameStats = 73;
        static final int TRANSACTION_closeSystemDialogs = 35;
        static final int TRANSACTION_createInputConsumer = 84;
        static final int TRANSACTION_destroyInputConsumer = 85;
        static final int TRANSACTION_disableKeyguard = 28;
        static final int TRANSACTION_dismissKeyguard = 33;
        static final int TRANSACTION_dontOverrideDisplayInfo = 93;
        static final int TRANSACTION_enableScreenIfNeeded = 72;
        static final int TRANSACTION_endProlongedAnimations = 25;
        static final int TRANSACTION_executeAppTransition = 24;
        static final int TRANSACTION_exitKeyguardSecurely = 30;
        static final int TRANSACTION_freezeDisplayRotation = 53;
        static final int TRANSACTION_freezeRotation = 50;
        static final int TRANSACTION_getAnimationScale = 36;
        static final int TRANSACTION_getAnimationScales = 37;
        static final int TRANSACTION_getBaseDisplayDensity = 10;
        static final int TRANSACTION_getBaseDisplaySize = 6;
        static final int TRANSACTION_getCurrentAnimatorScale = 40;
        static final int TRANSACTION_getCurrentImeTouchRegion = 86;
        static final int TRANSACTION_getDefaultDisplayRotation = 46;
        static final int TRANSACTION_getDockedStackSide = 75;
        static final int TRANSACTION_getInitialDisplayDensity = 9;
        static final int TRANSACTION_getInitialDisplaySize = 5;
        static final int TRANSACTION_getNavBarPosition = 69;
        static final int TRANSACTION_getPreferredOptionsPanelGravity = 49;
        static final int TRANSACTION_getRemoveContentMode = 96;
        static final int TRANSACTION_getStableInsets = 81;
        static final int TRANSACTION_getWindowContentFrameStats = 74;
        static final int TRANSACTION_getWindowingMode = 94;
        static final int TRANSACTION_hasNavigationBar = 68;
        static final int TRANSACTION_injectInputAfterTransactionsApplied = 104;
        static final int TRANSACTION_isDisplayRotationFrozen = 55;
        static final int TRANSACTION_isKeyguardLocked = 31;
        static final int TRANSACTION_isKeyguardSecure = 32;
        static final int TRANSACTION_isRotationFrozen = 52;
        static final int TRANSACTION_isSafeModeEnabled = 71;
        static final int TRANSACTION_isViewServerRunning = 3;
        static final int TRANSACTION_isWindowTraceEnabled = 91;
        static final int TRANSACTION_lockNow = 70;
        static final int TRANSACTION_openSession = 4;
        static final int TRANSACTION_overrideMiuiAnimSupportWinInset = 20;
        static final int TRANSACTION_overridePendingAppTransitionLaunchFromHome = 19;
        static final int TRANSACTION_overridePendingAppTransitionMultiThumbFuture = 22;
        static final int TRANSACTION_overridePendingAppTransitionRemote = 23;
        static final int TRANSACTION_prepareAppTransition = 18;
        static final int TRANSACTION_reenableKeyguard = 29;
        static final int TRANSACTION_refreshScreenCaptureDisabled = 44;
        static final int TRANSACTION_registerDisplayFoldListener = 87;
        static final int TRANSACTION_registerDockedStackListener = 77;
        static final int TRANSACTION_registerMiuiGestureControlHelper = 106;
        static final int TRANSACTION_registerPinnedStackListener = 78;
        static final int TRANSACTION_registerShortcutKey = 83;
        static final int TRANSACTION_registerSystemGestureExclusionListener = 59;
        static final int TRANSACTION_registerWallpaperVisibilityListener = 57;
        static final int TRANSACTION_removeRotationWatcher = 48;
        static final int TRANSACTION_removeWindowToken = 17;
        static final int TRANSACTION_requestAppKeyboardShortcuts = 80;
        static final int TRANSACTION_requestAssistScreenshot = 61;
        static final int TRANSACTION_requestUserActivityNotification = 92;
        static final int TRANSACTION_screenshotWallpaper = 56;
        static final int TRANSACTION_setAnimationScale = 38;
        static final int TRANSACTION_setAnimationScales = 39;
        static final int TRANSACTION_setDockedStackDividerTouchRegion = 76;
        static final int TRANSACTION_setEventDispatching = 15;
        static final int TRANSACTION_setForceShowSystemBars = 63;
        static final int TRANSACTION_setForcedDisplayDensityForUser = 11;
        static final int TRANSACTION_setForcedDisplayScalingMode = 13;
        static final int TRANSACTION_setForcedDisplaySize = 7;
        static final int TRANSACTION_setForwardedInsets = 82;
        static final int TRANSACTION_setInTouchMode = 41;
        static final int TRANSACTION_setNavBarVirtualKeyHapticFeedbackEnabled = 67;
        static final int TRANSACTION_setOverscan = 14;
        static final int TRANSACTION_setPipVisibility = 65;
        static final int TRANSACTION_setRecentsVisibility = 64;
        static final int TRANSACTION_setRemoveContentMode = 97;
        static final int TRANSACTION_setResizeDimLayer = 79;
        static final int TRANSACTION_setShelfHeight = 66;
        static final int TRANSACTION_setShouldShowIme = 103;
        static final int TRANSACTION_setShouldShowSystemDecors = 101;
        static final int TRANSACTION_setShouldShowWithInsecureKeyguard = 99;
        static final int TRANSACTION_setStrictModeVisualIndicatorPreference = 43;
        static final int TRANSACTION_setSwitchingUser = 34;
        static final int TRANSACTION_setWindowingMode = 95;
        static final int TRANSACTION_shouldShowIme = 102;
        static final int TRANSACTION_shouldShowSystemDecors = 100;
        static final int TRANSACTION_shouldShowWithInsecureKeyguard = 98;
        static final int TRANSACTION_showStrictModeViolation = 42;
        static final int TRANSACTION_startFreezingScreen = 26;
        static final int TRANSACTION_startViewServer = 1;
        static final int TRANSACTION_startWindowTrace = 89;
        static final int TRANSACTION_statusBarVisibilityChanged = 62;
        static final int TRANSACTION_stopFreezingScreen = 27;
        static final int TRANSACTION_stopViewServer = 2;
        static final int TRANSACTION_stopWindowTrace = 90;
        static final int TRANSACTION_syncInputTransactions = 105;
        static final int TRANSACTION_thawDisplayRotation = 54;
        static final int TRANSACTION_thawRotation = 51;
        static final int TRANSACTION_unregisterDisplayFoldListener = 88;
        static final int TRANSACTION_unregisterMiuiGestureControlHelper = 107;
        static final int TRANSACTION_unregisterSystemGestureExclusionListener = 60;
        static final int TRANSACTION_unregisterWallpaperVisibilityListener = 58;
        static final int TRANSACTION_updateRotation = 45;
        static final int TRANSACTION_watchRotation = 47;

        private static class Proxy implements IWindowManager {
            public static IWindowManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean startViewServer(int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(port);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().startViewServer(port);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopViewServer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopViewServer();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isViewServerRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isViewServerRunning();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IWindowSession openSession(IWindowSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IWindowSession iWindowSession = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        iWindowSession = Stub.getDefaultImpl();
                        if (iWindowSession != 0) {
                            iWindowSession = Stub.getDefaultImpl().openSession(callback);
                            return iWindowSession;
                        }
                    }
                    _reply.readException();
                    iWindowSession = android.view.IWindowSession.Stub.asInterface(_reply.readStrongBinder());
                    IWindowSession _result = iWindowSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getInitialDisplaySize(int displayId, Point size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            size.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getInitialDisplaySize(displayId, size);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getBaseDisplaySize(int displayId, Point size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            size.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getBaseDisplaySize(displayId, size);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplaySize(int displayId, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForcedDisplaySize(displayId, width, height);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearForcedDisplaySize(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearForcedDisplaySize(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInitialDisplayDensity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getInitialDisplayDensity(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBaseDisplayDensity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getBaseDisplayDensity(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplayDensityForUser(int displayId, int density, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(density);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForcedDisplayDensityForUser(displayId, density, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearForcedDisplayDensityForUser(int displayId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearForcedDisplayDensityForUser(displayId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplayScalingMode(int displayId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForcedDisplayScalingMode(displayId, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOverscan(int displayId, int left, int top, int right, int bottom) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(left);
                    _data.writeInt(top);
                    _data.writeInt(right);
                    _data.writeInt(bottom);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOverscan(displayId, left, top, right, bottom);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEventDispatching(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setEventDispatching(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addWindowToken(IBinder token, int type, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addWindowToken(token, type, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeWindowToken(IBinder token, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeWindowToken(token, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareAppTransition(int transit, boolean alwaysKeepCurrent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(transit);
                    _data.writeInt(alwaysKeepCurrent ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareAppTransition(transit, alwaysKeepCurrent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionLaunchFromHome(int startX, int startY, int startWidth, int startHeight, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startX);
                    _data.writeInt(startY);
                    _data.writeInt(startWidth);
                    _data.writeInt(startHeight);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overridePendingAppTransitionLaunchFromHome(startX, startY, startWidth, startHeight, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overrideMiuiAnimSupportWinInset(Rect inset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (inset != null) {
                        _data.writeInt(1);
                        inset.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overrideMiuiAnimSupportWinInset(inset);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelMiuiThumbnailAnimation(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelMiuiThumbnailAnimation(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture specsFuture, IRemoteCallback startedCallback, boolean scaleUp, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(specsFuture != null ? specsFuture.asBinder() : null);
                    if (startedCallback != null) {
                        iBinder = startedCallback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(scaleUp ? 1 : 0);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overridePendingAppTransitionMultiThumbFuture(specsFuture, startedCallback, scaleUp, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (remoteAnimationAdapter != null) {
                        _data.writeInt(1);
                        remoteAnimationAdapter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overridePendingAppTransitionRemote(remoteAnimationAdapter, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void executeAppTransition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().executeAppTransition();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endProlongedAnimations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endProlongedAnimations();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startFreezingScreen(int exitAnim, int enterAnim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(exitAnim);
                    _data.writeInt(enterAnim);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startFreezingScreen(exitAnim, enterAnim);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopFreezingScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopFreezingScreen();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableKeyguard(IBinder token, String tag, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(tag);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableKeyguard(token, tag, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reenableKeyguard(IBinder token, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reenableKeyguard(token, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void exitKeyguardSecurely(IOnKeyguardExitResult callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().exitKeyguardSecurely(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isKeyguardLocked() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isKeyguardLocked();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isKeyguardSecure(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isKeyguardSecure(userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismissKeyguard(IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dismissKeyguard(callback, message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSwitchingUser(boolean switching) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(switching ? 1 : 0);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSwitchingUser(switching);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeSystemDialogs(reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getAnimationScale(int which) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    float f = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getAnimationScale(which);
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float[] getAnimationScales() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    float[] fArr = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        fArr = Stub.getDefaultImpl();
                        if (fArr != 0) {
                            fArr = Stub.getDefaultImpl().getAnimationScales();
                            return fArr;
                        }
                    }
                    _reply.readException();
                    fArr = _reply.createFloatArray();
                    float[] _result = fArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAnimationScale(int which, float scale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeFloat(scale);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAnimationScale(which, scale);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAnimationScales(float[] scales) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloatArray(scales);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAnimationScales(scales);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getCurrentAnimatorScale() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    float f = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getCurrentAnimatorScale();
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInTouchMode(boolean showFocus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showFocus ? 1 : 0);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInTouchMode(showFocus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showStrictModeViolation(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showStrictModeViolation(on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStrictModeVisualIndicatorPreference(String enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(enabled);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStrictModeVisualIndicatorPreference(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshScreenCaptureDisabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().refreshScreenCaptureDisabled(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateRotation(boolean alwaysSendConfiguration, boolean forceRelayout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(alwaysSendConfiguration ? 1 : 0);
                    if (!forceRelayout) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateRotation(alwaysSendConfiguration, forceRelayout);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDefaultDisplayRotation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 46;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultDisplayRotation();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int watchRotation(IRotationWatcher watcher, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(displayId);
                    int i = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().watchRotation(watcher, displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRotationWatcher(IRotationWatcher watcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeRotationWatcher(watcher);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredOptionsPanelGravity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 49;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredOptionsPanelGravity(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freezeRotation(int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rotation);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().freezeRotation(rotation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void thawRotation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().thawRotation();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRotationFrozen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRotationFrozen();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freezeDisplayRotation(int displayId, int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(rotation);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().freezeDisplayRotation(displayId, rotation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void thawDisplayRotation(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().thawDisplayRotation(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDisplayRotationFrozen(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDisplayRotationFrozen(displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap screenshotWallpaper() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bitmap bitmap = 56;
                    if (!this.mRemote.transact(56, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != 0) {
                            bitmap = Stub.getDefaultImpl().screenshotWallpaper();
                            return bitmap;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        bitmap = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bitmap;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener listener, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().registerWallpaperVisibilityListener(listener, displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener listener, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterWallpaperVisibilityListener(listener, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerSystemGestureExclusionListener(ISystemGestureExclusionListener listener, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerSystemGestureExclusionListener(listener, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterSystemGestureExclusionListener(ISystemGestureExclusionListener listener, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterSystemGestureExclusionListener(listener, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestAssistScreenshot(IAssistDataReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().requestAssistScreenshot(receiver);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void statusBarVisibilityChanged(int displayId, int visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(visibility);
                    if (this.mRemote.transact(62, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().statusBarVisibilityChanged(displayId, visibility);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setForceShowSystemBars(boolean show) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(show ? 1 : 0);
                    if (this.mRemote.transact(63, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setForceShowSystemBars(show);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setRecentsVisibility(boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(64, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setRecentsVisibility(visible);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setPipVisibility(boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(65, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPipVisibility(visible);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setShelfHeight(boolean visible, int shelfHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    _data.writeInt(shelfHeight);
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShelfHeight(visible, shelfHeight);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNavBarVirtualKeyHapticFeedbackEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasNavigationBar(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasNavigationBar(displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNavBarPosition(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNavBarPosition(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void lockNow(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().lockNow(options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSafeModeEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSafeModeEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableScreenIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableScreenIfNeeded();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean clearWindowContentFrameStats(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().clearWindowContentFrameStats(token);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WindowContentFrameStats getWindowContentFrameStats(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    WindowContentFrameStats windowContentFrameStats = 74;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        windowContentFrameStats = Stub.getDefaultImpl();
                        if (windowContentFrameStats != 0) {
                            windowContentFrameStats = Stub.getDefaultImpl().getWindowContentFrameStats(token);
                            return windowContentFrameStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        windowContentFrameStats = (WindowContentFrameStats) WindowContentFrameStats.CREATOR.createFromParcel(_reply);
                    } else {
                        windowContentFrameStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return windowContentFrameStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDockedStackSide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDockedStackSide();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDockedStackDividerTouchRegion(Rect touchableRegion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (touchableRegion != null) {
                        _data.writeInt(1);
                        touchableRegion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDockedStackDividerTouchRegion(touchableRegion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerDockedStackListener(IDockedStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerDockedStackListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPinnedStackListener(int displayId, IPinnedStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerPinnedStackListener(displayId, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setResizeDimLayer(boolean visible, int targetWindowingMode, float alpha) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    _data.writeInt(targetWindowingMode);
                    _data.writeFloat(alpha);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setResizeDimLayer(visible, targetWindowingMode, alpha);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestAppKeyboardShortcuts(receiver, deviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getStableInsets(int displayId, Rect outInsets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            outInsets.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getStableInsets(displayId, outInsets);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForwardedInsets(int displayId, Insets insets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (insets != null) {
                        _data.writeInt(1);
                        insets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForwardedInsets(displayId, insets);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerShortcutKey(long shortcutCode, IShortcutService keySubscriber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(shortcutCode);
                    _data.writeStrongBinder(keySubscriber != null ? keySubscriber.asBinder() : null);
                    if (this.mRemote.transact(83, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerShortcutKey(shortcutCode, keySubscriber);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createInputConsumer(IBinder token, String name, int displayId, InputChannel inputChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(name);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            inputChannel.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createInputConsumer(token, name, displayId, inputChannel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean destroyInputConsumer(String name, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().destroyInputConsumer(name, displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Region getCurrentImeTouchRegion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Region region = 86;
                    if (!this.mRemote.transact(86, _data, _reply, 0)) {
                        region = Stub.getDefaultImpl();
                        if (region != 0) {
                            region = Stub.getDefaultImpl().getCurrentImeTouchRegion();
                            return region;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        region = (Region) Region.CREATOR.createFromParcel(_reply);
                    } else {
                        region = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return region;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerDisplayFoldListener(IDisplayFoldListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerDisplayFoldListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterDisplayFoldListener(IDisplayFoldListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterDisplayFoldListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startWindowTrace() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startWindowTrace();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWindowTrace() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(90, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWindowTrace();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWindowTraceEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(91, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWindowTraceEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestUserActivityNotification() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestUserActivityNotification();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dontOverrideDisplayInfo(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(93, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dontOverrideDisplayInfo(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWindowingMode(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 94;
                    if (!this.mRemote.transact(94, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWindowingMode(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWindowingMode(int displayId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(95, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWindowingMode(displayId, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRemoveContentMode(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 96;
                    if (!this.mRemote.transact(96, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRemoveContentMode(displayId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRemoveContentMode(int displayId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(97, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRemoveContentMode(displayId, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldShowWithInsecureKeyguard(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(98, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldShowWithInsecureKeyguard(displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(shouldShow ? 1 : 0);
                    if (this.mRemote.transact(99, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShouldShowWithInsecureKeyguard(displayId, shouldShow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldShowSystemDecors(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(100, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldShowSystemDecors(displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShouldShowSystemDecors(int displayId, boolean shouldShow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(shouldShow ? 1 : 0);
                    if (this.mRemote.transact(101, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShouldShowSystemDecors(displayId, shouldShow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldShowIme(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(102, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldShowIme(displayId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShouldShowIme(int displayId, boolean shouldShow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(shouldShow ? 1 : 0);
                    if (this.mRemote.transact(103, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShouldShowIme(displayId, shouldShow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean injectInputAfterTransactionsApplied(InputEvent ev, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (ev != null) {
                        _data.writeInt(1);
                        ev.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    if (this.mRemote.transact(104, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().injectInputAfterTransactionsApplied(ev, mode);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void syncInputTransactions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(105, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().syncInputTransactions();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerMiuiGestureControlHelper(IMiuiGestureControlHelper helper) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(helper != null ? helper.asBinder() : null);
                    if (this.mRemote.transact(106, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerMiuiGestureControlHelper(helper);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterMiuiGestureControlHelper() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(107, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterMiuiGestureControlHelper();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindowManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWindowManager)) {
                return new Proxy(obj);
            }
            return (IWindowManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startViewServer";
                case 2:
                    return "stopViewServer";
                case 3:
                    return "isViewServerRunning";
                case 4:
                    return "openSession";
                case 5:
                    return "getInitialDisplaySize";
                case 6:
                    return "getBaseDisplaySize";
                case 7:
                    return "setForcedDisplaySize";
                case 8:
                    return "clearForcedDisplaySize";
                case 9:
                    return "getInitialDisplayDensity";
                case 10:
                    return "getBaseDisplayDensity";
                case 11:
                    return "setForcedDisplayDensityForUser";
                case 12:
                    return "clearForcedDisplayDensityForUser";
                case 13:
                    return "setForcedDisplayScalingMode";
                case 14:
                    return "setOverscan";
                case 15:
                    return "setEventDispatching";
                case 16:
                    return "addWindowToken";
                case 17:
                    return "removeWindowToken";
                case 18:
                    return "prepareAppTransition";
                case 19:
                    return "overridePendingAppTransitionLaunchFromHome";
                case 20:
                    return "overrideMiuiAnimSupportWinInset";
                case 21:
                    return "cancelMiuiThumbnailAnimation";
                case 22:
                    return "overridePendingAppTransitionMultiThumbFuture";
                case 23:
                    return "overridePendingAppTransitionRemote";
                case 24:
                    return "executeAppTransition";
                case 25:
                    return "endProlongedAnimations";
                case 26:
                    return "startFreezingScreen";
                case 27:
                    return "stopFreezingScreen";
                case 28:
                    return "disableKeyguard";
                case 29:
                    return "reenableKeyguard";
                case 30:
                    return "exitKeyguardSecurely";
                case 31:
                    return "isKeyguardLocked";
                case 32:
                    return "isKeyguardSecure";
                case 33:
                    return "dismissKeyguard";
                case 34:
                    return "setSwitchingUser";
                case 35:
                    return "closeSystemDialogs";
                case 36:
                    return "getAnimationScale";
                case 37:
                    return "getAnimationScales";
                case 38:
                    return "setAnimationScale";
                case 39:
                    return "setAnimationScales";
                case 40:
                    return "getCurrentAnimatorScale";
                case 41:
                    return "setInTouchMode";
                case 42:
                    return "showStrictModeViolation";
                case 43:
                    return "setStrictModeVisualIndicatorPreference";
                case 44:
                    return "refreshScreenCaptureDisabled";
                case 45:
                    return "updateRotation";
                case 46:
                    return "getDefaultDisplayRotation";
                case 47:
                    return "watchRotation";
                case 48:
                    return "removeRotationWatcher";
                case 49:
                    return "getPreferredOptionsPanelGravity";
                case 50:
                    return "freezeRotation";
                case 51:
                    return "thawRotation";
                case 52:
                    return "isRotationFrozen";
                case 53:
                    return "freezeDisplayRotation";
                case 54:
                    return "thawDisplayRotation";
                case 55:
                    return "isDisplayRotationFrozen";
                case 56:
                    return "screenshotWallpaper";
                case 57:
                    return "registerWallpaperVisibilityListener";
                case 58:
                    return "unregisterWallpaperVisibilityListener";
                case 59:
                    return "registerSystemGestureExclusionListener";
                case 60:
                    return "unregisterSystemGestureExclusionListener";
                case 61:
                    return "requestAssistScreenshot";
                case 62:
                    return "statusBarVisibilityChanged";
                case 63:
                    return "setForceShowSystemBars";
                case 64:
                    return "setRecentsVisibility";
                case 65:
                    return "setPipVisibility";
                case 66:
                    return "setShelfHeight";
                case 67:
                    return "setNavBarVirtualKeyHapticFeedbackEnabled";
                case 68:
                    return "hasNavigationBar";
                case 69:
                    return "getNavBarPosition";
                case 70:
                    return "lockNow";
                case 71:
                    return "isSafeModeEnabled";
                case 72:
                    return "enableScreenIfNeeded";
                case 73:
                    return "clearWindowContentFrameStats";
                case 74:
                    return "getWindowContentFrameStats";
                case 75:
                    return "getDockedStackSide";
                case 76:
                    return "setDockedStackDividerTouchRegion";
                case 77:
                    return "registerDockedStackListener";
                case 78:
                    return "registerPinnedStackListener";
                case 79:
                    return "setResizeDimLayer";
                case 80:
                    return "requestAppKeyboardShortcuts";
                case 81:
                    return "getStableInsets";
                case 82:
                    return "setForwardedInsets";
                case 83:
                    return "registerShortcutKey";
                case 84:
                    return "createInputConsumer";
                case 85:
                    return "destroyInputConsumer";
                case 86:
                    return "getCurrentImeTouchRegion";
                case 87:
                    return "registerDisplayFoldListener";
                case 88:
                    return "unregisterDisplayFoldListener";
                case 89:
                    return "startWindowTrace";
                case 90:
                    return "stopWindowTrace";
                case 91:
                    return "isWindowTraceEnabled";
                case 92:
                    return "requestUserActivityNotification";
                case 93:
                    return "dontOverrideDisplayInfo";
                case 94:
                    return "getWindowingMode";
                case 95:
                    return "setWindowingMode";
                case 96:
                    return "getRemoveContentMode";
                case 97:
                    return "setRemoveContentMode";
                case 98:
                    return "shouldShowWithInsecureKeyguard";
                case 99:
                    return "setShouldShowWithInsecureKeyguard";
                case 100:
                    return "shouldShowSystemDecors";
                case 101:
                    return "setShouldShowSystemDecors";
                case 102:
                    return "shouldShowIme";
                case 103:
                    return "setShouldShowIme";
                case 104:
                    return "injectInputAfterTransactionsApplied";
                case 105:
                    return "syncInputTransactions";
                case 106:
                    return "registerMiuiGestureControlHelper";
                case 107:
                    return "unregisterMiuiGestureControlHelper";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg1 = false;
                boolean _result;
                int _arg0;
                Point _arg12;
                int _result2;
                Rect _arg02;
                int _result3;
                boolean _result4;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = startViewServer(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg1 = stopViewServer();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isViewServerRunning();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        IWindowSession _result5 = openSession(android.view.IWindowSessionCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = new Point();
                        getInitialDisplaySize(_arg0, _arg12);
                        reply.writeNoException();
                        parcel2.writeInt(1);
                        _arg12.writeToParcel(parcel2, 1);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = new Point();
                        getBaseDisplaySize(_arg0, _arg12);
                        reply.writeNoException();
                        parcel2.writeInt(1);
                        _arg12.writeToParcel(parcel2, 1);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        setForcedDisplaySize(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        clearForcedDisplaySize(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result2 = getInitialDisplayDensity(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result2 = getBaseDisplayDensity(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        setForcedDisplayDensityForUser(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        clearForcedDisplayDensityForUser(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        setForcedDisplayScalingMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        setOverscan(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setEventDispatching(_arg1);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        addWindowToken(data.readStrongBinder(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        removeWindowToken(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        prepareAppTransition(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        overridePendingAppTransitionLaunchFromHome(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        overrideMiuiAnimSupportWinInset(_arg02);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        cancelMiuiThumbnailAnimation(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        IAppTransitionAnimationSpecsFuture _arg03 = android.view.IAppTransitionAnimationSpecsFuture.Stub.asInterface(data.readStrongBinder());
                        IRemoteCallback _arg13 = android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        overridePendingAppTransitionMultiThumbFuture(_arg03, _arg13, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        RemoteAnimationAdapter _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (RemoteAnimationAdapter) RemoteAnimationAdapter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        overridePendingAppTransitionRemote(_arg04, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        executeAppTransition();
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        endProlongedAnimations();
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        startFreezingScreen(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        stopFreezingScreen();
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        disableKeyguard(data.readStrongBinder(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        reenableKeyguard(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        exitKeyguardSecurely(android.view.IOnKeyguardExitResult.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isKeyguardLocked();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result = isKeyguardSecure(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 33:
                        CharSequence _arg14;
                        parcel.enforceInterface(descriptor);
                        IKeyguardDismissCallback _arg05 = com.android.internal.policy.IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        dismissKeyguard(_arg05, _arg14);
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setSwitchingUser(_arg1);
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        closeSystemDialogs(data.readString());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        float _result6 = getAnimationScale(data.readInt());
                        reply.writeNoException();
                        parcel2.writeFloat(_result6);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        float[] _result7 = getAnimationScales();
                        reply.writeNoException();
                        parcel2.writeFloatArray(_result7);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        setAnimationScale(data.readInt(), data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        setAnimationScales(data.createFloatArray());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        float _result8 = getCurrentAnimatorScale();
                        reply.writeNoException();
                        parcel2.writeFloat(_result8);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setInTouchMode(_arg1);
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        showStrictModeViolation(_arg1);
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        setStrictModeVisualIndicatorPreference(data.readString());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        refreshScreenCaptureDisabled(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        updateRotation(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getDefaultDisplayRotation();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _result3 = watchRotation(android.view.IRotationWatcher.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        removeRotationWatcher(android.view.IRotationWatcher.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        _result2 = getPreferredOptionsPanelGravity(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        freezeRotation(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        thawRotation();
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isRotationFrozen();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        freezeDisplayRotation(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        thawDisplayRotation(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _result = isDisplayRotationFrozen(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        Bitmap _result9 = screenshotWallpaper();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        _result4 = registerWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        unregisterWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        registerSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        unregisterSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        _result = requestAssistScreenshot(android.app.IAssistDataReceiver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        statusBarVisibilityChanged(data.readInt(), data.readInt());
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setForceShowSystemBars(_arg1);
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setRecentsVisibility(_arg1);
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setPipVisibility(_arg1);
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setShelfHeight(_arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setNavBarVirtualKeyHapticFeedbackEnabled(_arg1);
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _result = hasNavigationBar(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _result2 = getNavBarPosition(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 70:
                        Bundle _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        lockNow(_arg06);
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isSafeModeEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        enableScreenIfNeeded();
                        reply.writeNoException();
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _result = clearWindowContentFrameStats(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        WindowContentFrameStats _result10 = getWindowContentFrameStats(data.readStrongBinder());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getDockedStackSide();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        setDockedStackDividerTouchRegion(_arg02);
                        reply.writeNoException();
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        registerDockedStackListener(android.view.IDockedStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        registerPinnedStackListener(data.readInt(), android.view.IPinnedStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setResizeDimLayer(_arg1, data.readInt(), data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        requestAppKeyboardShortcuts(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        Rect _arg15 = new Rect();
                        getStableInsets(_arg0, _arg15);
                        reply.writeNoException();
                        parcel2.writeInt(1);
                        _arg15.writeToParcel(parcel2, 1);
                        return true;
                    case 82:
                        Insets _arg16;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg16 = (Insets) Insets.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        setForwardedInsets(_arg0, _arg16);
                        reply.writeNoException();
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        registerShortcutKey(data.readLong(), com.android.internal.policy.IShortcutService.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        IBinder _arg07 = data.readStrongBinder();
                        String _arg17 = data.readString();
                        _result3 = data.readInt();
                        InputChannel _arg3 = new InputChannel();
                        createInputConsumer(_arg07, _arg17, _result3, _arg3);
                        reply.writeNoException();
                        parcel2.writeInt(1);
                        _arg3.writeToParcel(parcel2, 1);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        _result4 = destroyInputConsumer(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        Region _result11 = getCurrentImeTouchRegion();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        registerDisplayFoldListener(android.view.IDisplayFoldListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        unregisterDisplayFoldListener(android.view.IDisplayFoldListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        startWindowTrace();
                        reply.writeNoException();
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        stopWindowTrace();
                        reply.writeNoException();
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isWindowTraceEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        requestUserActivityNotification();
                        reply.writeNoException();
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        dontOverrideDisplayInfo(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        _result2 = getWindowingMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        setWindowingMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 96:
                        parcel.enforceInterface(descriptor);
                        _result2 = getRemoveContentMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 97:
                        parcel.enforceInterface(descriptor);
                        setRemoveContentMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 98:
                        parcel.enforceInterface(descriptor);
                        _result = shouldShowWithInsecureKeyguard(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setShouldShowWithInsecureKeyguard(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 100:
                        parcel.enforceInterface(descriptor);
                        _result = shouldShowSystemDecors(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 101:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setShouldShowSystemDecors(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 102:
                        parcel.enforceInterface(descriptor);
                        _result = shouldShowIme(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 103:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setShouldShowIme(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 104:
                        InputEvent _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (InputEvent) InputEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        _result4 = injectInputAfterTransactionsApplied(_arg08, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 105:
                        parcel.enforceInterface(descriptor);
                        syncInputTransactions();
                        reply.writeNoException();
                        return true;
                    case 106:
                        parcel.enforceInterface(descriptor);
                        registerMiuiGestureControlHelper(com.miui.internal.transition.IMiuiGestureControlHelper.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 107:
                        parcel.enforceInterface(descriptor);
                        unregisterMiuiGestureControlHelper();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWindowManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWindowManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addWindowToken(IBinder iBinder, int i, int i2) throws RemoteException;

    void cancelMiuiThumbnailAnimation(int i) throws RemoteException;

    void clearForcedDisplayDensityForUser(int i, int i2) throws RemoteException;

    void clearForcedDisplaySize(int i) throws RemoteException;

    boolean clearWindowContentFrameStats(IBinder iBinder) throws RemoteException;

    void closeSystemDialogs(String str) throws RemoteException;

    @UnsupportedAppUsage
    void createInputConsumer(IBinder iBinder, String str, int i, InputChannel inputChannel) throws RemoteException;

    @UnsupportedAppUsage
    boolean destroyInputConsumer(String str, int i) throws RemoteException;

    void disableKeyguard(IBinder iBinder, String str, int i) throws RemoteException;

    void dismissKeyguard(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    void dontOverrideDisplayInfo(int i) throws RemoteException;

    void enableScreenIfNeeded() throws RemoteException;

    @UnsupportedAppUsage
    void endProlongedAnimations() throws RemoteException;

    @UnsupportedAppUsage
    void executeAppTransition() throws RemoteException;

    void exitKeyguardSecurely(IOnKeyguardExitResult iOnKeyguardExitResult) throws RemoteException;

    void freezeDisplayRotation(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void freezeRotation(int i) throws RemoteException;

    @UnsupportedAppUsage
    float getAnimationScale(int i) throws RemoteException;

    @UnsupportedAppUsage
    float[] getAnimationScales() throws RemoteException;

    int getBaseDisplayDensity(int i) throws RemoteException;

    @UnsupportedAppUsage
    void getBaseDisplaySize(int i, Point point) throws RemoteException;

    float getCurrentAnimatorScale() throws RemoteException;

    Region getCurrentImeTouchRegion() throws RemoteException;

    int getDefaultDisplayRotation() throws RemoteException;

    @UnsupportedAppUsage
    int getDockedStackSide() throws RemoteException;

    @UnsupportedAppUsage
    int getInitialDisplayDensity(int i) throws RemoteException;

    @UnsupportedAppUsage
    void getInitialDisplaySize(int i, Point point) throws RemoteException;

    int getNavBarPosition(int i) throws RemoteException;

    int getPreferredOptionsPanelGravity(int i) throws RemoteException;

    int getRemoveContentMode(int i) throws RemoteException;

    @UnsupportedAppUsage
    void getStableInsets(int i, Rect rect) throws RemoteException;

    WindowContentFrameStats getWindowContentFrameStats(IBinder iBinder) throws RemoteException;

    int getWindowingMode(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasNavigationBar(int i) throws RemoteException;

    boolean injectInputAfterTransactionsApplied(InputEvent inputEvent, int i) throws RemoteException;

    boolean isDisplayRotationFrozen(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean isKeyguardLocked() throws RemoteException;

    @UnsupportedAppUsage
    boolean isKeyguardSecure(int i) throws RemoteException;

    boolean isRotationFrozen() throws RemoteException;

    @UnsupportedAppUsage
    boolean isSafeModeEnabled() throws RemoteException;

    boolean isViewServerRunning() throws RemoteException;

    boolean isWindowTraceEnabled() throws RemoteException;

    @UnsupportedAppUsage
    void lockNow(Bundle bundle) throws RemoteException;

    IWindowSession openSession(IWindowSessionCallback iWindowSessionCallback) throws RemoteException;

    void overrideMiuiAnimSupportWinInset(Rect rect) throws RemoteException;

    void overridePendingAppTransitionLaunchFromHome(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    @UnsupportedAppUsage
    void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture, IRemoteCallback iRemoteCallback, boolean z, int i) throws RemoteException;

    @UnsupportedAppUsage
    void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter, int i) throws RemoteException;

    void prepareAppTransition(int i, boolean z) throws RemoteException;

    void reenableKeyguard(IBinder iBinder, int i) throws RemoteException;

    void refreshScreenCaptureDisabled(int i) throws RemoteException;

    void registerDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) throws RemoteException;

    @UnsupportedAppUsage
    void registerDockedStackListener(IDockedStackListener iDockedStackListener) throws RemoteException;

    void registerMiuiGestureControlHelper(IMiuiGestureControlHelper iMiuiGestureControlHelper) throws RemoteException;

    void registerPinnedStackListener(int i, IPinnedStackListener iPinnedStackListener) throws RemoteException;

    void registerShortcutKey(long j, IShortcutService iShortcutService) throws RemoteException;

    void registerSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener, int i) throws RemoteException;

    boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) throws RemoteException;

    @UnsupportedAppUsage
    void removeRotationWatcher(IRotationWatcher iRotationWatcher) throws RemoteException;

    void removeWindowToken(IBinder iBinder, int i) throws RemoteException;

    void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) throws RemoteException;

    boolean requestAssistScreenshot(IAssistDataReceiver iAssistDataReceiver) throws RemoteException;

    void requestUserActivityNotification() throws RemoteException;

    Bitmap screenshotWallpaper() throws RemoteException;

    @UnsupportedAppUsage
    void setAnimationScale(int i, float f) throws RemoteException;

    @UnsupportedAppUsage
    void setAnimationScales(float[] fArr) throws RemoteException;

    void setDockedStackDividerTouchRegion(Rect rect) throws RemoteException;

    void setEventDispatching(boolean z) throws RemoteException;

    void setForceShowSystemBars(boolean z) throws RemoteException;

    void setForcedDisplayDensityForUser(int i, int i2, int i3) throws RemoteException;

    void setForcedDisplayScalingMode(int i, int i2) throws RemoteException;

    void setForcedDisplaySize(int i, int i2, int i3) throws RemoteException;

    void setForwardedInsets(int i, Insets insets) throws RemoteException;

    void setInTouchMode(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setNavBarVirtualKeyHapticFeedbackEnabled(boolean z) throws RemoteException;

    void setOverscan(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setPipVisibility(boolean z) throws RemoteException;

    void setRecentsVisibility(boolean z) throws RemoteException;

    void setRemoveContentMode(int i, int i2) throws RemoteException;

    void setResizeDimLayer(boolean z, int i, float f) throws RemoteException;

    @UnsupportedAppUsage
    void setShelfHeight(boolean z, int i) throws RemoteException;

    void setShouldShowIme(int i, boolean z) throws RemoteException;

    void setShouldShowSystemDecors(int i, boolean z) throws RemoteException;

    void setShouldShowWithInsecureKeyguard(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setStrictModeVisualIndicatorPreference(String str) throws RemoteException;

    void setSwitchingUser(boolean z) throws RemoteException;

    void setWindowingMode(int i, int i2) throws RemoteException;

    boolean shouldShowIme(int i) throws RemoteException;

    boolean shouldShowSystemDecors(int i) throws RemoteException;

    boolean shouldShowWithInsecureKeyguard(int i) throws RemoteException;

    void showStrictModeViolation(boolean z) throws RemoteException;

    void startFreezingScreen(int i, int i2) throws RemoteException;

    boolean startViewServer(int i) throws RemoteException;

    void startWindowTrace() throws RemoteException;

    void statusBarVisibilityChanged(int i, int i2) throws RemoteException;

    void stopFreezingScreen() throws RemoteException;

    boolean stopViewServer() throws RemoteException;

    void stopWindowTrace() throws RemoteException;

    void syncInputTransactions() throws RemoteException;

    void thawDisplayRotation(int i) throws RemoteException;

    @UnsupportedAppUsage
    void thawRotation() throws RemoteException;

    void unregisterDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) throws RemoteException;

    void unregisterMiuiGestureControlHelper() throws RemoteException;

    void unregisterSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener, int i) throws RemoteException;

    void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) throws RemoteException;

    void updateRotation(boolean z, boolean z2) throws RemoteException;

    int watchRotation(IRotationWatcher iRotationWatcher, int i) throws RemoteException;
}
