package com.android.ims.internal.uce.presence;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.StatusCode;

public interface IPresenceListener extends IInterface {

    public static class Default implements IPresenceListener {
        public void getVersionCb(String version) throws RemoteException {
        }

        public void serviceAvailable(StatusCode statusCode) throws RemoteException {
        }

        public void serviceUnAvailable(StatusCode statusCode) throws RemoteException {
        }

        public void publishTriggering(PresPublishTriggerType publishTrigger) throws RemoteException {
        }

        public void cmdStatus(PresCmdStatus cmdStatus) throws RemoteException {
        }

        public void sipResponseReceived(PresSipResponse sipResponse) throws RemoteException {
        }

        public void capInfoReceived(String presentityURI, PresTupleInfo[] tupleInfo) throws RemoteException {
        }

        public void listCapInfoReceived(PresRlmiInfo rlmiInfo, PresResInfo[] resInfo) throws RemoteException {
        }

        public void unpublishMessageSent() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPresenceListener {
        private static final String DESCRIPTOR = "com.android.ims.internal.uce.presence.IPresenceListener";
        static final int TRANSACTION_capInfoReceived = 7;
        static final int TRANSACTION_cmdStatus = 5;
        static final int TRANSACTION_getVersionCb = 1;
        static final int TRANSACTION_listCapInfoReceived = 8;
        static final int TRANSACTION_publishTriggering = 4;
        static final int TRANSACTION_serviceAvailable = 2;
        static final int TRANSACTION_serviceUnAvailable = 3;
        static final int TRANSACTION_sipResponseReceived = 6;
        static final int TRANSACTION_unpublishMessageSent = 9;

        private static class Proxy implements IPresenceListener {
            public static IPresenceListener sDefaultImpl;
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

            public void getVersionCb(String version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(version);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getVersionCb(version);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serviceAvailable(StatusCode statusCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (statusCode != null) {
                        _data.writeInt(1);
                        statusCode.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serviceAvailable(statusCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serviceUnAvailable(StatusCode statusCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (statusCode != null) {
                        _data.writeInt(1);
                        statusCode.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serviceUnAvailable(statusCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void publishTriggering(PresPublishTriggerType publishTrigger) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (publishTrigger != null) {
                        _data.writeInt(1);
                        publishTrigger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().publishTriggering(publishTrigger);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cmdStatus(PresCmdStatus cmdStatus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cmdStatus != null) {
                        _data.writeInt(1);
                        cmdStatus.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cmdStatus(cmdStatus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sipResponseReceived(PresSipResponse sipResponse) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sipResponse != null) {
                        _data.writeInt(1);
                        sipResponse.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sipResponseReceived(sipResponse);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void capInfoReceived(String presentityURI, PresTupleInfo[] tupleInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(presentityURI);
                    _data.writeTypedArray(tupleInfo, 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().capInfoReceived(presentityURI, tupleInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void listCapInfoReceived(PresRlmiInfo rlmiInfo, PresResInfo[] resInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rlmiInfo != null) {
                        _data.writeInt(1);
                        rlmiInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(resInfo, 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().listCapInfoReceived(rlmiInfo, resInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unpublishMessageSent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unpublishMessageSent();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPresenceListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPresenceListener)) {
                return new Proxy(obj);
            }
            return (IPresenceListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getVersionCb";
                case 2:
                    return "serviceAvailable";
                case 3:
                    return "serviceUnAvailable";
                case 4:
                    return "publishTriggering";
                case 5:
                    return "cmdStatus";
                case 6:
                    return "sipResponseReceived";
                case 7:
                    return "capInfoReceived";
                case 8:
                    return "listCapInfoReceived";
                case 9:
                    return "unpublishMessageSent";
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
                StatusCode _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        getVersionCb(data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (StatusCode) StatusCode.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        serviceAvailable(_arg0);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (StatusCode) StatusCode.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        serviceUnAvailable(_arg0);
                        reply.writeNoException();
                        return true;
                    case 4:
                        PresPublishTriggerType _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PresPublishTriggerType) PresPublishTriggerType.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        publishTriggering(_arg02);
                        reply.writeNoException();
                        return true;
                    case 5:
                        PresCmdStatus _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PresCmdStatus) PresCmdStatus.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        cmdStatus(_arg03);
                        reply.writeNoException();
                        return true;
                    case 6:
                        PresSipResponse _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (PresSipResponse) PresSipResponse.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        sipResponseReceived(_arg04);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        capInfoReceived(data.readString(), (PresTupleInfo[]) data.createTypedArray(PresTupleInfo.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 8:
                        PresRlmiInfo _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (PresRlmiInfo) PresRlmiInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        listCapInfoReceived(_arg05, (PresResInfo[]) data.createTypedArray(PresResInfo.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        unpublishMessageSent();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPresenceListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPresenceListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void capInfoReceived(String str, PresTupleInfo[] presTupleInfoArr) throws RemoteException;

    @UnsupportedAppUsage
    void cmdStatus(PresCmdStatus presCmdStatus) throws RemoteException;

    @UnsupportedAppUsage
    void getVersionCb(String str) throws RemoteException;

    @UnsupportedAppUsage
    void listCapInfoReceived(PresRlmiInfo presRlmiInfo, PresResInfo[] presResInfoArr) throws RemoteException;

    @UnsupportedAppUsage
    void publishTriggering(PresPublishTriggerType presPublishTriggerType) throws RemoteException;

    @UnsupportedAppUsage
    void serviceAvailable(StatusCode statusCode) throws RemoteException;

    @UnsupportedAppUsage
    void serviceUnAvailable(StatusCode statusCode) throws RemoteException;

    @UnsupportedAppUsage
    void sipResponseReceived(PresSipResponse presSipResponse) throws RemoteException;

    @UnsupportedAppUsage
    void unpublishMessageSent() throws RemoteException;
}
