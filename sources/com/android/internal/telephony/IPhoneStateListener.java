package com.android.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CallAttributes;
import android.telephony.CellInfo;
import android.telephony.DataConnectionRealTimeInfo;
import android.telephony.PhoneCapability;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseCallState;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.ims.ImsReasonInfo;
import java.util.List;
import java.util.Map;

public interface IPhoneStateListener extends IInterface {

    public static class Default implements IPhoneStateListener {
        public void onServiceStateChanged(ServiceState serviceState) throws RemoteException {
        }

        public void onSignalStrengthChanged(int asu) throws RemoteException {
        }

        public void onMessageWaitingIndicatorChanged(boolean mwi) throws RemoteException {
        }

        public void onCallForwardingIndicatorChanged(boolean cfi) throws RemoteException {
        }

        public void onCellLocationChanged(Bundle location) throws RemoteException {
        }

        public void onCallStateChanged(int state, String incomingNumber) throws RemoteException {
        }

        public void onDataConnectionStateChanged(int state, int networkType) throws RemoteException {
        }

        public void onDataActivity(int direction) throws RemoteException {
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) throws RemoteException {
        }

        public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> list) throws RemoteException {
        }

        public void onOtaspChanged(int otaspMode) throws RemoteException {
        }

        public void onCellInfoChanged(List<CellInfo> list) throws RemoteException {
        }

