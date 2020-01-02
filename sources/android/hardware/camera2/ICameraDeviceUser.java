package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;

public interface ICameraDeviceUser extends IInterface {
    public static final int CONSTRAINED_HIGH_SPEED_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int NO_IN_FLIGHT_REPEATING_FRAMES = -1;
    public static final int TEMPLATE_MANUAL = 6;
    public static final int TEMPLATE_PREVIEW = 1;
    public static final int TEMPLATE_RECORD = 3;
    public static final int TEMPLATE_STILL_CAPTURE = 2;
    public static final int TEMPLATE_VIDEO_SNAPSHOT = 4;
    public static final int TEMPLATE_ZERO_SHUTTER_LAG = 5;
    public static final int VENDOR_MODE_START = 32768;

    public static class Default implements ICameraDeviceUser {
        public void disconnect() throws RemoteException {
        }

        public SubmitInfo submitRequest(CaptureRequest request, boolean streaming) throws RemoteException {
            return null;
        }

        public SubmitInfo submitRequestList(CaptureRequest[] requestList, boolean streaming) throws RemoteException {
            return null;
        }

        public long cancelRequest(int requestId) throws RemoteException {
            return 0;
        }

        public void beginConfigure() throws RemoteException {
        }

        public void endConfigure(int operatingMode, CameraMetadataNative sessionParams) throws RemoteException {
        }

        public boolean isSessionConfigurationSupported(SessionConfiguration sessionConfiguration) throws RemoteException {
            return false;
        }

        public void deleteStream(int streamId) throws RemoteException {
        }

        public int createStream(OutputConfiguration outputConfiguration) throws RemoteException {
            return 0;
        }

        public int createInputStream(int width, int height, int format) throws RemoteException {
            return 0;
        }

        public Surface getInputSurface() throws RemoteException {
            return null;
        }

        public CameraMetadataNative createDefaultRequest(int templateId) throws RemoteException {
            return null;
        }

        public CameraMetadataNative getCameraInfo() throws RemoteException {
            return null;
        }

        public void waitUntilIdle() throws RemoteException {
        }

        public long flush() throws RemoteException {
            return 0;
        }

        public void prepare(int streamId) throws RemoteException {
        }

        public void tearDown(int streamId) throws RemoteException {
        }

        public void prepare2(int maxCount, int streamId) throws RemoteException {
        }

        public void updateOutputConfiguration(int streamId, OutputConfiguration outputConfiguration) throws RemoteException {
        }

