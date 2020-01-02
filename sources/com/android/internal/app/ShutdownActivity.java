package com.android.internal.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;

public class ShutdownActivity extends Activity {
    private static final String TAG = "ShutdownActivity";
    private boolean mConfirm;
    private boolean mReboot;
    private boolean mUserRequested;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        String reason;
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.mReboot = Intent.ACTION_REBOOT.equals(intent.getAction());
        this.mConfirm = intent.getBooleanExtra(Intent.EXTRA_KEY_CONFIRM, false);
        this.mUserRequested = intent.getBooleanExtra(Intent.EXTRA_USER_REQUESTED_SHUTDOWN, false);
        if (this.mUserRequested) {
            reason = PowerManager.SHUTDOWN_USER_REQUESTED;
        } else {
            reason = intent.getStringExtra(Intent.EXTRA_REASON);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onCreate(): confirm=");
        stringBuilder.append(this.mConfirm);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Slog.i(str, stringBuilder2);
        Thread thr = new Thread(str) {
            public void run() {
                IPowerManager pm = Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE));
                try {
                    if (ShutdownActivity.this.mReboot) {
                        pm.reboot(ShutdownActivity.this.mConfirm, null, false);
                    } else {
                        pm.shutdown(ShutdownActivity.this.mConfirm, reason, false);
                    }
                } catch (RemoteException e) {
                }
            }
        };
        thr.start();
        finish();
        try {
            thr.join();
        } catch (InterruptedException e) {
        }
    }
}
