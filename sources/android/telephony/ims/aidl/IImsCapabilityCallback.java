package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IImsCapabilityCallback extends IInterface {

    public static abstract class Stub extends Binder implements IImsCapabilityCallback {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsCapabilityCallback";
        static final int TRANSACTION_onCapabilitiesStatusChanged = 3;
        static final int TRANSACTION_onChangeCapabilityConfigurationError = 2;
        static final int TRANSACTION_onQueryCapabilityConfiguration = 1;

        private static class Proxy implements IImsCapabilityCallback {
            public static IImsCapabilityCallback sDefaultImpl;
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

            public void onQueryCapabilityConfiguration(int capability, int radioTech, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    _data.writeInt(radioTech);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onQueryCapabilityConfiguration(capability, radioTech, enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onChangeCapabilityConfigurationError(int capability, int radioTech, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    _data.writeInt(radioTech);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onChangeCapabilityConfigurationError(capability, radioTech, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCapabilitiesStatusChanged(int config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(config);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCapabilitiesStatusChanged(config);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsCapabilityCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsCapabilityCallback)) {
                return new Proxy(obj);
            }
            return (IImsCapabilityCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onQueryCapabilityConfiguration";
            }
            if (transactionCode == 2) {
                return "onChangeCapabilityConfigurationError";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onCapabilitiesStatusChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onQueryCapabilityConfiguration(data.readInt(), data.readInt(), data.readInt() != 0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onChangeCapabilityConfigurationError(data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onCapabilitiesStatusChanged(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IImsCapabilityCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsCapabilityCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IImsCapabilityCallback {
        public void onQueryCapabilityConfiguration(int capability, int radioTech, boolean enabled) throws RemoteException {
        }

        public void onChangeCapabilityConfigurationError(int capability, int radioTech, int reason) throws RemoteException {
        }

        public void onCapabilitiesStatusChanged(int config) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onCapabilitiesStatusChanged(int i) throws RemoteException;

    void onChangeCapabilityConfigurationError(int i, int i2, int i3) throws RemoteException;

    void onQueryCapabilityConfiguration(int i, int i2, boolean z) throws RemoteException;
}
