package com.android.ims.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;

public interface IImsMMTelFeature extends IInterface {

    public static class Default implements IImsMMTelFeature {
        public int startSession(PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
            return 0;
        }

        public void endSession(int sessionId) throws RemoteException {
        }

        public boolean isConnected(int callSessionType, int callType) throws RemoteException {
            return false;
        }

        public boolean isOpened() throws RemoteException {
            return false;
        }

        public int getFeatureStatus() throws RemoteException {
            return 0;
        }

        public void addRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
        }

        public void removeRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
        }

        public ImsCallProfile createCallProfile(int sessionId, int callSessionType, int callType) throws RemoteException {
            return null;
        }

        public IImsCallSession createCallSession(int sessionId, ImsCallProfile profile) throws RemoteException {
            return null;
        }

        public IImsCallSession getPendingCallSession(int sessionId, String callId) throws RemoteException {
            return null;
        }

        public IImsUt getUtInterface() throws RemoteException {
            return null;
        }

        public IImsConfig getConfigInterface() throws RemoteException {
            return null;
        }

        public void turnOnIms() throws RemoteException {
        }

        public void turnOffIms() throws RemoteException {
        }

        public IImsEcbm getEcbmInterface() throws RemoteException {
            return null;
        }

        public void setUiTTYMode(int uiTtyMode, Message onComplete) throws RemoteException {
        }

        public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsMMTelFeature {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsMMTelFeature";
        static final int TRANSACTION_addRegistrationListener = 6;
        static final int TRANSACTION_createCallProfile = 8;
        static final int TRANSACTION_createCallSession = 9;
        static final int TRANSACTION_endSession = 2;
        static final int TRANSACTION_getConfigInterface = 12;
        static final int TRANSACTION_getEcbmInterface = 15;
        static final int TRANSACTION_getFeatureStatus = 5;
        static final int TRANSACTION_getMultiEndpointInterface = 17;
        static final int TRANSACTION_getPendingCallSession = 10;
        static final int TRANSACTION_getUtInterface = 11;
        static final int TRANSACTION_isConnected = 3;
        static final int TRANSACTION_isOpened = 4;
        static final int TRANSACTION_removeRegistrationListener = 7;
        static final int TRANSACTION_setUiTTYMode = 16;
        static final int TRANSACTION_startSession = 1;
        static final int TRANSACTION_turnOffIms = 14;
        static final int TRANSACTION_turnOnIms = 13;

        private static class Proxy implements IImsMMTelFeature {
            public static IImsMMTelFeature sDefaultImpl;
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

            public int startSession(PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                            i = Stub.getDefaultImpl().startSession(incomingCallIntent, listener);
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

            public void endSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endSession(sessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConnected(int callSessionType, int callType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callSessionType);
                    _data.writeInt(callType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConnected(callSessionType, callType);
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

            public boolean isOpened() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOpened();
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

            public int getFeatureStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFeatureStatus();
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

            public void addRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addRegistrationListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeRegistrationListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ImsCallProfile createCallProfile(int sessionId, int callSessionType, int callType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(callSessionType);
                    _data.writeInt(callType);
                    ImsCallProfile imsCallProfile = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().createCallProfile(sessionId, callSessionType, callType);
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

            public IImsCallSession createCallSession(int sessionId, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IImsCallSession iImsCallSession = this.mRemote;
                    if (!iImsCallSession.transact(9, _data, _reply, 0)) {
                        iImsCallSession = Stub.getDefaultImpl();
                        if (iImsCallSession != null) {
                            iImsCallSession = Stub.getDefaultImpl().createCallSession(sessionId, profile);
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

            public IImsCallSession getPendingCallSession(int sessionId, String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeString(callId);
                    IImsCallSession iImsCallSession = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        iImsCallSession = Stub.getDefaultImpl();
                        if (iImsCallSession != 0) {
                            iImsCallSession = Stub.getDefaultImpl().getPendingCallSession(sessionId, callId);
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

            public IImsUt getUtInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsUt iImsUt = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        iImsUt = Stub.getDefaultImpl();
                        if (iImsUt != 0) {
                            iImsUt = Stub.getDefaultImpl().getUtInterface();
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

            public IImsConfig getConfigInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsConfig iImsConfig = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        iImsConfig = Stub.getDefaultImpl();
                        if (iImsConfig != 0) {
                            iImsConfig = Stub.getDefaultImpl().getConfigInterface();
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

            public void turnOnIms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().turnOnIms();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOffIms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().turnOffIms();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsEcbm getEcbmInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsEcbm iImsEcbm = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        iImsEcbm = Stub.getDefaultImpl();
                        if (iImsEcbm != 0) {
                            iImsEcbm = Stub.getDefaultImpl().getEcbmInterface();
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

            public void setUiTTYMode(int uiTtyMode, Message onComplete) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uiTtyMode);
                    if (onComplete != null) {
                        _data.writeInt(1);
                        onComplete.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUiTTYMode(uiTtyMode, onComplete);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsMultiEndpoint iImsMultiEndpoint = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        iImsMultiEndpoint = Stub.getDefaultImpl();
                        if (iImsMultiEndpoint != 0) {
                            iImsMultiEndpoint = Stub.getDefaultImpl().getMultiEndpointInterface();
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

        public static IImsMMTelFeature asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsMMTelFeature)) {
                return new Proxy(obj);
            }
            return (IImsMMTelFeature) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startSession";
                case 2:
                    return "endSession";
                case 3:
                    return "isConnected";
                case 4:
                    return "isOpened";
                case 5:
                    return "getFeatureStatus";
                case 6:
                    return "addRegistrationListener";
                case 7:
                    return "removeRegistrationListener";
                case 8:
                    return "createCallProfile";
                case 9:
                    return "createCallSession";
                case 10:
                    return "getPendingCallSession";
                case 11:
                    return "getUtInterface";
                case 12:
                    return "getConfigInterface";
                case 13:
                    return "turnOnIms";
                case 14:
                    return "turnOffIms";
                case 15:
                    return "getEcbmInterface";
                case 16:
                    return "setUiTTYMode";
                case 17:
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
                int _result;
                IImsCallSession _result2;
                switch (code) {
                    case 1:
                        PendingIntent _arg0;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _result3 = startSession(_arg0, com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        endSession(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        boolean _result4 = isConnected(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        boolean _result5 = isOpened();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = getFeatureStatus();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        addRegistrationListener(com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        removeRegistrationListener(com.android.ims.internal.IImsRegistrationListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        ImsCallProfile _result6 = createCallProfile(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 9:
                        ImsCallProfile _arg1;
                        data.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result2 = createCallSession(_arg02, _arg1);
                        reply.writeNoException();
                        if (_result2 != null) {
                            iBinder = _result2.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result2 = getPendingCallSession(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            iBinder = _result2.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        IImsUt _result7 = getUtInterface();
                        reply.writeNoException();
                        if (_result7 != null) {
                            iBinder = _result7.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        IImsConfig _result8 = getConfigInterface();
                        reply.writeNoException();
                        if (_result8 != null) {
                            iBinder = _result8.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        turnOnIms();
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        turnOffIms();
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        IImsEcbm _result9 = getEcbmInterface();
                        reply.writeNoException();
                        if (_result9 != null) {
                            iBinder = _result9.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 16:
                        Message _arg12;
                        data.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Message) Message.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        setUiTTYMode(_result, _arg12);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        IImsMultiEndpoint _result10 = getMultiEndpointInterface();
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

        public static boolean setDefaultImpl(IImsMMTelFeature impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsMMTelFeature getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addRegistrationListener(IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    ImsCallProfile createCallProfile(int i, int i2, int i3) throws RemoteException;

    IImsCallSession createCallSession(int i, ImsCallProfile imsCallProfile) throws RemoteException;

    void endSession(int i) throws RemoteException;

    IImsConfig getConfigInterface() throws RemoteException;

    IImsEcbm getEcbmInterface() throws RemoteException;

    int getFeatureStatus() throws RemoteException;

    IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException;

    IImsCallSession getPendingCallSession(int i, String str) throws RemoteException;

    IImsUt getUtInterface() throws RemoteException;

    boolean isConnected(int i, int i2) throws RemoteException;

    boolean isOpened() throws RemoteException;

    void removeRegistrationListener(IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    void setUiTTYMode(int i, Message message) throws RemoteException;

    int startSession(PendingIntent pendingIntent, IImsRegistrationListener iImsRegistrationListener) throws RemoteException;

    void turnOffIms() throws RemoteException;

    void turnOnIms() throws RemoteException;
}
