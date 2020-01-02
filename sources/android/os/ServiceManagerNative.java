package android.os;

import android.annotation.UnsupportedAppUsage;
import android.os.IPermissionController.Stub;

public abstract class ServiceManagerNative extends Binder implements IServiceManager {
    @UnsupportedAppUsage
    public static IServiceManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IServiceManager in = (IServiceManager) obj.queryLocalInterface(IServiceManager.descriptor);
        if (in != null) {
            return in;
        }
        return new ServiceManagerProxy(obj);
    }

    public ServiceManagerNative() {
        attachInterface(this, IServiceManager.descriptor);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        String name = IServiceManager.descriptor;
        if (code == 1) {
            data.enforceInterface(name);
            reply.writeStrongBinder(getService(data.readString()));
            return true;
        } else if (code == 2) {
            data.enforceInterface(name);
            reply.writeStrongBinder(checkService(data.readString()));
            return true;
        } else if (code == 3) {
            data.enforceInterface(name);
            addService(data.readString(), data.readStrongBinder(), data.readInt() != 0, data.readInt());
            return true;
        } else if (code != 4) {
            if (code == 6) {
                try {
                    data.enforceInterface(name);
                    setPermissionController(Stub.asInterface(data.readStrongBinder()));
                    return true;
                } catch (RemoteException e) {
                }
            }
            return false;
        } else {
            data.enforceInterface(name);
            reply.writeStringArray(listServices(data.readInt()));
            return true;
        }
    }

    public IBinder asBinder() {
        return this;
    }
}
