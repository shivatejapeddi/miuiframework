package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.InputChannel;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.inputmethod.IInputMethodPrivilegedOperations;

public interface IInputMethod extends IInterface {

    public static class Default implements IInputMethod {
        public void initializeInternal(IBinder token, int displayId, IInputMethodPrivilegedOperations privOps) throws RemoteException {
        }

        public void bindInput(InputBinding binding) throws RemoteException {
        }

        public void unbindInput() throws RemoteException {
        }

        public void startInput(IBinder startInputToken, IInputContext inputContext, int missingMethods, EditorInfo attribute, boolean restarting, boolean preRenderImeViews) throws RemoteException {
        }

        public void createSession(InputChannel channel, IInputSessionCallback callback) throws RemoteException {
        }

        public void setSessionEnabled(IInputMethodSession session, boolean enabled) throws RemoteException {
        }

        public void revokeSession(IInputMethodSession session) throws RemoteException {
        }

        public void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        public void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        public void changeInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInputMethod {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputMethod";
        static final int TRANSACTION_bindInput = 2;
        static final int TRANSACTION_changeInputMethodSubtype = 10;
        static final int TRANSACTION_createSession = 5;
        static final int TRANSACTION_hideSoftInput = 9;
        static final int TRANSACTION_initializeInternal = 1;
        static final int TRANSACTION_revokeSession = 7;
        static final int TRANSACTION_setSessionEnabled = 6;
        static final int TRANSACTION_showSoftInput = 8;
        static final int TRANSACTION_startInput = 4;
        static final int TRANSACTION_unbindInput = 3;

        private static class Proxy implements IInputMethod {
            public static IInputMethod sDefaultImpl;
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

            public void initializeInternal(IBinder token, int displayId, IInputMethodPrivilegedOperations privOps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(privOps != null ? privOps.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().initializeInternal(token, displayId, privOps);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void bindInput(InputBinding binding) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (binding != null) {
                        _data.writeInt(1);
                        binding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().bindInput(binding);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unbindInput() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unbindInput();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startInput(IBinder startInputToken, IInputContext inputContext, int missingMethods, EditorInfo attribute, boolean restarting, boolean preRenderImeViews) throws RemoteException {
                Throwable th;
                int i;
                EditorInfo editorInfo = attribute;
                Parcel _data = Parcel.obtain();
                IBinder iBinder;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    iBinder = startInputToken;
                    try {
                        _data.writeStrongBinder(startInputToken);
                        _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                        try {
                            _data.writeInt(missingMethods);
                            int i2 = 0;
                            if (editorInfo != null) {
                                _data.writeInt(1);
                                editorInfo.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(restarting ? 1 : 0);
                            if (preRenderImeViews) {
                                i2 = 1;
                            }
                            _data.writeInt(i2);
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().startInput(startInputToken, inputContext, missingMethods, attribute, restarting, preRenderImeViews);
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = missingMethods;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    iBinder = startInputToken;
                    i = missingMethods;
                    _data.recycle();
                    throw th;
                }
            }

            public void createSession(InputChannel channel, IInputSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().createSession(channel, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSessionEnabled(IInputMethodSession session, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setSessionEnabled(session, enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void revokeSession(IInputMethodSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().revokeSession(session);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showSoftInput(flags, resultReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hideSoftInput(flags, resultReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void changeInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().changeInputMethodSubtype(subtype);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputMethod asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputMethod)) {
                return new Proxy(obj);
            }
            return (IInputMethod) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "initializeInternal";
                case 2:
                    return "bindInput";
                case 3:
                    return "unbindInput";
                case 4:
                    return "startInput";
                case 5:
                    return "createSession";
                case 6:
                    return "setSessionEnabled";
                case 7:
                    return "revokeSession";
                case 8:
                    return "showSoftInput";
                case 9:
                    return "hideSoftInput";
                case 10:
                    return "changeInputMethodSubtype";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg1 = false;
                int _arg0;
                ResultReceiver _arg12;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        initializeInternal(data.readStrongBinder(), data.readInt(), com.android.internal.inputmethod.IInputMethodPrivilegedOperations.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        InputBinding _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (InputBinding) InputBinding.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        bindInput(_arg02);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        unbindInput();
                        return true;
                    case 4:
                        EditorInfo _arg3;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        IInputContext _arg13 = com.android.internal.view.IInputContext.Stub.asInterface(data.readStrongBinder());
                        int _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        startInput(_arg03, _arg13, _arg2, _arg3, data.readInt() != 0, data.readInt() != 0);
                        return true;
                    case 5:
                        InputChannel _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (InputChannel) InputChannel.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        createSession(_arg04, com.android.internal.view.IInputSessionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        IInputMethodSession _arg05 = com.android.internal.view.IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setSessionEnabled(_arg05, _arg1);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        revokeSession(com.android.internal.view.IInputMethodSession.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        showSoftInput(_arg0, _arg12);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        hideSoftInput(_arg0, _arg12);
                        return true;
                    case 10:
                        InputMethodSubtype _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (InputMethodSubtype) InputMethodSubtype.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        changeInputMethodSubtype(_arg06);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInputMethod impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInputMethod getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void bindInput(InputBinding inputBinding) throws RemoteException;

    void changeInputMethodSubtype(InputMethodSubtype inputMethodSubtype) throws RemoteException;

    void createSession(InputChannel inputChannel, IInputSessionCallback iInputSessionCallback) throws RemoteException;

    void hideSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void initializeInternal(IBinder iBinder, int i, IInputMethodPrivilegedOperations iInputMethodPrivilegedOperations) throws RemoteException;

    void revokeSession(IInputMethodSession iInputMethodSession) throws RemoteException;

    void setSessionEnabled(IInputMethodSession iInputMethodSession, boolean z) throws RemoteException;

    void showSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void startInput(IBinder iBinder, IInputContext iInputContext, int i, EditorInfo editorInfo, boolean z, boolean z2) throws RemoteException;

    void unbindInput() throws RemoteException;
}
