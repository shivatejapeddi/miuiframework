package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import com.android.ims.internal.IImsFeatureStatusCallback;

public interface IImsServiceController extends IInterface {

    public static abstract class Stub extends Binder implements IImsServiceController {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsServiceController";
        static final int TRANSACTION_createMmTelFeature = 2;
        static final int TRANSACTION_createRcsFeature = 3;
        static final int TRANSACTION_disableIms = 10;
        static final int TRANSACTION_enableIms = 9;
        static final int TRANSACTION_getConfig = 7;
        static final int TRANSACTION_getRegistration = 8;
        static final int TRANSACTION_notifyImsServiceReadyForFeatureCreation = 5;
        static final int TRANSACTION_querySupportedImsFeatures = 4;
        static final int TRANSACTION_removeImsFeature = 6;
        static final int TRANSACTION_setListener = 1;

        private static class Proxy implements IImsServiceController {
            public static IImsServiceController sDefaultImpl;
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

            public void setListener(IImsServiceControllerListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setListener(l);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMmTelFeature createMmTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    IImsMmTelFeature iImsMmTelFeature = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iImsMmTelFeature = Stub.getDefaultImpl();
                        if (iImsMmTelFeature != 0) {
                            iImsMmTelFeature = Stub.getDefaultImpl().createMmTelFeature(slotId, c);
                            return iImsMmTelFeature;
                        }
                    }
                    _reply.readException();
                    iImsMmTelFeature = android.telephony.ims.aidl.IImsMmTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsMmTelFeature _result = iImsMmTelFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsRcsFeature createRcsFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    IImsRcsFeature iImsRcsFeature = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iImsRcsFeature = Stub.getDefaultImpl();
                        if (iImsRcsFeature != 0) {
                            iImsRcsFeature = Stub.getDefaultImpl().createRcsFeature(slotId, c);
                            return iImsRcsFeature;
                        }
                    }
                    _reply.readException();
                    iImsRcsFeature = android.telephony.ims.aidl.IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsRcsFeature _result = iImsRcsFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ImsFeatureConfiguration imsFeatureConfiguration = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        imsFeatureConfiguration = Stub.getDefaultImpl();
                        if (imsFeatureConfiguration != 0) {
                            imsFeatureConfiguration = Stub.getDefaultImpl().querySupportedImsFeatures();
                            return imsFeatureConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsFeatureConfiguration = (ImsFeatureConfiguration) ImsFeatureConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        imsFeatureConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsFeatureConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyImsServiceReadyForFeatureCreation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyImsServiceReadyForFeatureCreation();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeImsFeature(int slotId, int featureType, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(featureType);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeImsFeature(slotId, featureType, c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsConfig getConfig(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    IImsConfig iImsConfig = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        iImsConfig = Stub.getDefaultImpl();
                        if (iImsConfig != 0) {
                            iImsConfig = Stub.getDefaultImpl().getConfig(slotId);
                            return iImsConfig;
                        }
                    }
                    _reply.readException();
                    iImsConfig = android.telephony.ims.aidl.IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    IImsConfig _result = iImsConfig;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsRegistration getRegistration(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    IImsRegistration iImsRegistration = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        iImsRegistration = Stub.getDefaultImpl();
                        if (iImsRegistration != 0) {
                            iImsRegistration = Stub.getDefaultImpl().getRegistration(slotId);
                            return iImsRegistration;
                        }
                    }
                    _reply.readException();
                    iImsRegistration = android.telephony.ims.aidl.IImsRegistration.Stub.asInterface(_reply.readStrongBinder());
                    IImsRegistration _result = iImsRegistration;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().enableIms(slotId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableIms(slotId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsServiceController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsServiceController)) {
                return new Proxy(obj);
            }
            return (IImsServiceController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setListener";
                case 2:
                    return "createMmTelFeature";
                case 3:
                    return "createRcsFeature";
                case 4:
                    return "querySupportedImsFeatures";
                case 5:
                    return "notifyImsServiceReadyForFeatureCreation";
                case 6:
                    return "removeImsFeature";
                case 7:
                    return "getConfig";
                case 8:
                    return "getRegistration";
                case 9:
                    return "enableIms";
                case 10:
                    return "disableIms";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        setListener(android.telephony.ims.aidl.IImsServiceControllerListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        IImsMmTelFeature _result = createMmTelFeature(data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        IImsRcsFeature _result2 = createRcsFeature(data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result2 != null) {
                            iBinder = _result2.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        ImsFeatureConfiguration _result3 = querySupportedImsFeatures();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        notifyImsServiceReadyForFeatureCreation();
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        removeImsFeature(data.readInt(), data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        IImsConfig _result4 = getConfig(data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            iBinder = _result4.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        IImsRegistration _result5 = getRegistration(data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        enableIms(data.readInt());
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        disableIms(data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsServiceController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsServiceController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IImsServiceController {
        public void setListener(IImsServiceControllerListener l) throws RemoteException {
        }

        public IImsMmTelFeature createMmTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        public IImsRcsFeature createRcsFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        public ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException {
            return null;
        }

        public void notifyImsServiceReadyForFeatureCreation() throws RemoteException {
        }

        public void removeImsFeature(int slotId, int featureType, IImsFeatureStatusCallback c) throws RemoteException {
        }

        public IImsConfig getConfig(int slotId) throws RemoteException {
            return null;
        }

        public IImsRegistration getRegistration(int slotId) throws RemoteException {
            return null;
        }

        public void enableIms(int slotId) throws RemoteException {
        }

        public void disableIms(int slotId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    IImsMmTelFeature createMmTelFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    IImsRcsFeature createRcsFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    void disableIms(int i) throws RemoteException;

    void enableIms(int i) throws RemoteException;

    IImsConfig getConfig(int i) throws RemoteException;

    IImsRegistration getRegistration(int i) throws RemoteException;

    void notifyImsServiceReadyForFeatureCreation() throws RemoteException;

    ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException;

    void removeImsFeature(int i, int i2, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    void setListener(IImsServiceControllerListener iImsServiceControllerListener) throws RemoteException;
}
