package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import com.android.ims.internal.IImsCallSession;

public interface IImsMmTelListener extends IInterface {

    public static class Default implements IImsMmTelListener {
        public void onIncomingCall(IImsCallSession c, Bundle extras) throws RemoteException {
        }

        public void onRejectedCall(ImsCallProfile callProfile, ImsReasonInfo reason) throws RemoteException {
        }

        public void onVoiceMessageCountUpdate(int count) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsMmTelListener {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsMmTelListener";
        static final int TRANSACTION_onIncomingCall = 1;
        static final int TRANSACTION_onRejectedCall = 2;
        static final int TRANSACTION_onVoiceMessageCountUpdate = 3;

        private static class Proxy implements IImsMmTelListener {
            public static IImsMmTelListener sDefaultImpl;
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

            public void onIncomingCall(IImsCallSession c, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onIncomingCall(c, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRejectedCall(ImsCallProfile callProfile, ImsReasonInfo reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callProfile != null) {
                        _data.writeInt(1);
                        callProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (reason != null) {
                        _data.writeInt(1);
                        reason.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRejectedCall(callProfile, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVoiceMessageCountUpdate(int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(count);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVoiceMessageCountUpdate(count);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsMmTelListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsMmTelListener)) {
                return new Proxy(obj);
            }
            return (IImsMmTelListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onIncomingCall";
            }
            if (transactionCode == 2) {
                return "onRejectedCall";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onVoiceMessageCountUpdate";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                Bundle _arg1;
                data.enforceInterface(descriptor);
                IImsCallSession _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                if (data.readInt() != 0) {
                    _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onIncomingCall(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                ImsCallProfile _arg02;
                ImsReasonInfo _arg12;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                if (data.readInt() != 0) {
                    _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                onRejectedCall(_arg02, _arg12);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onVoiceMessageCountUpdate(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IImsMmTelListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsMmTelListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onIncomingCall(IImsCallSession iImsCallSession, Bundle bundle) throws RemoteException;

    void onRejectedCall(ImsCallProfile imsCallProfile, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void onVoiceMessageCountUpdate(int i) throws RemoteException;
}
