package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.ResultReceiver;

public interface IBluetooth extends IInterface {

    public static class Default implements IBluetooth {
        public boolean isEnabled() throws RemoteException {
            return false;
        }

        public int getState() throws RemoteException {
            return 0;
        }

        public boolean enable() throws RemoteException {
            return false;
        }

        public boolean enableNoAutoConnect() throws RemoteException {
            return false;
        }

        public boolean disable() throws RemoteException {
            return false;
        }

        public String getAddress() throws RemoteException {
            return null;
        }

        public ParcelUuid[] getUuids() throws RemoteException {
            return null;
        }

        public boolean setName(String name) throws RemoteException {
            return false;
        }

        public String getName() throws RemoteException {
            return null;
        }

        public BluetoothClass getBluetoothClass() throws RemoteException {
            return null;
        }

        public boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException {
            return false;
        }

        public int getIoCapability() throws RemoteException {
            return 0;
        }

        public boolean setIoCapability(int capability) throws RemoteException {
            return false;
        }

        public int getLeIoCapability() throws RemoteException {
            return 0;
        }

        public boolean setLeIoCapability(int capability) throws RemoteException {
            return false;
        }

        public int getScanMode() throws RemoteException {
            return 0;
        }

        public boolean setScanMode(int mode, int duration) throws RemoteException {
            return false;
        }

        public int getDiscoverableTimeout() throws RemoteException {
            return 0;
        }

        public boolean setDiscoverableTimeout(int timeout) throws RemoteException {
            return false;
        }

