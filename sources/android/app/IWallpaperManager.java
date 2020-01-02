package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface IWallpaperManager extends IInterface {

    public static class Default implements IWallpaperManager {
        public ParcelFileDescriptor setWallpaper(String name, String callingPackage, Rect cropHint, boolean allowBackup, Bundle extras, int which, IWallpaperManagerCallback completion, int userId) throws RemoteException {
            return null;
        }

        public void setWallpaperComponentChecked(ComponentName name, String callingPackage, int userId) throws RemoteException {
        }

        public void setWallpaperComponent(ComponentName name) throws RemoteException {
        }

        public ParcelFileDescriptor getWallpaper(String callingPkg, IWallpaperManagerCallback cb, int which, Bundle outParams, int userId) throws RemoteException {
            return null;
        }

        public int getWallpaperIdForUser(int which, int userId) throws RemoteException {
            return 0;
        }

        public WallpaperInfo getWallpaperInfo(int userId) throws RemoteException {
            return null;
        }

        public void clearWallpaper(String callingPackage, int which, int userId) throws RemoteException {
        }

        public boolean hasNamedWallpaper(String name) throws RemoteException {
            return false;
        }

        public void setDimensionHints(int width, int height, String callingPackage, int displayId) throws RemoteException {
        }

        public int getWidthHint(int displayId) throws RemoteException {
            return 0;
        }

        public int getHeightHint(int displayId) throws RemoteException {
            return 0;
        }

        public void setDisplayPadding(Rect padding, String callingPackage, int displayId) throws RemoteException {
        }

        public String getName() throws RemoteException {
            return null;
        }

        public void settingsRestored() throws RemoteException {
        }

        public boolean isWallpaperSupported(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isSetWallpaperAllowed(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isWallpaperBackupEligible(int which, int userId) throws RemoteException {
            return false;
        }

        public boolean setLockWallpaperCallback(IWallpaperManagerCallback cb) throws RemoteException {
            return false;
        }

        public WallpaperColors getWallpaperColors(int which, int userId, int displayId) throws RemoteException {
            return null;
        }

        public WallpaperColors getPartialWallpaperColors(int which, int userId, Rect rectOnScreen) throws RemoteException {
            return null;
        }

        public void registerWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId, int displayId) throws RemoteException {
        }

        public void unregisterWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId, int displayId) throws RemoteException {
        }

        public void setInAmbientMode(boolean inAmbientMode, long animationDuration) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWallpaperManager {
        private static final String DESCRIPTOR = "android.app.IWallpaperManager";
        static final int TRANSACTION_clearWallpaper = 7;
        static final int TRANSACTION_getHeightHint = 11;
        static final int TRANSACTION_getName = 13;
        static final int TRANSACTION_getPartialWallpaperColors = 20;
        static final int TRANSACTION_getWallpaper = 4;
        static final int TRANSACTION_getWallpaperColors = 19;
        static final int TRANSACTION_getWallpaperIdForUser = 5;
        static final int TRANSACTION_getWallpaperInfo = 6;
        static final int TRANSACTION_getWidthHint = 10;
        static final int TRANSACTION_hasNamedWallpaper = 8;
        static final int TRANSACTION_isSetWallpaperAllowed = 16;
        static final int TRANSACTION_isWallpaperBackupEligible = 17;
        static final int TRANSACTION_isWallpaperSupported = 15;
        static final int TRANSACTION_registerWallpaperColorsCallback = 21;
        static final int TRANSACTION_setDimensionHints = 9;
        static final int TRANSACTION_setDisplayPadding = 12;
        static final int TRANSACTION_setInAmbientMode = 23;
        static final int TRANSACTION_setLockWallpaperCallback = 18;
        static final int TRANSACTION_setWallpaper = 1;
        static final int TRANSACTION_setWallpaperComponent = 3;
        static final int TRANSACTION_setWallpaperComponentChecked = 2;
        static final int TRANSACTION_settingsRestored = 14;
        static final int TRANSACTION_unregisterWallpaperColorsCallback = 22;

        private static class Proxy implements IWallpaperManager {
            public static IWallpaperManager sDefaultImpl;
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

            public ParcelFileDescriptor setWallpaper(String name, String callingPackage, Rect cropHint, boolean allowBackup, Bundle extras, int which, IWallpaperManagerCallback completion, int userId) throws RemoteException {
                Throwable th;
                Bundle bundle;
                int i;
                String str;
                Rect rect = cropHint;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(name);
                        try {
                            _data.writeString(callingPackage);
                            if (rect != null) {
                                _data.writeInt(1);
                                rect.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(allowBackup ? 1 : 0);
                        } catch (Throwable th2) {
                            th = th2;
                            bundle = extras;
                            i = which;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(which);
                            _data.writeStrongBinder(completion != null ? completion.asBinder() : null);
                            _data.writeInt(userId);
                            ParcelFileDescriptor _result;
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                if (_reply.readInt() != 0) {
                                    try {
                                        extras.readFromParcel(_reply);
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                } else {
                                    bundle = extras;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().setWallpaper(name, callingPackage, cropHint, allowBackup, extras, which, completion, userId);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            bundle = extras;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str = callingPackage;
                        bundle = extras;
                        i = which;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = name;
                    str = callingPackage;
                    bundle = extras;
                    i = which;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setWallpaperComponentChecked(ComponentName name, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWallpaperComponentChecked(name, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWallpaperComponent(ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWallpaperComponent(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getWallpaper(String callingPkg, IWallpaperManagerCallback cb, int which, Bundle outParams, int userId) throws RemoteException {
                Throwable th;
                Bundle bundle;
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                String str;
                try {
                    ParcelFileDescriptor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    str = callingPkg;
                    try {
                        _data.writeString(callingPkg);
                        _result = null;
                        _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                        try {
                            _data.writeInt(which);
                        } catch (Throwable th2) {
                            th = th2;
                            bundle = outParams;
                            i = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = which;
                        bundle = outParams;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                    } catch (Throwable th4) {
                        th = th4;
                        bundle = outParams;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                            }
                            if (_reply.readInt() != 0) {
                                try {
                                    outParams.readFromParcel(_reply);
                                } catch (Throwable th5) {
                                    th = th5;
                                }
                            } else {
                                bundle = outParams;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().getWallpaper(callingPkg, cb, which, outParams, userId);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th6) {
                        th = th6;
                        bundle = outParams;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    str = callingPkg;
                    i2 = which;
                    bundle = outParams;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getWallpaperIdForUser(int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWallpaperIdForUser(which, userId);
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

            public WallpaperInfo getWallpaperInfo(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    WallpaperInfo wallpaperInfo = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        wallpaperInfo = Stub.getDefaultImpl();
                        if (wallpaperInfo != 0) {
                            wallpaperInfo = Stub.getDefaultImpl().getWallpaperInfo(userId);
                            return wallpaperInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wallpaperInfo = (WallpaperInfo) WallpaperInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        wallpaperInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wallpaperInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearWallpaper(String callingPackage, int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearWallpaper(callingPackage, which, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasNamedWallpaper(String name) throws RemoteException {
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
                            z = Stub.getDefaultImpl().hasNamedWallpaper(name);
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

            public void setDimensionHints(int width, int height, String callingPackage, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeString(callingPackage);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDimensionHints(width, height, callingPackage, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWidthHint(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWidthHint(displayId);
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

            public int getHeightHint(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getHeightHint(displayId);
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

            public void setDisplayPadding(Rect padding, String callingPackage, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (padding != null) {
                        _data.writeInt(1);
                        padding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisplayPadding(padding, callingPackage, displayId);
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
                    String str = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
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

            public void settingsRestored() throws RemoteException {
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
                    Stub.getDefaultImpl().settingsRestored();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWallpaperSupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWallpaperSupported(callingPackage);
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

            public boolean isSetWallpaperAllowed(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSetWallpaperAllowed(callingPackage);
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

            public boolean isWallpaperBackupEligible(int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWallpaperBackupEligible(which, userId);
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

            public boolean setLockWallpaperCallback(IWallpaperManagerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setLockWallpaperCallback(cb);
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

            public WallpaperColors getWallpaperColors(int which, int userId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    _data.writeInt(displayId);
                    WallpaperColors wallpaperColors = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        wallpaperColors = Stub.getDefaultImpl();
                        if (wallpaperColors != 0) {
                            wallpaperColors = Stub.getDefaultImpl().getWallpaperColors(which, userId, displayId);
                            return wallpaperColors;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wallpaperColors = (WallpaperColors) WallpaperColors.CREATOR.createFromParcel(_reply);
                    } else {
                        wallpaperColors = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wallpaperColors;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WallpaperColors getPartialWallpaperColors(int which, int userId, Rect rectOnScreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    if (rectOnScreen != null) {
                        _data.writeInt(1);
                        rectOnScreen.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    WallpaperColors wallpaperColors = this.mRemote;
                    if (!wallpaperColors.transact(20, _data, _reply, 0)) {
                        wallpaperColors = Stub.getDefaultImpl();
                        if (wallpaperColors != null) {
                            wallpaperColors = Stub.getDefaultImpl().getPartialWallpaperColors(which, userId, rectOnScreen);
                            return wallpaperColors;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        wallpaperColors = (WallpaperColors) WallpaperColors.CREATOR.createFromParcel(_reply);
                    } else {
                        wallpaperColors = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return wallpaperColors;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerWallpaperColorsCallback(cb, userId, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterWallpaperColorsCallback(cb, userId, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInAmbientMode(boolean inAmbientMode, long animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(inAmbientMode ? 1 : 0);
                    _data.writeLong(animationDuration);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setInAmbientMode(inAmbientMode, animationDuration);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWallpaperManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWallpaperManager)) {
                return new Proxy(obj);
            }
            return (IWallpaperManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setWallpaper";
                case 2:
                    return "setWallpaperComponentChecked";
                case 3:
                    return "setWallpaperComponent";
                case 4:
                    return "getWallpaper";
                case 5:
                    return "getWallpaperIdForUser";
                case 6:
                    return "getWallpaperInfo";
                case 7:
                    return "clearWallpaper";
                case 8:
                    return "hasNamedWallpaper";
                case 9:
                    return "setDimensionHints";
                case 10:
                    return "getWidthHint";
                case 11:
                    return "getHeightHint";
                case 12:
                    return "setDisplayPadding";
                case 13:
                    return "getName";
                case 14:
                    return "settingsRestored";
                case 15:
                    return "isWallpaperSupported";
                case 16:
                    return "isSetWallpaperAllowed";
                case 17:
                    return "isWallpaperBackupEligible";
                case 18:
                    return "setLockWallpaperCallback";
                case 19:
                    return "getWallpaperColors";
                case 20:
                    return "getPartialWallpaperColors";
                case 21:
                    return "registerWallpaperColorsCallback";
                case 22:
                    return "unregisterWallpaperColorsCallback";
                case 23:
                    return "setInAmbientMode";
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
                boolean z = false;
                ParcelFileDescriptor _result;
                ComponentName _arg0;
                boolean _result2;
                int _result3;
                WallpaperColors _result4;
                switch (i) {
                    case 1:
                        Rect _arg2;
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        String _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        boolean _arg3 = data.readInt() != 0;
                        Bundle _arg4 = new Bundle();
                        Bundle _arg42 = _arg4;
                        _result = setWallpaper(_arg02, _arg1, _arg2, _arg3, _arg4, data.readInt(), android.app.IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        parcel2.writeInt(1);
                        _arg42.writeToParcel(parcel2, 1);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setWallpaperComponentChecked(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setWallpaperComponent(_arg0);
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        IWallpaperManagerCallback _arg12 = android.app.IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                        int _arg22 = data.readInt();
                        Bundle _arg32 = new Bundle();
                        Bundle _arg33 = _arg32;
                        _result = getWallpaper(_arg03, _arg12, _arg22, _arg32, data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        parcel2.writeInt(1);
                        _arg33.writeToParcel(parcel2, 1);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        int _result5 = getWallpaperIdForUser(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        WallpaperInfo _result6 = getWallpaperInfo(data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        clearWallpaper(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result2 = hasNamedWallpaper(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        setDimensionHints(data.readInt(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result3 = getWidthHint(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result3 = getHeightHint(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 12:
                        Rect _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        setDisplayPadding(_arg04, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        String _result7 = getName();
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        settingsRestored();
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result2 = isWallpaperSupported(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result2 = isSetWallpaperAllowed(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        boolean _result8 = isWallpaperBackupEligible(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result2 = setLockWallpaperCallback(android.app.IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result4 = getWallpaperColors(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 20:
                        Rect _arg23;
                        parcel.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg23 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _result4 = getPartialWallpaperColors(_arg05, _result3, _arg23);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        registerWallpaperColorsCallback(android.app.IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        unregisterWallpaperColorsCallback(android.app.IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setInAmbientMode(z, data.readLong());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWallpaperManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWallpaperManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void clearWallpaper(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    int getHeightHint(int i) throws RemoteException;

    String getName() throws RemoteException;

    WallpaperColors getPartialWallpaperColors(int i, int i2, Rect rect) throws RemoteException;

    @UnsupportedAppUsage
    ParcelFileDescriptor getWallpaper(String str, IWallpaperManagerCallback iWallpaperManagerCallback, int i, Bundle bundle, int i2) throws RemoteException;

    WallpaperColors getWallpaperColors(int i, int i2, int i3) throws RemoteException;

    int getWallpaperIdForUser(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    WallpaperInfo getWallpaperInfo(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getWidthHint(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasNamedWallpaper(String str) throws RemoteException;

    boolean isSetWallpaperAllowed(String str) throws RemoteException;

    boolean isWallpaperBackupEligible(int i, int i2) throws RemoteException;

    boolean isWallpaperSupported(String str) throws RemoteException;

    void registerWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i2) throws RemoteException;

    void setDimensionHints(int i, int i2, String str, int i3) throws RemoteException;

    void setDisplayPadding(Rect rect, String str, int i) throws RemoteException;

    void setInAmbientMode(boolean z, long j) throws RemoteException;

    boolean setLockWallpaperCallback(IWallpaperManagerCallback iWallpaperManagerCallback) throws RemoteException;

    ParcelFileDescriptor setWallpaper(String str, String str2, Rect rect, boolean z, Bundle bundle, int i, IWallpaperManagerCallback iWallpaperManagerCallback, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void setWallpaperComponent(ComponentName componentName) throws RemoteException;

    void setWallpaperComponentChecked(ComponentName componentName, String str, int i) throws RemoteException;

    void settingsRestored() throws RemoteException;

    void unregisterWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i2) throws RemoteException;
}
