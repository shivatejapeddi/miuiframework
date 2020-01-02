package com.android.internal.statusbar;

import android.annotation.UnsupportedAppUsage;
import android.app.Notification.Action;
import android.content.ComponentName;
import android.hardware.biometrics.IBiometricServiceReceiverInternal;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

public interface IStatusBarService extends IInterface {

    public static class Default implements IStatusBarService {
        public void setStatus(int what, IBinder token, String action, Bundle ext) throws RemoteException {
        }

        public void expandNotificationsPanel() throws RemoteException {
        }

        public void collapsePanels() throws RemoteException {
        }

        public void togglePanel() throws RemoteException {
        }

        public void disable(int what, IBinder token, String pkg) throws RemoteException {
        }

        public void disableForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
        }

        public void disable2(int what, IBinder token, String pkg) throws RemoteException {
        }

        public void disable2ForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
        }

        public int[] getDisableFlags(IBinder token, int userId) throws RemoteException {
            return null;
        }

        public void setIcon(String slot, String iconPackage, int iconId, int iconLevel, String contentDescription) throws RemoteException {
        }

        public void setIconVisibility(String slot, boolean visible) throws RemoteException {
        }

        public void removeIcon(String slot) throws RemoteException {
        }

        public void setImeWindowStatus(int displayId, IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
        }

        public void expandSettingsPanel(String subPanel) throws RemoteException {
        }

        public RegisterStatusBarResult registerStatusBar(IStatusBar callbacks) throws RemoteException {
            return null;
        }

        public void onPanelRevealed(boolean clearNotificationEffects, int numItems) throws RemoteException {
        }

        public void onPanelHidden() throws RemoteException {
        }

        public void clearNotificationEffects() throws RemoteException {
        }

        public void onNotificationClick(String key, NotificationVisibility nv) throws RemoteException {
        }

        public void onNotificationActionClick(String key, int actionIndex, Action action, NotificationVisibility nv, boolean generatedByAssistant) throws RemoteException {
        }

        public void onNotificationError(String pkg, String tag, int id, int uid, int initialPid, String message, int userId) throws RemoteException {
        }

        public void onClearAllNotifications(int userId) throws RemoteException {
        }

        public void onNotificationClear(String pkg, String tag, int id, int userId, String key, int dismissalSurface, int dismissalSentiment, NotificationVisibility nv) throws RemoteException {
        }

        public void onNotificationVisibilityChanged(NotificationVisibility[] newlyVisibleKeys, NotificationVisibility[] noLongerVisibleKeys) throws RemoteException {
        }

        public void onNotificationExpansionChanged(String key, boolean userAction, boolean expanded, int notificationLocation) throws RemoteException {
        }

        public void onNotificationDirectReplied(String key) throws RemoteException {
        }

        public void onNotificationSmartSuggestionsAdded(String key, int smartReplyCount, int smartActionCount, boolean generatedByAsssistant, boolean editBeforeSending) throws RemoteException {
        }

        public void onNotificationSmartReplySent(String key, int replyIndex, CharSequence reply, int notificationLocation, boolean modifiedBeforeSending) throws RemoteException {
        }

        public void onNotificationSettingsViewed(String key) throws RemoteException {
        }

        public void setSystemUiVisibility(int displayId, int vis, int mask, String cause) throws RemoteException {
        }

        public void onNotificationBubbleChanged(String key, boolean isBubble) throws RemoteException {
        }

        public void onGlobalActionsShown() throws RemoteException {
        }

        public void onGlobalActionsHidden() throws RemoteException {
        }

        public void shutdown() throws RemoteException {
        }

        public void reboot(boolean safeMode) throws RemoteException {
        }

        public void addTile(ComponentName tile) throws RemoteException {
        }

        public void remTile(ComponentName tile) throws RemoteException {
        }

        public void clickTile(ComponentName tile) throws RemoteException {
        }

        public void handleSystemKey(int key) throws RemoteException {
        }

        public void showPinningEnterExitToast(boolean entering) throws RemoteException {
        }

        public void showPinningEscapeToast() throws RemoteException {
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

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStatusBarService {
        private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBarService";
        static final int TRANSACTION_addTile = 36;
        static final int TRANSACTION_clearNotificationEffects = 18;
        static final int TRANSACTION_clickTile = 38;
        static final int TRANSACTION_collapsePanels = 3;
        static final int TRANSACTION_disable = 5;
        static final int TRANSACTION_disable2 = 7;
        static final int TRANSACTION_disable2ForUser = 8;
        static final int TRANSACTION_disableForUser = 6;
        static final int TRANSACTION_expandNotificationsPanel = 2;
        static final int TRANSACTION_expandSettingsPanel = 14;
        static final int TRANSACTION_getDisableFlags = 9;
        static final int TRANSACTION_handleSystemKey = 39;
        static final int TRANSACTION_hideBiometricDialog = 46;
        static final int TRANSACTION_onBiometricAuthenticated = 43;
        static final int TRANSACTION_onBiometricError = 45;
        static final int TRANSACTION_onBiometricHelp = 44;
        static final int TRANSACTION_onClearAllNotifications = 22;
        static final int TRANSACTION_onGlobalActionsHidden = 33;
        static final int TRANSACTION_onGlobalActionsShown = 32;
        static final int TRANSACTION_onNotificationActionClick = 20;
        static final int TRANSACTION_onNotificationBubbleChanged = 31;
        static final int TRANSACTION_onNotificationClear = 23;
        static final int TRANSACTION_onNotificationClick = 19;
        static final int TRANSACTION_onNotificationDirectReplied = 26;
        static final int TRANSACTION_onNotificationError = 21;
        static final int TRANSACTION_onNotificationExpansionChanged = 25;
        static final int TRANSACTION_onNotificationSettingsViewed = 29;
        static final int TRANSACTION_onNotificationSmartReplySent = 28;
        static final int TRANSACTION_onNotificationSmartSuggestionsAdded = 27;
        static final int TRANSACTION_onNotificationVisibilityChanged = 24;
        static final int TRANSACTION_onPanelHidden = 17;
        static final int TRANSACTION_onPanelRevealed = 16;
        static final int TRANSACTION_reboot = 35;
        static final int TRANSACTION_registerStatusBar = 15;
        static final int TRANSACTION_remTile = 37;
        static final int TRANSACTION_removeIcon = 12;
        static final int TRANSACTION_setIcon = 10;
        static final int TRANSACTION_setIconVisibility = 11;
        static final int TRANSACTION_setImeWindowStatus = 13;
        static final int TRANSACTION_setStatus = 1;
        static final int TRANSACTION_setSystemUiVisibility = 30;
        static final int TRANSACTION_showBiometricDialog = 42;
        static final int TRANSACTION_showPinningEnterExitToast = 40;
        static final int TRANSACTION_showPinningEscapeToast = 41;
        static final int TRANSACTION_shutdown = 34;
        static final int TRANSACTION_togglePanel = 4;

        private static class Proxy implements IStatusBarService {
            public static IStatusBarService sDefaultImpl;
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

            public void setStatus(int what, IBinder token, String action, Bundle ext) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(action);
                    if (ext != null) {
                        _data.writeInt(1);
                        ext.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStatus(what, token, action, ext);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void expandNotificationsPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().expandNotificationsPanel();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void collapsePanels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().collapsePanels();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void togglePanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().togglePanel();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable(int what, IBinder token, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disable(what, token, pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableForUser(what, token, pkg, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable2(int what, IBinder token, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disable2(what, token, pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable2ForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disable2ForUser(what, token, pkg, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getDisableFlags(IBinder token, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    int[] iArr = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getDisableFlags(token, userId);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIcon(String slot, String iconPackage, int iconId, int iconLevel, String contentDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    _data.writeString(iconPackage);
                    _data.writeInt(iconId);
                    _data.writeInt(iconLevel);
                    _data.writeString(contentDescription);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIcon(slot, iconPackage, iconId, iconLevel, contentDescription);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIconVisibility(String slot, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIconVisibility(slot, visible);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeIcon(String slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeIcon(slot);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setImeWindowStatus(int displayId, IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(token);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    _data.writeInt(showImeSwitcher ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setImeWindowStatus(displayId, token, vis, backDisposition, showImeSwitcher);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void expandSettingsPanel(String subPanel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(subPanel);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().expandSettingsPanel(subPanel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RegisterStatusBarResult registerStatusBar(IStatusBar callbacks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    RegisterStatusBarResult registerStatusBarResult = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        registerStatusBarResult = Stub.getDefaultImpl();
                        if (registerStatusBarResult != 0) {
                            registerStatusBarResult = Stub.getDefaultImpl().registerStatusBar(callbacks);
                            return registerStatusBarResult;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        registerStatusBarResult = (RegisterStatusBarResult) RegisterStatusBarResult.CREATOR.createFromParcel(_reply);
                    } else {
                        registerStatusBarResult = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return registerStatusBarResult;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPanelRevealed(boolean clearNotificationEffects, int numItems) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clearNotificationEffects ? 1 : 0);
                    _data.writeInt(numItems);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPanelRevealed(clearNotificationEffects, numItems);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPanelHidden() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onPanelHidden();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearNotificationEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearNotificationEffects();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationClick(String key, NotificationVisibility nv) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    if (nv != null) {
                        _data.writeInt(1);
                        nv.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationClick(key, nv);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationActionClick(String key, int actionIndex, Action action, NotificationVisibility nv, boolean generatedByAssistant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(actionIndex);
                    int i = 1;
                    if (action != null) {
                        _data.writeInt(1);
                        action.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (nv != null) {
                        _data.writeInt(1);
                        nv.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!generatedByAssistant) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationActionClick(key, actionIndex, action, nv, generatedByAssistant);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationError(String pkg, String tag, int id, int uid, int initialPid, String message, int userId) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(pkg);
                    } catch (Throwable th2) {
                        th = th2;
                        str = tag;
                        i = id;
                        i2 = uid;
                        i3 = initialPid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(tag);
                    } catch (Throwable th3) {
                        th = th3;
                        i = id;
                        i2 = uid;
                        i3 = initialPid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(id);
                        try {
                            _data.writeInt(uid);
                        } catch (Throwable th4) {
                            th = th4;
                            i3 = initialPid;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(initialPid);
                            _data.writeString(message);
                            _data.writeInt(userId);
                            if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onNotificationError(pkg, tag, id, uid, initialPid, message, userId);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = uid;
                        i3 = initialPid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = pkg;
                    str = tag;
                    i = id;
                    i2 = uid;
                    i3 = initialPid;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void onClearAllNotifications(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onClearAllNotifications(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationClear(String pkg, String tag, int id, int userId, String key, int dismissalSurface, int dismissalSentiment, NotificationVisibility nv) throws RemoteException {
                Throwable th;
                int i;
                String str;
                NotificationVisibility notificationVisibility = nv;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(pkg);
                        try {
                            _data.writeString(tag);
                        } catch (Throwable th2) {
                            th = th2;
                            i = id;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(id);
                            _data.writeInt(userId);
                            _data.writeString(key);
                            _data.writeInt(dismissalSurface);
                            _data.writeInt(dismissalSentiment);
                            if (notificationVisibility != null) {
                                _data.writeInt(1);
                                notificationVisibility.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onNotificationClear(pkg, tag, id, userId, key, dismissalSurface, dismissalSentiment, nv);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str = tag;
                        i = id;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str2 = pkg;
                    str = tag;
                    i = id;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void onNotificationVisibilityChanged(NotificationVisibility[] newlyVisibleKeys, NotificationVisibility[] noLongerVisibleKeys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(newlyVisibleKeys, 0);
                    _data.writeTypedArray(noLongerVisibleKeys, 0);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationVisibilityChanged(newlyVisibleKeys, noLongerVisibleKeys);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationExpansionChanged(String key, boolean userAction, boolean expanded, int notificationLocation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    int i = 1;
                    _data.writeInt(userAction ? 1 : 0);
                    if (!expanded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(notificationLocation);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationExpansionChanged(key, userAction, expanded, notificationLocation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationDirectReplied(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationDirectReplied(key);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationSmartSuggestionsAdded(String key, int smartReplyCount, int smartActionCount, boolean generatedByAsssistant, boolean editBeforeSending) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(smartReplyCount);
                    _data.writeInt(smartActionCount);
                    int i = 1;
                    _data.writeInt(generatedByAsssistant ? 1 : 0);
                    if (!editBeforeSending) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationSmartSuggestionsAdded(key, smartReplyCount, smartActionCount, generatedByAsssistant, editBeforeSending);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationSmartReplySent(String key, int replyIndex, CharSequence reply, int notificationLocation, boolean modifiedBeforeSending) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(replyIndex);
                    int i = 1;
                    if (reply != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(reply, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(notificationLocation);
                    if (!modifiedBeforeSending) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationSmartReplySent(key, replyIndex, reply, notificationLocation, modifiedBeforeSending);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationSettingsViewed(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationSettingsViewed(key);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemUiVisibility(int displayId, int vis, int mask, String cause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(vis);
                    _data.writeInt(mask);
                    _data.writeString(cause);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemUiVisibility(displayId, vis, mask, cause);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationBubbleChanged(String key, boolean isBubble) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(isBubble ? 1 : 0);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onNotificationBubbleChanged(key, isBubble);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onGlobalActionsShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onGlobalActionsShown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onGlobalActionsHidden() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onGlobalActionsHidden();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reboot(boolean safeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(safeMode ? 1 : 0);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reboot(safeMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addTile(tile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void remTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().remTile(tile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clickTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clickTile(tile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleSystemKey(int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(key);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleSystemKey(key);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showPinningEnterExitToast(boolean entering) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(entering ? 1 : 0);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showPinningEnterExitToast(entering);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showPinningEscapeToast() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showPinningEscapeToast();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showBiometricDialog(Bundle bundle, IBiometricServiceReceiverInternal receiver, int type, boolean requireConfirmation, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(type);
                    if (!requireConfirmation) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showBiometricDialog(bundle, receiver, type, requireConfirmation, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBiometricAuthenticated(boolean authenticated, String failureReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(authenticated ? 1 : 0);
                    _data.writeString(failureReason);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBiometricAuthenticated(authenticated, failureReason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBiometricHelp(String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBiometricHelp(message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBiometricError(String error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(error);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBiometricError(error);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hideBiometricDialog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hideBiometricDialog();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatusBarService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatusBarService)) {
                return new Proxy(obj);
            }
            return (IStatusBarService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setStatus";
                case 2:
                    return "expandNotificationsPanel";
                case 3:
                    return "collapsePanels";
                case 4:
                    return "togglePanel";
                case 5:
                    return "disable";
                case 6:
                    return "disableForUser";
                case 7:
                    return "disable2";
                case 8:
                    return "disable2ForUser";
                case 9:
                    return "getDisableFlags";
                case 10:
                    return "setIcon";
                case 11:
                    return "setIconVisibility";
                case 12:
                    return "removeIcon";
                case 13:
                    return "setImeWindowStatus";
                case 14:
                    return "expandSettingsPanel";
                case 15:
                    return "registerStatusBar";
                case 16:
                    return "onPanelRevealed";
                case 17:
                    return "onPanelHidden";
                case 18:
                    return "clearNotificationEffects";
                case 19:
                    return "onNotificationClick";
                case 20:
                    return "onNotificationActionClick";
                case 21:
                    return "onNotificationError";
                case 22:
                    return "onClearAllNotifications";
                case 23:
                    return "onNotificationClear";
                case 24:
                    return "onNotificationVisibilityChanged";
                case 25:
                    return "onNotificationExpansionChanged";
                case 26:
                    return "onNotificationDirectReplied";
                case 27:
                    return "onNotificationSmartSuggestionsAdded";
                case 28:
                    return "onNotificationSmartReplySent";
                case 29:
                    return "onNotificationSettingsViewed";
                case 30:
                    return "setSystemUiVisibility";
                case 31:
                    return "onNotificationBubbleChanged";
                case 32:
                    return "onGlobalActionsShown";
                case 33:
                    return "onGlobalActionsHidden";
                case 34:
                    return "shutdown";
                case 35:
                    return "reboot";
                case 36:
                    return "addTile";
                case 37:
                    return "remTile";
                case 38:
                    return "clickTile";
                case 39:
                    return "handleSystemKey";
                case 40:
                    return "showPinningEnterExitToast";
                case 41:
                    return "showPinningEscapeToast";
                case 42:
                    return "showBiometricDialog";
                case 43:
                    return "onBiometricAuthenticated";
                case 44:
                    return "onBiometricHelp";
                case 45:
                    return "onBiometricError";
                case 46:
                    return "hideBiometricDialog";
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
                boolean _arg0 = false;
                String _arg02;
                String _arg03;
                int _arg1;
                ComponentName _arg04;
                switch (i) {
                    case 1:
                        Bundle _arg3;
                        parcel.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        IBinder _arg12 = data.readStrongBinder();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        setStatus(_arg05, _arg12, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        expandNotificationsPanel();
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        collapsePanels();
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        togglePanel();
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        disable(data.readInt(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        disableForUser(data.readInt(), data.readStrongBinder(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        disable2(data.readInt(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        disable2ForUser(data.readInt(), data.readStrongBinder(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        int[] _result = getDisableFlags(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        setIcon(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setIconVisibility(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        removeIcon(data.readString());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        setImeWindowStatus(data.readInt(), data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        expandSettingsPanel(data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        RegisterStatusBarResult _result2 = registerStatusBar(com.android.internal.statusbar.IStatusBar.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onPanelRevealed(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        onPanelHidden();
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        clearNotificationEffects();
                        reply.writeNoException();
                        return true;
                    case 19:
                        NotificationVisibility _arg13;
                        parcel.enforceInterface(descriptor);
                        String _arg06 = data.readString();
                        if (data.readInt() != 0) {
                            _arg13 = (NotificationVisibility) NotificationVisibility.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        onNotificationClick(_arg06, _arg13);
                        reply.writeNoException();
                        return true;
                    case 20:
                        Action _arg22;
                        NotificationVisibility _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (Action) Action.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (NotificationVisibility) NotificationVisibility.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        onNotificationActionClick(_arg03, _arg1, _arg22, _arg32, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        onNotificationError(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        onClearAllNotifications(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        NotificationVisibility _arg7;
                        parcel.enforceInterface(descriptor);
                        String _arg07 = data.readString();
                        String _arg14 = data.readString();
                        int _arg23 = data.readInt();
                        int _arg33 = data.readInt();
                        String _arg4 = data.readString();
                        int _arg5 = data.readInt();
                        int _arg6 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg7 = (NotificationVisibility) NotificationVisibility.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        onNotificationClear(_arg07, _arg14, _arg23, _arg33, _arg4, _arg5, _arg6, _arg7);
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        onNotificationVisibilityChanged((NotificationVisibility[]) parcel.createTypedArray(NotificationVisibility.CREATOR), (NotificationVisibility[]) parcel.createTypedArray(NotificationVisibility.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        boolean _arg15 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onNotificationExpansionChanged(_arg02, _arg15, _arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        onNotificationDirectReplied(data.readString());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        onNotificationSmartSuggestionsAdded(data.readString(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 28:
                        CharSequence _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg24 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        onNotificationSmartReplySent(_arg03, _arg1, _arg24, data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        onNotificationSettingsViewed(data.readString());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        setSystemUiVisibility(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onNotificationBubbleChanged(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        onGlobalActionsShown();
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        onGlobalActionsHidden();
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        shutdown();
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        reboot(_arg0);
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        addTile(_arg04);
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        remTile(_arg04);
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        clickTile(_arg04);
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        handleSystemKey(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        showPinningEnterExitToast(_arg0);
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        showPinningEscapeToast();
                        reply.writeNoException();
                        return true;
                    case 42:
                        Bundle _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        showBiometricDialog(_arg08, android.hardware.biometrics.IBiometricServiceReceiverInternal.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onBiometricAuthenticated(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        onBiometricHelp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        onBiometricError(data.readString());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        hideBiometricDialog();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IStatusBarService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStatusBarService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addTile(ComponentName componentName) throws RemoteException;

    void clearNotificationEffects() throws RemoteException;

    void clickTile(ComponentName componentName) throws RemoteException;

    @UnsupportedAppUsage
    void collapsePanels() throws RemoteException;

    @UnsupportedAppUsage
    void disable(int i, IBinder iBinder, String str) throws RemoteException;

    void disable2(int i, IBinder iBinder, String str) throws RemoteException;

    void disable2ForUser(int i, IBinder iBinder, String str, int i2) throws RemoteException;

    void disableForUser(int i, IBinder iBinder, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void expandNotificationsPanel() throws RemoteException;

    void expandSettingsPanel(String str) throws RemoteException;

    int[] getDisableFlags(IBinder iBinder, int i) throws RemoteException;

    @UnsupportedAppUsage
    void handleSystemKey(int i) throws RemoteException;

    void hideBiometricDialog() throws RemoteException;

    void onBiometricAuthenticated(boolean z, String str) throws RemoteException;

    void onBiometricError(String str) throws RemoteException;

    void onBiometricHelp(String str) throws RemoteException;

    void onClearAllNotifications(int i) throws RemoteException;

    void onGlobalActionsHidden() throws RemoteException;

    void onGlobalActionsShown() throws RemoteException;

    void onNotificationActionClick(String str, int i, Action action, NotificationVisibility notificationVisibility, boolean z) throws RemoteException;

    void onNotificationBubbleChanged(String str, boolean z) throws RemoteException;

    void onNotificationClear(String str, String str2, int i, int i2, String str3, int i3, int i4, NotificationVisibility notificationVisibility) throws RemoteException;

    void onNotificationClick(String str, NotificationVisibility notificationVisibility) throws RemoteException;

    void onNotificationDirectReplied(String str) throws RemoteException;

    void onNotificationError(String str, String str2, int i, int i2, int i3, String str3, int i4) throws RemoteException;

    void onNotificationExpansionChanged(String str, boolean z, boolean z2, int i) throws RemoteException;

    void onNotificationSettingsViewed(String str) throws RemoteException;

    void onNotificationSmartReplySent(String str, int i, CharSequence charSequence, int i2, boolean z) throws RemoteException;

    void onNotificationSmartSuggestionsAdded(String str, int i, int i2, boolean z, boolean z2) throws RemoteException;

    void onNotificationVisibilityChanged(NotificationVisibility[] notificationVisibilityArr, NotificationVisibility[] notificationVisibilityArr2) throws RemoteException;

    void onPanelHidden() throws RemoteException;

    void onPanelRevealed(boolean z, int i) throws RemoteException;

    void reboot(boolean z) throws RemoteException;

    RegisterStatusBarResult registerStatusBar(IStatusBar iStatusBar) throws RemoteException;

    void remTile(ComponentName componentName) throws RemoteException;

    @UnsupportedAppUsage
    void removeIcon(String str) throws RemoteException;

    void setIcon(String str, String str2, int i, int i2, String str3) throws RemoteException;

    @UnsupportedAppUsage
    void setIconVisibility(String str, boolean z) throws RemoteException;

    void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) throws RemoteException;

    void setStatus(int i, IBinder iBinder, String str, Bundle bundle) throws RemoteException;

    void setSystemUiVisibility(int i, int i2, int i3, String str) throws RemoteException;

    void showBiometricDialog(Bundle bundle, IBiometricServiceReceiverInternal iBiometricServiceReceiverInternal, int i, boolean z, int i2) throws RemoteException;

    void showPinningEnterExitToast(boolean z) throws RemoteException;

    void showPinningEscapeToast() throws RemoteException;

    void shutdown() throws RemoteException;

    void togglePanel() throws RemoteException;
}
