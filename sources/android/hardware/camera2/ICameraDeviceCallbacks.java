package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICameraDeviceCallbacks extends IInterface {
    public static final int ERROR_CAMERA_BUFFER = 5;
    public static final int ERROR_CAMERA_DEVICE = 1;
    public static final int ERROR_CAMERA_DISABLED = 6;
    public static final int ERROR_CAMERA_DISCONNECTED = 0;
    public static final int ERROR_CAMERA_INVALID_ERROR = -1;
    public static final int ERROR_CAMERA_REQUEST = 3;
    public static final int ERROR_CAMERA_RESULT = 4;
    public static final int ERROR_CAMERA_SERVICE = 2;

    public static class Default implements ICameraDeviceCallbacks {
        public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) throws RemoteException {
        }

        public void onDeviceIdle() throws RemoteException {
        }

        public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) throws RemoteException {
        }

        public void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalCaptureResultInfos) throws RemoteException {
        }

        public void onPrepared(int streamId) throws RemoteException {
        }

        public void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) throws RemoteException {
        }

        public void onRequestQueueEmpty() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICameraDeviceCallbacks {
        private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceCallbacks";
        static final int TRANSACTION_onCaptureStarted = 3;
        static final int TRANSACTION_onDeviceError = 1;
        static final int TRANSACTION_onDeviceIdle = 2;
        static final int TRANSACTION_onPrepared = 5;
        static final int TRANSACTION_onRepeatingRequestError = 6;
        static final int TRANSACTION_onRequestQueueEmpty = 7;
        static final int TRANSACTION_onResultReceived = 4;

        private static class Proxy implements ICameraDeviceCallbacks {
            public static ICameraDeviceCallbacks sDefaultImpl;
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

            public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceError(errorCode, resultExtras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDeviceIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceIdle();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timestamp);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCaptureStarted(resultExtras, timestamp);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalCaptureResultInfos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(physicalCaptureResultInfos, 0);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onResultReceived(result, resultExtras, physicalCaptureResultInfos);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrepared(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrepared(streamId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(lastFrameNumber);
                    _data.writeInt(repeatingRequestId);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRequestQueueEmpty() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRequestQueueEmpty();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICameraDeviceCallbacks asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICameraDeviceCallbacks)) {
                return new Proxy(obj);
            }
            return (ICameraDeviceCallbacks) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onDeviceError";
                case 2:
                    return "onDeviceIdle";
                case 3:
                    return "onCaptureStarted";
                case 4:
                    return "onResultReceived";
                case 5:
                    return "onPrepared";
                case 6:
                    return "onRepeatingRequestError";
                case 7:
                    return "onRequestQueueEmpty";
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
                CaptureResultExtras _arg1;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (CaptureResultExtras) CaptureResultExtras.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        onDeviceError(_arg0, _arg1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onDeviceIdle();
                        return true;
                    case 3:
                        CaptureResultExtras _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (CaptureResultExtras) CaptureResultExtras.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        onCaptureStarted(_arg02, data.readLong());
                        return true;
                    case 4:
                        CameraMetadataNative _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (CameraMetadataNative) CameraMetadataNative.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (CaptureResultExtras) CaptureResultExtras.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        onResultReceived(_arg03, _arg1, (PhysicalCaptureResultInfo[]) data.createTypedArray(PhysicalCaptureResultInfo.CREATOR));
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onPrepared(data.readInt());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onRepeatingRequestError(data.readLong(), data.readInt());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        onRequestQueueEmpty();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ICameraDeviceCallbacks impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICameraDeviceCallbacks getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onCaptureStarted(CaptureResultExtras captureResultExtras, long j) throws RemoteException;

    void onDeviceError(int i, CaptureResultExtras captureResultExtras) throws RemoteException;

    void onDeviceIdle() throws RemoteException;

    void onPrepared(int i) throws RemoteException;

    void onRepeatingRequestError(long j, int i) throws RemoteException;

    void onRequestQueueEmpty() throws RemoteException;

    void onResultReceived(CameraMetadataNative cameraMetadataNative, CaptureResultExtras captureResultExtras, PhysicalCaptureResultInfo[] physicalCaptureResultInfoArr) throws RemoteException;
}
