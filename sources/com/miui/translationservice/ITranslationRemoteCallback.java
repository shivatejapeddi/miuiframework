package com.miui.translationservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.miui.translationservice.provider.TranslationResult;

public interface ITranslationRemoteCallback extends IInterface {

    public static abstract class Stub extends Binder implements ITranslationRemoteCallback {
        private static final String DESCRIPTOR = "com.miui.translationservice.ITranslationRemoteCallback";
        static final int TRANSACTION_onTranslationFinished = 1;

        private static class Proxy implements ITranslationRemoteCallback {
            public static ITranslationRemoteCallback sDefaultImpl;
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

            public void onTranslationFinished(TranslationResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTranslationFinished(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITranslationRemoteCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITranslationRemoteCallback)) {
                return new Proxy(obj);
            }
            return (ITranslationRemoteCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onTranslationFinished";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                TranslationResult _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (TranslationResult) TranslationResult.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onTranslationFinished(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ITranslationRemoteCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITranslationRemoteCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ITranslationRemoteCallback {
        public void onTranslationFinished(TranslationResult result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onTranslationFinished(TranslationResult translationResult) throws RemoteException;
}
