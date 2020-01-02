package android.nfc;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import miui.provider.MiProfile;

public interface INfcAdapter extends IInterface {

    public static class Default implements INfcAdapter {
        public INfcTag getNfcTagInterface() throws RemoteException {
            return null;
        }

        public INfcCardEmulation getNfcCardEmulationInterface() throws RemoteException {
            return null;
        }

        public INfcFCardEmulation getNfcFCardEmulationInterface() throws RemoteException {
            return null;
        }

        public INfcAdapterExtras getNfcAdapterExtrasInterface(String pkg) throws RemoteException {
            return null;
        }

        public INfcDta getNfcDtaInterface(String pkg) throws RemoteException {
            return null;
        }

        public IBinder getNfcAdapterVendorInterface(String vendor) throws RemoteException {
            return null;
        }

        public int getState() throws RemoteException {
            return 0;
        }

        public boolean disable(boolean saveState) throws RemoteException {
            return false;
        }

        public boolean enable() throws RemoteException {
            return false;
        }

        public boolean enableNdefPush() throws RemoteException {
            return false;
        }

        public boolean disableNdefPush() throws RemoteException {
            return false;
        }

        public boolean isNdefPushEnabled() throws RemoteException {
            return false;
        }

        public void pausePolling(int timeoutInMs) throws RemoteException {
        }

        public void resumePolling() throws RemoteException {
        }

        public void setForegroundDispatch(PendingIntent intent, IntentFilter[] filters, TechListParcel techLists) throws RemoteException {
        }

        public void setAppCallback(IAppCallback callback) throws RemoteException {
        }

        public void invokeBeam() throws RemoteException {
        }

        public void invokeBeamInternal(BeamShareData shareData) throws RemoteException {
        }

        public boolean ignore(int nativeHandle, int debounceMs, ITagRemovedCallback callback) throws RemoteException {
            return false;
        }

        public void dispatch(Tag tag) throws RemoteException {
        }

        public void setReaderMode(IBinder b, IAppCallback callback, int flags, Bundle extras) throws RemoteException {
        }

        public void setP2pModes(int initatorModes, int targetModes) throws RemoteException {
        }

        public void addNfcUnlockHandler(INfcUnlockHandler unlockHandler, int[] techList) throws RemoteException {
        }

        public void removeNfcUnlockHandler(INfcUnlockHandler unlockHandler) throws RemoteException {
        }

        public void verifyNfcPermission() throws RemoteException {
        }

        public boolean isNfcSecureEnabled() throws RemoteException {
            return false;
        }

        public boolean deviceSupportsNfcSecure() throws RemoteException {
            return false;
        }

        public boolean setNfcSecure(boolean enable) throws RemoteException {
            return false;
        }

        public void setListenTechMask(int flags_ListenMask) throws RemoteException {
        }

        public int getListenTechMask() throws RemoteException {
            return 0;
        }

        public int setConfig(String aid) throws RemoteException {
            return 0;
        }

        public boolean setSeRouting(int seID) throws RemoteException {
            return false;
        }

        public byte[] getNfccDieid() throws RemoteException {
            return null;
        }

