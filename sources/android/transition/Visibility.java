package android.transition;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.Animator.AnimatorPauseListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public abstract class Visibility extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String[] sTransitionProperties = new String[]{PROPNAME_VISIBILITY, PROPNAME_PARENT};
    private int mMode = 3;
    private boolean mSuppressLayout = true;

    private static class DisappearListener extends TransitionListenerAdapter implements AnimatorListener, AnimatorPauseListener {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        public DisappearListener(View view, int finalVisibility, boolean suppressLayout) {
            this.mView = view;
            this.mFinalVisibility = finalVisibility;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = suppressLayout;
            suppressLayout(true);
        }

        public void onAnimationPause(Animator animation) {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(this.mFinalVisibility);
            }
        }

        public void onAnimationResume(Animator animation) {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(0);
            }
        }

        public void onAnimationCancel(Animator animation) {
            this.mCanceled = true;
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            hideViewWhenNotCanceled();
        }

        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean suppress) {
            if (this.mSuppressLayout && this.mLayoutSuppressed != suppress) {
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    this.mLayoutSuppressed = suppress;
                    viewGroup.suppressLayout(suppress);
                }
            }
        }
    }

    private static class VisibilityInfo {
        ViewGroup endParent;
        int endVisibility;
        boolean fadeIn;
        ViewGroup startParent;
        int startVisibility;
        boolean visibilityChange;

        private VisibilityInfo() {
        }

        /* synthetic */ VisibilityInfo(AnonymousClass1 x0) {
            this();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface VisibilityMode {
    }

    public Visibility(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VisibilityTransition);
        int mode = a.getInt(0, 0);
        a.recycle();
        if (mode != 0) {
            setMode(mode);
        }
    }

    public void setSuppressLayout(boolean suppress) {
        this.mSuppressLayout = suppress;
    }

    public void setMode(int mode) {
        if ((mode & -4) == 0) {
            this.mMode = mode;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }

    public int getMode() {
        return this.mMode;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues) {
        int visibility = transitionValues.view.getVisibility();
        transitionValues.values.put(PROPNAME_VISIBILITY, Integer.valueOf(visibility));
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] loc = new int[2];
        transitionValues.view.getLocationOnScreen(loc);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, loc);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public boolean isVisible(TransitionValues values) {
        boolean z = false;
        if (values == null) {
            return false;
        }
        View parent = (View) values.values.get(PROPNAME_PARENT);
        if (((Integer) values.values.get(PROPNAME_VISIBILITY)).intValue() == 0 && parent != null) {
            z = true;
        }
        return z;
    }

    private static VisibilityInfo getVisibilityChangeInfo(TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visInfo = new VisibilityInfo();
        visInfo.visibilityChange = false;
        visInfo.fadeIn = false;
        String str = PROPNAME_PARENT;
        String str2 = PROPNAME_VISIBILITY;
        if (startValues == null || !startValues.values.containsKey(str2)) {
            visInfo.startVisibility = -1;
            visInfo.startParent = null;
        } else {
            visInfo.startVisibility = ((Integer) startValues.values.get(str2)).intValue();
            visInfo.startParent = (ViewGroup) startValues.values.get(str);
        }
        if (endValues == null || !endValues.values.containsKey(str2)) {
            visInfo.endVisibility = -1;
            visInfo.endParent = null;
        } else {
            visInfo.endVisibility = ((Integer) endValues.values.get(str2)).intValue();
            visInfo.endParent = (ViewGroup) endValues.values.get(str);
        }
        if (startValues == null || endValues == null) {
            if (startValues == null && visInfo.endVisibility == 0) {
                visInfo.fadeIn = true;
                visInfo.visibilityChange = true;
            } else if (endValues == null && visInfo.startVisibility == 0) {
                visInfo.fadeIn = false;
                visInfo.visibilityChange = true;
            }
        } else if (visInfo.startVisibility == visInfo.endVisibility && visInfo.startParent == visInfo.endParent) {
            return visInfo;
        } else {
            if (visInfo.startVisibility != visInfo.endVisibility) {
                if (visInfo.startVisibility == 0) {
                    visInfo.fadeIn = false;
                    visInfo.visibilityChange = true;
                } else if (visInfo.endVisibility == 0) {
                    visInfo.fadeIn = true;
                    visInfo.visibilityChange = true;
                }
            } else if (visInfo.startParent != visInfo.endParent) {
                if (visInfo.endParent == null) {
                    visInfo.fadeIn = false;
                    visInfo.visibilityChange = true;
                } else if (visInfo.startParent == null) {
                    visInfo.fadeIn = true;
                    visInfo.visibilityChange = true;
                }
            }
        }
        return visInfo;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visInfo = getVisibilityChangeInfo(startValues, endValues);
        if (!visInfo.visibilityChange || (visInfo.startParent == null && visInfo.endParent == null)) {
            return null;
        }
        if (visInfo.fadeIn) {
            return onAppear(sceneRoot, startValues, visInfo.startVisibility, endValues, visInfo.endVisibility);
        }
        return onDisappear(sceneRoot, startValues, visInfo.startVisibility, endValues, visInfo.endVisibility);
    }

    public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        if ((this.mMode & 1) != 1 || endValues == null) {
            return null;
        }
        if (startValues == null) {
            View endParent = (View) endValues.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(endParent, false), getTransitionValues(endParent, false)).visibilityChange) {
                return null;
            }
        }
        return onAppear(sceneRoot, endValues.view, startValues, endValues);
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    public Animator onDisappear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        ViewGroup viewGroup = sceneRoot;
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        int i = endVisibility;
        if ((this.mMode & 2) != 2 || transitionValues == null) {
            return null;
        }
        final View startView = transitionValues.view;
        View endView = transitionValues2 != null ? transitionValues2.view : null;
        View overlayView = null;
        View viewToKeep = null;
        boolean reusingOverlayView = false;
        View savedOverlayView = (View) startView.getTag(R.id.transition_overlay_view_tag);
        if (savedOverlayView != null) {
            overlayView = savedOverlayView;
            reusingOverlayView = true;
        } else {
            boolean needOverlayForStartView = false;
            if (endView == null || endView.getParent() == null) {
                if (endView != null) {
                    overlayView = endView;
                } else {
                    needOverlayForStartView = true;
                }
            } else if (i == 4) {
                viewToKeep = endView;
            } else if (startView == endView) {
                viewToKeep = endView;
            } else {
                needOverlayForStartView = true;
            }
            if (needOverlayForStartView) {
                if (startView.getParent() == null) {
                    overlayView = startView;
                } else if (startView.getParent() instanceof View) {
                    View startParent = (View) startView.getParent();
                    TransitionValues startParentValues = getTransitionValues(startParent, true);
                    TransitionValues endParentValues = getMatchedTransitionValues(startParent, true);
                    if (getVisibilityChangeInfo(startParentValues, endParentValues).visibilityChange) {
                        int id = startParent.getId();
                        if (startParent.getParent() == null) {
                            if (!(id == -1 || viewGroup.findViewById(id) == null || !this.mCanRemoveViews)) {
                                overlayView = startView;
                            }
                        }
                    } else {
                        overlayView = TransitionUtils.copyViewImage(viewGroup, startView, startParent);
                    }
                }
            }
        }
        Animator animator;
        if (overlayView != null) {
            ViewGroupOverlay overlay;
            if (reusingOverlayView) {
                overlay = null;
            } else {
                overlay = sceneRoot.getOverlay();
                int[] screenLoc = (int[]) transitionValues.values.get(PROPNAME_SCREEN_LOCATION);
                int screenX = screenLoc[0];
                int screenY = screenLoc[1];
                int[] loc = new int[2];
                viewGroup.getLocationOnScreen(loc);
                overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
                overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
                overlay.add(overlayView);
            }
            animator = onDisappear(viewGroup, overlayView, transitionValues, transitionValues2);
            if (!reusingOverlayView) {
                if (animator == null) {
                    overlay.remove(overlayView);
                } else {
                    startView.setTagInternal(R.id.transition_overlay_view_tag, overlayView);
                    final View finalOverlayView = overlayView;
                    addListener(new TransitionListenerAdapter() {
                        public void onTransitionPause(Transition transition) {
                            overlay.remove(finalOverlayView);
                        }

                        public void onTransitionResume(Transition transition) {
                            if (finalOverlayView.getParent() == null) {
                                overlay.add(finalOverlayView);
                            } else {
                                Visibility.this.cancel();
                            }
                        }

                        public void onTransitionEnd(Transition transition) {
                            startView.setTagInternal(R.id.transition_overlay_view_tag, null);
                            overlay.remove(finalOverlayView);
                            transition.removeListener(this);
                        }
                    });
                }
            }
            return animator;
        } else if (viewToKeep == null) {
            return null;
        } else {
            int originalVisibility = viewToKeep.getVisibility();
            viewToKeep.setTransitionVisibility(0);
            animator = onDisappear(viewGroup, viewToKeep, transitionValues, transitionValues2);
            if (animator != null) {
                DisappearListener disappearListener = new DisappearListener(viewToKeep, i, this.mSuppressLayout);
                animator.addListener(disappearListener);
                animator.addPauseListener(disappearListener);
                addListener(disappearListener);
            } else {
                viewToKeep.setTransitionVisibility(originalVisibility);
            }
            return animator;
        }
    }

    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues newValues) {
        boolean z = false;
        if (startValues == null && newValues == null) {
            return false;
        }
        if (!(startValues == null || newValues == null)) {
            Map map = newValues.values;
            String str = PROPNAME_VISIBILITY;
            if (map.containsKey(str) != startValues.values.containsKey(str)) {
                return false;
            }
        }
        VisibilityInfo changeInfo = getVisibilityChangeInfo(startValues, newValues);
        if (changeInfo.visibilityChange && (changeInfo.startVisibility == 0 || changeInfo.endVisibility == 0)) {
            z = true;
        }
        return z;
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }
}
