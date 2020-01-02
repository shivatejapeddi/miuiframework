package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CallQuality;
import android.telephony.CellInfo;
import android.telephony.PhoneCapability;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.ims.ImsReasonInfo;
import java.util.List;
import miui.telephony.IMiuiTelephony;

public interface ITelephonyRegistry extends IInterface {

    public static class Default implements ITelephonyRegistry {
        public void addOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        public void addOnOpportunisticSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        public void removeOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        public void listen(String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
        }

        public void listenForSubscriber(int subId, String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
        }

        public void notifyCallState(int state, String incomingNumber) throws RemoteException {
        }

        public void notifyCallStateForPhoneId(int phoneId, int subId, int state, String incomingNumber) throws RemoteException {
        }

        public void notifyServiceStateForPhoneId(int phoneId, int subId, ServiceState state) throws RemoteException {
        }

        public void notifySignalStrengthForPhoneId(int phoneId, int subId, SignalStrength signalStrength) throws RemoteException {
        }

        public void notifyMessageWaitingChangedForPhoneId(int phoneId, int subId, boolean mwi) throws RemoteException {
        }

        public void notifyCallForwardingChanged(boolean cfi) throws RemoteException {
        }

        public void notifyCallForwardingChangedForSubscriber(int subId, boolean cfi) throws RemoteException {
        }

        public void notifyDataActivity(int state) throws RemoteException {
        }

        public void notifyDataActivityForSubscriber(int subId, int state) throws RemoteException {
        }

        public void notifyDataConnection(int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
        }

        public void notifyDataConnectionForSubscriber(int phoneId, int subId, int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
        }

        public void notifyDataConnectionFailed(String apnType) throws RemoteException {
        }

        public void notifyDataConnectionFailedForSubscriber(int phoneId, int subId, String apnType) throws RemoteException {
        }

        public void notifyCellLocation(Bundle cellLocation) throws RemoteException {
        }

        public void notifyCellLocationForSubscriber(int subId, Bundle cellLocation) throws RemoteException {
        }

        public void notifyOtaspChanged(int subId, int otaspMode) throws RemoteException {
        }

        public void notifyCellInfo(List<CellInfo> list) throws RemoteException {
        }

        public void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> list) throws RemoteException {
        }

        public void notifyPhysicalChannelConfigurationForSubscriber(int subId, List<PhysicalChannelConfig> list) throws RemoteException {
        }

        public void notifyPreciseCallState(int phoneId, int subId, int ringingCallState, int foregroundCallState, int backgroundCallState) throws RemoteException {
        }

        public void notifyDisconnectCause(int phoneId, int subId, int disconnectCause, int preciseDisconnectCause) throws RemoteException {
        }

        public void notifyPreciseDataConnectionFailed(int phoneId, int subId, String apnType, String apn, int failCause) throws RemoteException {
        }

        public void notifyCellInfoForSubscriber(int subId, List<CellInfo> list) throws RemoteException {
        }

        public void notifySrvccStateChanged(int subId, int lteState) throws RemoteException {
        }

        public void notifySimActivationStateChangedForPhoneId(int phoneId, int subId, int activationState, int activationType) throws RemoteException {
        }

        public void notifyOemHookRawEventForSubscriber(int phoneId, int subId, byte[] rawData) throws RemoteException {
        }

        public void notifySubscriptionInfoChanged() throws RemoteException {
        }

        public void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException {
        }

        public void notifyCarrierNetworkChange(boolean active) throws RemoteException {
        }

        public void notifyUserMobileDataStateChangedForPhoneId(int phoneId, int subId, boolean state) throws RemoteException {
        }