        public boolean startDiscovery(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean cancelDiscovery() throws RemoteException {
            return false;
        }

        public boolean isDiscovering() throws RemoteException {
            return false;
        }

        public long getDiscoveryEndMillis() throws RemoteException {
            return 0;
        }

        public int getAdapterConnectionState() throws RemoteException {
            return 0;
        }

        public int getProfileConnectionState(int profile) throws RemoteException {
            return 0;
        }

        public BluetoothDevice[] getBondedDevices() throws RemoteException {
            return null;
        }

        public boolean createBond(BluetoothDevice device, int transport) throws RemoteException {
            return false;
        }

        public boolean createBondOutOfBand(BluetoothDevice device, int transport, OobData oobData) throws RemoteException {
            return false;
        }

        public boolean cancelBondProcess(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean removeBond(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public int getBondState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean isBondingInitiatedLocally(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public void setBondingInitiatedLocally(BluetoothDevice devicei, boolean localInitiated) throws RemoteException {
        }

        public long getSupportedProfiles() throws RemoteException {
            return 0;
        }

        public int getConnectionState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public String getRemoteName(BluetoothDevice device) throws RemoteException {
            return null;
        }

        public int getRemoteType(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public String getRemoteAlias(BluetoothDevice device) throws RemoteException {
            return null;
        }

        public boolean setRemoteAlias(BluetoothDevice device, String name) throws RemoteException {
            return false;
        }

        public int getRemoteClass(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public ParcelUuid[] getRemoteUuids(BluetoothDevice device) throws RemoteException {
            return null;
        }

        public boolean fetchRemoteUuids(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean sdpSearch(BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
            return false;
        }

        public int getBatteryLevel(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public int getMaxConnectedAudioDevices() throws RemoteException {
            return 0;
        }

        public boolean isTwsPlusDevice(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public String getTwsPlusPeerAddress(BluetoothDevice device) throws RemoteException {
            return null;
        }

        public boolean setPin(BluetoothDevice device, boolean accept, int len, byte[] pinCode) throws RemoteException {
            return false;
        }

        public boolean setPasskey(BluetoothDevice device, boolean accept, int len, byte[] passkey) throws RemoteException {
            return false;
        }

        public boolean setPairingConfirmation(BluetoothDevice device, boolean accept) throws RemoteException {
            return false;
        }

        public int getPhonebookAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean setSilenceMode(BluetoothDevice device, boolean silence) throws RemoteException {
            return false;
        }

        public boolean getSilenceMode(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean setPhonebookAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        public int getMessageAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean setMessageAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        public int getSimAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean setSimAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        public void sendConnectionStateChange(BluetoothDevice device, int profile, int state, int prevState) throws RemoteException {
        }

        public void registerCallback(IBluetoothCallback callback) throws RemoteException {
        }

        public void unregisterCallback(IBluetoothCallback callback) throws RemoteException {
        }

        public IBluetoothSocketManager getSocketManager() throws RemoteException {
            return null;
        }

        public boolean factoryReset() throws RemoteException {
            return false;
        }

        public boolean isMultiAdvertisementSupported() throws RemoteException {
            return false;
        }

        public boolean isOffloadedFilteringSupported() throws RemoteException {
            return false;
        }

        public boolean isOffloadedScanBatchingSupported() throws RemoteException {
            return false;
        }

        public boolean isActivityAndEnergyReportingSupported() throws RemoteException {
            return false;
        }

        public boolean isLe2MPhySupported() throws RemoteException {
            return false;
        }

        public boolean isLeCodedPhySupported() throws RemoteException {
            return false;
        }

        public boolean isLeExtendedAdvertisingSupported() throws RemoteException {
            return false;
        }

        public boolean isLePeriodicAdvertisingSupported() throws RemoteException {
            return false;
        }

        public int getLeMaximumAdvertisingDataLength() throws RemoteException {
            return 0;
        }

        public BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException {
            return null;
        }

        public boolean registerMetadataListener(IBluetoothMetadataListener listener, BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean unregisterMetadataListener(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean setMetadata(BluetoothDevice device, int key, byte[] value) throws RemoteException {
            return false;
        }

        public byte[] getMetadata(BluetoothDevice device, int key) throws RemoteException {
            return null;
        }

        public void requestActivityInfo(ResultReceiver result) throws RemoteException {
        }

        public void onLeServiceUp() throws RemoteException {
        }

        public void updateQuietModeStatus(boolean quietMode) throws RemoteException {
        }

        public void onBrEdrDown() throws RemoteException {
        }

        public int setSocketOpt(int type, int port, int optionName, byte[] optionVal, int optionLen) throws RemoteException {
            return 0;
        }

        public int getSocketOpt(int type, int port, int optionName, byte[] optionVal) throws RemoteException {
            return 0;
        }

        public int getSpecificCodecStatus(BluetoothDevice device, String codec) throws RemoteException {
            return 0;
        }

        public boolean setSpecificCodecStatus(BluetoothDevice device, String codec, int value) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetooth {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetooth";
        static final int TRANSACTION_cancelBondProcess = 29;
        static final int TRANSACTION_cancelDiscovery = 21;
        static final int TRANSACTION_createBond = 27;
        static final int TRANSACTION_createBondOutOfBand = 28;
        static final int TRANSACTION_disable = 5;
        static final int TRANSACTION_enable = 3;
        static final int TRANSACTION_enableNoAutoConnect = 4;
        static final int TRANSACTION_factoryReset = 63;
        static final int TRANSACTION_fetchRemoteUuids = 42;
        static final int TRANSACTION_getAdapterConnectionState = 24;
        static final int TRANSACTION_getAddress = 6;
        static final int TRANSACTION_getBatteryLevel = 44;
        static final int TRANSACTION_getBluetoothClass = 10;
        static final int TRANSACTION_getBondState = 31;
        static final int TRANSACTION_getBondedDevices = 26;
        static final int TRANSACTION_getConnectionState = 35;
        static final int TRANSACTION_getDiscoverableTimeout = 18;
        static final int TRANSACTION_getDiscoveryEndMillis = 23;
        static final int TRANSACTION_getIoCapability = 12;
        static final int TRANSACTION_getLeIoCapability = 14;
        static final int TRANSACTION_getLeMaximumAdvertisingDataLength = 72;
        static final int TRANSACTION_getMaxConnectedAudioDevices = 45;
        static final int TRANSACTION_getMessageAccessPermission = 55;
        static final int TRANSACTION_getMetadata = 77;
        static final int TRANSACTION_getName = 9;
        static final int TRANSACTION_getPhonebookAccessPermission = 51;
        static final int TRANSACTION_getProfileConnectionState = 25;
        static final int TRANSACTION_getRemoteAlias = 38;
        static final int TRANSACTION_getRemoteClass = 40;
        static final int TRANSACTION_getRemoteName = 36;
        static final int TRANSACTION_getRemoteType = 37;
        static final int TRANSACTION_getRemoteUuids = 41;
        static final int TRANSACTION_getScanMode = 16;
        static final int TRANSACTION_getSilenceMode = 53;
        static final int TRANSACTION_getSimAccessPermission = 57;
        static final int TRANSACTION_getSocketManager = 62;
        static final int TRANSACTION_getSocketOpt = 83;
        static final int TRANSACTION_getSpecificCodecStatus = 84;
        static final int TRANSACTION_getState = 2;
        static final int TRANSACTION_getSupportedProfiles = 34;
        static final int TRANSACTION_getTwsPlusPeerAddress = 47;
        static final int TRANSACTION_getUuids = 7;
        static final int TRANSACTION_isActivityAndEnergyReportingSupported = 67;
        static final int TRANSACTION_isBondingInitiatedLocally = 32;
        static final int TRANSACTION_isDiscovering = 22;
        static final int TRANSACTION_isEnabled = 1;
        static final int TRANSACTION_isLe2MPhySupported = 68;
        static final int TRANSACTION_isLeCodedPhySupported = 69;
        static final int TRANSACTION_isLeExtendedAdvertisingSupported = 70;
        static final int TRANSACTION_isLePeriodicAdvertisingSupported = 71;
        static final int TRANSACTION_isMultiAdvertisementSupported = 64;
        static final int TRANSACTION_isOffloadedFilteringSupported = 65;
        static final int TRANSACTION_isOffloadedScanBatchingSupported = 66;
        static final int TRANSACTION_isTwsPlusDevice = 46;
        static final int TRANSACTION_onBrEdrDown = 81;
        static final int TRANSACTION_onLeServiceUp = 79;
        static final int TRANSACTION_registerCallback = 60;
        static final int TRANSACTION_registerMetadataListener = 74;
        static final int TRANSACTION_removeBond = 30;
        static final int TRANSACTION_reportActivityInfo = 73;
        static final int TRANSACTION_requestActivityInfo = 78;
        static final int TRANSACTION_sdpSearch = 43;
        static final int TRANSACTION_sendConnectionStateChange = 59;
        static final int TRANSACTION_setBluetoothClass = 11;
        static final int TRANSACTION_setBondingInitiatedLocally = 33;
        static final int TRANSACTION_setDiscoverableTimeout = 19;
        static final int TRANSACTION_setIoCapability = 13;
        static final int TRANSACTION_setLeIoCapability = 15;
        static final int TRANSACTION_setMessageAccessPermission = 56;
        static final int TRANSACTION_setMetadata = 76;
        static final int TRANSACTION_setName = 8;
        static final int TRANSACTION_setPairingConfirmation = 50;
        static final int TRANSACTION_setPasskey = 49;
        static final int TRANSACTION_setPhonebookAccessPermission = 54;
        static final int TRANSACTION_setPin = 48;
        static final int TRANSACTION_setRemoteAlias = 39;
        static final int TRANSACTION_setScanMode = 17;
        static final int TRANSACTION_setSilenceMode = 52;
        static final int TRANSACTION_setSimAccessPermission = 58;
        static final int TRANSACTION_setSocketOpt = 82;
        static final int TRANSACTION_setSpecificCodecStatus = 85;
        static final int TRANSACTION_startDiscovery = 20;
        static final int TRANSACTION_unregisterCallback = 61;
        static final int TRANSACTION_unregisterMetadataListener = 75;
        static final int TRANSACTION_updateQuietModeStatus = 80;

        private static class Proxy implements IBluetooth {
            public static IBluetooth sDefaultImpl;
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

            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().isEnabled();
                    return z;
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
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
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

            public boolean enable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
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

            public boolean enableNoAutoConnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableNoAutoConnect();
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

            public boolean disable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disable();
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

            public String getAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAddress();
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

            public ParcelUuid[] getUuids() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelUuid[] parcelUuidArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        parcelUuidArr = Stub.getDefaultImpl();
                        if (parcelUuidArr != 0) {
                            parcelUuidArr = Stub.getDefaultImpl().getUuids();
                            return parcelUuidArr;
                        }
                    }
                    _reply.readException();
                    ParcelUuid[] _result = (ParcelUuid[]) _reply.createTypedArray(ParcelUuid.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setName(name);
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

            public String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getName();
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

            public BluetoothClass getBluetoothClass() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothClass bluetoothClass = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        bluetoothClass = Stub.getDefaultImpl();
                        if (bluetoothClass != 0) {
                            bluetoothClass = Stub.getDefaultImpl().getBluetoothClass();
                            return bluetoothClass;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bluetoothClass = (BluetoothClass) BluetoothClass.CREATOR.createFromParcel(_reply);
                    } else {
                        bluetoothClass = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bluetoothClass;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (bluetoothClass != null) {
                        _data.writeInt(1);
                        bluetoothClass.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setBluetoothClass(bluetoothClass);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getIoCapability() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getIoCapability();
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

            public boolean setIoCapability(int capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setIoCapability(capability);
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

            public int getLeIoCapability() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLeIoCapability();
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

            public boolean setLeIoCapability(int capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setLeIoCapability(capability);
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

            public int getScanMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getScanMode();
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

            public boolean setScanMode(int mode, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(duration);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setScanMode(mode, duration);
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

            public int getDiscoverableTimeout() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDiscoverableTimeout();
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

            public boolean setDiscoverableTimeout(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setDiscoverableTimeout(timeout);
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

            public boolean startDiscovery(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startDiscovery(callingPackage);
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

            public boolean cancelDiscovery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().cancelDiscovery();
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

            public boolean isDiscovering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDiscovering();
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

            public long getDiscoveryEndMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getDiscoveryEndMillis();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAdapterConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAdapterConnectionState();
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

            public int getProfileConnectionState(int profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    int i = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getProfileConnectionState(profile);
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

            public BluetoothDevice[] getBondedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothDevice[] bluetoothDeviceArr = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        bluetoothDeviceArr = Stub.getDefaultImpl();
                        if (bluetoothDeviceArr != 0) {
                            bluetoothDeviceArr = Stub.getDefaultImpl().getBondedDevices();
                            return bluetoothDeviceArr;
                        }
                    }
                    _reply.readException();
                    BluetoothDevice[] _result = (BluetoothDevice[]) _reply.createTypedArray(BluetoothDevice.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean createBond(BluetoothDevice device, int transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(transport);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().createBond(device, transport);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean createBondOutOfBand(BluetoothDevice device, int transport, OobData oobData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(transport);
                    if (oobData != null) {
                        _data.writeInt(1);
                        oobData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().createBondOutOfBand(device, transport, oobData);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean cancelBondProcess(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().cancelBondProcess(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeBond(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeBond(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBondState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getBondState(device);
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

            public boolean isBondingInitiatedLocally(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isBondingInitiatedLocally(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBondingInitiatedLocally(BluetoothDevice devicei, boolean localInitiated) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (devicei != null) {
                        _data.writeInt(1);
                        devicei.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!localInitiated) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBondingInitiatedLocally(devicei, localInitiated);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getSupportedProfiles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getSupportedProfiles();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getConnectionState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(35, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getConnectionState(device);
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

            public String getRemoteName(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(36, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getRemoteName(device);
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

            public int getRemoteType(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getRemoteType(device);
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

            public String getRemoteAlias(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(38, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getRemoteAlias(device);
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

            public boolean setRemoteAlias(BluetoothDevice device, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setRemoteAlias(device, name);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRemoteClass(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(40, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getRemoteClass(device);
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

            public ParcelUuid[] getRemoteUuids(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParcelUuid[] parcelUuidArr = this.mRemote;
                    if (!parcelUuidArr.transact(41, _data, _reply, 0)) {
                        parcelUuidArr = Stub.getDefaultImpl();
                        if (parcelUuidArr != null) {
                            parcelUuidArr = Stub.getDefaultImpl().getRemoteUuids(device);
                            return parcelUuidArr;
                        }
                    }
                    _reply.readException();
                    ParcelUuid[] _result = (ParcelUuid[]) _reply.createTypedArray(ParcelUuid.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean fetchRemoteUuids(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().fetchRemoteUuids(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean sdpSearch(BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().sdpSearch(device, uuid);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBatteryLevel(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(44, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getBatteryLevel(device);
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

            public int getMaxConnectedAudioDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 45;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMaxConnectedAudioDevices();
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

            public boolean isTwsPlusDevice(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isTwsPlusDevice(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTwsPlusPeerAddress(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(47, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getTwsPlusPeerAddress(device);
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

            public boolean setPin(BluetoothDevice device, boolean accept, int len, byte[] pinCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    _data.writeInt(len);
                    _data.writeByteArray(pinCode);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPin(device, accept, len, pinCode);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPasskey(BluetoothDevice device, boolean accept, int len, byte[] passkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    _data.writeInt(len);
                    _data.writeByteArray(passkey);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPasskey(device, accept, len, passkey);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPairingConfirmation(BluetoothDevice device, boolean accept) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPairingConfirmation(device, accept);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPhonebookAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(51, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getPhonebookAccessPermission(device);
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

            public boolean setSilenceMode(BluetoothDevice device, boolean silence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(silence ? 1 : 0);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setSilenceMode(device, silence);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSilenceMode(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getSilenceMode(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPhonebookAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPhonebookAccessPermission(device, value);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMessageAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(55, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMessageAccessPermission(device);
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

            public boolean setMessageAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setMessageAccessPermission(device, value);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSimAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(57, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getSimAccessPermission(device);
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

            public boolean setSimAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    if (this.mRemote.transact(58, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setSimAccessPermission(device, value);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendConnectionStateChange(BluetoothDevice device, int profile, int state, int prevState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profile);
                    _data.writeInt(state);
                    _data.writeInt(prevState);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendConnectionStateChange(device, profile, state, prevState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IBluetoothCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(IBluetoothCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBluetoothSocketManager getSocketManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBluetoothSocketManager iBluetoothSocketManager = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        iBluetoothSocketManager = Stub.getDefaultImpl();
                        if (iBluetoothSocketManager != 0) {
                            iBluetoothSocketManager = Stub.getDefaultImpl().getSocketManager();
                            return iBluetoothSocketManager;
                        }
                    }
                    _reply.readException();
                    iBluetoothSocketManager = android.bluetooth.IBluetoothSocketManager.Stub.asInterface(_reply.readStrongBinder());
                    IBluetoothSocketManager _result = iBluetoothSocketManager;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(63, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().factoryReset();
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

            public boolean isMultiAdvertisementSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(64, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMultiAdvertisementSupported();
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

            public boolean isOffloadedFilteringSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(65, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOffloadedFilteringSupported();
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

            public boolean isOffloadedScanBatchingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOffloadedScanBatchingSupported();
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

            public boolean isActivityAndEnergyReportingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isActivityAndEnergyReportingSupported();
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

            public boolean isLe2MPhySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLe2MPhySupported();
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

            public boolean isLeCodedPhySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLeCodedPhySupported();
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

            public boolean isLeExtendedAdvertisingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(70, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLeExtendedAdvertisingSupported();
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

            public boolean isLePeriodicAdvertisingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLePeriodicAdvertisingSupported();
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

            public int getLeMaximumAdvertisingDataLength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 72;
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLeMaximumAdvertisingDataLength();
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

            public BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        bluetoothActivityEnergyInfo = Stub.getDefaultImpl();
                        if (bluetoothActivityEnergyInfo != 0) {
                            bluetoothActivityEnergyInfo = Stub.getDefaultImpl().reportActivityInfo();
                            return bluetoothActivityEnergyInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bluetoothActivityEnergyInfo = (BluetoothActivityEnergyInfo) BluetoothActivityEnergyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        bluetoothActivityEnergyInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bluetoothActivityEnergyInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerMetadataListener(IBluetoothMetadataListener listener, BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(74, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().registerMetadataListener(listener, device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterMetadataListener(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(75, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().unregisterMetadataListener(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setMetadata(BluetoothDevice device, int key, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setMetadata(device, key, value);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getMetadata(BluetoothDevice device, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    byte[] bArr = this.mRemote;
                    if (!bArr.transact(77, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != null) {
                            bArr = Stub.getDefaultImpl().getMetadata(device, key);
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

            public void requestActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(78, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestActivityInfo(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLeServiceUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onLeServiceUp();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateQuietModeStatus(boolean quietMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(quietMode ? 1 : 0);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateQuietModeStatus(quietMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBrEdrDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBrEdrDown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setSocketOpt(int type, int port, int optionName, byte[] optionVal, int optionLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(port);
                    _data.writeInt(optionName);
                    _data.writeByteArray(optionVal);
                    _data.writeInt(optionLen);
                    int i = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setSocketOpt(type, port, optionName, optionVal, optionLen);
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

            public int getSocketOpt(int type, int port, int optionName, byte[] optionVal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(port);
                    _data.writeInt(optionName);
                    if (optionVal == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(optionVal.length);
                    }
                    int _result = 83;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        _result = Stub.getDefaultImpl();
                        if (_result != 0) {
                            _result = Stub.getDefaultImpl().getSocketOpt(type, port, optionName, optionVal);
                            return _result;
                        }
                    }
                    _reply.readException();
                    _result = _reply.readInt();
                    _reply.readByteArray(optionVal);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSpecificCodecStatus(BluetoothDevice device, String codec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(codec);
                    int i = this.mRemote;
                    if (!i.transact(84, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getSpecificCodecStatus(device, codec);
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

            public boolean setSpecificCodecStatus(BluetoothDevice device, String codec, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(codec);
                    _data.writeInt(value);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setSpecificCodecStatus(device, codec, value);
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

        public static IBluetooth asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetooth)) {
                return new Proxy(obj);
            }
            return (IBluetooth) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isEnabled";
                case 2:
                    return "getState";
                case 3:
                    return "enable";
                case 4:
                    return "enableNoAutoConnect";
                case 5:
                    return "disable";
                case 6:
                    return "getAddress";
                case 7:
                    return "getUuids";
                case 8:
                    return "setName";
                case 9:
                    return "getName";
                case 10:
                    return "getBluetoothClass";
                case 11:
                    return "setBluetoothClass";
                case 12:
                    return "getIoCapability";
                case 13:
                    return "setIoCapability";
                case 14:
                    return "getLeIoCapability";
                case 15:
                    return "setLeIoCapability";
                case 16:
                    return "getScanMode";
                case 17:
                    return "setScanMode";
                case 18:
                    return "getDiscoverableTimeout";
                case 19:
                    return "setDiscoverableTimeout";
                case 20:
                    return "startDiscovery";
                case 21:
                    return "cancelDiscovery";
                case 22:
                    return "isDiscovering";
                case 23:
                    return "getDiscoveryEndMillis";
                case 24:
                    return "getAdapterConnectionState";
                case 25:
                    return "getProfileConnectionState";
                case 26:
                    return "getBondedDevices";
                case 27:
                    return "createBond";
                case 28:
                    return "createBondOutOfBand";
                case 29:
                    return "cancelBondProcess";
                case 30:
                    return "removeBond";
                case 31:
                    return "getBondState";
                case 32:
                    return "isBondingInitiatedLocally";
                case 33:
                    return "setBondingInitiatedLocally";
                case 34:
                    return "getSupportedProfiles";
                case 35:
                    return "getConnectionState";
                case 36:
                    return "getRemoteName";
                case 37:
                    return "getRemoteType";
                case 38:
                    return "getRemoteAlias";
                case 39:
                    return "setRemoteAlias";
                case 40:
                    return "getRemoteClass";
                case 41:
                    return "getRemoteUuids";
                case 42:
                    return "fetchRemoteUuids";
                case 43:
                    return "sdpSearch";
                case 44:
                    return "getBatteryLevel";
                case 45:
                    return "getMaxConnectedAudioDevices";
                case 46:
                    return "isTwsPlusDevice";
                case 47:
                    return "getTwsPlusPeerAddress";
                case 48:
                    return "setPin";
                case 49:
                    return "setPasskey";
                case 50:
                    return "setPairingConfirmation";
                case 51:
                    return "getPhonebookAccessPermission";
                case 52:
                    return "setSilenceMode";
                case 53:
                    return "getSilenceMode";
                case 54:
                    return "setPhonebookAccessPermission";
                case 55:
                    return "getMessageAccessPermission";
                case 56:
                    return "setMessageAccessPermission";
                case 57:
                    return "getSimAccessPermission";
                case 58:
                    return "setSimAccessPermission";
                case 59:
                    return "sendConnectionStateChange";
                case 60:
                    return "registerCallback";
                case 61:
                    return "unregisterCallback";
                case 62:
                    return "getSocketManager";
                case 63:
                    return "factoryReset";
                case 64:
                    return "isMultiAdvertisementSupported";
                case 65:
                    return "isOffloadedFilteringSupported";
                case 66:
                    return "isOffloadedScanBatchingSupported";
                case 67:
                    return "isActivityAndEnergyReportingSupported";
                case 68:
                    return "isLe2MPhySupported";
                case 69:
                    return "isLeCodedPhySupported";
                case 70:
                    return "isLeExtendedAdvertisingSupported";
                case 71:
                    return "isLePeriodicAdvertisingSupported";
                case 72:
                    return "getLeMaximumAdvertisingDataLength";
                case 73:
                    return "reportActivityInfo";
                case 74:
                    return "registerMetadataListener";
                case 75:
                    return "unregisterMetadataListener";
                case 76:
                    return "setMetadata";
                case 77:
                    return "getMetadata";
                case 78:
                    return "requestActivityInfo";
                case 79:
                    return "onLeServiceUp";
                case 80:
                    return "updateQuietModeStatus";
                case 81:
                    return "onBrEdrDown";
                case 82:
                    return "setSocketOpt";
                case 83:
                    return "getSocketOpt";
                case 84:
                    return "getSpecificCodecStatus";
                case 85:
                    return "setSpecificCodecStatus";
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
                boolean _arg1 = false;
                int _result;
                String _result2;
                boolean _result3;
                boolean _result4;
                long _result5;
                int _result6;
                BluetoothDevice _arg0;
                boolean _result7;
                BluetoothDevice _arg02;
                String _result8;
                boolean _result9;
                int _arg2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = getState();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg1 = enable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg1 = enableNoAutoConnect();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg1 = disable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAddress();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        ParcelUuid[] _result10 = getUuids();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result10, 1);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result3 = setName(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result2 = getName();
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        BluetoothClass _result11 = getBluetoothClass();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        BluetoothClass _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (BluetoothClass) BluetoothClass.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result3 = setBluetoothClass(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = getIoCapability();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result3 = setIoCapability(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result = getLeIoCapability();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result3 = setLeIoCapability(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result = getScanMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result4 = setScanMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result = getDiscoverableTimeout();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result3 = setDiscoverableTimeout(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result3 = startDiscovery(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg1 = cancelDiscovery();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDiscovering();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result5 = getDiscoveryEndMillis();
                        reply.writeNoException();
                        parcel2.writeLong(_result5);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result = getAdapterConnectionState();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _result6 = getProfileConnectionState(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        BluetoothDevice[] _result12 = getBondedDevices();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result12, 1);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = createBond(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 28:
                        OobData _arg22;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (OobData) OobData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _result7 = createBondOutOfBand(_arg0, _result6, _arg22);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = cancelBondProcess(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = removeBond(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getBondState(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isBondingInitiatedLocally(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setBondingInitiatedLocally(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result5 = getSupportedProfiles();
                        reply.writeNoException();
                        parcel2.writeLong(_result5);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getConnectionState(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result8 = getRemoteName(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getRemoteType(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result8 = getRemoteAlias(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setRemoteAlias(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getRemoteClass(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        ParcelUuid[] _result13 = getRemoteUuids(_arg0);
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result13, 1);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = fetchRemoteUuids(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 43:
                        ParcelUuid _arg12;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        _result4 = sdpSearch(_arg0, _arg12);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getBatteryLevel(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _result = getMaxConnectedAudioDevices();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = isTwsPlusDevice(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result8 = getTwsPlusPeerAddress(_arg0);
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result9 = setPin(_arg02, _arg1, data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result9 = setPasskey(_arg02, _arg1, data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = setPairingConfirmation(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getPhonebookAccessPermission(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result4 = setSilenceMode(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = getSilenceMode(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setPhonebookAccessPermission(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getMessageAccessPermission(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setMessageAccessPermission(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result6 = getSimAccessPermission(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result4 = setSimAccessPermission(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        sendConnectionStateChange(_arg0, data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        registerCallback(android.bluetooth.IBluetoothCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        unregisterCallback(android.bluetooth.IBluetoothCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        IBluetoothSocketManager _result14 = getSocketManager();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result14 != null ? _result14.asBinder() : null);
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        _arg1 = factoryReset();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isMultiAdvertisementSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isOffloadedFilteringSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isOffloadedScanBatchingSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isActivityAndEnergyReportingSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isLe2MPhySupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isLeCodedPhySupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isLeExtendedAdvertisingSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isLePeriodicAdvertisingSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        _result = getLeMaximumAdvertisingDataLength();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        BluetoothActivityEnergyInfo _result15 = reportActivityInfo();
                        reply.writeNoException();
                        if (_result15 != null) {
                            parcel2.writeInt(1);
                            _result15.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        IBluetoothMetadataListener _arg04 = android.bluetooth.IBluetoothMetadataListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result4 = registerMetadataListener(_arg04, _arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = unregisterMetadataListener(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setMetadata(_arg0, data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        byte[] _result16 = getMetadata(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result16);
                        return true;
                    case 78:
                        ResultReceiver _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        requestActivityInfo(_arg05);
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        onLeServiceUp();
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        updateQuietModeStatus(_arg1);
                        reply.writeNoException();
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        onBrEdrDown();
                        reply.writeNoException();
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        _result = setSocketOpt(data.readInt(), data.readInt(), data.readInt(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 83:
                        byte[] _arg3;
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        _result6 = data.readInt();
                        _arg2 = data.readInt();
                        int _arg3_length = data.readInt();
                        if (_arg3_length < 0) {
                            _arg3 = null;
                        } else {
                            _arg3 = new byte[_arg3_length];
                        }
                        int _result17 = getSocketOpt(_result, _result6, _arg2, _arg3);
                        reply.writeNoException();
                        parcel2.writeInt(_result17);
                        parcel2.writeByteArray(_arg3);
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg2 = getSpecificCodecStatus(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result7 = setSpecificCodecStatus(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetooth impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetooth getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean cancelBondProcess(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean cancelDiscovery() throws RemoteException;

    boolean createBond(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean createBondOutOfBand(BluetoothDevice bluetoothDevice, int i, OobData oobData) throws RemoteException;

    boolean disable() throws RemoteException;

    boolean enable() throws RemoteException;

    boolean enableNoAutoConnect() throws RemoteException;

    boolean factoryReset() throws RemoteException;

    boolean fetchRemoteUuids(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getAdapterConnectionState() throws RemoteException;

    String getAddress() throws RemoteException;

    int getBatteryLevel(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothClass getBluetoothClass() throws RemoteException;

    int getBondState(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothDevice[] getBondedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getDiscoverableTimeout() throws RemoteException;

    long getDiscoveryEndMillis() throws RemoteException;

    int getIoCapability() throws RemoteException;

    int getLeIoCapability() throws RemoteException;

    int getLeMaximumAdvertisingDataLength() throws RemoteException;

    int getMaxConnectedAudioDevices() throws RemoteException;

    int getMessageAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    byte[] getMetadata(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    String getName() throws RemoteException;

    int getPhonebookAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getProfileConnectionState(int i) throws RemoteException;

    String getRemoteAlias(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getRemoteClass(BluetoothDevice bluetoothDevice) throws RemoteException;

    String getRemoteName(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getRemoteType(BluetoothDevice bluetoothDevice) throws RemoteException;

    ParcelUuid[] getRemoteUuids(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getScanMode() throws RemoteException;

    boolean getSilenceMode(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getSimAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    IBluetoothSocketManager getSocketManager() throws RemoteException;

    int getSocketOpt(int i, int i2, int i3, byte[] bArr) throws RemoteException;

    int getSpecificCodecStatus(BluetoothDevice bluetoothDevice, String str) throws RemoteException;

    int getState() throws RemoteException;

    long getSupportedProfiles() throws RemoteException;

    String getTwsPlusPeerAddress(BluetoothDevice bluetoothDevice) throws RemoteException;

    ParcelUuid[] getUuids() throws RemoteException;

    boolean isActivityAndEnergyReportingSupported() throws RemoteException;

    boolean isBondingInitiatedLocally(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isDiscovering() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isLe2MPhySupported() throws RemoteException;

    boolean isLeCodedPhySupported() throws RemoteException;

    boolean isLeExtendedAdvertisingSupported() throws RemoteException;

    boolean isLePeriodicAdvertisingSupported() throws RemoteException;

    boolean isMultiAdvertisementSupported() throws RemoteException;

    boolean isOffloadedFilteringSupported() throws RemoteException;

    boolean isOffloadedScanBatchingSupported() throws RemoteException;

    boolean isTwsPlusDevice(BluetoothDevice bluetoothDevice) throws RemoteException;

    void onBrEdrDown() throws RemoteException;

    void onLeServiceUp() throws RemoteException;

    void registerCallback(IBluetoothCallback iBluetoothCallback) throws RemoteException;

    boolean registerMetadataListener(IBluetoothMetadataListener iBluetoothMetadataListener, BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean removeBond(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException;

    void requestActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    boolean sdpSearch(BluetoothDevice bluetoothDevice, ParcelUuid parcelUuid) throws RemoteException;

    void sendConnectionStateChange(BluetoothDevice bluetoothDevice, int i, int i2, int i3) throws RemoteException;

    boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException;

    void setBondingInitiatedLocally(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    boolean setDiscoverableTimeout(int i) throws RemoteException;

    boolean setIoCapability(int i) throws RemoteException;

    boolean setLeIoCapability(int i) throws RemoteException;

    boolean setMessageAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean setMetadata(BluetoothDevice bluetoothDevice, int i, byte[] bArr) throws RemoteException;

    boolean setName(String str) throws RemoteException;

    boolean setPairingConfirmation(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    boolean setPasskey(BluetoothDevice bluetoothDevice, boolean z, int i, byte[] bArr) throws RemoteException;

    boolean setPhonebookAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean setPin(BluetoothDevice bluetoothDevice, boolean z, int i, byte[] bArr) throws RemoteException;

    boolean setRemoteAlias(BluetoothDevice bluetoothDevice, String str) throws RemoteException;

    boolean setScanMode(int i, int i2) throws RemoteException;

    boolean setSilenceMode(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    boolean setSimAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    int setSocketOpt(int i, int i2, int i3, byte[] bArr, int i4) throws RemoteException;

    boolean setSpecificCodecStatus(BluetoothDevice bluetoothDevice, String str, int i) throws RemoteException;

    boolean startDiscovery(String str) throws RemoteException;

    void unregisterCallback(IBluetoothCallback iBluetoothCallback) throws RemoteException;

    boolean unregisterMetadataListener(BluetoothDevice bluetoothDevice) throws RemoteException;

    void updateQuietModeStatus(boolean z) throws RemoteException;
}
