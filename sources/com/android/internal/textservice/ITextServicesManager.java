package com.android.internal.textservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.SpellCheckerSubtype;

public interface ITextServicesManager extends IInterface {

    public static class Default implements ITextServicesManager {
        public SpellCheckerInfo getCurrentSpellChecker(int userId, String locale) throws RemoteException {
            return null;
        }

        public SpellCheckerSubtype getCurrentSpellCheckerSubtype(int userId, boolean allowImplicitlySelectedSubtype) throws RemoteException {
            return null;
        }

        public void getSpellCheckerService(int userId, String sciId, String locale, ITextServicesSessionListener tsListener, ISpellCheckerSessionListener scListener, Bundle bundle) throws RemoteException {
        }

        public void finishSpellCheckerService(int userId, ISpellCheckerSessionListener listener) throws RemoteException {
        }

        public boolean isSpellCheckerEnabled(int userId) throws RemoteException {
            return false;
        }

        public SpellCheckerInfo[] getEnabledSpellCheckers(int userId) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITextServicesManager {
        private static final String DESCRIPTOR = "com.android.internal.textservice.ITextServicesManager";
        static final int TRANSACTION_finishSpellCheckerService = 4;
        static final int TRANSACTION_getCurrentSpellChecker = 1;
        static final int TRANSACTION_getCurrentSpellCheckerSubtype = 2;
        static final int TRANSACTION_getEnabledSpellCheckers = 6;
        static final int TRANSACTION_getSpellCheckerService = 3;
        static final int TRANSACTION_isSpellCheckerEnabled = 5;

        private static class Proxy implements ITextServicesManager {
            public static ITextServicesManager sDefaultImpl;
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

            public SpellCheckerInfo getCurrentSpellChecker(int userId, String locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(locale);
                    SpellCheckerInfo spellCheckerInfo = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        spellCheckerInfo = Stub.getDefaultImpl();
                        if (spellCheckerInfo != 0) {
                            spellCheckerInfo = Stub.getDefaultImpl().getCurrentSpellChecker(userId, locale);
                            return spellCheckerInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        spellCheckerInfo = (SpellCheckerInfo) SpellCheckerInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        spellCheckerInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return spellCheckerInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SpellCheckerSubtype getCurrentSpellCheckerSubtype(int userId, boolean allowImplicitlySelectedSubtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(allowImplicitlySelectedSubtype ? 1 : 0);
                    SpellCheckerSubtype spellCheckerSubtype = this.mRemote;
                    if (!spellCheckerSubtype.transact(2, _data, _reply, 0)) {
                        spellCheckerSubtype = Stub.getDefaultImpl();
                        if (spellCheckerSubtype != null) {
                            spellCheckerSubtype = Stub.getDefaultImpl().getCurrentSpellCheckerSubtype(userId, allowImplicitlySelectedSubtype);
                            return spellCheckerSubtype;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        spellCheckerSubtype = (SpellCheckerSubtype) SpellCheckerSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        spellCheckerSubtype = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return spellCheckerSubtype;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getSpellCheckerService(int userId, String sciId, String locale, ITextServicesSessionListener tsListener, ISpellCheckerSessionListener scListener, Bundle bundle) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                Bundle bundle2 = bundle;
                Parcel _data = Parcel.obtain();
                int i;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    i = userId;
                    try {
                        _data.writeInt(userId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = sciId;
                        str2 = locale;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(sciId);
                        try {
                            _data.writeString(locale);
                            _data.writeStrongBinder(tsListener != null ? tsListener.asBinder() : null);
                            _data.writeStrongBinder(scListener != null ? scListener.asBinder() : null);
                            if (bundle2 != null) {
                                _data.writeInt(1);
                                bundle2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().getSpellCheckerService(userId, sciId, locale, tsListener, scListener, bundle);
                                _data.recycle();
                            } catch (Throwable th3) {
                                th = th3;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str2 = locale;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    i = userId;
                    str = sciId;
                    str2 = locale;
                    _data.recycle();
                    throw th;
                }
            }

            public void finishSpellCheckerService(int userId, ISpellCheckerSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().finishSpellCheckerService(userId, listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean isSpellCheckerEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSpellCheckerEnabled(userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SpellCheckerInfo[] getEnabledSpellCheckers(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    SpellCheckerInfo[] spellCheckerInfoArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        spellCheckerInfoArr = Stub.getDefaultImpl();
                        if (spellCheckerInfoArr != 0) {
                            spellCheckerInfoArr = Stub.getDefaultImpl().getEnabledSpellCheckers(userId);
                            return spellCheckerInfoArr;
                        }
                    }
                    _reply.readException();
                    SpellCheckerInfo[] _result = (SpellCheckerInfo[]) _reply.createTypedArray(SpellCheckerInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITextServicesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITextServicesManager)) {
                return new Proxy(obj);
            }
            return (ITextServicesManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getCurrentSpellChecker";
                case 2:
                    return "getCurrentSpellCheckerSubtype";
                case 3:
                    return "getSpellCheckerService";
                case 4:
                    return "finishSpellCheckerService";
                case 5:
                    return "isSpellCheckerEnabled";
                case 6:
                    return "getEnabledSpellCheckers";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        SpellCheckerInfo _result = getCurrentSpellChecker(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        SpellCheckerSubtype _result2 = getCurrentSpellCheckerSubtype(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        Bundle _arg5;
                        parcel.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        String _arg1 = data.readString();
                        String _arg2 = data.readString();
                        ITextServicesSessionListener _arg3 = com.android.internal.textservice.ITextServicesSessionListener.Stub.asInterface(data.readStrongBinder());
                        ISpellCheckerSessionListener _arg4 = com.android.internal.textservice.ISpellCheckerSessionListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        getSpellCheckerService(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        finishSpellCheckerService(data.readInt(), com.android.internal.textservice.ISpellCheckerSessionListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = isSpellCheckerEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        SpellCheckerInfo[] _result4 = getEnabledSpellCheckers(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result4, 1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITextServicesManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITextServicesManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void finishSpellCheckerService(int i, ISpellCheckerSessionListener iSpellCheckerSessionListener) throws RemoteException;

    SpellCheckerInfo getCurrentSpellChecker(int i, String str) throws RemoteException;

    SpellCheckerSubtype getCurrentSpellCheckerSubtype(int i, boolean z) throws RemoteException;

    SpellCheckerInfo[] getEnabledSpellCheckers(int i) throws RemoteException;

    void getSpellCheckerService(int i, String str, String str2, ITextServicesSessionListener iTextServicesSessionListener, ISpellCheckerSessionListener iSpellCheckerSessionListener, Bundle bundle) throws RemoteException;

    boolean isSpellCheckerEnabled(int i) throws RemoteException;
}
