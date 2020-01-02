package com.android.ims.internal.uce.uceservice;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.UceLong;
import com.android.ims.internal.uce.options.IOptionsListener;
import com.android.ims.internal.uce.options.IOptionsService;
import com.android.ims.internal.uce.presence.IPresenceListener;
import com.android.ims.internal.uce.presence.IPresenceService;

public interface IUceService extends IInterface {

    public static abstract class Stub extends Binder implements IUceService {
        private static final String DESCRIPTOR = "com.android.ims.internal.uce.uceservice.IUceService";
        static final int TRANSACTION_createOptionsService = 4;
        static final int TRANSACTION_createOptionsServiceForSubscription = 5;
        static final int TRANSACTION_createPresenceService = 7;
        static final int TRANSACTION_createPresenceServiceForSubscription = 8;
        static final int TRANSACTION_destroyOptionsService = 6;
        static final int TRANSACTION_destroyPresenceService = 9;
        static final int TRANSACTION_getOptionsService = 13;
        static final int TRANSACTION_getOptionsServiceForSubscription = 14;
        static final int TRANSACTION_getPresenceService = 11;
        static final int TRANSACTION_getPresenceServiceForSubscription = 12;
        static final int TRANSACTION_getServiceStatus = 10;
        static final int TRANSACTION_isServiceStarted = 3;
        static final int TRANSACTION_startService = 1;
        static final int TRANSACTION_stopService = 2;

        private static class Proxy implements IUceService {
            public static IUceService sDefaultImpl;
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

