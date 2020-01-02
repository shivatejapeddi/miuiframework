package com.miui.enterprise;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IDeviceManager extends IInterface {

    public static class Default implements IDeviceManager {
        public boolean isDeviceRoot() throws RemoteException {
            return false;
        }

        public void deviceShutDown() throws RemoteException {
        }

        public void deviceReboot() throws RemoteException {
        }

        public void formatSdCard() throws RemoteException {
        }

        public void setUrlWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public void setUrlBlackList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getUrlWhiteList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getUrlBlackList(int userId) throws RemoteException {
            return null;
        }

        public void recoveryFactory(boolean formatSdcard) throws RemoteException {
        }

        public void setWifiConnRestriction(int mode, int userId) throws RemoteException {
        }

        public int getWifiConnRestriction(int userId) throws RemoteException {
            return 0;
        }

        public void setWifiApSsidWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public void setWifiApBssidWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public void setWifiApSsidBlackList(List<String> list, int userId) throws RemoteException {
        }

        public void setWifiApBssidBlackList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getWifiApSsidWhiteList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getWifiApBssidWhiteList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getWifiApSsidBlackList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getWifiApBssidBlackList(int userId) throws RemoteException {
            return null;
        }

        public void setRingerMode(int ringerMode) throws RemoteException {
        }

        public void setBrowserRestriction(int mode, int userId) throws RemoteException {
        }

        public Bitmap captureScreen() throws RemoteException {
            return null;
        }

        public void setIpRestriction(int mode, int userId) throws RemoteException {
        }

        public void setIpWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public void setIpBlackList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getIpWhiteList(int userId) throws RemoteException {
            return null;
        }

        public List<String> getIpBlackList(int userId) throws RemoteException {
            return null;
        }

        public void enableUsbDebug(boolean enable) throws RemoteException {
        }

        public boolean setBootAnimation(String path) throws RemoteException {
            return false;
        }

        public void setDefaultHome(String pkgName) throws RemoteException {
        }

        public String getDefaultHome() throws RemoteException {
            return null;
        }

        public void setWallPaper(String path) throws RemoteException {
        }

        public void setLockWallPaper(String path) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDeviceManager {
        private static final String DESCRIPTOR = "com.miui.enterprise.IDeviceManager";
        static final int TRANSACTION_captureScreen = 22;
        static final int TRANSACTION_deviceReboot = 3;
        static final int TRANSACTION_deviceShutDown = 2;
        static final int TRANSACTION_enableUsbDebug = 28;
        static final int TRANSACTION_formatSdCard = 4;
        static final int TRANSACTION_getDefaultHome = 31;
        static final int TRANSACTION_getIpBlackList = 27;
        static final int TRANSACTION_getIpWhiteList = 26;
        static final int TRANSACTION_getUrlBlackList = 8;
        static final int TRANSACTION_getUrlWhiteList = 7;
        static final int TRANSACTION_getWifiApBssidBlackList = 19;
        static final int TRANSACTION_getWifiApBssidWhiteList = 17;
        static final int TRANSACTION_getWifiApSsidBlackList = 18;
        static final int TRANSACTION_getWifiApSsidWhiteList = 16;
        static final int TRANSACTION_getWifiConnRestriction = 11;
        static final int TRANSACTION_isDeviceRoot = 1;
        static final int TRANSACTION_recoveryFactory = 9;
        static final int TRANSACTION_setBootAnimation = 29;
        static final int TRANSACTION_setBrowserRestriction = 21;
        static final int TRANSACTION_setDefaultHome = 30;
        static final int TRANSACTION_setIpBlackList = 25;
        static final int TRANSACTION_setIpRestriction = 23;
        static final int TRANSACTION_setIpWhiteList = 24;
        static final int TRANSACTION_setLockWallPaper = 33;
        static final int TRANSACTION_setRingerMode = 20;
        static final int TRANSACTION_setUrlBlackList = 6;
        static final int TRANSACTION_setUrlWhiteList = 5;
        static final int TRANSACTION_setWallPaper = 32;
        static final int TRANSACTION_setWifiApBssidBlackList = 15;
        static final int TRANSACTION_setWifiApBssidWhiteList = 13;
        static final int TRANSACTION_setWifiApSsidBlackList = 14;
        static final int TRANSACTION_setWifiApSsidWhiteList = 12;
        static final int TRANSACTION_setWifiConnRestriction = 10;

        private static class Proxy implements IDeviceManager {
            public static IDeviceManager sDefaultImpl;
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

            public boolean isDeviceRoot() throws RemoteException {
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
                    z = Stub.getDefaultImpl().isDeviceRoot();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deviceShutDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deviceShutDown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deviceReboot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deviceReboot();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void formatSdCard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().formatSdCard();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUrlWhiteList(List<String> urls, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(urls);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUrlWhiteList(urls, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUrlBlackList(List<String> urls, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(urls);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUrlBlackList(urls, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getUrlWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getUrlWhiteList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getUrlBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getUrlBlackList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void recoveryFactory(boolean formatSdcard) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(formatSdcard ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().recoveryFactory(formatSdcard);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiConnRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiConnRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWifiConnRestriction(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getWifiConnRestriction(userId);
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

            public void setWifiApSsidWhiteList(List<String> ssids, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(ssids);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiApSsidWhiteList(ssids, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApBssidWhiteList(List<String> bssid, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(bssid);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiApBssidWhiteList(bssid, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApSsidBlackList(List<String> ssids, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(ssids);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiApSsidBlackList(ssids, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiApBssidBlackList(List<String> bssid, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(bssid);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiApBssidBlackList(bssid, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getWifiApSsidWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWifiApSsidWhiteList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getWifiApBssidWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWifiApBssidWhiteList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getWifiApSsidBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWifiApSsidBlackList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getWifiApBssidBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWifiApBssidBlackList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRingerMode(int ringerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRingerMode(ringerMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBrowserRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBrowserRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap captureScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bitmap bitmap = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != 0) {
                            bitmap = Stub.getDefaultImpl().captureScreen();
                            return bitmap;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        bitmap = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bitmap;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIpRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIpRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIpWhiteList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIpWhiteList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIpBlackList(List<String> list, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIpBlackList(list, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getIpWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getIpWhiteList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getIpBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getIpBlackList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableUsbDebug(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableUsbDebug(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setBootAnimation(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setBootAnimation(path);
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

            public void setDefaultHome(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultHome(pkgName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultHome() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultHome();
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

            public void setWallPaper(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWallPaper(path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLockWallPaper(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLockWallPaper(path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeviceManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDeviceManager)) {
                return new Proxy(obj);
            }
            return (IDeviceManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isDeviceRoot";
                case 2:
                    return "deviceShutDown";
                case 3:
                    return "deviceReboot";
                case 4:
                    return "formatSdCard";
                case 5:
                    return "setUrlWhiteList";
                case 6:
                    return "setUrlBlackList";
                case 7:
                    return "getUrlWhiteList";
                case 8:
                    return "getUrlBlackList";
                case 9:
                    return "recoveryFactory";
                case 10:
                    return "setWifiConnRestriction";
                case 11:
                    return "getWifiConnRestriction";
                case 12:
                    return "setWifiApSsidWhiteList";
                case 13:
                    return "setWifiApBssidWhiteList";
                case 14:
                    return "setWifiApSsidBlackList";
                case 15:
                    return "setWifiApBssidBlackList";
                case 16:
                    return "getWifiApSsidWhiteList";
                case 17:
                    return "getWifiApBssidWhiteList";
                case 18:
                    return "getWifiApSsidBlackList";
                case 19:
                    return "getWifiApBssidBlackList";
                case 20:
                    return "setRingerMode";
                case 21:
                    return "setBrowserRestriction";
                case 22:
                    return "captureScreen";
                case 23:
                    return "setIpRestriction";
                case 24:
                    return "setIpWhiteList";
                case 25:
                    return "setIpBlackList";
                case 26:
                    return "getIpWhiteList";
                case 27:
                    return "getIpBlackList";
                case 28:
                    return "enableUsbDebug";
                case 29:
                    return "setBootAnimation";
                case 30:
                    return "setDefaultHome";
                case 31:
                    return "getDefaultHome";
                case 32:
                    return "setWallPaper";
                case 33:
                    return "setLockWallPaper";
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
                List<String> _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = isDeviceRoot();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        deviceShutDown();
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        deviceReboot();
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        formatSdCard();
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setUrlWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        setUrlBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = getUrlWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = getUrlBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        recoveryFactory(_arg0);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        setWifiConnRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        int _result2 = getWifiConnRestriction(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        setWifiApSsidWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        setWifiApBssidWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        setWifiApSsidBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        setWifiApBssidBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result = getWifiApSsidWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result = getWifiApBssidWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result = getWifiApSsidBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _result = getWifiApBssidBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        setRingerMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        setBrowserRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        Bitmap _result3 = captureScreen();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        setIpRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        setIpWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        setIpBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result = getIpWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result = getIpBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        enableUsbDebug(_arg0);
                        reply.writeNoException();
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        boolean _result4 = setBootAnimation(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        setDefaultHome(data.readString());
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        String _result5 = getDefaultHome();
                        reply.writeNoException();
                        reply.writeString(_result5);
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        setWallPaper(data.readString());
                        reply.writeNoException();
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        setLockWallPaper(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IDeviceManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDeviceManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Bitmap captureScreen() throws RemoteException;

    void deviceReboot() throws RemoteException;

    void deviceShutDown() throws RemoteException;

    void enableUsbDebug(boolean z) throws RemoteException;

    void formatSdCard() throws RemoteException;

    String getDefaultHome() throws RemoteException;

    List<String> getIpBlackList(int i) throws RemoteException;

    List<String> getIpWhiteList(int i) throws RemoteException;

    List<String> getUrlBlackList(int i) throws RemoteException;

    List<String> getUrlWhiteList(int i) throws RemoteException;

    List<String> getWifiApBssidBlackList(int i) throws RemoteException;

    List<String> getWifiApBssidWhiteList(int i) throws RemoteException;

    List<String> getWifiApSsidBlackList(int i) throws RemoteException;

    List<String> getWifiApSsidWhiteList(int i) throws RemoteException;

    int getWifiConnRestriction(int i) throws RemoteException;

    boolean isDeviceRoot() throws RemoteException;

    void recoveryFactory(boolean z) throws RemoteException;

    boolean setBootAnimation(String str) throws RemoteException;

    void setBrowserRestriction(int i, int i2) throws RemoteException;

    void setDefaultHome(String str) throws RemoteException;

    void setIpBlackList(List<String> list, int i) throws RemoteException;

    void setIpRestriction(int i, int i2) throws RemoteException;

    void setIpWhiteList(List<String> list, int i) throws RemoteException;

    void setLockWallPaper(String str) throws RemoteException;

    void setRingerMode(int i) throws RemoteException;

    void setUrlBlackList(List<String> list, int i) throws RemoteException;

    void setUrlWhiteList(List<String> list, int i) throws RemoteException;

    void setWallPaper(String str) throws RemoteException;

    void setWifiApBssidBlackList(List<String> list, int i) throws RemoteException;

    void setWifiApBssidWhiteList(List<String> list, int i) throws RemoteException;

    void setWifiApSsidBlackList(List<String> list, int i) throws RemoteException;

    void setWifiApSsidWhiteList(List<String> list, int i) throws RemoteException;

    void setWifiConnRestriction(int i, int i2) throws RemoteException;
}
