package com.android.ims.internal.uce.options;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.CapInfo;
import com.android.ims.internal.uce.common.StatusCode;
import com.android.ims.internal.uce.common.UceLong;

public interface IOptionsService extends IInterface {

    public static class Default implements IOptionsService {
        public StatusCode getVersion(int optionsServiceHandle) throws RemoteException {
            return null;
        }

        public StatusCode addListener(int optionsServiceHandle, IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
            return null;
        }

        public StatusCode removeListener(int optionsServiceHandle, UceLong optionsServiceListenerHdl) throws RemoteException {
            return null;
        }

        public StatusCode setMyInfo(int optionsServiceHandle, CapInfo capInfo, int reqUserData) throws RemoteException {
            return null;
        }

        public StatusCode getMyInfo(int optionsServiceHandle, int reqUserdata) throws RemoteException {
            return null;
        }

        public StatusCode getContactCap(int optionsServiceHandle, String remoteURI, int reqUserData) throws RemoteException {
            return null;
        }

        public StatusCode getContactListCap(int optionsServiceHandle, String[] remoteURIList, int reqUserData) throws RemoteException {
            return null;
        }

        public StatusCode responseIncomingOptions(int optionsServiceHandle, int tId, int sipResponseCode, String reasonPhrase, OptionsCapInfo capInfo, boolean bContactInBL) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOptionsService {
        private static final String DESCRIPTOR = "com.android.ims.internal.uce.options.IOptionsService";
        static final int TRANSACTION_addListener = 2;
        static final int TRANSACTION_getContactCap = 6;
        static final int TRANSACTION_getContactListCap = 7;
        static final int TRANSACTION_getMyInfo = 5;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_removeListener = 3;
        static final int TRANSACTION_responseIncomingOptions = 8;
        static final int TRANSACTION_setMyInfo = 4;

        private static class Proxy implements IOptionsService {
            public static IOptionsService sDefaultImpl;
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

            public StatusCode getVersion(int optionsServiceHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    StatusCode statusCode = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getVersion(optionsServiceHandle);
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

            public StatusCode addListener(int optionsServiceHandle, IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    StatusCode _result = null;
                    _data.writeStrongBinder(optionsListener != null ? optionsListener.asBinder() : null);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                        }
                        if (_reply.readInt() != 0) {
                            optionsServiceListenerHdl.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().addListener(optionsServiceHandle, optionsListener, optionsServiceListenerHdl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StatusCode removeListener(int optionsServiceHandle, UceLong optionsServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    StatusCode statusCode = this.mRemote;
                    if (!statusCode.transact(3, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != null) {
                            statusCode = Stub.getDefaultImpl().removeListener(optionsServiceHandle, optionsServiceListenerHdl);
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

            public StatusCode setMyInfo(int optionsServiceHandle, CapInfo capInfo, int reqUserData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    if (capInfo != null) {
                        _data.writeInt(1);
                        capInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(reqUserData);
                    StatusCode statusCode = this.mRemote;
                    if (!statusCode.transact(4, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != null) {
                            statusCode = Stub.getDefaultImpl().setMyInfo(optionsServiceHandle, capInfo, reqUserData);
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

            public StatusCode getMyInfo(int optionsServiceHandle, int reqUserdata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeInt(reqUserdata);
                    StatusCode statusCode = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getMyInfo(optionsServiceHandle, reqUserdata);
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

            public StatusCode getContactCap(int optionsServiceHandle, String remoteURI, int reqUserData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeString(remoteURI);
                    _data.writeInt(reqUserData);
                    StatusCode statusCode = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getContactCap(optionsServiceHandle, remoteURI, reqUserData);
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

            public StatusCode getContactListCap(int optionsServiceHandle, String[] remoteURIList, int reqUserData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeStringArray(remoteURIList);
                    _data.writeInt(reqUserData);
                    StatusCode statusCode = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        statusCode = Stub.getDefaultImpl();
                        if (statusCode != 0) {
                            statusCode = Stub.getDefaultImpl().getContactListCap(optionsServiceHandle, remoteURIList, reqUserData);
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

            public StatusCode responseIncomingOptions(int optionsServiceHandle, int tId, int sipResponseCode, String reasonPhrase, OptionsCapInfo capInfo, boolean bContactInBL) throws RemoteException {
                Throwable th;
                int i;
                String str;
                int i2;
                OptionsCapInfo optionsCapInfo = capInfo;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(optionsServiceHandle);
                        try {
                            _data.writeInt(tId);
                        } catch (Throwable th2) {
                            th = th2;
                            i = sipResponseCode;
                            str = reasonPhrase;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(sipResponseCode);
                        } catch (Throwable th3) {
                            th = th3;
                            str = reasonPhrase;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = tId;
                        i = sipResponseCode;
                        str = reasonPhrase;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(reasonPhrase);
                        int i3 = 1;
                        if (optionsCapInfo != null) {
                            _data.writeInt(1);
                            optionsCapInfo.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (!bContactInBL) {
                            i3 = 0;
                        }
                        _data.writeInt(i3);
                        try {
                            StatusCode _result;
                            if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (StatusCode) StatusCode.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().responseIncomingOptions(optionsServiceHandle, tId, sipResponseCode, reasonPhrase, capInfo, bContactInBL);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = optionsServiceHandle;
                    i2 = tId;
                    i = sipResponseCode;
                    str = reasonPhrase;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOptionsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOptionsService)) {
                return new Proxy(obj);
            }
            return (IOptionsService) iin;
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
                    return "setMyInfo";
                case 5:
                    return "getMyInfo";
                case 6:
                    return "getContactCap";
                case 7:
                    return "getContactListCap";
                case 8:
                    return "responseIncomingOptions";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                int _arg0;
                StatusCode _result;
                StatusCode _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        StatusCode _result3 = getVersion(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        UceLong _arg2;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        IOptionsListener _arg1 = com.android.ims.internal.uce.options.IOptionsListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (UceLong) UceLong.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result = addListener(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        if (_arg2 != null) {
                            parcel2.writeInt(1);
                            _arg2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        UceLong _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (UceLong) UceLong.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        _result2 = removeListener(_arg0, _arg12);
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        CapInfo _arg13;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (CapInfo) CapInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result = setMyInfo(_arg0, _arg13, data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = getMyInfo(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result = getContactCap(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result = getContactListCap(data.readInt(), data.createStringArray(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        OptionsCapInfo _arg4;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        int _arg14 = data.readInt();
                        int _arg22 = data.readInt();
                        String _arg3 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (OptionsCapInfo) OptionsCapInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        StatusCode _result4 = responseIncomingOptions(_arg02, _arg14, _arg22, _arg3, _arg4, data.readInt() != 0);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IOptionsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOptionsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    StatusCode addListener(int i, IOptionsListener iOptionsListener, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactCap(int i, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactListCap(int i, String[] strArr, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getMyInfo(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getVersion(int i) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode removeListener(int i, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode responseIncomingOptions(int i, int i2, int i3, String str, OptionsCapInfo optionsCapInfo, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode setMyInfo(int i, CapInfo capInfo, int i2) throws RemoteException;
}