        public void notifyPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
        }

        public void notifyActiveDataSubIdChanged(int activeDataSubId) throws RemoteException {
        }

        public void notifyRadioPowerStateChanged(int phoneId, int subId, int state) throws RemoteException {
        }

        public void notifyEmergencyNumberList(int phoneId, int subId) throws RemoteException {
        }

        public void notifyCallQualityChanged(CallQuality callQuality, int phoneId, int subId, int callNetworkType) throws RemoteException {
        }

        public void notifyImsDisconnectCause(int subId, ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public void setMiuiTelephony(IMiuiTelephony telephony) throws RemoteException {
        }

        public IMiuiTelephony getMiuiTelephony() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITelephonyRegistry {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephonyRegistry";
        static final int TRANSACTION_addOnOpportunisticSubscriptionsChangedListener = 2;
        static final int TRANSACTION_addOnSubscriptionsChangedListener = 1;
        static final int TRANSACTION_getMiuiTelephony = 43;
        static final int TRANSACTION_listen = 4;
        static final int TRANSACTION_listenForSubscriber = 5;
        static final int TRANSACTION_notifyActiveDataSubIdChanged = 37;
        static final int TRANSACTION_notifyCallForwardingChanged = 11;
        static final int TRANSACTION_notifyCallForwardingChangedForSubscriber = 12;
        static final int TRANSACTION_notifyCallQualityChanged = 40;
        static final int TRANSACTION_notifyCallState = 6;
        static final int TRANSACTION_notifyCallStateForPhoneId = 7;
        static final int TRANSACTION_notifyCarrierNetworkChange = 34;
        static final int TRANSACTION_notifyCellInfo = 22;
        static final int TRANSACTION_notifyCellInfoForSubscriber = 28;
        static final int TRANSACTION_notifyCellLocation = 19;
        static final int TRANSACTION_notifyCellLocationForSubscriber = 20;
        static final int TRANSACTION_notifyDataActivity = 13;
        static final int TRANSACTION_notifyDataActivityForSubscriber = 14;
        static final int TRANSACTION_notifyDataConnection = 15;
        static final int TRANSACTION_notifyDataConnectionFailed = 17;
        static final int TRANSACTION_notifyDataConnectionFailedForSubscriber = 18;
        static final int TRANSACTION_notifyDataConnectionForSubscriber = 16;
        static final int TRANSACTION_notifyDisconnectCause = 26;
        static final int TRANSACTION_notifyEmergencyNumberList = 39;
        static final int TRANSACTION_notifyImsDisconnectCause = 41;
        static final int TRANSACTION_notifyMessageWaitingChangedForPhoneId = 10;
        static final int TRANSACTION_notifyOemHookRawEventForSubscriber = 31;
        static final int TRANSACTION_notifyOpportunisticSubscriptionInfoChanged = 33;
        static final int TRANSACTION_notifyOtaspChanged = 21;
        static final int TRANSACTION_notifyPhoneCapabilityChanged = 36;
        static final int TRANSACTION_notifyPhysicalChannelConfiguration = 23;
        static final int TRANSACTION_notifyPhysicalChannelConfigurationForSubscriber = 24;
        static final int TRANSACTION_notifyPreciseCallState = 25;
        static final int TRANSACTION_notifyPreciseDataConnectionFailed = 27;
        static final int TRANSACTION_notifyRadioPowerStateChanged = 38;
        static final int TRANSACTION_notifyServiceStateForPhoneId = 8;
        static final int TRANSACTION_notifySignalStrengthForPhoneId = 9;
        static final int TRANSACTION_notifySimActivationStateChangedForPhoneId = 30;
        static final int TRANSACTION_notifySrvccStateChanged = 29;
        static final int TRANSACTION_notifySubscriptionInfoChanged = 32;
        static final int TRANSACTION_notifyUserMobileDataStateChangedForPhoneId = 35;
        static final int TRANSACTION_removeOnSubscriptionsChangedListener = 3;
        static final int TRANSACTION_setMiuiTelephony = 42;

        private static class Proxy implements ITelephonyRegistry {
            public static ITelephonyRegistry sDefaultImpl;
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

            public void addOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnSubscriptionsChangedListener(pkg, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addOnOpportunisticSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnOpportunisticSubscriptionsChangedListener(pkg, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeOnSubscriptionsChangedListener(pkg, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void listen(String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(events);
                    _data.writeInt(notifyNow ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().listen(pkg, callback, events, notifyNow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void listenForSubscriber(int subId, String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(events);
                    _data.writeInt(notifyNow ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().listenForSubscriber(subId, pkg, callback, events, notifyNow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCallState(int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCallState(state, incomingNumber);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCallStateForPhoneId(int phoneId, int subId, int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCallStateForPhoneId(phoneId, subId, state, incomingNumber);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyServiceStateForPhoneId(int phoneId, int subId, ServiceState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyServiceStateForPhoneId(phoneId, subId, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySignalStrengthForPhoneId(int phoneId, int subId, SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifySignalStrengthForPhoneId(phoneId, subId, signalStrength);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyMessageWaitingChangedForPhoneId(int phoneId, int subId, boolean mwi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(mwi ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyMessageWaitingChangedForPhoneId(phoneId, subId, mwi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCallForwardingChanged(boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cfi ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCallForwardingChanged(cfi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCallForwardingChangedForSubscriber(int subId, boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(cfi ? 1 : 0);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCallForwardingChangedForSubscriber(subId, cfi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDataActivity(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDataActivity(state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDataActivityForSubscriber(int subId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDataActivityForSubscriber(subId, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDataConnection(int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
                Throwable th;
                String str;
                LinkProperties linkProperties2 = linkProperties;
                NetworkCapabilities networkCapabilities2 = networkCapabilities;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(state);
                        i = 1;
                        _data.writeInt(isDataConnectivityPossible ? 1 : 0);
                    } catch (Throwable th2) {
                        th = th2;
                        str = apn;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(apn);
                        _data.writeString(apnType);
                        if (linkProperties2 != null) {
                            _data.writeInt(1);
                            linkProperties2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (networkCapabilities2 != null) {
                            _data.writeInt(1);
                            networkCapabilities2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(networkType);
                        if (!roaming) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().notifyDataConnection(state, isDataConnectivityPossible, apn, apnType, linkProperties, networkCapabilities, networkType, roaming);
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
                    int i2 = state;
                    str = apn;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void notifyDataConnectionForSubscriber(int phoneId, int subId, int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
                LinkProperties linkProperties2 = linkProperties;
                NetworkCapabilities networkCapabilities2 = networkCapabilities;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    int i = 1;
                    _data.writeInt(isDataConnectivityPossible ? 1 : 0);
                    _data.writeString(apn);
                    _data.writeString(apnType);
                    if (linkProperties2 != null) {
                        _data.writeInt(1);
                        linkProperties2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (networkCapabilities2 != null) {
                        _data.writeInt(1);
                        networkCapabilities2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(networkType);
                    if (!roaming) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDataConnectionForSubscriber(phoneId, subId, state, isDataConnectivityPossible, apn, apnType, linkProperties, networkCapabilities, networkType, roaming);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDataConnectionFailed(String apnType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDataConnectionFailed(apnType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDataConnectionFailedForSubscriber(int phoneId, int subId, String apnType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeString(apnType);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDataConnectionFailedForSubscriber(phoneId, subId, apnType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCellLocation(Bundle cellLocation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cellLocation != null) {
                        _data.writeInt(1);
                        cellLocation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCellLocation(cellLocation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCellLocationForSubscriber(int subId, Bundle cellLocation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (cellLocation != null) {
                        _data.writeInt(1);
                        cellLocation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCellLocationForSubscriber(subId, cellLocation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyOtaspChanged(int subId, int otaspMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(otaspMode);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyOtaspChanged(subId, otaspMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCellInfo(List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(cellInfo);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCellInfo(cellInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(configs);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPhysicalChannelConfiguration(configs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPhysicalChannelConfigurationForSubscriber(int subId, List<PhysicalChannelConfig> configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeTypedList(configs);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPhysicalChannelConfigurationForSubscriber(subId, configs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPreciseCallState(int phoneId, int subId, int ringingCallState, int foregroundCallState, int backgroundCallState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(ringingCallState);
                    _data.writeInt(foregroundCallState);
                    _data.writeInt(backgroundCallState);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPreciseCallState(phoneId, subId, ringingCallState, foregroundCallState, backgroundCallState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDisconnectCause(int phoneId, int subId, int disconnectCause, int preciseDisconnectCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(disconnectCause);
                    _data.writeInt(preciseDisconnectCause);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDisconnectCause(phoneId, subId, disconnectCause, preciseDisconnectCause);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPreciseDataConnectionFailed(int phoneId, int subId, String apnType, String apn, int failCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeString(apnType);
                    _data.writeString(apn);
                    _data.writeInt(failCause);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPreciseDataConnectionFailed(phoneId, subId, apnType, apn, failCause);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCellInfoForSubscriber(int subId, List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeTypedList(cellInfo);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCellInfoForSubscriber(subId, cellInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySrvccStateChanged(int subId, int lteState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(lteState);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifySrvccStateChanged(subId, lteState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySimActivationStateChangedForPhoneId(int phoneId, int subId, int activationState, int activationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    _data.writeInt(activationType);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifySimActivationStateChangedForPhoneId(phoneId, subId, activationState, activationType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyOemHookRawEventForSubscriber(int phoneId, int subId, byte[] rawData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeByteArray(rawData);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyOemHookRawEventForSubscriber(phoneId, subId, rawData);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySubscriptionInfoChanged() throws RemoteException {
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
                    Stub.getDefaultImpl().notifySubscriptionInfoChanged();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException {
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
                    Stub.getDefaultImpl().notifyOpportunisticSubscriptionInfoChanged();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCarrierNetworkChange(boolean active) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(active ? 1 : 0);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCarrierNetworkChange(active);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyUserMobileDataStateChangedForPhoneId(int phoneId, int subId, boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state ? 1 : 0);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyUserMobileDataStateChangedForPhoneId(phoneId, subId, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (capability != null) {
                        _data.writeInt(1);
                        capability.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPhoneCapabilityChanged(capability);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyActiveDataSubIdChanged(int activeDataSubId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activeDataSubId);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyActiveDataSubIdChanged(activeDataSubId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyRadioPowerStateChanged(int phoneId, int subId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyRadioPowerStateChanged(phoneId, subId, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyEmergencyNumberList(int phoneId, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyEmergencyNumberList(phoneId, subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCallQualityChanged(CallQuality callQuality, int phoneId, int subId, int callNetworkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callQuality != null) {
                        _data.writeInt(1);
                        callQuality.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(callNetworkType);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCallQualityChanged(callQuality, phoneId, subId, callNetworkType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyImsDisconnectCause(int subId, ImsReasonInfo imsReasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (imsReasonInfo != null) {
                        _data.writeInt(1);
                        imsReasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyImsDisconnectCause(subId, imsReasonInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMiuiTelephony(IMiuiTelephony telephony) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(telephony != null ? telephony.asBinder() : null);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMiuiTelephony(telephony);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IMiuiTelephony getMiuiTelephony() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IMiuiTelephony iMiuiTelephony = 43;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        iMiuiTelephony = Stub.getDefaultImpl();
                        if (iMiuiTelephony != 0) {
                            iMiuiTelephony = Stub.getDefaultImpl().getMiuiTelephony();
                            return iMiuiTelephony;
                        }
                    }
                    _reply.readException();
                    iMiuiTelephony = miui.telephony.IMiuiTelephony.Stub.asInterface(_reply.readStrongBinder());
                    IMiuiTelephony _result = iMiuiTelephony;
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

        public static ITelephonyRegistry asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelephonyRegistry)) {
                return new Proxy(obj);
            }
            return (ITelephonyRegistry) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addOnSubscriptionsChangedListener";
                case 2:
                    return "addOnOpportunisticSubscriptionsChangedListener";
                case 3:
                    return "removeOnSubscriptionsChangedListener";
                case 4:
                    return "listen";
                case 5:
                    return "listenForSubscriber";
                case 6:
                    return "notifyCallState";
                case 7:
                    return "notifyCallStateForPhoneId";
                case 8:
                    return "notifyServiceStateForPhoneId";
                case 9:
                    return "notifySignalStrengthForPhoneId";
                case 10:
                    return "notifyMessageWaitingChangedForPhoneId";
                case 11:
                    return "notifyCallForwardingChanged";
                case 12:
                    return "notifyCallForwardingChangedForSubscriber";
                case 13:
                    return "notifyDataActivity";
                case 14:
                    return "notifyDataActivityForSubscriber";
                case 15:
                    return "notifyDataConnection";
                case 16:
                    return "notifyDataConnectionForSubscriber";
                case 17:
                    return "notifyDataConnectionFailed";
                case 18:
                    return "notifyDataConnectionFailedForSubscriber";
                case 19:
                    return "notifyCellLocation";
                case 20:
                    return "notifyCellLocationForSubscriber";
                case 21:
                    return "notifyOtaspChanged";
                case 22:
                    return "notifyCellInfo";
                case 23:
                    return "notifyPhysicalChannelConfiguration";
                case 24:
                    return "notifyPhysicalChannelConfigurationForSubscriber";
                case 25:
                    return "notifyPreciseCallState";
                case 26:
                    return "notifyDisconnectCause";
                case 27:
                    return "notifyPreciseDataConnectionFailed";
                case 28:
                    return "notifyCellInfoForSubscriber";
                case 29:
                    return "notifySrvccStateChanged";
                case 30:
                    return "notifySimActivationStateChangedForPhoneId";
                case 31:
                    return "notifyOemHookRawEventForSubscriber";
                case 32:
                    return "notifySubscriptionInfoChanged";
                case 33:
                    return "notifyOpportunisticSubscriptionInfoChanged";
                case 34:
                    return "notifyCarrierNetworkChange";
                case 35:
                    return "notifyUserMobileDataStateChangedForPhoneId";
                case 36:
                    return "notifyPhoneCapabilityChanged";
                case 37:
                    return "notifyActiveDataSubIdChanged";
                case 38:
                    return "notifyRadioPowerStateChanged";
                case 39:
                    return "notifyEmergencyNumberList";
                case 40:
                    return "notifyCallQualityChanged";
                case 41:
                    return "notifyImsDisconnectCause";
                case 42:
                    return "setMiuiTelephony";
                case 43:
                    return "getMiuiTelephony";
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
                int _arg0;
                int _arg1;
                int _arg12;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        addOnSubscriptionsChangedListener(data.readString(), com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        addOnOpportunisticSubscriptionsChangedListener(data.readString(), com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        removeOnSubscriptionsChangedListener(data.readString(), com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        IPhoneStateListener _arg13 = com.android.internal.telephony.IPhoneStateListener.Stub.asInterface(data.readStrongBinder());
                        int _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        listen(_arg02, _arg13, _arg22, _arg2);
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        listenForSubscriber(data.readInt(), data.readString(), com.android.internal.telephony.IPhoneStateListener.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        notifyCallState(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        notifyCallStateForPhoneId(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        ServiceState _arg23;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg23 = (ServiceState) ServiceState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        notifyServiceStateForPhoneId(_arg0, _arg1, _arg23);
                        reply.writeNoException();
                        return true;
                    case 9:
                        SignalStrength _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg24 = (SignalStrength) SignalStrength.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        notifySignalStrengthForPhoneId(_arg0, _arg1, _arg24);
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg1 = data.readInt();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        notifyMessageWaitingChangedForPhoneId(_arg1, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        notifyCallForwardingChanged(_arg2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        notifyCallForwardingChangedForSubscriber(_arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        notifyDataActivity(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        notifyDataActivityForSubscriber(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        LinkProperties _arg4;
                        NetworkCapabilities _arg5;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        boolean _arg14 = data.readInt() != 0;
                        String _arg25 = data.readString();
                        String _arg3 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (LinkProperties) LinkProperties.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        notifyDataConnection(_arg03, _arg14, _arg25, _arg3, _arg4, _arg5, data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 16:
                        LinkProperties _arg6;
                        NetworkCapabilities _arg7;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        int _arg15 = data.readInt();
                        int _arg26 = data.readInt();
                        boolean _arg32 = data.readInt() != 0;
                        String _arg42 = data.readString();
                        String _arg52 = data.readString();
                        if (data.readInt() != 0) {
                            _arg6 = (LinkProperties) LinkProperties.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg7 = (NetworkCapabilities) NetworkCapabilities.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        notifyDataConnectionForSubscriber(_arg04, _arg15, _arg26, _arg32, _arg42, _arg52, _arg6, _arg7, data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        notifyDataConnectionFailed(data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        notifyDataConnectionFailedForSubscriber(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        Bundle _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        notifyCellLocation(_arg05);
                        reply.writeNoException();
                        return true;
                    case 20:
                        Bundle _arg16;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg16 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        notifyCellLocationForSubscriber(_arg0, _arg16);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        notifyOtaspChanged(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        notifyCellInfo(parcel.createTypedArrayList(CellInfo.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        notifyPhysicalChannelConfiguration(parcel.createTypedArrayList(PhysicalChannelConfig.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        notifyPhysicalChannelConfigurationForSubscriber(data.readInt(), parcel.createTypedArrayList(PhysicalChannelConfig.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        notifyPreciseCallState(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        notifyDisconnectCause(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        notifyPreciseDataConnectionFailed(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        notifyCellInfoForSubscriber(data.readInt(), parcel.createTypedArrayList(CellInfo.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        notifySrvccStateChanged(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        notifySimActivationStateChangedForPhoneId(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        notifyOemHookRawEventForSubscriber(data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        notifySubscriptionInfoChanged();
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        notifyOpportunisticSubscriptionInfoChanged();
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        notifyCarrierNetworkChange(_arg2);
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg1 = data.readInt();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        notifyUserMobileDataStateChangedForPhoneId(_arg1, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 36:
                        PhoneCapability _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (PhoneCapability) PhoneCapability.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        notifyPhoneCapabilityChanged(_arg06);
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        notifyActiveDataSubIdChanged(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        notifyRadioPowerStateChanged(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        notifyEmergencyNumberList(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        CallQuality _arg07;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (CallQuality) CallQuality.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        notifyCallQualityChanged(_arg07, data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 41:
                        ImsReasonInfo _arg17;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg17 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        notifyImsDisconnectCause(_arg0, _arg17);
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        setMiuiTelephony(miui.telephony.IMiuiTelephony.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        IMiuiTelephony _result = getMiuiTelephony();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result != null ? _result.asBinder() : null);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITelephonyRegistry impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITelephonyRegistry getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addOnOpportunisticSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    void addOnSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    IMiuiTelephony getMiuiTelephony() throws RemoteException;

    @UnsupportedAppUsage
    void listen(String str, IPhoneStateListener iPhoneStateListener, int i, boolean z) throws RemoteException;

    void listenForSubscriber(int i, String str, IPhoneStateListener iPhoneStateListener, int i2, boolean z) throws RemoteException;

    void notifyActiveDataSubIdChanged(int i) throws RemoteException;

    void notifyCallForwardingChanged(boolean z) throws RemoteException;

    void notifyCallForwardingChangedForSubscriber(int i, boolean z) throws RemoteException;

    void notifyCallQualityChanged(CallQuality callQuality, int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void notifyCallState(int i, String str) throws RemoteException;

    void notifyCallStateForPhoneId(int i, int i2, int i3, String str) throws RemoteException;

    void notifyCarrierNetworkChange(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void notifyCellInfo(List<CellInfo> list) throws RemoteException;

    void notifyCellInfoForSubscriber(int i, List<CellInfo> list) throws RemoteException;

    void notifyCellLocation(Bundle bundle) throws RemoteException;

    void notifyCellLocationForSubscriber(int i, Bundle bundle) throws RemoteException;

    void notifyDataActivity(int i) throws RemoteException;

    void notifyDataActivityForSubscriber(int i, int i2) throws RemoteException;

    void notifyDataConnection(int i, boolean z, String str, String str2, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i2, boolean z2) throws RemoteException;

    @UnsupportedAppUsage
    void notifyDataConnectionFailed(String str) throws RemoteException;

    void notifyDataConnectionFailedForSubscriber(int i, int i2, String str) throws RemoteException;

    void notifyDataConnectionForSubscriber(int i, int i2, int i3, boolean z, String str, String str2, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i4, boolean z2) throws RemoteException;

    void notifyDisconnectCause(int i, int i2, int i3, int i4) throws RemoteException;

    void notifyEmergencyNumberList(int i, int i2) throws RemoteException;

    void notifyImsDisconnectCause(int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void notifyMessageWaitingChangedForPhoneId(int i, int i2, boolean z) throws RemoteException;

    void notifyOemHookRawEventForSubscriber(int i, int i2, byte[] bArr) throws RemoteException;

    void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException;

    void notifyOtaspChanged(int i, int i2) throws RemoteException;

    void notifyPhoneCapabilityChanged(PhoneCapability phoneCapability) throws RemoteException;

    void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> list) throws RemoteException;

    void notifyPhysicalChannelConfigurationForSubscriber(int i, List<PhysicalChannelConfig> list) throws RemoteException;

    void notifyPreciseCallState(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void notifyPreciseDataConnectionFailed(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void notifyRadioPowerStateChanged(int i, int i2, int i3) throws RemoteException;

    void notifyServiceStateForPhoneId(int i, int i2, ServiceState serviceState) throws RemoteException;

    void notifySignalStrengthForPhoneId(int i, int i2, SignalStrength signalStrength) throws RemoteException;

    void notifySimActivationStateChangedForPhoneId(int i, int i2, int i3, int i4) throws RemoteException;

    void notifySrvccStateChanged(int i, int i2) throws RemoteException;

    void notifySubscriptionInfoChanged() throws RemoteException;

    void notifyUserMobileDataStateChangedForPhoneId(int i, int i2, boolean z) throws RemoteException;

    void removeOnSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    void setMiuiTelephony(IMiuiTelephony iMiuiTelephony) throws RemoteException;
}