        public int getSeRouting() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INfcAdapter {
        private static final String DESCRIPTOR = "android.nfc.INfcAdapter";
        static final int TRANSACTION_addNfcUnlockHandler = 23;
        static final int TRANSACTION_deviceSupportsNfcSecure = 27;
        static final int TRANSACTION_disable = 8;
        static final int TRANSACTION_disableNdefPush = 11;
        static final int TRANSACTION_dispatch = 20;
        static final int TRANSACTION_enable = 9;
        static final int TRANSACTION_enableNdefPush = 10;
        static final int TRANSACTION_getListenTechMask = 30;
        static final int TRANSACTION_getNfcAdapterExtrasInterface = 4;
        static final int TRANSACTION_getNfcAdapterVendorInterface = 6;
        static final int TRANSACTION_getNfcCardEmulationInterface = 2;
        static final int TRANSACTION_getNfcDtaInterface = 5;
        static final int TRANSACTION_getNfcFCardEmulationInterface = 3;
        static final int TRANSACTION_getNfcTagInterface = 1;
        static final int TRANSACTION_getNfccDieid = 33;
        static final int TRANSACTION_getSeRouting = 34;
        static final int TRANSACTION_getState = 7;
        static final int TRANSACTION_ignore = 19;
        static final int TRANSACTION_invokeBeam = 17;
        static final int TRANSACTION_invokeBeamInternal = 18;
        static final int TRANSACTION_isNdefPushEnabled = 12;
        static final int TRANSACTION_isNfcSecureEnabled = 26;
        static final int TRANSACTION_pausePolling = 13;
        static final int TRANSACTION_removeNfcUnlockHandler = 24;
        static final int TRANSACTION_resumePolling = 14;
        static final int TRANSACTION_setAppCallback = 16;
        static final int TRANSACTION_setConfig = 31;
        static final int TRANSACTION_setForegroundDispatch = 15;
        static final int TRANSACTION_setListenTechMask = 29;
        static final int TRANSACTION_setNfcSecure = 28;
        static final int TRANSACTION_setP2pModes = 22;
        static final int TRANSACTION_setReaderMode = 21;
        static final int TRANSACTION_setSeRouting = 32;
        static final int TRANSACTION_verifyNfcPermission = 25;

        private static class Proxy implements INfcAdapter {
            public static INfcAdapter sDefaultImpl;
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

            public INfcTag getNfcTagInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    INfcTag iNfcTag = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iNfcTag = Stub.getDefaultImpl();
                        if (iNfcTag != 0) {
                            iNfcTag = Stub.getDefaultImpl().getNfcTagInterface();
                            return iNfcTag;
                        }
                    }
                    _reply.readException();
                    iNfcTag = android.nfc.INfcTag.Stub.asInterface(_reply.readStrongBinder());
                    INfcTag _result = iNfcTag;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public INfcCardEmulation getNfcCardEmulationInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    INfcCardEmulation iNfcCardEmulation = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iNfcCardEmulation = Stub.getDefaultImpl();
                        if (iNfcCardEmulation != 0) {
                            iNfcCardEmulation = Stub.getDefaultImpl().getNfcCardEmulationInterface();
                            return iNfcCardEmulation;
                        }
                    }
                    _reply.readException();
                    iNfcCardEmulation = android.nfc.INfcCardEmulation.Stub.asInterface(_reply.readStrongBinder());
                    INfcCardEmulation _result = iNfcCardEmulation;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public INfcFCardEmulation getNfcFCardEmulationInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    INfcFCardEmulation iNfcFCardEmulation = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iNfcFCardEmulation = Stub.getDefaultImpl();
                        if (iNfcFCardEmulation != 0) {
                            iNfcFCardEmulation = Stub.getDefaultImpl().getNfcFCardEmulationInterface();
                            return iNfcFCardEmulation;
                        }
                    }
                    _reply.readException();
                    iNfcFCardEmulation = android.nfc.INfcFCardEmulation.Stub.asInterface(_reply.readStrongBinder());
                    INfcFCardEmulation _result = iNfcFCardEmulation;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public INfcAdapterExtras getNfcAdapterExtrasInterface(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    INfcAdapterExtras iNfcAdapterExtras = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        iNfcAdapterExtras = Stub.getDefaultImpl();
                        if (iNfcAdapterExtras != 0) {
                            iNfcAdapterExtras = Stub.getDefaultImpl().getNfcAdapterExtrasInterface(pkg);
                            return iNfcAdapterExtras;
                        }
                    }
                    _reply.readException();
                    iNfcAdapterExtras = android.nfc.INfcAdapterExtras.Stub.asInterface(_reply.readStrongBinder());
                    INfcAdapterExtras _result = iNfcAdapterExtras;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public INfcDta getNfcDtaInterface(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    INfcDta iNfcDta = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        iNfcDta = Stub.getDefaultImpl();
                        if (iNfcDta != 0) {
                            iNfcDta = Stub.getDefaultImpl().getNfcDtaInterface(pkg);
                            return iNfcDta;
                        }
                    }
                    _reply.readException();
                    iNfcDta = android.nfc.INfcDta.Stub.asInterface(_reply.readStrongBinder());
                    INfcDta _result = iNfcDta;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getNfcAdapterVendorInterface(String vendor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(vendor);
                    IBinder iBinder = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getNfcAdapterVendorInterface(vendor);
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState();
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

            public boolean disable(boolean saveState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(saveState ? 1 : 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().disable(saveState);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enable();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableNdefPush() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableNdefPush();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableNdefPush() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableNdefPush();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNdefPushEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNdefPushEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pausePolling(int timeoutInMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeoutInMs);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pausePolling(timeoutInMs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resumePolling() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resumePolling();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForegroundDispatch(PendingIntent intent, IntentFilter[] filters, TechListParcel techLists) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(filters, 0);
                    if (techLists != null) {
                        _data.writeInt(1);
                        techLists.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForegroundDispatch(intent, filters, techLists);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppCallback(IAppCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void invokeBeam() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().invokeBeam();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void invokeBeamInternal(BeamShareData shareData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (shareData != null) {
                        _data.writeInt(1);
                        shareData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().invokeBeamInternal(shareData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean ignore(int nativeHandle, int debounceMs, ITagRemovedCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(nativeHandle);
                    _data.writeInt(debounceMs);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().ignore(nativeHandle, debounceMs, callback);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatch(Tag tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tag != null) {
                        _data.writeInt(1);
                        tag.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispatch(tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setReaderMode(IBinder b, IAppCallback callback, int flags, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(b);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setReaderMode(b, callback, flags, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setP2pModes(int initatorModes, int targetModes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(initatorModes);
                    _data.writeInt(targetModes);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setP2pModes(initatorModes, targetModes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addNfcUnlockHandler(INfcUnlockHandler unlockHandler, int[] techList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(unlockHandler != null ? unlockHandler.asBinder() : null);
                    _data.writeIntArray(techList);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addNfcUnlockHandler(unlockHandler, techList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeNfcUnlockHandler(INfcUnlockHandler unlockHandler) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(unlockHandler != null ? unlockHandler.asBinder() : null);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeNfcUnlockHandler(unlockHandler);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void verifyNfcPermission() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().verifyNfcPermission();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isNfcSecureEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNfcSecureEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deviceSupportsNfcSecure() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deviceSupportsNfcSecure();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setNfcSecure(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNfcSecure(enable);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setListenTechMask(int flags_ListenMask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags_ListenMask);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setListenTechMask(flags_ListenMask);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getListenTechMask() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getListenTechMask();
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

            public int setConfig(String aid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aid);
                    int i = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setConfig(aid);
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

            public boolean setSeRouting(int seID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seID);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setSeRouting(seID);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getNfccDieid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getNfccDieid();
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSeRouting() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSeRouting();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INfcAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INfcAdapter)) {
                return new Proxy(obj);
            }
            return (INfcAdapter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getNfcTagInterface";
                case 2:
                    return "getNfcCardEmulationInterface";
                case 3:
                    return "getNfcFCardEmulationInterface";
                case 4:
                    return "getNfcAdapterExtrasInterface";
                case 5:
                    return "getNfcDtaInterface";
                case 6:
                    return "getNfcAdapterVendorInterface";
                case 7:
                    return "getState";
                case 8:
                    return "disable";
                case 9:
                    return "enable";
                case 10:
                    return "enableNdefPush";
                case 11:
                    return "disableNdefPush";
                case 12:
                    return "isNdefPushEnabled";
                case 13:
                    return "pausePolling";
                case 14:
                    return "resumePolling";
                case 15:
                    return "setForegroundDispatch";
                case 16:
                    return "setAppCallback";
                case 17:
                    return "invokeBeam";
                case 18:
                    return "invokeBeamInternal";
                case 19:
                    return MiProfile.MIPROFILE_IGNORE;
                case 20:
                    return "dispatch";
                case 21:
                    return "setReaderMode";
                case 22:
                    return "setP2pModes";
                case 23:
                    return "addNfcUnlockHandler";
                case 24:
                    return "removeNfcUnlockHandler";
                case 25:
                    return "verifyNfcPermission";
                case 26:
                    return "isNfcSecureEnabled";
                case 27:
                    return "deviceSupportsNfcSecure";
                case 28:
                    return "setNfcSecure";
                case 29:
                    return "setListenTechMask";
                case 30:
                    return "getListenTechMask";
                case 31:
                    return "setConfig";
                case 32:
                    return "setSeRouting";
                case 33:
                    return "getNfccDieid";
                case 34:
                    return "getSeRouting";
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
                boolean _arg0 = false;
                IBinder iBinder = null;
                int _result;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        INfcTag _result3 = getNfcTagInterface();
                        reply.writeNoException();
                        if (_result3 != null) {
                            iBinder = _result3.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        INfcCardEmulation _result4 = getNfcCardEmulationInterface();
                        reply.writeNoException();
                        if (_result4 != null) {
                            iBinder = _result4.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        INfcFCardEmulation _result5 = getNfcFCardEmulationInterface();
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        INfcAdapterExtras _result6 = getNfcAdapterExtrasInterface(data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            iBinder = _result6.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        INfcDta _result7 = getNfcDtaInterface(data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            iBinder = _result7.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        iBinder = getNfcAdapterVendorInterface(data.readString());
                        reply.writeNoException();
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = getState();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result2 = disable(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg0 = enable();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = enableNdefPush();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _arg0 = disableNdefPush();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _arg0 = isNdefPushEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        pausePolling(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        resumePolling();
                        reply.writeNoException();
                        return true;
                    case 15:
                        PendingIntent _arg02;
                        TechListParcel _arg2;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        IntentFilter[] _arg1 = (IntentFilter[]) data.createTypedArray(IntentFilter.CREATOR);
                        if (data.readInt() != 0) {
                            _arg2 = (TechListParcel) TechListParcel.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        setForegroundDispatch(_arg02, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        setAppCallback(android.nfc.IAppCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        invokeBeam();
                        return true;
                    case 18:
                        BeamShareData _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (BeamShareData) BeamShareData.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        invokeBeamInternal(_arg03);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        boolean _result8 = ignore(data.readInt(), data.readInt(), android.nfc.ITagRemovedCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 20:
                        Tag _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Tag) Tag.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        dispatch(_arg04);
                        reply.writeNoException();
                        return true;
                    case 21:
                        Bundle _arg3;
                        data.enforceInterface(descriptor);
                        IBinder _arg05 = data.readStrongBinder();
                        IAppCallback _arg12 = android.nfc.IAppCallback.Stub.asInterface(data.readStrongBinder());
                        int _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        setReaderMode(_arg05, _arg12, _arg22, _arg3);
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        setP2pModes(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        addNfcUnlockHandler(android.nfc.INfcUnlockHandler.Stub.asInterface(data.readStrongBinder()), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        removeNfcUnlockHandler(android.nfc.INfcUnlockHandler.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        verifyNfcPermission();
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _arg0 = isNfcSecureEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _arg0 = deviceSupportsNfcSecure();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result2 = setNfcSecure(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        setListenTechMask(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        _result = getListenTechMask();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        int _result9 = setConfig(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        _result2 = setSeRouting(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        byte[] _result10 = getNfccDieid();
                        reply.writeNoException();
                        reply.writeByteArray(_result10);
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        _result = getSeRouting();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INfcAdapter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INfcAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addNfcUnlockHandler(INfcUnlockHandler iNfcUnlockHandler, int[] iArr) throws RemoteException;

    boolean deviceSupportsNfcSecure() throws RemoteException;

    boolean disable(boolean z) throws RemoteException;

    boolean disableNdefPush() throws RemoteException;

    void dispatch(Tag tag) throws RemoteException;

    boolean enable() throws RemoteException;

    boolean enableNdefPush() throws RemoteException;

    int getListenTechMask() throws RemoteException;

    INfcAdapterExtras getNfcAdapterExtrasInterface(String str) throws RemoteException;

    IBinder getNfcAdapterVendorInterface(String str) throws RemoteException;

    INfcCardEmulation getNfcCardEmulationInterface() throws RemoteException;

    INfcDta getNfcDtaInterface(String str) throws RemoteException;

    INfcFCardEmulation getNfcFCardEmulationInterface() throws RemoteException;

    INfcTag getNfcTagInterface() throws RemoteException;

    byte[] getNfccDieid() throws RemoteException;

    int getSeRouting() throws RemoteException;

    int getState() throws RemoteException;

    boolean ignore(int i, int i2, ITagRemovedCallback iTagRemovedCallback) throws RemoteException;

    void invokeBeam() throws RemoteException;

    void invokeBeamInternal(BeamShareData beamShareData) throws RemoteException;

    boolean isNdefPushEnabled() throws RemoteException;

    boolean isNfcSecureEnabled() throws RemoteException;

    void pausePolling(int i) throws RemoteException;

    void removeNfcUnlockHandler(INfcUnlockHandler iNfcUnlockHandler) throws RemoteException;

    void resumePolling() throws RemoteException;

    void setAppCallback(IAppCallback iAppCallback) throws RemoteException;

    int setConfig(String str) throws RemoteException;

    void setForegroundDispatch(PendingIntent pendingIntent, IntentFilter[] intentFilterArr, TechListParcel techListParcel) throws RemoteException;

    void setListenTechMask(int i) throws RemoteException;

    boolean setNfcSecure(boolean z) throws RemoteException;

    void setP2pModes(int i, int i2) throws RemoteException;

    void setReaderMode(IBinder iBinder, IAppCallback iAppCallback, int i, Bundle bundle) throws RemoteException;

    boolean setSeRouting(int i) throws RemoteException;

    void verifyNfcPermission() throws RemoteException;
}
