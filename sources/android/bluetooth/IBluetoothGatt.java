package android.bluetooth;

import android.app.PendingIntent;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.IAdvertisingSetCallback;
import android.bluetooth.le.IPeriodicAdvertisingCallback;
import android.bluetooth.le.IScannerCallback;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;

public interface IBluetoothGatt extends IInterface {

    public static class Default implements IBluetoothGatt {
        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
            return null;
        }

        public void registerScanner(IScannerCallback callback, WorkSource workSource) throws RemoteException {
        }

        public void unregisterScanner(int scannerId) throws RemoteException {
        }

        public void startScan(int scannerId, ScanSettings settings, List<ScanFilter> list, List scanStorages, String callingPackage) throws RemoteException {
        }

        public void startScanForIntent(PendingIntent intent, ScanSettings settings, List<ScanFilter> list, String callingPackage) throws RemoteException {
        }

        public void stopScanForIntent(PendingIntent intent, String callingPackage) throws RemoteException {
        }

        public void stopScan(int scannerId) throws RemoteException {
        }

        public void flushPendingBatchResults(int scannerId) throws RemoteException {
        }

        public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtAdvEvents, IAdvertisingSetCallback callback) throws RemoteException {
        }

        public void stopAdvertisingSet(IAdvertisingSetCallback callback) throws RemoteException {
        }

        public void getOwnAddress(int advertiserId) throws RemoteException {
        }

        public void enableAdvertisingSet(int advertiserId, boolean enable, int duration, int maxExtAdvEvents) throws RemoteException {
        }

        public void setAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
        }

        public void setScanResponseData(int advertiserId, AdvertiseData data) throws RemoteException {
        }

        public void setAdvertisingParameters(int advertiserId, AdvertisingSetParameters parameters) throws RemoteException {
        }

        public void setPeriodicAdvertisingParameters(int advertiserId, PeriodicAdvertisingParameters parameters) throws RemoteException {
        }

        public void setPeriodicAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
        }

        public void setPeriodicAdvertisingEnable(int advertiserId, boolean enable) throws RemoteException {
        }

        public void registerSync(ScanResult scanResult, int skip, int timeout, IPeriodicAdvertisingCallback callback) throws RemoteException {
        }

        public void unregisterSync(IPeriodicAdvertisingCallback callback) throws RemoteException {
        }

        public void registerClient(ParcelUuid appId, IBluetoothGattCallback callback) throws RemoteException {
        }

        public void unregisterClient(int clientIf) throws RemoteException {
        }

        public void clientConnect(int clientIf, String address, boolean isDirect, int transport, boolean opportunistic, int phy) throws RemoteException {
        }

        public void clientDisconnect(int clientIf, String address) throws RemoteException {
        }

        public void clientSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
        }

        public void clientReadPhy(int clientIf, String address) throws RemoteException {
        }

        public void refreshDevice(int clientIf, String address) throws RemoteException {
        }

        public void discoverServices(int clientIf, String address) throws RemoteException {
        }

        public void discoverServiceByUuid(int clientIf, String address, ParcelUuid uuid) throws RemoteException {
        }

        public void readCharacteristic(int clientIf, String address, int handle, int authReq) throws RemoteException {
        }

        public void readUsingCharacteristicUuid(int clientIf, String address, ParcelUuid uuid, int startHandle, int endHandle, int authReq) throws RemoteException {
        }

        public void writeCharacteristic(int clientIf, String address, int handle, int writeType, int authReq, byte[] value) throws RemoteException {
        }

        public void readDescriptor(int clientIf, String address, int handle, int authReq) throws RemoteException {
        }

        public void writeDescriptor(int clientIf, String address, int handle, int authReq, byte[] value) throws RemoteException {
        }

        public void registerForNotification(int clientIf, String address, int handle, boolean enable) throws RemoteException {
        }

        public void beginReliableWrite(int clientIf, String address) throws RemoteException {
        }

        public void endReliableWrite(int clientIf, String address, boolean execute) throws RemoteException {
        }

        public void readRemoteRssi(int clientIf, String address) throws RemoteException {
        }

        public void configureMTU(int clientIf, String address, int mtu) throws RemoteException {
        }

        public void connectionParameterUpdate(int clientIf, String address, int connectionPriority) throws RemoteException {
        }

        public void leConnectionUpdate(int clientIf, String address, int minInterval, int maxInterval, int slaveLatency, int supervisionTimeout, int minConnectionEventLen, int maxConnectionEventLen) throws RemoteException {
        }

        public void registerServer(ParcelUuid appId, IBluetoothGattServerCallback callback) throws RemoteException {
        }

        public void unregisterServer(int serverIf) throws RemoteException {
        }

        public void serverConnect(int serverIf, String address, boolean isDirect, int transport) throws RemoteException {
        }

        public void serverDisconnect(int serverIf, String address) throws RemoteException {
        }

        public void serverSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
        }

        public void serverReadPhy(int clientIf, String address) throws RemoteException {
        }

        public void addService(int serverIf, BluetoothGattService service) throws RemoteException {
        }

        public void removeService(int serverIf, int handle) throws RemoteException {
        }

        public void clearServices(int serverIf) throws RemoteException {
        }

        public void sendResponse(int serverIf, String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
        }

        public void sendNotification(int serverIf, String address, int handle, boolean confirm, byte[] value) throws RemoteException {
        }

        public void disconnectAll() throws RemoteException {
        }

        public void unregAll() throws RemoteException {
        }

        public int numHwTrackFiltersAvailable() throws RemoteException {
            return 0;
        }

        public void registerStatisticsClient(IScannerCallback callback) throws RemoteException {
        }

        public void unregisterStatisticsClient(IScannerCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothGatt {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGatt";
        static final int TRANSACTION_addService = 48;
        static final int TRANSACTION_beginReliableWrite = 36;
        static final int TRANSACTION_clearServices = 50;
        static final int TRANSACTION_clientConnect = 23;
        static final int TRANSACTION_clientDisconnect = 24;
        static final int TRANSACTION_clientReadPhy = 26;
        static final int TRANSACTION_clientSetPreferredPhy = 25;
        static final int TRANSACTION_configureMTU = 39;
        static final int TRANSACTION_connectionParameterUpdate = 40;
        static final int TRANSACTION_disconnectAll = 53;
        static final int TRANSACTION_discoverServiceByUuid = 29;
        static final int TRANSACTION_discoverServices = 28;
        static final int TRANSACTION_enableAdvertisingSet = 12;
        static final int TRANSACTION_endReliableWrite = 37;
        static final int TRANSACTION_flushPendingBatchResults = 8;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 1;
        static final int TRANSACTION_getOwnAddress = 11;
        static final int TRANSACTION_leConnectionUpdate = 41;
        static final int TRANSACTION_numHwTrackFiltersAvailable = 55;
        static final int TRANSACTION_readCharacteristic = 30;
        static final int TRANSACTION_readDescriptor = 33;
        static final int TRANSACTION_readRemoteRssi = 38;
        static final int TRANSACTION_readUsingCharacteristicUuid = 31;
        static final int TRANSACTION_refreshDevice = 27;
        static final int TRANSACTION_registerClient = 21;
        static final int TRANSACTION_registerForNotification = 35;
        static final int TRANSACTION_registerScanner = 2;
        static final int TRANSACTION_registerServer = 42;
        static final int TRANSACTION_registerStatisticsClient = 56;
        static final int TRANSACTION_registerSync = 19;
        static final int TRANSACTION_removeService = 49;
        static final int TRANSACTION_sendNotification = 52;
        static final int TRANSACTION_sendResponse = 51;
        static final int TRANSACTION_serverConnect = 44;
        static final int TRANSACTION_serverDisconnect = 45;
        static final int TRANSACTION_serverReadPhy = 47;
        static final int TRANSACTION_serverSetPreferredPhy = 46;
        static final int TRANSACTION_setAdvertisingData = 13;
        static final int TRANSACTION_setAdvertisingParameters = 15;
        static final int TRANSACTION_setPeriodicAdvertisingData = 17;
        static final int TRANSACTION_setPeriodicAdvertisingEnable = 18;
        static final int TRANSACTION_setPeriodicAdvertisingParameters = 16;
        static final int TRANSACTION_setScanResponseData = 14;
        static final int TRANSACTION_startAdvertisingSet = 9;
        static final int TRANSACTION_startScan = 4;
        static final int TRANSACTION_startScanForIntent = 5;
        static final int TRANSACTION_stopAdvertisingSet = 10;
        static final int TRANSACTION_stopScan = 7;
        static final int TRANSACTION_stopScanForIntent = 6;
        static final int TRANSACTION_unregAll = 54;
        static final int TRANSACTION_unregisterClient = 22;
        static final int TRANSACTION_unregisterScanner = 3;
        static final int TRANSACTION_unregisterServer = 43;
        static final int TRANSACTION_unregisterStatisticsClient = 57;
        static final int TRANSACTION_unregisterSync = 20;
        static final int TRANSACTION_writeCharacteristic = 32;
        static final int TRANSACTION_writeDescriptor = 34;

        private static class Proxy implements IBluetoothGatt {
            public static IBluetoothGatt sDefaultImpl;
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

            public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    List<BluetoothDevice> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDevicesMatchingConnectionStates(states);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    List<BluetoothDevice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerScanner(IScannerCallback callback, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerScanner(callback, workSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterScanner(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterScanner(scannerId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startScan(int scannerId, ScanSettings settings, List<ScanFilter> filters, List scanStorages, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filters);
                    _data.writeList(scanStorages);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startScan(scannerId, settings, filters, scanStorages, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startScanForIntent(PendingIntent intent, ScanSettings settings, List<ScanFilter> filters, String callingPackage) throws RemoteException {
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
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filters);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startScanForIntent(intent, settings, filters, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopScanForIntent(PendingIntent intent, String callingPackage) throws RemoteException {
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
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopScanForIntent(intent, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopScan(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopScan(scannerId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void flushPendingBatchResults(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().flushPendingBatchResults(scannerId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtAdvEvents, IAdvertisingSetCallback callback) throws RemoteException {
                Throwable th;
                AdvertisingSetParameters advertisingSetParameters = parameters;
                AdvertiseData advertiseData2 = advertiseData;
                AdvertiseData advertiseData3 = scanResponse;
                PeriodicAdvertisingParameters periodicAdvertisingParameters = periodicParameters;
                AdvertiseData advertiseData4 = periodicData;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (advertisingSetParameters != null) {
                        try {
                            _data.writeInt(1);
                            advertisingSetParameters.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    if (advertiseData2 != null) {
                        _data.writeInt(1);
                        advertiseData2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (advertiseData3 != null) {
                        _data.writeInt(1);
                        advertiseData3.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (periodicAdvertisingParameters != null) {
                        _data.writeInt(1);
                        periodicAdvertisingParameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (advertiseData4 != null) {
                        _data.writeInt(1);
                        advertiseData4.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(duration);
                    _data.writeInt(maxExtAdvEvents);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _reply2.readException();
                        _reply2.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply2 = _reply;
                    try {
                        Stub.getDefaultImpl().startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, duration, maxExtAdvEvents, callback);
                        _reply2.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _reply2.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void stopAdvertisingSet(IAdvertisingSetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopAdvertisingSet(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getOwnAddress(int advertiserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getOwnAddress(advertiserId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableAdvertisingSet(int advertiserId, boolean enable, int duration, int maxExtAdvEvents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(duration);
                    _data.writeInt(maxExtAdvEvents);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableAdvertisingSet(advertiserId, enable, duration, maxExtAdvEvents);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAdvertisingData(advertiserId, data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScanResponseData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setScanResponseData(advertiserId, data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAdvertisingParameters(int advertiserId, AdvertisingSetParameters parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (parameters != null) {
                        _data.writeInt(1);
                        parameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAdvertisingParameters(advertiserId, parameters);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPeriodicAdvertisingParameters(int advertiserId, PeriodicAdvertisingParameters parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (parameters != null) {
                        _data.writeInt(1);
                        parameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPeriodicAdvertisingParameters(advertiserId, parameters);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPeriodicAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPeriodicAdvertisingData(advertiserId, data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPeriodicAdvertisingEnable(int advertiserId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPeriodicAdvertisingEnable(advertiserId, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerSync(ScanResult scanResult, int skip, int timeout, IPeriodicAdvertisingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(skip);
                    _data.writeInt(timeout);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerSync(scanResult, skip, timeout, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterSync(IPeriodicAdvertisingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterSync(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerClient(ParcelUuid appId, IBluetoothGattCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerClient(appId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterClient(int clientIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterClient(clientIf);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientConnect(int clientIf, String address, boolean isDirect, int transport, boolean opportunistic, int phy) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i3;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(clientIf);
                    } catch (Throwable th2) {
                        th = th2;
                        str = address;
                        i = transport;
                        i2 = phy;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(address);
                        i3 = 1;
                        _data.writeInt(isDirect ? 1 : 0);
                    } catch (Throwable th3) {
                        th = th3;
                        i = transport;
                        i2 = phy;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(transport);
                        if (!opportunistic) {
                            i3 = 0;
                        }
                        _data.writeInt(i3);
                        try {
                            _data.writeInt(phy);
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = phy;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().clientConnect(clientIf, address, isDirect, transport, opportunistic, phy);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = clientIf;
                    str = address;
                    i = transport;
                    i2 = phy;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void clientDisconnect(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clientDisconnect(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(phyOptions);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clientSetPreferredPhy(clientIf, address, txPhy, rxPhy, phyOptions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clientReadPhy(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clientReadPhy(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshDevice(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().refreshDevice(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void discoverServices(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().discoverServices(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void discoverServiceByUuid(int clientIf, String address, ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().discoverServiceByUuid(clientIf, address, uuid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readCharacteristic(int clientIf, String address, int handle, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().readCharacteristic(clientIf, address, handle, authReq);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readUsingCharacteristicUuid(int clientIf, String address, ParcelUuid uuid, int startHandle, int endHandle, int authReq) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                String str;
                ParcelUuid parcelUuid = uuid;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(clientIf);
                        try {
                            _data.writeString(address);
                            if (parcelUuid != null) {
                                _data.writeInt(1);
                                parcelUuid.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            i = startHandle;
                            i2 = endHandle;
                            i3 = authReq;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = address;
                        i = startHandle;
                        i2 = endHandle;
                        i3 = authReq;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(startHandle);
                        try {
                            _data.writeInt(endHandle);
                        } catch (Throwable th4) {
                            th = th4;
                            i3 = authReq;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(authReq);
                            if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().readUsingCharacteristicUuid(clientIf, address, uuid, startHandle, endHandle, authReq);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = endHandle;
                        i3 = authReq;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = clientIf;
                    str = address;
                    i = startHandle;
                    i2 = endHandle;
                    i3 = authReq;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void writeCharacteristic(int clientIf, String address, int handle, int writeType, int authReq, byte[] value) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                byte[] bArr;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(clientIf);
                    } catch (Throwable th2) {
                        th = th2;
                        str = address;
                        i = handle;
                        i2 = writeType;
                        i3 = authReq;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(address);
                        try {
                            _data.writeInt(handle);
                            try {
                                _data.writeInt(writeType);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = authReq;
                                bArr = value;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = writeType;
                            i3 = authReq;
                            bArr = value;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = handle;
                        i2 = writeType;
                        i3 = authReq;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(authReq);
                        try {
                            _data.writeByteArray(value);
                            if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().writeCharacteristic(clientIf, address, handle, writeType, authReq, value);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i4 = clientIf;
                    str = address;
                    i = handle;
                    i2 = writeType;
                    i3 = authReq;
                    bArr = value;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void readDescriptor(int clientIf, String address, int handle, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().readDescriptor(clientIf, address, handle, authReq);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void writeDescriptor(int clientIf, String address, int handle, int authReq, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().writeDescriptor(clientIf, address, handle, authReq, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerForNotification(int clientIf, String address, int handle, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerForNotification(clientIf, address, handle, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void beginReliableWrite(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().beginReliableWrite(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endReliableWrite(int clientIf, String address, boolean execute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(execute ? 1 : 0);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endReliableWrite(clientIf, address, execute);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void readRemoteRssi(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().readRemoteRssi(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void configureMTU(int clientIf, String address, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().configureMTU(clientIf, address, mtu);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectionParameterUpdate(int clientIf, String address, int connectionPriority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(connectionPriority);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connectionParameterUpdate(clientIf, address, connectionPriority);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void leConnectionUpdate(int clientIf, String address, int minInterval, int maxInterval, int slaveLatency, int supervisionTimeout, int minConnectionEventLen, int maxConnectionEventLen) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(clientIf);
                    } catch (Throwable th2) {
                        th = th2;
                        str = address;
                        i = minInterval;
                        i2 = maxInterval;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(address);
                    } catch (Throwable th3) {
                        th = th3;
                        i = minInterval;
                        i2 = maxInterval;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(minInterval);
                        try {
                            _data.writeInt(maxInterval);
                            _data.writeInt(slaveLatency);
                            _data.writeInt(supervisionTimeout);
                            _data.writeInt(minConnectionEventLen);
                            _data.writeInt(maxConnectionEventLen);
                            if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().leConnectionUpdate(clientIf, address, minInterval, maxInterval, slaveLatency, supervisionTimeout, minConnectionEventLen, maxConnectionEventLen);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = maxInterval;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = clientIf;
                    str = address;
                    i = minInterval;
                    i2 = maxInterval;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void registerServer(ParcelUuid appId, IBluetoothGattServerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerServer(appId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterServer(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterServer(serverIf);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverConnect(int serverIf, String address, boolean isDirect, int transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(isDirect ? 1 : 0);
                    _data.writeInt(transport);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serverConnect(serverIf, address, isDirect, transport);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverDisconnect(int serverIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serverDisconnect(serverIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(phyOptions);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serverSetPreferredPhy(clientIf, address, txPhy, rxPhy, phyOptions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serverReadPhy(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().serverReadPhy(clientIf, address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addService(int serverIf, BluetoothGattService service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addService(serverIf, service);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeService(int serverIf, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeInt(handle);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeService(serverIf, handle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearServices(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearServices(serverIf);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendResponse(int serverIf, String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                byte[] bArr;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(serverIf);
                    } catch (Throwable th2) {
                        th = th2;
                        str = address;
                        i = requestId;
                        i2 = status;
                        i3 = offset;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(address);
                        try {
                            _data.writeInt(requestId);
                            try {
                                _data.writeInt(status);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = offset;
                                bArr = value;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = status;
                            i3 = offset;
                            bArr = value;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = requestId;
                        i2 = status;
                        i3 = offset;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(offset);
                        try {
                            _data.writeByteArray(value);
                            if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendResponse(serverIf, address, requestId, status, offset, value);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        bArr = value;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i4 = serverIf;
                    str = address;
                    i = requestId;
                    i2 = status;
                    i3 = offset;
                    bArr = value;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendNotification(int serverIf, String address, int handle, boolean confirm, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(confirm ? 1 : 0);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendNotification(serverIf, address, handle, confirm, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disconnectAll();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregAll();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int numHwTrackFiltersAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 55;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().numHwTrackFiltersAvailable();
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

            public void registerStatisticsClient(IScannerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerStatisticsClient(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStatisticsClient(IScannerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterStatisticsClient(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothGatt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothGatt)) {
                return new Proxy(obj);
            }
            return (IBluetoothGatt) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDevicesMatchingConnectionStates";
                case 2:
                    return "registerScanner";
                case 3:
                    return "unregisterScanner";
                case 4:
                    return "startScan";
                case 5:
                    return "startScanForIntent";
                case 6:
                    return "stopScanForIntent";
                case 7:
                    return "stopScan";
                case 8:
                    return "flushPendingBatchResults";
                case 9:
                    return "startAdvertisingSet";
                case 10:
                    return "stopAdvertisingSet";
                case 11:
                    return "getOwnAddress";
                case 12:
                    return "enableAdvertisingSet";
                case 13:
                    return "setAdvertisingData";
                case 14:
                    return "setScanResponseData";
                case 15:
                    return "setAdvertisingParameters";
                case 16:
                    return "setPeriodicAdvertisingParameters";
                case 17:
                    return "setPeriodicAdvertisingData";
                case 18:
                    return "setPeriodicAdvertisingEnable";
                case 19:
                    return "registerSync";
                case 20:
                    return "unregisterSync";
                case 21:
                    return "registerClient";
                case 22:
                    return "unregisterClient";
                case 23:
                    return "clientConnect";
                case 24:
                    return "clientDisconnect";
                case 25:
                    return "clientSetPreferredPhy";
                case 26:
                    return "clientReadPhy";
                case 27:
                    return "refreshDevice";
                case 28:
                    return "discoverServices";
                case 29:
                    return "discoverServiceByUuid";
                case 30:
                    return "readCharacteristic";
                case 31:
                    return "readUsingCharacteristicUuid";
                case 32:
                    return "writeCharacteristic";
                case 33:
                    return "readDescriptor";
                case 34:
                    return "writeDescriptor";
                case 35:
                    return "registerForNotification";
                case 36:
                    return "beginReliableWrite";
                case 37:
                    return "endReliableWrite";
                case 38:
                    return "readRemoteRssi";
                case 39:
                    return "configureMTU";
                case 40:
                    return "connectionParameterUpdate";
                case 41:
                    return "leConnectionUpdate";
                case 42:
                    return "registerServer";
                case 43:
                    return "unregisterServer";
                case 44:
                    return "serverConnect";
                case 45:
                    return "serverDisconnect";
                case 46:
                    return "serverSetPreferredPhy";
                case 47:
                    return "serverReadPhy";
                case 48:
                    return "addService";
                case 49:
                    return "removeService";
                case 50:
                    return "clearServices";
                case 51:
                    return "sendResponse";
                case 52:
                    return "sendNotification";
                case 53:
                    return "disconnectAll";
                case 54:
                    return "unregAll";
                case 55:
                    return "numHwTrackFiltersAvailable";
                case 56:
                    return "registerStatisticsClient";
                case 57:
                    return "unregisterStatisticsClient";
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
                PendingIntent _arg0;
                int _arg02;
                int _arg03;
                AdvertiseData _arg12;
                ParcelUuid _arg04;
                String _arg13;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        List<BluetoothDevice> _result = getDevicesMatchingConnectionStates(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result);
                        return true;
                    case 2:
                        WorkSource _arg14;
                        parcel.enforceInterface(descriptor);
                        IScannerCallback _arg05 = android.bluetooth.le.IScannerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        registerScanner(_arg05, _arg14);
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        unregisterScanner(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        ScanSettings _arg15;
                        parcel.enforceInterface(descriptor);
                        int _arg06 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg15 = (ScanSettings) ScanSettings.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        startScan(_arg06, _arg15, parcel.createTypedArrayList(ScanFilter.CREATOR), parcel.readArrayList(getClass().getClassLoader()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 5:
                        ScanSettings _arg16;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (ScanSettings) ScanSettings.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        startScanForIntent(_arg0, _arg16, parcel.createTypedArrayList(ScanFilter.CREATOR), data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        stopScanForIntent(_arg0, data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        stopScan(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        flushPendingBatchResults(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        AdvertisingSetParameters _arg07;
                        AdvertiseData _arg17;
                        AdvertiseData _arg2;
                        PeriodicAdvertisingParameters _arg3;
                        AdvertiseData _arg4;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (AdvertisingSetParameters) AdvertisingSetParameters.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg17 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (PeriodicAdvertisingParameters) PeriodicAdvertisingParameters.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        startAdvertisingSet(_arg07, _arg17, _arg2, _arg3, _arg4, data.readInt(), data.readInt(), android.bluetooth.le.IAdvertisingSetCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        stopAdvertisingSet(android.bluetooth.le.IAdvertisingSetCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        getOwnAddress(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableAdvertisingSet(_arg02, _arg1, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setAdvertisingData(_arg03, _arg12);
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setScanResponseData(_arg03, _arg12);
                        reply.writeNoException();
                        return true;
                    case 15:
                        AdvertisingSetParameters _arg18;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg18 = (AdvertisingSetParameters) AdvertisingSetParameters.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        setAdvertisingParameters(_arg03, _arg18);
                        reply.writeNoException();
                        return true;
                    case 16:
                        PeriodicAdvertisingParameters _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg19 = (PeriodicAdvertisingParameters) PeriodicAdvertisingParameters.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        setPeriodicAdvertisingParameters(_arg03, _arg19);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (AdvertiseData) AdvertiseData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setPeriodicAdvertisingData(_arg03, _arg12);
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setPeriodicAdvertisingEnable(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 19:
                        ScanResult _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (ScanResult) ScanResult.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        registerSync(_arg08, data.readInt(), data.readInt(), android.bluetooth.le.IPeriodicAdvertisingCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        unregisterSync(android.bluetooth.le.IPeriodicAdvertisingCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        registerClient(_arg04, android.bluetooth.IBluetoothGattCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        unregisterClient(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        clientConnect(data.readInt(), data.readString(), data.readInt() != 0, data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        clientDisconnect(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        clientSetPreferredPhy(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        clientReadPhy(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        refreshDevice(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        discoverServices(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 29:
                        ParcelUuid _arg22;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        String _arg110 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        discoverServiceByUuid(_arg03, _arg110, _arg22);
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        readCharacteristic(data.readInt(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 31:
                        ParcelUuid _arg23;
                        parcel.enforceInterface(descriptor);
                        int _arg09 = data.readInt();
                        String _arg111 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        readUsingCharacteristicUuid(_arg09, _arg111, _arg23, data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        writeCharacteristic(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        readDescriptor(data.readInt(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        writeDescriptor(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg13 = data.readString();
                        int _arg24 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        registerForNotification(_arg02, _arg13, _arg24, _arg1);
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        beginReliableWrite(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        endReliableWrite(_arg02, _arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        readRemoteRssi(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        configureMTU(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        connectionParameterUpdate(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        leConnectionUpdate(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        registerServer(_arg04, android.bluetooth.IBluetoothGattServerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        unregisterServer(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        serverConnect(_arg02, _arg13, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        serverDisconnect(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        serverSetPreferredPhy(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        serverReadPhy(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 48:
                        BluetoothGattService _arg112;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg112 = (BluetoothGattService) BluetoothGattService.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        addService(_arg03, _arg112);
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        removeService(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        clearServices(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        sendResponse(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        sendNotification(data.readInt(), data.readString(), data.readInt(), data.readInt() != 0, data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        disconnectAll();
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        unregAll();
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _arg03 = numHwTrackFiltersAvailable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg03);
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        registerStatisticsClient(android.bluetooth.le.IScannerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        unregisterStatisticsClient(android.bluetooth.le.IScannerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothGatt impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothGatt getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addService(int i, BluetoothGattService bluetoothGattService) throws RemoteException;

    void beginReliableWrite(int i, String str) throws RemoteException;

    void clearServices(int i) throws RemoteException;

    void clientConnect(int i, String str, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    void clientDisconnect(int i, String str) throws RemoteException;

    void clientReadPhy(int i, String str) throws RemoteException;

    void clientSetPreferredPhy(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void configureMTU(int i, String str, int i2) throws RemoteException;

    void connectionParameterUpdate(int i, String str, int i2) throws RemoteException;

    void disconnectAll() throws RemoteException;

    void discoverServiceByUuid(int i, String str, ParcelUuid parcelUuid) throws RemoteException;

    void discoverServices(int i, String str) throws RemoteException;

    void enableAdvertisingSet(int i, boolean z, int i2, int i3) throws RemoteException;

    void endReliableWrite(int i, String str, boolean z) throws RemoteException;

    void flushPendingBatchResults(int i) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    void getOwnAddress(int i) throws RemoteException;

    void leConnectionUpdate(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7) throws RemoteException;

    int numHwTrackFiltersAvailable() throws RemoteException;

    void readCharacteristic(int i, String str, int i2, int i3) throws RemoteException;

    void readDescriptor(int i, String str, int i2, int i3) throws RemoteException;

    void readRemoteRssi(int i, String str) throws RemoteException;

    void readUsingCharacteristicUuid(int i, String str, ParcelUuid parcelUuid, int i2, int i3, int i4) throws RemoteException;

    void refreshDevice(int i, String str) throws RemoteException;

    void registerClient(ParcelUuid parcelUuid, IBluetoothGattCallback iBluetoothGattCallback) throws RemoteException;

    void registerForNotification(int i, String str, int i2, boolean z) throws RemoteException;

    void registerScanner(IScannerCallback iScannerCallback, WorkSource workSource) throws RemoteException;

    void registerServer(ParcelUuid parcelUuid, IBluetoothGattServerCallback iBluetoothGattServerCallback) throws RemoteException;

    void registerStatisticsClient(IScannerCallback iScannerCallback) throws RemoteException;

    void registerSync(ScanResult scanResult, int i, int i2, IPeriodicAdvertisingCallback iPeriodicAdvertisingCallback) throws RemoteException;

    void removeService(int i, int i2) throws RemoteException;

    void sendNotification(int i, String str, int i2, boolean z, byte[] bArr) throws RemoteException;

    void sendResponse(int i, String str, int i2, int i3, int i4, byte[] bArr) throws RemoteException;

    void serverConnect(int i, String str, boolean z, int i2) throws RemoteException;

    void serverDisconnect(int i, String str) throws RemoteException;

    void serverReadPhy(int i, String str) throws RemoteException;

    void serverSetPreferredPhy(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void setAdvertisingData(int i, AdvertiseData advertiseData) throws RemoteException;

    void setAdvertisingParameters(int i, AdvertisingSetParameters advertisingSetParameters) throws RemoteException;

    void setPeriodicAdvertisingData(int i, AdvertiseData advertiseData) throws RemoteException;

    void setPeriodicAdvertisingEnable(int i, boolean z) throws RemoteException;

    void setPeriodicAdvertisingParameters(int i, PeriodicAdvertisingParameters periodicAdvertisingParameters) throws RemoteException;

    void setScanResponseData(int i, AdvertiseData advertiseData) throws RemoteException;

    void startAdvertisingSet(AdvertisingSetParameters advertisingSetParameters, AdvertiseData advertiseData, AdvertiseData advertiseData2, PeriodicAdvertisingParameters periodicAdvertisingParameters, AdvertiseData advertiseData3, int i, int i2, IAdvertisingSetCallback iAdvertisingSetCallback) throws RemoteException;

    void startScan(int i, ScanSettings scanSettings, List<ScanFilter> list, List list2, String str) throws RemoteException;

    void startScanForIntent(PendingIntent pendingIntent, ScanSettings scanSettings, List<ScanFilter> list, String str) throws RemoteException;

    void stopAdvertisingSet(IAdvertisingSetCallback iAdvertisingSetCallback) throws RemoteException;

    void stopScan(int i) throws RemoteException;

    void stopScanForIntent(PendingIntent pendingIntent, String str) throws RemoteException;

    void unregAll() throws RemoteException;

    void unregisterClient(int i) throws RemoteException;

    void unregisterScanner(int i) throws RemoteException;

    void unregisterServer(int i) throws RemoteException;

    void unregisterStatisticsClient(IScannerCallback iScannerCallback) throws RemoteException;

    void unregisterSync(IPeriodicAdvertisingCallback iPeriodicAdvertisingCallback) throws RemoteException;

    void writeCharacteristic(int i, String str, int i2, int i3, int i4, byte[] bArr) throws RemoteException;

    void writeDescriptor(int i, String str, int i2, int i3, byte[] bArr) throws RemoteException;
}
