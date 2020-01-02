package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccountHandle;
import android.telephony.ims.ImsCallProfile;
import java.util.List;

public interface IInCallAdapter extends IInterface {

    public static class Default implements IInCallAdapter {
        public void answerCall(String callId, int videoState) throws RemoteException {
        }

        public void deflectCall(String callId, Uri address) throws RemoteException {
        }

        public void rejectCall(String callId, boolean rejectWithMessage, String textMessage) throws RemoteException {
        }

        public void disconnectCall(String callId) throws RemoteException {
        }

        public void holdCall(String callId) throws RemoteException {
        }

        public void unholdCall(String callId) throws RemoteException {
        }

        public void mute(boolean shouldMute) throws RemoteException {
        }

        public void setAudioRoute(int route, String bluetoothAddress) throws RemoteException {
        }

        public void playDtmfTone(String callId, char digit) throws RemoteException {
        }

        public void stopDtmfTone(String callId) throws RemoteException {
        }

        public void postDialContinue(String callId, boolean proceed) throws RemoteException {
        }

        public void phoneAccountSelected(String callId, PhoneAccountHandle accountHandle, boolean setDefault) throws RemoteException {
        }

        public void conference(String callId, String otherCallId) throws RemoteException {
        }

        public void splitFromConference(String callId) throws RemoteException {
        }

        public void mergeConference(String callId) throws RemoteException {
        }

        public void swapConference(String callId) throws RemoteException {
        }

        public void turnOnProximitySensor() throws RemoteException {
        }

        public void turnOffProximitySensor(boolean screenOnImmediately) throws RemoteException {
        }

        public void pullExternalCall(String callId) throws RemoteException {
        }

        public void sendCallEvent(String callId, String event, int targetSdkVer, Bundle extras) throws RemoteException {
        }

        public void putExtras(String callId, Bundle extras) throws RemoteException {
        }

        public void removeExtras(String callId, List<String> list) throws RemoteException {
        }

        public void sendRttRequest(String callId) throws RemoteException {
        }

        public void respondToRttRequest(String callId, int id, boolean accept) throws RemoteException {
        }

        public void stopRtt(String callId) throws RemoteException {
        }

        public void setRttMode(String callId, int mode) throws RemoteException {
        }

