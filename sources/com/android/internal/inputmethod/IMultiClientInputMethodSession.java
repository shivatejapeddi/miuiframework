package com.android.internal.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.inputmethod.EditorInfo;
import com.android.internal.view.IInputContext;

public interface IMultiClientInputMethodSession extends IInterface {

    public static class Default implements IMultiClientInputMethodSession {
        public void startInputOrWindowGainedFocus(IInputContext inputContext, int missingMethods, EditorInfo attribute, int controlFlags, int softInputMode, int targetWindowHandle) throws RemoteException {
        }

        public void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        public void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMultiClientInputMethodSession {
        private static final String DESCRIPTOR = "com.android.internal.inputmethod.IMultiClientInputMethodSession";
        static final int TRANSACTION_hideSoftInput = 3;
        static final int TRANSACTION_showSoftInput = 2;
        static final int TRANSACTION_startInputOrWindowGainedFocus = 1;

        private static class Proxy implements IMultiClientInputMethodSession {
            public static IMultiClientInputMethodSession sDefaultImpl;
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

            public void startInputOrWindowGainedFocus(IInputContext inputContext, int missingMethods, EditorInfo attribute, int controlFlags, int softInputMode, int targetWindowHandle) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                EditorInfo editorInfo = attribute;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                    try {
                        _data.writeInt(missingMethods);
                        if (editorInfo != null) {
                            _data.writeInt(1);
                            editorInfo.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(controlFlags);
                        } catch (Throwable th2) {
                            th = th2;
                            i = softInputMode;
                            i2 = targetWindowHandle;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i3 = controlFlags;
                        i = softInputMode;
                        i2 = targetWindowHandle;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(softInputMode);
                        try {
                            _data.writeInt(targetWindowHandle);
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().startInputOrWindowGainedFocus(inputContext, missingMethods, attribute, controlFlags, softInputMode, targetWindowHandle);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = targetWindowHandle;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = missingMethods;
                    i3 = controlFlags;
                    i = softInputMode;
                    i2 = targetWindowHandle;
                    _data.recycle();
                    throw th;
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
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hideSoftInput(flags, resultReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiClientInputMethodSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiClientInputMethodSession)) {
                return new Proxy(obj);
            }
            return (IMultiClientInputMethodSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "startInputOrWindowGainedFocus";
            }
            if (transactionCode == 2) {
                return "showSoftInput";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "hideSoftInput";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            int _arg0;
            ResultReceiver _arg1;
            if (i == 1) {
                EditorInfo _arg2;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                IInputContext _arg02 = com.android.internal.view.IInputContext.Stub.asInterface(data.readStrongBinder());
                int _arg12 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                startInputOrWindowGainedFocus(_arg02, _arg12, _arg2, data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (i == 2) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                showSoftInput(_arg0, _arg1);
                return true;
            } else if (i == 3) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                hideSoftInput(_arg0, _arg1);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMultiClientInputMethodSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMultiClientInputMethodSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void hideSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void showSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void startInputOrWindowGainedFocus(IInputContext iInputContext, int i, EditorInfo editorInfo, int i2, int i3, int i4) throws RemoteException;
}
