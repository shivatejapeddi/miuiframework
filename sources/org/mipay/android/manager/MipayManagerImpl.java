package org.mipay.android.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IHwBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Slog;
import java.util.ArrayList;

public class MipayManagerImpl implements IMipayManager {
    private static int CODE_CONTAINS = 1;
    private static int CODE_GEN_KEY_PAIR = 2;
    private static int CODE_GET_FP_IDS = 7;
    private static int CODE_RM_ALL_KEY = 6;
    private static int CODE_SIGN = 5;
    private static int CODE_SIGN_INIT = 3;
    private static int CODE_SIGN_UPDATE = 4;
    private static boolean DEBUG = false;
    private static volatile MipayManagerImpl INSTANCE = null;
    private static String INTERFACE_DESCRIPTOR;
    private static int MIPAY_TYPE_FINGER = 1;
    private static int MIPAY_TYPE_IRIS = 2;
    private static int MIPAY_VERISON_1 = 1;
    private static String SERVICE_NAME;
    private static String TAG = "MipayManagerImpl";
    private static Context mContext;
    private static DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            if (MipayManagerImpl.mService != null) {
                Slog.i(MipayManagerImpl.TAG, "binderDied, unlink the service.");
                MipayManagerImpl.mService.unlinkToDeath(MipayManagerImpl.mDeathRecipient, 0);
            }
        }
    };
    private static String mPackName = "com.tencent.soter.soterserver";
    private static IBinder mService;
    private static String mTidaActName = "org.mipay.android.manager.MipayService";
    private static String mTidaInterfaceDesc = "org.mipay.android.manager.IMipayService";
    private static ServiceConnection mipayconn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (MipayManagerImpl.DEBUG) {
                String access$400 = MipayManagerImpl.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onServiceConnected name = ");
                stringBuilder.append(name);
                Slog.i(access$400, stringBuilder.toString());
            }
            MipayManagerImpl.mService = service;
            try {
                MipayManagerImpl.mService.linkToDeath(MipayManagerImpl.mDeathRecipient, 0);
            } catch (RemoteException e) {
                String access$4002 = MipayManagerImpl.TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("linkToDeath fail. ");
                stringBuilder2.append(e);
                Slog.e(access$4002, stringBuilder2.toString());
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            if (MipayManagerImpl.DEBUG) {
                String access$400 = MipayManagerImpl.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onServiceDisconnected name = ");
                stringBuilder.append(name);
                Slog.i(access$400, stringBuilder.toString());
            }
            if (MipayManagerImpl.mContext != null) {
                Slog.i(MipayManagerImpl.TAG, "re-bind the service.");
                MipayManagerImpl.bindTidaService();
            }
        }
    };
    private IHwBinder mHwService;

    static {
        String str = "vendor.xiaomi.hardware.tidaservice@1.0::ITidaService";
        SERVICE_NAME = str;
        INTERFACE_DESCRIPTOR = str;
    }

    private void initService() throws RemoteException {
        if (this.mHwService == null) {
            this.mHwService = HwBinder.getService(SERVICE_NAME, "default");
        }
    }

    private static void bindTidaService() {
        Thread joinThread = new Thread("TidaThread") {
            public void run() {
                Intent tidaIntent = new Intent();
                tidaIntent.setClassName(MipayManagerImpl.mPackName, MipayManagerImpl.mTidaActName);
                if (!MipayManagerImpl.mContext.bindService(tidaIntent, MipayManagerImpl.mipayconn, 1)) {
                    String access$400 = MipayManagerImpl.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("cannot bind service ");
                    stringBuilder.append(MipayManagerImpl.mTidaActName);
                    Slog.e(access$400, stringBuilder.toString());
                }
                if (MipayManagerImpl.DEBUG) {
                    Slog.i(MipayManagerImpl.TAG, "Tida client calling joinThreadPool");
                }
                Binder.joinThreadPool();
            }
        };
        joinThread.setDaemon(true);
        joinThread.start();
    }

    private static int connectService(int cmd_id, Parcel data, Parcel reply) {
        String str;
        StringBuilder stringBuilder;
        int retry_count = 10;
        while (true) {
            int retry_count2 = retry_count - 1;
            if (retry_count <= 0) {
                return -1;
            }
            IBinder iBinder = mService;
            if (iBinder == null || !iBinder.pingBinder()) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("connectService waiting mipayService, remain: ");
                stringBuilder2.append(retry_count2);
                stringBuilder2.append(" time(s)");
                Slog.i(str2, stringBuilder2.toString());
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("connectService InterruptedException while waiting: ");
                    stringBuilder.append(e);
                    Slog.e(str, stringBuilder.toString());
                }
            } else {
                try {
                    mService.transact(cmd_id, data, reply, 0);
                    return 0;
                } catch (RemoteException e2) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("connectService transact failed. ");
                    stringBuilder.append(e2);
                    Slog.e(str, stringBuilder.toString());
                }
            }
            retry_count = retry_count2;
        }
    }

    public static IMipayManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MipayManagerImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MipayManagerImpl();
                    if (VERSION.SDK_INT >= 28) {
                        mContext = context;
                        bindTidaService();
                    }
                }
            }
        }
        return INSTANCE;
    }

    private int signUpdate(String message) {
        int res = -1;
        if (VERSION.SDK_INT >= 28) {
            int uid = Binder.getCallingUid();
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            data.writeInt(uid);
            data.writeString(message);
            res = connectService(CODE_SIGN_UPDATE, data, reply);
            if (res != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.readInt();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    hidl_request.writeString(message);
                    this.mHwService.transact(CODE_SIGN_UPDATE, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    res = hidl_reply.readInt32();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("signUpdate, res:");
            stringBuilder2.append(res);
            Slog.i(str2, stringBuilder2.toString());
        }
        return res;
    }

    public int getVersion() {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getVersion :");
            stringBuilder.append(MIPAY_VERISON_1);
            Slog.i(str, stringBuilder.toString());
        }
        return MIPAY_VERISON_1;
    }

    public int getSupportBIOTypes(Context context) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getSupportBIOTypes :");
            stringBuilder.append(MIPAY_TYPE_FINGER);
            Slog.i(str, stringBuilder.toString());
        }
        return MIPAY_TYPE_FINGER;
    }

    public boolean contains(String alias) {
        boolean res = false;
        if (VERSION.SDK_INT >= 28) {
            int uid = Binder.getCallingUid();
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            data.writeInt(uid);
            data.writeString(alias);
            if (connectService(CODE_CONTAINS, data, reply) != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.readBoolean();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    hidl_request.writeString(alias);
                    this.mHwService.transact(CODE_CONTAINS, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    res = hidl_reply.readBool();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("contains, ");
            stringBuilder2.append(alias);
            stringBuilder2.append(" res:");
            stringBuilder2.append(res);
            Slog.i(str2, stringBuilder2.toString());
        }
        return res;
    }

    public int generateKeyPair(String alias, String alg) {
        int res = -1;
        if (VERSION.SDK_INT >= 28) {
            int uid = Binder.getCallingUid();
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            data.writeInt(uid);
            data.writeString(alias);
            data.writeString(alg);
            res = connectService(CODE_GEN_KEY_PAIR, data, reply);
            if (res != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.readInt();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    hidl_request.writeString(alias);
                    hidl_request.writeString(alg);
                    this.mHwService.transact(CODE_GEN_KEY_PAIR, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    res = hidl_reply.readInt32();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("generateKeyPair, ");
            stringBuilder2.append(alias);
            stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder2.append(alg);
            stringBuilder2.append(" res:");
            stringBuilder2.append(res);
            Slog.i(str2, stringBuilder2.toString());
        }
        return res;
    }

    public int signInit(String alias, String algorithm) {
        int res = -1;
        if (VERSION.SDK_INT >= 28) {
            int uid = Binder.getCallingUid();
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            data.writeInt(uid);
            data.writeString(alias);
            data.writeString(algorithm);
            res = connectService(CODE_SIGN_INIT, data, reply);
            if (res != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.readInt();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    hidl_request.writeString(alias);
                    hidl_request.writeString(algorithm);
                    this.mHwService.transact(CODE_SIGN_INIT, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    res = hidl_reply.readInt32();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("signInit, ");
            stringBuilder2.append(alias);
            stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder2.append(algorithm);
            stringBuilder2.append(" res:");
            stringBuilder2.append(res);
            Slog.i(str2, stringBuilder2.toString());
        }
        return res;
    }

    public int signUpdate(byte[] b, int off, int len) {
        return signUpdate(new String(b, off, len));
    }

    public byte[] sign() {
        if (VERSION.SDK_INT >= 28) {
            byte[] res = new byte[0];
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            if (connectService(CODE_SIGN, data, reply) != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.createByteArray();
                data.recycle();
                reply.recycle();
            }
            return res;
        }
        HwParcel hidl_reply = new HwParcel();
        try {
            initService();
            if (this.mHwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                this.mHwService.transact(CODE_SIGN, hidl_request, hidl_reply, 0);
                hidl_reply.verifySuccess();
                hidl_request.releaseTemporaryStorage();
                ArrayList<Byte> val = hidl_reply.readInt8Vector();
                int n = val.size();
                byte[] array = new byte[n];
                for (int i = 0; i < n; i++) {
                    array[i] = ((Byte) val.get(i)).byteValue();
                }
                hidl_reply.release();
                return array;
            }
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("transact fail. ");
            stringBuilder.append(e);
            Slog.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(TAG, "sign fail, return null");
        return null;
    }

    public int removeAllKey() {
        int res = -1;
        if (VERSION.SDK_INT >= 28) {
            int uid = Binder.getCallingUid();
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            data.writeInt(uid);
            res = connectService(CODE_RM_ALL_KEY, data, reply);
            if (res != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                res = reply.readInt();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    this.mHwService.transact(CODE_RM_ALL_KEY, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    res = hidl_reply.readInt32();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("removeAllKey, res:");
            stringBuilder2.append(res);
            Slog.i(str2, stringBuilder2.toString());
        }
        return res;
    }

    public String getFpIds() {
        String fpIds = "";
        if (VERSION.SDK_INT >= 28) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(mTidaInterfaceDesc);
            if (connectService(CODE_GET_FP_IDS, data, reply) != 0) {
                Slog.e(TAG, "MipayService not found");
                data.recycle();
                reply.recycle();
            } else {
                reply.readException();
                fpIds = reply.readString();
                data.recycle();
                reply.recycle();
            }
        } else {
            HwParcel hidl_reply = new HwParcel();
            try {
                initService();
                if (this.mHwService != null) {
                    HwParcel hidl_request = new HwParcel();
                    hidl_request.writeInterfaceToken(INTERFACE_DESCRIPTOR);
                    this.mHwService.transact(CODE_GET_FP_IDS, hidl_request, hidl_reply, 0);
                    hidl_reply.verifySuccess();
                    hidl_request.releaseTemporaryStorage();
                    fpIds = hidl_reply.readString();
                }
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("transact fail. ");
                stringBuilder.append(e);
                Slog.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                hidl_reply.release();
            }
            hidl_reply.release();
        }
        if (DEBUG) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getFpIds, fpIds:");
            stringBuilder2.append(fpIds);
            Slog.i(str2, stringBuilder2.toString());
        }
        return fpIds;
    }
}