        public void handoverTo(String callId, PhoneAccountHandle destAcct, int videoState, Bundle extras) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInCallAdapter {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallAdapter";
        static final int TRANSACTION_answerCall = 1;
        static final int TRANSACTION_conference = 13;
        static final int TRANSACTION_deflectCall = 2;
        static final int TRANSACTION_disconnectCall = 4;
        static final int TRANSACTION_handoverTo = 27;
        static final int TRANSACTION_holdCall = 5;
        static final int TRANSACTION_mergeConference = 15;
        static final int TRANSACTION_mute = 7;
        static final int TRANSACTION_phoneAccountSelected = 12;
        static final int TRANSACTION_playDtmfTone = 9;
        static final int TRANSACTION_postDialContinue = 11;
        static final int TRANSACTION_pullExternalCall = 19;
        static final int TRANSACTION_putExtras = 21;
        static final int TRANSACTION_rejectCall = 3;
        static final int TRANSACTION_removeExtras = 22;
        static final int TRANSACTION_respondToRttRequest = 24;
        static final int TRANSACTION_sendCallEvent = 20;
        static final int TRANSACTION_sendRttRequest = 23;
        static final int TRANSACTION_setAudioRoute = 8;
        static final int TRANSACTION_setRttMode = 26;
        static final int TRANSACTION_splitFromConference = 14;
        static final int TRANSACTION_stopDtmfTone = 10;
        static final int TRANSACTION_stopRtt = 25;
        static final int TRANSACTION_swapConference = 16;
        static final int TRANSACTION_turnOffProximitySensor = 18;
        static final int TRANSACTION_turnOnProximitySensor = 17;
        static final int TRANSACTION_unholdCall = 6;

        private static class Proxy implements IInCallAdapter {
            public static IInCallAdapter sDefaultImpl;
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

            public void answerCall(String callId, int videoState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(videoState);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().answerCall(callId, videoState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deflectCall(String callId, Uri address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deflectCall(callId, address);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void rejectCall(String callId, boolean rejectWithMessage, String textMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(rejectWithMessage ? 1 : 0);
                    _data.writeString(textMessage);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().rejectCall(callId, rejectWithMessage, textMessage);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disconnectCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disconnectCall(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void holdCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().holdCall(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unholdCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unholdCall(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void mute(boolean shouldMute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(shouldMute ? 1 : 0);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().mute(shouldMute);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setAudioRoute(int route, String bluetoothAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(route);
                    _data.writeString(bluetoothAddress);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setAudioRoute(route, bluetoothAddress);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playDtmfTone(String callId, char digit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(digit);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playDtmfTone(callId, digit);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopDtmfTone(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopDtmfTone(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void postDialContinue(String callId, boolean proceed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(proceed ? 1 : 0);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().postDialContinue(callId, proceed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void phoneAccountSelected(String callId, PhoneAccountHandle accountHandle, boolean setDefault) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    int i = 0;
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (setDefault) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().phoneAccountSelected(callId, accountHandle, setDefault);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void conference(String callId, String otherCallId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(otherCallId);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().conference(callId, otherCallId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void splitFromConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().splitFromConference(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void mergeConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().mergeConference(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void swapConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().swapConference(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void turnOnProximitySensor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().turnOnProximitySensor();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void turnOffProximitySensor(boolean screenOnImmediately) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(screenOnImmediately ? 1 : 0);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().turnOffProximitySensor(screenOnImmediately);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void pullExternalCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().pullExternalCall(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendCallEvent(String callId, String event, int targetSdkVer, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(event);
                    _data.writeInt(targetSdkVer);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendCallEvent(callId, event, targetSdkVer, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void putExtras(String callId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().putExtras(callId, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeExtras(String callId, List<String> keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeStringList(keys);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeExtras(callId, keys);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendRttRequest(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendRttRequest(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void respondToRttRequest(String callId, int id, boolean accept) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(id);
                    _data.writeInt(accept ? 1 : 0);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().respondToRttRequest(callId, id, accept);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopRtt(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopRtt(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setRttMode(String callId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setRttMode(callId, mode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handoverTo(String callId, PhoneAccountHandle destAcct, int videoState, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (destAcct != null) {
                        _data.writeInt(1);
                        destAcct.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(videoState);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handoverTo(callId, destAcct, videoState, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInCallAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInCallAdapter)) {
                return new Proxy(obj);
            }
            return (IInCallAdapter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "answerCall";
                case 2:
                    return "deflectCall";
                case 3:
                    return "rejectCall";
                case 4:
                    return "disconnectCall";
                case 5:
                    return "holdCall";
                case 6:
                    return "unholdCall";
                case 7:
                    return "mute";
                case 8:
                    return "setAudioRoute";
                case 9:
                    return "playDtmfTone";
                case 10:
                    return "stopDtmfTone";
                case 11:
                    return "postDialContinue";
                case 12:
                    return "phoneAccountSelected";
                case 13:
                    return ImsCallProfile.EXTRA_CONFERENCE;
                case 14:
                    return "splitFromConference";
                case 15:
                    return "mergeConference";
                case 16:
                    return "swapConference";
                case 17:
                    return "turnOnProximitySensor";
                case 18:
                    return "turnOffProximitySensor";
                case 19:
                    return "pullExternalCall";
                case 20:
                    return "sendCallEvent";
                case 21:
                    return "putExtras";
                case 22:
                    return "removeExtras";
                case 23:
                    return "sendRttRequest";
                case 24:
                    return "respondToRttRequest";
                case 25:
                    return "stopRtt";
                case 26:
                    return "setRttMode";
                case 27:
                    return "handoverTo";
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
                String _arg02;
                String _arg03;
                int _arg2;
                Bundle _arg3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        answerCall(data.readString(), data.readInt());
                        return true;
                    case 2:
                        Uri _arg1;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (Uri) Uri.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        deflectCall(_arg02, _arg1);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        rejectCall(_arg03, _arg0, data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        disconnectCall(data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        holdCall(data.readString());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        unholdCall(data.readString());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        mute(_arg0);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        setAudioRoute(data.readInt(), data.readString());
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        playDtmfTone(data.readString(), (char) data.readInt());
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        stopDtmfTone(data.readString());
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        postDialContinue(_arg03, _arg0);
                        return true;
                    case 12:
                        PhoneAccountHandle _arg12;
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        phoneAccountSelected(_arg03, _arg12, _arg0);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        conference(data.readString(), data.readString());
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        splitFromConference(data.readString());
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        mergeConference(data.readString());
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        swapConference(data.readString());
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        turnOnProximitySensor();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        turnOffProximitySensor(_arg0);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        pullExternalCall(data.readString());
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg03 = data.readString();
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        sendCallEvent(_arg02, _arg03, _arg2, _arg3);
                        return true;
                    case 21:
                        Bundle _arg13;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg13 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg13 = null;
                        }
                        putExtras(_arg02, _arg13);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        removeExtras(data.readString(), data.createStringArrayList());
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        sendRttRequest(data.readString());
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        respondToRttRequest(_arg03, _arg2, _arg0);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        stopRtt(data.readString());
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        setRttMode(data.readString(), data.readInt());
                        return true;
                    case 27:
                        PhoneAccountHandle _arg14;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg14 = null;
                        }
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        handoverTo(_arg02, _arg14, _arg2, _arg3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInCallAdapter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInCallAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void answerCall(String str, int i) throws RemoteException;

    void conference(String str, String str2) throws RemoteException;

    void deflectCall(String str, Uri uri) throws RemoteException;

    void disconnectCall(String str) throws RemoteException;

    void handoverTo(String str, PhoneAccountHandle phoneAccountHandle, int i, Bundle bundle) throws RemoteException;

    void holdCall(String str) throws RemoteException;

    void mergeConference(String str) throws RemoteException;

    void mute(boolean z) throws RemoteException;

    void phoneAccountSelected(String str, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    void playDtmfTone(String str, char c) throws RemoteException;

    void postDialContinue(String str, boolean z) throws RemoteException;

    void pullExternalCall(String str) throws RemoteException;

    void putExtras(String str, Bundle bundle) throws RemoteException;

    void rejectCall(String str, boolean z, String str2) throws RemoteException;

    void removeExtras(String str, List<String> list) throws RemoteException;

    void respondToRttRequest(String str, int i, boolean z) throws RemoteException;

    void sendCallEvent(String str, String str2, int i, Bundle bundle) throws RemoteException;

    void sendRttRequest(String str) throws RemoteException;

    void setAudioRoute(int i, String str) throws RemoteException;

    void setRttMode(String str, int i) throws RemoteException;

    void splitFromConference(String str) throws RemoteException;

    void stopDtmfTone(String str) throws RemoteException;

    void stopRtt(String str) throws RemoteException;

    void swapConference(String str) throws RemoteException;

    void turnOffProximitySensor(boolean z) throws RemoteException;

    void turnOnProximitySensor() throws RemoteException;

    void unholdCall(String str) throws RemoteException;
}
