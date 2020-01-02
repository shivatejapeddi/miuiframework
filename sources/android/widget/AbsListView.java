package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.os.StrictMode.Span;
import android.os.Trace;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.StateSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Filter.FilterListener;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViewsAdapter.AsyncRemoteAdapterAction;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbsListView extends AdapterView<ListAdapter> implements TextWatcher, OnGlobalLayoutListener, FilterListener, OnTouchModeChangeListener, RemoteAdapterConnectionCallback {
    private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    private static final int INVALID_POINTER = -1;
    static final int LAYOUT_FORCE_BOTTOM = 3;
    static final int LAYOUT_FORCE_TOP = 1;
    static final int LAYOUT_MOVE_SELECTION = 6;
    static final int LAYOUT_NORMAL = 0;
    static final int LAYOUT_SET_SELECTION = 2;
    static final int LAYOUT_SPECIFIC = 4;
    static final int LAYOUT_SYNC = 5;
    private static final double MOVE_TOUCH_SLOP = 0.6d;
    private static final boolean OPTS_INPUT = true;
    static final int OVERSCROLL_LIMIT_DIVISOR = 3;
    private static final boolean PROFILE_FLINGING = false;
    private static final boolean PROFILE_SCROLLING = false;
    private static final String TAG = "AbsListView";
    static final int TOUCH_MODE_DONE_WAITING = 2;
    static final int TOUCH_MODE_DOWN = 0;
    static final int TOUCH_MODE_FLING = 4;
    private static final int TOUCH_MODE_OFF = 1;
    private static final int TOUCH_MODE_ON = 0;
    static final int TOUCH_MODE_OVERFLING = 6;
    static final int TOUCH_MODE_OVERSCROLL = 5;
    static final int TOUCH_MODE_REST = -1;
    static final int TOUCH_MODE_SCROLL = 3;
    static final int TOUCH_MODE_TAP = 1;
    private static final int TOUCH_MODE_UNKNOWN = -1;
    private static final double TOUCH_SLOP_MAX = 1.0d;
    private static final double TOUCH_SLOP_MIN = 0.6d;
    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final int TRANSCRIPT_MODE_DISABLED = 0;
    public static final int TRANSCRIPT_MODE_NORMAL = 1;
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    private ListItemAccessibilityDelegate mAccessibilityDelegate;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mActivePointerId;
    @UnsupportedAppUsage
    ListAdapter mAdapter;
    boolean mAdapterHasStableIds;
    private int mCacheColorHint;
    boolean mCachingActive;
    boolean mCachingStarted;
    SparseBooleanArray mCheckStates;
    LongSparseArray<Integer> mCheckedIdStates;
    int mCheckedItemCount;
    @UnsupportedAppUsage
    ActionMode mChoiceActionMode;
    int mChoiceMode;
    private Runnable mClearScrollingCache;
    @UnsupportedAppUsage
    private ContextMenuInfo mContextMenuInfo;
    @UnsupportedAppUsage
    AdapterDataSetObserver mDataSetObserver;
    private InputConnection mDefInputConnection;
    int mDefaultOverflingDistance;
    int mDefaultOverscrollDistance;
    private boolean mDeferNotifyDataSetChanged;
    private float mDensityScale;
    private int mDirection;
    boolean mDrawSelectorOnTop;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768444)
    EdgeEffect mEdgeGlowBottom;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769408)
    EdgeEffect mEdgeGlowTop;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768941)
    private FastScroller mFastScroll;
    boolean mFastScrollAlwaysVisible;
    boolean mFastScrollEnabled;
    private int mFastScrollStyle;
    private boolean mFiltered;
    private int mFirstPositionDistanceGuess;
    private boolean mFlingProfilingStarted;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    FlingRunnable mFlingRunnable;
    private Span mFlingStrictSpan;
    private boolean mForceTranscriptScroll;
    private boolean mGlobalLayoutListenerAddedFilter;
    private boolean mHasPerformedLongPress;
    @UnsupportedAppUsage
    private boolean mIsChildViewEnabled;
    private boolean mIsDetaching;
    private boolean mIsFirstTouchMoveEvent;
    final boolean[] mIsScrap;
    private int mLastAccessibilityScrollEventFromIndex;
    private int mLastAccessibilityScrollEventToIndex;
    private int mLastHandledItemCount;
    private int mLastPositionDistanceGuess;
    private int mLastScrollState;
    private int mLastTouchMode;
    int mLastY;
    @UnsupportedAppUsage
    int mLayoutMode;
    Rect mListPadding;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124051740)
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    int mMotionCorrection;
    @UnsupportedAppUsage
    int mMotionPosition;
    int mMotionViewNewTop;
    int mMotionViewOriginalTop;
    int mMotionX;
    @UnsupportedAppUsage
    int mMotionY;
    private int mMoveAcceleration;
    MultiChoiceModeWrapper mMultiChoiceModeCallback;
    private int mNestedYOffset;
    private int mNumTouchMoveEvent;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769353)
    private OnScrollListener mOnScrollListener;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769379)
    int mOverflingDistance;
    @UnsupportedAppUsage
    int mOverscrollDistance;
    int mOverscrollMax;
    private final Thread mOwnerThread;
    private CheckForKeyLongPress mPendingCheckForKeyLongPress;
    @UnsupportedAppUsage
    private CheckForLongPress mPendingCheckForLongPress;
    @UnsupportedAppUsage
    private CheckForTap mPendingCheckForTap;
    private SavedState mPendingSync;
    private PerformClick mPerformClick;
    @UnsupportedAppUsage
    PopupWindow mPopup;
    private boolean mPopupHidden;
    Runnable mPositionScrollAfterLayout;
    @UnsupportedAppUsage
    AbsPositionScroller mPositionScroller;
    private InputConnectionWrapper mPublicInputConnection;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769398)
    final RecycleBin mRecycler;
    private RemoteViewsAdapter mRemoteAdapter;
    int mResurrectToPosition;
    private final int[] mScrollConsumed;
    View mScrollDown;
    private final int[] mScrollOffset;
    private boolean mScrollProfilingStarted;
    private Span mScrollStrictSpan;
    View mScrollUp;
    boolean mScrollingCacheEnabled;
    int mSelectedTop;
    @UnsupportedAppUsage
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    @UnsupportedAppUsage
    int mSelectionTopPadding;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    Drawable mSelector;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mSelectorPosition;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    Rect mSelectorRect;
    private int[] mSelectorState;
    private boolean mSmoothScrollbarEnabled;
    boolean mStackFromBottom;
    EditText mTextFilter;
    private boolean mTextFilterEnabled;
    private final float[] mTmpPoint;
    private Rect mTouchFrame;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769413)
    int mTouchMode;
    private Runnable mTouchModeReset;
    @UnsupportedAppUsage
    private int mTouchSlop;
    private int mTranscriptMode;
    boolean mUsingMiuiTheme;
    private float mVelocityScale;
    @UnsupportedAppUsage
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;
    int mWidthMeasureSpec;

    static abstract class AbsPositionScroller {
        public abstract void start(int i);

        public abstract void start(int i, int i2);

        public abstract void startWithOffset(int i, int i2);

        public abstract void startWithOffset(int i, int i2, int i3);

        public abstract void stop();

        AbsPositionScroller() {
        }
    }

    class AdapterDataSetObserver extends AdapterDataSetObserver {
        AdapterDataSetObserver() {
            super();
        }

        public void onChanged() {
            super.onChanged();
            if (AbsListView.this.mFastScroll != null) {
                AbsListView.this.mFastScroll.onSectionsChanged();
            }
        }

        public void onInvalidated() {
            super.onInvalidated();
            if (AbsListView.this.mFastScroll != null) {
                AbsListView.this.mFastScroll.onSectionsChanged();
            }
        }
    }

    private class WindowRunnnable {
        private int mOriginalAttachCount;

        private WindowRunnnable() {
        }

        /* synthetic */ WindowRunnnable(AbsListView x0, AnonymousClass1 x1) {
            this();
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = AbsListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return AbsListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    private class CheckForKeyLongPress extends WindowRunnnable implements Runnable {
        private CheckForKeyLongPress() {
            super(AbsListView.this, null);
        }

        /* synthetic */ CheckForKeyLongPress(AbsListView x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (AbsListView.this.isPressed() && AbsListView.this.mSelectedPosition >= 0) {
                View v = AbsListView.this.getChildAt(AbsListView.this.mSelectedPosition - AbsListView.this.mFirstPosition);
                if (AbsListView.this.mDataChanged) {
                    AbsListView.this.setPressed(false);
                    if (v != null) {
                        v.setPressed(false);
                        return;
                    }
                    return;
                }
                boolean handled = false;
                if (sameWindow()) {
                    AbsListView absListView = AbsListView.this;
                    handled = absListView.performLongPress(v, absListView.mSelectedPosition, AbsListView.this.mSelectedRowId);
                }
                if (handled) {
                    AbsListView.this.setPressed(false);
                    v.setPressed(false);
                }
            }
        }
    }

    private class CheckForLongPress extends WindowRunnnable implements Runnable {
        private static final int INVALID_COORD = -1;
        private float mX;
        private float mY;

        private CheckForLongPress() {
            super(AbsListView.this, null);
            this.mX = -1.0f;
            this.mY = -1.0f;
        }

        /* synthetic */ CheckForLongPress(AbsListView x0, AnonymousClass1 x1) {
            this();
        }

        private void setCoords(float x, float y) {
            this.mX = x;
            this.mY = y;
        }

        public void run() {
            int motionPosition = AbsListView.this.mMotionPosition;
            View child = AbsListView.this;
            child = child.getChildAt(motionPosition - child.mFirstPosition);
            if (child != null) {
                int longPressPosition = AbsListView.this.mMotionPosition;
                long longPressId = AbsListView.this.mAdapter.getItemId(AbsListView.this.mMotionPosition);
                boolean handled = false;
                if (sameWindow() && !AbsListView.this.mDataChanged) {
                    float f = this.mX;
                    if (f != -1.0f) {
                        float f2 = this.mY;
                        if (f2 != -1.0f) {
                            handled = AbsListView.this.performLongPress(child, longPressPosition, longPressId, f, f2);
                        }
                    }
                    handled = AbsListView.this.performLongPress(child, longPressPosition, longPressId);
                }
                if (handled) {
                    AbsListView.this.mHasPerformedLongPress = true;
                    AbsListView absListView = AbsListView.this;
                    absListView.mTouchMode = -1;
                    absListView.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                AbsListView.this.mTouchMode = 2;
            }
        }
    }

    private final class CheckForTap implements Runnable {
        float x;
        float y;

        private CheckForTap() {
        }

        /* synthetic */ CheckForTap(AbsListView x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (AbsListView.this.mTouchMode == 0) {
                View child = AbsListView.this;
                child.mTouchMode = 1;
                child = child.getChildAt(child.mMotionPosition - AbsListView.this.mFirstPosition);
                if (child != null && !child.hasExplicitFocusable()) {
                    AbsListView absListView = AbsListView.this;
                    absListView.mLayoutMode = 0;
                    if (absListView.mDataChanged) {
                        AbsListView.this.mTouchMode = 2;
                        return;
                    }
                    float[] point = AbsListView.this.mTmpPoint;
                    point[0] = this.x;
                    point[1] = this.y;
                    AbsListView.this.transformPointToViewLocal(point, child);
                    child.drawableHotspotChanged(point[0], point[1]);
                    child.setPressed(true);
                    AbsListView.this.setPressed(true);
                    AbsListView.this.layoutChildren();
                    AbsListView absListView2 = AbsListView.this;
                    absListView2.positionSelector(absListView2.mMotionPosition, child);
                    AbsListView.this.refreshDrawableState();
                    int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    boolean longClickable = AbsListView.this.isLongClickable();
                    if (AbsListView.this.mSelector != null) {
                        Drawable d = AbsListView.this.mSelector.getCurrent();
                        if (d != null && (d instanceof TransitionDrawable)) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        AbsListView.this.mSelector.setHotspot(this.x, this.y);
                    }
                    if (longClickable) {
                        AbsListView absListView3;
                        if (AbsListView.this.mPendingCheckForLongPress == null) {
                            absListView3 = AbsListView.this;
                            absListView3.mPendingCheckForLongPress = new CheckForLongPress(absListView3, null);
                        }
                        AbsListView.this.mPendingCheckForLongPress.setCoords(this.x, this.y);
                        AbsListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                        absListView3 = AbsListView.this;
                        absListView3.postDelayed(absListView3.mPendingCheckForLongPress, (long) longPressTimeout);
                        return;
                    }
                    AbsListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private static final int FLYWHEEL_TIMEOUT = 40;
        private final Runnable mCheckFlywheel = new Runnable() {
            public void run() {
                int activeId = AbsListView.this.mActivePointerId;
                VelocityTracker vt = AbsListView.this.mVelocityTracker;
                OverScroller scroller = FlingRunnable.this.mScroller;
                if (vt != null && activeId != -1) {
                    vt.computeCurrentVelocity(1000, (float) AbsListView.this.mMaximumVelocity);
                    float yvel = -vt.getYVelocity(activeId);
                    if (Math.abs(yvel) < ((float) AbsListView.this.mMinimumVelocity) || !scroller.isScrollingInDirection(0.0f, yvel)) {
                        FlingRunnable.this.endFling();
                        AbsListView.this.mTouchMode = 3;
                        AbsListView.this.reportScrollStateChange(1);
                    } else {
                        AbsListView.this.postDelayed(this, 40);
                    }
                }
            }
        };
        private int mLastFlingY;
        @UnsupportedAppUsage
        OverScroller mScroller;
        boolean mSuppressIdleStateChangeCall;

        FlingRunnable() {
            this.mScroller = new OverScroller(AbsListView.this.getContext());
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage(maxTargetSdk = 28)
        public void start(int initialVelocity) {
            int initialY = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsListView absListView = AbsListView.this;
            absListView.mTouchMode = 4;
            this.mSuppressIdleStateChangeCall = false;
            absListView.postOnAnimation(this);
            if (AbsListView.this.mFlingStrictSpan == null) {
                AbsListView.this.mFlingStrictSpan = StrictMode.enterCriticalSpan("AbsListView-fling");
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void startSpringback() {
            this.mSuppressIdleStateChangeCall = false;
            if (this.mScroller.springBack(0, AbsListView.this.mScrollY, 0, 0, 0, 0)) {
                AbsListView absListView = AbsListView.this;
                absListView.mTouchMode = 6;
                absListView.invalidate();
                AbsListView.this.postOnAnimation(this);
                return;
            }
            AbsListView absListView2 = AbsListView.this;
            absListView2.mTouchMode = -1;
            absListView2.reportScrollStateChange(0);
        }

        /* Access modifiers changed, original: 0000 */
        public void startOverfling(int initialVelocity) {
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, AbsListView.this.mScrollY, 0, initialVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, AbsListView.this.getHeight());
            AbsListView absListView = AbsListView.this;
            absListView.mTouchMode = 6;
            this.mSuppressIdleStateChangeCall = false;
            absListView.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        /* Access modifiers changed, original: 0000 */
        public void edgeReached(int delta) {
            this.mScroller.notifyVerticalEdgeReached(AbsListView.this.mScrollY, 0, AbsListView.this.mOverflingDistance);
            int overscrollMode = AbsListView.this.getOverScrollMode();
            if (overscrollMode == 0 || (overscrollMode == 1 && !AbsListView.this.contentFits())) {
                AbsListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsListView.this.mEdgeGlowTop.onAbsorb(vel);
                } else {
                    AbsListView.this.mEdgeGlowBottom.onAbsorb(vel);
                }
            } else {
                AbsListView absListView = AbsListView.this;
                absListView.mTouchMode = -1;
                if (absListView.mPositionScroller != null) {
                    AbsListView.this.mPositionScroller.stop();
                }
            }
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        /* Access modifiers changed, original: 0000 */
        public void startScroll(int distance, int duration, boolean linear, boolean suppressEndFlingStateChangeCall) {
            int initialY = distance < 0 ? Integer.MAX_VALUE : 0;
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(linear ? AbsListView.sLinearInterpolator : null);
            this.mScroller.startScroll(0, initialY, 0, distance, duration);
            AbsListView absListView = AbsListView.this;
            absListView.mTouchMode = 4;
            this.mSuppressIdleStateChangeCall = suppressEndFlingStateChangeCall;
            absListView.postOnAnimation(this);
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage(maxTargetSdk = 28)
        public void endFling() {
            AbsListView absListView = AbsListView.this;
            absListView.mTouchMode = -1;
            absListView.removeCallbacks(this);
            AbsListView.this.removeCallbacks(this.mCheckFlywheel);
            if (!this.mSuppressIdleStateChangeCall) {
                AbsListView.this.reportScrollStateChange(0);
            }
            AbsListView.this.clearScrollingCache();
            this.mScroller.abortAnimation();
            if (AbsListView.this.mFlingStrictSpan != null) {
                AbsListView.this.mFlingStrictSpan.finish();
                AbsListView.this.mFlingStrictSpan = null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void flywheelTouch() {
            AbsListView.this.postDelayed(this.mCheckFlywheel, 40);
        }

        public void run() {
            OverScroller scroller;
            int velocity;
            int i = AbsListView.this.mTouchMode;
            boolean crossUp = false;
            if (i != 3) {
                if (i != 4) {
                    if (i != 6) {
                        endFling();
                        return;
                    }
                    scroller = this.mScroller;
                    if (scroller.computeScrollOffset()) {
                        int scrollY = AbsListView.this.mScrollY;
                        int currY = scroller.getCurrY();
                        int deltaY = currY - scrollY;
                        AbsListView absListView = AbsListView.this;
                        if (absListView.overScrollBy(0, deltaY, 0, scrollY, 0, 0, 0, absListView.mOverflingDistance, false)) {
                            boolean crossDown = scrollY <= 0 && currY > 0;
                            if (scrollY >= 0 && currY < 0) {
                                crossUp = true;
                            }
                            if (crossDown || crossUp) {
                                velocity = (int) scroller.getCurrVelocity();
                                if (crossUp) {
                                    velocity = -velocity;
                                }
                                scroller.abortAnimation();
                                start(velocity);
                            } else {
                                startSpringback();
                            }
                        } else {
                            AbsListView.this.invalidate();
                            AbsListView.this.postOnAnimation(this);
                        }
                    } else {
                        endFling();
                    }
                }
            } else if (this.mScroller.isFinished()) {
                return;
            }
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.layoutChildren();
            }
            if (AbsListView.this.mItemCount == 0 || AbsListView.this.getChildCount() == 0) {
                endFling();
                return;
            }
            scroller = this.mScroller;
            boolean more = scroller.computeScrollOffset();
            int y = scroller.getCurrY();
            int delta = this.mLastFlingY - y;
            if (delta > 0) {
                AbsListView absListView2 = AbsListView.this;
                absListView2.mMotionPosition = absListView2.mFirstPosition;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(0).getTop();
                delta = Math.min(((AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom) - AbsListView.this.mPaddingTop) - 1, delta);
            } else {
                int offsetToLast = AbsListView.this.getChildCount() - 1;
                AbsListView absListView3 = AbsListView.this;
                absListView3.mMotionPosition = absListView3.mFirstPosition + offsetToLast;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(offsetToLast).getTop();
                delta = Math.max(-(((AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom) - AbsListView.this.mPaddingTop) - 1), delta);
            }
            View motionView = AbsListView.this;
            motionView = motionView.getChildAt(motionView.mMotionPosition - AbsListView.this.mFirstPosition);
            int oldTop = 0;
            if (motionView != null) {
                oldTop = motionView.getTop();
            }
            boolean atEdge = AbsListView.this.trackMotionScroll(delta, delta);
            if (atEdge && delta != 0) {
                crossUp = true;
            }
            if (crossUp) {
                if (motionView != null) {
                    velocity = -(delta - (motionView.getTop() - oldTop));
                    AbsListView absListView4 = AbsListView.this;
                    absListView4.overScrollBy(0, velocity, 0, absListView4.mScrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false);
                }
                if (more) {
                    edgeReached(delta);
                }
            } else if (!more || crossUp) {
                endFling();
            } else {
                if (atEdge) {
                    AbsListView.this.invalidate();
                }
                this.mLastFlingY = y;
                AbsListView.this.postOnAnimation(this);
            }
        }
    }

    private class InputConnectionWrapper implements InputConnection {
        private final EditorInfo mOutAttrs;
        private InputConnection mTarget;

        public InputConnectionWrapper(EditorInfo outAttrs) {
            this.mOutAttrs = outAttrs;
        }

        private InputConnection getTarget() {
            if (this.mTarget == null) {
                this.mTarget = AbsListView.this.getTextFilterInput().onCreateInputConnection(this.mOutAttrs);
            }
            return this.mTarget;
        }

        public boolean reportFullscreenMode(boolean enabled) {
            return AbsListView.this.mDefInputConnection.reportFullscreenMode(enabled);
        }

        public boolean performEditorAction(int editorAction) {
            if (editorAction != 6) {
                return false;
            }
            InputMethodManager imm = (InputMethodManager) AbsListView.this.getContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                imm.hideSoftInputFromWindow(AbsListView.this.getWindowToken(), 0);
            }
            return true;
        }

        public boolean sendKeyEvent(KeyEvent event) {
            return AbsListView.this.mDefInputConnection.sendKeyEvent(event);
        }

        public CharSequence getTextBeforeCursor(int n, int flags) {
            InputConnection inputConnection = this.mTarget;
            if (inputConnection == null) {
                return "";
            }
            return inputConnection.getTextBeforeCursor(n, flags);
        }

        public CharSequence getTextAfterCursor(int n, int flags) {
            InputConnection inputConnection = this.mTarget;
            if (inputConnection == null) {
                return "";
            }
            return inputConnection.getTextAfterCursor(n, flags);
        }

        public CharSequence getSelectedText(int flags) {
            InputConnection inputConnection = this.mTarget;
            if (inputConnection == null) {
                return "";
            }
            return inputConnection.getSelectedText(flags);
        }

        public int getCursorCapsMode(int reqModes) {
            InputConnection inputConnection = this.mTarget;
            if (inputConnection == null) {
                return 16384;
            }
            return inputConnection.getCursorCapsMode(reqModes);
        }

        public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
            return getTarget().getExtractedText(request, flags);
        }

        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return getTarget().deleteSurroundingText(beforeLength, afterLength);
        }

        public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
            return getTarget().deleteSurroundingTextInCodePoints(beforeLength, afterLength);
        }

        public boolean setComposingText(CharSequence text, int newCursorPosition) {
            return getTarget().setComposingText(text, newCursorPosition);
        }

        public boolean setComposingRegion(int start, int end) {
            return getTarget().setComposingRegion(start, end);
        }

        public boolean finishComposingText() {
            InputConnection inputConnection = this.mTarget;
            return inputConnection == null || inputConnection.finishComposingText();
        }

        public boolean commitText(CharSequence text, int newCursorPosition) {
            return getTarget().commitText(text, newCursorPosition);
        }

        public boolean commitCompletion(CompletionInfo text) {
            return getTarget().commitCompletion(text);
        }

        public boolean commitCorrection(CorrectionInfo correctionInfo) {
            return getTarget().commitCorrection(correctionInfo);
        }

        public boolean setSelection(int start, int end) {
            return getTarget().setSelection(start, end);
        }

        public boolean performContextMenuAction(int id) {
            return getTarget().performContextMenuAction(id);
        }

        public boolean beginBatchEdit() {
            return getTarget().beginBatchEdit();
        }

        public boolean endBatchEdit() {
            return getTarget().endBatchEdit();
        }

        public boolean clearMetaKeyStates(int states) {
            return getTarget().clearMetaKeyStates(states);
        }

        public boolean performPrivateCommand(String action, Bundle data) {
            return getTarget().performPrivateCommand(action, data);
        }

        public boolean requestCursorUpdates(int cursorUpdateMode) {
            return getTarget().requestCursorUpdates(cursorUpdateMode);
        }

        public Handler getHandler() {
            return getTarget().getHandler();
        }

        public void closeConnection() {
            getTarget().closeConnection();
        }

        public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
            return getTarget().commitContent(inputContentInfo, flags, opts);
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<AbsListView> {
        private int mCacheColorHintId;
        private int mChoiceModeId;
        private int mDrawSelectorOnTopId;
        private int mFastScrollEnabledId;
        private int mListSelectorId;
        private boolean mPropertiesMapped = false;
        private int mScrollingCacheId;
        private int mSmoothScrollbarId;
        private int mStackFromBottomId;
        private int mTextFilterEnabledId;
        private int mTranscriptModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mCacheColorHintId = propertyMapper.mapColor("cacheColorHint", 16843009);
            SparseArray<String> choiceModeEnumMapping = new SparseArray();
            choiceModeEnumMapping.put(0, "none");
            choiceModeEnumMapping.put(1, "singleChoice");
            choiceModeEnumMapping.put(2, "multipleChoice");
            choiceModeEnumMapping.put(3, "multipleChoiceModal");
            Objects.requireNonNull(choiceModeEnumMapping);
            this.mChoiceModeId = propertyMapper.mapIntEnum("choiceMode", 16843051, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(choiceModeEnumMapping));
            this.mDrawSelectorOnTopId = propertyMapper.mapBoolean("drawSelectorOnTop", 16843004);
            this.mFastScrollEnabledId = propertyMapper.mapBoolean("fastScrollEnabled", 16843302);
            this.mListSelectorId = propertyMapper.mapObject("listSelector", 16843003);
            this.mScrollingCacheId = propertyMapper.mapBoolean("scrollingCache", 16843006);
            this.mSmoothScrollbarId = propertyMapper.mapBoolean("smoothScrollbar", 16843313);
            this.mStackFromBottomId = propertyMapper.mapBoolean("stackFromBottom", 16843005);
            this.mTextFilterEnabledId = propertyMapper.mapBoolean("textFilterEnabled", 16843007);
            SparseArray<String> transcriptModeEnumMapping = new SparseArray();
            transcriptModeEnumMapping.put(0, "disabled");
            transcriptModeEnumMapping.put(1, "normal");
            transcriptModeEnumMapping.put(2, "alwaysScroll");
            Objects.requireNonNull(transcriptModeEnumMapping);
            this.mTranscriptModeId = propertyMapper.mapIntEnum("transcriptMode", 16843008, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(transcriptModeEnumMapping));
            this.mPropertiesMapped = true;
        }

        public void readProperties(AbsListView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readColor(this.mCacheColorHintId, node.getCacheColorHint());
                propertyReader.readIntEnum(this.mChoiceModeId, node.getChoiceMode());
                propertyReader.readBoolean(this.mDrawSelectorOnTopId, node.isDrawSelectorOnTop());
                propertyReader.readBoolean(this.mFastScrollEnabledId, node.isFastScrollEnabled());
                propertyReader.readObject(this.mListSelectorId, node.getSelector());
                propertyReader.readBoolean(this.mScrollingCacheId, node.isScrollingCacheEnabled());
                propertyReader.readBoolean(this.mSmoothScrollbarId, node.isSmoothScrollbarEnabled());
                propertyReader.readBoolean(this.mStackFromBottomId, node.isStackFromBottom());
                propertyReader.readBoolean(this.mTextFilterEnabledId, node.isTextFilterEnabled());
                propertyReader.readIntEnum(this.mTranscriptModeId, node.getTranscriptMode());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        @ExportedProperty(category = "list")
        boolean forceAdd;
        boolean isEnabled;
        long itemId = -1;
        @ExportedProperty(category = "list")
        boolean recycledHeaderFooter;
        @UnsupportedAppUsage
        int scrappedFromPosition;
        @ExportedProperty(category = "list", mapping = {@IntToString(from = -1, to = "ITEM_VIEW_TYPE_IGNORE"), @IntToString(from = -2, to = "ITEM_VIEW_TYPE_HEADER_OR_FOOTER")})
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("list:viewType", this.viewType);
            encoder.addProperty("list:recycledHeaderFooter", this.recycledHeaderFooter);
            encoder.addProperty("list:forceAdd", this.forceAdd);
            encoder.addProperty("list:isEnabled", this.isEnabled);
        }
    }

    class ListItemAccessibilityDelegate extends AccessibilityDelegate {
        ListItemAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            AbsListView.this.onInitializeAccessibilityNodeInfoForItem(host, AbsListView.this.getPositionForView(host), info);
        }

        /* JADX WARNING: Missing block: B:47:0x009a, code skipped:
            return false;
     */
        public boolean performAccessibilityAction(android.view.View r8, int r9, android.os.Bundle r10) {
            /*
            r7 = this;
            r0 = super.performAccessibilityAction(r8, r9, r10);
            r1 = 1;
            if (r0 == 0) goto L_0x0008;
        L_0x0007:
            return r1;
        L_0x0008:
            r0 = android.widget.AbsListView.this;
            r0 = r0.getPositionForView(r8);
            r2 = -1;
            r3 = 0;
            if (r0 == r2) goto L_0x009a;
        L_0x0012:
            r4 = android.widget.AbsListView.this;
            r4 = r4.mAdapter;
            if (r4 != 0) goto L_0x001a;
        L_0x0018:
            goto L_0x009a;
        L_0x001a:
            r4 = android.widget.AbsListView.this;
            r4 = r4.mAdapter;
            r4 = r4.getCount();
            if (r0 < r4) goto L_0x0025;
        L_0x0024:
            return r3;
        L_0x0025:
            r4 = r8.getLayoutParams();
            r5 = r4 instanceof android.widget.AbsListView.LayoutParams;
            if (r5 == 0) goto L_0x0033;
        L_0x002d:
            r5 = r4;
            r5 = (android.widget.AbsListView.LayoutParams) r5;
            r5 = r5.isEnabled;
            goto L_0x0034;
        L_0x0033:
            r5 = 0;
        L_0x0034:
            r6 = android.widget.AbsListView.this;
            r6 = r6.isEnabled();
            if (r6 == 0) goto L_0x0099;
        L_0x003c:
            if (r5 != 0) goto L_0x003f;
        L_0x003e:
            goto L_0x0099;
        L_0x003f:
            r6 = 4;
            if (r9 == r6) goto L_0x008a;
        L_0x0042:
            r6 = 8;
            if (r9 == r6) goto L_0x007b;
        L_0x0046:
            r1 = 16;
            if (r9 == r1) goto L_0x0065;
        L_0x004a:
            r1 = 32;
            if (r9 == r1) goto L_0x004f;
        L_0x004e:
            return r3;
        L_0x004f:
            r1 = android.widget.AbsListView.this;
            r1 = r1.isLongClickable();
            if (r1 == 0) goto L_0x0064;
        L_0x0057:
            r1 = android.widget.AbsListView.this;
            r1 = r1.getItemIdAtPosition(r0);
            r3 = android.widget.AbsListView.this;
            r3 = r3.performLongPress(r8, r0, r1);
            return r3;
        L_0x0064:
            return r3;
        L_0x0065:
            r1 = android.widget.AbsListView.this;
            r1 = r1.isItemClickable(r8);
            if (r1 == 0) goto L_0x007a;
        L_0x006d:
            r1 = android.widget.AbsListView.this;
            r1 = r1.getItemIdAtPosition(r0);
            r3 = android.widget.AbsListView.this;
            r3 = r3.performItemClick(r8, r0, r1);
            return r3;
        L_0x007a:
            return r3;
        L_0x007b:
            r6 = android.widget.AbsListView.this;
            r6 = r6.getSelectedItemPosition();
            if (r6 != r0) goto L_0x0089;
        L_0x0083:
            r3 = android.widget.AbsListView.this;
            r3.setSelection(r2);
            return r1;
        L_0x0089:
            return r3;
        L_0x008a:
            r2 = android.widget.AbsListView.this;
            r2 = r2.getSelectedItemPosition();
            if (r2 == r0) goto L_0x0098;
        L_0x0092:
            r2 = android.widget.AbsListView.this;
            r2.setSelection(r0);
            return r1;
        L_0x0098:
            return r3;
        L_0x0099:
            return r3;
        L_0x009a:
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.AbsListView$ListItemAccessibilityDelegate.performAccessibilityAction(android.view.View, int, android.os.Bundle):boolean");
        }
    }

    public interface MultiChoiceModeListener extends Callback {
        void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z);
    }

    class MultiChoiceModeWrapper implements MultiChoiceModeListener {
        private MultiChoiceModeListener mWrapped;

        MultiChoiceModeWrapper() {
        }

        public void setWrapped(MultiChoiceModeListener wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean hasWrappedCallback() {
            return this.mWrapped != null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if (!this.mWrapped.onCreateActionMode(mode, menu)) {
                return false;
            }
            AbsListView.this.setLongClickable(false);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrapped.onDestroyActionMode(mode);
            AbsListView absListView = AbsListView.this;
            absListView.mChoiceActionMode = null;
            absListView.clearChoices();
            absListView = AbsListView.this;
            absListView.mDataChanged = true;
            absListView.rememberSyncState();
            AbsListView.this.requestLayout();
            AbsListView.this.setLongClickable(true);
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            this.mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
            if (AbsListView.this.getCheckedItemCount() == 0) {
                mode.finish();
            }
        }
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScroll(AbsListView absListView, int i, int i2, int i3);

        void onScrollStateChanged(AbsListView absListView, int i);
    }

    class OverFlingRunnable extends FlingRunnable {
        OverFlingRunnable() {
            super();
            OverScrollLogger.debug("startOverfling: replacing default configuration.");
            this.mScroller = new DynamicOverScroller(AbsListView.this.getContext());
        }

        /* Access modifiers changed, original: 0000 */
        public void startOverfling(int initialVelocity) {
            AbsListViewInjector.startOverfling(AbsListView.this, this, initialVelocity);
            this.mSuppressIdleStateChangeCall = false;
        }

        public void run() {
            AbsListViewInjector.doAnimationFrame(AbsListView.this, this);
        }

        /* Access modifiers changed, original: 0000 */
        public void superDoAnimationFrame() {
            super.run();
        }
    }

    private class PerformClick extends WindowRunnnable implements Runnable {
        int mClickMotionPosition;

        private PerformClick() {
            super(AbsListView.this, null);
        }

        /* synthetic */ PerformClick(AbsListView x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (!AbsListView.this.mDataChanged) {
                ListAdapter adapter = AbsListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && AbsListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow() && adapter.isEnabled(motionPosition)) {
                    View view = AbsListView.this;
                    view = view.getChildAt(motionPosition - view.mFirstPosition);
                    if (view != null) {
                        AbsListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                    }
                }
            }
        }
    }

    class PositionScroller extends AbsPositionScroller implements Runnable {
        private static final int MOVE_DOWN_BOUND = 3;
        private static final int MOVE_DOWN_POS = 1;
        private static final int MOVE_OFFSET = 5;
        private static final int MOVE_UP_BOUND = 4;
        private static final int MOVE_UP_POS = 2;
        private static final int SCROLL_DURATION = 200;
        private int mBoundPos;
        private final int mExtraScroll;
        private int mLastSeenPos;
        private int mMode;
        private int mOffsetFromTop;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(AbsListView.this.mContext).getScaledFadingEdgeLength();
        }

        public void start(final int position) {
            stop();
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position);
                    }
                };
                return;
            }
            int childCount = AbsListView.this.getChildCount();
            if (childCount != 0) {
                int viewTravelCount;
                int firstPos = AbsListView.this.mFirstPosition;
                int lastPos = (firstPos + childCount) - 1;
                int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                if (clampedPosition < firstPos) {
                    viewTravelCount = (firstPos - clampedPosition) + 1;
                    this.mMode = 2;
                } else if (clampedPosition > lastPos) {
                    viewTravelCount = (clampedPosition - lastPos) + 1;
                    this.mMode = 1;
                } else {
                    scrollToVisible(clampedPosition, -1, 200);
                    return;
                }
                if (viewTravelCount > 0) {
                    this.mScrollDuration = 200 / viewTravelCount;
                } else {
                    this.mScrollDuration = 200;
                }
                this.mTargetPos = clampedPosition;
                this.mBoundPos = -1;
                this.mLastSeenPos = -1;
                AbsListView.this.postOnAnimation(this);
            }
        }

        public void start(final int position, final int boundPosition) {
            stop();
            if (boundPosition == -1) {
                start(position);
            } else if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position, boundPosition);
                    }
                };
            } else {
                int childCount = AbsListView.this.getChildCount();
                if (childCount != 0) {
                    int viewTravelCount;
                    int firstPos = AbsListView.this.mFirstPosition;
                    int lastPos = (firstPos + childCount) - 1;
                    int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                    int boundPosFromLast;
                    int posTravel;
                    if (clampedPosition < firstPos) {
                        boundPosFromLast = lastPos - boundPosition;
                        if (boundPosFromLast >= 1) {
                            posTravel = (firstPos - clampedPosition) + 1;
                            int boundTravel = boundPosFromLast - 1;
                            if (boundTravel < posTravel) {
                                viewTravelCount = boundTravel;
                                this.mMode = 4;
                            } else {
                                viewTravelCount = posTravel;
                                this.mMode = 2;
                            }
                        } else {
                            return;
                        }
                    } else if (clampedPosition > lastPos) {
                        boundPosFromLast = boundPosition - firstPos;
                        if (boundPosFromLast >= 1) {
                            posTravel = (clampedPosition - lastPos) + 1;
                            viewTravelCount = boundPosFromLast - 1;
                            if (viewTravelCount < posTravel) {
                                this.mMode = 3;
                            } else {
                                int viewTravelCount2 = posTravel;
                                this.mMode = 1;
                                viewTravelCount = viewTravelCount2;
                            }
                        } else {
                            return;
                        }
                    } else {
                        scrollToVisible(clampedPosition, boundPosition, 200);
                        return;
                    }
                    if (viewTravelCount > 0) {
                        this.mScrollDuration = 200 / viewTravelCount;
                    } else {
                        this.mScrollDuration = 200;
                    }
                    this.mTargetPos = clampedPosition;
                    this.mBoundPos = boundPosition;
                    this.mLastSeenPos = -1;
                    AbsListView.this.postOnAnimation(this);
                }
            }
        }

        public void startWithOffset(int position, int offset) {
            startWithOffset(position, offset, 200);
        }

        public void startWithOffset(final int position, int offset, final int duration) {
            stop();
            final int postOffset;
            if (AbsListView.this.mDataChanged) {
                postOffset = offset;
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.startWithOffset(position, postOffset, duration);
                    }
                };
                return;
            }
            postOffset = AbsListView.this.getChildCount();
            if (postOffset != 0) {
                int viewTravelCount;
                offset += AbsListView.this.getPaddingTop();
                this.mTargetPos = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                this.mOffsetFromTop = offset;
                this.mBoundPos = -1;
                this.mLastSeenPos = -1;
                this.mMode = 5;
                int firstPos = AbsListView.this.mFirstPosition;
                int lastPos = (firstPos + postOffset) - 1;
                int i = this.mTargetPos;
                if (i < firstPos) {
                    viewTravelCount = firstPos - i;
                } else if (i > lastPos) {
                    viewTravelCount = i - lastPos;
                } else {
                    AbsListView.this.smoothScrollBy(AbsListView.this.getChildAt(i - firstPos).getTop() - offset, duration, true, false);
                    return;
                }
                float screenTravelCount = ((float) viewTravelCount) / ((float) postOffset);
                this.mScrollDuration = screenTravelCount < 1.0f ? duration : (int) (((float) duration) / screenTravelCount);
                this.mLastSeenPos = -1;
                AbsListView.this.postOnAnimation(this);
            }
        }

        private void scrollToVisible(int targetPos, int boundPos, int duration) {
            int i = targetPos;
            int boundPos2 = boundPos;
            int firstPos = AbsListView.this.mFirstPosition;
            int lastPos = (firstPos + AbsListView.this.getChildCount()) - 1;
            int paddedTop = AbsListView.this.mListPadding.top;
            int paddedBottom = AbsListView.this.getHeight() - AbsListView.this.mListPadding.bottom;
            if (i < firstPos || i > lastPos) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("scrollToVisible called with targetPos ");
                stringBuilder.append(i);
                stringBuilder.append(" not visible [");
                stringBuilder.append(firstPos);
                stringBuilder.append(", ");
                stringBuilder.append(lastPos);
                stringBuilder.append("]");
                Log.w(AbsListView.TAG, stringBuilder.toString());
            }
            if (boundPos2 < firstPos || boundPos2 > lastPos) {
                boundPos2 = -1;
            }
            View targetChild = AbsListView.this.getChildAt(i - firstPos);
            int targetTop = targetChild.getTop();
            int targetBottom = targetChild.getBottom();
            int scrollBy = 0;
            if (targetBottom > paddedBottom) {
                scrollBy = targetBottom - paddedBottom;
            }
            if (targetTop < paddedTop) {
                scrollBy = targetTop - paddedTop;
            }
            if (scrollBy != 0) {
                if (boundPos2 >= 0) {
                    View boundChild = AbsListView.this.getChildAt(boundPos2 - firstPos);
                    int boundTop = boundChild.getTop();
                    int boundBottom = boundChild.getBottom();
                    int absScroll = Math.abs(scrollBy);
                    if (scrollBy >= 0 || boundBottom + absScroll <= paddedBottom) {
                        if (scrollBy > 0 && boundTop - absScroll < paddedTop) {
                            scrollBy = Math.min(0, boundTop - paddedTop);
                        }
                    } else {
                        scrollBy = Math.max(0, boundBottom - paddedBottom);
                    }
                }
                AbsListView.this.smoothScrollBy(scrollBy, duration);
            }
        }

        public void stop() {
            AbsListView.this.removeCallbacks(this);
        }

        public void run() {
            int listHeight = AbsListView.this.getHeight();
            int firstPos = AbsListView.this.mFirstPosition;
            int i = this.mMode;
            boolean z = false;
            int lastPos;
            View lastView;
            int scrollBy;
            int i2;
            int nextViewTop;
            int extraScroll;
            int lastViewHeight;
            if (i == 1) {
                i = AbsListView.this.getChildCount() - 1;
                lastPos = firstPos + i;
                if (i >= 0) {
                    if (lastPos == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    lastView = AbsListView.this.getChildAt(i);
                    scrollBy = (lastView.getHeight() - (listHeight - lastView.getTop())) + (lastPos < AbsListView.this.mItemCount - 1 ? Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll) : AbsListView.this.mListPadding.bottom);
                    AbsListView absListView = AbsListView.this;
                    i2 = this.mScrollDuration;
                    if (lastPos < this.mTargetPos) {
                        z = true;
                    }
                    absListView.smoothScrollBy(scrollBy, i2, true, z);
                    this.mLastSeenPos = lastPos;
                    if (lastPos < this.mTargetPos) {
                        AbsListView.this.postOnAnimation(this);
                    }
                }
            } else if (i != 2) {
                int childCount;
                int nextViewHeight;
                if (i == 3) {
                    childCount = AbsListView.this.getChildCount();
                    if (firstPos == this.mBoundPos || childCount <= 1) {
                        i2 = 0;
                    } else if (firstPos + childCount >= AbsListView.this.mItemCount) {
                        i2 = 0;
                    } else {
                        lastPos = firstPos + 1;
                        if (lastPos == this.mLastSeenPos) {
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        lastView = AbsListView.this.getChildAt(1);
                        nextViewHeight = lastView.getHeight();
                        nextViewTop = lastView.getTop();
                        extraScroll = Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll);
                        if (lastPos < this.mBoundPos) {
                            AbsListView.this.smoothScrollBy(Math.max(0, (nextViewHeight + nextViewTop) - extraScroll), this.mScrollDuration, true, true);
                            this.mLastSeenPos = lastPos;
                            AbsListView.this.postOnAnimation(this);
                        } else if (nextViewTop > extraScroll) {
                            AbsListView.this.smoothScrollBy(nextViewTop - extraScroll, this.mScrollDuration, true, false);
                        } else {
                            AbsListView.this.reportScrollStateChange(0);
                        }
                    }
                    AbsListView.this.reportScrollStateChange(i2);
                } else if (i == 4) {
                    i = AbsListView.this.getChildCount() - 2;
                    if (i >= 0) {
                        childCount = firstPos + i;
                        if (childCount == this.mLastSeenPos) {
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        View lastView2 = AbsListView.this.getChildAt(i);
                        lastViewHeight = lastView2.getHeight();
                        nextViewHeight = lastView2.getTop();
                        nextViewTop = listHeight - nextViewHeight;
                        extraScroll = Math.max(AbsListView.this.mListPadding.top, this.mExtraScroll);
                        this.mLastSeenPos = childCount;
                        if (childCount > this.mBoundPos) {
                            AbsListView.this.smoothScrollBy(-(nextViewTop - extraScroll), this.mScrollDuration, true, true);
                            AbsListView.this.postOnAnimation(this);
                        } else {
                            int bottom = listHeight - extraScroll;
                            scrollBy = nextViewHeight + lastViewHeight;
                            if (bottom > scrollBy) {
                                AbsListView.this.smoothScrollBy(-(bottom - scrollBy), this.mScrollDuration, 1, false);
                            } else {
                                AbsListView.this.reportScrollStateChange(0);
                            }
                        }
                    }
                } else if (i == 5) {
                    if (this.mLastSeenPos == firstPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    this.mLastSeenPos = firstPos;
                    i = AbsListView.this.getChildCount();
                    if (i > 0) {
                        float firstPositionVisiblePart;
                        float lastPositionVisiblePart;
                        lastViewHeight = this.mTargetPos;
                        nextViewHeight = (firstPos + i) - 1;
                        View firstChild = AbsListView.this.getChildAt(0);
                        extraScroll = firstChild.getHeight();
                        View lastChild = AbsListView.this.getChildAt(i - 1);
                        scrollBy = lastChild.getHeight();
                        if (((float) extraScroll) == 0.0f) {
                            firstPositionVisiblePart = 1.0f;
                        } else {
                            firstPositionVisiblePart = ((float) (firstChild.getTop() + extraScroll)) / ((float) extraScroll);
                        }
                        if (((float) scrollBy) == 0.0f) {
                            lastPositionVisiblePart = 1.0f;
                        } else {
                            lastPositionVisiblePart = ((float) ((AbsListView.this.getHeight() + scrollBy) - lastChild.getBottom())) / ((float) scrollBy);
                        }
                        float viewTravelCount = 0.0f;
                        if (lastViewHeight < firstPos) {
                            viewTravelCount = (((float) (firstPos - lastViewHeight)) + (1.0f - firstPositionVisiblePart)) + 1.0f;
                        } else if (lastViewHeight > nextViewHeight) {
                            viewTravelCount = ((float) (lastViewHeight - nextViewHeight)) + (1.0f - lastPositionVisiblePart);
                        }
                        float screenTravelCount = viewTravelCount / ((float) i);
                        float modifier = Math.min(Math.abs(screenTravelCount), 1.0f);
                        if (lastViewHeight < firstPos) {
                            AbsListView.this.smoothScrollBy((int) (((float) (-AbsListView.this.getHeight())) * modifier), (int) (((float) this.mScrollDuration) * modifier), true, true);
                            AbsListView.this.postOnAnimation(this);
                        } else {
                            float f = screenTravelCount;
                            View view = firstChild;
                            if (lastViewHeight > nextViewHeight) {
                                AbsListView.this.smoothScrollBy((int) (((float) AbsListView.this.getHeight()) * modifier), (int) (((float) this.mScrollDuration) * modifier), true, true);
                                AbsListView.this.postOnAnimation(this);
                            } else {
                                i = AbsListView.this.getChildAt(lastViewHeight - firstPos).getTop();
                                childCount = i - this.mOffsetFromTop;
                                AbsListView.this.smoothScrollBy(childCount, (int) (((float) this.mScrollDuration) * (((float) Math.abs(childCount)) / ((float) AbsListView.this.getHeight()))), Float.MIN_VALUE, false);
                            }
                        }
                    }
                }
            } else if (firstPos == this.mLastSeenPos) {
                AbsListView.this.postOnAnimation(this);
            } else {
                z = false;
                View firstView = AbsListView.this.getChildAt(0);
                if (firstView != null) {
                    lastPos = firstView.getTop();
                    lastViewHeight = firstPos > 0 ? Math.max(this.mExtraScroll, AbsListView.this.mListPadding.top) : AbsListView.this.mListPadding.top;
                    AbsListView absListView2 = AbsListView.this;
                    nextViewTop = lastPos - lastViewHeight;
                    extraScroll = this.mScrollDuration;
                    if (firstPos > this.mTargetPos) {
                        z = true;
                    }
                    absListView2.smoothScrollBy(nextViewTop, extraScroll, true, z);
                    this.mLastSeenPos = firstPos;
                    if (firstPos > this.mTargetPos) {
                        AbsListView.this.postOnAnimation(this);
                    }
                }
            }
        }
    }

    class RecycleBin {
        private View[] mActiveViews = new View[0];
        private ArrayList<View> mCurrentScrap;
        private int mFirstActivePosition;
        @UnsupportedAppUsage
        private RecyclerListener mRecyclerListener;
        private ArrayList<View>[] mScrapViews;
        private ArrayList<View> mSkippedScrap;
        private SparseArray<View> mTransientStateViews;
        private LongSparseArray<View> mTransientStateViewsById;
        private int mViewTypeCount;

        RecycleBin() {
        }

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount >= 1) {
                ArrayList<View>[] scrapViews = new ArrayList[viewTypeCount];
                for (int i = 0; i < viewTypeCount; i++) {
                    scrapViews[i] = new ArrayList();
                }
                this.mViewTypeCount = viewTypeCount;
                this.mCurrentScrap = scrapViews[0];
                this.mScrapViews = scrapViews;
                return;
            }
            throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
        }

        public void markChildrenDirty() {
            int scrapCount;
            int typeCount;
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    ((View) scrap.get(i)).forceLayout();
                }
            } else {
                typeCount = this.mViewTypeCount;
                for (scrapCount = 0; scrapCount < typeCount; scrapCount++) {
                    ArrayList<View> scrap2 = this.mScrapViews[scrapCount];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        ((View) scrap2.get(j)).forceLayout();
                    }
                }
            }
            typeCount = this.mTransientStateViews;
            if (typeCount != 0) {
                typeCount = typeCount.size();
                for (scrapCount = 0; scrapCount < typeCount; scrapCount++) {
                    ((View) this.mTransientStateViews.valueAt(scrapCount)).forceLayout();
                }
            }
            LongSparseArray longSparseArray = this.mTransientStateViewsById;
            if (longSparseArray != null) {
                typeCount = longSparseArray.size();
                for (scrapCount = 0; scrapCount < typeCount; scrapCount++) {
                    ((View) this.mTransientStateViewsById.valueAt(scrapCount)).forceLayout();
                }
            }
        }

        public boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public void clear() {
            if (this.mViewTypeCount == 1) {
                clearScrap(this.mCurrentScrap);
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i = 0; i < typeCount; i++) {
                    clearScrap(this.mScrapViews[i]);
                }
            }
            clearTransientStateViews();
        }

        /* Access modifiers changed, original: 0000 */
        public void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = AbsListView.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!(lp == null || lp.viewType == -2)) {
                    activeViews[i] = child;
                    lp.scrappedFromPosition = firstActivePosition + i;
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public View getActiveView(int position) {
            int index = position - this.mFirstActivePosition;
            View[] activeViews = this.mActiveViews;
            if (index < 0 || index >= activeViews.length) {
                return null;
            }
            View match = activeViews[index];
            activeViews[index] = null;
            return match;
        }

        /* Access modifiers changed, original: 0000 */
        public View getTransientStateView(int position) {
            if (AbsListView.this.mAdapter == null || !AbsListView.this.mAdapterHasStableIds || this.mTransientStateViewsById == null) {
                int index = this.mTransientStateViews;
                if (index != 0) {
                    index = index.indexOfKey(position);
                    if (index >= 0) {
                        View result = (View) this.mTransientStateViews.valueAt(index);
                        this.mTransientStateViews.removeAt(index);
                        return result;
                    }
                }
                return null;
            }
            long id = AbsListView.this.mAdapter.getItemId(position);
            View result2 = (View) this.mTransientStateViewsById.get(id);
            this.mTransientStateViewsById.remove(id);
            return result2;
        }

        /* Access modifiers changed, original: 0000 */
        public void clearTransientStateViews() {
            int i;
            SparseArray<View> viewsByPos = this.mTransientStateViews;
            if (viewsByPos != null) {
                int N = viewsByPos.size();
                for (i = 0; i < N; i++) {
                    removeDetachedView((View) viewsByPos.valueAt(i), false);
                }
                viewsByPos.clear();
            }
            LongSparseArray<View> viewsById = this.mTransientStateViewsById;
            if (viewsById != null) {
                i = viewsById.size();
                for (int i2 = 0; i2 < i; i2++) {
                    removeDetachedView((View) viewsById.valueAt(i2), false);
                }
                viewsById.clear();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public View getScrapView(int position) {
            int whichScrap = AbsListView.this.mAdapter.getItemViewType(position);
            if (whichScrap < 0) {
                return null;
            }
            if (this.mViewTypeCount == 1) {
                return retrieveFromScrap(this.mCurrentScrap, position);
            }
            ArrayList[] arrayListArr = this.mScrapViews;
            if (whichScrap < arrayListArr.length) {
                return retrieveFromScrap(arrayListArr[whichScrap], position);
            }
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public void addScrapView(View scrap, int position) {
            LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                lp.scrappedFromPosition = position;
                int viewType = lp.viewType;
                if (shouldRecycleViewType(viewType)) {
                    scrap.dispatchStartTemporaryDetach();
                    AbsListView.this.notifyViewAccessibilityStateChangedIfNeeded(1);
                    if (!scrap.hasTransientState()) {
                        clearScrapForRebind(scrap);
                        if (this.mViewTypeCount == 1) {
                            this.mCurrentScrap.add(scrap);
                        } else {
                            this.mScrapViews[viewType].add(scrap);
                        }
                        RecyclerListener recyclerListener = this.mRecyclerListener;
                        if (recyclerListener != null) {
                            recyclerListener.onMovedToScrapHeap(scrap);
                        }
                    } else if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                        if (this.mTransientStateViewsById == null) {
                            this.mTransientStateViewsById = new LongSparseArray();
                        }
                        this.mTransientStateViewsById.put(lp.itemId, scrap);
                    } else if (AbsListView.this.mDataChanged) {
                        clearScrapForRebind(scrap);
                        getSkippedScrap().add(scrap);
                    } else {
                        if (this.mTransientStateViews == null) {
                            this.mTransientStateViews = new SparseArray();
                        }
                        this.mTransientStateViews.put(position, scrap);
                    }
                    return;
                }
                if (viewType != -2) {
                    getSkippedScrap().add(scrap);
                }
            }
        }

        private ArrayList<View> getSkippedScrap() {
            if (this.mSkippedScrap == null) {
                this.mSkippedScrap = new ArrayList();
            }
            return this.mSkippedScrap;
        }

        /* Access modifiers changed, original: 0000 */
        public void removeSkippedScrap() {
            int count = this.mSkippedScrap;
            if (count != 0) {
                count = count.size();
                for (int i = 0; i < count; i++) {
                    removeDetachedView((View) this.mSkippedScrap.get(i), false);
                }
                this.mSkippedScrap.clear();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void scrapActiveViews() {
            View[] activeViews = this.mActiveViews;
            boolean multipleScraps = true;
            boolean hasListener = this.mRecyclerListener != null;
            if (this.mViewTypeCount <= 1) {
                multipleScraps = false;
            }
            ArrayList<View> scrapViews = this.mCurrentScrap;
            for (int i = activeViews.length - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    LayoutParams lp = (LayoutParams) victim.getLayoutParams();
                    int whichScrap = lp.viewType;
                    activeViews[i] = null;
                    if (victim.hasTransientState()) {
                        victim.dispatchStartTemporaryDetach();
                        if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                            if (this.mTransientStateViewsById == null) {
                                this.mTransientStateViewsById = new LongSparseArray();
                            }
                            this.mTransientStateViewsById.put(AbsListView.this.mAdapter.getItemId(this.mFirstActivePosition + i), victim);
                        } else if (!AbsListView.this.mDataChanged) {
                            if (this.mTransientStateViews == null) {
                                this.mTransientStateViews = new SparseArray();
                            }
                            this.mTransientStateViews.put(this.mFirstActivePosition + i, victim);
                        } else if (whichScrap != -2) {
                            removeDetachedView(victim, false);
                        }
                    } else if (shouldRecycleViewType(whichScrap)) {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        lp.scrappedFromPosition = this.mFirstActivePosition + i;
                        removeDetachedView(victim, false);
                        scrapViews.add(victim);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    } else if (whichScrap != -2) {
                        removeDetachedView(victim, false);
                    }
                }
            }
            pruneScrapViews();
        }

        /* Access modifiers changed, original: 0000 */
        public void fullyDetachScrapViews() {
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                for (int j = scrapPile.size() - 1; j >= 0; j--) {
                    View view = (View) scrapPile.get(j);
                    if (view.isTemporarilyDetached()) {
                        removeDetachedView(view, false);
                    }
                }
            }
        }

        private void pruneScrapViews() {
            int size;
            int maxViews = this.mActiveViews.length;
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                size = scrapPile.size();
                while (size > maxViews) {
                    size--;
                    scrapPile.remove(size);
                }
            }
            SparseArray<View> transViewsByPos = this.mTransientStateViews;
            if (transViewsByPos != null) {
                size = 0;
                while (size < transViewsByPos.size()) {
                    View v = (View) transViewsByPos.valueAt(size);
                    if (!v.hasTransientState()) {
                        removeDetachedView(v, false);
                        transViewsByPos.removeAt(size);
                        size--;
                    }
                    size++;
                }
            }
            LongSparseArray<View> transViewsById = this.mTransientStateViewsById;
            if (transViewsById != null) {
                int i2 = 0;
                while (i2 < transViewsById.size()) {
                    View v2 = (View) transViewsById.valueAt(i2);
                    if (!v2.hasTransientState()) {
                        removeDetachedView(v2, false);
                        transViewsById.removeAt(i2);
                        i2--;
                    }
                    i2++;
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void reclaimScrapViews(List<View> views) {
            if (this.mViewTypeCount == 1) {
                views.addAll(this.mCurrentScrap);
                return;
            }
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                views.addAll(scrapViews[i]);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void setCacheColorHint(int color) {
            int scrapCount;
            int i;
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                scrapCount = scrap.size();
                for (i = 0; i < scrapCount; i++) {
                    ((View) scrap.get(i)).setDrawingCacheBackgroundColor(color);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (scrapCount = 0; scrapCount < typeCount; scrapCount++) {
                    ArrayList<View> scrap2 = this.mScrapViews[scrapCount];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        ((View) scrap2.get(j)).setDrawingCacheBackgroundColor(color);
                    }
                }
            }
            for (View victim : this.mActiveViews) {
                if (victim != null) {
                    victim.setDrawingCacheBackgroundColor(color);
                }
            }
        }

        private View retrieveFromScrap(ArrayList<View> scrapViews, int position) {
            int size = scrapViews.size();
            if (size <= 0) {
                return null;
            }
            for (int i = size - 1; i >= 0; i--) {
                LayoutParams params = (LayoutParams) ((View) scrapViews.get(i)).getLayoutParams();
                if (AbsListView.this.mAdapterHasStableIds) {
                    if (AbsListView.this.mAdapter.getItemId(position) == params.itemId) {
                        return (View) scrapViews.remove(i);
                    }
                } else if (params.scrappedFromPosition == position) {
                    View scrap = (View) scrapViews.remove(i);
                    clearScrapForRebind(scrap);
                    return scrap;
                }
            }
            View scrap2 = (View) scrapViews.remove(size - 1);
            clearScrapForRebind(scrap2);
            return scrap2;
        }

        private void clearScrap(ArrayList<View> scrap) {
            int scrapCount = scrap.size();
            for (int j = 0; j < scrapCount; j++) {
                removeDetachedView((View) scrap.remove((scrapCount - 1) - j), false);
            }
        }

        private void clearScrapForRebind(View view) {
            view.clearAccessibilityFocus();
            view.setAccessibilityDelegate(null);
        }

        private void removeDetachedView(View child, boolean animate) {
            child.setAccessibilityDelegate(null);
            AbsListView.this.removeDetachedView(child, animate);
        }
    }

    public interface RecyclerListener {
        void onMovedToScrapHeap(View view);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        LongSparseArray<Integer> checkIdState;
        SparseBooleanArray checkState;
        int checkedItemCount;
        String filter;
        @UnsupportedAppUsage
        long firstId;
        int height;
        boolean inActionMode;
        int position;
        long selectedId;
        @UnsupportedAppUsage
        int viewTop;

        /* synthetic */ SavedState(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.selectedId = in.readLong();
            this.firstId = in.readLong();
            this.viewTop = in.readInt();
            this.position = in.readInt();
            this.height = in.readInt();
            this.filter = in.readString();
            this.inActionMode = in.readByte() != (byte) 0;
            this.checkedItemCount = in.readInt();
            this.checkState = in.readSparseBooleanArray();
            int N = in.readInt();
            if (N > 0) {
                this.checkIdState = new LongSparseArray();
                for (int i = 0; i < N; i++) {
                    this.checkIdState.put(in.readLong(), Integer.valueOf(in.readInt()));
                }
            }
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(this.selectedId);
            out.writeLong(this.firstId);
            out.writeInt(this.viewTop);
            out.writeInt(this.position);
            out.writeInt(this.height);
            out.writeString(this.filter);
            out.writeByte((byte) this.inActionMode);
            out.writeInt(this.checkedItemCount);
            out.writeSparseBooleanArray(this.checkState);
            LongSparseArray longSparseArray = this.checkIdState;
            int N = longSparseArray != null ? longSparseArray.size() : 0;
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                out.writeLong(this.checkIdState.keyAt(i));
                out.writeInt(((Integer) this.checkIdState.valueAt(i)).intValue());
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AbsListView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" selectedId=");
            stringBuilder.append(this.selectedId);
            stringBuilder.append(" firstId=");
            stringBuilder.append(this.firstId);
            stringBuilder.append(" viewTop=");
            stringBuilder.append(this.viewTop);
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append(" height=");
            stringBuilder.append(this.height);
            stringBuilder.append(" filter=");
            stringBuilder.append(this.filter);
            stringBuilder.append(" checkState=");
            stringBuilder.append(this.checkState);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public interface SelectionBoundsAdjuster {
        void adjustListItemSelectionBounds(Rect rect);
    }

    public abstract void fillGap(boolean z);

    @UnsupportedAppUsage
    public abstract int findMotionRow(int i);

    public abstract void setSelectionInt(int i);

    public AbsListView(Context context) {
        super(context);
        this.mChoiceMode = 0;
        this.mLayoutMode = 0;
        this.mDeferNotifyDataSetChanged = false;
        this.mDrawSelectorOnTop = false;
        this.mSelectorPosition = -1;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mTmpPoint = new float[2];
        this.mNestedYOffset = 0;
        this.mActivePointerId = -1;
        this.mEdgeGlowTop = new EdgeEffect(this.mContext);
        this.mEdgeGlowBottom = new EdgeEffect(this.mContext);
        this.mDirection = 0;
        this.mIsFirstTouchMoveEvent = false;
        this.mNumTouchMoveEvent = 0;
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        setVerticalScrollBarEnabled(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.View);
        initializeScrollbarsInternal(a);
        a.recycle();
    }

    public AbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842858);
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mChoiceMode = 0;
        this.mLayoutMode = 0;
        this.mDeferNotifyDataSetChanged = false;
        this.mDrawSelectorOnTop = false;
        this.mSelectorPosition = -1;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mTmpPoint = new float[2];
        this.mNestedYOffset = 0;
        this.mActivePointerId = -1;
        this.mEdgeGlowTop = new EdgeEffect(this.mContext);
        this.mEdgeGlowBottom = new EdgeEffect(this.mContext);
        this.mDirection = 0;
        this.mIsFirstTouchMoveEvent = false;
        this.mNumTouchMoveEvent = 0;
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsListView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.AbsListView, attrs, a, defStyleAttr, defStyleRes);
        Drawable selector = a.getDrawable(0);
        if (selector != null) {
            setSelector(selector);
        }
        this.mDrawSelectorOnTop = a.getBoolean(1, false);
        setStackFromBottom(a.getBoolean(2, false));
        setScrollingCacheEnabled(a.getBoolean(3, true));
        setTextFilterEnabled(a.getBoolean(4, false));
        setTranscriptMode(a.getInt(5, 0));
        setCacheColorHint(a.getColor(6, 0));
        setSmoothScrollbarEnabled(a.getBoolean(9, true));
        setChoiceMode(a.getInt(7, 0));
        setFastScrollEnabled(a.getBoolean(8, false));
        setFastScrollStyle(a.getResourceId(11, 0));
        setFastScrollAlwaysVisible(a.getBoolean(10, false));
        a.recycle();
        if (context.getResources().getConfiguration().uiMode == 6) {
            setRevealOnFocusHint(false);
        }
    }

    private void initAbsListView() {
        setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
        setScrollingCacheEnabled(true);
        ViewConfiguration configuration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
        if (0.6d <= 0.0d) {
            this.mMoveAcceleration = this.mTouchSlop;
        } else if (0.6d < 0.6d) {
            this.mMoveAcceleration = (int) (((double) this.mTouchSlop) * 0.6d);
        } else if (0.6d < 0.6d || 0.6d >= TOUCH_SLOP_MAX) {
            this.mMoveAcceleration = this.mTouchSlop;
        } else {
            this.mMoveAcceleration = (int) (((double) this.mTouchSlop) * 0.6d);
        }
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
        this.mDensityScale = getContext().getResources().getDisplayMetrics().density;
        AbsListViewInjector.onInit(this);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            this.mAdapterHasStableIds = this.mAdapter.hasStableIds();
            if (this.mChoiceMode != 0 && this.mAdapterHasStableIds && this.mCheckedIdStates == null) {
                this.mCheckedIdStates = new LongSparseArray();
            }
        }
        clearChoices();
    }

    public int getCheckedItemCount() {
        return this.mCheckedItemCount;
    }

    public boolean isItemChecked(int position) {
        if (this.mChoiceMode != 0) {
            SparseBooleanArray sparseBooleanArray = this.mCheckStates;
            if (sparseBooleanArray != null) {
                return sparseBooleanArray.get(position);
            }
        }
        return false;
    }

    public int getCheckedItemPosition() {
        if (this.mChoiceMode == 1) {
            SparseBooleanArray sparseBooleanArray = this.mCheckStates;
            if (sparseBooleanArray != null && sparseBooleanArray.size() == 1) {
                return this.mCheckStates.keyAt(0);
            }
        }
        return -1;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        if (this.mChoiceMode != 0) {
            return this.mCheckStates;
        }
        return null;
    }

    public long[] getCheckedItemIds() {
        if (this.mChoiceMode == 0 || this.mCheckedIdStates == null || this.mAdapter == null) {
            return new long[0];
        }
        LongSparseArray<Integer> idStates = this.mCheckedIdStates;
        int count = idStates.size();
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = idStates.keyAt(i);
        }
        return ids;
    }

    public void clearChoices() {
        SparseBooleanArray sparseBooleanArray = this.mCheckStates;
        if (sparseBooleanArray != null) {
            sparseBooleanArray.clear();
        }
        LongSparseArray longSparseArray = this.mCheckedIdStates;
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
        this.mCheckedItemCount = 0;
    }

    public void setItemChecked(int position, boolean value) {
        int i = this.mChoiceMode;
        if (i != 0) {
            if (value && i == 3 && this.mChoiceActionMode == null) {
                MultiChoiceModeWrapper multiChoiceModeWrapper = this.mMultiChoiceModeCallback;
                if (multiChoiceModeWrapper == null || !multiChoiceModeWrapper.hasWrappedCallback()) {
                    throw new IllegalStateException("AbsListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
                }
                this.mChoiceActionMode = startActionMode(this.mMultiChoiceModeCallback);
            }
            i = this.mChoiceMode;
            boolean z = false;
            boolean oldValue;
            boolean itemCheckChanged;
            if (i == 2 || i == 3) {
                oldValue = this.mCheckStates.get(position);
                this.mCheckStates.put(position, value);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (value) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    } else {
                        this.mCheckedIdStates.delete(this.mAdapter.getItemId(position));
                    }
                }
                if (oldValue != value) {
                    z = true;
                }
                itemCheckChanged = z;
                if (itemCheckChanged) {
                    if (value) {
                        this.mCheckedItemCount++;
                    } else {
                        this.mCheckedItemCount--;
                    }
                }
                if (this.mChoiceActionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, this.mAdapter.getItemId(position), value);
                }
            } else {
                oldValue = this.mCheckedIdStates != null && this.mAdapter.hasStableIds();
                itemCheckChanged = isItemChecked(position) != value;
                if (value || isItemChecked(position)) {
                    this.mCheckStates.clear();
                    if (oldValue) {
                        this.mCheckedIdStates.clear();
                    }
                }
                if (value) {
                    this.mCheckStates.put(position, true);
                    if (oldValue) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
            }
            if (!(this.mInLayout || this.mBlockLayoutRequests || !itemCheckChanged)) {
                this.mDataChanged = true;
                rememberSyncState();
                requestLayout();
            }
        }
    }

    public boolean performItemClick(View view, int position, long id) {
        int i = position;
        boolean handled = false;
        boolean dispatchItemClick = true;
        int i2 = this.mChoiceMode;
        if (i2 != 0) {
            boolean checkedStateChanged = false;
            if (i2 == 2 || (i2 == 3 && this.mChoiceActionMode != null)) {
                boolean checked = this.mCheckStates.get(position, false) ^ 1;
                this.mCheckStates.put(position, checked);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (checked) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    } else {
                        this.mCheckedIdStates.delete(this.mAdapter.getItemId(position));
                    }
                }
                if (checked) {
                    this.mCheckedItemCount++;
                } else {
                    this.mCheckedItemCount--;
                }
                ActionMode actionMode = this.mChoiceActionMode;
                if (actionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(actionMode, position, id, checked);
                    dispatchItemClick = false;
                }
                checkedStateChanged = true;
            } else if (this.mChoiceMode == 1) {
                if (this.mCheckStates.get(position, false) ^ true) {
                    this.mCheckStates.clear();
                    this.mCheckStates.put(position, true);
                    if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                        this.mCheckedIdStates.clear();
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
                checkedStateChanged = true;
            }
            if (checkedStateChanged) {
                updateOnScreenCheckedViews();
            }
            handled = true;
        }
        if (dispatchItemClick) {
            return handled | super.performItemClick(view, position, id);
        }
        return handled;
    }

    private void updateOnScreenCheckedViews() {
        int firstPos = this.mFirstPosition;
        int count = getChildCount();
        boolean useActivated = getContext().getApplicationInfo().targetSdkVersion >= 11;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int position = firstPos + i;
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else if (useActivated) {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
    }

    public int getChoiceMode() {
        return this.mChoiceMode;
    }

    public void setChoiceMode(int choiceMode) {
        this.mChoiceMode = choiceMode;
        ActionMode actionMode = this.mChoiceActionMode;
        if (actionMode != null) {
            actionMode.finish();
            this.mChoiceActionMode = null;
        }
        if (this.mChoiceMode != 0) {
            if (this.mCheckStates == null) {
                this.mCheckStates = new SparseBooleanArray(0);
            }
            if (this.mCheckedIdStates == null) {
                ListAdapter listAdapter = this.mAdapter;
                if (listAdapter != null && listAdapter.hasStableIds()) {
                    this.mCheckedIdStates = new LongSparseArray(0);
                }
            }
            if (this.mChoiceMode == 3) {
                clearChoices();
                setLongClickable(true);
            }
        }
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener listener) {
        if (this.mMultiChoiceModeCallback == null) {
            this.mMultiChoiceModeCallback = new MultiChoiceModeWrapper();
        }
        this.mMultiChoiceModeCallback.setWrapped(listener);
    }

    private boolean contentFits() {
        int childCount = getChildCount();
        boolean z = true;
        if (childCount == 0) {
            return true;
        }
        if (childCount != this.mItemCount) {
            return false;
        }
        if (getChildAt(0).getTop() < this.mListPadding.top || getChildAt(childCount - 1).getBottom() > getHeight() - this.mListPadding.bottom) {
            z = false;
        }
        return z;
    }

    public void setFastScrollEnabled(final boolean enabled) {
        if (this.mFastScrollEnabled != enabled) {
            this.mFastScrollEnabled = enabled;
            if (isOwnerThread()) {
                setFastScrollerEnabledUiThread(enabled);
            } else {
                post(new Runnable() {
                    public void run() {
                        AbsListView.this.setFastScrollerEnabledUiThread(enabled);
                    }
                });
            }
        }
    }

    private void setFastScrollerEnabledUiThread(boolean enabled) {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.setEnabled(enabled);
        } else if (enabled) {
            this.mFastScroll = new FastScroller(this, this.mFastScrollStyle);
            this.mFastScroll.setEnabled(true);
        }
        resolvePadding();
        fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.updateLayout();
        }
    }

    public void setFastScrollStyle(int styleResId) {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller == null) {
            this.mFastScrollStyle = styleResId;
        } else {
            fastScroller.setStyle(styleResId);
        }
    }

    public void setFastScrollAlwaysVisible(final boolean alwaysShow) {
        if (this.mFastScrollAlwaysVisible != alwaysShow) {
            if (alwaysShow && !this.mFastScrollEnabled) {
                setFastScrollEnabled(true);
            }
            this.mFastScrollAlwaysVisible = alwaysShow;
            if (isOwnerThread()) {
                setFastScrollerAlwaysVisibleUiThread(alwaysShow);
            } else {
                post(new Runnable() {
                    public void run() {
                        AbsListView.this.setFastScrollerAlwaysVisibleUiThread(alwaysShow);
                    }
                });
            }
        }
    }

    private void setFastScrollerAlwaysVisibleUiThread(boolean alwaysShow) {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.setAlwaysShow(alwaysShow);
        }
    }

    private boolean isOwnerThread() {
        return this.mOwnerThread == Thread.currentThread();
    }

    public boolean isFastScrollAlwaysVisible() {
        FastScroller fastScroller = this.mFastScroll;
        boolean z = true;
        if (fastScroller == null) {
            if (!(this.mFastScrollEnabled && this.mFastScrollAlwaysVisible)) {
                z = false;
            }
            return z;
        }
        if (!(fastScroller.isEnabled() && this.mFastScroll.isAlwaysShowEnabled())) {
            z = false;
        }
        return z;
    }

    public int getVerticalScrollbarWidth() {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller == null || !fastScroller.isEnabled()) {
            return super.getVerticalScrollbarWidth();
        }
        return Math.max(super.getVerticalScrollbarWidth(), this.mFastScroll.getWidth());
    }

    @ExportedProperty
    public boolean isFastScrollEnabled() {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller == null) {
            return this.mFastScrollEnabled;
        }
        return fastScroller.isEnabled();
    }

    public void setVerticalScrollbarPosition(int position) {
        super.setVerticalScrollbarPosition(position);
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.setScrollbarPosition(position);
        }
    }

    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.setScrollBarStyle(style);
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean isVerticalScrollBarHidden() {
        return isFastScrollEnabled();
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    @ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
        invokeOnItemScrollListener();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void invokeOnItemScrollListener() {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.onScroll(this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScroll(this, this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        onScrollChanged(0, 0, 0, 0);
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int lastVisiblePosition = getLastVisiblePosition();
            if (this.mLastAccessibilityScrollEventFromIndex != firstVisiblePosition || this.mLastAccessibilityScrollEventToIndex != lastVisiblePosition) {
                this.mLastAccessibilityScrollEventFromIndex = firstVisiblePosition;
                this.mLastAccessibilityScrollEventToIndex = lastVisiblePosition;
            } else {
                return;
            }
        }
        super.sendAccessibilityEventUnchecked(event);
    }

    public CharSequence getAccessibilityClassName() {
        return AbsListView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            if (canScrollUp()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_UP);
                info.setScrollable(true);
            }
            if (canScrollDown()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_DOWN);
                info.setScrollable(true);
            }
        }
        info.removeAction(AccessibilityAction.ACTION_CLICK);
        info.setClickable(false);
    }

    /* Access modifiers changed, original: 0000 */
    public int getSelectionModeForAccessibility() {
        int choiceMode = getChoiceMode();
        if (choiceMode == 0) {
            return 0;
        }
        if (choiceMode != 1) {
            return (choiceMode == 2 || choiceMode == 3) ? 2 : 0;
        } else {
            return 1;
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action != 4096) {
            if (action == 8192 || action == 16908344) {
                if (!isEnabled() || !canScrollUp()) {
                    return false;
                }
                smoothScrollBy(-((getHeight() - this.mListPadding.top) - this.mListPadding.bottom), 200);
                return true;
            } else if (action != 16908346) {
                return false;
            }
        }
        if (!isEnabled() || !canScrollDown()) {
            return false;
        }
        smoothScrollBy((getHeight() - this.mListPadding.top) - this.mListPadding.bottom, 200);
        return true;
    }

    @ExportedProperty
    public boolean isScrollingCacheEnabled() {
        return this.mScrollingCacheEnabled;
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled && !enabled) {
            clearScrollingCache();
        }
        this.mScrollingCacheEnabled = enabled;
    }

    public void setTextFilterEnabled(boolean textFilterEnabled) {
        this.mTextFilterEnabled = textFilterEnabled;
    }

    @ExportedProperty
    public boolean isTextFilterEnabled() {
        return this.mTextFilterEnabled;
    }

    public void getFocusedRect(Rect r) {
        View view = getSelectedView();
        if (view == null || view.getParent() != this) {
            super.getFocusedRect(r);
            return;
        }
        view.getFocusedRect(r);
        offsetDescendantRectToMyCoords(view, r);
    }

    private void useDefaultSelector() {
        setSelector(getContext().getDrawable(17301602));
    }

    @ExportedProperty
    public boolean isStackFromBottom() {
        return this.mStackFromBottom;
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        if (this.mStackFromBottom != stackFromBottom) {
            this.mStackFromBottom = stackFromBottom;
            requestLayoutIfNecessary();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    public Parcelable onSaveInstanceState() {
        dismissPopup();
        SavedState ss = new SavedState(super.onSaveInstanceState());
        SavedState savedState = this.mPendingSync;
        if (savedState != null) {
            ss.selectedId = savedState.selectedId;
            ss.firstId = this.mPendingSync.firstId;
            ss.viewTop = this.mPendingSync.viewTop;
            ss.position = this.mPendingSync.position;
            ss.height = this.mPendingSync.height;
            ss.filter = this.mPendingSync.filter;
            ss.inActionMode = this.mPendingSync.inActionMode;
            ss.checkedItemCount = this.mPendingSync.checkedItemCount;
            ss.checkState = this.mPendingSync.checkState;
            ss.checkIdState = this.mPendingSync.checkIdState;
            return ss;
        }
        boolean z = true;
        boolean haveChildren = getChildCount() > 0 && this.mItemCount > 0;
        long selectedId = getSelectedItemId();
        ss.selectedId = selectedId;
        ss.height = getHeight();
        if (selectedId >= 0) {
            ss.viewTop = this.mSelectedTop;
            ss.position = getSelectedItemPosition();
            ss.firstId = -1;
        } else if (!haveChildren || this.mFirstPosition <= 0) {
            ss.viewTop = 0;
            ss.firstId = -1;
            ss.position = 0;
        } else {
            ss.viewTop = getChildAt(0).getTop();
            int firstPos = this.mFirstPosition;
            if (firstPos >= this.mItemCount) {
                firstPos = this.mItemCount - 1;
            }
            ss.position = firstPos;
            ss.firstId = this.mAdapter.getItemId(firstPos);
        }
        ss.filter = null;
        if (this.mFiltered) {
            EditText textFilter = this.mTextFilter;
            if (textFilter != null) {
                Editable filterText = textFilter.getText();
                if (filterText != null) {
                    ss.filter = filterText.toString();
                }
            }
        }
        if (this.mChoiceMode != 3 || this.mChoiceActionMode == null) {
            z = false;
        }
        ss.inActionMode = z;
        SparseBooleanArray sparseBooleanArray = this.mCheckStates;
        if (sparseBooleanArray != null) {
            ss.checkState = sparseBooleanArray.clone();
        }
        if (this.mCheckedIdStates != null) {
            LongSparseArray<Integer> idState = new LongSparseArray();
            int count = this.mCheckedIdStates.size();
            for (int i = 0; i < count; i++) {
                idState.put(this.mCheckedIdStates.keyAt(i), (Integer) this.mCheckedIdStates.valueAt(i));
            }
            ss.checkIdState = idState;
        }
        ss.checkedItemCount = this.mCheckedItemCount;
        RemoteViewsAdapter remoteViewsAdapter = this.mRemoteAdapter;
        if (remoteViewsAdapter != null) {
            remoteViewsAdapter.saveRemoteViewsCache();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDataChanged = true;
        this.mSyncHeight = (long) ss.height;
        if (ss.selectedId >= 0) {
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.selectedId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewTop;
            this.mSyncMode = 0;
        } else if (ss.firstId >= 0) {
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectorPosition = -1;
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.firstId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewTop;
            this.mSyncMode = 1;
        }
        setFilterText(ss.filter);
        if (ss.checkState != null) {
            this.mCheckStates = ss.checkState;
        }
        if (ss.checkIdState != null) {
            this.mCheckedIdStates = ss.checkIdState;
        }
        this.mCheckedItemCount = ss.checkedItemCount;
        if (ss.inActionMode && this.mChoiceMode == 3) {
            MultiChoiceModeWrapper multiChoiceModeWrapper = this.mMultiChoiceModeCallback;
            if (multiChoiceModeWrapper != null) {
                this.mChoiceActionMode = startActionMode(multiChoiceModeWrapper);
            }
        }
        requestLayout();
    }

    private boolean acceptFilter() {
        return this.mTextFilterEnabled && (getAdapter() instanceof Filterable) && ((Filterable) getAdapter()).getFilter() != null;
    }

    public void setFilterText(String filterText) {
        if (this.mTextFilterEnabled && !TextUtils.isEmpty(filterText)) {
            createTextFilter(false);
            this.mTextFilter.setText((CharSequence) filterText);
            this.mTextFilter.setSelection(filterText.length());
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter instanceof Filterable) {
                if (this.mPopup == null) {
                    ((Filterable) listAdapter).getFilter().filter(filterText);
                }
                this.mFiltered = true;
                this.mDataSetObserver.clearSavedState();
            }
        }
    }

    public CharSequence getTextFilter() {
        if (this.mTextFilterEnabled) {
            EditText editText = this.mTextFilter;
            if (editText != null) {
                return editText.getText();
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus && this.mSelectedPosition < 0 && !isInTouchMode()) {
            if (!(isAttachedToWindow() || this.mAdapter == null)) {
                this.mDataChanged = true;
                this.mOldItemCount = this.mItemCount;
                this.mItemCount = this.mAdapter.getCount();
            }
            resurrectSelection();
        }
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests && !this.mInLayout) {
            super.requestLayout();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void resetList() {
        removeAllViewsInLayout();
        this.mFirstPosition = 0;
        this.mDataChanged = false;
        this.mPositionScrollAfterLayout = null;
        this.mNeedSync = false;
        this.mPendingSync = null;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        this.mSelectedTop = 0;
        this.mSelectorPosition = -1;
        this.mSelectorRect.setEmpty();
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        int extent = count * 100;
        View view = getChildAt(0);
        int top = view.getTop();
        int height = view.getHeight();
        if (height > 0) {
            extent += (top * 100) / height;
        }
        view = getChildAt(count - 1);
        int bottom = view.getBottom();
        height = view.getHeight();
        if (height > 0) {
            extent -= ((bottom - getHeight()) * 100) / height;
        }
        return extent;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollOffset() {
        int firstPosition = this.mFirstPosition;
        int childCount = getChildCount();
        if (firstPosition >= 0 && childCount > 0) {
            if (this.mSmoothScrollbarEnabled) {
                View view = getChildAt(0);
                int top = view.getTop();
                int height = view.getHeight();
                if (height > 0) {
                    return Math.max(((firstPosition * 100) - ((top * 100) / height)) + ((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) this.mItemCount)) * 100.0f)), 0);
                }
            }
            int index;
            int count = this.mItemCount;
            if (firstPosition == 0) {
                index = 0;
            } else if (firstPosition + childCount == count) {
                index = count;
            } else {
                index = (childCount / 2) + firstPosition;
            }
            return (int) (((float) firstPosition) + (((float) childCount) * (((float) index) / ((float) count))));
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollRange() {
        if (!this.mSmoothScrollbarEnabled) {
            return this.mItemCount;
        }
        int result = Math.max(this.mItemCount * 100, 0);
        if (this.mScrollY != 0) {
            return result + Math.abs((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) this.mItemCount)) * 100.0f));
        }
        return result;
    }

    /* Access modifiers changed, original: protected */
    public float getTopFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getTopFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if (this.mFirstPosition > 0) {
            return 1.0f;
        }
        int top = getChildAt(0).getTop();
        return top < this.mPaddingTop ? ((float) (-(top - this.mPaddingTop))) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    /* Access modifiers changed, original: protected */
    public float getBottomFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getBottomFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if ((this.mFirstPosition + count) - 1 < this.mItemCount - 1) {
            return 1.0f;
        }
        int bottom = getChildAt(count - 1).getBottom();
        int height = getHeight();
        return bottom > height - this.mPaddingBottom ? ((float) ((bottom - height) + this.mPaddingBottom)) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mSelector == null) {
            useDefaultSelector();
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = this.mSelectionLeftPadding + this.mPaddingLeft;
        listPadding.top = this.mSelectionTopPadding + this.mPaddingTop;
        listPadding.right = this.mSelectionRightPadding + this.mPaddingRight;
        listPadding.bottom = this.mSelectionBottomPadding + this.mPaddingBottom;
        boolean z = true;
        if (this.mTranscriptMode == 1) {
            int childCount = getChildCount();
            int listBottom = getHeight() - getPaddingBottom();
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild != null ? lastChild.getBottom() : listBottom;
            if (this.mFirstPosition + childCount < this.mLastHandledItemCount || lastBottom > listBottom) {
                z = false;
            }
            this.mForceTranscriptScroll = z;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        int childCount = getChildCount();
        if (changed) {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        layoutChildren();
        this.mOverscrollMax = (b - t) / 3;
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.onItemCountChanged(getChildCount(), this.mItemCount);
        }
        this.mInLayout = false;
        AbsListViewInjector.onLayout(this, changed, l, t, r, b);
    }

    /* Access modifiers changed, original: protected */
    public boolean setFrame(int left, int top, int right, int bottom) {
        boolean changed = super.setFrame(left, top, right, bottom);
        if (changed) {
            boolean visible = getWindowVisibility() == 0;
            if (this.mFiltered && visible) {
                PopupWindow popupWindow = this.mPopup;
                if (popupWindow != null && popupWindow.isShowing()) {
                    positionPopup();
                }
            }
        }
        return changed;
    }

    /* Access modifiers changed, original: protected */
    public void layoutChildren() {
    }

    /* Access modifiers changed, original: 0000 */
    public View getAccessibilityFocusedChild(View focusedView) {
        View viewParent = focusedView.getParent();
        while ((viewParent instanceof View) && viewParent != this) {
            focusedView = viewParent;
            viewParent = viewParent.getParent();
        }
        if (viewParent instanceof View) {
            return focusedView;
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateScrollIndicators() {
        View view = this.mScrollUp;
        int i = 0;
        if (view != null) {
            view.setVisibility(canScrollUp() ? 0 : 4);
        }
        view = this.mScrollDown;
        if (view != null) {
            if (!canScrollDown()) {
                i = 4;
            }
            view.setVisibility(i);
        }
    }

    @UnsupportedAppUsage
    private boolean canScrollUp() {
        boolean z = true;
        boolean canScrollUp = this.mFirstPosition > 0;
        if (canScrollUp || getChildCount() <= 0) {
            return canScrollUp;
        }
        if (getChildAt(0).getTop() >= this.mListPadding.top) {
            z = false;
        }
        return z;
    }

    @UnsupportedAppUsage
    private boolean canScrollDown() {
        int count = getChildCount();
        boolean z = false;
        boolean canScrollDown = this.mFirstPosition + count < this.mItemCount;
        if (canScrollDown || count <= 0) {
            return canScrollDown;
        }
        if (getChildAt(count - 1).getBottom() > this.mBottom - this.mListPadding.bottom) {
            z = true;
        }
        return z;
    }

    @ExportedProperty
    public View getSelectedView() {
        if (this.mItemCount <= 0 || this.mSelectedPosition < 0) {
            return null;
        }
        return getChildAt(this.mSelectedPosition - this.mFirstPosition);
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
    }

    /* Access modifiers changed, original: 0000 */
    public View obtainView(int position, boolean[] outMetadata) {
        Trace.traceBegin(8, "obtainView");
        outMetadata[0] = false;
        View transientView = this.mRecycler.getTransientStateView(position);
        if (transientView != null) {
            if (((LayoutParams) transientView.getLayoutParams()).viewType == this.mAdapter.getItemViewType(position)) {
                View updatedView = this.mAdapter.getView(position, transientView, this);
                if (updatedView != transientView) {
                    setItemViewLayoutParams(updatedView, position);
                    this.mRecycler.addScrapView(updatedView, position);
                }
            }
            outMetadata[0] = true;
            transientView.dispatchFinishTemporaryDetach();
            return transientView;
        }
        View scrapView = this.mRecycler.getScrapView(position);
        View child = this.mAdapter.getView(position, scrapView, this);
        if (scrapView != null) {
            if (child != scrapView) {
                this.mRecycler.addScrapView(scrapView, position);
            } else if (child.isTemporarilyDetached()) {
                outMetadata[0] = true;
                child.dispatchFinishTemporaryDetach();
            }
        }
        int i = this.mCacheColorHint;
        if (i != 0) {
            child.setDrawingCacheBackgroundColor(i);
        }
        if (child.getImportantForAccessibility() == 0) {
            child.setImportantForAccessibility(1);
        }
        setItemViewLayoutParams(child, position);
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            if (this.mAccessibilityDelegate == null) {
                this.mAccessibilityDelegate = new ListItemAccessibilityDelegate();
            }
            if (child.getAccessibilityDelegate() == null) {
                child.setAccessibilityDelegate(this.mAccessibilityDelegate);
            }
        }
        Trace.traceEnd(8);
        return child;
    }

    private void setItemViewLayoutParams(View child, int position) {
        android.view.ViewGroup.LayoutParams lp;
        android.view.ViewGroup.LayoutParams vlp = child.getLayoutParams();
        if (vlp == null) {
            lp = (LayoutParams) generateDefaultLayoutParams();
        } else if (checkLayoutParams(vlp)) {
            lp = (LayoutParams) vlp;
        } else {
            lp = (LayoutParams) generateLayoutParams(vlp);
        }
        if (this.mAdapterHasStableIds) {
            lp.itemId = this.mAdapter.getItemId(position);
        }
        lp.viewType = this.mAdapter.getItemViewType(position);
        lp.isEnabled = this.mAdapter.isEnabled(position);
        if (lp != vlp) {
            child.setLayoutParams(lp);
        }
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        if (position != -1) {
            android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
            boolean isItemEnabled = lp instanceof LayoutParams ? ((LayoutParams) lp).isEnabled && isEnabled() : false;
            info.setEnabled(isItemEnabled);
            if (position == getSelectedItemPosition()) {
                info.setSelected(true);
                addAccessibilityActionIfEnabled(info, isItemEnabled, AccessibilityAction.ACTION_CLEAR_SELECTION);
            } else {
                addAccessibilityActionIfEnabled(info, isItemEnabled, AccessibilityAction.ACTION_SELECT);
            }
            if (isItemClickable(view)) {
                addAccessibilityActionIfEnabled(info, isItemEnabled, AccessibilityAction.ACTION_CLICK);
                info.setClickable(true);
            }
            if (isLongClickable()) {
                addAccessibilityActionIfEnabled(info, isItemEnabled, AccessibilityAction.ACTION_LONG_CLICK);
                info.setLongClickable(true);
            }
        }
    }

    private void addAccessibilityActionIfEnabled(AccessibilityNodeInfo info, boolean enabled, AccessibilityAction action) {
        if (enabled) {
            info.addAction(action);
        }
    }

    private boolean isItemClickable(View view) {
        return view.hasExplicitFocusable() ^ 1;
    }

    /* Access modifiers changed, original: 0000 */
    public void positionSelectorLikeTouch(int position, View sel, float x, float y) {
        positionSelector(position, sel, true, x, y);
    }

    /* Access modifiers changed, original: 0000 */
    public void positionSelectorLikeFocus(int position, View sel) {
        if (this.mSelector == null || this.mSelectorPosition == position || position == -1) {
            positionSelector(position, sel);
            return;
        }
        Rect bounds = this.mSelectorRect;
        positionSelector(position, sel, true, bounds.exactCenterX(), bounds.exactCenterY());
    }

    /* Access modifiers changed, original: 0000 */
    public void positionSelector(int position, View sel) {
        positionSelector(position, sel, false, -1.0f, -1.0f);
    }

    @UnsupportedAppUsage
    private void positionSelector(int position, View sel, boolean manageHotspot, float x, float y) {
        boolean positionChanged = position != this.mSelectorPosition;
        if (position != -1) {
            this.mSelectorPosition = position;
        }
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        if (sel instanceof SelectionBoundsAdjuster) {
            ((SelectionBoundsAdjuster) sel).adjustListItemSelectionBounds(selectorRect);
        }
        selectorRect.left -= this.mSelectionLeftPadding;
        selectorRect.top -= this.mSelectionTopPadding;
        selectorRect.right += this.mSelectionRightPadding;
        selectorRect.bottom += this.mSelectionBottomPadding;
        boolean isChildViewEnabled = sel.isEnabled();
        if (this.mIsChildViewEnabled != isChildViewEnabled) {
            this.mIsChildViewEnabled = isChildViewEnabled;
        }
        Drawable selector = this.mSelector;
        if (selector != null) {
            if (positionChanged) {
                selector.setVisible(false, false);
                selector.setState(StateSet.NOTHING);
            }
            selector.setBounds(selectorRect);
            if (positionChanged) {
                if (getVisibility() == 0) {
                    selector.setVisible(true, false);
                }
                updateSelectorState();
            }
            if (manageHotspot) {
                selector.setHotspot(x, y);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        int saveCount = 0;
        boolean clipToPadding = (this.mGroupFlags & 34) == 34;
        if (clipToPadding) {
            saveCount = canvas.save();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            canvas.clipRect(this.mPaddingLeft + scrollX, this.mPaddingTop + scrollY, ((this.mRight + scrollX) - this.mLeft) - this.mPaddingRight, ((this.mBottom + scrollY) - this.mTop) - this.mPaddingBottom);
            this.mGroupFlags &= -35;
        }
        boolean drawSelectorOnTop = this.mDrawSelectorOnTop;
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(saveCount);
            this.mGroupFlags = 34 | this.mGroupFlags;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean isPaddingOffsetRequired() {
        return (this.mGroupFlags & 34) != 34;
    }

    /* Access modifiers changed, original: protected */
    public int getLeftPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : -this.mPaddingLeft;
    }

    /* Access modifiers changed, original: protected */
    public int getTopPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : -this.mPaddingTop;
    }

    /* Access modifiers changed, original: protected */
    public int getRightPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : this.mPaddingRight;
    }

    /* Access modifiers changed, original: protected */
    public int getBottomPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : this.mPaddingBottom;
    }

    /* Access modifiers changed, original: protected */
    public void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if (isLayoutRequested()) {
            handleBoundsChange();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        handleBoundsChange();
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleBoundsChange() {
        if (!this.mInLayout) {
            int childCount = getChildCount();
            if (childCount > 0) {
                this.mDataChanged = true;
                rememberSyncState();
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
                    if (lp == null || lp.width < 1 || lp.height < 1) {
                        child.forceLayout();
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean touchModeDrawsInPressedState() {
        int i = this.mTouchMode;
        if (i == 1 || i == 2) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldShowSelector() {
        return (isFocused() && !isInTouchMode()) || (touchModeDrawsInPressedState() && isPressed());
    }

    private void drawSelector(Canvas canvas) {
        if (shouldDrawSelector()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }

    public final boolean shouldDrawSelector() {
        return this.mSelectorRect.isEmpty() ^ 1;
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public boolean isDrawSelectorOnTop() {
        return this.mDrawSelectorOnTop;
    }

    public void setSelector(int resID) {
        setSelector(getContext().getDrawable(resID));
    }

    public void setSelector(Drawable sel) {
        Drawable drawable = this.mSelector;
        if (drawable != null) {
            drawable.setCallback(null);
            unscheduleDrawable(this.mSelector);
        }
        this.mSelector = sel;
        Rect padding = new Rect();
        sel.getPadding(padding);
        this.mSelectionLeftPadding = padding.left;
        this.mSelectionTopPadding = padding.top;
        this.mSelectionRightPadding = padding.right;
        this.mSelectionBottomPadding = padding.bottom;
        sel.setCallback(this);
        updateSelectorState();
    }

    public Drawable getSelector() {
        return this.mSelector;
    }

    /* Access modifiers changed, original: 0000 */
    public void keyPressed() {
        if (isEnabled() && isClickable()) {
            Drawable selector = this.mSelector;
            Rect selectorRect = this.mSelectorRect;
            if (selector != null && ((isFocused() || touchModeDrawsInPressedState()) && !selectorRect.isEmpty())) {
                View v = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (v != null) {
                    if (!v.hasExplicitFocusable()) {
                        v.setPressed(true);
                    } else {
                        return;
                    }
                }
                setPressed(true);
                boolean longClickable = isLongClickable();
                Drawable d = selector.getCurrent();
                if (d != null && (d instanceof TransitionDrawable)) {
                    if (longClickable) {
                        ((TransitionDrawable) d).startTransition(ViewConfiguration.getLongPressTimeout());
                    } else {
                        ((TransitionDrawable) d).resetTransition();
                    }
                }
                if (longClickable && !this.mDataChanged) {
                    if (this.mPendingCheckForKeyLongPress == null) {
                        this.mPendingCheckForKeyLongPress = new CheckForKeyLongPress(this, null);
                    }
                    this.mPendingCheckForKeyLongPress.rememberWindowAttachCount();
                    postDelayed(this.mPendingCheckForKeyLongPress, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
        }
    }

    public void setScrollIndicators(View up, View down) {
        this.mScrollUp = up;
        this.mScrollDown = down;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void updateSelectorState() {
        Drawable selector = this.mSelector;
        if (selector != null && selector.isStateful()) {
            if (!shouldShowSelector()) {
                selector.setState(StateSet.NOTHING);
            } else if (selector.setState(getDrawableStateForSelector())) {
                invalidateDrawable(selector);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        updateSelectorState();
    }

    private int[] getDrawableStateForSelector() {
        if (this.mIsChildViewEnabled) {
            return super.getDrawableState();
        }
        int enabledState = ENABLED_STATE_SET[0];
        int[] state = onCreateDrawableState(1);
        int enabledPos = -1;
        for (int i = state.length - 1; i >= 0; i--) {
            if (state[i] == enabledState) {
                enabledPos = i;
                break;
            }
        }
        if (enabledPos >= 0) {
            System.arraycopy(state, enabledPos + 1, state, enabledPos, (state.length - enabledPos) - 1);
        }
        return state;
    }

    public boolean verifyDrawable(Drawable dr) {
        return this.mSelector == dr || super.verifyDrawable(dr);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mSelector;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        treeObserver.addOnTouchModeChangeListener(this);
        if (!(!this.mTextFilterEnabled || this.mPopup == null || this.mGlobalLayoutListenerAddedFilter)) {
            treeObserver.addOnGlobalLayoutListener(this);
        }
        if (this.mAdapter != null && this.mDataSetObserver == null) {
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mDataChanged = true;
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsDetaching = true;
        dismissPopup();
        this.mRecycler.clear();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        treeObserver.removeOnTouchModeChangeListener(this);
        if (this.mTextFilterEnabled && this.mPopup != null) {
            treeObserver.removeOnGlobalLayoutListener(this);
            this.mGlobalLayoutListenerAddedFilter = false;
        }
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            AdapterDataSetObserver adapterDataSetObserver = this.mDataSetObserver;
            if (adapterDataSetObserver != null) {
                listAdapter.unregisterDataSetObserver(adapterDataSetObserver);
                this.mDataSetObserver = null;
            }
        }
        Span span = this.mScrollStrictSpan;
        if (span != null) {
            span.finish();
            this.mScrollStrictSpan = null;
        }
        span = this.mFlingStrictSpan;
        if (span != null) {
            span.finish();
            this.mFlingStrictSpan = null;
        }
        FlingRunnable flingRunnable = this.mFlingRunnable;
        if (flingRunnable != null) {
            removeCallbacks(flingRunnable);
        }
        AbsPositionScroller absPositionScroller = this.mPositionScroller;
        if (absPositionScroller != null) {
            absPositionScroller.stop();
        }
        Runnable runnable = this.mClearScrollingCache;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        PerformClick performClick = this.mPerformClick;
        if (performClick != null) {
            removeCallbacks(performClick);
        }
        runnable = this.mTouchModeReset;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.mTouchModeReset.run();
        }
        this.mIsDetaching = false;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        int touchMode = isInTouchMode() ^ 1;
        if (hasWindowFocus) {
            if (this.mFiltered && !this.mPopupHidden) {
                showPopup();
            }
            int i = this.mLastTouchMode;
            if (!(touchMode == i || i == -1)) {
                if (touchMode == 1) {
                    resurrectSelection();
                } else {
                    hideSelector();
                    this.mLayoutMode = 0;
                    layoutChildren();
                }
            }
        } else {
            setChildrenDrawingCacheEnabled(false);
            FlingRunnable flingRunnable = this.mFlingRunnable;
            if (flingRunnable != null) {
                removeCallbacks(flingRunnable);
                flingRunnable = this.mFlingRunnable;
                flingRunnable.mSuppressIdleStateChangeCall = false;
                flingRunnable.endFling();
                AbsPositionScroller absPositionScroller = this.mPositionScroller;
                if (absPositionScroller != null) {
                    absPositionScroller.stop();
                }
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    invalidateParentCaches();
                    finishGlows();
                    invalidate();
                }
            }
            dismissPopup();
            if (touchMode == 1) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
        }
        this.mLastTouchMode = touchMode;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null) {
            fastScroller.setScrollbarPosition(getVerticalScrollbarPosition());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterContextMenuInfo(view, position, id);
    }

    public void onCancelPendingInputEvents() {
        super.onCancelPendingInputEvents();
        PerformClick performClick = this.mPerformClick;
        if (performClick != null) {
            removeCallbacks(performClick);
        }
        CheckForTap checkForTap = this.mPendingCheckForTap;
        if (checkForTap != null) {
            removeCallbacks(checkForTap);
        }
        CheckForLongPress checkForLongPress = this.mPendingCheckForLongPress;
        if (checkForLongPress != null) {
            removeCallbacks(checkForLongPress);
        }
        CheckForKeyLongPress checkForKeyLongPress = this.mPendingCheckForKeyLongPress;
        if (checkForKeyLongPress != null) {
            removeCallbacks(checkForKeyLongPress);
        }
    }

    private boolean performStylusButtonPressAction(MotionEvent ev) {
        if (this.mChoiceMode == 3 && this.mChoiceActionMode == null) {
            View child = getChildAt(this.mMotionPosition - this.mFirstPosition);
            if (child != null && performLongPress(child, this.mMotionPosition, this.mAdapter.getItemId(this.mMotionPosition))) {
                this.mTouchMode = -1;
                setPressed(false);
                child.setPressed(false);
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean performLongPress(View child, int longPressPosition, long longPressId) {
        return performLongPress(child, longPressPosition, longPressId, -1.0f, -1.0f);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean performLongPress(View child, int longPressPosition, long longPressId, float x, float y) {
        if (this.mChoiceMode == 3) {
            if (this.mChoiceActionMode == null) {
                ActionMode startActionMode = startActionMode(this.mMultiChoiceModeCallback);
                this.mChoiceActionMode = startActionMode;
                if (startActionMode != null) {
                    setItemChecked(longPressPosition, true);
                    performHapticFeedback(0);
                }
            }
            return true;
        }
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, child, longPressPosition, longPressId);
        }
        if (!handled) {
            this.mContextMenuInfo = createContextMenuInfo(child, longPressPosition, longPressId);
            if (x == -1.0f || y == -1.0f) {
                handled = super.showContextMenuForChild(this);
            } else {
                handled = super.showContextMenuForChild(this, x, y);
            }
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    /* Access modifiers changed, original: protected */
    public ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public boolean showContextMenu() {
        return showContextMenuInternal(0.0f, 0.0f, false);
    }

    public boolean showContextMenu(float x, float y) {
        return showContextMenuInternal(x, y, true);
    }

    private boolean showContextMenuInternal(float x, float y, boolean useOffsets) {
        int position = pointToPosition((int) x, (int) y);
        if (position != -1) {
            long id = this.mAdapter.getItemId(position);
            View child = getChildAt(position - this.mFirstPosition);
            if (child != null) {
                this.mContextMenuInfo = createContextMenuInfo(child, position, id);
                if (useOffsets) {
                    return super.showContextMenuForChild(this, x, y);
                }
                return super.showContextMenuForChild(this);
            }
        }
        if (useOffsets) {
            return super.showContextMenu(x, y);
        }
        return super.showContextMenu();
    }

    public boolean showContextMenuForChild(View originalView) {
        if (isShowingContextMenuWithCoords()) {
            return false;
        }
        return showContextMenuForChildInternal(originalView, 0.0f, 0.0f, false);
    }

    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return showContextMenuForChildInternal(originalView, x, y, true);
    }

    private boolean showContextMenuForChildInternal(View originalView, float x, float y, boolean useOffsets) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        long longPressId = this.mAdapter.getItemId(longPressPosition);
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, originalView, longPressPosition, longPressId);
        }
        if (!handled) {
            this.mContextMenuInfo = createContextMenuInfo(getChildAt(longPressPosition - this.mFirstPosition), longPressPosition, longPressId);
            if (useOffsets) {
                handled = super.showContextMenuForChild(originalView, x, y);
            } else {
                handled = super.showContextMenuForChild(originalView);
            }
        }
        return handled;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.isConfirmKey(keyCode)) {
            if (!isEnabled()) {
                return true;
            }
            if (isClickable() && isPressed() && this.mSelectedPosition >= 0 && this.mAdapter != null && this.mSelectedPosition < this.mAdapter.getCount()) {
                View view = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (view != null) {
                    performItemClick(view, this.mSelectedPosition, this.mSelectedRowId);
                    view.setPressed(false);
                }
                setPressed(false);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchSetPressed(boolean pressed) {
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    public long pointToRowId(int x, int y) {
        int position = pointToPosition(x, y);
        if (position >= 0) {
            return this.mAdapter.getItemId(position);
        }
        return Long.MIN_VALUE;
    }

    private boolean startScrollIfNeeded(int x, int y, MotionEvent vtev) {
        int deltaY = y - this.mMotionY;
        int distance = Math.abs(deltaY);
        boolean overscroll = this.mScrollY != 0;
        boolean isFarEnough;
        if (this.mIsFirstTouchMoveEvent) {
            isFarEnough = distance > this.mMoveAcceleration ? true : null;
        } else {
            isFarEnough = distance > this.mTouchSlop ? true : null;
        }
        if ((!overscroll && !isFarEnough) || (getNestedScrollAxes() & 2) != 0) {
            return false;
        }
        createScrollingCache();
        if (overscroll) {
            this.mTouchMode = 5;
            this.mMotionCorrection = 0;
        } else {
            this.mTouchMode = 3;
            int i;
            if (this.mIsFirstTouchMoveEvent) {
                i = this.mMoveAcceleration;
                if (deltaY <= 0) {
                    i = -i;
                }
                this.mMotionCorrection = i;
            } else {
                i = this.mTouchSlop;
                if (deltaY <= 0) {
                    i = -i;
                }
                this.mMotionCorrection = i;
            }
        }
        removeCallbacks(this.mPendingCheckForLongPress);
        setPressed(false);
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        reportScrollStateChange(1);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        scrollIfNeeded(x, y, vtev);
        return true;
    }

    private void scrollIfNeeded(int x, int y, MotionEvent vtev) {
        int scrollOffsetCorrection;
        int scrollConsumedCorrection;
        int rawDeltaY;
        int scrollOffsetCorrection2;
        int scrollConsumedCorrection2;
        int i = x;
        int i2 = y;
        MotionEvent motionEvent = vtev;
        int rawDeltaY2 = i2 - this.mMotionY;
        if (this.mLastY == Integer.MIN_VALUE) {
            rawDeltaY2 -= this.mMotionCorrection;
        }
        int i3 = this.mLastY;
        if (dispatchNestedPreScroll(0, i3 != Integer.MIN_VALUE ? i3 - i2 : -rawDeltaY2, this.mScrollConsumed, this.mScrollOffset)) {
            int[] iArr = this.mScrollConsumed;
            rawDeltaY2 += iArr[1];
            int[] iArr2 = this.mScrollOffset;
            scrollOffsetCorrection = -iArr2[1];
            scrollConsumedCorrection = iArr[1];
            if (motionEvent != null) {
                motionEvent.offsetLocation(0.0f, (float) iArr2[1]);
                this.mNestedYOffset += this.mScrollOffset[1];
            }
            rawDeltaY = rawDeltaY2;
            scrollOffsetCorrection2 = scrollOffsetCorrection;
            scrollConsumedCorrection2 = scrollConsumedCorrection;
        } else {
            rawDeltaY = rawDeltaY2;
            scrollOffsetCorrection2 = 0;
            scrollConsumedCorrection2 = 0;
        }
        int deltaY = rawDeltaY;
        rawDeltaY2 = this.mLastY;
        int incrementalDeltaY = rawDeltaY2 != Integer.MIN_VALUE ? (i2 - rawDeltaY2) + scrollConsumedCorrection2 : deltaY;
        int lastYCorrection = 0;
        rawDeltaY2 = this.mTouchMode;
        int motionViewPrevTop;
        int i4;
        if (rawDeltaY2 == 3) {
            if (this.mScrollStrictSpan == null) {
                this.mScrollStrictSpan = StrictMode.enterCriticalSpan("AbsListView-scroll");
            }
            if (i2 != this.mLastY) {
                int motionIndex;
                boolean atEdge;
                if ((this.mGroupFlags & 524288) == 0 && Math.abs(rawDeltaY) > this.mTouchSlop) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                rawDeltaY2 = this.mMotionPosition;
                if (rawDeltaY2 >= 0) {
                    motionIndex = rawDeltaY2 - this.mFirstPosition;
                } else {
                    motionIndex = getChildCount() / 2;
                }
                View motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevTop = motionView.getTop();
                } else {
                    motionViewPrevTop = 0;
                }
                if (incrementalDeltaY != 0) {
                    atEdge = trackMotionScroll(deltaY, incrementalDeltaY);
                } else {
                    atEdge = false;
                }
                View motionView2 = getChildAt(motionIndex);
                if (motionView2 != null) {
                    int motionViewRealTop = motionView2.getTop();
                    if (atEdge) {
                        int i5 = (-incrementalDeltaY) - (motionViewRealTop - motionViewPrevTop);
                        int overscroll = i5;
                        if (dispatchNestedScroll(0, i5 - incrementalDeltaY, 0, i5, this.mScrollOffset)) {
                            int[] iArr3 = this.mScrollOffset;
                            lastYCorrection = 0 - iArr3[1];
                            if (motionEvent != null) {
                                motionEvent.offsetLocation(0.0f, (float) iArr3[1]);
                                this.mNestedYOffset += this.mScrollOffset[1];
                            }
                            i3 = incrementalDeltaY;
                            i4 = deltaY;
                        } else {
                            int incrementalDeltaY2 = incrementalDeltaY;
                            boolean atOverscrollEdge = overScrollBy(0, overscroll, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                            if (atOverscrollEdge) {
                                VelocityTracker velocityTracker = this.mVelocityTracker;
                                if (velocityTracker != null) {
                                    velocityTracker.clear();
                                }
                            }
                            scrollOffsetCorrection = getOverScrollMode();
                            if (scrollOffsetCorrection != 0) {
                                if (scrollOffsetCorrection != 1) {
                                    i3 = incrementalDeltaY2;
                                } else if (contentFits()) {
                                    i3 = incrementalDeltaY2;
                                }
                            }
                            if (!atOverscrollEdge) {
                                this.mDirection = 0;
                                this.mTouchMode = 5;
                            }
                            i3 = incrementalDeltaY2;
                            if (i3 > 0) {
                                this.mEdgeGlowTop.onPull(((float) (-overscroll)) / ((float) getHeight()), ((float) i) / ((float) getWidth()));
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                                invalidateTopGlow();
                            } else {
                                i5 = overscroll;
                                if (i3 < 0) {
                                    this.mEdgeGlowBottom.onPull(((float) i5) / ((float) getHeight()), 1.0f - (((float) i) / ((float) getWidth())));
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                    invalidateBottomGlow();
                                }
                            }
                        }
                    } else {
                        i3 = incrementalDeltaY;
                        i4 = deltaY;
                    }
                    this.mMotionY = (i2 + lastYCorrection) + scrollOffsetCorrection2;
                } else {
                    i3 = incrementalDeltaY;
                    i4 = deltaY;
                }
                this.mLastY = (i2 + lastYCorrection) + scrollOffsetCorrection2;
                return;
            }
            i4 = deltaY;
            return;
        }
        i3 = incrementalDeltaY;
        i4 = deltaY;
        if (rawDeltaY2 == 5 && i2 != this.mLastY) {
            int overScrollDistance;
            int incrementalDeltaY3;
            int incrementalDeltaY4;
            int newDirection;
            deltaY = this.mScrollY;
            motionViewPrevTop = deltaY - i3;
            incrementalDeltaY = i2 > this.mLastY ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = incrementalDeltaY;
            }
            rawDeltaY2 = -i3;
            if ((motionViewPrevTop >= 0 || deltaY < 0) && (motionViewPrevTop <= 0 || deltaY > 0)) {
                overScrollDistance = rawDeltaY2;
                incrementalDeltaY3 = 0;
            } else {
                rawDeltaY2 = -deltaY;
                overScrollDistance = rawDeltaY2;
                incrementalDeltaY3 = i3 + rawDeltaY2;
            }
            if (overScrollDistance != 0) {
                incrementalDeltaY4 = incrementalDeltaY3;
                int overScrollDistance2 = overScrollDistance;
                newDirection = incrementalDeltaY;
                overScrollBy(0, overScrollDistance, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                rawDeltaY2 = getOverScrollMode();
                if (rawDeltaY2 != 0) {
                    if (rawDeltaY2 != 1) {
                        scrollConsumedCorrection = overScrollDistance2;
                    } else if (contentFits()) {
                        scrollConsumedCorrection = overScrollDistance2;
                    }
                }
                if (rawDeltaY > 0) {
                    this.mEdgeGlowTop.onPull(((float) overScrollDistance2) / ((float) getHeight()), ((float) i) / ((float) getWidth()));
                    if (!this.mEdgeGlowBottom.isFinished()) {
                        this.mEdgeGlowBottom.onRelease();
                    }
                    invalidateTopGlow();
                } else {
                    scrollConsumedCorrection = overScrollDistance2;
                    if (rawDeltaY < 0) {
                        this.mEdgeGlowBottom.onPull(((float) scrollConsumedCorrection) / ((float) getHeight()), 1.0f - (((float) i) / ((float) getWidth())));
                        if (!this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onRelease();
                        }
                        invalidateBottomGlow();
                    }
                }
            } else {
                incrementalDeltaY4 = incrementalDeltaY3;
                scrollConsumedCorrection = overScrollDistance;
                newDirection = incrementalDeltaY;
                int i6 = deltaY;
            }
            if (incrementalDeltaY4 != 0) {
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    invalidateParentIfNeeded();
                }
                trackMotionScroll(incrementalDeltaY4, incrementalDeltaY4);
                this.mTouchMode = 3;
                rawDeltaY2 = findClosestMotionRow(i2);
                scrollOffsetCorrection = 0;
                this.mMotionCorrection = 0;
                View motionView3 = getChildAt(rawDeltaY2 - this.mFirstPosition);
                if (motionView3 != null) {
                    scrollOffsetCorrection = motionView3.getTop();
                }
                this.mMotionViewOriginalTop = scrollOffsetCorrection;
                this.mMotionY = i2 + scrollOffsetCorrection2;
                this.mMotionPosition = rawDeltaY2;
            }
            this.mLastY = (i2 + 0) + scrollOffsetCorrection2;
            this.mDirection = newDirection;
        }
    }

    private void invalidateTopGlow() {
        if (shouldDisplayEdgeEffects()) {
            boolean clipToPadding = getClipToPadding();
            int left = 0;
            int top = clipToPadding ? this.mPaddingTop : 0;
            if (clipToPadding) {
                left = this.mPaddingLeft;
            }
            int right = getWidth();
            if (clipToPadding) {
                right -= this.mPaddingRight;
            }
            invalidate(left, top, right, this.mEdgeGlowTop.getMaxHeight() + top);
        }
    }

    private void invalidateBottomGlow() {
        if (shouldDisplayEdgeEffects()) {
            boolean clipToPadding = getClipToPadding();
            int bottom = getHeight();
            if (clipToPadding) {
                bottom -= this.mPaddingBottom;
            }
            int left = clipToPadding ? this.mPaddingLeft : 0;
            int right = getWidth();
            if (clipToPadding) {
                right -= this.mPaddingRight;
            }
            invalidate(left, bottom - this.mEdgeGlowBottom.getMaxHeight(), right, bottom);
        }
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode) {
            hideSelector();
            if (getHeight() > 0 && getChildCount() > 0) {
                layoutChildren();
            }
            updateSelectorState();
            return;
        }
        int touchMode = this.mTouchMode;
        if (touchMode == 5 || touchMode == 6) {
            FlingRunnable flingRunnable = this.mFlingRunnable;
            if (flingRunnable != null) {
                flingRunnable.endFling();
            }
            AbsPositionScroller absPositionScroller = this.mPositionScroller;
            if (absPositionScroller != null) {
                absPositionScroller.stop();
            }
            if (this.mScrollY != 0) {
                this.mScrollY = 0;
                invalidateParentCaches();
                finishGlows();
                invalidate();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean handleScrollBarDragging(MotionEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = true;
        if (isEnabled()) {
            AbsPositionScroller absPositionScroller = this.mPositionScroller;
            if (absPositionScroller != null) {
                absPositionScroller.stop();
            }
            if (this.mIsDetaching || !isAttachedToWindow()) {
                return false;
            }
            startNestedScroll(2);
            FastScroller fastScroller = this.mFastScroll;
            if (fastScroller != null && fastScroller.onTouchEvent(ev)) {
                return true;
            }
            initVelocityTrackerIfNotExists();
            MotionEvent vtev = MotionEvent.obtain(ev);
            int actionMasked = ev.getActionMasked();
            if (actionMasked == 0) {
                this.mNestedYOffset = 0;
            }
            vtev.offsetLocation(0.0f, (float) this.mNestedYOffset);
            int index;
            int id;
            int x;
            if (actionMasked == 0) {
                onTouchDown(ev);
                this.mNumTouchMoveEvent = 0;
            } else if (actionMasked == 1) {
                onTouchUp(ev);
                this.mNumTouchMoveEvent = 0;
            } else if (actionMasked == 2) {
                this.mNumTouchMoveEvent++;
                if (this.mNumTouchMoveEvent == 1) {
                    this.mIsFirstTouchMoveEvent = true;
                } else {
                    this.mIsFirstTouchMoveEvent = false;
                }
                onTouchMove(ev, vtev);
            } else if (actionMasked == 3) {
                onTouchCancel();
                this.mNumTouchMoveEvent = 0;
            } else if (actionMasked == 5) {
                index = ev.getActionIndex();
                id = ev.getPointerId(index);
                x = (int) ev.getX(index);
                int y = (int) ev.getY(index);
                this.mMotionCorrection = 0;
                this.mActivePointerId = id;
                this.mMotionX = x;
                this.mMotionY = y;
                int motionPosition = pointToPosition(x, y);
                if (motionPosition >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                    this.mMotionPosition = motionPosition;
                }
                this.mLastY = y;
                this.mNumTouchMoveEvent = 0;
            } else if (actionMasked == 6) {
                onSecondaryPointerUp(ev);
                index = this.mMotionX;
                id = this.mMotionY;
                x = pointToPosition(index, id);
                if (x >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(x - this.mFirstPosition).getTop();
                    this.mMotionPosition = x;
                }
                this.mLastY = id;
                this.mNumTouchMoveEvent = 0;
            }
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.addMovement(vtev);
            }
            vtev.recycle();
            return true;
        }
        if (!(isClickable() || isLongClickable())) {
            z = false;
        }
        return z;
    }

    private void onTouchDown(MotionEvent ev) {
        this.mHasPerformedLongPress = false;
        this.mActivePointerId = ev.getPointerId(0);
        hideSelector();
        if (this.mTouchMode == 6) {
            this.mFlingRunnable.endFling();
            AbsPositionScroller absPositionScroller = this.mPositionScroller;
            if (absPositionScroller != null) {
                absPositionScroller.stop();
            }
            this.mTouchMode = 5;
            this.mMotionX = (int) ev.getX();
            this.mMotionY = (int) ev.getY();
            this.mLastY = this.mMotionY;
            this.mMotionCorrection = 0;
            this.mDirection = 0;
        } else {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int motionPosition = pointToPosition(x, y);
            if (!this.mDataChanged) {
                if (this.mTouchMode == 4) {
                    createScrollingCache();
                    this.mTouchMode = 3;
                    this.mMotionCorrection = 0;
                    motionPosition = findMotionRow(y);
                    this.mFlingRunnable.flywheelTouch();
                } else if (motionPosition >= 0 && ((ListAdapter) getAdapter()).isEnabled(motionPosition)) {
                    this.mTouchMode = 0;
                    if (this.mPendingCheckForTap == null) {
                        this.mPendingCheckForTap = new CheckForTap(this, null);
                    }
                    this.mPendingCheckForTap.x = ev.getX();
                    this.mPendingCheckForTap.y = ev.getY();
                    postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                }
            }
            if (motionPosition >= 0) {
                this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
            }
            this.mMotionX = x;
            this.mMotionY = y;
            this.mMotionPosition = motionPosition;
            this.mLastY = Integer.MIN_VALUE;
        }
        if (this.mTouchMode == 0 && this.mMotionPosition != -1 && performButtonActionOnTouchDown(ev)) {
            removeCallbacks(this.mPendingCheckForTap);
        }
    }

    private void onTouchMove(MotionEvent ev, MotionEvent vtev) {
        if (!this.mHasPerformedLongPress) {
            int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
            if (pointerIndex == -1) {
                pointerIndex = 0;
                this.mActivePointerId = ev.getPointerId(0);
            }
            if (this.mDataChanged) {
                layoutChildren();
            }
            int y = (int) ev.getY(pointerIndex);
            int i = this.mTouchMode;
            if (i == 0 || i == 1 || i == 2) {
                if (!startScrollIfNeeded((int) ev.getX(pointerIndex), y, vtev)) {
                    View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                    float x = ev.getX(pointerIndex);
                    if (!pointInView(x, (float) y, (float) this.mTouchSlop)) {
                        setPressed(false);
                        if (motionView != null) {
                            motionView.setPressed(false);
                        }
                        removeCallbacks(this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress);
                        this.mTouchMode = 2;
                        updateSelectorState();
                    } else if (motionView != null) {
                        float[] point = this.mTmpPoint;
                        point[0] = x;
                        point[1] = (float) y;
                        transformPointToViewLocal(point, motionView);
                        motionView.drawableHotspotChanged(point[0], point[1]);
                    }
                }
            } else if (i == 3 || i == 5) {
                scrollIfNeeded((int) ev.getX(pointerIndex), y, vtev);
            }
        }
    }

    private void onTouchUp(MotionEvent ev) {
        int i = this.mTouchMode;
        int i2;
        if (i == 0 || i == 1 || i == 2) {
            i = this.mMotionPosition;
            final View child = getChildAt(i - this.mFirstPosition);
            if (child != null) {
                if (this.mTouchMode != 0) {
                    child.setPressed(false);
                }
                float x = ev.getX();
                boolean inList = x > ((float) this.mListPadding.left) && x < ((float) (getWidth() - this.mListPadding.right));
                if (inList && !child.hasExplicitFocusable()) {
                    if (this.mPerformClick == null) {
                        this.mPerformClick = new PerformClick(this, null);
                    }
                    final PerformClick performClick = this.mPerformClick;
                    performClick.mClickMotionPosition = i;
                    performClick.rememberWindowAttachCount();
                    this.mResurrectToPosition = i;
                    i2 = this.mTouchMode;
                    if (i2 == 0 || i2 == 1) {
                        removeCallbacks(this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress);
                        this.mLayoutMode = 0;
                        if (this.mDataChanged || !this.mAdapter.isEnabled(i)) {
                            this.mTouchMode = -1;
                            updateSelectorState();
                        } else {
                            this.mTouchMode = 1;
                            setSelectedPositionInt(this.mMotionPosition);
                            layoutChildren();
                            child.setPressed(true);
                            positionSelector(this.mMotionPosition, child);
                            setPressed(true);
                            Drawable d = this.mSelector;
                            if (d != null) {
                                d = d.getCurrent();
                                if (d != null && (d instanceof TransitionDrawable)) {
                                    ((TransitionDrawable) d).resetTransition();
                                }
                                this.mSelector.setHotspot(x, ev.getY());
                            }
                            Runnable runnable = this.mTouchModeReset;
                            if (runnable != null) {
                                removeCallbacks(runnable);
                            }
                            this.mTouchModeReset = new Runnable() {
                                public void run() {
                                    AbsListView.this.mTouchModeReset = null;
                                    AbsListView.this.mTouchMode = -1;
                                    child.setPressed(false);
                                    AbsListView.this.setPressed(false);
                                    if (!AbsListView.this.mDataChanged && !AbsListView.this.mIsDetaching && AbsListView.this.isAttachedToWindow()) {
                                        performClick.run();
                                    }
                                }
                            };
                            postDelayed(this.mTouchModeReset, (long) ViewConfiguration.getPressedStateDurationForListview());
                        }
                        return;
                    } else if (!this.mDataChanged && this.mAdapter.isEnabled(i)) {
                        performClick.run();
                    }
                }
            }
            this.mTouchMode = -1;
            updateSelectorState();
        } else if (i == 3) {
            i = getChildCount();
            if (i > 0) {
                int firstChildTop = getChildAt(0).getTop();
                i2 = getChildAt(i - 1).getBottom();
                int contentTop = this.mListPadding.top;
                int contentBottom = getHeight() - this.mListPadding.bottom;
                if (this.mFirstPosition != 0 || firstChildTop < contentTop || this.mFirstPosition + i >= this.mItemCount || i2 > getHeight() - contentBottom) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) (velocityTracker.getYVelocity(this.mActivePointerId) * this.mVelocityScale);
                    boolean flingVelocity = Math.abs(initialVelocity) > this.mMinimumVelocity;
                    if (!flingVelocity || ((this.mFirstPosition == 0 && firstChildTop == contentTop - this.mOverscrollDistance) || (this.mFirstPosition + i == this.mItemCount && i2 == this.mOverscrollDistance + contentBottom))) {
                        this.mTouchMode = -1;
                        reportScrollStateChange(0);
                        FlingRunnable flingRunnable = this.mFlingRunnable;
                        if (flingRunnable != null) {
                            flingRunnable.endFling();
                        }
                        AbsPositionScroller absPositionScroller = this.mPositionScroller;
                        if (absPositionScroller != null) {
                            absPositionScroller.stop();
                        }
                        if (flingVelocity && !dispatchNestedPreFling(0.0f, (float) (-initialVelocity))) {
                            dispatchNestedFling(0.0f, (float) (-initialVelocity), false);
                        }
                    } else if (dispatchNestedPreFling(0.0f, (float) (-initialVelocity))) {
                        this.mTouchMode = -1;
                        reportScrollStateChange(0);
                    } else {
                        if (this.mFlingRunnable == null) {
                            this.mFlingRunnable = new FlingRunnable();
                        }
                        reportScrollStateChange(2);
                        this.mFlingRunnable.start(-initialVelocity);
                        dispatchNestedFling(0.0f, (float) (-initialVelocity), true);
                    }
                } else {
                    this.mTouchMode = -1;
                    reportScrollStateChange(0);
                }
            } else {
                this.mTouchMode = -1;
                reportScrollStateChange(0);
            }
        } else if (i == 5) {
            if (this.mFlingRunnable == null) {
                this.mFlingRunnable = new FlingRunnable();
            }
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            velocityTracker2.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int initialVelocity2 = (int) velocityTracker2.getYVelocity(this.mActivePointerId);
            reportScrollStateChange(2);
            if (Math.abs(initialVelocity2) > this.mMinimumVelocity) {
                this.mFlingRunnable.startOverfling(-initialVelocity2);
            } else {
                this.mFlingRunnable.startSpringback();
            }
        }
        setPressed(false);
        if (shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
        invalidate();
        removeCallbacks(this.mPendingCheckForLongPress);
        recycleVelocityTracker();
        this.mActivePointerId = -1;
        Span span = this.mScrollStrictSpan;
        if (span != null) {
            span.finish();
            this.mScrollStrictSpan = null;
        }
    }

    private boolean shouldDisplayEdgeEffects() {
        return getOverScrollMode() != 2;
    }

    private void onTouchCancel() {
        int i = this.mTouchMode;
        if (i == 5) {
            if (this.mFlingRunnable == null) {
                this.mFlingRunnable = new FlingRunnable();
            }
            this.mFlingRunnable.startSpringback();
        } else if (i != 6) {
            this.mTouchMode = -1;
            setPressed(false);
            View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
            if (motionView != null) {
                motionView.setPressed(false);
            }
            clearScrollingCache();
            removeCallbacks(this.mPendingCheckForLongPress);
            recycleVelocityTracker();
        }
        if (shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
        this.mActivePointerId = -1;
    }

    /* Access modifiers changed, original: protected */
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScrollY != scrollY) {
            onScrollChanged(this.mScrollX, scrollY, this.mScrollX, this.mScrollY);
            this.mScrollY = scrollY;
            invalidateParentIfNeeded();
            awakenScrollBars();
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        int action = event.getAction();
        int delta;
        if (action == 8) {
            float axisValue;
            if (event.isFromSource(2)) {
                axisValue = event.getAxisValue(1.3E-44f);
            } else if (event.isFromSource(4194304)) {
                axisValue = event.getAxisValue(26);
            } else {
                axisValue = 0.0f;
            }
            delta = Math.round(this.mVerticalScrollFactor * axisValue);
            if (!(delta == 0 || trackMotionScroll(delta, delta))) {
                return true;
            }
        } else if (action == 11 && event.isFromSource(2)) {
            action = event.getActionButton();
            if (action == 32 || action == 2) {
                delta = this.mTouchMode;
                if ((delta == 0 || delta == 1) && performStylusButtonPressAction(event)) {
                    removeCallbacks(this.mPendingCheckForLongPress);
                    removeCallbacks(this.mPendingCheckForTap);
                }
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public void fling(int velocityY) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        reportScrollStateChange(2);
        this.mFlingRunnable.start(velocityY);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(2);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int i = dyUnconsumed;
        View motionView = getChildAt(getChildCount() / 2);
        int oldTop = motionView != null ? motionView.getTop() : 0;
        if (motionView == null || trackMotionScroll(-i, -i)) {
            int myUnconsumed;
            int myConsumed;
            int myUnconsumed2 = dyUnconsumed;
            if (motionView != null) {
                int myConsumed2 = motionView.getTop() - oldTop;
                myUnconsumed = myUnconsumed2 - myConsumed2;
                myConsumed = myConsumed2;
            } else {
                myUnconsumed = myUnconsumed2;
                myConsumed = 0;
            }
            dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        int childCount = getChildCount();
        if (consumed || childCount <= 0 || !canScrollList((int) velocityY) || Math.abs(velocityY) <= ((float) this.mMinimumVelocity)) {
            return dispatchNestedFling(velocityX, velocityY, consumed);
        }
        reportScrollStateChange(2);
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        if (!dispatchNestedPreFling(0.0f, velocityY)) {
            this.mFlingRunnable.start((int) velocityY);
        }
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shouldDisplayEdgeEffects()) {
            int width;
            int height;
            int translateX;
            int translateY;
            int restoreCount;
            int scrollY = this.mScrollY;
            boolean clipToPadding = getClipToPadding();
            if (clipToPadding) {
                width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                translateX = this.mPaddingLeft;
                translateY = this.mPaddingTop;
            } else {
                width = getWidth();
                height = getHeight();
                translateX = 0;
                translateY = 0;
            }
            this.mEdgeGlowTop.setSize(width, height);
            this.mEdgeGlowBottom.setSize(width, height);
            int i = 0;
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                canvas.clipRect(translateX, translateY, translateX + width, this.mEdgeGlowTop.getMaxHeight() + translateY);
                canvas.translate((float) translateX, (float) (Math.min(0, this.mFirstPositionDistanceGuess + scrollY) + translateY));
                if (this.mEdgeGlowTop.draw(canvas)) {
                    invalidateTopGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                canvas.clipRect(translateX, (translateY + height) - this.mEdgeGlowBottom.getMaxHeight(), translateX + width, translateY + height);
                int edgeX = (-width) + translateX;
                int edgeY = Math.max(getHeight(), this.mLastPositionDistanceGuess + scrollY);
                if (clipToPadding) {
                    i = this.mPaddingBottom;
                }
                canvas.translate((float) edgeX, (float) (edgeY - i));
                canvas.rotate(180.0f, (float) width, 0.0f);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    invalidateBottomGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller == null || !fastScroller.onInterceptHoverEvent(event)) {
            return super.onInterceptHoverEvent(event);
        }
        return true;
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        PointerIcon pointerIcon = this.mFastScroll;
        if (pointerIcon != null) {
            pointerIcon = pointerIcon.onResolvePointerIcon(event, pointerIndex);
            if (pointerIcon != null) {
                return pointerIcon;
            }
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        AbsPositionScroller absPositionScroller = this.mPositionScroller;
        if (absPositionScroller != null) {
            absPositionScroller.stop();
        }
        if (this.mIsDetaching || !isAttachedToWindow()) {
            return false;
        }
        FastScroller fastScroller = this.mFastScroll;
        if (fastScroller != null && fastScroller.onInterceptTouchEvent(ev)) {
            return true;
        }
        int pointerIndex;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    this.mNumTouchMoveEvent++;
                    if (this.mNumTouchMoveEvent == 1) {
                        this.mIsFirstTouchMoveEvent = true;
                    } else {
                        this.mIsFirstTouchMoveEvent = false;
                    }
                    if (this.mTouchMode == 0) {
                        pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex == -1) {
                            pointerIndex = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                        }
                        int y = (int) ev.getY(pointerIndex);
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (startScrollIfNeeded((int) ev.getX(pointerIndex), y, null)) {
                            return true;
                        }
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        this.mNumTouchMoveEvent = 0;
                        onSecondaryPointerUp(ev);
                    }
                }
            }
            this.mNumTouchMoveEvent = 0;
            this.mTouchMode = -1;
            this.mActivePointerId = -1;
            recycleVelocityTracker();
            reportScrollStateChange(0);
            stopNestedScroll();
        } else {
            this.mNumTouchMoveEvent = 0;
            int touchMode = this.mTouchMode;
            if (touchMode == 6 || touchMode == 5) {
                this.mMotionCorrection = 0;
                return true;
            }
            pointerIndex = (int) ev.getX();
            int y2 = (int) ev.getY();
            this.mActivePointerId = ev.getPointerId(0);
            int motionPosition = findMotionRow(y2);
            if (touchMode != 4 && motionPosition >= 0) {
                this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                this.mMotionX = pointerIndex;
                this.mMotionY = y2;
                this.mMotionPosition = motionPosition;
                this.mTouchMode = 0;
                clearScrollingCache();
            }
            this.mLastY = Integer.MIN_VALUE;
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement(ev);
            this.mNestedYOffset = 0;
            startNestedScroll(2);
            if (touchMode == 4) {
                return true;
            }
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mMotionCorrection = 0;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        int count = getChildCount();
        int firstPosition = this.mFirstPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (adapter.isEnabled(firstPosition + i)) {
                    views.add(child);
                }
                child.addTouchables(views);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769710)
    public void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState) {
            OnScrollListener onScrollListener = this.mOnScrollListener;
            if (onScrollListener != null) {
                this.mLastScrollState = newState;
                onScrollListener.onScrollStateChanged(this, newState);
            }
        }
    }

    public void setFriction(float friction) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        this.mFlingRunnable.mScroller.setFriction(friction);
    }

    public void setVelocityScale(float scale) {
        this.mVelocityScale = scale;
    }

    /* Access modifiers changed, original: 0000 */
    public AbsPositionScroller createPositionScroller() {
        return new PositionScroller();
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset, duration);
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        smoothScrollBy(distance, duration, false, false);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void smoothScrollBy(int distance, int duration, boolean linear, boolean suppressEndFlingStateChangeCall) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        int firstPos = this.mFirstPosition;
        int childCount = getChildCount();
        int lastPos = firstPos + childCount;
        int topLimit = getPaddingTop();
        int bottomLimit = getHeight() - getPaddingBottom();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getTop() == topLimit && distance < 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getBottom() == bottomLimit && distance > 0))) {
            this.mFlingRunnable.endFling();
            AbsPositionScroller absPositionScroller = this.mPositionScroller;
            if (absPositionScroller != null) {
                absPositionScroller.stop();
                return;
            }
            return;
        }
        reportScrollStateChange(2);
        this.mFlingRunnable.startScroll(distance, duration, linear, suppressEndFlingStateChangeCall);
    }

    /* Access modifiers changed, original: 0000 */
    public void smoothScrollByOffset(int position) {
        int index = -1;
        if (position < 0) {
            index = getFirstVisiblePosition();
        } else if (position > 0) {
            index = getLastVisiblePosition();
        }
        if (index > -1) {
            View child = getChildAt(index - getFirstVisiblePosition());
            if (child != null) {
                Rect visibleRect = new Rect();
                if (child.getGlobalVisibleRect(visibleRect)) {
                    float visibleArea = ((float) (visibleRect.width() * visibleRect.height())) / ((float) (child.getWidth() * child.getHeight()));
                    if (position < 0 && visibleArea < 0.75f) {
                        index++;
                    } else if (position > 0 && visibleArea < 0.75f) {
                        index--;
                    }
                }
                smoothScrollToPosition(Math.max(0, Math.min(getCount(), index + position)));
            }
        }
    }

    private void createScrollingCache() {
        if (this.mScrollingCacheEnabled && !this.mCachingStarted && !isHardwareAccelerated()) {
            setChildrenDrawnWithCacheEnabled(true);
            setChildrenDrawingCacheEnabled(true);
            this.mCachingActive = true;
            this.mCachingStarted = true;
        }
    }

    private void clearScrollingCache() {
        if (!isHardwareAccelerated()) {
            if (this.mClearScrollingCache == null) {
                this.mClearScrollingCache = new Runnable() {
                    public void run() {
                        if (AbsListView.this.mCachingStarted) {
                            AbsListView absListView = AbsListView.this;
                            absListView.mCachingActive = false;
                            absListView.mCachingStarted = false;
                            absListView.setChildrenDrawnWithCacheEnabled(false);
                            if ((AbsListView.this.mPersistentDrawingCache & 2) == 0) {
                                AbsListView.this.setChildrenDrawingCacheEnabled(false);
                            }
                            if (!AbsListView.this.isAlwaysDrawnWithCacheEnabled()) {
                                AbsListView.this.invalidate();
                            }
                        }
                    }
                };
            }
            post(this.mClearScrollingCache);
        }
    }

    public void scrollListBy(int y) {
        trackMotionScroll(-y, -y);
    }

    public boolean canScrollList(int direction) {
        int childCount = getChildCount();
        boolean z = false;
        if (childCount == 0) {
            return false;
        }
        int firstPosition = this.mFirstPosition;
        Rect listPadding = this.mListPadding;
        int lastBottom;
        if (direction > 0) {
            lastBottom = getChildAt(childCount - 1).getBottom();
            if (firstPosition + childCount < this.mItemCount || lastBottom > getHeight() - listPadding.bottom) {
                z = true;
            }
            return z;
        }
        lastBottom = getChildAt(0).getTop();
        if (firstPosition > 0 || lastBottom < listPadding.top) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01f2  */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124051739)
    public boolean trackMotionScroll(int r27, int r28) {
        /*
        r26 = this;
        r0 = r26;
        r1 = r27;
        r2 = r28;
        r3 = r26.getChildCount();
        r4 = 1;
        if (r3 != 0) goto L_0x000e;
    L_0x000d:
        return r4;
    L_0x000e:
        r5 = 0;
        r6 = r0.getChildAt(r5);
        r6 = r6.getTop();
        r7 = r3 + -1;
        r7 = r0.getChildAt(r7);
        r7 = r7.getBottom();
        r8 = r0.mListPadding;
        r9 = 0;
        r10 = 0;
        r11 = r0.mGroupFlags;
        r12 = 34;
        r11 = r11 & r12;
        if (r11 != r12) goto L_0x0030;
    L_0x002c:
        r9 = r8.top;
        r10 = r8.bottom;
    L_0x0030:
        r11 = r9 - r6;
        r13 = r26.getHeight();
        r13 = r13 - r10;
        r14 = r7 - r13;
        r15 = r26.getHeight();
        r5 = r0.mPaddingBottom;
        r15 = r15 - r5;
        r5 = r0.mPaddingTop;
        r15 = r15 - r5;
        if (r1 >= 0) goto L_0x004d;
    L_0x0045:
        r5 = r15 + -1;
        r5 = -r5;
        r1 = java.lang.Math.max(r5, r1);
        goto L_0x0053;
    L_0x004d:
        r5 = r15 + -1;
        r1 = java.lang.Math.min(r5, r1);
    L_0x0053:
        if (r2 >= 0) goto L_0x005d;
    L_0x0055:
        r5 = r15 + -1;
        r5 = -r5;
        r2 = java.lang.Math.max(r5, r2);
        goto L_0x0063;
    L_0x005d:
        r5 = r15 + -1;
        r2 = java.lang.Math.min(r5, r2);
    L_0x0063:
        r5 = r0.mFirstPosition;
        if (r5 != 0) goto L_0x006e;
    L_0x0067:
        r4 = r8.top;
        r4 = r6 - r4;
        r0.mFirstPositionDistanceGuess = r4;
        goto L_0x0073;
    L_0x006e:
        r4 = r0.mFirstPositionDistanceGuess;
        r4 = r4 + r2;
        r0.mFirstPositionDistanceGuess = r4;
    L_0x0073:
        r4 = r5 + r3;
        r12 = r0.mItemCount;
        if (r4 != r12) goto L_0x007f;
    L_0x0079:
        r4 = r8.bottom;
        r4 = r4 + r7;
        r0.mLastPositionDistanceGuess = r4;
        goto L_0x0084;
    L_0x007f:
        r4 = r0.mLastPositionDistanceGuess;
        r4 = r4 + r2;
        r0.mLastPositionDistanceGuess = r4;
    L_0x0084:
        if (r5 != 0) goto L_0x008e;
    L_0x0086:
        r4 = r8.top;
        if (r6 < r4) goto L_0x008e;
    L_0x008a:
        if (r2 < 0) goto L_0x008e;
    L_0x008c:
        r4 = 1;
        goto L_0x008f;
    L_0x008e:
        r4 = 0;
    L_0x008f:
        r12 = r5 + r3;
        r18 = r6;
        r6 = r0.mItemCount;
        if (r12 != r6) goto L_0x00a4;
    L_0x0097:
        r6 = r26.getHeight();
        r12 = r8.bottom;
        r6 = r6 - r12;
        if (r7 > r6) goto L_0x00a4;
    L_0x00a0:
        if (r2 > 0) goto L_0x00a4;
    L_0x00a2:
        r6 = 1;
        goto L_0x00a5;
    L_0x00a4:
        r6 = 0;
    L_0x00a5:
        if (r4 != 0) goto L_0x01fe;
    L_0x00a7:
        if (r6 == 0) goto L_0x00bd;
    L_0x00a9:
        r16 = r1;
        r17 = r3;
        r27 = r4;
        r28 = r6;
        r22 = r7;
        r23 = r9;
        r24 = r10;
        r25 = r13;
        r1 = 0;
        r9 = 1;
        goto L_0x0210;
    L_0x00bd:
        if (r2 >= 0) goto L_0x00c1;
    L_0x00bf:
        r12 = 1;
        goto L_0x00c2;
    L_0x00c1:
        r12 = 0;
    L_0x00c2:
        r19 = r26.isInTouchMode();
        if (r19 == 0) goto L_0x00cb;
    L_0x00c8:
        r26.hideSelector();
    L_0x00cb:
        r27 = r4;
        r4 = r26.getHeaderViewsCount();
        r28 = r6;
        r6 = r0.mItemCount;
        r20 = r26.getFooterViewsCount();
        r6 = r6 - r20;
        r20 = 0;
        r21 = 0;
        if (r12 == 0) goto L_0x0129;
    L_0x00e1:
        r22 = r7;
        r7 = -r2;
        r23 = r9;
        r9 = r0.mGroupFlags;
        r24 = r10;
        r10 = 34;
        r9 = r9 & r10;
        if (r9 != r10) goto L_0x00f2;
    L_0x00ef:
        r9 = r8.top;
        r7 = r7 + r9;
    L_0x00f2:
        r9 = 0;
    L_0x00f3:
        if (r9 >= r3) goto L_0x011e;
    L_0x00f5:
        r10 = r0.getChildAt(r9);
        r25 = r13;
        r13 = r10.getBottom();
        if (r13 < r7) goto L_0x0102;
    L_0x0101:
        goto L_0x0122;
    L_0x0102:
        r21 = r21 + 1;
        r13 = r5 + r9;
        if (r13 < r4) goto L_0x0115;
    L_0x0108:
        if (r13 >= r6) goto L_0x0115;
    L_0x010a:
        r10.clearAccessibilityFocus();
        r17 = r7;
        r7 = r0.mRecycler;
        r7.addScrapView(r10, r13);
        goto L_0x0117;
    L_0x0115:
        r17 = r7;
    L_0x0117:
        r9 = r9 + 1;
        r7 = r17;
        r13 = r25;
        goto L_0x00f3;
    L_0x011e:
        r17 = r7;
        r25 = r13;
    L_0x0122:
        r17 = r3;
        r7 = r20;
        r3 = r21;
        goto L_0x0173;
    L_0x0129:
        r22 = r7;
        r23 = r9;
        r24 = r10;
        r25 = r13;
        r7 = r26.getHeight();
        r7 = r7 - r2;
        r9 = r0.mGroupFlags;
        r10 = 34;
        r9 = r9 & r10;
        if (r9 != r10) goto L_0x0140;
    L_0x013d:
        r9 = r8.bottom;
        r7 = r7 - r9;
    L_0x0140:
        r9 = r3 + -1;
    L_0x0142:
        if (r9 < 0) goto L_0x016d;
    L_0x0144:
        r10 = r0.getChildAt(r9);
        r13 = r10.getTop();
        if (r13 > r7) goto L_0x0151;
    L_0x014e:
        r17 = r3;
        goto L_0x016f;
    L_0x0151:
        r20 = r9;
        r21 = r21 + 1;
        r13 = r5 + r9;
        if (r13 < r4) goto L_0x0166;
    L_0x0159:
        if (r13 >= r6) goto L_0x0166;
    L_0x015b:
        r10.clearAccessibilityFocus();
        r17 = r3;
        r3 = r0.mRecycler;
        r3.addScrapView(r10, r13);
        goto L_0x0168;
    L_0x0166:
        r17 = r3;
    L_0x0168:
        r9 = r9 + -1;
        r3 = r17;
        goto L_0x0142;
    L_0x016d:
        r17 = r3;
    L_0x016f:
        r7 = r20;
        r3 = r21;
    L_0x0173:
        r9 = r0.mMotionViewOriginalTop;
        r9 = r9 + r1;
        r0.mMotionViewNewTop = r9;
        r9 = 1;
        r0.mBlockLayoutRequests = r9;
        if (r3 <= 0) goto L_0x0185;
    L_0x017d:
        r0.detachViewsFromParent(r7, r3);
        r9 = r0.mRecycler;
        r9.removeSkippedScrap();
    L_0x0185:
        r9 = r26.awakenScrollBars();
        if (r9 != 0) goto L_0x018e;
    L_0x018b:
        r26.invalidate();
    L_0x018e:
        r0.offsetChildrenTopAndBottom(r2);
        if (r12 == 0) goto L_0x0198;
    L_0x0193:
        r9 = r0.mFirstPosition;
        r9 = r9 + r3;
        r0.mFirstPosition = r9;
    L_0x0198:
        r9 = java.lang.Math.abs(r2);
        if (r11 < r9) goto L_0x01a0;
    L_0x019e:
        if (r14 >= r9) goto L_0x01a3;
    L_0x01a0:
        r0.fillGap(r12);
    L_0x01a3:
        r10 = r0.mRecycler;
        r10.fullyDetachScrapViews();
        r10 = 0;
        r13 = -1;
        if (r19 != 0) goto L_0x01d3;
    L_0x01ac:
        r16 = r1;
        r1 = r0.mSelectedPosition;
        if (r1 == r13) goto L_0x01d0;
    L_0x01b2:
        r1 = r0.mSelectedPosition;
        r13 = r0.mFirstPosition;
        r1 = r1 - r13;
        if (r1 < 0) goto L_0x01cd;
    L_0x01b9:
        r13 = r26.getChildCount();
        if (r1 >= r13) goto L_0x01cd;
    L_0x01bf:
        r13 = r0.mSelectedPosition;
        r20 = r3;
        r3 = r0.getChildAt(r1);
        r0.positionSelector(r13, r3);
        r3 = 1;
        r10 = r3;
        goto L_0x01cf;
    L_0x01cd:
        r20 = r3;
    L_0x01cf:
        goto L_0x01f0;
    L_0x01d0:
        r20 = r3;
        goto L_0x01d7;
    L_0x01d3:
        r16 = r1;
        r20 = r3;
    L_0x01d7:
        r1 = r0.mSelectorPosition;
        if (r1 == r13) goto L_0x01cf;
    L_0x01db:
        r3 = r0.mFirstPosition;
        r1 = r1 - r3;
        if (r1 < 0) goto L_0x01f0;
    L_0x01e0:
        r3 = r26.getChildCount();
        if (r1 >= r3) goto L_0x01f0;
    L_0x01e6:
        r3 = r0.mSelectorPosition;
        r13 = r0.getChildAt(r1);
        r0.positionSelector(r3, r13);
        r10 = 1;
    L_0x01f0:
        if (r10 != 0) goto L_0x01f7;
    L_0x01f2:
        r1 = r0.mSelectorRect;
        r1.setEmpty();
    L_0x01f7:
        r1 = 0;
        r0.mBlockLayoutRequests = r1;
        r26.invokeOnItemScrollListener();
        return r1;
    L_0x01fe:
        r16 = r1;
        r17 = r3;
        r27 = r4;
        r28 = r6;
        r22 = r7;
        r23 = r9;
        r24 = r10;
        r25 = r13;
        r1 = 0;
        r9 = 1;
    L_0x0210:
        if (r2 == 0) goto L_0x0213;
    L_0x0212:
        r1 = r9;
    L_0x0213:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.AbsListView.trackMotionScroll(int, int):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    public int getHeaderViewsCount() {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int getFooterViewsCount() {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void hideSelector() {
        if (this.mSelectedPosition != -1) {
            if (this.mLayoutMode != 4) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
            if (this.mNextSelectedPosition >= 0 && this.mNextSelectedPosition != this.mSelectedPosition) {
                this.mResurrectToPosition = this.mNextSelectedPosition;
            }
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectedTop = 0;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
    }

    /* Access modifiers changed, original: 0000 */
    public int findClosestMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(y);
        return motionRow != -1 ? motionRow : (this.mFirstPosition + childCount) - 1;
    }

    public void invalidateViews() {
        this.mDataChanged = true;
        rememberSyncState();
        requestLayout();
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean resurrectSelectionIfNeeded() {
        if (this.mSelectedPosition >= 0 || !resurrectSelection()) {
            return false;
        }
        updateSelectorState();
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean resurrectSelection() {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return false;
        }
        int selectedPos;
        int selectedTop = 0;
        int childrenTop = this.mListPadding.top;
        int childrenBottom = (this.mBottom - this.mTop) - this.mListPadding.bottom;
        int firstPosition = this.mFirstPosition;
        int toPosition = this.mResurrectToPosition;
        boolean down = true;
        int selectedBottom;
        int i;
        if (toPosition >= firstPosition && toPosition < firstPosition + childCount) {
            selectedPos = toPosition;
            View selected = getChildAt(selectedPos - this.mFirstPosition);
            selectedTop = selected.getTop();
            selectedBottom = selected.getBottom();
            if (selectedTop < childrenTop) {
                selectedTop = childrenTop + getVerticalFadingEdgeLength();
            } else if (selectedBottom > childrenBottom) {
                selectedTop = (childrenBottom - selected.getMeasuredHeight()) - getVerticalFadingEdgeLength();
            }
        } else if (toPosition < firstPosition) {
            selectedPos = firstPosition;
            for (i = 0; i < childCount; i++) {
                int top = getChildAt(i).getTop();
                if (i == 0) {
                    selectedTop = top;
                    if (firstPosition > 0 || top < childrenTop) {
                        childrenTop += getVerticalFadingEdgeLength();
                    }
                }
                if (top >= childrenTop) {
                    selectedPos = firstPosition + i;
                    selectedTop = top;
                    break;
                }
            }
        } else {
            selectedPos = this.mItemCount;
            down = false;
            i = (firstPosition + childCount) - 1;
            for (selectedBottom = childCount - 1; selectedBottom >= 0; selectedBottom--) {
                View v = getChildAt(selectedBottom);
                int top2 = v.getTop();
                int bottom = v.getBottom();
                if (selectedBottom == childCount - 1) {
                    selectedTop = top2;
                    if (firstPosition + childCount < selectedPos || bottom > childrenBottom) {
                        childrenBottom -= getVerticalFadingEdgeLength();
                    }
                }
                if (bottom <= childrenBottom) {
                    selectedTop = top2;
                    selectedPos = firstPosition + selectedBottom;
                    break;
                }
            }
            selectedPos = i;
        }
        this.mResurrectToPosition = -1;
        removeCallbacks(this.mFlingRunnable);
        AbsPositionScroller absPositionScroller = this.mPositionScroller;
        if (absPositionScroller != null) {
            absPositionScroller.stop();
        }
        this.mTouchMode = -1;
        clearScrollingCache();
        this.mSpecificTop = selectedTop;
        int selectedPos2 = lookForSelectablePosition(selectedPos, down);
        if (selectedPos2 < firstPosition || selectedPos2 > getLastVisiblePosition()) {
            selectedPos2 = -1;
        } else {
            this.mLayoutMode = 4;
            updateSelectorState();
            setSelectionInt(selectedPos2);
            invokeOnItemScrollListener();
        }
        reportScrollStateChange(0);
        return selectedPos2 >= 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void confirmCheckedPositionsById() {
        this.mCheckStates.clear();
        boolean checkedCountChanged = false;
        int checkedIndex = 0;
        while (checkedIndex < this.mCheckedIdStates.size()) {
            int searchPos;
            long id = this.mCheckedIdStates.keyAt(checkedIndex);
            int lastPos = ((Integer) this.mCheckedIdStates.valueAt(checkedIndex)).intValue();
            boolean z = true;
            if (id != this.mAdapter.getItemId(lastPos)) {
                int start = Math.max(0, lastPos - 20);
                int end = Math.min(lastPos + 20, this.mItemCount);
                searchPos = start;
                while (searchPos < end) {
                    if (id == this.mAdapter.getItemId(searchPos)) {
                        this.mCheckStates.put(searchPos, z);
                        this.mCheckedIdStates.setValueAt(checkedIndex, Integer.valueOf(searchPos));
                        z = true;
                        break;
                    }
                    searchPos++;
                    z = true;
                }
                z = false;
                if (z) {
                } else {
                    this.mCheckedIdStates.delete(id);
                    checkedIndex--;
                    this.mCheckedItemCount--;
                    checkedCountChanged = true;
                    ActionMode actionMode = this.mChoiceActionMode;
                    if (actionMode != null) {
                        MultiChoiceModeWrapper multiChoiceModeWrapper = this.mMultiChoiceModeCallback;
                        if (multiChoiceModeWrapper != null) {
                            multiChoiceModeWrapper.onItemCheckedStateChanged(actionMode, lastPos, id, false);
                        }
                    }
                }
                searchPos = 1;
            } else {
                searchPos = 1;
                this.mCheckStates.put(lastPos, true);
            }
            checkedIndex += searchPos;
        }
        if (checkedCountChanged) {
            ActionMode actionMode2 = this.mChoiceActionMode;
            if (actionMode2 != null) {
                actionMode2.invalidate();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void handleDataChanged() {
        int count = this.mItemCount;
        int lastHandledItemCount = this.mLastHandledItemCount;
        this.mLastHandledItemCount = this.mItemCount;
        if (this.mChoiceMode != 0) {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null && listAdapter.hasStableIds()) {
                confirmCheckedPositionsById();
            }
        }
        this.mRecycler.clearTransientStateViews();
        int i = 3;
        if (count > 0) {
            int i2;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                this.mPendingSync = null;
                i2 = this.mTranscriptMode;
                if (i2 == 2) {
                    this.mLayoutMode = 3;
                    return;
                }
                if (i2 == 1) {
                    if (this.mForceTranscriptScroll) {
                        this.mForceTranscriptScroll = false;
                        this.mLayoutMode = 3;
                        return;
                    }
                    i2 = getChildCount();
                    int listBottom = getHeight() - getPaddingBottom();
                    View lastChild = getChildAt(i2 - 1);
                    int lastBottom = lastChild != null ? lastChild.getBottom() : listBottom;
                    if (this.mFirstPosition + i2 < lastHandledItemCount || lastBottom > listBottom) {
                        awakenScrollBars();
                    } else {
                        this.mLayoutMode = 3;
                        return;
                    }
                }
                i2 = this.mSyncMode;
                if (i2 != 0) {
                    if (i2 == 1) {
                        this.mLayoutMode = 5;
                        this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                        return;
                    }
                } else if (isInTouchMode()) {
                    this.mLayoutMode = 5;
                    this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                    return;
                } else {
                    i2 = findSyncPosition();
                    if (i2 >= 0 && lookForSelectablePosition(i2, true) == i2) {
                        this.mSyncPosition = i2;
                        if (this.mSyncHeight == ((long) getHeight())) {
                            this.mLayoutMode = 5;
                        } else {
                            this.mLayoutMode = 2;
                        }
                        setNextSelectedPositionInt(i2);
                        return;
                    }
                }
            }
            if (!isInTouchMode()) {
                i2 = getSelectedItemPosition();
                if (i2 >= count) {
                    i2 = count - 1;
                }
                if (i2 < 0) {
                    i2 = 0;
                }
                int selectablePos = lookForSelectablePosition(i2, true);
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    return;
                }
                selectablePos = lookForSelectablePosition(i2, false);
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    return;
                }
            } else if (this.mResurrectToPosition >= 0) {
                return;
            }
        }
        if (!this.mStackFromBottom) {
            i = 1;
        }
        this.mLayoutMode = i;
        this.mSelectedPosition = -1;
        this.mSelectedRowId = Long.MIN_VALUE;
        this.mNextSelectedPosition = -1;
        this.mNextSelectedRowId = Long.MIN_VALUE;
        this.mNeedSync = false;
        this.mPendingSync = null;
        this.mSelectorPosition = -1;
        checkSelectionChanged();
    }

    /* Access modifiers changed, original: protected */
    public void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
        PopupWindow popupWindow;
        if (hint != 0) {
            if (hint == 4) {
                popupWindow = this.mPopup;
                if (popupWindow != null && popupWindow.isShowing()) {
                    dismissPopup();
                }
            }
        } else if (this.mFiltered) {
            popupWindow = this.mPopup;
            if (!(popupWindow == null || popupWindow.isShowing())) {
                showPopup();
            }
        }
        this.mPopupHidden = hint == 4;
    }

    private void dismissPopup() {
        PopupWindow popupWindow = this.mPopup;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private void showPopup() {
        if (getWindowVisibility() == 0) {
            createTextFilter(true);
            positionPopup();
            checkFocus();
        }
    }

    private void positionPopup() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] xy = new int[2];
        getLocationOnScreen(xy);
        int bottomGap = ((screenHeight - xy[1]) - getHeight()) + ((int) (this.mDensityScale * 20.0f));
        if (this.mPopup.isShowing()) {
            this.mPopup.update(xy[0], bottomGap, -1, -1);
        } else {
            this.mPopup.showAtLocation((View) this, 81, xy[0], bottomGap);
        }
    }

    static int getDistance(Rect source, Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        if (direction == 1 || direction == 2) {
            sX = source.right + (source.width() / 2);
            sY = source.top + (source.height() / 2);
            dX = dest.left + (dest.width() / 2);
            dY = dest.top + (dest.height() / 2);
        } else if (direction == 17) {
            sX = source.left;
            sY = source.top + (source.height() / 2);
            dX = dest.right;
            dY = dest.top + (dest.height() / 2);
        } else if (direction == 33) {
            sX = source.left + (source.width() / 2);
            sY = source.top;
            dX = dest.left + (dest.width() / 2);
            dY = dest.bottom;
        } else if (direction == 66) {
            sX = source.right;
            sY = source.top + (source.height() / 2);
            dX = dest.left;
            dY = dest.top + (dest.height() / 2);
        } else if (direction == 130) {
            sX = source.left + (source.width() / 2);
            sY = source.bottom;
            dX = dest.left + (dest.width() / 2);
            dY = dest.top;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return (deltaY * deltaY) + (deltaX * deltaX);
    }

    /* Access modifiers changed, original: protected */
    public boolean isInFilterMode() {
        return this.mFiltered;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean sendToTextFilter(int keyCode, int count, KeyEvent event) {
        if (!acceptFilter()) {
            return false;
        }
        boolean handled = false;
        boolean okToSend = true;
        if (keyCode == 4) {
            if (this.mFiltered) {
                PopupWindow popupWindow = this.mPopup;
                if (popupWindow != null && popupWindow.isShowing()) {
                    if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                        DispatcherState state = getKeyDispatcherState();
                        if (state != null) {
                            state.startTracking(event, this);
                        }
                        handled = true;
                    } else if (event.getAction() == 1 && event.isTracking() && !event.isCanceled()) {
                        handled = true;
                        this.mTextFilter.setText((CharSequence) "");
                    }
                }
            }
            okToSend = false;
        } else if (keyCode != 62) {
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        break;
                }
            }
            okToSend = false;
        } else {
            okToSend = this.mFiltered;
        }
        if (okToSend) {
            createTextFilter(true);
            KeyEvent forwardEvent = event;
            if (forwardEvent.getRepeatCount() > 0) {
                forwardEvent = KeyEvent.changeTimeRepeat(event, event.getEventTime(), 0);
            }
            int action = event.getAction();
            if (action == 0) {
                handled = this.mTextFilter.onKeyDown(keyCode, forwardEvent);
            } else if (action == 1) {
                handled = this.mTextFilter.onKeyUp(keyCode, forwardEvent);
            } else if (action == 2) {
                handled = this.mTextFilter.onKeyMultiple(keyCode, count, event);
            }
        }
        return handled;
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (!isTextFilterEnabled()) {
            return null;
        }
        if (this.mPublicInputConnection == null) {
            this.mDefInputConnection = new BaseInputConnection((View) this, false);
            this.mPublicInputConnection = new InputConnectionWrapper(outAttrs);
        }
        outAttrs.inputType = 177;
        outAttrs.imeOptions = 6;
        return this.mPublicInputConnection;
    }

    public boolean checkInputConnectionProxy(View view) {
        return view == this.mTextFilter;
    }

    private void createTextFilter(boolean animateEntrance) {
        if (this.mPopup == null) {
            PopupWindow p = new PopupWindow(getContext());
            p.setFocusable(false);
            p.setTouchable(false);
            p.setInputMethodMode(2);
            p.setContentView(getTextFilterInput());
            p.setWidth(-2);
            p.setHeight(-2);
            p.setBackgroundDrawable(null);
            this.mPopup = p;
            getViewTreeObserver().addOnGlobalLayoutListener(this);
            this.mGlobalLayoutListenerAddedFilter = true;
        }
        if (animateEntrance) {
            this.mPopup.setAnimationStyle(R.style.Animation_TypingFilter);
        } else {
            this.mPopup.setAnimationStyle(R.style.Animation_TypingFilterRestore);
        }
    }

    private EditText getTextFilterInput() {
        if (this.mTextFilter == null) {
            this.mTextFilter = (EditText) LayoutInflater.from(getContext()).inflate((int) R.layout.typing_filter, null);
            this.mTextFilter.setRawInputType(177);
            this.mTextFilter.setImeOptions(268435456);
            this.mTextFilter.addTextChangedListener(this);
        }
        return this.mTextFilter;
    }

    public void clearTextFilter() {
        if (this.mFiltered) {
            getTextFilterInput().setText((CharSequence) "");
            this.mFiltered = false;
            PopupWindow popupWindow = this.mPopup;
            if (popupWindow != null && popupWindow.isShowing()) {
                dismissPopup();
            }
        }
    }

    public boolean hasTextFilter() {
        return this.mFiltered;
    }

    public void onGlobalLayout() {
        PopupWindow popupWindow;
        if (!isShown()) {
            popupWindow = this.mPopup;
            if (popupWindow != null && popupWindow.isShowing()) {
                dismissPopup();
            }
        } else if (this.mFiltered) {
            popupWindow = this.mPopup;
            if (popupWindow != null && !popupWindow.isShowing() && !this.mPopupHidden) {
                showPopup();
            }
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isTextFilterEnabled()) {
            createTextFilter(true);
            int length = s.length();
            boolean showing = this.mPopup.isShowing();
            if (!showing && length > 0) {
                showPopup();
                this.mFiltered = true;
            } else if (showing && length == 0) {
                dismissPopup();
                this.mFiltered = false;
            }
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter instanceof Filterable) {
                Filter f = ((Filterable) listAdapter).getFilter();
                if (f != null) {
                    f.filter(s, this);
                    return;
                }
                throw new IllegalStateException("You cannot call onTextChanged with a non filterable adapter");
            }
        }
    }

    public void afterTextChanged(Editable s) {
    }

    public void onFilterComplete(int count) {
        if (this.mSelectedPosition < 0 && count > 0) {
            this.mResurrectToPosition = -1;
            resurrectSelection();
        }
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2, 0);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void setTranscriptMode(int mode) {
        this.mTranscriptMode = mode;
    }

    public int getTranscriptMode() {
        return this.mTranscriptMode;
    }

    public int getSolidColor() {
        return this.mCacheColorHint;
    }

    public void setCacheColorHint(int color) {
        if (color != this.mCacheColorHint) {
            this.mCacheColorHint = color;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setDrawingCacheBackgroundColor(color);
            }
            this.mRecycler.setCacheColorHint(color);
        }
    }

    @ExportedProperty(category = "drawing")
    public int getCacheColorHint() {
        return this.mCacheColorHint;
    }

    public void reclaimViews(List<View> views) {
        int childCount = getChildCount();
        RecyclerListener listener = this.mRecycler.mRecyclerListener;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp != null && this.mRecycler.shouldRecycleViewType(lp.viewType)) {
                views.add(child);
                child.setAccessibilityDelegate(null);
                if (listener != null) {
                    listener.onMovedToScrapHeap(child);
                }
            }
        }
        this.mRecycler.reclaimScrapViews(views);
        removeAllViewsInLayout();
    }

    private void finishGlows() {
        if (shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.finish();
            this.mEdgeGlowBottom.finish();
        }
    }

    public void setRemoteViewsAdapter(Intent intent) {
        setRemoteViewsAdapter(intent, false);
    }

    public Runnable setRemoteViewsAdapterAsync(Intent intent) {
        return new AsyncRemoteAdapterAction(this, intent);
    }

    public void setRemoteViewsAdapter(Intent intent, boolean isAsync) {
        if (this.mRemoteAdapter == null || !new FilterComparison(intent).equals(new FilterComparison(this.mRemoteAdapter.getRemoteViewsServiceIntent()))) {
            this.mDeferNotifyDataSetChanged = false;
            this.mRemoteAdapter = new RemoteViewsAdapter(getContext(), intent, this, isAsync);
            if (this.mRemoteAdapter.isDataReady()) {
                setAdapter(this.mRemoteAdapter);
            }
        }
    }

    public void setRemoteViewsOnClickHandler(OnClickHandler handler) {
        RemoteViewsAdapter remoteViewsAdapter = this.mRemoteAdapter;
        if (remoteViewsAdapter != null) {
            remoteViewsAdapter.setRemoteViewsOnClickHandler(handler);
        }
    }

    public void deferNotifyDataSetChanged() {
        this.mDeferNotifyDataSetChanged = true;
    }

    public boolean onRemoteAdapterConnected() {
        ListAdapter listAdapter = this.mRemoteAdapter;
        if (listAdapter != this.mAdapter) {
            setAdapter(listAdapter);
            if (this.mDeferNotifyDataSetChanged) {
                this.mRemoteAdapter.notifyDataSetChanged();
                this.mDeferNotifyDataSetChanged = false;
            }
            return false;
        } else if (listAdapter == null) {
            return false;
        } else {
            listAdapter.superNotifyDataSetChanged();
            return true;
        }
    }

    public void onRemoteAdapterDisconnected() {
    }

    /* Access modifiers changed, original: 0000 */
    public void setVisibleRangeHint(int start, int end) {
        RemoteViewsAdapter remoteViewsAdapter = this.mRemoteAdapter;
        if (remoteViewsAdapter != null) {
            remoteViewsAdapter.setVisibleRangeHint(start, end);
        }
    }

    public void setEdgeEffectColor(int color) {
        setTopEdgeEffectColor(color);
        setBottomEdgeEffectColor(color);
    }

    public void setBottomEdgeEffectColor(int color) {
        this.mEdgeGlowBottom.setColor(color);
        invalidateBottomGlow();
    }

    public void setTopEdgeEffectColor(int color) {
        this.mEdgeGlowTop.setColor(color);
        invalidateTopGlow();
    }

    public int getTopEdgeEffectColor() {
        return this.mEdgeGlowTop.getColor();
    }

    public int getBottomEdgeEffectColor() {
        return this.mEdgeGlowBottom.getColor();
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecycler.mRecyclerListener = listener;
    }

    /* Access modifiers changed, original: 0000 */
    public int getHeightForPosition(int position) {
        int firstVisiblePosition = getFirstVisiblePosition();
        int childCount = getChildCount();
        int index = position - firstVisiblePosition;
        if (index >= 0 && index < childCount) {
            return getChildAt(index).getHeight();
        }
        View view = obtainView(position, this.mIsScrap);
        view.measure(this.mWidthMeasureSpec, 0);
        int height = view.getMeasuredHeight();
        this.mRecycler.addScrapView(view, position);
        return height;
    }

    public void setSelectionFromTop(int position, int y) {
        if (this.mAdapter != null) {
            if (isInTouchMode()) {
                this.mResurrectToPosition = position;
            } else {
                position = lookForSelectablePosition(position, true);
                if (position >= 0) {
                    setNextSelectedPositionInt(position);
                }
            }
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mListPadding.top + y;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                AbsPositionScroller absPositionScroller = this.mPositionScroller;
                if (absPositionScroller != null) {
                    absPositionScroller.stop();
                }
                requestLayout();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("drawing:cacheColorHint", getCacheColorHint());
        encoder.addProperty("list:fastScrollEnabled", isFastScrollEnabled());
        encoder.addProperty("list:scrollingCacheEnabled", isScrollingCacheEnabled());
        encoder.addProperty("list:smoothScrollbarEnabled", isSmoothScrollbarEnabled());
        encoder.addProperty("list:stackFromBottom", isStackFromBottom());
        encoder.addProperty("list:textFilterEnabled", isTextFilterEnabled());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            encoder.addPropertyKey("selectedView");
            selectedView.encode(encoder);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return AbsListViewInjector.overScrollBy(this, deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean superOverScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
