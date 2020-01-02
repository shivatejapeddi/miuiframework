package android.view;

import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISystemGestureExclusionListener extends IInterface {

    public static class Default implements ISystemGestureExclusionListener {
        public void onSystemGestureExclusionChanged(int displayId, Region systemGestureExclusion) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISystemGestureExclusionListener {
        private static final String DESCRIPTOR = "android.view.ISystemGestureExclusionListener";
        static final int TRANSACTION_onSystemGestureExclusionChanged = 1;

        private static class Proxy implements ISystemGestureExclusionListener {
            public static ISystemGestureExclusionListener sDefaultImpl;
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

            public void onSystemGestureExclusionChanged(int displayId, Region systemGestureExclusion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (systemGestureExclusion != null) {
                        _data.writeInt(1);
                        systemGestureExclusion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSystemGestureExclusionChanged(displayId, systemGestureExclusion);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISystemGestureExclusionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISystemGestureExclusionListener)) {
                return new Proxy(obj);
            }
            return (ISystemGestureExclusionListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onSystemGestureExclusionChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                Region _arg1;
                data.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (Region) Region.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onSystemGestureExclusionChanged(_arg0, _arg1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISystemGestureExclusionListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISystemGestureExclusionListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onSystemGestureExclusionChanged(int i, Region region) throws RemoteException;
}
