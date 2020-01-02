package android.net;

import android.net.SocketKeepalive.Callback;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.net.InetAddress;
import java.util.concurrent.Executor;

public final class NattSocketKeepalive extends SocketKeepalive {
    public static final int NATT_PORT = 4500;
    private final InetAddress mDestination;
    private final int mResourceId;
    private final InetAddress mSource;

    NattSocketKeepalive(IConnectivityManager service, Network network, ParcelFileDescriptor pfd, int resourceId, InetAddress source, InetAddress destination, Executor executor, Callback callback) {
        super(service, network, pfd, executor, callback);
        this.mSource = source;
        this.mDestination = destination;
        this.mResourceId = resourceId;
    }

    /* Access modifiers changed, original: 0000 */
    public void startImpl(int intervalSec) {
        this.mExecutor.execute(new -$$Lambda$NattSocketKeepalive$7nsE-7bVdhw33oN4gmk8WVi-r9U(this, intervalSec));
    }

    public /* synthetic */ void lambda$startImpl$0$NattSocketKeepalive(int intervalSec) {
        try {
            this.mService.startNattKeepaliveWithFd(this.mNetwork, this.mPfd.getFileDescriptor(), this.mResourceId, intervalSec, this.mCallback, this.mSource.getHostAddress(), this.mDestination.getHostAddress());
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopImpl() {
        this.mExecutor.execute(new -$$Lambda$NattSocketKeepalive$60CcdfQ34rdXme76td_j4bbtPHU(this));
    }

    public /* synthetic */ void lambda$stopImpl$1$NattSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
