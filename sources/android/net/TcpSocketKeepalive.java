package android.net;

import android.net.SocketKeepalive.Callback;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.Executor;

final class TcpSocketKeepalive extends SocketKeepalive {
    TcpSocketKeepalive(IConnectivityManager service, Network network, ParcelFileDescriptor pfd, Executor executor, Callback callback) {
        super(service, network, pfd, executor, callback);
    }

    /* Access modifiers changed, original: 0000 */
    public void startImpl(int intervalSec) {
        this.mExecutor.execute(new -$$Lambda$TcpSocketKeepalive$E1MP45uBTM6jPfrxAAqXFllEmAA(this, intervalSec));
    }

    public /* synthetic */ void lambda$startImpl$0$TcpSocketKeepalive(int intervalSec) {
        try {
            this.mService.startTcpKeepalive(this.mNetwork, this.mPfd.getFileDescriptor(), intervalSec, this.mCallback);
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stopImpl() {
        this.mExecutor.execute(new -$$Lambda$TcpSocketKeepalive$XcFd1FxqMQjF6WWgzFIVR4hV2xk(this));
    }

    public /* synthetic */ void lambda$stopImpl$1$TcpSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
