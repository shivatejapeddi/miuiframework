package android.os.image;

import android.os.RemoteException;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DynamicSystemClient$DynSystemServiceConnection$Q-VWaYUew87mkpsE47b33p5XLa8 implements Runnable {
    private final /* synthetic */ DynSystemServiceConnection f$0;
    private final /* synthetic */ RemoteException f$1;

    public /* synthetic */ -$$Lambda$DynamicSystemClient$DynSystemServiceConnection$Q-VWaYUew87mkpsE47b33p5XLa8(DynSystemServiceConnection dynSystemServiceConnection, RemoteException remoteException) {
        this.f$0 = dynSystemServiceConnection;
        this.f$1 = remoteException;
    }

    public final void run() {
        this.f$0.lambda$onServiceConnected$0$DynamicSystemClient$DynSystemServiceConnection(this.f$1);
    }
}
