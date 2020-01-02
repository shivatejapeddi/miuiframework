package android.hardware.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRecognitionStatusCallback extends IInterface {

    public static class Default implements IRecognitionStatusCallback {
        public void onKeyphraseDetected(KeyphraseRecognitionEvent recognitionEvent) throws RemoteException {
        }

        public void onGenericSoundTriggerDetected(GenericRecognitionEvent recognitionEvent) throws RemoteException {
        }

        public void onError(int status) throws RemoteException {
        }

        public void onRecognitionPaused() throws RemoteException {
        }

        public void onRecognitionResumed() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRecognitionStatusCallback {
        private static final String DESCRIPTOR = "android.hardware.soundtrigger.IRecognitionStatusCallback";
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onGenericSoundTriggerDetected = 2;
        static final int TRANSACTION_onKeyphraseDetected = 1;
        static final int TRANSACTION_onRecognitionPaused = 4;
        static final int TRANSACTION_onRecognitionResumed = 5;

        private static class Proxy implements IRecognitionStatusCallback {
            public static IRecognitionStatusCallback sDefaultImpl;
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

            public void onKeyphraseDetected(KeyphraseRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onKeyphraseDetected(recognitionEvent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onGenericSoundTriggerDetected(GenericRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onGenericSoundTriggerDetected(recognitionEvent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRecognitionPaused() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRecognitionPaused();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRecognitionResumed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRecognitionResumed();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecognitionStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRecognitionStatusCallback)) {
                return new Proxy(obj);
            }
            return (IRecognitionStatusCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onKeyphraseDetected";
            }
            if (transactionCode == 2) {
                return "onGenericSoundTriggerDetected";
            }
            if (transactionCode == 3) {
                return "onError";
            }
            if (transactionCode == 4) {
                return "onRecognitionPaused";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "onRecognitionResumed";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                KeyphraseRecognitionEvent _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (KeyphraseRecognitionEvent) KeyphraseRecognitionEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onKeyphraseDetected(_arg0);
                return true;
            } else if (code == 2) {
                GenericRecognitionEvent _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (GenericRecognitionEvent) GenericRecognitionEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onGenericSoundTriggerDetected(_arg02);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onError(data.readInt());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onRecognitionPaused();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                onRecognitionResumed();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRecognitionStatusCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRecognitionStatusCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onError(int i) throws RemoteException;

    void onGenericSoundTriggerDetected(GenericRecognitionEvent genericRecognitionEvent) throws RemoteException;

    void onKeyphraseDetected(KeyphraseRecognitionEvent keyphraseRecognitionEvent) throws RemoteException;

    void onRecognitionPaused() throws RemoteException;

    void onRecognitionResumed() throws RemoteException;
}
