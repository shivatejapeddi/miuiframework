package android.media;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAudioFocusDispatcher extends IInterface {

    public static abstract class Stub extends Binder implements IAudioFocusDispatcher {
        private static final String DESCRIPTOR = "android.media.IAudioFocusDispatcher";
        static final int TRANSACTION_dispatchAudioFocusChange = 1;
        static final int TRANSACTION_dispatchFocusResultFromExtPolicy = 2;

        private static class Proxy implements IAudioFocusDispatcher {
            public static IAudioFocusDispatcher sDefaultImpl;
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

            public void dispatchAudioFocusChange(int focusChange, String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusChange);
                    _data.writeString(clientId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchAudioFocusChange(focusChange, clientId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchFocusResultFromExtPolicy(int requestResult, String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestResult);
                    _data.writeString(clientId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchFocusResultFromExtPolicy(requestResult, clientId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAudioFocusDispatcher asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAudioFocusDispatcher)) {
                return new Proxy(obj);
            }
            return (IAudioFocusDispatcher) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "dispatchAudioFocusChange";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "dispatchFocusResultFromExtPolicy";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                dispatchAudioFocusChange(data.readInt(), data.readString());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                dispatchFocusResultFromExtPolicy(data.readInt(), data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAudioFocusDispatcher impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAudioFocusDispatcher getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAudioFocusDispatcher {
        public void dispatchAudioFocusChange(int focusChange, String clientId) throws RemoteException {
        }

        public void dispatchFocusResultFromExtPolicy(int requestResult, String clientId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    @UnsupportedAppUsage
    void dispatchAudioFocusChange(int i, String str) throws RemoteException;

    void dispatchFocusResultFromExtPolicy(int i, String str) throws RemoteException;
}
