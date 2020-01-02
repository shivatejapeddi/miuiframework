package com.miui.whetstone;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class WhetstoneResultBinder extends Binder implements IWhetstoneResult {
    public static IWhetstoneResult asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IWhetstoneResult in = (IWhetstoneResult) obj.queryLocalInterface(IWhetstoneResult.descriptor);
        if (in != null) {
            return in;
        }
        return new WhetstoneResultProxy(obj);
    }

    public void onResult(WhetstoneResult result) {
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code != 1) {
            return super.onTransact(code, data, reply, flags);
        }
        data.enforceInterface(IWhetstoneResult.descriptor);
        onResult((WhetstoneResult) WhetstoneResult.CREATOR.createFromParcel(data));
        reply.writeNoException();
        return true;
    }

    public IBinder asBinder() {
        return this;
    }
}
