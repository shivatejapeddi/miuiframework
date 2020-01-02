package com.android.ims.internal;

import android.annotation.UnsupportedAppUsage;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;

public interface IImsRegistrationListener extends IInterface {

    public static class Default implements IImsRegistrationListener {
        public void registrationConnected() throws RemoteException {
        }

        public void registrationProgressing() throws RemoteException {
        }

        public void registrationConnectedWithRadioTech(int imsRadioTech) throws RemoteException {
        }

        public void registrationProgressingWithRadioTech(int imsRadioTech) throws RemoteException {
        }

        public void registrationDisconnected(ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public void registrationResumed() throws RemoteException {
        }

        public void registrationSuspended() throws RemoteException {
        }

        public void registrationServiceCapabilityChanged(int serviceClass, int event) throws RemoteException {
        }

        public void registrationFeatureCapabilityChanged(int serviceClass, int[] enabledFeatures, int[] disabledFeatures) throws RemoteException {
        }

        public void voiceMessageCountUpdate(int count) throws RemoteException {
        }

        public void registrationAssociatedUriChanged(Uri[] uris) throws RemoteException {
        }

        public void registrationChangeFailed(int targetAccessTech, ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsRegistrationListener {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsRegistrationListener";
        static final int TRANSACTION_registrationAssociatedUriChanged = 11;
        static final int TRANSACTION_registrationChangeFailed = 12;
        static final int TRANSACTION_registrationConnected = 1;
        static final int TRANSACTION_registrationConnectedWithRadioTech = 3;
        static final int TRANSACTION_registrationDisconnected = 5;
        static final int TRANSACTION_registrationFeatureCapabilityChanged = 9;
        static final int TRANSACTION_registrationProgressing = 2;
        static final int TRANSACTION_registrationProgressingWithRadioTech = 4;
        static final int TRANSACTION_registrationResumed = 6;
        static final int TRANSACTION_registrationServiceCapabilityChanged = 8;
        static final int TRANSACTION_registrationSuspended = 7;
        static final int TRANSACTION_voiceMessageCountUpdate = 10;

        private static class Proxy implements IImsRegistrationListener {
            public static IImsRegistrationListener sDefaultImpl;
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

            public void registrationConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationConnected();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationProgressing() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationProgressing();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationConnectedWithRadioTech(int imsRadioTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imsRadioTech);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationConnectedWithRadioTech(imsRadioTech);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationProgressingWithRadioTech(int imsRadioTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imsRadioTech);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationProgressingWithRadioTech(imsRadioTech);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationDisconnected(ImsReasonInfo imsReasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (imsReasonInfo != null) {
                        _data.writeInt(1);
                        imsReasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationDisconnected(imsReasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationResumed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationResumed();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationSuspended() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationSuspended();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationServiceCapabilityChanged(int serviceClass, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceClass);
                    _data.writeInt(event);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationServiceCapabilityChanged(serviceClass, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationFeatureCapabilityChanged(int serviceClass, int[] enabledFeatures, int[] disabledFeatures) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serviceClass);
                    _data.writeIntArray(enabledFeatures);
                    _data.writeIntArray(disabledFeatures);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationFeatureCapabilityChanged(serviceClass, enabledFeatures, disabledFeatures);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void voiceMessageCountUpdate(int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(count);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().voiceMessageCountUpdate(count);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationAssociatedUriChanged(Uri[] uris) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(uris, 0);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationAssociatedUriChanged(uris);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registrationChangeFailed(int targetAccessTech, ImsReasonInfo imsReasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetAccessTech);
                    if (imsReasonInfo != null) {
                        _data.writeInt(1);
                        imsReasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registrationChangeFailed(targetAccessTech, imsReasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsRegistrationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsRegistrationListener)) {
                return new Proxy(obj);
            }
            return (IImsRegistrationListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registrationConnected";
                case 2:
                    return "registrationProgressing";
                case 3:
                    return "registrationConnectedWithRadioTech";
                case 4:
                    return "registrationProgressingWithRadioTech";
                case 5:
                    return "registrationDisconnected";
                case 6:
                    return "registrationResumed";
                case 7:
                    return "registrationSuspended";
                case 8:
                    return "registrationServiceCapabilityChanged";
                case 9:
                    return "registrationFeatureCapabilityChanged";
                case 10:
                    return "voiceMessageCountUpdate";
                case 11:
                    return "registrationAssociatedUriChanged";
                case 12:
                    return "registrationChangeFailed";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registrationConnected();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        registrationProgressing();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        registrationConnectedWithRadioTech(data.readInt());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        registrationProgressingWithRadioTech(data.readInt());
                        return true;
                    case 5:
                        ImsReasonInfo _arg0;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        registrationDisconnected(_arg0);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        registrationResumed();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        registrationSuspended();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        registrationServiceCapabilityChanged(data.readInt(), data.readInt());
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        registrationFeatureCapabilityChanged(data.readInt(), data.createIntArray(), data.createIntArray());
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        voiceMessageCountUpdate(data.readInt());
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        registrationAssociatedUriChanged((Uri[]) data.createTypedArray(Uri.CREATOR));
                        return true;
                    case 12:
                        ImsReasonInfo _arg1;
                        data.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        registrationChangeFailed(_arg02, _arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsRegistrationListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsRegistrationListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void registrationAssociatedUriChanged(Uri[] uriArr) throws RemoteException;

    @UnsupportedAppUsage
    void registrationChangeFailed(int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void registrationConnected() throws RemoteException;

    @UnsupportedAppUsage
    void registrationConnectedWithRadioTech(int i) throws RemoteException;

    @UnsupportedAppUsage
    void registrationDisconnected(ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void registrationFeatureCapabilityChanged(int i, int[] iArr, int[] iArr2) throws RemoteException;

    void registrationProgressing() throws RemoteException;

    @UnsupportedAppUsage
    void registrationProgressingWithRadioTech(int i) throws RemoteException;

    void registrationResumed() throws RemoteException;

    void registrationServiceCapabilityChanged(int i, int i2) throws RemoteException;

    void registrationSuspended() throws RemoteException;

    @UnsupportedAppUsage
    void voiceMessageCountUpdate(int i) throws RemoteException;
}
