package android.content;

import android.annotation.UnsupportedAppUsage;
import android.content.IOnPrimaryClipChangedListener.Stub;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;

public class ClipboardManager extends android.text.ClipboardManager {
    private final Context mContext;
    private final Handler mHandler;
    private final ArrayList<OnPrimaryClipChangedListener> mPrimaryClipChangedListeners = new ArrayList();
    private final Stub mPrimaryClipChangedServiceListener = new Stub() {
        public void dispatchPrimaryClipChanged() {
            ClipboardManager.this.mHandler.post(new -$$Lambda$ClipboardManager$1$hQk8olbGAgUi4WWNG4ZuDZsM39s(this));
        }

        public /* synthetic */ void lambda$dispatchPrimaryClipChanged$0$ClipboardManager$1() {
            ClipboardManager.this.reportPrimaryClipChanged();
        }
    };
    private final IClipboard mService;

    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }

    @UnsupportedAppUsage
    public ClipboardManager(Context context, Handler handler) throws ServiceNotFoundException {
        this.mContext = context;
        this.mHandler = handler;
        this.mService = IClipboard.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.CLIPBOARD_SERVICE));
    }

    public void setPrimaryClip(ClipData clip) {
        try {
            Preconditions.checkNotNull(clip);
            clip.prepareToLeaveProcess(true);
            this.mService.setPrimaryClip(clip, this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearPrimaryClip() {
        try {
            this.mService.clearPrimaryClip(this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ClipData getPrimaryClip() {
        try {
            return this.mService.getPrimaryClip(this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ClipDescription getPrimaryClipDescription() {
        try {
            return this.mService.getPrimaryClipDescription(this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasPrimaryClip() {
        try {
            return this.mService.hasPrimaryClip(this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            if (this.mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    this.mService.addPrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener, this.mContext.getOpPackageName(), this.mContext.getUserId());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mPrimaryClipChangedListeners.add(what);
        }
    }

    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            this.mPrimaryClipChangedListeners.remove(what);
            if (this.mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    this.mService.removePrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener, this.mContext.getOpPackageName(), this.mContext.getUserId());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Deprecated
    public CharSequence getText() {
        ClipData clip = getPrimaryClip();
        if (clip == null || clip.getItemCount() <= 0) {
            return null;
        }
        return clip.getItemAt(0).coerceToText(this.mContext);
    }

    @Deprecated
    public void setText(CharSequence text) {
        setPrimaryClip(ClipData.newPlainText(null, text));
    }

    @Deprecated
    public boolean hasText() {
        try {
            return this.mService.hasClipboardText(this.mContext.getOpPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:9:0x0015, code skipped:
            r0 = 0;
     */
    /* JADX WARNING: Missing block: B:11:0x0017, code skipped:
            if (r0 >= r1.length) goto L_0x0023;
     */
    /* JADX WARNING: Missing block: B:12:0x0019, code skipped:
            ((android.content.ClipboardManager.OnPrimaryClipChangedListener) r1[r0]).onPrimaryClipChanged();
            r0 = r0 + 1;
     */
    /* JADX WARNING: Missing block: B:13:0x0023, code skipped:
            return;
     */
    @android.annotation.UnsupportedAppUsage
    public void reportPrimaryClipChanged() {
        /*
        r3 = this;
        r0 = r3.mPrimaryClipChangedListeners;
        monitor-enter(r0);
        r1 = r3.mPrimaryClipChangedListeners;	 Catch:{ all -> 0x0024 }
        r1 = r1.size();	 Catch:{ all -> 0x0024 }
        if (r1 > 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        return;
    L_0x000d:
        r2 = r3.mPrimaryClipChangedListeners;	 Catch:{ all -> 0x0024 }
        r2 = r2.toArray();	 Catch:{ all -> 0x0024 }
        r1 = r2;
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        r0 = 0;
    L_0x0016:
        r2 = r1.length;
        if (r0 >= r2) goto L_0x0023;
    L_0x0019:
        r2 = r1[r0];
        r2 = (android.content.ClipboardManager.OnPrimaryClipChangedListener) r2;
        r2.onPrimaryClipChanged();
        r0 = r0 + 1;
        goto L_0x0016;
    L_0x0023:
        return;
    L_0x0024:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0024 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.ClipboardManager.reportPrimaryClipChanged():void");
    }
}
