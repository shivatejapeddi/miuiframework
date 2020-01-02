package com.miui.mishare.app.connect;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioSystem;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.miui.mishare.IMiShareDiscoverCallback;
import com.miui.mishare.IMiShareService;
import com.miui.mishare.IMiShareService.Stub;
import com.miui.mishare.IMiShareStateListener;
import com.miui.mishare.IMiShareTaskStateListener;
import com.miui.mishare.IScreenThrowListener;
import com.miui.mishare.MiShareTask;
import java.util.ArrayList;
import java.util.List;

public class MiShareConnectivity {
    public static final String ACTION_SEND_TASK = "com.miui.mishare.action.SEND_TASK";
    public static final String ACTION_TASK_STATE = "com.miui.mishare.connectivity.TASK_STATE";
    public static final String EXTRA_MISHARE_DEVICE = "device_id";
    public static final String EXTRA_MISHARE_TASK_STATE = "state";
    public static final String EXTRA_TASK = "task";
    public static final int MISHARE_CONNECTED = 6;
    public static final int MISHARE_CONNECTING = 5;
    public static final int MISHARE_DISABLED = 1;
    public static final int MISHARE_DISABLING = 7;
    public static final int MISHARE_DISCOVERING = 4;
    public static final int MISHARE_ENABLED = 3;
    public static final int MISHARE_ENABLING = 2;
    public static final int MISHARE_UNAVAILABLE = 0;
    private static final String SERVICE_NAME = "com.miui.mishare.connectivity.MiShareService";
    private static final String SERVICE_PACKAGE_NAME = "com.miui.mishare.connectivity";
    private static final String TAG = "MiShareConnectivity";
    private static MiShareConnectivity sInstance;
    private ServiceBindCallBack mCallback;
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MiShareConnectivity.this.mService = Stub.asInterface(service);
            if (MiShareConnectivity.this.mCallback != null) {
                MiShareConnectivity.this.mCallback.onServiceBound();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            MiShareConnectivity.this.mService = null;
        }
    };
    private Context mContext;
    private IMiShareService mService;

    public interface ServiceBindCallBack {
        void onServiceBound();
    }

    public static boolean isAvailable(Context context) {
        Intent intent = new Intent();
        intent.setClassName(SERVICE_PACKAGE_NAME, SERVICE_NAME);
        PackageManager pm = context.getPackageManager();
        boolean z = false;
        if (pm == null) {
            return false;
        }
        if (pm.queryIntentServices(intent, 0).size() > 0) {
            z = true;
        }
        return z;
    }

    public static MiShareConnectivity getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MiShareConnectivity.class) {
                if (sInstance == null) {
                    sInstance = new MiShareConnectivity(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private MiShareConnectivity(Context applicationContext) {
        this.mContext = applicationContext;
    }

    public boolean bind(ServiceBindCallBack callback) {
        this.mCallback = callback;
        Intent intent = new Intent();
        intent.setClassName(SERVICE_PACKAGE_NAME, SERVICE_NAME);
        return this.mContext.bindService(intent, this.mConnection, 1);
    }

    public void unbind() {
        this.mCallback = null;
        if (this.mService != null) {
            this.mContext.unbindService(this.mConnection);
        }
    }

    public void registerStateListener(IMiShareStateListener listener) {
        if (listener != null) {
            ensureServiceBound();
            try {
                this.mService.registerStateListener(listener);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "registerStateListener: ", e);
                return;
            }
        }
        throw new NullPointerException("null listener");
    }

    public void unregisterStateListener(IMiShareStateListener listener) {
        if (listener != null) {
            ensureServiceBound();
            try {
                this.mService.unregisterStateListener(listener);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "unregisterStateListener: ", e);
                return;
            }
        }
        throw new NullPointerException("null listener");
    }

    public int getServiceState() {
        IMiShareService iMiShareService = this.mService;
        if (iMiShareService == null) {
            return 0;
        }
        try {
            return iMiShareService.getState();
        } catch (RemoteException e) {
            Log.e(TAG, "getServiceState: ", e);
            return 0;
        }
    }

    public void enable() {
        ensureServiceBound();
        try {
            this.mService.enable();
        } catch (RemoteException e) {
            Log.e(TAG, "enable: ", e);
        }
    }

    public void startDiscover(IMiShareDiscoverCallback callback) {
        ensureServiceBound();
        try {
            this.mService.discover(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "startDiscover: ", e);
        }
    }

    public void startDiscoverWithIntent(IMiShareDiscoverCallback callback, Intent intent) {
        ensureServiceBound();
        try {
            this.mService.discoverWithIntent(callback, intent);
        } catch (RemoteException e) {
            Log.e(TAG, "startDiscoverWithConfig: ", e);
        }
    }

    public void stopDiscover(IMiShareDiscoverCallback callback) {
        ensureServiceBound();
        try {
            this.mService.stopDiscover(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "startDiscover: ", e);
        }
    }

    public void registerTaskStateListener(IMiShareTaskStateListener listener) {
        ensureServiceBound();
        try {
            this.mService.registerTaskStateListener(listener);
        } catch (RemoteException e) {
            Log.e(TAG, "registerTaskStateListener: ", e);
        }
    }

    public void unregisterTaskStateListener(IMiShareTaskStateListener listener) {
        ensureServiceBound();
        try {
            this.mService.unregisterTaskStateListener(listener);
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterTaskStateListener: ", e);
        }
    }

    public void registerScreenThrowListener(IScreenThrowListener listener) {
        ensureServiceBound();
        try {
            this.mService.registerScreenThrowListener(listener);
        } catch (RemoteException e) {
            Log.e(TAG, "registerScreenThrowListener: ", e);
        }
    }

    public void unregisterScreenThrowListener() {
        ensureServiceBound();
        try {
            this.mService.unregisterScreenThrowListener();
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterScreenThrowListener: ", e);
        }
    }

    public void openScreenThrow() {
        ensureServiceBound();
        try {
            this.mService.openScreenThrow();
        } catch (RemoteException e) {
            Log.e(TAG, "openScreenThrow: ", e);
        }
    }

    public void closeScreenThrow() {
        ensureServiceBound();
        try {
            this.mService.closeScreenThrow();
        } catch (RemoteException e) {
            Log.e(TAG, "closeScreenThrow: ", e);
        }
    }

    public void sendData(MiShareTask task) {
        if (task != null) {
            ensureServiceBound();
            ArrayList<Uri> contentUris = new ArrayList();
            for (int i = 0; i < task.clipData.getItemCount(); i++) {
                if (task.clipData.getItemAt(i) != null) {
                    Uri uri = task.clipData.getItemAt(i).getUri();
                    if (uri != null && uri.getScheme().startsWith("content")) {
                        contentUris.add(uri);
                    }
                }
            }
            Intent intent = new Intent(ACTION_SEND_TASK);
            intent.setClassName(SERVICE_PACKAGE_NAME, SERVICE_NAME);
            if (contentUris.size() > 0) {
                intent.setClipData(getClipData(contentUris));
                intent.addFlags(AudioSystem.DEVICE_IN_COMMUNICATION);
            }
            intent.putExtra(EXTRA_TASK, (Parcelable) task);
            this.mContext.startService(intent);
            return;
        }
        throw new NullPointerException("null task");
    }

    private ClipData getClipData(List<Uri> files) {
        if (files == null || files.size() <= 0) {
            return null;
        }
        ClipData data = new ClipData(new ClipDescription("mishare data", new String[]{""}), new Item((Uri) files.get(0)));
        int count = files.size();
        for (int i = 1; i < count; i++) {
            Uri uriItem = (Uri) files.get(i);
            if (uriItem != null) {
                data.addItem(new Item(uriItem));
            }
        }
        return data;
    }

    public void cancel(MiShareTask task) {
        IMiShareService iMiShareService = this.mService;
        if (iMiShareService != null) {
            try {
                iMiShareService.cancel(task);
            } catch (RemoteException e) {
                Log.e(TAG, "cancel: ", e);
            }
        }
    }

    public boolean checkServiceBound() {
        return this.mService != null;
    }

    private void ensureServiceBound() {
        if (this.mService == null) {
            throw new NullPointerException("service not bound");
        }
    }
}
