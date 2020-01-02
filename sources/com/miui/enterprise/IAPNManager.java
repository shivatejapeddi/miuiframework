package com.miui.enterprise;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.miui.enterprise.sdk.APNConfig;
import java.util.List;

public interface IAPNManager extends IInterface {

    public static class Default implements IAPNManager {
        public List<APNConfig> getAPNListForNumeric(String numeric) throws RemoteException {
            return null;
        }

        public List<APNConfig> getAPNList() throws RemoteException {
            return null;
        }

        public APNConfig getAPNForNumeric(String numeric, String name) throws RemoteException {
            return null;
        }

        public APNConfig getAPN(String name) throws RemoteException {
            return null;
        }

        public void addAPNForNumeric(String numeric, APNConfig config) throws RemoteException {
        }

        public boolean addAPN(APNConfig config) throws RemoteException {
            return false;
        }

        public void deleteAPNForNumeric(String numeric, String name) throws RemoteException {
        }

        public boolean deleteAPN(String name) throws RemoteException {
            return false;
        }

        public void editAPNForNumeric(String numeric, String name, APNConfig config) throws RemoteException {
        }

        public boolean editAPN(String name, APNConfig config) throws RemoteException {
            return false;
        }

        public void activeAPNForNumeric(String numeric, String name) throws RemoteException {
        }

        public boolean activeAPN(String name) throws RemoteException {
            return false;
        }

        public boolean resetAPN() throws RemoteException {
            return false;
        }

        public void setAPNActiveMode(int mode) throws RemoteException {
        }

        public int getAPNActiveMode() throws RemoteException {
            return 0;
        }

