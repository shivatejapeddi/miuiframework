package com.android.ims.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;

public interface IImsService extends IInterface {

    public static class Default implements IImsService {
        public int open(int phoneId, int serviceClass, PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
            return 0;
        }

        public void close(int serviceId) throws RemoteException {
        }

        public boolean isConnected(int serviceId, int serviceType, int callType) throws RemoteException {
            return false;
        }

        public boolean isOpened(int serviceId) throws RemoteException {
            return false;
        }

        public void setRegistrationListener(int serviceId, IImsRegistrationListener listener) throws RemoteException {
        }

        public void addRegistrationListener(int phoneId, int serviceClass, IImsRegistrationListener listener) throws RemoteException {
        }

        public ImsCallProfile createCallProfile(int serviceId, int serviceType, int callType) throws RemoteException {
            return null;
        }

        public IImsCallSession createCallSession(int serviceId, ImsCallProfile profile, IImsCallSessionListener listener) throws RemoteException {
            return null;
        }

        public IImsCallSession getPendingCallSession(int serviceId, String callId) throws RemoteException {
            return null;
        }

        public IImsUt getUtInterface(int serviceId) throws RemoteException {
            return null;
        }

        public IImsConfig getConfigInterface(int phoneId) throws RemoteException {
            return null;
        }

        public void turnOnIms(int phoneId) throws RemoteException {
        }

        public void turnOffIms(int phoneId) throws RemoteException {
        }

        public IImsEcbm getEcbmInterface(int serviceId) throws RemoteException {
            return null;
        }

        public void setUiTTYMode(int serviceId, int uiTtyMode, Message onComplete) throws RemoteException {
        }

