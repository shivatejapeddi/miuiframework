package android.media.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;

public interface ISoundTriggerDetectionService extends IInterface {

    public static class Default implements ISoundTriggerDetectionService {
        public void setClient(ParcelUuid uuid, Bundle params, ISoundTriggerDetectionServiceClient client) throws RemoteException {
        }

        public void removeClient(ParcelUuid uuid) throws RemoteException {
        }

        public void onGenericRecognitionEvent(ParcelUuid uuid, int opId, GenericRecognitionEvent event) throws RemoteException {
        }

        public void onError(ParcelUuid uuid, int opId, int status) throws RemoteException {
        }

        public void onStopOperation(ParcelUuid uuid, int opId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISoundTriggerDetectionService {
        private static final String DESCRIPTOR = "android.media.soundtrigger.ISoundTriggerDetectionService";
        static final int TRANSACTION_onError = 4;
        static final int TRANSACTION_onGenericRecognitionEvent = 3;
        static final int TRANSACTION_onStopOperation = 5;
        static final int TRANSACTION_removeClient = 2;
        static final int TRANSACTION_setClient = 1;

        private static class Proxy implements ISoundTriggerDetectionService {
            public static ISoundTriggerDetectionService sDefaultImpl;
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

            public void setClient(ParcelUuid uuid, Bundle params, ISoundTriggerDetectionServiceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setClient(uuid, params, client);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeClient(ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeClient(uuid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onGenericRecognitionEvent(ParcelUuid uuid, int opId, GenericRecognitionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(opId);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onGenericRecognitionEvent(uuid, opId, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(ParcelUuid uuid, int opId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(opId);
                    _data.writeInt(status);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(uuid, opId, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStopOperation(ParcelUuid uuid, int opId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(opId);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStopOperation(uuid, opId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISoundTriggerDetectionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISoundTriggerDetectionService)) {
                return new Proxy(obj);
            }
            return (ISoundTriggerDetectionService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setClient";
            }
            if (transactionCode == 2) {
                return "removeClient";
            }
            if (transactionCode == 3) {
                return "onGenericRecognitionEvent";
            }
            if (transactionCode == 4) {
                return "onError";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "onStopOperation";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            ParcelUuid _arg0;
            if (code == 1) {
                Bundle _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                setClient(_arg0, _arg1, android.media.soundtrigger.ISoundTriggerDetectionServiceClient.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                removeClient(_arg0);
                return true;
            } else if (code == 3) {
                GenericRecognitionEvent _arg2;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _arg12 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = (GenericRecognitionEvent) GenericRecognitionEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                onGenericRecognitionEvent(_arg0, _arg12, _arg2);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onError(_arg0, data.readInt(), data.readInt());
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onStopOperation(_arg0, data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISoundTriggerDetectionService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISoundTriggerDetectionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onError(ParcelUuid parcelUuid, int i, int i2) throws RemoteException;

    void onGenericRecognitionEvent(ParcelUuid parcelUuid, int i, GenericRecognitionEvent genericRecognitionEvent) throws RemoteException;

    void onStopOperation(ParcelUuid parcelUuid, int i) throws RemoteException;

    void removeClient(ParcelUuid parcelUuid) throws RemoteException;

    void setClient(ParcelUuid parcelUuid, Bundle bundle, ISoundTriggerDetectionServiceClient iSoundTriggerDetectionServiceClient) throws RemoteException;
}
