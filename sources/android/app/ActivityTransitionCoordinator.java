package android.app;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.GhostView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.internal.view.OneShotPreDrawListener;
import java.util.ArrayList;

abstract class ActivityTransitionCoordinator extends ResultReceiver {
    protected static final String KEY_ELEVATION = "shared_element:elevation";
    protected static final String KEY_IMAGE_MATRIX = "shared_element:imageMatrix";
    static final String KEY_REMOTE_RECEIVER = "android:remoteReceiver";
    protected static final String KEY_SCALE_TYPE = "shared_element:scaleType";
    protected static final String KEY_SCREEN_BOTTOM = "shared_element:screenBottom";
    protected static final String KEY_SCREEN_LEFT = "shared_element:screenLeft";
    protected static final String KEY_SCREEN_RIGHT = "shared_element:screenRight";
    protected static final String KEY_SCREEN_TOP = "shared_element:screenTop";
    protected static final String KEY_SNAPSHOT = "shared_element:bitmap";
    protected static final String KEY_TRANSLATION_Z = "shared_element:translationZ";
    public static final int MSG_ALLOW_RETURN_TRANSITION = 108;
    public static final int MSG_CANCEL = 106;
    public static final int MSG_EXIT_TRANSITION_COMPLETE = 104;
    public static final int MSG_HIDE_SHARED_ELEMENTS = 101;
    public static final int MSG_SET_REMOTE_RECEIVER = 100;
    public static final int MSG_SHARED_ELEMENT_DESTINATION = 107;
    public static final int MSG_START_EXIT_TRANSITION = 105;
    public static final int MSG_TAKE_SHARED_ELEMENTS = 103;
    protected static final ScaleType[] SCALE_TYPE_VALUES = ScaleType.values();
    private static final String TAG = "ActivityTransitionCoordinator";
    protected final ArrayList<String> mAllSharedElementNames;
    private boolean mBackgroundAnimatorComplete;
    private final FixedEpicenterCallback mEpicenterCallback = new FixedEpicenterCallback();
    private ArrayList<GhostViewListeners> mGhostViewListeners = new ArrayList();
    protected final boolean mIsReturning;
    private boolean mIsStartingTransition;
    protected SharedElementCallback mListener;
    private ArrayMap<View, Float> mOriginalAlphas = new ArrayMap();
    private Runnable mPendingTransition;
    protected ResultReceiver mResultReceiver;
    protected final ArrayList<String> mSharedElementNames = new ArrayList();
    private ArrayList<Matrix> mSharedElementParentMatrices;
    private boolean mSharedElementTransitionComplete;
    protected final ArrayList<View> mSharedElements = new ArrayList();
    private ArrayList<View> mStrippedTransitioningViews = new ArrayList();
    protected ArrayList<View> mTransitioningViews = new ArrayList();
    private boolean mViewsTransitionComplete;
    private Window mWindow;

    protected class ContinueTransitionListener extends TransitionListenerAdapter {
        protected ContinueTransitionListener() {
        }

        public void onTransitionStart(Transition transition) {
            ActivityTransitionCoordinator.this.mIsStartingTransition = false;
            Runnable pending = ActivityTransitionCoordinator.this.mPendingTransition;
            ActivityTransitionCoordinator.this.mPendingTransition = null;
            if (pending != null) {
                ActivityTransitionCoordinator.this.startTransition(pending);
            }
        }

        public void onTransitionEnd(Transition transition) {
            transition.removeListener(this);
        }
    }

    private static class FixedEpicenterCallback extends EpicenterCallback {
        private Rect mEpicenter;

        private FixedEpicenterCallback() {
        }

        public void setEpicenter(Rect epicenter) {
            this.mEpicenter = epicenter;
        }

        public Rect onGetEpicenter(Transition transition) {
            return this.mEpicenter;
        }
    }

    private static class GhostViewListeners implements OnPreDrawListener, OnAttachStateChangeListener {
        private ViewGroup mDecor;
        private Matrix mMatrix = new Matrix();
        private View mParent;
        private View mView;
        private ViewTreeObserver mViewTreeObserver;

        public GhostViewListeners(View view, View parent, ViewGroup decor) {
            this.mView = view;
            this.mParent = parent;
            this.mDecor = decor;
            this.mViewTreeObserver = parent.getViewTreeObserver();
        }

