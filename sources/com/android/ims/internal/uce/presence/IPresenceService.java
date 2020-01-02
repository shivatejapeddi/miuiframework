package com.android.ims.internal.uce.presence;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.StatusCode;
import com.android.ims.internal.uce.common.UceLong;

public interface IPresenceService extends IInterface {

    public static class Default implements IPresenceService {
        public StatusCode getVersion(int presenceServiceHdl) throws RemoteException {
            return null;
        }

        public StatusCode addListener(int presenceServiceHdl, IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl) throws RemoteException {
            return null;
        }

        public StatusCode removeListener(int presenceServiceHdl, UceLong presenceServiceListenerHdl) throws RemoteException {
            return null;
        }

        public StatusCode reenableService(int presenceServiceHdl, int userData) throws RemoteException {
            return null;
        }

        public StatusCode publishMyCap(int presenceServiceHdl, PresCapInfo myCapInfo, int userData) throws RemoteException {
            return null;
        }

        public StatusCode getContactCap(int presenceServiceHdl, String remoteUri, int userData) throws RemoteException {
            return null;
        }

        public StatusCode getContactListCap(int presenceServiceHdl, String[] remoteUriList, int userData) throws RemoteException {
            return null;
        }

        public StatusCode setNewFeatureTag(int presenceServiceHdl, String featureTag, PresServiceInfo serviceInfo, int userData) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPresenceService {
        private static final String DESCRIPTOR = "com.android.ims.internal.uce.presence.IPresenceService";
        static final int TRANSACTION_addListener = 2;
        static final int TRANSACTION_getContactCap = 6;
        static final int TRANSACTION_getContactListCap = 7;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_publishMyCap = 5;
        static final int TRANSACTION_reenableService = 4;
        static final int TRANSACTION_removeListener = 3;
        static final int TRANSACTION_setNewFeatureTag = 8;

        private static class Proxy implements IPresenceService {
            public static IPresenceService sDefaultImpl;
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

            public StatusCode getVersion(int presenceServiceHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    StatusCode statusCode = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getVersion(presenceServiceHdl);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode addListener(int presenceServiceHdl, IPresenceListener presenceServiceListener, UceLong presenceServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    StatusCode _result = null;
                    _data.writeStrongBinder(presenceServiceListener != null ? presenceServiceListener.asBinder() : null);
                    if (presenceServiceListenerHdl != null) {
                        _data.writeInt(1);
                        presenceServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                        }
                        if (_reply.readInt() != 0) {
                            presenceServiceListenerHdl.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addListener(presenceServiceHdl, presenceServiceListener, presenceServiceListenerHdl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode removeListener(int presenceServiceHdl, UceLong presenceServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    if (presenceServiceListenerHdl != null) {
                        _data.writeInt(1);
                        presenceServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    StatusCode statusCode = this.mRemote;
                    if (!statusCode.transact(3, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != null) {
                            statusCode = Stub.getDefaultImpl().removeListener(presenceServiceHdl, presenceServiceListenerHdl);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode reenableService(int presenceServiceHdl, int userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    _data.writeInt(userData);
                    StatusCode statusCode = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().reenableService(presenceServiceHdl, userData);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode publishMyCap(int presenceServiceHdl, PresCapInfo myCapInfo, int userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    if (myCapInfo != null) {
                        _data.writeInt(1);
                        myCapInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userData);
                    StatusCode statusCode = this.mRemote;
                    if (!statusCode.transact(5, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != null) {
                            statusCode = Stub.getDefaultImpl().publishMyCap(presenceServiceHdl, myCapInfo, userData);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode getContactCap(int presenceServiceHdl, String remoteUri, int userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    _data.writeString(remoteUri);
                    _data.writeInt(userData);
                    StatusCode statusCode = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getContactCap(presenceServiceHdl, remoteUri, userData);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode getContactListCap(int presenceServiceHdl, String[] remoteUriList, int userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    _data.writeStringArray(remoteUriList);
                    _data.writeInt(userData);
                    StatusCode statusCode = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getContactListCap(presenceServiceHdl, remoteUriList, userData);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode setNewFeatureTag(int presenceServiceHdl, String featureTag, PresServiceInfo serviceInfo, int userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(presenceServiceHdl);
                    _data.writeString(featureTag);
                    if (serviceInfo != null) {
                        _data.writeInt(1);
                        serviceInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userData);
                    StatusCode statusCode = this.mRemote;
                    if (!statusCode.transact(8, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != null) {
                            statusCode = Stub.getDefaultImpl().setNewFeatureTag(presenceServiceHdl, featureTag, serviceInfo, userData);
                            return statusCode;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusCode = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        statusCode = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusCode;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPresenceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPresenceService)) {
                return new Proxy(obj);
            }
            return (IPresenceService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getVersion";
                case 2:
                    return "addListener";
                case 3:
                    return "removeListener";
                case 4:
                    return "reenableService";
                case 5:
                    return "publishMyCap";
                case 6:
                    return "getContactCap";
                case 7:
                    return "getContactListCap";
                case 8:
                    return "setNewFeatureTag";
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
                StatusCode _result;
                StatusCode _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        StatusCode _result3 = getVersion(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        UceLong _arg2;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        IPresenceListener _arg1 = com.android.ims.internal.uce.presence.IPresenceListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        _result = addListener(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        if (_arg2 != null) {
                            reply.writeInt(1);
                            _arg2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        UceLong _arg12;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (UceLong) UceLong.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        _result2 = removeListener(_arg0, _arg12);
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = reenableService(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        PresCapInfo _arg13;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (PresCapInfo) PresCapInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg13 = null;
                        }
                        _result = publishMyCap(_arg0, _arg13, data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = getContactCap(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = getContactListCap(data.readInt(), data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        PresServiceInfo _arg22;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        String _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (PresServiceInfo) PresServiceInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        StatusCode _result4 = setNewFeatureTag(_arg0, _arg14, _arg22, data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPresenceService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPresenceService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    StatusCode addListener(int i, IPresenceListener iPresenceListener, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactCap(int i, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactListCap(int i, String[] strArr, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getVersion(int i) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode publishMyCap(int i, PresCapInfo presCapInfo, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode reenableService(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode removeListener(int i, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode setNewFeatureTag(int i, String str, PresServiceInfo presServiceInfo, int i2) throws RemoteException;
}
