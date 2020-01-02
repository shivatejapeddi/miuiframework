package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.RenderNodeAnimator;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.android.internal.R;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LockPatternView extends View {
    private static final int ASPECT_LOCK_HEIGHT = 2;
    private static final int ASPECT_LOCK_WIDTH = 1;
    private static final int ASPECT_SQUARE = 0;
    public static final boolean DEBUG_A11Y = false;
    private static final float DRAG_THRESHHOLD = 0.0f;
    private static final float LINE_FADE_ALPHA_MULTIPLIER = 1.5f;
    private static final int MILLIS_PER_CIRCLE_ANIMATING = 700;
    private static final boolean PROFILE_DRAWING = false;
    private static final String TAG = "LockPatternView";
    public static final int VIRTUAL_BASE_VIEW_ID = 1;
    private long mAnimatingPeriodStart;
    private int mAspect;
    private AudioManager mAudioManager;
    private final CellState[][] mCellStates;
    private final Path mCurrentPath;
    private final int mDotSize;
    private final int mDotSizeActivated;
    private boolean mDrawingProfilingStarted;
    private boolean mEnableHapticFeedback;
    private int mErrorColor;
    private PatternExploreByTouchHelper mExploreByTouchHelper;
    private boolean mFadePattern;
    private final Interpolator mFastOutSlowInInterpolator;
    private float mHitFactor;
    private float mInProgressX;
    private float mInProgressY;
    @UnsupportedAppUsage
    private boolean mInStealthMode;
    private boolean mInputEnabled;
    private final Rect mInvalidate;
    private long[] mLineFadeStart;
    private final Interpolator mLinearOutSlowInInterpolator;
    private Drawable mNotSelectedDrawable;
    private OnPatternListener mOnPatternListener;
    @UnsupportedAppUsage
    private final Paint mPaint;
    @UnsupportedAppUsage
    private final Paint mPathPaint;
    private final int mPathWidth;
    @UnsupportedAppUsage
    private final ArrayList<Cell> mPattern;
    @UnsupportedAppUsage
    private DisplayMode mPatternDisplayMode;
    private final boolean[][] mPatternDrawLookup;
    @UnsupportedAppUsage
    private boolean mPatternInProgress;
    private int mRegularColor;
    private Drawable mSelectedDrawable;
    @UnsupportedAppUsage
    private float mSquareHeight;
    @UnsupportedAppUsage
    private float mSquareWidth;
    private int mSuccessColor;
    private final Rect mTmpInvalidateRect;
    private boolean mUseLockPatternDrawable;

    public static final class Cell {
        private static final Cell[][] sCells = createCells();
        @UnsupportedAppUsage
        final int column;
        @UnsupportedAppUsage
        final int row;

        private static Cell[][] createCells() {
            Cell[][] res = (Cell[][]) Array.newInstance(Cell.class, new int[]{3, 3});
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    res[i][j] = new Cell(i, j);
                }
            }
            return res;
        }

        private Cell(int row, int column) {
            checkRange(row, column);
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return this.row;
        }

        public int getColumn() {
            return this.column;
        }

        public static Cell of(int row, int column) {
            checkRange(row, column);
            return sCells[row][column];
        }

        private static void checkRange(int row, int column) {
            if (row < 0 || row > 2) {
                throw new IllegalArgumentException("row must be in range 0-2");
            } else if (column < 0 || column > 2) {
                throw new IllegalArgumentException("column must be in range 0-2");
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(row=");
            stringBuilder.append(this.row);
            stringBuilder.append(",clmn=");
            stringBuilder.append(this.column);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    public static class CellState {
        float alpha = 1.0f;
        int col;
        boolean hwAnimating;
        CanvasProperty<Float> hwCenterX;
        CanvasProperty<Float> hwCenterY;
        CanvasProperty<Paint> hwPaint;
        CanvasProperty<Float> hwRadius;
        public ValueAnimator lineAnimator;
        public float lineEndX = Float.MIN_VALUE;
        public float lineEndY = Float.MIN_VALUE;
        float radius;
        int row;
        float translationY;
    }

    public enum DisplayMode {
        Correct,
        Animate,
        Wrong
    }

    public interface OnPatternListener {
        void onPatternCellAdded(List<Cell> list);

        void onPatternCleared();

        void onPatternDetected(List<Cell> list);

        void onPatternStart();
    }

    private final class PatternExploreByTouchHelper extends ExploreByTouchHelper {
        private final SparseArray<VirtualViewContainer> mItems = new SparseArray();
        private Rect mTempRect = new Rect();

        class VirtualViewContainer {
            CharSequence description;

            public VirtualViewContainer(CharSequence description) {
                this.description = description;
            }
        }

        public PatternExploreByTouchHelper(View forView) {
            super(forView);
            for (int i = 1; i < 10; i++) {
                this.mItems.put(i, new VirtualViewContainer(getTextForVirtualView(i)));
            }
        }

        /* Access modifiers changed, original: protected */
        public int getVirtualViewAt(float x, float y) {
            return getVirtualViewIdForHit(x, y);
        }

        /* Access modifiers changed, original: protected */
        public void getVisibleVirtualViews(IntArray virtualViewIds) {
            if (LockPatternView.this.mPatternInProgress) {
                for (int i = 1; i < 10; i++) {
                    virtualViewIds.add(i);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            VirtualViewContainer container = (VirtualViewContainer) this.mItems.get(virtualViewId);
            if (container != null) {
                event.getText().add(container.description);
            }
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            if (!LockPatternView.this.mPatternInProgress) {
                event.setContentDescription(LockPatternView.this.getContext().getText(R.string.lockscreen_access_pattern_area));
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
            node.setText(getTextForVirtualView(virtualViewId));
            node.setContentDescription(getTextForVirtualView(virtualViewId));
            if (LockPatternView.this.mPatternInProgress) {
                node.setFocusable(true);
                if (isClickable(virtualViewId)) {
                    node.addAction(AccessibilityAction.ACTION_CLICK);
                    node.setClickable(isClickable(virtualViewId));
                }
            }
            node.setBoundsInParent(getBoundsForVirtualView(virtualViewId));
        }

        private boolean isClickable(int virtualViewId) {
            if (virtualViewId == Integer.MIN_VALUE) {
                return false;
            }
            return LockPatternView.this.mPatternDrawLookup[(virtualViewId - 1) / 3][(virtualViewId - 1) % 3] ^ 1;
        }

        /* Access modifiers changed, original: protected */
        public boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            if (action != 16) {
                return false;
            }
            return onItemClicked(virtualViewId);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean onItemClicked(int index) {
            invalidateVirtualView(index);
            sendEventForVirtualView(index, 1);
            return true;
        }

        private Rect getBoundsForVirtualView(int virtualViewId) {
            int ordinal = virtualViewId - 1;
            Rect bounds = this.mTempRect;
            int row = ordinal / 3;
            int col = ordinal % 3;
            CellState cell = LockPatternView.this.mCellStates[row][col];
            float centerX = LockPatternView.this.getCenterXForColumn(col);
            float centerY = LockPatternView.this.getCenterYForRow(row);
            float cellheight = (LockPatternView.this.mSquareHeight * LockPatternView.this.mHitFactor) * 0.5f;
            float cellwidth = (LockPatternView.this.mSquareWidth * LockPatternView.this.mHitFactor) * 0.5f;
            bounds.left = (int) (centerX - cellwidth);
            bounds.right = (int) (centerX + cellwidth);
            bounds.top = (int) (centerY - cellheight);
            bounds.bottom = (int) (centerY + cellheight);
            return bounds;
        }

        private CharSequence getTextForVirtualView(int virtualViewId) {
            return LockPatternView.this.getResources().getString(R.string.lockscreen_access_pattern_cell_added_verbose, Integer.valueOf(virtualViewId));
        }

        private int getVirtualViewIdForHit(float x, float y) {
            int rowHit = LockPatternView.this.getRowHit(y);
            int view = Integer.MIN_VALUE;
            if (rowHit < 0) {
                return Integer.MIN_VALUE;
            }
            int columnHit = LockPatternView.this.getColumnHit(x);
            if (columnHit < 0) {
                return Integer.MIN_VALUE;
            }
            int dotId = ((rowHit * 3) + columnHit) + 1;
            if (LockPatternView.this.mPatternDrawLookup[rowHit][columnHit]) {
                view = dotId;
            }
            return view;
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private final int mDisplayMode;
        private final boolean mInStealthMode;
        private final boolean mInputEnabled;
        private final String mSerializedPattern;
        private final boolean mTactileFeedbackEnabled;

        /* synthetic */ SavedState(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        /* synthetic */ SavedState(Parcelable x0, String x1, int x2, boolean x3, boolean x4, boolean x5, AnonymousClass1 x6) {
            this(x0, x1, x2, x3, x4, x5);
        }

        @UnsupportedAppUsage
        private SavedState(Parcelable superState, String serializedPattern, int displayMode, boolean inputEnabled, boolean inStealthMode, boolean tactileFeedbackEnabled) {
            super(superState);
            this.mSerializedPattern = serializedPattern;
            this.mDisplayMode = displayMode;
            this.mInputEnabled = inputEnabled;
            this.mInStealthMode = inStealthMode;
            this.mTactileFeedbackEnabled = tactileFeedbackEnabled;
        }

        @UnsupportedAppUsage
        private SavedState(Parcel in) {
            super(in);
            this.mSerializedPattern = in.readString();
            this.mDisplayMode = in.readInt();
            this.mInputEnabled = ((Boolean) in.readValue(null)).booleanValue();
            this.mInStealthMode = ((Boolean) in.readValue(null)).booleanValue();
            this.mTactileFeedbackEnabled = ((Boolean) in.readValue(null)).booleanValue();
        }

        public String getSerializedPattern() {
            return this.mSerializedPattern;
        }

        public int getDisplayMode() {
            return this.mDisplayMode;
        }

        public boolean isInputEnabled() {
            return this.mInputEnabled;
        }

        public boolean isInStealthMode() {
            return this.mInStealthMode;
        }

        public boolean isTactileFeedbackEnabled() {
            return this.mTactileFeedbackEnabled;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.mSerializedPattern);
            dest.writeInt(this.mDisplayMode);
            dest.writeValue(Boolean.valueOf(this.mInputEnabled));
            dest.writeValue(Boolean.valueOf(this.mInStealthMode));
            dest.writeValue(Boolean.valueOf(this.mTactileFeedbackEnabled));
        }
    }

    public LockPatternView(Context context) {
        this(context, null);
    }

    @UnsupportedAppUsage
    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDrawingProfilingStarted = false;
        this.mPaint = new Paint();
        this.mPathPaint = new Paint();
        this.mPattern = new ArrayList(9);
        this.mPatternDrawLookup = (boolean[][]) Array.newInstance(boolean.class, new int[]{3, 3});
        this.mInProgressX = -1.0f;
        this.mInProgressY = -1.0f;
        this.mLineFadeStart = new long[9];
        this.mPatternDisplayMode = DisplayMode.Correct;
        this.mInputEnabled = true;
        this.mInStealthMode = false;
        this.mEnableHapticFeedback = true;
        this.mPatternInProgress = false;
        this.mFadePattern = true;
        this.mHitFactor = 0.6f;
        this.mCurrentPath = new Path();
        this.mInvalidate = new Rect();
        this.mTmpInvalidateRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockPatternView, R.attr.lockPatternStyle, R.style.Widget_LockPatternView);
        String aspect = a.getString(0);
        if ("square".equals(aspect)) {
            this.mAspect = 0;
        } else if ("lock_width".equals(aspect)) {
            this.mAspect = 1;
        } else if ("lock_height".equals(aspect)) {
            this.mAspect = 2;
        } else {
            this.mAspect = 0;
        }
        setClickable(true);
        this.mPathPaint.setAntiAlias(true);
        this.mPathPaint.setDither(true);
        this.mRegularColor = a.getColor(3, 0);
        this.mErrorColor = a.getColor(1, 0);
        this.mSuccessColor = a.getColor(4, 0);
        this.mPathPaint.setColor(a.getColor(2, this.mRegularColor));
        this.mPathPaint.setStyle(Style.STROKE);
        this.mPathPaint.setStrokeJoin(Join.ROUND);
        this.mPathPaint.setStrokeCap(Cap.ROUND);
        this.mPathWidth = getResources().getDimensionPixelSize(R.dimen.lock_pattern_dot_line_width);
        this.mPathPaint.setStrokeWidth((float) this.mPathWidth);
        this.mDotSize = getResources().getDimensionPixelSize(R.dimen.lock_pattern_dot_size);
        this.mDotSizeActivated = getResources().getDimensionPixelSize(R.dimen.lock_pattern_dot_size_activated);
        this.mUseLockPatternDrawable = getResources().getBoolean(R.bool.use_lock_pattern_drawable);
        if (this.mUseLockPatternDrawable) {
            this.mSelectedDrawable = getResources().getDrawable(R.drawable.lockscreen_selected);
            this.mNotSelectedDrawable = getResources().getDrawable(R.drawable.lockscreen_notselected);
        }
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mCellStates = (CellState[][]) Array.newInstance(CellState.class, new int[]{3, 3});
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.mCellStates[i][j] = new CellState();
                CellState[][] cellStateArr = this.mCellStates;
                cellStateArr[i][j].radius = (float) (this.mDotSize / 2);
                cellStateArr[i][j].row = i;
                cellStateArr[i][j].col = j;
            }
        }
        this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563661);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mExploreByTouchHelper = new PatternExploreByTouchHelper(this);
        setAccessibilityDelegate(this.mExploreByTouchHelper);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        a.recycle();
    }

    @UnsupportedAppUsage
    public CellState[][] getCellStates() {
        return this.mCellStates;
    }

    public boolean isInStealthMode() {
        return this.mInStealthMode;
    }

    public boolean isTactileFeedbackEnabled() {
        return this.mEnableHapticFeedback;
    }

    @UnsupportedAppUsage
    public void setInStealthMode(boolean inStealthMode) {
        this.mInStealthMode = inStealthMode;
    }

    public void setFadePattern(boolean fadePattern) {
        this.mFadePattern = fadePattern;
    }

    @UnsupportedAppUsage
    public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
        this.mEnableHapticFeedback = tactileFeedbackEnabled;
    }

    @UnsupportedAppUsage
    public void setOnPatternListener(OnPatternListener onPatternListener) {
        this.mOnPatternListener = onPatternListener;
    }

    public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
        this.mPattern.clear();
        this.mPattern.addAll(pattern);
        clearPatternDrawLookup();
        for (Cell cell : pattern) {
            this.mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
        }
        setDisplayMode(displayMode);
    }

    @UnsupportedAppUsage
    public void setDisplayMode(DisplayMode displayMode) {
        this.mPatternDisplayMode = displayMode;
        if (displayMode == DisplayMode.Animate) {
            if (this.mPattern.size() != 0) {
                this.mAnimatingPeriodStart = SystemClock.elapsedRealtime();
                Cell first = (Cell) this.mPattern.get(0);
                this.mInProgressX = getCenterXForColumn(first.getColumn());
                this.mInProgressY = getCenterYForRow(first.getRow());
                clearPatternDrawLookup();
            } else {
                throw new IllegalStateException("you must have a pattern to animate if you want to set the display mode to animate");
            }
        }
        invalidate();
    }

    public void startCellStateAnimation(CellState cellState, float startAlpha, float endAlpha, float startTranslationY, float endTranslationY, float startScale, float endScale, long delay, long duration, Interpolator interpolator, Runnable finishRunnable) {
        if (isHardwareAccelerated()) {
            startCellStateAnimationHw(cellState, startAlpha, endAlpha, startTranslationY, endTranslationY, startScale, endScale, delay, duration, interpolator, finishRunnable);
        } else {
            startCellStateAnimationSw(cellState, startAlpha, endAlpha, startTranslationY, endTranslationY, startScale, endScale, delay, duration, interpolator, finishRunnable);
        }
    }

    private void startCellStateAnimationSw(CellState cellState, float startAlpha, float endAlpha, float startTranslationY, float endTranslationY, float startScale, float endScale, long delay, long duration, Interpolator interpolator, Runnable finishRunnable) {
        CellState cellState2 = cellState;
        cellState2.alpha = startAlpha;
        cellState2.translationY = startTranslationY;
        cellState2.radius = ((float) (this.mDotSize / 2)) * startScale;
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setInterpolator(interpolator);
        final CellState cellState3 = cellState;
        final float f = startAlpha;
        final float f2 = endAlpha;
        AnonymousClass1 anonymousClass1 = r0;
        final float f3 = startTranslationY;
        final float f4 = endTranslationY;
        final float f5 = startScale;
        final float f6 = endScale;
        AnonymousClass1 anonymousClass12 = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float t = ((Float) animation.getAnimatedValue()).floatValue();
                CellState cellState = cellState3;
                cellState.alpha = ((1.0f - t) * f) + (f2 * t);
                cellState.translationY = ((1.0f - t) * f3) + (f4 * t);
                cellState.radius = ((float) (LockPatternView.this.mDotSize / 2)) * (((1.0f - t) * f5) + (f6 * t));
                LockPatternView.this.invalidate();
            }
        };
        animator.addUpdateListener(anonymousClass1);
        final Runnable runnable = finishRunnable;
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        animator.start();
    }

    private void startCellStateAnimationHw(final CellState cellState, float startAlpha, float endAlpha, float startTranslationY, float endTranslationY, float startScale, float endScale, long delay, long duration, Interpolator interpolator, Runnable finishRunnable) {
        CellState cellState2 = cellState;
        float f = endTranslationY;
        cellState2.alpha = endAlpha;
        cellState2.translationY = f;
        cellState2.radius = ((float) (this.mDotSize / 2)) * endScale;
        cellState2.hwAnimating = true;
        cellState2.hwCenterY = CanvasProperty.createFloat(getCenterYForRow(cellState2.row) + startTranslationY);
        cellState2.hwCenterX = CanvasProperty.createFloat(getCenterXForColumn(cellState2.col));
        cellState2.hwRadius = CanvasProperty.createFloat(((float) (this.mDotSize / 2)) * startScale);
        this.mPaint.setColor(getCurrentColor(false));
        this.mPaint.setAlpha((int) (255.0f * startAlpha));
        cellState2.hwPaint = CanvasProperty.createPaint(new Paint(this.mPaint));
        long j = delay;
        long j2 = duration;
        Interpolator interpolator2 = interpolator;
        startRtFloatAnimation(cellState2.hwCenterY, getCenterYForRow(cellState2.row) + f, j, j2, interpolator2);
        startRtFloatAnimation(cellState2.hwRadius, ((float) (this.mDotSize / 2)) * endScale, j, j2, interpolator2);
        final Runnable runnable = finishRunnable;
        startRtAlphaAnimation(cellState, endAlpha, j, j2, interpolator2, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                cellState.hwAnimating = false;
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        invalidate();
    }

    private void startRtAlphaAnimation(CellState cellState, float endAlpha, long delay, long duration, Interpolator interpolator, AnimatorListener listener) {
        RenderNodeAnimator animator = new RenderNodeAnimator(cellState.hwPaint, 1, (float) ((int) (255.0f * endAlpha)));
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setInterpolator(interpolator);
        animator.setTarget((View) this);
        animator.addListener(listener);
        animator.start();
    }

    private void startRtFloatAnimation(CanvasProperty<Float> property, float endValue, long delay, long duration, Interpolator interpolator) {
        RenderNodeAnimator animator = new RenderNodeAnimator((CanvasProperty) property, endValue);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setInterpolator(interpolator);
        animator.setTarget((View) this);
        animator.start();
    }

    private void notifyCellAdded() {
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternCellAdded(this.mPattern);
        }
        this.mExploreByTouchHelper.invalidateRoot();
    }

    private void notifyPatternStarted() {
        sendAccessEvent(R.string.lockscreen_access_pattern_start);
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternStart();
        }
    }

    @UnsupportedAppUsage
    private void notifyPatternDetected() {
        sendAccessEvent(R.string.lockscreen_access_pattern_detected);
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternDetected(this.mPattern);
        }
    }

    private void notifyPatternCleared() {
        sendAccessEvent(R.string.lockscreen_access_pattern_cleared);
        OnPatternListener onPatternListener = this.mOnPatternListener;
        if (onPatternListener != null) {
            onPatternListener.onPatternCleared();
        }
    }

    @UnsupportedAppUsage
    public void clearPattern() {
        resetPattern();
    }

    /* Access modifiers changed, original: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        return super.dispatchHoverEvent(event) | this.mExploreByTouchHelper.dispatchHoverEvent(event);
    }

    private void resetPattern() {
        this.mPattern.clear();
        clearPatternDrawLookup();
        this.mPatternDisplayMode = DisplayMode.Correct;
        invalidate();
    }

    private void clearPatternDrawLookup() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.mPatternDrawLookup[i][j] = false;
                this.mLineFadeStart[(j * 3) + i] = 0;
            }
        }
    }

    @UnsupportedAppUsage
    public void disableInput() {
        this.mInputEnabled = false;
    }

    @UnsupportedAppUsage
    public void enableInput() {
        this.mInputEnabled = true;
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        int width = (w - this.mPaddingLeft) - this.mPaddingRight;
        this.mSquareWidth = ((float) width) / 3.0f;
        int height = (h - this.mPaddingTop) - this.mPaddingBottom;
        this.mSquareHeight = ((float) height) / 3.0f;
        this.mExploreByTouchHelper.invalidateRoot();
        if (this.mUseLockPatternDrawable) {
            this.mNotSelectedDrawable.setBounds(this.mPaddingLeft, this.mPaddingTop, width, height);
            this.mSelectedDrawable.setBounds(this.mPaddingLeft, this.mPaddingTop, width, height);
        }
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == Integer.MIN_VALUE) {
            return Math.max(specSize, desired);
        }
        if (mode != 0) {
            return specSize;
        }
        return desired;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minimumWidth = getSuggestedMinimumWidth();
        int minimumHeight = getSuggestedMinimumHeight();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        int i = this.mAspect;
        if (i == 0) {
            i = Math.min(viewWidth, viewHeight);
            viewHeight = i;
            viewWidth = i;
        } else if (i == 1) {
            viewHeight = Math.min(viewWidth, viewHeight);
        } else if (i == 2) {
            viewWidth = Math.min(viewWidth, viewHeight);
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private Cell detectAndAddHit(float x, float y) {
        Cell cell = checkForNewHit(x, y);
        if (cell == null) {
            return null;
        }
        Cell fillInGapCell = null;
        ArrayList<Cell> pattern = this.mPattern;
        if (!pattern.isEmpty()) {
            Cell lastCell = (Cell) pattern.get(pattern.size() - 1);
            int dRow = cell.row - lastCell.row;
            int dColumn = cell.column - lastCell.column;
            int fillInRow = lastCell.row;
            int fillInColumn = lastCell.column;
            int i = -1;
            if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
                fillInRow = lastCell.row + (dRow > 0 ? 1 : -1);
            }
            if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
                int i2 = lastCell.column;
                if (dColumn > 0) {
                    i = 1;
                }
                fillInColumn = i2 + i;
            }
            fillInGapCell = Cell.of(fillInRow, fillInColumn);
        }
        if (!(fillInGapCell == null || this.mPatternDrawLookup[fillInGapCell.row][fillInGapCell.column])) {
            addCellToPattern(fillInGapCell);
        }
        addCellToPattern(cell);
        if (this.mEnableHapticFeedback) {
            performHapticFeedback(1, 3);
        }
        return cell;
    }

    private void addCellToPattern(Cell newCell) {
        this.mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
        this.mPattern.add(newCell);
        if (!this.mInStealthMode) {
            startCellActivatedAnimation(newCell);
        }
        notifyCellAdded();
    }

    private void startCellActivatedAnimation(Cell cell) {
        final CellState cellState = this.mCellStates[cell.row][cell.column];
        startRadiusAnimation((float) (this.mDotSize / 2), (float) (this.mDotSizeActivated / 2), 96, this.mLinearOutSlowInInterpolator, cellState, new Runnable() {
            public void run() {
                LockPatternView lockPatternView = LockPatternView.this;
                lockPatternView.startRadiusAnimation((float) (lockPatternView.mDotSizeActivated / 2), (float) (LockPatternView.this.mDotSize / 2), 192, LockPatternView.this.mFastOutSlowInInterpolator, cellState, null);
            }
        });
        startLineEndAnimation(cellState, this.mInProgressX, this.mInProgressY, getCenterXForColumn(cell.column), getCenterYForRow(cell.row));
    }

    /* JADX WARNING: Incorrect type for fill-array insn 0x0003, element type: float, insn element type: null */
    private void startLineEndAnimation(final com.android.internal.widget.LockPatternView.CellState r10, float r11, float r12, float r13, float r14) {
        /*
        r9 = this;
        r0 = 2;
        r0 = new float[r0];
        r0 = {0, 1065353216};
        r0 = android.animation.ValueAnimator.ofFloat(r0);
        r8 = new com.android.internal.widget.LockPatternView$5;
        r1 = r8;
        r2 = r9;
        r3 = r10;
        r4 = r11;
        r5 = r13;
        r6 = r12;
        r7 = r14;
        r1.<init>(r3, r4, r5, r6, r7);
        r0.addUpdateListener(r8);
        r1 = new com.android.internal.widget.LockPatternView$6;
        r1.<init>(r10);
        r0.addListener(r1);
        r1 = r9.mFastOutSlowInInterpolator;
        r0.setInterpolator(r1);
        r1 = 100;
        r0.setDuration(r1);
        r0.start();
        r10.lineAnimator = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.LockPatternView.startLineEndAnimation(com.android.internal.widget.LockPatternView$CellState, float, float, float, float):void");
    }

    private void startRadiusAnimation(float start, float end, long duration, Interpolator interpolator, final CellState state, final Runnable endRunnable) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{start, end});
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                state.radius = ((Float) animation.getAnimatedValue()).floatValue();
                LockPatternView.this.invalidate();
            }
        });
        if (endRunnable != null) {
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    endRunnable.run();
                }
            });
        }
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    private Cell checkForNewHit(float x, float y) {
        int rowHit = getRowHit(y);
        if (rowHit < 0) {
            return null;
        }
        int columnHit = getColumnHit(x);
        if (columnHit >= 0 && !this.mPatternDrawLookup[rowHit][columnHit]) {
            return Cell.of(rowHit, columnHit);
        }
        return null;
    }

    private int getRowHit(float y) {
        float squareHeight = this.mSquareHeight;
        float hitSize = this.mHitFactor * squareHeight;
        float offset = ((float) this.mPaddingTop) + ((squareHeight - hitSize) / 2.0f);
        for (int i = 0; i < 3; i++) {
            float hitTop = (((float) i) * squareHeight) + offset;
            if (y >= hitTop && y <= hitTop + hitSize) {
                return i;
            }
        }
        return -1;
    }

    private int getColumnHit(float x) {
        float squareWidth = this.mSquareWidth;
        float hitSize = this.mHitFactor * squareWidth;
        float offset = ((float) this.mPaddingLeft) + ((squareWidth - hitSize) / 2.0f);
        for (int i = 0; i < 3; i++) {
            float hitLeft = (((float) i) * squareWidth) + offset;
            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }
        return -1;
    }

    public boolean onHoverEvent(MotionEvent event) {
        if (AccessibilityManager.getInstance(this.mContext).isTouchExplorationEnabled()) {
            int action = event.getAction();
            if (action == 7) {
                event.setAction(2);
            } else if (action == 9) {
                event.setAction(0);
            } else if (action == 10) {
                event.setAction(1);
            }
            onTouchEvent(event);
            event.setAction(action);
        }
        return super.onHoverEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mInputEnabled || !isEnabled()) {
            return false;
        }
        int action = event.getAction();
        if (action == 0) {
            handleActionDown(event);
            return true;
        } else if (action == 1) {
            handleActionUp();
            return true;
        } else if (action == 2) {
            handleActionMove(event);
            return true;
        } else if (action != 3) {
            return false;
        } else {
            if (this.mPatternInProgress) {
                setPatternInProgress(false);
                resetPattern();
                notifyPatternCleared();
            }
            return true;
        }
    }

    private void setPatternInProgress(boolean progress) {
        this.mPatternInProgress = progress;
        this.mExploreByTouchHelper.invalidateRoot();
    }

    private void handleActionMove(MotionEvent event) {
        int historySize;
        MotionEvent motionEvent = event;
        float radius = (float) this.mPathWidth;
        int historySize2 = event.getHistorySize();
        this.mTmpInvalidateRect.setEmpty();
        boolean invalidateNow = false;
        int i = 0;
        while (i < historySize2 + 1) {
            float radius2;
            boolean invalidateNow2;
            float x = i < historySize2 ? motionEvent.getHistoricalX(i) : event.getX();
            float y = i < historySize2 ? motionEvent.getHistoricalY(i) : event.getY();
            Cell hitCell = detectAndAddHit(x, y);
            int patternSize = this.mPattern.size();
            if (hitCell != null && patternSize == 1) {
                setPatternInProgress(true);
                notifyPatternStarted();
            }
            float dx = Math.abs(x - this.mInProgressX);
            float dy = Math.abs(y - this.mInProgressY);
            if (dx > 0.0f || dy > 0.0f) {
                invalidateNow = true;
            }
            float f;
            Cell cell;
            int i2;
            float f2;
            if (!this.mPatternInProgress || patternSize <= 0) {
                radius2 = radius;
                historySize = historySize2;
                invalidateNow2 = invalidateNow;
                float f3 = x;
                f = y;
                cell = hitCell;
                i2 = patternSize;
                f2 = dx;
            } else {
                Cell lastCell = (Cell) this.mPattern.get(patternSize - 1);
                float lastCellCenterX = getCenterXForColumn(lastCell.column);
                float lastCellCenterY = getCenterYForRow(lastCell.row);
                float left = Math.min(lastCellCenterX, x) - radius;
                historySize = historySize2;
                historySize2 = Math.max(lastCellCenterX, x) + radius;
                invalidateNow2 = invalidateNow;
                invalidateNow = Math.min(lastCellCenterY, y) - radius;
                x = Math.max(lastCellCenterY, y) + radius;
                if (hitCell != null) {
                    radius2 = radius;
                    radius = this.mSquareWidth * 0.5f;
                    y = this.mSquareHeight * 0.5f;
                    patternSize = getCenterXForColumn(hitCell.column);
                    dx = getCenterYForRow(hitCell.row);
                    left = Math.min(patternSize - radius, left);
                    historySize2 = Math.max(patternSize + radius, historySize2);
                    invalidateNow = Math.min(dx - y, invalidateNow);
                    x = Math.max(dx + y, x);
                } else {
                    radius2 = radius;
                    f = y;
                    cell = hitCell;
                    i2 = patternSize;
                    f2 = dx;
                }
                this.mTmpInvalidateRect.union(Math.round(left), Math.round(invalidateNow), Math.round(historySize2), Math.round(x));
            }
            i++;
            motionEvent = event;
            radius = radius2;
            historySize2 = historySize;
            invalidateNow = invalidateNow2;
        }
        historySize = historySize2;
        this.mInProgressX = event.getX();
        this.mInProgressY = event.getY();
        if (invalidateNow) {
            this.mInvalidate.union(this.mTmpInvalidateRect);
            invalidate(this.mInvalidate);
            this.mInvalidate.set(this.mTmpInvalidateRect);
        }
    }

    private void sendAccessEvent(int resId) {
        announceForAccessibility(this.mContext.getString(resId));
    }

    private void handleActionUp() {
        if (!this.mPattern.isEmpty()) {
            setPatternInProgress(false);
            cancelLineAnimations();
            notifyPatternDetected();
            if (this.mFadePattern) {
                clearPatternDrawLookup();
                this.mPatternDisplayMode = DisplayMode.Correct;
            }
            invalidate();
        }
    }

    private void cancelLineAnimations() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CellState state = this.mCellStates[i][j];
                if (state.lineAnimator != null) {
                    state.lineAnimator.cancel();
                    state.lineEndX = Float.MIN_VALUE;
                    state.lineEndY = Float.MIN_VALUE;
                }
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        resetPattern();
        float x = event.getX();
        float y = event.getY();
        Cell hitCell = detectAndAddHit(x, y);
        if (hitCell != null) {
            setPatternInProgress(true);
            this.mPatternDisplayMode = DisplayMode.Correct;
            notifyPatternStarted();
        } else if (this.mPatternInProgress) {
            setPatternInProgress(false);
            notifyPatternCleared();
        }
        if (hitCell != null) {
            float startX = getCenterXForColumn(hitCell.column);
            float startY = getCenterYForRow(hitCell.row);
            float widthOffset = this.mSquareWidth / 2.0f;
            float heightOffset = this.mSquareHeight / 2.0f;
            invalidate((int) (startX - widthOffset), (int) (startY - heightOffset), (int) (startX + widthOffset), (int) (startY + heightOffset));
        }
        this.mInProgressX = x;
        this.mInProgressY = y;
    }

    private float getCenterXForColumn(int column) {
        float f = (float) this.mPaddingLeft;
        float f2 = (float) column;
        float f3 = this.mSquareWidth;
        return (f + (f2 * f3)) + (f3 / 2.0f);
    }

    private float getCenterYForRow(int row) {
        float f = (float) this.mPaddingTop;
        float f2 = (float) row;
        float f3 = this.mSquareHeight;
        return (f + (f2 * f3)) + (f3 / 2.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        float percentageOfNextCircle;
        float centerY;
        int j;
        float translationY;
        Path currentPath;
        float centerX;
        Canvas canvas2 = canvas;
        ArrayList<Cell> pattern = this.mPattern;
        int count = pattern.size();
        boolean[][] drawLookup = this.mPatternDrawLookup;
        if (this.mPatternDisplayMode == DisplayMode.Animate) {
            int oneCycle = (count + 1) * 700;
            int spotInCycle = ((int) (SystemClock.elapsedRealtime() - this.mAnimatingPeriodStart)) % oneCycle;
            int numCircles = spotInCycle / 700;
            clearPatternDrawLookup();
            for (int i = 0; i < numCircles; i++) {
                Cell cell = (Cell) pattern.get(i);
                drawLookup[cell.getRow()][cell.getColumn()] = true;
            }
            boolean needToUpdateInProgressPoint = numCircles > 0 && numCircles < count;
            if (needToUpdateInProgressPoint) {
                percentageOfNextCircle = ((float) (spotInCycle % 700)) / 700.0f;
                Cell currentCell = (Cell) pattern.get(numCircles - 1);
                float centerX2 = getCenterXForColumn(currentCell.column);
                float centerY2 = getCenterYForRow(currentCell.row);
                Cell nextCell = (Cell) pattern.get(numCircles);
                float dy = (getCenterYForRow(nextCell.row) - centerY2) * percentageOfNextCircle;
                this.mInProgressX = centerX2 + ((getCenterXForColumn(nextCell.column) - centerX2) * percentageOfNextCircle);
                this.mInProgressY = centerY2 + dy;
            }
            invalidate();
        }
        Path currentPath2 = this.mCurrentPath;
        currentPath2.rewind();
        int i2 = 0;
        while (true) {
            int i3 = 3;
            if (i2 >= 3) {
                break;
            }
            int j2;
            centerY = getCenterYForRow(i2);
            j = 0;
            while (j < i3) {
                CellState cellState = this.mCellStates[i2][j];
                percentageOfNextCircle = getCenterXForColumn(j);
                translationY = cellState.translationY;
                CellState currentPath3;
                if (this.mUseLockPatternDrawable) {
                    currentPath = currentPath2;
                    currentPath3 = cellState;
                    drawCellDrawable(canvas, i2, j, cellState.radius, drawLookup[i2][j]);
                    j2 = j;
                } else {
                    float translationY2 = translationY;
                    centerX = percentageOfNextCircle;
                    currentPath = currentPath2;
                    currentPath3 = cellState;
                    if (isHardwareAccelerated() && currentPath3.hwAnimating) {
                        ((RecordingCanvas) canvas2).drawCircle(currentPath3.hwCenterX, currentPath3.hwCenterY, currentPath3.hwRadius, currentPath3.hwPaint);
                        j2 = j;
                    } else {
                        j2 = j;
                        drawCircle(canvas, (float) ((int) centerX), ((float) ((int) centerY)) + translationY2, currentPath3.radius, drawLookup[i2][j], currentPath3.alpha);
                    }
                }
                j = j2 + 1;
                currentPath2 = currentPath;
                i3 = 3;
            }
            j2 = j;
            currentPath = currentPath2;
            i2++;
        }
        currentPath = currentPath2;
        boolean drawPath = this.mInStealthMode ^ true;
        Path currentPath4;
        if (drawPath) {
            long j3;
            ArrayList<Cell> currentPath5;
            this.mPathPaint.setColor(getCurrentColor(true));
            boolean anyCircles = false;
            float lastX = 0.0f;
            translationY = 0.0f;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            j = 0;
            while (j < count) {
                Cell cell2 = (Cell) pattern.get(j);
                boolean z;
                if (!drawLookup[cell2.row][cell2.column]) {
                    z = drawPath;
                    j3 = elapsedRealtime;
                    currentPath4 = currentPath;
                    currentPath5 = pattern;
                    break;
                }
                boolean anyCircles2;
                long[] jArr = this.mLineFadeStart;
                if (jArr[j] == 0) {
                    jArr[j] = SystemClock.elapsedRealtime();
                }
                centerX = getCenterXForColumn(cell2.column);
                centerY = getCenterYForRow(cell2.row);
                if (j != 0) {
                    z = drawPath;
                    anyCircles2 = true;
                    drawPath = (int) Math.min(((float) (elapsedRealtime - this.mLineFadeStart[j])) * true, 255.0f);
                    j3 = elapsedRealtime;
                    anyCircles = this.mCellStates[cell2.row][cell2.column];
                    currentPath.rewind();
                    currentPath4 = currentPath;
                    currentPath4.moveTo(lastX, translationY);
                    currentPath5 = pattern;
                    if (anyCircles.lineEndX == Float.MIN_VALUE || anyCircles.lineEndY == Float.MIN_VALUE) {
                        currentPath4.lineTo(centerX, centerY);
                        if (this.mFadePattern) {
                            this.mPathPaint.setAlpha(255 - drawPath);
                        } else {
                            this.mPathPaint.setAlpha(255);
                        }
                    } else {
                        currentPath4.lineTo(anyCircles.lineEndX, anyCircles.lineEndY);
                        if (this.mFadePattern) {
                            this.mPathPaint.setAlpha(255 - drawPath);
                        } else {
                            this.mPathPaint.setAlpha(255);
                        }
                    }
                    canvas2.drawPath(currentPath4, this.mPathPaint);
                } else {
                    z = drawPath;
                    anyCircles2 = true;
                    j3 = elapsedRealtime;
                    currentPath4 = currentPath;
                    currentPath5 = pattern;
                }
                lastX = centerX;
                translationY = centerY;
                j++;
                drawPath = z;
                anyCircles = anyCircles2;
                pattern = currentPath5;
                currentPath = currentPath4;
                elapsedRealtime = j3;
            }
            j3 = elapsedRealtime;
            currentPath4 = currentPath;
            currentPath5 = pattern;
            if ((this.mPatternInProgress || this.mPatternDisplayMode == DisplayMode.Animate) && anyCircles) {
                currentPath4.rewind();
                currentPath4.moveTo(lastX, translationY);
                currentPath4.lineTo(this.mInProgressX, this.mInProgressY);
                this.mPathPaint.setAlpha((int) (calculateLastSegmentAlpha(this.mInProgressX, this.mInProgressY, lastX, translationY) * 255.0f));
                canvas2.drawPath(currentPath4, this.mPathPaint);
                return;
            }
            return;
        }
        currentPath4 = currentPath;
    }

    private float calculateLastSegmentAlpha(float x, float y, float lastX, float lastY) {
        float diffX = x - lastX;
        float diffY = y - lastY;
        return Math.min(1.0f, Math.max(0.0f, ((((float) Math.sqrt((double) ((diffX * diffX) + (diffY * diffY)))) / this.mSquareWidth) - 0.3f) * 4.0f));
    }

    private int getCurrentColor(boolean partOfPattern) {
        if (!partOfPattern || this.mInStealthMode || this.mPatternInProgress) {
            return this.mRegularColor;
        }
        if (this.mPatternDisplayMode == DisplayMode.Wrong) {
            return this.mErrorColor;
        }
        if (this.mPatternDisplayMode == DisplayMode.Correct || this.mPatternDisplayMode == DisplayMode.Animate) {
            return this.mSuccessColor;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unknown display mode ");
        stringBuilder.append(this.mPatternDisplayMode);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void drawCircle(Canvas canvas, float centerX, float centerY, float radius, boolean partOfPattern, float alpha) {
        this.mPaint.setColor(getCurrentColor(partOfPattern));
        this.mPaint.setAlpha((int) (255.0f * alpha));
        canvas.drawCircle(centerX, centerY, radius, this.mPaint);
    }

    private void drawCellDrawable(Canvas canvas, int i, int j, float radius, boolean partOfPattern) {
        Rect dst = new Rect((int) (((float) this.mPaddingLeft) + (((float) j) * this.mSquareWidth)), (int) (((float) this.mPaddingTop) + (((float) i) * this.mSquareHeight)), (int) (((float) this.mPaddingLeft) + (((float) (j + 1)) * this.mSquareWidth)), (int) (((float) this.mPaddingTop) + (((float) (i + 1)) * this.mSquareHeight)));
        float scale = radius / ((float) (this.mDotSize / 2));
        canvas.save();
        canvas.clipRect(dst);
        canvas.scale(scale, scale, (float) dst.centerX(), (float) dst.centerY());
        if (!partOfPattern || scale > 1.0f) {
            this.mNotSelectedDrawable.draw(canvas);
        } else {
            this.mSelectedDrawable.draw(canvas);
        }
        canvas.restore();
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        byte[] patternBytes = LockPatternUtils.patternToByteArray(this.mPattern);
        return new SavedState(superState, patternBytes != null ? new String(patternBytes) : null, this.mPatternDisplayMode.ordinal(), this.mInputEnabled, this.mInStealthMode, this.mEnableHapticFeedback, null);
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setPattern(DisplayMode.Correct, LockPatternUtils.stringToPattern(ss.getSerializedPattern()));
        this.mPatternDisplayMode = DisplayMode.values()[ss.getDisplayMode()];
        this.mInputEnabled = ss.isInputEnabled();
        this.mInStealthMode = ss.isInStealthMode();
        this.mEnableHapticFeedback = ss.isTactileFeedbackEnabled();
    }
}
