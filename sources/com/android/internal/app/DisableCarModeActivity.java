package com.android.internal.app;

import android.app.Activity;
import android.app.IUiModeManager.Stub;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class DisableCarModeActivity extends Activity {
    private static final String TAG = "DisableCarModeActivity";

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Stub.asInterface(ServiceManager.getService(Context.UI_MODE_SERVICE)).disableCarMode(1);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to disable car mode", e);
        }
        finish();
    }
}
