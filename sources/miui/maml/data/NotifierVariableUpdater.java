package miui.maml.data;

import android.content.Context;
import android.content.Intent;
import miui.maml.NotifierManager;
import miui.maml.NotifierManager.OnNotifyListener;

public abstract class NotifierVariableUpdater extends VariableUpdater implements OnNotifyListener {
    protected NotifierManager mNotifierManager = NotifierManager.getInstance(getContext().mContext);
    private String mType;

    public abstract void onNotify(Context context, Intent intent, Object obj);

    public NotifierVariableUpdater(VariableUpdaterManager m, String type) {
        super(m);
        this.mType = type;
    }

    public void init() {
        this.mNotifierManager.acquireNotifier(this.mType, this);
    }

    public void resume() {
        this.mNotifierManager.resume(this.mType, this);
    }

    public void pause() {
        this.mNotifierManager.pause(this.mType, this);
    }

    public void finish() {
        this.mNotifierManager.releaseNotifier(this.mType, this);
    }
}
