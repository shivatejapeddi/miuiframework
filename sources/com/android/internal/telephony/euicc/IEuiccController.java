package com.android.internal.telephony.euicc;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;

public interface IEuiccController extends IInterface {

    public static class Default implements IEuiccController {
        public void continueOperation(int cardId, Intent resolutionIntent, Bundle resolutionExtras) throws RemoteException {
        }

        public void getDownloadableSubscriptionMetadata(int cardId, DownloadableSubscription subscription, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
        }

        public void getDefaultDownloadableSubscriptionList(int cardId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
        }

        public String getEid(int cardId, String callingPackage) throws RemoteException {
            return null;
        }

        public int getOtaStatus(int cardId) throws RemoteException {
            return 0;
        }

        public void downloadSubscription(int cardId, DownloadableSubscription subscription, boolean switchAfterDownload, String callingPackage, Bundle resolvedBundle, PendingIntent callbackIntent) throws RemoteException {
        }

        public EuiccInfo getEuiccInfo(int cardId) throws RemoteException {
            return null;
        }

        public void deleteSubscription(int cardId, int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
        }

        public void switchToSubscription(int cardId, int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
        }

        public void updateSubscriptionNickname(int cardId, int subscriptionId, String nickname, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
        }

        public void eraseSubscriptions(int cardId, PendingIntent callbackIntent) throws RemoteException {
        }

