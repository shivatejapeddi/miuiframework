package com.miui.translationservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITranslation extends IInterface {

    public static class Default implements ITranslation {
        public void translate(String source, String target, String word, ITranslationRemoteCallback callback) throws RemoteException {
        }

        public void translateByEngine(boolean offline, int offlineMode, String engine, int engineMode, String source, String target, String word, ITranslationRemoteCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITranslation {
        private static final String DESCRIPTOR = "com.miui.translationservice.ITranslation";
        static final int TRANSACTION_translate = 1;
        static final int TRANSACTION_translateByEngine = 2;

        private static class Proxy implements ITranslation {
            public static ITranslation sDefaultImpl;
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

            public void translate(String source, String target, String word, ITranslationRemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(source);
                    _data.writeString(target);
                    _data.writeString(word);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().translate(source, target, word, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void translateByEngine(boolean offline, int offlineMode, String engine, int engineMode, String source, String target, String word, ITranslationRemoteCallback callback) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                String str3;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(offline ? 1 : 0);
                    try {
                        _data.writeInt(offlineMode);
                    } catch (Throwable th2) {
                        th = th2;
                        str = engine;
                        i = engineMode;
                        str2 = source;
                        str3 = target;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(engine);
                        try {
                            _data.writeInt(engineMode);
                        } catch (Throwable th3) {
                            th = th3;
                            str2 = source;
                            str3 = target;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(source);
                        } catch (Throwable th4) {
                            th = th4;
                            str3 = target;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = engineMode;
                        str2 = source;
                        str3 = target;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(target);
                        _data.writeString(word);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().translateByEngine(offline, offlineMode, engine, engineMode, source, target, word, callback);
                        _data.recycle();
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i2 = offlineMode;
                    str = engine;
                    i = engineMode;
                    str2 = source;
                    str3 = target;
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITranslation asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITranslation)) {
                return new Proxy(obj);
            }
            return (ITranslation) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "translate";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "translateByEngine";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            if (i == 1) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                translate(data.readString(), data.readString(), data.readString(), com.miui.translationservice.ITranslationRemoteCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (i == 2) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                translateByEngine(data.readInt() != 0, data.readInt(), data.readString(), data.readInt(), data.readString(), data.readString(), data.readString(), com.miui.translationservice.ITranslationRemoteCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ITranslation impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITranslation getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void translate(String str, String str2, String str3, ITranslationRemoteCallback iTranslationRemoteCallback) throws RemoteException;

    void translateByEngine(boolean z, int i, String str, int i2, String str2, String str3, String str4, ITranslationRemoteCallback iTranslationRemoteCallback) throws RemoteException;
}
