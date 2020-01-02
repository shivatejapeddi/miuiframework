package com.miui.whetstone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.miui.whetstone.IWhetstoneSysInfoService.Stub;
import com.miui.whetstone.provider.WhetstoneProviderContract;

public class WhetstoneSysInfoManager {
    private static final long FAIL = -1;
    private static final String TAG = WhetstoneSysInfoManager.class.getSimpleName();
    private IWhetstoneSysInfoService mService;
    private ServiceConnection mServiceConnection;

    private static final class Holder {
        private static WhetstoneSysInfoManager INSTANCE = new WhetstoneSysInfoManager();

        private Holder() {
        }
    }

    /* synthetic */ WhetstoneSysInfoManager(AnonymousClass1 x0) {
        this();
    }

    public synchronized void bindWhetstoneSysInfoService(Context context) {
        if (this.mService == null) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(WhetstoneProviderContract.AUTHORITY, "com.miui.whetstone.sysinfo.WhetstoneSysInfoService"));
            boolean res = context.bindService(intent, this.mServiceConnection, 1);
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("bindWhetstoneSysInfoService ");
            stringBuilder.append(res);
            Log.i(str, stringBuilder.toString());
        } else {
            Log.w(TAG, "bindWhetstoneSysInfoService but mService is not null");
        }
    }

    public synchronized void unbindWhetstoneSysInfoService(Context context) {
        if (this.mService != null) {
            context.unbindService(this.mServiceConnection);
            this.mService = null;
            Log.i(TAG, "unbindWhetstoneSysInfoService");
        } else {
            Log.w(TAG, "unbindWhetstoneSysInfoService but service is null");
        }
    }

    public synchronized long getSysInfo(String type) {
        try {
            if (this.mService != null) {
                return this.mService.getSysInfo(type);
            }
            Log.e(TAG, "getSysInfo: Service is null");
            return -1;
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getSysInfo long RemoteException e");
            stringBuilder.append(e.getMessage());
            Log.e(str, stringBuilder.toString());
        }
    }

    public synchronized String[] getSysInfos(String[] types) {
        try {
            if (this.mService != null) {
                return this.mService.getSysInfos(types);
            }
            Log.e(TAG, "getSysInfo: Service is null");
            return null;
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getSysInfo String[] RemoteException e");
            stringBuilder.append(e.getMessage());
            Log.e(str, stringBuilder.toString());
        }
    }

    public static WhetstoneSysInfoManager getInstance() {
        return Holder.INSTANCE;
    }

    private WhetstoneSysInfoManager() {
        this.mServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i(WhetstoneSysInfoManager.TAG, "onServiceConnected");
                WhetstoneSysInfoManager.this.mService = Stub.asInterface(iBinder);
            }

            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(WhetstoneSysInfoManager.TAG, "onServiceDisconnected");
                WhetstoneSysInfoManager.this.mService = null;
            }
        };
    }
}