            public boolean startService(IUceListener uceListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(uceListener != null ? uceListener.asBinder() : null);
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
                    z = Stub.getDefaultImpl().startService(uceListener);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopService();
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

            public boolean isServiceStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isServiceStarted();
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

            public int createOptionsService(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(optionsListener != null ? optionsListener.asBinder() : null);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createOptionsService(optionsListener, optionsServiceListenerHdl);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        optionsServiceListenerHdl.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createOptionsServiceForSubscription(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl, String iccId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(optionsListener != null ? optionsListener.asBinder() : null);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(iccId);
                    int i = this.mRemote;
                    if (!i.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createOptionsServiceForSubscription(optionsListener, optionsServiceListenerHdl, iccId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        optionsServiceListenerHdl.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyOptionsService(int optionsServiceHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyOptionsService(optionsServiceHandle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createPresenceService(IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(presenceServiceListener != null ? presenceServiceListener.asBinder() : null);
                    if (presenceServiceListenerHdl != null) {
                        _data.writeInt(1);
                        presenceServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createPresenceService(presenceServiceListener, presenceServiceListenerHdl);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        presenceServiceListenerHdl.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createPresenceServiceForSubscription(IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl, String iccId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(presenceServiceListener != null ? presenceServiceListener.asBinder() : null);
                    if (presenceServiceListenerHdl != null) {
                        _data.writeInt(1);
                        presenceServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(iccId);
                    int i = this.mRemote;
                    if (!i.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createPresenceServiceForSubscription(presenceServiceListener, presenceServiceListenerHdl, iccId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        presenceServiceListenerHdl.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return i;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyPresenceService(int presenceServiceHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyPresenceService(presenceServiceHdl);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getServiceStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getServiceStatus();
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

            public IPresenceService getPresenceService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IPresenceService iPresenceService = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        iPresenceService = Stub.getDefaultImpl();
                        if (iPresenceService != 0) {
                            iPresenceService = Stub.getDefaultImpl().getPresenceService();
                            return iPresenceService;
                        }
                    }
                    _reply.readException();
                    iPresenceService = com.android.ims.internal.uce.presence.IPresenceService.Stub.asInterface(_reply.readStrongBinder());
                    IPresenceService _result = iPresenceService;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IPresenceService getPresenceServiceForSubscription(String iccId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iccId);
                    IPresenceService iPresenceService = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        iPresenceService = Stub.getDefaultImpl();
                        if (iPresenceService != 0) {
                            iPresenceService = Stub.getDefaultImpl().getPresenceServiceForSubscription(iccId);
                            return iPresenceService;
                        }
                    }
                    _reply.readException();
                    iPresenceService = com.android.ims.internal.uce.presence.IPresenceService.Stub.asInterface(_reply.readStrongBinder());
                    IPresenceService _result = iPresenceService;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IOptionsService getOptionsService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IOptionsService iOptionsService = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        iOptionsService = Stub.getDefaultImpl();
                        if (iOptionsService != 0) {
                            iOptionsService = Stub.getDefaultImpl().getOptionsService();
                            return iOptionsService;
                        }
                    }
                    _reply.readException();
                    iOptionsService = com.android.ims.internal.uce.options.IOptionsService.Stub.asInterface(_reply.readStrongBinder());
                    IOptionsService _result = iOptionsService;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IOptionsService getOptionsServiceForSubscription(String iccId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iccId);
                    IOptionsService iOptionsService = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        iOptionsService = Stub.getDefaultImpl();
                        if (iOptionsService != 0) {
                            iOptionsService = Stub.getDefaultImpl().getOptionsServiceForSubscription(iccId);
                            return iOptionsService;
                        }
                    }
                    _reply.readException();
                    iOptionsService = com.android.ims.internal.uce.options.IOptionsService.Stub.asInterface(_reply.readStrongBinder());
                    IOptionsService _result = iOptionsService;
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

        public static IUceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUceService)) {
                return new Proxy(obj);
            }
            return (IUceService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startService";
                case 2:
                    return "stopService";
                case 3:
                    return "isServiceStarted";
                case 4:
                    return "createOptionsService";
                case 5:
                    return "createOptionsServiceForSubscription";
                case 6:
                    return "destroyOptionsService";
                case 7:
                    return "createPresenceService";
                case 8:
                    return "createPresenceServiceForSubscription";
                case 9:
                    return "destroyPresenceService";
                case 10:
                    return "getServiceStatus";
                case 11:
                    return "getPresenceService";
                case 12:
                    return "getPresenceServiceForSubscription";
                case 13:
                    return "getOptionsService";
                case 14:
                    return "getOptionsServiceForSubscription";
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
                IBinder iBinder = null;
                boolean _result;
                IOptionsListener _arg0;
                UceLong _arg1;
                int _result2;
                int _result3;
                IPresenceListener _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        boolean _result4 = startService(com.android.ims.internal.uce.uceservice.IUceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = stopService();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = isServiceStarted();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.uce.options.IOptionsListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = createOptionsService(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        if (_arg1 != null) {
                            reply.writeInt(1);
                            _arg1.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.uce.options.IOptionsListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result3 = createOptionsServiceForSubscription(_arg0, _arg1, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        if (_arg1 != null) {
                            reply.writeInt(1);
                            _arg1.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        destroyOptionsService(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg02 = com.android.ims.internal.uce.presence.IPresenceListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = createPresenceService(_arg02, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        if (_arg1 != null) {
                            reply.writeInt(1);
                            _arg1.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg02 = com.android.ims.internal.uce.presence.IPresenceListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result3 = createPresenceServiceForSubscription(_arg02, _arg1, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        if (_arg1 != null) {
                            reply.writeInt(1);
                            _arg1.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        destroyPresenceService(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result = getServiceStatus();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        IPresenceService _result5 = getPresenceService();
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        IPresenceService _result6 = getPresenceServiceForSubscription(data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            iBinder = _result6.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        IOptionsService _result7 = getOptionsService();
                        reply.writeNoException();
                        if (_result7 != null) {
                            iBinder = _result7.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        IOptionsService _result8 = getOptionsServiceForSubscription(data.readString());
                        reply.writeNoException();
                        if (_result8 != null) {
                            iBinder = _result8.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IUceService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUceService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IUceService {
        public boolean startService(IUceListener uceListener) throws RemoteException {
            return false;
        }

        public boolean stopService() throws RemoteException {
            return false;
        }

        public boolean isServiceStarted() throws RemoteException {
            return false;
        }

        public int createOptionsService(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
            return 0;
        }

        public int createOptionsServiceForSubscription(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl, String iccId) throws RemoteException {
            return 0;
        }

        public void destroyOptionsService(int optionsServiceHandle) throws RemoteException {
        }

        public int createPresenceService(IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl) throws RemoteException {
            return 0;
        }

        public int createPresenceServiceForSubscription(IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl, String iccId) throws RemoteException {
            return 0;
        }

        public void destroyPresenceService(int presenceServiceHdl) throws RemoteException {
        }

        public boolean getServiceStatus() throws RemoteException {
            return false;
        }

        public IPresenceService getPresenceService() throws RemoteException {
            return null;
        }

        public IPresenceService getPresenceServiceForSubscription(String iccId) throws RemoteException {
            return null;
        }

        public IOptionsService getOptionsService() throws RemoteException {
            return null;
        }

        public IOptionsService getOptionsServiceForSubscription(String iccId) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    @UnsupportedAppUsage
    int createOptionsService(IOptionsListener iOptionsListener, UceLong uceLong) throws RemoteException;

    int createOptionsServiceForSubscription(IOptionsListener iOptionsListener, UceLong uceLong, String str) throws RemoteException;

    @UnsupportedAppUsage
    int createPresenceService(IPresenceListener iPresenceListener, UceLong uceLong) throws RemoteException;

    int createPresenceServiceForSubscription(IPresenceListener iPresenceListener, UceLong uceLong, String str) throws RemoteException;

    @UnsupportedAppUsage
    void destroyOptionsService(int i) throws RemoteException;

    @UnsupportedAppUsage
    void destroyPresenceService(int i) throws RemoteException;

    @UnsupportedAppUsage
    IOptionsService getOptionsService() throws RemoteException;

    IOptionsService getOptionsServiceForSubscription(String str) throws RemoteException;

    @UnsupportedAppUsage
    IPresenceService getPresenceService() throws RemoteException;

    IPresenceService getPresenceServiceForSubscription(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean getServiceStatus() throws RemoteException;

    @UnsupportedAppUsage
    boolean isServiceStarted() throws RemoteException;

    @UnsupportedAppUsage
    boolean startService(IUceListener iUceListener) throws RemoteException;

    @UnsupportedAppUsage
    boolean stopService() throws RemoteException;
}
