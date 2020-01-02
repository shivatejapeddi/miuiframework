package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWallpaperManagerCallback extends IInterface {

    public static class Default implements IWallpaperManagerCallback {
        public void onWallpaperChanged() throws RemoteException {
        }

        public void onWallpaperColorsChanged(WallpaperColors colors, int which, int userId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWallpaperManagerCallback {
        private static final String DESCRIPTOR = "android.app.IWallpaperManagerCallback";
        static final int TRANSACTION_onWallpaperChanged = 1;
        static final int TRANSACTION_onWallpaperColorsChanged = 2;

        private static class Proxy implements IWallpaperManagerCallback {
            public static IWallpaperManagerCallback sDefaultImpl;
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

            public void onWallpaperChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onWallpaperChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onWallpaperColorsChanged(WallpaperColors colors, int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (colors != null) {
                        _data.writeInt(1);
                        colors.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onWallpaperColorsChanged(colors, which, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWallpaperManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWallpaperManagerCallback)) {
                return new Proxy(obj);
            }
            return (IWallpaperManagerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onWallpaperChanged";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onWallpaperColorsChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onWallpaperChanged();
                return true;
            } else if (code == 2) {
                WallpaperColors _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (WallpaperColors) WallpaperColors.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onWallpaperColorsChanged(_arg0, data.readInt(), data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWallpaperManagerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWallpaperManagerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onWallpaperChanged() throws RemoteException;

    void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i, int i2) throws RemoteException;
}
