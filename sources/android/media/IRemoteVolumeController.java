package android.media;

import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteVolumeController extends IInterface {

    public static class Default implements IRemoteVolumeController {
        public void remoteVolumeChanged(Token sessionToken, int flags) throws RemoteException {
        }

        public void updateRemoteController(Token sessionToken) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRemoteVolumeController {
        private static final String DESCRIPTOR = "android.media.IRemoteVolumeController";
        static final int TRANSACTION_remoteVolumeChanged = 1;
        static final int TRANSACTION_updateRemoteController = 2;

        private static class Proxy implements IRemoteVolumeController {
            public static IRemoteVolumeController sDefaultImpl;
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

            public void remoteVolumeChanged(Token sessionToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().remoteVolumeChanged(sessionToken, flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateRemoteController(Token sessionToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateRemoteController(sessionToken);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteVolumeController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteVolumeController)) {
                return new Proxy(obj);
            }
            return (IRemoteVolumeController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "remoteVolumeChanged";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "updateRemoteController";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            Token _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Token) Token.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                remoteVolumeChanged(_arg0, data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Token) Token.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                updateRemoteController(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRemoteVolumeController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRemoteVolumeController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void remoteVolumeChanged(Token token, int i) throws RemoteException;

    void updateRemoteController(Token token) throws RemoteException;
}
