package com.miui.whetstone.app;

import android.os.Debug.MemoryInfo;
import android.os.IInterface;
import android.os.RemoteException;

public interface IWhetstoneApplicationThread extends IInterface {
    public static final int DUMP_MEM_INFO_TRANSACTION = 1;
    public static final int LONG_SCREENSHOT_TRANSACTION = 2;
    public static final String descriptor = "com.miui.whetstone.app.IWhetstoneApplicationThread";

    MemoryInfo dumpMemInfo(String[] strArr) throws RemoteException;

    boolean longScreenshot(int i) throws RemoteException;
}
