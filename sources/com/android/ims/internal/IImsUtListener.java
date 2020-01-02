package com.android.ims.internal;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;

public interface IImsUtListener extends IInterface {

    public static class Default implements IImsUtListener {
        public void utConfigurationUpdated(IImsUt ut, int id) throws RemoteException {
        }

        public void utConfigurationUpdateFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
        }

        public void utConfigurationQueried(IImsUt ut, int id, Bundle ssInfo) throws RemoteException {
        }

        public void utConfigurationQueryFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
        }

        public void utConfigurationCallBarringQueried(IImsUt ut, int id, ImsSsInfo[] cbInfo) throws RemoteException {
        }

        public void utConfigurationCallForwardQueried(IImsUt ut, int id, ImsCallForwardInfo[] cfInfo) throws RemoteException {
        }

        public void utConfigurationCallWaitingQueried(IImsUt ut, int id, ImsSsInfo[] cwInfo) throws RemoteException {
        }

        public void onSupplementaryServiceIndication(ImsSsData ssData) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsUtListener {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsUtListener";
        static final int TRANSACTION_onSupplementaryServiceIndication = 8;
        static final int TRANSACTION_utConfigurationCallBarringQueried = 5;
        static final int TRANSACTION_utConfigurationCallForwardQueried = 6;
        static final int TRANSACTION_utConfigurationCallWaitingQueried = 7;
        static final int TRANSACTION_utConfigurationQueried = 3;
        static final int TRANSACTION_utConfigurationQueryFailed = 4;
        static final int TRANSACTION_utConfigurationUpdateFailed = 2;
        static final int TRANSACTION_utConfigurationUpdated = 1;

        private static class Proxy implements IImsUtListener {
            public static IImsUtListener sDefaultImpl;
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

            public void utConfigurationUpdated(IImsUt ut, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationUpdated(ut, id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationUpdateFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (error != null) {
                        _data.writeInt(1);
                        error.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationUpdateFailed(ut, id, error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationQueried(IImsUt ut, int id, Bundle ssInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (ssInfo != null) {
                        _data.writeInt(1);
                        ssInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationQueried(ut, id, ssInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationQueryFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (error != null) {
                        _data.writeInt(1);
                        error.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationQueryFailed(ut, id, error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationCallBarringQueried(IImsUt ut, int id, ImsSsInfo[] cbInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cbInfo, 0);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationCallBarringQueried(ut, id, cbInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationCallForwardQueried(IImsUt ut, int id, ImsCallForwardInfo[] cfInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cfInfo, 0);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationCallForwardQueried(ut, id, cfInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void utConfigurationCallWaitingQueried(IImsUt ut, int id, ImsSsInfo[] cwInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cwInfo, 0);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().utConfigurationCallWaitingQueried(ut, id, cwInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSupplementaryServiceIndication(ImsSsData ssData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ssData != null) {
                        _data.writeInt(1);
                        ssData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSupplementaryServiceIndication(ssData);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsUtListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsUtListener)) {
                return new Proxy(obj);
            }
            return (IImsUtListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "utConfigurationUpdated";
                case 2:
                    return "utConfigurationUpdateFailed";
                case 3:
                    return "utConfigurationQueried";
                case 4:
                    return "utConfigurationQueryFailed";
                case 5:
                    return "utConfigurationCallBarringQueried";
                case 6:
                    return "utConfigurationCallForwardQueried";
                case 7:
                    return "utConfigurationCallWaitingQueried";
                case 8:
                    return "onSupplementaryServiceIndication";
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
                IImsUt _arg0;
                int _arg1;
                ImsReasonInfo _arg2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        utConfigurationUpdated(com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        utConfigurationUpdateFailed(_arg0, _arg1, _arg2);
                        return true;
                    case 3:
                        Bundle _arg22;
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        utConfigurationQueried(_arg0, _arg1, _arg22);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        utConfigurationQueryFailed(_arg0, _arg1, _arg2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        utConfigurationCallBarringQueried(com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder()), data.readInt(), (ImsSsInfo[]) data.createTypedArray(ImsSsInfo.CREATOR));
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        utConfigurationCallForwardQueried(com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder()), data.readInt(), (ImsCallForwardInfo[]) data.createTypedArray(ImsCallForwardInfo.CREATOR));
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        utConfigurationCallWaitingQueried(com.android.ims.internal.IImsUt.Stub.asInterface(data.readStrongBinder()), data.readInt(), (ImsSsInfo[]) data.createTypedArray(ImsSsInfo.CREATOR));
                        return true;
                    case 8:
                        ImsSsData _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ImsSsData) ImsSsData.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        onSupplementaryServiceIndication(_arg02);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsUtListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsUtListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onSupplementaryServiceIndication(ImsSsData imsSsData) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationCallBarringQueried(IImsUt iImsUt, int i, ImsSsInfo[] imsSsInfoArr) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationCallForwardQueried(IImsUt iImsUt, int i, ImsCallForwardInfo[] imsCallForwardInfoArr) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationCallWaitingQueried(IImsUt iImsUt, int i, ImsSsInfo[] imsSsInfoArr) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationQueried(IImsUt iImsUt, int i, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationQueryFailed(IImsUt iImsUt, int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationUpdateFailed(IImsUt iImsUt, int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void utConfigurationUpdated(IImsUt iImsUt, int i) throws RemoteException;
}
