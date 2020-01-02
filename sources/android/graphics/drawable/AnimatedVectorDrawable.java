package android.graphics.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.PropertyValuesHolder.PropertyValues;
import android.animation.PropertyValuesHolder.PropertyValues.DataSource;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.LongArray;
import android.util.PathParser.PathData;
import android.util.Property;
import android.util.TimeUtils;
import android.view.Choreographer;
import android.view.NativeVectorDrawableAnimator;
import android.view.RenderNodeAnimatorSetHelper;
import com.android.internal.R;
import com.android.internal.util.VirtualRefBasePtr;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawable extends Drawable implements Animatable2 {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVectorDrawable";
    private static final String TARGET = "target";
    @UnsupportedAppUsage
    private AnimatedVectorDrawableState mAnimatedVectorState;
    private ArrayList<AnimationCallback> mAnimationCallbacks;
    private AnimatorListener mAnimatorListener;
    @UnsupportedAppUsage
    private VectorDrawableAnimator mAnimatorSet;
    private AnimatorSet mAnimatorSetFromXml;
    private final Callback mCallback;
    private boolean mMutated;
    private Resources mRes;

    private static class AnimatedVectorDrawableState extends ConstantState {
        ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayList<PendingAnimator> mPendingAnims;
        private final boolean mShouldIgnoreInvalidAnim = AnimatedVectorDrawable.shouldIgnoreInvalidAnimation();
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawable mVectorDrawable;

        private static class PendingAnimator {
            public final int animResId;
            public final float pathErrorScale;
            public final String target;

            public PendingAnimator(int animResId, float pathErrorScale, String target) {
                this.animResId = animResId;
                this.pathErrorScale = pathErrorScale;
                this.target = target;
            }

            public Animator newInstance(Resources res, Theme theme) {
                return AnimatorInflater.loadAnimator(res, theme, this.animResId, this.pathErrorScale);
            }
        }

        public AnimatedVectorDrawableState(AnimatedVectorDrawableState copy, Callback owner, Resources res) {
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                ConstantState cs = copy.mVectorDrawable;
                if (cs != null) {
                    cs = cs.getConstantState();
                    if (res != null) {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable(res);
                    } else {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable();
                    }
                    this.mVectorDrawable = (VectorDrawable) this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback(owner);
                    this.mVectorDrawable.setLayoutDirection(copy.mVectorDrawable.getLayoutDirection());
                    this.mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                ArrayList arrayList = copy.mAnimators;
                if (arrayList != null) {
                    this.mAnimators = new ArrayList(arrayList);
                }
                ArrayMap arrayMap = copy.mTargetNameMap;
                if (arrayMap != null) {
                    this.mTargetNameMap = new ArrayMap(arrayMap);
                }
                arrayList = copy.mPendingAnims;
                if (arrayList != null) {
                    this.mPendingAnims = new ArrayList(arrayList);
                    return;
                }
                return;
            }
            this.mVectorDrawable = new VectorDrawable();
        }

        public boolean canApplyTheme() {
            VectorDrawable vectorDrawable = this.mVectorDrawable;
            return (vectorDrawable != null && vectorDrawable.canApplyTheme()) || this.mPendingAnims != null || super.canApplyTheme();
        }

        public Drawable newDrawable() {
            return new AnimatedVectorDrawable(this, null, null);
        }

        public Drawable newDrawable(Resources res) {
            return new AnimatedVectorDrawable(this, res, null);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public void addPendingAnimator(int resId, float pathErrorScale, String target) {
            if (this.mPendingAnims == null) {
                this.mPendingAnims = new ArrayList(1);
            }
            this.mPendingAnims.add(new PendingAnimator(resId, pathErrorScale, target));
        }

        public void addTargetAnimator(String targetName, Animator animator) {
            if (this.mAnimators == null) {
                this.mAnimators = new ArrayList(1);
                this.mTargetNameMap = new ArrayMap(1);
            }
            this.mAnimators.add(animator);
            this.mTargetNameMap.put(animator, targetName);
        }

        public void prepareLocalAnimators(AnimatorSet animatorSet, Resources res) {
            if (this.mPendingAnims != null) {
                if (res != null) {
                    inflatePendingAnimators(res, null);
                } else {
                    Log.e(AnimatedVectorDrawable.LOGTAG, "Failed to load animators. Either the AnimatedVectorDrawable must be created using a Resources object or applyTheme() must be called with a non-null Theme object.");
                }
                this.mPendingAnims = null;
            }
            ArrayList arrayList = this.mAnimators;
            int count = arrayList == null ? 0 : arrayList.size();
            if (count > 0) {
                Builder builder = animatorSet.play(prepareLocalAnimator(0));
                for (int i = 1; i < count; i++) {
                    builder.with(prepareLocalAnimator(i));
                }
            }
        }

        private Animator prepareLocalAnimator(int index) {
            Animator animator = (Animator) this.mAnimators.get(index);
            Animator localAnimator = animator.clone();
            String targetName = (String) this.mTargetNameMap.get(animator);
            Object target = this.mVectorDrawable.getTargetByName(targetName);
            if (!this.mShouldIgnoreInvalidAnim) {
                StringBuilder stringBuilder;
                if (target == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Target with the name \"");
                    stringBuilder.append(targetName);
                    stringBuilder.append("\" cannot be found in the VectorDrawable to be animated.");
                    throw new IllegalStateException(stringBuilder.toString());
                } else if (!((target instanceof VectorDrawableState) || (target instanceof VObject))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Target should be either VGroup, VPath, or ConstantState, ");
                    stringBuilder.append(target.getClass());
                    stringBuilder.append(" is not supported");
                    throw new UnsupportedOperationException(stringBuilder.toString());
                }
            }
            localAnimator.setTarget(target);
            return localAnimator;
        }

        public void inflatePendingAnimators(Resources res, Theme t) {
            ArrayList<PendingAnimator> pendingAnims = this.mPendingAnims;
            if (pendingAnims != null) {
                this.mPendingAnims = null;
                int count = pendingAnims.size();
                for (int i = 0; i < count; i++) {
                    PendingAnimator pendingAnimator = (PendingAnimator) pendingAnims.get(i);
                    Animator animator = pendingAnimator.newInstance(res, t);
                    AnimatedVectorDrawable.updateAnimatorProperty(animator, pendingAnimator.target, this.mVectorDrawable, this.mShouldIgnoreInvalidAnim);
                    addTargetAnimator(pendingAnimator.target, animator);
                }
            }
        }
    }

    private interface VectorDrawableAnimator {
        boolean canReverse();

        void end();

        void init(AnimatorSet animatorSet);

        boolean isInfinite();

        boolean isRunning();

        boolean isStarted();

        void onDraw(Canvas canvas);

        void pause();

        void removeListener(AnimatorListener animatorListener);

        void reset();

        void resume();

        void reverse();

        void setListener(AnimatorListener animatorListener);

        void start();
    }

    public static class VectorDrawableAnimatorRT implements VectorDrawableAnimator, NativeVectorDrawableAnimator {
        private static final int END_ANIMATION = 4;
        private static final int MAX_SAMPLE_POINTS = 300;
        private static final int RESET_ANIMATION = 3;
        private static final int REVERSE_ANIMATION = 2;
        private static final int START_ANIMATION = 1;
        private boolean mContainsSequentialAnimators = false;
        private final AnimatedVectorDrawable mDrawable;
        private Handler mHandler;
        private boolean mInitialized = false;
        private boolean mIsInfinite = false;
        private boolean mIsReversible = false;
        private int mLastListenerId = 0;
        private WeakReference<RenderNode> mLastSeenTarget = null;
        private AnimatorListener mListener = null;
        private final IntArray mPendingAnimationActions = new IntArray();
        private long mSetPtr = 0;
        private final VirtualRefBasePtr mSetRefBasePtr;
        private final LongArray mStartDelays = new LongArray();
        private boolean mStarted = false;
        private PropertyValues mTmpValues = new PropertyValues();

        VectorDrawableAnimatorRT(AnimatedVectorDrawable drawable) {
            this.mDrawable = drawable;
            this.mSetPtr = AnimatedVectorDrawable.nCreateAnimatorSet();
            this.mSetRefBasePtr = new VirtualRefBasePtr(this.mSetPtr);
        }

        public void init(AnimatorSet set) {
            if (this.mInitialized) {
                throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
            }
            parseAnimatorSet(set, 0);
            AnimatedVectorDrawable.nSetVectorDrawableTarget(this.mSetPtr, this.mDrawable.mAnimatedVectorState.mVectorDrawable.getNativeTree());
            this.mInitialized = true;
            this.mIsInfinite = set.getTotalDuration() == -1;
            this.mIsReversible = true;
            if (this.mContainsSequentialAnimators) {
                this.mIsReversible = false;
            } else {
                for (int i = 0; i < this.mStartDelays.size(); i++) {
                    if (this.mStartDelays.get(i) > 0) {
                        this.mIsReversible = false;
                        return;
                    }
                }
            }
        }

        private void parseAnimatorSet(AnimatorSet set, long startTime) {
            ArrayList<Animator> animators = set.getChildAnimations();
            boolean playTogether = set.shouldPlayTogether();
            for (int i = 0; i < animators.size(); i++) {
                Animator animator = (Animator) animators.get(i);
                if (animator instanceof AnimatorSet) {
                    parseAnimatorSet((AnimatorSet) animator, startTime);
                } else if (animator instanceof ObjectAnimator) {
                    createRTAnimator((ObjectAnimator) animator, startTime);
                }
                if (!playTogether) {
                    startTime += animator.getTotalDuration();
                    this.mContainsSequentialAnimators = true;
                }
            }
        }

        private void createRTAnimator(ObjectAnimator animator, long startTime) {
            PropertyValuesHolder[] values = animator.getValues();
            Object target = animator.getTarget();
            if (target instanceof VGroup) {
                createRTAnimatorForGroup(values, animator, (VGroup) target, startTime);
            } else if (target instanceof VPath) {
                for (PropertyValuesHolder propertyValues : values) {
                    propertyValues.getPropertyValues(this.mTmpValues);
                    if ((this.mTmpValues.endValue instanceof PathData) && this.mTmpValues.propertyName.equals("pathData")) {
                        createRTAnimatorForPath(animator, (VPath) target, startTime);
                    } else if (target instanceof VFullPath) {
                        createRTAnimatorForFullPath(animator, (VFullPath) target, startTime);
                    } else if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                        throw new IllegalArgumentException("ClipPath only supports PathData property");
                    }
                }
            } else if (target instanceof VectorDrawableState) {
                createRTAnimatorForRootGroup(values, animator, (VectorDrawableState) target, startTime);
            }
        }

        private void createRTAnimatorForGroup(PropertyValuesHolder[] values, ObjectAnimator animator, VGroup target, long startTime) {
            PropertyValuesHolder[] propertyValuesHolderArr = values;
            long nativePtr = target.getNativePtr();
            for (PropertyValuesHolder propertyValues : propertyValuesHolderArr) {
                propertyValues.getPropertyValues(this.mTmpValues);
                int propertyId = VGroup.getPropertyIndex(this.mTmpValues.propertyName);
                if ((this.mTmpValues.type == Float.class || this.mTmpValues.type == Float.TYPE) && propertyId >= 0) {
                    long propertyPtr = AnimatedVectorDrawable.nCreateGroupPropertyHolder(nativePtr, propertyId, ((Float) this.mTmpValues.startValue).floatValue(), ((Float) this.mTmpValues.endValue).floatValue());
                    if (this.mTmpValues.dataSource != null) {
                        float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                        AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                    }
                    createNativeChildAnimator(propertyPtr, startTime, animator);
                }
            }
        }

        private void createRTAnimatorForPath(ObjectAnimator animator, VPath target, long startTime) {
            createNativeChildAnimator(AnimatedVectorDrawable.nCreatePathDataPropertyHolder(target.getNativePtr(), ((PathData) this.mTmpValues.startValue).getNativePtr(), ((PathData) this.mTmpValues.endValue).getNativePtr()), startTime, animator);
        }

        private void createRTAnimatorForFullPath(ObjectAnimator animator, VFullPath target, long startTime) {
            long propertyPtr;
            VFullPath vFullPath = target;
            int propertyId = target.getPropertyIndex(this.mTmpValues.propertyName);
            long nativePtr = target.getNativePtr();
            StringBuilder stringBuilder;
            if (this.mTmpValues.type == Float.class || this.mTmpValues.type == Float.TYPE) {
                if (propertyId >= 0) {
                    propertyPtr = AnimatedVectorDrawable.nCreatePathPropertyHolder(nativePtr, propertyId, ((Float) this.mTmpValues.startValue).floatValue(), ((Float) this.mTmpValues.endValue).floatValue());
                    if (this.mTmpValues.dataSource != null) {
                        float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                        AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                    }
                } else if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Property: ");
                    stringBuilder.append(this.mTmpValues.propertyName);
                    stringBuilder.append(" is not supported for FullPath");
                    throw new IllegalArgumentException(stringBuilder.toString());
                } else {
                    return;
                }
            } else if (this.mTmpValues.type == Integer.class || this.mTmpValues.type == Integer.TYPE) {
                propertyPtr = AnimatedVectorDrawable.nCreatePathColorPropertyHolder(nativePtr, propertyId, ((Integer) this.mTmpValues.startValue).intValue(), ((Integer) this.mTmpValues.endValue).intValue());
                if (this.mTmpValues.dataSource != null) {
                    int[] dataPoints2 = createIntDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                    AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints2, dataPoints2.length);
                }
            } else if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported type: ");
                stringBuilder.append(this.mTmpValues.type);
                stringBuilder.append(". Only float, int or PathData value is supported for Paths.");
                throw new UnsupportedOperationException(stringBuilder.toString());
            } else {
                return;
            }
            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        private void createRTAnimatorForRootGroup(PropertyValuesHolder[] values, ObjectAnimator animator, VectorDrawableState target, long startTime) {
            PropertyValuesHolder[] propertyValuesHolderArr = values;
            long nativePtr = target.getNativeRenderer();
            String str = "alpha";
            if (animator.getPropertyName().equals(str)) {
                Float startValue;
                Float endValue;
                for (PropertyValuesHolder propertyValues : propertyValuesHolderArr) {
                    propertyValues.getPropertyValues(this.mTmpValues);
                    if (this.mTmpValues.propertyName.equals(str)) {
                        startValue = this.mTmpValues.startValue;
                        endValue = this.mTmpValues.endValue;
                        break;
                    }
                }
                startValue = null;
                endValue = null;
                if (startValue != null || endValue != null) {
                    long propertyPtr = AnimatedVectorDrawable.nCreateRootAlphaPropertyHolder(nativePtr, startValue.floatValue(), endValue.floatValue());
                    if (this.mTmpValues.dataSource != null) {
                        float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                        AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                    }
                    createNativeChildAnimator(propertyPtr, startTime, animator);
                } else if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    throw new UnsupportedOperationException("No alpha values are specified");
                }
            } else if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                throw new UnsupportedOperationException("Only alpha is supported for root group");
            }
        }

        private static int getFrameCount(long duration) {
            int numAnimFrames = Math.max(2, (int) Math.ceil(((double) duration) / ((double) ((int) (Choreographer.getInstance().getFrameIntervalNanos() / TimeUtils.NANOS_PER_MS)))));
            if (numAnimFrames <= 300) {
                return numAnimFrames;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Duration for the animation is too long :");
            stringBuilder.append(duration);
            stringBuilder.append(", the animation will subsample the keyframe or path data.");
            Log.w(AnimatedVectorDrawable.LOGTAG, stringBuilder.toString());
            return 300;
        }

        private static float[] createFloatDataPoints(DataSource dataSource, long duration) {
            int numAnimFrames = getFrameCount(duration);
            float[] values = new float[numAnimFrames];
            float lastFrame = (float) (numAnimFrames - 1);
            for (int i = 0; i < numAnimFrames; i++) {
                values[i] = ((Float) dataSource.getValueAtFraction(((float) i) / lastFrame)).floatValue();
            }
            return values;
        }

        private static int[] createIntDataPoints(DataSource dataSource, long duration) {
            int numAnimFrames = getFrameCount(duration);
            int[] values = new int[numAnimFrames];
            float lastFrame = (float) (numAnimFrames - 1);
            for (int i = 0; i < numAnimFrames; i++) {
                values[i] = ((Integer) dataSource.getValueAtFraction(((float) i) / lastFrame)).intValue();
            }
            return values;
        }

        private void createNativeChildAnimator(long propertyPtr, long extraDelay, ObjectAnimator animator) {
            long duration = animator.getDuration();
            int repeatCount = animator.getRepeatCount();
            long startDelay = extraDelay + animator.getStartDelay();
            TimeInterpolator interpolator = animator.getInterpolator();
            long nativeInterpolator = RenderNodeAnimatorSetHelper.createNativeInterpolator(interpolator, duration);
            long startDelay2 = (long) (((float) startDelay) * ValueAnimator.getDurationScale());
            duration = (long) (((float) duration) * ValueAnimator.getDurationScale());
            this.mStartDelays.add(startDelay2);
            AnimatedVectorDrawable.nAddAnimator(this.mSetPtr, propertyPtr, nativeInterpolator, startDelay2, duration, repeatCount, animator.getRepeatMode());
        }

        /* Access modifiers changed, original: protected */
        public void recordLastSeenTarget(RecordingCanvas canvas) {
            RenderNode node = RenderNodeAnimatorSetHelper.getTarget(canvas);
            this.mLastSeenTarget = new WeakReference(node);
            if ((this.mInitialized || this.mPendingAnimationActions.size() > 0) && useTarget(node)) {
                for (int i = 0; i < this.mPendingAnimationActions.size(); i++) {
                    handlePendingAction(this.mPendingAnimationActions.get(i));
                }
                this.mPendingAnimationActions.clear();
            }
        }

        private void handlePendingAction(int pendingAnimationAction) {
            if (pendingAnimationAction == 1) {
                startAnimation();
            } else if (pendingAnimationAction == 2) {
                reverseAnimation();
            } else if (pendingAnimationAction == 3) {
                resetAnimation();
            } else if (pendingAnimationAction == 4) {
                endAnimation();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Animation action ");
                stringBuilder.append(pendingAnimationAction);
                stringBuilder.append("is not supported");
                throw new UnsupportedOperationException(stringBuilder.toString());
            }
        }

        private boolean useLastSeenTarget() {
            WeakReference weakReference = this.mLastSeenTarget;
            if (weakReference != null) {
                return useTarget((RenderNode) weakReference.get());
            }
            return false;
        }

        private boolean useTarget(RenderNode target) {
            if (target == null || !target.isAttached()) {
                return false;
            }
            target.registerVectorDrawableAnimator(this);
            return true;
        }

        private void invalidateOwningView() {
            this.mDrawable.invalidateSelf();
        }

        private void addPendingAction(int pendingAnimationAction) {
            invalidateOwningView();
            this.mPendingAnimationActions.add(pendingAnimationAction);
        }

        public void start() {
            if (this.mInitialized) {
                if (useLastSeenTarget()) {
                    startAnimation();
                } else {
                    addPendingAction(1);
                }
            }
        }

        public void end() {
            if (this.mInitialized) {
                if (useLastSeenTarget()) {
                    endAnimation();
                } else {
                    addPendingAction(4);
                }
            }
        }

        public void reset() {
            if (this.mInitialized) {
                if (useLastSeenTarget()) {
                    resetAnimation();
                } else {
                    addPendingAction(3);
                }
            }
        }

        public void reverse() {
            if (this.mIsReversible && this.mInitialized) {
                if (useLastSeenTarget()) {
                    reverseAnimation();
                } else {
                    addPendingAction(2);
                }
            }
        }

        private void startAnimation() {
            this.mStarted = true;
            if (this.mHandler == null) {
                this.mHandler = new Handler();
            }
            long j = this.mSetPtr;
            int i = this.mLastListenerId + 1;
            this.mLastListenerId = i;
            AnimatedVectorDrawable.nStart(j, this, i);
            invalidateOwningView();
            AnimatorListener animatorListener = this.mListener;
            if (animatorListener != null) {
                animatorListener.onAnimationStart(null);
            }
        }

        private void endAnimation() {
            AnimatedVectorDrawable.nEnd(this.mSetPtr);
            invalidateOwningView();
        }

        private void resetAnimation() {
            AnimatedVectorDrawable.nReset(this.mSetPtr);
            invalidateOwningView();
        }

        private void reverseAnimation() {
            this.mStarted = true;
            long j = this.mSetPtr;
            int i = this.mLastListenerId + 1;
            this.mLastListenerId = i;
            AnimatedVectorDrawable.nReverse(j, this, i);
            invalidateOwningView();
            AnimatorListener animatorListener = this.mListener;
            if (animatorListener != null) {
                animatorListener.onAnimationStart(null);
            }
        }

        public long getAnimatorNativePtr() {
            return this.mSetPtr;
        }

        public boolean canReverse() {
            return this.mIsReversible;
        }

        public boolean isStarted() {
            return this.mStarted;
        }

        public boolean isRunning() {
            if (this.mInitialized) {
                return this.mStarted;
            }
            return false;
        }

        public void setListener(AnimatorListener listener) {
            this.mListener = listener;
        }

        public void removeListener(AnimatorListener listener) {
            this.mListener = null;
        }

        public void onDraw(Canvas canvas) {
            if (canvas.isHardwareAccelerated()) {
                recordLastSeenTarget((RecordingCanvas) canvas);
            }
        }

        public boolean isInfinite() {
            return this.mIsInfinite;
        }

        public void pause() {
        }

        public void resume() {
        }

        private void onAnimationEnd(int listenerId) {
            if (listenerId == this.mLastListenerId) {
                this.mStarted = false;
                invalidateOwningView();
                AnimatorListener animatorListener = this.mListener;
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(null);
                }
            }
        }

        @UnsupportedAppUsage
        private static void callOnFinished(VectorDrawableAnimatorRT set, int id) {
            set.mHandler.post(new -$$Lambda$AnimatedVectorDrawable$VectorDrawableAnimatorRT$PzjgSeyQweoFjbEZJP80UteZqm8(set, id));
        }

        private void transferPendingActions(VectorDrawableAnimator animatorSet) {
            for (int i = 0; i < this.mPendingAnimationActions.size(); i++) {
                int pendingAction = this.mPendingAnimationActions.get(i);
                if (pendingAction == 1) {
                    animatorSet.start();
                } else if (pendingAction == 4) {
                    animatorSet.end();
                } else if (pendingAction == 2) {
                    animatorSet.reverse();
                } else if (pendingAction == 3) {
                    animatorSet.reset();
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Animation action ");
                    stringBuilder.append(pendingAction);
                    stringBuilder.append("is not supported");
                    throw new UnsupportedOperationException(stringBuilder.toString());
                }
            }
            this.mPendingAnimationActions.clear();
        }
    }

    private static class VectorDrawableAnimatorUI implements VectorDrawableAnimator {
        private final Drawable mDrawable;
        private boolean mIsInfinite = false;
        private ArrayList<AnimatorListener> mListenerArray = null;
        private AnimatorSet mSet = null;

        VectorDrawableAnimatorUI(AnimatedVectorDrawable drawable) {
            this.mDrawable = drawable;
        }

        public void init(AnimatorSet set) {
            if (this.mSet == null) {
                this.mSet = set.clone();
                this.mIsInfinite = this.mSet.getTotalDuration() == -1;
                ArrayList arrayList = this.mListenerArray;
                if (arrayList != null && !arrayList.isEmpty()) {
                    for (int i = 0; i < this.mListenerArray.size(); i++) {
                        this.mSet.addListener((AnimatorListener) this.mListenerArray.get(i));
                    }
                    this.mListenerArray.clear();
                    this.mListenerArray = null;
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
        }

        public void start() {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null && !animatorSet.isStarted()) {
                this.mSet.start();
                invalidateOwningView();
            }
        }

        public void end() {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null) {
                animatorSet.end();
            }
        }

        public void reset() {
            if (this.mSet != null) {
                start();
                this.mSet.cancel();
            }
        }

        public void reverse() {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null) {
                animatorSet.reverse();
                invalidateOwningView();
            }
        }

        public boolean canReverse() {
            AnimatorSet animatorSet = this.mSet;
            return animatorSet != null && animatorSet.canReverse();
        }

        public void setListener(AnimatorListener listener) {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet == null) {
                if (this.mListenerArray == null) {
                    this.mListenerArray = new ArrayList();
                }
                this.mListenerArray.add(listener);
                return;
            }
            animatorSet.addListener(listener);
        }

        public void removeListener(AnimatorListener listener) {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet == null) {
                ArrayList arrayList = this.mListenerArray;
                if (arrayList != null) {
                    arrayList.remove(listener);
                } else {
                    return;
                }
            }
            animatorSet.removeListener(listener);
        }

        public void onDraw(Canvas canvas) {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null && animatorSet.isStarted()) {
                invalidateOwningView();
            }
        }

        public boolean isStarted() {
            AnimatorSet animatorSet = this.mSet;
            return animatorSet != null && animatorSet.isStarted();
        }

        public boolean isRunning() {
            AnimatorSet animatorSet = this.mSet;
            return animatorSet != null && animatorSet.isRunning();
        }

        public boolean isInfinite() {
            return this.mIsInfinite;
        }

        public void pause() {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null) {
                animatorSet.pause();
            }
        }

        public void resume() {
            AnimatorSet animatorSet = this.mSet;
            if (animatorSet != null) {
                animatorSet.resume();
            }
        }

        private void invalidateOwningView() {
            this.mDrawable.invalidateSelf();
        }
    }

    private static native void nAddAnimator(long j, long j2, long j3, long j4, long j5, int i, int i2);

    private static native long nCreateAnimatorSet();

    private static native long nCreateGroupPropertyHolder(long j, int i, float f, float f2);

    private static native long nCreatePathColorPropertyHolder(long j, int i, int i2, int i3);

    private static native long nCreatePathDataPropertyHolder(long j, long j2, long j3);

    private static native long nCreatePathPropertyHolder(long j, int i, float f, float f2);

    private static native long nCreateRootAlphaPropertyHolder(long j, float f, float f2);

    private static native void nEnd(long j);

    private static native void nReset(long j);

    private static native void nReverse(long j, VectorDrawableAnimatorRT vectorDrawableAnimatorRT, int i);

    private static native void nSetPropertyHolderData(long j, float[] fArr, int i);

    private static native void nSetPropertyHolderData(long j, int[] iArr, int i);

    private static native void nSetVectorDrawableTarget(long j, long j2);

    private static native void nStart(long j, VectorDrawableAnimatorRT vectorDrawableAnimatorRT, int i);

    /* synthetic */ AnimatedVectorDrawable(AnimatedVectorDrawableState x0, Resources x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    public AnimatedVectorDrawable() {
        this(null, null);
    }

    private AnimatedVectorDrawable(AnimatedVectorDrawableState state, Resources res) {
        this.mAnimatorSetFromXml = null;
        this.mAnimationCallbacks = null;
        this.mAnimatorListener = null;
        this.mCallback = new Callback() {
            public void invalidateDrawable(Drawable who) {
                AnimatedVectorDrawable.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable who, Runnable what, long when) {
                AnimatedVectorDrawable.this.scheduleSelf(what, when);
            }

            public void unscheduleDrawable(Drawable who, Runnable what) {
                AnimatedVectorDrawable.this.unscheduleSelf(what);
            }
        };
        this.mAnimatedVectorState = new AnimatedVectorDrawableState(state, this.mCallback, res);
        this.mAnimatorSet = new VectorDrawableAnimatorRT(this);
        this.mRes = res;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mAnimatedVectorState = new AnimatedVectorDrawableState(this.mAnimatedVectorState, this.mCallback, this.mRes);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        if (this.mAnimatedVectorState.mVectorDrawable != null) {
            this.mAnimatedVectorState.mVectorDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    /* JADX WARNING: Missing block: B:9:0x001b, code skipped:
            return true;
     */
    private static boolean shouldIgnoreInvalidAnimation() {
        /*
        r0 = android.app.ActivityThread.currentApplication();
        r1 = 1;
        if (r0 == 0) goto L_0x001b;
    L_0x0007:
        r2 = r0.getApplicationInfo();
        if (r2 != 0) goto L_0x000e;
    L_0x000d:
        goto L_0x001b;
    L_0x000e:
        r2 = r0.getApplicationInfo();
        r2 = r2.targetSdkVersion;
        r3 = 24;
        if (r2 >= r3) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r1 = 0;
        return r1;
    L_0x001b:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.AnimatedVectorDrawable.shouldIgnoreInvalidAnimation():boolean");
    }

    public ConstantState getConstantState() {
        this.mAnimatedVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mAnimatedVectorState;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mAnimatedVectorState.getChangingConfigurations();
    }

    public void draw(Canvas canvas) {
        if (!canvas.isHardwareAccelerated()) {
            VectorDrawableAnimator vectorDrawableAnimator = this.mAnimatorSet;
            if ((vectorDrawableAnimator instanceof VectorDrawableAnimatorRT) && !vectorDrawableAnimator.isRunning() && ((VectorDrawableAnimatorRT) this.mAnimatorSet).mPendingAnimationActions.size() > 0) {
                fallbackOntoUI();
            }
        }
        this.mAnimatorSet.onDraw(canvas);
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
        this.mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] state) {
        return this.mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int level) {
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mAnimatedVectorState.mVectorDrawable.setLayoutDirection(layoutDirection);
    }

    public int getAlpha() {
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public void setAlpha(int alpha) {
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    public ColorFilter getColorFilter() {
        return this.mAnimatedVectorState.mVectorDrawable.getColorFilter();
    }

    public void setTintList(ColorStateList tint) {
        this.mAnimatedVectorState.mVectorDrawable.setTintList(tint);
    }

    public void setHotspot(float x, float y) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspot(x, y);
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspotBounds(left, top, right, bottom);
    }

    public void setTintBlendMode(BlendMode blendMode) {
        this.mAnimatedVectorState.mVectorDrawable.setTintBlendMode(blendMode);
    }

    public boolean setVisible(boolean visible, boolean restart) {
        if (this.mAnimatorSet.isInfinite() && this.mAnimatorSet.isStarted()) {
            if (visible) {
                this.mAnimatorSet.resume();
            } else {
                this.mAnimatorSet.pause();
            }
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    public boolean isStateful() {
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public int getOpacity() {
        return -3;
    }

    public int getIntrinsicWidth() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public void getOutline(Outline outline) {
        this.mAnimatedVectorState.mVectorDrawable.getOutline(outline);
    }

    public Insets getOpticalInsets() {
        return this.mAnimatedVectorState.mVectorDrawable.getOpticalInsets();
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        Resources resources;
        Resources resources2 = res;
        AttributeSet attributeSet = attrs;
        Theme theme2 = theme;
        AnimatedVectorDrawableState state = this.mAnimatedVectorState;
        int eventType = parser.getEventType();
        float pathErrorScale = 1.0f;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            resources = null;
            if (eventType != 1 && (parser.getDepth() >= innerDepth || eventType != 3)) {
                if (eventType == 2) {
                    String tagName = parser.getName();
                    if (ANIMATED_VECTOR.equals(tagName)) {
                        TypedArray a = Drawable.obtainAttributes(resources2, theme2, attributeSet, R.styleable.AnimatedVectorDrawable);
                        int drawableRes = a.getResourceId(0, 0);
                        if (drawableRes != 0) {
                            VectorDrawable vectorDrawable = (VectorDrawable) resources2.getDrawable(drawableRes, theme2).mutate();
                            vectorDrawable.setAllowCaching(false);
                            vectorDrawable.setCallback(this.mCallback);
                            pathErrorScale = vectorDrawable.getPixelSize();
                            if (state.mVectorDrawable != null) {
                                state.mVectorDrawable.setCallback(null);
                            }
                            state.mVectorDrawable = vectorDrawable;
                        }
                        a.recycle();
                    } else if (TARGET.equals(tagName)) {
                        TypedArray a2 = Drawable.obtainAttributes(resources2, theme2, attributeSet, R.styleable.AnimatedVectorDrawableTarget);
                        String target = a2.getString(0);
                        int animResId = a2.getResourceId(1, 0);
                        if (animResId != 0) {
                            if (theme2 != null) {
                                Animator animator = AnimatorInflater.loadAnimator(resources2, theme2, animResId, pathErrorScale);
                                updateAnimatorProperty(animator, target, state.mVectorDrawable, state.mShouldIgnoreInvalidAnim);
                                state.addTargetAnimator(target, animator);
                            } else {
                                state.addPendingAnimator(animResId, pathErrorScale, target);
                            }
                        }
                        a2.recycle();
                    }
                }
                eventType = parser.next();
            }
        }
        if (state.mPendingAnims != null) {
            resources = resources2;
        }
        this.mRes = resources;
    }

    private static void updateAnimatorProperty(Animator animator, String targetName, VectorDrawable vectorDrawable, boolean ignoreInvalidAnim) {
        if (animator instanceof ObjectAnimator) {
            PropertyValuesHolder[] holders = ((ObjectAnimator) animator).getValues();
            for (PropertyValuesHolder pvh : holders) {
                String propertyName = pvh.getPropertyName();
                Object targetNameObj = vectorDrawable.getTargetByName(targetName);
                Property property = null;
                if (targetNameObj instanceof VObject) {
                    property = ((VObject) targetNameObj).getProperty(propertyName);
                } else if (targetNameObj instanceof VectorDrawableState) {
                    property = ((VectorDrawableState) targetNameObj).getProperty(propertyName);
                }
                if (property != null) {
                    if (containsSameValueType(pvh, property)) {
                        pvh.setProperty(property);
                    } else if (!ignoreInvalidAnim) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Wrong valueType for Property: ");
                        stringBuilder.append(propertyName);
                        stringBuilder.append(".  Expected type: ");
                        stringBuilder.append(property.getType().toString());
                        stringBuilder.append(". Actual type defined in resources: ");
                        stringBuilder.append(pvh.getValueType().toString());
                        throw new RuntimeException(stringBuilder.toString());
                    }
                }
            }
        } else if (animator instanceof AnimatorSet) {
            Iterator it = ((AnimatorSet) animator).getChildAnimations().iterator();
            while (it.hasNext()) {
                updateAnimatorProperty((Animator) it.next(), targetName, vectorDrawable, ignoreInvalidAnim);
            }
        }
    }

    private static boolean containsSameValueType(PropertyValuesHolder holder, Property property) {
        Class type1 = holder.getValueType();
        Class type2 = property.getType();
        boolean z = false;
        if (type1 == Float.TYPE || type1 == Float.class) {
            if (type2 == Float.TYPE || type2 == Float.class) {
                z = true;
            }
            return z;
        } else if (type1 == Integer.TYPE || type1 == Integer.class) {
            if (type2 == Integer.TYPE || type2 == Integer.class) {
                z = true;
            }
            return z;
        } else {
            if (type1 == type2) {
                z = true;
            }
            return z;
        }
    }

    @UnsupportedAppUsage
    public void forceAnimationOnUI() {
        VectorDrawableAnimatorRT animator = this.mAnimatorSet;
        if (!(animator instanceof VectorDrawableAnimatorRT)) {
            return;
        }
        if (animator.isRunning()) {
            throw new UnsupportedOperationException("Cannot force Animated Vector Drawable to run on UI thread when the animation has started on RenderThread.");
        }
        fallbackOntoUI();
    }

    private void fallbackOntoUI() {
        VectorDrawableAnimatorRT oldAnim = this.mAnimatorSet;
        if (oldAnim instanceof VectorDrawableAnimatorRT) {
            oldAnim = oldAnim;
            this.mAnimatorSet = new VectorDrawableAnimatorUI(this);
            AnimatorSet animatorSet = this.mAnimatorSetFromXml;
            if (animatorSet != null) {
                this.mAnimatorSet.init(animatorSet);
            }
            if (oldAnim.mListener != null) {
                this.mAnimatorSet.setListener(oldAnim.mListener);
            }
            oldAnim.transferPendingActions(this.mAnimatorSet);
        }
    }

    public boolean canApplyTheme() {
        AnimatedVectorDrawableState animatedVectorDrawableState = this.mAnimatedVectorState;
        return (animatedVectorDrawableState != null && animatedVectorDrawableState.canApplyTheme()) || super.canApplyTheme();
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        VectorDrawable vectorDrawable = this.mAnimatedVectorState.mVectorDrawable;
        if (vectorDrawable != null && vectorDrawable.canApplyTheme()) {
            vectorDrawable.applyTheme(t);
        }
        if (t != null) {
            this.mAnimatedVectorState.inflatePendingAnimators(t.getResources(), t);
        }
        if (this.mAnimatedVectorState.mPendingAnims == null) {
            this.mRes = null;
        }
    }

    public boolean isRunning() {
        return this.mAnimatorSet.isRunning();
    }

    public void reset() {
        ensureAnimatorSet();
        this.mAnimatorSet.reset();
    }

    public void start() {
        ensureAnimatorSet();
        this.mAnimatorSet.start();
    }

    private void ensureAnimatorSet() {
        if (this.mAnimatorSetFromXml == null) {
            this.mAnimatorSetFromXml = new AnimatorSet();
            this.mAnimatedVectorState.prepareLocalAnimators(this.mAnimatorSetFromXml, this.mRes);
            this.mAnimatorSet.init(this.mAnimatorSetFromXml);
            this.mRes = null;
        }
    }

    public void stop() {
        this.mAnimatorSet.end();
    }

    public void reverse() {
        ensureAnimatorSet();
        if (canReverse()) {
            this.mAnimatorSet.reverse();
        } else {
            Log.w(LOGTAG, "AnimatedVectorDrawable can't reverse()");
        }
    }

    public boolean canReverse() {
        return this.mAnimatorSet.canReverse();
    }

    public void registerAnimationCallback(AnimationCallback callback) {
        if (callback != null) {
            if (this.mAnimationCallbacks == null) {
                this.mAnimationCallbacks = new ArrayList();
            }
            this.mAnimationCallbacks.add(callback);
            if (this.mAnimatorListener == null) {
                this.mAnimatorListener = new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawable.this.mAnimationCallbacks);
                        int size = tmpCallbacks.size();
                        for (int i = 0; i < size; i++) {
                            ((AnimationCallback) tmpCallbacks.get(i)).onAnimationStart(AnimatedVectorDrawable.this);
                        }
                    }

                    public void onAnimationEnd(Animator animation) {
                        ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawable.this.mAnimationCallbacks);
                        int size = tmpCallbacks.size();
                        for (int i = 0; i < size; i++) {
                            ((AnimationCallback) tmpCallbacks.get(i)).onAnimationEnd(AnimatedVectorDrawable.this);
                        }
                    }
                };
            }
            this.mAnimatorSet.setListener(this.mAnimatorListener);
        }
    }

    private void removeAnimatorSetListener() {
        AnimatorListener animatorListener = this.mAnimatorListener;
        if (animatorListener != null) {
            this.mAnimatorSet.removeListener(animatorListener);
            this.mAnimatorListener = null;
        }
    }

    public boolean unregisterAnimationCallback(AnimationCallback callback) {
        boolean removed = this.mAnimationCallbacks;
        if (!removed || callback == null) {
            return false;
        }
        removed = removed.remove(callback);
        if (this.mAnimationCallbacks.size() == 0) {
            removeAnimatorSetListener();
        }
        return removed;
    }

    public void clearAnimationCallbacks() {
        removeAnimatorSetListener();
        ArrayList arrayList = this.mAnimationCallbacks;
        if (arrayList != null) {
            arrayList.clear();
        }
    }
}