        public IImsMultiEndpoint getMultiEndpointInterface(int serviceId) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsService {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsService";
        static final int TRANSACTION_addRegistrationListener = 6;
        static final int TRANSACTION_close = 2;
        static final int TRANSACTION_createCallProfile = 7;
        static final int TRANSACTION_createCallSession = 8;
        static final int TRANSACTION_getConfigInterface = 11;
        static final int TRANSACTION_getEcbmInterface = 14;
        static final int TRANSACTION_getMultiEndpointInterface = 16;
        static final int TRANSACTION_getPendingCallSession = 9;
        static final int TRANSACTION_getUtInterface = 10;
        static final int TRANSACTION_isConnected = 3;
        static final int TRANSACTION_isOpened = 4;
        static final int TRANSACTION_open = 1;
        static final int TRANSACTION_setRegistrationListener = 5;
        static final int TRANSACTION_setUiTTYMode = 15;
        static final int TRANSACTION_turnOffIms = 13;
        static final int TRANSACTION_turnOnIms = 12;

        private static class Proxy implements IImsService {
            public static IImsService sDefaultImpl;
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

            public int open(int phoneId, int serviceClass, PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(serviceClass);
                    int i = 0;
                    if (incomingCallIntent != null) {
                        _data.writeInt(1);
                        incomingCallIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().open(phoneId, serviceClass, incomingCallIntent, listener);
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

            public void close(int serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().close(serviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConnected(int serviceId, int serviceType, int callType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    _data.writeInt(serviceType);
                    _data.writeInt(callType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConnected(serviceId, serviceType, callType);
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

            public boolean isOpened(int serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOpened(serviceId);
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

            public void setRegistrationListener(int serviceId, IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRegistrationListener(serviceId, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addRegistrationListener(int phoneId, int serviceClass, IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(serviceClass);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addRegistrationListener(phoneId, serviceClass, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ImsCallProfile createCallProfile(int serviceId, int serviceType, int callType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    _data.writeInt(serviceType);
                    _data.writeInt(callType);
                    ImsCallProfile imsCallProfile = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().createCallProfile(serviceId, serviceType, callType);
                            return imsCallProfile;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsCallProfile = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        imsCallProfile = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsCallProfile;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsCallSession createCallSession(int serviceId, ImsCallProfile profile, IImsCallSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    IImsCallSession iImsCallSession = this.mRemote;
                    if (!iImsCallSession.transact(8, _data, _reply, 0)) {
                        iImsCallSession = Stub.getDefaultImpl();
                        if (iImsCallSession != null) {
                            iImsCallSession = Stub.getDefaultImpl().createCallSession(serviceId, profile, listener);
                            return iImsCallSession;
                        }
                    }
                    _reply.readException();
                    iImsCallSession = com.android.ims.internal.IImsCallSession.Stub.asInterface(_reply.readStrongBinder());
                    IImsCallSession _result = iImsCallSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsCallSession getPendingCallSession(int serviceId, String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    _data.writeString(callId);
                    IImsCallSession iImsCallSession = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        iImsCallSession = Stub.getDefaultImpl();
                        if (iImsCallSession != 0) {
                            iImsCallSession = Stub.getDefaultImpl().getPendingCallSession(serviceId, callId);
                            return iImsCallSession;
                        }
                    }
                    _reply.readException();
                    iImsCallSession = com.android.ims.internal.IImsCallSession.Stub.asInterface(_reply.readStrongBinder());
                    IImsCallSession _result = iImsCallSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsUt getUtInterface(int serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    IImsUt iImsUt = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        iImsUt = Stub.getDefaultImpl();
                        if (iImsUt != 0) {
                            iImsUt = Stub.getDefaultImpl().getUtInterface(serviceId);
                            return iImsUt;
                        }
                    }
                    _reply.readException();
                    iImsUt = com.android.ims.internal.IImsUt.Stub.asInterface(_reply.readStrongBinder());
                    IImsUt _result = iImsUt;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsConfig getConfigInterface(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    IImsConfig iImsConfig = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        iImsConfig = Stub.getDefaultImpl();
                        if (iImsConfig != 0) {
                            iImsConfig = Stub.getDefaultImpl().getConfigInterface(phoneId);
                            return iImsConfig;
                        }
                    }
                    _reply.readException();
                    iImsConfig = com.android.ims.internal.IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    IImsConfig _result = iImsConfig;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOnIms(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().turnOnIms(phoneId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOffIms(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().turnOffIms(phoneId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsEcbm getEcbmInterface(int serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    IImsEcbm iImsEcbm = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        iImsEcbm = Stub.getDefaultImpl();
                        if (iImsEcbm != 0) {
                            iImsEcbm = Stub.getDefaultImpl().getEcbmInterface(serviceId);
                            return iImsEcbm;
                        }
                    }
                    _reply.readException();
                    iImsEcbm = com.android.ims.internal.IImsEcbm.Stub.asInterface(_reply.readStrongBinder());
                    IImsEcbm _result = iImsEcbm;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUiTTYMode(int serviceId, int uiTtyMode, Message onComplete) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    _data.writeInt(uiTtyMode);
                    if (onComplete != null) {
                        _data.writeInt(1);
                        onComplete.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUiTTYMode(serviceId, uiTtyMode, onComplete);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMultiEndpoint getMultiEndpointInterface(int serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceId);
                    IImsMultiEndpoint iImsMultiEndpoint = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        iImsMultiEndpoint = Stub.getDefaultImpl();
                        if (iImsMultiEndpoint != 0) {
                            iImsMultiEndpoint = Stub.getDefaultImpl().getMultiEndpointInterface(serviceId);
                            return iImsMultiEndpoint;
                        }
                    }
                    _reply.readException();
                    iImsMultiEndpoint = com.android.ims.internal.IImsMultiEndpoint.Stub.asInterface(_reply.readStrongBinder());
                    IImsMultiEndpoint _result = iImsMultiEndpoint;
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

        public static IImsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsService)) {
                return new Proxy(obj);
            }
            return (IImsService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "open";
                case 2:
                    return "close";
                case 3:
                    return "isConnected";
                case 4:
                    return "isOpened";
                case 5:
                    return "setRegistrationListener";
                case 6:
                    return "addRegistrationListener";
                case 7:
                    return "createCallProfile";
                case 8:
                    return "createCallSession";
                case 9:
                    return "getPendingCallSession";
                case 10:
                    return "getUtInterface";
                case 11:
                    return "getConfigInterface";
                case 12:
                    return "turnOnIms";
                case 13:
                    return "turnOffIms";
                case 14:
                    return "getEcbmInterface";
                case 15:
                    return "setUiTTYMode";
                case 16:
                    return "getMultiEndpointInterface";
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
                int _arg0;
                int _arg1;
                switch (code) {
                    case 1:
                        PendingIntent _arg2;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        int _result = open(_arg0, _arg1, _arg2, com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        close(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        boolean _result2 = isConnected(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        boolean _result3 = isOpened(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setRegistrationListener(data.readInt(), com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        addRegistrationListener(data.readInt(), data.readInt(), com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        ImsCallProfile _result4 = createCallProfile(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        ImsCallProfile _arg12;
                        data.enforceInterface(descriptor);
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        IImsCallSession _result5 = createCallSession(_arg1, _arg12, com.android.ims.internal.IImsCallSessionListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        IImsCallSession _result6 = getPendingCallSession(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            iBinder = _result6.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        IImsUt _result7 = getUtInterface(data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            iBinder = _result7.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        IImsConfig _result8 = getConfigInterface(data.readInt());
                        reply.writeNoException();
                        if (_result8 != null) {
                            iBinder = _result8.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        turnOnIms(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        turnOffIms(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        IImsEcbm _result9 = getEcbmInterface(data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            iBinder = _result9.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 15:
                        Message _arg22;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (Message) Message.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        setUiTTYMode(_arg0, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        IImsMultiEndpoint _result10 = getMultiEndpointInterface(data.readInt());
                        reply.writeNoException();
                        if (_result10 != null) {
                            iBinder = _result10.asBinder();
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

        public static boolean setDefaultImpl(IImsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addRegistrationListener(int i, int i2, IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    void close(int i) throws RemoteException;

    ImsCallProfile createCallProfile(int i, int i2, int i3) throws RemoteException;

    IImsCallSession createCallSession(int i, ImsCallProfile imsCallProfile, IImsCallSessionListener iImsCallSessionListener) throws RemoteException;

    IImsConfig getConfigInterface(int i) throws RemoteException;

    IImsEcbm getEcbmInterface(int i) throws RemoteException;

    IImsMultiEndpoint getMultiEndpointInterface(int i) throws RemoteException;

    IImsCallSession getPendingCallSession(int i, String str) throws RemoteException;

    IImsUt getUtInterface(int i) throws RemoteException;

    boolean isConnected(int i, int i2, int i3) throws RemoteException;

    boolean isOpened(int i) throws RemoteException;

    int open(int i, int i2, PendingIntent pendingIntent, IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    void setRegistrationListener(int i, IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    void setUiTTYMode(int i, int i2, Message message) throws RemoteException;

    void turnOffIms(int i) throws RemoteException;

    void turnOnIms(int i) throws RemoteException;
}