        public View getView() {
            return this.mView;
        }

        public boolean onPreDraw() {
            GhostView ghostView = GhostView.getGhost(this.mView);
            if (ghostView == null || !this.mView.isAttachedToWindow()) {
                removeListener();
            } else {
                GhostView.calculateMatrix(this.mView, this.mDecor, this.mMatrix);
                ghostView.setMatrix(this.mMatrix);
            }
            return true;
        }

        public void removeListener() {
            if (this.mViewTreeObserver.isAlive()) {
                this.mViewTreeObserver.removeOnPreDrawListener(this);
            } else {
                this.mParent.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            this.mParent.removeOnAttachStateChangeListener(this);
        }

        public void onViewAttachedToWindow(View v) {
            this.mViewTreeObserver = v.getViewTreeObserver();
        }

        public void onViewDetachedFromWindow(View v) {
            removeListener();
        }
    }

    static class SharedElementOriginalState {
        int mBottom;
        float mElevation;
        int mLeft;
        Matrix mMatrix;
        int mMeasuredHeight;
        int mMeasuredWidth;
        int mRight;
        ScaleType mScaleType;
        int mTop;
        float mTranslationZ;

        SharedElementOriginalState() {
        }
    }

    public abstract Transition getViewsTransition();

    public ActivityTransitionCoordinator(Window window, ArrayList<String> allSharedElementNames, SharedElementCallback listener, boolean isReturning) {
        super(new Handler());
        this.mWindow = window;
        this.mListener = listener;
        this.mAllSharedElementNames = allSharedElementNames;
        this.mIsReturning = isReturning;
    }

