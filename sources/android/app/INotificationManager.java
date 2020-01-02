package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.NotificationManager.Policy;
import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.Adjustment;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.INotificationListener;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import android.service.notification.ZenModeConfig.ZenRule;
import java.util.List;

public interface INotificationManager extends IInterface {

    public static class Default implements INotificationManager {
        public void cancelAllNotifications(String pkg, int userId) throws RemoteException {
        }

        public void clearData(String pkg, int uid, boolean fromApp) throws RemoteException {
        }

        public void enqueueToast(String pkg, ITransientNotification callback, int duration, int displayId) throws RemoteException {
        }

        public void cancelToast(String pkg, ITransientNotification callback) throws RemoteException {
        }

        public void finishToken(String pkg, ITransientNotification callback) throws RemoteException {
        }

        public void enqueueNotificationWithTag(String pkg, String opPkg, String tag, int id, Notification notification, int userId) throws RemoteException {
        }

        public void cancelNotificationWithTag(String pkg, String tag, int id, int userId) throws RemoteException {
        }

        public void setShowBadge(String pkg, int uid, boolean showBadge) throws RemoteException {
        }

        public boolean canShowBadge(String pkg, int uid) throws RemoteException {
            return false;
        }

        public void setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
        }

        public void setNotificationsEnabledWithImportanceLockForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
        }

        public boolean areNotificationsEnabledForPackage(String pkg, int uid) throws RemoteException {
            return false;
        }

        public boolean areNotificationsEnabled(String pkg) throws RemoteException {
            return false;
        }

        public int getPackageImportance(String pkg) throws RemoteException {
            return 0;
        }

        public List<String> getAllowedAssistantAdjustments(String pkg) throws RemoteException {
            return null;
        }

        public void allowAssistantAdjustment(String adjustmentType) throws RemoteException {
        }

        public void disallowAssistantAdjustment(String adjustmentType) throws RemoteException {
        }

        public boolean shouldHideSilentStatusIcons(String callingPkg) throws RemoteException {
            return false;
        }

        public void setHideSilentStatusIcons(boolean hide) throws RemoteException {
        }

        public void setBubblesAllowed(String pkg, int uid, boolean allowed) throws RemoteException {
        }

        public boolean areBubblesAllowed(String pkg) throws RemoteException {
            return false;
        }

        public boolean areBubblesAllowedForPackage(String pkg, int uid) throws RemoteException {
            return false;
        }

        public boolean hasUserApprovedBubblesForPackage(String pkg, int uid) throws RemoteException {
            return false;
        }

        public void createNotificationChannelGroups(String pkg, ParceledListSlice channelGroupList) throws RemoteException {
        }

        public void createNotificationChannels(String pkg, ParceledListSlice channelsList) throws RemoteException {
        }

        public void createNotificationChannelsForPackage(String pkg, int uid, ParceledListSlice channelsList) throws RemoteException {
        }

        public ParceledListSlice getNotificationChannelGroupsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
            return null;
        }

        public NotificationChannelGroup getNotificationChannelGroupForPackage(String groupId, String pkg, int uid) throws RemoteException {
            return null;
        }

        public NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String pkg, int uid, String groupId, boolean includeDeleted) throws RemoteException {
            return null;
        }

        public void updateNotificationChannelGroupForPackage(String pkg, int uid, NotificationChannelGroup group) throws RemoteException {
        }

        public void updateNotificationChannelForPackage(String pkg, int uid, NotificationChannel channel) throws RemoteException {
        }

        public NotificationChannel getNotificationChannel(String callingPkg, int userId, String pkg, String channelId) throws RemoteException {
            return null;
        }

        public NotificationChannel getNotificationChannelForPackage(String pkg, int uid, String channelId, boolean includeDeleted) throws RemoteException {
            return null;
        }

        public void deleteNotificationChannel(String pkg, String channelId) throws RemoteException {
        }

        public ParceledListSlice getNotificationChannels(String callingPkg, String targetPkg, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
            return null;
        }

        public int getNumNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
            return 0;
        }

        public int getDeletedChannelCount(String pkg, int uid) throws RemoteException {
            return 0;
        }

        public int getBlockedChannelCount(String pkg, int uid) throws RemoteException {
            return 0;
        }

        public void deleteNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
        }

        public NotificationChannelGroup getNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getNotificationChannelGroups(String pkg) throws RemoteException {
            return null;
        }

        public boolean onlyHasDefaultChannel(String pkg, int uid) throws RemoteException {
            return false;
        }

        public int getBlockedAppCount(int userId) throws RemoteException {
            return 0;
        }

        public boolean areChannelsBypassingDnd() throws RemoteException {
            return false;
        }

        public int getAppsBypassingDndCount(int uid) throws RemoteException {
            return 0;
        }

        public ParceledListSlice getNotificationChannelsBypassingDnd(String pkg, int userId) throws RemoteException {
            return null;
        }

        public boolean isPackagePaused(String pkg) throws RemoteException {
            return false;
        }

        public StatusBarNotification[] getActiveNotifications(String callingPkg) throws RemoteException {
            return null;
        }

        public StatusBarNotification[] getHistoricalNotifications(String callingPkg, int count) throws RemoteException {
            return null;
        }

        public void registerListener(INotificationListener listener, ComponentName component, int userid) throws RemoteException {
        }

        public void unregisterListener(INotificationListener listener, int userid) throws RemoteException {
        }

        public void cancelNotificationFromListener(INotificationListener token, String pkg, String tag, int id) throws RemoteException {
        }

        public void cancelNotificationsFromListener(INotificationListener token, String[] keys) throws RemoteException {
        }

        public void snoozeNotificationUntilContextFromListener(INotificationListener token, String key, String snoozeCriterionId) throws RemoteException {
        }

        public void snoozeNotificationUntilFromListener(INotificationListener token, String key, long until) throws RemoteException {
        }

        public void requestBindListener(ComponentName component) throws RemoteException {
        }

        public void requestUnbindListener(INotificationListener token) throws RemoteException {
        }

        public void requestBindProvider(ComponentName component) throws RemoteException {
        }

        public void requestUnbindProvider(IConditionProvider token) throws RemoteException {
        }

        public void setNotificationsShownFromListener(INotificationListener token, String[] keys) throws RemoteException {
        }

        public ParceledListSlice getActiveNotificationsFromListener(INotificationListener token, String[] keys, int trim) throws RemoteException {
            return null;
        }

        public ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener token, int trim) throws RemoteException {
            return null;
        }

        public void clearRequestedListenerHints(INotificationListener token) throws RemoteException {
        }

        public void requestHintsFromListener(INotificationListener token, int hints) throws RemoteException {
        }

        public int getHintsFromListener(INotificationListener token) throws RemoteException {
            return 0;
        }

        public void requestInterruptionFilterFromListener(INotificationListener token, int interruptionFilter) throws RemoteException {
        }

        public int getInterruptionFilterFromListener(INotificationListener token) throws RemoteException {
            return 0;
        }

        public void setOnNotificationPostedTrimFromListener(INotificationListener token, int trim) throws RemoteException {
        }

        public void setInterruptionFilter(String pkg, int interruptionFilter) throws RemoteException {
        }

        public void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannelGroup group) throws RemoteException {
        }

        public void updateNotificationChannelFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannel channel) throws RemoteException {
        }

        public ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
            return null;
        }

        public ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
            return null;
        }

        public void applyEnqueuedAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
        }

        public void applyAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
        }

        public void applyAdjustmentsFromAssistant(INotificationListener token, List<Adjustment> list) throws RemoteException {
        }

        public void unsnoozeNotificationFromAssistant(INotificationListener token, String key) throws RemoteException {
        }

        public ComponentName getEffectsSuppressor() throws RemoteException {
            return null;
        }

        public boolean matchesCallFilter(Bundle extras) throws RemoteException {
            return false;
        }

        public boolean isSystemConditionProviderEnabled(String path) throws RemoteException {
            return false;
        }

        public boolean isNotificationListenerAccessGranted(ComponentName listener) throws RemoteException {
            return false;
        }

        public boolean isNotificationListenerAccessGrantedForUser(ComponentName listener, int userId) throws RemoteException {
            return false;
        }

        public boolean isNotificationAssistantAccessGranted(ComponentName assistant) throws RemoteException {
            return false;
        }

        public void setNotificationListenerAccessGranted(ComponentName listener, boolean enabled) throws RemoteException {
        }

        public void setNotificationAssistantAccessGranted(ComponentName assistant, boolean enabled) throws RemoteException {
        }

        public void setNotificationListenerAccessGrantedForUser(ComponentName listener, int userId, boolean enabled) throws RemoteException {
        }

        public void setNotificationAssistantAccessGrantedForUser(ComponentName assistant, int userId, boolean enabled) throws RemoteException {
        }

        public List<String> getEnabledNotificationListenerPackages() throws RemoteException {
            return null;
        }

        public List<ComponentName> getEnabledNotificationListeners(int userId) throws RemoteException {
            return null;
        }

        public ComponentName getAllowedNotificationAssistantForUser(int userId) throws RemoteException {
            return null;
        }

        public ComponentName getAllowedNotificationAssistant() throws RemoteException {
            return null;
        }

        public int getZenMode() throws RemoteException {
            return 0;
        }

        public ZenModeConfig getZenModeConfig() throws RemoteException {
            return null;
        }

        public Policy getConsolidatedNotificationPolicy() throws RemoteException {
            return null;
        }

        public void setZenMode(int mode, Uri conditionId, String reason) throws RemoteException {
        }

        public void notifyConditions(String pkg, IConditionProvider provider, Condition[] conditions) throws RemoteException {
        }

        public boolean isNotificationPolicyAccessGranted(String pkg) throws RemoteException {
            return false;
        }

        public Policy getNotificationPolicy(String pkg) throws RemoteException {
            return null;
        }

        public void setNotificationPolicy(String pkg, Policy policy) throws RemoteException {
        }

        public boolean isNotificationPolicyAccessGrantedForPackage(String pkg) throws RemoteException {
            return false;
        }

        public void setNotificationPolicyAccessGranted(String pkg, boolean granted) throws RemoteException {
        }

        public void setNotificationPolicyAccessGrantedForUser(String pkg, int userId, boolean granted) throws RemoteException {
        }

        public AutomaticZenRule getAutomaticZenRule(String id) throws RemoteException {
            return null;
        }

        public List<ZenRule> getZenRules() throws RemoteException {
            return null;
        }

        public String addAutomaticZenRule(AutomaticZenRule automaticZenRule) throws RemoteException {
            return null;
        }

        public boolean updateAutomaticZenRule(String id, AutomaticZenRule automaticZenRule) throws RemoteException {
            return false;
        }

        public boolean removeAutomaticZenRule(String id) throws RemoteException {
            return false;
        }

        public boolean removeAutomaticZenRules(String packageName) throws RemoteException {
            return false;
        }

        public int getRuleInstanceCount(ComponentName owner) throws RemoteException {
            return 0;
        }

        public void setAutomaticZenRuleState(String id, Condition condition) throws RemoteException {
        }

        public byte[] getBackupPayload(int user) throws RemoteException {
            return null;
        }

        public void applyRestore(byte[] payload, int user) throws RemoteException {
        }

        public ParceledListSlice getAppActiveNotifications(String callingPkg, int userId) throws RemoteException {
            return null;
        }

        public void setNotificationDelegate(String callingPkg, String delegate) throws RemoteException {
        }

        public String getNotificationDelegate(String callingPkg) throws RemoteException {
            return null;
        }

        public boolean canNotifyAsPackage(String callingPkg, String targetPkg, int userId) throws RemoteException {
            return false;
        }

        public void setPrivateNotificationsAllowed(boolean allow) throws RemoteException {
        }

        public boolean getPrivateNotificationsAllowed() throws RemoteException {
            return false;
        }

        public void buzzBeepBlinkForNotification(String key) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INotificationManager {
        private static final String DESCRIPTOR = "android.app.INotificationManager";
        static final int TRANSACTION_addAutomaticZenRule = 106;
        static final int TRANSACTION_allowAssistantAdjustment = 16;
        static final int TRANSACTION_applyAdjustmentFromAssistant = 76;
        static final int TRANSACTION_applyAdjustmentsFromAssistant = 77;
        static final int TRANSACTION_applyEnqueuedAdjustmentFromAssistant = 75;
        static final int TRANSACTION_applyRestore = 113;
        static final int TRANSACTION_areBubblesAllowed = 21;
        static final int TRANSACTION_areBubblesAllowedForPackage = 22;
        static final int TRANSACTION_areChannelsBypassingDnd = 45;
        static final int TRANSACTION_areNotificationsEnabled = 13;
        static final int TRANSACTION_areNotificationsEnabledForPackage = 12;
        static final int TRANSACTION_buzzBeepBlinkForNotification = 120;
        static final int TRANSACTION_canNotifyAsPackage = 117;
        static final int TRANSACTION_canShowBadge = 9;
        static final int TRANSACTION_cancelAllNotifications = 1;
        static final int TRANSACTION_cancelNotificationFromListener = 53;
        static final int TRANSACTION_cancelNotificationWithTag = 7;
        static final int TRANSACTION_cancelNotificationsFromListener = 54;
        static final int TRANSACTION_cancelToast = 4;
        static final int TRANSACTION_clearData = 2;
        static final int TRANSACTION_clearRequestedListenerHints = 64;
        static final int TRANSACTION_createNotificationChannelGroups = 24;
        static final int TRANSACTION_createNotificationChannels = 25;
        static final int TRANSACTION_createNotificationChannelsForPackage = 26;
        static final int TRANSACTION_deleteNotificationChannel = 34;
        static final int TRANSACTION_deleteNotificationChannelGroup = 40;
        static final int TRANSACTION_disallowAssistantAdjustment = 17;
        static final int TRANSACTION_enqueueNotificationWithTag = 6;
        static final int TRANSACTION_enqueueToast = 3;
        static final int TRANSACTION_finishToken = 5;
        static final int TRANSACTION_getActiveNotifications = 49;
        static final int TRANSACTION_getActiveNotificationsFromListener = 62;
        static final int TRANSACTION_getAllowedAssistantAdjustments = 15;
        static final int TRANSACTION_getAllowedNotificationAssistant = 92;
        static final int TRANSACTION_getAllowedNotificationAssistantForUser = 91;
        static final int TRANSACTION_getAppActiveNotifications = 114;
        static final int TRANSACTION_getAppsBypassingDndCount = 46;
        static final int TRANSACTION_getAutomaticZenRule = 104;
        static final int TRANSACTION_getBackupPayload = 112;
        static final int TRANSACTION_getBlockedAppCount = 44;
        static final int TRANSACTION_getBlockedChannelCount = 39;
        static final int TRANSACTION_getConsolidatedNotificationPolicy = 95;
        static final int TRANSACTION_getDeletedChannelCount = 38;
        static final int TRANSACTION_getEffectsSuppressor = 79;
        static final int TRANSACTION_getEnabledNotificationListenerPackages = 89;
        static final int TRANSACTION_getEnabledNotificationListeners = 90;
        static final int TRANSACTION_getHintsFromListener = 66;
        static final int TRANSACTION_getHistoricalNotifications = 50;
        static final int TRANSACTION_getInterruptionFilterFromListener = 68;
        static final int TRANSACTION_getNotificationChannel = 32;
        static final int TRANSACTION_getNotificationChannelForPackage = 33;
        static final int TRANSACTION_getNotificationChannelGroup = 41;
        static final int TRANSACTION_getNotificationChannelGroupForPackage = 28;
        static final int TRANSACTION_getNotificationChannelGroups = 42;
        static final int TRANSACTION_getNotificationChannelGroupsForPackage = 27;
        static final int TRANSACTION_getNotificationChannelGroupsFromPrivilegedListener = 74;
        static final int TRANSACTION_getNotificationChannels = 35;
        static final int TRANSACTION_getNotificationChannelsBypassingDnd = 47;
        static final int TRANSACTION_getNotificationChannelsForPackage = 36;
        static final int TRANSACTION_getNotificationChannelsFromPrivilegedListener = 73;
        static final int TRANSACTION_getNotificationDelegate = 116;
        static final int TRANSACTION_getNotificationPolicy = 99;
        static final int TRANSACTION_getNumNotificationChannelsForPackage = 37;
        static final int TRANSACTION_getPackageImportance = 14;
        static final int TRANSACTION_getPopulatedNotificationChannelGroupForPackage = 29;
        static final int TRANSACTION_getPrivateNotificationsAllowed = 119;
        static final int TRANSACTION_getRuleInstanceCount = 110;
        static final int TRANSACTION_getSnoozedNotificationsFromListener = 63;
        static final int TRANSACTION_getZenMode = 93;
        static final int TRANSACTION_getZenModeConfig = 94;
        static final int TRANSACTION_getZenRules = 105;
        static final int TRANSACTION_hasUserApprovedBubblesForPackage = 23;
        static final int TRANSACTION_isNotificationAssistantAccessGranted = 84;
        static final int TRANSACTION_isNotificationListenerAccessGranted = 82;
        static final int TRANSACTION_isNotificationListenerAccessGrantedForUser = 83;
        static final int TRANSACTION_isNotificationPolicyAccessGranted = 98;
        static final int TRANSACTION_isNotificationPolicyAccessGrantedForPackage = 101;
        static final int TRANSACTION_isPackagePaused = 48;
        static final int TRANSACTION_isSystemConditionProviderEnabled = 81;
        static final int TRANSACTION_matchesCallFilter = 80;
        static final int TRANSACTION_notifyConditions = 97;
        static final int TRANSACTION_onlyHasDefaultChannel = 43;
        static final int TRANSACTION_registerListener = 51;
        static final int TRANSACTION_removeAutomaticZenRule = 108;
        static final int TRANSACTION_removeAutomaticZenRules = 109;
        static final int TRANSACTION_requestBindListener = 57;
        static final int TRANSACTION_requestBindProvider = 59;
        static final int TRANSACTION_requestHintsFromListener = 65;
        static final int TRANSACTION_requestInterruptionFilterFromListener = 67;
        static final int TRANSACTION_requestUnbindListener = 58;
        static final int TRANSACTION_requestUnbindProvider = 60;
        static final int TRANSACTION_setAutomaticZenRuleState = 111;
        static final int TRANSACTION_setBubblesAllowed = 20;
        static final int TRANSACTION_setHideSilentStatusIcons = 19;
        static final int TRANSACTION_setInterruptionFilter = 70;
        static final int TRANSACTION_setNotificationAssistantAccessGranted = 86;
        static final int TRANSACTION_setNotificationAssistantAccessGrantedForUser = 88;
        static final int TRANSACTION_setNotificationDelegate = 115;
        static final int TRANSACTION_setNotificationListenerAccessGranted = 85;
        static final int TRANSACTION_setNotificationListenerAccessGrantedForUser = 87;
        static final int TRANSACTION_setNotificationPolicy = 100;
        static final int TRANSACTION_setNotificationPolicyAccessGranted = 102;
        static final int TRANSACTION_setNotificationPolicyAccessGrantedForUser = 103;
        static final int TRANSACTION_setNotificationsEnabledForPackage = 10;
        static final int TRANSACTION_setNotificationsEnabledWithImportanceLockForPackage = 11;
        static final int TRANSACTION_setNotificationsShownFromListener = 61;
        static final int TRANSACTION_setOnNotificationPostedTrimFromListener = 69;
        static final int TRANSACTION_setPrivateNotificationsAllowed = 118;
        static final int TRANSACTION_setShowBadge = 8;
        static final int TRANSACTION_setZenMode = 96;
        static final int TRANSACTION_shouldHideSilentStatusIcons = 18;
        static final int TRANSACTION_snoozeNotificationUntilContextFromListener = 55;
        static final int TRANSACTION_snoozeNotificationUntilFromListener = 56;
        static final int TRANSACTION_unregisterListener = 52;
        static final int TRANSACTION_unsnoozeNotificationFromAssistant = 78;
        static final int TRANSACTION_updateAutomaticZenRule = 107;
        static final int TRANSACTION_updateNotificationChannelForPackage = 31;
        static final int TRANSACTION_updateNotificationChannelFromPrivilegedListener = 72;
        static final int TRANSACTION_updateNotificationChannelGroupForPackage = 30;
        static final int TRANSACTION_updateNotificationChannelGroupFromPrivilegedListener = 71;

        private static class Proxy implements INotificationManager {
            public static INotificationManager sDefaultImpl;
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

            public void cancelAllNotifications(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelAllNotifications(pkg, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearData(String pkg, int uid, boolean fromApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(fromApp ? 1 : 0);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearData(pkg, uid, fromApp);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enqueueToast(String pkg, ITransientNotification callback, int duration, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(duration);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enqueueToast(pkg, callback, duration, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelToast(String pkg, ITransientNotification callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelToast(pkg, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishToken(String pkg, ITransientNotification callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishToken(pkg, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enqueueNotificationWithTag(String pkg, String opPkg, String tag, int id, Notification notification, int userId) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                String str2;
                Notification notification2 = notification;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(pkg);
                        try {
                            _data.writeString(opPkg);
                        } catch (Throwable th2) {
                            th = th2;
                            str = tag;
                            i = id;
                            i2 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(tag);
                        } catch (Throwable th3) {
                            th = th3;
                            i = id;
                            i2 = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str2 = opPkg;
                        str = tag;
                        i = id;
                        i2 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(id);
                        if (notification2 != null) {
                            _data.writeInt(1);
                            notification2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(userId);
                            if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().enqueueNotificationWithTag(pkg, opPkg, tag, id, notification, userId);
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
                        i2 = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str3 = pkg;
                    str2 = opPkg;
                    str = tag;
                    i = id;
                    i2 = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelNotificationWithTag(String pkg, String tag, int id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelNotificationWithTag(pkg, tag, id, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShowBadge(String pkg, int uid, boolean showBadge) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(showBadge ? 1 : 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShowBadge(pkg, uid, showBadge);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canShowBadge(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canShowBadge(pkg, uid);
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

            public void setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationsEnabledForPackage(pkg, uid, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationsEnabledWithImportanceLockForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationsEnabledWithImportanceLockForPackage(pkg, uid, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean areNotificationsEnabledForPackage(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areNotificationsEnabledForPackage(pkg, uid);
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

            public boolean areNotificationsEnabled(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areNotificationsEnabled(pkg);
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

            public int getPackageImportance(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPackageImportance(pkg);
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

            public List<String> getAllowedAssistantAdjustments(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    List<String> list = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllowedAssistantAdjustments(pkg);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void allowAssistantAdjustment(String adjustmentType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(adjustmentType);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().allowAssistantAdjustment(adjustmentType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disallowAssistantAdjustment(String adjustmentType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(adjustmentType);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disallowAssistantAdjustment(adjustmentType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldHideSilentStatusIcons(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldHideSilentStatusIcons(callingPkg);
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

            public void setHideSilentStatusIcons(boolean hide) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hide ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHideSilentStatusIcons(hide);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBubblesAllowed(String pkg, int uid, boolean allowed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(allowed ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBubblesAllowed(pkg, uid, allowed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean areBubblesAllowed(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areBubblesAllowed(pkg);
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

            public boolean areBubblesAllowedForPackage(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areBubblesAllowedForPackage(pkg, uid);
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

            public boolean hasUserApprovedBubblesForPackage(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasUserApprovedBubblesForPackage(pkg, uid);
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

            public void createNotificationChannelGroups(String pkg, ParceledListSlice channelGroupList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (channelGroupList != null) {
                        _data.writeInt(1);
                        channelGroupList.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createNotificationChannelGroups(pkg, channelGroupList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createNotificationChannels(String pkg, ParceledListSlice channelsList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (channelsList != null) {
                        _data.writeInt(1);
                        channelsList.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createNotificationChannels(pkg, channelsList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createNotificationChannelsForPackage(String pkg, int uid, ParceledListSlice channelsList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (channelsList != null) {
                        _data.writeInt(1);
                        channelsList.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createNotificationChannelsForPackage(pkg, uid, channelsList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannelGroupsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(27, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelGroupsForPackage(pkg, uid, includeDeleted);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NotificationChannelGroup getNotificationChannelGroupForPackage(String groupId, String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupId);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    NotificationChannelGroup notificationChannelGroup = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        notificationChannelGroup = Stub.getDefaultImpl();
                        if (notificationChannelGroup != 0) {
                            notificationChannelGroup = Stub.getDefaultImpl().getNotificationChannelGroupForPackage(groupId, pkg, uid);
                            return notificationChannelGroup;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        notificationChannelGroup = (NotificationChannelGroup) NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        notificationChannelGroup = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return notificationChannelGroup;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String pkg, int uid, String groupId, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(groupId);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    NotificationChannelGroup notificationChannelGroup = this.mRemote;
                    if (!notificationChannelGroup.transact(29, _data, _reply, 0)) {
                        notificationChannelGroup = Stub.getDefaultImpl();
                        if (notificationChannelGroup != null) {
                            notificationChannelGroup = Stub.getDefaultImpl().getPopulatedNotificationChannelGroupForPackage(pkg, uid, groupId, includeDeleted);
                            return notificationChannelGroup;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        notificationChannelGroup = (NotificationChannelGroup) NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        notificationChannelGroup = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return notificationChannelGroup;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateNotificationChannelGroupForPackage(String pkg, int uid, NotificationChannelGroup group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (group != null) {
                        _data.writeInt(1);
                        group.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateNotificationChannelGroupForPackage(pkg, uid, group);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateNotificationChannelForPackage(String pkg, int uid, NotificationChannel channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateNotificationChannelForPackage(pkg, uid, channel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NotificationChannel getNotificationChannel(String callingPkg, int userId, String pkg, String channelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    _data.writeString(channelId);
                    NotificationChannel notificationChannel = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        notificationChannel = Stub.getDefaultImpl();
                        if (notificationChannel != 0) {
                            notificationChannel = Stub.getDefaultImpl().getNotificationChannel(callingPkg, userId, pkg, channelId);
                            return notificationChannel;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        notificationChannel = (NotificationChannel) NotificationChannel.CREATOR.createFromParcel(_reply);
                    } else {
                        notificationChannel = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return notificationChannel;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NotificationChannel getNotificationChannelForPackage(String pkg, int uid, String channelId, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(channelId);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    NotificationChannel notificationChannel = this.mRemote;
                    if (!notificationChannel.transact(33, _data, _reply, 0)) {
                        notificationChannel = Stub.getDefaultImpl();
                        if (notificationChannel != null) {
                            notificationChannel = Stub.getDefaultImpl().getNotificationChannelForPackage(pkg, uid, channelId, includeDeleted);
                            return notificationChannel;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        notificationChannel = (NotificationChannel) NotificationChannel.CREATOR.createFromParcel(_reply);
                    } else {
                        notificationChannel = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return notificationChannel;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteNotificationChannel(String pkg, String channelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelId);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteNotificationChannel(pkg, channelId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannels(String callingPkg, String targetPkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(targetPkg);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannels(callingPkg, targetPkg, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(36, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelsForPackage(pkg, uid, includeDeleted);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNumNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getNumNotificationChannelsForPackage(pkg, uid, includeDeleted);
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

            public int getDeletedChannelCount(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    int i = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDeletedChannelCount(pkg, uid);
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

            public int getBlockedChannelCount(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    int i = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getBlockedChannelCount(pkg, uid);
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

            public void deleteNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelGroupId);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteNotificationChannelGroup(pkg, channelGroupId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NotificationChannelGroup getNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelGroupId);
                    NotificationChannelGroup notificationChannelGroup = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        notificationChannelGroup = Stub.getDefaultImpl();
                        if (notificationChannelGroup != 0) {
                            notificationChannelGroup = Stub.getDefaultImpl().getNotificationChannelGroup(pkg, channelGroupId);
                            return notificationChannelGroup;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        notificationChannelGroup = (NotificationChannelGroup) NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        notificationChannelGroup = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return notificationChannelGroup;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannelGroups(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    ParceledListSlice parceledListSlice = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelGroups(pkg);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean onlyHasDefaultChannel(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().onlyHasDefaultChannel(pkg, uid);
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

            public int getBlockedAppCount(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getBlockedAppCount(userId);
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

            public boolean areChannelsBypassingDnd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().areChannelsBypassingDnd();
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

            public int getAppsBypassingDndCount(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 46;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAppsBypassingDndCount(uid);
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

            public ParceledListSlice getNotificationChannelsBypassingDnd(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelsBypassingDnd(pkg, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackagePaused(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPackagePaused(pkg);
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

            public StatusBarNotification[] getActiveNotifications(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    StatusBarNotification[] statusBarNotificationArr = 49;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        statusBarNotificationArr = Stub.getDefaultImpl();
                        if (statusBarNotificationArr != 0) {
                            statusBarNotificationArr = Stub.getDefaultImpl().getActiveNotifications(callingPkg);
                            return statusBarNotificationArr;
                        }
                    }
                    _reply.readException();
                    StatusBarNotification[] _result = (StatusBarNotification[]) _reply.createTypedArray(StatusBarNotification.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusBarNotification[] getHistoricalNotifications(String callingPkg, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(count);
                    StatusBarNotification[] statusBarNotificationArr = 50;
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        statusBarNotificationArr = Stub.getDefaultImpl();
                        if (statusBarNotificationArr != 0) {
                            statusBarNotificationArr = Stub.getDefaultImpl().getHistoricalNotifications(callingPkg, count);
                            return statusBarNotificationArr;
                        }
                    }
                    _reply.readException();
                    StatusBarNotification[] _result = (StatusBarNotification[]) _reply.createTypedArray(StatusBarNotification.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListener(INotificationListener listener, ComponentName component, int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userid);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerListener(listener, component, userid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(INotificationListener listener, int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userid);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterListener(listener, userid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelNotificationFromListener(INotificationListener token, String pkg, String tag, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelNotificationFromListener(token, pkg, tag, id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelNotificationsFromListener(INotificationListener token, String[] keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelNotificationsFromListener(token, keys);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void snoozeNotificationUntilContextFromListener(INotificationListener token, String key, String snoozeCriterionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    _data.writeString(snoozeCriterionId);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().snoozeNotificationUntilContextFromListener(token, key, snoozeCriterionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void snoozeNotificationUntilFromListener(INotificationListener token, String key, long until) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    _data.writeLong(until);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().snoozeNotificationUntilFromListener(token, key, until);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestBindListener(ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestBindListener(component);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestUnbindListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestUnbindListener(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestBindProvider(ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestBindProvider(component);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestUnbindProvider(IConditionProvider token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestUnbindProvider(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationsShownFromListener(INotificationListener token, String[] keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationsShownFromListener(token, keys);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getActiveNotificationsFromListener(INotificationListener token, String[] keys, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    _data.writeInt(trim);
                    ParceledListSlice parceledListSlice = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getActiveNotificationsFromListener(token, keys, trim);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener token, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(trim);
                    ParceledListSlice parceledListSlice = 63;
                    if (!this.mRemote.transact(63, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getSnoozedNotificationsFromListener(token, trim);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearRequestedListenerHints(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearRequestedListenerHints(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestHintsFromListener(INotificationListener token, int hints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(hints);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestHintsFromListener(token, hints);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getHintsFromListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    int i = 66;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getHintsFromListener(token);
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

            public void requestInterruptionFilterFromListener(INotificationListener token, int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(interruptionFilter);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestInterruptionFilterFromListener(token, interruptionFilter);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInterruptionFilterFromListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    int i = 68;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getInterruptionFilterFromListener(token);
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

            public void setOnNotificationPostedTrimFromListener(INotificationListener token, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(trim);
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOnNotificationPostedTrimFromListener(token, trim);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInterruptionFilter(String pkg, int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(interruptionFilter);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInterruptionFilter(pkg, interruptionFilter);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannelGroup group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (group != null) {
                        _data.writeInt(1);
                        group.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateNotificationChannelGroupFromPrivilegedListener(token, pkg, user, group);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateNotificationChannelFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannel channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateNotificationChannelFromPrivilegedListener(token, pkg, user, channel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(73, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelsFromPrivilegedListener(token, pkg, user);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(74, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getNotificationChannelGroupsFromPrivilegedListener(token, pkg, user);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyEnqueuedAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (adjustment != null) {
                        _data.writeInt(1);
                        adjustment.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(75, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyEnqueuedAdjustmentFromAssistant(token, adjustment);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (adjustment != null) {
                        _data.writeInt(1);
                        adjustment.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyAdjustmentFromAssistant(token, adjustment);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyAdjustmentsFromAssistant(INotificationListener token, List<Adjustment> adjustments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeTypedList(adjustments);
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyAdjustmentsFromAssistant(token, adjustments);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unsnoozeNotificationFromAssistant(INotificationListener token, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unsnoozeNotificationFromAssistant(token, key);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getEffectsSuppressor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 79;
                    if (!this.mRemote.transact(79, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getEffectsSuppressor();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean matchesCallFilter(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().matchesCallFilter(extras);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSystemConditionProviderEnabled(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(81, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSystemConditionProviderEnabled(path);
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

            public boolean isNotificationListenerAccessGranted(ComponentName listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isNotificationListenerAccessGranted(listener);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNotificationListenerAccessGrantedForUser(ComponentName listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(83, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isNotificationListenerAccessGrantedForUser(listener, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNotificationAssistantAccessGranted(ComponentName assistant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isNotificationAssistantAccessGranted(assistant);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationListenerAccessGranted(ComponentName listener, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationListenerAccessGranted(listener, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationAssistantAccessGranted(ComponentName assistant, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationAssistantAccessGranted(assistant, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationListenerAccessGrantedForUser(ComponentName listener, int userId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationListenerAccessGrantedForUser(listener, userId, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationAssistantAccessGrantedForUser(ComponentName assistant, int userId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationAssistantAccessGrantedForUser(assistant, userId, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getEnabledNotificationListenerPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 89;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getEnabledNotificationListenerPackages();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ComponentName> getEnabledNotificationListeners(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<ComponentName> list = 90;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getEnabledNotificationListeners(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ComponentName.CREATOR);
                    List<ComponentName> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getAllowedNotificationAssistantForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ComponentName componentName = 91;
                    if (!this.mRemote.transact(91, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getAllowedNotificationAssistantForUser(userId);
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getAllowedNotificationAssistant() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 92;
                    if (!this.mRemote.transact(92, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getAllowedNotificationAssistant();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getZenMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 93;
                    if (!this.mRemote.transact(93, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getZenMode();
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

            public ZenModeConfig getZenModeConfig() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ZenModeConfig zenModeConfig = 94;
                    if (!this.mRemote.transact(94, _data, _reply, 0)) {
                        zenModeConfig = Stub.getDefaultImpl();
                        if (zenModeConfig != 0) {
                            zenModeConfig = Stub.getDefaultImpl().getZenModeConfig();
                            return zenModeConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        zenModeConfig = (ZenModeConfig) ZenModeConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        zenModeConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return zenModeConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Policy getConsolidatedNotificationPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Policy policy = 95;
                    if (!this.mRemote.transact(95, _data, _reply, 0)) {
                        policy = Stub.getDefaultImpl();
                        if (policy != 0) {
                            policy = Stub.getDefaultImpl().getConsolidatedNotificationPolicy();
                            return policy;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        policy = (Policy) Policy.CREATOR.createFromParcel(_reply);
                    } else {
                        policy = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return policy;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setZenMode(int mode, Uri conditionId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (conditionId != null) {
                        _data.writeInt(1);
                        conditionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(reason);
                    if (this.mRemote.transact(96, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setZenMode(mode, conditionId, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyConditions(String pkg, IConditionProvider provider, Condition[] conditions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(provider != null ? provider.asBinder() : null);
                    _data.writeTypedArray(conditions, 0);
                    if (this.mRemote.transact(97, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyConditions(pkg, provider, conditions);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean isNotificationPolicyAccessGranted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(98, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNotificationPolicyAccessGranted(pkg);
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

            public Policy getNotificationPolicy(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    Policy policy = 99;
                    if (!this.mRemote.transact(99, _data, _reply, 0)) {
                        policy = Stub.getDefaultImpl();
                        if (policy != 0) {
                            policy = Stub.getDefaultImpl().getNotificationPolicy(pkg);
                            return policy;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        policy = (Policy) Policy.CREATOR.createFromParcel(_reply);
                    } else {
                        policy = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return policy;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationPolicy(String pkg, Policy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(100, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationPolicy(pkg, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNotificationPolicyAccessGrantedForPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(101, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNotificationPolicyAccessGrantedForPackage(pkg);
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

            public void setNotificationPolicyAccessGranted(String pkg, boolean granted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(granted ? 1 : 0);
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationPolicyAccessGranted(pkg, granted);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationPolicyAccessGrantedForUser(String pkg, int userId, boolean granted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    _data.writeInt(granted ? 1 : 0);
                    if (this.mRemote.transact(103, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationPolicyAccessGrantedForUser(pkg, userId, granted);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AutomaticZenRule getAutomaticZenRule(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    AutomaticZenRule automaticZenRule = 104;
                    if (!this.mRemote.transact(104, _data, _reply, 0)) {
                        automaticZenRule = Stub.getDefaultImpl();
                        if (automaticZenRule != 0) {
                            automaticZenRule = Stub.getDefaultImpl().getAutomaticZenRule(id);
                            return automaticZenRule;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        automaticZenRule = (AutomaticZenRule) AutomaticZenRule.CREATOR.createFromParcel(_reply);
                    } else {
                        automaticZenRule = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return automaticZenRule;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ZenRule> getZenRules() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ZenRule> list = 105;
                    if (!this.mRemote.transact(105, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getZenRules();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ZenRule.CREATOR);
                    List<ZenRule> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String addAutomaticZenRule(AutomaticZenRule automaticZenRule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (automaticZenRule != null) {
                        _data.writeInt(1);
                        automaticZenRule.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(106, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().addAutomaticZenRule(automaticZenRule);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateAutomaticZenRule(String id, AutomaticZenRule automaticZenRule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    boolean _result = true;
                    if (automaticZenRule != null) {
                        _data.writeInt(1);
                        automaticZenRule.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(107, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateAutomaticZenRule(id, automaticZenRule);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeAutomaticZenRule(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(108, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeAutomaticZenRule(id);
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

            public boolean removeAutomaticZenRules(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(109, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeAutomaticZenRules(packageName);
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

            public int getRuleInstanceCount(ComponentName owner) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (owner != null) {
                        _data.writeInt(1);
                        owner.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(110, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getRuleInstanceCount(owner);
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

            public void setAutomaticZenRuleState(String id, Condition condition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (condition != null) {
                        _data.writeInt(1);
                        condition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(111, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAutomaticZenRuleState(id, condition);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getBackupPayload(int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(user);
                    byte[] bArr = 112;
                    if (!this.mRemote.transact(112, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getBackupPayload(user);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyRestore(byte[] payload, int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(payload);
                    _data.writeInt(user);
                    if (this.mRemote.transact(113, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyRestore(payload, user);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAppActiveNotifications(String callingPkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 114;
                    if (!this.mRemote.transact(114, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAppActiveNotifications(callingPkg, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificationDelegate(String callingPkg, String delegate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(delegate);
                    if (this.mRemote.transact(115, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificationDelegate(callingPkg, delegate);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNotificationDelegate(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    String str = 116;
                    if (!this.mRemote.transact(116, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNotificationDelegate(callingPkg);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canNotifyAsPackage(String callingPkg, String targetPkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(targetPkg);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(117, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canNotifyAsPackage(callingPkg, targetPkg, userId);
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

            public void setPrivateNotificationsAllowed(boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allow ? 1 : 0);
                    if (this.mRemote.transact(118, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPrivateNotificationsAllowed(allow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getPrivateNotificationsAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(119, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getPrivateNotificationsAllowed();
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

            public void buzzBeepBlinkForNotification(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    if (this.mRemote.transact(120, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().buzzBeepBlinkForNotification(key);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INotificationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INotificationManager)) {
                return new Proxy(obj);
            }
            return (INotificationManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "cancelAllNotifications";
                case 2:
                    return "clearData";
                case 3:
                    return "enqueueToast";
                case 4:
                    return "cancelToast";
                case 5:
                    return "finishToken";
                case 6:
                    return "enqueueNotificationWithTag";
                case 7:
                    return "cancelNotificationWithTag";
                case 8:
                    return "setShowBadge";
                case 9:
                    return "canShowBadge";
                case 10:
                    return "setNotificationsEnabledForPackage";
                case 11:
                    return "setNotificationsEnabledWithImportanceLockForPackage";
                case 12:
                    return "areNotificationsEnabledForPackage";
                case 13:
                    return "areNotificationsEnabled";
                case 14:
                    return "getPackageImportance";
                case 15:
                    return "getAllowedAssistantAdjustments";
                case 16:
                    return "allowAssistantAdjustment";
                case 17:
                    return "disallowAssistantAdjustment";
                case 18:
                    return "shouldHideSilentStatusIcons";
                case 19:
                    return "setHideSilentStatusIcons";
                case 20:
                    return "setBubblesAllowed";
                case 21:
                    return "areBubblesAllowed";
                case 22:
                    return "areBubblesAllowedForPackage";
                case 23:
                    return "hasUserApprovedBubblesForPackage";
                case 24:
                    return "createNotificationChannelGroups";
                case 25:
                    return "createNotificationChannels";
                case 26:
                    return "createNotificationChannelsForPackage";
                case 27:
                    return "getNotificationChannelGroupsForPackage";
                case 28:
                    return "getNotificationChannelGroupForPackage";
                case 29:
                    return "getPopulatedNotificationChannelGroupForPackage";
                case 30:
                    return "updateNotificationChannelGroupForPackage";
                case 31:
                    return "updateNotificationChannelForPackage";
                case 32:
                    return "getNotificationChannel";
                case 33:
                    return "getNotificationChannelForPackage";
                case 34:
                    return "deleteNotificationChannel";
                case 35:
                    return "getNotificationChannels";
                case 36:
                    return "getNotificationChannelsForPackage";
                case 37:
                    return "getNumNotificationChannelsForPackage";
                case 38:
                    return "getDeletedChannelCount";
                case 39:
                    return "getBlockedChannelCount";
                case 40:
                    return "deleteNotificationChannelGroup";
                case 41:
                    return "getNotificationChannelGroup";
                case 42:
                    return "getNotificationChannelGroups";
                case 43:
                    return "onlyHasDefaultChannel";
                case 44:
                    return "getBlockedAppCount";
                case 45:
                    return "areChannelsBypassingDnd";
                case 46:
                    return "getAppsBypassingDndCount";
                case 47:
                    return "getNotificationChannelsBypassingDnd";
                case 48:
                    return "isPackagePaused";
                case 49:
                    return "getActiveNotifications";
                case 50:
                    return "getHistoricalNotifications";
                case 51:
                    return "registerListener";
                case 52:
                    return "unregisterListener";
                case 53:
                    return "cancelNotificationFromListener";
                case 54:
                    return "cancelNotificationsFromListener";
                case 55:
                    return "snoozeNotificationUntilContextFromListener";
                case 56:
                    return "snoozeNotificationUntilFromListener";
                case 57:
                    return "requestBindListener";
                case 58:
                    return "requestUnbindListener";
                case 59:
                    return "requestBindProvider";
                case 60:
                    return "requestUnbindProvider";
                case 61:
                    return "setNotificationsShownFromListener";
                case 62:
                    return "getActiveNotificationsFromListener";
                case 63:
                    return "getSnoozedNotificationsFromListener";
                case 64:
                    return "clearRequestedListenerHints";
                case 65:
                    return "requestHintsFromListener";
                case 66:
                    return "getHintsFromListener";
                case 67:
                    return "requestInterruptionFilterFromListener";
                case 68:
                    return "getInterruptionFilterFromListener";
                case 69:
                    return "setOnNotificationPostedTrimFromListener";
                case 70:
                    return "setInterruptionFilter";
                case 71:
                    return "updateNotificationChannelGroupFromPrivilegedListener";
                case 72:
                    return "updateNotificationChannelFromPrivilegedListener";
                case 73:
                    return "getNotificationChannelsFromPrivilegedListener";
                case 74:
                    return "getNotificationChannelGroupsFromPrivilegedListener";
                case 75:
                    return "applyEnqueuedAdjustmentFromAssistant";
                case 76:
                    return "applyAdjustmentFromAssistant";
                case 77:
                    return "applyAdjustmentsFromAssistant";
                case 78:
                    return "unsnoozeNotificationFromAssistant";
                case 79:
                    return "getEffectsSuppressor";
                case 80:
                    return "matchesCallFilter";
                case 81:
                    return "isSystemConditionProviderEnabled";
                case 82:
                    return "isNotificationListenerAccessGranted";
                case 83:
                    return "isNotificationListenerAccessGrantedForUser";
                case 84:
                    return "isNotificationAssistantAccessGranted";
                case 85:
                    return "setNotificationListenerAccessGranted";
                case 86:
                    return "setNotificationAssistantAccessGranted";
                case 87:
                    return "setNotificationListenerAccessGrantedForUser";
                case 88:
                    return "setNotificationAssistantAccessGrantedForUser";
                case 89:
                    return "getEnabledNotificationListenerPackages";
                case 90:
                    return "getEnabledNotificationListeners";
                case 91:
                    return "getAllowedNotificationAssistantForUser";
                case 92:
                    return "getAllowedNotificationAssistant";
                case 93:
                    return "getZenMode";
                case 94:
                    return "getZenModeConfig";
                case 95:
                    return "getConsolidatedNotificationPolicy";
                case 96:
                    return "setZenMode";
                case 97:
                    return "notifyConditions";
                case 98:
                    return "isNotificationPolicyAccessGranted";
                case 99:
                    return "getNotificationPolicy";
                case 100:
                    return "setNotificationPolicy";
                case 101:
                    return "isNotificationPolicyAccessGrantedForPackage";
                case 102:
                    return "setNotificationPolicyAccessGranted";
                case 103:
                    return "setNotificationPolicyAccessGrantedForUser";
                case 104:
                    return "getAutomaticZenRule";
                case 105:
                    return "getZenRules";
                case 106:
                    return "addAutomaticZenRule";
                case 107:
                    return "updateAutomaticZenRule";
                case 108:
                    return "removeAutomaticZenRule";
                case 109:
                    return "removeAutomaticZenRules";
                case 110:
                    return "getRuleInstanceCount";
                case 111:
                    return "setAutomaticZenRuleState";
                case 112:
                    return "getBackupPayload";
                case 113:
                    return "applyRestore";
                case 114:
                    return "getAppActiveNotifications";
                case 115:
                    return "setNotificationDelegate";
                case 116:
                    return "getNotificationDelegate";
                case 117:
                    return "canNotifyAsPackage";
                case 118:
                    return "setPrivateNotificationsAllowed";
                case 119:
                    return "getPrivateNotificationsAllowed";
                case 120:
                    return "buzzBeepBlinkForNotification";
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
                boolean _arg2 = false;
                String _arg0;
                int _arg1;
                boolean _result;
                boolean _result2;
                int _result3;
                String _arg02;
                ParceledListSlice _arg12;
                ParceledListSlice _arg22;
                ParceledListSlice _result4;
                NotificationChannel _result5;
                NotificationChannelGroup _result6;
                ParceledListSlice _result7;
                INotificationListener _arg03;
                ComponentName _arg13;
                ComponentName _arg04;
                UserHandle _arg23;
                INotificationListener _arg05;
                String _arg14;
                UserHandle _arg24;
                Adjustment _arg15;
                int _result8;
                Policy _result9;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        cancelAllNotifications(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        clearData(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        enqueueToast(data.readString(), android.app.ITransientNotification.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        cancelToast(data.readString(), android.app.ITransientNotification.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        finishToken(data.readString(), android.app.ITransientNotification.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        Notification _arg4;
                        parcel.enforceInterface(descriptor);
                        String _arg06 = data.readString();
                        String _arg16 = data.readString();
                        String _arg25 = data.readString();
                        int _arg3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg4 = (Notification) Notification.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        enqueueNotificationWithTag(_arg06, _arg16, _arg25, _arg3, _arg4, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        cancelNotificationWithTag(data.readString(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setShowBadge(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result = canShowBadge(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationsEnabledForPackage(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationsEnabledWithImportanceLockForPackage(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = areNotificationsEnabledForPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result2 = areNotificationsEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result3 = getPackageImportance(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        List<String> _result10 = getAllowedAssistantAdjustments(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result10);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        allowAssistantAdjustment(data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        disallowAssistantAdjustment(data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result2 = shouldHideSilentStatusIcons(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setHideSilentStatusIcons(_arg2);
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setBubblesAllowed(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result2 = areBubblesAllowed(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result = areBubblesAllowedForPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result = hasUserApprovedBubblesForPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        createNotificationChannelGroups(_arg02, _arg12);
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        createNotificationChannels(_arg02, _arg12);
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        createNotificationChannelsForPackage(_arg02, _result3, _arg22);
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result4 = getNotificationChannelGroupsForPackage(data.readString(), data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        NotificationChannelGroup _result11 = getNotificationChannelGroupForPackage(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        NotificationChannelGroup _result12 = getPopulatedNotificationChannelGroupForPackage(data.readString(), data.readInt(), data.readString(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 30:
                        NotificationChannelGroup _arg26;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg26 = (NotificationChannelGroup) NotificationChannelGroup.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg26 = null;
                        }
                        updateNotificationChannelGroupForPackage(_arg02, _result3, _arg26);
                        reply.writeNoException();
                        return true;
                    case 31:
                        NotificationChannel _arg27;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg27 = (NotificationChannel) NotificationChannel.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        updateNotificationChannelForPackage(_arg02, _result3, _arg27);
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result5 = getNotificationChannel(data.readString(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result5 = getNotificationChannelForPackage(data.readString(), data.readInt(), data.readString(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        deleteNotificationChannel(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _result4 = getNotificationChannels(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result4 = getNotificationChannelsForPackage(data.readString(), data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        int _result13 = getNumNotificationChannelsForPackage(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result13);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg1 = getDeletedChannelCount(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        _arg1 = getBlockedChannelCount(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        deleteNotificationChannelGroup(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _result6 = getNotificationChannelGroup(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _arg22 = getNotificationChannelGroups(data.readString());
                        reply.writeNoException();
                        if (_arg22 != null) {
                            parcel2.writeInt(1);
                            _arg22.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _result = onlyHasDefaultChannel(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _result3 = getBlockedAppCount(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _arg2 = areChannelsBypassingDnd();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _result3 = getAppsBypassingDndCount(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _result7 = getNotificationChannelsBypassingDnd(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        _result2 = isPackagePaused(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        StatusBarNotification[] _result14 = getActiveNotifications(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result14, 1);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        StatusBarNotification[] _result15 = getHistoricalNotifications(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result15, 1);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        registerListener(_arg03, _arg13, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        unregisterListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        cancelNotificationFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        cancelNotificationsFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        snoozeNotificationUntilContextFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        snoozeNotificationUntilFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        requestBindListener(_arg04);
                        reply.writeNoException();
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        requestUnbindListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        requestBindProvider(_arg04);
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        requestUnbindProvider(android.service.notification.IConditionProvider.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        setNotificationsShownFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        _result4 = getActiveNotificationsFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        _result7 = getSnoozedNotificationsFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        clearRequestedListenerHints(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        requestHintsFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _result3 = getHintsFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        requestInterruptionFilterFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _result3 = getInterruptionFilterFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        setOnNotificationPostedTrimFromListener(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        setInterruptionFilter(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _result6 = (NotificationChannelGroup) NotificationChannelGroup.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        updateNotificationChannelGroupFromPrivilegedListener(_arg03, _arg0, _arg23, _result6);
                        reply.writeNoException();
                        return true;
                    case 72:
                        NotificationChannel _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (NotificationChannel) NotificationChannel.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        updateNotificationChannelFromPrivilegedListener(_arg03, _arg0, _arg23, _arg32);
                        reply.writeNoException();
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _result4 = getNotificationChannelsFromPrivilegedListener(_arg05, _arg14, _arg24);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _result4 = getNotificationChannelGroupsFromPrivilegedListener(_arg05, _arg14, _arg24);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg15 = (Adjustment) Adjustment.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        applyEnqueuedAdjustmentFromAssistant(_arg03, _arg15);
                        reply.writeNoException();
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg15 = (Adjustment) Adjustment.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        applyAdjustmentFromAssistant(_arg03, _arg15);
                        reply.writeNoException();
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        applyAdjustmentsFromAssistant(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), parcel.createTypedArrayList(Adjustment.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        unsnoozeNotificationFromAssistant(android.service.notification.INotificationListener.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        _arg13 = getEffectsSuppressor();
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            _arg13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 80:
                        Bundle _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        _result2 = matchesCallFilter(_arg07);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        _result2 = isSystemConditionProviderEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result2 = isNotificationListenerAccessGranted(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result = isNotificationListenerAccessGrantedForUser(_arg04, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result2 = isNotificationAssistantAccessGranted(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationListenerAccessGranted(_arg13, _arg2);
                        reply.writeNoException();
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationAssistantAccessGranted(_arg13, _arg2);
                        reply.writeNoException();
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationListenerAccessGrantedForUser(_arg13, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg13 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationAssistantAccessGrantedForUser(_arg13, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        List<String> _result16 = getEnabledNotificationListenerPackages();
                        reply.writeNoException();
                        parcel2.writeStringList(_result16);
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        List<ComponentName> _result17 = getEnabledNotificationListeners(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result17);
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        ComponentName _result18 = getAllowedNotificationAssistantForUser(data.readInt());
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(1);
                            _result18.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        _arg13 = getAllowedNotificationAssistant();
                        reply.writeNoException();
                        if (_arg13 != null) {
                            parcel2.writeInt(1);
                            _arg13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        _result8 = getZenMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        ZenModeConfig _result19 = getZenModeConfig();
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        _result9 = getConsolidatedNotificationPolicy();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 96:
                        Uri _arg17;
                        parcel.enforceInterface(descriptor);
                        _result8 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg17 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        setZenMode(_result8, _arg17, data.readString());
                        return true;
                    case 97:
                        parcel.enforceInterface(descriptor);
                        notifyConditions(data.readString(), android.service.notification.IConditionProvider.Stub.asInterface(data.readStrongBinder()), (Condition[]) parcel.createTypedArray(Condition.CREATOR));
                        return true;
                    case 98:
                        parcel.enforceInterface(descriptor);
                        _result2 = isNotificationPolicyAccessGranted(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        Policy _result20 = getNotificationPolicy(data.readString());
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 100:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _result9 = (Policy) Policy.CREATOR.createFromParcel(parcel);
                        } else {
                            _result9 = null;
                        }
                        setNotificationPolicy(_arg02, _result9);
                        reply.writeNoException();
                        return true;
                    case 101:
                        parcel.enforceInterface(descriptor);
                        _result2 = isNotificationPolicyAccessGrantedForPackage(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 102:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationPolicyAccessGranted(_arg0, _arg2);
                        reply.writeNoException();
                        return true;
                    case 103:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setNotificationPolicyAccessGrantedForUser(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 104:
                        parcel.enforceInterface(descriptor);
                        AutomaticZenRule _result21 = getAutomaticZenRule(data.readString());
                        reply.writeNoException();
                        if (_result21 != null) {
                            parcel2.writeInt(1);
                            _result21.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 105:
                        parcel.enforceInterface(descriptor);
                        List<ZenRule> _result22 = getZenRules();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result22);
                        return true;
                    case 106:
                        AutomaticZenRule _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (AutomaticZenRule) AutomaticZenRule.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        _arg0 = addAutomaticZenRule(_arg08);
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 107:
                        AutomaticZenRule _arg18;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg18 = (AutomaticZenRule) AutomaticZenRule.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        _result = updateAutomaticZenRule(_arg02, _arg18);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 108:
                        parcel.enforceInterface(descriptor);
                        _result2 = removeAutomaticZenRule(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 109:
                        parcel.enforceInterface(descriptor);
                        _result2 = removeAutomaticZenRules(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 110:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result3 = getRuleInstanceCount(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 111:
                        Condition _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg19 = (Condition) Condition.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        setAutomaticZenRuleState(_arg02, _arg19);
                        reply.writeNoException();
                        return true;
                    case 112:
                        parcel.enforceInterface(descriptor);
                        byte[] _result23 = getBackupPayload(data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result23);
                        return true;
                    case 113:
                        parcel.enforceInterface(descriptor);
                        applyRestore(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 114:
                        parcel.enforceInterface(descriptor);
                        _result7 = getAppActiveNotifications(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 115:
                        parcel.enforceInterface(descriptor);
                        setNotificationDelegate(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 116:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getNotificationDelegate(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 117:
                        parcel.enforceInterface(descriptor);
                        boolean _result24 = canNotifyAsPackage(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result24);
                        return true;
                    case 118:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setPrivateNotificationsAllowed(_arg2);
                        reply.writeNoException();
                        return true;
                    case 119:
                        parcel.enforceInterface(descriptor);
                        _arg2 = getPrivateNotificationsAllowed();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 120:
                        parcel.enforceInterface(descriptor);
                        buzzBeepBlinkForNotification(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INotificationManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INotificationManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String addAutomaticZenRule(AutomaticZenRule automaticZenRule) throws RemoteException;

    void allowAssistantAdjustment(String str) throws RemoteException;

    void applyAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException;

    void applyAdjustmentsFromAssistant(INotificationListener iNotificationListener, List<Adjustment> list) throws RemoteException;

    void applyEnqueuedAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException;

    void applyRestore(byte[] bArr, int i) throws RemoteException;

    boolean areBubblesAllowed(String str) throws RemoteException;

    boolean areBubblesAllowedForPackage(String str, int i) throws RemoteException;

    boolean areChannelsBypassingDnd() throws RemoteException;

    boolean areNotificationsEnabled(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean areNotificationsEnabledForPackage(String str, int i) throws RemoteException;

    void buzzBeepBlinkForNotification(String str) throws RemoteException;

    boolean canNotifyAsPackage(String str, String str2, int i) throws RemoteException;

    boolean canShowBadge(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void cancelAllNotifications(String str, int i) throws RemoteException;

    void cancelNotificationFromListener(INotificationListener iNotificationListener, String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    void cancelNotificationWithTag(String str, String str2, int i, int i2) throws RemoteException;

    void cancelNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    void cancelToast(String str, ITransientNotification iTransientNotification) throws RemoteException;

    void clearData(String str, int i, boolean z) throws RemoteException;

    void clearRequestedListenerHints(INotificationListener iNotificationListener) throws RemoteException;

    void createNotificationChannelGroups(String str, ParceledListSlice parceledListSlice) throws RemoteException;

    void createNotificationChannels(String str, ParceledListSlice parceledListSlice) throws RemoteException;

    void createNotificationChannelsForPackage(String str, int i, ParceledListSlice parceledListSlice) throws RemoteException;

    void deleteNotificationChannel(String str, String str2) throws RemoteException;

    void deleteNotificationChannelGroup(String str, String str2) throws RemoteException;

    void disallowAssistantAdjustment(String str) throws RemoteException;

    void enqueueNotificationWithTag(String str, String str2, String str3, int i, Notification notification, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void enqueueToast(String str, ITransientNotification iTransientNotification, int i, int i2) throws RemoteException;

    void finishToken(String str, ITransientNotification iTransientNotification) throws RemoteException;

    @UnsupportedAppUsage
    StatusBarNotification[] getActiveNotifications(String str) throws RemoteException;

    ParceledListSlice getActiveNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr, int i) throws RemoteException;

    List<String> getAllowedAssistantAdjustments(String str) throws RemoteException;

    ComponentName getAllowedNotificationAssistant() throws RemoteException;

    ComponentName getAllowedNotificationAssistantForUser(int i) throws RemoteException;

    ParceledListSlice getAppActiveNotifications(String str, int i) throws RemoteException;

    int getAppsBypassingDndCount(int i) throws RemoteException;

    AutomaticZenRule getAutomaticZenRule(String str) throws RemoteException;

    byte[] getBackupPayload(int i) throws RemoteException;

    int getBlockedAppCount(int i) throws RemoteException;

    int getBlockedChannelCount(String str, int i) throws RemoteException;

    Policy getConsolidatedNotificationPolicy() throws RemoteException;

    int getDeletedChannelCount(String str, int i) throws RemoteException;

    ComponentName getEffectsSuppressor() throws RemoteException;

    List<String> getEnabledNotificationListenerPackages() throws RemoteException;

    List<ComponentName> getEnabledNotificationListeners(int i) throws RemoteException;

    int getHintsFromListener(INotificationListener iNotificationListener) throws RemoteException;

    @UnsupportedAppUsage
    StatusBarNotification[] getHistoricalNotifications(String str, int i) throws RemoteException;

    int getInterruptionFilterFromListener(INotificationListener iNotificationListener) throws RemoteException;

    NotificationChannel getNotificationChannel(String str, int i, String str2, String str3) throws RemoteException;

    NotificationChannel getNotificationChannelForPackage(String str, int i, String str2, boolean z) throws RemoteException;

    NotificationChannelGroup getNotificationChannelGroup(String str, String str2) throws RemoteException;

    NotificationChannelGroup getNotificationChannelGroupForPackage(String str, String str2, int i) throws RemoteException;

    ParceledListSlice getNotificationChannelGroups(String str) throws RemoteException;

    ParceledListSlice getNotificationChannelGroupsForPackage(String str, int i, boolean z) throws RemoteException;

    ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException;

    ParceledListSlice getNotificationChannels(String str, String str2, int i) throws RemoteException;

    ParceledListSlice getNotificationChannelsBypassingDnd(String str, int i) throws RemoteException;

    ParceledListSlice getNotificationChannelsForPackage(String str, int i, boolean z) throws RemoteException;

    ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException;

    String getNotificationDelegate(String str) throws RemoteException;

    Policy getNotificationPolicy(String str) throws RemoteException;

    int getNumNotificationChannelsForPackage(String str, int i, boolean z) throws RemoteException;

    int getPackageImportance(String str) throws RemoteException;

    NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String str, int i, String str2, boolean z) throws RemoteException;

    boolean getPrivateNotificationsAllowed() throws RemoteException;

    int getRuleInstanceCount(ComponentName componentName) throws RemoteException;

    ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    @UnsupportedAppUsage
    int getZenMode() throws RemoteException;

    @UnsupportedAppUsage
    ZenModeConfig getZenModeConfig() throws RemoteException;

    List<ZenRule> getZenRules() throws RemoteException;

    boolean hasUserApprovedBubblesForPackage(String str, int i) throws RemoteException;

    boolean isNotificationAssistantAccessGranted(ComponentName componentName) throws RemoteException;

    boolean isNotificationListenerAccessGranted(ComponentName componentName) throws RemoteException;

    boolean isNotificationListenerAccessGrantedForUser(ComponentName componentName, int i) throws RemoteException;

    boolean isNotificationPolicyAccessGranted(String str) throws RemoteException;

    boolean isNotificationPolicyAccessGrantedForPackage(String str) throws RemoteException;

    boolean isPackagePaused(String str) throws RemoteException;

    boolean isSystemConditionProviderEnabled(String str) throws RemoteException;

    boolean matchesCallFilter(Bundle bundle) throws RemoteException;

    void notifyConditions(String str, IConditionProvider iConditionProvider, Condition[] conditionArr) throws RemoteException;

    boolean onlyHasDefaultChannel(String str, int i) throws RemoteException;

    void registerListener(INotificationListener iNotificationListener, ComponentName componentName, int i) throws RemoteException;

    boolean removeAutomaticZenRule(String str) throws RemoteException;

    boolean removeAutomaticZenRules(String str) throws RemoteException;

    void requestBindListener(ComponentName componentName) throws RemoteException;

    void requestBindProvider(ComponentName componentName) throws RemoteException;

    void requestHintsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    void requestInterruptionFilterFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    void requestUnbindListener(INotificationListener iNotificationListener) throws RemoteException;

    void requestUnbindProvider(IConditionProvider iConditionProvider) throws RemoteException;

    void setAutomaticZenRuleState(String str, Condition condition) throws RemoteException;

    void setBubblesAllowed(String str, int i, boolean z) throws RemoteException;

    void setHideSilentStatusIcons(boolean z) throws RemoteException;

    void setInterruptionFilter(String str, int i) throws RemoteException;

    void setNotificationAssistantAccessGranted(ComponentName componentName, boolean z) throws RemoteException;

    void setNotificationAssistantAccessGrantedForUser(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setNotificationDelegate(String str, String str2) throws RemoteException;

    void setNotificationListenerAccessGranted(ComponentName componentName, boolean z) throws RemoteException;

    void setNotificationListenerAccessGrantedForUser(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setNotificationPolicy(String str, Policy policy) throws RemoteException;

    void setNotificationPolicyAccessGranted(String str, boolean z) throws RemoteException;

    void setNotificationPolicyAccessGrantedForUser(String str, int i, boolean z) throws RemoteException;

    void setNotificationsEnabledForPackage(String str, int i, boolean z) throws RemoteException;

    void setNotificationsEnabledWithImportanceLockForPackage(String str, int i, boolean z) throws RemoteException;

    void setNotificationsShownFromListener(INotificationListener iNotificationListener, String[] strArr) throws RemoteException;

    void setOnNotificationPostedTrimFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    void setPrivateNotificationsAllowed(boolean z) throws RemoteException;

    void setShowBadge(String str, int i, boolean z) throws RemoteException;

    void setZenMode(int i, Uri uri, String str) throws RemoteException;

    boolean shouldHideSilentStatusIcons(String str) throws RemoteException;

    void snoozeNotificationUntilContextFromListener(INotificationListener iNotificationListener, String str, String str2) throws RemoteException;

    void snoozeNotificationUntilFromListener(INotificationListener iNotificationListener, String str, long j) throws RemoteException;

    void unregisterListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    void unsnoozeNotificationFromAssistant(INotificationListener iNotificationListener, String str) throws RemoteException;

    boolean updateAutomaticZenRule(String str, AutomaticZenRule automaticZenRule) throws RemoteException;

    void updateNotificationChannelForPackage(String str, int i, NotificationChannel notificationChannel) throws RemoteException;

    void updateNotificationChannelFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannel notificationChannel) throws RemoteException;

    void updateNotificationChannelGroupForPackage(String str, int i, NotificationChannelGroup notificationChannelGroup) throws RemoteException;

    void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup) throws RemoteException;
}
