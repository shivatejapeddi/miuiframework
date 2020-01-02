package miui.maml;

import android.text.TextUtils;
import miui.maml.ScreenElementRoot.OnExternCommandListener;

public class SystemCommandListener implements OnExternCommandListener {
    private static final String CLEAR_RESOURCE = "__clearResource";
    private static final String REQUEST_UPDATE = "__requestUpdate";
    private ScreenElementRoot mRoot;

    public SystemCommandListener(ScreenElementRoot r) {
        this.mRoot = r;
    }

    public void onCommand(String command, Double para1, String para2) {
        if (CLEAR_RESOURCE.equals(command)) {
            if (TextUtils.isEmpty(para2)) {
                this.mRoot.getContext().mResourceManager.clear();
            } else {
                this.mRoot.getContext().mResourceManager.clear(para2);
            }
        } else if (REQUEST_UPDATE.equals(command)) {
            this.mRoot.requestUpdate();
        }
    }
}
