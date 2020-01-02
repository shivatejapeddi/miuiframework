package org.ifaa.android.manager.face;

import android.content.Context;

public class IFAAFaceManagerFactory {
    public static IFAAFaceManager getIFAAFaceManager(Context context) {
        return IFAAFaceManagerImpl.getInstance(context);
    }
}
