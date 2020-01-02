package android.view.autofill;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IAugmentedAutofillManagerClient extends IInterface {

    public static abstract class Stub extends Binder implements IAugmentedAutofillManagerClient {
        private static final String DESCRIPTOR = "android.view.autofill.IAugmentedAutofillManagerClient";
        static final int TRANSACTION_autofill = 2;
        static final int TRANSACTION_getViewCoordinates = 1;
        static final int TRANSACTION_requestHideFillUi = 4;
        static final int TRANSACTION_requestShowFillUi = 3;

        private static class Proxy implements IAugmentedAutofillManagerClient {
            public static IAugmentedAutofillManagerClient sDefaultImpl;
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

            public Rect getViewCoordinates(AutofillId id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Rect rect = 0;
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        rect = Stub.getDefaultImpl();
                        if (rect != 0) {
                            rect = Stub.getDefaultImpl().getViewCoordinates(id);
                            return rect;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rect = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        rect = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rect;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedList(ids);
                    _data.writeTypedList(values);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().autofill(sessionId, ids, values);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                AutofillId autofillId = id;
                Rect rect = anchorBounds;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(sessionId);
                        if (autofillId != null) {
                            _data.writeInt(1);
                            autofillId.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(width);
                        } catch (Throwable th2) {
                            th = th2;
                            i = height;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(height);
                            if (rect != null) {
                                _data.writeInt(1);
                                rect.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeStrongBinder(presenter != null ? presenter.asBinder() : null);
                            try {
                                if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().requestShowFillUi(sessionId, id, width, height, anchorBounds, presenter);
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = width;
                        i = height;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = sessionId;
                    i2 = width;
                    i = height;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void requestHideFillUi(int sessionId, AutofillId id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestHideFillUi(sessionId, id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAugmentedAutofillManagerClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAugmentedAutofillManagerClient)) {
                return new Proxy(obj);
            }
            return (IAugmentedAutofillManagerClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getViewCoordinates";
            }
            if (transactionCode == 2) {
                return "autofill";
            }
            if (transactionCode == 3) {
                return "requestShowFillUi";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "requestHideFillUi";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                AutofillId _arg0;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                Rect _result = getViewCoordinates(_arg0);
                reply.writeNoException();
                if (_result != null) {
                    parcel2.writeInt(1);
                    _result.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                autofill(data.readInt(), parcel.createTypedArrayList(AutofillId.CREATOR), parcel.createTypedArrayList(AutofillValue.CREATOR));
                reply.writeNoException();
                return true;
            } else if (i == 3) {
                AutofillId _arg1;
                Rect _arg4;
                parcel.enforceInterface(descriptor);
                int _arg02 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                int _arg2 = data.readInt();
                int _arg3 = data.readInt();
                if (data.readInt() != 0) {
                    _arg4 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                } else {
                    _arg4 = null;
                }
                requestShowFillUi(_arg02, _arg1, _arg2, _arg3, _arg4, android.view.autofill.IAutofillWindowPresenter.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 4) {
                AutofillId _arg12;
                parcel.enforceInterface(descriptor);
                int _arg03 = data.readInt();
                if (data.readInt() != 0) {
                    _arg12 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                } else {
                    _arg12 = null;
                }
                requestHideFillUi(_arg03, _arg12);
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAugmentedAutofillManagerClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAugmentedAutofillManagerClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAugmentedAutofillManagerClient {
        public Rect getViewCoordinates(AutofillId id) throws RemoteException {
            return null;
        }

        public void autofill(int sessionId, List<AutofillId> list, List<AutofillValue> list2) throws RemoteException {
        }

        public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
        }

        public void requestHideFillUi(int sessionId, AutofillId id) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void autofill(int i, List<AutofillId> list, List<AutofillValue> list2) throws RemoteException;

    Rect getViewCoordinates(AutofillId autofillId) throws RemoteException;

    void requestHideFillUi(int i, AutofillId autofillId) throws RemoteException;

    void requestShowFillUi(int i, AutofillId autofillId, int i2, int i3, Rect rect, IAutofillWindowPresenter iAutofillWindowPresenter) throws RemoteException;
}