        public void finalizeOutputConfigurations(int streamId, OutputConfiguration outputConfiguration) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICameraDeviceUser {
        private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceUser";
        static final int TRANSACTION_beginConfigure = 5;
        static final int TRANSACTION_cancelRequest = 4;
        static final int TRANSACTION_createDefaultRequest = 12;
        static final int TRANSACTION_createInputStream = 10;
        static final int TRANSACTION_createStream = 9;
        static final int TRANSACTION_deleteStream = 8;
        static final int TRANSACTION_disconnect = 1;
        static final int TRANSACTION_endConfigure = 6;
        static final int TRANSACTION_finalizeOutputConfigurations = 20;
        static final int TRANSACTION_flush = 15;
        static final int TRANSACTION_getCameraInfo = 13;
        static final int TRANSACTION_getInputSurface = 11;
        static final int TRANSACTION_isSessionConfigurationSupported = 7;
        static final int TRANSACTION_prepare = 16;
        static final int TRANSACTION_prepare2 = 18;
        static final int TRANSACTION_submitRequest = 2;
        static final int TRANSACTION_submitRequestList = 3;
        static final int TRANSACTION_tearDown = 17;
        static final int TRANSACTION_updateOutputConfiguration = 19;
        static final int TRANSACTION_waitUntilIdle = 14;

        private static class Proxy implements ICameraDeviceUser {
            public static ICameraDeviceUser sDefaultImpl;
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

            public void disconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disconnect();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SubmitInfo submitRequest(CaptureRequest request, boolean streaming) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    SubmitInfo submitInfo = 0;
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!streaming) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        submitInfo = Stub.getDefaultImpl();
                        if (submitInfo != 0) {
                            submitInfo = Stub.getDefaultImpl().submitRequest(request, streaming);
                            return submitInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        submitInfo = (SubmitInfo) SubmitInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        submitInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return submitInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SubmitInfo submitRequestList(CaptureRequest[] requestList, boolean streaming) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(requestList, 0);
                    _data.writeInt(streaming ? 1 : 0);
                    SubmitInfo submitInfo = this.mRemote;
                    if (!submitInfo.transact(3, _data, _reply, 0)) {
                        submitInfo = Stub.getDefaultImpl();
                        if (submitInfo != null) {
                            submitInfo = Stub.getDefaultImpl().submitRequestList(requestList, streaming);
                            return submitInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        submitInfo = (SubmitInfo) SubmitInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        submitInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return submitInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long cancelRequest(int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    long j = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().cancelRequest(requestId);
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

            public void beginConfigure() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().beginConfigure();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endConfigure(int operatingMode, CameraMetadataNative sessionParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operatingMode);
                    if (sessionParams != null) {
                        _data.writeInt(1);
                        sessionParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endConfigure(operatingMode, sessionParams);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSessionConfigurationSupported(SessionConfiguration sessionConfiguration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (sessionConfiguration != null) {
                        _data.writeInt(1);
                        sessionConfiguration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSessionConfigurationSupported(sessionConfiguration);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteStream(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteStream(streamId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createStream(OutputConfiguration outputConfiguration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (outputConfiguration != null) {
                        _data.writeInt(1);
                        outputConfiguration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().createStream(outputConfiguration);
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

            public int createInputStream(int width, int height, int format) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(format);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createInputStream(width, height, format);
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

            public Surface getInputSurface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Surface surface = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        surface = Stub.getDefaultImpl();
                        if (surface != 0) {
                            surface = Stub.getDefaultImpl().getInputSurface();
                            return surface;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        surface = (Surface) Surface.CREATOR.createFromParcel(_reply);
                    } else {
                        surface = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return surface;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CameraMetadataNative createDefaultRequest(int templateId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(templateId);
                    CameraMetadataNative cameraMetadataNative = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        cameraMetadataNative = Stub.getDefaultImpl();
                        if (cameraMetadataNative != 0) {
                            cameraMetadataNative = Stub.getDefaultImpl().createDefaultRequest(templateId);
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

            public CameraMetadataNative getCameraInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CameraMetadataNative cameraMetadataNative = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        cameraMetadataNative = Stub.getDefaultImpl();
                        if (cameraMetadataNative != 0) {
                            cameraMetadataNative = Stub.getDefaultImpl().getCameraInfo();
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

            public void waitUntilIdle() throws RemoteException {
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
                    Stub.getDefaultImpl().waitUntilIdle();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long flush() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().flush();
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

            public void prepare(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepare(streamId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tearDown(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().tearDown(streamId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepare2(int maxCount, int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxCount);
                    _data.writeInt(streamId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepare2(maxCount, streamId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateOutputConfiguration(int streamId, OutputConfiguration outputConfiguration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (outputConfiguration != null) {
                        _data.writeInt(1);
                        outputConfiguration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateOutputConfiguration(streamId, outputConfiguration);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finalizeOutputConfigurations(int streamId, OutputConfiguration outputConfiguration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    if (outputConfiguration != null) {
                        _data.writeInt(1);
                        outputConfiguration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finalizeOutputConfigurations(streamId, outputConfiguration);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICameraDeviceUser asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICameraDeviceUser)) {
                return new Proxy(obj);
            }
            return (ICameraDeviceUser) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "disconnect";
                case 2:
                    return "submitRequest";
                case 3:
                    return "submitRequestList";
                case 4:
                    return "cancelRequest";
                case 5:
                    return "beginConfigure";
                case 6:
                    return "endConfigure";
                case 7:
                    return "isSessionConfigurationSupported";
                case 8:
                    return "deleteStream";
                case 9:
                    return "createStream";
                case 10:
                    return "createInputStream";
                case 11:
                    return "getInputSurface";
                case 12:
                    return "createDefaultRequest";
                case 13:
                    return "getCameraInfo";
                case 14:
                    return "waitUntilIdle";
                case 15:
                    return "flush";
                case 16:
                    return "prepare";
                case 17:
                    return "tearDown";
                case 18:
                    return "prepare2";
                case 19:
                    return "updateOutputConfiguration";
                case 20:
                    return "finalizeOutputConfigurations";
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
                SubmitInfo _result;
                long _result2;
                int _arg0;
                CameraMetadataNative _arg1;
                OutputConfiguration _arg12;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        disconnect();
                        reply.writeNoException();
                        return true;
                    case 2:
                        CaptureRequest _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (CaptureRequest) CaptureRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = submitRequest(_arg02, data.readInt() != 0);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = submitRequestList((CaptureRequest[]) data.createTypedArray(CaptureRequest.CREATOR), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = cancelRequest(data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        beginConfigure();
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (CameraMetadataNative) CameraMetadataNative.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        endConfigure(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 7:
                        SessionConfiguration _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (SessionConfiguration) SessionConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        boolean _result3 = isSessionConfigurationSupported(_arg03);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        deleteStream(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        OutputConfiguration _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (OutputConfiguration) OutputConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        int _result4 = createStream(_arg04);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        int _result5 = createInputStream(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        Surface _result6 = getInputSurface();
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        CameraMetadataNative _result7 = createDefaultRequest(data.readInt());
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            _result7.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg1 = getCameraInfo();
                        reply.writeNoException();
                        if (_arg1 != null) {
                            reply.writeInt(1);
                            _arg1.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        waitUntilIdle();
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result2 = flush();
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        prepare(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        tearDown(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        prepare2(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (OutputConfiguration) OutputConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        updateOutputConfiguration(_arg0, _arg12);
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (OutputConfiguration) OutputConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        finalizeOutputConfigurations(_arg0, _arg12);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ICameraDeviceUser impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICameraDeviceUser getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void beginConfigure() throws RemoteException;

    long cancelRequest(int i) throws RemoteException;

    CameraMetadataNative createDefaultRequest(int i) throws RemoteException;

    int createInputStream(int i, int i2, int i3) throws RemoteException;

    int createStream(OutputConfiguration outputConfiguration) throws RemoteException;

    void deleteStream(int i) throws RemoteException;

    void disconnect() throws RemoteException;

    void endConfigure(int i, CameraMetadataNative cameraMetadataNative) throws RemoteException;

    void finalizeOutputConfigurations(int i, OutputConfiguration outputConfiguration) throws RemoteException;

    long flush() throws RemoteException;

    CameraMetadataNative getCameraInfo() throws RemoteException;

    Surface getInputSurface() throws RemoteException;

    boolean isSessionConfigurationSupported(SessionConfiguration sessionConfiguration) throws RemoteException;

    void prepare(int i) throws RemoteException;

    void prepare2(int i, int i2) throws RemoteException;

    SubmitInfo submitRequest(CaptureRequest captureRequest, boolean z) throws RemoteException;

    SubmitInfo submitRequestList(CaptureRequest[] captureRequestArr, boolean z) throws RemoteException;

    void tearDown(int i) throws RemoteException;

    void updateOutputConfiguration(int i, OutputConfiguration outputConfiguration) throws RemoteException;

    void waitUntilIdle() throws RemoteException;
}
