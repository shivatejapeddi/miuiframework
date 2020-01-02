package com.miui.whetstone;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* compiled from: WhetstoneResultBinder */
class WhetstoneResultProxy implements IWhetstoneResult {
    private IBinder mRemote;

    public WhetstoneResultProxy(IBinder remote) {
        this.mRemote = remote;
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    public void onResult(WhetstoneResult result) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IWhetstoneResult.descriptor);
        result.writeToParcel(data, 0);
        this.mRemote.transact(1, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }
}
