package org.ifaa.android.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IHwBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Slog;
import java.util.ArrayList;
import java.util.Arrays;
import miui.util.FeatureParser;
import org.json.JSONObject;

public class IFAAManagerImpl extends IFAAManagerV4 {
    private static final int CODE_GETIDLIST_CMD = 2;
    private static final int CODE_PROCESS_CMD = 1;
    private static final boolean DEBUG = false;
    private static final int IFAA_TYPE_2DFA = 32;
    private static final int IFAA_TYPE_FINGER = 1;
    private static final int IFAA_TYPE_IRIS = 2;
    private static final int IFAA_TYPE_SENSOR_FOD = 16;
    private static volatile IFAAManagerImpl INSTANCE = null;
    private static final String INTERFACE_DESCRIPTOR = "vendor.xiaomi.hardware.mlipay@1.0::IMlipayService";
    private static final String SERVICE_NAME = "vendor.xiaomi.hardware.mlipay@1.0::IMlipayService";
    private static final String TAG = "IfaaManagerImpl";
    private static ServiceConnection ifaaconn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            IFAAManagerImpl.mService = service;
            try {
                IFAAManagerImpl.mService.linkToDeath(IFAAManagerImpl.mDeathRecipient, 0);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("linkToDeath fail. ");
                stringBuilder.append(e);
                Slog.e(IFAAManagerImpl.TAG, stringBuilder.toString());
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            if (IFAAManagerImpl.mContext != null) {
                Slog.i(IFAAManagerImpl.TAG, "re-bind the service.");
                IFAAManagerImpl.initService();
            }
        }
    };
    private static Context mContext = null;
    private static DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            if (IFAAManagerImpl.mService != null) {
                Slog.d(IFAAManagerImpl.TAG, "binderDied, unlink the service.");
                IFAAManagerImpl.mService.unlinkToDeath(IFAAManagerImpl.mDeathRecipient, 0);
            }
        }
    };
    private static final String mFingerActName = "com.android.settings.NewFingerprintActivity";
    private static final String mFingerPackName = "com.android.settings";
    private static final String mIfaaActName = "org.ifaa.android.manager.IFAAService";
    private static final String mIfaaInterfaceDesc = "org.ifaa.android.manager.IIFAAService";
    private static final String mIfaaPackName = "com.tencent.soter.soterserver";
    private static IBinder mService = null;
    private static final String seperate = ",";
    private String mDevModel = null;

    public static IFAAManagerV4 getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (IFAAManagerImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IFAAManagerImpl();
                    if (VERSION.SDK_INT >= 28) {
                        mContext = context;
                        initService();
                    }
                }
            }
        }
        return INSTANCE;
    }

    private static void initService() {
        Intent ifaaIntent = new Intent();
        ifaaIntent.setClassName(mIfaaPackName, mIfaaActName);
        if (!mContext.bindService(ifaaIntent, ifaaconn, 1)) {
            Slog.e(TAG, "cannot bind service org.ifaa.android.manager.IFAAService");
        }
    }

    public int getSupportBIOTypes(Context context) {
        int ifaaProp;
        int res;
        String fpVendor = "";
        int ifaa_2dfa_support = FeatureParser.getInteger("ifaa_2dfa_support", 0);
        String str = "";
        if (VERSION.SDK_INT >= 28) {
            ifaaProp = SystemProperties.getInt("persist.vendor.sys.pay.ifaa", 0);
            fpVendor = SystemProperties.get("persist.vendor.sys.fp.vendor", str);
        } else {
            ifaaProp = SystemProperties.getInt("persist.sys.ifaa", 0);
            fpVendor = SystemProperties.get("persist.sys.fp.vendor", str);
        }
        if ("none".equalsIgnoreCase(fpVendor)) {
            res = ifaaProp & 2;
        } else {
            res = ifaaProp & 3;
        }
        if ((res & 1) == 1 && sIsFod) {
            res |= 16;
        }
        if (ifaa_2dfa_support == 1) {
            if ("enable".equals(SystemProperties.get("ro.boot.hypvm", str))) {
                res |= 32;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getSupportBIOTypesV26:");
        stringBuilder.append(ifaaProp);
        String str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder.append(str2);
        stringBuilder.append(sIsFod);
        stringBuilder.append(str2);
        stringBuilder.append(fpVendor);
        stringBuilder.append(" res:");
        stringBuilder.append(res);
        Slog.i(TAG, stringBuilder.toString());
        return res;
    }

    public int startBIOManager(Context context, int authType) {
        int res = -1;
        if (1 == authType) {
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", mFingerActName);
            intent.setFlags(268435456);
            context.startActivity(intent);
            res = 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("startBIOManager authType:");
        stringBuilder.append(authType);
        stringBuilder.append(" res:");
        stringBuilder.append(res);
        Slog.i(TAG, stringBuilder.toString());
        return res;
    }

    public String getDeviceModel() {
        if (this.mDevModel == null) {
            String miuiFeature = FeatureParser.getString("finger_alipay_ifaa_model");
            if (miuiFeature == null || "".equalsIgnoreCase(miuiFeature)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Build.MANUFACTURER);
                stringBuilder.append("-");
                stringBuilder.append(Build.DEVICE);
                this.mDevModel = stringBuilder.toString();
            } else {
                this.mDevModel = miuiFeature;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("getDeviceModel devcieModel:");
        stringBuilder2.append(this.mDevModel);
        Slog.i(TAG, stringBuilder2.toString());
        return this.mDevModel;
    }

    public int getVersion() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getVersion sdk:");
        stringBuilder.append(VERSION.SDK_INT);
        stringBuilder.append(" ifaaVer:");
        stringBuilder.append(sIfaaVer);
        Slog.i(TAG, stringBuilder.toString());
        return sIfaaVer;
    }

    public byte[] processCmdV2(Context context, byte[] param) {
        byte[] createByteArray;
        String str = "vendor.xiaomi.hardware.mlipay@1.0::IMlipayService";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("processCmdV2 sdk:");
        stringBuilder.append(VERSION.SDK_INT);
        String stringBuilder2 = stringBuilder.toString();
        String str2 = TAG;
        Slog.i(str2, stringBuilder2);
        if (VERSION.SDK_INT >= 28) {
            int retry_count = 10;
            loop0:
            while (true) {
                int retry_count2 = retry_count - 1;
                if (retry_count <= 0) {
                    break loop0;
                }
                IBinder iBinder = mService;
                if (iBinder == null || !iBinder.pingBinder()) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("processCmdV2 waiting ifaaService, remain: ");
                    stringBuilder3.append(retry_count2);
                    stringBuilder3.append(" time(s)");
                    Slog.i(str2, stringBuilder3.toString());
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("processCmdV2 InterruptedException while waiting: ");
                        stringBuilder4.append(e);
                        Slog.e(str2, stringBuilder4.toString());
                    }
                } else {
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(mIfaaInterfaceDesc);
                        data.writeByteArray(param);
                        mService.transact(1, data, reply, 0);
                        reply.readException();
                        createByteArray = reply.createByteArray();
                    } catch (RemoteException e2) {
                        StringBuilder stringBuilder5 = new StringBuilder();
                        stringBuilder5.append("processCmdV2 transact failed. ");
                        stringBuilder5.append(e2);
                        Slog.e(str2, stringBuilder5.toString());
                        retry_count = retry_count2;
                    } finally {
                        data.recycle();
                        reply.recycle();
                        return createByteArray;
                    }
                }
                retry_count = retry_count2;
            }
            return createByteArray;
        }
        HwParcel hidl_reply = new HwParcel();
        try {
            IHwBinder hwService = HwBinder.getService(str, "default");
            if (hwService != null) {
                HwParcel hidl_request = new HwParcel();
                hidl_request.writeInterfaceToken(str);
                ArrayList sbuf = new ArrayList(Arrays.asList(HwBlob.wrapArray(param)));
                hidl_request.writeInt8Vector(sbuf);
                hidl_request.writeInt32(sbuf.size());
                hwService.transact(1, hidl_request, hidl_reply, 0);
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
        } catch (RemoteException e3) {
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append("transact failed. ");
            stringBuilder6.append(e3);
            Slog.e(str2, stringBuilder6.toString());
        } catch (Throwable th) {
            hidl_reply.release();
        }
        hidl_reply.release();
        Slog.e(str2, "processCmdV2, return null");
        return null;
    }

    public String getExtInfo(int authType, String keyExtInfo) {
        return initExtString();
    }

    public void setExtInfo(int authType, String keyExtInfo, String valExtInfo) {
    }

    public int getEnabled(int bioType) {
        if (1 == bioType) {
            return 1000;
        }
        return 1003;
    }

    public int[] getIDList(int bioType) {
        int[] idList = new int[]{0};
        if (1 == bioType) {
            int retry_count = 10;
            while (true) {
                int retry_count2 = retry_count - 1;
                if (retry_count <= 0) {
                    break;
                }
                IBinder iBinder = mService;
                String str = TAG;
                if (iBinder == null || !iBinder.pingBinder()) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("getIDList InterruptedException while waiting: ");
                        stringBuilder.append(e);
                        Slog.e(str, stringBuilder.toString());
                    }
                    retry_count = retry_count2;
                } else {
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(mIfaaInterfaceDesc);
                        data.writeInt(bioType);
                        mService.transact(2, data, reply, 0);
                        reply.readException();
                        str = reply.createIntArray();
                        idList = str;
                    } catch (RemoteException e2) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("getIDList transact failed. ");
                        stringBuilder2.append(e2);
                        Slog.e(str, stringBuilder2.toString());
                    } catch (Throwable th) {
                        data.recycle();
                        reply.recycle();
                    }
                    data.recycle();
                    reply.recycle();
                    retry_count = retry_count2;
                }
            }
        }
        return idList;
    }

    private String initExtString() {
        String[] splitWh = ",";
        String str = " wh:";
        String str2 = TAG;
        String extStr = "";
        JSONObject obj = new JSONObject();
        JSONObject keyInfo = new JSONObject();
        String xy = "";
        String wh = "";
        String str3 = "";
        if (VERSION.SDK_INT >= 28) {
            xy = SystemProperties.get("persist.vendor.sys.fp.fod.location.X_Y", str3);
            wh = SystemProperties.get("persist.vendor.sys.fp.fod.size.width_height", str3);
        } else {
            xy = SystemProperties.get("persist.sys.fp.fod.location.X_Y", str3);
            wh = SystemProperties.get("persist.sys.fp.fod.size.width_height", str3);
        }
        try {
            if (validateVal(xy) && validateVal(wh)) {
                String[] splitXy = xy.split(splitWh);
                splitWh = wh.split(splitWh);
                keyInfo.put("startX", Integer.parseInt(splitXy[0]));
                keyInfo.put("startY", Integer.parseInt(splitXy[1]));
                keyInfo.put("width", Integer.parseInt(splitWh[0]));
                keyInfo.put("height", Integer.parseInt(splitWh[1]));
                keyInfo.put("navConflict", true);
                obj.put("type", 0);
                obj.put("fullView", keyInfo);
                return obj.toString();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("initExtString invalidate, xy:");
            stringBuilder.append(xy);
            stringBuilder.append(str);
            stringBuilder.append(wh);
            Slog.e(str2, stringBuilder.toString());
            return extStr;
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Exception , xy:");
            stringBuilder2.append(xy);
            stringBuilder2.append(str);
            stringBuilder2.append(wh);
            Slog.e(str2, stringBuilder2.toString(), e);
            return extStr;
        }
    }

    private boolean validateVal(String value) {
        if ("".equalsIgnoreCase(value) || !value.contains(",")) {
            return false;
        }
        return true;
    }
}
