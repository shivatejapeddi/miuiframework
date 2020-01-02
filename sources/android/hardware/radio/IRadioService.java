package android.hardware.radio;

import android.hardware.radio.RadioManager.BandConfig;
import android.hardware.radio.RadioManager.ModuleProperties;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IRadioService extends IInterface {

    public static class Default implements IRadioService {
        public List<ModuleProperties> listModules() throws RemoteException {
            return null;
        }

        public ITuner openTuner(int moduleId, BandConfig bandConfig, boolean withAudio, ITunerCallback callback) throws RemoteException {
            return null;
        }

        public ICloseHandle addAnnouncementListener(int[] enabledTypes, IAnnouncementListener listener) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRadioService {
        private static final String DESCRIPTOR = "android.hardware.radio.IRadioService";
        static final int TRANSACTION_addAnnouncementListener = 3;
        static final int TRANSACTION_listModules = 1;
        static final int TRANSACTION_openTuner = 2;

        private static class Proxy implements IRadioService {
            public static IRadioService sDefaultImpl;
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

            public List<ModuleProperties> listModules() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ModuleProperties> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().listModules();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ModuleProperties.CREATOR);
                    List<ModuleProperties> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ITuner openTuner(int moduleId, BandConfig bandConfig, boolean withAudio, ITunerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(moduleId);
                    int i = 1;
                    ITuner iTuner = 0;
                    if (bandConfig != null) {
                        _data.writeInt(1);
                        bandConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!withAudio) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iTuner = Stub.getDefaultImpl();
                        if (iTuner != 0) {
                            iTuner = Stub.getDefaultImpl().openTuner(moduleId, bandConfig, withAudio, callback);
                            return iTuner;
                        }
                    }
                    _reply.readException();
                    iTuner = android.hardware.radio.ITuner.Stub.asInterface(_reply.readStrongBinder());
                    ITuner _result = iTuner;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ICloseHandle addAnnouncementListener(int[] enabledTypes, IAnnouncementListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(enabledTypes);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    ICloseHandle iCloseHandle = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iCloseHandle = Stub.getDefaultImpl();
                        if (iCloseHandle != 0) {
                            iCloseHandle = Stub.getDefaultImpl().addAnnouncementListener(enabledTypes, listener);
                            return iCloseHandle;
                        }
                    }
                    _reply.readException();
                    iCloseHandle = android.hardware.radio.ICloseHandle.Stub.asInterface(_reply.readStrongBinder());
                    ICloseHandle _result = iCloseHandle;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRadioService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRadioService)) {
                return new Proxy(obj);
            }
            return (IRadioService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "listModules";
            }
            if (transactionCode == 2) {
                return "openTuner";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "addAnnouncementListener";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != 1) {
                IBinder iBinder = null;
                if (code == 2) {
                    BandConfig _arg1;
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (BandConfig) BandConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    ITuner _result = openTuner(_arg0, _arg1, data.readInt() != 0, android.hardware.radio.ITunerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result != null) {
                        iBinder = _result.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                } else if (code == 3) {
                    data.enforceInterface(descriptor);
                    ICloseHandle _result2 = addAnnouncementListener(data.createIntArray(), android.hardware.radio.IAnnouncementListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result2 != null) {
                        iBinder = _result2.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                } else if (code != IBinder.INTERFACE_TRANSACTION) {
                    return super.onTransact(code, data, reply, flags);
                } else {
                    reply.writeString(descriptor);
                    return true;
                }
            }
            data.enforceInterface(descriptor);
            List<ModuleProperties> _result3 = listModules();
            reply.writeNoException();
            reply.writeTypedList(_result3);
            return true;
        }

        public static boolean setDefaultImpl(IRadioService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRadioService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    ICloseHandle addAnnouncementListener(int[] iArr, IAnnouncementListener iAnnouncementListener) throws RemoteException;

    List<ModuleProperties> listModules() throws RemoteException;

    ITuner openTuner(int i, BandConfig bandConfig, boolean z, ITunerCallback iTunerCallback) throws RemoteException;
}
