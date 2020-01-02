package com.android.internal.telecom;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.CallAudioState;
import android.telecom.ParcelableCall;

public interface IInCallService extends IInterface {

    public static class Default implements IInCallService {
        public void setInCallAdapter(IInCallAdapter inCallAdapter) throws RemoteException {
        }

        public void addCall(ParcelableCall call) throws RemoteException {
        }

        public void updateCall(ParcelableCall call) throws RemoteException {
        }

        public void setPostDial(String callId, String remaining) throws RemoteException {
        }

        public void setPostDialWait(String callId, String remaining) throws RemoteException {
        }

        public void onCallAudioStateChanged(CallAudioState callAudioState) throws RemoteException {
        }

        public void bringToForeground(boolean showDialpad) throws RemoteException {
        }

        public void onCanAddCallChanged(boolean canAddCall) throws RemoteException {
        }

        public void silenceRinger() throws RemoteException {
        }

        public void onConnectionEvent(String callId, String event, Bundle extras) throws RemoteException {
        }

        public void onRttUpgradeRequest(String callId, int id) throws RemoteException {
        }

        public void onRttInitiationFailure(String callId, int reason) throws RemoteException {
        }

        public void onHandoverFailed(String callId, int error) throws RemoteException {
        }

        public void onHandoverComplete(String callId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInCallService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallService";
        static final int TRANSACTION_addCall = 2;
        static final int TRANSACTION_bringToForeground = 7;
        static final int TRANSACTION_onCallAudioStateChanged = 6;
        static final int TRANSACTION_onCanAddCallChanged = 8;
        static final int TRANSACTION_onConnectionEvent = 10;
        static final int TRANSACTION_onHandoverComplete = 14;
        static final int TRANSACTION_onHandoverFailed = 13;
        static final int TRANSACTION_onRttInitiationFailure = 12;
        static final int TRANSACTION_onRttUpgradeRequest = 11;
        static final int TRANSACTION_setInCallAdapter = 1;
        static final int TRANSACTION_setPostDial = 4;
        static final int TRANSACTION_setPostDialWait = 5;
        static final int TRANSACTION_silenceRinger = 9;
        static final int TRANSACTION_updateCall = 3;

        private static class Proxy implements IInCallService {
            public static IInCallService sDefaultImpl;
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

            public void setInCallAdapter(IInCallAdapter inCallAdapter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(inCallAdapter != null ? inCallAdapter.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setInCallAdapter(inCallAdapter);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addCall(ParcelableCall call) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (call != null) {
                        _data.writeInt(1);
                        call.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addCall(call);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateCall(ParcelableCall call) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (call != null) {
                        _data.writeInt(1);
                        call.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateCall(call);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setPostDial(String callId, String remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(remaining);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPostDial(callId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setPostDialWait(String callId, String remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(remaining);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPostDialWait(callId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallAudioStateChanged(CallAudioState callAudioState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callAudioState != null) {
                        _data.writeInt(1);
                        callAudioState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallAudioStateChanged(callAudioState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void bringToForeground(boolean showDialpad) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showDialpad ? 1 : 0);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().bringToForeground(showDialpad);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCanAddCallChanged(boolean canAddCall) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(canAddCall ? 1 : 0);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCanAddCallChanged(canAddCall);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void silenceRinger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().silenceRinger();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onConnectionEvent(String callId, String event, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(event);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnectionEvent(callId, event, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRttUpgradeRequest(String callId, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(id);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRttUpgradeRequest(callId, id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRttInitiationFailure(String callId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRttInitiationFailure(callId, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onHandoverFailed(String callId, int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(error);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onHandoverFailed(callId, error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onHandoverComplete(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onHandoverComplete(callId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInCallService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInCallService)) {
                return new Proxy(obj);
            }
            return (IInCallService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setInCallAdapter";
                case 2:
                    return "addCall";
                case 3:
                    return "updateCall";
                case 4:
                    return "setPostDial";
                case 5:
                    return "setPostDialWait";
                case 6:
                    return "onCallAudioStateChanged";
                case 7:
                    return "bringToForeground";
                case 8:
                    return "onCanAddCallChanged";
                case 9:
                    return "silenceRinger";
                case 10:
                    return "onConnectionEvent";
                case 11:
                    return "onRttUpgradeRequest";
                case 12:
                    return "onRttInitiationFailure";
                case 13:
                    return "onHandoverFailed";
                case 14:
                    return "onHandoverComplete";
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
                ParcelableCall _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        setInCallAdapter(com.android.internal.telecom.IInCallAdapter.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelableCall) ParcelableCall.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        addCall(_arg02);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ParcelableCall) ParcelableCall.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        updateCall(_arg02);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        setPostDial(data.readString(), data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setPostDialWait(data.readString(), data.readString());
                        return true;
                    case 6:
                        CallAudioState _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (CallAudioState) CallAudioState.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        onCallAudioStateChanged(_arg03);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        bringToForeground(_arg0);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onCanAddCallChanged(_arg0);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        silenceRinger();
                        return true;
                    case 10:
                        Bundle _arg2;
                        data.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        String _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        onConnectionEvent(_arg04, _arg1, _arg2);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        onRttUpgradeRequest(data.readString(), data.readInt());
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        onRttInitiationFailure(data.readString(), data.readInt());
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        onHandoverFailed(data.readString(), data.readInt());
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        onHandoverComplete(data.readString());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInCallService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInCallService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addCall(ParcelableCall parcelableCall) throws RemoteException;

    void bringToForeground(boolean z) throws RemoteException;

    void onCallAudioStateChanged(CallAudioState callAudioState) throws RemoteException;

    void onCanAddCallChanged(boolean z) throws RemoteException;

    void onConnectionEvent(String str, String str2, Bundle bundle) throws RemoteException;

    void onHandoverComplete(String str) throws RemoteException;

    void onHandoverFailed(String str, int i) throws RemoteException;

    void onRttInitiationFailure(String str, int i) throws RemoteException;

    void onRttUpgradeRequest(String str, int i) throws RemoteException;

    void setInCallAdapter(IInCallAdapter iInCallAdapter) throws RemoteException;

    void setPostDial(String str, String str2) throws RemoteException;

    void setPostDialWait(String str, String str2) throws RemoteException;

    void silenceRinger() throws RemoteException;

    void updateCall(ParcelableCall parcelableCall) throws RemoteException;
}
