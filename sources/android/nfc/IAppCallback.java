package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAppCallback extends IInterface {

    public static class Default implements IAppCallback {
        public BeamShareData createBeamShareData(byte peerLlcpVersion) throws RemoteException {
            return null;
        }

        public void onNdefPushComplete(byte peerLlcpVersion) throws RemoteException {
        }

        public void onTagDiscovered(Tag tag) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppCallback {
        private static final String DESCRIPTOR = "android.nfc.IAppCallback";
        static final int TRANSACTION_createBeamShareData = 1;
        static final int TRANSACTION_onNdefPushComplete = 2;
        static final int TRANSACTION_onTagDiscovered = 3;

        private static class Proxy implements IAppCallback {
            public static IAppCallback sDefaultImpl;
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

            public BeamShareData createBeamShareData(byte peerLlcpVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(peerLlcpVersion);
                    BeamShareData beamShareData = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        beamShareData = Stub.getDefaultImpl();
                        if (beamShareData != 0) {
                            beamShareData = Stub.getDefaultImpl().createBeamShareData(peerLlcpVersion);
                            return beamShareData;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        beamShareData = (BeamShareData) BeamShareData.CREATOR.createFromParcel(_reply);
                    } else {
                        beamShareData = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return beamShareData;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNdefPushComplete(byte peerLlcpVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(peerLlcpVersion);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNdefPushComplete(peerLlcpVersion);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTagDiscovered(Tag tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tag != null) {
                        _data.writeInt(1);
                        tag.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTagDiscovered(tag);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppCallback)) {
                return new Proxy(obj);
            }
            return (IAppCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "createBeamShareData";
            }
            if (transactionCode == 2) {
                return "onNdefPushComplete";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onTagDiscovered";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                BeamShareData _result = createBeamShareData(data.readByte());
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onNdefPushComplete(data.readByte());
                return true;
            } else if (code == 3) {
                Tag _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Tag) Tag.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onTagDiscovered(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAppCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    BeamShareData createBeamShareData(byte b) throws RemoteException;

    void onNdefPushComplete(byte b) throws RemoteException;

    void onTagDiscovered(Tag tag) throws RemoteException;
}
