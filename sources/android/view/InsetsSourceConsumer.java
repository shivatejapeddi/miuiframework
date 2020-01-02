package android.view;

import android.view.SurfaceControl.Transaction;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;

public class InsetsSourceConsumer {
    protected final InsetsController mController;
    private InsetsSourceControl mSourceControl;
    private final InsetsState mState;
    private final Supplier<Transaction> mTransactionSupplier;
    private final int mType;
    protected boolean mVisible;

    @Retention(RetentionPolicy.SOURCE)
    @interface ShowResult {
        public static final int SHOW_DELAYED = 1;
        public static final int SHOW_FAILED = 2;
        public static final int SHOW_IMMEDIATELY = 0;
    }

    public InsetsSourceConsumer(int type, InsetsState state, Supplier<Transaction> transactionSupplier, InsetsController controller) {
        this.mType = type;
        this.mState = state;
        this.mTransactionSupplier = transactionSupplier;
        this.mController = controller;
        this.mVisible = InsetsState.getDefaultVisibility(type);
    }

    public void setControl(InsetsSourceControl control) {
        if (this.mSourceControl != control) {
            this.mSourceControl = control;
            applyHiddenToControl();
            if (applyLocalVisibilityOverride()) {
                this.mController.notifyVisibilityChanged();
            }
            if (this.mSourceControl == null) {
                this.mController.notifyControlRevoked(this);
            }
        }
    }

    @VisibleForTesting
    public InsetsSourceControl getControl() {
        return this.mSourceControl;
    }

    /* Access modifiers changed, original: 0000 */
    public int getType() {
        return this.mType;
    }

    @VisibleForTesting
    public void show() {
        setVisible(true);
    }

    @VisibleForTesting
    public void hide() {
        setVisible(false);
    }

    public void onWindowFocusGained() {
    }

    public void onWindowFocusLost() {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean applyLocalVisibilityOverride() {
        if (this.mSourceControl == null || this.mState.getSource(this.mType).isVisible() == this.mVisible) {
            return false;
        }
        this.mState.getSource(this.mType).setVisible(this.mVisible);
        return true;
    }

    @VisibleForTesting
    public boolean isVisible() {
        return this.mVisible;
    }

    /* Access modifiers changed, original: 0000 */
    public int requestShow(boolean fromController) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyHidden() {
    }

    private void setVisible(boolean visible) {
        if (this.mVisible != visible) {
            this.mVisible = visible;
            applyHiddenToControl();
            applyLocalVisibilityOverride();
            this.mController.notifyVisibilityChanged();
        }
    }

    private void applyHiddenToControl() {
        if (this.mSourceControl != null) {
            Transaction t = (Transaction) this.mTransactionSupplier.get();
            if (this.mVisible) {
                t.show(this.mSourceControl.getLeash());
            } else {
                t.hide(this.mSourceControl.getLeash());
            }
            t.apply();
        }
    }
}
