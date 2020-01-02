package com.android.internal.inputmethod;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;

public interface IInputMethodPrivilegedOperations extends IInterface {

    public static class Default implements IInputMethodPrivilegedOperations {
        public void setImeWindowStatus(int vis, int backDisposition) throws RemoteException {
        }

        public void reportStartInput(IBinder startInputToken) throws RemoteException {
        }

        public IInputContentUriToken createInputContentUriToken(Uri contentUri, String packageName) throws RemoteException {
            return null;
        }

        public void reportFullscreenMode(boolean fullscreen) throws RemoteException {
        }

        public void setInputMethod(String id) throws RemoteException {
        }

        public void setInputMethodAndSubtype(String id, InputMethodSubtype subtype) throws RemoteException {
        }

        public void hideMySoftInput(int flags) throws RemoteException {
        }

        public void showMySoftInput(int flags) throws RemoteException {
        }

        public void updateStatusIcon(String packageName, int iconId) throws RemoteException {
        }

        public boolean switchToPreviousInputMethod() throws RemoteException {
            return false;
        }

        public boolean switchToNextInputMethod(boolean onlyCurrentIme) throws RemoteException {
            return false;
        }

        public boolean shouldOfferSwitchingToNextInputMethod() throws RemoteException {
            return false;
        }

        public void notifyUserAction() throws RemoteException {
        }

        public void reportPreRendered(EditorInfo info) throws RemoteException {
        }

        public void applyImeVisibility(boolean setVisible) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInputMethodPrivilegedOperations {
        private static final String DESCRIPTOR = "com.android.internal.inputmethod.IInputMethodPrivilegedOperations";
        static final int TRANSACTION_applyImeVisibility = 15;
        static final int TRANSACTION_createInputContentUriToken = 3;
        static final int TRANSACTION_hideMySoftInput = 7;
        static final int TRANSACTION_notifyUserAction = 13;
        static final int TRANSACTION_reportFullscreenMode = 4;
        static final int TRANSACTION_reportPreRendered = 14;
        static final int TRANSACTION_reportStartInput = 2;
        static final int TRANSACTION_setImeWindowStatus = 1;
        static final int TRANSACTION_setInputMethod = 5;
        static final int TRANSACTION_setInputMethodAndSubtype = 6;
        static final int TRANSACTION_shouldOfferSwitchingToNextInputMethod = 12;
        static final int TRANSACTION_showMySoftInput = 8;
        static final int TRANSACTION_switchToNextInputMethod = 11;
        static final int TRANSACTION_switchToPreviousInputMethod = 10;
        static final int TRANSACTION_updateStatusIcon = 9;

        private static class Proxy implements IInputMethodPrivilegedOperations {
            public static IInputMethodPrivilegedOperations sDefaultImpl;
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

            public void setImeWindowStatus(int vis, int backDisposition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setImeWindowStatus(vis, backDisposition);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportStartInput(IBinder startInputToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(startInputToken);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportStartInput(startInputToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IInputContentUriToken createInputContentUriToken(Uri contentUri, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    IInputContentUriToken iInputContentUriToken = this.mRemote;
                    if (!iInputContentUriToken.transact(3, _data, _reply, 0)) {
                        iInputContentUriToken = Stub.getDefaultImpl();
                        if (iInputContentUriToken != null) {
                            iInputContentUriToken = Stub.getDefaultImpl().createInputContentUriToken(contentUri, packageName);
                            return iInputContentUriToken;
                        }
                    }
                    _reply.readException();
                    iInputContentUriToken = com.android.internal.inputmethod.IInputContentUriToken.Stub.asInterface(_reply.readStrongBinder());
                    IInputContentUriToken _result = iInputContentUriToken;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportFullscreenMode(boolean fullscreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fullscreen ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportFullscreenMode(fullscreen);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInputMethod(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInputMethod(id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInputMethodAndSubtype(String id, InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInputMethodAndSubtype(id, subtype);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hideMySoftInput(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hideMySoftInput(flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showMySoftInput(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showMySoftInput(flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateStatusIcon(String packageName, int iconId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(iconId);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateStatusIcon(packageName, iconId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean switchToPreviousInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().switchToPreviousInputMethod();
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

            public boolean switchToNextInputMethod(boolean onlyCurrentIme) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(onlyCurrentIme ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().switchToNextInputMethod(onlyCurrentIme);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldOfferSwitchingToNextInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldOfferSwitchingToNextInputMethod();
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

            public void notifyUserAction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyUserAction();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportPreRendered(EditorInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportPreRendered(info);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyImeVisibility(boolean setVisible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(setVisible ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyImeVisibility(setVisible);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputMethodPrivilegedOperations asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputMethodPrivilegedOperations)) {
                return new Proxy(obj);
            }
            return (IInputMethodPrivilegedOperations) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setImeWindowStatus";
                case 2:
                    return "reportStartInput";
                case 3:
                    return "createInputContentUriToken";
                case 4:
                    return "reportFullscreenMode";
                case 5:
                    return "setInputMethod";
                case 6:
                    return "setInputMethodAndSubtype";
                case 7:
                    return "hideMySoftInput";
                case 8:
                    return "showMySoftInput";
                case 9:
                    return "updateStatusIcon";
                case 10:
                    return "switchToPreviousInputMethod";
                case 11:
                    return "switchToNextInputMethod";
                case 12:
                    return "shouldOfferSwitchingToNextInputMethod";
                case 13:
                    return "notifyUserAction";
                case 14:
                    return "reportPreRendered";
                case 15:
                    return "applyImeVisibility";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg0 = false;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        setImeWindowStatus(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        reportStartInput(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 3:
                        Uri _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Uri) Uri.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        IInputContentUriToken _result = createInputContentUriToken(_arg02, data.readString());
                        reply.writeNoException();
                        reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        reportFullscreenMode(_arg0);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setInputMethod(data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        InputMethodSubtype _arg1;
                        data.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (InputMethodSubtype) InputMethodSubtype.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        setInputMethodAndSubtype(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        hideMySoftInput(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        showMySoftInput(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        updateStatusIcon(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = switchToPreviousInputMethod();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        boolean _result2 = switchToNextInputMethod(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _arg0 = shouldOfferSwitchingToNextInputMethod();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        notifyUserAction();
                        reply.writeNoException();
                        return true;
                    case 14:
                        EditorInfo _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        reportPreRendered(_arg04);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        applyImeVisibility(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInputMethodPrivilegedOperations impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInputMethodPrivilegedOperations getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void applyImeVisibility(boolean z) throws RemoteException;

    IInputContentUriToken createInputContentUriToken(Uri uri, String str) throws RemoteException;

    void hideMySoftInput(int i) throws RemoteException;

    void notifyUserAction() throws RemoteException;

    void reportFullscreenMode(boolean z) throws RemoteException;

    void reportPreRendered(EditorInfo editorInfo) throws RemoteException;

    void reportStartInput(IBinder iBinder) throws RemoteException;

    void setImeWindowStatus(int i, int i2) throws RemoteException;

    void setInputMethod(String str) throws RemoteException;

    void setInputMethodAndSubtype(String str, InputMethodSubtype inputMethodSubtype) throws RemoteException;

    boolean shouldOfferSwitchingToNextInputMethod() throws RemoteException;

    void showMySoftInput(int i) throws RemoteException;

    boolean switchToNextInputMethod(boolean z) throws RemoteException;

    boolean switchToPreviousInputMethod() throws RemoteException;

    void updateStatusIcon(String str, int i) throws RemoteException;
}
