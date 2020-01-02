package android.location;

import android.location.ILocationManager.Stub;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import java.io.FileDescriptor;

public class MiuiLocationManagerProxy {
    private static IInvokeMonitor sInvokeMonitor;

    public interface IInvokeMonitor {
        boolean onInvoke(IBinder iBinder, int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException;
    }

    private static class ProxyBinder implements IBinder {
        private IBinder mBinder;

        public ProxyBinder(IBinder b) {
            this.mBinder = b;
        }

        public String getInterfaceDescriptor() throws RemoteException {
            return this.mBinder.getInterfaceDescriptor();
        }

        public boolean pingBinder() {
            return this.mBinder.pingBinder();
        }

        public boolean isBinderAlive() {
            return this.mBinder.isBinderAlive();
        }

        public IInterface queryLocalInterface(String descriptor) {
            return this.mBinder.queryLocalInterface(descriptor);
        }

        public void dump(FileDescriptor fd, String[] args) throws RemoteException {
            this.mBinder.dump(fd, args);
        }

        public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
            this.mBinder.dumpAsync(fd, args);
        }

        public void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
            this.mBinder.shellCommand(in, out, err, args, callback, resultReceiver);
        }

        public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            synchronized (MiuiLocationManagerProxy.class) {
                if (MiuiLocationManagerProxy.sInvokeMonitor != null) {
                    boolean onInvoke = MiuiLocationManagerProxy.sInvokeMonitor.onInvoke(this.mBinder, code, data, reply, flags);
                    return onInvoke;
                }
                return this.mBinder.transact(code, data, reply, flags);
            }
        }

        public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {
            this.mBinder.linkToDeath(recipient, flags);
        }

        public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
            return this.mBinder.unlinkToDeath(recipient, flags);
        }
    }

    public static void setInvokeMonitor(IInvokeMonitor m) {
        synchronized (MiuiLocationManagerProxy.class) {
            sInvokeMonitor = m;
        }
    }

    public static ILocationManager getProxy(ILocationManager locationManager) {
        return Stub.asInterface(new ProxyBinder(locationManager.asBinder()));
    }
}