        public void onPreciseCallStateChanged(PreciseCallState callState) throws RemoteException {
        }

        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) throws RemoteException {
        }

        public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) throws RemoteException {
        }

        public void onSrvccStateChanged(int state) throws RemoteException {
        }

        public void onVoiceActivationStateChanged(int activationState) throws RemoteException {
        }

        public void onDataActivationStateChanged(int activationState) throws RemoteException {
        }

        public void onOemHookRawEvent(byte[] rawData) throws RemoteException {
        }

        public void onCarrierNetworkChange(boolean active) throws RemoteException {
        }

        public void onUserMobileDataStateChanged(boolean enabled) throws RemoteException {
        }

        public void onPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
        }

        public void onActiveDataSubIdChanged(int subId) throws RemoteException {
        }

        public void onRadioPowerStateChanged(int state) throws RemoteException {
        }

        public void onCallAttributesChanged(CallAttributes callAttributes) throws RemoteException {
        }

        public void onEmergencyNumberListChanged(Map emergencyNumberList) throws RemoteException {
        }

        public void onCallDisconnectCauseChanged(int disconnectCause, int preciseDisconnectCause) throws RemoteException {
        }

        public void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPhoneStateListener {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneStateListener";
        static final int TRANSACTION_onActiveDataSubIdChanged = 23;
        static final int TRANSACTION_onCallAttributesChanged = 25;
        static final int TRANSACTION_onCallDisconnectCauseChanged = 27;
        static final int TRANSACTION_onCallForwardingIndicatorChanged = 4;
        static final int TRANSACTION_onCallStateChanged = 6;
        static final int TRANSACTION_onCarrierNetworkChange = 20;
        static final int TRANSACTION_onCellInfoChanged = 12;
        static final int TRANSACTION_onCellLocationChanged = 5;
        static final int TRANSACTION_onDataActivationStateChanged = 18;
        static final int TRANSACTION_onDataActivity = 8;
        static final int TRANSACTION_onDataConnectionRealTimeInfoChanged = 15;
        static final int TRANSACTION_onDataConnectionStateChanged = 7;
        static final int TRANSACTION_onEmergencyNumberListChanged = 26;
        static final int TRANSACTION_onImsCallDisconnectCauseChanged = 28;
        static final int TRANSACTION_onMessageWaitingIndicatorChanged = 3;
        static final int TRANSACTION_onOemHookRawEvent = 19;
        static final int TRANSACTION_onOtaspChanged = 11;
        static final int TRANSACTION_onPhoneCapabilityChanged = 22;
        static final int TRANSACTION_onPhysicalChannelConfigurationChanged = 10;
        static final int TRANSACTION_onPreciseCallStateChanged = 13;
        static final int TRANSACTION_onPreciseDataConnectionStateChanged = 14;
        static final int TRANSACTION_onRadioPowerStateChanged = 24;
        static final int TRANSACTION_onServiceStateChanged = 1;
        static final int TRANSACTION_onSignalStrengthChanged = 2;
        static final int TRANSACTION_onSignalStrengthsChanged = 9;
        static final int TRANSACTION_onSrvccStateChanged = 16;
        static final int TRANSACTION_onUserMobileDataStateChanged = 21;
        static final int TRANSACTION_onVoiceActivationStateChanged = 17;

        private static class Proxy implements IPhoneStateListener {
            public static IPhoneStateListener sDefaultImpl;
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

            public void onServiceStateChanged(ServiceState serviceState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serviceState != null) {
                        _data.writeInt(1);
                        serviceState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onServiceStateChanged(serviceState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSignalStrengthChanged(int asu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(asu);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSignalStrengthChanged(asu);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMessageWaitingIndicatorChanged(boolean mwi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mwi ? 1 : 0);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMessageWaitingIndicatorChanged(mwi);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallForwardingIndicatorChanged(boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cfi ? 1 : 0);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallForwardingIndicatorChanged(cfi);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCellLocationChanged(Bundle location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCellLocationChanged(location);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallStateChanged(int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallStateChanged(state, incomingNumber);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataConnectionStateChanged(int state, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(networkType);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataConnectionStateChanged(state, networkType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataActivity(int direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataActivity(direction);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSignalStrengthsChanged(SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSignalStrengthsChanged(signalStrength);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(configs);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhysicalChannelConfigurationChanged(configs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onOtaspChanged(int otaspMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(otaspMode);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onOtaspChanged(otaspMode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCellInfoChanged(List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(cellInfo);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCellInfoChanged(cellInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPreciseCallStateChanged(PreciseCallState callState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callState != null) {
                        _data.writeInt(1);
                        callState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPreciseCallStateChanged(callState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dataConnectionState != null) {
                        _data.writeInt(1);
                        dataConnectionState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPreciseDataConnectionStateChanged(dataConnectionState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dcRtInfo != null) {
                        _data.writeInt(1);
                        dcRtInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataConnectionRealTimeInfoChanged(dcRtInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSrvccStateChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSrvccStateChanged(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVoiceActivationStateChanged(int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activationState);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVoiceActivationStateChanged(activationState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataActivationStateChanged(int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activationState);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataActivationStateChanged(activationState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onOemHookRawEvent(byte[] rawData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(rawData);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onOemHookRawEvent(rawData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCarrierNetworkChange(boolean active) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(active ? 1 : 0);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCarrierNetworkChange(active);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onUserMobileDataStateChanged(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUserMobileDataStateChanged(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (capability != null) {
                        _data.writeInt(1);
                        capability.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhoneCapabilityChanged(capability);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActiveDataSubIdChanged(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActiveDataSubIdChanged(subId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRadioPowerStateChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRadioPowerStateChanged(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallAttributesChanged(CallAttributes callAttributes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callAttributes != null) {
                        _data.writeInt(1);
                        callAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallAttributesChanged(callAttributes);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onEmergencyNumberListChanged(Map emergencyNumberList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(emergencyNumberList);
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEmergencyNumberListChanged(emergencyNumberList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallDisconnectCauseChanged(int disconnectCause, int preciseDisconnectCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(disconnectCause);
                    _data.writeInt(preciseDisconnectCause);
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallDisconnectCauseChanged(disconnectCause, preciseDisconnectCause);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (imsReasonInfo != null) {
                        _data.writeInt(1);
                        imsReasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onImsCallDisconnectCauseChanged(imsReasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneStateListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPhoneStateListener)) {
                return new Proxy(obj);
            }
            return (IPhoneStateListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onServiceStateChanged";
                case 2:
                    return "onSignalStrengthChanged";
                case 3:
                    return "onMessageWaitingIndicatorChanged";
                case 4:
                    return "onCallForwardingIndicatorChanged";
                case 5:
                    return "onCellLocationChanged";
                case 6:
                    return "onCallStateChanged";
                case 7:
                    return "onDataConnectionStateChanged";
                case 8:
                    return "onDataActivity";
                case 9:
                    return "onSignalStrengthsChanged";
                case 10:
                    return "onPhysicalChannelConfigurationChanged";
                case 11:
                    return "onOtaspChanged";
                case 12:
                    return "onCellInfoChanged";
                case 13:
                    return "onPreciseCallStateChanged";
                case 14:
                    return "onPreciseDataConnectionStateChanged";
                case 15:
                    return "onDataConnectionRealTimeInfoChanged";
                case 16:
                    return "onSrvccStateChanged";
                case 17:
                    return "onVoiceActivationStateChanged";
                case 18:
                    return "onDataActivationStateChanged";
                case 19:
                    return "onOemHookRawEvent";
                case 20:
                    return "onCarrierNetworkChange";
                case 21:
                    return "onUserMobileDataStateChanged";
                case 22:
                    return "onPhoneCapabilityChanged";
                case 23:
                    return "onActiveDataSubIdChanged";
                case 24:
                    return "onRadioPowerStateChanged";
                case 25:
                    return "onCallAttributesChanged";
                case 26:
                    return "onEmergencyNumberListChanged";
                case 27:
                    return "onCallDisconnectCauseChanged";
                case 28:
                    return "onImsCallDisconnectCauseChanged";
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
                boolean _arg0 = false;
                switch (code) {
                    case 1:
                        ServiceState _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ServiceState) ServiceState.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        onServiceStateChanged(_arg02);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onSignalStrengthChanged(data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onMessageWaitingIndicatorChanged(_arg0);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onCallForwardingIndicatorChanged(_arg0);
                        return true;
                    case 5:
                        Bundle _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        onCellLocationChanged(_arg03);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onCallStateChanged(data.readInt(), data.readString());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        onDataConnectionStateChanged(data.readInt(), data.readInt());
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        onDataActivity(data.readInt());
                        return true;
                    case 9:
                        SignalStrength _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (SignalStrength) SignalStrength.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        onSignalStrengthsChanged(_arg04);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        onPhysicalChannelConfigurationChanged(data.createTypedArrayList(PhysicalChannelConfig.CREATOR));
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        onOtaspChanged(data.readInt());
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        onCellInfoChanged(data.createTypedArrayList(CellInfo.CREATOR));
                        return true;
                    case 13:
                        PreciseCallState _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (PreciseCallState) PreciseCallState.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        onPreciseCallStateChanged(_arg05);
                        return true;
                    case 14:
                        PreciseDataConnectionState _arg06;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (PreciseDataConnectionState) PreciseDataConnectionState.CREATOR.createFromParcel(data);
                        } else {
                            _arg06 = null;
                        }
                        onPreciseDataConnectionStateChanged(_arg06);
                        return true;
                    case 15:
                        DataConnectionRealTimeInfo _arg07;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (DataConnectionRealTimeInfo) DataConnectionRealTimeInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg07 = null;
                        }
                        onDataConnectionRealTimeInfoChanged(_arg07);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        onSrvccStateChanged(data.readInt());
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        onVoiceActivationStateChanged(data.readInt());
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        onDataActivationStateChanged(data.readInt());
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        onOemHookRawEvent(data.createByteArray());
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onCarrierNetworkChange(_arg0);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onUserMobileDataStateChanged(_arg0);
                        return true;
                    case 22:
                        PhoneCapability _arg08;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (PhoneCapability) PhoneCapability.CREATOR.createFromParcel(data);
                        } else {
                            _arg08 = null;
                        }
                        onPhoneCapabilityChanged(_arg08);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        onActiveDataSubIdChanged(data.readInt());
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        onRadioPowerStateChanged(data.readInt());
                        return true;
                    case 25:
                        CallAttributes _arg09;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (CallAttributes) CallAttributes.CREATOR.createFromParcel(data);
                        } else {
                            _arg09 = null;
                        }
                        onCallAttributesChanged(_arg09);
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        onEmergencyNumberListChanged(data.readHashMap(getClass().getClassLoader()));
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        onCallDisconnectCauseChanged(data.readInt(), data.readInt());
                        return true;
                    case 28:
                        ImsReasonInfo _arg010;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg010 = null;
                        }
                        onImsCallDisconnectCauseChanged(_arg010);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPhoneStateListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPhoneStateListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onActiveDataSubIdChanged(int i) throws RemoteException;

    void onCallAttributesChanged(CallAttributes callAttributes) throws RemoteException;

    void onCallDisconnectCauseChanged(int i, int i2) throws RemoteException;

    void onCallForwardingIndicatorChanged(boolean z) throws RemoteException;

    void onCallStateChanged(int i, String str) throws RemoteException;

    void onCarrierNetworkChange(boolean z) throws RemoteException;

    void onCellInfoChanged(List<CellInfo> list) throws RemoteException;

    void onCellLocationChanged(Bundle bundle) throws RemoteException;

    void onDataActivationStateChanged(int i) throws RemoteException;

    void onDataActivity(int i) throws RemoteException;

    void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dataConnectionRealTimeInfo) throws RemoteException;

    void onDataConnectionStateChanged(int i, int i2) throws RemoteException;

    void onEmergencyNumberListChanged(Map map) throws RemoteException;

    void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) throws RemoteException;

    void onMessageWaitingIndicatorChanged(boolean z) throws RemoteException;

    void onOemHookRawEvent(byte[] bArr) throws RemoteException;

    void onOtaspChanged(int i) throws RemoteException;

    void onPhoneCapabilityChanged(PhoneCapability phoneCapability) throws RemoteException;

    void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> list) throws RemoteException;

    void onPreciseCallStateChanged(PreciseCallState preciseCallState) throws RemoteException;

    void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) throws RemoteException;

    void onRadioPowerStateChanged(int i) throws RemoteException;

    void onServiceStateChanged(ServiceState serviceState) throws RemoteException;

    void onSignalStrengthChanged(int i) throws RemoteException;

    void onSignalStrengthsChanged(SignalStrength signalStrength) throws RemoteException;

    void onSrvccStateChanged(int i) throws RemoteException;

    void onUserMobileDataStateChanged(boolean z) throws RemoteException;

    void onVoiceActivationStateChanged(int i) throws RemoteException;
}
