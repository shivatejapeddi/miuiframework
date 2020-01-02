package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INfcTag extends IInterface {

    public static class Default implements INfcTag {
        public int connect(int nativeHandle, int technology) throws RemoteException {
            return 0;
        }

        public int reconnect(int nativeHandle) throws RemoteException {
            return 0;
        }

        public int[] getTechList(int nativeHandle) throws RemoteException {
            return null;
        }

        public boolean isNdef(int nativeHandle) throws RemoteException {
            return false;
        }

        public boolean isPresent(int nativeHandle) throws RemoteException {
            return false;
        }

        public TransceiveResult transceive(int nativeHandle, byte[] data, boolean raw) throws RemoteException {
            return null;
        }

        public NdefMessage ndefRead(int nativeHandle) throws RemoteException {
            return null;
        }

        public int ndefWrite(int nativeHandle, NdefMessage msg) throws RemoteException {
            return 0;
        }

        public int ndefMakeReadOnly(int nativeHandle) throws RemoteException {
            return 0;
        }

        public boolean ndefIsWritable(int nativeHandle) throws RemoteException {
            return false;
        }

        public int formatNdef(int nativeHandle, byte[] key) throws RemoteException {
            return 0;
        }

        public Tag rediscover(int nativehandle) throws RemoteException {
            return null;
        }

        public int setTimeout(int technology, int timeout) throws RemoteException {
            return 0;
        }

        public int getTimeout(int technology) throws RemoteException {
            return 0;
        }

        public void resetTimeouts() throws RemoteException {
        }

        public boolean canMakeReadOnly(int ndefType) throws RemoteException {
            return false;
        }

        public int getMaxTransceiveLength(int technology) throws RemoteException {
            return 0;
        }

        public boolean getExtendedLengthApdusSupported() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INfcTag {
        private static final String DESCRIPTOR = "android.nfc.INfcTag";
        static final int TRANSACTION_canMakeReadOnly = 16;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_formatNdef = 11;
        static final int TRANSACTION_getExtendedLengthApdusSupported = 18;
        static final int TRANSACTION_getMaxTransceiveLength = 17;
        static final int TRANSACTION_getTechList = 3;
        static final int TRANSACTION_getTimeout = 14;
        static final int TRANSACTION_isNdef = 4;
        static final int TRANSACTION_isPresent = 5;
        static final int TRANSACTION_ndefIsWritable = 10;
        static final int TRANSACTION_ndefMakeReadOnly = 9;
        static final int TRANSACTION_ndefRead = 7;
        static final int TRANSACTION_ndefWrite = 8;
        static final int TRANSACTION_reconnect = 2;
        static final int TRANSACTION_rediscover = 12;
        static final int TRANSACTION_resetTimeouts = 15;
        static final int TRANSACTION_setTimeout = 13;
        static final int TRANSACTION_transceive = 6;

        private static class Proxy implements INfcTag {
            public static INfcTag sDefaultImpl;
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

            public int connect(int nativeHandle, int technology) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    _data.writeInt(technology);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().connect(nativeHandle, technology);
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

            public int reconnect(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().reconnect(nativeHandle);
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

            public int[] getTechList(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    int[] iArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getTechList(nativeHandle);
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

            public boolean isNdef(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNdef(nativeHandle);
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

            public boolean isPresent(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPresent(nativeHandle);
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

            public TransceiveResult transceive(int nativeHandle, byte[] data, boolean raw) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    _data.writeByteArray(data);
                    _data.writeInt(raw ? 1 : 0);
                    TransceiveResult transceiveResult = this.mRemote;
                    if (!transceiveResult.transact(6, _data, _reply, 0)) {
                        transceiveResult = Stub.getDefaultImpl();
                        if (transceiveResult != null) {
                            transceiveResult = Stub.getDefaultImpl().transceive(nativeHandle, data, raw);
                            return transceiveResult;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        transceiveResult = (TransceiveResult) TransceiveResult.CREATOR.createFromParcel(_reply);
                    } else {
                        transceiveResult = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return transceiveResult;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NdefMessage ndefRead(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    NdefMessage ndefMessage = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        ndefMessage = Stub.getDefaultImpl();
                        if (ndefMessage != 0) {
                            ndefMessage = Stub.getDefaultImpl().ndefRead(nativeHandle);
                            return ndefMessage;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        ndefMessage = (NdefMessage) NdefMessage.CREATOR.createFromParcel(_reply);
                    } else {
                        ndefMessage = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return ndefMessage;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int ndefWrite(int nativeHandle, NdefMessage msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    if (msg != null) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().ndefWrite(nativeHandle, msg);
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

            public int ndefMakeReadOnly(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().ndefMakeReadOnly(nativeHandle);
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

            public boolean ndefIsWritable(int nativeHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().ndefIsWritable(nativeHandle);
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

            public int formatNdef(int nativeHandle, byte[] key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    _data.writeByteArray(key);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().formatNdef(nativeHandle, key);
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

            public Tag rediscover(int nativehandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativehandle);
                    Tag tag = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        tag = Stub.getDefaultImpl();
                        if (tag != 0) {
                            tag = Stub.getDefaultImpl().rediscover(nativehandle);
                            return tag;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        tag = (Tag) Tag.CREATOR.createFromParcel(_reply);
                    } else {
                        tag = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return tag;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setTimeout(int technology, int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(technology);
                    _data.writeInt(timeout);
                    int i = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setTimeout(technology, timeout);
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

            public int getTimeout(int technology) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(technology);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getTimeout(technology);
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

            public void resetTimeouts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetTimeouts();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canMakeReadOnly(int ndefType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ndefType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canMakeReadOnly(ndefType);
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

            public int getMaxTransceiveLength(int technology) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(technology);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMaxTransceiveLength(technology);
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

            public boolean getExtendedLengthApdusSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getExtendedLengthApdusSupported();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INfcTag asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INfcTag)) {
                return new Proxy(obj);
            }
            return (INfcTag) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "connect";
                case 2:
                    return "reconnect";
                case 3:
                    return "getTechList";
                case 4:
                    return "isNdef";
                case 5:
                    return "isPresent";
                case 6:
                    return "transceive";
                case 7:
                    return "ndefRead";
                case 8:
                    return "ndefWrite";
                case 9:
                    return "ndefMakeReadOnly";
                case 10:
                    return "ndefIsWritable";
                case 11:
                    return "formatNdef";
                case 12:
                    return "rediscover";
                case 13:
                    return "setTimeout";
                case 14:
                    return "getTimeout";
                case 15:
                    return "resetTimeouts";
                case 16:
                    return "canMakeReadOnly";
                case 17:
                    return "getMaxTransceiveLength";
                case 18:
                    return "getExtendedLengthApdusSupported";
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
                int _result;
                int _result2;
                boolean _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = connect(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result2 = reconnect(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        int[] _result4 = getTechList(data.readInt());
                        reply.writeNoException();
                        reply.writeIntArray(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result3 = isNdef(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result3 = isPresent(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        TransceiveResult _result5 = transceive(data.readInt(), data.createByteArray(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        NdefMessage _result6 = ndefRead(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        NdefMessage _arg1;
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (NdefMessage) NdefMessage.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result = ndefWrite(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = ndefMakeReadOnly(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result3 = ndefIsWritable(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result = formatNdef(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        Tag _result7 = rediscover(data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            _result7.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result = setTimeout(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result2 = getTimeout(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        resetTimeouts();
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result3 = canMakeReadOnly(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result2 = getMaxTransceiveLength(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        boolean _result8 = getExtendedLengthApdusSupported();
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INfcTag impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INfcTag getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean canMakeReadOnly(int i) throws RemoteException;

    int connect(int i, int i2) throws RemoteException;

    int formatNdef(int i, byte[] bArr) throws RemoteException;

    boolean getExtendedLengthApdusSupported() throws RemoteException;

    int getMaxTransceiveLength(int i) throws RemoteException;

    int[] getTechList(int i) throws RemoteException;

    int getTimeout(int i) throws RemoteException;

    boolean isNdef(int i) throws RemoteException;

    boolean isPresent(int i) throws RemoteException;

    boolean ndefIsWritable(int i) throws RemoteException;

    int ndefMakeReadOnly(int i) throws RemoteException;

    NdefMessage ndefRead(int i) throws RemoteException;

    int ndefWrite(int i, NdefMessage ndefMessage) throws RemoteException;

    int reconnect(int i) throws RemoteException;

    Tag rediscover(int i) throws RemoteException;

    void resetTimeouts() throws RemoteException;

    int setTimeout(int i, int i2) throws RemoteException;

    TransceiveResult transceive(int i, byte[] bArr, boolean z) throws RemoteException;
}
