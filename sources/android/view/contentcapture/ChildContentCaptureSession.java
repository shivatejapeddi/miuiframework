package android.view.contentcapture;

import android.view.autofill.AutofillId;
import android.view.contentcapture.ViewNode.ViewStructureImpl;

final class ChildContentCaptureSession extends ContentCaptureSession {
    private final ContentCaptureSession mParent;

    protected ChildContentCaptureSession(ContentCaptureSession parent, ContentCaptureContext clientContext) {
        super(clientContext);
        this.mParent = parent;
    }

    /* Access modifiers changed, original: 0000 */
    public MainContentCaptureSession getMainCaptureSession() {
        ContentCaptureSession contentCaptureSession = this.mParent;
        if (contentCaptureSession instanceof MainContentCaptureSession) {
            return (MainContentCaptureSession) contentCaptureSession;
        }
        return contentCaptureSession.getMainCaptureSession();
    }

    /* Access modifiers changed, original: 0000 */
    public ContentCaptureSession newChild(ContentCaptureContext clientContext) {
        ContentCaptureSession child = new ChildContentCaptureSession(this, clientContext);
        getMainCaptureSession().notifyChildSessionStarted(this.mId, child.mId, clientContext);
        return child;
    }

    /* Access modifiers changed, original: 0000 */
    public void flush(int reason) {
        this.mParent.flush(reason);
    }

    public void updateContentCaptureContext(ContentCaptureContext context) {
        getMainCaptureSession().notifyContextUpdated(this.mId, context);
    }

    /* Access modifiers changed, original: 0000 */
    public void onDestroy() {
        getMainCaptureSession().notifyChildSessionFinished(this.mParent.mId, this.mId);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewAppeared(ViewStructureImpl node) {
        getMainCaptureSession().notifyViewAppeared(this.mId, node);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewDisappeared(AutofillId id) {
        getMainCaptureSession().notifyViewDisappeared(this.mId, id);
    }

    /* Access modifiers changed, original: 0000 */
    public void internalNotifyViewTextChanged(AutofillId id, CharSequence text) {
        getMainCaptureSession().notifyViewTextChanged(this.mId, id, text);
    }

    public void internalNotifyViewTreeEvent(boolean started) {
        getMainCaptureSession().notifyViewTreeEvent(this.mId, started);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isContentCaptureEnabled() {
        return getMainCaptureSession().isContentCaptureEnabled();
    }
}
