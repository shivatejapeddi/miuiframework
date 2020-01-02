package android.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.Property;
import android.util.SparseArray;
import android.view.WindowInsets.Type;
import android.view.WindowInsetsAnimationListener.InsetsAnimation;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.ArrayList;

public class InsetsController implements WindowInsetsController {
    private static final int ANIMATION_DURATION_HIDE_MS = 340;
    private static final int ANIMATION_DURATION_SHOW_MS = 275;
    private static final int DIRECTION_HIDE = 2;
    private static final int DIRECTION_NONE = 0;
    private static final int DIRECTION_SHOW = 1;
    private static final Interpolator INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    private static TypeEvaluator<Insets> sEvaluator = -$$Lambda$InsetsController$Cj7UJrCkdHvJAZ_cYKrXuTMsjz8.INSTANCE;
    private final String TAG = "InsetsControllerImpl";
    private final Runnable mAnimCallback;
    private boolean mAnimCallbackScheduled;
    private final ArrayList<InsetsAnimationControlImpl> mAnimationControls = new ArrayList();
    @AnimationDirection
    private int mAnimationDirection;
    private final Rect mFrame = new Rect();
    private WindowInsets mLastInsets;
    private final Rect mLastLegacyContentInsets = new Rect();
    private int mLastLegacySoftInputMode;
    private final Rect mLastLegacyStableInsets = new Rect();
    private int mPendingTypesToShow;
    private final SparseArray<InsetsSourceConsumer> mSourceConsumers = new SparseArray();
    private final InsetsState mState = new InsetsState();
    private final SparseArray<InsetsSourceControl> mTmpControlArray = new SparseArray();
    private final ArrayList<InsetsAnimationControlImpl> mTmpFinishedControls = new ArrayList();
    private final InsetsState mTmpState = new InsetsState();
    private final ViewRootImpl mViewRoot;

    private @interface AnimationDirection {
    }

    private static class InsetsProperty extends Property<WindowInsetsAnimationController, Insets> {
        InsetsProperty() {
            super(Insets.class, "Insets");
        }

        public Insets get(WindowInsetsAnimationController object) {
            return object.getCurrentInsets();
        }

        public void set(WindowInsetsAnimationController object, Insets value) {
            object.changeInsets(value);
        }
    }

    public InsetsController(ViewRootImpl viewRoot) {
        this.mViewRoot = viewRoot;
        this.mAnimCallback = new -$$Lambda$InsetsController$HI9QZ2HvGm6iykc-WONz2KPG61Q(this);
    }

    public /* synthetic */ void lambda$new$1$InsetsController() {
        this.mAnimCallbackScheduled = false;
        if (!this.mAnimationControls.isEmpty()) {
            this.mTmpFinishedControls.clear();
            InsetsState state = new InsetsState(this.mState, true);
            for (int i = this.mAnimationControls.size() - 1; i >= 0; i--) {
                InsetsAnimationControlImpl control = (InsetsAnimationControlImpl) this.mAnimationControls.get(i);
                if (((InsetsAnimationControlImpl) this.mAnimationControls.get(i)).applyChangeInsets(state)) {
                    this.mTmpFinishedControls.add(control);
                }
            }
            this.mViewRoot.mView.dispatchWindowInsetsAnimationProgress(state.calculateInsets(this.mFrame, this.mLastInsets.isRound(), this.mLastInsets.shouldAlwaysConsumeSystemBars(), this.mLastInsets.getDisplayCutout(), this.mLastLegacyContentInsets, this.mLastLegacyStableInsets, this.mLastLegacySoftInputMode, null));
            for (int i2 = this.mTmpFinishedControls.size() - 1; i2 >= 0; i2--) {
                dispatchAnimationFinished(((InsetsAnimationControlImpl) this.mTmpFinishedControls.get(i2)).getAnimation());
            }
        }
    }

    @VisibleForTesting
    public void onFrameChanged(Rect frame) {
        if (!this.mFrame.equals(frame)) {
            this.mViewRoot.notifyInsetsChanged();
            this.mFrame.set(frame);
        }
    }