        public List<String> queryApn(String selections) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAPNManager {
        private static final String DESCRIPTOR = "com.miui.enterprise.IAPNManager";
        static final int TRANSACTION_activeAPN = 12;
        static final int TRANSACTION_activeAPNForNumeric = 11;
        static final int TRANSACTION_addAPN = 6;
        static final int TRANSACTION_addAPNForNumeric = 5;
        static final int TRANSACTION_deleteAPN = 8;
        static final int TRANSACTION_deleteAPNForNumeric = 7;
        static final int TRANSACTION_editAPN = 10;
        static final int TRANSACTION_editAPNForNumeric = 9;
        static final int TRANSACTION_getAPN = 4;
        static final int TRANSACTION_getAPNActiveMode = 15;
        static final int TRANSACTION_getAPNForNumeric = 3;
        static final int TRANSACTION_getAPNList = 2;
        static final int TRANSACTION_getAPNListForNumeric = 1;
        static final int TRANSACTION_queryApn = 16;
        static final int TRANSACTION_resetAPN = 13;
        static final int TRANSACTION_setAPNActiveMode = 14;

        private static class Proxy implements IAPNManager {
            public static IAPNManager sDefaultImpl;
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

            public List<APNConfig> getAPNListForNumeric(String numeric) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    List<APNConfig> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAPNListForNumeric(numeric);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(APNConfig.CREATOR);
                    List<APNConfig> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<APNConfig> getAPNList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<APNConfig> list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAPNList();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(APNConfig.CREATOR);
                    List<APNConfig> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public APNConfig getAPNForNumeric(String numeric, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeString(name);
                    APNConfig aPNConfig = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        aPNConfig = Stub.getDefaultImpl();
                        if (aPNConfig != 0) {
                            aPNConfig = Stub.getDefaultImpl().getAPNForNumeric(numeric, name);
                            return aPNConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        aPNConfig = (APNConfig) APNConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        aPNConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return aPNConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public APNConfig getAPN(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    APNConfig aPNConfig = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        aPNConfig = Stub.getDefaultImpl();
                        if (aPNConfig != 0) {
                            aPNConfig = Stub.getDefaultImpl().getAPN(name);
                            return aPNConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        aPNConfig = (APNConfig) APNConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        aPNConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return aPNConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addAPNForNumeric(String numeric, APNConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addAPNForNumeric(numeric, config);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addAPN(APNConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
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
                    _result = Stub.getDefaultImpl().addAPN(config);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteAPNForNumeric(String numeric, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeString(name);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteAPNForNumeric(numeric, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteAPN(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deleteAPN(name);
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

            public void editAPNForNumeric(String numeric, String name, APNConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeString(name);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().editAPNForNumeric(numeric, name, config);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean editAPN(String name, APNConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean _result = true;
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().editAPN(name, config);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activeAPNForNumeric(String numeric, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(numeric);
                    _data.writeString(name);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activeAPNForNumeric(numeric, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean activeAPN(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().activeAPN(name);
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

            public boolean resetAPN() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().resetAPN();
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

            public void setAPNActiveMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAPNActiveMode(mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAPNActiveMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAPNActiveMode();
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

            public List<String> queryApn(String selections) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(selections);
                    List<String> list = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().queryApn(selections);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAPNManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAPNManager)) {
                return new Proxy(obj);
            }
            return (IAPNManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAPNListForNumeric";
                case 2:
                    return "getAPNList";
                case 3:
                    return "getAPNForNumeric";
                case 4:
                    return "getAPN";
                case 5:
                    return "addAPNForNumeric";
                case 6:
                    return "addAPN";
                case 7:
                    return "deleteAPNForNumeric";
                case 8:
                    return "deleteAPN";
                case 9:
                    return "editAPNForNumeric";
                case 10:
                    return "editAPN";
                case 11:
                    return "activeAPNForNumeric";
                case 12:
                    return "activeAPN";
                case 13:
                    return "resetAPN";
                case 14:
                    return "setAPNActiveMode";
                case 15:
                    return "getAPNActiveMode";
                case 16:
                    return "queryApn";
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
                APNConfig _result;
                String _arg0;
                APNConfig _arg1;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        List<APNConfig> _result3 = getAPNListForNumeric(data.readString());
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        List<APNConfig> _result4 = getAPNList();
                        reply.writeNoException();
                        reply.writeTypedList(_result4);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        APNConfig _result5 = getAPNForNumeric(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getAPN(data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (APNConfig) APNConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        addAPNForNumeric(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 6:
                        APNConfig _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (APNConfig) APNConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = addAPN(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        deleteAPNForNumeric(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result2 = deleteAPN(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _result = (APNConfig) APNConfig.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        editAPNForNumeric(_arg0, _arg12, _result);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (APNConfig) APNConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        boolean _result6 = editAPN(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        activeAPNForNumeric(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result2 = activeAPN(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        boolean _result7 = resetAPN();
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        setAPNActiveMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        int _result8 = getAPNActiveMode();
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        List<String> _result9 = queryApn(data.readString());
                        reply.writeNoException();
                        reply.writeStringList(_result9);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAPNManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAPNManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean activeAPN(String str) throws RemoteException;

    void activeAPNForNumeric(String str, String str2) throws RemoteException;

    boolean addAPN(APNConfig aPNConfig) throws RemoteException;

    void addAPNForNumeric(String str, APNConfig aPNConfig) throws RemoteException;

    boolean deleteAPN(String str) throws RemoteException;

    void deleteAPNForNumeric(String str, String str2) throws RemoteException;

    boolean editAPN(String str, APNConfig aPNConfig) throws RemoteException;

    void editAPNForNumeric(String str, String str2, APNConfig aPNConfig) throws RemoteException;

    APNConfig getAPN(String str) throws RemoteException;

    int getAPNActiveMode() throws RemoteException;

    APNConfig getAPNForNumeric(String str, String str2) throws RemoteException;

    List<APNConfig> getAPNList() throws RemoteException;

    List<APNConfig> getAPNListForNumeric(String str) throws RemoteException;

    List<String> queryApn(String str) throws RemoteException;

    boolean resetAPN() throws RemoteException;

    void setAPNActiveMode(int i) throws RemoteException;
}
