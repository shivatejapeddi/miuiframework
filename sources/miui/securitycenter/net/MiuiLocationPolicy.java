package miui.securitycenter.net;

import android.content.Context;
import android.location.LocationPolicyManager;

public class MiuiLocationPolicy {
    LocationPolicyManager mLocalPolicy;

    public MiuiLocationPolicy(Context context) {
        this.mLocalPolicy = LocationPolicyManager.from(context);
    }

    public void setAppRestrictBackground(int uid, boolean enable) {
        if (enable) {
            this.mLocalPolicy.setUidPolicy(uid, 255);
        } else {
            this.mLocalPolicy.setUidPolicy(uid, 0);
        }
    }

    public boolean getAppRestrictBackground(int uid) {
        return this.mLocalPolicy.getUidPolicy(uid) != 0;
    }
}