    /* Access modifiers changed, original: protected */
    public void viewsReady(ArrayMap<String, View> sharedElements) {
        sharedElements.retainAll(this.mAllSharedElementNames);
        SharedElementCallback sharedElementCallback = this.mListener;
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(this.mAllSharedElementNames, sharedElements);
        }
        setSharedElements(sharedElements);
        if (!(getViewsTransition() == null || this.mTransitioningViews == null)) {
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.captureTransitioningViews(this.mTransitioningViews);
            }
            this.mTransitioningViews.removeAll(this.mSharedElements);
        }
        setEpicenter();
    }

    private void setSharedElements(ArrayMap<String, View> sharedElements) {
        boolean isFirstRun = true;
        while (!sharedElements.isEmpty()) {
            for (int i = sharedElements.size() - 1; i >= 0; i--) {
                View view = (View) sharedElements.valueAt(i);
                String name = (String) sharedElements.keyAt(i);
                if (isFirstRun && (view == null || !view.isAttachedToWindow() || name == null)) {
                    sharedElements.removeAt(i);
                } else if (!isNested(view, sharedElements)) {
                    this.mSharedElementNames.add(name);
                    this.mSharedElements.add(view);
                    sharedElements.removeAt(i);
                }
            }
            isFirstRun = false;
        }
    }

    private static boolean isNested(View view, ArrayMap<String, View> sharedElements) {
        ViewParent parent = view.getParent();
        while (parent instanceof View) {
            View parentView = (View) parent;
            if (sharedElements.containsValue(parentView)) {
                return true;
            }
            parent = parentView.getParent();
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void stripOffscreenViews() {
        if (this.mTransitioningViews != null) {
            Rect r = new Rect();
            for (int i = this.mTransitioningViews.size() - 1; i >= 0; i--) {
                View view = (View) this.mTransitioningViews.get(i);
                if (!view.getGlobalVisibleRect(r)) {
                    this.mTransitioningViews.remove(i);
                    this.mStrippedTransitioningViews.add(view);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Window getWindow() {
        return this.mWindow;
    }

    public ViewGroup getDecor() {
        Window window = this.mWindow;
        return window == null ? null : (ViewGroup) window.getDecorView();
    }

    /* Access modifiers changed, original: protected */
    public void setEpicenter() {
        View epicenter = null;
        if (!(this.mAllSharedElementNames.isEmpty() || this.mSharedElementNames.isEmpty())) {
            int index = this.mSharedElementNames.indexOf(this.mAllSharedElementNames.get(0));
            if (index >= 0) {
                epicenter = (View) this.mSharedElements.get(index);
            }
        }
        setEpicenter(epicenter);
    }

    private void setEpicenter(View view) {
        if (view == null) {
            this.mEpicenterCallback.setEpicenter(null);
            return;
        }
        Rect epicenter = new Rect();
        view.getBoundsOnScreen(epicenter);
        this.mEpicenterCallback.setEpicenter(epicenter);
    }

    public ArrayList<String> getAcceptedNames() {
        return this.mSharedElementNames;
    }

    public ArrayList<String> getMappedNames() {
        ArrayList<String> names = new ArrayList(this.mSharedElements.size());
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            names.add(((View) this.mSharedElements.get(i)).getTransitionName());
        }
        return names;
    }

    public ArrayList<View> copyMappedViews() {
        return new ArrayList(this.mSharedElements);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:5:0x000c, code skipped:
            if (r0.isEmpty() == false) goto L_0x000f;
     */
    public android.transition.Transition setTargets(android.transition.Transition r5, boolean r6) {
        /*
        r4 = this;
        if (r5 == 0) goto L_0x0066;
    L_0x0002:
        if (r6 == 0) goto L_0x000f;
    L_0x0004:
        r0 = r4.mTransitioningViews;
        if (r0 == 0) goto L_0x0066;
    L_0x0008:
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x000f;
    L_0x000e:
        goto L_0x0066;
    L_0x000f:
        r0 = new android.transition.TransitionSet;
        r0.<init>();
        r1 = r4.mTransitioningViews;
        r2 = 1;
        if (r1 == 0) goto L_0x0034;
    L_0x0019:
        r1 = r1.size();
        r1 = r1 - r2;
    L_0x001e:
        if (r1 < 0) goto L_0x0034;
    L_0x0020:
        r3 = r4.mTransitioningViews;
        r3 = r3.get(r1);
        r3 = (android.view.View) r3;
        if (r6 == 0) goto L_0x002e;
    L_0x002a:
        r0.addTarget(r3);
        goto L_0x0031;
    L_0x002e:
        r0.excludeTarget(r3, r2);
    L_0x0031:
        r1 = r1 + -1;
        goto L_0x001e;
    L_0x0034:
        r1 = r4.mStrippedTransitioningViews;
        if (r1 == 0) goto L_0x004d;
    L_0x0038:
        r1 = r1.size();
        r1 = r1 - r2;
    L_0x003d:
        if (r1 < 0) goto L_0x004d;
    L_0x003f:
        r3 = r4.mStrippedTransitioningViews;
        r3 = r3.get(r1);
        r3 = (android.view.View) r3;
        r0.excludeTarget(r3, r2);
        r1 = r1 + -1;
        goto L_0x003d;
    L_0x004d:
        r0.addTransition(r5);
        if (r6 != 0) goto L_0x0065;
    L_0x0052:
        r1 = r4.mTransitioningViews;
        if (r1 == 0) goto L_0x0065;
    L_0x0056:
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x0065;
    L_0x005c:
        r1 = new android.transition.TransitionSet;
        r1.<init>();
        r0 = r1.addTransition(r0);
    L_0x0065:
        return r0;
    L_0x0066:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityTransitionCoordinator.setTargets(android.transition.Transition, boolean):android.transition.Transition");
    }

    /* Access modifiers changed, original: protected */
    public Transition configureTransition(Transition transition, boolean includeTransitioningViews) {
        if (transition != null) {
            transition = transition.clone();
            transition.setEpicenterCallback(this.mEpicenterCallback);
            transition = setTargets(transition, includeTransitioningViews);
        }
        noLayoutSuppressionForVisibilityTransitions(transition);
        return transition;
    }

    protected static void removeExcludedViews(Transition transition, ArrayList<View> views) {
        ArraySet<View> included = new ArraySet();
        findIncludedViews(transition, views, included);
        views.clear();
        views.addAll(included);
    }

    private static void findIncludedViews(Transition transition, ArrayList<View> views, ArraySet<View> included) {
        if (transition instanceof TransitionSet) {
            int i;
            TransitionSet set = (TransitionSet) transition;
            ArrayList<View> includedViews = new ArrayList();
            int numViews = views.size();
            for (i = 0; i < numViews; i++) {
                View view = (View) views.get(i);
                if (transition.isValidTarget(view)) {
                    includedViews.add(view);
                }
            }
            i = set.getTransitionCount();
            for (int i2 = 0; i2 < i; i2++) {
                findIncludedViews(set.getTransitionAt(i2), includedViews, included);
            }
            return;
        }
        int numViews2 = views.size();
        for (int i3 = 0; i3 < numViews2; i3++) {
            View view2 = (View) views.get(i3);
            if (transition.isValidTarget(view2)) {
                included.add(view2);
            }
        }
    }

    protected static Transition mergeTransitions(Transition transition1, Transition transition2) {
        if (transition1 == null) {
            return transition2;
        }
        if (transition2 == null) {
            return transition1;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(transition1);
        transitionSet.addTransition(transition2);
        return transitionSet;
    }

    /* Access modifiers changed, original: protected */
    public ArrayMap<String, View> mapSharedElements(ArrayList<String> accepted, ArrayList<View> localViews) {
        ArrayMap<String, View> sharedElements = new ArrayMap();
        if (accepted != null) {
            for (int i = 0; i < accepted.size(); i++) {
                sharedElements.put((String) accepted.get(i), (View) localViews.get(i));
            }
        } else {
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.findNamedViews(sharedElements);
            }
        }
        return sharedElements;
    }

    /* Access modifiers changed, original: protected */
    public void setResultReceiver(ResultReceiver resultReceiver) {
        this.mResultReceiver = resultReceiver;
    }

    private void setSharedElementState(View view, String name, Bundle transitionArgs, Matrix tempMatrix, RectF tempRect, int[] decorLoc) {
        View view2 = view;
        Matrix matrix = tempMatrix;
        RectF rectF = tempRect;
        Bundle sharedElementBundle = transitionArgs.getBundle(name);
        if (sharedElementBundle != null) {
            if (view2 instanceof ImageView) {
                int scaleTypeInt = sharedElementBundle.getInt(KEY_SCALE_TYPE, -1);
                if (scaleTypeInt >= 0) {
                    ImageView imageView = (ImageView) view2;
                    ScaleType scaleType = SCALE_TYPE_VALUES[scaleTypeInt];
                    imageView.setScaleType(scaleType);
                    if (scaleType == ScaleType.MATRIX) {
                        matrix.setValues(sharedElementBundle.getFloatArray(KEY_IMAGE_MATRIX));
                        imageView.setImageMatrix(matrix);
                    }
                }
            }
            view2.setTranslationZ(sharedElementBundle.getFloat(KEY_TRANSLATION_Z));
            view2.setElevation(sharedElementBundle.getFloat(KEY_ELEVATION));
            float left = sharedElementBundle.getFloat(KEY_SCREEN_LEFT);
            float top = sharedElementBundle.getFloat(KEY_SCREEN_TOP);
            float right = sharedElementBundle.getFloat(KEY_SCREEN_RIGHT);
            float bottom = sharedElementBundle.getFloat(KEY_SCREEN_BOTTOM);
            if (decorLoc != null) {
                left -= (float) decorLoc[0];
                top -= (float) decorLoc[1];
                right -= (float) decorLoc[0];
                bottom -= (float) decorLoc[1];
            } else {
                getSharedElementParentMatrix(view2, matrix);
                rectF.set(left, top, right, bottom);
                tempMatrix.mapRect(tempRect);
                float leftInParent = rectF.left;
                float topInParent = rectF.top;
                view.getInverseMatrix().mapRect(rectF);
                float width = tempRect.width();
                float height = tempRect.height();
                view2.setLeft(0);
                view2.setTop(0);
                view2.setRight(Math.round(width));
                view2.setBottom(Math.round(height));
                rectF.set(0.0f, 0.0f, width, height);
                view.getMatrix().mapRect(rectF);
                left = leftInParent - rectF.left;
                top = topInParent - rectF.top;
                right = left + width;
                bottom = top + height;
            }
            int x = Math.round(left);
            int y = Math.round(top);
            int width2 = Math.round(right) - x;
            int height2 = Math.round(bottom) - y;
            int widthSpec = MeasureSpec.makeMeasureSpec(width2, 1073741824);
            view2.measure(widthSpec, MeasureSpec.makeMeasureSpec(height2, 1073741824));
            view2.layout(x, y, x + width2, y + height2);
        }
    }

    private void setSharedElementMatrices() {
        int numSharedElements = this.mSharedElements.size();
        if (numSharedElements > 0) {
            this.mSharedElementParentMatrices = new ArrayList(numSharedElements);
        }
        for (int i = 0; i < numSharedElements; i++) {
            ViewGroup parent = (ViewGroup) ((View) this.mSharedElements.get(i)).getParent();
            Matrix matrix = new Matrix();
            if (parent != null) {
                parent.transformMatrixToLocal(matrix);
                matrix.postTranslate((float) parent.getScrollX(), (float) parent.getScrollY());
            }
            this.mSharedElementParentMatrices.add(matrix);
        }
    }

    private void getSharedElementParentMatrix(View view, Matrix matrix) {
        int index;
        if (this.mSharedElementParentMatrices == null) {
            index = -1;
        } else {
            index = this.mSharedElements.indexOf(view);
        }
        if (index < 0) {
            matrix.reset();
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) viewParent;
                parent.transformMatrixToLocal(matrix);
                matrix.postTranslate((float) parent.getScrollX(), (float) parent.getScrollY());
                return;
            }
            return;
        }
        matrix.set((Matrix) this.mSharedElementParentMatrices.get(index));
    }

    /* Access modifiers changed, original: protected */
    public ArrayList<SharedElementOriginalState> setSharedElementState(Bundle sharedElementState, ArrayList<View> snapshots) {
        ArrayList<SharedElementOriginalState> originalImageState = new ArrayList();
        if (sharedElementState != null) {
            Matrix tempMatrix = new Matrix();
            RectF tempRect = new RectF();
            int numSharedElements = this.mSharedElements.size();
            for (int i = 0; i < numSharedElements; i++) {
                View sharedElement = (View) this.mSharedElements.get(i);
                String name = (String) this.mSharedElementNames.get(i);
                originalImageState.add(getOldSharedElementState(sharedElement, name, sharedElementState));
                setSharedElementState(sharedElement, name, sharedElementState, tempMatrix, tempRect, null);
            }
        }
        SharedElementCallback sharedElementCallback = this.mListener;
        if (sharedElementCallback != null) {
            sharedElementCallback.onSharedElementStart(this.mSharedElementNames, this.mSharedElements, snapshots);
        }
        return originalImageState;
    }

    /* Access modifiers changed, original: protected */
    /* renamed from: notifySharedElementEnd */
    public void lambda$scheduleSetSharedElementEnd$0$ActivityTransitionCoordinator(ArrayList<View> snapshots) {
        SharedElementCallback sharedElementCallback = this.mListener;
        if (sharedElementCallback != null) {
            sharedElementCallback.onSharedElementEnd(this.mSharedElementNames, this.mSharedElements, snapshots);
        }
    }

    /* Access modifiers changed, original: protected */
    public void scheduleSetSharedElementEnd(ArrayList<View> snapshots) {
        View decorView = getDecor();
        if (decorView != null) {
            OneShotPreDrawListener.add(decorView, new -$$Lambda$ActivityTransitionCoordinator$fkaPvc8GCghP2GMwEgS_J5m_T_4(this, snapshots));
        }
    }

    private static SharedElementOriginalState getOldSharedElementState(View view, String name, Bundle transitionArgs) {
        SharedElementOriginalState state = new SharedElementOriginalState();
        state.mLeft = view.getLeft();
        state.mTop = view.getTop();
        state.mRight = view.getRight();
        state.mBottom = view.getBottom();
        state.mMeasuredWidth = view.getMeasuredWidth();
        state.mMeasuredHeight = view.getMeasuredHeight();
        state.mTranslationZ = view.getTranslationZ();
        state.mElevation = view.getElevation();
        if (!(view instanceof ImageView)) {
            return state;
        }
        Bundle bundle = transitionArgs.getBundle(name);
        if (bundle == null || bundle.getInt(KEY_SCALE_TYPE, -1) < 0) {
            return state;
        }
        ImageView imageView = (ImageView) view;
        state.mScaleType = imageView.getScaleType();
        if (state.mScaleType == ScaleType.MATRIX) {
            state.mMatrix = new Matrix(imageView.getImageMatrix());
        }
        return state;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005b  */
    public java.util.ArrayList<android.view.View> createSnapshots(android.os.Bundle r19, java.util.Collection<java.lang.String> r20) {
        /*
        r18 = this;
        r0 = r20.size();
        r1 = new java.util.ArrayList;
        r1.<init>(r0);
        if (r0 != 0) goto L_0x000c;
    L_0x000b:
        return r1;
    L_0x000c:
        r2 = r18.getWindow();
        r2 = r2.getContext();
        r3 = 2;
        r3 = new int[r3];
        r11 = r18.getDecor();
        if (r11 == 0) goto L_0x0020;
    L_0x001d:
        r11.getLocationOnScreen(r3);
    L_0x0020:
        r8 = new android.graphics.Matrix;
        r8.<init>();
        r12 = r20.iterator();
    L_0x0029:
        r4 = r12.hasNext();
        if (r4 == 0) goto L_0x0075;
    L_0x002f:
        r4 = r12.next();
        r13 = r4;
        r13 = (java.lang.String) r13;
        r14 = r19;
        r15 = r14.getBundle(r13);
        r4 = 0;
        if (r15 == 0) goto L_0x0071;
    L_0x003f:
        r5 = "shared_element:bitmap";
        r10 = r15.getParcelable(r5);
        if (r10 == 0) goto L_0x0055;
    L_0x0048:
        r9 = r18;
        r5 = r9.mListener;
        if (r5 == 0) goto L_0x0057;
    L_0x004e:
        r4 = r5.onCreateSnapshotView(r2, r10);
        r16 = r4;
        goto L_0x0059;
    L_0x0055:
        r9 = r18;
    L_0x0057:
        r16 = r4;
    L_0x0059:
        if (r16 == 0) goto L_0x006d;
    L_0x005b:
        r17 = 0;
        r4 = r18;
        r5 = r16;
        r6 = r13;
        r7 = r19;
        r9 = r17;
        r17 = r10;
        r10 = r3;
        r4.setSharedElementState(r5, r6, r7, r8, r9, r10);
        goto L_0x006f;
    L_0x006d:
        r17 = r10;
    L_0x006f:
        r4 = r16;
    L_0x0071:
        r1.add(r4);
        goto L_0x0029;
    L_0x0075:
        r14 = r19;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityTransitionCoordinator.createSnapshots(android.os.Bundle, java.util.Collection):java.util.ArrayList");
    }

    protected static void setOriginalSharedElementState(ArrayList<View> sharedElements, ArrayList<SharedElementOriginalState> originalState) {
        for (int i = 0; i < originalState.size(); i++) {
            View view = (View) sharedElements.get(i);
            SharedElementOriginalState state = (SharedElementOriginalState) originalState.get(i);
            if ((view instanceof ImageView) && state.mScaleType != null) {
                ImageView imageView = (ImageView) view;
                imageView.setScaleType(state.mScaleType);
                if (state.mScaleType == ScaleType.MATRIX) {
                    imageView.setImageMatrix(state.mMatrix);
                }
            }
            view.setElevation(state.mElevation);
            view.setTranslationZ(state.mTranslationZ);
            view.measure(MeasureSpec.makeMeasureSpec(state.mMeasuredWidth, 1073741824), MeasureSpec.makeMeasureSpec(state.mMeasuredHeight, 1073741824));
            view.layout(state.mLeft, state.mTop, state.mRight, state.mBottom);
        }
    }

    /* Access modifiers changed, original: protected */
    public Bundle captureSharedElementState() {
        Bundle bundle = new Bundle();
        RectF tempBounds = new RectF();
        Matrix tempMatrix = new Matrix();
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            captureSharedElementState((View) this.mSharedElements.get(i), (String) this.mSharedElementNames.get(i), bundle, tempMatrix, tempBounds);
        }
        return bundle;
    }

    /* Access modifiers changed, original: protected */
    public void clearState() {
        this.mWindow = null;
        this.mSharedElements.clear();
        this.mTransitioningViews = null;
        this.mStrippedTransitioningViews = null;
        this.mOriginalAlphas.clear();
        this.mResultReceiver = null;
        this.mPendingTransition = null;
        this.mListener = null;
        this.mSharedElementParentMatrices = null;
    }

    /* Access modifiers changed, original: protected */
    public long getFadeDuration() {
        return getWindow().getTransitionBackgroundFadeDuration();
    }

    /* Access modifiers changed, original: protected */
    public void hideViews(ArrayList<View> views) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            View view = (View) views.get(i);
            if (!this.mOriginalAlphas.containsKey(view)) {
                this.mOriginalAlphas.put(view, Float.valueOf(view.getAlpha()));
            }
            view.setAlpha(0.0f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void showViews(ArrayList<View> views, boolean setTransitionAlpha) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            showView((View) views.get(i), setTransitionAlpha);
        }
    }

    private void showView(View view, boolean setTransitionAlpha) {
        Float alpha = (Float) this.mOriginalAlphas.remove(view);
        if (alpha != null) {
            view.setAlpha(alpha.floatValue());
        }
        if (setTransitionAlpha) {
            view.setTransitionAlpha(1.0f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void captureSharedElementState(View view, String name, Bundle transitionArgs, Matrix tempMatrix, RectF tempBounds) {
        Bundle sharedElementBundle = new Bundle();
        tempMatrix.reset();
        view.transformMatrixToGlobal(tempMatrix);
        tempBounds.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        tempMatrix.mapRect(tempBounds);
        sharedElementBundle.putFloat(KEY_SCREEN_LEFT, tempBounds.left);
        sharedElementBundle.putFloat(KEY_SCREEN_RIGHT, tempBounds.right);
        sharedElementBundle.putFloat(KEY_SCREEN_TOP, tempBounds.top);
        sharedElementBundle.putFloat(KEY_SCREEN_BOTTOM, tempBounds.bottom);
        sharedElementBundle.putFloat(KEY_TRANSLATION_Z, view.getTranslationZ());
        sharedElementBundle.putFloat(KEY_ELEVATION, view.getElevation());
        Parcelable bitmap = null;
        SharedElementCallback sharedElementCallback = this.mListener;
        if (sharedElementCallback != null) {
            bitmap = sharedElementCallback.onCaptureSharedElementSnapshot(view, tempMatrix, tempBounds);
        }
        if (bitmap != null) {
            sharedElementBundle.putParcelable(KEY_SNAPSHOT, bitmap);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            sharedElementBundle.putInt(KEY_SCALE_TYPE, scaleTypeToInt(imageView.getScaleType()));
            if (imageView.getScaleType() == ScaleType.MATRIX) {
                float[] matrix = new float[9];
                imageView.getImageMatrix().getValues(matrix);
                sharedElementBundle.putFloatArray(KEY_IMAGE_MATRIX, matrix);
            }
        }
        transitionArgs.putBundle(name, sharedElementBundle);
    }

    /* Access modifiers changed, original: protected */
    public void startTransition(Runnable runnable) {
        if (this.mIsStartingTransition) {
            this.mPendingTransition = runnable;
            return;
        }
        this.mIsStartingTransition = true;
        runnable.run();
    }

    /* Access modifiers changed, original: protected */
    public void transitionStarted() {
        this.mIsStartingTransition = false;
    }

    /* Access modifiers changed, original: protected */
    public boolean cancelPendingTransitions() {
        this.mPendingTransition = null;
        return this.mIsStartingTransition;
    }

    /* Access modifiers changed, original: protected */
    public void moveSharedElementsToOverlay() {
        Window window = this.mWindow;
        if (window != null && window.getSharedElementsUseOverlay()) {
            setSharedElementMatrices();
            int numSharedElements = this.mSharedElements.size();
            ViewGroup decor = getDecor();
            if (decor != null) {
                boolean moveWithParent = moveSharedElementWithParent();
                Matrix tempMatrix = new Matrix();
                for (int i = 0; i < numSharedElements; i++) {
                    View view = (View) this.mSharedElements.get(i);
                    if (view.isAttachedToWindow()) {
                        tempMatrix.reset();
                        ((Matrix) this.mSharedElementParentMatrices.get(i)).invert(tempMatrix);
                        GhostView.addGhost(view, decor, tempMatrix);
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (moveWithParent && !isInTransitionGroup(parent, decor)) {
                            GhostViewListeners listener = new GhostViewListeners(view, parent, decor);
                            parent.getViewTreeObserver().addOnPreDrawListener(listener);
                            parent.addOnAttachStateChangeListener(listener);
                            this.mGhostViewListeners.add(listener);
                        }
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean moveSharedElementWithParent() {
        return true;
    }

    public static boolean isInTransitionGroup(ViewParent viewParent, ViewGroup decor) {
        if (viewParent == decor || !(viewParent instanceof ViewGroup)) {
            return false;
        }
        ViewGroup parent = (ViewGroup) viewParent;
        if (parent.isTransitionGroup()) {
            return true;
        }
        return isInTransitionGroup(parent.getParent(), decor);
    }

    /* Access modifiers changed, original: protected */
    public void moveSharedElementsFromOverlay() {
        int numListeners = this.mGhostViewListeners.size();
        for (int i = 0; i < numListeners; i++) {
            ((GhostViewListeners) this.mGhostViewListeners.get(i)).removeListener();
        }
        this.mGhostViewListeners.clear();
        Window window = this.mWindow;
        if (window != null && window.getSharedElementsUseOverlay()) {
            ViewGroup decor = getDecor();
            if (decor != null) {
                ViewGroupOverlay overlay = decor.getOverlay();
                int count = this.mSharedElements.size();
                for (int i2 = 0; i2 < count; i2++) {
                    GhostView.removeGhost((View) this.mSharedElements.get(i2));
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    /* renamed from: setGhostVisibility */
    public void lambda$scheduleGhostVisibilityChange$1$ActivityTransitionCoordinator(int visibility) {
        int numSharedElements = this.mSharedElements.size();
        for (int i = 0; i < numSharedElements; i++) {
            GhostView ghostView = GhostView.getGhost((View) this.mSharedElements.get(i));
            if (ghostView != null) {
                ghostView.setVisibility(visibility);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void scheduleGhostVisibilityChange(int visibility) {
        View decorView = getDecor();
        if (decorView != null) {
            OneShotPreDrawListener.add(decorView, new -$$Lambda$ActivityTransitionCoordinator$_HMo0E-15AzCK9fwQ8WHzdz8ZIw(this, visibility));
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isViewsTransitionComplete() {
        return this.mViewsTransitionComplete;
    }

    /* Access modifiers changed, original: protected */
    public void viewsTransitionComplete() {
        this.mViewsTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    /* Access modifiers changed, original: protected */
    public void backgroundAnimatorComplete() {
        this.mBackgroundAnimatorComplete = true;
    }

    /* Access modifiers changed, original: protected */
    public void sharedElementTransitionComplete() {
        this.mSharedElementTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    private void startInputWhenTransitionsComplete() {
        if (this.mViewsTransitionComplete && this.mSharedElementTransitionComplete) {
            View decor = getDecor();
            if (decor != null) {
                ViewRootImpl viewRoot = decor.getViewRootImpl();
                if (viewRoot != null) {
                    viewRoot.setPausedForTransition(false);
                }
            }
            onTransitionsComplete();
        }
    }

    /* Access modifiers changed, original: protected */
    public void pauseInput() {
        View decor = getDecor();
        ViewRootImpl viewRoot = decor == null ? null : decor.getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.setPausedForTransition(true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onTransitionsComplete() {
    }

    private static int scaleTypeToInt(ScaleType scaleType) {
        int i = 0;
        while (true) {
            ScaleType[] scaleTypeArr = SCALE_TYPE_VALUES;
            if (i >= scaleTypeArr.length) {
                return -1;
            }
            if (scaleType == scaleTypeArr[i]) {
                return i;
            }
            i++;
        }
    }

    /* Access modifiers changed, original: protected */
    public void setTransitioningViewsVisiblity(int visiblity, boolean invalidate) {
        int numElements = this.mTransitioningViews;
        numElements = numElements == 0 ? 0 : numElements.size();
        for (int i = 0; i < numElements; i++) {
            View view = (View) this.mTransitioningViews.get(i);
            if (invalidate) {
                view.setVisibility(visiblity);
            } else {
                view.setTransitionVisibility(visiblity);
            }
        }
    }

    private static void noLayoutSuppressionForVisibilityTransitions(Transition transition) {
        if (transition instanceof Visibility) {
            ((Visibility) transition).setSuppressLayout(false);
        } else if (transition instanceof TransitionSet) {
            TransitionSet set = (TransitionSet) transition;
            int count = set.getTransitionCount();
            for (int i = 0; i < count; i++) {
                noLayoutSuppressionForVisibilityTransitions(set.getTransitionAt(i));
            }
        }
    }

    public boolean isTransitionRunning() {
        return (this.mViewsTransitionComplete && this.mSharedElementTransitionComplete && this.mBackgroundAnimatorComplete) ? false : true;
    }
}
