package com.miui.whetstone.app;

import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* compiled from: WhetstoneApplicationThread */
class WhetstoneApplicationThreadProxy implements IWhetstoneApplicationThread {
    private final IBinder mRemote;

    public WhetstoneApplicationThreadProxy(IBinder remote) {
        this.mRemote = remote;
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    public MemoryInfo dumpMemInfo(String[] args) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IWhetstoneApplicationThread.descriptor);
        data.writeStringArray(args);
        this.mRemote.transact(1, data, reply, 0);
        reply.readException();
        MemoryInfo info = new MemoryInfo();
        info.readFromParcel(reply);
        data.recycle();
        reply.recycle();
        return info;
    }

    public boolean longScreenshot(int cmd) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IWhetstoneApplicationThread.descriptor);
        data.writeInt(cmd);
        boolean z = false;
        this.mRemote.transact(2, data, reply, 0);
        reply.readException();
        if (reply.readByte() != (byte) 0) {
            z = true;
        }
        boolean result = z;
        data.recycle();
        reply.recycle();
        return result;
    }
}
