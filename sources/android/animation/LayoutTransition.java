package android.animation;

import android.annotation.UnsupportedAppUsage;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LayoutTransition {
    private static TimeInterpolator ACCEL_DECEL_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    public static final int APPEARING = 2;
    public static final int CHANGE_APPEARING = 0;
    public static final int CHANGE_DISAPPEARING = 1;
    public static final int CHANGING = 4;
    private static TimeInterpolator DECEL_INTERPOLATOR = new DecelerateInterpolator();
    private static long DEFAULT_DURATION = 300;
    public static final int DISAPPEARING = 3;
    private static final int FLAG_APPEARING = 1;
    private static final int FLAG_CHANGE_APPEARING = 4;
    private static final int FLAG_CHANGE_DISAPPEARING = 8;
    private static final int FLAG_CHANGING = 16;
    private static final int FLAG_DISAPPEARING = 2;
    private static ObjectAnimator defaultChange;
    private static ObjectAnimator defaultChangeIn;
    private static ObjectAnimator defaultChangeOut;
    private static ObjectAnimator defaultFadeIn;
    private static ObjectAnimator defaultFadeOut;
    private static TimeInterpolator sAppearingInterpolator;
    private static TimeInterpolator sChangingAppearingInterpolator;
    private static TimeInterpolator sChangingDisappearingInterpolator;
    private static TimeInterpolator sChangingInterpolator;
    private static TimeInterpolator sDisappearingInterpolator;
    private final LinkedHashMap<View, Animator> currentAppearingAnimations;
    private final LinkedHashMap<View, Animator> currentChangingAnimations;
    private final LinkedHashMap<View, Animator> currentDisappearingAnimations;
    private final HashMap<View, OnLayoutChangeListener> layoutChangeListenerMap;
    private boolean mAnimateParentHierarchy;
    private Animator mAppearingAnim = null;
    private long mAppearingDelay;
    private long mAppearingDuration;
    private TimeInterpolator mAppearingInterpolator;
    private Animator mChangingAnim = null;
    private Animator mChangingAppearingAnim = null;
    private long mChangingAppearingDelay;
    private long mChangingAppearingDuration;
    private TimeInterpolator mChangingAppearingInterpolator;
    private long mChangingAppearingStagger;
    private long mChangingDelay;
    private Animator mChangingDisappearingAnim = null;
    private long mChangingDisappearingDelay;
    private long mChangingDisappearingDuration;
    private TimeInterpolator mChangingDisappearingInterpolator;
    private long mChangingDisappearingStagger;
    private long mChangingDuration;
    private TimeInterpolator mChangingInterpolator;
    private long mChangingStagger;
    private Animator mDisappearingAnim = null;
    private long mDisappearingDelay;
    private long mDisappearingDuration;
    private TimeInterpolator mDisappearingInterpolator;
    private ArrayList<TransitionListener> mListeners;
    private int mTransitionTypes;
    private final HashMap<View, Animator> pendingAnimations;
    private long staggerDelay;

    private static final class CleanupCallback implements OnPreDrawListener, OnAttachStateChangeListener {
        final Map<View, OnLayoutChangeListener> layoutChangeListenerMap;
        final ViewGroup parent;

        CleanupCallback(Map<View, OnLayoutChangeListener> listenerMap, ViewGroup parent) {
            this.layoutChangeListenerMap = listenerMap;
            this.parent = parent;
        }

        private void cleanup() {
            this.parent.getViewTreeObserver().removeOnPreDrawListener(this);
            this.parent.removeOnAttachStateChangeListener(this);
            if (this.layoutChangeListenerMap.size() > 0) {
                for (View view : this.layoutChangeListenerMap.keySet()) {
                    view.removeOnLayoutChangeListener((OnLayoutChangeListener) this.layoutChangeListenerMap.get(view));
                }
                this.layoutChangeListenerMap.clear();
            }
        }

        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            cleanup();
        }

        public boolean onPreDraw() {
            cleanup();
            return true;
        }
    }

    public interface TransitionListener {
        void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i);

        void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i);
    }

    static /* synthetic */ long access$214(LayoutTransition x0, long x1) {
        long j = x0.staggerDelay + x1;
        x0.staggerDelay = j;
        return j;
    }

    static {
        TimeInterpolator timeInterpolator = ACCEL_DECEL_INTERPOLATOR;
        sAppearingInterpolator = timeInterpolator;
        sDisappearingInterpolator = timeInterpolator;
        timeInterpolator = DECEL_INTERPOLATOR;
        sChangingAppearingInterpolator = timeInterpolator;
        sChangingDisappearingInterpolator = timeInterpolator;
        sChangingInterpolator = timeInterpolator;
    }

    public LayoutTransition() {
        long j = DEFAULT_DURATION;
        this.mChangingAppearingDuration = j;
        this.mChangingDisappearingDuration = j;
        this.mChangingDuration = j;
        this.mAppearingDuration = j;
        this.mDisappearingDuration = j;
        this.mAppearingDelay = j;
        this.mDisappearingDelay = 0;
        this.mChangingAppearingDelay = 0;
        this.mChangingDisappearingDelay = j;
        this.mChangingDelay = 0;
        this.mChangingAppearingStagger = 0;
        this.mChangingDisappearingStagger = 0;
        this.mChangingStagger = 0;
        this.mAppearingInterpolator = sAppearingInterpolator;
        this.mDisappearingInterpolator = sDisappearingInterpolator;
        this.mChangingAppearingInterpolator = sChangingAppearingInterpolator;
        this.mChangingDisappearingInterpolator = sChangingDisappearingInterpolator;
        this.mChangingInterpolator = sChangingInterpolator;
        this.pendingAnimations = new HashMap();
        this.currentChangingAnimations = new LinkedHashMap();
        this.currentAppearingAnimations = new LinkedHashMap();
        this.currentDisappearingAnimations = new LinkedHashMap();
        this.layoutChangeListenerMap = new HashMap();
        this.mTransitionTypes = 15;
        this.mAnimateParentHierarchy = true;
        if (defaultChangeIn == null) {
            PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
            PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
            PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
            PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
            PropertyValuesHolder pvhScrollX = PropertyValuesHolder.ofInt("scrollX", 0, 1);
            PropertyValuesHolder pvhScrollY = PropertyValuesHolder.ofInt("scrollY", 0, 1);
            defaultChangeIn = ObjectAnimator.ofPropertyValuesHolder(null, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScrollX, pvhScrollY);
            defaultChangeIn.setDuration(DEFAULT_DURATION);
            defaultChangeIn.setStartDelay(this.mChangingAppearingDelay);
            defaultChangeIn.setInterpolator(this.mChangingAppearingInterpolator);
            defaultChangeOut = defaultChangeIn.clone();
            defaultChangeOut.setStartDelay(this.mChangingDisappearingDelay);
            defaultChangeOut.setInterpolator(this.mChangingDisappearingInterpolator);
            defaultChange = defaultChangeIn.clone();
            defaultChange.setStartDelay(this.mChangingDelay);
            defaultChange.setInterpolator(this.mChangingInterpolator);
            String str = "alpha";
            defaultFadeIn = ObjectAnimator.ofFloat(null, str, 0.0f, 1.0f);
            defaultFadeIn.setDuration(DEFAULT_DURATION);
            defaultFadeIn.setStartDelay(this.mAppearingDelay);
            defaultFadeIn.setInterpolator(this.mAppearingInterpolator);
            defaultFadeOut = ObjectAnimator.ofFloat(null, str, 1.0f, 0.0f);
            defaultFadeOut.setDuration(DEFAULT_DURATION);
            defaultFadeOut.setStartDelay(this.mDisappearingDelay);
            defaultFadeOut.setInterpolator(this.mDisappearingInterpolator);
        }
        this.mChangingAppearingAnim = defaultChangeIn;
        this.mChangingDisappearingAnim = defaultChangeOut;
        this.mChangingAnim = defaultChange;
        this.mAppearingAnim = defaultFadeIn;
        this.mDisappearingAnim = defaultFadeOut;
    }

    public void setDuration(long duration) {
        this.mChangingAppearingDuration = duration;
        this.mChangingDisappearingDuration = duration;
        this.mChangingDuration = duration;
        this.mAppearingDuration = duration;
        this.mDisappearingDuration = duration;
    }

    public void enableTransitionType(int transitionType) {
        if (transitionType == 0) {
            this.mTransitionTypes = 4 | this.mTransitionTypes;
        } else if (transitionType == 1) {
            this.mTransitionTypes |= 8;
        } else if (transitionType == 2) {
            this.mTransitionTypes |= 1;
        } else if (transitionType == 3) {
            this.mTransitionTypes |= 2;
        } else if (transitionType == 4) {
            this.mTransitionTypes |= 16;
        }
    }

    public void disableTransitionType(int transitionType) {
        if (transitionType == 0) {
            this.mTransitionTypes &= -5;
        } else if (transitionType == 1) {
            this.mTransitionTypes &= -9;
        } else if (transitionType == 2) {
            this.mTransitionTypes &= -2;
        } else if (transitionType == 3) {
            this.mTransitionTypes &= -3;
        } else if (transitionType == 4) {
            this.mTransitionTypes &= -17;
        }
    }

    public boolean isTransitionTypeEnabled(int transitionType) {
        boolean z = false;
        if (transitionType == 0) {
            if ((this.mTransitionTypes & 4) == 4) {
                z = true;
            }
            return z;
        } else if (transitionType == 1) {
            if ((this.mTransitionTypes & 8) == 8) {
                z = true;
            }
            return z;
        } else if (transitionType == 2) {
            if ((this.mTransitionTypes & 1) == 1) {
                z = true;
            }
            return z;
        } else if (transitionType == 3) {
            if ((this.mTransitionTypes & 2) == 2) {
                z = true;
            }
            return z;
        } else if (transitionType != 4) {
            return false;
        } else {
            if ((this.mTransitionTypes & 16) == 16) {
                z = true;
            }
            return z;
        }
    }

    public void setStartDelay(int transitionType, long delay) {
        if (transitionType == 0) {
            this.mChangingAppearingDelay = delay;
        } else if (transitionType == 1) {
            this.mChangingDisappearingDelay = delay;
        } else if (transitionType == 2) {
            this.mAppearingDelay = delay;
        } else if (transitionType == 3) {
            this.mDisappearingDelay = delay;
        } else if (transitionType == 4) {
            this.mChangingDelay = delay;
        }
    }

    public long getStartDelay(int transitionType) {
        if (transitionType == 0) {
            return this.mChangingAppearingDelay;
        }
        if (transitionType == 1) {
            return this.mChangingDisappearingDelay;
        }
        if (transitionType == 2) {
            return this.mAppearingDelay;
        }
        if (transitionType == 3) {
            return this.mDisappearingDelay;
        }
        if (transitionType != 4) {
            return 0;
        }
        return this.mChangingDelay;
    }

    public void setDuration(int transitionType, long duration) {
        if (transitionType == 0) {
            this.mChangingAppearingDuration = duration;
        } else if (transitionType == 1) {
            this.mChangingDisappearingDuration = duration;
        } else if (transitionType == 2) {
            this.mAppearingDuration = duration;
        } else if (transitionType == 3) {
            this.mDisappearingDuration = duration;
        } else if (transitionType == 4) {
            this.mChangingDuration = duration;
        }
    }

    public long getDuration(int transitionType) {
        if (transitionType == 0) {
            return this.mChangingAppearingDuration;
        }
        if (transitionType == 1) {
            return this.mChangingDisappearingDuration;
        }
        if (transitionType == 2) {
            return this.mAppearingDuration;
        }
        if (transitionType == 3) {
            return this.mDisappearingDuration;
        }
        if (transitionType != 4) {
            return 0;
        }
        return this.mChangingDuration;
    }

    public void setStagger(int transitionType, long duration) {
        if (transitionType == 0) {
            this.mChangingAppearingStagger = duration;
        } else if (transitionType == 1) {
            this.mChangingDisappearingStagger = duration;
        } else if (transitionType == 4) {
            this.mChangingStagger = duration;
        }
    }

    public long getStagger(int transitionType) {
        if (transitionType == 0) {
            return this.mChangingAppearingStagger;
        }
        if (transitionType == 1) {
            return this.mChangingDisappearingStagger;
        }
        if (transitionType != 4) {
            return 0;
        }
        return this.mChangingStagger;
    }

    public void setInterpolator(int transitionType, TimeInterpolator interpolator) {
        if (transitionType == 0) {
            this.mChangingAppearingInterpolator = interpolator;
        } else if (transitionType == 1) {
            this.mChangingDisappearingInterpolator = interpolator;
        } else if (transitionType == 2) {
            this.mAppearingInterpolator = interpolator;
        } else if (transitionType == 3) {
            this.mDisappearingInterpolator = interpolator;
        } else if (transitionType == 4) {
            this.mChangingInterpolator = interpolator;
        }
    }

    public TimeInterpolator getInterpolator(int transitionType) {
        if (transitionType == 0) {
            return this.mChangingAppearingInterpolator;
        }
        if (transitionType == 1) {
            return this.mChangingDisappearingInterpolator;
        }
        if (transitionType == 2) {
            return this.mAppearingInterpolator;
        }
        if (transitionType == 3) {
            return this.mDisappearingInterpolator;
        }
        if (transitionType != 4) {
            return null;
        }
        return this.mChangingInterpolator;
    }

    public void setAnimator(int transitionType, Animator animator) {
        if (transitionType == 0) {
            this.mChangingAppearingAnim = animator;
        } else if (transitionType == 1) {
            this.mChangingDisappearingAnim = animator;
        } else if (transitionType == 2) {
            this.mAppearingAnim = animator;
        } else if (transitionType == 3) {
            this.mDisappearingAnim = animator;
        } else if (transitionType == 4) {
            this.mChangingAnim = animator;
        }
    }

    public Animator getAnimator(int transitionType) {
        if (transitionType == 0) {
            return this.mChangingAppearingAnim;
        }
        if (transitionType == 1) {
            return this.mChangingDisappearingAnim;
        }
        if (transitionType == 2) {
            return this.mAppearingAnim;
        }
        if (transitionType == 3) {
            return this.mDisappearingAnim;
        }
        if (transitionType != 4) {
            return null;
        }
        return this.mChangingAnim;
    }

    private void runChangeTransition(ViewGroup parent, View newView, int changeReason) {
        Animator baseAnimator;
        Animator parentAnimator;
        long duration;
        ViewGroup viewGroup = parent;
        int i = changeReason;
        Animator baseAnimator2;
        long duration2;
        if (i == 2) {
            baseAnimator2 = this.mChangingAppearingAnim;
            duration2 = this.mChangingAppearingDuration;
            baseAnimator = baseAnimator2;
            parentAnimator = defaultChangeIn;
            duration = duration2;
        } else if (i == 3) {
            baseAnimator2 = this.mChangingDisappearingAnim;
            duration2 = this.mChangingDisappearingDuration;
            baseAnimator = baseAnimator2;
            parentAnimator = defaultChangeOut;
            duration = duration2;
        } else if (i != 4) {
            baseAnimator = null;
            parentAnimator = null;
            duration = 0;
        } else {
            baseAnimator2 = this.mChangingAnim;
            duration2 = this.mChangingDuration;
            baseAnimator = baseAnimator2;
            parentAnimator = defaultChange;
            duration = duration2;
        }
        if (baseAnimator != null) {
            this.staggerDelay = 0;
            ViewTreeObserver observer = parent.getViewTreeObserver();
            if (observer.isAlive()) {
                int i2;
                int numChildren = parent.getChildCount();
                int i3 = 0;
                while (i3 < numChildren) {
                    View child = viewGroup.getChildAt(i3);
                    if (child != newView) {
                        i2 = i3;
                        setupChangeAnimation(parent, changeReason, baseAnimator, duration, child);
                    } else {
                        i2 = i3;
                    }
                    i3 = i2 + 1;
                }
                i2 = i3;
                if (this.mAnimateParentHierarchy) {
                    View tempParent = parent;
                    while (tempParent != null) {
                        View view;
                        ViewParent parentParent = tempParent.getParent();
                        if (parentParent instanceof ViewGroup) {
                            ViewParent parentParent2 = parentParent;
                            setupChangeAnimation((ViewGroup) parentParent, changeReason, parentAnimator, duration, tempParent);
                            view = (ViewGroup) parentParent2;
                        } else {
                            view = null;
                        }
                        tempParent = view;
                    }
                }
                CleanupCallback callback = new CleanupCallback(this.layoutChangeListenerMap, viewGroup);
                observer.addOnPreDrawListener(callback);
                viewGroup.addOnAttachStateChangeListener(callback);
            }
        }
    }

    public void setAnimateParentHierarchy(boolean animateParentHierarchy) {
        this.mAnimateParentHierarchy = animateParentHierarchy;
    }

    private void setupChangeAnimation(ViewGroup parent, int changeReason, Animator baseAnimator, long duration, View child) {
        final View view = child;
        if (this.layoutChangeListenerMap.get(view) == null) {
            if (child.getWidth() != 0 || child.getHeight() != 0) {
                Animator anim = baseAnimator.clone();
                anim.setTarget(view);
                anim.setupStartValues();
                Animator currentAnimation = (Animator) this.pendingAnimations.get(view);
                if (currentAnimation != null) {
                    currentAnimation.cancel();
                    this.pendingAnimations.remove(view);
                }
                this.pendingAnimations.put(view, anim);
                ValueAnimator pendingAnimRemover = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(duration + 100);
                pendingAnimRemover.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        LayoutTransition.this.pendingAnimations.remove(view);
                    }
                });
                pendingAnimRemover.start();
                final Animator animator = anim;
                final int i = changeReason;
                final long j = duration;
                final View view2 = child;
                final ViewGroup viewGroup = parent;
                OnLayoutChangeListener listener = new OnLayoutChangeListener() {
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        animator.setupEndValues();
                        ValueAnimator valueAnim = animator;
                        if (valueAnim instanceof ValueAnimator) {
                            boolean valuesDiffer = false;
                            PropertyValuesHolder[] oldValues = valueAnim.getValues();
                            for (PropertyValuesHolder pvh : oldValues) {
                                if (pvh.mKeyframes instanceof KeyframeSet) {
                                    KeyframeSet keyframeSet = pvh.mKeyframes;
                                    if (keyframeSet.mFirstKeyframe == null || keyframeSet.mLastKeyframe == null || !keyframeSet.mFirstKeyframe.getValue().equals(keyframeSet.mLastKeyframe.getValue())) {
                                        valuesDiffer = true;
                                    }
                                } else if (!pvh.mKeyframes.getValue(0.0f).equals(pvh.mKeyframes.getValue(1.0f))) {
                                    valuesDiffer = true;
                                }
                            }
                            if (!valuesDiffer) {
                                return;
                            }
                        }
                        long startDelay = 0;
                        int i = i;
                        LayoutTransition layoutTransition;
                        if (i == 2) {
                            startDelay = LayoutTransition.this.mChangingAppearingDelay + LayoutTransition.this.staggerDelay;
                            layoutTransition = LayoutTransition.this;
                            LayoutTransition.access$214(layoutTransition, layoutTransition.mChangingAppearingStagger);
                            if (LayoutTransition.this.mChangingAppearingInterpolator != LayoutTransition.sChangingAppearingInterpolator) {
                                animator.setInterpolator(LayoutTransition.this.mChangingAppearingInterpolator);
                            }
                        } else if (i == 3) {
                            startDelay = LayoutTransition.this.mChangingDisappearingDelay + LayoutTransition.this.staggerDelay;
                            layoutTransition = LayoutTransition.this;
                            LayoutTransition.access$214(layoutTransition, layoutTransition.mChangingDisappearingStagger);
                            if (LayoutTransition.this.mChangingDisappearingInterpolator != LayoutTransition.sChangingDisappearingInterpolator) {
                                animator.setInterpolator(LayoutTransition.this.mChangingDisappearingInterpolator);
                            }
                        } else if (i == 4) {
                            startDelay = LayoutTransition.this.mChangingDelay + LayoutTransition.this.staggerDelay;
                            layoutTransition = LayoutTransition.this;
                            LayoutTransition.access$214(layoutTransition, layoutTransition.mChangingStagger);
                            if (LayoutTransition.this.mChangingInterpolator != LayoutTransition.sChangingInterpolator) {
                                animator.setInterpolator(LayoutTransition.this.mChangingInterpolator);
                            }
                        }
                        animator.setStartDelay(startDelay);
                        animator.setDuration(j);
                        Animator prevAnimation = (Animator) LayoutTransition.this.currentChangingAnimations.get(view2);
                        if (prevAnimation != null) {
                            prevAnimation.cancel();
                        }
                        if (((Animator) LayoutTransition.this.pendingAnimations.get(view2)) != null) {
                            LayoutTransition.this.pendingAnimations.remove(view2);
                        }
                        LayoutTransition.this.currentChangingAnimations.put(view2, animator);
                        viewGroup.requestTransitionStart(LayoutTransition.this);
                        view2.removeOnLayoutChangeListener(this);
                        LayoutTransition.this.layoutChangeListenerMap.remove(view2);
                    }
                };
                final ViewGroup viewGroup2 = parent;
                final View view3 = child;
                final int i2 = changeReason;
                final OnLayoutChangeListener onLayoutChangeListener = listener;
                anim.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        if (LayoutTransition.this.hasListeners()) {
                            Iterator it = ((ArrayList) LayoutTransition.this.mListeners.clone()).iterator();
                            while (it.hasNext()) {
                                TransitionListener listener = (TransitionListener) it.next();
                                LayoutTransition layoutTransition = LayoutTransition.this;
                                ViewGroup viewGroup = viewGroup2;
                                View view = view3;
                                int i = i2;
                                i = i == 2 ? 0 : i == 3 ? 1 : 4;
                                listener.startTransition(layoutTransition, viewGroup, view, i);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animator) {
                        view3.removeOnLayoutChangeListener(onLayoutChangeListener);
                        LayoutTransition.this.layoutChangeListenerMap.remove(view3);
                    }

                    public void onAnimationEnd(Animator animator) {
                        LayoutTransition.this.currentChangingAnimations.remove(view3);
                        if (LayoutTransition.this.hasListeners()) {
                            Iterator it = ((ArrayList) LayoutTransition.this.mListeners.clone()).iterator();
                            while (it.hasNext()) {
                                TransitionListener listener = (TransitionListener) it.next();
                                LayoutTransition layoutTransition = LayoutTransition.this;
                                ViewGroup viewGroup = viewGroup2;
                                View view = view3;
                                int i = i2;
                                i = i == 2 ? 0 : i == 3 ? 1 : 4;
                                listener.endTransition(layoutTransition, viewGroup, view, i);
                            }
                        }
                    }
                });
                view.addOnLayoutChangeListener(listener);
                this.layoutChangeListenerMap.put(view, listener);
            }
        }
    }

    public void startChangingAnimations() {
        for (Animator anim : ((LinkedHashMap) this.currentChangingAnimations.clone()).values()) {
            if (anim instanceof ObjectAnimator) {
                ((ObjectAnimator) anim).setCurrentPlayTime(0);
            }
            anim.start();
        }
    }

    public void endChangingAnimations() {
        for (Animator anim : ((LinkedHashMap) this.currentChangingAnimations.clone()).values()) {
            anim.start();
            anim.end();
        }
        this.currentChangingAnimations.clear();
    }

    public boolean isChangingLayout() {
        return this.currentChangingAnimations.size() > 0;
    }

    public boolean isRunning() {
        return this.currentChangingAnimations.size() > 0 || this.currentAppearingAnimations.size() > 0 || this.currentDisappearingAnimations.size() > 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void cancel() {
        if (this.currentChangingAnimations.size() > 0) {
            for (Animator anim : ((LinkedHashMap) this.currentChangingAnimations.clone()).values()) {
                anim.cancel();
            }
            this.currentChangingAnimations.clear();
        }
        if (this.currentAppearingAnimations.size() > 0) {
            for (Animator anim2 : ((LinkedHashMap) this.currentAppearingAnimations.clone()).values()) {
                anim2.end();
            }
            this.currentAppearingAnimations.clear();
        }
        if (this.currentDisappearingAnimations.size() > 0) {
            for (Animator anim22 : ((LinkedHashMap) this.currentDisappearingAnimations.clone()).values()) {
                anim22.end();
            }
            this.currentDisappearingAnimations.clear();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void cancel(int transitionType) {
        if (!(transitionType == 0 || transitionType == 1)) {
            if (transitionType != 2) {
                if (transitionType != 3) {
                    if (transitionType != 4) {
                        return;
                    }
                } else if (this.currentDisappearingAnimations.size() > 0) {
                    for (Animator anim : ((LinkedHashMap) this.currentDisappearingAnimations.clone()).values()) {
                        anim.end();
                    }
                    this.currentDisappearingAnimations.clear();
                    return;
                } else {
                    return;
                }
            } else if (this.currentAppearingAnimations.size() > 0) {
                for (Animator anim2 : ((LinkedHashMap) this.currentAppearingAnimations.clone()).values()) {
                    anim2.end();
                }
                this.currentAppearingAnimations.clear();
                return;
            } else {
                return;
            }
        }
        if (this.currentChangingAnimations.size() > 0) {
            for (Animator anim22 : ((LinkedHashMap) this.currentChangingAnimations.clone()).values()) {
                anim22.cancel();
            }
            this.currentChangingAnimations.clear();
        }
    }

    private void runAppearingTransition(final ViewGroup parent, final View child) {
        Animator currentAnimation = (Animator) this.currentDisappearingAnimations.get(child);
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }
        Animator anim = this.mAppearingAnim;
        if (anim == null) {
            if (hasListeners()) {
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((TransitionListener) it.next()).endTransition(this, parent, child, 2);
                }
            }
            return;
        }
        anim = anim.clone();
        anim.setTarget(child);
        anim.setStartDelay(this.mAppearingDelay);
        anim.setDuration(this.mAppearingDuration);
        TimeInterpolator timeInterpolator = this.mAppearingInterpolator;
        if (timeInterpolator != sAppearingInterpolator) {
            anim.setInterpolator(timeInterpolator);
        }
        if (anim instanceof ObjectAnimator) {
            ((ObjectAnimator) anim).setCurrentPlayTime(0);
        }
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                LayoutTransition.this.currentAppearingAnimations.remove(child);
                if (LayoutTransition.this.hasListeners()) {
                    Iterator it = ((ArrayList) LayoutTransition.this.mListeners.clone()).iterator();
                    while (it.hasNext()) {
                        ((TransitionListener) it.next()).endTransition(LayoutTransition.this, parent, child, 2);
                    }
                }
            }
        });
        this.currentAppearingAnimations.put(child, anim);
        anim.start();
    }

    private void runDisappearingTransition(final ViewGroup parent, final View child) {
        Animator currentAnimation = (Animator) this.currentAppearingAnimations.get(child);
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }
        Animator anim = this.mDisappearingAnim;
        if (anim == null) {
            if (hasListeners()) {
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((TransitionListener) it.next()).endTransition(this, parent, child, 3);
                }
            }
            return;
        }
        anim = anim.clone();
        anim.setStartDelay(this.mDisappearingDelay);
        anim.setDuration(this.mDisappearingDuration);
        TimeInterpolator timeInterpolator = this.mDisappearingInterpolator;
        if (timeInterpolator != sDisappearingInterpolator) {
            anim.setInterpolator(timeInterpolator);
        }
        anim.setTarget(child);
        final float preAnimAlpha = child.getAlpha();
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                LayoutTransition.this.currentDisappearingAnimations.remove(child);
                child.setAlpha(preAnimAlpha);
                if (LayoutTransition.this.hasListeners()) {
                    Iterator it = ((ArrayList) LayoutTransition.this.mListeners.clone()).iterator();
                    while (it.hasNext()) {
                        ((TransitionListener) it.next()).endTransition(LayoutTransition.this, parent, child, 3);
                    }
                }
            }
        });
        if (anim instanceof ObjectAnimator) {
            ((ObjectAnimator) anim).setCurrentPlayTime(0);
        }
        this.currentDisappearingAnimations.put(child, anim);
        anim.start();
    }

    private void addChild(ViewGroup parent, View child, boolean changesLayout) {
        if (parent.getWindowVisibility() == 0) {
            if ((this.mTransitionTypes & 1) == 1) {
                cancel(3);
            }
            if (changesLayout && (this.mTransitionTypes & 4) == 4) {
                cancel(0);
                cancel(4);
            }
            if (hasListeners() && (this.mTransitionTypes & 1) == 1) {
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((TransitionListener) it.next()).startTransition(this, parent, child, 2);
                }
            }
            if (changesLayout && (this.mTransitionTypes & 4) == 4) {
                runChangeTransition(parent, child, 2);
            }
            if ((this.mTransitionTypes & 1) == 1) {
                runAppearingTransition(parent, child);
            }
        }
    }

    private boolean hasListeners() {
        ArrayList arrayList = this.mListeners;
        return arrayList != null && arrayList.size() > 0;
    }

    public void layoutChange(ViewGroup parent) {
        if (parent.getWindowVisibility() == 0 && (this.mTransitionTypes & 16) == 16 && !isRunning()) {
            runChangeTransition(parent, null, 4);
        }
    }

    public void addChild(ViewGroup parent, View child) {
        addChild(parent, child, true);
    }

    @Deprecated
    public void showChild(ViewGroup parent, View child) {
        addChild(parent, child, true);
    }

    public void showChild(ViewGroup parent, View child, int oldVisibility) {
        addChild(parent, child, oldVisibility == 8);
    }

    private void removeChild(ViewGroup parent, View child, boolean changesLayout) {
        if (parent.getWindowVisibility() == 0) {
            if ((this.mTransitionTypes & 2) == 2) {
                cancel(2);
            }
            if (changesLayout && (this.mTransitionTypes & 8) == 8) {
                cancel(1);
                cancel(4);
            }
            if (hasListeners() && (this.mTransitionTypes & 2) == 2) {
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((TransitionListener) it.next()).startTransition(this, parent, child, 3);
                }
            }
            if (changesLayout && (this.mTransitionTypes & 8) == 8) {
                runChangeTransition(parent, child, 3);
            }
            if ((this.mTransitionTypes & 2) == 2) {
                runDisappearingTransition(parent, child);
            }
        }
    }

    public void removeChild(ViewGroup parent, View child) {
        removeChild(parent, child, true);
    }

    @Deprecated
    public void hideChild(ViewGroup parent, View child) {
        removeChild(parent, child, true);
    }

    public void hideChild(ViewGroup parent, View child, int newVisibility) {
        removeChild(parent, child, newVisibility == 8);
    }

    public void addTransitionListener(TransitionListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(listener);
    }

    public void removeTransitionListener(TransitionListener listener) {
        ArrayList arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public List<TransitionListener> getTransitionListeners() {
        return this.mListeners;
    }
}
