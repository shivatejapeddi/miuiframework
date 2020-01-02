package android.hardware;

import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.VendorTagDescriptor;
import android.hardware.camera2.params.VendorTagDescriptorCache;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICameraService extends IInterface {
    public static final int API_VERSION_1 = 1;
    public static final int API_VERSION_2 = 2;
    public static final int CAMERA_HAL_API_VERSION_UNSPECIFIED = -1;
    public static final int CAMERA_TYPE_ALL = 1;
    public static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
    public static final int DEVICE_STATE_BACK_COVERED = 1;
    public static final int DEVICE_STATE_FOLDED = 4;
    public static final int DEVICE_STATE_FRONT_COVERED = 2;
    public static final int DEVICE_STATE_LAST_FRAMEWORK_BIT = Integer.MIN_VALUE;
    public static final int DEVICE_STATE_NORMAL = 0;
    public static final int ERROR_ALREADY_EXISTS = 2;
    public static final int ERROR_CAMERA_IN_USE = 7;
    public static final int ERROR_DEPRECATED_HAL = 9;
    public static final int ERROR_DISABLED = 6;
    public static final int ERROR_DISCONNECTED = 4;
    public static final int ERROR_ILLEGAL_ARGUMENT = 3;
    public static final int ERROR_INVALID_OPERATION = 10;
    public static final int ERROR_MAX_CAMERAS_IN_USE = 8;
    public static final int ERROR_PERMISSION_DENIED = 1;
    public static final int ERROR_TIMED_OUT = 5;
    public static final int EVENT_NONE = 0;
    public static final int EVENT_USER_SWITCHED = 1;
    public static final int USE_CALLING_PID = -1;
    public static final int USE_CALLING_UID = -1;

    public static class Default implements ICameraService {
        public int getNumberOfCameras(int type) throws RemoteException {
            return 0;
        }

        public CameraInfo getCameraInfo(int cameraId) throws RemoteException {
            return null;
        }

        public ICamera connect(ICameraClient client, int cameraId, String opPackageName, int clientUid, int clientPid) throws RemoteException {
            return null;
        }

        public ICameraDeviceUser connectDevice(ICameraDeviceCallbacks callbacks, String cameraId, String opPackageName, int clientUid) throws RemoteException {
            return null;
        }

        public ICamera connectLegacy(ICameraClient client, int cameraId, int halVersion, String opPackageName, int clientUid) throws RemoteException {
            return null;
        }

        public CameraStatus[] addListener(ICameraServiceListener listener) throws RemoteException {
            return null;
        }

        public void removeListener(ICameraServiceListener listener) throws RemoteException {
        }

        public CameraMetadataNative getCameraCharacteristics(String cameraId) throws RemoteException {
            return null;
        }

        public VendorTagDescriptor getCameraVendorTagDescriptor() throws RemoteException {
            return null;
        }

        public VendorTagDescriptorCache getCameraVendorTagCache() throws RemoteException {
            return null;
        }

        public String getLegacyParameters(int cameraId) throws RemoteException {
            return null;
        }

        public boolean supportsCameraApi(String cameraId, int apiVersion) throws RemoteException {
            return false;
        }

        public boolean isHiddenPhysicalCamera(String cameraId) throws RemoteException {
            return false;
        }

        public void setTorchMode(String cameraId, boolean enabled, IBinder clientBinder) throws RemoteException {
        }

        public void notifySystemEvent(int eventId, int[] args) throws RemoteException {
        }

        public void notifyDeviceStateChange(long newState) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICameraService {
        private static final String DESCRIPTOR = "android.hardware.ICameraService";
        static final int TRANSACTION_addListener = 6;
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_connectDevice = 4;
        static final int TRANSACTION_connectLegacy = 5;
        static final int TRANSACTION_getCameraCharacteristics = 8;
        static final int TRANSACTION_getCameraInfo = 2;
        static final int TRANSACTION_getCameraVendorTagCache = 10;
        static final int TRANSACTION_getCameraVendorTagDescriptor = 9;
        static final int TRANSACTION_getLegacyParameters = 11;
        static final int TRANSACTION_getNumberOfCameras = 1;
        static final int TRANSACTION_isHiddenPhysicalCamera = 13;
        static final int TRANSACTION_notifyDeviceStateChange = 16;
        static final int TRANSACTION_notifySystemEvent = 15;
        static final int TRANSACTION_removeListener = 7;
        static final int TRANSACTION_setTorchMode = 14;
        static final int TRANSACTION_supportsCameraApi = 12;

        private static class Proxy implements ICameraService {
            public static ICameraService sDefaultImpl;
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

            public int getNumberOfCameras(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNumberOfCameras(type);
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

            public CameraInfo getCameraInfo(int cameraId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    CameraInfo cameraInfo = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        cameraInfo = Stub.getDefaultImpl();
                        if (cameraInfo != 0) {
                            cameraInfo = Stub.getDefaultImpl().getCameraInfo(cameraId);
                            return cameraInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        cameraInfo = (CameraInfo) CameraInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        cameraInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return cameraInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ICamera connect(ICameraClient client, int cameraId, String opPackageName, int clientUid, int clientPid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    _data.writeInt(clientPid);
                    ICamera iCamera = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iCamera = Stub.getDefaultImpl();
                        if (iCamera != 0) {
                            iCamera = Stub.getDefaultImpl().connect(client, cameraId, opPackageName, clientUid, clientPid);
                            return iCamera;
                        }
                    }
                    _reply.readException();
                    iCamera = android.hardware.ICamera.Stub.asInterface(_reply.readStrongBinder());
                    ICamera _result = iCamera;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ICameraDeviceUser connectDevice(ICameraDeviceCallbacks callbacks, String cameraId, String opPackageName, int clientUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    _data.writeString(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    ICameraDeviceUser iCameraDeviceUser = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        iCameraDeviceUser = Stub.getDefaultImpl();
                        if (iCameraDeviceUser != 0) {
                            iCameraDeviceUser = Stub.getDefaultImpl().connectDevice(callbacks, cameraId, opPackageName, clientUid);
                            return iCameraDeviceUser;
                        }
                    }
                    _reply.readException();
                    iCameraDeviceUser = android.hardware.camera2.ICameraDeviceUser.Stub.asInterface(_reply.readStrongBinder());
                    ICameraDeviceUser _result = iCameraDeviceUser;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ICamera connectLegacy(ICameraClient client, int cameraId, int halVersion, String opPackageName, int clientUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeInt(halVersion);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    ICamera iCamera = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        iCamera = Stub.getDefaultImpl();
                        if (iCamera != 0) {
                            iCamera = Stub.getDefaultImpl().connectLegacy(client, cameraId, halVersion, opPackageName, clientUid);
                            return iCamera;
                        }
                    }
                    _reply.readException();
                    iCamera = android.hardware.ICamera.Stub.asInterface(_reply.readStrongBinder());
                    ICamera _result = iCamera;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CameraStatus[] addListener(ICameraServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    CameraStatus[] cameraStatusArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        cameraStatusArr = Stub.getDefaultImpl();
                        if (cameraStatusArr != 0) {
                            cameraStatusArr = Stub.getDefaultImpl().addListener(listener);
                            return cameraStatusArr;
                        }
                    }
                    _reply.readException();
                    CameraStatus[] _result = (CameraStatus[]) _reply.createTypedArray(CameraStatus.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeListener(ICameraServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CameraMetadataNative getCameraCharacteristics(String cameraId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    CameraMetadataNative cameraMetadataNative = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        cameraMetadataNative = Stub.getDefaultImpl();
                        if (cameraMetadataNative != 0) {
                            cameraMetadataNative = Stub.getDefaultImpl().getCameraCharacteristics(cameraId);
                            return cameraMetadataNative;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        cameraMetadataNative = (CameraMetadataNative) CameraMetadataNative.CREATOR.createFromParcel(_reply);
                    } else {
                        cameraMetadataNative = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return cameraMetadataNative;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VendorTagDescriptor getCameraVendorTagDescriptor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    VendorTagDescriptor vendorTagDescriptor = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        vendorTagDescriptor = Stub.getDefaultImpl();
                        if (vendorTagDescriptor != 0) {
                            vendorTagDescriptor = Stub.getDefaultImpl().getCameraVendorTagDescriptor();
                            return vendorTagDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        vendorTagDescriptor = (VendorTagDescriptor) VendorTagDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        vendorTagDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return vendorTagDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VendorTagDescriptorCache getCameraVendorTagCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    VendorTagDescriptorCache vendorTagDescriptorCache = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        vendorTagDescriptorCache = Stub.getDefaultImpl();
                        if (vendorTagDescriptorCache != 0) {
                            vendorTagDescriptorCache = Stub.getDefaultImpl().getCameraVendorTagCache();
                            return vendorTagDescriptorCache;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        vendorTagDescriptorCache = (VendorTagDescriptorCache) VendorTagDescriptorCache.CREATOR.createFromParcel(_reply);
                    } else {
                        vendorTagDescriptorCache = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return vendorTagDescriptorCache;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLegacyParameters(int cameraId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    String str = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLegacyParameters(cameraId);
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

            public boolean supportsCameraApi(String cameraId, int apiVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    _data.writeInt(apiVersion);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supportsCameraApi(cameraId, apiVersion);
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

            public boolean isHiddenPhysicalCamera(String cameraId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isHiddenPhysicalCamera(cameraId);
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

            public void setTorchMode(String cameraId, boolean enabled, IBinder clientBinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeStrongBinder(clientBinder);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTorchMode(cameraId, enabled, clientBinder);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySystemEvent(int eventId, int[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeIntArray(args);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifySystemEvent(eventId, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyDeviceStateChange(long newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(newState);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyDeviceStateChange(newState);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICameraService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICameraService)) {
                return new Proxy(obj);
            }
            return (ICameraService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getNumberOfCameras";
                case 2:
                    return "getCameraInfo";
                case 3:
                    return "connect";
                case 4:
                    return "connectDevice";
                case 5:
                    return "connectLegacy";
                case 6:
                    return "addListener";
                case 7:
                    return "removeListener";
                case 8:
                    return "getCameraCharacteristics";
                case 9:
                    return "getCameraVendorTagDescriptor";
                case 10:
                    return "getCameraVendorTagCache";
                case 11:
                    return "getLegacyParameters";
                case 12:
                    return "supportsCameraApi";
                case 13:
                    return "isHiddenPhysicalCamera";
                case 14:
                    return "setTorchMode";
                case 15:
                    return "notifySystemEvent";
                case 16:
                    return "notifyDeviceStateChange";
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
                IBinder iBinder = null;
                boolean _arg1 = false;
                ICamera _result;
                String _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        int _result3 = getNumberOfCameras(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        CameraInfo _result4 = getCameraInfo(data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = connect(android.hardware.ICameraClient.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        ICameraDeviceUser _result5 = connectDevice(android.hardware.camera2.ICameraDeviceCallbacks.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = connectLegacy(android.hardware.ICameraClient.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        CameraStatus[] _result6 = addListener(android.hardware.ICameraServiceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result6, 1);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        removeListener(android.hardware.ICameraServiceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        CameraMetadataNative _result7 = getCameraCharacteristics(data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        VendorTagDescriptor _result8 = getCameraVendorTagDescriptor();
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        VendorTagDescriptorCache _result9 = getCameraVendorTagCache();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = getLegacyParameters(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        boolean _result10 = supportsCameraApi(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        boolean _result11 = isHiddenPhysicalCamera(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setTorchMode(_result2, _arg1, data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        notifySystemEvent(data.readInt(), data.createIntArray());
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        notifyDeviceStateChange(data.readLong());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ICameraService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICameraService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    CameraStatus[] addListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    ICamera connect(ICameraClient iCameraClient, int i, String str, int i2, int i3) throws RemoteException;

    ICameraDeviceUser connectDevice(ICameraDeviceCallbacks iCameraDeviceCallbacks, String str, String str2, int i) throws RemoteException;

    ICamera connectLegacy(ICameraClient iCameraClient, int i, int i2, String str, int i3) throws RemoteException;

    CameraMetadataNative getCameraCharacteristics(String str) throws RemoteException;

    CameraInfo getCameraInfo(int i) throws RemoteException;

    VendorTagDescriptorCache getCameraVendorTagCache() throws RemoteException;

    VendorTagDescriptor getCameraVendorTagDescriptor() throws RemoteException;

    String getLegacyParameters(int i) throws RemoteException;

    int getNumberOfCameras(int i) throws RemoteException;

    boolean isHiddenPhysicalCamera(String str) throws RemoteException;

    void notifyDeviceStateChange(long j) throws RemoteException;

    void notifySystemEvent(int i, int[] iArr) throws RemoteException;

    void removeListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    void setTorchMode(String str, boolean z, IBinder iBinder) throws RemoteException;

    boolean supportsCameraApi(String str, int i) throws RemoteException;
}
