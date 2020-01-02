package android.net.wifi.aware;

import android.util.Log;

public class PublishDiscoverySession extends DiscoverySession {
    private static final String TAG = "PublishDiscoverySession";

    public PublishDiscoverySession(WifiAwareManager manager, int clientId, int sessionId) {
        super(manager, clientId, sessionId);
    }

    public void updatePublish(PublishConfig publishConfig) {
        boolean z = this.mTerminated;
        String str = TAG;
        if (z) {
            Log.w(str, "updatePublish: called on terminated session");
            return;
        }
        WifiAwareManager mgr = (WifiAwareManager) this.mMgr.get();
        if (mgr == null) {
            Log.w(str, "updatePublish: called post GC on WifiAwareManager");
        } else {
            mgr.updatePublish(this.mClientId, this.mSessionId, publishConfig);
        }
    }
}
