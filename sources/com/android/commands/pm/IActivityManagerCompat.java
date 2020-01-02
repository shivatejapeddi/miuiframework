package com.android.commands.pm;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.Intent;
import android.os.RemoteException;

public class IActivityManagerCompat {
    public static int startActivity(Intent intent) throws RemoteException {
        IActivityManager am = ActivityManagerNative.getDefault();
        return am.startActivityAsUser(null, "pm", intent, null, null, null, 0, 0, null, null, am.getCurrentUser().getUserHandle().getIdentifier());
    }
}