    public InsetsState getState() {
        return this.mState;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean onStateChanged(InsetsState state) {
        if (this.mState.equals(state)) {
            return false;
        }
        this.mState.set(state);
        this.mTmpState.set(state, true);
        applyLocalVisibilityOverride();
        this.mViewRoot.notifyInsetsChanged();
        if (!this.mState.equals(this.mTmpState)) {
            sendStateToWindowManager();
        }
        return true;
    }

    @VisibleForTesting
    public WindowInsets calculateInsets(boolean isScreenRound, boolean alwaysConsumeSystemBars, DisplayCutout cutout, Rect legacyContentInsets, Rect legacyStableInsets, int legacySoftInputMode) {
        this.mLastLegacyContentInsets.set(legacyContentInsets);
        this.mLastLegacyStableInsets.set(legacyStableInsets);
        this.mLastLegacySoftInputMode = legacySoftInputMode;
        this.mLastInsets = this.mState.calculateInsets(this.mFrame, isScreenRound, alwaysConsumeSystemBars, cutout, legacyContentInsets, legacyStableInsets, legacySoftInputMode, null);
        return this.mLastInsets;
    }

    public void onControlsChanged(InsetsSourceControl[] activeControls) {
        int size;
        if (activeControls != null) {
            for (InsetsSourceControl activeControl : activeControls) {
                if (activeControl != null) {
                    this.mTmpControlArray.put(activeControl.getType(), activeControl);
                }
            }
        }
        for (size = this.mSourceConsumers.size() - 1; size >= 0; size--) {
            InsetsSourceConsumer consumer = (InsetsSourceConsumer) this.mSourceConsumers.valueAt(size);
            consumer.setControl((InsetsSourceControl) this.mTmpControlArray.get(consumer.getType()));
        }
        for (size = this.mTmpControlArray.size() - 1; size >= 0; size--) {
            InsetsSourceControl control = (InsetsSourceControl) this.mTmpControlArray.valueAt(size);
            getSourceConsumer(control.getType()).setControl(control);
        }
        this.mTmpControlArray.clear();
    }

    public void show(int types) {
        show(types, false);
    }

    private void show(int types, boolean fromIme) {
        int typesReady = 0;
        ArraySet<Integer> internalTypes = InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            InsetsSourceConsumer consumer = getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue());
            if (this.mAnimationDirection == 2) {
                cancelExistingAnimation();
            } else if (consumer.isVisible()) {
                int i2 = this.mAnimationDirection;
                if (i2 != 0) {
                    if (i2 == 2) {
                    }
                }
            }
            typesReady |= InsetsState.toPublicType(consumer.getType());
        }
        applyAnimation(typesReady, true, fromIme);
    }

    public void hide(int types) {
        int typesReady = 0;
        ArraySet<Integer> internalTypes = InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            InsetsSourceConsumer consumer = getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue());
            if (this.mAnimationDirection == 1) {
                cancelExistingAnimation();
            } else if (!consumer.isVisible()) {
                int i2 = this.mAnimationDirection;
                if (i2 != 0) {
                    if (i2 == 2) {
                    }
                }
            }
            typesReady |= InsetsState.toPublicType(consumer.getType());
        }
        applyAnimation(typesReady, false, false);
    }

    public void controlWindowInsetsAnimation(int types, WindowInsetsAnimationControlListener listener) {
        controlWindowInsetsAnimation(types, listener, false);
    }

    private void controlWindowInsetsAnimation(int types, WindowInsetsAnimationControlListener listener, boolean fromIme) {
        if (this.mState.getDisplayFrame().equals(this.mFrame)) {
            controlAnimationUnchecked(types, listener, this.mFrame, fromIme);
        } else {
            listener.onCancelled();
        }
    }

    private void controlAnimationUnchecked(int types, WindowInsetsAnimationControlListener listener, Rect frame, boolean fromIme) {
        if (types != 0) {
            cancelExistingControllers(types);
            InsetsState insetsState = this.mState;
            ArraySet<Integer> internalTypes = InsetsState.toInternalType(types);
            SparseArray<InsetsSourceConsumer> consumers = new SparseArray();
            Pair<Integer, Boolean> typesReadyPair = collectConsumers(fromIme, internalTypes, consumers);
            int typesReady = ((Integer) typesReadyPair.first).intValue();
            if (((Boolean) typesReadyPair.second).booleanValue()) {
                int typesReady2 = collectPendingConsumers(typesReady, consumers);
                if (typesReady2 == 0) {
                    listener.onCancelled();
                    return;
                }
                this.mAnimationControls.add(new InsetsAnimationControlImpl(consumers, frame, this.mState, listener, typesReady2, new -$$Lambda$InsetsController$n9dGLDW5oKSxT73i9ZlnIPWSzms(this), this));
                return;
            }
            this.mPendingTypesToShow = typesReady;
        }
    }

    public /* synthetic */ SyncRtSurfaceTransactionApplier lambda$controlAnimationUnchecked$2$InsetsController() {
        return new SyncRtSurfaceTransactionApplier(this.mViewRoot.mView);
    }

    private Pair<Integer, Boolean> collectConsumers(boolean fromIme, ArraySet<Integer> internalTypes, SparseArray<InsetsSourceConsumer> consumers) {
        int typesReady = 0;
        boolean isReady = true;
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            InsetsSourceConsumer consumer = getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue());
            if (consumer.getControl() != null) {
                if (consumer.isVisible()) {
                    consumer.notifyHidden();
                    typesReady |= InsetsState.toPublicType(consumer.getType());
                } else {
                    int requestShow = consumer.requestShow(fromIme);
                    if (requestShow == 0) {
                        typesReady |= InsetsState.toPublicType(consumer.getType());
                    } else if (requestShow == 1) {
                        isReady = false;
                    } else if (requestShow == 2) {
                        requestShow = this.mPendingTypesToShow;
                        if (requestShow != 0) {
                            this.mPendingTypesToShow = requestShow & (~InsetsState.toPublicType(10));
                        }
                    }
                }
                consumers.put(consumer.getType(), consumer);
            }
        }
        return new Pair(Integer.valueOf(typesReady), Boolean.valueOf(isReady));
    }

    private int collectPendingConsumers(int typesReady, SparseArray<InsetsSourceConsumer> consumers) {
        ArraySet<Integer> internalTypes = this.mPendingTypesToShow;
        if (internalTypes != null) {
            typesReady |= internalTypes;
            InsetsState insetsState = this.mState;
            internalTypes = InsetsState.toInternalType(internalTypes);
            for (int i = internalTypes.size() - 1; i >= 0; i--) {
                InsetsSourceConsumer consumer = getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue());
                consumers.put(consumer.getType(), consumer);
            }
            this.mPendingTypesToShow = 0;
        }
        return typesReady;
    }

    private void cancelExistingControllers(int types) {
        for (int i = this.mAnimationControls.size() - 1; i >= 0; i--) {
            InsetsAnimationControlImpl control = (InsetsAnimationControlImpl) this.mAnimationControls.get(i);
            if ((control.getTypes() & types) != 0) {
                cancelAnimation(control);
            }
        }
    }

    @VisibleForTesting
    public void notifyFinished(InsetsAnimationControlImpl controller, int shownTypes) {
        this.mAnimationControls.remove(controller);
        hideDirectly(controller.getTypes() & (~shownTypes));
        showDirectly(controller.getTypes() & shownTypes);
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyControlRevoked(InsetsSourceConsumer consumer) {
        for (int i = this.mAnimationControls.size() - 1; i >= 0; i--) {
            InsetsAnimationControlImpl control = (InsetsAnimationControlImpl) this.mAnimationControls.get(i);
            if ((control.getTypes() & InsetsState.toPublicType(consumer.getType())) != 0) {
                cancelAnimation(control);
            }
        }
    }

    private void cancelAnimation(InsetsAnimationControlImpl control) {
        control.onCancelled();
        this.mAnimationControls.remove(control);
    }

    private void applyLocalVisibilityOverride() {
        for (int i = this.mSourceConsumers.size() - 1; i >= 0; i--) {
            ((InsetsSourceConsumer) this.mSourceConsumers.valueAt(i)).applyLocalVisibilityOverride();
        }
    }

    @VisibleForTesting
    public InsetsSourceConsumer getSourceConsumer(int type) {
        InsetsSourceConsumer controller = (InsetsSourceConsumer) this.mSourceConsumers.get(type);
        if (controller != null) {
            return controller;
        }
        controller = createConsumerOfType(type);
        this.mSourceConsumers.put(type, controller);
        return controller;
    }

    @VisibleForTesting
    public void notifyVisibilityChanged() {
        this.mViewRoot.notifyInsetsChanged();
        sendStateToWindowManager();
    }

    public void onWindowFocusGained() {
        getSourceConsumer(10).onWindowFocusGained();
    }

    public void onWindowFocusLost() {
        getSourceConsumer(10).onWindowFocusLost();
    }

    /* Access modifiers changed, original: 0000 */
    public ViewRootImpl getViewRoot() {
        return this.mViewRoot;
    }

    @VisibleForTesting
    public void applyImeVisibility(boolean setVisible) {
        if (setVisible) {
            show(2, true);
        } else {
            hide(2);
        }
    }

    private InsetsSourceConsumer createConsumerOfType(int type) {
        if (type == 10) {
            return new ImeInsetsSourceConsumer(this.mState, -$$Lambda$9vBfnQOmNnsc9WU80IIatZHQGKc.INSTANCE, this);
        }
        return new InsetsSourceConsumer(type, this.mState, -$$Lambda$9vBfnQOmNnsc9WU80IIatZHQGKc.INSTANCE, this);
    }

    private void sendStateToWindowManager() {
        InsetsState tmpState = new InsetsState();
        for (int i = this.mSourceConsumers.size() - 1; i >= 0; i--) {
            InsetsSourceConsumer consumer = (InsetsSourceConsumer) this.mSourceConsumers.valueAt(i);
            if (consumer.getControl() != null) {
                tmpState.addSource(this.mState.getSource(consumer.getType()));
            }
        }
        try {
            this.mViewRoot.mWindowSession.insetsModified(this.mViewRoot.mWindow, tmpState);
        } catch (RemoteException e) {
            Log.e("InsetsControllerImpl", "Failed to call insetsModified", e);
        }
    }

    private void applyAnimation(final int types, final boolean show, boolean fromIme) {
        if (types != 0) {
            controlAnimationUnchecked(types, new WindowInsetsAnimationControlListener() {
                private ObjectAnimator mAnimator;
                private WindowInsetsAnimationController mController;

                public void onReady(WindowInsetsAnimationController controller, int types) {
                    long j;
                    this.mController = controller;
                    if (show) {
                        InsetsController.this.showDirectly(types);
                    } else {
                        InsetsController.this.hideDirectly(types);
                    }
                    Property insetsProperty = new InsetsProperty();
                    TypeEvaluator access$200 = InsetsController.sEvaluator;
                    Object[] objArr = new Insets[2];
                    objArr[0] = show ? controller.getHiddenStateInsets() : controller.getShownStateInsets();
                    objArr[1] = show ? controller.getShownStateInsets() : controller.getHiddenStateInsets();
                    this.mAnimator = ObjectAnimator.ofObject((Object) controller, insetsProperty, access$200, objArr);
                    ObjectAnimator objectAnimator = this.mAnimator;
                    if (show) {
                        j = 275;
                    } else {
                        j = 340;
                    }
                    objectAnimator.setDuration(j);
                    this.mAnimator.setInterpolator(InsetsController.INTERPOLATOR);
                    this.mAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            AnonymousClass1.this.onAnimationFinish();
                        }
                    });
                    this.mAnimator.start();
                }

                public void onCancelled() {
                    this.mAnimator.cancel();
                }

                private void onAnimationFinish() {
                    int i = 0;
                    InsetsController.this.mAnimationDirection = 0;
                    WindowInsetsAnimationController windowInsetsAnimationController = this.mController;
                    if (show) {
                        i = types;
                    }
                    windowInsetsAnimationController.finish(i);
                }
            }, this.mState.getDisplayFrame(), fromIme);
        }
    }

    private void hideDirectly(int types) {
        ArraySet<Integer> internalTypes = InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue()).hide();
        }
    }

    private void showDirectly(int types) {
        ArraySet<Integer> internalTypes = InsetsState.toInternalType(types);
        for (int i = internalTypes.size() - 1; i >= 0; i--) {
            getSourceConsumer(((Integer) internalTypes.valueAt(i)).intValue()).show();
        }
    }

    @VisibleForTesting
    public void cancelExistingAnimation() {
        cancelExistingControllers(Type.all());
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, PrintWriter pw) {
        pw.println(prefix);
        pw.println("InsetsController:");
        InsetsState insetsState = this.mState;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("  ");
        insetsState.dump(stringBuilder.toString(), pw);
    }

    @VisibleForTesting
    public void dispatchAnimationStarted(InsetsAnimation animation) {
        this.mViewRoot.mView.dispatchWindowInsetsAnimationStarted(animation);
    }

    @VisibleForTesting
    public void dispatchAnimationFinished(InsetsAnimation animation) {
        this.mViewRoot.mView.dispatchWindowInsetsAnimationFinished(animation);
    }

    @VisibleForTesting
    public void scheduleApplyChangeInsets() {
        if (!this.mAnimCallbackScheduled) {
            this.mViewRoot.mChoreographer.postCallback(2, this.mAnimCallback, null);
            this.mAnimCallbackScheduled = true;
        }
    }
}
