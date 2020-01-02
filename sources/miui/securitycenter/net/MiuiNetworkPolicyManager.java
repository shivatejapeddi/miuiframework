package miui.securitycenter.net;

import android.content.Context;
import android.net.NetworkPolicyManager;

public class MiuiNetworkPolicyManager {
    private NetworkPolicyManager mPolicyService;

    public MiuiNetworkPolicyManager(Context context) {
        this.mPolicyService = NetworkPolicyManager.from(context);
    }

    public void setRestrictBackground(boolean restrictBackground) {
        this.mPolicyService.setRestrictBackground(restrictBackground);
    }

    public boolean getRestrictBackground() {
        return this.mPolicyService.getRestrictBackground();
    }

    public void setAppRestrictBackground(int uid, boolean restrictBackground) {
        this.mPolicyService.setUidPolicy(uid, restrictBackground);
    }

    public int getAppRestrictBackground(int uid) {
        return this.mPolicyService.getUidPolicy(uid);
    }

    public boolean isAppRestrictBackground(int uid) {
        return this.mPolicyService.getUidPolicy(uid) == 1;
    }
}
