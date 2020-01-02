package android.view.autofill;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.ThreadedRenderer;
import android.view.WindowManager.LayoutParams;

public interface IAutofillWindowPresenter extends IInterface {

    public static class Default implements IAutofillWindowPresenter {
        public void show(LayoutParams p, Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) throws RemoteException {
        }

        public void hide(Rect transitionEpicenter) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAutofillWindowPresenter {
        private static final String DESCRIPTOR = "android.view.autofill.IAutofillWindowPresenter";
        static final int TRANSACTION_hide = 2;
        static final int TRANSACTION_show = 1;

        private static class Proxy implements IAutofillWindowPresenter {
            public static IAutofillWindowPresenter sDefaultImpl;
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

            public void show(LayoutParams p, Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (transitionEpicenter != null) {
                        _data.writeInt(1);
                        transitionEpicenter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fitsSystemWindows) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(layoutDirection);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().show(p, transitionEpicenter, fitsSystemWindows, layoutDirection);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hide(Rect transitionEpicenter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transitionEpicenter != null) {
                        _data.writeInt(1);
                        transitionEpicenter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hide(transitionEpicenter);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAutofillWindowPresenter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAutofillWindowPresenter)) {
                return new Proxy(obj);
            }
            return (IAutofillWindowPresenter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return ThreadedRenderer.OVERDRAW_PROPERTY_SHOW;
            }
            if (transactionCode != 2) {
                return null;
            }
            return "hide";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                LayoutParams _arg0;
                Rect _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (LayoutParams) LayoutParams.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (Rect) Rect.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                show(_arg0, _arg1, data.readInt() != 0, data.readInt());
                return true;
            } else if (code == 2) {
                Rect _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (Rect) Rect.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                hide(_arg02);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAutofillWindowPresenter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAutofillWindowPresenter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void hide(Rect rect) throws RemoteException;

    void show(LayoutParams layoutParams, Rect rect, boolean z, int i) throws RemoteException;
}
