package android.service.autofill;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.view.autofill.AutofillValue;
import java.util.List;
import java.util.Map;

public interface IAutofillFieldClassificationService extends IInterface {

    public static abstract class Stub extends Binder implements IAutofillFieldClassificationService {
        private static final String DESCRIPTOR = "android.service.autofill.IAutofillFieldClassificationService";
        static final int TRANSACTION_calculateScores = 1;

        private static class Proxy implements IAutofillFieldClassificationService {
            public static IAutofillFieldClassificationService sDefaultImpl;
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

            public void calculateScores(RemoteCallback callback, List<AutofillValue> actualValues, String[] userDataValues, String[] categoryIds, String defaultAlgorithm, Bundle defaultArgs, Map algorithms, Map args) throws RemoteException {
                Throwable th;
                String[] strArr;
                String[] strArr2;
                RemoteCallback remoteCallback = callback;
                Bundle bundle = defaultArgs;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (remoteCallback != null) {
                        _data.writeInt(1);
                        remoteCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeTypedList(actualValues);
                    } catch (Throwable th2) {
                        th = th2;
                        strArr = userDataValues;
                        strArr2 = categoryIds;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringArray(userDataValues);
                        try {
                            _data.writeStringArray(categoryIds);
                            _data.writeString(defaultAlgorithm);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeMap(algorithms);
                            _data.writeMap(args);
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().calculateScores(callback, actualValues, userDataValues, categoryIds, defaultAlgorithm, defaultArgs, algorithms, args);
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        strArr2 = categoryIds;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    List<AutofillValue> list = actualValues;
                    strArr = userDataValues;
                    strArr2 = categoryIds;
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAutofillFieldClassificationService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAutofillFieldClassificationService)) {
                return new Proxy(obj);
            }
            return (IAutofillFieldClassificationService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "calculateScores";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                RemoteCallback _arg0;
                Bundle _arg5;
                Parcel parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                List<AutofillValue> _arg1 = parcel.createTypedArrayList(AutofillValue.CREATOR);
                String[] _arg2 = data.createStringArray();
                String[] _arg3 = data.createStringArray();
                String _arg4 = data.readString();
                if (data.readInt() != 0) {
                    _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                } else {
                    _arg5 = null;
                }
                ClassLoader cl = getClass().getClassLoader();
                calculateScores(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, parcel.readHashMap(cl), parcel.readHashMap(cl));
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAutofillFieldClassificationService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAutofillFieldClassificationService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAutofillFieldClassificationService {
        public void calculateScores(RemoteCallback callback, List<AutofillValue> list, String[] userDataValues, String[] categoryIds, String defaultAlgorithm, Bundle defaultArgs, Map algorithms, Map args) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void calculateScores(RemoteCallback remoteCallback, List<AutofillValue> list, String[] strArr, String[] strArr2, String str, Bundle bundle, Map map, Map map2) throws RemoteException;
}
