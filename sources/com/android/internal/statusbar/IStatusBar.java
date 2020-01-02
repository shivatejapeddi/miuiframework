package com.android.internal.statusbar;

import android.content.ComponentName;
import android.graphics.Rect;
import android.hardware.biometrics.IBiometricServiceReceiverInternal;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStatusBar extends IInterface {

    public static class Default implements IStatusBar {
        public void setStatus(int what, String action, Bundle ext) throws RemoteException {
        }

        public void setIcon(String slot, StatusBarIcon icon) throws RemoteException {
        }

        public void removeIcon(String slot) throws RemoteException {
        }

        public void disable(int displayId, int state1, int state2) throws RemoteException {
        }

        public void animateExpandNotificationsPanel() throws RemoteException {
        }

        public void animateExpandSettingsPanel(String subPanel) throws RemoteException {
        }

        public void animateCollapsePanels() throws RemoteException {
        }

        public void togglePanel() throws RemoteException {
        }

        public void showWirelessChargingAnimation(int batteryLevel) throws RemoteException {
        }

        public void setSystemUiVisibility(int displayId, int vis, int fullscreenStackVis, int dockedStackVis, int mask, Rect fullscreenBounds, Rect dockedBounds, boolean navbarColorManagedByIme) throws RemoteException {
        }

        public void topAppWindowChanged(int displayId, boolean menuVisible) throws RemoteException {
        }

        public void setImeWindowStatus(int displayId, IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
        }

        public void setWindowState(int display, int window, int state) throws RemoteException {
        }

        public void showRecentApps(boolean triggeredFromAltTab) throws RemoteException {
        }

        public void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws RemoteException {
        }

        public void toggleRecentApps() throws RemoteException {
        }

        public void toggleSplitScreen() throws RemoteException {
        }

        public void preloadRecentApps() throws RemoteException {
        }

        public void cancelPreloadRecentApps() throws RemoteException {
        }

        public void showScreenPinningRequest(int taskId) throws RemoteException {
        }

        public void dismissKeyboardShortcutsMenu() throws RemoteException {
        }

        public void toggleKeyboardShortcutsMenu(int deviceId) throws RemoteException {
        }

        public void appTransitionPending(int displayId) throws RemoteException {
        }

        public void appTransitionCancelled(int displayId) throws RemoteException {
        }

        public void appTransitionStarting(int displayId, long statusBarAnimationsStartTime, long statusBarAnimationsDuration) throws RemoteException {
        }

        public void appTransitionFinished(int displayId) throws RemoteException {
        }

        public void showAssistDisclosure() throws RemoteException {
        }

        public void startAssist(Bundle args) throws RemoteException {
        }

        public void onCameraLaunchGestureDetected(int source) throws RemoteException {
        }

        public void showPictureInPictureMenu() throws RemoteException {
        }

        public void showGlobalActionsMenu() throws RemoteException {
        }

        public void onProposedRotationChanged(int rotation, boolean isValid) throws RemoteException {
        }

        public void setTopAppHidesStatusBar(boolean hidesStatusBar) throws RemoteException {
        }

        public void addQsTile(ComponentName tile) throws RemoteException {
        }

        public void remQsTile(ComponentName tile) throws RemoteException {
        }

        public void clickQsTile(ComponentName tile) throws RemoteException {
        }

        public void handleSystemKey(int key) throws RemoteException {
        }

        public void showPinningEnterExitToast(boolean entering) throws RemoteException {
        }

        public void showPinningEscapeToast() throws RemoteException {
        }

        public void showShutdownUi(boolean isReboot, String reason) throws RemoteException {
        }

        public void showBiometricDialog(Bundle bundle, IBiometricServiceReceiverInternal receiver, int type, boolean requireConfirmation, int userId) throws RemoteException {
        }

        public void onBiometricAuthenticated(boolean authenticated, String failureReason) throws RemoteException {
        }

        public void onBiometricHelp(String message) throws RemoteException {
        }

        public void onBiometricError(String error) throws RemoteException {
        }

        public void hideBiometricDialog() throws RemoteException {
        }

        public void onDisplayReady(int displayId) throws RemoteException {
        }

        public void onRecentsAnimationStateChanged(boolean running) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStatusBar {
        private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBar";
        static final int TRANSACTION_addQsTile = 34;
        static final int TRANSACTION_animateCollapsePanels = 7;
        static final int TRANSACTION_animateExpandNotificationsPanel = 5;
        static final int TRANSACTION_animateExpandSettingsPanel = 6;
        static final int TRANSACTION_appTransitionCancelled = 24;
        static final int TRANSACTION_appTransitionFinished = 26;
        static final int TRANSACTION_appTransitionPending = 23;
        static final int TRANSACTION_appTransitionStarting = 25;
        static final int TRANSACTION_cancelPreloadRecentApps = 19;
        static final int TRANSACTION_clickQsTile = 36;
        static final int TRANSACTION_disable = 4;
        static final int TRANSACTION_dismissKeyboardShortcutsMenu = 21;
        static final int TRANSACTION_handleSystemKey = 37;
        static final int TRANSACTION_hideBiometricDialog = 45;
        static final int TRANSACTION_hideRecentApps = 15;
        static final int TRANSACTION_onBiometricAuthenticated = 42;
        static final int TRANSACTION_onBiometricError = 44;
        static final int TRANSACTION_onBiometricHelp = 43;
        static final int TRANSACTION_onCameraLaunchGestureDetected = 29;
        static final int TRANSACTION_onDisplayReady = 46;
        static final int TRANSACTION_onProposedRotationChanged = 32;
        static final int TRANSACTION_onRecentsAnimationStateChanged = 47;
        static final int TRANSACTION_preloadRecentApps = 18;
        static final int TRANSACTION_remQsTile = 35;
        static final int TRANSACTION_removeIcon = 3;
        static final int TRANSACTION_setIcon = 2;
        static final int TRANSACTION_setImeWindowStatus = 12;
        static final int TRANSACTION_setStatus = 1;
        static final int TRANSACTION_setSystemUiVisibility = 10;
        static final int TRANSACTION_setTopAppHidesStatusBar = 33;
        static final int TRANSACTION_setWindowState = 13;
        static final int TRANSACTION_showAssistDisclosure = 27;
        static final int TRANSACTION_showBiometricDialog = 41;
        static final int TRANSACTION_showGlobalActionsMenu = 31;
        static final int TRANSACTION_showPictureInPictureMenu = 30;
        static final int TRANSACTION_showPinningEnterExitToast = 38;
        static final int TRANSACTION_showPinningEscapeToast = 39;
        static final int TRANSACTION_showRecentApps = 14;
        static final int TRANSACTION_showScreenPinningRequest = 20;
        static final int TRANSACTION_showShutdownUi = 40;
        static final int TRANSACTION_showWirelessChargingAnimation = 9;
        static final int TRANSACTION_startAssist = 28;
        static final int TRANSACTION_toggleKeyboardShortcutsMenu = 22;
        static final int TRANSACTION_togglePanel = 8;
        static final int TRANSACTION_toggleRecentApps = 16;
        static final int TRANSACTION_toggleSplitScreen = 17;
        static final int TRANSACTION_topAppWindowChanged = 11;

        private static class Proxy implements IStatusBar {
            public static IStatusBar sDefaultImpl;
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

            public void setStatus(int what, String action, Bundle ext) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeString(action);
                    if (ext != null) {
                        _data.writeInt(1);
                        ext.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setStatus(what, action, ext);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setIcon(String slot, StatusBarIcon icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setIcon(slot, icon);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeIcon(String slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeIcon(slot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disable(int displayId, int state1, int state2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(state1);
                    _data.writeInt(state2);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disable(displayId, state1, state2);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void animateExpandNotificationsPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().animateExpandNotificationsPanel();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void animateExpandSettingsPanel(String subPanel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(subPanel);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().animateExpandSettingsPanel(subPanel);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void animateCollapsePanels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().animateCollapsePanels();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void togglePanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().togglePanel();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showWirelessChargingAnimation(int batteryLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(batteryLevel);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showWirelessChargingAnimation(batteryLevel);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSystemUiVisibility(int displayId, int vis, int fullscreenStackVis, int dockedStackVis, int mask, Rect fullscreenBounds, Rect dockedBounds, boolean navbarColorManagedByIme) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                Rect rect = fullscreenBounds;
                Rect rect2 = dockedBounds;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(displayId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = vis;
                        i2 = fullscreenStackVis;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(vis);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = fullscreenStackVis;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(fullscreenStackVis);
                        _data.writeInt(dockedStackVis);
                        _data.writeInt(mask);
                        int i3 = 0;
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (rect2 != null) {
                            _data.writeInt(1);
                            rect2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (navbarColorManagedByIme) {
                            i3 = 1;
                        }
                        _data.writeInt(i3);
                        if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().setSystemUiVisibility(displayId, vis, fullscreenStackVis, dockedStackVis, mask, fullscreenBounds, dockedBounds, navbarColorManagedByIme);
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i4 = displayId;
                    i = vis;
                    i2 = fullscreenStackVis;
                    _data.recycle();
                    throw th;
                }
            }

            public void topAppWindowChanged(int displayId, boolean menuVisible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(menuVisible ? 1 : 0);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().topAppWindowChanged(displayId, menuVisible);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setImeWindowStatus(int displayId, IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(token);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    _data.writeInt(showImeSwitcher ? 1 : 0);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setImeWindowStatus(displayId, token, vis, backDisposition, showImeSwitcher);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setWindowState(int display, int window, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(display);
                    _data.writeInt(window);
                    _data.writeInt(state);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setWindowState(display, window, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showRecentApps(boolean triggeredFromAltTab) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab ? 1 : 0);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showRecentApps(triggeredFromAltTab);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    _data.writeInt(triggeredFromAltTab ? 1 : 0);
                    if (triggeredFromHomeKey) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hideRecentApps(triggeredFromAltTab, triggeredFromHomeKey);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void toggleRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().toggleRecentApps();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void toggleSplitScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().toggleSplitScreen();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void preloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().preloadRecentApps();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancelPreloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelPreloadRecentApps();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showScreenPinningRequest(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showScreenPinningRequest(taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dismissKeyboardShortcutsMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dismissKeyboardShortcutsMenu();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void toggleKeyboardShortcutsMenu(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().toggleKeyboardShortcutsMenu(deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void appTransitionPending(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().appTransitionPending(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void appTransitionCancelled(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().appTransitionCancelled(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void appTransitionStarting(int displayId, long statusBarAnimationsStartTime, long statusBarAnimationsDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeLong(statusBarAnimationsStartTime);
                    _data.writeLong(statusBarAnimationsDuration);
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().appTransitionStarting(displayId, statusBarAnimationsStartTime, statusBarAnimationsDuration);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void appTransitionFinished(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().appTransitionFinished(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showAssistDisclosure() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showAssistDisclosure();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startAssist(Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startAssist(args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCameraLaunchGestureDetected(int source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCameraLaunchGestureDetected(source);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showPictureInPictureMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(30, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showPictureInPictureMenu();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showGlobalActionsMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(31, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showGlobalActionsMenu();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onProposedRotationChanged(int rotation, boolean isValid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rotation);
                    _data.writeInt(isValid ? 1 : 0);
                    if (this.mRemote.transact(32, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onProposedRotationChanged(rotation, isValid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setTopAppHidesStatusBar(boolean hidesStatusBar) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hidesStatusBar ? 1 : 0);
                    if (this.mRemote.transact(33, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setTopAppHidesStatusBar(hidesStatusBar);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addQsTile(tile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void remQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(35, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().remQsTile(tile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void clickQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().clickQsTile(tile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handleSystemKey(int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(key);
                    if (this.mRemote.transact(37, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handleSystemKey(key);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showPinningEnterExitToast(boolean entering) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(entering ? 1 : 0);
                    if (this.mRemote.transact(38, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showPinningEnterExitToast(entering);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showPinningEscapeToast() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(39, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showPinningEscapeToast();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showShutdownUi(boolean isReboot, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isReboot ? 1 : 0);
                    _data.writeString(reason);
                    if (this.mRemote.transact(40, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showShutdownUi(isReboot, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showBiometricDialog(Bundle bundle, IBiometricServiceReceiverInternal receiver, int type, boolean requireConfirmation, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(type);
                    if (requireConfirmation) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(41, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showBiometricDialog(bundle, receiver, type, requireConfirmation, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBiometricAuthenticated(boolean authenticated, String failureReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(authenticated ? 1 : 0);
                    _data.writeString(failureReason);
                    if (this.mRemote.transact(42, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBiometricAuthenticated(authenticated, failureReason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBiometricHelp(String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    if (this.mRemote.transact(43, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBiometricHelp(message);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBiometricError(String error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(error);
                    if (this.mRemote.transact(44, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBiometricError(error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hideBiometricDialog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(45, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hideBiometricDialog();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDisplayReady(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(46, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDisplayReady(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRecentsAnimationStateChanged(boolean running) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(running ? 1 : 0);
                    if (this.mRemote.transact(47, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRecentsAnimationStateChanged(running);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatusBar asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatusBar)) {
                return new Proxy(obj);
            }
            return (IStatusBar) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setStatus";
                case 2:
                    return "setIcon";
                case 3:
                    return "removeIcon";
                case 4:
                    return "disable";
                case 5:
                    return "animateExpandNotificationsPanel";
                case 6:
                    return "animateExpandSettingsPanel";
                case 7:
                    return "animateCollapsePanels";
                case 8:
                    return "togglePanel";
                case 9:
                    return "showWirelessChargingAnimation";
                case 10:
                    return "setSystemUiVisibility";
                case 11:
                    return "topAppWindowChanged";
                case 12:
                    return "setImeWindowStatus";
                case 13:
                    return "setWindowState";
                case 14:
                    return "showRecentApps";
                case 15:
                    return "hideRecentApps";
                case 16:
                    return "toggleRecentApps";
                case 17:
                    return "toggleSplitScreen";
                case 18:
                    return "preloadRecentApps";
                case 19:
                    return "cancelPreloadRecentApps";
                case 20:
                    return "showScreenPinningRequest";
                case 21:
                    return "dismissKeyboardShortcutsMenu";
                case 22:
                    return "toggleKeyboardShortcutsMenu";
                case 23:
                    return "appTransitionPending";
                case 24:
                    return "appTransitionCancelled";
                case 25:
                    return "appTransitionStarting";
                case 26:
                    return "appTransitionFinished";
                case 27:
                    return "showAssistDisclosure";
                case 28:
                    return "startAssist";
                case 29:
                    return "onCameraLaunchGestureDetected";
                case 30:
                    return "showPictureInPictureMenu";
                case 31:
                    return "showGlobalActionsMenu";
                case 32:
                    return "onProposedRotationChanged";
                case 33:
                    return "setTopAppHidesStatusBar";
                case 34:
                    return "addQsTile";
                case 35:
                    return "remQsTile";
                case 36:
                    return "clickQsTile";
                case 37:
                    return "handleSystemKey";
                case 38:
                    return "showPinningEnterExitToast";
                case 39:
                    return "showPinningEscapeToast";
                case 40:
                    return "showShutdownUi";
                case 41:
                    return "showBiometricDialog";
                case 42:
                    return "onBiometricAuthenticated";
                case 43:
                    return "onBiometricHelp";
                case 44:
                    return "onBiometricError";
                case 45:
                    return "hideBiometricDialog";
                case 46:
                    return "onDisplayReady";
                case 47:
                    return "onRecentsAnimationStateChanged";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg0 = false;
                int _arg02;
                ComponentName _arg03;
                switch (i) {
                    case 1:
                        Bundle _arg2;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        String _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        setStatus(_arg04, _arg1, _arg2);
                        return true;
                    case 2:
                        StatusBarIcon _arg12;
                        parcel.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (StatusBarIcon) StatusBarIcon.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setIcon(_arg05, _arg12);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        removeIcon(data.readString());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        disable(data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        animateExpandNotificationsPanel();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        animateExpandSettingsPanel(data.readString());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        animateCollapsePanels();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        togglePanel();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        showWirelessChargingAnimation(data.readInt());
                        return true;
                    case 10:
                        Rect _arg5;
                        Rect _arg6;
                        parcel.enforceInterface(descriptor);
                        int _arg06 = data.readInt();
                        int _arg13 = data.readInt();
                        int _arg22 = data.readInt();
                        int _arg3 = data.readInt();
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        setSystemUiVisibility(_arg06, _arg13, _arg22, _arg3, _arg4, _arg5, _arg6, data.readInt() != 0);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        topAppWindowChanged(_arg02, _arg0);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        setImeWindowStatus(data.readInt(), data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt() != 0);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        setWindowState(data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        showRecentApps(_arg0);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        boolean _arg07 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        hideRecentApps(_arg07, _arg0);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        toggleRecentApps();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        toggleSplitScreen();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        preloadRecentApps();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        cancelPreloadRecentApps();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        showScreenPinningRequest(data.readInt());
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        dismissKeyboardShortcutsMenu();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        toggleKeyboardShortcutsMenu(data.readInt());
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        appTransitionPending(data.readInt());
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        appTransitionCancelled(data.readInt());
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        appTransitionStarting(data.readInt(), data.readLong(), data.readLong());
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        appTransitionFinished(data.readInt());
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        showAssistDisclosure();
                        return true;
                    case 28:
                        Bundle _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        startAssist(_arg08);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        onCameraLaunchGestureDetected(data.readInt());
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        showPictureInPictureMenu();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        showGlobalActionsMenu();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onProposedRotationChanged(_arg02, _arg0);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setTopAppHidesStatusBar(_arg0);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        addQsTile(_arg03);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        remQsTile(_arg03);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        clickQsTile(_arg03);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        handleSystemKey(data.readInt());
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        showPinningEnterExitToast(_arg0);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        showPinningEscapeToast();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        showShutdownUi(_arg0, data.readString());
                        return true;
                    case 41:
                        Bundle _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        showBiometricDialog(_arg09, android.hardware.biometrics.IBiometricServiceReceiverInternal.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt() != 0, data.readInt());
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onBiometricAuthenticated(_arg0, data.readString());
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        onBiometricHelp(data.readString());
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        onBiometricError(data.readString());
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        hideBiometricDialog();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        onDisplayReady(data.readInt());
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onRecentsAnimationStateChanged(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IStatusBar impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStatusBar getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addQsTile(ComponentName componentName) throws RemoteException;

    void animateCollapsePanels() throws RemoteException;

    void animateExpandNotificationsPanel() throws RemoteException;

    void animateExpandSettingsPanel(String str) throws RemoteException;

    void appTransitionCancelled(int i) throws RemoteException;

    void appTransitionFinished(int i) throws RemoteException;

    void appTransitionPending(int i) throws RemoteException;

    void appTransitionStarting(int i, long j, long j2) throws RemoteException;

    void cancelPreloadRecentApps() throws RemoteException;

    void clickQsTile(ComponentName componentName) throws RemoteException;

    void disable(int i, int i2, int i3) throws RemoteException;

    void dismissKeyboardShortcutsMenu() throws RemoteException;

    void handleSystemKey(int i) throws RemoteException;

    void hideBiometricDialog() throws RemoteException;

    void hideRecentApps(boolean z, boolean z2) throws RemoteException;

    void onBiometricAuthenticated(boolean z, String str) throws RemoteException;

    void onBiometricError(String str) throws RemoteException;

    void onBiometricHelp(String str) throws RemoteException;

    void onCameraLaunchGestureDetected(int i) throws RemoteException;

    void onDisplayReady(int i) throws RemoteException;

    void onProposedRotationChanged(int i, boolean z) throws RemoteException;

    void onRecentsAnimationStateChanged(boolean z) throws RemoteException;

    void preloadRecentApps() throws RemoteException;

    void remQsTile(ComponentName componentName) throws RemoteException;

    void removeIcon(String str) throws RemoteException;

    void setIcon(String str, StatusBarIcon statusBarIcon) throws RemoteException;

    void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) throws RemoteException;

    void setStatus(int i, String str, Bundle bundle) throws RemoteException;

    void setSystemUiVisibility(int i, int i2, int i3, int i4, int i5, Rect rect, Rect rect2, boolean z) throws RemoteException;

    void setTopAppHidesStatusBar(boolean z) throws RemoteException;

    void setWindowState(int i, int i2, int i3) throws RemoteException;

    void showAssistDisclosure() throws RemoteException;

    void showBiometricDialog(Bundle bundle, IBiometricServiceReceiverInternal iBiometricServiceReceiverInternal, int i, boolean z, int i2) throws RemoteException;

    void showGlobalActionsMenu() throws RemoteException;

    void showPictureInPictureMenu() throws RemoteException;

    void showPinningEnterExitToast(boolean z) throws RemoteException;

    void showPinningEscapeToast() throws RemoteException;

    void showRecentApps(boolean z) throws RemoteException;

    void showScreenPinningRequest(int i) throws RemoteException;

    void showShutdownUi(boolean z, String str) throws RemoteException;

    void showWirelessChargingAnimation(int i) throws RemoteException;

    void startAssist(Bundle bundle) throws RemoteException;

    void toggleKeyboardShortcutsMenu(int i) throws RemoteException;

    void togglePanel() throws RemoteException;

    void toggleRecentApps() throws RemoteException;

    void toggleSplitScreen() throws RemoteException;

    void topAppWindowChanged(int i, boolean z) throws RemoteException;
}
