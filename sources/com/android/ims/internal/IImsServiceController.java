package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IImsServiceController extends IInterface {

    public static class Default implements IImsServiceController {
        public IImsMMTelFeature createEmergencyMMTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        public IImsMMTelFeature createMMTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        public IImsRcsFeature createRcsFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        public void removeImsFeature(int slotId, int featureType, IImsFeatureStatusCallback c) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsServiceController {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsServiceController";
        static final int TRANSACTION_createEmergencyMMTelFeature = 1;
        static final int TRANSACTION_createMMTelFeature = 2;
        static final int TRANSACTION_createRcsFeature = 3;
        static final int TRANSACTION_removeImsFeature = 4;

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

            public IImsMMTelFeature createEmergencyMMTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    IImsMMTelFeature iImsMMTelFeature = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iImsMMTelFeature = Stub.getDefaultImpl();
                        if (iImsMMTelFeature != 0) {
                            iImsMMTelFeature = Stub.getDefaultImpl().createEmergencyMMTelFeature(slotId, c);
                            return iImsMMTelFeature;
                        }
                    }
                    _reply.readException();
                    iImsMMTelFeature = com.android.ims.internal.IImsMMTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsMMTelFeature _result = iImsMMTelFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMMTelFeature createMMTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    IImsMMTelFeature iImsMMTelFeature = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iImsMMTelFeature = Stub.getDefaultImpl();
                        if (iImsMMTelFeature != 0) {
                            iImsMMTelFeature = Stub.getDefaultImpl().createMMTelFeature(slotId, c);
                            return iImsMMTelFeature;
                        }
                    }
                    _reply.readException();
                    iImsMMTelFeature = com.android.ims.internal.IImsMMTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsMMTelFeature _result = iImsMMTelFeature;
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
                    iImsRcsFeature = com.android.ims.internal.IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsRcsFeature _result = iImsRcsFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
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
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
            if (transactionCode == 1) {
                return "createEmergencyMMTelFeature";
            }
            if (transactionCode == 2) {
                return "createMMTelFeature";
            }
            if (transactionCode == 3) {
                return "createRcsFeature";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "removeImsFeature";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            IBinder iBinder = null;
            IImsMMTelFeature _result;
            if (code == 1) {
                data.enforceInterface(descriptor);
                _result = createEmergencyMMTelFeature(data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                if (_result != null) {
                    iBinder = _result.asBinder();
                }
                reply.writeStrongBinder(iBinder);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                _result = createMMTelFeature(data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                if (_result != null) {
                    iBinder = _result.asBinder();
                }
                reply.writeStrongBinder(iBinder);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                IImsRcsFeature _result2 = createRcsFeature(data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                if (_result2 != null) {
                    iBinder = _result2.asBinder();
                }
                reply.writeStrongBinder(iBinder);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                removeImsFeature(data.readInt(), data.readInt(), com.android.ims.internal.IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
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

    IImsMMTelFeature createEmergencyMMTelFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    IImsMMTelFeature createMMTelFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    IImsRcsFeature createRcsFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    void removeImsFeature(int i, int i2, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;
}