        public void retainSubscriptionsForFactoryReset(int cardId, PendingIntent callbackIntent) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEuiccController {
        private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccController";
        static final int TRANSACTION_continueOperation = 1;
        static final int TRANSACTION_deleteSubscription = 8;
        static final int TRANSACTION_downloadSubscription = 6;
        static final int TRANSACTION_eraseSubscriptions = 11;
        static final int TRANSACTION_getDefaultDownloadableSubscriptionList = 3;
        static final int TRANSACTION_getDownloadableSubscriptionMetadata = 2;
        static final int TRANSACTION_getEid = 4;
        static final int TRANSACTION_getEuiccInfo = 7;
        static final int TRANSACTION_getOtaStatus = 5;
        static final int TRANSACTION_retainSubscriptionsForFactoryReset = 12;
        static final int TRANSACTION_switchToSubscription = 9;
        static final int TRANSACTION_updateSubscriptionNickname = 10;

        private static class Proxy implements IEuiccController {
            public static IEuiccController sDefaultImpl;
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

            public void continueOperation(int cardId, Intent resolutionIntent, Bundle resolutionExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    if (resolutionIntent != null) {
                        _data.writeInt(1);
                        resolutionIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resolutionExtras != null) {
                        _data.writeInt(1);
                        resolutionExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().continueOperation(cardId, resolutionIntent, resolutionExtras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDownloadableSubscriptionMetadata(int cardId, DownloadableSubscription subscription, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    if (subscription != null) {
                        _data.writeInt(1);
                        subscription.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDownloadableSubscriptionMetadata(cardId, subscription, callingPackage, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDefaultDownloadableSubscriptionList(int cardId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDefaultDownloadableSubscriptionList(cardId, callingPackage, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getEid(int cardId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    _data.writeString(callingPackage);
                    String str = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getEid(cardId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getOtaStatus(int cardId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getOtaStatus(cardId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void downloadSubscription(int cardId, DownloadableSubscription subscription, boolean switchAfterDownload, String callingPackage, Bundle resolvedBundle, PendingIntent callbackIntent) throws RemoteException {
                Throwable th;
                String str;
                DownloadableSubscription downloadableSubscription = subscription;
                Bundle bundle = resolvedBundle;
                PendingIntent pendingIntent = callbackIntent;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(cardId);
                        if (downloadableSubscription != null) {
                            _data.writeInt(1);
                            downloadableSubscription.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(switchAfterDownload ? 1 : 0);
                        try {
                            _data.writeString(callingPackage);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = callingPackage;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().downloadSubscription(cardId, subscription, switchAfterDownload, callingPackage, resolvedBundle, callbackIntent);
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = cardId;
                    str = callingPackage;
                    _data.recycle();
                    throw th;
                }
            }

            public EuiccInfo getEuiccInfo(int cardId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    EuiccInfo euiccInfo = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        euiccInfo = Stub.getDefaultImpl();
                        if (euiccInfo != 0) {
                            euiccInfo = Stub.getDefaultImpl().getEuiccInfo(cardId);
                            return euiccInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        euiccInfo = (EuiccInfo) EuiccInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        euiccInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return euiccInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteSubscription(int cardId, int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    _data.writeInt(subscriptionId);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deleteSubscription(cardId, subscriptionId, callingPackage, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void switchToSubscription(int cardId, int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    _data.writeInt(subscriptionId);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().switchToSubscription(cardId, subscriptionId, callingPackage, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateSubscriptionNickname(int cardId, int subscriptionId, String nickname, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    _data.writeInt(subscriptionId);
                    _data.writeString(nickname);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateSubscriptionNickname(cardId, subscriptionId, nickname, callingPackage, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void eraseSubscriptions(int cardId, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().eraseSubscriptions(cardId, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void retainSubscriptionsForFactoryReset(int cardId, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cardId);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().retainSubscriptionsForFactoryReset(cardId, callbackIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEuiccController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEuiccController)) {
                return new Proxy(obj);
            }
            return (IEuiccController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "continueOperation";
                case 2:
                    return "getDownloadableSubscriptionMetadata";
                case 3:
                    return "getDefaultDownloadableSubscriptionList";
                case 4:
                    return "getEid";
                case 5:
                    return "getOtaStatus";
                case 6:
                    return "downloadSubscription";
                case 7:
                    return "getEuiccInfo";
                case 8:
                    return "deleteSubscription";
                case 9:
                    return "switchToSubscription";
                case 10:
                    return "updateSubscriptionNickname";
                case 11:
                    return "eraseSubscriptions";
                case 12:
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
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                int _arg0;
                String _arg2;
                PendingIntent _arg3;
                int _result;
                int _arg02;
                String _arg32;
                PendingIntent _arg1;
                switch (i) {
                    case 1:
                        Intent _arg12;
                        Bundle _arg22;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        continueOperation(_arg0, _arg12, _arg22);
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
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        getDownloadableSubscriptionMetadata(_arg0, _arg13, _arg2, _arg3);
                        return true;
                    case 3:
                        PendingIntent _arg23;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        String _arg14 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        getDefaultDownloadableSubscriptionList(_arg0, _arg14, _arg23);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg2 = getEid(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg2);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = getOtaStatus(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 6:
                        DownloadableSubscription _arg15;
                        Bundle _arg4;
                        PendingIntent _arg5;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg15 = (DownloadableSubscription) DownloadableSubscription.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        boolean _arg24 = data.readInt() != 0;
                        _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        downloadSubscription(_arg02, _arg15, _arg24, _arg32, _arg4, _arg5);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        EuiccInfo _result2 = getEuiccInfo(data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _result = data.readInt();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        deleteSubscription(_arg0, _result, _arg2, _arg3);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _result = data.readInt();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        switchToSubscription(_arg0, _result, _arg2, _arg3);
                        return true;
                    case 10:
                        PendingIntent _arg42;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        _arg02 = data.readInt();
                        String _arg25 = data.readString();
                        _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg42 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        updateSubscriptionNickname(_arg03, _arg02, _arg25, _arg32, _arg42);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        eraseSubscriptions(_arg0, _arg1);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        retainSubscriptionsForFactoryReset(_arg0, _arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IEuiccController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEuiccController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void continueOperation(int i, Intent intent, Bundle bundle) throws RemoteException;

    void deleteSubscription(int i, int i2, String str, PendingIntent pendingIntent) throws RemoteException;

    void downloadSubscription(int i, DownloadableSubscription downloadableSubscription, boolean z, String str, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    void eraseSubscriptions(int i, PendingIntent pendingIntent) throws RemoteException;

    void getDefaultDownloadableSubscriptionList(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    void getDownloadableSubscriptionMetadata(int i, DownloadableSubscription downloadableSubscription, String str, PendingIntent pendingIntent) throws RemoteException;

    String getEid(int i, String str) throws RemoteException;

    EuiccInfo getEuiccInfo(int i) throws RemoteException;

    int getOtaStatus(int i) throws RemoteException;

    void retainSubscriptionsForFactoryReset(int i, PendingIntent pendingIntent) throws RemoteException;

    void switchToSubscription(int i, int i2, String str, PendingIntent pendingIntent) throws RemoteException;

    void updateSubscriptionNickname(int i, int i2, String str, String str2, PendingIntent pendingIntent) throws RemoteException;
}
