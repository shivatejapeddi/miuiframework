package android.app.admin;

import android.app.admin.DevicePolicyManager.InstallSystemUpdateCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DevicePolicyManager$aBAov4sAc4DWENs1-hCXh31NAg0 implements Runnable {
    private final /* synthetic */ InstallSystemUpdateCallback f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$DevicePolicyManager$aBAov4sAc4DWENs1-hCXh31NAg0(InstallSystemUpdateCallback installSystemUpdateCallback, int i, String str) {
        this.f$0 = installSystemUpdateCallback;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.onInstallUpdateError(this.f$1, this.f$2);
    }
}
