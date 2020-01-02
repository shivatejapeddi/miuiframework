package android.nfc;

import android.content.ComponentName;
import android.nfc.cardemulation.NfcFServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface INfcFCardEmulation extends IInterface {

    public static class Default implements INfcFCardEmulation {
        public String getSystemCodeForService(int userHandle, ComponentName service) throws RemoteException {
            return null;
        }

        public boolean registerSystemCodeForService(int userHandle, ComponentName service, String systemCode) throws RemoteException {
            return false;
        }

        public boolean removeSystemCodeForService(int userHandle, ComponentName service) throws RemoteException {
            return false;
        }

        public String getNfcid2ForService(int userHandle, ComponentName service) throws RemoteException {
            return null;
        }

        public boolean setNfcid2ForService(int userHandle, ComponentName service, String nfcid2) throws RemoteException {
            return false;
        }

        public boolean enableNfcFForegroundService(ComponentName service) throws RemoteException {
            return false;
        }

        public boolean disableNfcFForegroundService() throws RemoteException {
            return false;
        }

        public List<NfcFServiceInfo> getNfcFServices(int userHandle) throws RemoteException {
            return null;
        }

        public int getMaxNumOfRegisterableSystemCodes() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INfcFCardEmulation {
        private static final String DESCRIPTOR = "android.nfc.INfcFCardEmulation";
        static final int TRANSACTION_disableNfcFForegroundService = 7;
        static final int TRANSACTION_enableNfcFForegroundService = 6;
        static final int TRANSACTION_getMaxNumOfRegisterableSystemCodes = 9;
        static final int TRANSACTION_getNfcFServices = 8;
        static final int TRANSACTION_getNfcid2ForService = 4;
        static final int TRANSACTION_getSystemCodeForService = 1;
        static final int TRANSACTION_registerSystemCodeForService = 2;
        static final int TRANSACTION_removeSystemCodeForService = 3;
        static final int TRANSACTION_setNfcid2ForService = 5;

        private static class Proxy implements INfcFCardEmulation {
            public static INfcFCardEmulation sDefaultImpl;
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

            public String getSystemCodeForService(int userHandle, ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    String str = 0;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSystemCodeForService(userHandle, service);
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

            public boolean registerSystemCodeForService(int userHandle, ComponentName service, String systemCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(systemCode);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().registerSystemCodeForService(userHandle, service, systemCode);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeSystemCodeForService(int userHandle, ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeSystemCodeForService(userHandle, service);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNfcid2ForService(int userHandle, ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(4, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getNfcid2ForService(userHandle, service);
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

            public boolean setNfcid2ForService(int userHandle, ComponentName service, String nfcid2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(nfcid2);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNfcid2ForService(userHandle, service, nfcid2);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableNfcFForegroundService(ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().enableNfcFForegroundService(service);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableNfcFForegroundService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableNfcFForegroundService();
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

            public List<NfcFServiceInfo> getNfcFServices(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    List<NfcFServiceInfo> list = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getNfcFServices(userHandle);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(NfcFServiceInfo.CREATOR);
                    List<NfcFServiceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMaxNumOfRegisterableSystemCodes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMaxNumOfRegisterableSystemCodes();
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

        public static INfcFCardEmulation asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INfcFCardEmulation)) {
                return new Proxy(obj);
            }
            return (INfcFCardEmulation) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getSystemCodeForService";
                case 2:
                    return "registerSystemCodeForService";
                case 3:
                    return "removeSystemCodeForService";
                case 4:
                    return "getNfcid2ForService";
                case 5:
                    return "setNfcid2ForService";
                case 6:
                    return "enableNfcFForegroundService";
                case 7:
                    return "disableNfcFForegroundService";
                case 8:
                    return "getNfcFServices";
                case 9:
                    return "getMaxNumOfRegisterableSystemCodes";
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
                int _arg0;
                ComponentName _arg1;
                String _result;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result = getSystemCodeForService(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = registerSystemCodeForService(_arg0, _arg1, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        boolean _result3 = removeSystemCodeForService(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result = getNfcid2ForService(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = setNfcid2ForService(_arg0, _arg1, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        ComponentName _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        boolean _result4 = enableNfcFForegroundService(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        boolean _result5 = disableNfcFForegroundService();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        List<NfcFServiceInfo> _result6 = getNfcFServices(data.readInt());
                        reply.writeNoException();
                        reply.writeTypedList(_result6);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg0 = getMaxNumOfRegisterableSystemCodes();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INfcFCardEmulation impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INfcFCardEmulation getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean disableNfcFForegroundService() throws RemoteException;

    boolean enableNfcFForegroundService(ComponentName componentName) throws RemoteException;

    int getMaxNumOfRegisterableSystemCodes() throws RemoteException;

    List<NfcFServiceInfo> getNfcFServices(int i) throws RemoteException;

    String getNfcid2ForService(int i, ComponentName componentName) throws RemoteException;

    String getSystemCodeForService(int i, ComponentName componentName) throws RemoteException;

    boolean registerSystemCodeForService(int i, ComponentName componentName, String str) throws RemoteException;

    boolean removeSystemCodeForService(int i, ComponentName componentName) throws RemoteException;

    boolean setNfcid2ForService(int i, ComponentName componentName, String str) throws RemoteException;
}
