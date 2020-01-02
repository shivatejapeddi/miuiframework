package android.service.euicc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;

public interface IEuiccService extends IInterface {

    public static abstract class Stub extends Binder implements IEuiccService {
        private static final String DESCRIPTOR = "android.service.euicc.IEuiccService";
        static final int TRANSACTION_deleteSubscription = 9;
        static final int TRANSACTION_downloadSubscription = 1;
        static final int TRANSACTION_eraseSubscriptions = 12;
        static final int TRANSACTION_getDefaultDownloadableSubscriptionList = 7;
        static final int TRANSACTION_getDownloadableSubscriptionMetadata = 2;
        static final int TRANSACTION_getEid = 3;
        static final int TRANSACTION_getEuiccInfo = 8;
        static final int TRANSACTION_getEuiccProfileInfoList = 6;
        static final int TRANSACTION_getOtaStatus = 4;
        static final int TRANSACTION_retainSubscriptionsForFactoryReset = 13;
        static final int TRANSACTION_startOtaIfNecessary = 5;
        static final int TRANSACTION_switchToSubscription = 10;
        static final int TRANSACTION_updateSubscriptionNickname = 11;

        private static class Proxy implements IEuiccService {
            public static IEuiccService sDefaultImpl;
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

            public void downloadSubscription(int slotId, DownloadableSubscription subscription, boolean switchAfterDownload, boolean forceDeactivateSim, Bundle resolvedBundle, IDownloadSubscriptionCallback callback) throws RemoteException {
                Throwable th;
                DownloadableSubscription downloadableSubscription = subscription;
                Bundle bundle = resolvedBundle;
                Parcel _data = Parcel.obtain();
                int i;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    i = slotId;
                    try {
                        _data.writeInt(slotId);
                        if (downloadableSubscription != null) {
                            _data.writeInt(1);
                            subscription.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(switchAfterDownload ? 1 : 0);
                        _data.writeInt(forceDeactivateSim ? 1 : 0);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        try {
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().downloadSubscription(slotId, subscription, switchAfterDownload, forceDeactivateSim, resolvedBundle, callback);
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    i = slotId;
                    _data.recycle();
                    throw th;
                }
            }

            public void getDownloadableSubscriptionMetadata(int slotId, DownloadableSubscription subscription, boolean forceDeactivateSim, IGetDownloadableSubscriptionMetadataCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    int i = 0;
                    if (subscription != null) {
                        _data.writeInt(1);
                        subscription.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (forceDeactivateSim) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDownloadableSubscriptionMetadata(slotId, subscription, forceDeactivateSim, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEid(int slotId, IGetEidCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEid(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getOtaStatus(int slotId, IGetOtaStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getOtaStatus(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startOtaIfNecessary(int slotId, IOtaStatusChangedCallback statusChangedCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(statusChangedCallback != null ? statusChangedCallback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startOtaIfNecessary(slotId, statusChangedCallback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccProfileInfoList(int slotId, IGetEuiccProfileInfoListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccProfileInfoList(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDefaultDownloadableSubscriptionList(int slotId, boolean forceDeactivateSim, IGetDefaultDownloadableSubscriptionListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(forceDeactivateSim ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDefaultDownloadableSubscriptionList(slotId, forceDeactivateSim, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEuiccInfo(int slotId, IGetEuiccInfoCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEuiccInfo(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deleteSubscription(int slotId, String iccid, IDeleteSubscriptionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deleteSubscription(slotId, iccid, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void switchToSubscription(int slotId, String iccid, boolean forceDeactivateSim, ISwitchToSubscriptionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(iccid);
                    _data.writeInt(forceDeactivateSim ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().switchToSubscription(slotId, iccid, forceDeactivateSim, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateSubscriptionNickname(int slotId, String iccid, String nickname, IUpdateSubscriptionNicknameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(iccid);
                    _data.writeString(nickname);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateSubscriptionNickname(slotId, iccid, nickname, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void eraseSubscriptions(int slotId, IEraseSubscriptionsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().eraseSubscriptions(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retainSubscriptionsForFactoryReset(int slotId, IRetainSubscriptionsForFactoryResetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retainSubscriptionsForFactoryReset(slotId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEuiccService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEuiccService)) {
                return new Proxy(obj);
            }
            return (IEuiccService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "downloadSubscription";
                case 2:
                    return "getDownloadableSubscriptionMetadata";
                case 3:
                    return "getEid";
                case 4:
                    return "getOtaStatus";
                case 5:
                    return "startOtaIfNecessary";
                case 6:
                    return "getEuiccProfileInfoList";
                case 7:
                    return "getDefaultDownloadableSubscriptionList";
                case 8:
                    return "getEuiccInfo";
                case 9:
                    return "deleteSubscription";
                case 10:
                    return "switchToSubscription";
                case 11:
                    return "updateSubscriptionNickname";
                case 12:
                    return "eraseSubscriptions";
                case 13:
                    return "retainSubscriptionsForFactoryReset";
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
                switch (i) {
                    case 1:
                        DownloadableSubscription _arg12;
                        Bundle _arg4;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (DownloadableSubscription) DownloadableSubscription.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        boolean _arg2 = data.readInt() != 0;
                        boolean _arg3 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        downloadSubscription(_arg02, _arg12, _arg2, _arg3, _arg4, android.service.euicc.IDownloadSubscriptionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        DownloadableSubscription _arg13;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (DownloadableSubscription) DownloadableSubscription.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        getDownloadableSubscriptionMetadata(_arg0, _arg13, _arg1, android.service.euicc.IGetDownloadableSubscriptionMetadataCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        getEid(data.readInt(), android.service.euicc.IGetEidCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        getOtaStatus(data.readInt(), android.service.euicc.IGetOtaStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        startOtaIfNecessary(data.readInt(), android.service.euicc.IOtaStatusChangedCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        getEuiccProfileInfoList(data.readInt(), android.service.euicc.IGetEuiccProfileInfoListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        getDefaultDownloadableSubscriptionList(_arg0, _arg1, android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        getEuiccInfo(data.readInt(), android.service.euicc.IGetEuiccInfoCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        deleteSubscription(data.readInt(), data.readString(), android.service.euicc.IDeleteSubscriptionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        String _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        switchToSubscription(_arg0, _arg14, _arg1, android.service.euicc.ISwitchToSubscriptionCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        updateSubscriptionNickname(data.readInt(), data.readString(), data.readString(), android.service.euicc.IUpdateSubscriptionNicknameCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        eraseSubscriptions(data.readInt(), android.service.euicc.IEraseSubscriptionsCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        retainSubscriptionsForFactoryReset(data.readInt(), android.service.euicc.IRetainSubscriptionsForFactoryResetCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IEuiccService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEuiccService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IEuiccService {
        public void downloadSubscription(int slotId, DownloadableSubscription subscription, boolean switchAfterDownload, boolean forceDeactivateSim, Bundle resolvedBundle, IDownloadSubscriptionCallback callback) throws RemoteException {
        }

        public void getDownloadableSubscriptionMetadata(int slotId, DownloadableSubscription subscription, boolean forceDeactivateSim, IGetDownloadableSubscriptionMetadataCallback callback) throws RemoteException {
        }

        public void getEid(int slotId, IGetEidCallback callback) throws RemoteException {
        }

        public void getOtaStatus(int slotId, IGetOtaStatusCallback callback) throws RemoteException {
        }

        public void startOtaIfNecessary(int slotId, IOtaStatusChangedCallback statusChangedCallback) throws RemoteException {
        }

        public void getEuiccProfileInfoList(int slotId, IGetEuiccProfileInfoListCallback callback) throws RemoteException {
        }

        public void getDefaultDownloadableSubscriptionList(int slotId, boolean forceDeactivateSim, IGetDefaultDownloadableSubscriptionListCallback callback) throws RemoteException {
        }

        public void getEuiccInfo(int slotId, IGetEuiccInfoCallback callback) throws RemoteException {
        }

        public void deleteSubscription(int slotId, String iccid, IDeleteSubscriptionCallback callback) throws RemoteException {
        }

        public void switchToSubscription(int slotId, String iccid, boolean forceDeactivateSim, ISwitchToSubscriptionCallback callback) throws RemoteException {
        }

        public void updateSubscriptionNickname(int slotId, String iccid, String nickname, IUpdateSubscriptionNicknameCallback callback) throws RemoteException {
        }

        public void eraseSubscriptions(int slotId, IEraseSubscriptionsCallback callback) throws RemoteException {
        }

        public void retainSubscriptionsForFactoryReset(int slotId, IRetainSubscriptionsForFactoryResetCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void deleteSubscription(int i, String str, IDeleteSubscriptionCallback iDeleteSubscriptionCallback) throws RemoteException;

    void downloadSubscription(int i, DownloadableSubscription downloadableSubscription, boolean z, boolean z2, Bundle bundle, IDownloadSubscriptionCallback iDownloadSubscriptionCallback) throws RemoteException;

    void eraseSubscriptions(int i, IEraseSubscriptionsCallback iEraseSubscriptionsCallback) throws RemoteException;

    void getDefaultDownloadableSubscriptionList(int i, boolean z, IGetDefaultDownloadableSubscriptionListCallback iGetDefaultDownloadableSubscriptionListCallback) throws RemoteException;

    void getDownloadableSubscriptionMetadata(int i, DownloadableSubscription downloadableSubscription, boolean z, IGetDownloadableSubscriptionMetadataCallback iGetDownloadableSubscriptionMetadataCallback) throws RemoteException;

    void getEid(int i, IGetEidCallback iGetEidCallback) throws RemoteException;

    void getEuiccInfo(int i, IGetEuiccInfoCallback iGetEuiccInfoCallback) throws RemoteException;

    void getEuiccProfileInfoList(int i, IGetEuiccProfileInfoListCallback iGetEuiccProfileInfoListCallback) throws RemoteException;

    void getOtaStatus(int i, IGetOtaStatusCallback iGetOtaStatusCallback) throws RemoteException;

    void retainSubscriptionsForFactoryReset(int i, IRetainSubscriptionsForFactoryResetCallback iRetainSubscriptionsForFactoryResetCallback) throws RemoteException;

    void startOtaIfNecessary(int i, IOtaStatusChangedCallback iOtaStatusChangedCallback) throws RemoteException;

    void switchToSubscription(int i, String str, boolean z, ISwitchToSubscriptionCallback iSwitchToSubscriptionCallback) throws RemoteException;

    void updateSubscriptionNickname(int i, String str, String str2, IUpdateSubscriptionNicknameCallback iUpdateSubscriptionNicknameCallback) throws RemoteException;
}
