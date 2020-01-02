package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import java.util.List;

public interface ISub extends IInterface {

    public static class Default implements ISub {
        public List<SubscriptionInfo> getAllSubInfoList(String callingPackage) throws RemoteException {
            return null;
        }

        public int getAllSubInfoCount(String callingPackage) throws RemoteException {
            return 0;
        }

        public SubscriptionInfo getActiveSubscriptionInfo(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public SubscriptionInfo getActiveSubscriptionInfoForIccId(String iccId, String callingPackage) throws RemoteException {
            return null;
        }

        public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        public List<SubscriptionInfo> getActiveSubscriptionInfoList(String callingPackage) throws RemoteException {
            return null;
        }

        public int getActiveSubInfoCount(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getActiveSubInfoCountMax() throws RemoteException {
            return 0;
        }

        public List<SubscriptionInfo> getAvailableSubscriptionInfoList(String callingPackage) throws RemoteException {
            return null;
        }

        public List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String callingPackage) throws RemoteException {
            return null;
        }

        public void requestEmbeddedSubscriptionInfoListRefresh(int cardId) throws RemoteException {
        }

        public int addSubInfoRecord(String iccId, int slotIndex) throws RemoteException {
            return 0;
        }

        public int addSubInfo(String uniqueId, String displayName, int slotIndex, int subscriptionType) throws RemoteException {
            return 0;
        }

        public int removeSubInfo(String uniqueId, int subscriptionType) throws RemoteException {
            return 0;
        }

        public int setIconTint(int tint, int subId) throws RemoteException {
            return 0;
        }

        public int setDisplayNameUsingSrc(String displayName, int subId, int nameSource) throws RemoteException {
            return 0;
        }

        public int setDisplayNumber(String number, int subId) throws RemoteException {
            return 0;
        }

        public int setDataRoaming(int roaming, int subId) throws RemoteException {
            return 0;
        }

        public int setOpportunistic(boolean opportunistic, int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public ParcelUuid createSubscriptionGroup(int[] subIdList, String callingPackage) throws RemoteException {
            return null;
        }

        public void setPreferredDataSubscriptionId(int subId, boolean needValidation, ISetOpportunisticDataCallback callback) throws RemoteException {
        }

        public int getPreferredDataSubscriptionId() throws RemoteException {
            return 0;
        }

        public List<SubscriptionInfo> getOpportunisticSubscriptions(String callingPackage) throws RemoteException {
            return null;
        }

        public void removeSubscriptionsFromGroup(int[] subIdList, ParcelUuid groupUuid, String callingPackage) throws RemoteException {
        }

        public void addSubscriptionsIntoGroup(int[] subIdList, ParcelUuid groupUuid, String callingPackage) throws RemoteException {
        }

        public List<SubscriptionInfo> getSubscriptionsInGroup(ParcelUuid groupUuid, String callingPackage) throws RemoteException {
            return null;
        }

        public int getSlotIndex(int subId) throws RemoteException {
            return 0;
        }

        public int[] getSubId(int slotIndex) throws RemoteException {
            return null;
        }

        public int getDefaultSubId() throws RemoteException {
            return 0;
        }

        public int clearSubInfo() throws RemoteException {
            return 0;
        }

        public int getPhoneId(int subId) throws RemoteException {
            return 0;
        }

        public int getDefaultDataSubId() throws RemoteException {
            return 0;
        }

        public void setDefaultDataSubId(int subId) throws RemoteException {
        }

        public int getDefaultVoiceSubId() throws RemoteException {
            return 0;
        }

        public void setDefaultVoiceSubId(int subId) throws RemoteException {
        }

        public int getDefaultSmsSubId() throws RemoteException {
            return 0;
        }

        public void setDefaultSmsSubId(int subId) throws RemoteException {
        }

        public int[] getActiveSubIdList(boolean visibleOnly) throws RemoteException {
            return null;
        }

        public int setSubscriptionProperty(int subId, String propKey, String propValue) throws RemoteException {
            return 0;
        }

        public String getSubscriptionProperty(int subId, String propKey, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean setSubscriptionEnabled(boolean enable, int subId) throws RemoteException {
            return false;
        }

        public boolean isSubscriptionEnabled(int subId) throws RemoteException {
            return false;
        }

        public int getEnabledSubscriptionId(int slotIndex) throws RemoteException {
            return 0;
        }

        public int getSimStateForSlotIndex(int slotIndex) throws RemoteException {
            return 0;
        }

        public boolean isActiveSubId(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean setAlwaysAllowMmsData(int subId, boolean alwaysAllow) throws RemoteException {
            return false;
        }

        public int getActiveDataSubscriptionId() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISub {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ISub";
        static final int TRANSACTION_addSubInfo = 13;
        static final int TRANSACTION_addSubInfoRecord = 12;
        static final int TRANSACTION_addSubscriptionsIntoGroup = 25;
        static final int TRANSACTION_clearSubInfo = 30;
        static final int TRANSACTION_createSubscriptionGroup = 20;
        static final int TRANSACTION_getAccessibleSubscriptionInfoList = 10;
        static final int TRANSACTION_getActiveDataSubscriptionId = 47;
        static final int TRANSACTION_getActiveSubIdList = 38;
        static final int TRANSACTION_getActiveSubInfoCount = 7;
        static final int TRANSACTION_getActiveSubInfoCountMax = 8;
        static final int TRANSACTION_getActiveSubscriptionInfo = 3;
        static final int TRANSACTION_getActiveSubscriptionInfoForIccId = 4;
        static final int TRANSACTION_getActiveSubscriptionInfoForSimSlotIndex = 5;
        static final int TRANSACTION_getActiveSubscriptionInfoList = 6;
        static final int TRANSACTION_getAllSubInfoCount = 2;
        static final int TRANSACTION_getAllSubInfoList = 1;
        static final int TRANSACTION_getAvailableSubscriptionInfoList = 9;
        static final int TRANSACTION_getDefaultDataSubId = 32;
        static final int TRANSACTION_getDefaultSmsSubId = 36;
        static final int TRANSACTION_getDefaultSubId = 29;
        static final int TRANSACTION_getDefaultVoiceSubId = 34;
        static final int TRANSACTION_getEnabledSubscriptionId = 43;
        static final int TRANSACTION_getOpportunisticSubscriptions = 23;
        static final int TRANSACTION_getPhoneId = 31;
        static final int TRANSACTION_getPreferredDataSubscriptionId = 22;
        static final int TRANSACTION_getSimStateForSlotIndex = 44;
        static final int TRANSACTION_getSlotIndex = 27;
        static final int TRANSACTION_getSubId = 28;
        static final int TRANSACTION_getSubscriptionProperty = 40;
        static final int TRANSACTION_getSubscriptionsInGroup = 26;
        static final int TRANSACTION_isActiveSubId = 45;
        static final int TRANSACTION_isSubscriptionEnabled = 42;
        static final int TRANSACTION_removeSubInfo = 14;
        static final int TRANSACTION_removeSubscriptionsFromGroup = 24;
        static final int TRANSACTION_requestEmbeddedSubscriptionInfoListRefresh = 11;
        static final int TRANSACTION_setAlwaysAllowMmsData = 46;
        static final int TRANSACTION_setDataRoaming = 18;
        static final int TRANSACTION_setDefaultDataSubId = 33;
        static final int TRANSACTION_setDefaultSmsSubId = 37;
        static final int TRANSACTION_setDefaultVoiceSubId = 35;
        static final int TRANSACTION_setDisplayNameUsingSrc = 16;
        static final int TRANSACTION_setDisplayNumber = 17;
        static final int TRANSACTION_setIconTint = 15;
        static final int TRANSACTION_setOpportunistic = 19;
        static final int TRANSACTION_setPreferredDataSubscriptionId = 21;
        static final int TRANSACTION_setSubscriptionEnabled = 41;
        static final int TRANSACTION_setSubscriptionProperty = 39;

        private static class Proxy implements ISub {
            public static ISub sDefaultImpl;
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

            public List<SubscriptionInfo> getAllSubInfoList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllSubInfoList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAllSubInfoCount(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAllSubInfoCount(callingPackage);
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

            public SubscriptionInfo getActiveSubscriptionInfo(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    SubscriptionInfo subscriptionInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        subscriptionInfo = Stub.getDefaultImpl();
                        if (subscriptionInfo != 0) {
                            subscriptionInfo = Stub.getDefaultImpl().getActiveSubscriptionInfo(subId, callingPackage);
                            return subscriptionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        subscriptionInfo = (SubscriptionInfo) SubscriptionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        subscriptionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return subscriptionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SubscriptionInfo getActiveSubscriptionInfoForIccId(String iccId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iccId);
                    _data.writeString(callingPackage);
                    SubscriptionInfo subscriptionInfo = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        subscriptionInfo = Stub.getDefaultImpl();
                        if (subscriptionInfo != 0) {
                            subscriptionInfo = Stub.getDefaultImpl().getActiveSubscriptionInfoForIccId(iccId, callingPackage);
                            return subscriptionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        subscriptionInfo = (SubscriptionInfo) SubscriptionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        subscriptionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return subscriptionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    SubscriptionInfo subscriptionInfo = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        subscriptionInfo = Stub.getDefaultImpl();
                        if (subscriptionInfo != 0) {
                            subscriptionInfo = Stub.getDefaultImpl().getActiveSubscriptionInfoForSimSlotIndex(slotIndex, callingPackage);
                            return subscriptionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        subscriptionInfo = (SubscriptionInfo) SubscriptionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        subscriptionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return subscriptionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<SubscriptionInfo> getActiveSubscriptionInfoList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getActiveSubscriptionInfoList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActiveSubInfoCount(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActiveSubInfoCount(callingPackage);
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

            public int getActiveSubInfoCountMax() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActiveSubInfoCountMax();
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

            public List<SubscriptionInfo> getAvailableSubscriptionInfoList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAvailableSubscriptionInfoList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAccessibleSubscriptionInfoList(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestEmbeddedSubscriptionInfoListRefresh(int cardId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestEmbeddedSubscriptionInfoListRefresh(cardId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int addSubInfoRecord(String iccId, int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iccId);
                    _data.writeInt(slotIndex);
                    int i = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addSubInfoRecord(iccId, slotIndex);
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

            public int addSubInfo(String uniqueId, String displayName, int slotIndex, int subscriptionType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uniqueId);
                    _data.writeString(displayName);
                    _data.writeInt(slotIndex);
                    _data.writeInt(subscriptionType);
                    int i = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addSubInfo(uniqueId, displayName, slotIndex, subscriptionType);
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

            public int removeSubInfo(String uniqueId, int subscriptionType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uniqueId);
                    _data.writeInt(subscriptionType);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().removeSubInfo(uniqueId, subscriptionType);
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

            public int setIconTint(int tint, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tint);
                    _data.writeInt(subId);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setIconTint(tint, subId);
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

            public int setDisplayNameUsingSrc(String displayName, int subId, int nameSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(displayName);
                    _data.writeInt(subId);
                    _data.writeInt(nameSource);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setDisplayNameUsingSrc(displayName, subId, nameSource);
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

            public int setDisplayNumber(String number, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    _data.writeInt(subId);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setDisplayNumber(number, subId);
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

            public int setDataRoaming(int roaming, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(roaming);
                    _data.writeInt(subId);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setDataRoaming(roaming, subId);
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

            public int setOpportunistic(boolean opportunistic, int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(opportunistic ? 1 : 0);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = this.mRemote;
                    if (!i.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setOpportunistic(opportunistic, subId, callingPackage);
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

            public ParcelUuid createSubscriptionGroup(int[] subIdList, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(subIdList);
                    _data.writeString(callingPackage);
                    ParcelUuid parcelUuid = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        parcelUuid = Stub.getDefaultImpl();
                        if (parcelUuid != 0) {
                            parcelUuid = Stub.getDefaultImpl().createSubscriptionGroup(subIdList, callingPackage);
                            return parcelUuid;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelUuid = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelUuid = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelUuid;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPreferredDataSubscriptionId(int subId, boolean needValidation, ISetOpportunisticDataCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(needValidation ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPreferredDataSubscriptionId(subId, needValidation, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredDataSubscriptionId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredDataSubscriptionId();
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

            public List<SubscriptionInfo> getOpportunisticSubscriptions(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getOpportunisticSubscriptions(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSubscriptionsFromGroup(int[] subIdList, ParcelUuid groupUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(subIdList);
                    if (groupUuid != null) {
                        _data.writeInt(1);
                        groupUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSubscriptionsFromGroup(subIdList, groupUuid, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSubscriptionsIntoGroup(int[] subIdList, ParcelUuid groupUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(subIdList);
                    if (groupUuid != null) {
                        _data.writeInt(1);
                        groupUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSubscriptionsIntoGroup(subIdList, groupUuid, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<SubscriptionInfo> getSubscriptionsInGroup(ParcelUuid groupUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (groupUuid != null) {
                        _data.writeInt(1);
                        groupUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    List<SubscriptionInfo> list = this.mRemote;
                    if (!list.transact(26, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getSubscriptionsInGroup(groupUuid, callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                    List<SubscriptionInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSlotIndex(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSlotIndex(subId);
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

            public int[] getSubId(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    int[] iArr = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getSubId(slotIndex);
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

            public int getDefaultSubId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultSubId();
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

            public int clearSubInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().clearSubInfo();
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

            public int getPhoneId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPhoneId(subId);
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

            public int getDefaultDataSubId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultDataSubId();
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

            public void setDefaultDataSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultDataSubId(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDefaultVoiceSubId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultVoiceSubId();
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

            public void setDefaultVoiceSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultVoiceSubId(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDefaultSmsSubId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultSmsSubId();
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

            public void setDefaultSmsSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultSmsSubId(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getActiveSubIdList(boolean visibleOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visibleOnly ? 1 : 0);
                    int[] iArr = this.mRemote;
                    if (!iArr.transact(38, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != null) {
                            iArr = Stub.getDefaultImpl().getActiveSubIdList(visibleOnly);
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

            public int setSubscriptionProperty(int subId, String propKey, String propValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(propKey);
                    _data.writeString(propValue);
                    int i = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setSubscriptionProperty(subId, propKey, propValue);
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

            public String getSubscriptionProperty(int subId, String propKey, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(propKey);
                    _data.writeString(callingPackage);
                    String str = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriptionProperty(subId, propKey, callingPackage);
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

            public boolean setSubscriptionEnabled(boolean enable, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setSubscriptionEnabled(enable, subId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSubscriptionEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSubscriptionEnabled(subId);
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

            public int getEnabledSubscriptionId(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    int i = 43;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getEnabledSubscriptionId(slotIndex);
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

            public int getSimStateForSlotIndex(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    int i = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSimStateForSlotIndex(slotIndex);
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

            public boolean isActiveSubId(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isActiveSubId(subId, callingPackage);
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

            public boolean setAlwaysAllowMmsData(int subId, boolean alwaysAllow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _result = true;
                    _data.writeInt(alwaysAllow ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setAlwaysAllowMmsData(subId, alwaysAllow);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActiveDataSubscriptionId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActiveDataSubscriptionId();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISub asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISub)) {
                return new Proxy(obj);
            }
            return (ISub) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAllSubInfoList";
                case 2:
                    return "getAllSubInfoCount";
                case 3:
                    return "getActiveSubscriptionInfo";
                case 4:
                    return "getActiveSubscriptionInfoForIccId";
                case 5:
                    return "getActiveSubscriptionInfoForSimSlotIndex";
                case 6:
                    return "getActiveSubscriptionInfoList";
                case 7:
                    return "getActiveSubInfoCount";
                case 8:
                    return "getActiveSubInfoCountMax";
                case 9:
                    return "getAvailableSubscriptionInfoList";
                case 10:
                    return "getAccessibleSubscriptionInfoList";
                case 11:
                    return "requestEmbeddedSubscriptionInfoListRefresh";
                case 12:
                    return "addSubInfoRecord";
                case 13:
                    return "addSubInfo";
                case 14:
                    return "removeSubInfo";
                case 15:
                    return "setIconTint";
                case 16:
                    return "setDisplayNameUsingSrc";
                case 17:
                    return "setDisplayNumber";
                case 18:
                    return "setDataRoaming";
                case 19:
                    return "setOpportunistic";
                case 20:
                    return "createSubscriptionGroup";
                case 21:
                    return "setPreferredDataSubscriptionId";
                case 22:
                    return "getPreferredDataSubscriptionId";
                case 23:
                    return "getOpportunisticSubscriptions";
                case 24:
                    return "removeSubscriptionsFromGroup";
                case 25:
                    return "addSubscriptionsIntoGroup";
                case 26:
                    return "getSubscriptionsInGroup";
                case 27:
                    return "getSlotIndex";
                case 28:
                    return "getSubId";
                case 29:
                    return "getDefaultSubId";
                case 30:
                    return "clearSubInfo";
                case 31:
                    return "getPhoneId";
                case 32:
                    return "getDefaultDataSubId";
                case 33:
                    return "setDefaultDataSubId";
                case 34:
                    return "getDefaultVoiceSubId";
                case 35:
                    return "setDefaultVoiceSubId";
                case 36:
                    return "getDefaultSmsSubId";
                case 37:
                    return "setDefaultSmsSubId";
                case 38:
                    return "getActiveSubIdList";
                case 39:
                    return "setSubscriptionProperty";
                case 40:
                    return "getSubscriptionProperty";
                case 41:
                    return "setSubscriptionEnabled";
                case 42:
                    return "isSubscriptionEnabled";
                case 43:
                    return "getEnabledSubscriptionId";
                case 44:
                    return "getSimStateForSlotIndex";
                case 45:
                    return "isActiveSubId";
                case 46:
                    return "setAlwaysAllowMmsData";
                case 47:
                    return "getActiveDataSubscriptionId";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg1 = false;
                List<SubscriptionInfo> _result;
                int _result2;
                SubscriptionInfo _result3;
                int _result4;
                int _result5;
                int _result6;
                int[] _arg0;
                ParcelUuid _arg12;
                int[] _result7;
                boolean _result8;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = getAllSubInfoList(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result2 = getAllSubInfoCount(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result3 = getActiveSubscriptionInfo(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result3 = getActiveSubscriptionInfoForIccId(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result3 = getActiveSubscriptionInfoForSimSlotIndex(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = getActiveSubscriptionInfoList(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getActiveSubInfoCount(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result4 = getActiveSubInfoCountMax();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = getAvailableSubscriptionInfoList(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result = getAccessibleSubscriptionInfoList(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        requestEmbeddedSubscriptionInfoListRefresh(data.readInt());
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result5 = addSubInfoRecord(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        int _result9 = addSubInfo(data.readString(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result5 = removeSubInfo(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result5 = setIconTint(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result6 = setDisplayNameUsingSrc(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result5 = setDisplayNumber(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result5 = setDataRoaming(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result6 = setOpportunistic(_arg1, data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        ParcelUuid _result10 = createSubscriptionGroup(data.createIntArray(), data.readString());
                        reply.writeNoException();
                        if (_result10 != null) {
                            reply.writeInt(1);
                            _result10.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setPreferredDataSubscriptionId(_result2, _arg1, com.android.internal.telephony.ISetOpportunisticDataCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        _result4 = getPreferredDataSubscriptionId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        _result = getOpportunisticSubscriptions(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _arg0 = data.createIntArray();
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        removeSubscriptionsFromGroup(_arg0, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        _arg0 = data.createIntArray();
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        addSubscriptionsIntoGroup(_arg0, _arg12, data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        ParcelUuid _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        List<SubscriptionInfo> _result11 = getSubscriptionsInGroup(_arg02, data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result11);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result2 = getSlotIndex(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        _result7 = getSubId(data.readInt());
                        reply.writeNoException();
                        reply.writeIntArray(_result7);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        _result4 = getDefaultSubId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        _result4 = clearSubInfo();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        _result2 = getPhoneId(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        _result4 = getDefaultDataSubId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        setDefaultDataSubId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        _result4 = getDefaultVoiceSubId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 35:
                        data.enforceInterface(descriptor);
                        setDefaultVoiceSubId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 36:
                        data.enforceInterface(descriptor);
                        _result4 = getDefaultSmsSubId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 37:
                        data.enforceInterface(descriptor);
                        setDefaultSmsSubId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 38:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result7 = getActiveSubIdList(_arg1);
                        reply.writeNoException();
                        reply.writeIntArray(_result7);
                        return true;
                    case 39:
                        data.enforceInterface(descriptor);
                        _result6 = setSubscriptionProperty(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 40:
                        data.enforceInterface(descriptor);
                        String _result12 = getSubscriptionProperty(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result12);
                        return true;
                    case 41:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result8 = setSubscriptionEnabled(_arg1, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 42:
                        data.enforceInterface(descriptor);
                        boolean _result13 = isSubscriptionEnabled(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result13);
                        return true;
                    case 43:
                        data.enforceInterface(descriptor);
                        _result2 = getEnabledSubscriptionId(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 44:
                        data.enforceInterface(descriptor);
                        _result2 = getSimStateForSlotIndex(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 45:
                        data.enforceInterface(descriptor);
                        _result8 = isActiveSubId(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 46:
                        data.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result8 = setAlwaysAllowMmsData(_result2, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 47:
                        data.enforceInterface(descriptor);
                        _result4 = getActiveDataSubscriptionId();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISub impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISub getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int addSubInfo(String str, String str2, int i, int i2) throws RemoteException;

    int addSubInfoRecord(String str, int i) throws RemoteException;

    void addSubscriptionsIntoGroup(int[] iArr, ParcelUuid parcelUuid, String str) throws RemoteException;

    int clearSubInfo() throws RemoteException;

    ParcelUuid createSubscriptionGroup(int[] iArr, String str) throws RemoteException;

    List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String str) throws RemoteException;

    int getActiveDataSubscriptionId() throws RemoteException;

    int[] getActiveSubIdList(boolean z) throws RemoteException;

    int getActiveSubInfoCount(String str) throws RemoteException;

    int getActiveSubInfoCountMax() throws RemoteException;

    SubscriptionInfo getActiveSubscriptionInfo(int i, String str) throws RemoteException;

    SubscriptionInfo getActiveSubscriptionInfoForIccId(String str, String str2) throws RemoteException;

    SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int i, String str) throws RemoteException;

    List<SubscriptionInfo> getActiveSubscriptionInfoList(String str) throws RemoteException;

    int getAllSubInfoCount(String str) throws RemoteException;

    List<SubscriptionInfo> getAllSubInfoList(String str) throws RemoteException;

    List<SubscriptionInfo> getAvailableSubscriptionInfoList(String str) throws RemoteException;

    int getDefaultDataSubId() throws RemoteException;

    int getDefaultSmsSubId() throws RemoteException;

    int getDefaultSubId() throws RemoteException;

    int getDefaultVoiceSubId() throws RemoteException;

    int getEnabledSubscriptionId(int i) throws RemoteException;

    List<SubscriptionInfo> getOpportunisticSubscriptions(String str) throws RemoteException;

    int getPhoneId(int i) throws RemoteException;

    int getPreferredDataSubscriptionId() throws RemoteException;

    int getSimStateForSlotIndex(int i) throws RemoteException;

    int getSlotIndex(int i) throws RemoteException;

    int[] getSubId(int i) throws RemoteException;

    String getSubscriptionProperty(int i, String str, String str2) throws RemoteException;

    List<SubscriptionInfo> getSubscriptionsInGroup(ParcelUuid parcelUuid, String str) throws RemoteException;

    boolean isActiveSubId(int i, String str) throws RemoteException;

    boolean isSubscriptionEnabled(int i) throws RemoteException;

    int removeSubInfo(String str, int i) throws RemoteException;

    void removeSubscriptionsFromGroup(int[] iArr, ParcelUuid parcelUuid, String str) throws RemoteException;

    void requestEmbeddedSubscriptionInfoListRefresh(int i) throws RemoteException;

    boolean setAlwaysAllowMmsData(int i, boolean z) throws RemoteException;

    int setDataRoaming(int i, int i2) throws RemoteException;

    void setDefaultDataSubId(int i) throws RemoteException;

    void setDefaultSmsSubId(int i) throws RemoteException;

    void setDefaultVoiceSubId(int i) throws RemoteException;

    int setDisplayNameUsingSrc(String str, int i, int i2) throws RemoteException;

    int setDisplayNumber(String str, int i) throws RemoteException;

    int setIconTint(int i, int i2) throws RemoteException;

    int setOpportunistic(boolean z, int i, String str) throws RemoteException;

    void setPreferredDataSubscriptionId(int i, boolean z, ISetOpportunisticDataCallback iSetOpportunisticDataCallback) throws RemoteException;

    boolean setSubscriptionEnabled(boolean z, int i) throws RemoteException;

    int setSubscriptionProperty(int i, String str, String str2) throws RemoteException;
}
