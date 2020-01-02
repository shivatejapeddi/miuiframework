package android.hardware.display;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ParceledListSlice;
import android.graphics.Point;
import android.media.projection.IMediaProjection;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.Surface;

public interface IDisplayManager extends IInterface {

    public static class Default implements IDisplayManager {
        public DisplayInfo getDisplayInfo(int displayId) throws RemoteException {
            return null;
        }

        public int[] getDisplayIds() throws RemoteException {
            return null;
        }

        public boolean isUidPresentOnDisplay(int uid, int displayId) throws RemoteException {
            return false;
        }

        public void registerCallback(IDisplayManagerCallback callback) throws RemoteException {
        }

        public void startWifiDisplayScan() throws RemoteException {
        }

        public void stopWifiDisplayScan() throws RemoteException {
        }

        public void connectWifiDisplay(String address) throws RemoteException {
        }

        public void disconnectWifiDisplay() throws RemoteException {
        }

        public void renameWifiDisplay(String address, String alias) throws RemoteException {
        }

        public void forgetWifiDisplay(String address) throws RemoteException {
        }

        public void pauseWifiDisplay() throws RemoteException {
        }

        public void resumeWifiDisplay() throws RemoteException {
        }

        public WifiDisplayStatus getWifiDisplayStatus() throws RemoteException {
            return null;
        }

        public void requestColorMode(int displayId, int colorMode) throws RemoteException {
        }

        public int createVirtualDisplay(IVirtualDisplayCallback callback, IMediaProjection projectionToken, String packageName, String name, int width, int height, int densityDpi, Surface surface, int flags, String uniqueId) throws RemoteException {
            return 0;
        }

        public void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) throws RemoteException {
        }

