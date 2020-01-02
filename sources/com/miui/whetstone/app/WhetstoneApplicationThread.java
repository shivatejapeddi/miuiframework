package com.miui.whetstone.app;

import android.os.Binder;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import miui.util.LongScreenshotUtils.ContentPort;

public class WhetstoneApplicationThread extends Binder implements IWhetstoneApplicationThread {
    private ContentPort mContentPort;

    public static IWhetstoneApplicationThread asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IWhetstoneApplicationThread in = (IWhetstoneApplicationThread) obj.queryLocalInterface(IWhetstoneApplicationThread.descriptor);
        if (in != null) {
            return in;
        }
        return new WhetstoneApplicationThreadProxy(obj);
    }

    public WhetstoneApplicationThread() {
        attachInterface(this, IWhetstoneApplicationThread.descriptor);
    }

    public MemoryInfo dumpMemInfo(String[] args) throws RemoteException {
        MemoryInfo memInfo = new MemoryInfo();
        Debug.getMemoryInfo(memInfo);
        return memInfo;
    }

    public boolean longScreenshot(int cmd) throws RemoteException {
        if (this.mContentPort == null) {
            this.mContentPort = new ContentPort();
        }
        return this.mContentPort.longScreenshot(cmd, 0);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        int cmd = IWhetstoneApplicationThread.descriptor;
        if (code == 1) {
            data.enforceInterface(cmd);
            MemoryInfo mi = dumpMemInfo(data.readStringArray());
            reply.writeNoException();
            mi.writeToParcel(reply, 0);
            return true;
        } else if (code != 2) {
            return super.onTransact(code, data, reply, flags);
        } else {
            data.enforceInterface(cmd);
            boolean result = longScreenshot(data.readInt());
            reply.writeNoException();
            reply.writeByte((byte) result);
            return true;
        }
    }

    public IBinder asBinder() {
        return this;
    }
}
