package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothHeadsetPhone extends IInterface {

    public static class Default implements IBluetoothHeadsetPhone {
        public boolean answerCall() throws RemoteException {
            return false;
        }

        public boolean hangupCall() throws RemoteException {
            return false;
        }

        public boolean sendDtmf(int dtmf) throws RemoteException {
            return false;
        }

        public boolean processChld(int chld) throws RemoteException {
            return false;
        }

        public String getNetworkOperator() throws RemoteException {
            return null;
        }

        public String getSubscriberNumber() throws RemoteException {
            return null;
        }

        public boolean listCurrentCalls() throws RemoteException {
            return false;
        }

        public boolean queryPhoneState() throws RemoteException {
            return false;
        }

        public boolean isHighDefCallInProgress() throws RemoteException {
            return false;
        }

        public void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException {
        }

        public void cdmaSwapSecondCallState() throws RemoteException {
        }

        public void cdmaSetSecondCallState(boolean state) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothHeadsetPhone {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHeadsetPhone";
        static final int TRANSACTION_answerCall = 1;
        static final int TRANSACTION_cdmaSetSecondCallState = 12;
        static final int TRANSACTION_cdmaSwapSecondCallState = 11;
        static final int TRANSACTION_getNetworkOperator = 5;
        static final int TRANSACTION_getSubscriberNumber = 6;
        static final int TRANSACTION_hangupCall = 2;
        static final int TRANSACTION_isHighDefCallInProgress = 9;
        static final int TRANSACTION_listCurrentCalls = 7;
        static final int TRANSACTION_processChld = 4;
        static final int TRANSACTION_queryPhoneState = 8;
        static final int TRANSACTION_sendDtmf = 3;
        static final int TRANSACTION_updateBtHandsfreeAfterRadioTechnologyChange = 10;

        private static class Proxy implements IBluetoothHeadsetPhone {
            public static IBluetoothHeadsetPhone sDefaultImpl;
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

            public boolean answerCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                    z = Stub.getDefaultImpl().answerCall();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hangupCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hangupCall();
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

            public boolean sendDtmf(int dtmf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dtmf);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().sendDtmf(dtmf);
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

            public boolean processChld(int chld) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chld);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().processChld(chld);
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

            public String getNetworkOperator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNetworkOperator();
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

            public String getSubscriberNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriberNumber();
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

            public boolean listCurrentCalls() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().listCurrentCalls();
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

            public boolean queryPhoneState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().queryPhoneState();
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

            public boolean isHighDefCallInProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isHighDefCallInProgress();
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

            public void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateBtHandsfreeAfterRadioTechnologyChange();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cdmaSwapSecondCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cdmaSwapSecondCallState();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cdmaSetSecondCallState(boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state ? 1 : 0);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cdmaSetSecondCallState(state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothHeadsetPhone asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothHeadsetPhone)) {
                return new Proxy(obj);
            }
            return (IBluetoothHeadsetPhone) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "answerCall";
                case 2:
                    return "hangupCall";
                case 3:
                    return "sendDtmf";
                case 4:
                    return "processChld";
                case 5:
                    return "getNetworkOperator";
                case 6:
                    return "getSubscriberNumber";
                case 7:
                    return "listCurrentCalls";
                case 8:
                    return "queryPhoneState";
                case 9:
                    return "isHighDefCallInProgress";
                case 10:
                    return "updateBtHandsfreeAfterRadioTechnologyChange";
                case 11:
                    return "cdmaSwapSecondCallState";
                case 12:
                    return "cdmaSetSecondCallState";
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
                boolean _result;
                boolean _result2;
                String _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = answerCall();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = hangupCall();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = sendDtmf(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = processChld(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result3 = getNetworkOperator();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result3 = getSubscriberNumber();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = listCurrentCalls();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = queryPhoneState();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = isHighDefCallInProgress();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        updateBtHandsfreeAfterRadioTechnologyChange();
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        cdmaSwapSecondCallState();
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        cdmaSetSecondCallState(data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothHeadsetPhone impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothHeadsetPhone getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean answerCall() throws RemoteException;

    void cdmaSetSecondCallState(boolean z) throws RemoteException;

    void cdmaSwapSecondCallState() throws RemoteException;

    String getNetworkOperator() throws RemoteException;

    String getSubscriberNumber() throws RemoteException;

    boolean hangupCall() throws RemoteException;

    boolean isHighDefCallInProgress() throws RemoteException;

    boolean listCurrentCalls() throws RemoteException;

    boolean processChld(int i) throws RemoteException;

    boolean queryPhoneState() throws RemoteException;

    boolean sendDtmf(int i) throws RemoteException;

    void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException;
}