        public void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) throws RemoteException {
        }

        public void releaseVirtualDisplay(IVirtualDisplayCallback token) throws RemoteException {
        }

        public void setVirtualDisplayState(IVirtualDisplayCallback token, boolean isOn) throws RemoteException {
        }

        public Point getStableDisplaySize() throws RemoteException {
            return null;
        }

        public ParceledListSlice getBrightnessEvents(String callingPackage) throws RemoteException {
            return null;
        }

        public ParceledListSlice getAmbientBrightnessStats() throws RemoteException {
            return null;
        }

        public void setBrightnessConfigurationForUser(BrightnessConfiguration c, int userId, String packageName) throws RemoteException {
        }

        public BrightnessConfiguration getBrightnessConfigurationForUser(int userId) throws RemoteException {
            return null;
        }

        public BrightnessConfiguration getDefaultBrightnessConfiguration() throws RemoteException {
            return null;
        }

        public void setTemporaryBrightness(int brightness) throws RemoteException {
        }

        public void setTemporaryAutoBrightnessAdjustment(float adjustment) throws RemoteException {
        }

        public Curve getMinimumBrightnessCurve() throws RemoteException {
            return null;
        }

        public int getPreferredWideGamutColorSpaceId() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDisplayManager {
        private static final String DESCRIPTOR = "android.hardware.display.IDisplayManager";
        static final int TRANSACTION_connectWifiDisplay = 7;
        static final int TRANSACTION_createVirtualDisplay = 15;
        static final int TRANSACTION_disconnectWifiDisplay = 8;
        static final int TRANSACTION_forgetWifiDisplay = 10;
        static final int TRANSACTION_getAmbientBrightnessStats = 22;
        static final int TRANSACTION_getBrightnessConfigurationForUser = 24;
        static final int TRANSACTION_getBrightnessEvents = 21;
        static final int TRANSACTION_getDefaultBrightnessConfiguration = 25;
        static final int TRANSACTION_getDisplayIds = 2;
        static final int TRANSACTION_getDisplayInfo = 1;
        static final int TRANSACTION_getMinimumBrightnessCurve = 28;
        static final int TRANSACTION_getPreferredWideGamutColorSpaceId = 29;
        static final int TRANSACTION_getStableDisplaySize = 20;
        static final int TRANSACTION_getWifiDisplayStatus = 13;
        static final int TRANSACTION_isUidPresentOnDisplay = 3;
        static final int TRANSACTION_pauseWifiDisplay = 11;
        static final int TRANSACTION_registerCallback = 4;
        static final int TRANSACTION_releaseVirtualDisplay = 18;
        static final int TRANSACTION_renameWifiDisplay = 9;
        static final int TRANSACTION_requestColorMode = 14;
        static final int TRANSACTION_resizeVirtualDisplay = 16;
        static final int TRANSACTION_resumeWifiDisplay = 12;
        static final int TRANSACTION_setBrightnessConfigurationForUser = 23;
        static final int TRANSACTION_setTemporaryAutoBrightnessAdjustment = 27;
        static final int TRANSACTION_setTemporaryBrightness = 26;
        static final int TRANSACTION_setVirtualDisplayState = 19;
        static final int TRANSACTION_setVirtualDisplaySurface = 17;
        static final int TRANSACTION_startWifiDisplayScan = 5;
        static final int TRANSACTION_stopWifiDisplayScan = 6;

        private static class Proxy implements IDisplayManager {
            public static IDisplayManager sDefaultImpl;
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

            public DisplayInfo getDisplayInfo(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    DisplayInfo displayInfo = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        displayInfo = Stub.getDefaultImpl();
                        if (displayInfo != 0) {
                            displayInfo = Stub.getDefaultImpl().getDisplayInfo(displayId);
                            return displayInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        displayInfo = (DisplayInfo) DisplayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        displayInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return displayInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getDisplayIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getDisplayIds();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUidPresentOnDisplay(int uid, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(displayId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUidPresentOnDisplay(uid, displayId);
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

            public void registerCallback(IDisplayManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void startWifiDisplayScan() throws RemoteException {
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
                    Stub.getDefaultImpl().startWifiDisplayScan();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopWifiDisplayScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopWifiDisplayScan();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connectWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connectWifiDisplay(address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnectWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disconnectWifiDisplay();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void renameWifiDisplay(String address, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(alias);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().renameWifiDisplay(address, alias);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forgetWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forgetWifiDisplay(address);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pauseWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pauseWifiDisplay();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resumeWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resumeWifiDisplay();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WifiDisplayStatus getWifiDisplayStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WifiDisplayStatus wifiDisplayStatus = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        wifiDisplayStatus = Stub.getDefaultImpl();
                        if (wifiDisplayStatus != 0) {
                            wifiDisplayStatus = Stub.getDefaultImpl().getWifiDisplayStatus();
                            return wifiDisplayStatus;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wifiDisplayStatus = (WifiDisplayStatus) WifiDisplayStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        wifiDisplayStatus = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wifiDisplayStatus;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestColorMode(int displayId, int colorMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(colorMode);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestColorMode(displayId, colorMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createVirtualDisplay(IVirtualDisplayCallback callback, IMediaProjection projectionToken, String packageName, String name, int width, int height, int densityDpi, Surface surface, int flags, String uniqueId) throws RemoteException {
                Throwable th;
                Surface surface2 = surface;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (projectionToken != null) {
                        iBinder = projectionToken.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    try {
                        _data.writeString(packageName);
                        _data.writeString(name);
                        _data.writeInt(width);
                        _data.writeInt(height);
                        _data.writeInt(densityDpi);
                        if (surface2 != null) {
                            _data.writeInt(1);
                            surface2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(flags);
                        _data.writeString(uniqueId);
                        if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int createVirtualDisplay = Stub.getDefaultImpl().createVirtualDisplay(callback, projectionToken, packageName, name, width, height, densityDpi, surface, flags, uniqueId);
                        _reply.recycle();
                        _data.recycle();
                        return createVirtualDisplay;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    String str = packageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resizeVirtualDisplay(token, width, height, densityDpi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVirtualDisplaySurface(token, surface);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void releaseVirtualDisplay(IVirtualDisplayCallback token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releaseVirtualDisplay(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVirtualDisplayState(IVirtualDisplayCallback token, boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(isOn ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVirtualDisplayState(token, isOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Point getStableDisplaySize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Point point = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        point = Stub.getDefaultImpl();
                        if (point != 0) {
                            point = Stub.getDefaultImpl().getStableDisplaySize();
                            return point;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        point = (Point) Point.CREATOR.createFromParcel(_reply);
                    } else {
                        point = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return point;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getBrightnessEvents(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    ParceledListSlice parceledListSlice = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getBrightnessEvents(callingPackage);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAmbientBrightnessStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAmbientBrightnessStats();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBrightnessConfigurationForUser(BrightnessConfiguration c, int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (c != null) {
                        _data.writeInt(1);
                        c.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBrightnessConfigurationForUser(c, userId, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BrightnessConfiguration getBrightnessConfigurationForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    BrightnessConfiguration brightnessConfiguration = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        brightnessConfiguration = Stub.getDefaultImpl();
                        if (brightnessConfiguration != 0) {
                            brightnessConfiguration = Stub.getDefaultImpl().getBrightnessConfigurationForUser(userId);
                            return brightnessConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        brightnessConfiguration = (BrightnessConfiguration) BrightnessConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        brightnessConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return brightnessConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BrightnessConfiguration getDefaultBrightnessConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BrightnessConfiguration brightnessConfiguration = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        brightnessConfiguration = Stub.getDefaultImpl();
                        if (brightnessConfiguration != 0) {
                            brightnessConfiguration = Stub.getDefaultImpl().getDefaultBrightnessConfiguration();
                            return brightnessConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        brightnessConfiguration = (BrightnessConfiguration) BrightnessConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        brightnessConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return brightnessConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTemporaryBrightness(int brightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(brightness);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTemporaryBrightness(brightness);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTemporaryAutoBrightnessAdjustment(float adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(adjustment);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTemporaryAutoBrightnessAdjustment(adjustment);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Curve getMinimumBrightnessCurve() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Curve curve = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        curve = Stub.getDefaultImpl();
                        if (curve != 0) {
                            curve = Stub.getDefaultImpl().getMinimumBrightnessCurve();
                            return curve;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        curve = (Curve) Curve.CREATOR.createFromParcel(_reply);
                    } else {
                        curve = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return curve;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredWideGamutColorSpaceId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredWideGamutColorSpaceId();
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

        public static IDisplayManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDisplayManager)) {
                return new Proxy(obj);
            }
            return (IDisplayManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDisplayInfo";
                case 2:
                    return "getDisplayIds";
                case 3:
                    return "isUidPresentOnDisplay";
                case 4:
                    return "registerCallback";
                case 5:
                    return "startWifiDisplayScan";
                case 6:
                    return "stopWifiDisplayScan";
                case 7:
                    return "connectWifiDisplay";
                case 8:
                    return "disconnectWifiDisplay";
                case 9:
                    return "renameWifiDisplay";
                case 10:
                    return "forgetWifiDisplay";
                case 11:
                    return "pauseWifiDisplay";
                case 12:
                    return "resumeWifiDisplay";
                case 13:
                    return "getWifiDisplayStatus";
                case 14:
                    return "requestColorMode";
                case 15:
                    return "createVirtualDisplay";
                case 16:
                    return "resizeVirtualDisplay";
                case 17:
                    return "setVirtualDisplaySurface";
                case 18:
                    return "releaseVirtualDisplay";
                case 19:
                    return "setVirtualDisplayState";
                case 20:
                    return "getStableDisplaySize";
                case 21:
                    return "getBrightnessEvents";
                case 22:
                    return "getAmbientBrightnessStats";
                case 23:
                    return "setBrightnessConfigurationForUser";
                case 24:
                    return "getBrightnessConfigurationForUser";
                case 25:
                    return "getDefaultBrightnessConfiguration";
                case 26:
                    return "setTemporaryBrightness";
                case 27:
                    return "setTemporaryAutoBrightnessAdjustment";
                case 28:
                    return "getMinimumBrightnessCurve";
                case 29:
                    return "getPreferredWideGamutColorSpaceId";
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
            boolean z;
            if (i != 1598968902) {
                boolean _arg1 = false;
                int _result;
                switch (i) {
                    case 1:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        DisplayInfo _result2 = getDisplayInfo(data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(i);
                            _result2.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 2:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        int[] _result3 = getDisplayIds();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result3);
                        return z;
                    case 3:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        boolean _result4 = isUidPresentOnDisplay(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 4:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        registerCallback(android.hardware.display.IDisplayManagerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z;
                    case 5:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        startWifiDisplayScan();
                        reply.writeNoException();
                        return z;
                    case 6:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        stopWifiDisplayScan();
                        reply.writeNoException();
                        return z;
                    case 7:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        connectWifiDisplay(data.readString());
                        reply.writeNoException();
                        return z;
                    case 8:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        disconnectWifiDisplay();
                        reply.writeNoException();
                        return z;
                    case 9:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        renameWifiDisplay(data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 10:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        forgetWifiDisplay(data.readString());
                        reply.writeNoException();
                        return z;
                    case 11:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        pauseWifiDisplay();
                        reply.writeNoException();
                        return z;
                    case 12:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        resumeWifiDisplay();
                        reply.writeNoException();
                        return z;
                    case 13:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        WifiDisplayStatus _result5 = getWifiDisplayStatus();
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(i);
                            _result5.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 14:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        requestColorMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 15:
                        Surface _arg7;
                        parcel.enforceInterface(descriptor);
                        IVirtualDisplayCallback _arg0 = android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        IMediaProjection _arg12 = android.media.projection.IMediaProjection.Stub.asInterface(data.readStrongBinder());
                        String _arg2 = data.readString();
                        String _arg3 = data.readString();
                        int _arg4 = data.readInt();
                        int _arg5 = data.readInt();
                        int _arg6 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg7 = (Surface) Surface.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        z = true;
                        _result = createVirtualDisplay(_arg0, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        resizeVirtualDisplay(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        Surface _arg13;
                        parcel.enforceInterface(descriptor);
                        IVirtualDisplayCallback _arg02 = android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg13 = (Surface) Surface.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        setVirtualDisplaySurface(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        releaseVirtualDisplay(android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        IVirtualDisplayCallback _arg03 = android.hardware.display.IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setVirtualDisplayState(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        Point _result6 = getStableDisplaySize();
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result7 = getBrightnessEvents(data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result8 = getAmbientBrightnessStats();
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 23:
                        BrightnessConfiguration _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (BrightnessConfiguration) BrightnessConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        setBrightnessConfigurationForUser(_arg04, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        BrightnessConfiguration _result9 = getBrightnessConfigurationForUser(data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        BrightnessConfiguration _result10 = getDefaultBrightnessConfiguration();
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        setTemporaryBrightness(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        setTemporaryAutoBrightnessAdjustment(data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        Curve _result11 = getMinimumBrightnessCurve();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _result = getPreferredWideGamutColorSpaceId();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IDisplayManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDisplayManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void connectWifiDisplay(String str) throws RemoteException;

    int createVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, String str, String str2, int i, int i2, int i3, Surface surface, int i4, String str3) throws RemoteException;

    void disconnectWifiDisplay() throws RemoteException;

    void forgetWifiDisplay(String str) throws RemoteException;

    ParceledListSlice getAmbientBrightnessStats() throws RemoteException;

    BrightnessConfiguration getBrightnessConfigurationForUser(int i) throws RemoteException;

    ParceledListSlice getBrightnessEvents(String str) throws RemoteException;

    BrightnessConfiguration getDefaultBrightnessConfiguration() throws RemoteException;

    int[] getDisplayIds() throws RemoteException;

    @UnsupportedAppUsage
    DisplayInfo getDisplayInfo(int i) throws RemoteException;

    Curve getMinimumBrightnessCurve() throws RemoteException;

    int getPreferredWideGamutColorSpaceId() throws RemoteException;

    Point getStableDisplaySize() throws RemoteException;

    WifiDisplayStatus getWifiDisplayStatus() throws RemoteException;

    boolean isUidPresentOnDisplay(int i, int i2) throws RemoteException;

    void pauseWifiDisplay() throws RemoteException;

    void registerCallback(IDisplayManagerCallback iDisplayManagerCallback) throws RemoteException;

    void releaseVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback) throws RemoteException;

    void renameWifiDisplay(String str, String str2) throws RemoteException;

    void requestColorMode(int i, int i2) throws RemoteException;

    void resizeVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, int i, int i2, int i3) throws RemoteException;

    void resumeWifiDisplay() throws RemoteException;

    void setBrightnessConfigurationForUser(BrightnessConfiguration brightnessConfiguration, int i, String str) throws RemoteException;

    void setTemporaryAutoBrightnessAdjustment(float f) throws RemoteException;

    void setTemporaryBrightness(int i) throws RemoteException;

    void setVirtualDisplayState(IVirtualDisplayCallback iVirtualDisplayCallback, boolean z) throws RemoteException;

    void setVirtualDisplaySurface(IVirtualDisplayCallback iVirtualDisplayCallback, Surface surface) throws RemoteException;

    void startWifiDisplayScan() throws RemoteException;

    void stopWifiDisplayScan() throws RemoteException;
}
